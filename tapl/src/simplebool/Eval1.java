package simplebool;

public interface Eval1<Term, Ty> extends GTermAlg<Term, Ty, Term>, bool.Eval1<Term>, typed.Eval1<Term, Ty> {
	@Override TermAlg<Term, Ty> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
}
