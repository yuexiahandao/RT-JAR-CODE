/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class JAXBException extends Exception
/*     */ {
/*     */   private String errorCode;
/*     */   private Throwable linkedException;
/*     */   static final long serialVersionUID = -5621384651494307979L;
/*     */ 
/*     */   public JAXBException(String message)
/*     */   {
/*  62 */     this(message, null, null);
/*     */   }
/*     */ 
/*     */   public JAXBException(String message, String errorCode)
/*     */   {
/*  73 */     this(message, errorCode, null);
/*     */   }
/*     */ 
/*     */   public JAXBException(Throwable exception)
/*     */   {
/*  83 */     this(null, null, exception);
/*     */   }
/*     */ 
/*     */   public JAXBException(String message, Throwable exception)
/*     */   {
/*  94 */     this(message, null, exception);
/*     */   }
/*     */ 
/*     */   public JAXBException(String message, String errorCode, Throwable exception)
/*     */   {
/* 106 */     super(message);
/* 107 */     this.errorCode = errorCode;
/* 108 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */   public String getErrorCode()
/*     */   {
/* 117 */     return this.errorCode;
/*     */   }
/*     */ 
/*     */   public Throwable getLinkedException()
/*     */   {
/* 126 */     return this.linkedException;
/*     */   }
/*     */ 
/*     */   public synchronized void setLinkedException(Throwable exception)
/*     */   {
/* 137 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 145 */     return super.toString() + "\n - with linked exception:\n[" + this.linkedException.toString() + "]";
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream s)
/*     */   {
/* 158 */     super.printStackTrace(s);
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 167 */     super.printStackTrace();
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter s)
/*     */   {
/* 177 */     super.printStackTrace(s);
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 182 */     return this.linkedException;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.JAXBException
 * JD-Core Version:    0.6.2
 */