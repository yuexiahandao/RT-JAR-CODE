/*    */ package com.sun.org.apache.xml.internal.security.encryption;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ 
/*    */ public class XMLEncryptionException extends XMLSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public XMLEncryptionException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public XMLEncryptionException(String paramString)
/*    */   {
/* 45 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public XMLEncryptionException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 53 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public XMLEncryptionException(String paramString, Exception paramException)
/*    */   {
/* 62 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public XMLEncryptionException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 72 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.encryption.XMLEncryptionException
 * JD-Core Version:    0.6.2
 */