package fullref;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends bot.TyAlg<Ty>, fullsub.TyAlg<Ty>, variant.TyAlg<Ty> {
	Ty TyRef(Ty ty);
	Ty TySource(Ty ty);
	Ty TySink(Ty ty);
}