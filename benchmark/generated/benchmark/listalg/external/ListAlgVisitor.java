package benchmark.listalg.external;

public interface ListAlgVisitor<OList> extends benchmark.listalg.shared.GListAlg<List, OList> {
	default OList visitList(List e) {
		return e.accept(this);
	}
}
