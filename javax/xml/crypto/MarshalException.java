/*     */ package javax.xml.crypto;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class MarshalException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = -863185580332643547L;
/*     */   private Throwable cause;
/*     */ 
/*     */   public MarshalException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MarshalException(String paramString)
/*     */   {
/*  79 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public MarshalException(String paramString, Throwable paramThrowable)
/*     */   {
/*  94 */     super(paramString);
/*  95 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public MarshalException(Throwable paramThrowable)
/*     */   {
/* 108 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 109 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 122 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 130 */     super.printStackTrace();
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 141 */     super.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 152 */     super.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.MarshalException
 * JD-Core Version:    0.6.2
 */