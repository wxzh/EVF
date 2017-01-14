package typed;

import java.util.function.Function;

import typed.termalg.shared.TermAlgTransformWithCtx;

public interface TermSubst<Term, Ty> extends TermAlgTransformWithCtx<Integer, Term, Ty>, varapp.TermSubst<Term> {
  @Override TermShift<Term, Ty> termShift(int d);
  @Override default Function<Integer, Term> TmAbs(String x, Ty ty, Term t) {
    return c -> alg().TmAbs(x, ty, visitTerm(t).apply(c+1));
  }
}