package let;

import java.util.function.Function;

import let.termalg.shared.GTermAlg;
import utils.TmMapCtx;

public interface TmMap<Term> extends GTermAlg<Term, Function<TmMapCtx<Term>, Term>> {
  GTermAlg<Term,Term> alg();
	@Override default Function<TmMapCtx<Term>,Term> TmLet(String x, Term bind, Term body) {
		return ctx -> alg().TmLet(x, visitTerm(bind).apply(ctx), visitTerm(body)
		        .apply(new TmMapCtx<>(ctx.onvar, ctx.c + 1)));
	}
}
