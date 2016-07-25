/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ToolkitImage extends Image
/*     */ {
/*     */   ImageProducer source;
/*     */   InputStreamImageSource src;
/*     */   ImageRepresentation imagerep;
/*  80 */   private int width = -1;
/*  81 */   private int height = -1;
/*     */   private Hashtable properties;
/*     */   private int availinfo;
/*     */ 
/*     */   protected ToolkitImage()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ToolkitImage(ImageProducer paramImageProducer)
/*     */   {
/*  67 */     this.source = paramImageProducer;
/*  68 */     if ((paramImageProducer instanceof InputStreamImageSource))
/*  69 */       this.src = ((InputStreamImageSource)paramImageProducer);
/*     */   }
/*     */ 
/*     */   public ImageProducer getSource()
/*     */   {
/*  74 */     if (this.src != null) {
/*  75 */       this.src.checkSecurity(null, false);
/*     */     }
/*  77 */     return this.source;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/*  91 */     if (this.src != null) {
/*  92 */       this.src.checkSecurity(null, false);
/*     */     }
/*  94 */     if ((this.availinfo & 0x1) == 0) {
/*  95 */       reconstruct(1);
/*     */     }
/*  97 */     return this.width;
/*     */   }
/*     */ 
/*     */   public synchronized int getWidth(ImageObserver paramImageObserver)
/*     */   {
/* 106 */     if (this.src != null) {
/* 107 */       this.src.checkSecurity(null, false);
/*     */     }
/* 109 */     if ((this.availinfo & 0x1) == 0) {
/* 110 */       addWatcher(paramImageObserver, true);
/* 111 */       if ((this.availinfo & 0x1) == 0) {
/* 112 */         return -1;
/*     */       }
/*     */     }
/* 115 */     return this.width;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 123 */     if (this.src != null) {
/* 124 */       this.src.checkSecurity(null, false);
/*     */     }
/* 126 */     if ((this.availinfo & 0x2) == 0) {
/* 127 */       reconstruct(2);
/*     */     }
/* 129 */     return this.height;
/*     */   }
/*     */ 
/*     */   public synchronized int getHeight(ImageObserver paramImageObserver)
/*     */   {
/* 138 */     if (this.src != null) {
/* 139 */       this.src.checkSecurity(null, false);
/*     */     }
/* 141 */     if ((this.availinfo & 0x2) == 0) {
/* 142 */       addWatcher(paramImageObserver, true);
/* 143 */       if ((this.availinfo & 0x2) == 0) {
/* 144 */         return -1;
/*     */       }
/*     */     }
/* 147 */     return this.height;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString, ImageObserver paramImageObserver)
/*     */   {
/* 161 */     if (paramString == null) {
/* 162 */       throw new NullPointerException("null property name is not allowed");
/*     */     }
/*     */ 
/* 165 */     if (this.src != null) {
/* 166 */       this.src.checkSecurity(null, false);
/*     */     }
/* 168 */     if (this.properties == null) {
/* 169 */       addWatcher(paramImageObserver, true);
/* 170 */       if (this.properties == null) {
/* 171 */         return null;
/*     */       }
/*     */     }
/* 174 */     Object localObject = this.properties.get(paramString);
/* 175 */     if (localObject == null) {
/* 176 */       localObject = Image.UndefinedProperty;
/*     */     }
/* 178 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean hasError() {
/* 182 */     if (this.src != null) {
/* 183 */       this.src.checkSecurity(null, false);
/*     */     }
/* 185 */     return (this.availinfo & 0x40) != 0;
/*     */   }
/*     */ 
/*     */   public int check(ImageObserver paramImageObserver) {
/* 189 */     if (this.src != null) {
/* 190 */       this.src.checkSecurity(null, false);
/*     */     }
/* 192 */     if (((this.availinfo & 0x40) == 0) && (((this.availinfo ^ 0xFFFFFFFF) & 0x7) != 0))
/*     */     {
/* 196 */       addWatcher(paramImageObserver, false);
/*     */     }
/* 198 */     return this.availinfo;
/*     */   }
/*     */ 
/*     */   public void preload(ImageObserver paramImageObserver) {
/* 202 */     if (this.src != null) {
/* 203 */       this.src.checkSecurity(null, false);
/*     */     }
/* 205 */     if ((this.availinfo & 0x20) == 0)
/* 206 */       addWatcher(paramImageObserver, true);
/*     */   }
/*     */ 
/*     */   private synchronized void addWatcher(ImageObserver paramImageObserver, boolean paramBoolean)
/*     */   {
/* 211 */     if ((this.availinfo & 0x40) != 0) {
/* 212 */       if (paramImageObserver != null) {
/* 213 */         paramImageObserver.imageUpdate(this, 192, -1, -1, -1, -1);
/*     */       }
/*     */ 
/* 216 */       return;
/*     */     }
/* 218 */     ImageRepresentation localImageRepresentation = getImageRep();
/* 219 */     localImageRepresentation.addWatcher(paramImageObserver);
/* 220 */     if (paramBoolean)
/* 221 */       localImageRepresentation.startProduction();
/*     */   }
/*     */ 
/*     */   private synchronized void reconstruct(int paramInt)
/*     */   {
/* 226 */     if ((paramInt & (this.availinfo ^ 0xFFFFFFFF)) != 0) {
/* 227 */       if ((this.availinfo & 0x40) != 0) {
/* 228 */         return;
/*     */       }
/* 230 */       ImageRepresentation localImageRepresentation = getImageRep();
/* 231 */       localImageRepresentation.startProduction();
/* 232 */       while ((paramInt & (this.availinfo ^ 0xFFFFFFFF)) != 0) {
/*     */         try {
/* 234 */           wait();
/*     */         } catch (InterruptedException localInterruptedException) {
/* 236 */           Thread.currentThread().interrupt();
/* 237 */           return;
/*     */         }
/* 239 */         if ((this.availinfo & 0x40) != 0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void addInfo(int paramInt)
/*     */   {
/* 247 */     this.availinfo |= paramInt;
/* 248 */     notifyAll();
/*     */   }
/*     */ 
/*     */   void setDimensions(int paramInt1, int paramInt2) {
/* 252 */     this.width = paramInt1;
/* 253 */     this.height = paramInt2;
/* 254 */     addInfo(3);
/*     */   }
/*     */ 
/*     */   void setProperties(Hashtable paramHashtable) {
/* 258 */     if (paramHashtable == null) {
/* 259 */       paramHashtable = new Hashtable();
/*     */     }
/* 261 */     this.properties = paramHashtable;
/* 262 */     addInfo(4);
/*     */   }
/*     */ 
/*     */   synchronized void infoDone(int paramInt) {
/* 266 */     if ((paramInt == 1) || (((this.availinfo ^ 0xFFFFFFFF) & 0x3) != 0))
/*     */     {
/* 269 */       addInfo(64);
/* 270 */     } else if ((this.availinfo & 0x4) == 0)
/* 271 */       setProperties(null);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 276 */     if (this.src != null)
/* 277 */       this.src.checkSecurity(null, false);
/*     */     ImageRepresentation localImageRepresentation;
/* 281 */     synchronized (this) {
/* 282 */       this.availinfo &= -65;
/* 283 */       localImageRepresentation = this.imagerep;
/* 284 */       this.imagerep = null;
/*     */     }
/* 286 */     if (localImageRepresentation != null) {
/* 287 */       localImageRepresentation.abort();
/*     */     }
/* 289 */     if (this.src != null)
/* 290 */       this.src.flush();
/*     */   }
/*     */ 
/*     */   protected ImageRepresentation makeImageRep()
/*     */   {
/* 295 */     return new ImageRepresentation(this, ColorModel.getRGBdefault(), false);
/*     */   }
/*     */ 
/*     */   public synchronized ImageRepresentation getImageRep()
/*     */   {
/* 300 */     if (this.src != null) {
/* 301 */       this.src.checkSecurity(null, false);
/*     */     }
/* 303 */     if (this.imagerep == null) {
/* 304 */       this.imagerep = makeImageRep();
/*     */     }
/* 306 */     return this.imagerep;
/*     */   }
/*     */ 
/*     */   public Graphics getGraphics() {
/* 310 */     throw new UnsupportedOperationException("getGraphics() not valid for images created with createImage(producer)");
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/* 316 */     ImageRepresentation localImageRepresentation = getImageRep();
/* 317 */     return localImageRepresentation.getColorModel();
/*     */   }
/*     */ 
/*     */   public BufferedImage getBufferedImage()
/*     */   {
/* 322 */     ImageRepresentation localImageRepresentation = getImageRep();
/* 323 */     return localImageRepresentation.getBufferedImage();
/*     */   }
/*     */ 
/*     */   public void setAccelerationPriority(float paramFloat) {
/* 327 */     super.setAccelerationPriority(paramFloat);
/* 328 */     ImageRepresentation localImageRepresentation = getImageRep();
/* 329 */     localImageRepresentation.setAccelerationPriority(this.accelerationPriority);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  57 */     NativeLibLoader.loadLibraries();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ToolkitImage
 * JD-Core Version:    0.6.2
 */