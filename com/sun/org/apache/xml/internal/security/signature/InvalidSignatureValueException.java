/*    */ package com.sun.org.apache.xml.internal.security.signature;
/*    */ 
/*    */ public class InvalidSignatureValueException extends XMLSignatureException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public InvalidSignatureValueException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidSignatureValueException(String paramString)
/*    */   {
/* 52 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public InvalidSignatureValueException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 62 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public InvalidSignatureValueException(String paramString, Exception paramException)
/*    */   {
/* 73 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public InvalidSignatureValueException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 85 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.InvalidSignatureValueException
 * JD-Core Version:    0.6.2
 */