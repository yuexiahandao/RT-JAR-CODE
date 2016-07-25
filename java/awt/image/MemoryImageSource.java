/*     */ package java.awt.image;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class MemoryImageSource
/*     */   implements ImageProducer
/*     */ {
/*     */   int width;
/*     */   int height;
/*     */   ColorModel model;
/*     */   Object pixels;
/*     */   int pixeloffset;
/*     */   int pixelscan;
/*     */   Hashtable properties;
/* 115 */   Vector theConsumers = new Vector();
/*     */   boolean animating;
/*     */   boolean fullbuffers;
/*     */ 
/*     */   public MemoryImageSource(int paramInt1, int paramInt2, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */   {
/* 134 */     initialize(paramInt1, paramInt2, paramColorModel, paramArrayOfByte, paramInt3, paramInt4, null);
/*     */   }
/*     */ 
/*     */   public MemoryImageSource(int paramInt1, int paramInt2, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt3, int paramInt4, Hashtable<?, ?> paramHashtable)
/*     */   {
/* 156 */     initialize(paramInt1, paramInt2, paramColorModel, paramArrayOfByte, paramInt3, paramInt4, paramHashtable);
/*     */   }
/*     */ 
/*     */   public MemoryImageSource(int paramInt1, int paramInt2, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt3, int paramInt4)
/*     */   {
/* 174 */     initialize(paramInt1, paramInt2, paramColorModel, paramArrayOfInt, paramInt3, paramInt4, null);
/*     */   }
/*     */ 
/*     */   public MemoryImageSource(int paramInt1, int paramInt2, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt3, int paramInt4, Hashtable<?, ?> paramHashtable)
/*     */   {
/* 196 */     initialize(paramInt1, paramInt2, paramColorModel, paramArrayOfInt, paramInt3, paramInt4, paramHashtable);
/*     */   }
/*     */ 
/*     */   private void initialize(int paramInt1, int paramInt2, ColorModel paramColorModel, Object paramObject, int paramInt3, int paramInt4, Hashtable paramHashtable)
/*     */   {
/* 201 */     this.width = paramInt1;
/* 202 */     this.height = paramInt2;
/* 203 */     this.model = paramColorModel;
/* 204 */     this.pixels = paramObject;
/* 205 */     this.pixeloffset = paramInt3;
/* 206 */     this.pixelscan = paramInt4;
/* 207 */     if (paramHashtable == null) {
/* 208 */       paramHashtable = new Hashtable();
/*     */     }
/* 210 */     this.properties = paramHashtable;
/*     */   }
/*     */ 
/*     */   public MemoryImageSource(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4)
/*     */   {
/* 227 */     initialize(paramInt1, paramInt2, ColorModel.getRGBdefault(), paramArrayOfInt, paramInt3, paramInt4, null);
/*     */   }
/*     */ 
/*     */   public MemoryImageSource(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, Hashtable<?, ?> paramHashtable)
/*     */   {
/* 249 */     initialize(paramInt1, paramInt2, ColorModel.getRGBdefault(), paramArrayOfInt, paramInt3, paramInt4, paramHashtable);
/*     */   }
/*     */ 
/*     */   public synchronized void addConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 262 */     if (this.theConsumers.contains(paramImageConsumer)) {
/* 263 */       return;
/*     */     }
/* 265 */     this.theConsumers.addElement(paramImageConsumer);
/*     */     try {
/* 267 */       initConsumer(paramImageConsumer);
/* 268 */       sendPixels(paramImageConsumer, 0, 0, this.width, this.height);
/* 269 */       if (isConsumer(paramImageConsumer)) {
/* 270 */         paramImageConsumer.imageComplete(this.animating ? 2 : 3);
/*     */ 
/* 273 */         if ((!this.animating) && (isConsumer(paramImageConsumer))) {
/* 274 */           paramImageConsumer.imageComplete(1);
/* 275 */           removeConsumer(paramImageConsumer);
/*     */         }
/*     */       }
/*     */     } catch (Exception localException) {
/* 279 */       if (isConsumer(paramImageConsumer))
/* 280 */         paramImageConsumer.imageComplete(1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean isConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 294 */     return this.theConsumers.contains(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public synchronized void removeConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 304 */     this.theConsumers.removeElement(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public void startProduction(ImageConsumer paramImageConsumer)
/*     */   {
/* 316 */     addConsumer(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void setAnimated(boolean paramBoolean)
/*     */   {
/* 344 */     this.animating = paramBoolean;
/* 345 */     if (!this.animating) {
/* 346 */       Enumeration localEnumeration = this.theConsumers.elements();
/* 347 */       while (localEnumeration.hasMoreElements()) {
/* 348 */         ImageConsumer localImageConsumer = (ImageConsumer)localEnumeration.nextElement();
/* 349 */         localImageConsumer.imageComplete(3);
/* 350 */         if (isConsumer(localImageConsumer)) {
/* 351 */           localImageConsumer.imageComplete(1);
/*     */         }
/*     */       }
/* 354 */       this.theConsumers.removeAllElements();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setFullBufferUpdates(boolean paramBoolean)
/*     */   {
/* 374 */     if (this.fullbuffers == paramBoolean) {
/* 375 */       return;
/*     */     }
/* 377 */     this.fullbuffers = paramBoolean;
/* 378 */     if (this.animating) {
/* 379 */       Enumeration localEnumeration = this.theConsumers.elements();
/* 380 */       while (localEnumeration.hasMoreElements()) {
/* 381 */         ImageConsumer localImageConsumer = (ImageConsumer)localEnumeration.nextElement();
/* 382 */         localImageConsumer.setHints(paramBoolean ? 6 : 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void newPixels()
/*     */   {
/* 401 */     newPixels(0, 0, this.width, this.height, true);
/*     */   }
/*     */ 
/*     */   public synchronized void newPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 425 */     newPixels(paramInt1, paramInt2, paramInt3, paramInt4, true);
/*     */   }
/*     */ 
/*     */   public synchronized void newPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 453 */     if (this.animating) {
/* 454 */       if (this.fullbuffers) {
/* 455 */         paramInt1 = paramInt2 = 0;
/* 456 */         paramInt3 = this.width;
/* 457 */         paramInt4 = this.height;
/*     */       } else {
/* 459 */         if (paramInt1 < 0) {
/* 460 */           paramInt3 += paramInt1;
/* 461 */           paramInt1 = 0;
/*     */         }
/* 463 */         if (paramInt1 + paramInt3 > this.width) {
/* 464 */           paramInt3 = this.width - paramInt1;
/*     */         }
/* 466 */         if (paramInt2 < 0) {
/* 467 */           paramInt4 += paramInt2;
/* 468 */           paramInt2 = 0;
/*     */         }
/* 470 */         if (paramInt2 + paramInt4 > this.height) {
/* 471 */           paramInt4 = this.height - paramInt2;
/*     */         }
/*     */       }
/* 474 */       if (((paramInt3 <= 0) || (paramInt4 <= 0)) && (!paramBoolean)) {
/* 475 */         return;
/*     */       }
/* 477 */       Enumeration localEnumeration = this.theConsumers.elements();
/* 478 */       while (localEnumeration.hasMoreElements()) {
/* 479 */         ImageConsumer localImageConsumer = (ImageConsumer)localEnumeration.nextElement();
/* 480 */         if ((paramInt3 > 0) && (paramInt4 > 0)) {
/* 481 */           sendPixels(localImageConsumer, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */         }
/* 483 */         if ((paramBoolean) && (isConsumer(localImageConsumer)))
/* 484 */           localImageConsumer.imageComplete(2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void newPixels(byte[] paramArrayOfByte, ColorModel paramColorModel, int paramInt1, int paramInt2)
/*     */   {
/* 506 */     this.pixels = paramArrayOfByte;
/* 507 */     this.model = paramColorModel;
/* 508 */     this.pixeloffset = paramInt1;
/* 509 */     this.pixelscan = paramInt2;
/* 510 */     newPixels();
/*     */   }
/*     */ 
/*     */   public synchronized void newPixels(int[] paramArrayOfInt, ColorModel paramColorModel, int paramInt1, int paramInt2)
/*     */   {
/* 529 */     this.pixels = paramArrayOfInt;
/* 530 */     this.model = paramColorModel;
/* 531 */     this.pixeloffset = paramInt1;
/* 532 */     this.pixelscan = paramInt2;
/* 533 */     newPixels();
/*     */   }
/*     */ 
/*     */   private void initConsumer(ImageConsumer paramImageConsumer) {
/* 537 */     if (isConsumer(paramImageConsumer)) {
/* 538 */       paramImageConsumer.setDimensions(this.width, this.height);
/*     */     }
/* 540 */     if (isConsumer(paramImageConsumer)) {
/* 541 */       paramImageConsumer.setProperties(this.properties);
/*     */     }
/* 543 */     if (isConsumer(paramImageConsumer)) {
/* 544 */       paramImageConsumer.setColorModel(this.model);
/*     */     }
/* 546 */     if (isConsumer(paramImageConsumer))
/* 547 */       paramImageConsumer.setHints(this.animating ? 1 : this.fullbuffers ? 6 : 30);
/*     */   }
/*     */ 
/*     */   private void sendPixels(ImageConsumer paramImageConsumer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 560 */     int i = this.pixeloffset + this.pixelscan * paramInt2 + paramInt1;
/* 561 */     if (isConsumer(paramImageConsumer))
/* 562 */       if ((this.pixels instanceof byte[])) {
/* 563 */         paramImageConsumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, this.model, (byte[])this.pixels, i, this.pixelscan);
/*     */       }
/*     */       else
/* 566 */         paramImageConsumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, this.model, (int[])this.pixels, i, this.pixelscan);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.MemoryImageSource
 * JD-Core Version:    0.6.2
 */