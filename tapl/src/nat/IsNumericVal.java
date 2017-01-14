package nat;

import library.Zero;
import nat.termalg.shared.TermAlgDefault;

public interface IsNumericVal<Term> extends TermAlgDefault<Term, Boolean> {
	@Override default Zero<Boolean> m() {
		return () -> false;
	}

	default Boolean TmZero() {
		return true;
	}

	default Boolean TmSucc(Term t) {
		return visitTerm(t);
	}
}