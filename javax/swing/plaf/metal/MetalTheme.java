/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ 
/*     */ public abstract class MetalTheme
/*     */ {
/*     */   static final int CONTROL_TEXT_FONT = 0;
/*     */   static final int SYSTEM_TEXT_FONT = 1;
/*     */   static final int USER_TEXT_FONT = 2;
/*     */   static final int MENU_TEXT_FONT = 3;
/*     */   static final int WINDOW_TITLE_FONT = 4;
/*     */   static final int SUB_TEXT_FONT = 5;
/*  76 */   static ColorUIResource white = new ColorUIResource(255, 255, 255);
/*  77 */   private static ColorUIResource black = new ColorUIResource(0, 0, 0);
/*     */ 
/*     */   public abstract String getName();
/*     */ 
/*     */   protected abstract ColorUIResource getPrimary1();
/*     */ 
/*     */   protected abstract ColorUIResource getPrimary2();
/*     */ 
/*     */   protected abstract ColorUIResource getPrimary3();
/*     */ 
/*     */   protected abstract ColorUIResource getSecondary1();
/*     */ 
/*     */   protected abstract ColorUIResource getSecondary2();
/*     */ 
/*     */   protected abstract ColorUIResource getSecondary3();
/*     */ 
/*     */   public abstract FontUIResource getControlTextFont();
/*     */ 
/*     */   public abstract FontUIResource getSystemTextFont();
/*     */ 
/*     */   public abstract FontUIResource getUserTextFont();
/*     */ 
/*     */   public abstract FontUIResource getMenuTextFont();
/*     */ 
/*     */   public abstract FontUIResource getWindowTitleFont();
/*     */ 
/*     */   public abstract FontUIResource getSubTextFont();
/*     */ 
/*     */   protected ColorUIResource getWhite()
/*     */   {
/* 176 */     return white;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getBlack()
/*     */   {
/* 184 */     return black;
/*     */   }
/*     */ 
/*     */   public ColorUIResource getFocusColor()
/*     */   {
/* 192 */     return getPrimary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getDesktopColor()
/*     */   {
/* 200 */     return getPrimary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControl()
/*     */   {
/* 208 */     return getSecondary3();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlShadow()
/*     */   {
/* 216 */     return getSecondary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlDarkShadow()
/*     */   {
/* 224 */     return getSecondary1();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlInfo()
/*     */   {
/* 232 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlHighlight()
/*     */   {
/* 240 */     return getWhite();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlDisabled()
/*     */   {
/* 248 */     return getSecondary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getPrimaryControl()
/*     */   {
/* 256 */     return getPrimary3();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getPrimaryControlShadow()
/*     */   {
/* 264 */     return getPrimary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getPrimaryControlDarkShadow()
/*     */   {
/* 271 */     return getPrimary1();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getPrimaryControlInfo()
/*     */   {
/* 279 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getPrimaryControlHighlight()
/*     */   {
/* 287 */     return getWhite();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getSystemTextColor()
/*     */   {
/* 295 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getControlTextColor()
/*     */   {
/* 303 */     return getControlInfo();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getInactiveControlTextColor()
/*     */   {
/* 311 */     return getControlDisabled();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getInactiveSystemTextColor()
/*     */   {
/* 319 */     return getSecondary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getUserTextColor()
/*     */   {
/* 327 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getTextHighlightColor()
/*     */   {
/* 335 */     return getPrimary3();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getHighlightedTextColor()
/*     */   {
/* 343 */     return getControlTextColor();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getWindowBackground()
/*     */   {
/* 351 */     return getWhite();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getWindowTitleBackground()
/*     */   {
/* 359 */     return getPrimary3();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getWindowTitleForeground()
/*     */   {
/* 367 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getWindowTitleInactiveBackground()
/*     */   {
/* 375 */     return getSecondary3();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getWindowTitleInactiveForeground()
/*     */   {
/* 383 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuBackground()
/*     */   {
/* 391 */     return getSecondary3();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuForeground()
/*     */   {
/* 399 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuSelectedBackground()
/*     */   {
/* 407 */     return getPrimary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuSelectedForeground()
/*     */   {
/* 415 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getMenuDisabledForeground()
/*     */   {
/* 423 */     return getSecondary2();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getSeparatorBackground()
/*     */   {
/* 431 */     return getWhite();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getSeparatorForeground()
/*     */   {
/* 439 */     return getPrimary1();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getAcceleratorForeground()
/*     */   {
/* 447 */     return getPrimary1();
/*     */   }
/*     */ 
/*     */   public ColorUIResource getAcceleratorSelectedForeground()
/*     */   {
/* 455 */     return getBlack();
/*     */   }
/*     */ 
/*     */   public void addCustomEntriesToTable(UIDefaults paramUIDefaults)
/*     */   {
/*     */   }
/*     */ 
/*     */   void install()
/*     */   {
/*     */   }
/*     */ 
/*     */   boolean isSystemTheme()
/*     */   {
/* 483 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalTheme
 * JD-Core Version:    0.6.2
 */