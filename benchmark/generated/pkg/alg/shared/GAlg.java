package pkg.alg.shared;

public interface GAlg<Exp, OExp> {
	OExp Add(Exp p1, Exp p2);
	OExp Lit(int p1);
	OExp visitExp(Exp e);
}