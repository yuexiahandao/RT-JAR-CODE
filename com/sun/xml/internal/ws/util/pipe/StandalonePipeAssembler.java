/*    */ package com.sun.xml.internal.ws.util.pipe;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.pipe.ClientPipeAssemblerContext;
/*    */ import com.sun.xml.internal.ws.api.pipe.Pipe;
/*    */ import com.sun.xml.internal.ws.api.pipe.PipelineAssembler;
/*    */ import com.sun.xml.internal.ws.api.pipe.ServerPipeAssemblerContext;
/*    */ 
/*    */ public class StandalonePipeAssembler
/*    */   implements PipelineAssembler
/*    */ {
/* 86 */   private static final boolean dump = b;
/*    */ 
/*    */   @NotNull
/*    */   public Pipe createClient(ClientPipeAssemblerContext context)
/*    */   {
/* 46 */     Pipe head = context.createTransportPipe();
/* 47 */     head = context.createSecurityPipe(head);
/*    */ 
/* 49 */     if (dump)
/*    */     {
/* 52 */       head = context.createDumpPipe("client", System.out, head);
/*    */     }
/* 54 */     head = context.createWsaPipe(head);
/* 55 */     head = context.createClientMUPipe(head);
/* 56 */     return context.createHandlerPipe(head);
/*    */   }
/*    */ 
/*    */   public Pipe createServer(ServerPipeAssemblerContext context)
/*    */   {
/* 65 */     Pipe head = context.getTerminalPipe();
/* 66 */     head = context.createHandlerPipe(head);
/* 67 */     head = context.createMonitoringPipe(head);
/* 68 */     head = context.createServerMUPipe(head);
/* 69 */     head = context.createWsaPipe(head);
/* 70 */     head = context.createSecurityPipe(head);
/* 71 */     return head;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 80 */     boolean b = false;
/*    */     try {
/* 82 */       b = Boolean.getBoolean(StandalonePipeAssembler.class.getName() + ".dump");
/*    */     }
/*    */     catch (Throwable t)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.pipe.StandalonePipeAssembler
 * JD-Core Version:    0.6.2
 */