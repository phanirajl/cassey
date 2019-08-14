package io.github.malczuuu.cassey.cql;

import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CqlSessionShutdownHook extends Thread {

  private static final Logger log = LoggerFactory.getLogger(CqlSessionShutdownHook.class);

  private final CqlSession session;

  CqlSessionShutdownHook(CqlSession session) {
    this.session = session;
  }

  @Override
  public void run() {
    if (!session.isClosed()) {
      log.info("Closing connection to Cassandra cluster...");
      session.close();
    } else {
      log.info("Connection to Cassandra cluster already closed");
    }
  }
}
