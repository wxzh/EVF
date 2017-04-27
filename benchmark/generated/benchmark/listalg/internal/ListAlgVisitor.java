package benchmark.listalg.internal;

public interface ListAlgVisitor<OList> extends benchmark.listalg.shared.GListAlg<OList, OList> {
	default OList visitList(OList e) {
		return e;
	}
}
