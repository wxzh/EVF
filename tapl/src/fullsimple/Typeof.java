package fullsimple;

import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>,
    moreextension.Typeof<Term, Ty, Bind>, variant.Typeof<Term, Ty, Bind>, simplebool.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override BindingAlg<Bind, Term, Ty> bindAlg();
	@Override TyAlg<Ty> tyAlg();
}