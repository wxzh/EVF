package benchmark.listalg.shared;

public interface GListAlg<List, OList> {
	OList Nil();
	OList Cons(java.lang.Integer p1, List p2);
	OList Link(boolean p1, List p2);
	OList visitList(List e);
}