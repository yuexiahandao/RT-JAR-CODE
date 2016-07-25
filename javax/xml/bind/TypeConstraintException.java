/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class TypeConstraintException extends RuntimeException
/*     */ {
/*     */   private String errorCode;
/*     */   private Throwable linkedException;
/*     */ 
/*     */   public TypeConstraintException(String message)
/*     */   {
/*  70 */     this(message, null, null);
/*     */   }
/*     */ 
/*     */   public TypeConstraintException(String message, String errorCode)
/*     */   {
/*  81 */     this(message, errorCode, null);
/*     */   }
/*     */ 
/*     */   public TypeConstraintException(Throwable exception)
/*     */   {
/*  91 */     this(null, null, exception);
/*     */   }
/*     */ 
/*     */   public TypeConstraintException(String message, Throwable exception)
/*     */   {
/* 102 */     this(message, null, exception);
/*     */   }
/*     */ 
/*     */   public TypeConstraintException(String message, String errorCode, Throwable exception)
/*     */   {
/* 114 */     super(message);
/* 115 */     this.errorCode = errorCode;
/* 116 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */   public String getErrorCode()
/*     */   {
/* 125 */     return this.errorCode;
/*     */   }
/*     */ 
/*     */   public Throwable getLinkedException()
/*     */   {
/* 134 */     return this.linkedException;
/*     */   }
/*     */ 
/*     */   public synchronized void setLinkedException(Throwable exception)
/*     */   {
/* 145 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 153 */     return super.toString() + "\n - with linked exception:\n[" + this.linkedException.toString() + "]";
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream s)
/*     */   {
/* 166 */     if (this.linkedException != null) {
/* 167 */       this.linkedException.printStackTrace(s);
/* 168 */       s.println("--------------- linked to ------------------");
/*     */     }
/*     */ 
/* 171 */     super.printStackTrace(s);
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 180 */     printStackTrace(System.err);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.TypeConstraintException
 * JD-Core Version:    0.6.2
 */