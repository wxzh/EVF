package simplebool;

import library.Zero;
import simplebool.termalg.external.TermAlgMatcher;
import simplebool.termalg.shared.GTermAlg;
import simplebool.termalg.shared.TermAlgDefault;

public interface Eval1<Term, Ty> extends TermAlgDefault<Term, Ty, Term>, bool.Eval1<Term>, typed.Eval1<Term, Ty> {
	@Override TermShiftAndSubst<Term, Ty> termShiftAndSubst();
	@Override IsVal<Term, Ty> isVal();
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();

	@Override default Zero<Term> m() {
		return bool.Eval1.super.m();
	}
}
