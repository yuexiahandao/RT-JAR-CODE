/*     */ package sun.java2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import sun.awt.DisplayChangedListener;
/*     */ import sun.awt.SunDisplayChanger;
/*     */ import sun.font.FontManager;
/*     */ import sun.font.FontManagerFactory;
/*     */ import sun.font.FontManagerForSGE;
/*     */ 
/*     */ public abstract class SunGraphicsEnvironment extends GraphicsEnvironment
/*     */   implements DisplayChangedListener
/*     */ {
/*     */   public static boolean isOpenSolaris;
/*     */   private static Font defaultFont;
/*     */   protected GraphicsDevice[] screens;
/* 303 */   protected SunDisplayChanger displayChanger = new SunDisplayChanger();
/*     */ 
/*     */   public SunGraphicsEnvironment()
/*     */   {
/*  83 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  86 */         String str1 = System.getProperty("os.version", "0.0");
/*     */         try {
/*  88 */           float f = Float.parseFloat(str1);
/*  89 */           if (f > 5.1F) {
/*  90 */             File localFile1 = new File("/etc/release");
/*  91 */             FileInputStream localFileInputStream = new FileInputStream(localFile1);
/*  92 */             InputStreamReader localInputStreamReader = new InputStreamReader(localFileInputStream, "ISO-8859-1");
/*     */ 
/*  94 */             BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
/*  95 */             String str2 = localBufferedReader.readLine();
/*  96 */             if (str2.indexOf("OpenSolaris") >= 0) {
/*  97 */               SunGraphicsEnvironment.isOpenSolaris = true;
/*     */             }
/*     */             else
/*     */             {
/* 110 */               String str3 = "/usr/openwin/lib/X11/fonts/TrueType/CourierNew.ttf";
/*     */ 
/* 112 */               File localFile2 = new File(str3);
/* 113 */               SunGraphicsEnvironment.isOpenSolaris = !localFile2.exists();
/*     */             }
/* 115 */             localFileInputStream.close();
/*     */           }
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/* 121 */         SunGraphicsEnvironment.access$002(new Font("Dialog", 0, 12));
/*     */ 
/* 123 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public synchronized GraphicsDevice[] getScreenDevices()
/*     */   {
/* 134 */     GraphicsDevice[] arrayOfGraphicsDevice = this.screens;
/* 135 */     if (arrayOfGraphicsDevice == null) {
/* 136 */       int i = getNumScreens();
/* 137 */       arrayOfGraphicsDevice = new GraphicsDevice[i];
/* 138 */       for (int j = 0; j < i; j++) {
/* 139 */         arrayOfGraphicsDevice[j] = makeScreenDevice(j);
/*     */       }
/* 141 */       this.screens = arrayOfGraphicsDevice;
/*     */     }
/* 143 */     return arrayOfGraphicsDevice;
/*     */   }
/*     */ 
/*     */   protected abstract int getNumScreens();
/*     */ 
/*     */   protected abstract GraphicsDevice makeScreenDevice(int paramInt);
/*     */ 
/*     */   public GraphicsDevice getDefaultScreenDevice()
/*     */   {
/* 168 */     return getScreenDevices()[0];
/*     */   }
/*     */ 
/*     */   public Graphics2D createGraphics(BufferedImage paramBufferedImage)
/*     */   {
/* 177 */     if (paramBufferedImage == null) {
/* 178 */       throw new NullPointerException("BufferedImage cannot be null");
/*     */     }
/* 180 */     SurfaceData localSurfaceData = SurfaceData.getPrimarySurfaceData(paramBufferedImage);
/* 181 */     return new SunGraphics2D(localSurfaceData, Color.white, Color.black, defaultFont);
/*     */   }
/*     */ 
/*     */   public static FontManagerForSGE getFontManagerForSGE() {
/* 185 */     FontManager localFontManager = FontManagerFactory.getInstance();
/* 186 */     return (FontManagerForSGE)localFontManager;
/*     */   }
/*     */ 
/*     */   public static void useAlternateFontforJALocales()
/*     */   {
/* 197 */     getFontManagerForSGE().useAlternateFontforJALocales();
/*     */   }
/*     */ 
/*     */   public Font[] getAllFonts()
/*     */   {
/* 204 */     FontManagerForSGE localFontManagerForSGE = getFontManagerForSGE();
/* 205 */     Font[] arrayOfFont1 = localFontManagerForSGE.getAllInstalledFonts();
/* 206 */     Font[] arrayOfFont2 = localFontManagerForSGE.getCreatedFonts();
/* 207 */     if ((arrayOfFont2 == null) || (arrayOfFont2.length == 0)) {
/* 208 */       return arrayOfFont1;
/*     */     }
/* 210 */     int i = arrayOfFont1.length + arrayOfFont2.length;
/* 211 */     Font[] arrayOfFont3 = (Font[])Arrays.copyOf(arrayOfFont1, i);
/* 212 */     System.arraycopy(arrayOfFont2, 0, arrayOfFont3, arrayOfFont1.length, arrayOfFont2.length);
/*     */ 
/* 214 */     return arrayOfFont3;
/*     */   }
/*     */ 
/*     */   public String[] getAvailableFontFamilyNames(Locale paramLocale)
/*     */   {
/* 219 */     FontManagerForSGE localFontManagerForSGE = getFontManagerForSGE();
/* 220 */     String[] arrayOfString1 = localFontManagerForSGE.getInstalledFontFamilyNames(paramLocale);
/*     */ 
/* 229 */     TreeMap localTreeMap = localFontManagerForSGE.getCreatedFontFamilyNames();
/* 230 */     if ((localTreeMap == null) || (localTreeMap.size() == 0)) {
/* 231 */       return arrayOfString1;
/*     */     }
/* 233 */     for (int i = 0; i < arrayOfString1.length; i++) {
/* 234 */       localTreeMap.put(arrayOfString1[i].toLowerCase(paramLocale), arrayOfString1[i]);
/*     */     }
/*     */ 
/* 237 */     String[] arrayOfString2 = new String[localTreeMap.size()];
/* 238 */     Object[] arrayOfObject = localTreeMap.keySet().toArray();
/* 239 */     for (int j = 0; j < arrayOfObject.length; j++) {
/* 240 */       arrayOfString2[j] = ((String)localTreeMap.get(arrayOfObject[j]));
/*     */     }
/* 242 */     return arrayOfString2;
/*     */   }
/*     */ 
/*     */   public String[] getAvailableFontFamilyNames()
/*     */   {
/* 247 */     return getAvailableFontFamilyNames(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static Rectangle getUsableBounds(GraphicsDevice paramGraphicsDevice)
/*     */   {
/* 255 */     GraphicsConfiguration localGraphicsConfiguration = paramGraphicsDevice.getDefaultConfiguration();
/* 256 */     Insets localInsets = Toolkit.getDefaultToolkit().getScreenInsets(localGraphicsConfiguration);
/* 257 */     Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/*     */ 
/* 259 */     localRectangle.x += localInsets.left;
/* 260 */     localRectangle.y += localInsets.top;
/* 261 */     localRectangle.width -= localInsets.left + localInsets.right;
/* 262 */     localRectangle.height -= localInsets.top + localInsets.bottom;
/*     */ 
/* 264 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public void displayChanged()
/*     */   {
/* 273 */     for (GraphicsDevice localGraphicsDevice : getScreenDevices()) {
/* 274 */       if ((localGraphicsDevice instanceof DisplayChangedListener)) {
/* 275 */         ((DisplayChangedListener)localGraphicsDevice).displayChanged();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 281 */     this.displayChanger.notifyListeners();
/*     */   }
/*     */ 
/*     */   public void paletteChanged()
/*     */   {
/* 289 */     this.displayChanger.notifyPaletteChanged();
/*     */   }
/*     */ 
/*     */   public abstract boolean isDisplayLocal();
/*     */ 
/*     */   public void addDisplayChangedListener(DisplayChangedListener paramDisplayChangedListener)
/*     */   {
/* 310 */     this.displayChanger.add(paramDisplayChangedListener);
/*     */   }
/*     */ 
/*     */   public void removeDisplayChangedListener(DisplayChangedListener paramDisplayChangedListener)
/*     */   {
/* 317 */     this.displayChanger.remove(paramDisplayChangedListener);
/*     */   }
/*     */ 
/*     */   public boolean isFlipStrategyPreferred(ComponentPeer paramComponentPeer)
/*     */   {
/* 335 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.SunGraphicsEnvironment
 * JD-Core Version:    0.6.2
 */