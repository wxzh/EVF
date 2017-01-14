package fullref;

import fullref.termalg.shared.GTermAlg;
import fullref.tyalg.external.TyAlgMatcher;
import fullref.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, bot.Typeof<Term, Ty, Bind>, fullsub.Typeof<Term, Ty, Bind>, variant.Typeof<Term, Ty, Bind> {
	@Override TyEqv<Ty> tyEqv();
	@Override JoinMeet<Ty> joinMeet();
	@Override Subtype<Ty> subtype();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();

	@Override default ITypeof<Ty, Bind> TmRef(Term t) {
		return ctx -> tyAlg().TyRef(visitTerm(t).typeof(ctx));
	}

	@Override default ITypeof<Ty, Bind> TmLoc(int l) {
		return ctx -> m().empty().typeof(ctx);
	}

	@Override default ITypeof<Ty, Bind> TmDeref(Term t) {
		return ctx -> tyMatcher()
				.TyRef(ty -> ty)
				.TyBot(() -> tyAlg().TyBot())
				.TySource(ty -> ty)
				.otherwise(() -> m().empty().typeof(ctx))
				.visitTy(visitTerm(t).typeof(ctx));
	}

	@Override default ITypeof<Ty, Bind> TmAssign(Term t1, Term t2) {
		return ctx -> tyMatcher()
				.TyRef(ty1 -> subtype().subtype(visitTerm(t2).typeof(ctx), ty1) ? tyAlg().TyUnit() : m().empty().typeof(ctx))
				.TyBot(() -> {
					visitTerm(t2).typeof(ctx);
					return tyAlg().TyBot(); })
				.TySink(ty1 -> subtype().subtype(visitTerm(t2).typeof(ctx), ty1) ? tyAlg().TyUnit() : m().empty().typeof(ctx))
				.otherwise(() -> m().empty().typeof(ctx))
				.visitTy(visitTerm(t1).typeof(ctx));
	}
}
