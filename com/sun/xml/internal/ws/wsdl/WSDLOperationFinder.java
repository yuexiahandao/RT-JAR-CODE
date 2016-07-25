/*    */ package com.sun.xml.internal.ws.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract class WSDLOperationFinder
/*    */ {
/*    */   protected final WSDLPort wsdlModel;
/*    */   protected final WSBinding binding;
/*    */   protected final SEIModel seiModel;
/*    */ 
/*    */   public WSDLOperationFinder(@NotNull WSDLPort wsdlModel, @NotNull WSBinding binding, @Nullable SEIModel seiModel)
/*    */   {
/* 51 */     this.wsdlModel = wsdlModel;
/* 52 */     this.binding = binding;
/* 53 */     this.seiModel = seiModel;
/*    */   }
/*    */ 
/*    */   public abstract QName getWSDLOperationQName(Packet paramPacket)
/*    */     throws DispatchException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.WSDLOperationFinder
 * JD-Core Version:    0.6.2
 */