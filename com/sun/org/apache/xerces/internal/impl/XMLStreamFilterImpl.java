/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.StreamFilter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public class XMLStreamFilterImpl
/*     */   implements XMLStreamReader
/*     */ {
/*  50 */   private StreamFilter fStreamFilter = null;
/*  51 */   private XMLStreamReader fStreamReader = null;
/*     */   private int fCurrentEvent;
/*  53 */   private boolean fEventAccepted = false;
/*     */ 
/*  58 */   private boolean fStreamAdvancedByHasNext = false;
/*     */ 
/*     */   public XMLStreamFilterImpl(XMLStreamReader reader, StreamFilter filter)
/*     */   {
/*  62 */     this.fStreamReader = reader;
/*  63 */     this.fStreamFilter = filter;
/*     */     try
/*     */     {
/*  68 */       if (this.fStreamFilter.accept(this.fStreamReader))
/*  69 */         this.fEventAccepted = true;
/*     */       else
/*  71 */         findNextEvent();
/*     */     }
/*     */     catch (XMLStreamException xs) {
/*  74 */       System.err.println("Error while creating a stream Filter" + xs);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setStreamFilter(StreamFilter sf)
/*     */   {
/*  83 */     this.fStreamFilter = sf;
/*     */   }
/*     */ 
/*     */   public int next()
/*     */     throws XMLStreamException
/*     */   {
/*  92 */     if ((this.fStreamAdvancedByHasNext) && (this.fEventAccepted)) {
/*  93 */       this.fStreamAdvancedByHasNext = false;
/*  94 */       return this.fCurrentEvent;
/*     */     }
/*  96 */     int event = findNextEvent();
/*  97 */     if (event != -1) {
/*  98 */       return event;
/*     */     }
/*     */ 
/* 101 */     throw new IllegalStateException("The stream reader has reached the end of the document, or there are no more  items to return");
/*     */   }
/*     */ 
/*     */   public int nextTag()
/*     */     throws XMLStreamException
/*     */   {
/* 110 */     if ((this.fStreamAdvancedByHasNext) && (this.fEventAccepted) && ((this.fCurrentEvent == 1) || (this.fCurrentEvent == 1)))
/*     */     {
/* 112 */       this.fStreamAdvancedByHasNext = false;
/* 113 */       return this.fCurrentEvent;
/*     */     }
/*     */ 
/* 116 */     int event = findNextTag();
/* 117 */     if (event != -1) {
/* 118 */       return event;
/*     */     }
/* 120 */     throw new IllegalStateException("The stream reader has reached the end of the document, or there are no more  items to return");
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */     throws XMLStreamException
/*     */   {
/* 130 */     if (this.fStreamReader.hasNext()) {
/* 131 */       if (!this.fEventAccepted) {
/* 132 */         if ((this.fCurrentEvent = findNextEvent()) == -1) {
/* 133 */           return false;
/*     */         }
/* 135 */         this.fStreamAdvancedByHasNext = true;
/*     */       }
/*     */ 
/* 138 */       return true;
/*     */     }
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   private int findNextEvent() throws XMLStreamException {
/* 144 */     this.fStreamAdvancedByHasNext = false;
/* 145 */     while (this.fStreamReader.hasNext()) {
/* 146 */       this.fCurrentEvent = this.fStreamReader.next();
/* 147 */       if (this.fStreamFilter.accept(this.fStreamReader)) {
/* 148 */         this.fEventAccepted = true;
/* 149 */         return this.fCurrentEvent;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 155 */     if (this.fCurrentEvent == 8) {
/* 156 */       return this.fCurrentEvent;
/*     */     }
/* 158 */     return -1;
/*     */   }
/*     */   private int findNextTag() throws XMLStreamException {
/* 161 */     this.fStreamAdvancedByHasNext = false;
/* 162 */     while (this.fStreamReader.hasNext()) {
/* 163 */       this.fCurrentEvent = this.fStreamReader.nextTag();
/* 164 */       if (this.fStreamFilter.accept(this.fStreamReader)) {
/* 165 */         this.fEventAccepted = true;
/* 166 */         return this.fCurrentEvent;
/*     */       }
/*     */     }
/* 169 */     if (this.fCurrentEvent == 8) {
/* 170 */       return this.fCurrentEvent;
/*     */     }
/* 172 */     return -1;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws XMLStreamException
/*     */   {
/* 179 */     this.fStreamReader.close();
/*     */   }
/*     */ 
/*     */   public int getAttributeCount()
/*     */   {
/* 187 */     return this.fStreamReader.getAttributeCount();
/*     */   }
/*     */ 
/*     */   public QName getAttributeName(int index)
/*     */   {
/* 196 */     return this.fStreamReader.getAttributeName(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeNamespace(int index)
/*     */   {
/* 205 */     return this.fStreamReader.getAttributeNamespace(index);
/*     */   }
/*     */ 
/*     */   public String getAttributePrefix(int index)
/*     */   {
/* 214 */     return this.fStreamReader.getAttributePrefix(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeType(int index)
/*     */   {
/* 223 */     return this.fStreamReader.getAttributeType(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(int index)
/*     */   {
/* 232 */     return this.fStreamReader.getAttributeValue(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(String namespaceURI, String localName)
/*     */   {
/* 242 */     return this.fStreamReader.getAttributeValue(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public String getCharacterEncodingScheme()
/*     */   {
/* 250 */     return this.fStreamReader.getCharacterEncodingScheme();
/*     */   }
/*     */ 
/*     */   public String getElementText()
/*     */     throws XMLStreamException
/*     */   {
/* 259 */     return this.fStreamReader.getElementText();
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 267 */     return this.fStreamReader.getEncoding();
/*     */   }
/*     */ 
/*     */   public int getEventType()
/*     */   {
/* 275 */     return this.fStreamReader.getEventType();
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 283 */     return this.fStreamReader.getLocalName();
/*     */   }
/*     */ 
/*     */   public Location getLocation()
/*     */   {
/* 291 */     return this.fStreamReader.getLocation();
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/* 299 */     return this.fStreamReader.getName();
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/* 307 */     return this.fStreamReader.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public int getNamespaceCount()
/*     */   {
/* 315 */     return this.fStreamReader.getNamespaceCount();
/*     */   }
/*     */ 
/*     */   public String getNamespacePrefix(int index)
/*     */   {
/* 324 */     return this.fStreamReader.getNamespacePrefix(index);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI()
/*     */   {
/* 332 */     return this.fStreamReader.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(int index)
/*     */   {
/* 341 */     return this.fStreamReader.getNamespaceURI(index);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/* 350 */     return this.fStreamReader.getNamespaceURI(prefix);
/*     */   }
/*     */ 
/*     */   public String getPIData()
/*     */   {
/* 358 */     return this.fStreamReader.getPIData();
/*     */   }
/*     */ 
/*     */   public String getPITarget()
/*     */   {
/* 366 */     return this.fStreamReader.getPITarget();
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 374 */     return this.fStreamReader.getPrefix();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 384 */     return this.fStreamReader.getProperty(name);
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 392 */     return this.fStreamReader.getText();
/*     */   }
/*     */ 
/*     */   public char[] getTextCharacters()
/*     */   {
/* 400 */     return this.fStreamReader.getTextCharacters();
/*     */   }
/*     */ 
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
/*     */     throws XMLStreamException
/*     */   {
/* 413 */     return this.fStreamReader.getTextCharacters(sourceStart, target, targetStart, length);
/*     */   }
/*     */ 
/*     */   public int getTextLength()
/*     */   {
/* 421 */     return this.fStreamReader.getTextLength();
/*     */   }
/*     */ 
/*     */   public int getTextStart()
/*     */   {
/* 429 */     return this.fStreamReader.getTextStart();
/*     */   }
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 437 */     return this.fStreamReader.getVersion();
/*     */   }
/*     */ 
/*     */   public boolean hasName()
/*     */   {
/* 445 */     return this.fStreamReader.hasName();
/*     */   }
/*     */ 
/*     */   public boolean hasText()
/*     */   {
/* 453 */     return this.fStreamReader.hasText();
/*     */   }
/*     */ 
/*     */   public boolean isAttributeSpecified(int index)
/*     */   {
/* 462 */     return this.fStreamReader.isAttributeSpecified(index);
/*     */   }
/*     */ 
/*     */   public boolean isCharacters()
/*     */   {
/* 470 */     return this.fStreamReader.isCharacters();
/*     */   }
/*     */ 
/*     */   public boolean isEndElement()
/*     */   {
/* 478 */     return this.fStreamReader.isEndElement();
/*     */   }
/*     */ 
/*     */   public boolean isStandalone()
/*     */   {
/* 486 */     return this.fStreamReader.isStandalone();
/*     */   }
/*     */ 
/*     */   public boolean isStartElement()
/*     */   {
/* 494 */     return this.fStreamReader.isStartElement();
/*     */   }
/*     */ 
/*     */   public boolean isWhiteSpace()
/*     */   {
/* 502 */     return this.fStreamReader.isWhiteSpace();
/*     */   }
/*     */ 
/*     */   public void require(int type, String namespaceURI, String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 514 */     this.fStreamReader.require(type, namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public boolean standaloneSet()
/*     */   {
/* 522 */     return this.fStreamReader.standaloneSet();
/*     */   }
/*     */ 
/*     */   public String getAttributeLocalName(int index)
/*     */   {
/* 531 */     return this.fStreamReader.getAttributeLocalName(index);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLStreamFilterImpl
 * JD-Core Version:    0.6.2
 */