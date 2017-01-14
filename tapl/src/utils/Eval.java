package utils;

public interface Eval<Term> {
	Term eval1(Term t);
	boolean isVal(Term t);

	default Term eval(Term t) {
		try {
			Term t1 = eval1(t);
			return eval(t1);
		} catch(NoRuleApplies e) {
			if (isVal(t)) {
				return t;
			}
			else {
				throw e;
			}
		}
	}
}