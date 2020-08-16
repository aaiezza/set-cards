package org.shaba.setcards.application;

import static io.vavr.Predicates.not;
import static java.util.Collections.emptyList;
import static org.shaba.setcards.SetFinder.setFinder;

import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.shaba.setcards.*;

@lombok.Data
public class Game {
  private final SetFinder setFinder;
  private final FieldCards fieldCards;

  public Game addFieldCard(final Card card) {
    return new Game(setFinder, fieldCards.add(card));
  }

  public Game removeFieldCard(final int cardIndex) {
    return new Game(setFinder, fieldCards.remove(cardIndex));
  }

  public Game clearField() {
    return new Game(setFinder, new FieldCards());
  }

  public Sets getAvailableSets() {
    return new Sets(setFinder.findSets(fieldCards.getValue().toArray(StandardCard[]::new)));
  }

  @Override
  public String toString() {
    return new StringBuilder().append(fieldCards).append(getAvailableSets()).toString();
  }

  public static Game newGame() {
    return new Game(setFinder(), new FieldCards());
  }

  @lombok.Data
  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class FieldCards {
    private final List<Card> value;

    public FieldCards() {
      this(emptyList());
    }

    public FieldCards add(final Card card) {
      return new FieldCards(StreamEx.of(value).append(card).toImmutableList());
    }

    public FieldCards remove(final Integer cardIndex) {
      return new FieldCards(
          EntryStream.of(value).filterKeys(not(cardIndex::equals)).values().toImmutableList());
    }

    @Override
    public String toString() {
      final StringBuilder out = new StringBuilder("   𝔽ield");
      final int cardsPerRow = 4;
      EntryStream.of(value)
          .forKeyValue(
              (index, card) ->
                  out.append(index % cardsPerRow == 0 ? "\n" : " │ ")
                      .append(String.format("%2d: %s", index, card)));

      return out.append("\n").toString();
    }
  }

  @lombok.Data
  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Sets {
    private final List<Set<Card>> value;

    @Override
    public String toString() {
      final StringBuilder out = new StringBuilder("    𝕊ets");
      EntryStream.of(value)
          .forKeyValue(
              (index, cards) ->
                  out.append("\n")
                      .append(String.format(" %2d: %s", index, StreamEx.of(cards).joining(", "))));

      return out.append("\n").toString();
    }
  }
}
