package fullsub;

import java.util.List;

import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.shared.TyAlgDefault;
import library.Tuple2;
import library.Zero;
import utils.ISubtype;

public interface Subtype<Ty> extends TyAlgDefault<Ty, ISubtype<Ty>>, top.Subtype<Ty>, record.Subtype<Ty>, typed.Subtype<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
	@Override default Zero<ISubtype<Ty>> m() {
	  return () -> ty -> false;
	}

  default ISubtype<Ty> TyArr(Ty tyS1, Ty tyS2) {
    return top.Subtype.super.TyArr(tyS1, tyS2);
  }

  default ISubtype<Ty> TyTop() {
    return top.Subtype.super.TyTop();
  }

  default ISubtype<Ty> TyRecord(List<Tuple2<String, Ty>> fS) {
    return record.Subtype.super.TyRecord(fS);
  }
}
