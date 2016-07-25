/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
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
/*     */ public class StAXEvent2SAX
/*     */   implements XMLReader, Locator
/*     */ {
/*     */   private final XMLEventReader staxEventReader;
/*  75 */   private ContentHandler _sax = null;
/*  76 */   private LexicalHandler _lex = null;
/*  77 */   private SAXImpl _saxImpl = null;
/*     */ 
/*  79 */   private String version = null;
/*  80 */   private String encoding = null;
/*     */ 
/*     */   public StAXEvent2SAX(XMLEventReader staxCore)
/*     */   {
/*  84 */     this.staxEventReader = staxCore;
/*     */   }
/*     */ 
/*     */   public ContentHandler getContentHandler() {
/*  88 */     return this._sax;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */     throws NullPointerException
/*     */   {
/*  94 */     this._sax = handler;
/*  95 */     if ((handler instanceof LexicalHandler)) {
/*  96 */       this._lex = ((LexicalHandler)handler);
/*     */     }
/*     */ 
/*  99 */     if ((handler instanceof SAXImpl))
/* 100 */       this._saxImpl = ((SAXImpl)handler);
/*     */   }
/*     */ 
/*     */   public void parse(InputSource unused) throws IOException, SAXException
/*     */   {
/*     */     try
/*     */     {
/* 107 */       bridge();
/*     */     } catch (XMLStreamException e) {
/* 109 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parse()
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 116 */     bridge();
/*     */   }
/*     */ 
/*     */   private void bridge()
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 144 */       int depth = 0;
/* 145 */       boolean startedAtDocument = false;
/*     */ 
/* 147 */       XMLEvent event = this.staxEventReader.peek();
/*     */ 
/* 149 */       if ((!event.isStartDocument()) && (!event.isStartElement())) {
/* 150 */         throw new IllegalStateException();
/*     */       }
/*     */ 
/* 153 */       if (event.getEventType() == 7) {
/* 154 */         startedAtDocument = true;
/* 155 */         this.version = ((StartDocument)event).getVersion();
/* 156 */         if (((StartDocument)event).encodingSet())
/* 157 */           this.encoding = ((StartDocument)event).getCharacterEncodingScheme();
/* 158 */         event = this.staxEventReader.nextEvent();
/* 159 */         event = this.staxEventReader.nextEvent();
/*     */       }
/*     */ 
/* 162 */       handleStartDocument(event);
/*     */ 
/* 165 */       while (event.getEventType() != 1) {
/* 166 */         switch (event.getEventType()) {
/*     */         case 4:
/* 168 */           handleCharacters(event.asCharacters());
/* 169 */           break;
/*     */         case 3:
/* 171 */           handlePI((ProcessingInstruction)event);
/* 172 */           break;
/*     */         case 5:
/* 174 */           handleComment();
/* 175 */           break;
/*     */         case 11:
/* 177 */           handleDTD();
/* 178 */           break;
/*     */         case 6:
/* 180 */           handleSpace();
/* 181 */           break;
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/*     */         default:
/* 183 */           throw new InternalError("processing prolog event: " + event);
/*     */         }
/* 185 */         event = this.staxEventReader.nextEvent();
/*     */       }
/*     */ 
/*     */       do
/*     */       {
/* 193 */         switch (event.getEventType()) {
/*     */         case 1:
/* 195 */           depth++;
/* 196 */           handleStartElement(event.asStartElement());
/* 197 */           break;
/*     */         case 2:
/* 199 */           handleEndElement(event.asEndElement());
/* 200 */           depth--;
/* 201 */           break;
/*     */         case 4:
/* 203 */           handleCharacters(event.asCharacters());
/* 204 */           break;
/*     */         case 9:
/* 206 */           handleEntityReference();
/* 207 */           break;
/*     */         case 3:
/* 209 */           handlePI((ProcessingInstruction)event);
/* 210 */           break;
/*     */         case 5:
/* 212 */           handleComment();
/* 213 */           break;
/*     */         case 11:
/* 215 */           handleDTD();
/* 216 */           break;
/*     */         case 10:
/* 218 */           handleAttribute();
/* 219 */           break;
/*     */         case 13:
/* 221 */           handleNamespace();
/* 222 */           break;
/*     */         case 12:
/* 224 */           handleCDATA();
/* 225 */           break;
/*     */         case 15:
/* 227 */           handleEntityDecl();
/* 228 */           break;
/*     */         case 14:
/* 230 */           handleNotationDecl();
/* 231 */           break;
/*     */         case 6:
/* 233 */           handleSpace();
/* 234 */           break;
/*     */         case 7:
/*     */         case 8:
/*     */         default:
/* 236 */           throw new InternalError("processing event: " + event);
/*     */         }
/*     */ 
/* 239 */         event = this.staxEventReader.nextEvent();
/* 240 */       }while (depth != 0);
/*     */ 
/* 242 */       if (startedAtDocument)
/*     */       {
/* 244 */         while (event.getEventType() != 8) {
/* 245 */           switch (event.getEventType()) {
/*     */           case 4:
/* 247 */             handleCharacters(event.asCharacters());
/* 248 */             break;
/*     */           case 3:
/* 250 */             handlePI((ProcessingInstruction)event);
/* 251 */             break;
/*     */           case 5:
/* 253 */             handleComment();
/* 254 */             break;
/*     */           case 6:
/* 256 */             handleSpace();
/* 257 */             break;
/*     */           default:
/* 259 */             throw new InternalError("processing misc event after document element: " + event);
/*     */           }
/* 261 */           event = this.staxEventReader.nextEvent();
/*     */         }
/*     */       }
/*     */ 
/* 265 */       handleEndDocument();
/*     */     } catch (SAXException e) {
/* 267 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleEndDocument() throws SAXException
/*     */   {
/* 273 */     this._sax.endDocument();
/*     */   }
/*     */ 
/*     */   private void handleStartDocument(final XMLEvent event) throws SAXException {
/* 277 */     this._sax.setDocumentLocator(new Locator2() {
/*     */       public int getColumnNumber() {
/* 279 */         return event.getLocation().getColumnNumber();
/*     */       }
/*     */       public int getLineNumber() {
/* 282 */         return event.getLocation().getLineNumber();
/*     */       }
/*     */       public String getPublicId() {
/* 285 */         return event.getLocation().getPublicId();
/*     */       }
/*     */       public String getSystemId() {
/* 288 */         return event.getLocation().getSystemId();
/*     */       }
/*     */       public String getXMLVersion() {
/* 291 */         return StAXEvent2SAX.this.version;
/*     */       }
/*     */       public String getEncoding() {
/* 294 */         return StAXEvent2SAX.this.encoding;
/*     */       }
/*     */     });
/* 298 */     this._sax.startDocument();
/*     */   }
/*     */ 
/*     */   private void handlePI(ProcessingInstruction event) throws XMLStreamException
/*     */   {
/*     */     try {
/* 304 */       this._sax.processingInstruction(event.getTarget(), event.getData());
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 308 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleCharacters(Characters event) throws XMLStreamException {
/*     */     try {
/* 314 */       this._sax.characters(event.getData().toCharArray(), 0, event.getData().length());
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 319 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleEndElement(EndElement event) throws XMLStreamException {
/* 324 */     QName qName = event.getName();
/*     */ 
/* 327 */     String qname = "";
/* 328 */     if ((qName.getPrefix() != null) && (qName.getPrefix().trim().length() != 0)) {
/* 329 */       qname = qName.getPrefix() + ":";
/*     */     }
/* 331 */     qname = qname + qName.getLocalPart();
/*     */     try
/*     */     {
/* 335 */       this._sax.endElement(qName.getNamespaceURI(), qName.getLocalPart(), qname);
/*     */ 
/* 341 */       for (i = event.getNamespaces(); i.hasNext(); ) {
/* 342 */         String prefix = (String)i.next();
/* 343 */         if (prefix == null) {
/* 344 */           prefix = "";
/*     */         }
/* 346 */         this._sax.endPrefixMapping(prefix);
/*     */       }
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */       Iterator i;
/* 349 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleStartElement(StartElement event) throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 357 */       for (Iterator i = event.getNamespaces(); i.hasNext(); ) {
/* 358 */         String prefix = ((Namespace)i.next()).getPrefix();
/* 359 */         if (prefix == null) {
/* 360 */           prefix = "";
/*     */         }
/* 362 */         this._sax.startPrefixMapping(prefix, event.getNamespaceURI(prefix));
/*     */       }
/*     */ 
/* 368 */       QName qName = event.getName();
/* 369 */       String prefix = qName.getPrefix();
/*     */       String rawname;
/*     */       String rawname;
/* 371 */       if ((prefix == null) || (prefix.length() == 0))
/* 372 */         rawname = qName.getLocalPart();
/*     */       else {
/* 374 */         rawname = prefix + ':' + qName.getLocalPart();
/*     */       }
/*     */ 
/* 377 */       Attributes saxAttrs = getAttributes(event);
/* 378 */       this._sax.startElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname, saxAttrs);
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 384 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Attributes getAttributes(StartElement event)
/*     */   {
/* 394 */     AttributesImpl attrs = new AttributesImpl();
/*     */ 
/* 396 */     if (!event.isStartElement()) {
/* 397 */       throw new InternalError("getAttributes() attempting to process: " + event);
/*     */     }
/*     */ 
/* 406 */     for (Iterator i = event.getAttributes(); i.hasNext(); ) {
/* 407 */       Attribute staxAttr = (Attribute)i.next();
/*     */ 
/* 409 */       String uri = staxAttr.getName().getNamespaceURI();
/* 410 */       if (uri == null) {
/* 411 */         uri = "";
/*     */       }
/* 413 */       String localName = staxAttr.getName().getLocalPart();
/* 414 */       String prefix = staxAttr.getName().getPrefix();
/*     */       String qName;
/*     */       String qName;
/* 416 */       if ((prefix == null) || (prefix.length() == 0))
/* 417 */         qName = localName;
/*     */       else {
/* 419 */         qName = prefix + ':' + localName;
/*     */       }
/* 421 */       String type = staxAttr.getDTDType();
/* 422 */       String value = staxAttr.getValue();
/*     */ 
/* 424 */       attrs.addAttribute(uri, localName, qName, type, value);
/*     */     }
/*     */ 
/* 427 */     return attrs;
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
/* 481 */     return null;
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler()
/*     */   {
/* 489 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 499 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void parse(String sysId)
/*     */     throws IOException, SAXException
/*     */   {
/* 516 */     throw new IOException("This method is not yet implemented.");
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
/* 540 */     return null;
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
/* 567 */     return null;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 575 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 583 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 591 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 599 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.StAXEvent2SAX
 * JD-Core Version:    0.6.2
 */