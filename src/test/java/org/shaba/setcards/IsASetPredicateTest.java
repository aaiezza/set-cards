package org.shaba.setcards;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.shaba.setcards.StandardCard.standardCardBuilder;

import java.util.stream.Stream;
import one.util.streamex.StreamEx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.shaba.setcards.StandardCard.QuantitySelector;

class IsASetPredicateTest {
  private IsASetPredicate subject;

  @BeforeEach
  private void setUp() {
    subject = new IsASetPredicate();
  }

  @ParameterizedTest
  @MethodSource
  void shouldAllBeConsideredSets(final Card c1, final Card c2, final Card c3) {
    assertThat(subject.apply(c1, c2, c3)).isTrue();
  }

  static Stream<Arguments> shouldAllBeConsideredSets() {
    return StreamEx.of(
        arguments(
            card().one().red().empty().oval(),
            card().one().red().empty().oval(),
            card().one().red().empty().oval()),
        arguments(
            card().one().red().empty().oval(),
            card().two().red().empty().ovals(),
            card().three().red().empty().ovals()),
        arguments(
            card().one().red().empty().oval(),
            card().one().green().empty().fish(),
            card().one().purple().empty().diamond()),
        arguments(
            card().one().red().empty().oval(),
            card().two().red().striped().fishes(),
            card().three().red().filled().diamonds()),
        arguments(
            card().three().red().striped().fishes(),
            card().one().purple().filled().diamond(),
            card().two().green().empty().ovals()));
  }

  @ParameterizedTest
  @MethodSource
  void shouldAllNotBeConsideredSets(final Card c1, final Card c2, final Card c3) {
    assertThat(subject.apply(c1, c2, c3)).isFalse();
  }

  static Stream<Arguments> shouldAllNotBeConsideredSets() {
    return StreamEx.of(
        arguments(
            card().three().red().empty().ovals(),
            card().one().red().empty().oval(),
            card().one().red().empty().oval()),
        arguments(
            card().three().red().empty().ovals(),
            card().one().red().empty().oval(),
            card().one().green().empty().oval()),
        arguments(
            card().three().red().empty().ovals(),
            card().one().red().filled().oval(),
            card().one().green().empty().oval()),
        arguments(
            card().three().red().empty().fishes(),
            card().one().red().filled().oval(),
            card().one().green().empty().oval()));
  }

  private static QuantitySelector card() {
    return standardCardBuilder();
  }
}
