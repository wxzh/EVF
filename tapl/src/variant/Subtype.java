package variant;

public interface Subtype<Ty> extends typed.Subtype<Ty> {
	@Override
	TyEqv<Ty> tyEqv();
	@Override
	SubtypeAlg<Ty> subtype();
}
