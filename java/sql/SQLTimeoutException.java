/*     */ package java.sql;
/*     */ 
/*     */ public class SQLTimeoutException extends SQLTransientException
/*     */ {
/*     */   private static final long serialVersionUID = -4487171280562520262L;
/*     */ 
/*     */   public SQLTimeoutException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLTimeoutException(String paramString)
/*     */   {
/*  66 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLTimeoutException(String paramString1, String paramString2)
/*     */   {
/*  83 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLTimeoutException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 101 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLTimeoutException(Throwable paramThrowable)
/*     */   {
/* 118 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTimeoutException(String paramString, Throwable paramThrowable)
/*     */   {
/* 134 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTimeoutException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 150 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTimeoutException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 167 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLTimeoutException
 * JD-Core Version:    0.6.2
 */