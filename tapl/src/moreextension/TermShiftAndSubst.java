package moreextension;

public interface TermShiftAndSubst<Term> extends extension.TermShiftAndSubst<Term> {
	@Override TmMap<Term> tmMap();
}
