package moreextension;

import moreextension.termalg.shared.TermAlgDefault;

public interface IsVal<Term, Ty> extends TermAlgDefault<Term, Ty, Boolean>, extension.IsVal<Term> {
	default Boolean TmUnit() {
		return true;
	}
}