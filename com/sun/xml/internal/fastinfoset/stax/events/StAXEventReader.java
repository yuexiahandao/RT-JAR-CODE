/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.stream.util.XMLEventAllocator;
/*     */ 
/*     */ public class StAXEventReader
/*     */   implements XMLEventReader
/*     */ {
/*     */   protected XMLStreamReader _streamReader;
/*     */   protected XMLEventAllocator _eventAllocator;
/*     */   private XMLEvent _currentEvent;
/*  45 */   private XMLEvent[] events = new XMLEvent[3];
/*  46 */   private int size = 3;
/*  47 */   private int currentIndex = 0;
/*  48 */   private boolean hasEvent = false;
/*     */ 
/*     */   public StAXEventReader(XMLStreamReader reader) throws XMLStreamException
/*     */   {
/*  52 */     this._streamReader = reader;
/*  53 */     this._eventAllocator = ((XMLEventAllocator)reader.getProperty("javax.xml.stream.allocator"));
/*  54 */     if (this._eventAllocator == null) {
/*  55 */       this._eventAllocator = new StAXEventAllocatorBase();
/*     */     }
/*     */ 
/*  58 */     if (this._streamReader.hasNext())
/*     */     {
/*  60 */       this._streamReader.next();
/*  61 */       this._currentEvent = this._eventAllocator.allocate(this._streamReader);
/*  62 */       this.events[0] = this._currentEvent;
/*  63 */       this.hasEvent = true;
/*     */     } else {
/*  65 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.noElement"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasNext() {
/*  70 */     return this.hasEvent;
/*     */   }
/*     */ 
/*     */   public XMLEvent nextEvent() throws XMLStreamException {
/*  74 */     XMLEvent event = null;
/*  75 */     XMLEvent nextEvent = null;
/*  76 */     if (this.hasEvent)
/*     */     {
/*  78 */       event = this.events[this.currentIndex];
/*  79 */       this.events[this.currentIndex] = null;
/*  80 */       if (this._streamReader.hasNext())
/*     */       {
/*  83 */         this._streamReader.next();
/*  84 */         nextEvent = this._eventAllocator.allocate(this._streamReader);
/*  85 */         if (++this.currentIndex == this.size)
/*  86 */           this.currentIndex = 0;
/*  87 */         this.events[this.currentIndex] = nextEvent;
/*  88 */         this.hasEvent = true;
/*     */       } else {
/*  90 */         this._currentEvent = null;
/*  91 */         this.hasEvent = false;
/*     */       }
/*  93 */       return event;
/*     */     }
/*     */ 
/*  96 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/* 102 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException
/*     */   {
/* 107 */     this._streamReader.close();
/*     */   }
/*     */ 
/*     */   public String getElementText()
/*     */     throws XMLStreamException
/*     */   {
/* 117 */     if (!this.hasEvent) {
/* 118 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/* 121 */     if (!this._currentEvent.isStartElement()) {
/* 122 */       StAXDocumentParser parser = (StAXDocumentParser)this._streamReader;
/* 123 */       return parser.getElementText(true);
/*     */     }
/* 125 */     return this._streamReader.getElementText();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 135 */     return this._streamReader.getProperty(name);
/*     */   }
/*     */ 
/*     */   public XMLEvent nextTag()
/*     */     throws XMLStreamException
/*     */   {
/* 147 */     if (!this.hasEvent) {
/* 148 */       throw new NoSuchElementException();
/*     */     }
/* 150 */     StAXDocumentParser parser = (StAXDocumentParser)this._streamReader;
/* 151 */     parser.nextTag(true);
/* 152 */     return this._eventAllocator.allocate(this._streamReader);
/*     */   }
/*     */ 
/*     */   public Object next()
/*     */   {
/*     */     try {
/* 158 */       return nextEvent(); } catch (XMLStreamException streamException) {
/*     */     }
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLEvent peek() throws XMLStreamException
/*     */   {
/* 165 */     if (!this.hasEvent)
/* 166 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.noElement"));
/* 167 */     this._currentEvent = this.events[this.currentIndex];
/* 168 */     return this._currentEvent;
/*     */   }
/*     */ 
/*     */   public void setAllocator(XMLEventAllocator allocator) {
/* 172 */     if (allocator == null) {
/* 173 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.nullXMLEventAllocator"));
/*     */     }
/* 175 */     this._eventAllocator = allocator;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.StAXEventReader
 * JD-Core Version:    0.6.2
 */