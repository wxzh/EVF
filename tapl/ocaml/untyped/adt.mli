(* Data type definitions *)
type term =
    TmVar of info * int * int
  | TmAbs of info * string * term
  | TmApp of info * term * term

type binding =
    NameBind 


