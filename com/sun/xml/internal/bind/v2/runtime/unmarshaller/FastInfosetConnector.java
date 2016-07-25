/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class FastInfosetConnector extends StAXConnector
/*     */ {
/*     */   private final StAXDocumentParser fastInfosetStreamReader;
/*     */   private boolean textReported;
/*  54 */   private final Base64Data base64Data = new Base64Data();
/*     */ 
/*  57 */   private final StringBuilder buffer = new StringBuilder();
/*     */ 
/* 238 */   private final CharSequenceImpl charArray = new CharSequenceImpl();
/*     */ 
/*     */   public FastInfosetConnector(StAXDocumentParser fastInfosetStreamReader, XmlVisitor visitor)
/*     */   {
/*  61 */     super(visitor);
/*  62 */     fastInfosetStreamReader.setStringInterning(true);
/*  63 */     this.fastInfosetStreamReader = fastInfosetStreamReader;
/*     */   }
/*     */ 
/*     */   public void bridge() throws XMLStreamException
/*     */   {
/*     */     try {
/*  69 */       int depth = 0;
/*     */ 
/*  72 */       int event = this.fastInfosetStreamReader.getEventType();
/*  73 */       if (event == 7)
/*     */       {
/*  75 */         while (!this.fastInfosetStreamReader.isStartElement()) {
/*  76 */           event = this.fastInfosetStreamReader.next();
/*     */         }
/*     */       }
/*     */ 
/*  80 */       if (event != 1) {
/*  81 */         throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
/*     */       }
/*     */ 
/*  85 */       handleStartDocument(this.fastInfosetStreamReader.getNamespaceContext());
/*     */       while (true)
/*     */       {
/*  92 */         switch (event) {
/*     */         case 1:
/*  94 */           handleStartElement();
/*  95 */           depth++;
/*  96 */           break;
/*     */         case 2:
/*  98 */           depth--;
/*  99 */           handleEndElement();
/* 100 */           if (depth != 0) break; break;
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 105 */           if (this.predictor.expectText())
/*     */           {
/* 108 */             event = this.fastInfosetStreamReader.peekNext();
/* 109 */             if (event == 2)
/* 110 */               processNonIgnorableText();
/* 111 */             else if (event == 1)
/* 112 */               processIgnorableText();
/*     */             else
/* 114 */               handleFragmentedCharacters();  } break;
/*     */         case 3:
/*     */         case 5:
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/* 120 */         case 11: } event = this.fastInfosetStreamReader.next();
/*     */       }
/*     */ 
/* 123 */       this.fastInfosetStreamReader.next();
/*     */ 
/* 125 */       handleEndDocument();
/*     */     } catch (SAXException e) {
/* 127 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Location getCurrentLocation() {
/* 132 */     return this.fastInfosetStreamReader.getLocation();
/*     */   }
/*     */ 
/*     */   protected String getCurrentQName() {
/* 136 */     return this.fastInfosetStreamReader.getNameString();
/*     */   }
/*     */ 
/*     */   private void handleStartElement() throws SAXException {
/* 140 */     processUnreportedText();
/*     */ 
/* 142 */     for (int i = 0; i < this.fastInfosetStreamReader.accessNamespaceCount(); i++) {
/* 143 */       this.visitor.startPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i), this.fastInfosetStreamReader.getNamespaceURI(i));
/*     */     }
/*     */ 
/* 147 */     this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI();
/* 148 */     this.tagName.local = this.fastInfosetStreamReader.accessLocalName();
/* 149 */     this.tagName.atts = this.fastInfosetStreamReader.getAttributesHolder();
/*     */ 
/* 151 */     this.visitor.startElement(this.tagName);
/*     */   }
/*     */ 
/*     */   private void handleFragmentedCharacters() throws XMLStreamException, SAXException {
/* 155 */     this.buffer.setLength(0);
/*     */ 
/* 158 */     this.buffer.append(this.fastInfosetStreamReader.getTextCharacters(), this.fastInfosetStreamReader.getTextStart(), this.fastInfosetStreamReader.getTextLength());
/*     */     while (true)
/*     */     {
/* 164 */       switch (this.fastInfosetStreamReader.peekNext()) {
/*     */       case 1:
/* 166 */         processBufferedText(true);
/* 167 */         return;
/*     */       case 2:
/* 169 */         processBufferedText(false);
/* 170 */         return;
/*     */       case 4:
/*     */       case 6:
/*     */       case 12:
/* 175 */         this.fastInfosetStreamReader.next();
/* 176 */         this.buffer.append(this.fastInfosetStreamReader.getTextCharacters(), this.fastInfosetStreamReader.getTextStart(), this.fastInfosetStreamReader.getTextLength());
/*     */ 
/* 179 */         break;
/*     */       case 3:
/*     */       case 5:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       default:
/* 181 */         this.fastInfosetStreamReader.next();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleEndElement() throws SAXException {
/* 187 */     processUnreportedText();
/*     */ 
/* 189 */     this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI();
/* 190 */     this.tagName.local = this.fastInfosetStreamReader.accessLocalName();
/*     */ 
/* 192 */     this.visitor.endElement(this.tagName);
/*     */ 
/* 194 */     for (int i = this.fastInfosetStreamReader.accessNamespaceCount() - 1; i >= 0; i--)
/* 195 */       this.visitor.endPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i));
/*     */   }
/*     */ 
/*     */   private void processNonIgnorableText()
/*     */     throws SAXException
/*     */   {
/* 241 */     this.textReported = true;
/* 242 */     boolean isTextAlgorithmAplied = this.fastInfosetStreamReader.getTextAlgorithmBytes() != null;
/*     */ 
/* 245 */     if ((isTextAlgorithmAplied) && (this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1))
/*     */     {
/* 247 */       this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
/* 248 */       this.visitor.text(this.base64Data);
/*     */     } else {
/* 250 */       if (isTextAlgorithmAplied) {
/* 251 */         this.fastInfosetStreamReader.getText();
/*     */       }
/*     */ 
/* 254 */       this.charArray.set();
/* 255 */       this.visitor.text(this.charArray);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processIgnorableText() throws SAXException {
/* 260 */     boolean isTextAlgorithmAplied = this.fastInfosetStreamReader.getTextAlgorithmBytes() != null;
/*     */ 
/* 263 */     if ((isTextAlgorithmAplied) && (this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1))
/*     */     {
/* 265 */       this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
/* 266 */       this.visitor.text(this.base64Data);
/* 267 */       this.textReported = true;
/*     */     } else {
/* 269 */       if (isTextAlgorithmAplied) {
/* 270 */         this.fastInfosetStreamReader.getText();
/*     */       }
/*     */ 
/* 273 */       this.charArray.set();
/* 274 */       if (!WhiteSpaceProcessor.isWhiteSpace(this.charArray)) {
/* 275 */         this.visitor.text(this.charArray);
/* 276 */         this.textReported = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processBufferedText(boolean ignorable) throws SAXException {
/* 282 */     if ((!ignorable) || (!WhiteSpaceProcessor.isWhiteSpace(this.buffer))) {
/* 283 */       this.visitor.text(this.buffer);
/* 284 */       this.textReported = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processUnreportedText() throws SAXException {
/* 289 */     if ((!this.textReported) && (this.predictor.expectText())) {
/* 290 */       this.visitor.text("");
/*     */     }
/* 292 */     this.textReported = false;
/*     */   }
/*     */ 
/*     */   private final class CharSequenceImpl
/*     */     implements CharSequence
/*     */   {
/*     */     char[] ch;
/*     */     int start;
/*     */     int length;
/*     */ 
/*     */     CharSequenceImpl()
/*     */     {
/*     */     }
/*     */ 
/*     */     CharSequenceImpl(char[] ch, int start, int length)
/*     */     {
/* 208 */       this.ch = ch;
/* 209 */       this.start = start;
/* 210 */       this.length = length;
/*     */     }
/*     */ 
/*     */     public void set() {
/* 214 */       this.ch = FastInfosetConnector.this.fastInfosetStreamReader.getTextCharacters();
/* 215 */       this.start = FastInfosetConnector.this.fastInfosetStreamReader.getTextStart();
/* 216 */       this.length = FastInfosetConnector.this.fastInfosetStreamReader.getTextLength();
/*     */     }
/*     */ 
/*     */     public final int length()
/*     */     {
/* 222 */       return this.length;
/*     */     }
/*     */ 
/*     */     public final char charAt(int index) {
/* 226 */       return this.ch[(this.start + index)];
/*     */     }
/*     */ 
/*     */     public final CharSequence subSequence(int start, int end) {
/* 230 */       return new CharSequenceImpl(FastInfosetConnector.this, this.ch, this.start + start, end - start);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 234 */       return new String(this.ch, this.start, this.length);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.FastInfosetConnector
 * JD-Core Version:    0.6.2
 */