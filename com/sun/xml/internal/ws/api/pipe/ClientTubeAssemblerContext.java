/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.addressing.W3CWsaClientTube;
/*     */ import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionWsaClientTube;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.client.ClientPipelineHook;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.ClientSchemaValidationTube;
/*     */ import com.sun.xml.internal.ws.developer.SchemaValidationFeature;
/*     */ import com.sun.xml.internal.ws.developer.WSBindingProvider;
/*     */ import com.sun.xml.internal.ws.handler.ClientLogicalHandlerTube;
/*     */ import com.sun.xml.internal.ws.handler.ClientMessageHandlerTube;
/*     */ import com.sun.xml.internal.ws.handler.ClientSOAPHandlerTube;
/*     */ import com.sun.xml.internal.ws.handler.HandlerTube;
/*     */ import com.sun.xml.internal.ws.protocol.soap.ClientMUTube;
/*     */ import com.sun.xml.internal.ws.transport.DeferredTransportPipe;
/*     */ import com.sun.xml.internal.ws.util.pipe.DumpTube;
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ 
/*     */ public class ClientTubeAssemblerContext
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   private final EndpointAddress address;
/*     */ 
/*     */   @Nullable
/*     */   private final WSDLPort wsdlModel;
/*     */ 
/*     */   @Nullable
/*     */   private final SEIModel seiModel;
/*     */ 
/*     */   @NotNull
/*     */   private final WSService rootOwner;
/*     */ 
/*     */   @NotNull
/*     */   private final WSBinding binding;
/*     */ 
/*     */   @NotNull
/*     */   private final Container container;
/*     */ 
/*     */   @NotNull
/*     */   private Codec codec;
/*     */ 
/*     */   @Nullable
/*     */   private final WSBindingProvider bindingProvider;
/*     */ 
/*     */   /** @deprecated */
/*     */   public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding)
/*     */   {
/*  83 */     this(address, wsdlModel, rootOwner, binding, Container.NONE);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container)
/*     */   {
/*  95 */     this(address, wsdlModel, rootOwner, binding, container, ((BindingImpl)binding).createCodec());
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container, Codec codec)
/*     */   {
/* 106 */     this(address, wsdlModel, rootOwner, binding, container, codec, null);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container, Codec codec, SEIModel seiModel)
/*     */   {
/* 117 */     this(address, wsdlModel, rootOwner, null, binding, container, codec, seiModel);
/*     */   }
/*     */ 
/*     */   public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSBindingProvider bindingProvider, @NotNull WSBinding binding, @NotNull Container container, Codec codec, SEIModel seiModel)
/*     */   {
/* 128 */     this(address, wsdlModel, bindingProvider == null ? null : bindingProvider.getPortInfo().getOwner(), bindingProvider, binding, container, codec, seiModel);
/*     */   }
/*     */ 
/*     */   private ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @Nullable WSService rootOwner, @Nullable WSBindingProvider bindingProvider, @NotNull WSBinding binding, @NotNull Container container, Codec codec, SEIModel seiModel)
/*     */   {
/* 137 */     this.address = address;
/* 138 */     this.wsdlModel = wsdlModel;
/* 139 */     this.rootOwner = rootOwner;
/* 140 */     this.bindingProvider = bindingProvider;
/* 141 */     this.binding = binding;
/* 142 */     this.container = container;
/* 143 */     this.codec = codec;
/* 144 */     this.seiModel = seiModel;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public EndpointAddress getAddress()
/*     */   {
/* 153 */     return this.address;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public WSDLPort getWsdlModel()
/*     */   {
/* 162 */     return this.wsdlModel;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WSService getService()
/*     */   {
/* 171 */     return this.rootOwner;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public WSPortInfo getPortInfo()
/*     */   {
/* 179 */     return this.bindingProvider == null ? null : this.bindingProvider.getPortInfo();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public WSBindingProvider getBindingProvider()
/*     */   {
/* 188 */     return this.bindingProvider;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WSBinding getBinding()
/*     */   {
/* 195 */     return this.binding;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public SEIModel getSEIModel()
/*     */   {
/* 205 */     return this.seiModel;
/*     */   }
/*     */ 
/*     */   public Container getContainer()
/*     */   {
/* 214 */     return this.container;
/*     */   }
/*     */ 
/*     */   public Tube createDumpTube(String name, PrintStream out, Tube next)
/*     */   {
/* 221 */     return new DumpTube(name, out, next);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Tube createSecurityTube(@NotNull Tube next)
/*     */   {
/* 228 */     ClientPipelineHook hook = (ClientPipelineHook)this.container.getSPI(ClientPipelineHook.class);
/* 229 */     if (hook != null) {
/* 230 */       ClientPipeAssemblerContext ctxt = new ClientPipeAssemblerContext(this.address, this.wsdlModel, this.rootOwner, this.binding, this.container);
/*     */ 
/* 232 */       return PipeAdapter.adapt(hook.createSecurityPipe(ctxt, PipeAdapter.adapt(next)));
/*     */     }
/* 234 */     return next;
/*     */   }
/*     */ 
/*     */   public Tube createWsaTube(Tube next)
/*     */   {
/* 241 */     if (((this.binding instanceof SOAPBinding)) && (AddressingVersion.isEnabled(this.binding)) && (this.wsdlModel != null)) {
/* 242 */       if (AddressingVersion.fromBinding(this.binding) == AddressingVersion.MEMBER) {
/* 243 */         return new MemberSubmissionWsaClientTube(this.wsdlModel, this.binding, next);
/*     */       }
/* 245 */       return new W3CWsaClientTube(this.wsdlModel, this.binding, next);
/*     */     }
/*     */ 
/* 248 */     return next;
/*     */   }
/*     */ 
/*     */   public Tube createHandlerTube(Tube next)
/*     */   {
/* 255 */     HandlerTube cousinHandlerTube = null;
/*     */ 
/* 257 */     if ((this.binding instanceof SOAPBinding))
/*     */     {
/* 259 */       HandlerTube messageHandlerTube = new ClientMessageHandlerTube(this.seiModel, this.binding, this.wsdlModel, next);
/* 260 */       next = cousinHandlerTube = messageHandlerTube;
/*     */ 
/* 263 */       HandlerTube soapHandlerTube = new ClientSOAPHandlerTube(this.binding, next, cousinHandlerTube);
/* 264 */       next = cousinHandlerTube = soapHandlerTube;
/*     */     }
/* 266 */     return new ClientLogicalHandlerTube(this.binding, this.seiModel, next, cousinHandlerTube);
/*     */   }
/*     */ 
/*     */   public Tube createClientMUTube(Tube next)
/*     */   {
/* 274 */     if ((this.binding instanceof SOAPBinding)) {
/* 275 */       return new ClientMUTube(this.binding, next);
/*     */     }
/* 277 */     return next;
/*     */   }
/*     */ 
/*     */   public Tube createValidationTube(Tube next)
/*     */   {
/* 284 */     if (((this.binding instanceof SOAPBinding)) && (this.binding.isFeatureEnabled(SchemaValidationFeature.class)) && (this.wsdlModel != null)) {
/* 285 */       return new ClientSchemaValidationTube(this.binding, this.wsdlModel, next);
/*     */     }
/* 287 */     return next;
/*     */   }
/*     */ 
/*     */   public Tube createTransportTube()
/*     */   {
/* 294 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 300 */     return new DeferredTransportPipe(cl, this);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Codec getCodec()
/*     */   {
/* 310 */     return this.codec;
/*     */   }
/*     */ 
/*     */   public void setCodec(@NotNull Codec codec)
/*     */   {
/* 325 */     this.codec = codec;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext
 * JD-Core Version:    0.6.2
 */