package fullsub;

public interface TermShiftAndSubst<Term, Ty> extends moreextension.TermShiftAndSubst<Term>, typed.TermShiftAndSubst<Term, Ty> {
	@Override TmMap<Term, Ty> tmMap();
}
