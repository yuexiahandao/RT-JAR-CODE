/*     */ package com.sun.xml.internal.ws.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class WSDLModelImpl extends AbstractExtensibleImpl
/*     */   implements WSDLModel
/*     */ {
/*  54 */   private final Map<QName, WSDLMessageImpl> messages = new HashMap();
/*  55 */   private final Map<QName, WSDLPortTypeImpl> portTypes = new HashMap();
/*  56 */   private final Map<QName, WSDLBoundPortTypeImpl> bindings = new HashMap();
/*  57 */   private final Map<QName, WSDLServiceImpl> services = new LinkedHashMap();
/*     */   private PolicyMap policyMap;
/*  60 */   private final Map<QName, WSDLBoundPortType> unmBindings = Collections.unmodifiableMap(this.bindings);
/*     */ 
/*     */   public WSDLModelImpl(@NotNull String systemId)
/*     */   {
/*  65 */     super(systemId, -1);
/*     */   }
/*     */ 
/*     */   public WSDLModelImpl()
/*     */   {
/*  72 */     super(null, -1);
/*     */   }
/*     */ 
/*     */   public void addMessage(WSDLMessageImpl msg) {
/*  76 */     this.messages.put(msg.getName(), msg);
/*     */   }
/*     */ 
/*     */   public WSDLMessageImpl getMessage(QName name) {
/*  80 */     return (WSDLMessageImpl)this.messages.get(name);
/*     */   }
/*     */ 
/*     */   public void addPortType(WSDLPortTypeImpl pt) {
/*  84 */     this.portTypes.put(pt.getName(), pt);
/*     */   }
/*     */ 
/*     */   public WSDLPortTypeImpl getPortType(QName name) {
/*  88 */     return (WSDLPortTypeImpl)this.portTypes.get(name);
/*     */   }
/*     */ 
/*     */   public void addBinding(WSDLBoundPortTypeImpl boundPortType) {
/*  92 */     assert (!this.bindings.containsValue(boundPortType));
/*  93 */     this.bindings.put(boundPortType.getName(), boundPortType);
/*     */   }
/*     */ 
/*     */   public WSDLBoundPortTypeImpl getBinding(QName name) {
/*  97 */     return (WSDLBoundPortTypeImpl)this.bindings.get(name);
/*     */   }
/*     */ 
/*     */   public void addService(WSDLServiceImpl svc) {
/* 101 */     this.services.put(svc.getName(), svc);
/*     */   }
/*     */ 
/*     */   public WSDLServiceImpl getService(QName name) {
/* 105 */     return (WSDLServiceImpl)this.services.get(name);
/*     */   }
/*     */ 
/*     */   public Map<QName, WSDLMessageImpl> getMessages() {
/* 109 */     return this.messages;
/*     */   }
/*     */   @NotNull
/*     */   public Map<QName, WSDLPortTypeImpl> getPortTypes() {
/* 113 */     return this.portTypes;
/*     */   }
/*     */   @NotNull
/*     */   public Map<QName, WSDLBoundPortType> getBindings() {
/* 117 */     return this.unmBindings;
/*     */   }
/*     */   @NotNull
/*     */   public Map<QName, WSDLServiceImpl> getServices() {
/* 121 */     return this.services;
/*     */   }
/*     */ 
/*     */   public QName getFirstServiceName()
/*     */   {
/* 128 */     if (this.services.isEmpty())
/* 129 */       return null;
/* 130 */     return ((WSDLServiceImpl)this.services.values().iterator().next()).getName();
/*     */   }
/*     */ 
/*     */   public QName getFirstPortName()
/*     */   {
/* 137 */     WSDLPort fp = getFirstPort();
/* 138 */     if (fp == null) {
/* 139 */       return null;
/*     */     }
/* 141 */     return fp.getName();
/*     */   }
/*     */ 
/*     */   private WSDLPort getFirstPort() {
/* 145 */     if (this.services.isEmpty())
/* 146 */       return null;
/* 147 */     WSDLService service = (WSDLService)this.services.values().iterator().next();
/* 148 */     Iterator iter = service.getPorts().iterator();
/* 149 */     WSDLPort port = iter.hasNext() ? (WSDLPort)iter.next() : null;
/* 150 */     return port;
/*     */   }
/*     */ 
/*     */   public WSDLPortImpl getMatchingPort(QName serviceName, QName portType)
/*     */   {
/* 157 */     return getService(serviceName).getMatchingPort(portType);
/*     */   }
/*     */ 
/*     */   public WSDLBoundPortTypeImpl getBinding(QName serviceName, QName portName)
/*     */   {
/* 168 */     WSDLServiceImpl service = (WSDLServiceImpl)this.services.get(serviceName);
/* 169 */     if (service != null) {
/* 170 */       WSDLPortImpl port = service.get(portName);
/* 171 */       if (port != null)
/* 172 */         return port.getBinding();
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   void finalizeRpcLitBinding(WSDLBoundPortTypeImpl boundPortType) {
/* 178 */     assert (boundPortType != null);
/* 179 */     QName portTypeName = boundPortType.getPortTypeName();
/* 180 */     if (portTypeName == null)
/* 181 */       return;
/* 182 */     WSDLPortType pt = (WSDLPortType)this.portTypes.get(portTypeName);
/* 183 */     if (pt == null)
/* 184 */       return;
/* 185 */     for (Iterator i$ = boundPortType.getBindingOperations().iterator(); i$.hasNext(); ) { bop = (WSDLBoundOperationImpl)i$.next();
/* 186 */       WSDLOperation pto = pt.get(bop.getName().getLocalPart());
/* 187 */       WSDLMessage inMsgName = pto.getInput().getMessage();
/* 188 */       if (inMsgName != null)
/*     */       {
/* 190 */         WSDLMessageImpl inMsg = (WSDLMessageImpl)this.messages.get(inMsgName.getName());
/* 191 */         bodyindex = 0;
/* 192 */         if (inMsg != null) {
/* 193 */           for (WSDLPartImpl part : inMsg.parts()) {
/* 194 */             String name = part.getName();
/* 195 */             ParameterBinding pb = bop.getInputBinding(name);
/* 196 */             if (pb.isBody()) {
/* 197 */               part.setIndex(bodyindex++);
/* 198 */               part.setBinding(pb);
/* 199 */               bop.addPart(part, WebParam.Mode.IN);
/*     */             }
/*     */           }
/*     */         }
/* 203 */         bodyindex = 0;
/* 204 */         if (!pto.isOneWay())
/*     */         {
/* 206 */           WSDLMessage outMsgName = pto.getOutput().getMessage();
/* 207 */           if (outMsgName != null)
/*     */           {
/* 209 */             WSDLMessageImpl outMsg = (WSDLMessageImpl)this.messages.get(outMsgName.getName());
/* 210 */             if (outMsg != null)
/* 211 */               for (WSDLPartImpl part : outMsg.parts()) {
/* 212 */                 String name = part.getName();
/* 213 */                 ParameterBinding pb = bop.getOutputBinding(name);
/* 214 */                 if (pb.isBody()) {
/* 215 */                   part.setIndex(bodyindex++);
/* 216 */                   part.setBinding(pb);
/* 217 */                   bop.addPart(part, WebParam.Mode.OUT);
/*     */                 }
/*     */               }
/*     */           }
/*     */         }
/*     */       } }
/*     */     WSDLBoundOperationImpl bop;
/*     */     int bodyindex;
/*     */   }
/*     */ 
/*     */   public PolicyMap getPolicyMap()
/*     */   {
/* 230 */     return this.policyMap;
/*     */   }
/*     */ 
/*     */   public void setPolicyMap(PolicyMap policyMap)
/*     */   {
/* 238 */     this.policyMap = policyMap;
/*     */   }
/*     */ 
/*     */   public void freeze()
/*     */   {
/* 245 */     for (WSDLServiceImpl service : this.services.values()) {
/* 246 */       service.freeze(this);
/*     */     }
/* 248 */     for (WSDLBoundPortTypeImpl bp : this.bindings.values())
/* 249 */       bp.freeze();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl
 * JD-Core Version:    0.6.2
 */