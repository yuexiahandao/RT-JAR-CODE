/*    */ package com.sun.xml.internal.ws.util.pipe;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
/*    */ import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubelineAssembler;
/*    */ 
/*    */ public class StandaloneTubeAssembler
/*    */   implements TubelineAssembler
/*    */ {
/* 91 */   public static final boolean dump = b;
/*    */ 
/*    */   @NotNull
/*    */   public Tube createClient(ClientTubeAssemblerContext context)
/*    */   {
/* 45 */     Tube head = context.createTransportTube();
/* 46 */     head = context.createSecurityTube(head);
/* 47 */     if (dump)
/*    */     {
/* 50 */       head = context.createDumpTube("client", System.out, head);
/*    */     }
/* 52 */     head = context.createWsaTube(head);
/* 53 */     head = context.createClientMUTube(head);
/* 54 */     head = context.createValidationTube(head);
/* 55 */     return context.createHandlerTube(head);
/*    */   }
/*    */ 
/*    */   public Tube createServer(ServerTubeAssemblerContext context)
/*    */   {
/* 64 */     Tube head = context.getTerminalTube();
/* 65 */     head = context.createValidationTube(head);
/* 66 */     head = context.createHandlerTube(head);
/* 67 */     head = context.createMonitoringTube(head);
/* 68 */     head = context.createServerMUTube(head);
/* 69 */     head = context.createWsaTube(head);
/* 70 */     if (dump)
/*    */     {
/* 73 */       head = context.createDumpTube("server", System.out, head);
/*    */     }
/* 75 */     head = context.createSecurityTube(head);
/* 76 */     return head;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 85 */     boolean b = false;
/*    */     try {
/* 87 */       b = Boolean.getBoolean(StandaloneTubeAssembler.class.getName() + ".dump");
/*    */     }
/*    */     catch (Throwable t)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.pipe.StandaloneTubeAssembler
 * JD-Core Version:    0.6.2
 */