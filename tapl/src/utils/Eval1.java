package utils;

public interface Eval1<Term> {
	default Term noRuleApplies() {
	  throw new NoRuleApplies();
	}
}
