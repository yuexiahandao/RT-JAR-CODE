/*    */ package com.sun.org.apache.xerces.internal.xs;
/*    */ 
/*    */ public class XSException extends RuntimeException
/*    */ {
/*    */   static final long serialVersionUID = 3111893084677917742L;
/*    */   public short code;
/*    */   public static final short NOT_SUPPORTED_ERR = 1;
/*    */   public static final short INDEX_SIZE_ERR = 2;
/*    */ 
/*    */   public XSException(short code, String message)
/*    */   {
/* 41 */     super(message);
/* 42 */     this.code = code;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSException
 * JD-Core Version:    0.6.2
 */