package varapp;

public class TmMapCtx<Term> {
	interface VarMapper<Term> {
		Term apply(int c, int x, int n);
	}

	public VarMapper<Term> onvar;
	public int c;

	public TmMapCtx(VarMapper<Term> onvar, int c) {
		this.onvar = onvar;
		this.c = c;
	}

	public Term tmMap(int x, int n) {
		return onvar.apply(c, x, n);
	}
}