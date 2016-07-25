/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ServerNotRegistered extends UserException
/*    */ {
/* 13 */   public int serverId = 0;
/*    */ 
/*    */   public ServerNotRegistered()
/*    */   {
/* 17 */     super(ServerNotRegisteredHelper.id());
/*    */   }
/*    */ 
/*    */   public ServerNotRegistered(int paramInt)
/*    */   {
/* 22 */     super(ServerNotRegisteredHelper.id());
/* 23 */     this.serverId = paramInt;
/*    */   }
/*    */ 
/*    */   public ServerNotRegistered(String paramString, int paramInt)
/*    */   {
/* 29 */     super(ServerNotRegisteredHelper.id() + "  " + paramString);
/* 30 */     this.serverId = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerNotRegistered
 * JD-Core Version:    0.6.2
 */