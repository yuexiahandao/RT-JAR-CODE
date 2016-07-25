/*     */ package com.sun.xml.internal.messaging.saaj;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import javax.xml.soap.SOAPException;
/*     */ 
/*     */ public class SOAPExceptionImpl extends SOAPException
/*     */ {
/*     */   private Throwable cause;
/*     */ 
/*     */   public SOAPExceptionImpl()
/*     */   {
/*  57 */     this.cause = null;
/*     */   }
/*     */ 
/*     */   public SOAPExceptionImpl(String reason)
/*     */   {
/*  67 */     super(reason);
/*  68 */     this.cause = null;
/*     */   }
/*     */ 
/*     */   public SOAPExceptionImpl(String reason, Throwable cause)
/*     */   {
/*  82 */     super(reason);
/*  83 */     initCause(cause);
/*     */   }
/*     */ 
/*     */   public SOAPExceptionImpl(Throwable cause)
/*     */   {
/*  91 */     super(cause.toString());
/*  92 */     initCause(cause);
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 110 */     String message = super.getMessage();
/* 111 */     if ((message == null) && (this.cause != null)) {
/* 112 */       return this.cause.getMessage();
/*     */     }
/* 114 */     return message;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 128 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public synchronized Throwable initCause(Throwable cause)
/*     */   {
/* 162 */     if (this.cause != null) {
/* 163 */       throw new IllegalStateException("Can't override cause");
/*     */     }
/* 165 */     if (cause == this) {
/* 166 */       throw new IllegalArgumentException("Self-causation not permitted");
/*     */     }
/* 168 */     this.cause = cause;
/*     */ 
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */   public void printStackTrace() {
/* 174 */     super.printStackTrace();
/* 175 */     if (this.cause != null) {
/* 176 */       System.err.println("\nCAUSE:\n");
/* 177 */       this.cause.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream s) {
/* 182 */     super.printStackTrace(s);
/* 183 */     if (this.cause != null) {
/* 184 */       s.println("\nCAUSE:\n");
/* 185 */       this.cause.printStackTrace(s);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter s) {
/* 190 */     super.printStackTrace(s);
/* 191 */     if (this.cause != null) {
/* 192 */       s.println("\nCAUSE:\n");
/* 193 */       this.cause.printStackTrace(s);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl
 * JD-Core Version:    0.6.2
 */