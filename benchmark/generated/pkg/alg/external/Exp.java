package pkg.alg.external;

public interface Exp {
	<OExp> OExp accept(AlgVisitor<OExp> v);
}
