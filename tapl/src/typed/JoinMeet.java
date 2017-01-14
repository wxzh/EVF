package typed;

public interface JoinMeet<Ty> {
	Subtype<Ty> subtype();
	Join<Ty> join();
	Meet<Ty> meet();

	default Ty join(Ty ty1, Ty ty2) {
		if (subtype().subtype(ty1, ty2))
			return ty2;
		if (subtype().subtype(ty2, ty1))
			return ty1;
		return join().visitTy(ty1).join(ty2);
	}

	default Ty meet(Ty ty1, Ty ty2) {
		if (subtype().subtype(ty1, ty2))
			return ty1;
		if (subtype().subtype(ty2, ty1))
			return ty2;
		return meet().visitTy(ty1).meet(ty2);
	}
}