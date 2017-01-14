package fullref;

import java.util.ArrayList;
import java.util.List;

public class Store<Term> {
	List<Term> l;

	public Store() {
		l = new ArrayList<>();
	}

	public int extend(Term t) {
		l.add(t);
		return l.size() - 1;
	}

	public Term lookup(int n) {
		return l.get(n);
	}

	public void update(int n, Term t) {
		l.set(n, t);
	}
}
