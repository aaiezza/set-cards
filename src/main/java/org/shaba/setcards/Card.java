package org.shaba.setcards;

import static java.util.function.UnaryOperator.identity;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

public interface Card {
  public StreamEx<Category.Value> getValueStream();

  public default EntryStream<Category, Category.Value> getCategoryStream() {
    return getValueStream().mapToEntry(Category.Value::getCategory, identity());
  }
}
