package arith;

import arith.termalg.shared.TermAlgDefault;
import library.Zero;

public interface IsVal<Term> extends TermAlgDefault<Term, Boolean>, bool.IsVal<Term>, nat.IsNumericVal<Term> {
	@Override default Zero<Boolean> m() {
		return bool.IsVal.super.m();
	}
}