package pkg.alg.internal;

public class AlgFactory implements pkg.Alg<Exp> {
	public Exp Add(Exp p1, Exp p2) {
		return new Exp() {
			public <OExp> OExp accept(AlgVisitor<OExp> v) {
				return v.Add(p1.accept(v), p2.accept(v));
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