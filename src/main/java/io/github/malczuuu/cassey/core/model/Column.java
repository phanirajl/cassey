package io.github.malczuuu.cassey.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Column {

  private final String columnName;
  private final String clusteringOrder;
  private final Integer position;
  private final String kind;
  private final String type;

  @JsonCreator
  public Column(
      @JsonProperty("column_name") String columnName,
      @JsonProperty("clustering_order") String clusteringOrder,
      @JsonProperty("position") Integer position,
      @JsonProperty("kind") String kind,
      @JsonProperty("type") String type) {
    this.columnName = columnName;
    this.clusteringOrder = clusteringOrder;
    this.position = position;
    this.kind = kind;
    this.type = type;
  }

  @JsonProperty("column_name")
  public String getColumnName() {
    return columnName;
  }

  @JsonProperty("clustering_order")
  public String getClusteringOrder() {
    return clusteringOrder;
  }

  @JsonProperty("position")
  public Integer getPosition() {
    return position;
  }

  @JsonProperty("kind")
  public String getKind() {
    return kind;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "(column_name="
        + columnName
        + ", clustering_order="
        + clusteringOrder
        + ", position="
        + position
        + ", kind="
        + kind
        + ", type="
        + type
        + ")";
  }
}
