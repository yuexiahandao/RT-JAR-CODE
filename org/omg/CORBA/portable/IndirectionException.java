/*    */ package org.omg.CORBA.portable;
/*    */ 
/*    */ import org.omg.CORBA.CompletionStatus;
/*    */ import org.omg.CORBA.SystemException;
/*    */ 
/*    */ public class IndirectionException extends SystemException
/*    */ {
/*    */   public int offset;
/*    */ 
/*    */   public IndirectionException(int paramInt)
/*    */   {
/* 65 */     super("", 0, CompletionStatus.COMPLETED_MAYBE);
/* 66 */     this.offset = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.portable.IndirectionException
 * JD-Core Version:    0.6.2
 */