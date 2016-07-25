/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.TxwException;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DomSerializer
/*     */   implements XmlSerializer
/*     */ {
/*     */   private final SaxSerializer serializer;
/*     */ 
/*     */   public DomSerializer(Node node)
/*     */   {
/*  58 */     Dom2SaxAdapter adapter = new Dom2SaxAdapter(node);
/*  59 */     this.serializer = new SaxSerializer(adapter, adapter, false);
/*     */   }
/*     */ 
/*     */   public DomSerializer(DOMResult domResult) {
/*  63 */     Node node = domResult.getNode();
/*     */ 
/*  65 */     if (node == null)
/*     */       try {
/*  67 */         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  68 */         dbf.setNamespaceAware(true);
/*  69 */         DocumentBuilder db = dbf.newDocumentBuilder();
/*  70 */         Document doc = db.newDocument();
/*  71 */         domResult.setNode(doc);
/*  72 */         this.serializer = new SaxSerializer(new Dom2SaxAdapter(doc), null, false);
/*     */       } catch (ParserConfigurationException pce) {
/*  74 */         throw new TxwException(pce);
/*     */       }
/*     */     else
/*  77 */       this.serializer = new SaxSerializer(new Dom2SaxAdapter(node), null, false);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/*  83 */     this.serializer.startDocument();
/*     */   }
/*     */ 
/*     */   public void beginStartTag(String uri, String localName, String prefix) {
/*  87 */     this.serializer.beginStartTag(uri, localName, prefix);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/*  91 */     this.serializer.writeAttribute(uri, localName, prefix, value);
/*     */   }
/*     */ 
/*     */   public void writeXmlns(String prefix, String uri) {
/*  95 */     this.serializer.writeXmlns(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endStartTag(String uri, String localName, String prefix) {
/*  99 */     this.serializer.endStartTag(uri, localName, prefix);
/*     */   }
/*     */ 
/*     */   public void endTag() {
/* 103 */     this.serializer.endTag();
/*     */   }
/*     */ 
/*     */   public void text(StringBuilder text) {
/* 107 */     this.serializer.text(text);
/*     */   }
/*     */ 
/*     */   public void cdata(StringBuilder text) {
/* 111 */     this.serializer.cdata(text);
/*     */   }
/*     */ 
/*     */   public void comment(StringBuilder comment) {
/* 115 */     this.serializer.comment(comment);
/*     */   }
/*     */ 
/*     */   public void endDocument() {
/* 119 */     this.serializer.endDocument();
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.DomSerializer
 * JD-Core Version:    0.6.2
 */