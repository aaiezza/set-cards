package org.shaba.setcards.category;

import org.shaba.setcards.Category;

public enum Fill implements Category.Value {
  EMPTY,
  STRIPED,
  FILLED;

  public Category getCategory() {
    return new Category() {
      public Class<? extends Value> getCategoryClass() {
        return Fill.class;
      }
    };
  }
}
