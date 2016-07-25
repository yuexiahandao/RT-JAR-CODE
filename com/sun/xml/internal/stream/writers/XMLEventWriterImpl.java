/*     */ package com.sun.xml.internal.stream.writers;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.DTD;
/*     */ import javax.xml.stream.events.EntityReference;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ public class XMLEventWriterImpl
/*     */   implements XMLEventWriter
/*     */ {
/*     */   private XMLStreamWriter fStreamWriter;
/*     */   private static final boolean DEBUG = false;
/*     */ 
/*     */   public XMLEventWriterImpl(XMLStreamWriter streamWriter)
/*     */   {
/*  59 */     this.fStreamWriter = streamWriter;
/*     */   }
/*     */ 
/*     */   public void add(XMLEventReader xMLEventReader)
/*     */     throws XMLStreamException
/*     */   {
/*  68 */     if (xMLEventReader == null) throw new XMLStreamException("Event reader shouldn't be null");
/*  69 */     while (xMLEventReader.hasNext())
/*  70 */       add(xMLEventReader.nextEvent());
/*     */   }
/*     */ 
/*     */   public void add(XMLEvent xMLEvent)
/*     */     throws XMLStreamException
/*     */   {
/*  80 */     int type = xMLEvent.getEventType();
/*     */     Iterator attributes;
/*  81 */     switch (type) {
/*     */     case 11:
/*  83 */       DTD dtd = (DTD)xMLEvent;
/*     */ 
/*  85 */       this.fStreamWriter.writeDTD(dtd.getDocumentTypeDeclaration());
/*  86 */       break;
/*     */     case 7:
/*  89 */       StartDocument startDocument = (StartDocument)xMLEvent;
/*     */       try
/*     */       {
/*  92 */         this.fStreamWriter.writeStartDocument(startDocument.getCharacterEncodingScheme(), startDocument.getVersion());
/*     */       } catch (XMLStreamException e) {
/*  94 */         this.fStreamWriter.writeStartDocument(startDocument.getVersion());
/*     */       }
/*     */ 
/*     */     case 1:
/*  99 */       StartElement startElement = xMLEvent.asStartElement();
/*     */ 
/* 101 */       QName qname = startElement.getName();
/* 102 */       this.fStreamWriter.writeStartElement(qname.getPrefix(), qname.getLocalPart(), qname.getNamespaceURI());
/*     */ 
/* 110 */       Iterator iterator = startElement.getNamespaces();
/* 111 */       while (iterator.hasNext()) {
/* 112 */         Namespace namespace = (Namespace)iterator.next();
/* 113 */         this.fStreamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
/*     */       }
/*     */ 
/* 116 */       attributes = startElement.getAttributes();
/*     */     case 13:
/*     */     case 5:
/*     */     case 3:
/*     */     case 4:
/*     */     case 9:
/*     */     case 10:
/*     */     case 12:
/*     */     case 2:
/*     */     case 8:
/* 117 */       while (attributes.hasNext()) {
/* 118 */         Attribute attribute = (Attribute)attributes.next();
/* 119 */         QName aqname = attribute.getName();
/* 120 */         this.fStreamWriter.writeAttribute(aqname.getPrefix(), aqname.getNamespaceURI(), aqname.getLocalPart(), attribute.getValue());
/* 121 */         continue;
/*     */ 
/* 125 */         Namespace namespace = (Namespace)xMLEvent;
/*     */ 
/* 127 */         this.fStreamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
/* 128 */         break;
/*     */ 
/* 131 */         Comment comment = (Comment)xMLEvent;
/*     */ 
/* 133 */         this.fStreamWriter.writeComment(comment.getText());
/* 134 */         break;
/*     */ 
/* 137 */         ProcessingInstruction processingInstruction = (ProcessingInstruction)xMLEvent;
/*     */ 
/* 139 */         this.fStreamWriter.writeProcessingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
/* 140 */         break;
/*     */ 
/* 143 */         Characters characters = xMLEvent.asCharacters();
/*     */ 
/* 146 */         if (characters.isCData()) {
/* 147 */           this.fStreamWriter.writeCData(characters.getData());
/*     */         }
/*     */         else {
/* 150 */           this.fStreamWriter.writeCharacters(characters.getData());
/*     */ 
/* 152 */           break;
/*     */ 
/* 155 */           EntityReference entityReference = (EntityReference)xMLEvent;
/*     */ 
/* 157 */           this.fStreamWriter.writeEntityRef(entityReference.getName());
/* 158 */           break;
/*     */ 
/* 161 */           Attribute attribute = (Attribute)xMLEvent;
/*     */ 
/* 163 */           QName qname = attribute.getName();
/* 164 */           this.fStreamWriter.writeAttribute(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalPart(), attribute.getValue());
/* 165 */           break;
/*     */ 
/* 170 */           Characters characters = (Characters)xMLEvent;
/*     */ 
/* 172 */           if (characters.isCData()) {
/* 173 */             this.fStreamWriter.writeCData(characters.getData()); break;
/*     */ 
/* 184 */             this.fStreamWriter.writeEndElement();
/* 185 */             break;
/*     */ 
/* 190 */             this.fStreamWriter.writeEndDocument();
/*     */           }
/*     */         }
/*     */       }
/*     */     case 6:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws XMLStreamException
/*     */   {
/* 203 */     this.fStreamWriter.close();
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws XMLStreamException
/*     */   {
/* 212 */     this.fStreamWriter.flush();
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/* 220 */     return this.fStreamWriter.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public String getPrefix(String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 230 */     return this.fStreamWriter.getPrefix(namespaceURI);
/*     */   }
/*     */ 
/*     */   public void setDefaultNamespace(String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 239 */     this.fStreamWriter.setDefaultNamespace(uri);
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext namespaceContext)
/*     */     throws XMLStreamException
/*     */   {
/* 248 */     this.fStreamWriter.setNamespaceContext(namespaceContext);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix, String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 258 */     this.fStreamWriter.setPrefix(prefix, uri);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.writers.XMLEventWriterImpl
 * JD-Core Version:    0.6.2
 */