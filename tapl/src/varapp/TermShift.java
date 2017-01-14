package varapp;

import java.util.function.Function;

import varapp.termalg.shared.TermAlgTransformWithCtx;


public interface TermShift<Term> extends TermAlgTransformWithCtx<Integer, Term> {
  int d();
  default Function<Integer,Term> TmVar(int x, int n) {
    return c -> x >= c ? alg().TmVar(x+d(), n+d()) : alg().TmVar(x, n+d());
  }
//  default Function<Integer> TmAbs(String x, Tm t) {
//    return (d, c) -> alg().TmAbs(x, visitTerm(t).termShift(d, c));
//  }
}