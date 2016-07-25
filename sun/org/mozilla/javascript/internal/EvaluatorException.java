/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class EvaluatorException extends RhinoException
/*     */ {
/*     */   static final long serialVersionUID = -8743165779676009808L;
/*     */ 
/*     */   public EvaluatorException(String paramString)
/*     */   {
/*  51 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public EvaluatorException(String paramString1, String paramString2, int paramInt)
/*     */   {
/*  67 */     this(paramString1, paramString2, paramInt, null, 0);
/*     */   }
/*     */ 
/*     */   public EvaluatorException(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*     */   {
/*  87 */     super(paramString1);
/*  88 */     recordErrorOrigin(paramString2, paramInt1, paramString3, paramInt2);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getSourceName()
/*     */   {
/*  96 */     return sourceName();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getLineNumber()
/*     */   {
/* 104 */     return lineNumber();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getColumnNumber()
/*     */   {
/* 112 */     return columnNumber();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getLineSource()
/*     */   {
/* 120 */     return lineSource();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.EvaluatorException
 * JD-Core Version:    0.6.2
 */