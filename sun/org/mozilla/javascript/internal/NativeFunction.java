/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.debug.DebuggableScript;
/*     */ 
/*     */ public abstract class NativeFunction extends BaseFunction
/*     */ {
/*     */   public final void initScriptFunction(Context paramContext, Scriptable paramScriptable)
/*     */   {
/*  57 */     ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable);
/*     */   }
/*     */ 
/*     */   final String decompile(int paramInt1, int paramInt2)
/*     */   {
/*  68 */     String str = getEncodedSource();
/*  69 */     if (str == null) {
/*  70 */       return super.decompile(paramInt1, paramInt2);
/*     */     }
/*  72 */     UintMap localUintMap = new UintMap(1);
/*  73 */     localUintMap.put(1, paramInt1);
/*  74 */     return Decompiler.decompile(str, paramInt2, localUintMap);
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  81 */     int i = getParamCount();
/*  82 */     if (getLanguageVersion() != 120) {
/*  83 */       return i;
/*     */     }
/*  85 */     Context localContext = Context.getContext();
/*  86 */     NativeCall localNativeCall = ScriptRuntime.findFunctionActivation(localContext, this);
/*  87 */     if (localNativeCall == null) {
/*  88 */       return i;
/*     */     }
/*  90 */     return localNativeCall.originalArgs.length;
/*     */   }
/*     */ 
/*     */   public int getArity()
/*     */   {
/*  96 */     return getParamCount();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String jsGet_name()
/*     */   {
/* 106 */     return getFunctionName();
/*     */   }
/*     */ 
/*     */   public String getEncodedSource()
/*     */   {
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   public DebuggableScript getDebuggableView()
/*     */   {
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public Object resumeGenerator(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject1, Object paramObject2)
/*     */   {
/* 134 */     throw new EvaluatorException("resumeGenerator() not implemented");
/*     */   }
/*     */ 
/*     */   protected abstract int getLanguageVersion();
/*     */ 
/*     */   protected abstract int getParamCount();
/*     */ 
/*     */   protected abstract int getParamAndVarCount();
/*     */ 
/*     */   protected abstract String getParamOrVarName(int paramInt);
/*     */ 
/*     */   protected boolean getParamOrVarConst(int paramInt)
/*     */   {
/* 169 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeFunction
 * JD-Core Version:    0.6.2
 */