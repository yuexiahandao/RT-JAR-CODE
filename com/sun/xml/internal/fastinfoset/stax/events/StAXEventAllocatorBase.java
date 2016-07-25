/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.stream.util.XMLEventAllocator;
/*     */ import javax.xml.stream.util.XMLEventConsumer;
/*     */ 
/*     */ public class StAXEventAllocatorBase
/*     */   implements XMLEventAllocator
/*     */ {
/*     */   XMLEventFactory factory;
/*     */ 
/*     */   public StAXEventAllocatorBase()
/*     */   {
/*  60 */     if (System.getProperty("javax.xml.stream.XMLEventFactory") == null) {
/*  61 */       System.setProperty("javax.xml.stream.XMLEventFactory", "com.sun.xml.internal.fastinfoset.stax.factory.StAXEventFactory");
/*     */     }
/*     */ 
/*  64 */     this.factory = XMLEventFactory.newInstance();
/*     */   }
/*     */ 
/*     */   public XMLEventAllocator newInstance()
/*     */   {
/*  74 */     return new StAXEventAllocatorBase();
/*     */   }
/*     */ 
/*     */   public XMLEvent allocate(XMLStreamReader streamReader)
/*     */     throws XMLStreamException
/*     */   {
/*  85 */     if (streamReader == null)
/*  86 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.nullReader"));
/*  87 */     return getXMLEvent(streamReader);
/*     */   }
/*     */ 
/*     */   public void allocate(XMLStreamReader streamReader, XMLEventConsumer consumer)
/*     */     throws XMLStreamException
/*     */   {
/*  98 */     consumer.add(getXMLEvent(streamReader));
/*     */   }
/*     */ 
/*     */   XMLEvent getXMLEvent(XMLStreamReader reader)
/*     */   {
/* 105 */     XMLEvent event = null;
/*     */ 
/* 107 */     int eventType = reader.getEventType();
/*     */ 
/* 109 */     this.factory.setLocation(reader.getLocation());
/* 110 */     switch (eventType)
/*     */     {
/*     */     case 1:
/* 114 */       StartElementEvent startElement = (StartElementEvent)this.factory.createStartElement(reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
/*     */ 
/* 117 */       addAttributes(startElement, reader);
/* 118 */       addNamespaces(startElement, reader);
/*     */ 
/* 121 */       event = startElement;
/* 122 */       break;
/*     */     case 2:
/* 126 */       EndElementEvent endElement = (EndElementEvent)this.factory.createEndElement(reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
/*     */ 
/* 128 */       addNamespaces(endElement, reader);
/* 129 */       event = endElement;
/* 130 */       break;
/*     */     case 3:
/* 134 */       event = this.factory.createProcessingInstruction(reader.getPITarget(), reader.getPIData());
/* 135 */       break;
/*     */     case 4:
/* 139 */       if (reader.isWhiteSpace())
/* 140 */         event = this.factory.createSpace(reader.getText());
/*     */       else {
/* 142 */         event = this.factory.createCharacters(reader.getText());
/*     */       }
/* 144 */       break;
/*     */     case 5:
/* 148 */       event = this.factory.createComment(reader.getText());
/* 149 */       break;
/*     */     case 7:
/* 153 */       StartDocumentEvent docEvent = (StartDocumentEvent)this.factory.createStartDocument(reader.getVersion(), reader.getEncoding(), reader.isStandalone());
/*     */ 
/* 155 */       if (reader.getCharacterEncodingScheme() != null)
/* 156 */         docEvent.setDeclaredEncoding(true);
/*     */       else {
/* 158 */         docEvent.setDeclaredEncoding(false);
/*     */       }
/* 160 */       event = docEvent;
/* 161 */       break;
/*     */     case 8:
/* 164 */       EndDocumentEvent endDocumentEvent = new EndDocumentEvent();
/* 165 */       event = endDocumentEvent;
/* 166 */       break;
/*     */     case 9:
/* 169 */       event = this.factory.createEntityReference(reader.getLocalName(), new EntityDeclarationImpl(reader.getLocalName(), reader.getText()));
/*     */ 
/* 171 */       break;
/*     */     case 10:
/* 175 */       event = null;
/* 176 */       break;
/*     */     case 11:
/* 179 */       event = this.factory.createDTD(reader.getText());
/* 180 */       break;
/*     */     case 12:
/* 183 */       event = this.factory.createCData(reader.getText());
/* 184 */       break;
/*     */     case 6:
/* 187 */       event = this.factory.createSpace(reader.getText());
/*     */     }
/*     */ 
/* 191 */     return event;
/*     */   }
/*     */ 
/*     */   protected void addAttributes(StartElementEvent event, XMLStreamReader streamReader)
/*     */   {
/* 196 */     AttributeBase attr = null;
/* 197 */     for (int i = 0; i < streamReader.getAttributeCount(); i++) {
/* 198 */       attr = (AttributeBase)this.factory.createAttribute(streamReader.getAttributeName(i), streamReader.getAttributeValue(i));
/*     */ 
/* 200 */       attr.setAttributeType(streamReader.getAttributeType(i));
/* 201 */       attr.setSpecified(streamReader.isAttributeSpecified(i));
/* 202 */       event.addAttribute(attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addNamespaces(StartElementEvent event, XMLStreamReader streamReader)
/*     */   {
/* 208 */     Namespace namespace = null;
/* 209 */     for (int i = 0; i < streamReader.getNamespaceCount(); i++) {
/* 210 */       namespace = this.factory.createNamespace(streamReader.getNamespacePrefix(i), streamReader.getNamespaceURI(i));
/*     */ 
/* 212 */       event.addNamespace(namespace);
/*     */     }
/*     */   }
/*     */ 
/* 216 */   protected void addNamespaces(EndElementEvent event, XMLStreamReader streamReader) { Namespace namespace = null;
/* 217 */     for (int i = 0; i < streamReader.getNamespaceCount(); i++) {
/* 218 */       namespace = this.factory.createNamespace(streamReader.getNamespacePrefix(i), streamReader.getNamespaceURI(i));
/*     */ 
/* 220 */       event.addNamespace(namespace);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.StAXEventAllocatorBase
 * JD-Core Version:    0.6.2
 */