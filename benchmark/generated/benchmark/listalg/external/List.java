package benchmark.listalg.external;

public interface List {
	<OList> OList accept(ListAlgVisitor<OList> v);
}
