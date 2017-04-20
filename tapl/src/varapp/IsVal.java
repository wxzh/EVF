package varapp;

import varapp.termalg.shared.GTermAlg;

public interface IsVal<Term> extends GTermAlg<Term, Boolean> {
  default Boolean TmVar(int x, int n) {
    return false;
  }
  default Boolean TmApp(Term p1, Term p2) {
    return false;
  }
}
