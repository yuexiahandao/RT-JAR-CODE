/*    */ package org.w3c.dom.xpath;
/*    */ 
/*    */ public class XPathException extends RuntimeException
/*    */ {
/*    */   public short code;
/*    */   public static final short INVALID_EXPRESSION_ERR = 1;
/*    */   public static final short TYPE_ERR = 2;
/*    */ 
/*    */   public XPathException(short code, String message)
/*    */   {
/* 51 */     super(message);
/* 52 */     this.code = code;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.xpath.XPathException
 * JD-Core Version:    0.6.2
 */