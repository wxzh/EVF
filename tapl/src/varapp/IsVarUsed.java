package varapp;

import java.util.function.Function;

import library.Monoid;
import utils.OrMonoid;
import varapp.termalg.shared.TermAlgQueryWithCtx;

public interface IsVarUsed<Term> extends TermAlgQueryWithCtx<Integer, Boolean, Term> {
  @Override default Monoid<Boolean> m() {
    return new OrMonoid();
  }
  @Override default Function<Integer, Boolean> TmVar(int x, int n) {
    return c -> x == c;
  }
}
