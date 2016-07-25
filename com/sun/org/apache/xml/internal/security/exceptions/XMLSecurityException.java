/*     */ package com.sun.org.apache.xml.internal.security.exceptions;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.MessageFormat;
/*     */ 
/*     */ public class XMLSecurityException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  75 */   protected Exception originalException = null;
/*     */   protected String msgID;
/*     */ 
/*     */   public XMLSecurityException()
/*     */   {
/*  86 */     super("Missing message string");
/*     */ 
/*  88 */     this.msgID = null;
/*  89 */     this.originalException = null;
/*     */   }
/*     */ 
/*     */   public XMLSecurityException(String paramString)
/*     */   {
/*  99 */     super(I18n.getExceptionMessage(paramString));
/*     */ 
/* 101 */     this.msgID = paramString;
/* 102 */     this.originalException = null;
/*     */   }
/*     */ 
/*     */   public XMLSecurityException(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 113 */     super(MessageFormat.format(I18n.getExceptionMessage(paramString), paramArrayOfObject));
/*     */ 
/* 115 */     this.msgID = paramString;
/* 116 */     this.originalException = null;
/*     */   }
/*     */ 
/*     */   public XMLSecurityException(Exception paramException)
/*     */   {
/* 126 */     super("Missing message ID to locate message string in resource bundle \"com/sun/org/apache/xml/internal/security/resource/xmlsecurity\". Original Exception was a " + paramException.getClass().getName() + " and message " + paramException.getMessage());
/*     */ 
/* 132 */     this.originalException = paramException;
/*     */   }
/*     */ 
/*     */   public XMLSecurityException(String paramString, Exception paramException)
/*     */   {
/* 143 */     super(I18n.getExceptionMessage(paramString, paramException));
/*     */ 
/* 145 */     this.msgID = paramString;
/* 146 */     this.originalException = paramException;
/*     */   }
/*     */ 
/*     */   public XMLSecurityException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*     */   {
/* 159 */     super(MessageFormat.format(I18n.getExceptionMessage(paramString), paramArrayOfObject));
/*     */ 
/* 161 */     this.msgID = paramString;
/* 162 */     this.originalException = paramException;
/*     */   }
/*     */ 
/*     */   public String getMsgID()
/*     */   {
/* 172 */     if (this.msgID == null) {
/* 173 */       return "Missing message ID";
/*     */     }
/* 175 */     return this.msgID;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 181 */     String str1 = getClass().getName();
/* 182 */     String str2 = super.getLocalizedMessage();
/*     */ 
/* 184 */     if (str2 != null)
/* 185 */       str2 = str1 + ": " + str2;
/*     */     else {
/* 187 */       str2 = str1;
/*     */     }
/*     */ 
/* 190 */     if (this.originalException != null) {
/* 191 */       str2 = str2 + "\nOriginal Exception was " + this.originalException.toString();
/*     */     }
/*     */ 
/* 195 */     return str2;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 204 */     synchronized (System.err) {
/* 205 */       super.printStackTrace(System.err);
/*     */ 
/* 207 */       if (this.originalException != null)
/* 208 */         this.originalException.printStackTrace(System.err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter paramPrintWriter)
/*     */   {
/* 220 */     super.printStackTrace(paramPrintWriter);
/*     */ 
/* 222 */     if (this.originalException != null)
/* 223 */       this.originalException.printStackTrace(paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream paramPrintStream)
/*     */   {
/* 234 */     super.printStackTrace(paramPrintStream);
/*     */ 
/* 236 */     if (this.originalException != null)
/* 237 */       this.originalException.printStackTrace(paramPrintStream);
/*     */   }
/*     */ 
/*     */   public Exception getOriginalException()
/*     */   {
/* 247 */     return this.originalException;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException
 * JD-Core Version:    0.6.2
 */