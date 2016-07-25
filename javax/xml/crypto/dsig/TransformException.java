/*     */ package javax.xml.crypto.dsig;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class TransformException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 5082634801360427800L;
/*     */   private Throwable cause;
/*     */ 
/*     */   public TransformException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TransformException(String paramString)
/*     */   {
/*  73 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public TransformException(String paramString, Throwable paramThrowable)
/*     */   {
/*  88 */     super(paramString);
/*  89 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public TransformException(Throwable paramThrowable)
/*     */   {
/* 103 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 104 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 117 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 125 */     super.printStackTrace();
/* 126 */     if (this.cause != null)
/* 127 */       this.cause.printStackTrace();
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 138 */     super.printStackTrace(paramPrintStream);
/* 139 */     if (this.cause != null)
/* 140 */       this.cause.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 151 */     super.printStackTrace(paramPrintWriter);
/* 152 */     if (this.cause != null)
/* 153 */       this.cause.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.TransformException
 * JD-Core Version:    0.6.2
 */