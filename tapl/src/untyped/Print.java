package untyped;

import library.Tuple2;
import untyped.termalg.shared.GTermAlg;
import utils.Context;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>>, varapp.Print<Term, Bind> {
	default IPrint<Bind> TmAbs(String x, Term t) {
		return ctx -> {
			Tuple2<Context<Bind>, String> pr = ctx.pickFreshName(x);
			return "\\" + pr._2 + "." + visitTerm(t).print(pr._1);
		};
	}
}
