package fullsimple;

public interface Eval1<Term, Ty, Bind> extends GTermAlg<Term, Ty, Term>, moreextension.Eval1<Term, Ty, Bind>, variant.Eval1<Term, Ty>, typed.Eval1<Term, Ty> {
	@Override TermAlg<Term, Ty> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
}