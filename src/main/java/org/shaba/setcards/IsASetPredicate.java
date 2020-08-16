package org.shaba.setcards;

import io.vavr.Function3;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("serial")
public class IsASetPredicate implements Function3<Card, Card, Card, Boolean>, Predicate<Set<Card>> {
  @Override
  public Boolean apply(final Card t1, final Card t2, final Card t3) {
    return t1.getCategoryStream()
        .mapKeys(this::getValueFor)
        .mapKeyValue(
            (categoryOf, v1) ->
                allSameOrAllDifferent(v1, categoryOf.apply(t2), categoryOf.apply(t3)))
        .allMatch(Boolean.TRUE::equals);
  }

  @Override
  public boolean test(final Set<Card> cards) {
    if (3 != cards.size()) throw new IllegalArgumentException();

    final Iterator<Card> cardIter = cards.iterator();
    return apply(cardIter.next(), cardIter.next(), cardIter.next());
  }

  private Function<Card, Category.Value> getValueFor(final Category category) {
    return card -> card.getCategoryStream().filterKeys(category::equals).values().findFirst().get();
  }

  private Boolean allSameOrAllDifferent(
      final Category.Value v1, final Category.Value v2, final Category.Value v3) {
    return (v1.equals(v2) && v2.equals(v3)) || (!v1.equals(v2) && !v2.equals(v3) && !v1.equals(v3));
  }
}
