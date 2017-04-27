package pkg.alg.internal;

public interface Exp {
	<OExp> OExp accept(AlgVisitor<OExp> v);
}
