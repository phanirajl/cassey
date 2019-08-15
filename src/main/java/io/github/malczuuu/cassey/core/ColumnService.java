package io.github.malczuuu.cassey.core;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.github.malczuuu.cassey.http.NotFoundException;
import io.github.malczuuu.cassey.core.model.Column;
import io.github.malczuuu.cassey.core.model.Content;
import java.util.ArrayList;
import java.util.List;

class ColumnService {

  private final CqlSession session;

  ColumnService(CqlSession session) {
    this.session = session;
  }

  Content<Column> findAllColumns(String keyspaceName, String tableOrViewName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"columns\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .whereColumn("table_name")
                .isEqualTo(QueryBuilder.literal(tableOrViewName))
                .asCql());
    List<Column> columns = new ArrayList<>();
    rs.forEach(
        row ->
            columns.add(
                new Column(
                    row.getString("column_name"),
                    row.getString("clustering_order"),
                    row.getInt("position"),
                    row.getString("kind"),
                    row.getString("type"))));
    return new Content<>(columns);
  }

  Column findOneColumn(String keyspaceName, String tableOrViewName, String columnName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"columns\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .whereColumn("table_name")
                .isEqualTo(QueryBuilder.literal(tableOrViewName))
                .whereColumn("column_name")
                .isEqualTo(QueryBuilder.literal(columnName))
                .asCql());
    Row row = rs.one();
    if (row == null) {
      throw new NotFoundException(
          "column "
              + keyspaceName
              + ".\""
              + tableOrViewName
              + "\".\""
              + columnName
              + "\" not found");
    }
    return new Column(
        row.getString("column_name"),
        row.getString("clustering_order"),
        row.getInt("position"),
        row.getString("kind"),
        row.getString("type"));
  }
}
