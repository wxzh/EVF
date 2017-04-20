package bot;

import bot.tyalg.external.TyAlgMatcher;
import bot.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends top.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();

	@Override default ITypeof<Ty, Bind> TmApp(Term t1, Term t2) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			Ty ty2 = visitTerm(t2).typeof(ctx);

			return tyMatcher()
					.TyArr(ty11 -> ty12 -> subtype(ty2, ty11) ? ty12 : typeError("parameter type mismatch"))
					.TyBot(() -> tyAlg().TyBot())
					.otherwise(() -> typeError("arrow type expected"))
					.visitTy(ty1);
		};
	}
}