package io.github.malczuuu.cassey;

import static spark.Spark.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.malczuuu.cassey.core.IndexService;
import io.github.malczuuu.cassey.core.KeyspaceService;
import io.github.malczuuu.cassey.core.TableService;
import io.github.malczuuu.cassey.core.ViewService;
import io.github.malczuuu.cassey.http.CommonProblems;
import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemException;
import spark.Spark;

class HttpServerStarter {

  private final KeyspaceService keyspaceService;
  private final TableService tableService;
  private final IndexService indexService;
  private final ViewService viewService;
  private final ObjectMapper mapper;

  HttpServerStarter(
      KeyspaceService keyspaceService,
      TableService tableService,
      IndexService indexService,
      ViewService viewService,
      ObjectMapper mapper) {
    this.keyspaceService = keyspaceService;
    this.tableService = tableService;
    this.indexService = indexService;
    this.viewService = viewService;
    this.mapper = mapper;
  }

  void prepare() throws Exception {
    CommonProblems commonProblems = CommonProblems.load();
    Spark.exception(
        ProblemException.class,
        (ex, req, res) -> {
          res.status(ex.getProblem().getStatus());
          res.type(Problem.CONTENT_TYPE);
          try {
            res.body(mapper.writeValueAsString(ex.getProblem()));
          } catch (JsonProcessingException e) {
            res.status(500);
            res.body(commonProblems.internalServerError());
          }
        });

    Spark.notFound(
        (req, res) -> {
          res.status(404);
          res.type(Problem.CONTENT_TYPE);
          return commonProblems.notFound();
        });

    Spark.after(
        (req, res) -> {
          if (req.pathInfo().startsWith("/api")) {
            res.type("application/json");
          }
        });
  }

  void run() {
    get(
        "/api/keyspaces",
        (req, res) -> mapper.writeValueAsString(keyspaceService.findAllKeyspaces()));

    get(
        "/api/keyspaces/:keyspace_name",
        (req, res) ->
            mapper.writeValueAsString(
                keyspaceService.findOneKeyspace(req.params("keyspace_name"))));

    get(
        "/api/keyspaces/:keyspace_name/tables",
        (req, res) ->
            mapper.writeValueAsString(tableService.findAllTables(req.params("keyspace_name"))));

    get(
        "/api/keyspaces/:keyspace_name/tables/:table_name",
        (req, res) ->
            mapper.writeValueAsString(
                tableService.findOneTable(req.params("keyspace_name"), req.params("table_name"))));

    get(
        "/api/keyspaces/:keyspace_name/tables/:table_name/columns",
        (req, res) ->
            mapper.writeValueAsString(
                tableService.findAllColumns(
                    req.params("keyspace_name"), req.params("table_name"))));
    get(
        "/api/keyspaces/:keyspace_name/tables/:table_name/columns/:column_name",
        (req, res) ->
            mapper.writeValueAsString(
                tableService.findOneColumn(
                    req.params("keyspace_name"),
                    req.params("table_name"),
                    req.params("column_name"))));

    get(
        "/api/keyspaces/:keyspace_name/tables/:table_name/indexes",
        (req, res) ->
            mapper.writeValueAsString(
                indexService.findAllIndexes(
                    req.params("keyspace_name"), req.params("table_name"))));
    get(
        "/api/keyspaces/:keyspace_name/tables/:table_name/indexes/:index_name",
        (req, res) ->
            mapper.writeValueAsString(
                indexService.findOneIndex(
                    req.params("keyspace_name"),
                    req.params("table_name"),
                    req.params("index_name"))));

    get(
        "/api/keyspaces/:keyspace_name/views",
        (req, res) ->
            mapper.writeValueAsString(viewService.findAllViews(req.params("keyspace_name"))));
    get(
        "/api/keyspaces/:keyspace_name/views/:view_name",
        (req, res) ->
            mapper.writeValueAsString(
                viewService.findOneView(req.params("keyspace_name"), req.params("view_name"))));

    get(
        "/api/keyspaces/:keyspace_name/views/:view_name/columns",
        (req, res) ->
            mapper.writeValueAsString(
                viewService.findAllColumns(req.params("keyspace_name"), req.params("view_name"))));
    get(
        "/api/keyspaces/:keyspace_name/views/:view_name/columns/:column_name",
        (req, res) ->
            mapper.writeValueAsString(
                viewService.findOneColumn(
                    req.params("keyspace_name"),
                    req.params("view_name"),
                    req.params("column_name"))));
  }
}
