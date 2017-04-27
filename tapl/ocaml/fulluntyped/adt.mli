(* Data type definitions *)
type term =
    TmTrue of info
  | TmFalse of info
  | TmIf of info * term * term * term
  | TmVar of info * int * int
  | TmAbs of info * string * term
  | TmApp of info * term * term
  | TmRecord of info * (string * term) list
  | TmProj of info * term * string
  | TmFloat of info * float
  | TmTimesfloat of info * term * term
  | TmString of info * string
  | TmZero of info
  | TmSucc of info * term
  | TmPred of info * term
  | TmIsZero of info * term
  | TmLet of info * string * term * term

type binding =
    NameBind 
  | TmAbbBind of term


