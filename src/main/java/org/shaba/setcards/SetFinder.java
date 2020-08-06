package org.shaba.setcards;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import one.util.streamex.StreamEx;

@lombok.Data
public class SetFinder {
  private final IsASetPredicate isASetPredicate;

  public List<Set<Card>> findSets(final Card... cards) {
    return StreamEx.of(Sets.combinations(StreamEx.of(cards).toSet(), 3))
        .filter(isASetPredicate)
        .toList();
  }
}
