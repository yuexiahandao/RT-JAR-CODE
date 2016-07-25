/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class NoSuchEndPoint extends UserException
/*    */ {
/*    */   public NoSuchEndPoint()
/*    */   {
/* 16 */     super(NoSuchEndPointHelper.id());
/*    */   }
/*    */ 
/*    */   public NoSuchEndPoint(String paramString)
/*    */   {
/* 22 */     super(NoSuchEndPointHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.NoSuchEndPoint
 * JD-Core Version:    0.6.2
 */