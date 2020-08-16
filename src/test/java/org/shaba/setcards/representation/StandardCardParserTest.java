package org.shaba.setcards.representation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.shaba.setcards.StandardCard.standardCardBuilder;

import java.util.stream.Stream;
import one.util.streamex.StreamEx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.shaba.setcards.StandardCard;
import org.shaba.setcards.StandardCard.QuantitySelector;
import org.shaba.setcards.representation.CardParser.UnknownCardFormatException;

class StandardCardParserTest {
  private StandardCardParser subject;

  @BeforeEach
  private void setUp() {
    subject = new StandardCardParser();
  }

  @ParameterizedTest
  @MethodSource
  void shouldParseSuccessfully(final String cardString, final StandardCard expectedCard) {
    assertThat(subject.apply(cardString).get()).isEqualTo(expectedCard);
  }

  static Stream<Arguments> shouldParseSuccessfully() {
    return StreamEx.of(
        arguments("1REO", card().one().red().empty().oval()),
        arguments(" 3geo   ", card().three().green().empty().ovals()),
        arguments("1PED", card().one().purple().empty().diamond()),
        arguments("       \n  2RFF  ", card().two().red().filled().fishes()),
        arguments("3RSF", card().three().red().striped().fishes()));
  }

  private static QuantitySelector card() {
    return standardCardBuilder();
  }

  @ParameterizedTest
  @MethodSource
  void shouldNotParseSuccessfully(final String cardString) {
    assertThatCode(subject.apply(cardString)::get).isInstanceOf(UnknownCardFormatException.class);
  }

  static Stream<Arguments> shouldNotParseSuccessfully() {
    return StreamEx.of(
        arguments("1 REO"),
        arguments(" 4geo   "),
        arguments("1TED"),
        arguments("31TED"),
        arguments("3TEL"),
        arguments("PED"),
        arguments("1ED"),
        arguments(""),
        arguments("3j"),
        arguments("j"),
        arguments("3RSF2345"));
  }
}
