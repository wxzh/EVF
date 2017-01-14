package top;

import top.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface SubtypeAlg<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, Subtype<Ty>, typed.SubtypeAlg<Ty> {
	@Override default ISubtype<Ty> TyTop() {
		return ty -> false;
	}
}
