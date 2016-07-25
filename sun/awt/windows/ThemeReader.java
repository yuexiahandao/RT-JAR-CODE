/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ public class ThemeReader
/*     */ {
/*  49 */   private static final HashMap<String, Long> widgetToTheme = new HashMap();
/*     */ 
/*  55 */   private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*     */ 
/*  57 */   private static final Lock readLock = readWriteLock.readLock();
/*  58 */   private static final Lock writeLock = readWriteLock.writeLock();
/*  59 */   private static volatile boolean valid = false;
/*     */   static volatile boolean xpStyleEnabled;
/*     */ 
/*     */   static void flush()
/*     */   {
/*  66 */     valid = false;
/*     */   }
/*     */ 
/*     */   public static native boolean isThemed();
/*     */ 
/*     */   public static boolean isXPStyleEnabled() {
/*  72 */     return xpStyleEnabled;
/*     */   }
/*     */ 
/*     */   private static Long getThemeImpl(String paramString)
/*     */   {
/*  77 */     Long localLong = (Long)widgetToTheme.get(paramString);
/*  78 */     if (localLong == null) {
/*  79 */       int i = paramString.indexOf("::");
/*  80 */       if (i > 0)
/*     */       {
/*  83 */         setWindowTheme(paramString.substring(0, i));
/*  84 */         localLong = Long.valueOf(openTheme(paramString.substring(i + 2)));
/*  85 */         setWindowTheme(null);
/*     */       } else {
/*  87 */         localLong = Long.valueOf(openTheme(paramString));
/*     */       }
/*  89 */       widgetToTheme.put(paramString, localLong);
/*     */     }
/*  91 */     return localLong;
/*     */   }
/*     */ 
/*     */   private static Long getTheme(String paramString)
/*     */   {
/*  97 */     if (!valid) {
/*  98 */       readLock.unlock();
/*  99 */       writeLock.lock();
/*     */       try {
/* 101 */         if (!valid)
/*     */         {
/* 103 */           for (localObject1 = widgetToTheme.values().iterator(); ((Iterator)localObject1).hasNext(); ) { Long localLong = (Long)((Iterator)localObject1).next();
/* 104 */             closeTheme(localLong.longValue());
/*     */           }
/* 106 */           widgetToTheme.clear();
/* 107 */           valid = true;
/*     */         }
/*     */       } finally {
/* 110 */         readLock.lock();
/* 111 */         writeLock.unlock();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 116 */     Object localObject1 = (Long)widgetToTheme.get(paramString);
/* 117 */     if (localObject1 == null) {
/* 118 */       readLock.unlock();
/* 119 */       writeLock.lock();
/*     */       try {
/* 121 */         localObject1 = getThemeImpl(paramString);
/*     */       } finally {
/* 123 */         readLock.lock();
/* 124 */         writeLock.unlock();
/*     */       }
/*     */     }
/* 127 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public static native void paintBackground(int[] paramArrayOfInt, long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */ 
/*     */   public static void paintBackground(int[] paramArrayOfInt, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/* 135 */     readLock.lock();
/*     */     try {
/* 137 */       paintBackground(paramArrayOfInt, getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     } finally {
/* 139 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native Insets getThemeMargins(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public static Insets getThemeMargins(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 147 */     readLock.lock();
/*     */     try {
/* 149 */       return getThemeMargins(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3);
/*     */     } finally {
/* 151 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native boolean isThemePartDefined(long paramLong, int paramInt1, int paramInt2);
/*     */ 
/*     */   public static boolean isThemePartDefined(String paramString, int paramInt1, int paramInt2) {
/* 158 */     readLock.lock();
/*     */     try {
/* 160 */       return isThemePartDefined(getTheme(paramString).longValue(), paramInt1, paramInt2);
/*     */     } finally {
/* 162 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native Color getColor(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public static Color getColor(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 170 */     readLock.lock();
/*     */     try {
/* 172 */       return getColor(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3);
/*     */     } finally {
/* 174 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native int getInt(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public static int getInt(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 182 */     readLock.lock();
/*     */     try {
/* 184 */       return getInt(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3);
/*     */     } finally {
/* 186 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native int getEnum(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public static int getEnum(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 194 */     readLock.lock();
/*     */     try {
/* 196 */       return getEnum(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3);
/*     */     } finally {
/* 198 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native boolean getBoolean(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public static boolean getBoolean(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 207 */     readLock.lock();
/*     */     try {
/* 209 */       return getBoolean(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3);
/*     */     } finally {
/* 211 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native boolean getSysBoolean(long paramLong, int paramInt);
/*     */ 
/*     */   public static boolean getSysBoolean(String paramString, int paramInt) {
/* 218 */     readLock.lock();
/*     */     try {
/* 220 */       return getSysBoolean(getTheme(paramString).longValue(), paramInt);
/*     */     } finally {
/* 222 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native Point getPoint(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public static Point getPoint(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 230 */     readLock.lock();
/*     */     try {
/* 232 */       return getPoint(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3);
/*     */     } finally {
/* 234 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native Dimension getPosition(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public static Dimension getPosition(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 243 */     readLock.lock();
/*     */     try {
/* 245 */       return getPosition(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3);
/*     */     } finally {
/* 247 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native Dimension getPartSize(long paramLong, int paramInt1, int paramInt2);
/*     */ 
/*     */   public static Dimension getPartSize(String paramString, int paramInt1, int paramInt2) {
/* 254 */     readLock.lock();
/*     */     try {
/* 256 */       return getPartSize(getTheme(paramString).longValue(), paramInt1, paramInt2);
/*     */     } finally {
/* 258 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native long openTheme(String paramString);
/*     */ 
/*     */   public static native void closeTheme(long paramLong);
/*     */ 
/*     */   public static native void setWindowTheme(String paramString);
/*     */ 
/*     */   private static native long getThemeTransitionDuration(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public static long getThemeTransitionDuration(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 272 */     readLock.lock();
/*     */     try {
/* 274 */       return getThemeTransitionDuration(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     finally {
/* 277 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static native boolean isGetThemeTransitionDurationDefined();
/*     */ 
/*     */   private static native Insets getThemeBackgroundContentMargins(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public static Insets getThemeBackgroundContentMargins(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 286 */     readLock.lock();
/*     */     try {
/* 288 */       return getThemeBackgroundContentMargins(getTheme(paramString).longValue(), paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     finally {
/* 291 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.ThemeReader
 * JD-Core Version:    0.6.2
 */