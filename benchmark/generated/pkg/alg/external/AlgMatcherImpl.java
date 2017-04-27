package pkg.alg.external;

interface AlgApplierWithVisitor<O> extends AlgApplier<Exp, O>, AlgVisitor<O> {
}

public class AlgMatcherImpl<O> implements AlgMatcher<Exp, O> {
	private java.util.function.Function<Exp, java.util.function.Function<Exp, O>> Add = null;
	private java.util.function.Function<Integer, O> Lit = null;

	public java.util.function.Function<Exp, java.util.function.Function<Exp, O>> AddMapper() {
		return Add;
	}

	public java.util.function.Function<Integer, O> LitMapper() {
		return Lit;
	}
	public AlgMatcher<Exp, O> Add(java.util.function.Function<Exp, java.util.function.Function<Exp, O>> Add) {
		this.Add = Add;
		return this;
	}

	public AlgMatcher<Exp, O> Lit(java.util.function.Function<Integer, O> Lit) {
		this.Lit = Lit;
		return this;
	}
	public pkg.alg.shared.GAlg<Exp, O> otherwise(java.util.function.Supplier<O> otherwise) {
		if (Add == null) Add = p1 -> p2 -> otherwise.get();
		if (Lit == null) Lit = p1 -> otherwise.get();

		return new AlgApplierWithVisitor<O>() {
			public AlgMapper<Exp, O> mapper() {
				return AlgMatcherImpl.this;
			}
		};
	}
}
