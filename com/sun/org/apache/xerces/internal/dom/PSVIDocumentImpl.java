/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMConfiguration;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class PSVIDocumentImpl extends DocumentImpl
/*     */ {
/*     */   static final long serialVersionUID = -8822220250676434522L;
/*     */ 
/*     */   public PSVIDocumentImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PSVIDocumentImpl(DocumentType doctype)
/*     */   {
/*  57 */     super(doctype);
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean deep)
/*     */   {
/*  71 */     PSVIDocumentImpl newdoc = new PSVIDocumentImpl();
/*  72 */     callUserDataHandlers(this, newdoc, (short)1);
/*  73 */     cloneNode(newdoc, deep);
/*     */ 
/*  76 */     newdoc.mutationEvents = this.mutationEvents;
/*     */ 
/*  78 */     return newdoc;
/*     */   }
/*     */ 
/*     */   public DOMImplementation getImplementation()
/*     */   {
/*  91 */     return PSVIDOMImplementationImpl.getDOMImplementation();
/*     */   }
/*     */ 
/*     */   public Element createElementNS(String namespaceURI, String qualifiedName)
/*     */     throws DOMException
/*     */   {
/*  99 */     return new PSVIElementNSImpl(this, namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public Element createElementNS(String namespaceURI, String qualifiedName, String localpart)
/*     */     throws DOMException
/*     */   {
/* 107 */     return new PSVIElementNSImpl(this, namespaceURI, qualifiedName, localpart);
/*     */   }
/*     */ 
/*     */   public Attr createAttributeNS(String namespaceURI, String qualifiedName)
/*     */     throws DOMException
/*     */   {
/* 115 */     return new PSVIAttrNSImpl(this, namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public Attr createAttributeNS(String namespaceURI, String qualifiedName, String localName)
/*     */     throws DOMException
/*     */   {
/* 123 */     return new PSVIAttrNSImpl(this, namespaceURI, qualifiedName, localName);
/*     */   }
/*     */ 
/*     */   public DOMConfiguration getDomConfig()
/*     */   {
/* 133 */     super.getDomConfig();
/* 134 */     return this.fConfiguration;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 142 */     throw new NotSerializableException(getClass().getName());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
/*     */   {
/* 147 */     throw new NotSerializableException(getClass().getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl
 * JD-Core Version:    0.6.2
 */