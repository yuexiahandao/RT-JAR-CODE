/*     */ package com.sun.org.apache.xml.internal.security.exceptions;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.MessageFormat;
/*     */ 
/*     */ public class XMLSecurityRuntimeException extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  50 */   protected Exception originalException = null;
/*     */   protected String msgID;
/*     */ 
/*     */   public XMLSecurityRuntimeException()
/*     */   {
/*  61 */     super("Missing message string");
/*     */ 
/*  63 */     this.msgID = null;
/*  64 */     this.originalException = null;
/*     */   }
/*     */ 
/*     */   public XMLSecurityRuntimeException(String paramString)
/*     */   {
/*  74 */     super(I18n.getExceptionMessage(paramString));
/*     */ 
/*  76 */     this.msgID = paramString;
/*  77 */     this.originalException = null;
/*     */   }
/*     */ 
/*     */   public XMLSecurityRuntimeException(String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  88 */     super(MessageFormat.format(I18n.getExceptionMessage(paramString), paramArrayOfObject));
/*     */ 
/*  90 */     this.msgID = paramString;
/*  91 */     this.originalException = null;
/*     */   }
/*     */ 
/*     */   public XMLSecurityRuntimeException(Exception paramException)
/*     */   {
/* 101 */     super("Missing message ID to locate message string in resource bundle \"com/sun/org/apache/xml/internal/security/resource/xmlsecurity\". Original Exception was a " + paramException.getClass().getName() + " and message " + paramException.getMessage());
/*     */ 
/* 107 */     this.originalException = paramException;
/*     */   }
/*     */ 
/*     */   public XMLSecurityRuntimeException(String paramString, Exception paramException)
/*     */   {
/* 118 */     super(I18n.getExceptionMessage(paramString, paramException));
/*     */ 
/* 120 */     this.msgID = paramString;
/* 121 */     this.originalException = paramException;
/*     */   }
/*     */ 
/*     */   public XMLSecurityRuntimeException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*     */   {
/* 134 */     super(MessageFormat.format(I18n.getExceptionMessage(paramString), paramArrayOfObject));
/*     */ 
/* 136 */     this.msgID = paramString;
/* 137 */     this.originalException = paramException;
/*     */   }
/*     */ 
/*     */   public String getMsgID()
/*     */   {
/* 147 */     if (this.msgID == null) {
/* 148 */       return "Missing message ID";
/*     */     }
/* 150 */     return this.msgID;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 156 */     String str1 = getClass().getName();
/* 157 */     String str2 = super.getLocalizedMessage();
/*     */ 
/* 159 */     if (str2 != null)
/* 160 */       str2 = str1 + ": " + str2;
/*     */     else {
/* 162 */       str2 = str1;
/*     */     }
/*     */ 
/* 165 */     if (this.originalException != null) {
/* 166 */       str2 = str2 + "\nOriginal Exception was " + this.originalException.toString();
/*     */     }
/*     */ 
/* 170 */     return str2;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 179 */     synchronized (System.err) {
/* 180 */       super.printStackTrace(System.err);
/*     */ 
/* 182 */       if (this.originalException != null)
/* 183 */         this.originalException.printStackTrace(System.err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 195 */     super.printStackTrace(paramPrintWriter);
/*     */ 
/* 197 */     if (this.originalException != null)
/* 198 */       this.originalException.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 209 */     super.printStackTrace(paramPrintStream);
/*     */ 
/* 211 */     if (this.originalException != null)
/* 212 */       this.originalException.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public Exception getOriginalException()
/*     */   {
/* 222 */     return this.originalException;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityRuntimeException
 * JD-Core Version:    0.6.2
 */