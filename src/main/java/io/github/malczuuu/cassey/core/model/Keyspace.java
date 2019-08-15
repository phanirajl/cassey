package io.github.malczuuu.cassey.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.stream.Collectors;

public class Keyspace {

  private final String keyspaceName;
  private final boolean durableWrites;
  private final Map<String, String> replication;

  @JsonCreator
  public Keyspace(
      @JsonProperty("keyspace_name") String keyspaceName,
      @JsonProperty("durable_writes") boolean durableWrites,
      @JsonProperty("replication") Map<String, String> replication) {
    this.keyspaceName = keyspaceName;
    this.durableWrites = durableWrites;
    this.replication = replication;
  }

  @JsonProperty("keyspace_name")
  public String getKeyspaceName() {
    return keyspaceName;
  }

  @JsonProperty("durable_writes")
  public boolean isDurableWrites() {
    return durableWrites;
  }

  @JsonProperty("replication")
  public Map<String, String> getReplication() {
    return replication;
  }

  @Override
  public String toString() {
    return "(keyspace_name="
        + keyspaceName
        + ", durable_writes="
        + durableWrites
        + ", replication="
        + replicationAsString()
        + ")";
  }

  private String replicationAsString() {
    return replication.entrySet().stream()
        .map(entry -> entry.getKey() + "=" + entry.getValue())
        .collect(Collectors.joining(",", "(", ")"));
  }
}
