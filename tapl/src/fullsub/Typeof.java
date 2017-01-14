package fullsub;

import fullsub.termalg.shared.GTermAlg;
import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, moreextension.Typeof<Term, Ty, Bind>, top.Typeof<Term, Ty, Bind> {
	@Override TyEqv<Ty> tyEqv();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();
	Subtype<Ty> subtype();
	JoinMeet<Ty> joinMeet();

	@Override default ITypeof<Ty, Bind> TmIf(Term t1, Term t2, Term t3) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			if (subtype().subtype(ty1, tyAlg().TyBool())) {
				Ty ty2 = visitTerm(t2).typeof(ctx);
				Ty ty3 = visitTerm(t3).typeof(ctx);
				return joinMeet().join(ty2, ty3);
			}
			return m().empty().typeof(ctx);
		};
	}

	@Override default ITypeof<Ty, Bind> TmFix(Term t) {
		return ctx -> {
			Ty ty = visitTerm(t).typeof(ctx);
			return tyMatcher()
					.TyArr(ty1 -> ty2 -> subtype().subtype(ty1, ty2) ? ty2 : m().empty().typeof(ctx))
					.otherwise(() -> m().empty().typeof(ctx))
					.visitTy(ty);
		};
	}

	@Override default ITypeof<Ty, Bind> TmTimesFloat(Term t1, Term t2) {
		Ty tyFloat = tyAlg().TyFloat();
		return ctx -> subtype().subtype(visitTerm(t1).typeof(ctx), tyFloat) &&
				subtype().subtype(visitTerm(t2).typeof(ctx), tyFloat) ? tyFloat : m().empty().typeof(ctx);
	}

	@Override default ITypeof<Ty, Bind> TmAscribe(Term t, Ty ty) {
		return ctx -> subtype().subtype(visitTerm(t).typeof(ctx), ty) ? ty : m().empty().typeof(ctx);
	}

	@Override default ITypeof<Ty, Bind> TmSucc(Term t) {
		Ty tyNat = tyAlg().TyNat();
		return ctx -> subtype().subtype(visitTerm(t).typeof(ctx), tyNat) ? tyNat : m().empty().typeof(ctx);
	}

	@Override default ITypeof<Ty, Bind> TmPred(Term t) {
		Ty tyNat = tyAlg().TyNat();
		return ctx -> subtype().subtype(visitTerm(t).typeof(ctx), tyNat) ? tyNat : m().empty().typeof(ctx);
	}

	@Override default ITypeof<Ty, Bind> TmIsZero(Term t) {
		return ctx -> subtype().subtype(visitTerm(t).typeof(ctx), tyAlg().TyNat()) ? tyAlg().TyBool() : m().empty().typeof(ctx);
	}
}