package fullref;

import fullref.termalg.external.TermAlgMatcher;
import fullref.termalg.shared.GTermAlg;
import fullref.termalg.shared.TermAlgDefault;

public interface Eval1<Term, Ty, Bind> extends TermAlgDefault<Term, Ty, Term>, fullsimple.Eval1<Term, Ty, Bind> {
	Store<Term> store();
	@Override IsVal<Term, Ty> isVal();
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();

	@Override default Term TmRef(Term t) {
		return isVal().visitTerm(t) ? alg().TmLoc(store().extend(t)) : alg().TmRef(visitTerm(t));
	}

	@Override default Term TmDeref(Term t) {
		return isVal().visitTerm(t) ? matcher()
				.TmLoc(l -> store().lookup(l))
				.otherwise(() -> m().empty())
				.visitTerm(t) : alg().TmRef(visitTerm(t));
	}

	@Override default Term TmAssign(Term t1, Term t2) {
		if (!isVal().visitTerm(t1)) return alg().TmAssign(visitTerm(t1), t2);
		if (!isVal().visitTerm(t2)) return alg().TmAssign(t1, visitTerm(t2));
		return matcher().TmLoc(l -> {
			store().update(l, t2);
			return alg().TmUnit();
		}).otherwise(() -> m().empty()).visitTerm(t1);
	}
}