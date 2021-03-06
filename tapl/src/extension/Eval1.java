package extension;

public interface Eval1<Term, Bind> extends GTermAlg<Term, Term>,
    arith.Eval1<Term>, record.Eval1<Term>, floatstring.Eval1<Term>, let.Eval1<Term, Bind> {
	@Override TermAlgMatcher<Term, Term> matcher();
	@Override TermAlg<Term> alg();
}