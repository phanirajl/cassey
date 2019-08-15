package io.github.malczuuu.cassey;

import com.datastax.oss.driver.api.core.CqlSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.github.malczuuu.cassey.core.IndexService;
import io.github.malczuuu.cassey.core.KeyspaceService;
import io.github.malczuuu.cassey.core.TableService;
import io.github.malczuuu.cassey.core.ViewService;
import io.github.malczuuu.cassey.cql.CqlSessionFactory;
import io.github.malczuuu.problem4j.jackson.ProblemModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cassey {

  private static final Logger log = LoggerFactory.getLogger(Cassey.class);

  private static final CqlSession session =
      new CqlSessionFactory()
          .withContactPoints("localhost:9042")
          .withLocalDatacenter("datacenter1")
          .createCqlSession();

  private static final KeyspaceService keyspaceService = new KeyspaceService(session);
  private static final TableService tableService = new TableService(session);
  private static final IndexService indexService = new IndexService(session);
  private static final ViewService viewService = new ViewService(session);

  private static final ObjectMapper mapper =
      new ObjectMapper()
          .registerModule(new ProblemModule())
          .registerModule(new ParameterNamesModule())
          .registerModule(new Jdk8Module())
          .registerModule(new JavaTimeModule());

  public static void main(String[] args) {

    HttpServerStarter application =
        new HttpServerStarter(keyspaceService, tableService, indexService, viewService, mapper);
    try {
      application.prepare();
      application.run();
    } catch (Exception e) {
      log.error("Unable to start Cassey application");
      System.exit(-1);
    }
  }
}
