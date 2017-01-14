package fullref;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends fullsimple.TermAlg<Term, Ty> {
	Term TmLoc(int l);
	Term TmRef(Term t);
	Term TmDeref(Term t);
	Term TmAssign(Term t1, Term t2);
}