/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Locale;
/*     */ import sun.font.FontManager;
/*     */ import sun.font.FontManagerFactory;
/*     */ import sun.java2d.HeadlessGraphicsEnvironment;
/*     */ import sun.java2d.SunGraphicsEnvironment;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class GraphicsEnvironment
/*     */ {
/*     */   private static GraphicsEnvironment localEnv;
/*     */   private static Boolean headless;
/*     */   private static Boolean defaultHeadless;
/*     */ 
/*     */   public static synchronized GraphicsEnvironment getLocalGraphicsEnvironment()
/*     */   {
/*  80 */     if (localEnv == null) {
/*  81 */       localEnv = createGE();
/*     */     }
/*     */ 
/*  84 */     return localEnv;
/*     */   }
/*     */ 
/*     */   private static GraphicsEnvironment createGE()
/*     */   {
/*  95 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.awt.graphicsenv", null));
/*     */     Object localObject;
/*     */     try
/*     */     {
/*     */       Class localClass;
/*     */       try
/*     */       {
/* 102 */         localClass = Class.forName(str);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException2)
/*     */       {
/* 106 */         ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/* 107 */         localClass = Class.forName(str, true, localClassLoader);
/*     */       }
/* 109 */       localObject = (GraphicsEnvironment)localClass.newInstance();
/*     */ 
/* 112 */       if (isHeadless())
/* 113 */         localObject = new HeadlessGraphicsEnvironment((GraphicsEnvironment)localObject);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1) {
/* 116 */       throw new Error("Could not find class: " + str);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 118 */       throw new Error("Could not instantiate Graphics Environment: " + str);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 121 */       throw new Error("Could not access Graphics Environment: " + str);
/*     */     }
/*     */ 
/* 124 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static boolean isHeadless()
/*     */   {
/* 140 */     return getHeadlessProperty();
/*     */   }
/*     */ 
/*     */   static String getHeadlessMessage()
/*     */   {
/* 149 */     if (headless == null) {
/* 150 */       getHeadlessProperty();
/*     */     }
/* 152 */     return defaultHeadless != Boolean.TRUE ? null : "\nNo X11 DISPLAY variable was set, but this program performed an operation which requires it.";
/*     */   }
/*     */ 
/*     */   private static boolean getHeadlessProperty()
/*     */   {
/* 162 */     if (headless == null) {
/* 163 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/* 166 */           String str1 = System.getProperty("java.awt.headless");
/*     */ 
/* 168 */           if (str1 == null)
/*     */           {
/* 170 */             if (System.getProperty("javaplugin.version") != null) {
/* 171 */               GraphicsEnvironment.access$002(GraphicsEnvironment.access$102(Boolean.FALSE));
/*     */             } else {
/* 173 */               String str2 = System.getProperty("os.name");
/* 174 */               if ((str2.contains("OS X")) && ("sun.awt.HToolkit".equals(System.getProperty("awt.toolkit"))))
/*     */               {
/* 177 */                 GraphicsEnvironment.access$002(GraphicsEnvironment.access$102(Boolean.TRUE));
/*     */               }
/* 179 */               else GraphicsEnvironment.access$002(GraphicsEnvironment.access$102(Boolean.valueOf((("Linux".equals(str2)) || ("SunOS".equals(str2)) || ("FreeBSD".equals(str2)) || ("NetBSD".equals(str2)) || ("OpenBSD".equals(str2))) && (System.getenv("DISPLAY") == null))));
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/* 188 */           else if (str1.equals("true"))
/* 189 */             GraphicsEnvironment.access$002(Boolean.TRUE);
/*     */           else {
/* 191 */             GraphicsEnvironment.access$002(Boolean.FALSE);
/*     */           }
/* 193 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 198 */     return headless.booleanValue();
/*     */   }
/*     */ 
/*     */   static void checkHeadless()
/*     */     throws HeadlessException
/*     */   {
/* 206 */     if (isHeadless())
/* 207 */       throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   public boolean isHeadlessInstance()
/*     */   {
/* 227 */     return getHeadlessProperty();
/*     */   }
/*     */ 
/*     */   public abstract GraphicsDevice[] getScreenDevices()
/*     */     throws HeadlessException;
/*     */ 
/*     */   public abstract GraphicsDevice getDefaultScreenDevice()
/*     */     throws HeadlessException;
/*     */ 
/*     */   public abstract Graphics2D createGraphics(BufferedImage paramBufferedImage);
/*     */ 
/*     */   public abstract Font[] getAllFonts();
/*     */ 
/*     */   public abstract String[] getAvailableFontFamilyNames();
/*     */ 
/*     */   public abstract String[] getAvailableFontFamilyNames(Locale paramLocale);
/*     */ 
/*     */   public boolean registerFont(Font paramFont)
/*     */   {
/* 366 */     if (paramFont == null) {
/* 367 */       throw new NullPointerException("font cannot be null.");
/*     */     }
/* 369 */     FontManager localFontManager = FontManagerFactory.getInstance();
/* 370 */     return localFontManager.registerFont(paramFont);
/*     */   }
/*     */ 
/*     */   public void preferLocaleFonts()
/*     */   {
/* 394 */     FontManager localFontManager = FontManagerFactory.getInstance();
/* 395 */     localFontManager.preferLocaleFonts();
/*     */   }
/*     */ 
/*     */   public void preferProportionalFonts()
/*     */   {
/* 415 */     FontManager localFontManager = FontManagerFactory.getInstance();
/* 416 */     localFontManager.preferProportionalFonts();
/*     */   }
/*     */ 
/*     */   public Point getCenterPoint()
/*     */     throws HeadlessException
/*     */   {
/* 432 */     Rectangle localRectangle = SunGraphicsEnvironment.getUsableBounds(getDefaultScreenDevice());
/*     */ 
/* 434 */     return new Point(localRectangle.width / 2 + localRectangle.x, localRectangle.height / 2 + localRectangle.y);
/*     */   }
/*     */ 
/*     */   public Rectangle getMaximumWindowBounds()
/*     */     throws HeadlessException
/*     */   {
/* 460 */     return SunGraphicsEnvironment.getUsableBounds(getDefaultScreenDevice());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.GraphicsEnvironment
 * JD-Core Version:    0.6.2
 */