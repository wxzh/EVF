package fullsub;

import fullsub.termalg.shared.TermAlgDefault;
import library.Zero;

public interface IsNumericVal<Term, Ty> extends TermAlgDefault<Term, Ty, Boolean>, arith.IsNumericVal<Term> {
	@Override default Zero<Boolean> m() {
		return arith.IsNumericVal.super.m();
	}
}
