package typed;

import library.Tuple2;
import typed.termalg.shared.GTermAlg;
import utils.Context;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, varapp.Print<Term, Bind> {
  String printTy(Ty ty, Context<Bind> ctx);

	@Override default IPrint<Bind> TmAbs(String x, Ty ty, Term t) {
		return ctx -> {
			Tuple2<Context<Bind>, String> pr = ctx.pickFreshName(x);
			return "lambda " + pr._2 + ":" + printTy(ty, ctx) + ". " + visitTerm(t).print(pr._1);
		};
	}
}