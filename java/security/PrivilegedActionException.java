/*     */ package java.security;
/*     */ 
/*     */ public class PrivilegedActionException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 4724086851538908602L;
/*     */   private Exception exception;
/*     */ 
/*     */   public PrivilegedActionException(Exception paramException)
/*     */   {
/*  66 */     super((Throwable)null);
/*  67 */     this.exception = paramException;
/*     */   }
/*     */ 
/*     */   public Exception getException()
/*     */   {
/*  86 */     return this.exception;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/*  98 */     return this.exception;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 102 */     String str = getClass().getName();
/* 103 */     return this.exception != null ? str + ": " + this.exception.toString() : str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.PrivilegedActionException
 * JD-Core Version:    0.6.2
 */