/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class InterningXmlVisitor
/*     */   implements XmlVisitor
/*     */ {
/*     */   private final XmlVisitor next;
/*  41 */   private final AttributesImpl attributes = new AttributesImpl(null);
/*     */ 
/*     */   public InterningXmlVisitor(XmlVisitor next) {
/*  44 */     this.next = next;
/*     */   }
/*     */ 
/*     */   public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException {
/*  48 */     this.next.startDocument(locator, nsContext);
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/*  52 */     this.next.endDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(TagName tagName) throws SAXException {
/*  56 */     this.attributes.setAttributes(tagName.atts);
/*  57 */     tagName.atts = this.attributes;
/*  58 */     tagName.uri = intern(tagName.uri);
/*  59 */     tagName.local = intern(tagName.local);
/*  60 */     this.next.startElement(tagName);
/*     */   }
/*     */ 
/*     */   public void endElement(TagName tagName) throws SAXException {
/*  64 */     tagName.uri = intern(tagName.uri);
/*  65 */     tagName.local = intern(tagName.local);
/*  66 */     this.next.endElement(tagName);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
/*  70 */     this.next.startPrefixMapping(intern(prefix), intern(nsUri));
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/*  74 */     this.next.endPrefixMapping(intern(prefix));
/*     */   }
/*     */ 
/*     */   public void text(CharSequence pcdata) throws SAXException {
/*  78 */     this.next.text(pcdata);
/*     */   }
/*     */ 
/*     */   public UnmarshallingContext getContext() {
/*  82 */     return this.next.getContext();
/*     */   }
/*     */ 
/*     */   public XmlVisitor.TextPredictor getPredictor() {
/*  86 */     return this.next.getPredictor();
/*     */   }
/*     */ 
/*     */   private static String intern(String s)
/*     */   {
/* 151 */     if (s == null) return null;
/* 152 */     return s.intern();
/*     */   }
/*     */ 
/*     */   private static class AttributesImpl
/*     */     implements Attributes
/*     */   {
/*     */     private Attributes core;
/*     */ 
/*     */     void setAttributes(Attributes att)
/*     */     {
/*  93 */       this.core = att;
/*     */     }
/*     */ 
/*     */     public int getIndex(String qName) {
/*  97 */       return this.core.getIndex(qName);
/*     */     }
/*     */ 
/*     */     public int getIndex(String uri, String localName) {
/* 101 */       return this.core.getIndex(uri, localName);
/*     */     }
/*     */ 
/*     */     public int getLength() {
/* 105 */       return this.core.getLength();
/*     */     }
/*     */ 
/*     */     public String getLocalName(int index) {
/* 109 */       return InterningXmlVisitor.intern(this.core.getLocalName(index));
/*     */     }
/*     */ 
/*     */     public String getQName(int index) {
/* 113 */       return InterningXmlVisitor.intern(this.core.getQName(index));
/*     */     }
/*     */ 
/*     */     public String getType(int index) {
/* 117 */       return InterningXmlVisitor.intern(this.core.getType(index));
/*     */     }
/*     */ 
/*     */     public String getType(String qName) {
/* 121 */       return InterningXmlVisitor.intern(this.core.getType(qName));
/*     */     }
/*     */ 
/*     */     public String getType(String uri, String localName) {
/* 125 */       return InterningXmlVisitor.intern(this.core.getType(uri, localName));
/*     */     }
/*     */ 
/*     */     public String getURI(int index) {
/* 129 */       return InterningXmlVisitor.intern(this.core.getURI(index));
/*     */     }
/*     */ 
/*     */     public String getValue(int index)
/*     */     {
/* 138 */       return this.core.getValue(index);
/*     */     }
/*     */ 
/*     */     public String getValue(String qName) {
/* 142 */       return this.core.getValue(qName);
/*     */     }
/*     */ 
/*     */     public String getValue(String uri, String localName) {
/* 146 */       return this.core.getValue(uri, localName);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor
 * JD-Core Version:    0.6.2
 */