/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class EcmaError extends RhinoException
/*     */ {
/*     */   static final long serialVersionUID = -6261226256957286699L;
/*     */   private String errorName;
/*     */   private String errorMessage;
/*     */ 
/*     */   EcmaError(String paramString1, String paramString2, String paramString3, int paramInt1, String paramString4, int paramInt2)
/*     */   {
/*  71 */     recordErrorOrigin(paramString3, paramInt1, paramString4, paramInt2);
/*  72 */     this.errorName = paramString1;
/*  73 */     this.errorMessage = paramString2;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public EcmaError(Scriptable paramScriptable, String paramString1, int paramInt1, int paramInt2, String paramString2)
/*     */   {
/*  83 */     this("InternalError", ScriptRuntime.toString(paramScriptable), paramString1, paramInt1, paramString2, paramInt2);
/*     */   }
/*     */ 
/*     */   public String details()
/*     */   {
/*  90 */     return this.errorName + ": " + this.errorMessage;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 107 */     return this.errorName;
/*     */   }
/*     */ 
/*     */   public String getErrorMessage()
/*     */   {
/* 119 */     return this.errorMessage;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getSourceName()
/*     */   {
/* 127 */     return sourceName();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getLineNumber()
/*     */   {
/* 135 */     return lineNumber();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getColumnNumber()
/*     */   {
/* 143 */     return columnNumber();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getLineSource()
/*     */   {
/* 150 */     return lineSource();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Scriptable getErrorObject()
/*     */   {
/* 159 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.EcmaError
 * JD-Core Version:    0.6.2
 */