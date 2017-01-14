package record;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import library.Tuple2;
import record.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, typed.PrintTy<Ty, Bind> {
	default IPrint<Bind> TyRecord(List<Tuple2<String, Ty>> fields) {
		return ctx -> "{" + IntStream.range(0, fields.size()).mapToObj(i -> {
			String label = fields.get(i)._1;
			Ty ty = fields.get(i)._2;
			return String.valueOf(i + 1).equals(label) ? visitTy(ty).print(ctx) : label + ":" + visitTy(ty).print(ctx);
		}).collect(Collectors.joining(",")) + "}";
	}
}