package pkg.alg.external;

public interface AlgMatcher<IExp, O> extends AlgMapper<IExp, O> {
	AlgMatcher<IExp, O> Add(java.util.function.Function<IExp, java.util.function.Function<IExp, O>> Add);
	AlgMatcher<IExp, O> Lit(java.util.function.Function<Integer, O> Lit);
	pkg.alg.shared.GAlg<IExp, O> otherwise(java.util.function.Supplier<O> Otherwise);
}
