/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime;
/*     */ 
/*     */ public class MessagingException extends Exception
/*     */ {
/*     */   private Exception next;
/*     */ 
/*     */   public MessagingException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MessagingException(String s)
/*     */   {
/*  63 */     super(s);
/*     */   }
/*     */ 
/*     */   public MessagingException(String s, Exception e)
/*     */   {
/*  76 */     super(s);
/*  77 */     this.next = e;
/*     */   }
/*     */ 
/*     */   public Exception getNextException()
/*     */   {
/*  88 */     return this.next;
/*     */   }
/*     */ 
/*     */   public synchronized boolean setNextException(Exception ex)
/*     */   {
/* 101 */     Exception theEnd = this;
/* 102 */     while (((theEnd instanceof MessagingException)) && (((MessagingException)theEnd).next != null))
/*     */     {
/* 104 */       theEnd = ((MessagingException)theEnd).next;
/*     */     }
/*     */ 
/* 108 */     if ((theEnd instanceof MessagingException)) {
/* 109 */       ((MessagingException)theEnd).next = ex;
/* 110 */       return true;
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 120 */     if (this.next == null)
/* 121 */       return super.getMessage();
/* 122 */     Exception n = this.next;
/* 123 */     String s = super.getMessage();
/* 124 */     StringBuffer sb = new StringBuffer(s == null ? "" : s);
/* 125 */     while (n != null) {
/* 126 */       sb.append(";\n  nested exception is:\n\t");
/* 127 */       if ((n instanceof MessagingException)) {
/* 128 */         MessagingException mex = (MessagingException)n;
/* 129 */         sb.append(n.getClass().toString());
/* 130 */         String msg = mex.getSuperMessage();
/* 131 */         if (msg != null) {
/* 132 */           sb.append(": ");
/* 133 */           sb.append(msg);
/*     */         }
/* 135 */         n = mex.next;
/*     */       } else {
/* 137 */         sb.append(n.toString());
/* 138 */         n = null;
/*     */       }
/*     */     }
/* 141 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String getSuperMessage() {
/* 145 */     return super.getMessage();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException
 * JD-Core Version:    0.6.2
 */