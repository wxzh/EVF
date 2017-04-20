package record;

import java.util.List;

import library.Tuple2;
import record.termalg.shared.GTermAlg;

public interface IsVal<Term> extends GTermAlg<Term, Boolean> {
	default Boolean TmRecord(List<Tuple2<String, Term>> fields) {
		return fields.stream().map(pr -> visitTerm(pr._2)).reduce(true, (x, y) -> x && y);
	}
	default Boolean TmProj(Term p1, String p2) {
	  return false;
	}
}
