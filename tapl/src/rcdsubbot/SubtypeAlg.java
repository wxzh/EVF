package rcdsubbot;

import rcdsubbot.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface SubtypeAlg<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, Subtype<Ty>, bot.SubtypeAlg<Ty>, record.SubtypeAlg<Ty> {
}
