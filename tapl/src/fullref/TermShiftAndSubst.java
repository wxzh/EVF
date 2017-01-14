package fullref;

public interface TermShiftAndSubst<Term, Ty> extends fullsimple.TermShiftAndSubst<Term, Ty> {
	@Override TmMap<Term, Ty> tmMap();
}
