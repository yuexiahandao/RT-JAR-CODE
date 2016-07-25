/*     */ package java.sql;
/*     */ 
/*     */ public class SQLSyntaxErrorException extends SQLNonTransientException
/*     */ {
/*     */   private static final long serialVersionUID = -1843832610477496053L;
/*     */ 
/*     */   public SQLSyntaxErrorException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLSyntaxErrorException(String paramString)
/*     */   {
/*  68 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLSyntaxErrorException(String paramString1, String paramString2)
/*     */   {
/*  85 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLSyntaxErrorException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 103 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLSyntaxErrorException(Throwable paramThrowable)
/*     */   {
/* 120 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLSyntaxErrorException(String paramString, Throwable paramThrowable)
/*     */   {
/* 136 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLSyntaxErrorException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 152 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLSyntaxErrorException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 169 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLSyntaxErrorException
 * JD-Core Version:    0.6.2
 */