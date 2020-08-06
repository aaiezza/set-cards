package org.shaba.setcards.category;

import org.shaba.setcards.Category;

public enum Quantity implements Category.Value {
  ONE,
  TWO,
  THREE;

  public Category getCategory() {
    return new Category() {
      public Class<? extends Value> getCategoryClass() {
        return Quantity.class;
      }
    };
  }
}
