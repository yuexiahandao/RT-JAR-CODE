/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
/*    */ import com.sun.xml.internal.ws.api.policy.PolicyResolver;
/*    */ import com.sun.xml.internal.ws.api.server.Container;
/*    */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;
/*    */ 
/*    */ final class WSDLParserExtensionContextImpl
/*    */   implements WSDLParserExtensionContext
/*    */ {
/*    */   private final boolean isClientSide;
/*    */   private final WSDLModel wsdlModel;
/*    */   private final Container container;
/*    */   private final PolicyResolver policyResolver;
/*    */ 
/*    */   protected WSDLParserExtensionContextImpl(WSDLModel model, boolean isClientSide, Container container, PolicyResolver policyResolver)
/*    */   {
/* 50 */     this.wsdlModel = model;
/* 51 */     this.isClientSide = isClientSide;
/* 52 */     this.container = container;
/* 53 */     this.policyResolver = policyResolver;
/*    */   }
/*    */ 
/*    */   public boolean isClientSide() {
/* 57 */     return this.isClientSide;
/*    */   }
/*    */ 
/*    */   public WSDLModel getWSDLModel() {
/* 61 */     return this.wsdlModel;
/*    */   }
/*    */ 
/*    */   public Container getContainer() {
/* 65 */     return this.container;
/*    */   }
/*    */ 
/*    */   public PolicyResolver getPolicyResolver() {
/* 69 */     return this.policyResolver;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.WSDLParserExtensionContextImpl
 * JD-Core Version:    0.6.2
 */