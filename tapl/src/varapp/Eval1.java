package varapp;

import library.Zero;
import utils.NoRuleApplies;
import varapp.termalg.external.TermAlgMatcher;
import varapp.termalg.shared.GTermAlg;
import varapp.termalg.shared.TermAlgDefault;

public interface Eval1<Term> extends TermAlgDefault<Term, Term>  {
	GTermAlg<Term, Term> alg();
	TermShiftAndSubst<Term> termShiftAndSubst();
	IsVal<Term> isVal();
	TermAlgMatcher<Term, Term> matcher();

	@Override default Zero<Term> m() {
		throw new NoRuleApplies();
	}

}