/*     */ package com.sun.xml.internal.ws.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.MEP;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import com.sun.xml.internal.ws.util.QNameMap;
/*     */ import com.sun.xml.internal.ws.util.QNameMap.Entry;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ final class PayloadQNameBasedOperationFinder extends WSDLOperationFinder
/*     */ {
/*  59 */   private static final Logger LOGGER = Logger.getLogger(PayloadQNameBasedOperationFinder.class.getName());
/*     */   public static final String EMPTY_PAYLOAD_LOCAL = "";
/*     */   public static final String EMPTY_PAYLOAD_NSURI = "";
/*  63 */   public static final QName EMPTY_PAYLOAD = new QName("", "");
/*     */ 
/*  65 */   private final QNameMap<QName> methodHandlers = new QNameMap();
/*  66 */   private final QNameMap<List<String>> unique = new QNameMap();
/*     */ 
/*     */   public PayloadQNameBasedOperationFinder(WSDLPort wsdlModel, WSBinding binding, @Nullable SEIModel seiModel)
/*     */   {
/*  70 */     super(wsdlModel, binding, seiModel);
/*  71 */     if (seiModel != null)
/*     */     {
/*  73 */       for (JavaMethodImpl m : ((AbstractSEIModelImpl)seiModel).getJavaMethods()) {
/*  74 */         if (!m.getMEP().isAsync)
/*     */         {
/*  76 */           QName name = m.getRequestPayloadName();
/*  77 */           if (name == null)
/*  78 */             name = EMPTY_PAYLOAD;
/*  79 */           List methods = (List)this.unique.get(name);
/*  80 */           if (methods == null) {
/*  81 */             methods = new ArrayList();
/*  82 */             this.unique.put(name, methods);
/*     */           }
/*  84 */           methods.add(m.getMethod().getName());
/*     */         }
/*     */       }
/*     */ 
/*  88 */       for (QNameMap.Entry e : this.unique.entrySet()) {
/*  89 */         if (((List)e.getValue()).size() > 1) {
/*  90 */           LOGGER.warning(ServerMessages.NON_UNIQUE_DISPATCH_QNAME(e.getValue(), e.createQName()));
/*     */         }
/*     */       }
/*     */ 
/*  94 */       for (JavaMethodImpl m : ((AbstractSEIModelImpl)seiModel).getJavaMethods()) {
/*  95 */         QName name = m.getRequestPayloadName();
/*  96 */         if (name == null) {
/*  97 */           name = EMPTY_PAYLOAD;
/*     */         }
/*     */ 
/* 100 */         if (((List)this.unique.get(name)).size() == 1)
/* 101 */           this.methodHandlers.put(name, m.getOperation().getName());
/*     */       }
/*     */     }
/*     */     else {
/* 105 */       for (WSDLBoundOperation wsdlOp : wsdlModel.getBinding().getBindingOperations()) {
/* 106 */         QName name = wsdlOp.getReqPayloadName();
/* 107 */         if (name == null)
/* 108 */           name = EMPTY_PAYLOAD;
/* 109 */         this.methodHandlers.put(name, wsdlOp.getName());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public QName getWSDLOperationQName(Packet request)
/*     */     throws DispatchException
/*     */   {
/* 123 */     Message message = request.getMessage();
/* 124 */     String localPart = message.getPayloadLocalPart();
/*     */     String nsUri;
/*     */     String nsUri;
/* 126 */     if (localPart == null) {
/* 127 */       localPart = "";
/* 128 */       nsUri = "";
/*     */     } else {
/* 130 */       nsUri = message.getPayloadNamespaceURI();
/* 131 */       if (nsUri == null)
/* 132 */         nsUri = "";
/*     */     }
/* 134 */     QName op = (QName)this.methodHandlers.get(nsUri, localPart);
/*     */ 
/* 137 */     if ((op == null) && (!this.unique.containsKey(nsUri, localPart))) {
/* 138 */       String dispatchKey = "{" + nsUri + "}" + localPart;
/* 139 */       String faultString = ServerMessages.DISPATCH_CANNOT_FIND_METHOD(dispatchKey);
/* 140 */       throw new DispatchException(SOAPFaultBuilder.createSOAPFaultMessage(this.binding.getSOAPVersion(), faultString, this.binding.getSOAPVersion().faultCodeClient));
/*     */     }
/*     */ 
/* 143 */     return op;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.PayloadQNameBasedOperationFinder
 * JD-Core Version:    0.6.2
 */