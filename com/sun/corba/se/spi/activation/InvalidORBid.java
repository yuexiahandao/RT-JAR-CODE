/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class InvalidORBid extends UserException
/*    */ {
/*    */   public InvalidORBid()
/*    */   {
/* 16 */     super(InvalidORBidHelper.id());
/*    */   }
/*    */ 
/*    */   public InvalidORBid(String paramString)
/*    */   {
/* 22 */     super(InvalidORBidHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.InvalidORBid
 * JD-Core Version:    0.6.2
 */