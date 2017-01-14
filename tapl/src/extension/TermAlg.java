package extension;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> extends varapp.TermAlg<Term>, arith.TermAlg<Term>, record.TermAlg<Term> {
	Term TmFloat(float f);
	Term TmTimesFloat(Term t1, Term t2);
	Term TmString(String s);
	Term TmLet(String x, Term t1, Term t2);
}