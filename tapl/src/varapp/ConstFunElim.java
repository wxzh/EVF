package varapp;

import java.util.Optional;

public interface ConstFunElim<Term> extends TermAlgTransform<Term> {
  boolean isVarUsed(int n, Term t);
  Term termShift(int d, Term t);
  Optional<Term> getBodyFromTmAbs(Term t);

  default Term TmApp(Term e1, Term e2) {
    Term e = visitTerm(e1);
    return getBodyFromTmAbs(e)
        .map(t -> isVarUsed(0, t) ? alg().TmApp(e, visitTerm(e2)) : termShift(-1, t))
        .orElse(alg().TmApp(e, visitTerm(e2)));
  }
}