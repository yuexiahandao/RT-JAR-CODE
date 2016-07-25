/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
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
/*     */ public class StAXEventWriter
/*     */   implements XMLEventWriter
/*     */ {
/*     */   private XMLStreamWriter _streamWriter;
/*     */ 
/*     */   public StAXEventWriter(XMLStreamWriter streamWriter)
/*     */   {
/*  48 */     this._streamWriter = streamWriter;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws XMLStreamException
/*     */   {
/*  56 */     this._streamWriter.flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws XMLStreamException
/*     */   {
/*  63 */     this._streamWriter.close();
/*     */   }
/*     */ 
/*     */   public void add(XMLEventReader eventReader)
/*     */     throws XMLStreamException
/*     */   {
/*  72 */     if (eventReader == null) throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.nullEventReader"));
/*  73 */     while (eventReader.hasNext())
/*  74 */       add(eventReader.nextEvent());
/*     */   }
/*     */ 
/*     */   public void add(XMLEvent event)
/*     */     throws XMLStreamException
/*     */   {
/*  87 */     int type = event.getEventType();
/*     */     Iterator attributes;
/*  88 */     switch (type) {
/*     */     case 11:
/*  90 */       DTD dtd = (DTD)event;
/*  91 */       this._streamWriter.writeDTD(dtd.getDocumentTypeDeclaration());
/*  92 */       break;
/*     */     case 7:
/*  95 */       StartDocument startDocument = (StartDocument)event;
/*  96 */       this._streamWriter.writeStartDocument(startDocument.getCharacterEncodingScheme(), startDocument.getVersion());
/*  97 */       break;
/*     */     case 1:
/* 100 */       StartElement startElement = event.asStartElement();
/* 101 */       QName qname = startElement.getName();
/* 102 */       this._streamWriter.writeStartElement(qname.getPrefix(), qname.getLocalPart(), qname.getNamespaceURI());
/*     */ 
/* 104 */       Iterator iterator = startElement.getNamespaces();
/* 105 */       while (iterator.hasNext()) {
/* 106 */         Namespace namespace = (Namespace)iterator.next();
/* 107 */         this._streamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
/*     */       }
/*     */ 
/* 110 */       attributes = startElement.getAttributes();
/*     */     case 13:
/*     */     case 5:
/*     */     case 3:
/*     */     case 4:
/*     */     case 9:
/*     */     case 10:
/*     */     case 12:
/*     */     case 2:
/*     */     case 8:
/*     */     case 6:
/*     */     default:
/* 111 */       while (attributes.hasNext()) {
/* 112 */         Attribute attribute = (Attribute)attributes.next();
/* 113 */         QName name = attribute.getName();
/* 114 */         this._streamWriter.writeAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attribute.getValue());
/*     */ 
/* 116 */         continue;
/*     */ 
/* 120 */         Namespace namespace = (Namespace)event;
/* 121 */         this._streamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
/* 122 */         break;
/*     */ 
/* 125 */         Comment comment = (Comment)event;
/* 126 */         this._streamWriter.writeComment(comment.getText());
/* 127 */         break;
/*     */ 
/* 130 */         ProcessingInstruction processingInstruction = (ProcessingInstruction)event;
/* 131 */         this._streamWriter.writeProcessingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
/* 132 */         break;
/*     */ 
/* 135 */         Characters characters = event.asCharacters();
/*     */ 
/* 137 */         if (characters.isCData()) {
/* 138 */           this._streamWriter.writeCData(characters.getData());
/*     */         }
/*     */         else {
/* 141 */           this._streamWriter.writeCharacters(characters.getData());
/*     */ 
/* 143 */           break;
/*     */ 
/* 146 */           EntityReference entityReference = (EntityReference)event;
/* 147 */           this._streamWriter.writeEntityRef(entityReference.getName());
/* 148 */           break;
/*     */ 
/* 151 */           Attribute attribute = (Attribute)event;
/* 152 */           QName qname = attribute.getName();
/* 153 */           this._streamWriter.writeAttribute(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalPart(), attribute.getValue());
/* 154 */           break;
/*     */ 
/* 159 */           Characters characters = (Characters)event;
/* 160 */           if (characters.isCData()) {
/* 161 */             this._streamWriter.writeCData(characters.getData()); break;
/*     */ 
/* 167 */             this._streamWriter.writeEndElement();
/* 168 */             break;
/*     */ 
/* 171 */             this._streamWriter.writeEndDocument();
/* 172 */             break;
/*     */ 
/* 175 */             throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.eventTypeNotSupported", new Object[] { Util.getEventTypeString(type) }));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 187 */     return this._streamWriter.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/* 196 */     return this._streamWriter.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public void setDefaultNamespace(String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 210 */     this._streamWriter.setDefaultNamespace(uri);
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext namespaceContext)
/*     */     throws XMLStreamException
/*     */   {
/* 224 */     this._streamWriter.setNamespaceContext(namespaceContext);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix, String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 236 */     this._streamWriter.setPrefix(prefix, uri);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.StAXEventWriter
 * JD-Core Version:    0.6.2
 */