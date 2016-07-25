/*     */ package java.sql;
/*     */ 
/*     */ public class SQLDataException extends SQLNonTransientException
/*     */ {
/*     */   private static final long serialVersionUID = -6889123282670549800L;
/*     */ 
/*     */   public SQLDataException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SQLDataException(String paramString)
/*     */   {
/*  71 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLDataException(String paramString1, String paramString2)
/*     */   {
/*  88 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public SQLDataException(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 106 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public SQLDataException(Throwable paramThrowable)
/*     */   {
/* 123 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLDataException(String paramString, Throwable paramThrowable)
/*     */   {
/* 138 */     super(paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLDataException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 153 */     super(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public SQLDataException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 169 */     super(paramString1, paramString2, paramInt, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLDataException
 * JD-Core Version:    0.6.2
 */