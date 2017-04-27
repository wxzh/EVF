package benchmark.listalg.shared;

public interface G_ListAlgQuery<List, OList> extends benchmark.listalg.shared.GListAlg<List, OList> {
	library.Monoid<OList> mList();

	default OList Nil() {
		return mList().empty();
	}

	default OList Cons(java.lang.Integer p1, List p2) {
		return visitList(p2);
	}

	default OList Link(boolean p1, List p2) {
		return visitList(p2);
	}
}
