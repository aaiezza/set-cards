package org.shaba.setcards.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.shaba.setcards.StandardCard.standardCardBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shaba.setcards.StandardCard.QuantitySelector;
import org.shaba.setcards.application.Game.FieldCards;

class FieldCardsTest {
  private FieldCards subject;

  @BeforeEach
  void setUp() {
    subject = new FieldCards();
  }

  @Test
  void shouldWork() {
    subject =
        subject
            .add(card().one().red().empty().oval())
            .add(card().three().green().empty().ovals())
            .add(card().one().purple().empty().diamond())
            .add(card().two().red().filled().fishes())
            .add(card().three().red().striped().fishes());
    System.out.println(subject);
    assertThat(subject)
        .hasToString("   𝔽ield\n" + " 0: 1REO │  1: 3GEO │  2: 1PED\n" + " 3: 2RFF │  4: 3RSF\n");
  }

  private static QuantitySelector card() {
    return standardCardBuilder();
  }
}
