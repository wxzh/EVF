package typed;

import typed.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, varapp.IsVal<Term> {
	@Override default Boolean TmAbs(String x, Ty ty, Term t) {
		return true;
	}
}
