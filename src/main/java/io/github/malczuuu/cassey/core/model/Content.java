package io.github.malczuuu.cassey.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Iterator;
import java.util.List;

public class Content<T> implements Iterable<T> {

  private final List<T> content;

  @JsonCreator
  public Content(@JsonProperty("content") List<T> content) {
    this.content = content;
  }

  @JsonProperty("content")
  public List<T> getContent() {
    return content;
  }

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }

  @Override
  public String toString() {
    return content.toString();
  }
}
