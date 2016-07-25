/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.util.Properties;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public final class ToHTMLSAXHandler extends ToSAXHandler
/*     */ {
/*  54 */   private boolean m_dtdHandled = false;
/*     */ 
/*  59 */   protected boolean m_escapeSetting = true;
/*     */ 
/*     */   public Properties getOutputFormat()
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public void indent(int n)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void serialize(Node node)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean setEscaping(boolean escape)
/*     */     throws SAXException
/*     */   {
/* 118 */     boolean oldEscapeSetting = this.m_escapeSetting;
/* 119 */     this.m_escapeSetting = escape;
/*     */ 
/* 121 */     if (escape)
/* 122 */       processingInstruction("javax.xml.transform.enable-output-escaping", "");
/*     */     else {
/* 124 */       processingInstruction("javax.xml.transform.disable-output-escaping", "");
/*     */     }
/*     */ 
/* 127 */     return oldEscapeSetting;
/*     */   }
/*     */ 
/*     */   public void setIndent(boolean indent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setOutputFormat(Properties format)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setOutputStream(OutputStream output)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer writer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void elementDecl(String name, String model)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void externalEntityDecl(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void internalEntityDecl(String name, String value)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 246 */     flushPending();
/* 247 */     this.m_saxHandler.endElement(uri, localName, qName);
/*     */ 
/* 250 */     if (this.m_tracer != null)
/* 251 */       super.fireEndElem(qName);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 293 */     flushPending();
/* 294 */     this.m_saxHandler.processingInstruction(target, data);
/*     */ 
/* 298 */     if (this.m_tracer != null)
/* 299 */       super.fireEscapingEvent(target, data);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator arg0)
/*     */   {
/* 308 */     super.setDocumentLocator(arg0);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 350 */     flushPending();
/* 351 */     super.startElement(namespaceURI, localName, qName, atts);
/* 352 */     this.m_saxHandler.startElement(namespaceURI, localName, qName, atts);
/* 353 */     this.m_elemContext.m_startTagOpen = false;
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 368 */     flushPending();
/* 369 */     if (this.m_lexHandler != null) {
/* 370 */       this.m_lexHandler.comment(ch, start, length);
/*     */     }
/*     */ 
/* 373 */     if (this.m_tracer != null)
/* 374 */       super.fireCommentEvent(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void endCDATA()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDTD()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startCDATA()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 429 */     flushPending();
/*     */ 
/* 432 */     this.m_saxHandler.endDocument();
/*     */ 
/* 434 */     if (this.m_tracer != null)
/* 435 */       super.fireEndDoc();
/*     */   }
/*     */ 
/*     */   protected void closeStartTag()
/*     */     throws SAXException
/*     */   {
/* 445 */     this.m_elemContext.m_startTagOpen = false;
/*     */ 
/* 448 */     this.m_saxHandler.startElement("", this.m_elemContext.m_elementName, this.m_elemContext.m_elementName, this.m_attributes);
/*     */ 
/* 453 */     this.m_attributes.clear();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(String chars)
/*     */     throws SAXException
/*     */   {
/* 477 */     int length = chars.length();
/* 478 */     if (length > this.m_charsBuff.length)
/*     */     {
/* 480 */       this.m_charsBuff = new char[length * 2 + 1];
/*     */     }
/* 482 */     chars.getChars(0, length, this.m_charsBuff, 0);
/* 483 */     characters(this.m_charsBuff, 0, length);
/*     */   }
/*     */ 
/*     */   public ToHTMLSAXHandler(ContentHandler handler, String encoding)
/*     */   {
/* 494 */     super(handler, encoding);
/*     */   }
/*     */ 
/*     */   public ToHTMLSAXHandler(ContentHandler handler, LexicalHandler lex, String encoding)
/*     */   {
/* 507 */     super(handler, lex, encoding);
/*     */   }
/*     */ 
/*     */   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
/*     */     throws SAXException
/*     */   {
/* 527 */     super.startElement(elementNamespaceURI, elementLocalName, elementName);
/*     */ 
/* 529 */     flushPending();
/*     */ 
/* 532 */     if (!this.m_dtdHandled)
/*     */     {
/* 534 */       String doctypeSystem = getDoctypeSystem();
/* 535 */       String doctypePublic = getDoctypePublic();
/* 536 */       if (((doctypeSystem != null) || (doctypePublic != null)) && 
/* 537 */         (this.m_lexHandler != null)) {
/* 538 */         this.m_lexHandler.startDTD(elementName, doctypePublic, doctypeSystem);
/*     */       }
/*     */ 
/* 543 */       this.m_dtdHandled = true;
/*     */     }
/* 545 */     this.m_elemContext = this.m_elemContext.push(elementNamespaceURI, elementLocalName, elementName);
/*     */   }
/*     */ 
/*     */   public void startElement(String elementName)
/*     */     throws SAXException
/*     */   {
/* 556 */     startElement(null, null, elementName);
/*     */   }
/*     */ 
/*     */   public void endElement(String elementName)
/*     */     throws SAXException
/*     */   {
/* 569 */     flushPending();
/* 570 */     this.m_saxHandler.endElement("", elementName, elementName);
/*     */ 
/* 573 */     if (this.m_tracer != null)
/* 574 */       super.fireEndElem(elementName);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int off, int len)
/*     */     throws SAXException
/*     */   {
/* 609 */     flushPending();
/* 610 */     this.m_saxHandler.characters(ch, off, len);
/*     */ 
/* 613 */     if (this.m_tracer != null)
/* 614 */       super.fireCharEvent(ch, off, len);
/*     */   }
/*     */ 
/*     */   public void flushPending()
/*     */     throws SAXException
/*     */   {
/* 623 */     if (this.m_needToCallStartDocument)
/*     */     {
/* 625 */       startDocumentInternal();
/* 626 */       this.m_needToCallStartDocument = false;
/*     */     }
/*     */ 
/* 629 */     if (this.m_elemContext.m_startTagOpen)
/*     */     {
/* 631 */       closeStartTag();
/* 632 */       this.m_elemContext.m_startTagOpen = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
/*     */     throws SAXException
/*     */   {
/* 661 */     if (shouldFlush)
/* 662 */       flushPending();
/* 663 */     this.m_saxHandler.startPrefixMapping(prefix, uri);
/* 664 */     return false;
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 685 */     startPrefixMapping(prefix, uri, true);
/*     */   }
/*     */ 
/*     */   public void namespaceAfterStartElement(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 705 */     if (this.m_elemContext.m_elementURI == null)
/*     */     {
/* 707 */       String prefix1 = getPrefixPart(this.m_elemContext.m_elementName);
/* 708 */       if ((prefix1 == null) && ("".equals(prefix)))
/*     */       {
/* 714 */         this.m_elemContext.m_elementURI = uri;
/*     */       }
/*     */     }
/* 717 */     startPrefixMapping(prefix, uri, false);
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */   {
/* 730 */     boolean wasReset = false;
/* 731 */     if (super.reset())
/*     */     {
/* 733 */       resetToHTMLSAXHandler();
/* 734 */       wasReset = true;
/*     */     }
/* 736 */     return wasReset;
/*     */   }
/*     */ 
/*     */   private void resetToHTMLSAXHandler()
/*     */   {
/* 745 */     this.m_escapeSetting = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToHTMLSAXHandler
 * JD-Core Version:    0.6.2
 */