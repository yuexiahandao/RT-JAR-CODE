/*    */ package com.sun.xml.internal.ws.client;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*    */ import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
/*    */ import com.sun.xml.internal.ws.model.SOAPSEIModel;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ 
/*    */ final class SEIPortInfo extends PortInfo
/*    */ {
/*    */   public final Class sei;
/*    */   public final SOAPSEIModel model;
/*    */ 
/*    */   public SEIPortInfo(WSServiceDelegate owner, Class sei, SOAPSEIModel model, @NotNull WSDLPort portModel)
/*    */   {
/* 54 */     super(owner, portModel);
/* 55 */     this.sei = sei;
/* 56 */     this.model = model;
/* 57 */     assert ((sei != null) && (model != null));
/*    */   }
/*    */ 
/*    */   public BindingImpl createBinding(WebServiceFeature[] webServiceFeatures, Class<?> portInterface) {
/* 61 */     BindingImpl bindingImpl = super.createBinding(webServiceFeatures, portInterface);
/* 62 */     if ((bindingImpl instanceof SOAPBindingImpl)) {
/* 63 */       ((SOAPBindingImpl)bindingImpl).setPortKnownHeaders(this.model.getKnownHeaders());
/*    */     }
/*    */ 
/* 67 */     return bindingImpl;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.SEIPortInfo
 * JD-Core Version:    0.6.2
 */