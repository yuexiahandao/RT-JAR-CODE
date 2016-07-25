/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.ActivationDataFlavor;
/*     */ import javax.activation.DataContentHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.transform.Source;
/*     */ 
/*     */ public class FastInfosetDataContentHandler
/*     */   implements DataContentHandler
/*     */ {
/*  44 */   public final String STR_SRC = "com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource";
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/*  54 */     DataFlavor[] flavors = new DataFlavor[1];
/*  55 */     flavors[0] = new ActivationDataFlavor(FastInfosetReflection.getFastInfosetSource_class(), "application/fastinfoset", "Fast Infoset");
/*     */ 
/*  58 */     return flavors;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor flavor, DataSource dataSource)
/*     */     throws IOException
/*     */   {
/*  70 */     if (flavor.getMimeType().startsWith("application/fastinfoset")) {
/*     */       try {
/*  72 */         if (flavor.getRepresentationClass().getName().equals("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource")) {
/*  73 */           return FastInfosetReflection.FastInfosetSource_new(dataSource.getInputStream());
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*  78 */         throw new IOException(e.getMessage());
/*     */       }
/*     */     }
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getContent(DataSource dataSource) throws IOException {
/*     */     try {
/*  86 */       return FastInfosetReflection.FastInfosetSource_new(dataSource.getInputStream());
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  90 */       throw new IOException(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String mimeType, OutputStream os)
/*     */     throws IOException
/*     */   {
/* 102 */     if (!mimeType.equals("application/fastinfoset")) {
/* 103 */       throw new IOException("Invalid content type \"" + mimeType + "\" for FastInfosetDCH");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 108 */       InputStream is = FastInfosetReflection.FastInfosetSource_getInputStream((Source)obj);
/*     */ 
/* 111 */       byte[] buffer = new byte[4096];
/*     */       int n;
/* 112 */       while ((n = is.read(buffer)) != -1)
/* 113 */         os.write(buffer, 0, n);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 117 */       throw new IOException("Error copying FI source to output stream " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.FastInfosetDataContentHandler
 * JD-Core Version:    0.6.2
 */