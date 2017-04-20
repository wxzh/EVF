package fullsimple;

import fullsimple.bindingalg.shared.GBindingAlg;
import fullsimple.termalg.shared.GTermAlg;
import fullsimple.tyalg.external.TyAlgMatcher;
import fullsimple.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>,
    moreextension.Typeof<Term, Ty, Bind>, variant.Typeof<Term, Ty, Bind>, simplebool.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override GBindingAlg<Bind, Term, Ty, Bind> bindAlg();
	@Override GTyAlg<Ty, Ty> tyAlg();
}