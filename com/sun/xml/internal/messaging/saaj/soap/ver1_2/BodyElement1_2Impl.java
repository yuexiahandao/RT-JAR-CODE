/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.BodyElementImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.Name;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPException;
/*    */ 
/*    */ public class BodyElement1_2Impl extends BodyElementImpl
/*    */ {
/*    */   public BodyElement1_2Impl(SOAPDocumentImpl ownerDoc, Name qname)
/*    */   {
/* 43 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public BodyElement1_2Impl(SOAPDocumentImpl ownerDoc, QName qname) {
/* 47 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public SOAPElement setElementQName(QName newName) throws SOAPException {
/* 51 */     BodyElementImpl copy = new BodyElement1_2Impl((SOAPDocumentImpl)getOwnerDocument(), newName);
/*    */ 
/* 53 */     return replaceElementWithSOAPElement(this, copy);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.BodyElement1_2Impl
 * JD-Core Version:    0.6.2
 */