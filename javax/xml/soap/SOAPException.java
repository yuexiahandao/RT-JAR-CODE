/*     */ package javax.xml.soap;
/*     */ 
/*     */ public class SOAPException extends Exception
/*     */ {
/*     */   private Throwable cause;
/*     */ 
/*     */   public SOAPException()
/*     */   {
/*  52 */     this.cause = null;
/*     */   }
/*     */ 
/*     */   public SOAPException(String reason)
/*     */   {
/*  62 */     super(reason);
/*  63 */     this.cause = null;
/*     */   }
/*     */ 
/*     */   public SOAPException(String reason, Throwable cause)
/*     */   {
/*  77 */     super(reason);
/*  78 */     initCause(cause);
/*     */   }
/*     */ 
/*     */   public SOAPException(Throwable cause)
/*     */   {
/*  86 */     super(cause.toString());
/*  87 */     initCause(cause);
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 105 */     String message = super.getMessage();
/* 106 */     if ((message == null) && (this.cause != null)) {
/* 107 */       return this.cause.getMessage();
/*     */     }
/* 109 */     return message;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 123 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public synchronized Throwable initCause(Throwable cause)
/*     */   {
/* 153 */     if (this.cause != null) {
/* 154 */       throw new IllegalStateException("Can't override cause");
/*     */     }
/* 156 */     if (cause == this) {
/* 157 */       throw new IllegalArgumentException("Self-causation not permitted");
/*     */     }
/* 159 */     this.cause = cause;
/*     */ 
/* 161 */     return this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPException
 * JD-Core Version:    0.6.2
 */