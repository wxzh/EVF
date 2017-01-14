package fullerror;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends simplebool.TermAlg<Term, Ty> {
	Term TmError();
	Term TmTry(Term t1, Term t2);
}
