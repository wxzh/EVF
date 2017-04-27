package pkg.alg.shared;

public interface AlgQueryWithCtx<I, O, Exp> extends pkg.alg.shared.GAlg<Exp, java.util.function.Function<I, O>> {
	library.Monoid<O> m();

	default java.util.function.Function<I, O> Add(Exp p1, Exp p2) {
		return c -> java.util.stream.Stream.of(visitExp(p1).apply(c), visitExp(p2).apply(c)).reduce(m().empty(), m()::join);
	}

	default java.util.function.Function<I, O> Lit(int p1) {
		return c -> m().empty();
	}
}