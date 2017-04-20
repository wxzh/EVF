package fulluntyped;

import fulluntyped.termalg.shared.GTermAlg;

public interface IsVal<Term> extends GTermAlg<Term, Boolean>, extension.IsVal<Term>, untyped.IsVal<Term> {}
