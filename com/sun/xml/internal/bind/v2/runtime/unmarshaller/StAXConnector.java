/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ abstract class StAXConnector
/*     */ {
/*     */   protected final XmlVisitor visitor;
/*     */   protected final UnmarshallingContext context;
/*     */   protected final XmlVisitor.TextPredictor predictor;
/*  55 */   protected final TagName tagName = new TagNameImpl(null);
/*     */ 
/*     */   public abstract void bridge() throws XMLStreamException;
/*     */ 
/*  58 */   protected StAXConnector(XmlVisitor visitor) { this.visitor = visitor;
/*  59 */     this.context = visitor.getContext();
/*  60 */     this.predictor = visitor.getPredictor();
/*     */   }
/*     */ 
/*     */   protected abstract Location getCurrentLocation();
/*     */ 
/*     */   protected abstract String getCurrentQName();
/*     */ 
/*     */   protected final void handleStartDocument(NamespaceContext nsc)
/*     */     throws SAXException
/*     */   {
/*  75 */     this.visitor.startDocument(new LocatorEx() {
/*     */       public ValidationEventLocator getLocation() {
/*  77 */         return new ValidationEventLocatorImpl(this);
/*     */       }
/*     */       public int getColumnNumber() {
/*  80 */         return StAXConnector.this.getCurrentLocation().getColumnNumber();
/*     */       }
/*     */       public int getLineNumber() {
/*  83 */         return StAXConnector.this.getCurrentLocation().getLineNumber();
/*     */       }
/*     */       public String getPublicId() {
/*  86 */         return StAXConnector.this.getCurrentLocation().getPublicId();
/*     */       }
/*     */       public String getSystemId() {
/*  89 */         return StAXConnector.this.getCurrentLocation().getSystemId();
/*     */       }
/*     */     }
/*     */     , nsc);
/*     */   }
/*     */ 
/*     */   protected final void handleEndDocument()
/*     */     throws SAXException
/*     */   {
/*  95 */     this.visitor.endDocument();
/*     */   }
/*     */ 
/*     */   protected static String fixNull(String s) {
/*  99 */     if (s == null) return "";
/* 100 */     return s;
/*     */   }
/*     */ 
/*     */   protected final String getQName(String prefix, String localName) {
/* 104 */     if ((prefix == null) || (prefix.length() == 0)) {
/* 105 */       return localName;
/*     */     }
/* 107 */     return prefix + ':' + localName;
/*     */   }
/*     */ 
/*     */   private final class TagNameImpl extends TagName
/*     */   {
/*     */     private TagNameImpl()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String getQname()
/*     */     {
/*  51 */       return StAXConnector.this.getCurrentQName();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector
 * JD-Core Version:    0.6.2
 */