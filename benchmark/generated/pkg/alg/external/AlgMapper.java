package pkg.alg.external;

public interface AlgMapper<IExp, O> {
	java.util.function.Function<IExp, java.util.function.Function<IExp, O>> AddMapper();
	java.util.function.Function<Integer, O> LitMapper();}