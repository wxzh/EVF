package untyped;

import java.util.function.Function;

import untyped.termalg.shared.TermAlgQueryWithCtx;

public interface IsVarUsed<Term> extends TermAlgQueryWithCtx<Integer, Boolean, Term>, varapp.IsVarUsed<Term> {
  @Override default Function<Integer,Boolean> TmAbs(String x, Term t) {
    return c -> visitTerm(t).apply(c+1);
  }
}
