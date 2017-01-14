package varapp;

import utils.IPrint;
import utils.PrintBind;
import varapp.termalg.shared.GTermAlg;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>> {
	PrintBind<Bind> printBind();

	@Override
	default IPrint<Bind> TmApp(Term t1, Term t2) {
		return ctx -> visitTerm(t1).print(ctx) + " " + visitTerm(t2).print(ctx);
	}

	@Override
	default IPrint<Bind> TmVar(int x, int n) {
		return ctx -> {
			if (ctx.length() == n)
				return ctx.index2Name(x);
			else
				return "[bad index: " + x + "/" + n + " in " + ctx.toString(bind -> printBind().visitBind(bind).print(ctx)) + "]";
		};
	}
}