package variant;

import java.util.List;
import java.util.stream.IntStream;

import fullsimple.tyalg.shared.GTyAlg;
import library.Tuple2;
import utils.ITyEqv;
import variant.tyalg.external.TyAlgMatcher;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>> {
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyVariant(List<Tuple2<String, Ty>> fields1) {
		return ty -> matcher().TyVariant(
				fields2 -> fields1.size() == fields2.size() && IntStream.range(0, fields1.size()).mapToObj(i -> {
					String l1 = fields1.get(i)._1;
					String l2 = fields2.get(i)._1;
					Ty ty1 = fields1.get(i)._2;
					Ty ty2 = fields2.get(i)._2;
					return l1.equals(l2) && visitTy(ty1).tyEqv(ty2);
				}).reduce(true, (x, y) -> x && y)).otherwise(() -> false).visitTy(ty);
	}
}