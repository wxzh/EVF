package moreextension;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends extension.TermAlg<Term> {
	Term TmFix(Term t);
	Term TmUnit();
	Term TmAscribe(Term t, Ty ty);
	Term TmInert(Ty ty);
}
