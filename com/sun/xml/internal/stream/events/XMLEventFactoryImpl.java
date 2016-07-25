/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.DTD;
/*     */ import javax.xml.stream.events.EndDocument;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.EntityDeclaration;
/*     */ import javax.xml.stream.events.EntityReference;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ 
/*     */ public class XMLEventFactoryImpl extends XMLEventFactory
/*     */ {
/*  40 */   Location location = null;
/*     */ 
/*     */   public Attribute createAttribute(String localName, String value)
/*     */   {
/*  46 */     AttributeImpl attr = new AttributeImpl(localName, value);
/*  47 */     if (this.location != null) attr.setLocation(this.location);
/*  48 */     return attr;
/*     */   }
/*     */ 
/*     */   public Attribute createAttribute(QName name, String value) {
/*  52 */     return createAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), value);
/*     */   }
/*     */ 
/*     */   public Attribute createAttribute(String prefix, String namespaceURI, String localName, String value) {
/*  56 */     AttributeImpl attr = new AttributeImpl(prefix, namespaceURI, localName, value, null);
/*  57 */     if (this.location != null) attr.setLocation(this.location);
/*  58 */     return attr;
/*     */   }
/*     */ 
/*     */   public Characters createCData(String content)
/*     */   {
/*  64 */     CharacterEvent charEvent = new CharacterEvent(content, true);
/*  65 */     if (this.location != null) charEvent.setLocation(this.location);
/*  66 */     return charEvent;
/*     */   }
/*     */ 
/*     */   public Characters createCharacters(String content) {
/*  70 */     CharacterEvent charEvent = new CharacterEvent(content);
/*  71 */     if (this.location != null) charEvent.setLocation(this.location);
/*  72 */     return charEvent;
/*     */   }
/*     */ 
/*     */   public Comment createComment(String text) {
/*  76 */     CommentEvent charEvent = new CommentEvent(text);
/*  77 */     if (this.location != null) charEvent.setLocation(this.location);
/*  78 */     return charEvent;
/*     */   }
/*     */ 
/*     */   public DTD createDTD(String dtd) {
/*  82 */     DTDEvent dtdEvent = new DTDEvent(dtd);
/*  83 */     if (this.location != null) dtdEvent.setLocation(this.location);
/*  84 */     return dtdEvent;
/*     */   }
/*     */ 
/*     */   public EndDocument createEndDocument() {
/*  88 */     EndDocumentEvent event = new EndDocumentEvent();
/*  89 */     if (this.location != null) event.setLocation(this.location);
/*  90 */     return event;
/*     */   }
/*     */ 
/*     */   public EndElement createEndElement(QName name, Iterator namespaces) {
/*  94 */     return createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */   public EndElement createEndElement(String prefix, String namespaceUri, String localName) {
/*  98 */     EndElementEvent event = new EndElementEvent(prefix, namespaceUri, localName);
/*  99 */     if (this.location != null) event.setLocation(this.location);
/* 100 */     return event;
/*     */   }
/*     */ 
/*     */   public EndElement createEndElement(String prefix, String namespaceUri, String localName, Iterator namespaces)
/*     */   {
/* 105 */     EndElementEvent event = new EndElementEvent(prefix, namespaceUri, localName);
/* 106 */     if (namespaces != null) {
/* 107 */       while (namespaces.hasNext())
/* 108 */         event.addNamespace((Namespace)namespaces.next());
/*     */     }
/* 110 */     if (this.location != null) event.setLocation(this.location);
/* 111 */     return event;
/*     */   }
/*     */ 
/*     */   public EntityReference createEntityReference(String name, EntityDeclaration entityDeclaration) {
/* 115 */     EntityReferenceEvent event = new EntityReferenceEvent(name, entityDeclaration);
/* 116 */     if (this.location != null) event.setLocation(this.location);
/* 117 */     return event;
/*     */   }
/*     */ 
/*     */   public Characters createIgnorableSpace(String content) {
/* 121 */     CharacterEvent event = new CharacterEvent(content, false, true);
/* 122 */     if (this.location != null) event.setLocation(this.location);
/* 123 */     return event;
/*     */   }
/*     */ 
/*     */   public Namespace createNamespace(String namespaceURI) {
/* 127 */     NamespaceImpl event = new NamespaceImpl(namespaceURI);
/* 128 */     if (this.location != null) event.setLocation(this.location);
/* 129 */     return event;
/*     */   }
/*     */ 
/*     */   public Namespace createNamespace(String prefix, String namespaceURI) {
/* 133 */     NamespaceImpl event = new NamespaceImpl(prefix, namespaceURI);
/* 134 */     if (this.location != null) event.setLocation(this.location);
/* 135 */     return event;
/*     */   }
/*     */ 
/*     */   public ProcessingInstruction createProcessingInstruction(String target, String data) {
/* 139 */     ProcessingInstructionEvent event = new ProcessingInstructionEvent(target, data);
/* 140 */     if (this.location != null) event.setLocation(this.location);
/* 141 */     return event;
/*     */   }
/*     */ 
/*     */   public Characters createSpace(String content) {
/* 145 */     CharacterEvent event = new CharacterEvent(content);
/* 146 */     if (this.location != null) event.setLocation(this.location);
/* 147 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument() {
/* 151 */     StartDocumentEvent event = new StartDocumentEvent();
/* 152 */     if (this.location != null) event.setLocation(this.location);
/* 153 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument(String encoding) {
/* 157 */     StartDocumentEvent event = new StartDocumentEvent(encoding);
/* 158 */     if (this.location != null) event.setLocation(this.location);
/* 159 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument(String encoding, String version) {
/* 163 */     StartDocumentEvent event = new StartDocumentEvent(encoding, version);
/* 164 */     if (this.location != null) event.setLocation(this.location);
/* 165 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument(String encoding, String version, boolean standalone) {
/* 169 */     StartDocumentEvent event = new StartDocumentEvent(encoding, version, standalone);
/* 170 */     if (this.location != null) event.setLocation(this.location);
/* 171 */     return event;
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(QName name, Iterator attributes, Iterator namespaces) {
/* 175 */     return createStartElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attributes, namespaces);
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(String prefix, String namespaceUri, String localName) {
/* 179 */     StartElementEvent event = new StartElementEvent(prefix, namespaceUri, localName);
/* 180 */     if (this.location != null) event.setLocation(this.location);
/* 181 */     return event;
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces) {
/* 185 */     return createStartElement(prefix, namespaceUri, localName, attributes, namespaces, null);
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces, NamespaceContext context) {
/* 189 */     StartElementEvent elem = new StartElementEvent(prefix, namespaceUri, localName);
/* 190 */     elem.addAttributes(attributes);
/* 191 */     elem.addNamespaceAttributes(namespaces);
/* 192 */     elem.setNamespaceContext(context);
/* 193 */     if (this.location != null) elem.setLocation(this.location);
/* 194 */     return elem;
/*     */   }
/*     */ 
/*     */   public void setLocation(Location location) {
/* 198 */     this.location = location;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.XMLEventFactoryImpl
 * JD-Core Version:    0.6.2
 */