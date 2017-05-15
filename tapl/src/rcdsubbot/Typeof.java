package rcdsubbot;

import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, bot.Typeof<Term, Ty, Bind>, record.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override TyAlg<Ty> tyAlg();
}