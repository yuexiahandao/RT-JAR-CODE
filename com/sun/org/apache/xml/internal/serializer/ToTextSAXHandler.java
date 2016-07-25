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
/*     */ public final class ToTextSAXHandler extends ToSAXHandler
/*     */ {
/*     */   public void endElement(String elemName)
/*     */     throws SAXException
/*     */   {
/*  53 */     if (this.m_tracer != null)
/*  54 */       super.fireEndElem(elemName);
/*     */   }
/*     */ 
/*     */   public void endElement(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/*  63 */     if (this.m_tracer != null)
/*  64 */       super.fireEndElem(arg2);
/*     */   }
/*     */ 
/*     */   public ToTextSAXHandler(ContentHandler hdlr, LexicalHandler lex, String encoding)
/*     */   {
/*  69 */     super(hdlr, lex, encoding);
/*     */   }
/*     */ 
/*     */   public ToTextSAXHandler(ContentHandler handler, String encoding)
/*     */   {
/*  77 */     super(handler, encoding);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*  83 */     if (this.m_tracer != null)
/*  84 */       super.fireCommentEvent(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void comment(String data) throws SAXException
/*     */   {
/*  89 */     int length = data.length();
/*  90 */     if (length > this.m_charsBuff.length)
/*     */     {
/*  92 */       this.m_charsBuff = new char[length * 2 + 1];
/*     */     }
/*  94 */     data.getChars(0, length, this.m_charsBuff, 0);
/*  95 */     comment(this.m_charsBuff, 0, length);
/*     */   }
/*     */ 
/*     */   public Properties getOutputFormat()
/*     */   {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public void indent(int n)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */   {
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   public void serialize(Node node)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean setEscaping(boolean escape)
/*     */   {
/* 151 */     return false;
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
/*     */   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
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
/*     */   public void endPrefixMapping(String arg0)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/* 253 */     if (this.m_tracer != null)
/* 254 */       super.fireEscapingEvent(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator arg0)
/*     */   {
/* 262 */     super.setDocumentLocator(arg0);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
/*     */     throws SAXException
/*     */   {
/* 282 */     flushPending();
/* 283 */     super.startElement(arg0, arg1, arg2, arg3);
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
/*     */   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
/*     */     throws SAXException
/*     */   {
/* 325 */     super.startElement(elementNamespaceURI, elementLocalName, elementName);
/*     */   }
/*     */ 
/*     */   public void startElement(String elementName)
/*     */     throws SAXException
/*     */   {
/* 331 */     super.startElement(elementName);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 341 */     flushPending();
/* 342 */     this.m_saxHandler.endDocument();
/*     */ 
/* 344 */     if (this.m_tracer != null)
/* 345 */       super.fireEndDoc();
/*     */   }
/*     */ 
/*     */   public void characters(String characters)
/*     */     throws SAXException
/*     */   {
/* 355 */     int length = characters.length();
/* 356 */     if (length > this.m_charsBuff.length)
/*     */     {
/* 358 */       this.m_charsBuff = new char[length * 2 + 1];
/*     */     }
/* 360 */     characters.getChars(0, length, this.m_charsBuff, 0);
/*     */ 
/* 362 */     this.m_saxHandler.characters(this.m_charsBuff, 0, length);
/*     */   }
/*     */ 
/*     */   public void characters(char[] characters, int offset, int length)
/*     */     throws SAXException
/*     */   {
/* 372 */     this.m_saxHandler.characters(characters, offset, length);
/*     */ 
/* 375 */     if (this.m_tracer != null)
/* 376 */       super.fireCharEvent(characters, offset, length);
/*     */   }
/*     */ 
/*     */   public void addAttribute(String name, String value)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
/*     */     throws SAXException
/*     */   {
/* 395 */     return false;
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void namespaceAfterStartElement(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToTextSAXHandler
 * JD-Core Version:    0.6.2
 */