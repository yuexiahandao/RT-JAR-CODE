/*    */ package com.sun.org.apache.xml.internal.security.transforms;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ 
/*    */ public class TransformationException extends XMLSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public TransformationException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TransformationException(String paramString)
/*    */   {
/* 53 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public TransformationException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 63 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public TransformationException(String paramString, Exception paramException)
/*    */   {
/* 73 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public TransformationException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 85 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.TransformationException
 * JD-Core Version:    0.6.2
 */