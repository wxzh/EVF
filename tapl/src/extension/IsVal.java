package extension;

import extension.termalg.shared.GTermAlg;

public interface IsVal<Term> extends GTermAlg<Term, Boolean>, arith.IsVal<Term>, record.IsVal<Term>, floatstring.IsVal<Term>, let.IsVal<Term> {}
