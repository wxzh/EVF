package varapp;

import java.util.function.Function;

import varapp.termalg.shared.TermAlgTransformWithCtx;

public interface TmMap<Term> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term> {
	default Function<TmMapCtx<Term>, Term> TmVar(int x, int n) {
		return ctx -> ctx.tmMap(x, n);
	}
}