/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class WSDLInputImpl extends AbstractExtensibleImpl
/*    */   implements WSDLInput
/*    */ {
/*    */   private String name;
/*    */   private QName messageName;
/*    */   private WSDLOperationImpl operation;
/*    */   private WSDLMessageImpl message;
/*    */   private String action;
/* 46 */   private boolean defaultAction = true;
/*    */ 
/*    */   public WSDLInputImpl(XMLStreamReader xsr, String name, QName messageName, WSDLOperationImpl operation) {
/* 49 */     super(xsr);
/* 50 */     this.name = name;
/* 51 */     this.messageName = messageName;
/* 52 */     this.operation = operation;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 56 */     if (this.name != null) {
/* 57 */       return this.name;
/*    */     }
/* 59 */     return this.operation.getName().getLocalPart() + "Request";
/*    */   }
/*    */ 
/*    */   public WSDLMessage getMessage() {
/* 63 */     return this.message;
/*    */   }
/*    */ 
/*    */   public String getAction() {
/* 67 */     return this.action;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public WSDLOperation getOperation() {
/* 72 */     return this.operation;
/*    */   }
/*    */ 
/*    */   public QName getQName() {
/* 76 */     return new QName(this.operation.getName().getNamespaceURI(), getName());
/*    */   }
/*    */ 
/*    */   public void setAction(String action) {
/* 80 */     this.action = action;
/*    */   }
/*    */ 
/*    */   public boolean isDefaultAction() {
/* 84 */     return this.defaultAction;
/*    */   }
/*    */ 
/*    */   public void setDefaultAction(boolean defaultAction) {
/* 88 */     this.defaultAction = defaultAction;
/*    */   }
/*    */ 
/*    */   void freeze(WSDLModelImpl parent) {
/* 92 */     this.message = parent.getMessage(this.messageName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLInputImpl
 * JD-Core Version:    0.6.2
 */