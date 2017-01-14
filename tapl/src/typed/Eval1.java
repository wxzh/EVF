package typed;

import typed.termalg.external.TermAlgMatcher;
import typed.termalg.shared.GTermAlg;
import typed.termalg.shared.TermAlgDefault;

public interface Eval1<Term, Ty> extends TermAlgDefault<Term, Ty, Term>, varapp.Eval1<Term> {
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermShiftAndSubst<Term, Ty> termShiftAndSubst();
	@Override IsVal<Term, Ty> isVal();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();

	@Override default Term TmApp(Term t1, Term t2) {
		return matcher()
				.TmAbs(x -> ty -> t -> isVal().visitTerm(t2) ? termShiftAndSubst().termSubstTop(t2, t)
						: (isVal().visitTerm(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2)))
				.otherwise(
						() -> isVal().visitTerm(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2))
				.visitTerm(t1);
	}
}