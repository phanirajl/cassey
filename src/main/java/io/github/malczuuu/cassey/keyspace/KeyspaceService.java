package io.github.malczuuu.cassey.keyspace;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import io.github.malczuuu.cassey.model.Content;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeyspaceService {

  private final CqlSession session;

  public KeyspaceService(CqlSession session) {
    this.session = session;
  }

  public Content<Keyspace> findAllKeyspaces() {
    ResultSet rs =
        session.execute(QueryBuilder.selectFrom("system_schema", "\"keyspaces\"").all().asCql());
    List<Keyspace> keyspaces = new ArrayList<>();
    rs.forEach(
        row ->
            keyspaces.add(
                new Keyspace(
                    row.getString("keyspace_name"),
                    row.getBoolean("durable_writes"),
                    row.getMap("replication", String.class, String.class))));
    return new Content<>(keyspaces);
  }

  public void createKeyspace(Keyspace keyspace) {
    String query =
        "CREATE KEYSPACE "
            + keyspace.getName()
            + " WITH REPLICATION = "
            + replicationAsJson(keyspace)
            + " AND DURABLE_WRITES = "
            + keyspace.isDurableWrites();
    session.execute(query);
  }

  public void dropKeyspace(String name) {
    String query = "DROP KEYSPACE " + name;
    session.execute(query);
  }

  private String replicationAsJson(Keyspace keyspace) {
    return keyspace.getReplication().entrySet().stream()
        .map(entry -> "'" + entry.getKey() + "':'" + entry.getValue() + "'")
        .collect(Collectors.joining(",", "{", "}"));
  }
}
