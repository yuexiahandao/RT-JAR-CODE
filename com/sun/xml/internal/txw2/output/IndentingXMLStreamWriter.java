/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class IndentingXMLStreamWriter extends DelegatingXMLStreamWriter
/*     */ {
/*  36 */   private static final Object SEEN_NOTHING = new Object();
/*  37 */   private static final Object SEEN_ELEMENT = new Object();
/*  38 */   private static final Object SEEN_DATA = new Object();
/*     */ 
/*  40 */   private Object state = SEEN_NOTHING;
/*  41 */   private Stack<Object> stateStack = new Stack();
/*     */ 
/*  43 */   private String indentStep = "  ";
/*  44 */   private int depth = 0;
/*     */ 
/*     */   public IndentingXMLStreamWriter(XMLStreamWriter writer) {
/*  47 */     super(writer);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getIndentStep()
/*     */   {
/*  65 */     return this.indentStep.length();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setIndentStep(int indentStep)
/*     */   {
/*  80 */     StringBuilder s = new StringBuilder();
/*  81 */     for (; indentStep > 0; indentStep--) s.append(' ');
/*  82 */     setIndentStep(s.toString());
/*     */   }
/*     */ 
/*     */   public void setIndentStep(String s) {
/*  86 */     this.indentStep = s;
/*     */   }
/*     */ 
/*     */   private void onStartElement() throws XMLStreamException {
/*  90 */     this.stateStack.push(SEEN_ELEMENT);
/*  91 */     this.state = SEEN_NOTHING;
/*  92 */     if (this.depth > 0) {
/*  93 */       super.writeCharacters("\n");
/*     */     }
/*  95 */     doIndent();
/*  96 */     this.depth += 1;
/*     */   }
/*     */ 
/*     */   private void onEndElement() throws XMLStreamException {
/* 100 */     this.depth -= 1;
/* 101 */     if (this.state == SEEN_ELEMENT) {
/* 102 */       super.writeCharacters("\n");
/* 103 */       doIndent();
/*     */     }
/* 105 */     this.state = this.stateStack.pop();
/*     */   }
/*     */ 
/*     */   private void onEmptyElement() throws XMLStreamException {
/* 109 */     this.state = SEEN_ELEMENT;
/* 110 */     if (this.depth > 0) {
/* 111 */       super.writeCharacters("\n");
/*     */     }
/* 113 */     doIndent();
/*     */   }
/*     */ 
/*     */   private void doIndent()
/*     */     throws XMLStreamException
/*     */   {
/* 124 */     if (this.depth > 0)
/* 125 */       for (int i = 0; i < this.depth; i++)
/* 126 */         super.writeCharacters(this.indentStep);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument()
/*     */     throws XMLStreamException
/*     */   {
/* 132 */     super.writeStartDocument();
/* 133 */     super.writeCharacters("\n");
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/* 137 */     super.writeStartDocument(version);
/* 138 */     super.writeCharacters("\n");
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 142 */     super.writeStartDocument(encoding, version);
/* 143 */     super.writeCharacters("\n");
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/* 147 */     onStartElement();
/* 148 */     super.writeStartElement(localName);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/* 152 */     onStartElement();
/* 153 */     super.writeStartElement(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 157 */     onStartElement();
/* 158 */     super.writeStartElement(prefix, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/* 162 */     onEmptyElement();
/* 163 */     super.writeEmptyElement(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 167 */     onEmptyElement();
/* 168 */     super.writeEmptyElement(prefix, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/* 172 */     onEmptyElement();
/* 173 */     super.writeEmptyElement(localName);
/*     */   }
/*     */ 
/*     */   public void writeEndElement() throws XMLStreamException {
/* 177 */     onEndElement();
/* 178 */     super.writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/* 182 */     this.state = SEEN_DATA;
/* 183 */     super.writeCharacters(text);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
/* 187 */     this.state = SEEN_DATA;
/* 188 */     super.writeCharacters(text, start, len);
/*     */   }
/*     */ 
/*     */   public void writeCData(String data) throws XMLStreamException {
/* 192 */     this.state = SEEN_DATA;
/* 193 */     super.writeCData(data);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter
 * JD-Core Version:    0.6.2
 */