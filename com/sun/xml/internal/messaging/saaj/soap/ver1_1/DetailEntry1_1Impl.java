/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.DetailEntryImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.Name;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPException;
/*    */ 
/*    */ public class DetailEntry1_1Impl extends DetailEntryImpl
/*    */ {
/*    */   public DetailEntry1_1Impl(SOAPDocumentImpl ownerDoc, Name qname)
/*    */   {
/* 43 */     super(ownerDoc, qname);
/*    */   }
/*    */   public DetailEntry1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
/* 46 */     super(ownerDoc, qname);
/*    */   }
/*    */   public SOAPElement setElementQName(QName newName) throws SOAPException {
/* 49 */     DetailEntryImpl copy = new DetailEntry1_1Impl((SOAPDocumentImpl)getOwnerDocument(), newName);
/*    */ 
/* 51 */     return replaceElementWithSOAPElement(this, copy);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.DetailEntry1_1Impl
 * JD-Core Version:    0.6.2
 */