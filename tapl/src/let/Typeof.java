package let;

import let.termalg.shared.GTermAlg;
import typed.BindingAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>> {
	BindingAlg<Bind,Ty> bindAlg();

	@Override default ITypeof<Ty, Bind> TmLet(String x, Term t1, Term t2) {
		return ctx -> {
			Ty tyT1 = visitTerm(t1).typeof(ctx);
			return visitTerm(t2).typeof(ctx.addBinding(x, bindAlg().VarBind(tyT1)));
		};
	}
}