package untyped;

import java.util.function.Function;

import untyped.termalg.shared.TermAlgTransformWithCtx;
import utils.TmMapCtx;

public interface TmMap<Term> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term>, varapp.TmMap<Term> {
	default Function<TmMapCtx<Term>, Term> TmAbs(String x, Term t) {
		return ctx -> alg().TmAbs(x, visitTerm(t).apply(new TmMapCtx<>(ctx.onvar, ctx.c + 1)));
	}
}