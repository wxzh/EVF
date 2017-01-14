package untyped;

import untyped.termalg.external.TermAlgMatcher;
import untyped.termalg.shared.GTermAlg;
import untyped.termalg.shared.TermAlgDefault;

public interface Eval1<Term> extends TermAlgDefault<Term, Term>, varapp.Eval1<Term> {
	@Override GTermAlg<Term, Term> alg();
	@Override TermShiftAndSubst<Term> termShiftAndSubst();
	@Override IsVal<Term> isVal();
	@Override TermAlgMatcher<Term, Term> matcher();

	@Override default Term TmApp(Term t1, Term t2) {
		return matcher()
				.TmAbs(x -> t -> isVal().visitTerm(t2) ? termShiftAndSubst().termSubstTop(t2, t)
						: (isVal().visitTerm(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2)))
				.otherwise(
						() -> isVal().visitTerm(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2))
				.visitTerm(t1);
	}
}