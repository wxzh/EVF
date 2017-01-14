package record;

import java.util.List;

import annotation.Visitor;
import library.Tuple2;

@Visitor public interface TyAlg<Ty> extends typed.TyAlg<Ty> {
	Ty TyRecord(List<Tuple2<String, Ty>> els);
}
