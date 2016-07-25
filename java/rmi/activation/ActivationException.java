/*     */ package java.rmi.activation;
/*     */ 
/*     */ public class ActivationException extends Exception
/*     */ {
/*     */   public Throwable detail;
/*     */   private static final long serialVersionUID = -4320118837291406071L;
/*     */ 
/*     */   public ActivationException()
/*     */   {
/*  65 */     initCause(null);
/*     */   }
/*     */ 
/*     */   public ActivationException(String paramString)
/*     */   {
/*  75 */     super(paramString);
/*  76 */     initCause(null);
/*     */   }
/*     */ 
/*     */   public ActivationException(String paramString, Throwable paramThrowable)
/*     */   {
/*  88 */     super(paramString);
/*  89 */     initCause(null);
/*  90 */     this.detail = paramThrowable;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 100 */     if (this.detail == null) {
/* 101 */       return super.getMessage();
/*     */     }
/* 103 */     return super.getMessage() + "; nested exception is: \n\t" + this.detail.toString();
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 116 */     return this.detail;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationException
 * JD-Core Version:    0.6.2
 */