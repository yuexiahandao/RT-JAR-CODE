/*    */ package com.sun.xml.internal.stream;
/*    */ 
/*    */ import java.util.NoSuchElementException;
/*    */ import javax.xml.stream.EventFilter;
/*    */ import javax.xml.stream.XMLEventReader;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.events.XMLEvent;
/*    */ import javax.xml.stream.util.EventReaderDelegate;
/*    */ 
/*    */ public class EventFilterSupport extends EventReaderDelegate
/*    */ {
/*    */   EventFilter fEventFilter;
/*    */ 
/*    */   public EventFilterSupport(XMLEventReader eventReader, EventFilter eventFilter)
/*    */   {
/* 46 */     setParent(eventReader);
/* 47 */     this.fEventFilter = eventFilter;
/*    */   }
/*    */ 
/*    */   public Object next() {
/*    */     try {
/* 52 */       return nextEvent(); } catch (XMLStreamException ex) {
/*    */     }
/* 54 */     throw new NoSuchElementException();
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/*    */     try {
/* 60 */       return peek() != null; } catch (XMLStreamException ex) {
/*    */     }
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   public XMLEvent nextEvent() throws XMLStreamException
/*    */   {
/* 67 */     if (super.hasNext())
/*    */     {
/* 69 */       XMLEvent event = super.nextEvent();
/*    */ 
/* 72 */       if (this.fEventFilter.accept(event)) {
/* 73 */         return event;
/*    */       }
/*    */ 
/* 76 */       return nextEvent();
/*    */     }
/*    */ 
/* 79 */     throw new NoSuchElementException();
/*    */   }
/*    */ 
/*    */   public XMLEvent nextTag() throws XMLStreamException
/*    */   {
/* 84 */     if (super.hasNext()) {
/* 85 */       XMLEvent event = super.nextTag();
/*    */ 
/* 87 */       if (this.fEventFilter.accept(event)) {
/* 88 */         return event;
/*    */       }
/*    */ 
/* 91 */       return nextTag();
/*    */     }
/*    */ 
/* 94 */     throw new NoSuchElementException();
/*    */   }
/*    */ 
/*    */   public XMLEvent peek() throws XMLStreamException
/*    */   {
/*    */     while (true) {
/* 100 */       XMLEvent event = super.peek();
/* 101 */       if (event == null) return null;
/*    */ 
/* 103 */       if (this.fEventFilter.accept(event)) {
/* 104 */         return event;
/*    */       }
/*    */ 
/* 107 */       super.next();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.EventFilterSupport
 * JD-Core Version:    0.6.2
 */