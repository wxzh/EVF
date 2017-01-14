package fulluntyped;

import fulluntyped.bindingalg.external.BindingAlgMatcher;
import fulluntyped.termalg.external.TermAlgMatcher;
import fulluntyped.termalg.shared.GTermAlg;
import fulluntyped.termalg.shared.TermAlgDefault;
import utils.Context;

public interface Eval1<Term, Bind> extends TermAlgDefault<Term, Term>, untyped.Eval1<Term>, extension.Eval1<Term, Bind> {
	@Override IsVal<Term> isVal();
	@Override TermAlgMatcher<Term, Term> matcher();
	@Override GTermAlg<Term, Term> alg();
	@Override TermShiftAndSubst<Term> termShiftAndSubst();
	BindingAlgMatcher<Bind, Term, Term> bindMatcher();
	Context<Bind> ctx();

	default Term TmVar(int x, int n) {
		return bindMatcher()
				.TmAbbBind(t -> t)
				.otherwise(() -> m().empty())
				.visitBind(ctx().getBinding(x));
	}
}
