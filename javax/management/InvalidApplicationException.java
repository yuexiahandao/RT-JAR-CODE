/*    */ package javax.management;
/*    */ 
/*    */ public class InvalidApplicationException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -3048022274675537269L;
/*    */   private Object val;
/*    */ 
/*    */   public InvalidApplicationException(Object paramObject)
/*    */   {
/* 56 */     this.val = paramObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.InvalidApplicationException
 * JD-Core Version:    0.6.2
 */