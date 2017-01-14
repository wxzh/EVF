package utils;

import annotation.Visitor;

@Visitor public interface BindingAlg<Bind> {
	Bind NameBind();
}
