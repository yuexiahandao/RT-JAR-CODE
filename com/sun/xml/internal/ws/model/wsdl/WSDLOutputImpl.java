/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class WSDLOutputImpl extends AbstractExtensibleImpl
/*    */   implements WSDLOutput
/*    */ {
/*    */   private String name;
/*    */   private QName messageName;
/*    */   private WSDLOperationImpl operation;
/*    */   private WSDLMessageImpl message;
/*    */   private String action;
/* 45 */   private boolean defaultAction = true;
/*    */ 
/* 47 */   public WSDLOutputImpl(XMLStreamReader xsr, String name, QName messageName, WSDLOperationImpl operation) { super(xsr);
/* 48 */     this.name = name;
/* 49 */     this.messageName = messageName;
/* 50 */     this.operation = operation; }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 54 */     return this.name == null ? this.operation.getName().getLocalPart() + "Response" : this.name;
/*    */   }
/*    */ 
/*    */   public WSDLMessage getMessage() {
/* 58 */     return this.message;
/*    */   }
/*    */ 
/*    */   public String getAction() {
/* 62 */     return this.action;
/*    */   }
/*    */ 
/*    */   public boolean isDefaultAction() {
/* 66 */     return this.defaultAction;
/*    */   }
/*    */ 
/*    */   public void setDefaultAction(boolean defaultAction) {
/* 70 */     this.defaultAction = defaultAction;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public WSDLOperation getOperation() {
/* 75 */     return this.operation;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public QName getQName() {
/* 80 */     return new QName(this.operation.getName().getNamespaceURI(), getName());
/*    */   }
/*    */ 
/*    */   public void setAction(String action) {
/* 84 */     this.action = action;
/*    */   }
/*    */ 
/*    */   void freeze(WSDLModelImpl root) {
/* 88 */     this.message = root.getMessage(this.messageName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLOutputImpl
 * JD-Core Version:    0.6.2
 */