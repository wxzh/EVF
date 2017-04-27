package benchmark.listalg.external;

public class ListAlgFactory implements ListAlgVisitor<List> {
	public List Nil() {
		return new List() {
			public <OList> OList accept(ListAlgVisitor<OList> v) {
				return v.Nil();
			}
		};
	}

	public List Cons(java.lang.Integer p1, List p2) {
		return new List() {
			public <OList> OList accept(ListAlgVisitor<OList> v) {
				return v.Cons(p1, p2);
			}
		};
	}

	public List Link(boolean p1, List p2) {
		return new List() {
			public <OList> OList accept(ListAlgVisitor<OList> v) {
				return v.Link(p1, p2);
			}
		};
	}
}