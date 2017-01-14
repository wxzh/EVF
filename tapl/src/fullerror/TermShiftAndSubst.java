package fullerror;

public interface TermShiftAndSubst<Term, Ty> extends simplebool.TermShiftAndSubst<Term, Ty> {
	@Override TmMap<Term, Ty> tmMap();
}
