package org.shaba.setcards.representation;

import static io.vavr.API.*;

import io.vavr.Function0;
import io.vavr.control.Try;
import java.util.PrimitiveIterator.OfInt;
import java.util.function.Function;
import org.shaba.setcards.StandardCard;
import org.shaba.setcards.StandardCard.*;

public class StandardCardParser implements CardParser {

  public static StandardCardParser standardCardParser() {
    return new StandardCardParser();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Try<StandardCard> apply(final String rawCardString) {
    return Success(rawCardString)
        .map(String::trim)
        .map(String::toUpperCase)
        .transform(
            cardString ->
                cardString
                    .map(this::parseFormattedCardString)
                    .mapTry(Function0::apply)
                    .mapFailure(Case($(), t -> new UnknownCardFormatException(cardString.get()))));
  }

  private Function0<StandardCard> parseFormattedCardString(final String cardString) {
    final OfInt properties = cardString.chars().iterator();

    return Function(StandardCard::standardCardBuilder)
        .andThen(withQuantity((char) properties.nextInt()))
        .andThen(withColor((char) properties.nextInt()))
        .andThen(withFill((char) properties.nextInt()))
        .andThen(withShape((char) properties.nextInt()))
        .andThen(
            f -> {
              if (properties.hasNext()) throw new IllegalArgumentException();
              else return f;
            });
  }

  private Function<QuantitySelector, ColorSelector> withQuantity(final char quantity) {
    switch (quantity) {
      case '1':
        return QuantitySelector::one;
      case '2':
        return QuantitySelector::two;
      case '3':
        return QuantitySelector::three;
      default:
        throw new IllegalArgumentException();
    }
  }

  private Function<ColorSelector, FillSelector> withColor(final char color) {
    switch (color) {
      case 'R':
        return ColorSelector::red;
      case 'G':
        return ColorSelector::green;
      case 'P':
        return ColorSelector::purple;
      default:
        throw new IllegalArgumentException();
    }
  }

  private Function<FillSelector, ShapeSelector> withFill(final char fill) {
    switch (fill) {
      case 'E':
        return FillSelector::empty;
      case 'S':
        return FillSelector::striped;
      case 'F':
        return FillSelector::filled;
      default:
        throw new IllegalArgumentException();
    }
  }

  private Function<ShapeSelector, StandardCard> withShape(final char shape) {
    switch (shape) {
      case 'O':
        return ShapeSelector::ovals;
      case 'F':
        return ShapeSelector::fishes;
      case 'D':
        return ShapeSelector::diamonds;
      default:
        throw new IllegalArgumentException();
    }
  }
}
