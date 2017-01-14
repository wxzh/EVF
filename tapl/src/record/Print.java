package record;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import library.Tuple2;
import record.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>> {
	@Override default IPrint<Bind> TmRecord(List<Tuple2<String, Term>> fields) {
		return ctx -> "{" + IntStream.range(0, fields.size()).mapToObj(i -> {
			String l = fields.get(i)._1;
			String t = visitTerm(fields.get(i)._2).print(ctx);
			return String.valueOf(i).equals(l) ? t : l + "=" + t;
		}).collect(Collectors.joining(",")) + "}";
	}

	@Override default IPrint<Bind> TmProj(Term t, String l) {
		return ctx -> visitTerm(t).print(ctx) + "." + l;
	}
}