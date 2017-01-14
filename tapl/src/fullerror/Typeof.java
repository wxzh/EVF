package fullerror;

import fullerror.termalg.shared.GTermAlg;
import fullerror.tyalg.external.TyAlgMatcher;
import fullerror.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, simplebool.Typeof<Term, Ty, Bind>, bot.Typeof<Term, Ty, Bind> {
	JoinMeet<Ty> joinMeet();
	@Override Subtype<Ty> subtype();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();

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

	@Override default ITypeof<Ty, Bind> TmError() {
		return ctx -> tyAlg().TyBot();
	}

	@Override default ITypeof<Ty, Bind> TmTry(Term t1, Term t2) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			Ty ty2 = visitTerm(t2).typeof(ctx);
			return joinMeet().join(ty1, ty2);
		};
	}
}