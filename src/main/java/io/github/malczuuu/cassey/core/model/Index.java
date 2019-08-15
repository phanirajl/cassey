package io.github.malczuuu.cassey.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class Index {

  private final String indexName;
  private final String kind;
  private final Map<String, String> options;

  @JsonCreator
  public Index(
      @JsonProperty("index_name") String indexName,
      @JsonProperty("kind") String kind,
      @JsonProperty("options") Map<String, String> options) {
    this.indexName = indexName;
    this.kind = kind;
    this.options = options;
  }

  @JsonProperty("index_name")
  public String getIndexName() {
    return indexName;
  }

  @JsonProperty("kind")
  public String getKind() {
    return kind;
  }

  @JsonProperty("options")
  public Map<String, String> getOptions() {
    return options;
  }
}
