/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import sun.awt.AppContext;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class DefaultMetalTheme extends MetalTheme
/*     */ {
/*     */   private static final boolean PLAIN_FONTS;
/* 101 */   private static final String[] fontNames = { "Dialog", "Dialog", "Dialog", "Dialog", "Dialog", "Dialog" };
/*     */ 
/* 108 */   private static final int[] fontStyles = { 1, 0, 0, 1, 1, 0 };
/*     */ 
/* 114 */   private static final int[] fontSizes = { 12, 12, 12, 12, 12, 10 };
/*     */ 
/* 128 */   private static final String[] defaultNames = { "swing.plaf.metal.controlFont", "swing.plaf.metal.systemFont", "swing.plaf.metal.userFont", "swing.plaf.metal.controlFont", "swing.plaf.metal.controlFont", "swing.plaf.metal.smallFont" };
/*     */ 
/* 194 */   private static final ColorUIResource primary1 = new ColorUIResource(102, 102, 153);
/*     */ 
/* 196 */   private static final ColorUIResource primary2 = new ColorUIResource(153, 153, 204);
/*     */ 
/* 198 */   private static final ColorUIResource primary3 = new ColorUIResource(204, 204, 255);
/*     */ 
/* 200 */   private static final ColorUIResource secondary1 = new ColorUIResource(102, 102, 102);
/*     */ 
/* 202 */   private static final ColorUIResource secondary2 = new ColorUIResource(153, 153, 153);
/*     */ 
/* 204 */   private static final ColorUIResource secondary3 = new ColorUIResource(204, 204, 204);
/*     */   private FontDelegate fontDelegate;
/*     */ 
/*     */   static String getDefaultFontName(int paramInt)
/*     */   {
/* 141 */     return fontNames[paramInt];
/*     */   }
/*     */ 
/*     */   static int getDefaultFontSize(int paramInt)
/*     */   {
/* 148 */     return fontSizes[paramInt];
/*     */   }
/*     */ 
/*     */   static int getDefaultFontStyle(int paramInt)
/*     */   {
/* 155 */     if (paramInt != 4) {
/* 156 */       Object localObject = null;
/* 157 */       if (AppContext.getAppContext().get(SwingUtilities2.LAF_STATE_KEY) != null)
/*     */       {
/* 162 */         localObject = UIManager.get("swing.boldMetal");
/*     */       }
/* 164 */       if (localObject != null) {
/* 165 */         if (Boolean.FALSE.equals(localObject)) {
/* 166 */           return 0;
/*     */         }
/*     */       }
/* 169 */       else if (PLAIN_FONTS) {
/* 170 */         return 0;
/*     */       }
/*     */     }
/* 173 */     return fontStyles[paramInt];
/*     */   }
/*     */ 
/*     */   static String getDefaultPropertyName(int paramInt)
/*     */   {
/* 180 */     return defaultNames[paramInt];
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 214 */     return "Steel";
/*     */   }
/*     */ 
/*     */   public DefaultMetalTheme()
/*     */   {
/* 220 */     install();
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary1()
/*     */   {
/* 229 */     return primary1;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary2()
/*     */   {
/* 237 */     return primary2;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getPrimary3()
/*     */   {
/* 245 */     return primary3;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary1()
/*     */   {
/* 253 */     return secondary1;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary2()
/*     */   {
/* 261 */     return secondary2;
/*     */   }
/*     */ 
/*     */   protected ColorUIResource getSecondary3()
/*     */   {
/* 269 */     return secondary3;
/*     */   }
/*     */ 
/*     */   public FontUIResource getControlTextFont()
/*     */   {
/* 281 */     return getFont(0);
/*     */   }
/*     */ 
/*     */   public FontUIResource getSystemTextFont()
/*     */   {
/* 290 */     return getFont(1);
/*     */   }
/*     */ 
/*     */   public FontUIResource getUserTextFont()
/*     */   {
/* 299 */     return getFont(2);
/*     */   }
/*     */ 
/*     */   public FontUIResource getMenuTextFont()
/*     */   {
/* 311 */     return getFont(3);
/*     */   }
/*     */ 
/*     */   public FontUIResource getWindowTitleFont()
/*     */   {
/* 320 */     return getFont(4);
/*     */   }
/*     */ 
/*     */   public FontUIResource getSubTextFont()
/*     */   {
/* 329 */     return getFont(5);
/*     */   }
/*     */ 
/*     */   private FontUIResource getFont(int paramInt) {
/* 333 */     return this.fontDelegate.getFont(paramInt);
/*     */   }
/*     */ 
/*     */   void install() {
/* 337 */     if ((MetalLookAndFeel.isWindows()) && (MetalLookAndFeel.useSystemFonts()))
/*     */     {
/* 339 */       this.fontDelegate = new WindowsFontDelegate();
/*     */     }
/*     */     else
/* 342 */       this.fontDelegate = new FontDelegate();
/*     */   }
/*     */ 
/*     */   boolean isSystemTheme()
/*     */   {
/* 350 */     return getClass() == DefaultMetalTheme.class;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 184 */     Object localObject = AccessController.doPrivileged(new GetPropertyAction("swing.boldMetal"));
/*     */ 
/* 186 */     if ((localObject == null) || (!"false".equals(localObject))) {
/* 187 */       PLAIN_FONTS = false;
/*     */     }
/*     */     else
/* 190 */       PLAIN_FONTS = true;
/*     */   }
/*     */ 
/*     */   private static class FontDelegate
/*     */   {
/* 357 */     private static int[] defaultMapping = { 0, 1, 2, 0, 0, 5 };
/*     */     FontUIResource[] fonts;
/*     */ 
/*     */     public FontDelegate()
/*     */     {
/* 366 */       this.fonts = new FontUIResource[6];
/*     */     }
/*     */ 
/*     */     public FontUIResource getFont(int paramInt) {
/* 370 */       int i = defaultMapping[paramInt];
/* 371 */       if (this.fonts[paramInt] == null) {
/* 372 */         Font localFont = getPrivilegedFont(i);
/*     */ 
/* 374 */         if (localFont == null) {
/* 375 */           localFont = new Font(DefaultMetalTheme.getDefaultFontName(paramInt), DefaultMetalTheme.getDefaultFontStyle(paramInt), DefaultMetalTheme.getDefaultFontSize(paramInt));
/*     */         }
/*     */ 
/* 379 */         this.fonts[paramInt] = new FontUIResource(localFont);
/*     */       }
/* 381 */       return this.fonts[paramInt];
/*     */     }
/*     */ 
/*     */     protected Font getPrivilegedFont(final int paramInt)
/*     */     {
/* 390 */       return (Font)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Font run() {
/* 393 */           return Font.getFont(DefaultMetalTheme.getDefaultPropertyName(paramInt));
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class WindowsFontDelegate extends DefaultMetalTheme.FontDelegate
/*     */   {
/*     */     private MetalFontDesktopProperty[] props;
/*     */     private boolean[] checkedPriviledged;
/*     */ 
/*     */     public WindowsFontDelegate()
/*     */     {
/* 408 */       this.props = new MetalFontDesktopProperty[6];
/* 409 */       this.checkedPriviledged = new boolean[6];
/*     */     }
/*     */ 
/*     */     public FontUIResource getFont(int paramInt) {
/* 413 */       if (this.fonts[paramInt] != null) {
/* 414 */         return this.fonts[paramInt];
/*     */       }
/* 416 */       if (this.checkedPriviledged[paramInt] == 0) {
/* 417 */         Font localFont = getPrivilegedFont(paramInt);
/*     */ 
/* 419 */         this.checkedPriviledged[paramInt] = true;
/* 420 */         if (localFont != null) {
/* 421 */           this.fonts[paramInt] = new FontUIResource(localFont);
/* 422 */           return this.fonts[paramInt];
/*     */         }
/*     */       }
/* 425 */       if (this.props[paramInt] == null) {
/* 426 */         this.props[paramInt] = new MetalFontDesktopProperty(paramInt);
/*     */       }
/*     */ 
/* 430 */       return (FontUIResource)this.props[paramInt].createValue(null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.DefaultMetalTheme
 * JD-Core Version:    0.6.2
 */