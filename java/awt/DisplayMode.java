/*     */ package java.awt;
/*     */ 
/*     */ public final class DisplayMode
/*     */ {
/*     */   private Dimension size;
/*     */   private int bitDepth;
/*     */   private int refreshRate;
/*     */   public static final int BIT_DEPTH_MULTI = -1;
/*     */   public static final int REFRESH_RATE_UNKNOWN = 0;
/*     */ 
/*     */   public DisplayMode(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  66 */     this.size = new Dimension(paramInt1, paramInt2);
/*  67 */     this.bitDepth = paramInt3;
/*  68 */     this.refreshRate = paramInt4;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/*  76 */     return this.size.height;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/*  84 */     return this.size.width;
/*     */   }
/*     */ 
/*     */   public int getBitDepth()
/*     */   {
/* 103 */     return this.bitDepth;
/*     */   }
/*     */ 
/*     */   public int getRefreshRate()
/*     */   {
/* 120 */     return this.refreshRate;
/*     */   }
/*     */ 
/*     */   public boolean equals(DisplayMode paramDisplayMode)
/*     */   {
/* 128 */     if (paramDisplayMode == null) {
/* 129 */       return false;
/*     */     }
/* 131 */     return (getHeight() == paramDisplayMode.getHeight()) && (getWidth() == paramDisplayMode.getWidth()) && (getBitDepth() == paramDisplayMode.getBitDepth()) && (getRefreshRate() == paramDisplayMode.getRefreshRate());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 141 */     if ((paramObject instanceof DisplayMode)) {
/* 142 */       return equals((DisplayMode)paramObject);
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 152 */     return getWidth() + getHeight() + getBitDepth() * 7 + getRefreshRate() * 13;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.DisplayMode
 * JD-Core Version:    0.6.2
 */