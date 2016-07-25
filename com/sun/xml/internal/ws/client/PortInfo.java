/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.api.client.ServiceInterceptor;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver.ClientContext;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.policy.jaxws.PolicyUtil;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public class PortInfo
/*     */   implements WSPortInfo
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   private final WSServiceDelegate owner;
/*     */ 
/*     */   @NotNull
/*     */   public final QName portName;
/*     */ 
/*     */   @NotNull
/*     */   public final EndpointAddress targetEndpoint;
/*     */ 
/*     */   @NotNull
/*     */   public final BindingID bindingId;
/*     */ 
/*     */   @NotNull
/*     */   public final PolicyMap policyMap;
/*     */ 
/*     */   @Nullable
/*     */   public final WSDLPort portModel;
/*     */ 
/*     */   public PortInfo(WSServiceDelegate owner, EndpointAddress targetEndpoint, QName name, BindingID bindingId)
/*     */   {
/*  74 */     this.owner = owner;
/*  75 */     this.targetEndpoint = targetEndpoint;
/*  76 */     this.portName = name;
/*  77 */     this.bindingId = bindingId;
/*  78 */     this.portModel = getPortModel(owner, name);
/*  79 */     this.policyMap = createPolicyMap();
/*     */   }
/*     */ 
/*     */   public PortInfo(@NotNull WSServiceDelegate owner, @NotNull WSDLPort port)
/*     */   {
/*  84 */     this.owner = owner;
/*  85 */     this.targetEndpoint = port.getAddress();
/*  86 */     this.portName = port.getName();
/*  87 */     this.bindingId = port.getBinding().getBindingId();
/*  88 */     this.portModel = port;
/*  89 */     this.policyMap = createPolicyMap();
/*     */   }
/*     */ 
/*     */   public PolicyMap getPolicyMap() {
/*  93 */     return this.policyMap;
/*     */   }
/*     */ 
/*     */   public PolicyMap createPolicyMap()
/*     */   {
/*     */     PolicyMap map;
/*     */     PolicyMap map;
/*  98 */     if (this.portModel != null)
/*  99 */       map = ((WSDLModelImpl)this.portModel.getOwner().getParent()).getPolicyMap();
/*     */     else {
/* 101 */       map = PolicyResolverFactory.create().resolve(new PolicyResolver.ClientContext(null, this.owner.getContainer()));
/*     */     }
/*     */ 
/* 104 */     if (map == null)
/* 105 */       map = PolicyMap.createPolicyMap(null);
/* 106 */     return map;
/*     */   }
/*     */ 
/*     */   public BindingImpl createBinding(WebServiceFeature[] webServiceFeatures, Class<?> portInterface)
/*     */   {
/* 119 */     WebServiceFeatureList r = new WebServiceFeatureList(webServiceFeatures);
/*     */     Iterable configFeatures;
/*     */     Iterable configFeatures;
/* 127 */     if (this.portModel != null)
/*     */     {
/* 133 */       configFeatures = this.portModel.getFeatures();
/*     */     }
/* 135 */     else configFeatures = PolicyUtil.getPortScopedFeatures(this.policyMap, this.owner.getServiceName(), this.portName);
/*     */ 
/* 137 */     r.mergeFeatures(configFeatures, false);
/*     */ 
/* 140 */     r.mergeFeatures(this.owner.serviceInterceptor.preCreateBinding(this, portInterface, r), false);
/*     */ 
/* 142 */     BindingImpl bindingImpl = BindingImpl.create(this.bindingId, r.toArray());
/* 143 */     this.owner.getHandlerConfigurator().configureHandlers(this, bindingImpl);
/*     */ 
/* 145 */     return bindingImpl;
/*     */   }
/*     */ 
/*     */   private WSDLPort getPortModel(WSServiceDelegate owner, QName portName)
/*     */   {
/* 151 */     if (owner.getWsdlService() != null) {
/* 152 */       Iterable ports = owner.getWsdlService().getPorts();
/* 153 */       for (WSDLPortImpl port : ports) {
/* 154 */         if (port.getName().equals(portName))
/* 155 */           return port;
/*     */       }
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public WSDLPort getPort()
/*     */   {
/* 167 */     return this.portModel;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WSService getOwner() {
/* 172 */     return this.owner;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public BindingID getBindingId() {
/* 177 */     return this.bindingId;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public EndpointAddress getEndpointAddress() {
/* 182 */     return this.targetEndpoint;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public QName getServiceName()
/*     */   {
/* 191 */     return this.owner.getServiceName();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public QName getPortName()
/*     */   {
/* 200 */     return this.portName;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getBindingID()
/*     */   {
/* 209 */     return this.bindingId.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.PortInfo
 * JD-Core Version:    0.6.2
 */