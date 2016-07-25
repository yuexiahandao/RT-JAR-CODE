/*    */ package com.sun.xml.internal.ws.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*    */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class OperationDispatcher
/*    */ {
/*    */   private List<WSDLOperationFinder> opFinders;
/*    */   private WSBinding binding;
/*    */ 
/*    */   public OperationDispatcher(@NotNull WSDLPort wsdlModel, @NotNull WSBinding binding, @Nullable SEIModel seiModel)
/*    */   {
/* 57 */     this.binding = binding;
/* 58 */     this.opFinders = new ArrayList();
/* 59 */     if (binding.getAddressingVersion() != null) {
/* 60 */       this.opFinders.add(new ActionBasedOperationFinder(wsdlModel, binding, seiModel));
/*    */     }
/* 62 */     this.opFinders.add(new PayloadQNameBasedOperationFinder(wsdlModel, binding, seiModel));
/* 63 */     this.opFinders.add(new SOAPActionBasedOperationFinder(wsdlModel, binding, seiModel));
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public QName getWSDLOperationQName(Packet request)
/*    */     throws DispatchException
/*    */   {
/* 75 */     for (WSDLOperationFinder finder : this.opFinders) {
/* 76 */       QName opName = finder.getWSDLOperationQName(request);
/* 77 */       if (opName != null) {
/* 78 */         return opName;
/*    */       }
/*    */     }
/* 81 */     String err = MessageFormat.format("Request=[SOAPAction={0},Payload='{'{1}'}'{2}]", new Object[] { request.soapAction, request.getMessage().getPayloadNamespaceURI(), request.getMessage().getPayloadLocalPart() });
/*    */ 
/* 84 */     String faultString = ServerMessages.DISPATCH_CANNOT_FIND_METHOD(err);
/* 85 */     Message faultMsg = SOAPFaultBuilder.createSOAPFaultMessage(this.binding.getSOAPVersion(), faultString, this.binding.getSOAPVersion().faultCodeClient);
/*    */ 
/* 87 */     throw new DispatchException(faultMsg);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.OperationDispatcher
 * JD-Core Version:    0.6.2
 */