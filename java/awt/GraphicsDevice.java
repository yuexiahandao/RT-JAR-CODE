/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public abstract class GraphicsDevice
/*     */ {
/*     */   private Window fullScreenWindow;
/*     */   private AppContext fullScreenAppContext;
/*  85 */   private final Object fsAppContextLock = new Object();
/*     */   private Rectangle windowedModeBounds;
/*     */   public static final int TYPE_RASTER_SCREEN = 0;
/*     */   public static final int TYPE_PRINTER = 1;
/*     */   public static final int TYPE_IMAGE_BUFFER = 2;
/*     */ 
/*     */   public abstract int getType();
/*     */ 
/*     */   public abstract String getIDstring();
/*     */ 
/*     */   public abstract GraphicsConfiguration[] getConfigurations();
/*     */ 
/*     */   public abstract GraphicsConfiguration getDefaultConfiguration();
/*     */ 
/*     */   public GraphicsConfiguration getBestConfiguration(GraphicsConfigTemplate paramGraphicsConfigTemplate)
/*     */   {
/* 206 */     GraphicsConfiguration[] arrayOfGraphicsConfiguration = getConfigurations();
/* 207 */     return paramGraphicsConfigTemplate.getBestConfiguration(arrayOfGraphicsConfiguration);
/*     */   }
/*     */ 
/*     */   public boolean isFullScreenSupported()
/*     */   {
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */   public void setFullScreenWindow(Window paramWindow)
/*     */   {
/* 286 */     if (paramWindow != null) {
/* 287 */       if (paramWindow.getShape() != null) {
/* 288 */         paramWindow.setShape(null);
/*     */       }
/* 290 */       if (paramWindow.getOpacity() < 1.0F) {
/* 291 */         paramWindow.setOpacity(1.0F);
/*     */       }
/* 293 */       if (!paramWindow.isOpaque()) {
/* 294 */         localObject1 = paramWindow.getBackground();
/* 295 */         localObject1 = new Color(((Color)localObject1).getRed(), ((Color)localObject1).getGreen(), ((Color)localObject1).getBlue(), 255);
/*     */ 
/* 297 */         paramWindow.setBackground((Color)localObject1);
/*     */       }
/*     */ 
/* 300 */       Object localObject1 = paramWindow.getGraphicsConfiguration();
/* 301 */       if ((localObject1 != null) && (((GraphicsConfiguration)localObject1).getDevice() != this) && (((GraphicsConfiguration)localObject1).getDevice().getFullScreenWindow() == paramWindow))
/*     */       {
/* 303 */         ((GraphicsConfiguration)localObject1).getDevice().setFullScreenWindow(null);
/*     */       }
/*     */     }
/* 306 */     if ((this.fullScreenWindow != null) && (this.windowedModeBounds != null))
/*     */     {
/* 309 */       if (this.windowedModeBounds.width == 0) this.windowedModeBounds.width = 1;
/* 310 */       if (this.windowedModeBounds.height == 0) this.windowedModeBounds.height = 1;
/* 311 */       this.fullScreenWindow.setBounds(this.windowedModeBounds);
/*     */     }
/*     */ 
/* 314 */     synchronized (this.fsAppContextLock)
/*     */     {
/* 316 */       if (paramWindow == null)
/* 317 */         this.fullScreenAppContext = null;
/*     */       else {
/* 319 */         this.fullScreenAppContext = AppContext.getAppContext();
/*     */       }
/* 321 */       this.fullScreenWindow = paramWindow;
/*     */     }
/* 323 */     if (this.fullScreenWindow != null) {
/* 324 */       this.windowedModeBounds = this.fullScreenWindow.getBounds();
/*     */ 
/* 328 */       ??? = getDefaultConfiguration().getBounds();
/* 329 */       this.fullScreenWindow.setBounds(((Rectangle)???).x, ((Rectangle)???).y, ((Rectangle)???).width, ((Rectangle)???).height);
/*     */ 
/* 331 */       this.fullScreenWindow.setVisible(true);
/* 332 */       this.fullScreenWindow.toFront();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Window getFullScreenWindow()
/*     */   {
/* 346 */     Window localWindow = null;
/* 347 */     synchronized (this.fsAppContextLock)
/*     */     {
/* 350 */       if (this.fullScreenAppContext == AppContext.getAppContext()) {
/* 351 */         localWindow = this.fullScreenWindow;
/*     */       }
/*     */     }
/* 354 */     return localWindow;
/*     */   }
/*     */ 
/*     */   public boolean isDisplayChangeSupported()
/*     */   {
/* 372 */     return false;
/*     */   }
/*     */ 
/*     */   public void setDisplayMode(DisplayMode paramDisplayMode)
/*     */   {
/* 427 */     throw new UnsupportedOperationException("Cannot change display mode");
/*     */   }
/*     */ 
/*     */   public DisplayMode getDisplayMode()
/*     */   {
/* 443 */     GraphicsConfiguration localGraphicsConfiguration = getDefaultConfiguration();
/* 444 */     Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/* 445 */     ColorModel localColorModel = localGraphicsConfiguration.getColorModel();
/* 446 */     return new DisplayMode(localRectangle.width, localRectangle.height, localColorModel.getPixelSize(), 0);
/*     */   }
/*     */ 
/*     */   public DisplayMode[] getDisplayModes()
/*     */   {
/* 461 */     return new DisplayMode[] { getDisplayMode() };
/*     */   }
/*     */ 
/*     */   public int getAvailableAcceleratedMemory()
/*     */   {
/* 493 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isWindowTranslucencySupported(WindowTranslucency paramWindowTranslucency)
/*     */   {
/* 506 */     switch (1.$SwitchMap$java$awt$GraphicsDevice$WindowTranslucency[paramWindowTranslucency.ordinal()]) {
/*     */     case 1:
/* 508 */       return isWindowShapingSupported();
/*     */     case 2:
/* 510 */       return isWindowOpacitySupported();
/*     */     case 3:
/* 512 */       return isWindowPerpixelTranslucencySupported();
/*     */     }
/* 514 */     return false;
/*     */   }
/*     */ 
/*     */   static boolean isWindowShapingSupported()
/*     */   {
/* 525 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 526 */     if (!(localToolkit instanceof SunToolkit)) {
/* 527 */       return false;
/*     */     }
/* 529 */     return ((SunToolkit)localToolkit).isWindowShapingSupported();
/*     */   }
/*     */ 
/*     */   static boolean isWindowOpacitySupported()
/*     */   {
/* 540 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 541 */     if (!(localToolkit instanceof SunToolkit)) {
/* 542 */       return false;
/*     */     }
/* 544 */     return ((SunToolkit)localToolkit).isWindowOpacitySupported();
/*     */   }
/*     */ 
/*     */   boolean isWindowPerpixelTranslucencySupported()
/*     */   {
/* 556 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 557 */     if (!(localToolkit instanceof SunToolkit)) {
/* 558 */       return false;
/*     */     }
/* 560 */     if (!((SunToolkit)localToolkit).isWindowTranslucencySupported()) {
/* 561 */       return false;
/*     */     }
/*     */ 
/* 565 */     return getTranslucencyCapableGC() != null;
/*     */   }
/*     */ 
/*     */   GraphicsConfiguration getTranslucencyCapableGC()
/*     */   {
/* 572 */     GraphicsConfiguration localGraphicsConfiguration = getDefaultConfiguration();
/* 573 */     if (localGraphicsConfiguration.isTranslucencyCapable()) {
/* 574 */       return localGraphicsConfiguration;
/*     */     }
/*     */ 
/* 578 */     GraphicsConfiguration[] arrayOfGraphicsConfiguration = getConfigurations();
/* 579 */     for (int i = 0; i < arrayOfGraphicsConfiguration.length; i++) {
/* 580 */       if (arrayOfGraphicsConfiguration[i].isTranslucencyCapable()) {
/* 581 */         return arrayOfGraphicsConfiguration[i];
/*     */       }
/*     */     }
/*     */ 
/* 585 */     return null;
/*     */   }
/*     */ 
/*     */   public static enum WindowTranslucency
/*     */   {
/* 129 */     PERPIXEL_TRANSPARENT, 
/*     */ 
/* 135 */     TRANSLUCENT, 
/*     */ 
/* 141 */     PERPIXEL_TRANSLUCENT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.GraphicsDevice
 * JD-Core Version:    0.6.2
 */