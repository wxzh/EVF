package fulluntyped;

import fulluntyped.bindingalg.external.BindingAlgMatcher;
import fulluntyped.termalg.external.TermAlgMatcher;
import fulluntyped.termalg.shared.GTermAlg;
import utils.Context;

public interface Eval1<Term, Bind> extends GTermAlg<Term, Term>, untyped.Eval1<Term>, extension.Eval1<Term, Bind> {
	@Override TermAlgMatcher<Term, Term> matcher();
	@Override GTermAlg<Term, Term> alg();
	BindingAlgMatcher<Bind, Term, Term> bindMatcher();
	Context<Bind> ctx();

	@Override default Term TmVar(int x, int n) {
		return bindMatcher()
				.TmAbbBind(t -> t)
				.otherwise(() -> noRuleApplies())
				.visitBind(ctx().getBinding(x));
	}
}
