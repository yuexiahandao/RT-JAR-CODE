/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class WSDLPortTypeImpl extends AbstractExtensibleImpl
/*    */   implements WSDLPortType
/*    */ {
/*    */   private QName name;
/*    */   private final Map<String, WSDLOperationImpl> portTypeOperations;
/*    */   private WSDLModelImpl owner;
/*    */ 
/*    */   public WSDLPortTypeImpl(XMLStreamReader xsr, WSDLModelImpl owner, QName name)
/*    */   {
/* 47 */     super(xsr);
/* 48 */     this.name = name;
/* 49 */     this.owner = owner;
/* 50 */     this.portTypeOperations = new Hashtable();
/*    */   }
/*    */ 
/*    */   public QName getName() {
/* 54 */     return this.name;
/*    */   }
/*    */ 
/*    */   public WSDLOperationImpl get(String operationName) {
/* 58 */     return (WSDLOperationImpl)this.portTypeOperations.get(operationName);
/*    */   }
/*    */ 
/*    */   public Iterable<WSDLOperationImpl> getOperations() {
/* 62 */     return this.portTypeOperations.values();
/*    */   }
/*    */ 
/*    */   public void put(String opName, WSDLOperationImpl ptOp)
/*    */   {
/* 72 */     this.portTypeOperations.put(opName, ptOp);
/*    */   }
/*    */ 
/*    */   WSDLModelImpl getOwner() {
/* 76 */     return this.owner;
/*    */   }
/*    */ 
/*    */   void freeze() {
/* 80 */     for (WSDLOperationImpl op : this.portTypeOperations.values())
/* 81 */       op.freez(this.owner);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLPortTypeImpl
 * JD-Core Version:    0.6.2
 */