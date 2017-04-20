package variant;

import java.util.List;
import java.util.stream.Collectors;

import library.Tuple2;
import library.Tuple3;
import utils.Context;
import utils.IPrint;
import variant.termalg.shared.GTermAlg;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>> {
	String printTy(Ty ty, Context<Bind> ctx);

	@Override default IPrint<Bind> TmTag(String label, Term t, Ty ty) {
		return ctx -> "<" + label + "=" + visitTerm(t).print(ctx) + "> as " + printTy(ty,ctx);
	}

	@Override default IPrint<Bind> TmCase(Term t, List<Tuple3<String, String, Term>> cases) {
		return ctx -> "case " + visitTerm(t).print(ctx) + " of " + cases.stream().map(c -> {
			String label = c._1;
			Tuple2<Context<Bind>, String> pr = ctx.pickFreshName(c._2);
			return "<" + label + "=" + pr._2 + ">==>" + visitTerm(c._3).print(pr._1);
		}).collect(Collectors.joining("| "));
	}

}