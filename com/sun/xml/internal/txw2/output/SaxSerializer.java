/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.TxwException;
/*     */ import java.util.Stack;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class SaxSerializer
/*     */   implements XmlSerializer
/*     */ {
/*     */   private final ContentHandler writer;
/*     */   private final LexicalHandler lexical;
/*  90 */   private final Stack<String> prefixBindings = new Stack();
/*     */ 
/* 107 */   private final Stack<String> elementBindings = new Stack();
/*     */ 
/* 119 */   private final AttributesImpl attrs = new AttributesImpl();
/*     */ 
/*     */   public SaxSerializer(ContentHandler handler)
/*     */   {
/*  48 */     this(handler, null, true);
/*     */   }
/*     */ 
/*     */   public SaxSerializer(ContentHandler handler, LexicalHandler lex)
/*     */   {
/*  59 */     this(handler, lex, true);
/*     */   }
/*     */ 
/*     */   public SaxSerializer(ContentHandler handler, LexicalHandler lex, boolean indenting) {
/*  63 */     if (!indenting) {
/*  64 */       this.writer = handler;
/*  65 */       this.lexical = lex;
/*     */     } else {
/*  67 */       IndentingXMLFilter indenter = new IndentingXMLFilter(handler, lex);
/*  68 */       this.writer = indenter;
/*  69 */       this.lexical = indenter;
/*     */     }
/*     */   }
/*     */ 
/*     */   public SaxSerializer(SAXResult result) {
/*  74 */     this(result.getHandler(), result.getLexicalHandler());
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/*     */     try
/*     */     {
/*  82 */       this.writer.startDocument();
/*     */     } catch (SAXException e) {
/*  84 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeXmlns(String prefix, String uri)
/*     */   {
/*  94 */     if (prefix == null) {
/*  95 */       prefix = "";
/*     */     }
/*     */ 
/*  98 */     if (prefix.equals("xml")) {
/*  99 */       return;
/*     */     }
/*     */ 
/* 102 */     this.prefixBindings.add(uri);
/* 103 */     this.prefixBindings.add(prefix);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(String uri, String localName, String prefix)
/*     */   {
/* 111 */     this.elementBindings.add(getQName(prefix, localName));
/* 112 */     this.elementBindings.add(localName);
/* 113 */     this.elementBindings.add(uri);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value)
/*     */   {
/* 122 */     this.attrs.addAttribute(uri, localName, getQName(prefix, localName), "CDATA", value.toString());
/*     */   }
/*     */ 
/*     */   public void endStartTag(String uri, String localName, String prefix)
/*     */   {
/*     */     try
/*     */     {
/* 131 */       while (this.prefixBindings.size() != 0) {
/* 132 */         this.writer.startPrefixMapping((String)this.prefixBindings.pop(), (String)this.prefixBindings.pop());
/*     */       }
/*     */ 
/* 137 */       this.writer.startElement(uri, localName, getQName(prefix, localName), this.attrs);
/*     */ 
/* 142 */       this.attrs.clear();
/*     */     } catch (SAXException e) {
/* 144 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endTag() {
/*     */     try {
/* 150 */       this.writer.endElement((String)this.elementBindings.pop(), (String)this.elementBindings.pop(), (String)this.elementBindings.pop());
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 155 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(StringBuilder text) {
/*     */     try {
/* 161 */       this.writer.characters(text.toString().toCharArray(), 0, text.length());
/*     */     } catch (SAXException e) {
/* 163 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cdata(StringBuilder text) {
/* 168 */     if (this.lexical == null)
/* 169 */       throw new UnsupportedOperationException("LexicalHandler is needed to write PCDATA");
/*     */     try
/*     */     {
/* 172 */       this.lexical.startCDATA();
/* 173 */       text(text);
/* 174 */       this.lexical.endCDATA();
/*     */     } catch (SAXException e) {
/* 176 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void comment(StringBuilder comment) {
/*     */     try {
/* 182 */       if (this.lexical == null) {
/* 183 */         throw new UnsupportedOperationException("LexicalHandler is needed to write comments");
/*     */       }
/* 185 */       this.lexical.comment(comment.toString().toCharArray(), 0, comment.length());
/*     */     } catch (SAXException e) {
/* 187 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endDocument() {
/*     */     try {
/* 193 */       this.writer.endDocument();
/*     */     } catch (SAXException e) {
/* 195 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*     */   }
/*     */ 
/*     */   private static String getQName(String prefix, String localName)
/*     */   {
/*     */     String qName;
/*     */     String qName;
/* 206 */     if ((prefix == null) || (prefix.length() == 0))
/* 207 */       qName = localName;
/*     */     else {
/* 209 */       qName = prefix + ':' + localName;
/*     */     }
/* 211 */     return qName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.SaxSerializer
 * JD-Core Version:    0.6.2
 */