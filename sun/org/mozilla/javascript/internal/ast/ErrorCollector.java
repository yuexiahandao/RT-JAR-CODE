/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.org.mozilla.javascript.internal.EvaluatorException;
/*     */ 
/*     */ public class ErrorCollector
/*     */   implements IdeErrorReporter
/*     */ {
/*  56 */   private List<ParseProblem> errors = new ArrayList();
/*     */ 
/*     */   public void warning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*     */   {
/*  65 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void warning(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*     */   {
/*  73 */     this.errors.add(new ParseProblem(ParseProblem.Type.Warning, paramString1, paramString2, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public void error(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*     */   {
/*  86 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void error(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*     */   {
/*  95 */     this.errors.add(new ParseProblem(ParseProblem.Type.Error, paramString1, paramString2, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public EvaluatorException runtimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*     */   {
/* 107 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public List<ParseProblem> getErrors()
/*     */   {
/* 114 */     return this.errors;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 119 */     StringBuilder localStringBuilder = new StringBuilder(this.errors.size() * 100);
/* 120 */     for (ParseProblem localParseProblem : this.errors) {
/* 121 */       localStringBuilder.append(localParseProblem.toString()).append("\n");
/*     */     }
/* 123 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ErrorCollector
 * JD-Core Version:    0.6.2
 */