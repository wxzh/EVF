package benchmark.listalg.external;

public interface ListAlgApplier<IList, O> extends benchmark.listalg.shared.GListAlg<IList, O> {
	ListAlgMapper<IList, O> mapper();

	default O Nil() {
		return mapper().NilMapper().get();
	}

	default O Cons(java.lang.Integer p1, IList p2) {
		return mapper().ConsMapper().apply(p1).apply(p2);
	}

	default O Link(boolean p1, IList p2) {
		return mapper().LinkMapper().apply(p1).apply(p2);
	}
}