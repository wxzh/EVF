package pkg.alg.internal;

public interface AlgVisitor<OExp> extends pkg.alg.shared.GAlg<OExp, OExp> {
	default OExp visitExp(OExp e) {
		return e;
	}
}
