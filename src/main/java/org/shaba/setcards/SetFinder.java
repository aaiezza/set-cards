package org.shaba.setcards;

import com.google.common.collect.Sets;
import io.vavr.API;
import java.util.List;
import java.util.Set;
import one.util.streamex.StreamEx;

@lombok.Data
public class SetFinder {
  private final IsASetPredicate isASetPredicate;

  public List<Set<Card>> findSets(final Card... cards) {
    return API.Try(() -> Sets.combinations(StreamEx.of(cards).toSet(), 3))
        .map(StreamEx::of)
        .getOrElse(StreamEx::empty)
        .filter(isASetPredicate)
        .toList();
  }

  public static SetFinder setFinder() {
    return new SetFinder(new IsASetPredicate());
  }
}
