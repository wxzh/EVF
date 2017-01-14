package typed;

import java.util.function.Function;

import typed.termalg.shared.TermAlgTransformWithCtx;

public interface TermShift<Term, Ty> extends TermAlgTransformWithCtx<Integer, Term, Ty>, varapp.TermShift<Term> {
  @Override default Function<Integer, Term> TmAbs(String x, Ty ty, Term t) {
    return c -> alg().TmAbs(x, ty, visitTerm(t).apply(c+1));
  }
}