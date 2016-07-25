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
/*     */ public final class ToXMLSAXHandler extends ToSAXHandler
/*     */ {
/*  53 */   protected boolean m_escapeSetting = true;
/*     */ 
/*     */   public ToXMLSAXHandler()
/*     */   {
/*  58 */     this.m_prefixMap = new NamespaceMappings();
/*  59 */     initCDATA();
/*     */   }
/*     */ 
/*     */   public Properties getOutputFormat()
/*     */   {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/*  83 */     return null;
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
/* 106 */     boolean oldEscapeSetting = this.m_escapeSetting;
/* 107 */     this.m_escapeSetting = escape;
/*     */ 
/* 109 */     if (escape)
/* 110 */       processingInstruction("javax.xml.transform.enable-output-escaping", "");
/*     */     else {
/* 112 */       processingInstruction("javax.xml.transform.disable-output-escaping", "");
/*     */     }
/*     */ 
/* 115 */     return oldEscapeSetting;
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
/*     */   public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void elementDecl(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void externalEntityDecl(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void internalEntityDecl(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 182 */     flushPending();
/*     */ 
/* 185 */     this.m_saxHandler.endDocument();
/*     */ 
/* 187 */     if (this.m_tracer != null)
/* 188 */       super.fireEndDoc();
/*     */   }
/*     */ 
/*     */   protected void closeStartTag()
/*     */     throws SAXException
/*     */   {
/* 198 */     this.m_elemContext.m_startTagOpen = false;
/*     */ 
/* 200 */     String localName = getLocalName(this.m_elemContext.m_elementName);
/* 201 */     String uri = getNamespaceURI(this.m_elemContext.m_elementName, true);
/*     */ 
/* 204 */     if (this.m_needToCallStartDocument)
/*     */     {
/* 206 */       startDocumentInternal();
/*     */     }
/* 208 */     this.m_saxHandler.startElement(uri, localName, this.m_elemContext.m_elementName, this.m_attributes);
/*     */ 
/* 211 */     this.m_attributes.clear();
/*     */ 
/* 213 */     if (this.m_state != null)
/* 214 */       this.m_state.setCurrentNode(null);
/*     */   }
/*     */ 
/*     */   public void closeCDATA()
/*     */     throws SAXException
/*     */   {
/* 228 */     if ((this.m_lexHandler != null) && (this.m_cdataTagOpen)) {
/* 229 */       this.m_lexHandler.endCDATA();
/*     */     }
/*     */ 
/* 237 */     this.m_cdataTagOpen = false;
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 247 */     flushPending();
/*     */ 
/* 249 */     if (namespaceURI == null)
/*     */     {
/* 251 */       if (this.m_elemContext.m_elementURI != null)
/* 252 */         namespaceURI = this.m_elemContext.m_elementURI;
/*     */       else {
/* 254 */         namespaceURI = getNamespaceURI(qName, true);
/*     */       }
/*     */     }
/* 257 */     if (localName == null)
/*     */     {
/* 259 */       if (this.m_elemContext.m_elementLocalName != null)
/* 260 */         localName = this.m_elemContext.m_elementLocalName;
/*     */       else {
/* 262 */         localName = getLocalName(qName);
/*     */       }
/*     */     }
/* 265 */     this.m_saxHandler.endElement(namespaceURI, localName, qName);
/*     */ 
/* 267 */     if (this.m_tracer != null) {
/* 268 */       super.fireEndElem(qName);
/*     */     }
/*     */ 
/* 273 */     this.m_prefixMap.popNamespaces(this.m_elemContext.m_currentElemDepth, this.m_saxHandler);
/*     */ 
/* 275 */     this.m_elemContext = this.m_elemContext.m_prev;
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
/*     */     throws SAXException
/*     */   {
/* 295 */     this.m_saxHandler.ignorableWhitespace(arg0, arg1, arg2);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator arg0)
/*     */   {
/* 303 */     super.setDocumentLocator(arg0);
/* 304 */     this.m_saxHandler.setDocumentLocator(arg0);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/* 312 */     this.m_saxHandler.skippedEntity(arg0);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 323 */     startPrefixMapping(prefix, uri, true);
/*     */   }
/*     */ 
/*     */   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
/*     */     throws SAXException
/*     */   {
/*     */     int pushDepth;
/*     */     int pushDepth;
/* 351 */     if (shouldFlush)
/*     */     {
/* 353 */       flushPending();
/*     */ 
/* 355 */       pushDepth = this.m_elemContext.m_currentElemDepth + 1;
/*     */     }
/*     */     else
/*     */     {
/* 360 */       pushDepth = this.m_elemContext.m_currentElemDepth;
/*     */     }
/* 362 */     boolean pushed = this.m_prefixMap.pushNamespace(prefix, uri, pushDepth);
/*     */ 
/* 364 */     if (pushed)
/*     */     {
/* 366 */       this.m_saxHandler.startPrefixMapping(prefix, uri);
/*     */ 
/* 368 */       if (getShouldOutputNSAttr())
/*     */       {
/* 377 */         if ("".equals(prefix))
/*     */         {
/* 379 */           String name = "xmlns";
/* 380 */           addAttributeAlways("http://www.w3.org/2000/xmlns/", name, name, "CDATA", uri, false);
/*     */         }
/* 384 */         else if (!"".equals(uri))
/*     */         {
/* 386 */           String name = "xmlns:" + prefix;
/*     */ 
/* 392 */           addAttributeAlways("http://www.w3.org/2000/xmlns/", prefix, name, "CDATA", uri, false);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 397 */     return pushed;
/*     */   }
/*     */ 
/*     */   public void comment(char[] arg0, int arg1, int arg2)
/*     */     throws SAXException
/*     */   {
/* 406 */     flushPending();
/* 407 */     if (this.m_lexHandler != null) {
/* 408 */       this.m_lexHandler.comment(arg0, arg1, arg2);
/*     */     }
/* 410 */     if (this.m_tracer != null)
/* 411 */       super.fireCommentEvent(arg0, arg1, arg2);
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
/* 447 */     if (this.m_lexHandler != null)
/* 448 */       this.m_lexHandler.endDTD();
/*     */   }
/*     */ 
/*     */   public void startEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/* 456 */     if (this.m_lexHandler != null)
/* 457 */       this.m_lexHandler.startEntity(arg0);
/*     */   }
/*     */ 
/*     */   public void characters(String chars)
/*     */     throws SAXException
/*     */   {
/* 465 */     int length = chars.length();
/* 466 */     if (length > this.m_charsBuff.length)
/*     */     {
/* 468 */       this.m_charsBuff = new char[length * 2 + 1];
/*     */     }
/* 470 */     chars.getChars(0, length, this.m_charsBuff, 0);
/* 471 */     characters(this.m_charsBuff, 0, length);
/*     */   }
/*     */ 
/*     */   public ToXMLSAXHandler(ContentHandler handler, String encoding)
/*     */   {
/* 477 */     super(handler, encoding);
/*     */ 
/* 479 */     initCDATA();
/*     */ 
/* 481 */     this.m_prefixMap = new NamespaceMappings();
/*     */   }
/*     */ 
/*     */   public ToXMLSAXHandler(ContentHandler handler, LexicalHandler lex, String encoding)
/*     */   {
/* 489 */     super(handler, lex, encoding);
/*     */ 
/* 491 */     initCDATA();
/*     */ 
/* 493 */     this.m_prefixMap = new NamespaceMappings();
/*     */   }
/*     */ 
/*     */   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
/*     */     throws SAXException
/*     */   {
/* 505 */     startElement(elementNamespaceURI, elementLocalName, elementName, null);
/*     */   }
/*     */ 
/*     */   public void startElement(String elementName)
/*     */     throws SAXException
/*     */   {
/* 512 */     startElement(null, null, elementName, null);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int off, int len)
/*     */     throws SAXException
/*     */   {
/* 520 */     if (this.m_needToCallStartDocument)
/*     */     {
/* 522 */       startDocumentInternal();
/* 523 */       this.m_needToCallStartDocument = false;
/*     */     }
/*     */ 
/* 526 */     if (this.m_elemContext.m_startTagOpen)
/*     */     {
/* 528 */       closeStartTag();
/* 529 */       this.m_elemContext.m_startTagOpen = false;
/*     */     }
/*     */ 
/* 532 */     if ((this.m_elemContext.m_isCdataSection) && (!this.m_cdataTagOpen) && (this.m_lexHandler != null))
/*     */     {
/* 535 */       this.m_lexHandler.startCDATA();
/*     */ 
/* 539 */       this.m_cdataTagOpen = true;
/*     */     }
/*     */ 
/* 546 */     this.m_saxHandler.characters(ch, off, len);
/*     */ 
/* 549 */     if (this.m_tracer != null)
/* 550 */       fireCharEvent(ch, off, len);
/*     */   }
/*     */ 
/*     */   public void endElement(String elemName)
/*     */     throws SAXException
/*     */   {
/* 559 */     endElement(null, null, elemName);
/*     */   }
/*     */ 
/*     */   public void namespaceAfterStartElement(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 573 */     startPrefixMapping(prefix, uri, false);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 584 */     flushPending();
/*     */ 
/* 587 */     this.m_saxHandler.processingInstruction(target, data);
/*     */ 
/* 591 */     if (this.m_tracer != null)
/* 592 */       super.fireEscapingEvent(target, data);
/*     */   }
/*     */ 
/*     */   protected boolean popNamespace(String prefix)
/*     */   {
/*     */     try
/*     */     {
/* 603 */       if (this.m_prefixMap.popNamespace(prefix))
/*     */       {
/* 605 */         this.m_saxHandler.endPrefixMapping(prefix);
/* 606 */         return true;
/*     */       }
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/*     */ 
/* 613 */     return false;
/*     */   }
/*     */ 
/*     */   public void startCDATA()
/*     */     throws SAXException
/*     */   {
/* 626 */     if (!this.m_cdataTagOpen)
/*     */     {
/* 628 */       flushPending();
/* 629 */       if (this.m_lexHandler != null) {
/* 630 */         this.m_lexHandler.startCDATA();
/*     */ 
/* 635 */         this.m_cdataTagOpen = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String name, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 650 */     flushPending();
/* 651 */     super.startElement(namespaceURI, localName, name, atts);
/*     */ 
/* 654 */     if (this.m_needToOutputDocTypeDecl)
/*     */     {
/* 656 */       String doctypeSystem = getDoctypeSystem();
/* 657 */       if ((doctypeSystem != null) && (this.m_lexHandler != null))
/*     */       {
/* 659 */         String doctypePublic = getDoctypePublic();
/* 660 */         if (doctypeSystem != null) {
/* 661 */           this.m_lexHandler.startDTD(name, doctypePublic, doctypeSystem);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 666 */       this.m_needToOutputDocTypeDecl = false;
/*     */     }
/* 668 */     this.m_elemContext = this.m_elemContext.push(namespaceURI, localName, name);
/*     */ 
/* 672 */     if (namespaceURI != null) {
/* 673 */       ensurePrefixIsDeclared(namespaceURI, name);
/*     */     }
/*     */ 
/* 676 */     if (atts != null) {
/* 677 */       addAttributes(atts);
/*     */     }
/*     */ 
/* 681 */     this.m_elemContext.m_isCdataSection = isCdataSection();
/*     */   }
/*     */ 
/*     */   private void ensurePrefixIsDeclared(String ns, String rawName)
/*     */     throws SAXException
/*     */   {
/* 689 */     if ((ns != null) && (ns.length() > 0))
/*     */     {
/*     */       int index;
/* 692 */       boolean no_prefix = (index = rawName.indexOf(":")) < 0;
/* 693 */       String prefix = no_prefix ? "" : rawName.substring(0, index);
/*     */ 
/* 696 */       if (null != prefix)
/*     */       {
/* 698 */         String foundURI = this.m_prefixMap.lookupNamespace(prefix);
/*     */ 
/* 700 */         if ((null == foundURI) || (!foundURI.equals(ns)))
/*     */         {
/* 702 */           startPrefixMapping(prefix, ns, false);
/*     */ 
/* 704 */           if (getShouldOutputNSAttr())
/*     */           {
/* 707 */             addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, "xmlns:" + prefix, "CDATA", ns, false);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
/*     */     throws SAXException
/*     */   {
/* 742 */     if (this.m_elemContext.m_startTagOpen)
/*     */     {
/* 744 */       ensurePrefixIsDeclared(uri, rawName);
/* 745 */       addAttributeAlways(uri, localName, rawName, type, value, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */   {
/* 760 */     boolean wasReset = false;
/* 761 */     if (super.reset())
/*     */     {
/* 763 */       resetToXMLSAXHandler();
/* 764 */       wasReset = true;
/*     */     }
/* 766 */     return wasReset;
/*     */   }
/*     */ 
/*     */   private void resetToXMLSAXHandler()
/*     */   {
/* 775 */     this.m_escapeSetting = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler
 * JD-Core Version:    0.6.2
 */