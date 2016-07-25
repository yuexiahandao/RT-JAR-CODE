/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XMLEventWriterOutput extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XMLEventWriter out;
/*     */   private final XMLEventFactory ef;
/*     */   private final Characters sp;
/*     */ 
/*     */   public XMLEventWriterOutput(XMLEventWriter out)
/*     */   {
/*  53 */     this.out = out;
/*  54 */     this.ef = XMLEventFactory.newInstance();
/*  55 */     this.sp = this.ef.createCharacters(" ");
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/*  61 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  62 */     if (!fragment)
/*  63 */       this.out.add(this.ef.createStartDocument());
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/*  67 */     if (!fragment) {
/*  68 */       this.out.add(this.ef.createEndDocument());
/*  69 */       this.out.flush();
/*     */     }
/*  71 */     super.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/*  75 */     this.out.add(this.ef.createStartElement(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName));
/*     */ 
/*  81 */     NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/*  82 */     if (nse.count() > 0)
/*  83 */       for (int i = nse.count() - 1; i >= 0; i--) {
/*  84 */         String uri = nse.getNsUri(i);
/*  85 */         if ((uri.length() != 0) || (nse.getBase() != 1))
/*     */         {
/*  87 */           this.out.add(this.ef.createNamespace(nse.getPrefix(i), uri));
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void attribute(int prefix, String localName, String value)
/*     */     throws IOException, XMLStreamException
/*     */   {
/*     */     Attribute att;
/*     */     Attribute att;
/*  94 */     if (prefix == -1)
/*  95 */       att = this.ef.createAttribute(localName, value);
/*     */     else {
/*  97 */       att = this.ef.createAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName, value);
/*     */     }
/*     */ 
/* 102 */     this.out.add(att);
/*     */   }
/*     */ 
/*     */   public void endStartTag() throws IOException, SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName) throws IOException, SAXException, XMLStreamException {
/* 110 */     this.out.add(this.ef.createEndElement(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName));
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needsSeparatingWhitespace)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 118 */     if (needsSeparatingWhitespace)
/* 119 */       this.out.add(this.sp);
/* 120 */     this.out.add(this.ef.createCharacters(value));
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 124 */     text(value.toString(), needsSeparatingWhitespace);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.XMLEventWriterOutput
 * JD-Core Version:    0.6.2
 */