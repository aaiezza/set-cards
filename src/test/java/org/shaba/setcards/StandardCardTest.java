package org.shaba.setcards;

import org.junit.Test;
import org.shaba.setcards.category.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.shaba.setcards.StandardCard.standardCardBuilder;

public class StandardCardTest {

  @Test
  public void shouldBuildStandardCardAsExpected() {
    final StandardCard card = standardCardBuilder().one().green().filled().fish();
    assertThat(card)
        .returns(Quantity.ONE, StandardCard::getQuantity)
        .returns(Color.GREEN, StandardCard::getColor)
        .returns(Fill.FILLED, StandardCard::getFill)
        .returns(Shape.FISH, StandardCard::getShape);
    System.out.println(card); 
    System.out.println(card.hashCode()); 
  }
}
