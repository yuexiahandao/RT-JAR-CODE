/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import javax.xml.stream.EventFilter;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ public class StAXFilteredEvent
/*     */   implements XMLEventReader
/*     */ {
/*     */   private XMLEventReader eventReader;
/*     */   private EventFilter _filter;
/*     */ 
/*     */   public StAXFilteredEvent()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StAXFilteredEvent(XMLEventReader reader, EventFilter filter)
/*     */     throws XMLStreamException
/*     */   {
/*  47 */     this.eventReader = reader;
/*  48 */     this._filter = filter;
/*     */   }
/*     */ 
/*     */   public void setEventReader(XMLEventReader reader) {
/*  52 */     this.eventReader = reader;
/*     */   }
/*     */ 
/*     */   public void setFilter(EventFilter filter) {
/*  56 */     this._filter = filter;
/*     */   }
/*     */ 
/*     */   public Object next() {
/*     */     try {
/*  61 */       return nextEvent(); } catch (XMLStreamException e) {
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLEvent nextEvent()
/*     */     throws XMLStreamException
/*     */   {
/*  69 */     if (hasNext())
/*  70 */       return this.eventReader.nextEvent();
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public String getElementText() throws XMLStreamException
/*     */   {
/*  76 */     StringBuffer buffer = new StringBuffer();
/*  77 */     XMLEvent e = nextEvent();
/*  78 */     if (!e.isStartElement()) {
/*  79 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.mustBeOnSTART_ELEMENT"));
/*     */     }
/*     */ 
/*  82 */     while (hasNext()) {
/*  83 */       e = nextEvent();
/*  84 */       if (e.isStartElement()) {
/*  85 */         throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.getElementTextExpectTextOnly"));
/*     */       }
/*  87 */       if (e.isCharacters())
/*  88 */         buffer.append(((Characters)e).getData());
/*  89 */       if (e.isEndElement())
/*  90 */         return buffer.toString();
/*     */     }
/*  92 */     throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.END_ELEMENTnotFound"));
/*     */   }
/*     */ 
/*     */   public XMLEvent nextTag() throws XMLStreamException {
/*  96 */     while (hasNext()) {
/*  97 */       XMLEvent e = nextEvent();
/*  98 */       if ((e.isStartElement()) || (e.isEndElement()))
/*  99 */         return e;
/*     */     }
/* 101 */     throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.startOrEndNotFound"));
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*     */     try
/*     */     {
/* 108 */       while (this.eventReader.hasNext()) {
/* 109 */         if (this._filter.accept(this.eventReader.peek())) return true;
/* 110 */         this.eventReader.nextEvent();
/*     */       }
/* 112 */       return false; } catch (XMLStreamException e) {
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public XMLEvent peek() throws XMLStreamException
/*     */   {
/* 124 */     if (hasNext())
/* 125 */       return this.eventReader.peek();
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException
/*     */   {
/* 131 */     this.eventReader.close();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) {
/* 135 */     return this.eventReader.getProperty(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.StAXFilteredEvent
 * JD-Core Version:    0.6.2
 */