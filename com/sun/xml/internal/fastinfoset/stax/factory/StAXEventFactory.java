/*     */ package com.sun.xml.internal.fastinfoset.stax.factory;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.AttributeBase;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.CharactersEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.CommentEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.DTDEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.EndDocumentEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.EndElementEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.EntityReferenceEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.NamespaceBase;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.ProcessingInstructionEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.StartDocumentEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.StartElementEvent;
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
/*     */ public class StAXEventFactory extends XMLEventFactory
/*     */ {
/*  40 */   Location location = null;
/*     */ 
/*     */   public void setLocation(Location location)
/*     */   {
/*  53 */     this.location = location;
/*     */   }
/*     */ 
/*     */   public Attribute createAttribute(String prefix, String namespaceURI, String localName, String value)
/*     */   {
/*  65 */     AttributeBase attr = new AttributeBase(prefix, namespaceURI, localName, value, null);
/*  66 */     if (this.location != null) attr.setLocation(this.location);
/*  67 */     return attr;
/*     */   }
/*     */ 
/*     */   public Attribute createAttribute(String localName, String value)
/*     */   {
/*  77 */     AttributeBase attr = new AttributeBase(localName, value);
/*  78 */     if (this.location != null) attr.setLocation(this.location);
/*  79 */     return attr;
/*     */   }
/*     */ 
/*     */   public Attribute createAttribute(QName name, String value) {
/*  83 */     AttributeBase attr = new AttributeBase(name, value);
/*  84 */     if (this.location != null) attr.setLocation(this.location);
/*  85 */     return attr;
/*     */   }
/*     */ 
/*     */   public Namespace createNamespace(String namespaceURI)
/*     */   {
/*  94 */     NamespaceBase event = new NamespaceBase(namespaceURI);
/*  95 */     if (this.location != null) event.setLocation(this.location);
/*  96 */     return event;
/*     */   }
/*     */ 
/*     */   public Namespace createNamespace(String prefix, String namespaceURI)
/*     */   {
/* 106 */     NamespaceBase event = new NamespaceBase(prefix, namespaceURI);
/* 107 */     if (this.location != null) event.setLocation(this.location);
/* 108 */     return event;
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(QName name, Iterator attributes, Iterator namespaces)
/*     */   {
/* 121 */     return createStartElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attributes, namespaces);
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(String prefix, String namespaceUri, String localName) {
/* 125 */     StartElementEvent event = new StartElementEvent(prefix, namespaceUri, localName);
/* 126 */     if (this.location != null) event.setLocation(this.location);
/* 127 */     return event;
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces) {
/* 131 */     return createStartElement(prefix, namespaceUri, localName, attributes, namespaces, null);
/*     */   }
/*     */ 
/*     */   public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces, NamespaceContext context) {
/* 135 */     StartElementEvent elem = new StartElementEvent(prefix, namespaceUri, localName);
/* 136 */     elem.addAttributes(attributes);
/* 137 */     elem.addNamespaces(namespaces);
/* 138 */     elem.setNamespaceContext(context);
/* 139 */     if (this.location != null) elem.setLocation(this.location);
/* 140 */     return elem;
/*     */   }
/*     */ 
/*     */   public EndElement createEndElement(QName name, Iterator namespaces)
/*     */   {
/* 151 */     return createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), namespaces);
/*     */   }
/*     */ 
/*     */   public EndElement createEndElement(String prefix, String namespaceUri, String localName)
/*     */   {
/* 162 */     EndElementEvent event = new EndElementEvent(prefix, namespaceUri, localName);
/* 163 */     if (this.location != null) event.setLocation(this.location);
/* 164 */     return event;
/*     */   }
/*     */ 
/*     */   public EndElement createEndElement(String prefix, String namespaceUri, String localName, Iterator namespaces)
/*     */   {
/* 178 */     EndElementEvent event = new EndElementEvent(prefix, namespaceUri, localName);
/* 179 */     if (namespaces != null) {
/* 180 */       while (namespaces.hasNext())
/* 181 */         event.addNamespace((Namespace)namespaces.next());
/*     */     }
/* 183 */     if (this.location != null) event.setLocation(this.location);
/* 184 */     return event;
/*     */   }
/*     */ 
/*     */   public Characters createCharacters(String content)
/*     */   {
/* 194 */     CharactersEvent charEvent = new CharactersEvent(content);
/* 195 */     if (this.location != null) charEvent.setLocation(this.location);
/* 196 */     return charEvent;
/*     */   }
/*     */ 
/*     */   public Characters createCData(String content)
/*     */   {
/* 205 */     CharactersEvent charEvent = new CharactersEvent(content, true);
/* 206 */     if (this.location != null) charEvent.setLocation(this.location);
/* 207 */     return charEvent;
/*     */   }
/*     */ 
/*     */   public Characters createSpace(String content)
/*     */   {
/* 216 */     CharactersEvent event = new CharactersEvent(content);
/* 217 */     event.setSpace(true);
/* 218 */     if (this.location != null) event.setLocation(this.location);
/* 219 */     return event;
/*     */   }
/*     */ 
/*     */   public Characters createIgnorableSpace(String content)
/*     */   {
/* 227 */     CharactersEvent event = new CharactersEvent(content, false);
/* 228 */     event.setSpace(true);
/* 229 */     event.setIgnorable(true);
/* 230 */     if (this.location != null) event.setLocation(this.location);
/* 231 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument()
/*     */   {
/* 238 */     StartDocumentEvent event = new StartDocumentEvent();
/* 239 */     if (this.location != null) event.setLocation(this.location);
/* 240 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument(String encoding)
/*     */   {
/* 250 */     StartDocumentEvent event = new StartDocumentEvent(encoding);
/* 251 */     if (this.location != null) event.setLocation(this.location);
/* 252 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument(String encoding, String version)
/*     */   {
/* 263 */     StartDocumentEvent event = new StartDocumentEvent(encoding, version);
/* 264 */     if (this.location != null) event.setLocation(this.location);
/* 265 */     return event;
/*     */   }
/*     */ 
/*     */   public StartDocument createStartDocument(String encoding, String version, boolean standalone)
/*     */   {
/* 277 */     StartDocumentEvent event = new StartDocumentEvent(encoding, version);
/* 278 */     event.setStandalone(standalone);
/* 279 */     if (this.location != null) event.setLocation(this.location);
/* 280 */     return event;
/*     */   }
/*     */ 
/*     */   public EndDocument createEndDocument() {
/* 284 */     EndDocumentEvent event = new EndDocumentEvent();
/* 285 */     if (this.location != null) event.setLocation(this.location);
/* 286 */     return event;
/*     */   }
/*     */ 
/*     */   public EntityReference createEntityReference(String name, EntityDeclaration entityDeclaration)
/*     */   {
/* 296 */     EntityReferenceEvent event = new EntityReferenceEvent(name, entityDeclaration);
/* 297 */     if (this.location != null) event.setLocation(this.location);
/* 298 */     return event;
/*     */   }
/*     */ 
/*     */   public Comment createComment(String text)
/*     */   {
/* 307 */     CommentEvent charEvent = new CommentEvent(text);
/* 308 */     if (this.location != null) charEvent.setLocation(this.location);
/* 309 */     return charEvent;
/*     */   }
/*     */ 
/*     */   public DTD createDTD(String dtd)
/*     */   {
/* 320 */     DTDEvent dtdEvent = new DTDEvent(dtd);
/* 321 */     if (this.location != null) dtdEvent.setLocation(this.location);
/* 322 */     return dtdEvent;
/*     */   }
/*     */ 
/*     */   public ProcessingInstruction createProcessingInstruction(String target, String data)
/*     */   {
/* 333 */     ProcessingInstructionEvent event = new ProcessingInstructionEvent(target, data);
/* 334 */     if (this.location != null) event.setLocation(this.location);
/* 335 */     return event;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.factory.StAXEventFactory
 * JD-Core Version:    0.6.2
 */