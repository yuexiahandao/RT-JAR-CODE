/*    */ package com.sun.org.apache.xml.internal.security.c14n;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ 
/*    */ public class InvalidCanonicalizerException extends XMLSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public InvalidCanonicalizerException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidCanonicalizerException(String paramString)
/*    */   {
/* 53 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public InvalidCanonicalizerException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 63 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public InvalidCanonicalizerException(String paramString, Exception paramException)
/*    */   {
/* 74 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public InvalidCanonicalizerException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 86 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException
 * JD-Core Version:    0.6.2
 */