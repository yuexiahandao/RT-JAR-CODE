/*    */ package com.sun.org.apache.xml.internal.security.keys.keyresolver;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ 
/*    */ public class InvalidKeyResolverException extends XMLSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public InvalidKeyResolverException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidKeyResolverException(String paramString)
/*    */   {
/* 54 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public InvalidKeyResolverException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 64 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public InvalidKeyResolverException(String paramString, Exception paramException)
/*    */   {
/* 75 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public InvalidKeyResolverException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 87 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.InvalidKeyResolverException
 * JD-Core Version:    0.6.2
 */