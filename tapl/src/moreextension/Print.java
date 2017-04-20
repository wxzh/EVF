package moreextension;

import moreextension.termalg.external.TermAlgMatcher;
import moreextension.termalg.shared.GTermAlg;
import utils.Context;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, extension.Print<Term, Bind> {
	@Override TermAlgMatcher<Term, Ty, String> matcher();
	String printTy(Ty ty, Context<Bind> ctx);

	@Override default IPrint<Bind> TmInert(Ty ty) {
		return ctx -> "inert[" + printTy(ty, ctx) + "]";
	}

	@Override default IPrint<Bind> TmFix(Term t) {
		return ctx -> "fix " + visitTerm(t).print(ctx);
	}

	@Override default IPrint<Bind> TmAscribe(Term t, Ty ty) {
		return ctx -> visitTerm(t).print(ctx) + " as " + printTy(ty, ctx);
	}

	@Override default IPrint<Bind> TmUnit() {
		return ctx -> "Unit";
	}
}