/*     */ package java.util.logging;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class Level
/*     */   implements Serializable
/*     */ {
/*     */   private static final String defaultBundle = "sun.util.logging.resources.logging";
/*     */   private final String name;
/*     */   private final int value;
/*     */   private final String resourceBundleName;
/*     */   private transient String localizedLevelName;
/*     */   private transient Locale cachedLocale;
/*  92 */   public static final Level OFF = new Level("OFF", 2147483647, "sun.util.logging.resources.logging");
/*     */ 
/* 103 */   public static final Level SEVERE = new Level("SEVERE", 1000, "sun.util.logging.resources.logging");
/*     */ 
/* 113 */   public static final Level WARNING = new Level("WARNING", 900, "sun.util.logging.resources.logging");
/*     */ 
/* 124 */   public static final Level INFO = new Level("INFO", 800, "sun.util.logging.resources.logging");
/*     */ 
/* 136 */   public static final Level CONFIG = new Level("CONFIG", 700, "sun.util.logging.resources.logging");
/*     */ 
/* 157 */   public static final Level FINE = new Level("FINE", 500, "sun.util.logging.resources.logging");
/*     */ 
/* 165 */   public static final Level FINER = new Level("FINER", 400, "sun.util.logging.resources.logging");
/*     */ 
/* 171 */   public static final Level FINEST = new Level("FINEST", 300, "sun.util.logging.resources.logging");
/*     */ 
/* 177 */   public static final Level ALL = new Level("ALL", -2147483648, "sun.util.logging.resources.logging");
/*     */   private static final long serialVersionUID = -8176160795706313070L;
/*     */ 
/*     */   protected Level(String paramString, int paramInt)
/*     */   {
/* 192 */     this(paramString, paramInt, null);
/*     */   }
/*     */ 
/*     */   protected Level(String paramString1, int paramInt, String paramString2)
/*     */   {
/* 207 */     if (paramString1 == null) {
/* 208 */       throw new NullPointerException();
/*     */     }
/* 210 */     this.name = paramString1;
/* 211 */     this.value = paramInt;
/* 212 */     this.resourceBundleName = paramString2;
/* 213 */     this.localizedLevelName = (paramString2 == null ? paramString1 : null);
/* 214 */     this.cachedLocale = null;
/* 215 */     KnownLevel.add(this);
/*     */   }
/*     */ 
/*     */   public String getResourceBundleName()
/*     */   {
/* 225 */     return this.resourceBundleName;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 234 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/* 247 */     return getLocalizedLevelName();
/*     */   }
/*     */ 
/*     */   final String getLevelName()
/*     */   {
/* 253 */     return this.name;
/*     */   }
/*     */ 
/*     */   private String computeLocalizedLevelName(Locale paramLocale) {
/* 257 */     ResourceBundle localResourceBundle = ResourceBundle.getBundle(this.resourceBundleName, paramLocale);
/* 258 */     String str = localResourceBundle.getString(this.name);
/*     */ 
/* 260 */     boolean bool = "sun.util.logging.resources.logging".equals(this.resourceBundleName);
/* 261 */     if (!bool) return str;
/*     */ 
/* 266 */     Locale localLocale1 = localResourceBundle.getLocale();
/* 267 */     Locale localLocale2 = (Locale.ROOT.equals(localLocale1)) || (this.name.equals(str.toUpperCase(Locale.ROOT))) ? Locale.ROOT : localLocale1;
/*     */ 
/* 276 */     return Locale.ROOT.equals(localLocale2) ? this.name : str.toUpperCase(localLocale2);
/*     */   }
/*     */ 
/*     */   final String getCachedLocalizedLevelName()
/*     */   {
/* 283 */     if ((this.localizedLevelName != null) && 
/* 284 */       (this.cachedLocale != null) && 
/* 285 */       (this.cachedLocale.equals(Locale.getDefault())))
/*     */     {
/* 288 */       return this.localizedLevelName;
/*     */     }
/*     */ 
/* 293 */     if (this.resourceBundleName == null)
/*     */     {
/* 295 */       return this.name;
/*     */     }
/*     */ 
/* 301 */     return null;
/*     */   }
/*     */ 
/*     */   final synchronized String getLocalizedLevelName()
/*     */   {
/* 307 */     String str = getCachedLocalizedLevelName();
/* 308 */     if (str != null) {
/* 309 */       return str;
/*     */     }
/*     */ 
/* 314 */     Locale localLocale = Locale.getDefault();
/*     */     try {
/* 316 */       this.localizedLevelName = computeLocalizedLevelName(localLocale);
/*     */     } catch (Exception localException) {
/* 318 */       this.localizedLevelName = this.name;
/*     */     }
/* 320 */     this.cachedLocale = localLocale;
/* 321 */     return this.localizedLevelName;
/*     */   }
/*     */ 
/*     */   static Level findLevel(String paramString)
/*     */   {
/* 336 */     if (paramString == null) {
/* 337 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 343 */     KnownLevel localKnownLevel = KnownLevel.findByName(paramString);
/* 344 */     if (localKnownLevel != null) {
/* 345 */       return localKnownLevel.mirroredLevel;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 352 */       int i = Integer.parseInt(paramString);
/* 353 */       localKnownLevel = KnownLevel.findByValue(i);
/* 354 */       if (localKnownLevel == null)
/*     */       {
/* 356 */         Level localLevel = new Level(paramString, i);
/* 357 */         localKnownLevel = KnownLevel.findByValue(i);
/*     */       }
/* 359 */       return localKnownLevel.mirroredLevel;
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException)
/*     */     {
/* 365 */       localKnownLevel = KnownLevel.findByLocalizedLevelName(paramString);
/* 366 */       if (localKnownLevel != null) {
/* 367 */         return localKnownLevel.mirroredLevel;
/*     */       }
/*     */     }
/* 370 */     return null;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 380 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final int intValue()
/*     */   {
/* 390 */     return this.value;
/*     */   }
/*     */ 
/*     */   private Object readResolve()
/*     */   {
/* 398 */     KnownLevel localKnownLevel = KnownLevel.matches(this);
/* 399 */     if (localKnownLevel != null) {
/* 400 */       return localKnownLevel.levelObject;
/*     */     }
/*     */ 
/* 405 */     Level localLevel = new Level(this.name, this.value, this.resourceBundleName);
/* 406 */     return localLevel;
/*     */   }
/*     */ 
/*     */   public static synchronized Level parse(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 438 */     paramString.length();
/*     */ 
/* 443 */     KnownLevel localKnownLevel = KnownLevel.findByName(paramString);
/* 444 */     if (localKnownLevel != null) {
/* 445 */       return localKnownLevel.levelObject;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 452 */       int i = Integer.parseInt(paramString);
/* 453 */       localKnownLevel = KnownLevel.findByValue(i);
/* 454 */       if (localKnownLevel == null)
/*     */       {
/* 456 */         Level localLevel = new Level(paramString, i);
/* 457 */         localKnownLevel = KnownLevel.findByValue(i);
/*     */       }
/* 459 */       return localKnownLevel.levelObject;
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException)
/*     */     {
/* 468 */       localKnownLevel = KnownLevel.findByLocalizedName(paramString);
/* 469 */       if (localKnownLevel != null) {
/* 470 */         return localKnownLevel.levelObject;
/*     */       }
/*     */     }
/*     */ 
/* 474 */     throw new IllegalArgumentException("Bad level \"" + paramString + "\"");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 484 */       Level localLevel = (Level)paramObject;
/* 485 */       return localLevel.value == this.value; } catch (Exception localException) {
/*     */     }
/* 487 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 497 */     return this.value;
/*     */   }
/*     */ 
/*     */   static final class KnownLevel
/*     */   {
/* 521 */     private static Map<String, List<KnownLevel>> nameToLevels = new HashMap();
/* 522 */     private static Map<Integer, List<KnownLevel>> intToLevels = new HashMap();
/*     */     final Level levelObject;
/*     */     final Level mirroredLevel;
/*     */ 
/*     */     KnownLevel(Level paramLevel)
/*     */     {
/* 526 */       this.levelObject = paramLevel;
/* 527 */       if (paramLevel.getClass() == Level.class)
/* 528 */         this.mirroredLevel = paramLevel;
/*     */       else
/* 530 */         this.mirroredLevel = new Level(paramLevel.name, paramLevel.value, paramLevel.resourceBundleName);
/*     */     }
/*     */ 
/*     */     static synchronized void add(Level paramLevel)
/*     */     {
/* 537 */       KnownLevel localKnownLevel = new KnownLevel(paramLevel);
/* 538 */       Object localObject = (List)nameToLevels.get(paramLevel.name);
/* 539 */       if (localObject == null) {
/* 540 */         localObject = new ArrayList();
/* 541 */         nameToLevels.put(paramLevel.name, localObject);
/*     */       }
/* 543 */       ((List)localObject).add(localKnownLevel);
/*     */ 
/* 545 */       localObject = (List)intToLevels.get(Integer.valueOf(paramLevel.value));
/* 546 */       if (localObject == null) {
/* 547 */         localObject = new ArrayList();
/* 548 */         intToLevels.put(Integer.valueOf(paramLevel.value), localObject);
/*     */       }
/* 550 */       ((List)localObject).add(localKnownLevel);
/*     */     }
/*     */ 
/*     */     static synchronized KnownLevel findByName(String paramString)
/*     */     {
/* 555 */       List localList = (List)nameToLevels.get(paramString);
/* 556 */       if (localList != null) {
/* 557 */         return (KnownLevel)localList.get(0);
/*     */       }
/* 559 */       return null;
/*     */     }
/*     */ 
/*     */     static synchronized KnownLevel findByValue(int paramInt)
/*     */     {
/* 564 */       List localList = (List)intToLevels.get(Integer.valueOf(paramInt));
/* 565 */       if (localList != null) {
/* 566 */         return (KnownLevel)localList.get(0);
/*     */       }
/* 568 */       return null;
/*     */     }
/*     */ 
/*     */     static synchronized KnownLevel findByLocalizedLevelName(String paramString)
/*     */     {
/* 577 */       for (List localList : nameToLevels.values()) {
/* 578 */         for (KnownLevel localKnownLevel : localList) {
/* 579 */           String str = localKnownLevel.levelObject.getLocalizedLevelName();
/* 580 */           if (paramString.equals(str)) {
/* 581 */             return localKnownLevel;
/*     */           }
/*     */         }
/*     */       }
/* 585 */       return null;
/*     */     }
/*     */ 
/*     */     static synchronized KnownLevel findByLocalizedName(String paramString)
/*     */     {
/* 591 */       for (List localList : nameToLevels.values()) {
/* 592 */         for (KnownLevel localKnownLevel : localList) {
/* 593 */           String str = localKnownLevel.levelObject.getLocalizedName();
/* 594 */           if (paramString.equals(str)) {
/* 595 */             return localKnownLevel;
/*     */           }
/*     */         }
/*     */       }
/* 599 */       return null;
/*     */     }
/*     */ 
/*     */     static synchronized KnownLevel matches(Level paramLevel) {
/* 603 */       List localList = (List)nameToLevels.get(paramLevel.name);
/* 604 */       if (localList != null) {
/* 605 */         for (KnownLevel localKnownLevel : localList) {
/* 606 */           Level localLevel = localKnownLevel.mirroredLevel;
/* 607 */           if ((paramLevel.value == localLevel.value) && ((paramLevel.resourceBundleName == localLevel.resourceBundleName) || ((paramLevel.resourceBundleName != null) && (paramLevel.resourceBundleName.equals(localLevel.resourceBundleName)))))
/*     */           {
/* 611 */             return localKnownLevel;
/*     */           }
/*     */         }
/*     */       }
/* 615 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.Level
 * JD-Core Version:    0.6.2
 */