package rcdsubbot;

import rcdsubbot.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term,Ty,Bind> extends GTermAlg<Term,Ty,IPrint<Bind>>, typed.Print<Term,Ty,Bind>, record.Print<Term,Bind> {}
