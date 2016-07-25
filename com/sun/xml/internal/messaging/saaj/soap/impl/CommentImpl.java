/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class CommentImpl extends com.sun.org.apache.xerces.internal.dom.CommentImpl
/*     */   implements javax.xml.soap.Text, Comment
/*     */ {
/*  44 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.impl", "com.sun.xml.internal.messaging.saaj.soap.impl.LocalStrings");
/*     */ 
/*  47 */   protected static ResourceBundle rb = log.getResourceBundle();
/*     */ 
/*     */   public CommentImpl(SOAPDocumentImpl ownerDoc, String text)
/*     */   {
/*  51 */     super(ownerDoc, text);
/*     */   }
/*     */ 
/*     */   public String getValue() {
/*  55 */     String nodeValue = getNodeValue();
/*  56 */     return nodeValue.equals("") ? null : nodeValue;
/*     */   }
/*     */ 
/*     */   public void setValue(String text) {
/*  60 */     setNodeValue(text);
/*     */   }
/*     */ 
/*     */   public void setParentElement(SOAPElement element) throws SOAPException
/*     */   {
/*  65 */     if (element == null) {
/*  66 */       log.severe("SAAJ0112.impl.no.null.to.parent.elem");
/*  67 */       throw new SOAPException("Cannot pass NULL to setParentElement");
/*     */     }
/*  69 */     ((ElementImpl)element).addNode(this);
/*     */   }
/*     */ 
/*     */   public SOAPElement getParentElement() {
/*  73 */     return (SOAPElement)getParentNode();
/*     */   }
/*     */ 
/*     */   public void detachNode() {
/*  77 */     Node parent = getParentNode();
/*  78 */     if (parent != null)
/*  79 */       parent.removeChild(this);
/*     */   }
/*     */ 
/*     */   public void recycleNode()
/*     */   {
/*  84 */     detachNode();
/*     */   }
/*     */ 
/*     */   public boolean isComment()
/*     */   {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   public org.w3c.dom.Text splitText(int offset) throws DOMException {
/*  95 */     log.severe("SAAJ0113.impl.cannot.split.text.from.comment");
/*  96 */     throw new UnsupportedOperationException("Cannot split text from a Comment Node.");
/*     */   }
/*     */ 
/*     */   public org.w3c.dom.Text replaceWholeText(String content) throws DOMException {
/* 100 */     log.severe("SAAJ0114.impl.cannot.replace.wholetext.from.comment");
/* 101 */     throw new UnsupportedOperationException("Cannot replace Whole Text from a Comment Node.");
/*     */   }
/*     */ 
/*     */   public String getWholeText()
/*     */   {
/* 106 */     throw new UnsupportedOperationException("Not Supported");
/*     */   }
/*     */ 
/*     */   public boolean isElementContentWhitespace()
/*     */   {
/* 111 */     throw new UnsupportedOperationException("Not Supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.CommentImpl
 * JD-Core Version:    0.6.2
 */