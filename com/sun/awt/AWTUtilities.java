/*     */ package com.sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ComponentAccessor;
/*     */ import sun.awt.AWTAccessor.WindowAccessor;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public final class AWTUtilities
/*     */ {
/*     */   public static boolean isTranslucencySupported(Translucency paramTranslucency)
/*     */   {
/* 117 */     switch (1.$SwitchMap$com$sun$awt$AWTUtilities$Translucency[paramTranslucency.ordinal()]) {
/*     */     case 1:
/* 119 */       return isWindowShapingSupported();
/*     */     case 2:
/* 121 */       return isWindowOpacitySupported();
/*     */     case 3:
/* 123 */       return isWindowTranslucencySupported();
/*     */     }
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isWindowOpacitySupported()
/*     */   {
/* 137 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 138 */     if (!(localToolkit instanceof SunToolkit)) {
/* 139 */       return false;
/*     */     }
/* 141 */     return ((SunToolkit)localToolkit).isWindowOpacitySupported();
/*     */   }
/*     */ 
/*     */   public static void setWindowOpacity(Window paramWindow, float paramFloat)
/*     */   {
/* 169 */     if (paramWindow == null) {
/* 170 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/* 174 */     AWTAccessor.getWindowAccessor().setOpacity(paramWindow, paramFloat);
/*     */   }
/*     */ 
/*     */   public static float getWindowOpacity(Window paramWindow)
/*     */   {
/* 185 */     if (paramWindow == null) {
/* 186 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/* 190 */     return AWTAccessor.getWindowAccessor().getOpacity(paramWindow);
/*     */   }
/*     */ 
/*     */   public static boolean isWindowShapingSupported()
/*     */   {
/* 201 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 202 */     if (!(localToolkit instanceof SunToolkit)) {
/* 203 */       return false;
/*     */     }
/* 205 */     return ((SunToolkit)localToolkit).isWindowShapingSupported();
/*     */   }
/*     */ 
/*     */   public static Shape getWindowShape(Window paramWindow)
/*     */   {
/* 219 */     if (paramWindow == null) {
/* 220 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/* 223 */     return AWTAccessor.getWindowAccessor().getShape(paramWindow);
/*     */   }
/*     */ 
/*     */   public static void setWindowShape(Window paramWindow, Shape paramShape)
/*     */   {
/* 250 */     if (paramWindow == null) {
/* 251 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/* 254 */     AWTAccessor.getWindowAccessor().setShape(paramWindow, paramShape);
/*     */   }
/*     */ 
/*     */   private static boolean isWindowTranslucencySupported()
/*     */   {
/* 267 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 268 */     if (!(localToolkit instanceof SunToolkit)) {
/* 269 */       return false;
/*     */     }
/*     */ 
/* 272 */     if (!((SunToolkit)localToolkit).isWindowTranslucencySupported()) {
/* 273 */       return false;
/*     */     }
/*     */ 
/* 276 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*     */ 
/* 282 */     if (isTranslucencyCapable(localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration()))
/*     */     {
/* 285 */       return true;
/*     */     }
/*     */ 
/* 289 */     GraphicsDevice[] arrayOfGraphicsDevice = localGraphicsEnvironment.getScreenDevices();
/*     */ 
/* 291 */     for (int i = 0; i < arrayOfGraphicsDevice.length; i++) {
/* 292 */       GraphicsConfiguration[] arrayOfGraphicsConfiguration = arrayOfGraphicsDevice[i].getConfigurations();
/* 293 */       for (int j = 0; j < arrayOfGraphicsConfiguration.length; j++) {
/* 294 */         if (isTranslucencyCapable(arrayOfGraphicsConfiguration[j])) {
/* 295 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   public static void setWindowOpaque(Window paramWindow, boolean paramBoolean)
/*     */   {
/* 352 */     if (paramWindow == null) {
/* 353 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/* 356 */     if ((!paramBoolean) && (!isTranslucencySupported(Translucency.PERPIXEL_TRANSLUCENT))) {
/* 357 */       throw new UnsupportedOperationException("The PERPIXEL_TRANSLUCENT translucency kind is not supported");
/*     */     }
/*     */ 
/* 360 */     AWTAccessor.getWindowAccessor().setOpaque(paramWindow, paramBoolean);
/*     */   }
/*     */ 
/*     */   public static boolean isWindowOpaque(Window paramWindow)
/*     */   {
/* 372 */     if (paramWindow == null) {
/* 373 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/* 377 */     return paramWindow.isOpaque();
/*     */   }
/*     */ 
/*     */   public static boolean isTranslucencyCapable(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 398 */     if (paramGraphicsConfiguration == null) {
/* 399 */       throw new NullPointerException("The gc argument should not be null");
/*     */     }
/*     */ 
/* 404 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 405 */     if (!(localToolkit instanceof SunToolkit)) {
/* 406 */       return false;
/*     */     }
/* 408 */     return ((SunToolkit)localToolkit).isTranslucencyCapable(paramGraphicsConfiguration);
/*     */   }
/*     */ 
/*     */   public static void setComponentMixingCutoutShape(Component paramComponent, Shape paramShape)
/*     */   {
/* 453 */     if (paramComponent == null) {
/* 454 */       throw new NullPointerException("The component argument should not be null.");
/*     */     }
/*     */ 
/* 458 */     AWTAccessor.getComponentAccessor().setMixingCutoutShape(paramComponent, paramShape);
/*     */   }
/*     */ 
/*     */   public static enum Translucency
/*     */   {
/*  83 */     PERPIXEL_TRANSPARENT, 
/*     */ 
/*  90 */     TRANSLUCENT, 
/*     */ 
/*  97 */     PERPIXEL_TRANSLUCENT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.awt.AWTUtilities
 * JD-Core Version:    0.6.2
 */