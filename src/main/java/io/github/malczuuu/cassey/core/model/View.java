package io.github.malczuuu.cassey.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class View {

  private final String viewName;
  private final String baseTableName;
  private final boolean includeAllColumns;
  private final String whereClause;

  @JsonCreator
  public View(
      @JsonProperty("view_name") String viewName,
      @JsonProperty("base_table_name") String baseTableName,
      @JsonProperty("include_all_columns") boolean includeAllColumns,
      @JsonProperty("where_clause") String whereClause) {
    this.viewName = viewName;
    this.baseTableName = baseTableName;
    this.includeAllColumns = includeAllColumns;
    this.whereClause = whereClause;
  }

  @JsonProperty("view_name")
  public String getViewName() {
    return viewName;
  }

  @JsonProperty("base_table_name")
  public String getBaseTableName() {
    return baseTableName;
  }

  @JsonProperty("include_all_columns")
  public boolean isIncludeAllColumns() {
    return includeAllColumns;
  }

  @JsonProperty("where_clause")
  public String getWhereClause() {
    return whereClause;
  }
}
