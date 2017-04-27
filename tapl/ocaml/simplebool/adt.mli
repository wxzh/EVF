(* Data type definitions *)
type ty =
    TyArr of ty * ty
  | TyBool

type term =
    TmVar of info * int * int
  | TmAbs of info * string * ty * term
  | TmApp of info * term * term
  | TmTrue of info
  | TmFalse of info
  | TmIf of info * term * term * term

type binding =
    NameBind 
  | VarBind of ty


