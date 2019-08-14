package io.github.malczuuu.cassey.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Content<T> implements Collection<T> {

  private final List<T> content;

  public Content(List<T> content) {
    this.content = content;
  }

  public List<T> getContent() {
    return content;
  }

  @Override
  public int size() {
    return content.size();
  }

  @Override
  public boolean isEmpty() {
    return content.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return content.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }

  @Override
  public Object[] toArray() {
    return content.toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] a) {
    return content.toArray(a);
  }

  @Override
  public boolean add(T t) {
    return content.add(t);
  }

  @Override
  public boolean remove(Object o) {
    return content.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return content.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return content.addAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return content.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return content.retainAll(c);
  }

  @Override
  public void clear() {
    content.clear();
  }

  @Override
  public String toString() {
    return content.toString();
  }
}
