package typed;

import java.util.function.Function;

import typed.termalg.shared.TermAlgTransformWithCtx;
import utils.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, varapp.TmMap<Term> {
	default Function<TmMapCtx<Term>, Term> TmAbs(String x, Ty ty, Term t) {
		return ctx -> alg().TmAbs(x, ty, visitTerm(t).apply(new TmMapCtx<>(ctx.onvar, ctx.c + 1)));
	}
}