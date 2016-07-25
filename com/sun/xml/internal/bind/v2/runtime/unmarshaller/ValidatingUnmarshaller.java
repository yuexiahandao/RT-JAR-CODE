/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.util.FatalAdapter;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class ValidatingUnmarshaller
/*     */   implements XmlVisitor, XmlVisitor.TextPredictor
/*     */ {
/*     */   private final XmlVisitor next;
/*     */   private final ValidatorHandler validator;
/*  45 */   private NamespaceContext nsContext = null;
/*     */   private final XmlVisitor.TextPredictor predictor;
/*  52 */   private char[] buf = new char[256];
/*     */ 
/*     */   public ValidatingUnmarshaller(Schema schema, XmlVisitor next)
/*     */   {
/*  58 */     this.validator = schema.newValidatorHandler();
/*  59 */     this.next = next;
/*  60 */     this.predictor = next.getPredictor();
/*     */ 
/*  63 */     this.validator.setErrorHandler(new FatalAdapter(getContext()));
/*     */   }
/*     */ 
/*     */   public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException {
/*  67 */     this.nsContext = nsContext;
/*  68 */     this.validator.setDocumentLocator(locator);
/*  69 */     this.validator.startDocument();
/*  70 */     this.next.startDocument(locator, nsContext);
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/*  74 */     this.nsContext = null;
/*  75 */     this.validator.endDocument();
/*  76 */     this.next.endDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(TagName tagName) throws SAXException {
/*  80 */     if (this.nsContext != null) {
/*  81 */       String tagNamePrefix = tagName.getPrefix().intern();
/*  82 */       if (tagNamePrefix != "") {
/*  83 */         this.validator.startPrefixMapping(tagNamePrefix, this.nsContext.getNamespaceURI(tagNamePrefix));
/*     */       }
/*     */     }
/*  86 */     this.validator.startElement(tagName.uri, tagName.local, tagName.getQname(), tagName.atts);
/*  87 */     this.next.startElement(tagName);
/*     */   }
/*     */ 
/*     */   public void endElement(TagName tagName) throws SAXException {
/*  91 */     this.validator.endElement(tagName.uri, tagName.local, tagName.getQname());
/*  92 */     this.next.endElement(tagName);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
/*  96 */     this.validator.startPrefixMapping(prefix, nsUri);
/*  97 */     this.next.startPrefixMapping(prefix, nsUri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 101 */     this.validator.endPrefixMapping(prefix);
/* 102 */     this.next.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void text(CharSequence pcdata) throws SAXException {
/* 106 */     int len = pcdata.length();
/* 107 */     if (this.buf.length < len) {
/* 108 */       this.buf = new char[len];
/*     */     }
/* 110 */     for (int i = 0; i < len; i++) {
/* 111 */       this.buf[i] = pcdata.charAt(i);
/*     */     }
/* 113 */     this.validator.characters(this.buf, 0, len);
/* 114 */     if (this.predictor.expectText())
/* 115 */       this.next.text(pcdata);
/*     */   }
/*     */ 
/*     */   public UnmarshallingContext getContext() {
/* 119 */     return this.next.getContext();
/*     */   }
/*     */ 
/*     */   public XmlVisitor.TextPredictor getPredictor() {
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public boolean expectText()
/*     */   {
/* 132 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.ValidatingUnmarshaller
 * JD-Core Version:    0.6.2
 */