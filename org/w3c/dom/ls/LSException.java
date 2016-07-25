/*    */ package org.w3c.dom.ls;
/*    */ 
/*    */ public class LSException extends RuntimeException
/*    */ {
/*    */   public short code;
/*    */   public static final short PARSE_ERR = 81;
/*    */   public static final short SERIALIZE_ERR = 82;
/*    */ 
/*    */   public LSException(short code, String message)
/*    */   {
/* 60 */     super(message);
/* 61 */     this.code = code;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ls.LSException
 * JD-Core Version:    0.6.2
 */