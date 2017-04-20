package extension;

import extension.tyalg.external.TyAlgMatcher;
import extension.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, tyarith.TyEqv<Ty>, record.TyEqv<Ty>, floatstring.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}