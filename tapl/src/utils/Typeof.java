package utils;

public interface Typeof<Ty> {
  boolean tyEqv(Ty ty1, Ty ty2);

	default Ty typeError(String msg) {
	  throw new TypeError();
	}
}
