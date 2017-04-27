(* Data type definitions *)
type ty =
    TyTop
  | TyBot
  | TyArr of ty * ty

type term =
    TmVar of info * int * int
  | TmAbs of info * string * ty * term
  | TmApp of info * term * term

type binding =
    NameBind 
  | VarBind of ty


