package record;

import java.util.ArrayList;
import java.util.List;

import library.Tuple2;
import record.termalg.external.TermAlgMatcher;
import record.termalg.shared.GTermAlg;

public interface Eval1<Term> extends GTermAlg<Term, Term>, utils.Eval1<Term> {
	GTermAlg<Term, Term> alg();
	TermAlgMatcher<Term, Term> matcher();
	boolean isVal(Term t);

	default Term TmRecord(List<Tuple2<String, Term>> fields) {
		return alg().TmRecord(evalAField(fields));
	}

	default List<Tuple2<String, Term>> evalAField(List<Tuple2<String, Term>> fields) {
		if (fields.size() == 0) noRuleApplies();
		Tuple2<String, Term> pair = fields.get(0);
		List<Tuple2<String, Term>> rest = fields.subList(1, fields.size());
		List<Tuple2<String, Term>> xs;
		if (isVal(pair._2)) {
			xs = new ArrayList<>(evalAField(rest));
			xs.add(0, pair);
		} else {
			xs = new ArrayList<>(rest);
			xs.add(0, new Tuple2<>(pair._1, visitTerm(pair._2)));
		}
		return xs;
	}

	@Override default Term TmProj(Term t, String l) {
		return matcher()
				.TmRecord(fields -> isVal(t)
						? fields.stream().filter(pr -> pr._1.equals(l)).findFirst()
								.map(pr -> pr._2).orElseGet(() -> noRuleApplies()) // should be lazy for throwing errors
						: alg().TmProj(visitTerm(t), l))
				.otherwise(() -> alg().TmProj(visitTerm(t), l))
				.visitTerm(t);
	}
}