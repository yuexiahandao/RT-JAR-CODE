/*     */ package com.sun.xml.internal.ws.encoding;
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
/*  50 */   private static final Logger log = Logger.getLogger(ImageDataContentHandler.class.getName());
/*     */   private final DataFlavor[] flavor;
/*     */ 
/*     */   public ImageDataContentHandler()
/*     */   {
/*  54 */     String[] mimeTypes = ImageIO.getReaderMIMETypes();
/*  55 */     this.flavor = new DataFlavor[mimeTypes.length];
/*  56 */     for (int i = 0; i < mimeTypes.length; i++)
/*  57 */       this.flavor[i] = new ActivationDataFlavor(Image.class, mimeTypes[i], "Image");
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
/*  84 */     for (DataFlavor aFlavor : this.flavor) {
/*  85 */       if (aFlavor.equals(df)) {
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
/*     */       BufferedImage bufImage;
/* 119 */       if ((obj instanceof BufferedImage)) {
/* 120 */         bufImage = (BufferedImage)obj;
/*     */       }
/*     */       else
/*     */       {
/*     */         BufferedImage bufImage;
/* 121 */         if ((obj instanceof Image))
/* 122 */           bufImage = render((Image)obj);
/*     */         else
/* 124 */           throw new IOException("ImageDataContentHandler requires Image object, was given object of type " + obj.getClass().toString());
/*     */       }
/*     */       BufferedImage bufImage;
/* 129 */       ImageWriter writer = null;
/* 130 */       Iterator i = ImageIO.getImageWritersByMIMEType(type);
/* 131 */       if (i.hasNext()) {
/* 132 */         writer = (ImageWriter)i.next();
/*     */       }
/* 134 */       if (writer != null) {
/* 135 */         ImageOutputStream stream = ImageIO.createImageOutputStream(os);
/* 136 */         writer.setOutput(stream);
/* 137 */         writer.write(bufImage);
/* 138 */         writer.dispose();
/* 139 */         stream.close();
/*     */       } else {
/* 141 */         throw new IOException("Unsupported mime type:" + type);
/*     */       }
/*     */     } catch (Exception e) {
/* 144 */       throw new IOException("Unable to encode the image to a stream " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private BufferedImage render(Image img)
/*     */     throws InterruptedException
/*     */   {
/* 152 */     MediaTracker tracker = new MediaTracker(this);
/* 153 */     tracker.addImage(img, 0);
/* 154 */     tracker.waitForAll();
/* 155 */     BufferedImage bufImage = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
/*     */ 
/* 157 */     Graphics g = bufImage.createGraphics();
/* 158 */     g.drawImage(img, 0, 0, null);
/* 159 */     g.dispose();
/* 160 */     return bufImage;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.ImageDataContentHandler
 * JD-Core Version:    0.6.2
 */