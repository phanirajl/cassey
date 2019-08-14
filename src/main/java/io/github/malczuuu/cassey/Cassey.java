package io.github.malczuuu.cassey;

import com.datastax.oss.driver.api.core.CqlSession;
import io.github.malczuuu.cassey.cql.CqlSessionFactory;
import io.github.malczuuu.cassey.keyspace.Keyspace;
import io.github.malczuuu.cassey.keyspace.KeyspaceService;
import io.github.malczuuu.cassey.model.Content;
import java.util.HashMap;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cassey {

  private static final Logger log = LoggerFactory.getLogger(Cassey.class);

  public static void main(String[] args) {
    CqlSession session =
        new CqlSessionFactory()
            .withContactPoints("localhost:9042")
            .withLocalDatacenter("datacenter1")
            .createCqlSession();
    KeyspaceService keyspaceService = new KeyspaceService(session);

    String keyspaceName = "a" + UUID.randomUUID().toString().replace("-", "");

    Keyspace keyspace =
        new Keyspace(
            keyspaceName,
            true,
            new HashMap<String, String>() {
              {
                put("class", "SimpleStrategy");
                put("replication_factor", "1");
              }
            });

    keyspaceService.createKeyspace(keyspace);
    log.info("Created keyspace {}", keyspaceName);

    Content<Keyspace> keyspaces = keyspaceService.findAllKeyspaces();
    log.info("Retrieved {} of keyspaces", keyspaces.size());

    keyspaceService.dropKeyspace(keyspaceName);
    log.info("Dropped keyspace {}", keyspaceName);
  }
}
