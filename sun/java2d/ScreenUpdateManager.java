/*     */ package sun.java2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import sun.awt.Win32GraphicsConfig;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.d3d.D3DScreenUpdateManager;
/*     */ import sun.java2d.windows.WindowsFlags;
/*     */ 
/*     */ public class ScreenUpdateManager
/*     */ {
/*     */   private static ScreenUpdateManager theInstance;
/*     */ 
/*     */   public synchronized Graphics2D createGraphics(SurfaceData paramSurfaceData, WComponentPeer paramWComponentPeer, Color paramColor1, Color paramColor2, Font paramFont)
/*     */   {
/*  63 */     return new SunGraphics2D(paramSurfaceData, paramColor1, paramColor2, paramFont);
/*     */   }
/*     */ 
/*     */   public SurfaceData createScreenSurface(Win32GraphicsConfig paramWin32GraphicsConfig, WComponentPeer paramWComponentPeer, int paramInt, boolean paramBoolean)
/*     */   {
/*  86 */     return paramWin32GraphicsConfig.createSurfaceData(paramWComponentPeer, paramInt);
/*     */   }
/*     */ 
/*     */   public void dropScreenSurface(SurfaceData paramSurfaceData)
/*     */   {
/*     */   }
/*     */ 
/*     */   public SurfaceData getReplacementScreenSurface(WComponentPeer paramWComponentPeer, SurfaceData paramSurfaceData)
/*     */   {
/* 113 */     SurfaceData localSurfaceData = paramWComponentPeer.getSurfaceData();
/* 114 */     if ((localSurfaceData == null) || (localSurfaceData.isValid())) {
/* 115 */       return localSurfaceData;
/*     */     }
/* 117 */     paramWComponentPeer.replaceSurfaceData();
/* 118 */     return paramWComponentPeer.getSurfaceData();
/*     */   }
/*     */ 
/*     */   public static synchronized ScreenUpdateManager getInstance()
/*     */   {
/* 127 */     if (theInstance == null) {
/* 128 */       if (WindowsFlags.isD3DEnabled())
/* 129 */         theInstance = new D3DScreenUpdateManager();
/*     */       else {
/* 131 */         theInstance = new ScreenUpdateManager();
/*     */       }
/*     */     }
/* 134 */     return theInstance;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.ScreenUpdateManager
 * JD-Core Version:    0.6.2
 */