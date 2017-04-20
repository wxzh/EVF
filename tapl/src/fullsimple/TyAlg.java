package fullsimple;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends moreextension.TyAlg<Ty>, variant.TyAlg<Ty>, simplebool.TyAlg<Ty> {
}