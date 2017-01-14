package typed;

import simplebool.termalg.shared.TermAlgDefault;

public interface IsVal<Term, Ty> extends TermAlgDefault<Term, Ty, Boolean>, varapp.IsVal<Term> {
	@Override default Boolean TmAbs(String p1, Ty p2, Term p3) {
		return true;
	}
}
