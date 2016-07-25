/*     */ package java.sql;
/*     */ 
/*     */ public class SQLTransactionRollbackException extends SQLTransientException
/*     */ {
/*     */   private static final long serialVersionUID = 5246680841170837229L;
/*     */ 
/*     */   public SQLTransactionRollbackException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLTransactionRollbackException(String paramString)
/*     */   {
/*  68 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLTransactionRollbackException(String paramString1, String paramString2)
/*     */   {
/*  85 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLTransactionRollbackException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 103 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLTransactionRollbackException(Throwable paramThrowable)
/*     */   {
/* 120 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransactionRollbackException(String paramString, Throwable paramThrowable)
/*     */   {
/* 136 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransactionRollbackException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 152 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransactionRollbackException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 169 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLTransactionRollbackException
 * JD-Core Version:    0.6.2
 */