package varapp;

import java.util.function.Function;

import varapp.termalg.shared.TermAlgTransformWithCtx;

public interface TermSubst<Term> extends TermAlgTransformWithCtx<Integer, Term> {
  Term s();
  int j();
  TermShift<Term> termShift(int d);
  default Function<Integer, Term> TmVar(int x, int n) {
    return c -> x == j() + c ? termShift(c).visitTerm(s()).apply(0) : alg().TmVar(x, n);
  }
}