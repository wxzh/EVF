package untyped;

import library.Zero;
import untyped.termalg.shared.TermAlgDefault;

public interface IsVal<Term> extends TermAlgDefault<Term, Boolean>, varapp.IsVal<Term> {
	@Override default Zero<Boolean> m() {
		return () -> false;
	}

	default Boolean TmAbs(String p1, Term p2) {
		return true;
	}
}
