package fullerror;

import fullerror.termalg.shared.GTermAlg;
import fullerror.tyalg.external.TyAlgMatcher;
import fullerror.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, simplebool.Typeof<Term, Ty, Bind>, bot.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GTyAlg<Ty, Ty> tyAlg();
	Ty join(Ty ty1, Ty ty2);

	@Override default ITypeof<Ty, Bind> TmError() {
		return ctx -> tyAlg().TyBot();
	}

	@Override default ITypeof<Ty, Bind> TmTry(Term t1, Term t2) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			Ty ty2 = visitTerm(t2).typeof(ctx);
			return join(ty1, ty2);
		};
	}
}