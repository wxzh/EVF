package fullerror;

import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, bot.Subtype<Ty>, typed.Subtype<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
	default ISubtype<Ty> TyBool() {
	  return ty -> false;
	}
}
