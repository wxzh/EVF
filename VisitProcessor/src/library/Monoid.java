package library;

public interface Monoid<R> extends Zero<R> {
	R join(R x, R y);
}
