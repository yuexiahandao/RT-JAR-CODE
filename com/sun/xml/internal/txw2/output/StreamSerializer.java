/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.TxwException;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class StreamSerializer
/*     */   implements XmlSerializer
/*     */ {
/*     */   private final SaxSerializer serializer;
/*     */   private final XMLWriter writer;
/*     */ 
/*     */   public StreamSerializer(OutputStream out)
/*     */   {
/*  52 */     this(createWriter(out));
/*     */   }
/*     */ 
/*     */   public StreamSerializer(OutputStream out, String encoding) throws UnsupportedEncodingException {
/*  56 */     this(createWriter(out, encoding));
/*     */   }
/*     */ 
/*     */   public StreamSerializer(Writer out) {
/*  60 */     this(new StreamResult(out));
/*     */   }
/*     */ 
/*     */   public StreamSerializer(StreamResult streamResult)
/*     */   {
/*  65 */     final OutputStream[] autoClose = new OutputStream[1];
/*     */ 
/*  67 */     if (streamResult.getWriter() != null) {
/*  68 */       this.writer = createWriter(streamResult.getWriter());
/*  69 */     } else if (streamResult.getOutputStream() != null) {
/*  70 */       this.writer = createWriter(streamResult.getOutputStream());
/*  71 */     } else if (streamResult.getSystemId() != null) {
/*  72 */       String fileURL = streamResult.getSystemId();
/*     */ 
/*  74 */       fileURL = convertURL(fileURL);
/*     */       try
/*     */       {
/*  77 */         FileOutputStream fos = new FileOutputStream(fileURL);
/*  78 */         autoClose[0] = fos;
/*  79 */         this.writer = createWriter(fos);
/*     */       } catch (IOException e) {
/*  81 */         throw new TxwException(e);
/*     */       }
/*     */     } else {
/*  84 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*  87 */     this.serializer = new SaxSerializer(this.writer, this.writer, false) {
/*     */       public void endDocument() {
/*  89 */         super.endDocument();
/*  90 */         if (autoClose[0] != null) {
/*     */           try {
/*  92 */             autoClose[0].close();
/*     */           } catch (IOException e) {
/*  94 */             throw new TxwException(e);
/*     */           }
/*  96 */           autoClose[0] = null;
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private StreamSerializer(XMLWriter writer) {
/* 103 */     this.writer = writer;
/*     */ 
/* 105 */     this.serializer = new SaxSerializer(writer, writer, false);
/*     */   }
/*     */ 
/*     */   private String convertURL(String url) {
/* 109 */     url = url.replace('\\', '/');
/* 110 */     url = url.replaceAll("//", "/");
/* 111 */     url = url.replaceAll("//", "/");
/* 112 */     if (url.startsWith("file:/")) {
/* 113 */       if (url.substring(6).indexOf(":") > 0)
/* 114 */         url = url.substring(6);
/*     */       else
/* 116 */         url = url.substring(5);
/*     */     }
/* 118 */     return url;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/* 123 */     this.serializer.startDocument();
/*     */   }
/*     */ 
/*     */   public void beginStartTag(String uri, String localName, String prefix) {
/* 127 */     this.serializer.beginStartTag(uri, localName, prefix);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/* 131 */     this.serializer.writeAttribute(uri, localName, prefix, value);
/*     */   }
/*     */ 
/*     */   public void writeXmlns(String prefix, String uri) {
/* 135 */     this.serializer.writeXmlns(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endStartTag(String uri, String localName, String prefix) {
/* 139 */     this.serializer.endStartTag(uri, localName, prefix);
/*     */   }
/*     */ 
/*     */   public void endTag() {
/* 143 */     this.serializer.endTag();
/*     */   }
/*     */ 
/*     */   public void text(StringBuilder text) {
/* 147 */     this.serializer.text(text);
/*     */   }
/*     */ 
/*     */   public void cdata(StringBuilder text) {
/* 151 */     this.serializer.cdata(text);
/*     */   }
/*     */ 
/*     */   public void comment(StringBuilder comment) {
/* 155 */     this.serializer.comment(comment);
/*     */   }
/*     */ 
/*     */   public void endDocument() {
/* 159 */     this.serializer.endDocument();
/*     */   }
/*     */ 
/*     */   public void flush() {
/* 163 */     this.serializer.flush();
/*     */     try {
/* 165 */       this.writer.flush();
/*     */     } catch (IOException e) {
/* 167 */       throw new TxwException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static XMLWriter createWriter(Writer w)
/*     */   {
/* 174 */     DataWriter dw = new DataWriter(new BufferedWriter(w));
/* 175 */     dw.setIndentStep("  ");
/* 176 */     return dw;
/*     */   }
/*     */ 
/*     */   private static XMLWriter createWriter(OutputStream os, String encoding) throws UnsupportedEncodingException {
/* 180 */     XMLWriter writer = createWriter(new OutputStreamWriter(os, encoding));
/* 181 */     writer.setEncoding(encoding);
/* 182 */     return writer;
/*     */   }
/*     */ 
/*     */   private static XMLWriter createWriter(OutputStream os) {
/*     */     try {
/* 187 */       return createWriter(os, "UTF-8");
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 190 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.StreamSerializer
 * JD-Core Version:    0.6.2
 */