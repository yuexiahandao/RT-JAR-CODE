/*     */ package javax.xml.transform;
/*     */ 
/*     */ public class TransformerFactoryConfigurationError extends Error
/*     */ {
/*     */   private static final long serialVersionUID = -6527718720676281516L;
/*     */   private Exception exception;
/*     */ 
/*     */   public TransformerFactoryConfigurationError()
/*     */   {
/*  51 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public TransformerFactoryConfigurationError(String msg)
/*     */   {
/*  62 */     super(msg);
/*     */ 
/*  64 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public TransformerFactoryConfigurationError(Exception e)
/*     */   {
/*  76 */     super(e.toString());
/*     */ 
/*  78 */     this.exception = e;
/*     */   }
/*     */ 
/*     */   public TransformerFactoryConfigurationError(Exception e, String msg)
/*     */   {
/*  91 */     super(msg);
/*     */ 
/*  93 */     this.exception = e;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 105 */     String message = super.getMessage();
/*     */ 
/* 107 */     if ((message == null) && (this.exception != null)) {
/* 108 */       return this.exception.getMessage();
/*     */     }
/*     */ 
/* 111 */     return message;
/*     */   }
/*     */ 
/*     */   public Exception getException()
/*     */   {
/* 121 */     return this.exception;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 128 */     return this.exception;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.TransformerFactoryConfigurationError
 * JD-Core Version:    0.6.2
 */