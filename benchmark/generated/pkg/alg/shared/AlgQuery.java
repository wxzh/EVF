package pkg.alg.shared;

public interface AlgQuery<Exp, O> extends pkg.alg.shared.GAlg<Exp, O> {
	library.Monoid<O> m();

	default O Add(Exp p1, Exp p2) {
		return java.util.stream.Stream.of(visitExp(p1), visitExp(p2)).reduce(m().empty(), m()::join);
	}

	default O Lit(int p1) {
		return m().empty();
	}
}
