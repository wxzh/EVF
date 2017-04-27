package benchmark.listalg.shared;

public interface ListAlgQuery<List, O> extends benchmark.listalg.shared.GListAlg<List, O> {
	library.Monoid<O> m();

	default O Nil() {
		return m().empty();
	}

	default O Cons(java.lang.Integer p1, List p2) {
		return visitList(p2);
	}

	default O Link(boolean p1, List p2) {
		return visitList(p2);
	}
}
