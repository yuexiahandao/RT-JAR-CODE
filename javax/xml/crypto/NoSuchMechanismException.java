/*     */ package javax.xml.crypto;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class NoSuchMechanismException extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 4189669069570660166L;
/*     */   private Throwable cause;
/*     */ 
/*     */   public NoSuchMechanismException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public NoSuchMechanismException(String paramString)
/*     */   {
/*  80 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public NoSuchMechanismException(String paramString, Throwable paramThrowable)
/*     */   {
/*  95 */     super(paramString);
/*  96 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public NoSuchMechanismException(Throwable paramThrowable)
/*     */   {
/* 109 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 110 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 123 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 131 */     super.printStackTrace();
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 142 */     super.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 153 */     super.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.NoSuchMechanismException
 * JD-Core Version:    0.6.2
 */