/*     */ package java.util;
/*     */ 
/*     */ public class ConcurrentModificationException extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = -3666751008965953603L;
/*     */ 
/*     */   public ConcurrentModificationException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ConcurrentModificationException(String paramString)
/*     */   {
/*  86 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public ConcurrentModificationException(Throwable paramThrowable)
/*     */   {
/* 101 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public ConcurrentModificationException(String paramString, Throwable paramThrowable)
/*     */   {
/* 121 */     super(paramString, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ConcurrentModificationException
 * JD-Core Version:    0.6.2
 */