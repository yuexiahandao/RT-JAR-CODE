/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import javax.xml.transform.Transformer;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class EmptySerializer
/*     */   implements SerializationHandler
/*     */ {
/*     */   protected static final String ERR = "EmptySerializer method not over-ridden";
/*     */ 
/*     */   protected void couldThrowIOException()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void couldThrowSAXException()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void couldThrowSAXException(char[] chars, int off, int len)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void couldThrowSAXException(String elemQName)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void couldThrowException()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   void aMethodIsCalled()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ContentHandler asContentHandler()
/*     */     throws IOException
/*     */   {
/*  96 */     couldThrowIOException();
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler ch)
/*     */   {
/* 104 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 111 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public Properties getOutputFormat()
/*     */   {
/* 118 */     aMethodIsCalled();
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 126 */     aMethodIsCalled();
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/* 134 */     aMethodIsCalled();
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */   {
/* 142 */     aMethodIsCalled();
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */   public void serialize(Node node)
/*     */     throws IOException
/*     */   {
/* 150 */     couldThrowIOException();
/*     */   }
/*     */ 
/*     */   public void setCdataSectionElements(Vector URI_and_localNames)
/*     */   {
/* 157 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public boolean setEscaping(boolean escape)
/*     */     throws SAXException
/*     */   {
/* 164 */     couldThrowSAXException();
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   public void setIndent(boolean indent)
/*     */   {
/* 172 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setIndentAmount(int spaces)
/*     */   {
/* 179 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setIsStandalone(boolean isStandalone)
/*     */   {
/* 186 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setOutputFormat(Properties format)
/*     */   {
/* 193 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setOutputStream(OutputStream output)
/*     */   {
/* 200 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setVersion(String version)
/*     */   {
/* 207 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer writer)
/*     */   {
/* 214 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setTransformer(Transformer transformer)
/*     */   {
/* 221 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public Transformer getTransformer()
/*     */   {
/* 228 */     aMethodIsCalled();
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   public void flushPending()
/*     */     throws SAXException
/*     */   {
/* 236 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
/*     */     throws SAXException
/*     */   {
/* 250 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void addAttributes(Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 257 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void addAttribute(String name, String value)
/*     */   {
/* 264 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void characters(String chars)
/*     */     throws SAXException
/*     */   {
/* 272 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void endElement(String elemName)
/*     */     throws SAXException
/*     */   {
/* 279 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 286 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 294 */     couldThrowSAXException(qName);
/*     */   }
/*     */ 
/*     */   public void startElement(String qName)
/*     */     throws SAXException
/*     */   {
/* 301 */     couldThrowSAXException(qName);
/*     */   }
/*     */ 
/*     */   public void namespaceAfterStartElement(String uri, String prefix)
/*     */     throws SAXException
/*     */   {
/* 309 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
/*     */     throws SAXException
/*     */   {
/* 320 */     couldThrowSAXException();
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   public void entityReference(String entityName)
/*     */     throws SAXException
/*     */   {
/* 328 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public NamespaceMappings getNamespaceMappings()
/*     */   {
/* 335 */     aMethodIsCalled();
/* 336 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */   {
/* 343 */     aMethodIsCalled();
/* 344 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String name, boolean isElement)
/*     */   {
/* 351 */     aMethodIsCalled();
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespaceURIFromPrefix(String prefix)
/*     */   {
/* 359 */     aMethodIsCalled();
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator arg0)
/*     */   {
/* 367 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 374 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/* 382 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String arg0)
/*     */     throws SAXException
/*     */   {
/* 389 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
/*     */     throws SAXException
/*     */   {
/* 401 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void endElement(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/* 409 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void characters(char[] arg0, int arg1, int arg2)
/*     */     throws SAXException
/*     */   {
/* 416 */     couldThrowSAXException(arg0, arg1, arg2);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
/*     */     throws SAXException
/*     */   {
/* 424 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/* 432 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/* 439 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void comment(String comment)
/*     */     throws SAXException
/*     */   {
/* 446 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void startDTD(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/* 454 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void endDTD()
/*     */     throws SAXException
/*     */   {
/* 461 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void startEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/* 468 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void endEntity(String arg0)
/*     */     throws SAXException
/*     */   {
/* 475 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void startCDATA()
/*     */     throws SAXException
/*     */   {
/* 482 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void endCDATA()
/*     */     throws SAXException
/*     */   {
/* 489 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void comment(char[] arg0, int arg1, int arg2)
/*     */     throws SAXException
/*     */   {
/* 496 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public String getDoctypePublic()
/*     */   {
/* 503 */     aMethodIsCalled();
/* 504 */     return null;
/*     */   }
/*     */ 
/*     */   public String getDoctypeSystem()
/*     */   {
/* 511 */     aMethodIsCalled();
/* 512 */     return null;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 519 */     aMethodIsCalled();
/* 520 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getIndent()
/*     */   {
/* 527 */     aMethodIsCalled();
/* 528 */     return false;
/*     */   }
/*     */ 
/*     */   public int getIndentAmount()
/*     */   {
/* 535 */     aMethodIsCalled();
/* 536 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getMediaType()
/*     */   {
/* 543 */     aMethodIsCalled();
/* 544 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getOmitXMLDeclaration()
/*     */   {
/* 551 */     aMethodIsCalled();
/* 552 */     return false;
/*     */   }
/*     */ 
/*     */   public String getStandalone()
/*     */   {
/* 559 */     aMethodIsCalled();
/* 560 */     return null;
/*     */   }
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 567 */     aMethodIsCalled();
/* 568 */     return null;
/*     */   }
/*     */ 
/*     */   public void setCdataSectionElements(Hashtable h)
/*     */     throws Exception
/*     */   {
/* 575 */     couldThrowException();
/*     */   }
/*     */ 
/*     */   public void setDoctype(String system, String pub)
/*     */   {
/* 582 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setDoctypePublic(String doctype)
/*     */   {
/* 589 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setDoctypeSystem(String doctype)
/*     */   {
/* 596 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 603 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setMediaType(String mediatype)
/*     */   {
/* 610 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setOmitXMLDeclaration(boolean b)
/*     */   {
/* 617 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setStandalone(String standalone)
/*     */   {
/* 624 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void elementDecl(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/* 631 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4)
/*     */     throws SAXException
/*     */   {
/* 644 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void internalEntityDecl(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/* 652 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void externalEntityDecl(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/* 660 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException arg0)
/*     */     throws SAXException
/*     */   {
/* 667 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException arg0)
/*     */     throws SAXException
/*     */   {
/* 674 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException arg0)
/*     */     throws SAXException
/*     */   {
/* 681 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public DOMSerializer asDOMSerializer()
/*     */     throws IOException
/*     */   {
/* 688 */     couldThrowIOException();
/* 689 */     return null;
/*     */   }
/*     */ 
/*     */   public void setNamespaceMappings(NamespaceMappings mappings)
/*     */   {
/* 696 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void setSourceLocator(SourceLocator locator)
/*     */   {
/* 704 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void addUniqueAttribute(String name, String value, int flags)
/*     */     throws SAXException
/*     */   {
/* 713 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void characters(Node node)
/*     */     throws SAXException
/*     */   {
/* 721 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void addXSLAttribute(String qName, String value, String uri)
/*     */   {
/* 729 */     aMethodIsCalled();
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String rawName, String type, String value)
/*     */     throws SAXException
/*     */   {
/* 737 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void notationDecl(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/* 744 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3)
/*     */     throws SAXException
/*     */   {
/* 756 */     couldThrowSAXException();
/*     */   }
/*     */ 
/*     */   public void setDTDEntityExpansion(boolean expand)
/*     */   {
/* 763 */     aMethodIsCalled();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.EmptySerializer
 * JD-Core Version:    0.6.2
 */