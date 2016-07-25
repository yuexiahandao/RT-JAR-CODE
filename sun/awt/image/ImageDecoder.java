/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public abstract class ImageDecoder
/*     */ {
/*     */   InputStreamImageSource source;
/*     */   InputStream input;
/*     */   Thread feeder;
/*     */   protected boolean aborted;
/*     */   protected boolean finished;
/*     */   ImageConsumerQueue queue;
/*     */   ImageDecoder next;
/*     */ 
/*     */   public ImageDecoder(InputStreamImageSource paramInputStreamImageSource, InputStream paramInputStream)
/*     */   {
/*  44 */     this.source = paramInputStreamImageSource;
/*  45 */     this.input = paramInputStream;
/*  46 */     this.feeder = Thread.currentThread();
/*     */   }
/*     */ 
/*     */   public boolean isConsumer(ImageConsumer paramImageConsumer) {
/*  50 */     return ImageConsumerQueue.isConsumer(this.queue, paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public void removeConsumer(ImageConsumer paramImageConsumer) {
/*  54 */     this.queue = ImageConsumerQueue.removeConsumer(this.queue, paramImageConsumer, false);
/*  55 */     if ((!this.finished) && (this.queue == null))
/*  56 */       abort();
/*     */   }
/*     */ 
/*     */   protected ImageConsumerQueue nextConsumer(ImageConsumerQueue paramImageConsumerQueue)
/*     */   {
/*  61 */     synchronized (this.source) {
/*  62 */       if (this.aborted) {
/*  63 */         return null;
/*     */       }
/*  65 */       paramImageConsumerQueue = paramImageConsumerQueue == null ? this.queue : paramImageConsumerQueue.next;
/*  66 */       while (paramImageConsumerQueue != null) {
/*  67 */         if (paramImageConsumerQueue.interested) {
/*  68 */           return paramImageConsumerQueue;
/*     */         }
/*  70 */         paramImageConsumerQueue = paramImageConsumerQueue.next;
/*     */       }
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   protected int setDimensions(int paramInt1, int paramInt2) {
/*  77 */     ImageConsumerQueue localImageConsumerQueue = null;
/*  78 */     int i = 0;
/*  79 */     while ((localImageConsumerQueue = nextConsumer(localImageConsumerQueue)) != null) {
/*  80 */       localImageConsumerQueue.consumer.setDimensions(paramInt1, paramInt2);
/*  81 */       i++;
/*     */     }
/*  83 */     return i;
/*     */   }
/*     */ 
/*     */   protected int setProperties(Hashtable paramHashtable) {
/*  87 */     ImageConsumerQueue localImageConsumerQueue = null;
/*  88 */     int i = 0;
/*  89 */     while ((localImageConsumerQueue = nextConsumer(localImageConsumerQueue)) != null) {
/*  90 */       localImageConsumerQueue.consumer.setProperties(paramHashtable);
/*  91 */       i++;
/*     */     }
/*  93 */     return i;
/*     */   }
/*     */ 
/*     */   protected int setColorModel(ColorModel paramColorModel) {
/*  97 */     ImageConsumerQueue localImageConsumerQueue = null;
/*  98 */     int i = 0;
/*  99 */     while ((localImageConsumerQueue = nextConsumer(localImageConsumerQueue)) != null) {
/* 100 */       localImageConsumerQueue.consumer.setColorModel(paramColorModel);
/* 101 */       i++;
/*     */     }
/* 103 */     return i;
/*     */   }
/*     */ 
/*     */   protected int setHints(int paramInt) {
/* 107 */     ImageConsumerQueue localImageConsumerQueue = null;
/* 108 */     int i = 0;
/* 109 */     while ((localImageConsumerQueue = nextConsumer(localImageConsumerQueue)) != null) {
/* 110 */       localImageConsumerQueue.consumer.setHints(paramInt);
/* 111 */       i++;
/*     */     }
/* 113 */     return i;
/*     */   }
/*     */ 
/*     */   protected void headerComplete() {
/* 117 */     this.feeder.setPriority(3);
/*     */   }
/*     */ 
/*     */   protected int setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 122 */     this.source.latchConsumers(this);
/* 123 */     ImageConsumerQueue localImageConsumerQueue = null;
/* 124 */     int i = 0;
/* 125 */     while ((localImageConsumerQueue = nextConsumer(localImageConsumerQueue)) != null) {
/* 126 */       localImageConsumerQueue.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/* 127 */       i++;
/*     */     }
/* 129 */     return i;
/*     */   }
/*     */ 
/*     */   protected int setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 134 */     this.source.latchConsumers(this);
/* 135 */     ImageConsumerQueue localImageConsumerQueue = null;
/* 136 */     int i = 0;
/* 137 */     while ((localImageConsumerQueue = nextConsumer(localImageConsumerQueue)) != null) {
/* 138 */       localImageConsumerQueue.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/* 139 */       i++;
/*     */     }
/* 141 */     return i;
/*     */   }
/*     */ 
/*     */   protected int imageComplete(int paramInt, boolean paramBoolean) {
/* 145 */     this.source.latchConsumers(this);
/* 146 */     if (paramBoolean) {
/* 147 */       this.finished = true;
/* 148 */       this.source.doneDecoding(this);
/*     */     }
/* 150 */     ImageConsumerQueue localImageConsumerQueue = null;
/* 151 */     int i = 0;
/* 152 */     while ((localImageConsumerQueue = nextConsumer(localImageConsumerQueue)) != null) {
/* 153 */       localImageConsumerQueue.consumer.imageComplete(paramInt);
/* 154 */       i++;
/*     */     }
/* 156 */     return i;
/*     */   }
/*     */ 
/*     */   public abstract void produceImage() throws IOException, ImageFormatException;
/*     */ 
/*     */   public void abort()
/*     */   {
/* 163 */     this.aborted = true;
/* 164 */     this.source.doneDecoding(this);
/* 165 */     close();
/* 166 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 169 */         ImageDecoder.this.feeder.interrupt();
/* 170 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public synchronized void close() {
/* 176 */     if (this.input != null)
/*     */       try {
/* 178 */         this.input.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ImageDecoder
 * JD-Core Version:    0.6.2
 */