/*     */ package java.awt;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.CursorAccessor;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class Cursor
/*     */   implements Serializable
/*     */ {
/*     */   public static final int DEFAULT_CURSOR = 0;
/*     */   public static final int CROSSHAIR_CURSOR = 1;
/*     */   public static final int TEXT_CURSOR = 2;
/*     */   public static final int WAIT_CURSOR = 3;
/*     */   public static final int SW_RESIZE_CURSOR = 4;
/*     */   public static final int SE_RESIZE_CURSOR = 5;
/*     */   public static final int NW_RESIZE_CURSOR = 6;
/*     */   public static final int NE_RESIZE_CURSOR = 7;
/*     */   public static final int N_RESIZE_CURSOR = 8;
/*     */   public static final int S_RESIZE_CURSOR = 9;
/*     */   public static final int W_RESIZE_CURSOR = 10;
/*     */   public static final int E_RESIZE_CURSOR = 11;
/*     */   public static final int HAND_CURSOR = 12;
/*     */   public static final int MOVE_CURSOR = 13;
/*     */ 
/*     */   @Deprecated
/* 123 */   protected static Cursor[] predefined = new Cursor[14];
/*     */ 
/* 128 */   private static final Cursor[] predefinedPrivate = new Cursor[14];
/*     */ 
/* 131 */   static final String[][] cursorProperties = { { "AWT.DefaultCursor", "Default Cursor" }, { "AWT.CrosshairCursor", "Crosshair Cursor" }, { "AWT.TextCursor", "Text Cursor" }, { "AWT.WaitCursor", "Wait Cursor" }, { "AWT.SWResizeCursor", "Southwest Resize Cursor" }, { "AWT.SEResizeCursor", "Southeast Resize Cursor" }, { "AWT.NWResizeCursor", "Northwest Resize Cursor" }, { "AWT.NEResizeCursor", "Northeast Resize Cursor" }, { "AWT.NResizeCursor", "North Resize Cursor" }, { "AWT.SResizeCursor", "South Resize Cursor" }, { "AWT.WResizeCursor", "West Resize Cursor" }, { "AWT.EResizeCursor", "East Resize Cursor" }, { "AWT.HandCursor", "Hand Cursor" }, { "AWT.MoveCursor", "Move Cursor" } };
/*     */ 
/* 155 */   int type = 0;
/*     */   public static final int CUSTOM_CURSOR = -1;
/* 166 */   private static final Hashtable systemCustomCursors = new Hashtable(1);
/* 167 */   private static final String systemCustomCursorDirPrefix = initCursorDir();
/*     */ 
/* 177 */   private static final String systemCustomCursorPropertiesFile = systemCustomCursorDirPrefix + "cursors.properties";
/*     */ 
/* 179 */   private static Properties systemCustomCursorProperties = null;
/*     */   private static final String CursorDotPrefix = "Cursor.";
/*     */   private static final String DotFileSuffix = ".File";
/*     */   private static final String DotHotspotSuffix = ".HotSpot";
/*     */   private static final String DotNameSuffix = ".Name";
/*     */   private static final long serialVersionUID = 8028237497568985504L;
/* 191 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.Cursor");
/*     */   private transient long pData;
/* 227 */   private transient Object anchor = new Object();
/*     */   transient CursorDisposer disposer;
/*     */   protected String name;
/*     */ 
/*     */   private static String initCursorDir()
/*     */   {
/* 170 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.home"));
/*     */ 
/* 172 */     return str + File.separator + "lib" + File.separator + "images" + File.separator + "cursors" + File.separator;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private void setPData(long paramLong)
/*     */   {
/* 242 */     this.pData = paramLong;
/* 243 */     if (GraphicsEnvironment.isHeadless()) {
/* 244 */       return;
/*     */     }
/* 246 */     if (this.disposer == null) {
/* 247 */       this.disposer = new CursorDisposer(paramLong);
/*     */ 
/* 249 */       if (this.anchor == null) {
/* 250 */         this.anchor = new Object();
/*     */       }
/* 252 */       Disposer.addRecord(this.anchor, this.disposer);
/*     */     } else {
/* 254 */       this.disposer.pData = paramLong;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Cursor getPredefinedCursor(int paramInt)
/*     */   {
/* 275 */     if ((paramInt < 0) || (paramInt > 13)) {
/* 276 */       throw new IllegalArgumentException("illegal cursor type");
/*     */     }
/* 278 */     Cursor localCursor = predefinedPrivate[paramInt];
/* 279 */     if (localCursor == null)
/*     */     {
/*     */       void tmp42_39 = new Cursor(paramInt); localCursor = tmp42_39; predefinedPrivate[paramInt] = tmp42_39;
/*     */     }
/*     */ 
/* 283 */     if (predefined[paramInt] == null) {
/* 284 */       predefined[paramInt] = localCursor;
/*     */     }
/* 286 */     return localCursor;
/*     */   }
/*     */ 
/*     */   public static Cursor getSystemCustomCursor(String paramString)
/*     */     throws AWTException, HeadlessException
/*     */   {
/* 300 */     GraphicsEnvironment.checkHeadless();
/* 301 */     Cursor localCursor = (Cursor)systemCustomCursors.get(paramString);
/*     */ 
/* 303 */     if (localCursor == null) {
/* 304 */       synchronized (systemCustomCursors) {
/* 305 */         if (systemCustomCursorProperties == null) {
/* 306 */           loadSystemCustomCursorProperties();
/*     */         }
/*     */       }
/* 309 */       ??? = "Cursor." + paramString;
/* 310 */       String str1 = (String)??? + ".File";
/*     */ 
/* 312 */       if (!systemCustomCursorProperties.containsKey(str1)) {
/* 313 */         if (log.isLoggable(400)) {
/* 314 */           log.finer("Cursor.getSystemCustomCursor(" + paramString + ") returned null");
/*     */         }
/* 316 */         return null;
/*     */       }
/*     */ 
/* 319 */       String str2 = systemCustomCursorProperties.getProperty(str1);
/*     */ 
/* 322 */       String str3 = systemCustomCursorProperties.getProperty((String)??? + ".Name");
/*     */ 
/* 324 */       if (str3 == null) str3 = paramString;
/*     */ 
/* 326 */       String str4 = systemCustomCursorProperties.getProperty((String)??? + ".HotSpot");
/*     */ 
/* 328 */       if (str4 == null) {
/* 329 */         throw new AWTException("no hotspot property defined for cursor: " + paramString);
/*     */       }
/* 331 */       StringTokenizer localStringTokenizer = new StringTokenizer(str4, ",");
/*     */ 
/* 333 */       if (localStringTokenizer.countTokens() != 2) {
/* 334 */         throw new AWTException("failed to parse hotspot property for cursor: " + paramString);
/*     */       }
/* 336 */       NumberFormatException localNumberFormatException1 = 0;
/* 337 */       int i = 0;
/*     */       try
/*     */       {
/* 340 */         localNumberFormatException1 = Integer.parseInt(localStringTokenizer.nextToken());
/* 341 */         i = Integer.parseInt(localStringTokenizer.nextToken());
/*     */       } catch (NumberFormatException localNumberFormatException2) {
/* 343 */         throw new AWTException("failed to parse hotspot property for cursor: " + paramString);
/*     */       }
/*     */       try
/*     */       {
/* 347 */         localNumberFormatException2 = localNumberFormatException1;
/* 348 */         final int j = i;
/* 349 */         final String str5 = str3;
/*     */ 
/* 351 */         localCursor = (Cursor)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() throws Exception {
/* 354 */             Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 355 */             Image localImage = localToolkit.getImage(Cursor.systemCustomCursorDirPrefix + this.val$fileName);
/*     */ 
/* 357 */             return localToolkit.createCustomCursor(localImage, new Point(localNumberFormatException2, j), str5);
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (Exception localException) {
/* 362 */         throw new AWTException("Exception: " + localException.getClass() + " " + localException.getMessage() + " occurred while creating cursor " + paramString);
/*     */       }
/*     */ 
/* 367 */       if (localCursor == null) {
/* 368 */         if (log.isLoggable(400))
/* 369 */           log.finer("Cursor.getSystemCustomCursor(" + paramString + ") returned null");
/*     */       }
/*     */       else {
/* 372 */         systemCustomCursors.put(paramString, localCursor);
/*     */       }
/*     */     }
/*     */ 
/* 376 */     return localCursor;
/*     */   }
/*     */ 
/*     */   public static Cursor getDefaultCursor()
/*     */   {
/* 383 */     return getPredefinedCursor(0);
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"type"})
/*     */   public Cursor(int paramInt)
/*     */   {
/* 394 */     if ((paramInt < 0) || (paramInt > 13)) {
/* 395 */       throw new IllegalArgumentException("illegal cursor type");
/*     */     }
/* 397 */     this.type = paramInt;
/*     */ 
/* 400 */     this.name = Toolkit.getProperty(cursorProperties[paramInt][0], cursorProperties[paramInt][1]);
/*     */   }
/*     */ 
/*     */   protected Cursor(String paramString)
/*     */   {
/* 413 */     this.type = -1;
/* 414 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 421 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 430 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 439 */     return getClass().getName() + "[" + getName() + "]";
/*     */   }
/*     */ 
/*     */   private static void loadSystemCustomCursorProperties()
/*     */     throws AWTException
/*     */   {
/* 446 */     synchronized (systemCustomCursors) {
/* 447 */       systemCustomCursorProperties = new Properties();
/*     */       try
/*     */       {
/* 450 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() throws Exception {
/* 453 */             FileInputStream localFileInputStream = null;
/*     */             try {
/* 455 */               localFileInputStream = new FileInputStream(Cursor.systemCustomCursorPropertiesFile);
/*     */ 
/* 457 */               Cursor.systemCustomCursorProperties.load(localFileInputStream);
/*     */             } finally {
/* 459 */               if (localFileInputStream != null)
/* 460 */                 localFileInputStream.close();
/*     */             }
/* 462 */             return null;
/*     */           } } );
/*     */       }
/*     */       catch (Exception localException) {
/* 466 */         systemCustomCursorProperties = null;
/* 467 */         throw new AWTException("Exception: " + localException.getClass() + " " + localException.getMessage() + " occurred while loading: " + systemCustomCursorPropertiesFile);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void finalizeImpl(long paramLong);
/*     */ 
/*     */   static
/*     */   {
/* 195 */     Toolkit.loadLibraries();
/* 196 */     if (!GraphicsEnvironment.isHeadless()) {
/* 197 */       initIDs();
/*     */     }
/*     */ 
/* 200 */     AWTAccessor.setCursorAccessor(new AWTAccessor.CursorAccessor()
/*     */     {
/*     */       public long getPData(Cursor paramAnonymousCursor) {
/* 203 */         return paramAnonymousCursor.pData;
/*     */       }
/*     */ 
/*     */       public void setPData(Cursor paramAnonymousCursor, long paramAnonymousLong) {
/* 207 */         paramAnonymousCursor.pData = paramAnonymousLong;
/*     */       }
/*     */ 
/*     */       public int getType(Cursor paramAnonymousCursor) {
/* 211 */         return paramAnonymousCursor.type;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static class CursorDisposer
/*     */     implements DisposerRecord
/*     */   {
/*     */     volatile long pData;
/*     */ 
/*     */     public CursorDisposer(long paramLong)
/*     */     {
/* 232 */       this.pData = paramLong;
/*     */     }
/*     */     public void dispose() {
/* 235 */       if (this.pData != 0L)
/* 236 */         Cursor.finalizeImpl(this.pData);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Cursor
 * JD-Core Version:    0.6.2
 */