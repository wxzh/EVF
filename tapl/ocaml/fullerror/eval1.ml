let rec eval1 ctx t = match t with
    TmIf(_,TmTrue(_),t2,t3) ->
      t2
  | TmIf(_,TmFalse(_),t2,t3) ->
      t3
  | TmVar(fi,n,_) ->
      (match getbinding fi ctx n with
          TmAbbBind(t,_) -> t 
        | _ -> raise NoRuleApplies)
  | TmApp(_,TmError(fi),t2) ->
      TmError(fi) 
  | TmApp(_,v1,TmError(fi)) when isval ctx v1 ->
      TmError(fi) 
  | TmApp(fi,TmAbs(_,x,tyT11,t12),v2) when isval ctx v2 ->
      termSubstTop v2 t12
  | TmApp(fi,v1,t2) when isval ctx v1 ->
      let t2' = eval1 ctx t2 in
      TmApp(fi, v1, t2')
  | TmApp(fi,t1,t2) ->
      let t1' = eval1 ctx t1 in
      TmApp(fi, t1', t2)
  | TmIf(_,TmError(fi),t2,t3) ->
      TmError(fi) 
  | TmIf(fi,t1,t2,t3) ->
      let t1' = eval1 ctx t1 in
      TmIf(fi, t1', t2, t3)
  | _ -> 
      raise NoRuleApplies

