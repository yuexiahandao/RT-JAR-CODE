/*     */ package sun.java2d.windows;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ComponentColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import sun.awt.Win32GraphicsConfig;
/*     */ import sun.awt.Win32GraphicsDevice;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.InvalidPipeException;
/*     */ import sun.java2d.ScreenUpdateManager;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.SurfaceDataProxy;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.FontInfo;
/*     */ import sun.java2d.loops.GraphicsPrimitive;
/*     */ import sun.java2d.loops.RenderLoops;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.loops.XORComposite;
/*     */ import sun.java2d.pipe.PixelToShapeConverter;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class GDIWindowSurfaceData extends SurfaceData
/*     */ {
/*     */   private WComponentPeer peer;
/*     */   private Win32GraphicsConfig graphicsConfig;
/*     */   private RenderLoops solidloops;
/*     */   public static final String DESC_GDI = "GDI";
/*  63 */   public static final SurfaceType AnyGdi = SurfaceType.IntRgb.deriveSubType("GDI");
/*     */ 
/*  66 */   public static final SurfaceType IntRgbGdi = SurfaceType.IntRgb.deriveSubType("GDI");
/*     */ 
/*  69 */   public static final SurfaceType Ushort565RgbGdi = SurfaceType.Ushort565Rgb.deriveSubType("GDI");
/*     */ 
/*  72 */   public static final SurfaceType Ushort555RgbGdi = SurfaceType.Ushort555Rgb.deriveSubType("GDI");
/*     */ 
/*  75 */   public static final SurfaceType ThreeByteBgrGdi = SurfaceType.ThreeByteBgr.deriveSubType("GDI");
/*     */   protected static GDIRenderer gdiPipe;
/* 150 */   protected static PixelToShapeConverter gdiTxPipe = new PixelToShapeConverter(gdiPipe);
/*     */ 
/*     */   private static native void initIDs(Class paramClass);
/*     */ 
/*     */   public static SurfaceType getSurfaceType(ColorModel paramColorModel)
/*     */   {
/*  89 */     switch (paramColorModel.getPixelSize()) {
/*     */     case 24:
/*     */     case 32:
/*  92 */       if ((paramColorModel instanceof DirectColorModel)) {
/*  93 */         if (((DirectColorModel)paramColorModel).getRedMask() == 16711680) {
/*  94 */           return IntRgbGdi;
/*     */         }
/*  96 */         return SurfaceType.IntRgbx;
/*     */       }
/*     */ 
/*  99 */       return ThreeByteBgrGdi;
/*     */     case 15:
/* 102 */       return Ushort555RgbGdi;
/*     */     case 16:
/* 104 */       if (((paramColorModel instanceof DirectColorModel)) && (((DirectColorModel)paramColorModel).getBlueMask() == 62))
/*     */       {
/* 107 */         return SurfaceType.Ushort555Rgbx;
/*     */       }
/* 109 */       return Ushort565RgbGdi;
/*     */     case 8:
/* 112 */       if ((paramColorModel.getColorSpace().getType() == 6) && ((paramColorModel instanceof ComponentColorModel)))
/*     */       {
/* 114 */         return SurfaceType.ByteGray;
/* 115 */       }if (((paramColorModel instanceof IndexColorModel)) && (isOpaqueGray((IndexColorModel)paramColorModel)))
/*     */       {
/* 117 */         return SurfaceType.Index8Gray;
/*     */       }
/* 119 */       return SurfaceType.ByteIndexedOpaque;
/*     */     }
/*     */ 
/* 122 */     throw new InvalidPipeException("Unsupported bit depth: " + paramColorModel.getPixelSize());
/*     */   }
/*     */ 
/*     */   public static GDIWindowSurfaceData createData(WComponentPeer paramWComponentPeer)
/*     */   {
/* 129 */     SurfaceType localSurfaceType = getSurfaceType(paramWComponentPeer.getDeviceColorModel());
/* 130 */     return new GDIWindowSurfaceData(paramWComponentPeer, localSurfaceType);
/*     */   }
/*     */ 
/*     */   public SurfaceDataProxy makeProxyFor(SurfaceData paramSurfaceData)
/*     */   {
/* 135 */     return SurfaceDataProxy.UNCACHED;
/*     */   }
/*     */ 
/*     */   public Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 139 */     throw new InternalError("not implemented yet");
/*     */   }
/*     */ 
/*     */   public void validatePipe(SunGraphics2D paramSunGraphics2D)
/*     */   {
/* 155 */     if ((paramSunGraphics2D.antialiasHint != 2) && (paramSunGraphics2D.paintState <= 1) && ((paramSunGraphics2D.compositeState <= 0) || (paramSunGraphics2D.compositeState == 2)))
/*     */     {
/* 160 */       if (paramSunGraphics2D.clipState == 2)
/*     */       {
/* 165 */         super.validatePipe(paramSunGraphics2D);
/*     */       }
/* 167 */       else switch (paramSunGraphics2D.textAntialiasHint)
/*     */         {
/*     */         case 0:
/*     */         case 1:
/* 172 */           paramSunGraphics2D.textpipe = solidTextRenderer;
/* 173 */           break;
/*     */         case 2:
/* 176 */           paramSunGraphics2D.textpipe = aaTextRenderer;
/* 177 */           break;
/*     */         default:
/* 180 */           switch (paramSunGraphics2D.getFontInfo().aaHint)
/*     */           {
/*     */           case 4:
/*     */           case 6:
/* 184 */             paramSunGraphics2D.textpipe = lcdTextRenderer;
/* 185 */             break;
/*     */           case 2:
/* 188 */             paramSunGraphics2D.textpipe = aaTextRenderer;
/* 189 */             break;
/*     */           case 3:
/*     */           case 5:
/*     */           default:
/* 192 */             paramSunGraphics2D.textpipe = solidTextRenderer;
/*     */           }
/*     */           break;
/*     */         }
/* 196 */       paramSunGraphics2D.imagepipe = imagepipe;
/* 197 */       if (paramSunGraphics2D.transformState >= 3) {
/* 198 */         paramSunGraphics2D.drawpipe = gdiTxPipe;
/* 199 */         paramSunGraphics2D.fillpipe = gdiTxPipe;
/* 200 */       } else if (paramSunGraphics2D.strokeState != 0) {
/* 201 */         paramSunGraphics2D.drawpipe = gdiTxPipe;
/* 202 */         paramSunGraphics2D.fillpipe = gdiPipe;
/*     */       } else {
/* 204 */         paramSunGraphics2D.drawpipe = gdiPipe;
/* 205 */         paramSunGraphics2D.fillpipe = gdiPipe;
/*     */       }
/* 207 */       paramSunGraphics2D.shapepipe = gdiPipe;
/*     */ 
/* 213 */       if (paramSunGraphics2D.loops == null)
/*     */       {
/* 215 */         paramSunGraphics2D.loops = getRenderLoops(paramSunGraphics2D);
/*     */       }
/*     */     } else {
/* 218 */       super.validatePipe(paramSunGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   public RenderLoops getRenderLoops(SunGraphics2D paramSunGraphics2D) {
/* 223 */     if ((paramSunGraphics2D.paintState <= 1) && (paramSunGraphics2D.compositeState <= 0))
/*     */     {
/* 226 */       return this.solidloops;
/*     */     }
/* 228 */     return super.getRenderLoops(paramSunGraphics2D);
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration getDeviceConfiguration() {
/* 232 */     return this.graphicsConfig;
/*     */   }
/*     */ 
/*     */   private native void initOps(WComponentPeer paramWComponentPeer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   private GDIWindowSurfaceData(WComponentPeer paramWComponentPeer, SurfaceType paramSurfaceType)
/*     */   {
/* 242 */     super(paramSurfaceType, paramWComponentPeer.getDeviceColorModel());
/* 243 */     ColorModel localColorModel = paramWComponentPeer.getDeviceColorModel();
/* 244 */     this.peer = paramWComponentPeer;
/* 245 */     int i = 0; int j = 0; int k = 0;
/*     */     int m;
/* 247 */     switch (localColorModel.getPixelSize()) {
/*     */     case 24:
/*     */     case 32:
/* 250 */       if ((localColorModel instanceof DirectColorModel))
/* 251 */         m = 32;
/*     */       else {
/* 253 */         m = 24;
/*     */       }
/* 255 */       break;
/*     */     default:
/* 257 */       m = localColorModel.getPixelSize();
/*     */     }
/* 259 */     if ((localColorModel instanceof DirectColorModel)) {
/* 260 */       localObject = (DirectColorModel)localColorModel;
/* 261 */       i = ((DirectColorModel)localObject).getRedMask();
/* 262 */       j = ((DirectColorModel)localObject).getGreenMask();
/* 263 */       k = ((DirectColorModel)localObject).getBlueMask();
/*     */     }
/* 265 */     this.graphicsConfig = ((Win32GraphicsConfig)paramWComponentPeer.getGraphicsConfiguration());
/*     */ 
/* 267 */     this.solidloops = this.graphicsConfig.getSolidLoops(paramSurfaceType);
/*     */ 
/* 269 */     Object localObject = (Win32GraphicsDevice)this.graphicsConfig.getDevice();
/*     */ 
/* 271 */     initOps(paramWComponentPeer, m, i, j, k, ((Win32GraphicsDevice)localObject).getScreen());
/* 272 */     setBlitProxyKey(this.graphicsConfig.getProxyKey());
/*     */   }
/*     */ 
/*     */   public SurfaceData getReplacement()
/*     */   {
/* 284 */     ScreenUpdateManager localScreenUpdateManager = ScreenUpdateManager.getInstance();
/* 285 */     return localScreenUpdateManager.getReplacementScreenSurface(this.peer, this);
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds() {
/* 289 */     Rectangle localRectangle = this.peer.getBounds();
/* 290 */     localRectangle.x = (localRectangle.y = 0);
/* 291 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public boolean copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 297 */     CompositeType localCompositeType = paramSunGraphics2D.imageComp;
/* 298 */     if ((paramSunGraphics2D.transformState < 3) && (paramSunGraphics2D.clipState != 2) && ((CompositeType.SrcOverNoEa.equals(localCompositeType)) || (CompositeType.SrcNoEa.equals(localCompositeType))))
/*     */     {
/* 303 */       paramInt1 += paramSunGraphics2D.transX;
/* 304 */       paramInt2 += paramSunGraphics2D.transY;
/* 305 */       int i = paramInt1 + paramInt5;
/* 306 */       int j = paramInt2 + paramInt6;
/* 307 */       int k = i + paramInt3;
/* 308 */       int m = j + paramInt4;
/* 309 */       Region localRegion = paramSunGraphics2D.getCompClip();
/* 310 */       if (i < localRegion.getLoX()) i = localRegion.getLoX();
/* 311 */       if (j < localRegion.getLoY()) j = localRegion.getLoY();
/* 312 */       if (k > localRegion.getHiX()) k = localRegion.getHiX();
/* 313 */       if (m > localRegion.getHiY()) m = localRegion.getHiY();
/* 314 */       if ((i < k) && (j < m)) {
/* 315 */         gdiPipe.devCopyArea(this, i - paramInt5, j - paramInt6, paramInt5, paramInt6, k - i, m - j);
/*     */       }
/*     */ 
/* 319 */       return true;
/*     */     }
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   private native void invalidateSD();
/*     */ 
/*     */   public void invalidate() {
/* 327 */     if (isValid()) {
/* 328 */       invalidateSD();
/* 329 */       super.invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getDestination()
/*     */   {
/* 339 */     return this.peer.getTarget();
/*     */   }
/*     */ 
/*     */   public WComponentPeer getPeer() {
/* 343 */     return this.peer;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  81 */     initIDs(XORComposite.class);
/*  82 */     if (WindowsFlags.isGdiBlitEnabled())
/*     */     {
/*  84 */       GDIBlitLoops.register();
/*     */     }
/*     */ 
/* 146 */     gdiPipe = new GDIRenderer();
/* 147 */     if (GraphicsPrimitive.tracingEnabled())
/* 148 */       gdiPipe = gdiPipe.traceWrap();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.windows.GDIWindowSurfaceData
 * JD-Core Version:    0.6.2
 */