package benchmark.listalg.shared;

public interface ListAlgQueryWithCtx<I, O, List> extends benchmark.listalg.shared.GListAlg<List, java.util.function.Function<I, O>> {
	library.Monoid<O> m();

	default java.util.function.Function<I, O> Nil() {
		return c -> m().empty();
	}

	default java.util.function.Function<I, O> Cons(java.lang.Integer p1, List p2) {
		return c -> visitList(p2).apply(c);
	}

	default java.util.function.Function<I, O> Link(boolean p1, List p2) {
		return c -> visitList(p2).apply(c);
	}
}