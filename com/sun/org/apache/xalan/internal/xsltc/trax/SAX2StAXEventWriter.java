/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ 
/*     */ public class SAX2StAXEventWriter extends SAX2StAXBaseWriter
/*     */ {
/*     */   private XMLEventWriter writer;
/*     */   private XMLEventFactory eventFactory;
/*  57 */   private List namespaceStack = new ArrayList();
/*     */ 
/*  60 */   private boolean needToCallStartDocument = false;
/*     */ 
/*     */   public SAX2StAXEventWriter()
/*     */   {
/*  65 */     this.eventFactory = XMLEventFactory.newInstance();
/*     */   }
/*     */ 
/*     */   public SAX2StAXEventWriter(XMLEventWriter writer)
/*     */   {
/*  72 */     this.writer = writer;
/*  73 */     this.eventFactory = XMLEventFactory.newInstance();
/*     */   }
/*     */ 
/*     */   public SAX2StAXEventWriter(XMLEventWriter writer, XMLEventFactory factory)
/*     */   {
/*  80 */     this.writer = writer;
/*  81 */     if (factory != null)
/*     */     {
/*  83 */       this.eventFactory = factory;
/*     */     }
/*     */     else
/*     */     {
/*  87 */       this.eventFactory = XMLEventFactory.newInstance();
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLEventWriter getEventWriter()
/*     */   {
/*  95 */     return this.writer;
/*     */   }
/*     */ 
/*     */   public void setEventWriter(XMLEventWriter writer)
/*     */   {
/* 102 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */   public XMLEventFactory getEventFactory()
/*     */   {
/* 109 */     return this.eventFactory;
/*     */   }
/*     */ 
/*     */   public void setEventFactory(XMLEventFactory factory)
/*     */   {
/* 116 */     this.eventFactory = factory;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 122 */     super.startDocument();
/*     */ 
/* 124 */     this.namespaceStack.clear();
/*     */ 
/* 126 */     this.eventFactory.setLocation(getCurrentLocation());
/*     */ 
/* 131 */     this.needToCallStartDocument = true;
/*     */   }
/*     */ 
/*     */   private void writeStartDocument() throws SAXException {
/*     */     try {
/* 136 */       if (this.docLocator == null)
/* 137 */         this.writer.add(this.eventFactory.createStartDocument());
/*     */       else
/*     */         try {
/* 140 */           this.writer.add(this.eventFactory.createStartDocument(((Locator2)this.docLocator).getEncoding(), ((Locator2)this.docLocator).getXMLVersion()));
/*     */         } catch (ClassCastException e) {
/* 142 */           this.writer.add(this.eventFactory.createStartDocument());
/*     */         }
/*     */     }
/*     */     catch (XMLStreamException e) {
/* 146 */       throw new SAXException(e);
/*     */     }
/* 148 */     this.needToCallStartDocument = false;
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException
/*     */   {
/* 153 */     this.eventFactory.setLocation(getCurrentLocation());
/*     */     try
/*     */     {
/* 157 */       this.writer.add(this.eventFactory.createEndDocument());
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 161 */       throw new SAXException(e);
/*     */     }
/*     */ 
/* 165 */     super.endDocument();
/*     */ 
/* 168 */     this.namespaceStack.clear();
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */     throws SAXException
/*     */   {
/* 174 */     if (this.needToCallStartDocument) {
/* 175 */       writeStartDocument();
/*     */     }
/*     */ 
/* 179 */     this.eventFactory.setLocation(getCurrentLocation());
/*     */ 
/* 182 */     Collection[] events = { null, null };
/* 183 */     createStartEvents(attributes, events);
/*     */ 
/* 185 */     this.namespaceStack.add(events[0]);
/*     */     try
/*     */     {
/* 189 */       String[] qname = { null, null };
/* 190 */       parseQName(qName, qname);
/*     */ 
/* 192 */       this.writer.add(this.eventFactory.createStartElement(qname[0], uri, qname[1], events[1].iterator(), events[0].iterator()));
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 197 */       throw new SAXException(e);
/*     */     }
/*     */     finally
/*     */     {
/* 201 */       super.startElement(uri, localName, qName, attributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 210 */     super.endElement(uri, localName, qName);
/*     */ 
/* 212 */     this.eventFactory.setLocation(getCurrentLocation());
/*     */ 
/* 215 */     String[] qname = { null, null };
/* 216 */     parseQName(qName, qname);
/*     */ 
/* 219 */     Collection nsList = (Collection)this.namespaceStack.remove(this.namespaceStack.size() - 1);
/* 220 */     Iterator nsIter = nsList.iterator();
/*     */     try
/*     */     {
/* 224 */       this.writer.add(this.eventFactory.createEndElement(qname[0], uri, qname[1], nsIter));
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 229 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 236 */     if (this.needToCallStartDocument)
/*     */     {
/* 240 */       writeStartDocument();
/*     */     }
/*     */ 
/* 243 */     super.comment(ch, start, length);
/*     */ 
/* 245 */     this.eventFactory.setLocation(getCurrentLocation());
/*     */     try
/*     */     {
/* 248 */       this.writer.add(this.eventFactory.createComment(new String(ch, start, length)));
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 253 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 262 */     super.characters(ch, start, length);
/*     */     try
/*     */     {
/* 266 */       if (!this.isCDATA)
/*     */       {
/* 268 */         this.eventFactory.setLocation(getCurrentLocation());
/* 269 */         this.writer.add(this.eventFactory.createCharacters(new String(ch, start, length)));
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 276 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 285 */     super.ignorableWhitespace(ch, start, length);
/* 286 */     characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 293 */     if (this.needToCallStartDocument)
/*     */     {
/* 297 */       writeStartDocument();
/*     */     }
/*     */ 
/* 300 */     super.processingInstruction(target, data);
/*     */     try
/*     */     {
/* 303 */       this.writer.add(this.eventFactory.createProcessingInstruction(target, data));
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 307 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endCDATA()
/*     */     throws SAXException
/*     */   {
/* 315 */     this.eventFactory.setLocation(getCurrentLocation());
/*     */     try
/*     */     {
/* 318 */       this.writer.add(this.eventFactory.createCData(this.CDATABuffer.toString()));
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 322 */       throw new SAXException(e);
/*     */     }
/*     */ 
/* 326 */     super.endCDATA();
/*     */   }
/*     */ 
/*     */   protected void createStartEvents(Attributes attributes, Collection[] events)
/*     */   {
/* 333 */     Map nsMap = null;
/* 334 */     List attrs = null;
/*     */ 
/* 337 */     if (this.namespaces != null) {
/* 338 */       int nDecls = this.namespaces.size();
/* 339 */       for (int i = 0; i < nDecls; i++) {
/* 340 */         String prefix = (String)this.namespaces.elementAt(i++);
/* 341 */         String uri = (String)this.namespaces.elementAt(i);
/* 342 */         Namespace ns = createNamespace(prefix, uri);
/* 343 */         if (nsMap == null) {
/* 344 */           nsMap = new HashMap();
/*     */         }
/* 346 */         nsMap.put(prefix, ns);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 351 */     String[] qname = { null, null };
/* 352 */     int i = 0; for (int s = attributes.getLength(); i < s; i++)
/*     */     {
/* 354 */       parseQName(attributes.getQName(i), qname);
/*     */ 
/* 356 */       String attrPrefix = qname[0];
/* 357 */       String attrLocal = qname[1];
/*     */ 
/* 359 */       String attrQName = attributes.getQName(i);
/* 360 */       String attrValue = attributes.getValue(i);
/* 361 */       String attrURI = attributes.getURI(i);
/*     */ 
/* 363 */       if (("xmlns".equals(attrQName)) || ("xmlns".equals(attrPrefix)))
/*     */       {
/* 367 */         if (nsMap == null) {
/* 368 */           nsMap = new HashMap();
/*     */         }
/*     */ 
/* 371 */         if (!nsMap.containsKey(attrLocal)) {
/* 372 */           Namespace ns = createNamespace(attrLocal, attrValue);
/* 373 */           nsMap.put(attrLocal, ns);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         Attribute attribute;
/*     */         Attribute attribute;
/* 379 */         if (attrPrefix.length() > 0)
/*     */         {
/* 381 */           attribute = this.eventFactory.createAttribute(attrPrefix, attrURI, attrLocal, attrValue);
/*     */         }
/*     */         else
/*     */         {
/* 386 */           attribute = this.eventFactory.createAttribute(attrLocal, attrValue);
/*     */         }
/*     */ 
/* 391 */         if (attrs == null)
/*     */         {
/* 393 */           attrs = new ArrayList();
/*     */         }
/*     */ 
/* 396 */         attrs.add(attribute);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 401 */     events[0] = (nsMap == null ? Collections.EMPTY_LIST : nsMap.values());
/* 402 */     events[1] = (attrs == null ? Collections.EMPTY_LIST : attrs);
/*     */   }
/*     */ 
/*     */   protected Namespace createNamespace(String prefix, String uri)
/*     */   {
/* 408 */     if ((prefix == null) || (prefix.length() == 0))
/*     */     {
/* 410 */       return this.eventFactory.createNamespace(uri);
/*     */     }
/*     */ 
/* 414 */     return this.eventFactory.createNamespace(prefix, uri);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXEventWriter
 * JD-Core Version:    0.6.2
 */