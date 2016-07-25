/*     */ package com.sun.xml.internal.ws.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.MEP;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ final class ActionBasedOperationFinder extends WSDLOperationFinder
/*     */ {
/*  63 */   private static final Logger LOGGER = Logger.getLogger(ActionBasedOperationFinder.class.getName());
/*     */   private final Map<ActionBasedOperationSignature, QName> uniqueOpSignatureMap;
/*     */   private final Map<String, QName> actionMap;
/*     */ 
/*     */   @NotNull
/*     */   private final AddressingVersion av;
/*     */ 
/*     */   public ActionBasedOperationFinder(WSDLPort wsdlModel, WSBinding binding, @Nullable SEIModel seiModel)
/*     */   {
/*  70 */     super(wsdlModel, binding, seiModel);
/*     */ 
/*  72 */     assert (binding.getAddressingVersion() != null);
/*  73 */     this.av = binding.getAddressingVersion();
/*  74 */     this.uniqueOpSignatureMap = new HashMap();
/*  75 */     this.actionMap = new HashMap();
/*     */ 
/*  77 */     if (seiModel != null)
/*  78 */       for (JavaMethodImpl m : ((AbstractSEIModelImpl)seiModel).getJavaMethods())
/*  79 */         if (!m.getMEP().isAsync)
/*     */         {
/*  82 */           String action = m.getInputAction();
/*  83 */           QName payloadName = m.getRequestPayloadName();
/*  84 */           if (payloadName == null) {
/*  85 */             payloadName = PayloadQNameBasedOperationFinder.EMPTY_PAYLOAD;
/*     */           }
/*  87 */           if ((action == null) || (action.equals(""))) {
/*  88 */             action = m.getOperation().getOperation().getInput().getAction();
/*     */           }
/*     */ 
/*  91 */           if (action != null) {
/*  92 */             ActionBasedOperationSignature opSignature = new ActionBasedOperationSignature(action, payloadName);
/*  93 */             if (this.uniqueOpSignatureMap.get(opSignature) != null) {
/*  94 */               LOGGER.warning(AddressingMessages.NON_UNIQUE_OPERATION_SIGNATURE(this.uniqueOpSignatureMap.get(opSignature), m.getOperation().getName(), action, payloadName));
/*     */             }
/*     */ 
/*  97 */             this.uniqueOpSignatureMap.put(opSignature, m.getOperation().getName());
/*  98 */             this.actionMap.put(action, m.getOperation().getName());
/*     */           }
/*     */         }
/*     */     else
/* 102 */       for (WSDLBoundOperation wsdlOp : wsdlModel.getBinding().getBindingOperations()) {
/* 103 */         QName payloadName = wsdlOp.getReqPayloadName();
/* 104 */         if (payloadName == null)
/* 105 */           payloadName = PayloadQNameBasedOperationFinder.EMPTY_PAYLOAD;
/* 106 */         String action = wsdlOp.getOperation().getInput().getAction();
/* 107 */         ActionBasedOperationSignature opSignature = new ActionBasedOperationSignature(action, payloadName);
/*     */ 
/* 109 */         if (this.uniqueOpSignatureMap.get(opSignature) != null) {
/* 110 */           LOGGER.warning(AddressingMessages.NON_UNIQUE_OPERATION_SIGNATURE(this.uniqueOpSignatureMap.get(opSignature), wsdlOp.getName(), action, payloadName));
/*     */         }
/*     */ 
/* 114 */         this.uniqueOpSignatureMap.put(opSignature, wsdlOp.getName());
/* 115 */         this.actionMap.put(action, wsdlOp.getName());
/*     */       }
/*     */   }
/*     */ 
/*     */   public QName getWSDLOperationQName(Packet request)
/*     */     throws DispatchException
/*     */   {
/* 130 */     HeaderList hl = request.getMessage().getHeaders();
/* 131 */     String action = hl.getAction(this.av, this.binding.getSOAPVersion());
/*     */ 
/* 133 */     if (action == null)
/*     */     {
/* 135 */       return null;
/*     */     }
/* 137 */     Message message = request.getMessage();
/*     */ 
/* 139 */     String localPart = message.getPayloadLocalPart();
/*     */     QName payloadName;
/*     */     QName payloadName;
/* 140 */     if (localPart == null) {
/* 141 */       payloadName = PayloadQNameBasedOperationFinder.EMPTY_PAYLOAD;
/*     */     } else {
/* 143 */       String nsUri = message.getPayloadNamespaceURI();
/* 144 */       if (nsUri == null)
/* 145 */         nsUri = "";
/* 146 */       payloadName = new QName(nsUri, localPart);
/*     */     }
/*     */ 
/* 149 */     QName opName = (QName)this.uniqueOpSignatureMap.get(new ActionBasedOperationSignature(action, payloadName));
/* 150 */     if (opName != null) {
/* 151 */       return opName;
/*     */     }
/*     */ 
/* 156 */     opName = (QName)this.actionMap.get(action);
/* 157 */     if (opName != null) {
/* 158 */       return opName;
/*     */     }
/*     */ 
/* 161 */     Message result = Messages.create(action, this.av, this.binding.getSOAPVersion());
/*     */ 
/* 163 */     throw new DispatchException(result);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.ActionBasedOperationFinder
 * JD-Core Version:    0.6.2
 */