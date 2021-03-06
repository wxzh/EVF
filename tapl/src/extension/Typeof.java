package extension;

import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>,
		tyarith.Typeof<Term, Ty, Bind>, record.Typeof<Term, Ty, Bind>, floatstring.Typeof<Term, Ty, Bind>, let.Typeof<Term, Ty, Bind> {
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override TyAlg<Ty> tyAlg();
}