/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ServerHeldDown extends UserException
/*    */ {
/* 13 */   public int serverId = 0;
/*    */ 
/*    */   public ServerHeldDown()
/*    */   {
/* 17 */     super(ServerHeldDownHelper.id());
/*    */   }
/*    */ 
/*    */   public ServerHeldDown(int paramInt)
/*    */   {
/* 22 */     super(ServerHeldDownHelper.id());
/* 23 */     this.serverId = paramInt;
/*    */   }
/*    */ 
/*    */   public ServerHeldDown(String paramString, int paramInt)
/*    */   {
/* 29 */     super(ServerHeldDownHelper.id() + "  " + paramString);
/* 30 */     this.serverId = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerHeldDown
 * JD-Core Version:    0.6.2
 */