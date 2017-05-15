package bot;

import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, top.Subtype<Ty> {
  default ISubtype<Ty> TyBot() {
    return ty -> true;
  }
}