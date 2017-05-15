package fullsub;

public interface Eval1<Term, Ty, Bind> extends GTermAlg<Term, Ty, Term>, moreextension.Eval1<Term, Ty, Bind>, typed.Eval1<Term, Ty> {
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
	@Override TermAlg<Term, Ty> alg();
}
