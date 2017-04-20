package fullsimple;

import fullsimple.termalg.external.TermAlgMatcher;
import fullsimple.termalg.shared.GTermAlg;

public interface Eval1<Term, Ty, Bind> extends GTermAlg<Term, Ty, Term>, moreextension.Eval1<Term, Ty, Bind>, variant.Eval1<Term, Ty>, typed.Eval1<Term, Ty> {
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
}