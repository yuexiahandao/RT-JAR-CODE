/*     */ package javax.script;
/*     */ 
/*     */ import java.io.Reader;
/*     */ 
/*     */ public abstract class AbstractScriptEngine
/*     */   implements ScriptEngine
/*     */ {
/*     */   protected ScriptContext context;
/*     */ 
/*     */   public AbstractScriptEngine()
/*     */   {
/*  64 */     this.context = new SimpleScriptContext();
/*     */   }
/*     */ 
/*     */   public AbstractScriptEngine(Bindings paramBindings)
/*     */   {
/*  77 */     this();
/*  78 */     if (paramBindings == null) {
/*  79 */       throw new NullPointerException("n is null");
/*     */     }
/*  81 */     this.context.setBindings(paramBindings, 100);
/*     */   }
/*     */ 
/*     */   public void setContext(ScriptContext paramScriptContext)
/*     */   {
/*  92 */     if (paramScriptContext == null) {
/*  93 */       throw new NullPointerException("null context");
/*     */     }
/*  95 */     this.context = paramScriptContext;
/*     */   }
/*     */ 
/*     */   public ScriptContext getContext()
/*     */   {
/* 104 */     return this.context;
/*     */   }
/*     */ 
/*     */   public Bindings getBindings(int paramInt)
/*     */   {
/* 120 */     if (paramInt == 200)
/* 121 */       return this.context.getBindings(200);
/* 122 */     if (paramInt == 100) {
/* 123 */       return this.context.getBindings(100);
/*     */     }
/* 125 */     throw new IllegalArgumentException("Invalid scope value.");
/*     */   }
/*     */ 
/*     */   public void setBindings(Bindings paramBindings, int paramInt)
/*     */   {
/* 143 */     if (paramInt == 200)
/* 144 */       this.context.setBindings(paramBindings, 200);
/* 145 */     else if (paramInt == 100)
/* 146 */       this.context.setBindings(paramBindings, 100);
/*     */     else
/* 148 */       throw new IllegalArgumentException("Invalid scope value.");
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Object paramObject)
/*     */   {
/* 164 */     Bindings localBindings = getBindings(100);
/* 165 */     if (localBindings != null)
/* 166 */       localBindings.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */   {
/* 182 */     Bindings localBindings = getBindings(100);
/* 183 */     if (localBindings != null) {
/* 184 */       return localBindings.get(paramString);
/*     */     }
/*     */ 
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */   public Object eval(Reader paramReader, Bindings paramBindings)
/*     */     throws ScriptException
/*     */   {
/* 210 */     ScriptContext localScriptContext = getScriptContext(paramBindings);
/*     */ 
/* 212 */     return eval(paramReader, localScriptContext);
/*     */   }
/*     */ 
/*     */   public Object eval(String paramString, Bindings paramBindings)
/*     */     throws ScriptException
/*     */   {
/* 231 */     ScriptContext localScriptContext = getScriptContext(paramBindings);
/*     */ 
/* 233 */     return eval(paramString, localScriptContext);
/*     */   }
/*     */ 
/*     */   public Object eval(Reader paramReader)
/*     */     throws ScriptException
/*     */   {
/* 249 */     return eval(paramReader, this.context);
/*     */   }
/*     */ 
/*     */   public Object eval(String paramString)
/*     */     throws ScriptException
/*     */   {
/* 264 */     return eval(paramString, this.context);
/*     */   }
/*     */ 
/*     */   protected ScriptContext getScriptContext(Bindings paramBindings)
/*     */   {
/* 290 */     SimpleScriptContext localSimpleScriptContext = new SimpleScriptContext();
/* 291 */     Bindings localBindings = getBindings(200);
/*     */ 
/* 293 */     if (localBindings != null) {
/* 294 */       localSimpleScriptContext.setBindings(localBindings, 200);
/*     */     }
/*     */ 
/* 297 */     if (paramBindings != null) {
/* 298 */       localSimpleScriptContext.setBindings(paramBindings, 100);
/*     */     }
/*     */     else {
/* 301 */       throw new NullPointerException("Engine scope Bindings may not be null.");
/*     */     }
/*     */ 
/* 304 */     localSimpleScriptContext.setReader(this.context.getReader());
/* 305 */     localSimpleScriptContext.setWriter(this.context.getWriter());
/* 306 */     localSimpleScriptContext.setErrorWriter(this.context.getErrorWriter());
/*     */ 
/* 308 */     return localSimpleScriptContext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.AbstractScriptEngine
 * JD-Core Version:    0.6.2
 */