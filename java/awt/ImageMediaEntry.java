/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ class ImageMediaEntry extends MediaEntry
/*     */   implements ImageObserver, Serializable
/*     */ {
/*     */   Image image;
/*     */   int width;
/*     */   int height;
/*     */   private static final long serialVersionUID = 4739377000350280650L;
/*     */ 
/*     */   ImageMediaEntry(MediaTracker paramMediaTracker, Image paramImage, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 892 */     super(paramMediaTracker, paramInt1);
/* 893 */     this.image = paramImage;
/* 894 */     this.width = paramInt2;
/* 895 */     this.height = paramInt3;
/*     */   }
/*     */ 
/*     */   boolean matches(Image paramImage, int paramInt1, int paramInt2) {
/* 899 */     return (this.image == paramImage) && (this.width == paramInt1) && (this.height == paramInt2);
/*     */   }
/*     */ 
/*     */   Object getMedia() {
/* 903 */     return this.image;
/*     */   }
/*     */ 
/*     */   synchronized int getStatus(boolean paramBoolean1, boolean paramBoolean2) {
/* 907 */     if (paramBoolean2) {
/* 908 */       int i = this.tracker.target.checkImage(this.image, this.width, this.height, null);
/* 909 */       int j = parseflags(i);
/* 910 */       if (j == 0) {
/* 911 */         if ((this.status & 0xC) != 0)
/* 912 */           setStatus(2);
/*     */       }
/* 914 */       else if (j != this.status) {
/* 915 */         setStatus(j);
/*     */       }
/*     */     }
/* 918 */     return super.getStatus(paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   void startLoad() {
/* 922 */     if (this.tracker.target.prepareImage(this.image, this.width, this.height, this))
/* 923 */       setStatus(8);
/*     */   }
/*     */ 
/*     */   int parseflags(int paramInt)
/*     */   {
/* 928 */     if ((paramInt & 0x40) != 0)
/* 929 */       return 4;
/* 930 */     if ((paramInt & 0x80) != 0)
/* 931 */       return 2;
/* 932 */     if ((paramInt & 0x30) != 0) {
/* 933 */       return 8;
/*     */     }
/* 935 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 940 */     if (this.cancelled) {
/* 941 */       return false;
/*     */     }
/* 943 */     int i = parseflags(paramInt1);
/* 944 */     if ((i != 0) && (i != this.status)) {
/* 945 */       setStatus(i);
/*     */     }
/* 947 */     return (this.status & 0x1) != 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.ImageMediaEntry
 * JD-Core Version:    0.6.2
 */