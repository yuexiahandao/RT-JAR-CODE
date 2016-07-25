/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class SAXParserHandler extends DefaultHandler
/*     */ {
/*  42 */   private EntityResolver er = null;
/*  43 */   private ContentHandler ch = null;
/*     */ 
/*     */   public void setEntityResolver(EntityResolver er)
/*     */   {
/*  50 */     this.er = er;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler ch) {
/*  54 */     this.ch = ch;
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/*  61 */     if (this.er != null) {
/*     */       try {
/*  63 */         return this.er.resolveEntity(publicId, systemId);
/*     */       } catch (IOException e) {
/*  65 */         System.out.println("resolveEntity threw IOException!");
/*  66 */         return null;
/*     */       }
/*     */     }
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*  76 */     if (this.ch != null)
/*  77 */       this.ch.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*  83 */     if (this.ch != null)
/*  84 */       this.ch.endDocument();
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*  90 */     if (this.ch != null)
/*  91 */       this.ch.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/*  97 */     if (this.ch != null)
/*  98 */       this.ch.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 104 */     if (this.ch != null)
/* 105 */       this.ch.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 111 */     if (this.ch != null)
/* 112 */       this.ch.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 117 */     if (this.ch != null)
/* 118 */       this.ch.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 124 */     if (this.ch != null)
/* 125 */       this.ch.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 131 */     if (this.ch != null)
/* 132 */       this.ch.startDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 139 */     if (this.ch != null)
/* 140 */       this.ch.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 146 */     if (this.ch != null)
/* 147 */       this.ch.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.SAXParserHandler
 * JD-Core Version:    0.6.2
 */