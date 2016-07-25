/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ /** @deprecated */
/*     */ public final class ClientPipeAssemblerContext extends ClientTubeAssemblerContext
/*     */ {
/*     */   public ClientPipeAssemblerContext(@NotNull EndpointAddress address, @NotNull WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding)
/*     */   {
/*  49 */     this(address, wsdlModel, rootOwner, binding, Container.NONE);
/*     */   }
/*     */ 
/*     */   public ClientPipeAssemblerContext(@NotNull EndpointAddress address, @NotNull WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container)
/*     */   {
/*  55 */     super(address, wsdlModel, rootOwner, binding, container);
/*     */   }
/*     */ 
/*     */   public Pipe createDumpPipe(String name, PrintStream out, Pipe next)
/*     */   {
/*  62 */     return PipeAdapter.adapt(super.createDumpTube(name, out, PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   public Pipe createWsaPipe(Pipe next)
/*     */   {
/*  70 */     return PipeAdapter.adapt(super.createWsaTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   public Pipe createClientMUPipe(Pipe next)
/*     */   {
/*  78 */     return PipeAdapter.adapt(super.createClientMUTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   public Pipe createValidationPipe(Pipe next)
/*     */   {
/*  85 */     return PipeAdapter.adapt(super.createValidationTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   public Pipe createHandlerPipe(Pipe next)
/*     */   {
/*  92 */     return PipeAdapter.adapt(super.createHandlerTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Pipe createSecurityPipe(@NotNull Pipe next)
/*     */   {
/*  99 */     return PipeAdapter.adapt(super.createSecurityTube(PipeAdapter.adapt(next)));
/*     */   }
/*     */ 
/*     */   public Pipe createTransportPipe()
/*     */   {
/* 106 */     return PipeAdapter.adapt(super.createTransportTube());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.ClientPipeAssemblerContext
 * JD-Core Version:    0.6.2
 */