/*    */ package org.w3c.dom.ranges;
/*    */ 
/*    */ public class RangeException extends RuntimeException
/*    */ {
/*    */   public short code;
/*    */   public static final short BAD_BOUNDARYPOINTS_ERR = 1;
/*    */   public static final short INVALID_NODE_TYPE_ERR = 2;
/*    */ 
/*    */   public RangeException(short code, String message)
/*    */   {
/* 52 */     super(message);
/* 53 */     this.code = code;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ranges.RangeException
 * JD-Core Version:    0.6.2
 */