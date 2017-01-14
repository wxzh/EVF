package typed;

public interface Subtype<Ty> {
	TyEqv<Ty> tyEqv();
	SubtypeAlg<Ty> subtype();

	default boolean subtype(Ty ty1, Ty ty2) {
		return tyEqv().visitTy(ty1).tyEqv(ty2) || subtype().visitTy(ty1).subtype(ty2);
	}
}
