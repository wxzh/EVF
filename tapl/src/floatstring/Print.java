package floatstring;

import floatstring.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>> {
	@Override default IPrint<Bind> TmString(String s) {
		return ctx -> s;
	}

	@Override default IPrint<Bind> TmFloat(float f) {
		return ctx -> String.valueOf(f);
	}

	@Override default IPrint<Bind> TmTimesFloat(Term t1, Term t2) {
		return ctx -> "timesfloat " + visitTerm(t1).print(ctx) + " " + visitTerm(t2).print(ctx);
	}
}