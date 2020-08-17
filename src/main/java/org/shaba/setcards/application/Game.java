package org.shaba.setcards.application;

import static java.util.Collections.emptyList;
import static org.shaba.setcards.SetFinder.setFinder;

import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.shaba.setcards.Card;
import org.shaba.setcards.SetFinder;

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
    return fieldCards.findSetsWith(setFinder);
  }

  public Game removeFieldCardsInSet(final int setIndex) {
    return new Game(
        setFinder,
        getAvailableSets()
            .getSetStream(setIndex)
            .sequential()
            .reduce(fieldCards, FieldCards::remove, (l, r) -> l));
  }

  @Override
  public String toString() {
    return new StringBuilder().append(fieldCards).append(getAvailableSets()).toString();
  }

  public static Game newGame() {
    return new Game(setFinder(), new FieldCards());
  }

  @lombok.EqualsAndHashCode
  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class FieldCards {
    private final List<Card> value;

    public FieldCards() {
      this(emptyList());
    }

    public List<Card> asList() {
      return value;
    }

    public StreamEx<Card> stream() {
      return StreamEx.of(asList());
    }

    public EntryStream<Integer, Card> entryStream() {
      return EntryStream.of(asList());
    }

    public Card[] asArray() {
      return asList().toArray(Card[]::new);
    }

    public Sets findSetsWith(final SetFinder setFinder) {
      return new Sets(setFinder.findSets(asArray()));
    }

    public FieldCards add(final Card card) {
      return new FieldCards(stream().append(card).toImmutableList());
    }

    public FieldCards remove(final Integer cardIndex) {
      return new FieldCards(entryStream().removeKeys(cardIndex::equals).values().toImmutableList());
    }

    public FieldCards remove(final Card card) {
      return new FieldCards(stream().remove(card::equals).toImmutableList());
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

  @lombok.EqualsAndHashCode
  @lombok.RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Sets {
    @lombok.Getter private final List<Set<Card>> value;

    public StreamEx<Card> getSetStream(final Integer setIndex) {
      return StreamEx.of(value.get(setIndex));
    }

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
