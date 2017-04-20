package simplebool;

import simplebool.termalg.shared.GTermAlg;
import simplebool.tyalg.external.TyAlgMatcher;
import simplebool.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>,
    typed.Typeof<Term, Ty, Bind>, bool.Typeof<Term, Ty, Bind> {
	@Override GTyAlg<Ty, Ty> tyAlg();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
}