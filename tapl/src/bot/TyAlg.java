package bot;

import annotation.Visitor;

@Visitor
public interface TyAlg<Ty> extends top.TyAlg<Ty> {
	Ty TyBot();
}
