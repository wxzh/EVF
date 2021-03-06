package moreextension;

public interface Eval1<Term, Ty, Bind> extends GTermAlg<Term, Ty, Term>, extension.Eval1<Term, Bind> {
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
	@Override TermAlg<Term, Ty> alg();

	default Term TmAscribe(Term t, Ty ty) {
		return isVal(t) ? t : alg().TmAscribe(visitTerm(t), ty);
	}

	default Term TmFix(Term t) {
		return alg().TmFix(visitTerm(t));
	}

	default Term TmUnit() {
	  return noRuleApplies();
	}

	default Term TmInert(Ty p1) {
	  return noRuleApplies();
	}
}
