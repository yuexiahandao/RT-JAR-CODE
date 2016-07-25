/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
/*     */ import java.io.IOException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
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
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class StAXStream2SAX
/*     */   implements XMLReader, Locator
/*     */ {
/*     */   private final XMLStreamReader staxStreamReader;
/*  80 */   private ContentHandler _sax = null;
/*  81 */   private LexicalHandler _lex = null;
/*  82 */   private SAXImpl _saxImpl = null;
/*     */ 
/*     */   public StAXStream2SAX(XMLStreamReader staxSrc)
/*     */   {
/*  86 */     this.staxStreamReader = staxSrc;
/*     */   }
/*     */ 
/*     */   public ContentHandler getContentHandler() {
/*  90 */     return this._sax;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*  96 */     this._sax = handler;
/*  97 */     if ((handler instanceof LexicalHandler)) {
/*  98 */       this._lex = ((LexicalHandler)handler);
/*     */     }
/*     */ 
/* 101 */     if ((handler instanceof SAXImpl))
/* 102 */       this._saxImpl = ((SAXImpl)handler);
/*     */   }
/*     */ 
/*     */   public void parse(InputSource unused) throws IOException, SAXException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       bridge();
/*     */     } catch (XMLStreamException e) {
/* 111 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parse()
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 118 */     bridge();
/*     */   }
/*     */ 
/*     */   public void parse(String sysId)
/*     */     throws IOException, SAXException
/*     */   {
/* 127 */     throw new IOException("This method is not yet implemented.");
/*     */   }
/*     */ 
/*     */   public void bridge()
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 135 */       int depth = 0;
/*     */ 
/* 138 */       int event = this.staxStreamReader.getEventType();
/* 139 */       if (event == 7) {
/* 140 */         event = this.staxStreamReader.next();
/*     */       }
/*     */ 
/* 144 */       if (event != 1) {
/* 145 */         event = this.staxStreamReader.nextTag();
/*     */ 
/* 147 */         if (event != 1) {
/* 148 */           throw new IllegalStateException("The current event is not START_ELEMENT\n but" + event);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 153 */       handleStartDocument();
/*     */       do
/*     */       {
/* 159 */         switch (event) {
/*     */         case 1:
/* 161 */           depth++;
/* 162 */           handleStartElement();
/* 163 */           break;
/*     */         case 2:
/* 165 */           handleEndElement();
/* 166 */           depth--;
/* 167 */           break;
/*     */         case 4:
/* 169 */           handleCharacters();
/* 170 */           break;
/*     */         case 9:
/* 172 */           handleEntityReference();
/* 173 */           break;
/*     */         case 3:
/* 175 */           handlePI();
/* 176 */           break;
/*     */         case 5:
/* 178 */           handleComment();
/* 179 */           break;
/*     */         case 11:
/* 181 */           handleDTD();
/* 182 */           break;
/*     */         case 10:
/* 184 */           handleAttribute();
/* 185 */           break;
/*     */         case 13:
/* 187 */           handleNamespace();
/* 188 */           break;
/*     */         case 12:
/* 190 */           handleCDATA();
/* 191 */           break;
/*     */         case 15:
/* 193 */           handleEntityDecl();
/* 194 */           break;
/*     */         case 14:
/* 196 */           handleNotationDecl();
/* 197 */           break;
/*     */         case 6:
/* 199 */           handleSpace();
/* 200 */           break;
/*     */         case 7:
/*     */         case 8:
/*     */         default:
/* 202 */           throw new InternalError("processing event: " + event);
/*     */         }
/*     */ 
/* 205 */         event = this.staxStreamReader.next();
/* 206 */       }while (depth != 0);
/*     */ 
/* 208 */       handleEndDocument();
/*     */     } catch (SAXException e) {
/* 210 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleEndDocument() throws SAXException {
/* 215 */     this._sax.endDocument();
/*     */   }
/*     */ 
/*     */   private void handleStartDocument() throws SAXException {
/* 219 */     this._sax.setDocumentLocator(new Locator2() {
/*     */       public int getColumnNumber() {
/* 221 */         return StAXStream2SAX.this.staxStreamReader.getLocation().getColumnNumber();
/*     */       }
/*     */       public int getLineNumber() {
/* 224 */         return StAXStream2SAX.this.staxStreamReader.getLocation().getLineNumber();
/*     */       }
/*     */       public String getPublicId() {
/* 227 */         return StAXStream2SAX.this.staxStreamReader.getLocation().getPublicId();
/*     */       }
/*     */       public String getSystemId() {
/* 230 */         return StAXStream2SAX.this.staxStreamReader.getLocation().getSystemId();
/*     */       }
/*     */       public String getXMLVersion() {
/* 233 */         return StAXStream2SAX.this.staxStreamReader.getVersion();
/*     */       }
/*     */       public String getEncoding() {
/* 236 */         return StAXStream2SAX.this.staxStreamReader.getEncoding();
/*     */       }
/*     */     });
/* 239 */     this._sax.startDocument();
/*     */   }
/*     */ 
/*     */   private void handlePI() throws XMLStreamException {
/*     */     try {
/* 244 */       this._sax.processingInstruction(this.staxStreamReader.getPITarget(), this.staxStreamReader.getPIData());
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 248 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleCharacters()
/*     */     throws XMLStreamException
/*     */   {
/* 256 */     int textLength = this.staxStreamReader.getTextLength();
/* 257 */     char[] chars = new char[textLength];
/*     */ 
/* 259 */     this.staxStreamReader.getTextCharacters(0, chars, 0, textLength);
/*     */     try
/*     */     {
/* 262 */       this._sax.characters(chars, 0, chars.length);
/*     */     } catch (SAXException e) {
/* 264 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleEndElement()
/*     */     throws XMLStreamException
/*     */   {
/* 282 */     QName qName = this.staxStreamReader.getName();
/*     */     try
/*     */     {
/* 286 */       String qname = "";
/* 287 */       if ((qName.getPrefix() != null) && (qName.getPrefix().trim().length() != 0)) {
/* 288 */         qname = qName.getPrefix() + ":";
/*     */       }
/* 290 */       qname = qname + qName.getLocalPart();
/*     */ 
/* 293 */       this._sax.endElement(qName.getNamespaceURI(), qName.getLocalPart(), qname);
/*     */ 
/* 299 */       int nsCount = this.staxStreamReader.getNamespaceCount();
/* 300 */       for (int i = nsCount - 1; i >= 0; i--) {
/* 301 */         String prefix = this.staxStreamReader.getNamespacePrefix(i);
/* 302 */         if (prefix == null) {
/* 303 */           prefix = "";
/*     */         }
/* 305 */         this._sax.endPrefixMapping(prefix);
/*     */       }
/*     */     } catch (SAXException e) {
/* 308 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleStartElement() throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 316 */       int nsCount = this.staxStreamReader.getNamespaceCount();
/* 317 */       for (int i = 0; i < nsCount; i++) {
/* 318 */         String prefix = this.staxStreamReader.getNamespacePrefix(i);
/* 319 */         if (prefix == null) {
/* 320 */           prefix = "";
/*     */         }
/* 322 */         this._sax.startPrefixMapping(prefix, this.staxStreamReader.getNamespaceURI(i));
/*     */       }
/*     */ 
/* 328 */       QName qName = this.staxStreamReader.getName();
/* 329 */       String prefix = qName.getPrefix();
/*     */       String rawname;
/*     */       String rawname;
/* 331 */       if ((prefix == null) || (prefix.length() == 0))
/* 332 */         rawname = qName.getLocalPart();
/*     */       else
/* 334 */         rawname = prefix + ':' + qName.getLocalPart();
/* 335 */       Attributes attrs = getAttributes();
/* 336 */       this._sax.startElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname, attrs);
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 342 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Attributes getAttributes()
/*     */   {
/* 353 */     AttributesImpl attrs = new AttributesImpl();
/*     */ 
/* 355 */     int eventType = this.staxStreamReader.getEventType();
/* 356 */     if ((eventType != 10) && (eventType != 1))
/*     */     {
/* 358 */       throw new InternalError("getAttributes() attempting to process: " + eventType);
/*     */     }
/*     */ 
/* 367 */     for (int i = 0; i < this.staxStreamReader.getAttributeCount(); i++) {
/* 368 */       String uri = this.staxStreamReader.getAttributeNamespace(i);
/* 369 */       if (uri == null) uri = "";
/* 370 */       String localName = this.staxStreamReader.getAttributeLocalName(i);
/* 371 */       String prefix = this.staxStreamReader.getAttributePrefix(i);
/*     */       String qName;
/*     */       String qName;
/* 373 */       if ((prefix == null) || (prefix.length() == 0))
/* 374 */         qName = localName;
/*     */       else
/* 376 */         qName = prefix + ':' + localName;
/* 377 */       String type = this.staxStreamReader.getAttributeType(i);
/* 378 */       String value = this.staxStreamReader.getAttributeValue(i);
/*     */ 
/* 380 */       attrs.addAttribute(uri, localName, qName, type, value);
/*     */     }
/*     */ 
/* 383 */     return attrs;
/*     */   }
/*     */ 
/*     */   private void handleNamespace()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleAttribute()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleDTD()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleComment()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleEntityReference()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleSpace()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleNotationDecl()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleEntityDecl()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleCDATA()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DTDHandler getDTDHandler()
/*     */   {
/* 437 */     return null;
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler()
/*     */   {
/* 445 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 455 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver()
/*     */   {
/* 488 */     return null;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 515 */     return null;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 523 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 531 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 539 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 547 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.StAXStream2SAX
 * JD-Core Version:    0.6.2
 */