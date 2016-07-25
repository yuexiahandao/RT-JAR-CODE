/*     */ package com.sun.xml.internal.stream;
/*     */ 
/*     */ import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EntityDeclaration;
/*     */ import javax.xml.stream.events.EntityReference;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.stream.util.XMLEventAllocator;
/*     */ 
/*     */ public class XMLEventReaderImpl
/*     */   implements XMLEventReader
/*     */ {
/*     */   protected XMLStreamReader fXMLReader;
/*     */   protected XMLEventAllocator fXMLEventAllocator;
/*     */   private XMLEvent fPeekedEvent;
/*     */   private XMLEvent fLastEvent;
/*     */ 
/*     */   public XMLEventReaderImpl(XMLStreamReader reader)
/*     */     throws XMLStreamException
/*     */   {
/*  50 */     this.fXMLReader = reader;
/*  51 */     this.fXMLEventAllocator = ((XMLEventAllocator)reader.getProperty("javax.xml.stream.allocator"));
/*  52 */     if (this.fXMLEventAllocator == null) {
/*  53 */       this.fXMLEventAllocator = new XMLEventAllocatorImpl();
/*     */     }
/*  55 */     this.fPeekedEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  61 */     if (this.fPeekedEvent != null) return true;
/*     */ 
/*  64 */     boolean next = false;
/*     */     try {
/*  66 */       next = this.fXMLReader.hasNext();
/*     */     } catch (XMLStreamException ex) {
/*  68 */       return false;
/*     */     }
/*  70 */     return next;
/*     */   }
/*     */ 
/*     */   public XMLEvent nextEvent()
/*     */     throws XMLStreamException
/*     */   {
/*  76 */     if (this.fPeekedEvent != null) {
/*  77 */       this.fLastEvent = this.fPeekedEvent;
/*  78 */       this.fPeekedEvent = null;
/*  79 */       return this.fLastEvent;
/*     */     }
/*  81 */     if (this.fXMLReader.hasNext())
/*     */     {
/*  83 */       this.fXMLReader.next();
/*  84 */       return this.fLastEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
/*     */     }
/*     */ 
/*  87 */     this.fLastEvent = null;
/*  88 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/*  94 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException
/*     */   {
/*  99 */     this.fXMLReader.close();
/*     */   }
/*     */ 
/*     */   public String getElementText()
/*     */     throws XMLStreamException
/*     */   {
/* 111 */     if (this.fLastEvent.getEventType() != 1) {
/* 112 */       throw new XMLStreamException("parser must be on START_ELEMENT to read next text", this.fLastEvent.getLocation());
/*     */     }
/*     */ 
/* 122 */     String data = null;
/*     */ 
/* 124 */     if (this.fPeekedEvent != null) {
/* 125 */       XMLEvent event = this.fPeekedEvent;
/* 126 */       this.fPeekedEvent = null;
/* 127 */       int type = event.getEventType();
/*     */ 
/* 129 */       if ((type == 4) || (type == 6) || (type == 12))
/*     */       {
/* 131 */         data = event.asCharacters().getData();
/*     */       }
/* 133 */       else if (type == 9) {
/* 134 */         data = ((EntityReference)event).getDeclaration().getReplacementText();
/*     */       }
/* 136 */       else if ((type != 5) && (type != 3))
/*     */       {
/* 138 */         if (type == 1) {
/* 139 */           throw new XMLStreamException("elementGetText() function expects text only elment but START_ELEMENT was encountered.", event.getLocation());
/*     */         }
/* 141 */         if (type == 2) {
/* 142 */           return "";
/*     */         }
/*     */       }
/*     */ 
/* 146 */       StringBuffer buffer = new StringBuffer();
/* 147 */       if ((data != null) && (data.length() > 0)) {
/* 148 */         buffer.append(data);
/*     */       }
/*     */ 
/* 155 */       event = nextEvent();
/* 156 */       while (event.getEventType() != 2) {
/* 157 */         if ((type == 4) || (type == 6) || (type == 12))
/*     */         {
/* 159 */           data = event.asCharacters().getData();
/*     */         }
/* 161 */         else if (type == 9) {
/* 162 */           data = ((EntityReference)event).getDeclaration().getReplacementText();
/*     */         }
/* 164 */         else if ((type != 5) && (type != 3))
/*     */         {
/* 166 */           if (type == 8)
/* 167 */             throw new XMLStreamException("unexpected end of document when reading element text content");
/* 168 */           if (type == 1) {
/* 169 */             throw new XMLStreamException("elementGetText() function expects text only elment but START_ELEMENT was encountered.", event.getLocation());
/*     */           }
/*     */ 
/* 172 */           throw new XMLStreamException("Unexpected event type " + type, event.getLocation());
/*     */         }
/*     */ 
/* 176 */         if ((data != null) && (data.length() > 0)) {
/* 177 */           buffer.append(data);
/*     */         }
/* 179 */         event = nextEvent();
/*     */       }
/* 181 */       return buffer.toString();
/*     */     }
/*     */ 
/* 186 */     data = this.fXMLReader.getElementText();
/* 187 */     this.fLastEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
/* 188 */     return data;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 197 */     return this.fXMLReader.getProperty(name);
/*     */   }
/*     */ 
/*     */   public XMLEvent nextTag()
/*     */     throws XMLStreamException
/*     */   {
/* 210 */     if (this.fPeekedEvent != null)
/*     */     {
/* 212 */       XMLEvent event = this.fPeekedEvent;
/* 213 */       this.fPeekedEvent = null;
/* 214 */       int eventType = event.getEventType();
/*     */ 
/* 217 */       if (((event.isCharacters()) && (event.asCharacters().isWhiteSpace())) || (eventType == 3) || (eventType == 5) || (eventType == 7))
/*     */       {
/* 221 */         event = nextEvent();
/* 222 */         eventType = event.getEventType();
/*     */       }
/*     */ 
/* 228 */       while (((event.isCharacters()) && (event.asCharacters().isWhiteSpace())) || (eventType == 3) || (eventType == 5))
/*     */       {
/* 230 */         event = nextEvent();
/* 231 */         eventType = event.getEventType();
/*     */       }
/*     */ 
/* 234 */       if ((eventType != 1) && (eventType != 2)) {
/* 235 */         throw new XMLStreamException("expected start or end tag", event.getLocation());
/*     */       }
/* 237 */       return event;
/*     */     }
/*     */ 
/* 241 */     this.fXMLReader.nextTag();
/* 242 */     return this.fLastEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
/*     */   }
/*     */ 
/*     */   public Object next() {
/* 246 */     Object object = null;
/*     */     try {
/* 248 */       object = nextEvent();
/*     */     } catch (XMLStreamException streamException) {
/* 250 */       this.fLastEvent = null;
/*     */ 
/* 252 */       NoSuchElementException e = new NoSuchElementException(streamException.getMessage());
/* 253 */       e.initCause(streamException.getCause());
/* 254 */       throw e;
/*     */     }
/* 256 */     return object;
/*     */   }
/*     */ 
/*     */   public XMLEvent peek()
/*     */     throws XMLStreamException
/*     */   {
/* 262 */     if (this.fPeekedEvent != null) return this.fPeekedEvent;
/*     */ 
/* 264 */     if (hasNext())
/*     */     {
/* 275 */       this.fXMLReader.next();
/* 276 */       this.fPeekedEvent = this.fXMLEventAllocator.allocate(this.fXMLReader);
/* 277 */       return this.fPeekedEvent;
/*     */     }
/* 279 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.XMLEventReaderImpl
 * JD-Core Version:    0.6.2
 */