/*     */ package java.sql;
/*     */ 
/*     */ public class SQLNonTransientException extends SQLException
/*     */ {
/*     */   private static final long serialVersionUID = -9104382843534716547L;
/*     */ 
/*     */   public SQLNonTransientException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLNonTransientException(String paramString)
/*     */   {
/*  69 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientException(String paramString1, String paramString2)
/*     */   {
/*  86 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 104 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientException(Throwable paramThrowable)
/*     */   {
/* 121 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientException(String paramString, Throwable paramThrowable)
/*     */   {
/* 137 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 154 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLNonTransientException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 171 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLNonTransientException
 * JD-Core Version:    0.6.2
 */