/*    */ package com.sun.xml.internal.ws.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*    */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ final class SOAPActionBasedOperationFinder extends WSDLOperationFinder
/*    */ {
/*    */   private final Map<String, QName> methodHandlers;
/*    */ 
/*    */   public SOAPActionBasedOperationFinder(WSDLPort wsdlModel, WSBinding binding, @Nullable SEIModel seiModel)
/*    */   {
/* 55 */     super(wsdlModel, binding, seiModel);
/* 56 */     this.methodHandlers = new HashMap();
/*    */ 
/* 59 */     Map unique = new HashMap();
/* 60 */     if (seiModel != null) {
/* 61 */       for (JavaMethodImpl m : ((AbstractSEIModelImpl)seiModel).getJavaMethods()) {
/* 62 */         String soapAction = m.getOperation().getSOAPAction();
/* 63 */         Integer count = (Integer)unique.get(soapAction);
/* 64 */         if (count == null)
/* 65 */           unique.put(soapAction, Integer.valueOf(1));
/*    */         else {
/* 67 */           unique.put(soapAction, count = Integer.valueOf(count.intValue() + 1));
/*    */         }
/*    */       }
/*    */ 
/* 71 */       for (JavaMethodImpl m : ((AbstractSEIModelImpl)seiModel).getJavaMethods()) {
/* 72 */         String soapAction = m.getOperation().getSOAPAction();
/*    */ 
/* 75 */         if (((Integer)unique.get(soapAction)).intValue() == 1)
/* 76 */           this.methodHandlers.put('"' + soapAction + '"', m.getOperation().getName());
/*    */       }
/*    */     }
/*    */     else {
/* 80 */       for (WSDLBoundOperation wsdlOp : wsdlModel.getBinding().getBindingOperations())
/* 81 */         this.methodHandlers.put(wsdlOp.getSOAPAction(), wsdlOp.getName());
/*    */     }
/*    */   }
/*    */ 
/*    */   public QName getWSDLOperationQName(Packet request)
/*    */   {
/* 88 */     return request.soapAction == null ? null : (QName)this.methodHandlers.get(request.soapAction);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.SOAPActionBasedOperationFinder
 * JD-Core Version:    0.6.2
 */