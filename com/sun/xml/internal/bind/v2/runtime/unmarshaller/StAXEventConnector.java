/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ final class StAXEventConnector extends StAXConnector
/*     */ {
/*     */   private final XMLEventReader staxEventReader;
/*     */   private XMLEvent event;
/*  66 */   private final AttributesImpl attrs = new AttributesImpl();
/*     */ 
/*  72 */   private final StringBuilder buffer = new StringBuilder();
/*     */   private boolean seenText;
/*     */ 
/*     */   public StAXEventConnector(XMLEventReader staxCore, XmlVisitor visitor)
/*     */   {
/*  86 */     super(visitor);
/*  87 */     this.staxEventReader = staxCore;
/*     */   }
/*     */ 
/*     */   public void bridge() throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/*  94 */       int depth = 0;
/*     */ 
/*  96 */       this.event = this.staxEventReader.peek();
/*     */ 
/*  98 */       if ((!this.event.isStartDocument()) && (!this.event.isStartElement())) {
/*  99 */         throw new IllegalStateException();
/*     */       }
/*     */       do
/*     */       {
/* 103 */         this.event = this.staxEventReader.nextEvent();
/* 104 */       }while (!this.event.isStartElement());
/*     */ 
/* 106 */       handleStartDocument(this.event.asStartElement().getNamespaceContext());
/*     */       while (true)
/*     */       {
/* 113 */         switch (this.event.getEventType()) {
/*     */         case 1:
/* 115 */           handleStartElement(this.event.asStartElement());
/* 116 */           depth++;
/* 117 */           break;
/*     */         case 2:
/* 119 */           depth--;
/* 120 */           handleEndElement(this.event.asEndElement());
/* 121 */           if (depth != 0) break; break;
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 126 */           handleCharacters(this.event.asCharacters());
/*     */         case 3:
/*     */         case 5:
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/* 131 */         case 11: } this.event = this.staxEventReader.nextEvent();
/*     */       }
/*     */ 
/* 134 */       handleEndDocument();
/* 135 */       this.event = null;
/*     */     } catch (SAXException e) {
/* 137 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Location getCurrentLocation() {
/* 142 */     return this.event.getLocation();
/*     */   }
/*     */ 
/*     */   protected String getCurrentQName()
/*     */   {
/*     */     QName qName;
/*     */     QName qName;
/* 147 */     if (this.event.isEndElement())
/* 148 */       qName = this.event.asEndElement().getName();
/*     */     else
/* 150 */       qName = this.event.asStartElement().getName();
/* 151 */     return getQName(qName.getPrefix(), qName.getLocalPart());
/*     */   }
/*     */ 
/*     */   private void handleCharacters(Characters event) throws SAXException, XMLStreamException
/*     */   {
/* 156 */     if (!this.predictor.expectText()) {
/* 157 */       return;
/*     */     }
/* 159 */     this.seenText = true;
/*     */     XMLEvent next;
/*     */     while (true)
/*     */     {
/* 164 */       next = this.staxEventReader.peek();
/* 165 */       if (!isIgnorable(next))
/*     */         break;
/* 167 */       this.staxEventReader.nextEvent();
/*     */     }
/*     */ 
/* 170 */     if (isTag(next))
/*     */     {
/* 172 */       this.visitor.text(event.getData());
/* 173 */       return;
/*     */     }
/*     */ 
/* 178 */     this.buffer.append(event.getData());
/*     */     while (true)
/*     */     {
/* 182 */       next = this.staxEventReader.peek();
/* 183 */       if (isIgnorable(next))
/*     */       {
/* 185 */         this.staxEventReader.nextEvent();
/*     */       }
/*     */       else {
/* 188 */         if (isTag(next))
/*     */         {
/* 190 */           this.visitor.text(this.buffer);
/* 191 */           this.buffer.setLength(0);
/* 192 */           return;
/*     */         }
/*     */ 
/* 195 */         this.buffer.append(next.asCharacters().getData());
/* 196 */         this.staxEventReader.nextEvent();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 201 */   private boolean isTag(XMLEvent event) { int eventType = event.getEventType();
/* 202 */     return (eventType == 1) || (eventType == 2); }
/*     */ 
/*     */   private boolean isIgnorable(XMLEvent event)
/*     */   {
/* 206 */     int eventType = event.getEventType();
/* 207 */     return (eventType == 5) || (eventType == 3);
/*     */   }
/*     */ 
/*     */   private void handleEndElement(EndElement event) throws SAXException {
/* 211 */     if ((!this.seenText) && (this.predictor.expectText())) {
/* 212 */       this.visitor.text("");
/*     */     }
/*     */ 
/* 216 */     QName qName = event.getName();
/* 217 */     this.tagName.uri = fixNull(qName.getNamespaceURI());
/* 218 */     this.tagName.local = qName.getLocalPart();
/* 219 */     this.visitor.endElement(this.tagName);
/*     */ 
/* 222 */     for (Iterator i = event.getNamespaces(); i.hasNext(); ) {
/* 223 */       String prefix = fixNull(((Namespace)i.next()).getPrefix());
/* 224 */       this.visitor.endPrefixMapping(prefix);
/*     */     }
/*     */ 
/* 227 */     this.seenText = false;
/*     */   }
/*     */ 
/*     */   private void handleStartElement(StartElement event) throws SAXException
/*     */   {
/* 232 */     for (Iterator i = event.getNamespaces(); i.hasNext(); ) {
/* 233 */       Namespace ns = (Namespace)i.next();
/* 234 */       this.visitor.startPrefixMapping(fixNull(ns.getPrefix()), fixNull(ns.getNamespaceURI()));
/*     */     }
/*     */ 
/* 240 */     QName qName = event.getName();
/* 241 */     this.tagName.uri = fixNull(qName.getNamespaceURI());
/* 242 */     String localName = qName.getLocalPart();
/* 243 */     this.tagName.uri = fixNull(qName.getNamespaceURI());
/* 244 */     this.tagName.local = localName;
/* 245 */     this.tagName.atts = getAttributes(event);
/* 246 */     this.visitor.startElement(this.tagName);
/*     */ 
/* 248 */     this.seenText = false;
/*     */   }
/*     */ 
/*     */   private Attributes getAttributes(StartElement event)
/*     */   {
/* 259 */     this.attrs.clear();
/*     */ 
/* 266 */     for (Iterator i = event.getAttributes(); i.hasNext(); ) {
/* 267 */       Attribute staxAttr = (Attribute)i.next();
/*     */ 
/* 269 */       QName name = staxAttr.getName();
/* 270 */       String uri = fixNull(name.getNamespaceURI());
/* 271 */       String localName = name.getLocalPart();
/* 272 */       String prefix = name.getPrefix();
/*     */       String qName;
/*     */       String qName;
/* 274 */       if ((prefix == null) || (prefix.length() == 0))
/* 275 */         qName = localName;
/*     */       else
/* 277 */         qName = prefix + ':' + localName;
/* 278 */       String type = staxAttr.getDTDType();
/* 279 */       String value = staxAttr.getValue();
/*     */ 
/* 281 */       this.attrs.addAttribute(uri, localName, qName, type, value);
/*     */     }
/*     */ 
/* 284 */     return this.attrs;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXEventConnector
 * JD-Core Version:    0.6.2
 */