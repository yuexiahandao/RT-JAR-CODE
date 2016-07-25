/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.lang.ref.WeakReference;
/*     */ 
/*     */ public abstract class ImageWatched
/*     */ {
/*  33 */   public static Link endlink = new Link();
/*     */   public Link watcherList;
/*     */ 
/*     */   public ImageWatched()
/*     */   {
/*  38 */     this.watcherList = endlink;
/*     */   }
/*     */ 
/*     */   public synchronized void addWatcher(ImageObserver paramImageObserver)
/*     */   {
/* 143 */     if ((paramImageObserver != null) && (!isWatcher(paramImageObserver))) {
/* 144 */       this.watcherList = new WeakLink(paramImageObserver, this.watcherList);
/*     */     }
/* 146 */     this.watcherList = this.watcherList.removeWatcher(null);
/*     */   }
/*     */ 
/*     */   public synchronized boolean isWatcher(ImageObserver paramImageObserver) {
/* 150 */     return this.watcherList.isWatcher(paramImageObserver);
/*     */   }
/*     */ 
/*     */   public void removeWatcher(ImageObserver paramImageObserver) {
/* 154 */     synchronized (this) {
/* 155 */       this.watcherList = this.watcherList.removeWatcher(paramImageObserver);
/*     */     }
/* 157 */     if (this.watcherList == endlink)
/* 158 */       notifyWatcherListEmpty();
/*     */   }
/*     */ 
/*     */   public boolean isWatcherListEmpty()
/*     */   {
/* 163 */     synchronized (this) {
/* 164 */       this.watcherList = this.watcherList.removeWatcher(null);
/*     */     }
/* 166 */     return this.watcherList == endlink;
/*     */   }
/*     */ 
/*     */   public void newInfo(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/* 170 */     if (this.watcherList.newInfo(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5))
/*     */     {
/* 172 */       removeWatcher(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void notifyWatcherListEmpty();
/*     */ 
/*     */   public static class Link
/*     */   {
/*     */     public boolean isWatcher(ImageObserver paramImageObserver)
/*     */     {
/*  54 */       return false;
/*     */     }
/*     */ 
/*     */     public Link removeWatcher(ImageObserver paramImageObserver)
/*     */     {
/*  69 */       return this;
/*     */     }
/*     */ 
/*     */     public boolean newInfo(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     {
/*  84 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class WeakLink extends ImageWatched.Link
/*     */   {
/*     */     private WeakReference<ImageObserver> myref;
/*     */     private ImageWatched.Link next;
/*     */ 
/*     */     public WeakLink(ImageObserver paramImageObserver, ImageWatched.Link paramLink)
/*     */     {
/*  97 */       this.myref = new WeakReference(paramImageObserver);
/*  98 */       this.next = paramLink;
/*     */     }
/*     */ 
/*     */     public boolean isWatcher(ImageObserver paramImageObserver) {
/* 102 */       return (this.myref.get() == paramImageObserver) || (this.next.isWatcher(paramImageObserver));
/*     */     }
/*     */ 
/*     */     public ImageWatched.Link removeWatcher(ImageObserver paramImageObserver) {
/* 106 */       ImageObserver localImageObserver = (ImageObserver)this.myref.get();
/* 107 */       if (localImageObserver == null)
/*     */       {
/* 109 */         return this.next.removeWatcher(paramImageObserver);
/*     */       }
/*     */ 
/* 113 */       if (localImageObserver == paramImageObserver)
/*     */       {
/* 115 */         return this.next;
/*     */       }
/*     */ 
/* 119 */       this.next = this.next.removeWatcher(paramImageObserver);
/* 120 */       return this;
/*     */     }
/*     */ 
/*     */     public boolean newInfo(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     {
/* 127 */       boolean bool = this.next.newInfo(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/* 128 */       ImageObserver localImageObserver = (ImageObserver)this.myref.get();
/* 129 */       if (localImageObserver == null)
/*     */       {
/* 131 */         bool = true;
/* 132 */       } else if (!localImageObserver.imageUpdate(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5))
/*     */       {
/* 135 */         this.myref.clear();
/* 136 */         bool = true;
/*     */       }
/* 138 */       return bool;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ImageWatched
 * JD-Core Version:    0.6.2
 */