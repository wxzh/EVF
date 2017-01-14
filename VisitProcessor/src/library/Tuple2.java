package library;

public class Tuple2<T1, T2> {
	public final T1 _1;
	public final T2 _2;
	public Tuple2(T1 arg1, T2 arg2) {
		this._1 = arg1;
		this._2 = arg2;
	}
	public String toString() {
		return String.format("(%s, %s)", _1, _2);
	}
}