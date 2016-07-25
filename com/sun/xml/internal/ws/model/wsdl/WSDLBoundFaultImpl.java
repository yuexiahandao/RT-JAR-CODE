/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class WSDLBoundFaultImpl extends AbstractExtensibleImpl
/*    */   implements WSDLBoundFault
/*    */ {
/*    */   private final String name;
/*    */   private WSDLFault fault;
/*    */   private WSDLBoundOperationImpl owner;
/*    */ 
/*    */   public WSDLBoundFaultImpl(XMLStreamReader xsr, String name, WSDLBoundOperationImpl owner)
/*    */   {
/* 46 */     super(xsr);
/* 47 */     this.name = name;
/* 48 */     this.owner = owner;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public String getName()
/*    */   {
/* 54 */     return this.name;
/*    */   }
/*    */ 
/*    */   public QName getQName() {
/* 58 */     if (this.owner.getOperation() != null) {
/* 59 */       return new QName(this.owner.getOperation().getName().getNamespaceURI(), this.name);
/*    */     }
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   public WSDLFault getFault() {
/* 65 */     return this.fault;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public WSDLBoundOperation getBoundOperation() {
/* 70 */     return this.owner;
/*    */   }
/*    */ 
/*    */   void freeze(WSDLBoundOperationImpl root) {
/* 74 */     assert (root != null);
/* 75 */     WSDLOperation op = root.getOperation();
/* 76 */     if (op != null)
/* 77 */       for (WSDLFault f : op.getFaults())
/* 78 */         if (f.getName().equals(this.name)) {
/* 79 */           this.fault = f;
/* 80 */           break;
/*    */         }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLBoundFaultImpl
 * JD-Core Version:    0.6.2
 */