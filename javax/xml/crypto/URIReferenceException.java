/*     */ package javax.xml.crypto;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class URIReferenceException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 7173469703932561419L;
/*     */   private Throwable cause;
/*     */   private URIReference uriReference;
/*     */ 
/*     */   public URIReferenceException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public URIReferenceException(String paramString)
/*     */   {
/*  77 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public URIReferenceException(String paramString, Throwable paramThrowable)
/*     */   {
/*  92 */     super(paramString);
/*  93 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public URIReferenceException(String paramString, Throwable paramThrowable, URIReference paramURIReference)
/*     */   {
/* 113 */     this(paramString, paramThrowable);
/* 114 */     if (paramURIReference == null) {
/* 115 */       throw new NullPointerException("uriReference cannot be null");
/*     */     }
/* 117 */     this.uriReference = paramURIReference;
/*     */   }
/*     */ 
/*     */   public URIReferenceException(Throwable paramThrowable)
/*     */   {
/* 130 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 131 */     this.cause = paramThrowable;
/*     */   }
/*     */ 
/*     */   public URIReference getURIReference()
/*     */   {
/* 142 */     return this.uriReference;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 155 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 163 */     super.printStackTrace();
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 174 */     super.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 185 */     super.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.URIReferenceException
 * JD-Core Version:    0.6.2
 */