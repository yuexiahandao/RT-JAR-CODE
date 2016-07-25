/*     */ package com.sun.xml.internal.bind.marshaller;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class DataWriter extends XMLWriter
/*     */ {
/* 359 */   private static final Object SEEN_NOTHING = new Object();
/* 360 */   private static final Object SEEN_ELEMENT = new Object();
/* 361 */   private static final Object SEEN_DATA = new Object();
/*     */ 
/* 369 */   private Object state = SEEN_NOTHING;
/* 370 */   private Stack<Object> stateStack = new Stack();
/*     */ 
/* 372 */   private String indentStep = "";
/* 373 */   private int depth = 0;
/*     */ 
/*     */   public DataWriter(Writer writer, String encoding, CharacterEscapeHandler _escapeHandler)
/*     */   {
/* 120 */     super(writer, encoding, _escapeHandler);
/*     */   }
/*     */ 
/*     */   public DataWriter(Writer writer, String encoding)
/*     */   {
/* 125 */     this(writer, encoding, DumbEscapeHandler.theInstance);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getIndentStep()
/*     */   {
/* 151 */     return this.indentStep.length();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setIndentStep(int indentStep)
/*     */   {
/* 167 */     StringBuilder buf = new StringBuilder();
/* 168 */     for (; indentStep > 0; indentStep--)
/* 169 */       buf.append(' ');
/* 170 */     setIndentStep(buf.toString());
/*     */   }
/*     */ 
/*     */   public void setIndentStep(String s) {
/* 174 */     this.indentStep = s;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 194 */     this.depth = 0;
/* 195 */     this.state = SEEN_NOTHING;
/* 196 */     this.stateStack = new Stack();
/* 197 */     super.reset();
/*     */   }
/*     */ 
/*     */   protected void writeXmlDecl(String decl) throws IOException {
/* 201 */     super.writeXmlDecl(decl);
/* 202 */     write('\n');
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 229 */     this.stateStack.push(SEEN_ELEMENT);
/* 230 */     this.state = SEEN_NOTHING;
/* 231 */     if (this.depth > 0) {
/* 232 */       super.characters("\n");
/*     */     }
/* 234 */     doIndent();
/* 235 */     super.startElement(uri, localName, qName, atts);
/* 236 */     this.depth += 1;
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 261 */     this.depth -= 1;
/* 262 */     if (this.state == SEEN_ELEMENT) {
/* 263 */       super.characters("\n");
/* 264 */       doIndent();
/*     */     }
/* 266 */     super.endElement(uri, localName, qName);
/* 267 */     this.state = this.stateStack.pop();
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/*     */     try {
/* 272 */       write('\n');
/*     */     } catch (IOException e) {
/* 274 */       throw new SAXException(e);
/*     */     }
/* 276 */     super.endDocument();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 325 */     this.state = SEEN_DATA;
/* 326 */     super.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   private void doIndent()
/*     */     throws SAXException
/*     */   {
/* 346 */     if (this.depth > 0) {
/* 347 */       char[] ch = this.indentStep.toCharArray();
/* 348 */       for (int i = 0; i < this.depth; i++)
/* 349 */         characters(ch, 0, ch.length);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.DataWriter
 * JD-Core Version:    0.6.2
 */