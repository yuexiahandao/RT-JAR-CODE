/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ class DefaultErrorReporter
/*     */   implements ErrorReporter
/*     */ {
/*  48 */   static final DefaultErrorReporter instance = new DefaultErrorReporter();
/*     */   private boolean forEval;
/*     */   private ErrorReporter chainedReporter;
/*     */ 
/*     */   static ErrorReporter forEval(ErrorReporter paramErrorReporter)
/*     */   {
/*  57 */     DefaultErrorReporter localDefaultErrorReporter = new DefaultErrorReporter();
/*  58 */     localDefaultErrorReporter.forEval = true;
/*  59 */     localDefaultErrorReporter.chainedReporter = paramErrorReporter;
/*  60 */     return localDefaultErrorReporter;
/*     */   }
/*     */ 
/*     */   public void warning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*     */   {
/*  66 */     if (this.chainedReporter != null)
/*  67 */       this.chainedReporter.warning(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*     */   }
/*     */ 
/*     */   public void error(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*     */   {
/*  77 */     if (this.forEval)
/*     */     {
/*  81 */       String str = "SyntaxError";
/*     */ 
/*  85 */       if (paramString1.startsWith("TypeError: ")) {
/*  86 */         str = "TypeError";
/*  87 */         paramString1 = paramString1.substring("TypeError: ".length());
/*     */       }
/*  89 */       throw ScriptRuntime.constructError(str, paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*     */     }
/*     */ 
/*  92 */     if (this.chainedReporter != null) {
/*  93 */       this.chainedReporter.error(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*     */     }
/*     */     else
/*  96 */       throw runtimeError(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*     */   }
/*     */ 
/*     */   public EvaluatorException runtimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*     */   {
/* 105 */     if (this.chainedReporter != null) {
/* 106 */       return this.chainedReporter.runtimeError(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*     */     }
/*     */ 
/* 109 */     return new EvaluatorException(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.DefaultErrorReporter
 * JD-Core Version:    0.6.2
 */