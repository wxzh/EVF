package benchmark.listalg.external;

interface ListAlgApplierWithVisitor<O> extends ListAlgApplier<List, O>, ListAlgVisitor<O> {
}

public class ListAlgMatcherImpl<O> implements ListAlgMatcher<List, O> {
	private java.util.function.Supplier<O> Nil = null;
	private java.util.function.Function<java.lang.Integer, java.util.function.Function<List, O>> Cons = null;
	private java.util.function.Function<Boolean, java.util.function.Function<List, O>> Link = null;

	public java.util.function.Supplier<O> NilMapper() {
		return Nil;
	}

	public java.util.function.Function<java.lang.Integer, java.util.function.Function<List, O>> ConsMapper() {
		return Cons;
	}

	public java.util.function.Function<Boolean, java.util.function.Function<List, O>> LinkMapper() {
		return Link;
	}
	public ListAlgMatcher<List, O> Nil(java.util.function.Supplier<O> Nil) {
		this.Nil = Nil;
		return this;
	}

	public ListAlgMatcher<List, O> Cons(java.util.function.Function<java.lang.Integer, java.util.function.Function<List, O>> Cons) {
		this.Cons = Cons;
		return this;
	}

	public ListAlgMatcher<List, O> Link(java.util.function.Function<Boolean, java.util.function.Function<List, O>> Link) {
		this.Link = Link;
		return this;
	}
	public benchmark.listalg.shared.GListAlg<List, O> otherwise(java.util.function.Supplier<O> otherwise) {
		if (Nil == null) Nil = () -> otherwise.get();
		if (Cons == null) Cons = p1 -> p2 -> otherwise.get();
		if (Link == null) Link = p1 -> p2 -> otherwise.get();

		return new ListAlgApplierWithVisitor<O>() {
			public ListAlgMapper<List, O> mapper() {
				return ListAlgMatcherImpl.this;
			}
		};
	}
}
