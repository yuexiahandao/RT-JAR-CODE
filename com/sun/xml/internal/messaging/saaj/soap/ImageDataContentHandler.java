/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.activation.ActivationDataFlavor;
/*     */ import javax.activation.DataContentHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ 
/*     */ public class ImageDataContentHandler extends Component
/*     */   implements DataContentHandler
/*     */ {
/*  46 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap", "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
/*     */   private DataFlavor[] flavor;
/*     */ 
/*     */   public ImageDataContentHandler()
/*     */   {
/*  53 */     String[] mimeTypes = ImageIO.getReaderMIMETypes();
/*  54 */     this.flavor = new DataFlavor[mimeTypes.length];
/*  55 */     for (int i = 0; i < mimeTypes.length; i++)
/*  56 */       this.flavor[i] = new ActivationDataFlavor(Image.class, mimeTypes[i], "Image");
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/*  70 */     return this.flavor;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor df, DataSource ds)
/*     */     throws IOException
/*     */   {
/*  84 */     for (int i = 0; i < this.flavor.length; i++) {
/*  85 */       if (this.flavor[i].equals(df)) {
/*  86 */         return getContent(ds);
/*     */       }
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getContent(DataSource ds)
/*     */     throws IOException
/*     */   {
/* 101 */     return ImageIO.read(new BufferedInputStream(ds.getInputStream()));
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String type, OutputStream os)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 118 */       BufferedImage bufImage = null;
/* 119 */       if ((obj instanceof BufferedImage)) {
/* 120 */         bufImage = (BufferedImage)obj;
/* 121 */       } else if ((obj instanceof Image)) {
/* 122 */         bufImage = render((Image)obj);
/*     */       } else {
/* 124 */         log.log(Level.SEVERE, "SAAJ0520.soap.invalid.obj.type", new String[] { obj.getClass().toString() });
/*     */ 
/* 127 */         throw new IOException("ImageDataContentHandler requires Image object, was given object of type " + obj.getClass().toString());
/*     */       }
/*     */ 
/* 132 */       ImageWriter writer = null;
/* 133 */       Iterator i = ImageIO.getImageWritersByMIMEType(type);
/* 134 */       if (i.hasNext()) {
/* 135 */         writer = (ImageWriter)i.next();
/*     */       }
/* 137 */       if (writer != null) {
/* 138 */         ImageOutputStream stream = null;
/* 139 */         stream = ImageIO.createImageOutputStream(os);
/* 140 */         writer.setOutput(stream);
/* 141 */         writer.write(bufImage);
/* 142 */         writer.dispose();
/* 143 */         stream.close();
/*     */       } else {
/* 145 */         log.log(Level.SEVERE, "SAAJ0526.soap.unsupported.mime.type", new String[] { type });
/*     */ 
/* 147 */         throw new IOException("Unsupported mime type:" + type);
/*     */       }
/*     */     } catch (Exception e) {
/* 150 */       log.severe("SAAJ0525.soap.cannot.encode.img");
/* 151 */       throw new IOException("Unable to encode the image to a stream " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private BufferedImage render(Image img)
/*     */     throws InterruptedException
/*     */   {
/* 159 */     MediaTracker tracker = new MediaTracker(this);
/* 160 */     tracker.addImage(img, 0);
/* 161 */     tracker.waitForAll();
/* 162 */     BufferedImage bufImage = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
/*     */ 
/* 164 */     Graphics g = bufImage.createGraphics();
/* 165 */     g.drawImage(img, 0, 0, null);
/* 166 */     g.dispose();
/* 167 */     return bufImage;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ImageDataContentHandler
 * JD-Core Version:    0.6.2
 */