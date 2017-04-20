package moreextension;

import moreextension.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, extension.IsVal<Term> {
	default Boolean TmUnit() {
		return true;
	}
	default Boolean TmFix(Term p1) {
	  return false;
	}
	default Boolean TmInert(Ty p1) {
	  return false;
	}
	default Boolean TmAscribe(Term p1, Ty p2) {
	  return false;
	}
}