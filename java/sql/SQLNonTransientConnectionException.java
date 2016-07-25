/*     */ package java.sql;
/*     */ 
/*     */ public class SQLNonTransientConnectionException extends SQLNonTransientException
/*     */ {
/*     */   private static final long serialVersionUID = -5852318857474782892L;
/*     */ 
/*     */   public SQLNonTransientConnectionException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLNonTransientConnectionException(String paramString)
/*     */   {
/*  70 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientConnectionException(String paramString1, String paramString2)
/*     */   {
/*  87 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientConnectionException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 105 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientConnectionException(Throwable paramThrowable)
/*     */   {
/* 122 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientConnectionException(String paramString, Throwable paramThrowable)
/*     */   {
/* 138 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientConnectionException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 154 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientConnectionException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 171 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLNonTransientConnectionException
 * JD-Core Version:    0.6.2
 */