package simplebool;

import simplebool.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, typed.IsVal<Term, Ty>, bool.IsVal<Term> {
}
