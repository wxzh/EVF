package benchmark.listalg.external;

public interface ListAlgMapper<IList, O> {
	java.util.function.Supplier<O> NilMapper();
	java.util.function.Function<java.lang.Integer, java.util.function.Function<IList, O>> ConsMapper();
	java.util.function.Function<Boolean, java.util.function.Function<IList, O>> LinkMapper();}