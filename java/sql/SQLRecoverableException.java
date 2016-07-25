/*     */ package java.sql;
/*     */ 
/*     */ public class SQLRecoverableException extends SQLException
/*     */ {
/*     */   private static final long serialVersionUID = -4144386502923131579L;
/*     */ 
/*     */   public SQLRecoverableException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLRecoverableException(String paramString)
/*     */   {
/*  70 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLRecoverableException(String paramString1, String paramString2)
/*     */   {
/*  87 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLRecoverableException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 105 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLRecoverableException(Throwable paramThrowable)
/*     */   {
/* 122 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLRecoverableException(String paramString, Throwable paramThrowable)
/*     */   {
/* 138 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLRecoverableException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 154 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLRecoverableException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 171 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLRecoverableException
 * JD-Core Version:    0.6.2
 */