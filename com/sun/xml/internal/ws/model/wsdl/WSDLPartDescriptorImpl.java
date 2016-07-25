/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLDescriptorKind;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class WSDLPartDescriptorImpl extends AbstractObjectImpl
/*    */   implements WSDLPartDescriptor
/*    */ {
/*    */   private QName name;
/*    */   private WSDLDescriptorKind type;
/*    */ 
/*    */   public WSDLPartDescriptorImpl(XMLStreamReader xsr, QName name, WSDLDescriptorKind kind)
/*    */   {
/* 42 */     super(xsr);
/* 43 */     this.name = name;
/* 44 */     this.type = kind;
/*    */   }
/*    */ 
/*    */   public QName name() {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */   public WSDLDescriptorKind type() {
/* 52 */     return this.type;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLPartDescriptorImpl
 * JD-Core Version:    0.6.2
 */