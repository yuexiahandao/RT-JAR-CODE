/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public abstract class InputStreamImageSource
/*     */   implements ImageProducer, ImageFetchable
/*     */ {
/*     */   ImageConsumerQueue consumers;
/*     */   ImageDecoder decoder;
/*     */   ImageDecoder decoders;
/*  42 */   boolean awaitingFetch = false;
/*     */ 
/*     */   abstract boolean checkSecurity(Object paramObject, boolean paramBoolean);
/*     */ 
/*     */   int countConsumers(ImageConsumerQueue paramImageConsumerQueue) {
/*  47 */     int i = 0;
/*  48 */     while (paramImageConsumerQueue != null) {
/*  49 */       i++;
/*  50 */       paramImageConsumerQueue = paramImageConsumerQueue.next;
/*     */     }
/*  52 */     return i;
/*     */   }
/*     */ 
/*     */   synchronized int countConsumers() {
/*  56 */     ImageDecoder localImageDecoder = this.decoders;
/*  57 */     int i = countConsumers(this.consumers);
/*  58 */     while (localImageDecoder != null) {
/*  59 */       i += countConsumers(localImageDecoder.queue);
/*  60 */       localImageDecoder = localImageDecoder.next;
/*     */     }
/*  62 */     return i;
/*     */   }
/*     */ 
/*     */   public void addConsumer(ImageConsumer paramImageConsumer) {
/*  66 */     addConsumer(paramImageConsumer, false);
/*     */   }
/*     */ 
/*     */   synchronized void printQueue(ImageConsumerQueue paramImageConsumerQueue, String paramString) {
/*  70 */     while (paramImageConsumerQueue != null) {
/*  71 */       System.out.println(paramString + paramImageConsumerQueue);
/*  72 */       paramImageConsumerQueue = paramImageConsumerQueue.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void printQueues(String paramString) {
/*  77 */     System.out.println(paramString + "[ -----------");
/*  78 */     printQueue(this.consumers, "  ");
/*  79 */     for (ImageDecoder localImageDecoder = this.decoders; localImageDecoder != null; localImageDecoder = localImageDecoder.next) {
/*  80 */       System.out.println("    " + localImageDecoder);
/*  81 */       printQueue(localImageDecoder.queue, "      ");
/*     */     }
/*  83 */     System.out.println("----------- ]" + paramString);
/*     */   }
/*     */ 
/*     */   synchronized void addConsumer(ImageConsumer paramImageConsumer, boolean paramBoolean) {
/*  87 */     checkSecurity(null, false);
/*  88 */     for (Object localObject1 = this.decoders; localObject1 != null; localObject1 = ((ImageDecoder)localObject1).next) {
/*  89 */       if (((ImageDecoder)localObject1).isConsumer(paramImageConsumer))
/*     */       {
/*  91 */         return;
/*     */       }
/*     */     }
/*  94 */     localObject1 = this.consumers;
/*  95 */     while ((localObject1 != null) && (((ImageConsumerQueue)localObject1).consumer != paramImageConsumer)) {
/*  96 */       localObject1 = ((ImageConsumerQueue)localObject1).next;
/*     */     }
/*  98 */     if (localObject1 == null) {
/*  99 */       localObject1 = new ImageConsumerQueue(this, paramImageConsumer);
/* 100 */       ((ImageConsumerQueue)localObject1).next = this.consumers;
/* 101 */       this.consumers = ((ImageConsumerQueue)localObject1);
/*     */     } else {
/* 103 */       if (!((ImageConsumerQueue)localObject1).secure) {
/* 104 */         Object localObject2 = null;
/* 105 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 106 */         if (localSecurityManager != null) {
/* 107 */           localObject2 = localSecurityManager.getSecurityContext();
/*     */         }
/* 109 */         if (((ImageConsumerQueue)localObject1).securityContext == null) {
/* 110 */           ((ImageConsumerQueue)localObject1).securityContext = localObject2;
/* 111 */         } else if (!((ImageConsumerQueue)localObject1).securityContext.equals(localObject2))
/*     */         {
/* 117 */           errorConsumer((ImageConsumerQueue)localObject1, false);
/* 118 */           throw new SecurityException("Applets are trading image data!");
/*     */         }
/*     */       }
/* 121 */       ((ImageConsumerQueue)localObject1).interested = true;
/*     */     }
/* 123 */     if ((paramBoolean) && (this.decoder == null))
/* 124 */       startProduction();
/*     */   }
/*     */ 
/*     */   public synchronized boolean isConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 129 */     for (ImageDecoder localImageDecoder = this.decoders; localImageDecoder != null; localImageDecoder = localImageDecoder.next) {
/* 130 */       if (localImageDecoder.isConsumer(paramImageConsumer)) {
/* 131 */         return true;
/*     */       }
/*     */     }
/* 134 */     return ImageConsumerQueue.isConsumer(this.consumers, paramImageConsumer);
/*     */   }
/*     */ 
/*     */   private void errorAllConsumers(ImageConsumerQueue paramImageConsumerQueue, boolean paramBoolean) {
/* 138 */     while (paramImageConsumerQueue != null) {
/* 139 */       if (paramImageConsumerQueue.interested) {
/* 140 */         errorConsumer(paramImageConsumerQueue, paramBoolean);
/*     */       }
/* 142 */       paramImageConsumerQueue = paramImageConsumerQueue.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void errorConsumer(ImageConsumerQueue paramImageConsumerQueue, boolean paramBoolean) {
/* 147 */     paramImageConsumerQueue.consumer.imageComplete(1);
/* 148 */     if ((paramBoolean) && ((paramImageConsumerQueue.consumer instanceof ImageRepresentation))) {
/* 149 */       ((ImageRepresentation)paramImageConsumerQueue.consumer).image.flush();
/*     */     }
/* 151 */     removeConsumer(paramImageConsumerQueue.consumer);
/*     */   }
/*     */ 
/*     */   public synchronized void removeConsumer(ImageConsumer paramImageConsumer) {
/* 155 */     for (ImageDecoder localImageDecoder = this.decoders; localImageDecoder != null; localImageDecoder = localImageDecoder.next) {
/* 156 */       localImageDecoder.removeConsumer(paramImageConsumer);
/*     */     }
/* 158 */     this.consumers = ImageConsumerQueue.removeConsumer(this.consumers, paramImageConsumer, false);
/*     */   }
/*     */ 
/*     */   public void startProduction(ImageConsumer paramImageConsumer) {
/* 162 */     addConsumer(paramImageConsumer, true);
/*     */   }
/*     */ 
/*     */   private synchronized void startProduction() {
/* 166 */     if (!this.awaitingFetch)
/* 167 */       if (ImageFetcher.add(this)) {
/* 168 */         this.awaitingFetch = true;
/*     */       } else {
/* 170 */         ImageConsumerQueue localImageConsumerQueue = this.consumers;
/* 171 */         this.consumers = null;
/* 172 */         errorAllConsumers(localImageConsumerQueue, false);
/*     */       }
/*     */   }
/*     */ 
/*     */   private synchronized void stopProduction()
/*     */   {
/* 178 */     if (this.awaitingFetch) {
/* 179 */       ImageFetcher.remove(this);
/* 180 */       this.awaitingFetch = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected abstract ImageDecoder getDecoder();
/*     */ 
/*     */   protected ImageDecoder decoderForType(InputStream paramInputStream, String paramString)
/*     */   {
/* 210 */     return null;
/*     */   }
/*     */ 
/*     */   protected ImageDecoder getDecoder(InputStream paramInputStream) {
/* 214 */     if (!paramInputStream.markSupported()) {
/* 215 */       paramInputStream = new BufferedInputStream(paramInputStream);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 220 */       paramInputStream.mark(8);
/* 221 */       int i = paramInputStream.read();
/* 222 */       int j = paramInputStream.read();
/* 223 */       int k = paramInputStream.read();
/* 224 */       int m = paramInputStream.read();
/* 225 */       int n = paramInputStream.read();
/* 226 */       int i1 = paramInputStream.read();
/*     */ 
/* 228 */       int i2 = paramInputStream.read();
/* 229 */       int i3 = paramInputStream.read();
/*     */ 
/* 231 */       paramInputStream.reset();
/* 232 */       paramInputStream.mark(-1);
/*     */ 
/* 234 */       if ((i == 71) && (j == 73) && (k == 70) && (m == 56))
/* 235 */         return new GifImageDecoder(this, paramInputStream);
/* 236 */       if ((i == 255) && (j == 216) && (k == 255))
/* 237 */         return new JPEGImageDecoder(this, paramInputStream);
/* 238 */       if ((i == 35) && (j == 100) && (k == 101) && (m == 102)) {
/* 239 */         return new XbmImageDecoder(this, paramInputStream);
/*     */       }
/*     */ 
/* 244 */       if ((i == 137) && (j == 80) && (k == 78) && (m == 71) && (n == 13) && (i1 == 10) && (i2 == 26) && (i3 == 10))
/*     */       {
/* 247 */         return new PNGImageDecoder(this, paramInputStream);
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */   public void doFetch() {
/* 257 */     synchronized (this) {
/* 258 */       if (this.consumers == null) {
/* 259 */         this.awaitingFetch = false;
/* 260 */         return;
/*     */       }
/*     */     }
/* 263 */     ??? = getDecoder();
/* 264 */     if (??? == null) {
/* 265 */       badDecoder();
/*     */     } else {
/* 267 */       setDecoder((ImageDecoder)???);
/*     */       try {
/* 269 */         ((ImageDecoder)???).produceImage();
/*     */       } catch (IOException localIOException) {
/* 271 */         localIOException.printStackTrace();
/*     */       }
/*     */       catch (ImageFormatException localImageFormatException) {
/* 274 */         localImageFormatException.printStackTrace();
/*     */       }
/*     */       finally {
/* 277 */         removeDecoder((ImageDecoder)???);
/* 278 */         if ((Thread.currentThread().isInterrupted()) || (!Thread.currentThread().isAlive()))
/* 279 */           errorAllConsumers(((ImageDecoder)???).queue, true);
/*     */         else
/* 281 */           errorAllConsumers(((ImageDecoder)???).queue, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void badDecoder()
/*     */   {
/*     */     ImageConsumerQueue localImageConsumerQueue;
/* 289 */     synchronized (this) {
/* 290 */       localImageConsumerQueue = this.consumers;
/* 291 */       this.consumers = null;
/* 292 */       this.awaitingFetch = false;
/*     */     }
/* 294 */     errorAllConsumers(localImageConsumerQueue, false);
/*     */   }
/*     */ 
/*     */   private void setDecoder(ImageDecoder paramImageDecoder)
/*     */   {
/*     */     ImageConsumerQueue localImageConsumerQueue;
/* 299 */     synchronized (this) {
/* 300 */       paramImageDecoder.next = this.decoders;
/* 301 */       this.decoders = paramImageDecoder;
/* 302 */       this.decoder = paramImageDecoder;
/* 303 */       localImageConsumerQueue = this.consumers;
/* 304 */       paramImageDecoder.queue = localImageConsumerQueue;
/* 305 */       this.consumers = null;
/* 306 */       this.awaitingFetch = false;
/*     */     }
/* 308 */     while (localImageConsumerQueue != null) {
/* 309 */       if (localImageConsumerQueue.interested)
/*     */       {
/* 312 */         if (!checkSecurity(localImageConsumerQueue.securityContext, true)) {
/* 313 */           errorConsumer(localImageConsumerQueue, false);
/*     */         }
/*     */       }
/* 316 */       localImageConsumerQueue = localImageConsumerQueue.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void removeDecoder(ImageDecoder paramImageDecoder) {
/* 321 */     doneDecoding(paramImageDecoder);
/* 322 */     Object localObject = null;
/* 323 */     for (ImageDecoder localImageDecoder = this.decoders; localImageDecoder != null; localImageDecoder = localImageDecoder.next) {
/* 324 */       if (localImageDecoder == paramImageDecoder) {
/* 325 */         if (localObject == null) {
/* 326 */           this.decoders = localImageDecoder.next; break;
/*     */         }
/* 328 */         localObject.next = localImageDecoder.next;
/*     */ 
/* 330 */         break;
/*     */       }
/* 332 */       localObject = localImageDecoder;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void doneDecoding(ImageDecoder paramImageDecoder) {
/* 337 */     if (this.decoder == paramImageDecoder) {
/* 338 */       this.decoder = null;
/* 339 */       if (this.consumers != null)
/* 340 */         startProduction();
/*     */     }
/*     */   }
/*     */ 
/*     */   void latchConsumers(ImageDecoder paramImageDecoder)
/*     */   {
/* 346 */     doneDecoding(paramImageDecoder);
/*     */   }
/*     */ 
/*     */   synchronized void flush() {
/* 350 */     this.decoder = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.InputStreamImageSource
 * JD-Core Version:    0.6.2
 */