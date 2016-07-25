/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.activation.ActivationDataFlavor;
/*     */ import javax.activation.DataContentHandler;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ public class StringDataContentHandler
/*     */   implements DataContentHandler
/*     */ {
/*  41 */   private static ActivationDataFlavor myDF = new ActivationDataFlavor(String.class, "text/plain", "Text String");
/*     */ 
/*     */   protected ActivationDataFlavor getDF()
/*     */   {
/*  47 */     return myDF;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/*  56 */     return new DataFlavor[] { getDF() };
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor df, DataSource ds)
/*     */     throws IOException
/*     */   {
/*  70 */     if (getDF().equals(df)) {
/*  71 */       return getContent(ds);
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getContent(DataSource ds) throws IOException {
/*  77 */     String enc = null;
/*  78 */     InputStreamReader is = null;
/*     */     try
/*     */     {
/*  81 */       enc = getCharset(ds.getContentType());
/*  82 */       is = new InputStreamReader(ds.getInputStream(), enc);
/*     */     }
/*     */     catch (IllegalArgumentException iex)
/*     */     {
/*  92 */       throw new UnsupportedEncodingException(enc);
/*     */     }
/*     */     try
/*     */     {
/*  96 */       int pos = 0;
/*     */ 
/*  98 */       char[] buf = new char[1024];
/*     */       int count;
/*     */       int size;
/* 100 */       while ((count = is.read(buf, pos, buf.length - pos)) != -1) {
/* 101 */         pos += count;
/* 102 */         if (pos >= buf.length) {
/* 103 */           size = buf.length;
/* 104 */           if (size < 262144)
/* 105 */             size += size;
/*     */           else
/* 107 */             size += 262144;
/* 108 */           char[] tbuf = new char[size];
/* 109 */           System.arraycopy(buf, 0, tbuf, 0, pos);
/* 110 */           buf = tbuf;
/*     */         }
/*     */       }
/* 113 */       return new String(buf, 0, pos);
/*     */     } finally {
/*     */       try {
/* 116 */         is.close();
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String type, OutputStream os) throws IOException
/*     */   {
/* 126 */     if (!(obj instanceof String)) {
/* 127 */       throw new IOException("\"" + getDF().getMimeType() + "\" DataContentHandler requires String object, " + "was given object of type " + obj.getClass().toString());
/*     */     }
/*     */ 
/* 131 */     String enc = null;
/* 132 */     OutputStreamWriter osw = null;
/*     */     try
/*     */     {
/* 135 */       enc = getCharset(type);
/* 136 */       osw = new OutputStreamWriter(os, enc);
/*     */     }
/*     */     catch (IllegalArgumentException iex)
/*     */     {
/* 146 */       throw new UnsupportedEncodingException(enc);
/*     */     }
/*     */ 
/* 149 */     String s = (String)obj;
/* 150 */     osw.write(s, 0, s.length());
/* 151 */     osw.flush();
/*     */   }
/*     */ 
/*     */   private String getCharset(String type) {
/*     */     try {
/* 156 */       ContentType ct = new ContentType(type);
/* 157 */       String charset = ct.getParameter("charset");
/* 158 */       if (charset == null)
/*     */       {
/* 160 */         charset = "us-ascii";
/* 161 */       }return MimeUtility.javaCharset(charset); } catch (Exception ex) {
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.StringDataContentHandler
 * JD-Core Version:    0.6.2
 */