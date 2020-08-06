package org.shaba.setcards.category;

import org.shaba.setcards.Category;

public enum Color implements Category.Value {
  RED,
  GREEN,
  PURPLE;

  public Category getCategory() {
    return new Category() {
      public Class<? extends Value> getCategoryClass() {
        return Color.class;
      }
    };
  }
}
