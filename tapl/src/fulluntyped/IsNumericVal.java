package fulluntyped;

import library.Zero;

public interface IsNumericVal<Term> extends TermAlgDefault<Term, Boolean>, arith.IsNumericVal<Term> {
	@Override default Zero<Boolean> m() {
		return arith.IsNumericVal.super.m();
	}
}