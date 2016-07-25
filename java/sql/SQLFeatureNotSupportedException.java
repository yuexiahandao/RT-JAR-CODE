/*     */ package java.sql;
/*     */ 
/*     */ public class SQLFeatureNotSupportedException extends SQLNonTransientException
/*     */ {
/*     */   private static final long serialVersionUID = -1026510870282316051L;
/*     */ 
/*     */   public SQLFeatureNotSupportedException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLFeatureNotSupportedException(String paramString)
/*     */   {
/*  74 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLFeatureNotSupportedException(String paramString1, String paramString2)
/*     */   {
/*  91 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLFeatureNotSupportedException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 109 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLFeatureNotSupportedException(Throwable paramThrowable)
/*     */   {
/* 126 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLFeatureNotSupportedException(String paramString, Throwable paramThrowable)
/*     */   {
/* 142 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLFeatureNotSupportedException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 158 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLFeatureNotSupportedException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 175 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLFeatureNotSupportedException
 * JD-Core Version:    0.6.2
 */