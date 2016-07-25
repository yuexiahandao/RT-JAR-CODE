/*     */ package javax.xml.crypto.dsig;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class XMLSignatureException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = -3438102491013869995L;
/*     */   private Throwable cause;
/*     */ 
/*     */   public XMLSignatureException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLSignatureException(String paramString)
/*     */   {
/*  70 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public XMLSignatureException(String paramString, Throwable paramThrowable)
/*     */   {
/*  85 */     super(paramString);
/*  86 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public XMLSignatureException(Throwable paramThrowable)
/*     */   {
/* 100 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 101 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 114 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 122 */     super.printStackTrace();
/* 123 */     if (this.cause != null)
/* 124 */       this.cause.printStackTrace();
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 135 */     super.printStackTrace(paramPrintStream);
/* 136 */     if (this.cause != null)
/* 137 */       this.cause.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 148 */     super.printStackTrace(paramPrintWriter);
/* 149 */     if (this.cause != null)
/* 150 */       this.cause.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.XMLSignatureException
 * JD-Core Version:    0.6.2
 */