package record;

import java.util.List;

import library.Tuple2;
import library.Zero;
import record.termalg.shared.TermAlgDefault;

public interface IsVal<Term> extends TermAlgDefault<Term, Boolean> {
	@Override default Zero<Boolean> m() {
		return () -> false;
	}

	default Boolean TmRecord(List<Tuple2<String, Term>> fields) {
		return fields.stream().map(pr -> visitTerm(pr._2)).reduce(true, (x, y) -> x && y);
	}
}
