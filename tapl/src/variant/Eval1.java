package variant;

import java.util.List;

import library.Tuple3;
import variant.termalg.external.TermAlgMatcher;
import variant.termalg.shared.GTermAlg;
import variant.termalg.shared.TermAlgDefault;

public interface Eval1<Term, Ty> extends TermAlgDefault<Term, Ty, Term>, typed.Eval1<Term, Ty> {
	@Override GTermAlg<Term, Ty, Term> alg();
	@Override IsVal<Term, Ty> isVal();
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
	@Override TermShiftAndSubst<Term, Ty> termShiftAndSubst();

	@Override default Term TmTag(String label, Term t, Ty ty) {
		return alg().TmTag(label, visitTerm(t), ty);
	}

	default Term TmCase(Term t, List<Tuple3<String, String, Term>> branches) {
		return matcher()
				.TmTag(label -> ty -> t1 -> isVal().visitTerm(t) ? branches.stream().filter(b -> b._1.equals(label))
						.findFirst().map(b -> termShiftAndSubst().termSubstTop(t, b._3)).orElseGet(() -> m().empty())
						: alg().TmCase(visitTerm(t), branches))
				.otherwise(() -> alg().TmCase(visitTerm(t), branches))
				.visitTerm(t);
	}
}