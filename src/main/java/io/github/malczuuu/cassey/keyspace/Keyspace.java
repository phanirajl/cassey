package io.github.malczuuu.cassey.keyspace;

import java.util.Map;
import java.util.stream.Collectors;

public class Keyspace {

  private final String name;
  private final boolean durableWrites;
  private final Map<String, String> replication;

  public Keyspace(String name, boolean durableWrites, Map<String, String> replication) {
    this.name = name;
    this.durableWrites = durableWrites;
    this.replication = replication;
  }

  public String getName() {
    return name;
  }

  public boolean isDurableWrites() {
    return durableWrites;
  }

  public Map<String, String> getReplication() {
    return replication;
  }

  @Override
  public String toString() {
    return "(name="
        + name
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
