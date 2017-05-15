package pkg.alg.shared;

public interface AlgTransformWithCtx<O, Exp> extends pkg.alg.shared.GAlg<Exp, java.util.function.Function<O, Exp>> {
	pkg.Alg<Exp> alg();

	default java.util.function.Function<O, Exp> Add(Exp p1, Exp p2) {
		return c -> alg().Add(visitExp(p1).apply(c), visitExp(p2).apply(c));
	}

	default java.util.function.Function<O, Exp> Lit(int p1) {
		return c -> alg().Lit(p1);
	}
}