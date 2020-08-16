package org.shaba.setcards.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.shaba.setcards.StandardCard.standardCardBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shaba.setcards.StandardCard.QuantitySelector;
import org.shaba.setcards.application.Game.Sets;

class SetsTest {
  private Game game;

  private Sets subject;

  @BeforeEach
  void setUp() {
    game = Game.newGame();
  }

  @Test
  void shouldWork() {
    subject =
        game.addFieldCard(card().one().red().striped().fish())
            .addFieldCard(card().two().red().striped().fishes())
            .addFieldCard(card().three().red().striped().fishes())
            .addFieldCard(card().one().green().empty().diamond())
            .addFieldCard(card().one().purple().filled().oval())
            .getAvailableSets();
    System.out.println(subject);
    assertThat(subject)
        .hasToString("    𝕊ets\n" + "  0: 1PFO, 1GED, 1RSF\n" + "  1: 1RSF, 2RSF, 3RSF\n");
  }

  private static QuantitySelector card() {
    return standardCardBuilder();
  }
}
