/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.addressing.W3CWsaServerTube;
/*     */ import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionWsaServerTube;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.ServerPipelineHook;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.developer.SchemaValidationFeature;
/*     */ import com.sun.xml.internal.ws.handler.HandlerTube;
/*     */ import com.sun.xml.internal.ws.handler.ServerLogicalHandlerTube;
/*     */ import com.sun.xml.internal.ws.handler.ServerMessageHandlerTube;
/*     */ import com.sun.xml.internal.ws.handler.ServerSOAPHandlerTube;
/*     */ import com.sun.xml.internal.ws.protocol.soap.ServerMUTube;
/*     */ import com.sun.xml.internal.ws.server.ServerSchemaValidationTube;
/*     */ import com.sun.xml.internal.ws.util.pipe.DumpTube;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ 
/*     */ public class ServerTubeAssemblerContext
/*     */ {
/*     */   private final SEIModel seiModel;
/*     */   private final WSDLPort wsdlModel;
/*     */   private final WSEndpoint endpoint;
/*     */   private final BindingImpl binding;
/*     */   private final Tube terminal;
/*     */   private final boolean isSynchronous;
/*     */ 
/*     */   @NotNull
/*     */   private Codec codec;
/*     */ 
/*     */   public ServerTubeAssemblerContext(@Nullable SEIModel seiModel, @Nullable WSDLPort wsdlModel, @NotNull WSEndpoint endpoint, @NotNull Tube terminal, boolean isSynchronous)
/*     */   {
/*  71 */     this.seiModel = seiModel;
/*  72 */     this.wsdlModel = wsdlModel;
/*  73 */     this.endpoint = endpoint;
/*  74 */     this.terminal = terminal;
/*     */ 
/*  76 */     this.binding = ((BindingImpl)endpoint.getBinding());
/*  77 */     this.isSynchronous = isSynchronous;
/*  78 */     this.codec = this.binding.createCodec();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public SEIModel getSEIModel()
/*     */   {
/*  88 */     return this.seiModel;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public WSDLPort getWsdlModel()
/*     */   {
/*  98 */     return this.wsdlModel;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WSEndpoint getEndpoint()
/*     */   {
/* 109 */     return this.endpoint;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Tube getTerminalTube()
/*     */   {
/* 123 */     return this.terminal;
/*     */   }
/*     */ 
/*     */   public boolean isSynchronous()
/*     */   {
/* 133 */     return this.isSynchronous;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Tube createServerMUTube(@NotNull Tube next)
/*     */   {
/* 141 */     if ((this.binding instanceof SOAPBinding)) {
/* 142 */       return new ServerMUTube(this, next);
/*     */     }
/* 144 */     return next;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Tube createHandlerTube(@NotNull Tube next)
/*     */   {
/* 151 */     if (!this.binding.getHandlerChain().isEmpty()) {
/* 152 */       HandlerTube cousin = new ServerLogicalHandlerTube(this.binding, this.seiModel, this.wsdlModel, next);
/* 153 */       next = cousin;
/* 154 */       if ((this.binding instanceof SOAPBinding))
/*     */       {
/* 156 */         next = cousin = new ServerSOAPHandlerTube(this.binding, next, cousin);
/*     */ 
/* 159 */         next = new ServerMessageHandlerTube(this.seiModel, this.binding, next, cousin);
/*     */       }
/*     */     }
/* 162 */     return next;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Tube createMonitoringTube(@NotNull Tube next)
/*     */   {
/* 170 */     ServerPipelineHook hook = (ServerPipelineHook)this.endpoint.getContainer().getSPI(ServerPipelineHook.class);
/* 171 */     if (hook != null) {
/* 172 */       ServerPipeAssemblerContext ctxt = new ServerPipeAssemblerContext(this.seiModel, this.wsdlModel, this.endpoint, this.terminal, this.isSynchronous);
/* 173 */       return PipeAdapter.adapt(hook.createMonitoringPipe(ctxt, PipeAdapter.adapt(next)));
/*     */     }
/* 175 */     return next;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Tube createSecurityTube(@NotNull Tube next)
/*     */   {
/* 182 */     ServerPipelineHook hook = (ServerPipelineHook)this.endpoint.getContainer().getSPI(ServerPipelineHook.class);
/* 183 */     if (hook != null) {
/* 184 */       ServerPipeAssemblerContext ctxt = new ServerPipeAssemblerContext(this.seiModel, this.wsdlModel, this.endpoint, this.terminal, this.isSynchronous);
/* 185 */       return PipeAdapter.adapt(hook.createSecurityPipe(ctxt, PipeAdapter.adapt(next)));
/*     */     }
/* 187 */     return next;
/*     */   }
/*     */ 
/*     */   public Tube createDumpTube(String name, PrintStream out, Tube next)
/*     */   {
/* 194 */     return new DumpTube(name, out, next);
/*     */   }
/*     */ 
/*     */   public Tube createValidationTube(Tube next)
/*     */   {
/* 201 */     if (((this.binding instanceof SOAPBinding)) && (this.binding.isFeatureEnabled(SchemaValidationFeature.class)) && (this.wsdlModel != null)) {
/* 202 */       return new ServerSchemaValidationTube(this.endpoint, this.binding, this.seiModel, this.wsdlModel, next);
/*     */     }
/* 204 */     return next;
/*     */   }
/*     */ 
/*     */   public Tube createWsaTube(Tube next)
/*     */   {
/* 211 */     if (((this.binding instanceof SOAPBinding)) && (AddressingVersion.isEnabled(this.binding))) {
/* 212 */       if (AddressingVersion.fromBinding(this.binding) == AddressingVersion.MEMBER) {
/* 213 */         return new MemberSubmissionWsaServerTube(this.endpoint, this.wsdlModel, this.binding, next);
/*     */       }
/* 215 */       return new W3CWsaServerTube(this.endpoint, this.wsdlModel, this.binding, next);
/*     */     }
/*     */ 
/* 218 */     return next;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Codec getCodec()
/*     */   {
/* 231 */     return this.codec;
/*     */   }
/*     */ 
/*     */   public void setCodec(@NotNull Codec codec)
/*     */   {
/* 253 */     this.codec = codec;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext
 * JD-Core Version:    0.6.2
 */