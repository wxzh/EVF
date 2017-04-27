package benchmark.listalg.shared;

public interface ListAlgDefault<List, O> extends benchmark.listalg.shared.GListAlg<List, O> {
	library.Zero<O> m();

	default O Nil() {
		return m().empty();
	}

	default O Cons(java.lang.Integer p1, List p2) {
		return m().empty();
	}

	default O Link(boolean p1, List p2) {
		return m().empty();
	}
}