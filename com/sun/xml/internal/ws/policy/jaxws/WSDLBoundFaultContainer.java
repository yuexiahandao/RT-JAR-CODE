/*    */ package com.sun.xml.internal.ws.policy.jaxws;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLObject;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ class WSDLBoundFaultContainer
/*    */   implements WSDLObject
/*    */ {
/*    */   private final WSDLBoundFault boundFault;
/*    */   private final WSDLBoundOperation boundOperation;
/*    */ 
/*    */   public WSDLBoundFaultContainer(WSDLBoundFault fault, WSDLBoundOperation operation)
/*    */   {
/* 46 */     this.boundFault = fault;
/* 47 */     this.boundOperation = operation;
/*    */   }
/*    */ 
/*    */   public Locator getLocation() {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   public WSDLBoundFault getBoundFault() {
/* 55 */     return this.boundFault;
/*    */   }
/*    */ 
/*    */   public WSDLBoundOperation getBoundOperation() {
/* 59 */     return this.boundOperation;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.jaxws.WSDLBoundFaultContainer
 * JD-Core Version:    0.6.2
 */