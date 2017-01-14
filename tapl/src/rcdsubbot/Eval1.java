package rcdsubbot;

import library.Zero;
import rcdsubbot.termalg.external.TermAlgMatcher;
import rcdsubbot.termalg.shared.GTermAlg;
import rcdsubbot.termalg.shared.TermAlgDefault;

public interface Eval1<Term, Ty> extends TermAlgDefault<Term, Ty, Term>, typed.Eval1<Term, Ty>, record.Eval1<Term> {
	@Override TermShiftAndSubst<Term, Ty> termShiftAndSubst();
	@Override IsVal<Term, Ty> isVal();
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();

	@Override
	default Zero<Term> m() {
		return typed.Eval1.super.m();
	}
}
