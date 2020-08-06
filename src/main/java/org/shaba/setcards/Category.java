package org.shaba.setcards;

public abstract class Category {
  public abstract Class<? extends Value> getCategoryClass();

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getCategoryClass() == null) ? 0 : getCategoryClass().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Category other = (Category) obj;
    if (getCategoryClass() == null) {
      if (other.getCategoryClass() != null) return false;
    } else if (!getCategoryClass().equals(other.getCategoryClass())) return false;
    return true;
  }

  public static interface Value {
    public Category getCategory();
  }
}
