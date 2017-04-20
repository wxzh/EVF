package fullsub;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends moreextension.TyAlg<Ty>, top.TyAlg<Ty>, typed.TyAlg<Ty> {}