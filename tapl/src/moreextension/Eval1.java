package moreextension;

import moreextension.termalg.external.TermAlgMatcher;
import moreextension.termalg.shared.GTermAlg;
import moreextension.termalg.shared.TermAlgDefault;

public interface Eval1<Term, Ty, Bind> extends TermAlgDefault<Term, Ty, Term>, extension.Eval1<Term, Bind> {
	@Override IsVal<Term, Ty> isVal();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermShiftAndSubst<Term> termShiftAndSubst();

	@Override default Term TmAscribe(Term t, Ty ty) {
		return isVal().visitTerm(t) ? t : alg().TmAscribe(visitTerm(t), ty);
	}

	@Override default Term TmFix(Term t) {
		return alg().TmFix(visitTerm(t));
	}
}
