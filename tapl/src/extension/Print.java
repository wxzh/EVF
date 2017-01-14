package extension;

import extension.termalg.external.TermAlgMatcher;
import extension.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>>, varapp.Print<Term, Bind>,
		arith.Print<Term, Bind>, record.Print<Term, Bind> {
	@Override TermAlgMatcher<Term, String> matcher();

	@Override default IPrint<Bind> TmString(String s) {
		return ctx -> s;
	}

	@Override default IPrint<Bind> TmFloat(float f) {
		return ctx -> String.valueOf(f);
	}

	@Override default IPrint<Bind> TmTimesFloat(Term t1, Term t2) {
		return ctx -> "timesfloat " + visitTerm(t1).print(ctx) + " " + visitTerm(t2).print(ctx);
	}

	@Override default IPrint<Bind> TmLet(String x, Term t1, Term t2) {
		return ctx -> "let " + x + "=" + visitTerm(t1).print(ctx) + " in " + visitTerm(t2).print(ctx.addName(x));
	}
}