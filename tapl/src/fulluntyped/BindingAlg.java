package fulluntyped;

import annotation.Visitor;

@Visitor public interface BindingAlg<Bind, Term> extends utils.BindingAlg<Bind> {
	Bind TmAbbBind(Term t);
}
