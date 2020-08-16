package org.shaba.setcards;

import lombok.AccessLevel;
import one.util.streamex.StreamEx;
import org.shaba.setcards.category.*;

@lombok.Data
@lombok.NonNull
public class StandardCard implements Card {
  private final Quantity quantity;
  private final Color color;
  private final Fill fill;
  private final Shape shape;

  public StreamEx<Category.Value> getValueStream() {
    return StreamEx.of(shape, fill, color, quantity).select(Category.Value.class);
  }

  @Override
  public String toString() {
    return String.format(
        "%d%s%s%s",
        quantity.ordinal() + 1,
        color.name().substring(0, 1),
        fill.name().substring(0, 1),
        shape.name().substring(0, 1));
  }

  public static QuantitySelector standardCardBuilder() {
    return new QuantitySelector();
  }

  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class QuantitySelector {
    public ColorSelector one() {
      return new ColorSelector(Quantity.ONE);
    }

    public ColorSelector two() {
      return new ColorSelector(Quantity.TWO);
    }

    public ColorSelector three() {
      return new ColorSelector(Quantity.THREE);
    }
  }

  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ColorSelector {
    private final Quantity quantity;

    public FillSelector red() {
      return new FillSelector(quantity, Color.RED);
    }

    public FillSelector green() {
      return new FillSelector(quantity, Color.GREEN);
    }

    public FillSelector purple() {
      return new FillSelector(quantity, Color.PURPLE);
    }
  }

  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class FillSelector {
    private final Quantity quantity;
    private final Color color;

    public ShapeSelector empty() {
      return new ShapeSelector(quantity, color, Fill.EMPTY);
    }

    public ShapeSelector striped() {
      return new ShapeSelector(quantity, color, Fill.STRIPED);
    }

    public ShapeSelector filled() {
      return new ShapeSelector(quantity, color, Fill.FILLED);
    }
  }

  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ShapeSelector {
    private final Quantity quantity;
    private final Color color;
    private final Fill fill;

    public StandardCard oval() {
      return ovals();
    }

    public StandardCard ovals() {
      return new StandardCard(quantity, color, fill, Shape.OVAL);
    }

    public StandardCard fish() {
      return fishes();
    }

    public StandardCard fishes() {
      return new StandardCard(quantity, color, fill, Shape.FISH);
    }

    public StandardCard diamond() {
      return diamonds();
    }

    public StandardCard diamonds() {
      return new StandardCard(quantity, color, fill, Shape.DIAMOND);
    }
  }
}
