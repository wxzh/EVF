package fullref;

import fullref.termalg.external.TermAlgMatcher;
import fullref.termalg.shared.GTermAlg;

public interface Eval1<Term, Ty, Bind> extends GTermAlg<Term, Ty, Term>, fullsimple.Eval1<Term, Ty, Bind> {
	Store<Term> store();
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();

	default Term TmRef(Term t) {
		return isVal(t) ? alg().TmLoc(store().extend(t)) : alg().TmRef(visitTerm(t));
	}

	default Term TmDeref(Term t) {
		return isVal(t) ? matcher()
				.TmLoc(l -> store().lookup(l))
				.otherwise(() -> noRuleApplies())
				.visitTerm(t) : alg().TmRef(visitTerm(t));
	}

	default Term TmAssign(Term t1, Term t2) {
		if (!isVal(t1)) return alg().TmAssign(visitTerm(t1), t2);
		if (!isVal(t2)) return alg().TmAssign(t1, visitTerm(t2));
		return matcher()
		    .TmLoc(l -> { store().update(l, t2); return alg().TmUnit(); })
		    .otherwise(() -> noRuleApplies())
		    .visitTerm(t1);
	}

	default Term TmLoc(int p1) {
	  return noRuleApplies();
	}
}