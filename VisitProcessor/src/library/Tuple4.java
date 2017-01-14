package library;

public class Tuple4<T1, T2, T3, T4> {
	public final T1 _1;
	public final T2 _2;
	public final T3 _3;
	public final T4 _4;
	public Tuple4(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
		this._1 = arg1;
		this._2 = arg2;
		this._3 = arg3;
		this._4 = arg4;
	}
	public String toString() {
		return String.format("(%s, %s, %s, %s)", _1, _2, _3, _4);
	}
}