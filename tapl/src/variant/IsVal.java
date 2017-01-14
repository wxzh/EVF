package variant;

import variant.termalg.shared.TermAlgDefault;

public interface IsVal<Term, Ty> extends TermAlgDefault<Term, Ty, Boolean>, typed.IsVal<Term, Ty> {
	@Override default Boolean TmTag(String x, Term t, Ty ty) {
		return visitTerm(t);
	}
}
