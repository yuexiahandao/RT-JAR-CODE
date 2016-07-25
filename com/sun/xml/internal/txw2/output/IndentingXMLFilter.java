/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class IndentingXMLFilter extends XMLFilterImpl
/*     */   implements LexicalHandler
/*     */ {
/*     */   private LexicalHandler lexical;
/* 148 */   private static final char[] NEWLINE = { '\n' };
/*     */ 
/* 298 */   private static final Object SEEN_NOTHING = new Object();
/* 299 */   private static final Object SEEN_ELEMENT = new Object();
/* 300 */   private static final Object SEEN_DATA = new Object();
/*     */ 
/* 307 */   private Object state = SEEN_NOTHING;
/* 308 */   private Stack<Object> stateStack = new Stack();
/*     */ 
/* 310 */   private String indentStep = "";
/* 311 */   private int depth = 0;
/*     */ 
/*     */   public IndentingXMLFilter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public IndentingXMLFilter(ContentHandler handler)
/*     */   {
/*  48 */     setContentHandler(handler);
/*     */   }
/*     */ 
/*     */   public IndentingXMLFilter(ContentHandler handler, LexicalHandler lexical) {
/*  52 */     setContentHandler(handler);
/*  53 */     setLexicalHandler(lexical);
/*     */   }
/*     */ 
/*     */   public LexicalHandler getLexicalHandler() {
/*  57 */     return this.lexical;
/*     */   }
/*     */ 
/*     */   public void setLexicalHandler(LexicalHandler lexical) {
/*  61 */     this.lexical = lexical;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getIndentStep()
/*     */   {
/*  81 */     return this.indentStep.length();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setIndentStep(int indentStep)
/*     */   {
/*  97 */     StringBuilder s = new StringBuilder();
/*  98 */     for (; indentStep > 0; indentStep--) s.append(' ');
/*  99 */     setIndentStep(s.toString());
/*     */   }
/*     */ 
/*     */   public void setIndentStep(String s) {
/* 103 */     this.indentStep = s;
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 134 */     this.stateStack.push(SEEN_ELEMENT);
/* 135 */     this.state = SEEN_NOTHING;
/* 136 */     if (this.depth > 0) {
/* 137 */       writeNewLine();
/*     */     }
/* 139 */     doIndent();
/* 140 */     super.startElement(uri, localName, qName, atts);
/* 141 */     this.depth += 1;
/*     */   }
/*     */ 
/*     */   private void writeNewLine() throws SAXException {
/* 145 */     super.characters(NEWLINE, 0, NEWLINE.length);
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 172 */     this.depth -= 1;
/* 173 */     if (this.state == SEEN_ELEMENT) {
/* 174 */       writeNewLine();
/* 175 */       doIndent();
/*     */     }
/* 177 */     super.endElement(uri, localName, qName);
/* 178 */     this.state = this.stateStack.pop();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 228 */     this.state = SEEN_DATA;
/* 229 */     super.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/* 233 */     if (this.depth > 0) {
/* 234 */       writeNewLine();
/*     */     }
/* 236 */     doIndent();
/* 237 */     if (this.lexical != null)
/* 238 */       this.lexical.comment(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException {
/* 242 */     if (this.lexical != null)
/* 243 */       this.lexical.startDTD(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void endDTD() throws SAXException {
/* 247 */     if (this.lexical != null)
/* 248 */       this.lexical.endDTD();
/*     */   }
/*     */ 
/*     */   public void startEntity(String name) throws SAXException {
/* 252 */     if (this.lexical != null)
/* 253 */       this.lexical.startEntity(name);
/*     */   }
/*     */ 
/*     */   public void endEntity(String name) throws SAXException {
/* 257 */     if (this.lexical != null)
/* 258 */       this.lexical.endEntity(name);
/*     */   }
/*     */ 
/*     */   public void startCDATA() throws SAXException {
/* 262 */     if (this.lexical != null)
/* 263 */       this.lexical.startCDATA();
/*     */   }
/*     */ 
/*     */   public void endCDATA() throws SAXException {
/* 267 */     if (this.lexical != null)
/* 268 */       this.lexical.endCDATA();
/*     */   }
/*     */ 
/*     */   private void doIndent()
/*     */     throws SAXException
/*     */   {
/* 286 */     if (this.depth > 0) {
/* 287 */       char[] ch = this.indentStep.toCharArray();
/* 288 */       for (int i = 0; i < this.depth; i++)
/* 289 */         characters(ch, 0, ch.length);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.IndentingXMLFilter
 * JD-Core Version:    0.6.2
 */