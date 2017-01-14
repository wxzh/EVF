package moreextension;

import moreextension.termalg.external.TermAlgMatcher;
import moreextension.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, extension.Print<Term, Bind> {
	PrintTy<Ty, Bind> printTy();
	@Override TermAlgMatcher<Term, Ty, String> matcher();

	@Override default IPrint<Bind> TmInert(Ty ty) {
		return ctx -> "inert[" + printTy().visitTy(ty).print(ctx) + "]";
	}

	@Override default IPrint<Bind> TmFix(Term t) {
		return ctx -> "fix " + visitTerm(t).print(ctx);
	}

	@Override default IPrint<Bind> TmAscribe(Term t, Ty ty) {
		return ctx -> visitTerm(t).print(ctx) + " as " + printTy().visitTy(ty).print(ctx);
	}

	@Override default IPrint<Bind> TmUnit() {
		return ctx -> "Unit";
	}
}