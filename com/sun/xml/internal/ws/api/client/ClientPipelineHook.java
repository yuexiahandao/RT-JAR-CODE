/*    */ package com.sun.xml.internal.ws.api.client;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.pipe.ClientPipeAssemblerContext;
/*    */ import com.sun.xml.internal.ws.api.pipe.Pipe;
/*    */ 
/*    */ public abstract class ClientPipelineHook
/*    */ {
/*    */   @NotNull
/*    */   public Pipe createSecurityPipe(ClientPipeAssemblerContext ctxt, @NotNull Pipe tail)
/*    */   {
/* 71 */     return tail;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.client.ClientPipelineHook
 * JD-Core Version:    0.6.2
 */