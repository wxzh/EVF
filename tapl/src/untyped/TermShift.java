package untyped;

import java.util.function.Function;

import untyped.termalg.shared.TermAlgTransformWithCtx;

public interface TermShift<Term> extends TermAlgTransformWithCtx<Integer, Term>, varapp.TermShift<Term> {
  @Override default Function<Integer, Term> TmAbs(String x, Term t) {
    return c -> alg().TmAbs(x, visitTerm(t).apply(c+1));
  }
}