package varapp;

import varapp.termalg.shared.GTermAlg;

public interface Eval1<Term> extends GTermAlg<Term, Term>, utils.Eval1<Term> {
  Term termSubstTop(Term s, Term t);
 	boolean isVal(Term t);

  default Term TmVar(int p1, int p2) {
    return noRuleApplies();
  }
  default Term TmApp(Term p1, Term p2) {
    return null;
  }
}
