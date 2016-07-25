/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ServerAlreadyActive extends UserException
/*    */ {
/* 13 */   public int serverId = 0;
/*    */ 
/*    */   public ServerAlreadyActive()
/*    */   {
/* 17 */     super(ServerAlreadyActiveHelper.id());
/*    */   }
/*    */ 
/*    */   public ServerAlreadyActive(int paramInt)
/*    */   {
/* 22 */     super(ServerAlreadyActiveHelper.id());
/* 23 */     this.serverId = paramInt;
/*    */   }
/*    */ 
/*    */   public ServerAlreadyActive(String paramString, int paramInt)
/*    */   {
/* 29 */     super(ServerAlreadyActiveHelper.id() + "  " + paramString);
/* 30 */     this.serverId = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerAlreadyActive
 * JD-Core Version:    0.6.2
 */