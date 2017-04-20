package variant;

import java.util.List;

import annotation.Visitor;
import library.Tuple3;

@Visitor public interface TermAlg<Term, Ty> {
	Term TmCase(Term t, List<Tuple3<String, String, Term>> alts);
	Term TmTag(String x, Term t, Ty ty);
}