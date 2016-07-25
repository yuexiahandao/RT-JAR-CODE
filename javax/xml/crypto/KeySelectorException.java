/*     */ package javax.xml.crypto;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class KeySelectorException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = -7480033639322531109L;
/*     */   private Throwable cause;
/*     */ 
/*     */   public KeySelectorException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public KeySelectorException(String paramString)
/*     */   {
/*  71 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public KeySelectorException(String paramString, Throwable paramThrowable)
/*     */   {
/*  86 */     super(paramString);
/*  87 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public KeySelectorException(Throwable paramThrowable)
/*     */   {
/* 101 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 102 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 115 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 123 */     super.printStackTrace();
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 134 */     super.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 145 */     super.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.KeySelectorException
 * JD-Core Version:    0.6.2
 */