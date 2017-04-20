package floatstring;

import floatstring.termalg.shared.GTermAlg;

public interface IsVal<Term> extends GTermAlg<Term, Boolean> {
  default Boolean TmFloat(float p1) {
    return true;
  }
  default Boolean TmString(String p1) {
    return true;
  }
  default Boolean TmTimesFloat(Term t1, Term t2) {
    return false;
  }
}
