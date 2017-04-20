package utils;

public interface JoinMeet<Ty> {
	boolean subtype(Ty ty1, Ty ty2);
	Ty joinImpl(Ty ty1, Ty ty2);
	Ty meetImpl(Ty ty1, Ty ty2);

	default Ty join(Ty ty1, Ty ty2) {
		if (subtype(ty1, ty2)) return ty2;
		if (subtype(ty2, ty1)) return ty1;
		return joinImpl(ty1,ty2);
	}

	default Ty meet(Ty ty1, Ty ty2) {
		if (subtype(ty1, ty2)) return ty1;
		if (subtype(ty2, ty1)) return ty2;
		return meetImpl(ty1, ty2);
	}

  default Ty notFound() {
    throw new RuntimeException();
  }
}
