/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.ActivationDataFlavor;
/*     */ import javax.activation.DataContentHandler;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ public class GifDataContentHandler extends Component
/*     */   implements DataContentHandler
/*     */ {
/*  40 */   private static ActivationDataFlavor myDF = new ActivationDataFlavor(Image.class, "image/gif", "GIF Image");
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
/*  77 */     InputStream is = ds.getInputStream();
/*  78 */     int pos = 0;
/*     */ 
/*  80 */     byte[] buf = new byte[1024];
/*     */     int count;
/*  82 */     while ((count = is.read(buf, pos, buf.length - pos)) != -1) {
/*  83 */       pos += count;
/*  84 */       if (pos >= buf.length) {
/*  85 */         int size = buf.length;
/*  86 */         if (size < 262144)
/*  87 */           size += size;
/*     */         else
/*  89 */           size += 262144;
/*  90 */         byte[] tbuf = new byte[size];
/*  91 */         System.arraycopy(buf, 0, tbuf, 0, pos);
/*  92 */         buf = tbuf;
/*     */       }
/*     */     }
/*  95 */     Toolkit tk = Toolkit.getDefaultToolkit();
/*  96 */     return tk.createImage(buf, 0, pos);
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String type, OutputStream os)
/*     */     throws IOException
/*     */   {
/* 104 */     if ((obj != null) && (!(obj instanceof Image))) {
/* 105 */       throw new IOException("\"" + getDF().getMimeType() + "\" DataContentHandler requires Image object, " + "was given object of type " + obj.getClass().toString());
/*     */     }
/*     */ 
/* 109 */     throw new IOException(getDF().getMimeType() + " encoding not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.GifDataContentHandler
 * JD-Core Version:    0.6.2
 */