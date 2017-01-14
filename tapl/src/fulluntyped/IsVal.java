package fulluntyped;

import fulluntyped.termalg.shared.TermAlgDefault;
import library.Zero;

public interface IsVal<Term> extends TermAlgDefault<Term, Boolean>, extension.IsVal<Term>, untyped.IsVal<Term> {
	@Override default Zero<Boolean> m() {
		return extension.IsVal.super.m();
	}
}
