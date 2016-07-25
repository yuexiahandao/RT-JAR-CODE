/*     */ package javax.script;
/*     */ 
/*     */ public abstract class CompiledScript
/*     */ {
/*     */   public abstract Object eval(ScriptContext paramScriptContext)
/*     */     throws ScriptException;
/*     */ 
/*     */   public Object eval(Bindings paramBindings)
/*     */     throws ScriptException
/*     */   {
/*  79 */     Object localObject = getEngine().getContext();
/*     */ 
/*  81 */     if (paramBindings != null) {
/*  82 */       SimpleScriptContext localSimpleScriptContext = new SimpleScriptContext();
/*  83 */       localSimpleScriptContext.setBindings(paramBindings, 100);
/*  84 */       localSimpleScriptContext.setBindings(((ScriptContext)localObject).getBindings(200), 200);
/*     */ 
/*  86 */       localSimpleScriptContext.setWriter(((ScriptContext)localObject).getWriter());
/*  87 */       localSimpleScriptContext.setReader(((ScriptContext)localObject).getReader());
/*  88 */       localSimpleScriptContext.setErrorWriter(((ScriptContext)localObject).getErrorWriter());
/*  89 */       localObject = localSimpleScriptContext;
/*     */     }
/*     */ 
/*  92 */     return eval((ScriptContext)localObject);
/*     */   }
/*     */ 
/*     */   public Object eval()
/*     */     throws ScriptException
/*     */   {
/* 106 */     return eval(getEngine().getContext());
/*     */   }
/*     */ 
/*     */   public abstract ScriptEngine getEngine();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.CompiledScript
 * JD-Core Version:    0.6.2
 */