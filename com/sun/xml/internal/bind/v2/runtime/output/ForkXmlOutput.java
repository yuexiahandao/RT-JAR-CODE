/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class ForkXmlOutput extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XmlOutput lhs;
/*     */   private final XmlOutput rhs;
/*     */ 
/*     */   public ForkXmlOutput(XmlOutput lhs, XmlOutput rhs)
/*     */   {
/*  46 */     this.lhs = lhs;
/*  47 */     this.rhs = rhs;
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException
/*     */   {
/*  52 */     this.lhs.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  53 */     this.rhs.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException
/*     */   {
/*  58 */     this.lhs.endDocument(fragment);
/*  59 */     this.rhs.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(Name name) throws IOException, XMLStreamException
/*     */   {
/*  64 */     this.lhs.beginStartTag(name);
/*  65 */     this.rhs.beginStartTag(name);
/*     */   }
/*     */ 
/*     */   public void attribute(Name name, String value) throws IOException, XMLStreamException
/*     */   {
/*  70 */     this.lhs.attribute(name, value);
/*  71 */     this.rhs.attribute(name, value);
/*     */   }
/*     */ 
/*     */   public void endTag(Name name) throws IOException, SAXException, XMLStreamException
/*     */   {
/*  76 */     this.lhs.endTag(name);
/*  77 */     this.rhs.endTag(name);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/*  81 */     this.lhs.beginStartTag(prefix, localName);
/*  82 */     this.rhs.beginStartTag(prefix, localName);
/*     */   }
/*     */ 
/*     */   public void attribute(int prefix, String localName, String value) throws IOException, XMLStreamException {
/*  86 */     this.lhs.attribute(prefix, localName, value);
/*  87 */     this.rhs.attribute(prefix, localName, value);
/*     */   }
/*     */ 
/*     */   public void endStartTag() throws IOException, SAXException {
/*  91 */     this.lhs.endStartTag();
/*  92 */     this.rhs.endStartTag();
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName) throws IOException, SAXException, XMLStreamException {
/*  96 */     this.lhs.endTag(prefix, localName);
/*  97 */     this.rhs.endTag(prefix, localName);
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 101 */     this.lhs.text(value, needsSeparatingWhitespace);
/* 102 */     this.rhs.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 106 */     this.lhs.text(value, needsSeparatingWhitespace);
/* 107 */     this.rhs.text(value, needsSeparatingWhitespace);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.ForkXmlOutput
 * JD-Core Version:    0.6.2
 */