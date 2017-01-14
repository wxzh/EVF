package typed;

import java.util.function.Function;

import typed.termalg.shared.TermAlgQueryWithCtx;

public interface IsVarUsed<Term, Ty> extends TermAlgQueryWithCtx<Integer, Boolean, Term, Ty>, varapp.IsVarUsed<Term> {
  @Override default Function<Integer,Boolean> TmAbs(String x, Ty ty, Term t) {
    return c -> visitTerm(t).apply(c+1);
  }
}
