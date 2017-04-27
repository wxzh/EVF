package benchmark.listalg.shared;

public interface ListAlgTransformWithCtx<O, List> extends benchmark.listalg.shared.GListAlg<List, java.util.function.Function<O, List>> {
	benchmark.listalg.shared.GListAlg<List, List> alg();

	default java.util.function.Function<O, List> Nil() {
		return c -> alg().Nil();
	}

	default java.util.function.Function<O, List> Cons(java.lang.Integer p1, List p2) {
		return c -> alg().Cons(p1, visitList(p2).apply(c));
	}

	default java.util.function.Function<O, List> Link(boolean p1, List p2) {
		return c -> alg().Link(p1, visitList(p2).apply(c));
	}
}