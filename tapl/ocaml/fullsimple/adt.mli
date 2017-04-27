(* Data type definitions *)
type ty =
    TyVar of int * int
  | TyId of string
  | TyArr of ty * ty
  | TyUnit
  | TyRecord of (string * ty) list
  | TyVariant of (string * ty) list
  | TyBool
  | TyString
  | TyFloat
  | TyNat

type term =
    TmTrue of info
  | TmFalse of info
  | TmIf of info * term * term * term
  | TmCase of info * term * (string * (string * term)) list
  | TmTag of info * string * term * ty
  | TmVar of info * int * int
  | TmAbs of info * string * ty * term
  | TmApp of info * term * term
  | TmLet of info * string * term * term
  | TmFix of info * term
  | TmString of info * string
  | TmUnit of info
  | TmAscribe of info * term * ty
  | TmRecord of info * (string * term) list
  | TmProj of info * term * string
  | TmFloat of info * float
  | TmTimesfloat of info * term * term
  | TmZero of info
  | TmSucc of info * term
  | TmPred of info * term
  | TmIsZero of info * term
  | TmInert of info * ty

type binding =
    NameBind 
  | TyVarBind
  | VarBind of ty
  | TmAbbBind of term * (ty option)
  | TyAbbBind of ty


