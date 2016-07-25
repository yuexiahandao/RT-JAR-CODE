/*     */ package java.awt.image;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class FilteredImageSource
/*     */   implements ImageProducer
/*     */ {
/*     */   ImageProducer src;
/*     */   ImageFilter filter;
/*     */   private Hashtable proxies;
/*     */ 
/*     */   public FilteredImageSource(ImageProducer paramImageProducer, ImageFilter paramImageFilter)
/*     */   {
/*  67 */     this.src = paramImageProducer;
/*  68 */     this.filter = paramImageFilter;
/*     */   }
/*     */ 
/*     */   public synchronized void addConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/*  96 */     if (this.proxies == null) {
/*  97 */       this.proxies = new Hashtable();
/*     */     }
/*  99 */     if (!this.proxies.containsKey(paramImageConsumer)) {
/* 100 */       ImageFilter localImageFilter = this.filter.getFilterInstance(paramImageConsumer);
/* 101 */       this.proxies.put(paramImageConsumer, localImageFilter);
/* 102 */       this.src.addConsumer(localImageFilter);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean isConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 122 */     return (this.proxies != null) && (this.proxies.containsKey(paramImageConsumer));
/*     */   }
/*     */ 
/*     */   public synchronized void removeConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 139 */     if (this.proxies != null) {
/* 140 */       ImageFilter localImageFilter = (ImageFilter)this.proxies.get(paramImageConsumer);
/* 141 */       if (localImageFilter != null) {
/* 142 */         this.src.removeConsumer(localImageFilter);
/* 143 */         this.proxies.remove(paramImageConsumer);
/* 144 */         if (this.proxies.isEmpty())
/* 145 */           this.proxies = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startProduction(ImageConsumer paramImageConsumer)
/*     */   {
/* 175 */     if (this.proxies == null) {
/* 176 */       this.proxies = new Hashtable();
/*     */     }
/* 178 */     ImageFilter localImageFilter = (ImageFilter)this.proxies.get(paramImageConsumer);
/* 179 */     if (localImageFilter == null) {
/* 180 */       localImageFilter = this.filter.getFilterInstance(paramImageConsumer);
/* 181 */       this.proxies.put(paramImageConsumer, localImageFilter);
/*     */     }
/* 183 */     this.src.startProduction(localImageFilter);
/*     */   }
/*     */ 
/*     */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*     */   {
/* 202 */     if (this.proxies != null) {
/* 203 */       ImageFilter localImageFilter = (ImageFilter)this.proxies.get(paramImageConsumer);
/* 204 */       if (localImageFilter != null)
/* 205 */         localImageFilter.resendTopDownLeftRight(this.src);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.FilteredImageSource
 * JD-Core Version:    0.6.2
 */