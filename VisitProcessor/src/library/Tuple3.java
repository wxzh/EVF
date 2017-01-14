package library;

public class Tuple3<T1, T2, T3> {
	public final T1 _1;
	public final T2 _2;
	public final T3 _3;
	public Tuple3(T1 arg1, T2 arg2, T3 arg3) {
		this._1 = arg1;
		this._2 = arg2;
		this._3 = arg3;
	}
	public String toString() {
		return String.format("(%s, %s, %s)", _1, _2, _3);
	}
}