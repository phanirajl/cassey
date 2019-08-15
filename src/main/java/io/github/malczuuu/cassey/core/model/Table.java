package io.github.malczuuu.cassey.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Table {

  private final String tableName;

  @JsonCreator
  public Table(@JsonProperty("table_name") String tableName) {
    this.tableName = tableName;
  }

  @JsonProperty("table_name")
  public String getTableName() {
    return tableName;
  }

  @Override
  public String toString() {
    return "(table_name=" + tableName + ")";
  }
}
