package library;

public class Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
	public final T1 _1;
	public final T2 _2;
	public final T3 _3;
	public final T4 _4;
	public final T5 _5;
	public final T6 _6;
	public final T7 _7;
	public final T8 _8;
	public final T9 _9;
	public final T10 _10;
	public final T11 _11;
	public final T12 _12;
	public final T13 _13;
	public Tuple13(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13) {
		this._1 = arg1;
		this._2 = arg2;
		this._3 = arg3;
		this._4 = arg4;
		this._5 = arg5;
		this._6 = arg6;
		this._7 = arg7;
		this._8 = arg8;
		this._9 = arg9;
		this._10 = arg10;
		this._11 = arg11;
		this._12 = arg12;
		this._13 = arg13;
	}
	public String toString() {
		return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13);
	}
}