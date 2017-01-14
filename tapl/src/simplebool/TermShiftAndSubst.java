package simplebool;

public interface TermShiftAndSubst<Term, Ty> extends typed.TermShiftAndSubst<Term, Ty> {
	@Override TmMap<Term, Ty> tmMap();
}
