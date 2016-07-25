/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class WSDLFaultImpl extends AbstractExtensibleImpl
/*    */   implements WSDLFault
/*    */ {
/*    */   private final String name;
/*    */   private final QName messageName;
/*    */   private WSDLMessageImpl message;
/*    */   private WSDLOperationImpl operation;
/* 43 */   private String action = "";
/* 44 */   private boolean defaultAction = true;
/*    */ 
/*    */   public WSDLFaultImpl(XMLStreamReader xsr, String name, QName messageName, WSDLOperationImpl operation) {
/* 47 */     super(xsr);
/* 48 */     this.name = name;
/* 49 */     this.messageName = messageName;
/* 50 */     this.operation = operation;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 54 */     return this.name;
/*    */   }
/*    */ 
/*    */   public WSDLMessageImpl getMessage() {
/* 58 */     return this.message;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public WSDLOperation getOperation() {
/* 63 */     return this.operation;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public QName getQName() {
/* 68 */     return new QName(this.operation.getName().getNamespaceURI(), this.name);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public String getAction() {
/* 73 */     return this.action;
/*    */   }
/*    */   public void setAction(String action) {
/* 76 */     this.action = action;
/*    */   }
/*    */ 
/*    */   public boolean isDefaultAction() {
/* 80 */     return this.defaultAction;
/*    */   }
/*    */ 
/*    */   public void setDefaultAction(boolean defaultAction) {
/* 84 */     this.defaultAction = defaultAction;
/*    */   }
/*    */ 
/*    */   void freeze(WSDLModelImpl root) {
/* 88 */     this.message = root.getMessage(this.messageName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLFaultImpl
 * JD-Core Version:    0.6.2
 */