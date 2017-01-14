package arith;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> extends bool.TermAlg<Term>, nat.TermAlg<Term> {
	Term TmIsZero(Term t);
}