package io.github.malczuuu.cassey.cql;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import java.net.InetSocketAddress;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CqlSessionFactory {

  private static final Logger log = LoggerFactory.getLogger(CqlSessionFactory.class);

  private String contactPoints = "localhost:9042";
  private String localDatacenter = "datacenter1";

  public CqlSessionFactory withContactPoints(String contactPoints) {
    this.contactPoints = contactPoints;
    return this;
  }

  public CqlSessionFactory withLocalDatacenter(String localDatacenter) {
    this.localDatacenter = localDatacenter;
    return this;
  }

  public CqlSession createCqlSession() {
    CqlSession session = connect();

    registerShutdownHook(session);

    retrieveAndLogCassandraVersion(session);

    return session;
  }

  private CqlSession connect() {
    log.info("Connecting to Cassandra cluster...");
    CqlSessionBuilder builder = CqlSession.builder();
    Stream.of(contactPoints.split(","))
        .forEach(
            contactPoint -> {
              String[] afterSplit = contactPoint.split(":");
              builder.addContactPoint(
                  InetSocketAddress.createUnresolved(
                      afterSplit[0], Integer.parseInt(afterSplit[1])));
            });
    builder.withLocalDatacenter(localDatacenter);
    return builder.build();
  }

  private void registerShutdownHook(CqlSession session) {
    Runtime.getRuntime().addShutdownHook(new CqlSessionShutdownHook(session));
    log.info("Registered shutdown hook to gracefully shutdown Cassandra connection");
  }

  private void retrieveAndLogCassandraVersion(CqlSession session) {
    ResultSet rs = session.execute("select release_version from system.local");
    Row row = rs.one();
    if (row != null) {
      log.info("Connected to Cassandra cluster version {}", row.getString("release_version"));
    } else {
      log.info("Unable to distinguish Cassandra cluster version");
    }
  }
}
