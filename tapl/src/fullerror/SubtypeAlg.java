package fullerror;

import fullerror.tyalg.shared.TyAlgDefault;
import library.Zero;
import utils.ISubtype;

public interface SubtypeAlg<Ty> extends TyAlgDefault<Ty, ISubtype<Ty>>, Subtype<Ty>, bot.SubtypeAlg<Ty> {
	@Override default Zero<ISubtype<Ty>> m() {
		return () -> ty -> false;
	}

	@Override default ISubtype<Ty> TyArr(Ty tyS1, Ty tyS2) {
		return bot.SubtypeAlg.super.TyArr(tyS1, tyS2);
	}

	@Override default ISubtype<Ty> TyBot() {
		return bot.SubtypeAlg.super.TyBot();
	}

	@Override default ISubtype<Ty> TyTop() {
		return bot.SubtypeAlg.super.TyTop();
	}
}