package extension;

public interface TermShiftAndSubst<Term> extends varapp.TermShiftAndSubst<Term> {
	@Override TmMap<Term> tmMap();
}
