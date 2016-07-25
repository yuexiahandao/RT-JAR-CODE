/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public abstract class ToSAXHandler extends SerializerBase
/*     */ {
/*     */   protected ContentHandler m_saxHandler;
/*     */   protected LexicalHandler m_lexHandler;
/*  82 */   private boolean m_shouldGenerateNSAttribute = true;
/*     */ 
/*  88 */   protected TransformStateSetter m_state = null;
/*     */ 
/*     */   public ToSAXHandler()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ToSAXHandler(ContentHandler hdlr, LexicalHandler lex, String encoding)
/*     */   {
/*  53 */     setContentHandler(hdlr);
/*  54 */     setLexHandler(lex);
/*  55 */     setEncoding(encoding);
/*     */   }
/*     */ 
/*     */   public ToSAXHandler(ContentHandler handler, String encoding) {
/*  59 */     setContentHandler(handler);
/*  60 */     setEncoding(encoding);
/*     */   }
/*     */ 
/*     */   protected void startDocumentInternal()
/*     */     throws SAXException
/*     */   {
/*  95 */     if (this.m_needToCallStartDocument)
/*     */     {
/*  97 */       super.startDocumentInternal();
/*     */ 
/*  99 */       this.m_saxHandler.startDocument();
/* 100 */       this.m_needToCallStartDocument = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startDTD(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(String characters)
/*     */     throws SAXException
/*     */   {
/* 124 */     int len = characters.length();
/* 125 */     if (len > this.m_charsBuff.length)
/*     */     {
/* 127 */       this.m_charsBuff = new char[len * 2 + 1];
/*     */     }
/* 129 */     characters.getChars(0, len, this.m_charsBuff, 0);
/* 130 */     characters(this.m_charsBuff, 0, len);
/*     */   }
/*     */ 
/*     */   public void comment(String comment)
/*     */     throws SAXException
/*     */   {
/* 140 */     flushPending();
/*     */ 
/* 143 */     if (this.m_lexHandler != null)
/*     */     {
/* 145 */       int len = comment.length();
/* 146 */       if (len > this.m_charsBuff.length)
/*     */       {
/* 148 */         this.m_charsBuff = new char[len * 2 + 1];
/*     */       }
/* 150 */       comment.getChars(0, len, this.m_charsBuff, 0);
/* 151 */       this.m_lexHandler.comment(this.m_charsBuff, 0, len);
/*     */ 
/* 153 */       if (this.m_tracer != null)
/* 154 */         super.fireCommentEvent(this.m_charsBuff, 0, len);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void closeStartTag()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void closeCDATA()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
/*     */     throws SAXException
/*     */   {
/* 201 */     if (this.m_state != null) {
/* 202 */       this.m_state.resetState(getTransformer());
/*     */     }
/*     */ 
/* 206 */     if (this.m_tracer != null)
/* 207 */       super.fireStartElem(arg2);
/*     */   }
/*     */ 
/*     */   public void setLexHandler(LexicalHandler _lexHandler)
/*     */   {
/* 216 */     this.m_lexHandler = _lexHandler;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler _saxHandler)
/*     */   {
/* 225 */     this.m_saxHandler = _saxHandler;
/* 226 */     if ((this.m_lexHandler == null) && ((_saxHandler instanceof LexicalHandler)))
/*     */     {
/* 230 */       this.m_lexHandler = ((LexicalHandler)_saxHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCdataSectionElements(Vector URI_and_localNames)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setShouldOutputNSAttr(boolean doOutputNSAttr)
/*     */   {
/* 252 */     this.m_shouldGenerateNSAttribute = doOutputNSAttr;
/*     */   }
/*     */ 
/*     */   boolean getShouldOutputNSAttr()
/*     */   {
/* 263 */     return this.m_shouldGenerateNSAttribute;
/*     */   }
/*     */ 
/*     */   public void flushPending()
/*     */     throws SAXException
/*     */   {
/* 273 */     if (this.m_needToCallStartDocument)
/*     */     {
/* 275 */       startDocumentInternal();
/* 276 */       this.m_needToCallStartDocument = false;
/*     */     }
/*     */ 
/* 279 */     if (this.m_elemContext.m_startTagOpen)
/*     */     {
/* 281 */       closeStartTag();
/* 282 */       this.m_elemContext.m_startTagOpen = false;
/*     */     }
/*     */ 
/* 285 */     if (this.m_cdataTagOpen)
/*     */     {
/* 287 */       closeCDATA();
/* 288 */       this.m_cdataTagOpen = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setTransformState(TransformStateSetter ts)
/*     */   {
/* 302 */     this.m_state = ts;
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 318 */     if (this.m_state != null) {
/* 319 */       this.m_state.resetState(getTransformer());
/*     */     }
/*     */ 
/* 323 */     if (this.m_tracer != null)
/* 324 */       super.fireStartElem(qName);
/*     */   }
/*     */ 
/*     */   public void startElement(String qName)
/*     */     throws SAXException
/*     */   {
/* 335 */     if (this.m_state != null) {
/* 336 */       this.m_state.resetState(getTransformer());
/*     */     }
/*     */ 
/* 339 */     if (this.m_tracer != null)
/* 340 */       super.fireStartElem(qName);
/*     */   }
/*     */ 
/*     */   public void characters(Node node)
/*     */     throws SAXException
/*     */   {
/* 353 */     if (this.m_state != null)
/*     */     {
/* 355 */       this.m_state.setCurrentNode(node);
/*     */     }
/*     */ 
/* 360 */     String data = node.getNodeValue();
/* 361 */     if (data != null)
/* 362 */       characters(data);
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException exc)
/*     */     throws SAXException
/*     */   {
/* 370 */     super.fatalError(exc);
/*     */ 
/* 372 */     this.m_needToCallStartDocument = false;
/*     */ 
/* 374 */     if ((this.m_saxHandler instanceof ErrorHandler))
/* 375 */       ((ErrorHandler)this.m_saxHandler).fatalError(exc);
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException exc)
/*     */     throws SAXException
/*     */   {
/* 383 */     super.error(exc);
/*     */ 
/* 385 */     if ((this.m_saxHandler instanceof ErrorHandler))
/* 386 */       ((ErrorHandler)this.m_saxHandler).error(exc);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException exc)
/*     */     throws SAXException
/*     */   {
/* 394 */     super.warning(exc);
/*     */ 
/* 396 */     if ((this.m_saxHandler instanceof ErrorHandler))
/* 397 */       ((ErrorHandler)this.m_saxHandler).warning(exc);
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */   {
/* 411 */     boolean wasReset = false;
/* 412 */     if (super.reset())
/*     */     {
/* 414 */       resetToSAXHandler();
/* 415 */       wasReset = true;
/*     */     }
/* 417 */     return wasReset;
/*     */   }
/*     */ 
/*     */   private void resetToSAXHandler()
/*     */   {
/* 426 */     this.m_lexHandler = null;
/* 427 */     this.m_saxHandler = null;
/* 428 */     this.m_state = null;
/* 429 */     this.m_shouldGenerateNSAttribute = false;
/*     */   }
/*     */ 
/*     */   public void addUniqueAttribute(String qName, String value, int flags)
/*     */     throws SAXException
/*     */   {
/* 438 */     addAttribute(qName, value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToSAXHandler
 * JD-Core Version:    0.6.2
 */