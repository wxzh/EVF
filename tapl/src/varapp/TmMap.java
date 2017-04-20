package varapp;

import java.util.function.Function;

import utils.TmMapCtx;
import varapp.termalg.shared.TermAlgTransformWithCtx;

public interface TmMap<Term> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term> {
  default Function<TmMapCtx<Term>, Term> TmVar(int x, int n) {
    return ctx -> ctx.tmMap(x, n);
  }

	default Term termShiftAbove(int d, int c, Term t) {
	  return visitTerm(t).apply(new TmMapCtx<>((c1,x,n) -> x >= c1 ? alg().TmVar(x + d, n + d) : alg().TmVar(x, n + d), c));
	}

	default Term termShift(int d, Term t) {
		return termShiftAbove(d, 0, t);
	}

	default Term termSubst(int j, Term s, Term t) {
	  return visitTerm(t).apply(new TmMapCtx<>((c,x,n) -> x==c ? termShift(c, s) : alg().TmVar(x,n), j));
	}

	default Term termSubstTop(Term s, Term t) {
		return termShift(-1, termSubst(0, termShift(1, s), t));
	}
}