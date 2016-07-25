/*     */ package org.xml.sax;
/*     */ 
/*     */ public class SAXException extends Exception
/*     */ {
/*     */   private Exception exception;
/*     */   static final long serialVersionUID = 583241635256073760L;
/*     */ 
/*     */   public SAXException()
/*     */   {
/*  71 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public SAXException(String message)
/*     */   {
/*  81 */     super(message);
/*  82 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public SAXException(Exception e)
/*     */   {
/*  98 */     this.exception = e;
/*     */   }
/*     */ 
/*     */   public SAXException(String message, Exception e)
/*     */   {
/* 113 */     super(message);
/* 114 */     this.exception = e;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 129 */     String message = super.getMessage();
/*     */ 
/* 131 */     if ((message == null) && (this.exception != null)) {
/* 132 */       return this.exception.getMessage();
/*     */     }
/* 134 */     return message;
/*     */   }
/*     */ 
/*     */   public Exception getException()
/*     */   {
/* 146 */     return this.exception;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 155 */     return this.exception;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 165 */     if (this.exception != null) {
/* 166 */       return super.toString() + "\n" + this.exception.toString();
/*     */     }
/* 168 */     return super.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.SAXException
 * JD-Core Version:    0.6.2
 */