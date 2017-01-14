package extension;

import extension.termalg.shared.TermAlgDefault;
import library.Zero;

public interface IsVal<Term> extends TermAlgDefault<Term, Boolean>, varapp.IsVal<Term>, arith.IsVal<Term>, record.IsVal<Term> {
	default Boolean TmFloat(float p1) {
		return true;
	}

	default Boolean TmString(String p1) {
		return true;
	}

	@Override default Zero<Boolean> m() {
		return arith.IsVal.super.m();
	}
}
