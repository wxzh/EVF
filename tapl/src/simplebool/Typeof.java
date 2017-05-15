package simplebool;

import simplebool.termalg.shared.GTermAlg;
import simplebool.tyalg.external.TyAlgMatcher;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>,
    typed.Typeof<Term, Ty, Bind>, bool.Typeof<Term, Ty, Bind> {
	@Override TyAlg<Ty> tyAlg();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
}