package arith;

import arith.termalg.shared.GTermAlg;

//TODO: Succ(True) is not a val
public interface IsVal<Term> extends GTermAlg<Term, Boolean>, bool.IsVal<Term>, nat.IsNumericVal<Term> {
  default Boolean TmIsZero(Term t) {
    return false;
  }
}