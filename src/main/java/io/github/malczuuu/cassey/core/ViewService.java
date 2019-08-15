package io.github.malczuuu.cassey.core;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.github.malczuuu.cassey.http.BadRequestException;
import io.github.malczuuu.cassey.http.NotFoundException;
import io.github.malczuuu.cassey.core.model.Column;
import io.github.malczuuu.cassey.core.model.View;
import io.github.malczuuu.cassey.core.model.Content;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViewService {

  private final CqlSession session;
  private final ColumnService columnService;

  public ViewService(CqlSession session) {
    this.session = session;
    this.columnService = new ColumnService(session);
  }

  public Content<View> findAllViews(String keyspaceName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"views\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .asCql());
    List<View> tables = new ArrayList<>();
    rs.forEach(
        row ->
            tables.add(
                new View(
                    row.getString("view_name"),
                    row.getString("base_table_name"),
                    row.getBoolean("include_all_columns"),
                    row.getString("where_clause"))));
    return new Content<>(tables);
  }

  public View findOneView(String keyspaceName, String viewName) {
    return fetchView(keyspaceName, viewName)
        .orElseThrow(
            () ->
                new NotFoundException("table " + keyspaceName + ".\"" + viewName + "\" not found"));
  }

  private Optional<View> fetchView(String keyspaceName, String viewName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"views\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .whereColumn("view_name")
                .isEqualTo(QueryBuilder.literal(viewName))
                .asCql());
    Row row = rs.one();
    if (row == null) {
      return Optional.empty();
    }
    return Optional.of(
        new View(
            row.getString("view_name"),
            row.getString("base_table_name"),
            row.getBoolean("include_all_columns"),
            row.getString("where_clause")));
  }

  public Content<Column> findAllColumns(String keyspaceName, String viewName) {
    requireViewExists(keyspaceName, viewName);
    return columnService.findAllColumns(keyspaceName, viewName);
  }

  private void requireViewExists(String keyspaceName, String viewName) {
    if (fetchView(keyspaceName, viewName).isEmpty()) {
      throw new BadRequestException(
          "table " + keyspaceName + ".\"" + viewName + "\" does not exist");
    }
  }

  public Column findOneColumn(String keyspaceName, String viewName, String columnName) {
    requireViewExists(keyspaceName, viewName);
    return columnService.findOneColumn(keyspaceName, viewName, columnName);
  }
}
