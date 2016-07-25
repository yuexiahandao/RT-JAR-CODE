/*     */ package java.lang;
/*     */ 
/*     */ public class AssertionError extends Error
/*     */ {
/*     */   private static final long serialVersionUID = -5013299493970297370L;
/*     */ 
/*     */   public AssertionError()
/*     */   {
/*     */   }
/*     */ 
/*     */   private AssertionError(String paramString)
/*     */   {
/*  58 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public AssertionError(Object paramObject)
/*     */   {
/*  74 */     this("" + paramObject);
/*  75 */     if ((paramObject instanceof Throwable))
/*  76 */       initCause((Throwable)paramObject);
/*     */   }
/*     */ 
/*     */   public AssertionError(boolean paramBoolean)
/*     */   {
/*  88 */     this("" + paramBoolean);
/*     */   }
/*     */ 
/*     */   public AssertionError(char paramChar)
/*     */   {
/* 100 */     this("" + paramChar);
/*     */   }
/*     */ 
/*     */   public AssertionError(int paramInt)
/*     */   {
/* 112 */     this("" + paramInt);
/*     */   }
/*     */ 
/*     */   public AssertionError(long paramLong)
/*     */   {
/* 124 */     this("" + paramLong);
/*     */   }
/*     */ 
/*     */   public AssertionError(float paramFloat)
/*     */   {
/* 136 */     this("" + paramFloat);
/*     */   }
/*     */ 
/*     */   public AssertionError(double paramDouble)
/*     */   {
/* 148 */     this("" + paramDouble);
/*     */   }
/*     */ 
/*     */   public AssertionError(String paramString, Throwable paramThrowable)
/*     */   {
/* 165 */     super(paramString, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.AssertionError
 * JD-Core Version:    0.6.2
 */