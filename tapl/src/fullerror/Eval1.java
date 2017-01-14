package fullerror;

import fullerror.termalg.external.TermAlgMatcher;
import fullerror.termalg.shared.GTermAlg;

public interface Eval1<Term, Ty> extends simplebool.Eval1<Term, Ty> {
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
	@Override IsVal<Term, Ty> isVal();
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermShiftAndSubst<Term, Ty> termShiftAndSubst();

	@Override default Term TmIf(Term t1, Term t2, Term t3) {
		return matcher()
				.TmError(() -> t1)
				.otherwise(() -> simplebool.Eval1.super.TmIf(t1, t2, t3))
				.visitTerm(t1);
	}

	@Override default Term TmApp(Term t1, Term t2) {
		return matcher()
				.TmError(() -> alg().TmError())
				.otherwise(() -> isVal().visitTerm(t1) ? matcher()
						.TmError(() -> alg().TmError())
						.otherwise(() -> simplebool.Eval1.super.TmApp(t1, t2))
						.visitTerm(t2) : simplebool.Eval1.super.TmApp(t1, t2))
				.visitTerm(t1);
	}
}