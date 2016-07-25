/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import javax.activation.ActivationDataFlavor;
/*     */ import javax.activation.DataContentHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ 
/*     */ public class XmlDataContentHandler
/*     */   implements DataContentHandler
/*     */ {
/*     */   private final DataFlavor[] flavors;
/*     */ 
/*     */   public XmlDataContentHandler()
/*     */     throws ClassNotFoundException
/*     */   {
/*  54 */     this.flavors = new DataFlavor[3];
/*  55 */     this.flavors[0] = new ActivationDataFlavor(StreamSource.class, "text/xml", "XML");
/*  56 */     this.flavors[1] = new ActivationDataFlavor(StreamSource.class, "application/xml", "XML");
/*  57 */     this.flavors[2] = new ActivationDataFlavor(String.class, "text/xml", "XML String");
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors() {
/*  61 */     return this.flavors;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor df, DataSource ds)
/*     */     throws IOException
/*     */   {
/*  67 */     for (DataFlavor aFlavor : this.flavors) {
/*  68 */       if (aFlavor.equals(df)) {
/*  69 */         return getContent(ds);
/*     */       }
/*     */     }
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getContent(DataSource ds)
/*     */     throws IOException
/*     */   {
/*  79 */     String ctStr = ds.getContentType();
/*  80 */     String charset = null;
/*  81 */     if (ctStr != null) {
/*  82 */       ContentType ct = new ContentType(ctStr);
/*  83 */       if (!isXml(ct)) {
/*  84 */         throw new IOException("Cannot convert DataSource with content type \"" + ctStr + "\" to object in XmlDataContentHandler");
/*     */       }
/*     */ 
/*  88 */       charset = ct.getParameter("charset");
/*     */     }
/*  90 */     return charset != null ? new StreamSource(new InputStreamReader(ds.getInputStream()), charset) : new StreamSource(ds.getInputStream());
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String mimeType, OutputStream os)
/*     */     throws IOException
/*     */   {
/* 101 */     if ((!(obj instanceof DataSource)) && (!(obj instanceof Source)) && (!(obj instanceof String))) {
/* 102 */       throw new IOException("Invalid Object type = " + obj.getClass() + ". XmlDataContentHandler can only convert DataSource|Source|String to XML.");
/*     */     }
/*     */ 
/* 106 */     ContentType ct = new ContentType(mimeType);
/* 107 */     if (!isXml(ct)) {
/* 108 */       throw new IOException("Invalid content type \"" + mimeType + "\" for XmlDataContentHandler");
/*     */     }
/*     */ 
/* 112 */     String charset = ct.getParameter("charset");
/* 113 */     if ((obj instanceof String)) {
/* 114 */       String s = (String)obj;
/* 115 */       if (charset == null) {
/* 116 */         charset = "utf-8";
/*     */       }
/* 118 */       OutputStreamWriter osw = new OutputStreamWriter(os, charset);
/* 119 */       osw.write(s, 0, s.length());
/* 120 */       osw.flush();
/* 121 */       return;
/*     */     }
/*     */ 
/* 124 */     Source source = (obj instanceof DataSource) ? (Source)getContent((DataSource)obj) : (Source)obj;
/*     */     try
/*     */     {
/* 127 */       Transformer transformer = XmlUtil.newTransformer();
/* 128 */       if (charset != null) {
/* 129 */         transformer.setOutputProperty("encoding", charset);
/*     */       }
/* 131 */       StreamResult result = new StreamResult(os);
/* 132 */       transformer.transform(source, result);
/*     */     } catch (Exception ex) {
/* 134 */       throw new IOException("Unable to run the JAXP transformer in XmlDataContentHandler " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isXml(ContentType ct)
/*     */   {
/* 141 */     return (ct.getSubType().equals("xml")) && ((ct.getPrimaryType().equals("text")) || (ct.getPrimaryType().equals("application")));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.XmlDataContentHandler
 * JD-Core Version:    0.6.2
 */