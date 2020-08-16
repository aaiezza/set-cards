package org.shaba.setcards;

import static org.assertj.core.api.Assertions.assertThat;
import static org.shaba.setcards.StandardCard.standardCardBuilder;

import one.util.streamex.StreamEx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shaba.setcards.StandardCard.QuantitySelector;

class SetFinderTest {
  private SetFinder subject;

  @BeforeEach
  void setUp() {
    subject = new SetFinder(new IsASetPredicate());
  }

  @Test
  void shouldReturnExpectedSets() {
    assertThat(
            StreamEx.of(
                    subject.findSets(
                        card().one().red().filled().diamond(),
                        card().two().red().filled().ovals(),
                        card().two().purple().filled().ovals(),
                        card().two().purple().empty().fishes(),
                        card().two().green().empty().diamonds(),
                        card().two().green().empty().ovals(),
                        card().three().purple().empty().ovals(),
                        card().two().red().striped().fishes(),
                        card().one().red().empty().oval(),
                        card().two().red().empty().diamonds(),
                        card().two().purple().empty().diamonds(),
                        card().one().green().striped().oval()))
                .peek(System.out::println)
                .toList())
        .hasSize(6);
  }

  @Test
  void shouldReturnExpectedSets2() {
    assertThat(
            StreamEx.of(
                    subject.findSets(
                        card().three().purple().filled().ovals(),
                        card().three().green().empty().ovals(),
                        card().one().red().striped().diamond(),
                        card().three().purple().empty().ovals(),
                        card().three().purple().striped().ovals(),
                        card().two().green().filled().fishes(),
                        card().two().red().empty().ovals(),
                        card().three().green().empty().fishes(),
                        card().two().red().filled().ovals(),
                        card().three().red().empty().ovals(),
                        card().one().purple().striped().diamond(),
                        card().one().red().empty().diamond()))
                .peek(System.out::println)
                .toList())
        .hasSize(6);
  }

  private static QuantitySelector card() {
    return standardCardBuilder();
  }
}
