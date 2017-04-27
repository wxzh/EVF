package benchmark.listalg.external;

public interface ListAlgMatcher<IList, O> extends ListAlgMapper<IList, O> {
	ListAlgMatcher<IList, O> Nil(java.util.function.Supplier<O> Nil);
	ListAlgMatcher<IList, O> Cons(java.util.function.Function<java.lang.Integer, java.util.function.Function<IList, O>> Cons);
	ListAlgMatcher<IList, O> Link(java.util.function.Function<Boolean, java.util.function.Function<IList, O>> Link);
	benchmark.listalg.shared.GListAlg<IList, O> otherwise(java.util.function.Supplier<O> Otherwise);
}
