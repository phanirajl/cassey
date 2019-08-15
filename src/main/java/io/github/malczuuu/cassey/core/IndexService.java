package io.github.malczuuu.cassey.core;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.github.malczuuu.cassey.http.NotFoundException;
import io.github.malczuuu.cassey.core.model.Index;
import io.github.malczuuu.cassey.core.model.Content;
import java.util.ArrayList;
import java.util.List;

public class IndexService {

  private final CqlSession session;

  public IndexService(CqlSession session) {
    this.session = session;
  }

  public Content<Index> findAllIndexes(String keyspaceName, String tableName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"indexes\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .whereColumn("table_name")
                .isEqualTo(QueryBuilder.literal(tableName))
                .asCql());
    List<Index> indexes = new ArrayList<>();
    rs.forEach(
        row ->
            indexes.add(
                new Index(
                    row.getString("index_name"),
                    row.getString("kind"),
                    row.getMap("options", String.class, String.class))));
    return new Content<>(indexes);
  }

  public Index findOneIndex(String keyspaceName, String tableName, String indexName) {
    ResultSet rs =
        session.execute(
            QueryBuilder.selectFrom("system_schema", "\"indexes\"")
                .all()
                .whereColumn("keyspace_name")
                .isEqualTo(QueryBuilder.literal(keyspaceName))
                .whereColumn("table_name")
                .isEqualTo(QueryBuilder.literal(tableName))
                .whereColumn("index_name")
                .isEqualTo(QueryBuilder.literal(indexName))
                .asCql());
    Row row = rs.one();
    if (row == null) {
      throw new NotFoundException(
          "index " + keyspaceName + ".\"" + tableName + "\".\"" + indexName + "\" not found");
    }
    return new Index(
        row.getString("index_name"),
        row.getString("kind"),
        row.getMap("options", String.class, String.class));
  }
}
