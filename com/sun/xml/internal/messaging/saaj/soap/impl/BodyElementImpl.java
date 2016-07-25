/*    */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.Name;
/*    */ import javax.xml.soap.SOAPBody;
/*    */ import javax.xml.soap.SOAPBodyElement;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPException;
/*    */ 
/*    */ public abstract class BodyElementImpl extends ElementImpl
/*    */   implements SOAPBodyElement
/*    */ {
/*    */   public BodyElementImpl(SOAPDocumentImpl ownerDoc, Name qname)
/*    */   {
/* 43 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public BodyElementImpl(SOAPDocumentImpl ownerDoc, QName qname) {
/* 47 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public void setParentElement(SOAPElement element) throws SOAPException {
/* 51 */     if (!(element instanceof SOAPBody)) {
/* 52 */       log.severe("SAAJ0101.impl.parent.of.body.elem.mustbe.body");
/* 53 */       throw new SOAPException("Parent of a SOAPBodyElement has to be a SOAPBody");
/*    */     }
/* 55 */     super.setParentElement(element);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.BodyElementImpl
 * JD-Core Version:    0.6.2
 */