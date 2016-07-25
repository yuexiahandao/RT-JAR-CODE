/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ final class IncrementalSAXSource_Filter
/*     */   implements IncrementalSAXSource, ContentHandler, DTDHandler, LexicalHandler, ErrorHandler, Runnable
/*     */ {
/*  76 */   boolean DEBUG = false;
/*     */ 
/*  81 */   private CoroutineManager fCoroutineManager = null;
/*  82 */   private int fControllerCoroutineID = -1;
/*  83 */   private int fSourceCoroutineID = -1;
/*     */ 
/*  85 */   private ContentHandler clientContentHandler = null;
/*  86 */   private LexicalHandler clientLexicalHandler = null;
/*  87 */   private DTDHandler clientDTDHandler = null;
/*  88 */   private ErrorHandler clientErrorHandler = null;
/*     */   private int eventcounter;
/*  90 */   private int frequency = 5;
/*     */ 
/*  95 */   private boolean fNoMoreEvents = false;
/*     */ 
/*  98 */   private XMLReader fXMLReader = null;
/*  99 */   private InputSource fXMLReaderInputSource = null;
/*     */ 
/*     */   public IncrementalSAXSource_Filter()
/*     */   {
/* 106 */     init(new CoroutineManager(), -1, -1);
/*     */   }
/*     */ 
/*     */   public IncrementalSAXSource_Filter(CoroutineManager co, int controllerCoroutineID)
/*     */   {
/* 114 */     init(co, controllerCoroutineID, -1);
/*     */   }
/*     */ 
/*     */   public static IncrementalSAXSource createIncrementalSAXSource(CoroutineManager co, int controllerCoroutineID)
/*     */   {
/* 121 */     return new IncrementalSAXSource_Filter(co, controllerCoroutineID);
/*     */   }
/*     */ 
/*     */   public void init(CoroutineManager co, int controllerCoroutineID, int sourceCoroutineID)
/*     */   {
/* 131 */     if (co == null)
/* 132 */       co = new CoroutineManager();
/* 133 */     this.fCoroutineManager = co;
/* 134 */     this.fControllerCoroutineID = co.co_joinCoroutineSet(controllerCoroutineID);
/* 135 */     this.fSourceCoroutineID = co.co_joinCoroutineSet(sourceCoroutineID);
/* 136 */     if ((this.fControllerCoroutineID == -1) || (this.fSourceCoroutineID == -1)) {
/* 137 */       throw new RuntimeException(XMLMessages.createXMLMessage("ER_COJOINROUTINESET_FAILED", null));
/*     */     }
/* 139 */     this.fNoMoreEvents = false;
/* 140 */     this.eventcounter = this.frequency;
/*     */   }
/*     */ 
/*     */   public void setXMLReader(XMLReader eventsource)
/*     */   {
/* 150 */     this.fXMLReader = eventsource;
/* 151 */     eventsource.setContentHandler(this);
/* 152 */     eventsource.setDTDHandler(this);
/* 153 */     eventsource.setErrorHandler(this);
/*     */     try
/*     */     {
/* 158 */       eventsource.setProperty("http://xml.org/sax/properties/lexical-handler", this);
/*     */     }
/*     */     catch (SAXNotRecognizedException e)
/*     */     {
/*     */     }
/*     */     catch (SAXNotSupportedException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */   {
/* 178 */     this.clientContentHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */   {
/* 183 */     this.clientDTDHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setLexicalHandler(LexicalHandler handler)
/*     */   {
/* 191 */     this.clientLexicalHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setErrHandler(ErrorHandler handler)
/*     */   {
/* 197 */     this.clientErrorHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setReturnFrequency(int events)
/*     */   {
/* 204 */     if (events < 1) events = 1;
/* 205 */     this.frequency = (this.eventcounter = events);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 230 */     if (--this.eventcounter <= 0)
/*     */     {
/* 232 */       co_yield(true);
/* 233 */       this.eventcounter = this.frequency;
/*     */     }
/* 235 */     if (this.clientContentHandler != null)
/* 236 */       this.clientContentHandler.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 242 */     if (this.clientContentHandler != null) {
/* 243 */       this.clientContentHandler.endDocument();
/*     */     }
/* 245 */     this.eventcounter = 0;
/* 246 */     co_yield(false);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 252 */     if (--this.eventcounter <= 0)
/*     */     {
/* 254 */       co_yield(true);
/* 255 */       this.eventcounter = this.frequency;
/*     */     }
/* 257 */     if (this.clientContentHandler != null)
/* 258 */       this.clientContentHandler.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException
/*     */   {
/* 263 */     if (--this.eventcounter <= 0)
/*     */     {
/* 265 */       co_yield(true);
/* 266 */       this.eventcounter = this.frequency;
/*     */     }
/* 268 */     if (this.clientContentHandler != null)
/* 269 */       this.clientContentHandler.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
/*     */   {
/* 274 */     if (--this.eventcounter <= 0)
/*     */     {
/* 276 */       co_yield(true);
/* 277 */       this.eventcounter = this.frequency;
/*     */     }
/* 279 */     if (this.clientContentHandler != null)
/* 280 */       this.clientContentHandler.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException
/*     */   {
/* 285 */     if (--this.eventcounter <= 0)
/*     */     {
/* 287 */       co_yield(true);
/* 288 */       this.eventcounter = this.frequency;
/*     */     }
/* 290 */     if (this.clientContentHandler != null)
/* 291 */       this.clientContentHandler.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator) {
/* 295 */     if (--this.eventcounter <= 0)
/*     */     {
/* 299 */       this.eventcounter = this.frequency;
/*     */     }
/* 301 */     if (this.clientContentHandler != null)
/* 302 */       this.clientContentHandler.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name) throws SAXException
/*     */   {
/* 307 */     if (--this.eventcounter <= 0)
/*     */     {
/* 309 */       co_yield(true);
/* 310 */       this.eventcounter = this.frequency;
/*     */     }
/* 312 */     if (this.clientContentHandler != null)
/* 313 */       this.clientContentHandler.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void startDocument() throws SAXException
/*     */   {
/* 318 */     co_entry_pause();
/*     */ 
/* 321 */     if (--this.eventcounter <= 0)
/*     */     {
/* 323 */       co_yield(true);
/* 324 */       this.eventcounter = this.frequency;
/*     */     }
/* 326 */     if (this.clientContentHandler != null)
/* 327 */       this.clientContentHandler.startDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 333 */     if (--this.eventcounter <= 0)
/*     */     {
/* 335 */       co_yield(true);
/* 336 */       this.eventcounter = this.frequency;
/*     */     }
/* 338 */     if (this.clientContentHandler != null)
/* 339 */       this.clientContentHandler.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException
/*     */   {
/* 344 */     if (--this.eventcounter <= 0)
/*     */     {
/* 346 */       co_yield(true);
/* 347 */       this.eventcounter = this.frequency;
/*     */     }
/* 349 */     if (this.clientContentHandler != null)
/* 350 */       this.clientContentHandler.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 366 */     if (null != this.clientLexicalHandler)
/* 367 */       this.clientLexicalHandler.comment(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void endCDATA() throws SAXException
/*     */   {
/* 372 */     if (null != this.clientLexicalHandler)
/* 373 */       this.clientLexicalHandler.endCDATA();
/*     */   }
/*     */ 
/*     */   public void endDTD() throws SAXException
/*     */   {
/* 378 */     if (null != this.clientLexicalHandler)
/* 379 */       this.clientLexicalHandler.endDTD();
/*     */   }
/*     */ 
/*     */   public void endEntity(String name) throws SAXException
/*     */   {
/* 384 */     if (null != this.clientLexicalHandler)
/* 385 */       this.clientLexicalHandler.endEntity(name);
/*     */   }
/*     */ 
/*     */   public void startCDATA() throws SAXException
/*     */   {
/* 390 */     if (null != this.clientLexicalHandler)
/* 391 */       this.clientLexicalHandler.startCDATA();
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 397 */     if (null != this.clientLexicalHandler)
/* 398 */       this.clientLexicalHandler.startDTD(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void startEntity(String name) throws SAXException
/*     */   {
/* 403 */     if (null != this.clientLexicalHandler)
/* 404 */       this.clientLexicalHandler.startEntity(name);
/*     */   }
/*     */ 
/*     */   public void notationDecl(String a, String b, String c)
/*     */     throws SAXException
/*     */   {
/* 412 */     if (null != this.clientDTDHandler)
/* 413 */       this.clientDTDHandler.notationDecl(a, b, c);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String a, String b, String c, String d) throws SAXException {
/* 417 */     if (null != this.clientDTDHandler)
/* 418 */       this.clientDTDHandler.unparsedEntityDecl(a, b, c, d);
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 438 */     if (null != this.clientErrorHandler)
/* 439 */       this.clientErrorHandler.error(exception);
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 446 */     if (null != this.clientErrorHandler) {
/* 447 */       this.clientErrorHandler.error(exception);
/*     */     }
/* 449 */     this.eventcounter = 0;
/* 450 */     co_yield(false);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 456 */     if (null != this.clientErrorHandler)
/* 457 */       this.clientErrorHandler.error(exception);
/*     */   }
/*     */ 
/*     */   public int getSourceCoroutineID()
/*     */   {
/* 466 */     return this.fSourceCoroutineID;
/*     */   }
/*     */   public int getControllerCoroutineID() {
/* 469 */     return this.fControllerCoroutineID;
/*     */   }
/*     */ 
/*     */   public CoroutineManager getCoroutineManager()
/*     */   {
/* 479 */     return this.fCoroutineManager;
/*     */   }
/*     */ 
/*     */   protected void count_and_yield(boolean moreExpected)
/*     */     throws SAXException
/*     */   {
/* 495 */     if (!moreExpected) this.eventcounter = 0;
/*     */ 
/* 497 */     if (--this.eventcounter <= 0)
/*     */     {
/* 499 */       co_yield(true);
/* 500 */       this.eventcounter = this.frequency;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void co_entry_pause()
/*     */     throws SAXException
/*     */   {
/* 513 */     if (this.fCoroutineManager == null)
/*     */     {
/* 516 */       init(null, -1, -1);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 521 */       Object arg = this.fCoroutineManager.co_entry_pause(this.fSourceCoroutineID);
/* 522 */       if (arg == Boolean.FALSE) {
/* 523 */         co_yield(false);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (NoSuchMethodException e)
/*     */     {
/* 529 */       if (this.DEBUG) e.printStackTrace();
/* 530 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void co_yield(boolean moreRemains)
/*     */     throws SAXException
/*     */   {
/* 559 */     if (this.fNoMoreEvents) {
/* 560 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 564 */       Object arg = Boolean.FALSE;
/* 565 */       if (moreRemains)
/*     */       {
/* 568 */         arg = this.fCoroutineManager.co_resume(Boolean.TRUE, this.fSourceCoroutineID, this.fControllerCoroutineID);
/*     */       }
/*     */ 
/* 574 */       if (arg == Boolean.FALSE)
/*     */       {
/* 576 */         this.fNoMoreEvents = true;
/*     */ 
/* 578 */         if (this.fXMLReader != null) {
/* 579 */           throw new StopException();
/*     */         }
/*     */ 
/* 582 */         this.fCoroutineManager.co_exit_to(Boolean.FALSE, this.fSourceCoroutineID, this.fControllerCoroutineID);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (NoSuchMethodException e)
/*     */     {
/* 590 */       this.fNoMoreEvents = true;
/* 591 */       this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
/* 592 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startParse(InputSource source)
/*     */     throws SAXException
/*     */   {
/* 611 */     if (this.fNoMoreEvents)
/* 612 */       throw new SAXException(XMLMessages.createXMLMessage("ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", null));
/* 613 */     if (this.fXMLReader == null) {
/* 614 */       throw new SAXException(XMLMessages.createXMLMessage("ER_XMLRDR_NOT_BEFORE_STARTPARSE", null));
/*     */     }
/* 616 */     this.fXMLReaderInputSource = source;
/*     */ 
/* 620 */     ThreadControllerWrapper.runThread(this, -1);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 628 */     if (this.fXMLReader == null) return;
/*     */ 
/* 630 */     if (this.DEBUG) System.out.println("IncrementalSAXSource_Filter parse thread launched");
/*     */ 
/* 633 */     Object arg = Boolean.FALSE;
/*     */     try
/*     */     {
/* 641 */       this.fXMLReader.parse(this.fXMLReaderInputSource);
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 645 */       arg = ex;
/*     */     }
/*     */     catch (StopException ex)
/*     */     {
/* 650 */       if (this.DEBUG) System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
/*     */     }
/*     */     catch (SAXException ex)
/*     */     {
/* 654 */       Exception inner = ex.getException();
/* 655 */       if ((inner instanceof StopException))
/*     */       {
/* 657 */         if (this.DEBUG) System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 662 */         if (this.DEBUG)
/*     */         {
/* 664 */           System.out.println("Active IncrementalSAXSource_Filter UNEXPECTED SAX exception: " + inner);
/* 665 */           inner.printStackTrace();
/*     */         }
/* 667 */         arg = ex;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 672 */     this.fXMLReader = null;
/*     */     try
/*     */     {
/* 677 */       this.fNoMoreEvents = true;
/* 678 */       this.fCoroutineManager.co_exit_to(arg, this.fSourceCoroutineID, this.fControllerCoroutineID);
/*     */     }
/*     */     catch (NoSuchMethodException e)
/*     */     {
/* 685 */       e.printStackTrace(System.err);
/* 686 */       this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object deliverMoreNodes(boolean parsemore)
/*     */   {
/* 717 */     if (this.fNoMoreEvents) {
/* 718 */       return Boolean.FALSE;
/*     */     }
/*     */     try
/*     */     {
/* 722 */       Object result = this.fCoroutineManager.co_resume(parsemore ? Boolean.TRUE : Boolean.FALSE, this.fControllerCoroutineID, this.fSourceCoroutineID);
/*     */ 
/* 725 */       if (result == Boolean.FALSE) {
/* 726 */         this.fCoroutineManager.co_exit(this.fControllerCoroutineID);
/*     */       }
/* 728 */       return result;
/*     */     }
/*     */     catch (NoSuchMethodException e)
/*     */     {
/* 736 */       return e;
/*     */     }
/*     */   }
/*     */ 
/*     */   class StopException extends RuntimeException
/*     */   {
/*     */     static final long serialVersionUID = -1129245796185754956L;
/*     */ 
/*     */     StopException()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter
 * JD-Core Version:    0.6.2
 */