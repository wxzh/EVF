package bool;

import bool.termalg.shared.GTermAlg;

public interface IsVal<Term> extends GTermAlg<Term, Boolean> {
	default Boolean TmTrue() {
		return true;
	}

	default Boolean TmFalse() {
		return true;
	}

	default Boolean TmIf(Term t1, Term t2, Term t3) {
	  return false;
	}
}