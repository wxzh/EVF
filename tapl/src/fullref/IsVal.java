package fullref;

import fullref.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, fullsimple.IsVal<Term, Ty> {
  default Boolean TmLoc(int l) {
		return true;
	}
	default Boolean TmRef(Term t) {
	  return false;
	}
	default Boolean TmDeref(Term t) {
	  return false;
	}
	default Boolean TmAssign(Term t1, Term t2) {
	  return false;
	}
}