package record;

import java.util.List;

import annotation.Visitor;
import library.Tuple2;

@Visitor public interface TermAlg<Term> {
	Term TmRecord(List<Tuple2<String, Term>> fields);
	Term TmProj(Term t, String l);
}