/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ServerNotActive extends UserException
/*    */ {
/* 13 */   public int serverId = 0;
/*    */ 
/*    */   public ServerNotActive()
/*    */   {
/* 17 */     super(ServerNotActiveHelper.id());
/*    */   }
/*    */ 
/*    */   public ServerNotActive(int paramInt)
/*    */   {
/* 22 */     super(ServerNotActiveHelper.id());
/* 23 */     this.serverId = paramInt;
/*    */   }
/*    */ 
/*    */   public ServerNotActive(String paramString, int paramInt)
/*    */   {
/* 29 */     super(ServerNotActiveHelper.id() + "  " + paramString);
/* 30 */     this.serverId = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerNotActive
 * JD-Core Version:    0.6.2
 */