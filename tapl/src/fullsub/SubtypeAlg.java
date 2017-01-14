package fullsub;

import java.util.List;

import fullsub.tyalg.shared.TyAlgDefault;
import library.Tuple2;
import library.Zero;
import utils.ISubtype;

public interface SubtypeAlg<Ty> extends TyAlgDefault<Ty, ISubtype<Ty>>, Subtype<Ty>, record.SubtypeAlg<Ty>, top.SubtypeAlg<Ty> {
	@Override default Zero<ISubtype<Ty>> m() {
		return () -> ty -> false;
	}

	default ISubtype<Ty> TyArr(Ty tyS1, Ty tyS2) {
		return record.SubtypeAlg.super.TyArr(tyS1, tyS2);
	}

	@Override default ISubtype<Ty> TyTop() {
		return top.SubtypeAlg.super.TyTop();
	}

	@Override default ISubtype<Ty> TyRecord(List<Tuple2<String, Ty>> fS) {
		return record.SubtypeAlg.super.TyRecord(fS);
	}
}