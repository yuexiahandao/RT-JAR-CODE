/*    */ package com.sun.xml.internal.ws.api.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.pipe.Pipe;
/*    */ import com.sun.xml.internal.ws.api.pipe.ServerPipeAssemblerContext;
/*    */ 
/*    */ public abstract class ServerPipelineHook
/*    */ {
/*    */   @NotNull
/*    */   public Pipe createMonitoringPipe(ServerPipeAssemblerContext ctxt, @NotNull Pipe tail)
/*    */   {
/* 71 */     return tail;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public Pipe createSecurityPipe(ServerPipeAssemblerContext ctxt, @NotNull Pipe tail)
/*    */   {
/* 97 */     return tail;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.ServerPipelineHook
 * JD-Core Version:    0.6.2
 */