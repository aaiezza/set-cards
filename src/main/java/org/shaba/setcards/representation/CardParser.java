package org.shaba.setcards.representation;

import static java.lang.String.format;

import io.vavr.control.Try;
import java.util.function.Function;
import org.shaba.setcards.Card;

@SuppressWarnings("serial")
@FunctionalInterface
public interface CardParser extends Function<String, Try<? extends Card>> {

  public static class CardParserException extends RuntimeException {

    public CardParserException(final String message) {
      super(message);
    }
  }

  @lombok.Data
  @lombok.EqualsAndHashCode(callSuper = true)
  public static class UnknownCardFormatException extends CardParserException {
    private final String badCardFormat;

    public UnknownCardFormatException(final String badCardFormat) {
      super(format("Unknown card format: %s", badCardFormat));
      this.badCardFormat = badCardFormat;
    }
  }
}
