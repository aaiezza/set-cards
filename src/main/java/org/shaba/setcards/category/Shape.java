package org.shaba.setcards.category;

import org.shaba.setcards.Category;

public enum Shape implements Category.Value {
  OVAL,
  FISH,
  DIAMOND;

  public Category getCategory() {
    return new Category() {
      public Class<? extends Value> getCategoryClass() {
        return Shape.class;
      }
    };
  }
}
