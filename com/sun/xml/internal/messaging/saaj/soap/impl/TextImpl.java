/*    */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPException;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public class TextImpl extends com.sun.org.apache.xerces.internal.dom.TextImpl
/*    */   implements javax.xml.soap.Text, org.w3c.dom.Text
/*    */ {
/* 40 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.impl", "com.sun.xml.internal.messaging.saaj.soap.impl.LocalStrings");
/*    */ 
/*    */   public TextImpl(SOAPDocumentImpl ownerDoc, String text)
/*    */   {
/* 45 */     super(ownerDoc, text);
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 49 */     String nodeValue = getNodeValue();
/* 50 */     return nodeValue.equals("") ? null : nodeValue;
/*    */   }
/*    */ 
/*    */   public void setValue(String text) {
/* 54 */     setNodeValue(text);
/*    */   }
/*    */ 
/*    */   public void setParentElement(SOAPElement parent) throws SOAPException {
/* 58 */     if (parent == null) {
/* 59 */       log.severe("SAAJ0126.impl.cannot.locate.ns");
/* 60 */       throw new SOAPException("Cannot pass NULL to setParentElement");
/*    */     }
/* 62 */     ((ElementImpl)parent).addNode(this);
/*    */   }
/*    */ 
/*    */   public SOAPElement getParentElement() {
/* 66 */     return (SOAPElement)getParentNode();
/*    */   }
/*    */ 
/*    */   public void detachNode()
/*    */   {
/* 71 */     Node parent = getParentNode();
/* 72 */     if (parent != null)
/* 73 */       parent.removeChild(this);
/*    */   }
/*    */ 
/*    */   public void recycleNode()
/*    */   {
/* 78 */     detachNode();
/*    */   }
/*    */ 
/*    */   public boolean isComment()
/*    */   {
/* 85 */     String txt = getNodeValue();
/* 86 */     if (txt == null) {
/* 87 */       return false;
/*    */     }
/* 89 */     return (txt.startsWith("<!--")) && (txt.endsWith("-->"));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.TextImpl
 * JD-Core Version:    0.6.2
 */