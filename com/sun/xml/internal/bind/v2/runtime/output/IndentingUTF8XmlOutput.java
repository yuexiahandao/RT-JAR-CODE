/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class IndentingUTF8XmlOutput extends UTF8XmlOutput
/*     */ {
/*     */   private final Encoded indent8;
/*     */   private final int unitLen;
/*  62 */   private int depth = 0;
/*     */ 
/*  64 */   private boolean seenText = false;
/*     */ 
/*     */   public IndentingUTF8XmlOutput(OutputStream out, String indentStr, Encoded[] localNames, CharacterEscapeHandler escapeHandler)
/*     */   {
/*  73 */     super(out, localNames, escapeHandler);
/*     */ 
/*  75 */     if (indentStr != null) {
/*  76 */       Encoded e = new Encoded(indentStr);
/*  77 */       this.indent8 = new Encoded();
/*  78 */       this.indent8.ensureSize(e.len * 8);
/*  79 */       this.unitLen = e.len;
/*  80 */       for (int i = 0; i < 8; i++)
/*  81 */         System.arraycopy(e.buf, 0, this.indent8.buf, this.unitLen * i, this.unitLen);
/*     */     } else {
/*  83 */       this.indent8 = null;
/*  84 */       this.unitLen = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName) throws IOException
/*     */   {
/*  90 */     indentStartTag();
/*  91 */     super.beginStartTag(prefix, localName);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(Name name) throws IOException
/*     */   {
/*  96 */     indentStartTag();
/*  97 */     super.beginStartTag(name);
/*     */   }
/*     */ 
/*     */   private void indentStartTag() throws IOException {
/* 101 */     closeStartTag();
/* 102 */     if (!this.seenText)
/* 103 */       printIndent();
/* 104 */     this.depth += 1;
/* 105 */     this.seenText = false;
/*     */   }
/*     */ 
/*     */   public void endTag(Name name) throws IOException
/*     */   {
/* 110 */     indentEndTag();
/* 111 */     super.endTag(name);
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName) throws IOException
/*     */   {
/* 116 */     indentEndTag();
/* 117 */     super.endTag(prefix, localName);
/*     */   }
/*     */ 
/*     */   private void indentEndTag() throws IOException {
/* 121 */     this.depth -= 1;
/* 122 */     if ((!this.closeStartTagPending) && (!this.seenText))
/* 123 */       printIndent();
/* 124 */     this.seenText = false;
/*     */   }
/*     */ 
/*     */   private void printIndent() throws IOException {
/* 128 */     write(10);
/* 129 */     int i = this.depth % 8;
/*     */ 
/* 131 */     write(this.indent8.buf, 0, i * this.unitLen);
/*     */ 
/* 133 */     i >>= 3;
/*     */ 
/* 135 */     for (; i > 0; i--)
/* 136 */       this.indent8.write(this);
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needSP) throws IOException
/*     */   {
/* 141 */     this.seenText = true;
/* 142 */     super.text(value, needSP);
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needSP) throws IOException
/*     */   {
/* 147 */     this.seenText = true;
/* 148 */     super.text(value, needSP);
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException
/*     */   {
/* 153 */     write(10);
/* 154 */     super.endDocument(fragment);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.IndentingUTF8XmlOutput
 * JD-Core Version:    0.6.2
 */