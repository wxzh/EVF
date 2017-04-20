package fullsub;

import fullsub.termalg.shared.GTermAlg;
import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>,
    moreextension.Typeof<Term, Ty, Bind>, top.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();
	Ty join(Ty ty1, Ty ty2);

	@Override default ITypeof<Ty, Bind> TmIf(Term t1, Term t2, Term t3) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			if (subtype(ty1, tyAlg().TyBool())) {
				Ty ty2 = visitTerm(t2).typeof(ctx);
				Ty ty3 = visitTerm(t3).typeof(ctx);
				return join(ty2, ty3);
			}
			return typeError("guard of conditional not a boolean");
		};
	}

	@Override default ITypeof<Ty, Bind> TmFix(Term t) {
		return ctx -> {
			Ty ty = visitTerm(t).typeof(ctx);
			return tyMatcher()
					.TyArr(ty1 -> ty2 -> subtype(ty1, ty2) ? ty2 : typeError("result of body not compatible with domain"))
					.otherwise(() -> typeError("arrow type expected"))
					.visitTy(ty);
		};
	}

	@Override default ITypeof<Ty, Bind> TmAscribe(Term t, Ty ty) {
		return ctx -> subtype(visitTerm(t).typeof(ctx), ty) ? ty : typeError("body of as-term does not have the expected type");
	}

	@Override default ITypeof<Ty, Bind> TmSucc(Term t) {
		Ty tyNat = tyAlg().TyNat();
		return ctx -> subtype(visitTerm(t).typeof(ctx), tyNat) ? tyNat : typeError("argument of succ is not a number");
	}

	@Override default ITypeof<Ty, Bind> TmPred(Term t) {
		Ty tyNat = tyAlg().TyNat();
		return ctx -> subtype(visitTerm(t).typeof(ctx), tyNat) ? tyNat : typeError("argument of pred is not a number");
	}

	@Override default ITypeof<Ty, Bind> TmIsZero(Term t) {
		return ctx -> subtype(visitTerm(t).typeof(ctx), tyAlg().TyNat()) ? tyAlg().TyBool() : typeError("argument of iszero is not a number");
	}
}