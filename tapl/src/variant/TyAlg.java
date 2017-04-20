package variant;

import java.util.List;

import annotation.Visitor;
import library.Tuple2;

@Visitor public interface TyAlg<Ty> {
	Ty TyVariant(List<Tuple2<String, Ty>> els);
}
