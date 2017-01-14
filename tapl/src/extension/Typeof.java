package extension;

import extension.termalg.shared.GTermAlg;
import extension.tyalg.external.TyAlgMatcher;
import extension.tyalg.shared.GTyAlg;
import library.Zero;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>,
		tyarith.Typeof<Term, Ty, Bind>, record.Typeof<Term, Ty, Bind> {
	@Override TyEqv<Ty> tyEqv();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();

	@Override default ITypeof<Ty, Bind> TmTimesFloat(Term t1, Term t2) {
		return ctx -> {
			Ty tyT1 = visitTerm(t1).typeof(ctx);
			Ty tyT2 = visitTerm(t2).typeof(ctx);
			Ty tyFloat = tyAlg().TyFloat();
			return tyEqv().visitTy(tyT1).tyEqv(tyFloat) &&
					tyEqv().visitTy(tyT2).tyEqv(tyFloat)
					? tyFloat
					: m().empty().typeof(ctx);
		};
	}

	@Override default ITypeof<Ty, Bind> TmFloat(float p1) {
		return ctx -> tyAlg().TyFloat();
	}

	@Override default ITypeof<Ty, Bind> TmString(String p1) {
		return ctx -> tyAlg().TyString();
	}

	@Override default ITypeof<Ty, Bind> TmLet(String x, Term t1, Term t2) {
		return ctx -> {
			Ty tyT1 = visitTerm(t1).typeof(ctx);
			return visitTerm(t2).typeof(ctx.addBinding(x, bindAlg().VarBind(tyT1)));
		};
	}
	@Override default Zero<ITypeof<Ty, Bind>> m() {
		return record.Typeof.super.m();
	}
}