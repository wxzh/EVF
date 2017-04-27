package benchmark.listalg.shared;

public interface ListAlgTransform<List> extends benchmark.listalg.shared.GListAlg<List, List> {
	benchmark.listalg.shared.GListAlg<List, List> alg();

	default List Nil() {
		return alg().Nil();
	}

	default List Cons(java.lang.Integer p1, List p2) {
		return alg().Cons(p1, visitList(p2));
	}

	default List Link(boolean p1, List p2) {
		return alg().Link(p1, visitList(p2));
	}
}