/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import java.io.Writer;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class DataWriter extends XMLWriter
/*     */ {
/* 356 */   private static final Object SEEN_NOTHING = new Object();
/* 357 */   private static final Object SEEN_ELEMENT = new Object();
/* 358 */   private static final Object SEEN_DATA = new Object();
/*     */ 
/* 366 */   private Object state = SEEN_NOTHING;
/* 367 */   private Stack stateStack = new Stack();
/*     */ 
/* 369 */   private String indentStep = "";
/* 370 */   private int depth = 0;
/*     */ 
/*     */   public DataWriter(Writer writer, String encoding, CharacterEscapeHandler _escapeHandler)
/*     */   {
/* 119 */     super(writer, encoding, _escapeHandler);
/*     */   }
/*     */ 
/*     */   public DataWriter(Writer writer, String encoding)
/*     */   {
/* 124 */     this(writer, encoding, DumbEscapeHandler.theInstance);
/*     */   }
/*     */ 
/*     */   public DataWriter(Writer writer) {
/* 128 */     this(writer, null, DumbEscapeHandler.theInstance);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getIndentStep()
/*     */   {
/* 154 */     return this.indentStep.length();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setIndentStep(int indentStep)
/*     */   {
/* 170 */     StringBuilder s = new StringBuilder();
/* 171 */     for (; indentStep > 0; indentStep--) s.append(' ');
/* 172 */     setIndentStep(s.toString());
/*     */   }
/*     */ 
/*     */   public void setIndentStep(String s) {
/* 176 */     this.indentStep = s;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 196 */     this.depth = 0;
/* 197 */     this.state = SEEN_NOTHING;
/* 198 */     this.stateStack = new Stack();
/* 199 */     super.reset();
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 226 */     this.stateStack.push(SEEN_ELEMENT);
/* 227 */     this.state = SEEN_NOTHING;
/* 228 */     if (this.depth > 0) {
/* 229 */       super.characters("\n");
/*     */     }
/* 231 */     doIndent();
/* 232 */     super.startElement(uri, localName, qName, atts);
/* 233 */     this.depth += 1;
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 258 */     this.depth -= 1;
/* 259 */     if (this.state == SEEN_ELEMENT) {
/* 260 */       super.characters("\n");
/* 261 */       doIndent();
/*     */     }
/* 263 */     super.endElement(uri, localName, qName);
/* 264 */     this.state = this.stateStack.pop();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 314 */     this.state = SEEN_DATA;
/* 315 */     super.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/* 319 */     if (this.depth > 0) {
/* 320 */       super.characters("\n");
/*     */     }
/* 322 */     doIndent();
/* 323 */     super.comment(ch, start, length);
/*     */   }
/*     */ 
/*     */   private void doIndent()
/*     */     throws SAXException
/*     */   {
/* 343 */     if (this.depth > 0) {
/* 344 */       char[] ch = this.indentStep.toCharArray();
/* 345 */       for (int i = 0; i < this.depth; i++)
/* 346 */         characters(ch, 0, ch.length);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.DataWriter
 * JD-Core Version:    0.6.2
 */