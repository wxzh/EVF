package pkg.alg.shared;

public interface AlgDefault<Exp, O> extends pkg.alg.shared.GAlg<Exp, O> {
	library.Zero<O> m();

	default O Add(Exp p1, Exp p2) {
		return m().empty();
	}

	default O Lit(int p1) {
		return m().empty();
	}
}