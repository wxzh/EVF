package fullref;

import fullref.termalg.shared.TermAlgDefault;

public interface IsVal<Term, Ty> extends TermAlgDefault<Term, Ty, Boolean>, fullsimple.IsVal<Term, Ty> {
	@Override default Boolean TmLoc(int l) {
		return true;
	}
}