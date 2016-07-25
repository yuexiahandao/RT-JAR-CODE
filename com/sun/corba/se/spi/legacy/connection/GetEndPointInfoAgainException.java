/*    */ package com.sun.corba.se.spi.legacy.connection;
/*    */ 
/*    */ import com.sun.corba.se.spi.transport.SocketInfo;
/*    */ 
/*    */ public class GetEndPointInfoAgainException extends Exception
/*    */ {
/*    */   private SocketInfo socketInfo;
/*    */ 
/*    */   public GetEndPointInfoAgainException(SocketInfo paramSocketInfo)
/*    */   {
/* 45 */     this.socketInfo = paramSocketInfo;
/*    */   }
/*    */ 
/*    */   public SocketInfo getEndPointInfo()
/*    */   {
/* 50 */     return this.socketInfo;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException
 * JD-Core Version:    0.6.2
 */