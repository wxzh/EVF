package rcdsubbot;

import rcdsubbot.termalg.external.TermAlgMatcher;
import rcdsubbot.termalg.shared.GTermAlg;

public interface Eval1<Term, Ty> extends GTermAlg<Term, Ty, Term>, typed.Eval1<Term, Ty>, record.Eval1<Term> {
	@Override TermAlg<Term, Ty> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
}
