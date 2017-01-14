package fulluntyped;

public interface TermShiftAndSubst<Term> extends untyped.TermShiftAndSubst<Term> {
	@Override TmMap<Term> tmMap();
}
