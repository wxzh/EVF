package extension;

import extension.termalg.external.TermAlgMatcher;
import extension.termalg.shared.GTermAlg;
import extension.termalg.shared.TermAlgDefault;
import library.Zero;

public interface Eval1<Term, Bind> extends TermAlgDefault<Term, Term>, arith.Eval1<Term>, varapp.Eval1<Term>, record.Eval1<Term> {
	@Override IsVal<Term> isVal();
	@Override TermAlgMatcher<Term, Term> matcher();
	@Override GTermAlg<Term, Term> alg();

	@Override default Term TmTimesFloat(Term t1, Term t2) {
		return matcher()
				.TmFloat(f1 -> matcher().TmFloat(f2 -> alg().TmFloat(f1 * f2))
						.otherwise(() -> alg().TmTimesFloat(t1, visitTerm(t2)))
						.visitTerm(t2))
				.otherwise(() -> alg().TmTimesFloat(visitTerm(t1), t2))
				.visitTerm(t1);
	}

	@Override default Term TmLet(String x, Term t1, Term t2) {
		return isVal().visitTerm(t1) ? termShiftAndSubst().termSubstTop(t1, t2) : alg().TmLet(x, visitTerm(t1), t2);
	}

	@Override default Zero<Term> m() {
		return varapp.Eval1.super.m();
	}
}