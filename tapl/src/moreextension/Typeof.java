package moreextension;

import moreextension.termalg.shared.GTermAlg;
import moreextension.tyalg.external.TyAlgMatcher;
import moreextension.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, extension.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();

	@Override default ITypeof<Ty, Bind> TmUnit() {
		return ctx -> tyAlg().TyUnit();
	}

	@Override default ITypeof<Ty, Bind> TmInert(Ty ty) {
		return ctx -> ty;
	}

	@Override default ITypeof<Ty, Bind> TmFix(Term t) {
		return ctx -> {
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyMatcher()
					.TyArr(ty1 -> ty2 -> tyEqv(ty1,ty2) ? ty2 : typeError("result of body not compatible with domain"))
					.otherwise(() -> typeError("arrow type expected"))
					.visitTy(tyT);
		};
	}

	@Override default ITypeof<Ty, Bind> TmAscribe(Term t, Ty ty) {
		return ctx -> {
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyEqv(tyT,ty) ? ty : typeError("body of as-term does not have the expected type");
		};
	}
}