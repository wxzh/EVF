(* Data type definitions *)
type ty =
    TyVar of int * int
  | TyTop
  | TyBot
  | TyId of string
  | TyArr of ty * ty
  | TyRecord of (string * ty) list
  | TyVariant of (string * ty) list
  | TyRef of ty
  | TyBool
  | TyString
  | TyUnit
  | TyFloat
  | TySource of ty
  | TySink of ty
  | TyNat

type term =
    TmVar of info * int * int
  | TmAbs of info * string * ty * term
  | TmApp of info * term * term
  | TmTrue of info
  | TmFalse of info
  | TmIf of info * term * term * term
  | TmLet of info * string * term * term
  | TmFix of info * term
  | TmRecord of info * (string * term) list
  | TmProj of info * term * string
  | TmCase of info * term * (string * (string * term)) list
  | TmTag of info * string * term * ty
  | TmAscribe of info * term * ty
  | TmString of info * string
  | TmUnit of info
  | TmLoc of info * int
  | TmRef of info * term
  | TmDeref of info * term 
  | TmAssign of info * term * term
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


