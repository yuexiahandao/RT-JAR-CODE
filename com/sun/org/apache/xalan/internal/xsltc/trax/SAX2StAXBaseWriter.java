/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLReporter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public abstract class SAX2StAXBaseWriter extends DefaultHandler
/*     */   implements LexicalHandler
/*     */ {
/*     */   protected boolean isCDATA;
/*     */   protected StringBuffer CDATABuffer;
/*     */   protected Vector namespaces;
/*     */   protected Locator docLocator;
/*     */   protected XMLReporter reporter;
/*     */ 
/*     */   public SAX2StAXBaseWriter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SAX2StAXBaseWriter(XMLReporter reporter)
/*     */   {
/*  61 */     this.reporter = reporter;
/*     */   }
/*     */ 
/*     */   public void setXMLReporter(XMLReporter reporter) {
/*  65 */     this.reporter = reporter;
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator) {
/*  69 */     this.docLocator = locator;
/*     */   }
/*     */ 
/*     */   public Location getCurrentLocation()
/*     */   {
/*  74 */     if (this.docLocator != null) {
/*  75 */       return new SAXLocation(this.docLocator, null);
/*     */     }
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/*  83 */     reportException("ERROR", e);
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/*  87 */     reportException("FATAL", e);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException e) throws SAXException {
/*  91 */     reportException("WARNING", e);
/*     */   }
/*     */ 
/*     */   public void startDocument() throws SAXException {
/*  95 */     this.namespaces = new Vector(2);
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/*  99 */     this.namespaces = null;
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
/*     */   {
/* 104 */     this.namespaces = null;
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException
/*     */   {
/* 109 */     this.namespaces = null;
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 115 */     if (prefix == null)
/* 116 */       prefix = "";
/* 117 */     else if (prefix.equals("xml")) {
/* 118 */       return;
/*     */     }
/*     */ 
/* 121 */     if (this.namespaces == null) {
/* 122 */       this.namespaces = new Vector(2);
/*     */     }
/* 124 */     this.namespaces.addElement(prefix);
/* 125 */     this.namespaces.addElement(uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startCDATA() throws SAXException {
/* 133 */     this.isCDATA = true;
/* 134 */     if (this.CDATABuffer == null)
/* 135 */       this.CDATABuffer = new StringBuffer();
/*     */     else
/* 137 */       this.CDATABuffer.setLength(0);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 143 */     if (this.isCDATA)
/* 144 */       this.CDATABuffer.append(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void endCDATA() throws SAXException
/*     */   {
/* 149 */     this.isCDATA = false;
/* 150 */     this.CDATABuffer.setLength(0);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDTD() throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endEntity(String name) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startEntity(String name) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void reportException(String type, SAXException e) throws SAXException
/*     */   {
/* 176 */     if (this.reporter != null)
/*     */       try {
/* 178 */         this.reporter.report(e.getMessage(), type, e, getCurrentLocation());
/*     */       } catch (XMLStreamException e1) {
/* 180 */         throw new SAXException(e1);
/*     */       }
/*     */   }
/*     */ 
/*     */   public static final void parseQName(String qName, String[] results)
/*     */   {
/* 197 */     int idx = qName.indexOf(':');
/*     */     String local;
/*     */     String prefix;
/*     */     String local;
/* 198 */     if (idx >= 0) {
/* 199 */       String prefix = qName.substring(0, idx);
/* 200 */       local = qName.substring(idx + 1);
/*     */     } else {
/* 202 */       prefix = "";
/* 203 */       local = qName;
/*     */     }
/* 205 */     results[0] = prefix;
/* 206 */     results[1] = local;
/*     */   }
/*     */ 
/*     */   private static final class SAXLocation
/*     */     implements Location
/*     */   {
/*     */     private int lineNumber;
/*     */     private int columnNumber;
/*     */     private String publicId;
/*     */     private String systemId;
/*     */ 
/*     */     private SAXLocation(Locator locator)
/*     */     {
/* 222 */       this.lineNumber = locator.getLineNumber();
/* 223 */       this.columnNumber = locator.getColumnNumber();
/* 224 */       this.publicId = locator.getPublicId();
/* 225 */       this.systemId = locator.getSystemId();
/*     */     }
/*     */ 
/*     */     public int getLineNumber() {
/* 229 */       return this.lineNumber;
/*     */     }
/*     */ 
/*     */     public int getColumnNumber() {
/* 233 */       return this.columnNumber;
/*     */     }
/*     */ 
/*     */     public int getCharacterOffset() {
/* 237 */       return -1;
/*     */     }
/*     */ 
/*     */     public String getPublicId() {
/* 241 */       return this.publicId;
/*     */     }
/*     */ 
/*     */     public String getSystemId() {
/* 245 */       return this.systemId;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter
 * JD-Core Version:    0.6.2
 */