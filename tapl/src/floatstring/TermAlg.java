package floatstring;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> {
	Term TmString(String s);
	Term TmFloat(float f);
	Term TmTimesFloat(Term t1, Term t2);
}
