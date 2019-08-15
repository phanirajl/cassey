package io.github.malczuuu.cassey.core;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.github.malczuuu.cassey.http.BadRequestException;
import io.github.malczuuu.cassey.http.NotFoundException;
import io.github.malczuuu.cassey.core.model.Column;
import io.github.malczuuu.cassey.core.model.Table;
import io.github.malczuuu.cassey.core.model.Content;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableService {

  private final CqlSession session;
  private final ColumnService columnService;

  public TableService(CqlSession session) {
    this.session = session;
    this.columnService = new ColumnService(session);
  }

  public Content<Table> findAllTables(String keyspaceName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"tables\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .asCql());
    List<Table> tables = new ArrayList<>();
    rs.forEach(row -> tables.add(new Table(row.getString("table_name"))));
    return new Content<>(tables);
  }

  public Table findOneTable(String keyspaceName, String tableName) {
    return fetchTable(keyspaceName, tableName)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "table " + keyspaceName + ".\"" + tableName + "\" not found"));
  }

  private Optional<Table> fetchTable(String keyspaceName, String tableName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"tables\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .whereColumn("table_name")
                .isEqualTo(QueryBuilder.literal(tableName))
                .asCql());
    Row row = rs.one();
    if (row == null) {
      return Optional.empty();
    }
    return Optional.of(new Table(row.getString("table_name")));
  }

  public Content<Column> findAllColumns(String keyspaceName, String tableName) {
    requireTableExists(keyspaceName, tableName);
    return columnService.findAllColumns(keyspaceName, tableName);
  }

  private void requireTableExists(String keyspaceName, String tableName) {
    if (fetchTable(keyspaceName, tableName).isEmpty()) {
      throw new BadRequestException(
          "table " + keyspaceName + ".\"" + tableName + "\" does not exist");
    }
  }

  public Column findOneColumn(String keyspaceName, String tableName, String columnName) {
    requireTableExists(keyspaceName, tableName);
    return columnService.findOneColumn(keyspaceName, tableName, columnName);
  }
}
