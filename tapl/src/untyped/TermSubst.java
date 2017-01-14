package untyped;

import java.util.function.Function;

import untyped.termalg.shared.TermAlgTransformWithCtx;

public interface TermSubst<Term> extends TermAlgTransformWithCtx<Integer, Term>, varapp.TermSubst<Term> {
  @Override TermShift<Term> termShift(int d);
  @Override default Function<Integer, Term> TmAbs(String x, Term t) {
    return c -> alg().TmAbs(x, visitTerm(t).apply(c+1));
  }
}