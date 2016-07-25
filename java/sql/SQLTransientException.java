/*     */ package java.sql;
/*     */ 
/*     */ public class SQLTransientException extends SQLException
/*     */ {
/*     */   private static final long serialVersionUID = -9042733978262274539L;
/*     */ 
/*     */   public SQLTransientException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLTransientException(String paramString)
/*     */   {
/*  67 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLTransientException(String paramString1, String paramString2)
/*     */   {
/*  84 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLTransientException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 102 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLTransientException(Throwable paramThrowable)
/*     */   {
/* 119 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransientException(String paramString, Throwable paramThrowable)
/*     */   {
/* 135 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransientException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 151 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransientException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 168 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLTransientException
 * JD-Core Version:    0.6.2
 */