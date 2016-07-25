/*    */ package com.sun.org.apache.xml.internal.security.signature;
/*    */ 
/*    */ public class InvalidDigestValueException extends XMLSignatureException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public InvalidDigestValueException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidDigestValueException(String paramString)
/*    */   {
/* 51 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public InvalidDigestValueException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 61 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public InvalidDigestValueException(String paramString, Exception paramException)
/*    */   {
/* 72 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public InvalidDigestValueException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 84 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.InvalidDigestValueException
 * JD-Core Version:    0.6.2
 */