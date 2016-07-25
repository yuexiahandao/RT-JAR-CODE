/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ class MetalBumps
/*     */   implements Icon
/*     */ {
/*  45 */   static final Color ALPHA = new Color(0, 0, 0, 0);
/*     */   protected int xBumps;
/*     */   protected int yBumps;
/*     */   protected Color topColor;
/*     */   protected Color shadowColor;
/*     */   protected Color backColor;
/*  53 */   private static final Object METAL_BUMPS = new Object();
/*     */   protected BumpBuffer buffer;
/*     */ 
/*     */   public MetalBumps(int paramInt1, int paramInt2, Color paramColor1, Color paramColor2, Color paramColor3)
/*     */   {
/*  63 */     setBumpArea(paramInt1, paramInt2);
/*  64 */     setBumpColors(paramColor1, paramColor2, paramColor3);
/*     */   }
/*     */ 
/*     */   private static BumpBuffer createBuffer(GraphicsConfiguration paramGraphicsConfiguration, Color paramColor1, Color paramColor2, Color paramColor3)
/*     */   {
/*  69 */     AppContext localAppContext = AppContext.getAppContext();
/*  70 */     Object localObject1 = (List)localAppContext.get(METAL_BUMPS);
/*  71 */     if (localObject1 == null) {
/*  72 */       localObject1 = new ArrayList();
/*  73 */       localAppContext.put(METAL_BUMPS, localObject1);
/*     */     }
/*  75 */     for (Object localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { BumpBuffer localBumpBuffer = (BumpBuffer)((Iterator)localObject2).next();
/*  76 */       if (localBumpBuffer.hasSameConfiguration(paramGraphicsConfiguration, paramColor1, paramColor2, paramColor3)) {
/*  77 */         return localBumpBuffer;
/*     */       }
/*     */     }
/*  80 */     localObject2 = new BumpBuffer(paramGraphicsConfiguration, paramColor1, paramColor2, paramColor3);
/*  81 */     ((List)localObject1).add(localObject2);
/*  82 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public void setBumpArea(Dimension paramDimension) {
/*  86 */     setBumpArea(paramDimension.width, paramDimension.height);
/*     */   }
/*     */ 
/*     */   public void setBumpArea(int paramInt1, int paramInt2) {
/*  90 */     this.xBumps = (paramInt1 / 2);
/*  91 */     this.yBumps = (paramInt2 / 2);
/*     */   }
/*     */ 
/*     */   public void setBumpColors(Color paramColor1, Color paramColor2, Color paramColor3) {
/*  95 */     this.topColor = paramColor1;
/*  96 */     this.shadowColor = paramColor2;
/*  97 */     if (paramColor3 == null) {
/*  98 */       this.backColor = ALPHA;
/*     */     }
/*     */     else
/* 101 */       this.backColor = paramColor3;
/*     */   }
/*     */ 
/*     */   public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 106 */     GraphicsConfiguration localGraphicsConfiguration = (paramGraphics instanceof Graphics2D) ? ((Graphics2D)paramGraphics).getDeviceConfiguration() : null;
/*     */ 
/* 109 */     if ((this.buffer == null) || (!this.buffer.hasSameConfiguration(localGraphicsConfiguration, this.topColor, this.shadowColor, this.backColor))) {
/* 110 */       this.buffer = createBuffer(localGraphicsConfiguration, this.topColor, this.shadowColor, this.backColor);
/*     */     }
/*     */ 
/* 113 */     int i = 64;
/* 114 */     int j = 64;
/* 115 */     int k = getIconWidth();
/* 116 */     int m = getIconHeight();
/* 117 */     int n = paramInt1 + k;
/* 118 */     int i1 = paramInt2 + m;
/* 119 */     int i2 = paramInt1;
/*     */ 
/* 121 */     while (paramInt2 < i1) {
/* 122 */       int i3 = Math.min(i1 - paramInt2, j);
/* 123 */       for (paramInt1 = i2; paramInt1 < n; paramInt1 += i) {
/* 124 */         int i4 = Math.min(n - paramInt1, i);
/* 125 */         paramGraphics.drawImage(this.buffer.getImage(), paramInt1, paramInt2, paramInt1 + i4, paramInt2 + i3, 0, 0, i4, i3, null);
/*     */       }
/*     */ 
/* 130 */       paramInt2 += j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getIconWidth() {
/* 135 */     return this.xBumps * 2;
/*     */   }
/*     */ 
/*     */   public int getIconHeight() {
/* 139 */     return this.yBumps * 2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalBumps
 * JD-Core Version:    0.6.2
 */