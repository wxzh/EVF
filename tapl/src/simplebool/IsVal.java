package simplebool;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, typed.IsVal<Term, Ty>, bool.IsVal<Term> {}
