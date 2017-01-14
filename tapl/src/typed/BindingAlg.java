package typed;

import annotation.Visitor;

@Visitor public interface BindingAlg<Bind, Ty> extends utils.BindingAlg<Bind> {
	Bind VarBind(Ty ty);
}
