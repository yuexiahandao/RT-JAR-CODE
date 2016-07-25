/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ /** @deprecated */
/*     */ public final class ServerPipeAssemblerContext extends ServerTubeAssemblerContext
/*     */ {
/*     */   public ServerPipeAssemblerContext(@Nullable SEIModel seiModel, @Nullable WSDLPort wsdlModel, @NotNull WSEndpoint endpoint, @NotNull Tube terminal, boolean isSynchronous)
/*     */   {
/*  48 */     super(seiModel, wsdlModel, endpoint, terminal, isSynchronous);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Pipe createServerMUPipe(@NotNull Pipe next)
/*     */   {
/*  56 */     return PipeAdapter.adapt(super.createServerMUTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   public Pipe createDumpPipe(String name, PrintStream out, Pipe next)
/*     */   {
/*  63 */     return PipeAdapter.adapt(super.createDumpTube(name, out, PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Pipe createMonitoringPipe(@NotNull Pipe next)
/*     */   {
/*  71 */     return PipeAdapter.adapt(super.createMonitoringTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Pipe createSecurityPipe(@NotNull Pipe next)
/*     */   {
/*  78 */     return PipeAdapter.adapt(super.createSecurityTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Pipe createValidationPipe(@NotNull Pipe next)
/*     */   {
/*  85 */     return PipeAdapter.adapt(super.createValidationTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Pipe createHandlerPipe(@NotNull Pipe next)
/*     */   {
/*  92 */     return PipeAdapter.adapt(super.createHandlerTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Pipe getTerminalPipe()
/*     */   {
/* 106 */     return PipeAdapter.adapt(super.getTerminalTube());
/*     */   }
/*     */ 
/*     */   public Pipe createWsaPipe(Pipe next)
/*     */   {
/* 113 */     return PipeAdapter.adapt(super.createWsaTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.ServerPipeAssemblerContext
 * JD-Core Version:    0.6.2
 */