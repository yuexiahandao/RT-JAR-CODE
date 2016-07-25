/*     */ package java.sql;
/*     */ 
/*     */ public class SQLIntegrityConstraintViolationException extends SQLNonTransientException
/*     */ {
/*     */   private static final long serialVersionUID = 8033405298774849169L;
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException(String paramString)
/*     */   {
/*  69 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException(String paramString1, String paramString2)
/*     */   {
/*  86 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 104 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException(Throwable paramThrowable)
/*     */   {
/* 121 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException(String paramString, Throwable paramThrowable)
/*     */   {
/* 137 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 153 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLIntegrityConstraintViolationException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 170 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLIntegrityConstraintViolationException
 * JD-Core Version:    0.6.2
 */