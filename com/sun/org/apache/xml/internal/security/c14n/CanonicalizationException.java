/*    */ package com.sun.org.apache.xml.internal.security.c14n;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ 
/*    */ public class CanonicalizationException extends XMLSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public CanonicalizationException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CanonicalizationException(String paramString)
/*    */   {
/* 54 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public CanonicalizationException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 64 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public CanonicalizationException(String paramString, Exception paramException)
/*    */   {
/* 74 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public CanonicalizationException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 86 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException
 * JD-Core Version:    0.6.2
 */