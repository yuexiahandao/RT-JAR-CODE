/*    */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPException;
/*    */ import javax.xml.soap.SOAPFaultElement;
/*    */ 
/*    */ public abstract class FaultElementImpl extends ElementImpl
/*    */   implements SOAPFaultElement
/*    */ {
/*    */   protected FaultElementImpl(SOAPDocumentImpl ownerDoc, NameImpl qname)
/*    */   {
/* 43 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   protected FaultElementImpl(SOAPDocumentImpl ownerDoc, QName qname) {
/* 47 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   protected abstract boolean isStandardFaultElement();
/*    */ 
/*    */   public SOAPElement setElementQName(QName newName) throws SOAPException {
/* 53 */     log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[] { this.elementQName.getLocalPart(), newName.getLocalPart() });
/*    */ 
/* 57 */     throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl
 * JD-Core Version:    0.6.2
 */