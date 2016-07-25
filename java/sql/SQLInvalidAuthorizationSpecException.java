/*     */ package java.sql;
/*     */ 
/*     */ public class SQLInvalidAuthorizationSpecException extends SQLNonTransientException
/*     */ {
/*     */   private static final long serialVersionUID = -64105250450891498L;
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException(String paramString)
/*     */   {
/*  69 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException(String paramString1, String paramString2)
/*     */   {
/*  86 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 104 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException(Throwable paramThrowable)
/*     */   {
/* 121 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException(String paramString, Throwable paramThrowable)
/*     */   {
/* 137 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 153 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLInvalidAuthorizationSpecException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 170 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLInvalidAuthorizationSpecException
 * JD-Core Version:    0.6.2
 */