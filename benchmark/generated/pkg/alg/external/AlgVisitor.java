package pkg.alg.external;

public interface AlgVisitor<OExp> extends pkg.alg.shared.GAlg<Exp, OExp> {
	default OExp visitExp(Exp e) {
		return e.accept(this);
	}
}
