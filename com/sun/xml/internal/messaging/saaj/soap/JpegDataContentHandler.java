/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import javax.activation.ActivationDataFlavor;
/*     */ import javax.activation.DataContentHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.imageio.ImageIO;
/*     */ 
/*     */ public class JpegDataContentHandler extends Component
/*     */   implements DataContentHandler
/*     */ {
/*  47 */   public final String STR_SRC = "java.awt.Image";
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/*  54 */     DataFlavor[] flavors = new DataFlavor[1];
/*     */     try
/*     */     {
/*  57 */       flavors[0] = new ActivationDataFlavor(Class.forName("java.awt.Image"), "image/jpeg", "JPEG");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  63 */       System.out.println(e);
/*     */     }
/*     */ 
/*  66 */     return flavors;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor df, DataSource ds)
/*     */   {
/*  79 */     if ((df.getMimeType().startsWith("image/jpeg")) && 
/*  80 */       (df.getRepresentationClass().getName().equals("java.awt.Image"))) {
/*  81 */       InputStream inputStream = null;
/*  82 */       BufferedImage jpegLoadImage = null;
/*     */       try
/*     */       {
/*  85 */         inputStream = ds.getInputStream();
/*  86 */         jpegLoadImage = ImageIO.read(inputStream);
/*     */       }
/*     */       catch (Exception e) {
/*  89 */         System.out.println(e);
/*     */       }
/*     */ 
/*  92 */       return jpegLoadImage;
/*     */     }
/*     */ 
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getContent(DataSource ds)
/*     */   {
/* 102 */     InputStream inputStream = null;
/* 103 */     BufferedImage jpegLoadImage = null;
/*     */     try
/*     */     {
/* 106 */       inputStream = ds.getInputStream();
/* 107 */       jpegLoadImage = ImageIO.read(inputStream);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/* 112 */     return jpegLoadImage;
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String mimeType, OutputStream os)
/*     */     throws IOException
/*     */   {
/* 122 */     if (!mimeType.equals("image/jpeg")) {
/* 123 */       throw new IOException("Invalid content type \"" + mimeType + "\" for ImageContentHandler");
/*     */     }
/*     */ 
/* 128 */     if (obj.equals(null)) {
/* 129 */       throw new IOException("Null object for ImageContentHandler");
/*     */     }
/*     */     try
/*     */     {
/* 133 */       BufferedImage bufImage = null;
/* 134 */       if ((obj instanceof BufferedImage)) {
/* 135 */         bufImage = (BufferedImage)obj;
/*     */       }
/*     */       else {
/* 138 */         Image img = (Image)obj;
/* 139 */         MediaTracker tracker = new MediaTracker(this);
/* 140 */         tracker.addImage(img, 0);
/* 141 */         tracker.waitForAll();
/* 142 */         if (tracker.isErrorAny()) {
/* 143 */           throw new IOException("Error while loading image");
/*     */         }
/* 145 */         bufImage = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
/*     */ 
/* 151 */         Graphics g = bufImage.createGraphics();
/* 152 */         g.drawImage(img, 0, 0, null);
/*     */       }
/* 154 */       ImageIO.write(bufImage, "jpeg", os);
/*     */     }
/*     */     catch (Exception ex) {
/* 157 */       throw new IOException("Unable to run the JPEG Encoder on a stream " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler
 * JD-Core Version:    0.6.2
 */