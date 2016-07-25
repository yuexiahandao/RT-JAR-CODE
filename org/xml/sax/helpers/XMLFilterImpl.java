/*     */ package org.xml.sax.helpers;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLFilter;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class XMLFilterImpl
/*     */   implements XMLFilter, EntityResolver, DTDHandler, ContentHandler, ErrorHandler
/*     */ {
/* 728 */   private XMLReader parent = null;
/* 729 */   private Locator locator = null;
/* 730 */   private EntityResolver entityResolver = null;
/* 731 */   private DTDHandler dtdHandler = null;
/* 732 */   private ContentHandler contentHandler = null;
/* 733 */   private ErrorHandler errorHandler = null;
/*     */ 
/*     */   public XMLFilterImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLFilterImpl(XMLReader parent)
/*     */   {
/* 114 */     setParent(parent);
/*     */   }
/*     */ 
/*     */   public void setParent(XMLReader parent)
/*     */   {
/* 139 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public XMLReader getParent()
/*     */   {
/* 151 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 177 */     if (this.parent != null)
/* 178 */       this.parent.setFeature(name, value);
/*     */     else
/* 180 */       throw new SAXNotRecognizedException("Feature: " + name);
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 201 */     if (this.parent != null) {
/* 202 */       return this.parent.getFeature(name);
/*     */     }
/* 204 */     throw new SAXNotRecognizedException("Feature: " + name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 225 */     if (this.parent != null)
/* 226 */       this.parent.setProperty(name, value);
/*     */     else
/* 228 */       throw new SAXNotRecognizedException("Property: " + name);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 247 */     if (this.parent != null) {
/* 248 */       return this.parent.getProperty(name);
/*     */     }
/* 250 */     throw new SAXNotRecognizedException("Property: " + name);
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */   {
/* 262 */     this.entityResolver = resolver;
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver()
/*     */   {
/* 273 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */   {
/* 284 */     this.dtdHandler = handler;
/*     */   }
/*     */ 
/*     */   public DTDHandler getDTDHandler()
/*     */   {
/* 295 */     return this.dtdHandler;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */   {
/* 306 */     this.contentHandler = handler;
/*     */   }
/*     */ 
/*     */   public ContentHandler getContentHandler()
/*     */   {
/* 317 */     return this.contentHandler;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler handler)
/*     */   {
/* 328 */     this.errorHandler = handler;
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler()
/*     */   {
/* 339 */     return this.errorHandler;
/*     */   }
/*     */ 
/*     */   public void parse(InputSource input)
/*     */     throws SAXException, IOException
/*     */   {
/* 356 */     setupParse();
/* 357 */     this.parent.parse(input);
/*     */   }
/*     */ 
/*     */   public void parse(String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 374 */     parse(new InputSource(systemId));
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 399 */     if (this.entityResolver != null) {
/* 400 */       return this.entityResolver.resolveEntity(publicId, systemId);
/*     */     }
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 425 */     if (this.dtdHandler != null)
/* 426 */       this.dtdHandler.notationDecl(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*     */     throws SAXException
/*     */   {
/* 445 */     if (this.dtdHandler != null)
/* 446 */       this.dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 465 */     this.locator = locator;
/* 466 */     if (this.contentHandler != null)
/* 467 */       this.contentHandler.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 481 */     if (this.contentHandler != null)
/* 482 */       this.contentHandler.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 496 */     if (this.contentHandler != null)
/* 497 */       this.contentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 513 */     if (this.contentHandler != null)
/* 514 */       this.contentHandler.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/* 529 */     if (this.contentHandler != null)
/* 530 */       this.contentHandler.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 550 */     if (this.contentHandler != null)
/* 551 */       this.contentHandler.startElement(uri, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 569 */     if (this.contentHandler != null)
/* 570 */       this.contentHandler.endElement(uri, localName, qName);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 587 */     if (this.contentHandler != null)
/* 588 */       this.contentHandler.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 605 */     if (this.contentHandler != null)
/* 606 */       this.contentHandler.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 622 */     if (this.contentHandler != null)
/* 623 */       this.contentHandler.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 638 */     if (this.contentHandler != null)
/* 639 */       this.contentHandler.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/* 660 */     if (this.errorHandler != null)
/* 661 */       this.errorHandler.warning(e);
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/* 676 */     if (this.errorHandler != null)
/* 677 */       this.errorHandler.error(e);
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/* 692 */     if (this.errorHandler != null)
/* 693 */       this.errorHandler.fatalError(e);
/*     */   }
/*     */ 
/*     */   private void setupParse()
/*     */   {
/* 713 */     if (this.parent == null) {
/* 714 */       throw new NullPointerException("No parent for filter");
/*     */     }
/* 716 */     this.parent.setEntityResolver(this);
/* 717 */     this.parent.setDTDHandler(this);
/* 718 */     this.parent.setContentHandler(this);
/* 719 */     this.parent.setErrorHandler(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.XMLFilterImpl
 * JD-Core Version:    0.6.2
 */