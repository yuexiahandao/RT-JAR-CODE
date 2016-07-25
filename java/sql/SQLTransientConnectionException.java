/*     */ package java.sql;
/*     */ 
/*     */ public class SQLTransientConnectionException extends SQLTransientException
/*     */ {
/*     */   private static final long serialVersionUID = -2520155553543391200L;
/*     */ 
/*     */   public SQLTransientConnectionException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLTransientConnectionException(String paramString)
/*     */   {
/*  69 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLTransientConnectionException(String paramString1, String paramString2)
/*     */   {
/*  86 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLTransientConnectionException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 104 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLTransientConnectionException(Throwable paramThrowable)
/*     */   {
/* 121 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransientConnectionException(String paramString, Throwable paramThrowable)
/*     */   {
/* 137 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransientConnectionException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 153 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLTransientConnectionException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 170 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLTransientConnectionException
 * JD-Core Version:    0.6.2
 */