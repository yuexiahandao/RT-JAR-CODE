/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
/*    */ import java.util.ArrayList;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class WSDLMessageImpl extends AbstractExtensibleImpl
/*    */   implements WSDLMessage
/*    */ {
/*    */   private final QName name;
/*    */   private final ArrayList<WSDLPartImpl> parts;
/*    */ 
/*    */   public WSDLMessageImpl(XMLStreamReader xsr, QName name)
/*    */   {
/* 46 */     super(xsr);
/* 47 */     this.name = name;
/* 48 */     this.parts = new ArrayList();
/*    */   }
/*    */ 
/*    */   public QName getName() {
/* 52 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void add(WSDLPartImpl part) {
/* 56 */     this.parts.add(part);
/*    */   }
/*    */ 
/*    */   public Iterable<WSDLPartImpl> parts() {
/* 60 */     return this.parts;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLMessageImpl
 * JD-Core Version:    0.6.2
 */