package untyped;

public interface IsVal<Term> extends GTermAlg<Term, Boolean>, varapp.IsVal<Term> {
	default Boolean TmAbs(String p1, Term p2) {
		return true;
	}
}
