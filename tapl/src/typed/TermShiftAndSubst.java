package typed;

public interface TermShiftAndSubst<Term, Ty> extends varapp.TermShiftAndSubst<Term> {
	@Override TmMap<Term, Ty> tmMap();
}
