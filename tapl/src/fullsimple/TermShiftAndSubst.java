package fullsimple;

public interface TermShiftAndSubst<Term, Ty> extends variant.TermShiftAndSubst<Term, Ty>, moreextension.TermShiftAndSubst<Term> {
	@Override TmMap<Term, Ty> tmMap();
}
