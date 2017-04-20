package utils;

public class TmMapCtx<Term> {
	public OnVar<Term> onvar;
	public int c;

	public TmMapCtx(OnVar<Term> onvar, int c) {
		this.onvar = onvar;
		this.c = c;
	}

	public Term tmMap(int x, int n) {
		return onvar.apply(c, x, n);
	}
}