package pkg.alg.external;

public class AlgFactory implements AlgVisitor<Exp> {
	public Exp Add(Exp p1, Exp p2) {
		return new Exp() {
			public <OExp> OExp accept(AlgVisitor<OExp> v) {
				return v.Add(p1, p2);
			}
		};
	}

	public Exp Lit(int p1) {
		return new Exp() {
			public <OExp> OExp accept(AlgVisitor<OExp> v) {
				return v.Lit(p1);
			}
		};
	}
}