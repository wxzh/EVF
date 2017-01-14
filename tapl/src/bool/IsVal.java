package bool;


import bool.termalg.shared.TermAlgDefault;
import library.Zero;

public interface IsVal<Term> extends TermAlgDefault<Term, Boolean> {
	@Override default Zero<Boolean> m() {
		return () -> false;
	}

	@Override default Boolean TmTrue() {
		return true;
	}

	@Override default Boolean TmFalse() {
		return true;
	}
}