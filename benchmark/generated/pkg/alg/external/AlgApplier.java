package pkg.alg.external;

public interface AlgApplier<IExp, O> extends pkg.alg.shared.GAlg<IExp, O> {
	AlgMapper<IExp, O> mapper();

	default O Add(IExp p1, IExp p2) {
		return mapper().AddMapper().apply(p1).apply(p2);
	}

	default O Lit(int p1) {
		return mapper().LitMapper().apply(p1);
	}
}