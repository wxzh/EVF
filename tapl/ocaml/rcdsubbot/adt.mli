(* Data type definitions *)
type ty =
    TyTop
  | TyBot
  | TyRecord of (string * ty) list
  | TyArr of ty * ty

type term =
    TmVar of info * int * int
  | TmAbs of info * string * ty * term
  | TmApp of info * term * term
  | TmRecord of info * (string * term) list
  | TmProj of info * term * string

type binding =
    NameBind 
  | VarBind of ty


