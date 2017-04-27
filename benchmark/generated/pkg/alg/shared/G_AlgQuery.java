package pkg.alg.shared;

public interface G_AlgQuery<Exp, OExp> extends pkg.alg.shared.GAlg<Exp, OExp> {
	library.Monoid<OExp> mExp();

	default OExp Add(Exp p1, Exp p2) {
		return java.util.stream.Stream.of(visitExp(p1), visitExp(p2)).reduce(mExp().empty(), mExp()::join);
	}

	default OExp Lit(int p1) {
		return mExp().empty();
	}
}
