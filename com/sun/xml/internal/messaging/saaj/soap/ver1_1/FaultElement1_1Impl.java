/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPException;
/*    */ 
/*    */ public class FaultElement1_1Impl extends FaultElementImpl
/*    */ {
/*    */   public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc, NameImpl qname)
/*    */   {
/* 43 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
/* 47 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc, String localName)
/*    */   {
/* 52 */     super(ownerDoc, NameImpl.createFaultElement1_1Name(localName));
/*    */   }
/*    */ 
/*    */   public FaultElement1_1Impl(SOAPDocumentImpl ownerDoc, String localName, String prefix)
/*    */   {
/* 58 */     super(ownerDoc, NameImpl.createFaultElement1_1Name(localName, prefix));
/*    */   }
/*    */ 
/*    */   protected boolean isStandardFaultElement()
/*    */   {
/* 63 */     String localName = this.elementQName.getLocalPart();
/* 64 */     if ((localName.equalsIgnoreCase("faultcode")) || (localName.equalsIgnoreCase("faultstring")) || (localName.equalsIgnoreCase("faultactor")))
/*    */     {
/* 67 */       return true;
/*    */     }
/* 69 */     return false;
/*    */   }
/*    */ 
/*    */   public SOAPElement setElementQName(QName newName) throws SOAPException {
/* 73 */     if (!isStandardFaultElement()) {
/* 74 */       FaultElement1_1Impl copy = new FaultElement1_1Impl((SOAPDocumentImpl)getOwnerDocument(), newName);
/*    */ 
/* 76 */       return replaceElementWithSOAPElement(this, copy);
/*    */     }
/* 78 */     return super.setElementQName(newName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.FaultElement1_1Impl
 * JD-Core Version:    0.6.2
 */