/*    */ package com.sun.org.apache.xml.internal.security.keys.keyresolver;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ 
/*    */ public class KeyResolverException extends XMLSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public KeyResolverException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public KeyResolverException(String paramString)
/*    */   {
/* 57 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public KeyResolverException(String paramString, Object[] paramArrayOfObject)
/*    */   {
/* 67 */     super(paramString, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   public KeyResolverException(String paramString, Exception paramException)
/*    */   {
/* 77 */     super(paramString, paramException);
/*    */   }
/*    */ 
/*    */   public KeyResolverException(String paramString, Object[] paramArrayOfObject, Exception paramException)
/*    */   {
/* 89 */     super(paramString, paramArrayOfObject, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException
 * JD-Core Version:    0.6.2
 */