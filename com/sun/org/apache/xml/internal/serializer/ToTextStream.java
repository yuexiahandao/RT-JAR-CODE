/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Messages;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Utils;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Writer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class ToTextStream extends ToStream
/*     */ {
/*     */   protected void startDocumentInternal()
/*     */     throws SAXException
/*     */   {
/*  67 */     super.startDocumentInternal();
/*     */ 
/*  69 */     this.m_needToCallStartDocument = false;
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*  90 */     flushPending();
/*  91 */     flushWriter();
/*  92 */     if (this.m_tracer != null)
/*  93 */       super.fireEndDoc();
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String name, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 133 */     if (this.m_tracer != null) {
/* 134 */       super.fireStartElem(name);
/* 135 */       firePseudoAttributes();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String name)
/*     */     throws SAXException
/*     */   {
/* 168 */     if (this.m_tracer != null)
/* 169 */       super.fireEndElem(name);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 201 */     flushPending();
/*     */     try
/*     */     {
/* 205 */       if (inTemporaryOutputState())
/*     */       {
/* 217 */         this.m_writer.write(ch, start, length);
/*     */       }
/*     */       else
/*     */       {
/* 221 */         writeNormalizedChars(ch, start, length, this.m_lineSepUse);
/*     */       }
/*     */ 
/* 224 */       if (this.m_tracer != null)
/* 225 */         super.fireCharEvent(ch, start, length);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 229 */       throw new SAXException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void charactersRaw(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 250 */       writeNormalizedChars(ch, start, length, this.m_lineSepUse);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 254 */       throw new SAXException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeNormalizedChars(char[] ch, int start, int length, boolean useLineSep)
/*     */     throws IOException, SAXException
/*     */   {
/* 279 */     String encoding = getEncoding();
/* 280 */     Writer writer = this.m_writer;
/* 281 */     int end = start + length;
/*     */ 
/* 284 */     char S_LINEFEED = '\n';
/*     */ 
/* 290 */     for (int i = start; i < end; i++) {
/* 291 */       char c = ch[i];
/*     */ 
/* 293 */       if (('\n' == c) && (useLineSep)) {
/* 294 */         writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*     */       }
/* 296 */       else if (this.m_encodingInfo.isInEncoding(c)) {
/* 297 */         writer.write(c);
/*     */       }
/* 299 */       else if (Encodings.isHighUTF16Surrogate(c)) {
/* 300 */         int codePoint = writeUTF16Surrogate(c, ch, i, end);
/* 301 */         if (codePoint != 0)
/*     */         {
/* 304 */           String integralValue = Integer.toString(codePoint);
/* 305 */           String msg = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[] { integralValue, encoding });
/*     */ 
/* 312 */           System.err.println(msg);
/*     */         }
/*     */ 
/* 315 */         i++;
/*     */       }
/* 320 */       else if (encoding != null)
/*     */       {
/* 326 */         writer.write(38);
/* 327 */         writer.write(35);
/* 328 */         writer.write(Integer.toString(c));
/* 329 */         writer.write(59);
/*     */ 
/* 333 */         String integralValue = Integer.toString(c);
/* 334 */         String msg = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[] { integralValue, encoding });
/*     */ 
/* 341 */         System.err.println(msg);
/*     */       }
/*     */       else
/*     */       {
/* 346 */         writer.write(c);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cdata(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 384 */       writeNormalizedChars(ch, start, length, this.m_lineSepUse);
/* 385 */       if (this.m_tracer != null)
/* 386 */         super.fireCDATAEvent(ch, start, length);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 390 */       throw new SAXException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 426 */       writeNormalizedChars(ch, start, length, this.m_lineSepUse);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 430 */       throw new SAXException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 457 */     flushPending();
/*     */ 
/* 459 */     if (this.m_tracer != null)
/* 460 */       super.fireEscapingEvent(target, data);
/*     */   }
/*     */ 
/*     */   public void comment(String data)
/*     */     throws SAXException
/*     */   {
/* 474 */     int length = data.length();
/* 475 */     if (length > this.m_charsBuff.length)
/*     */     {
/* 477 */       this.m_charsBuff = new char[length * 2 + 1];
/*     */     }
/* 479 */     data.getChars(0, length, this.m_charsBuff, 0);
/* 480 */     comment(this.m_charsBuff, 0, length);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 499 */     flushPending();
/* 500 */     if (this.m_tracer != null)
/* 501 */       super.fireCommentEvent(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void entityReference(String name)
/*     */     throws SAXException
/*     */   {
/* 513 */     if (this.m_tracer != null)
/* 514 */       super.fireEntityReference(name);
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endCDATA()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endElement(String elemName)
/*     */     throws SAXException
/*     */   {
/* 544 */     if (this.m_tracer != null)
/* 545 */       super.fireEndElem(elemName);
/*     */   }
/*     */ 
/*     */   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
/*     */     throws SAXException
/*     */   {
/* 557 */     if (this.m_needToCallStartDocument) {
/* 558 */       startDocumentInternal();
/*     */     }
/* 560 */     if (this.m_tracer != null) {
/* 561 */       super.fireStartElem(elementName);
/* 562 */       firePseudoAttributes();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(String characters)
/*     */     throws SAXException
/*     */   {
/* 575 */     int length = characters.length();
/* 576 */     if (length > this.m_charsBuff.length)
/*     */     {
/* 578 */       this.m_charsBuff = new char[length * 2 + 1];
/*     */     }
/* 580 */     characters.getChars(0, length, this.m_charsBuff, 0);
/* 581 */     characters(this.m_charsBuff, 0, length);
/*     */   }
/*     */ 
/*     */   public void addAttribute(String name, String value)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addUniqueAttribute(String qName, String value, int flags)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
/*     */     throws SAXException
/*     */   {
/* 609 */     return false;
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
/*     */ 
/*     */   public void flushPending()
/*     */     throws SAXException
/*     */   {
/* 630 */     if (this.m_needToCallStartDocument)
/*     */     {
/* 632 */       startDocumentInternal();
/* 633 */       this.m_needToCallStartDocument = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToTextStream
 * JD-Core Version:    0.6.2
 */