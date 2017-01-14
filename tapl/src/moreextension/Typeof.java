package moreextension;

import moreextension.termalg.shared.GTermAlg;
import moreextension.tyalg.external.TyAlgMatcher;
import moreextension.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, extension.Typeof<Term, Ty, Bind> {
	@Override TyEqv<Ty> tyEqv();
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
					.TyArr(ty1 -> ty2 -> tyEqv().visitTy(ty1).tyEqv(ty2) ? ty2 : m().empty().typeof(ctx))
					.otherwise(() -> m().empty().typeof(ctx))
					.visitTy(tyT);
		};
	}

	@Override default ITypeof<Ty, Bind> TmAscribe(Term t, Ty ty) {
		return ctx -> {
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyEqv().visitTy(tyT).tyEqv(ty) ? ty : m().empty().typeof(ctx);
		};
	}
}