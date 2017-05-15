package pkg.alg.shared;

public interface AlgTransform<Exp> extends pkg.alg.shared.GAlg<Exp, Exp> {
	pkg.Alg<Exp> alg();

	default Exp Add(Exp p1, Exp p2) {
		return alg().Add(visitExp(p1), visitExp(p2));
	}

	default Exp Lit(int p1) {
		return alg().Lit(p1);
	}
}