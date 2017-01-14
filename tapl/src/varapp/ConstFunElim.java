package varapp;

import varapp.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term> extends TermAlgTransform<Term> {
  IsVarUsed<Term> isVarUsed();
  TermShift<Term> termShift(int d);
  GetBodyFromTmAbs<Term> getBodyFromTmAbs();

  default Term TmApp(Term e1, Term e2) {
    Term e = visitTerm(e1);
    return getBodyFromTmAbs().visitTerm(e)
        .map(t -> isVarUsed().visitTerm(t).apply(0)
          ? alg().TmApp(e, visitTerm(e2)) : termShift(-1).visitTerm(t).apply(0))
        .orElse(alg().TmApp(e, visitTerm(e2)));
  }
}