package benchmark.listalg.internal;

public interface List {
	<OList> OList accept(ListAlgVisitor<OList> v);
}
