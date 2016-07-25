/*     */ package java.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import sun.misc.JavaAWTAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.util.TimeZoneNameUtility;
/*     */ import sun.util.calendar.ZoneInfo;
/*     */ import sun.util.calendar.ZoneInfoFile;
/*     */ 
/*     */ public abstract class TimeZone
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   public static final int SHORT = 0;
/*     */   public static final int LONG = 1;
/*     */   private static final int ONE_MINUTE = 60000;
/*     */   private static final int ONE_HOUR = 3600000;
/*     */   private static final int ONE_DAY = 86400000;
/*     */   static final long serialVersionUID = 3581463369166924961L;
/* 170 */   private static final boolean allowSetDefault = AccessController.doPrivileged(new GetPropertyAction("jdk.util.TimeZone.allowSetDefault")) != null;
/*     */ 
/* 816 */   static final TimeZone NO_TIMEZONE = null;
/*     */   private String ID;
/*     */   private static volatile TimeZone defaultTimeZone;
/*     */   static final String GMT_ID = "GMT";
/*     */   private static final int GMT_ID_LENGTH = 3;
/*     */   private static volatile TimeZone mainAppContextDefault;
/*     */ 
/*     */   public abstract int getOffset(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*     */ 
/*     */   public int getOffset(long paramLong)
/*     */   {
/* 217 */     if (inDaylightTime(new Date(paramLong))) {
/* 218 */       return getRawOffset() + getDSTSavings();
/*     */     }
/* 220 */     return getRawOffset();
/*     */   }
/*     */ 
/*     */   int getOffsets(long paramLong, int[] paramArrayOfInt)
/*     */   {
/* 240 */     int i = getRawOffset();
/* 241 */     int j = 0;
/* 242 */     if (inDaylightTime(new Date(paramLong))) {
/* 243 */       j = getDSTSavings();
/*     */     }
/* 245 */     if (paramArrayOfInt != null) {
/* 246 */       paramArrayOfInt[0] = i;
/* 247 */       paramArrayOfInt[1] = j;
/*     */     }
/* 249 */     return i + j;
/*     */   }
/*     */ 
/*     */   public abstract void setRawOffset(int paramInt);
/*     */ 
/*     */   public abstract int getRawOffset();
/*     */ 
/*     */   public String getID()
/*     */   {
/* 290 */     return this.ID;
/*     */   }
/*     */ 
/*     */   public void setID(String paramString)
/*     */   {
/* 300 */     if (paramString == null) {
/* 301 */       throw new NullPointerException();
/*     */     }
/* 303 */     this.ID = paramString;
/*     */   }
/*     */ 
/*     */   public final String getDisplayName()
/*     */   {
/* 323 */     return getDisplayName(false, 1, Locale.getDefault(Locale.Category.DISPLAY));
/*     */   }
/*     */ 
/*     */   public final String getDisplayName(Locale paramLocale)
/*     */   {
/* 343 */     return getDisplayName(false, 1, paramLocale);
/*     */   }
/*     */ 
/*     */   public final String getDisplayName(boolean paramBoolean, int paramInt)
/*     */   {
/* 371 */     return getDisplayName(paramBoolean, paramInt, Locale.getDefault(Locale.Category.DISPLAY));
/*     */   }
/*     */ 
/*     */   public String getDisplayName(boolean paramBoolean, int paramInt, Locale paramLocale)
/*     */   {
/* 404 */     if ((paramInt != 0) && (paramInt != 1)) {
/* 405 */       throw new IllegalArgumentException("Illegal style: " + paramInt);
/*     */     }
/*     */ 
/* 408 */     String str = getID();
/* 409 */     String[] arrayOfString = getDisplayNames(str, paramLocale);
/* 410 */     if (arrayOfString == null) {
/* 411 */       if (str.startsWith("GMT")) {
/* 412 */         i = str.charAt(3);
/* 413 */         if ((i == 43) || (i == 45)) {
/* 414 */           return str;
/*     */         }
/*     */       }
/* 417 */       i = getRawOffset();
/* 418 */       if (paramBoolean) {
/* 419 */         i += getDSTSavings();
/*     */       }
/* 421 */       return ZoneInfoFile.toCustomID(i);
/*     */     }
/*     */ 
/* 424 */     int i = paramBoolean ? 3 : 1;
/* 425 */     if (paramInt == 0) {
/* 426 */       i++;
/*     */     }
/* 428 */     return arrayOfString[i];
/*     */   }
/*     */ 
/*     */   private static final String[] getDisplayNames(String paramString, Locale paramLocale)
/*     */   {
/* 440 */     Map localMap = DisplayNames.CACHE;
/*     */ 
/* 442 */     SoftReference localSoftReference = (SoftReference)localMap.get(paramString);
/*     */     Object localObject2;
/* 443 */     if (localSoftReference != null) {
/* 444 */       localObject1 = (Map)localSoftReference.get();
/* 445 */       if (localObject1 != null) {
/* 446 */         localObject2 = (String[])((Map)localObject1).get(paramLocale);
/* 447 */         if (localObject2 != null) {
/* 448 */           return localObject2;
/*     */         }
/* 450 */         localObject2 = TimeZoneNameUtility.retrieveDisplayNames(paramString, paramLocale);
/* 451 */         if (localObject2 != null) {
/* 452 */           ((Map)localObject1).put(paramLocale, localObject2);
/*     */         }
/* 454 */         return localObject2;
/*     */       }
/*     */     }
/*     */ 
/* 458 */     Object localObject1 = TimeZoneNameUtility.retrieveDisplayNames(paramString, paramLocale);
/* 459 */     if (localObject1 != null) {
/* 460 */       localObject2 = new ConcurrentHashMap();
/* 461 */       ((Map)localObject2).put(paramLocale, localObject1);
/* 462 */       localSoftReference = new SoftReference(localObject2);
/* 463 */       localMap.put(paramString, localSoftReference);
/*     */     }
/* 465 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public int getDSTSavings()
/*     */   {
/* 495 */     if (useDaylightTime()) {
/* 496 */       return 3600000;
/*     */     }
/* 498 */     return 0;
/*     */   }
/*     */ 
/*     */   public abstract boolean useDaylightTime();
/*     */ 
/*     */   public boolean observesDaylightTime()
/*     */   {
/* 537 */     return (useDaylightTime()) || (inDaylightTime(new Date()));
/*     */   }
/*     */ 
/*     */   public abstract boolean inDaylightTime(Date paramDate);
/*     */ 
/*     */   public static synchronized TimeZone getTimeZone(String paramString)
/*     */   {
/* 562 */     return getTimeZone(paramString, true);
/*     */   }
/*     */ 
/*     */   private static TimeZone getTimeZone(String paramString, boolean paramBoolean) {
/* 566 */     Object localObject = ZoneInfo.getTimeZone(paramString);
/* 567 */     if (localObject == null) {
/* 568 */       localObject = parseCustomTimeZone(paramString);
/* 569 */       if ((localObject == null) && (paramBoolean)) {
/* 570 */         localObject = new ZoneInfo("GMT", 0);
/*     */       }
/*     */     }
/* 573 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static synchronized String[] getAvailableIDs(int paramInt)
/*     */   {
/* 586 */     return ZoneInfo.getAvailableIDs(paramInt);
/*     */   }
/*     */ 
/*     */   public static synchronized String[] getAvailableIDs()
/*     */   {
/* 594 */     return ZoneInfo.getAvailableIDs();
/*     */   }
/*     */ 
/*     */   private static native String getSystemTimeZoneID(String paramString1, String paramString2);
/*     */ 
/*     */   private static native String getSystemGMTOffsetID();
/*     */ 
/*     */   public static TimeZone getDefault()
/*     */   {
/* 617 */     return (TimeZone)getDefaultRef().clone();
/*     */   }
/*     */ 
/*     */   static TimeZone getDefaultRef()
/*     */   {
/* 625 */     TimeZone localTimeZone = getDefaultInAppContext();
/* 626 */     if (localTimeZone == null) {
/* 627 */       localTimeZone = defaultTimeZone;
/* 628 */       if (localTimeZone == null)
/*     */       {
/* 630 */         localTimeZone = setDefaultZone();
/* 631 */         assert (localTimeZone != null);
/*     */       }
/*     */     }
/*     */ 
/* 635 */     return localTimeZone;
/*     */   }
/*     */ 
/*     */   private static synchronized TimeZone setDefaultZone() {
/* 639 */     TimeZone localTimeZone = null;
/*     */ 
/* 641 */     Object localObject1 = (String)AccessController.doPrivileged(new GetPropertyAction("user.timezone"));
/*     */ 
/* 646 */     if ((localObject1 == null) || (((String)localObject1).equals(""))) {
/* 647 */       localObject2 = (String)AccessController.doPrivileged(new GetPropertyAction("user.country"));
/*     */ 
/* 649 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.home"));
/*     */       try
/*     */       {
/* 652 */         localObject1 = getSystemTimeZoneID(str, (String)localObject2);
/* 653 */         if (localObject1 == null)
/* 654 */           localObject1 = "GMT";
/*     */       }
/*     */       catch (NullPointerException localNullPointerException) {
/* 657 */         localObject1 = "GMT";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 663 */     localTimeZone = getTimeZone((String)localObject1, false);
/*     */ 
/* 665 */     if (localTimeZone == null)
/*     */     {
/* 669 */       localObject2 = getSystemGMTOffsetID();
/* 670 */       if (localObject2 != null) {
/* 671 */         localObject1 = localObject2;
/*     */       }
/* 673 */       localTimeZone = getTimeZone((String)localObject1, true);
/*     */     }
/* 675 */     assert (localTimeZone != null);
/*     */ 
/* 677 */     Object localObject2 = localObject1;
/* 678 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 680 */         System.setProperty("user.timezone", this.val$id);
/* 681 */         return null;
/*     */       }
/*     */     });
/* 685 */     defaultTimeZone = localTimeZone;
/* 686 */     return localTimeZone;
/*     */   }
/*     */ 
/*     */   private static boolean hasPermission() {
/* 690 */     boolean bool = true;
/* 691 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 692 */     if (localSecurityManager != null) {
/*     */       try {
/* 694 */         localSecurityManager.checkPermission(new PropertyPermission("user.timezone", "write"));
/*     */       }
/*     */       catch (SecurityException localSecurityException) {
/* 697 */         if (!allowSetDefault) {
/* 698 */           throw localSecurityException;
/*     */         }
/* 700 */         bool = false;
/*     */       }
/*     */     }
/* 703 */     return bool;
/*     */   }
/*     */ 
/*     */   public static void setDefault(TimeZone paramTimeZone)
/*     */   {
/* 716 */     if (hasPermission())
/* 717 */       synchronized (TimeZone.class) {
/* 718 */         defaultTimeZone = paramTimeZone;
/* 719 */         setDefaultInAppContext(null);
/*     */       }
/*     */     else
/* 722 */       setDefaultInAppContext(paramTimeZone);
/*     */   }
/*     */ 
/*     */   private static TimeZone getDefaultInAppContext()
/*     */   {
/* 738 */     if (allowSetDefault)
/*     */     {
/* 740 */       JavaAWTAccess localJavaAWTAccess = SharedSecrets.getJavaAWTAccess();
/* 741 */       if ((System.getSecurityManager() == null) || (localJavaAWTAccess == null))
/* 742 */         return mainAppContextDefault;
/* 743 */       if (localJavaAWTAccess.isDisposed()) {
/* 744 */         return null;
/*     */       }
/* 746 */       TimeZone localTimeZone = (TimeZone)localJavaAWTAccess.get(TimeZone.class);
/* 747 */       if ((localTimeZone == null) && (localJavaAWTAccess.isMainAppContext())) {
/* 748 */         return mainAppContextDefault;
/*     */       }
/* 750 */       return localTimeZone;
/*     */     }
/*     */ 
/* 754 */     return null;
/*     */   }
/*     */ 
/*     */   private static void setDefaultInAppContext(TimeZone paramTimeZone)
/*     */   {
/* 769 */     if (allowSetDefault)
/*     */     {
/* 771 */       JavaAWTAccess localJavaAWTAccess = SharedSecrets.getJavaAWTAccess();
/* 772 */       if ((System.getSecurityManager() == null) || (localJavaAWTAccess == null)) {
/* 773 */         mainAppContextDefault = paramTimeZone;
/* 774 */       } else if (!localJavaAWTAccess.isDisposed()) {
/* 775 */         localJavaAWTAccess.put(TimeZone.class, paramTimeZone);
/* 776 */         if (localJavaAWTAccess.isMainAppContext())
/* 777 */           mainAppContextDefault = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasSameRules(TimeZone paramTimeZone)
/*     */   {
/* 793 */     return (paramTimeZone != null) && (getRawOffset() == paramTimeZone.getRawOffset()) && (useDaylightTime() == paramTimeZone.useDaylightTime());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 805 */       TimeZone localTimeZone = (TimeZone)super.clone();
/* 806 */       localTimeZone.ID = this.ID;
/* 807 */       return localTimeZone; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 809 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   private static final TimeZone parseCustomTimeZone(String paramString)
/*     */   {
/*     */     int i;
/* 850 */     if (((i = paramString.length()) < 5) || (paramString.indexOf("GMT") != 0))
/*     */     {
/* 852 */       return null;
/*     */     }
/*     */ 
/* 860 */     ZoneInfo localZoneInfo = ZoneInfoFile.getZoneInfo(paramString);
/* 861 */     if (localZoneInfo != null) {
/* 862 */       return localZoneInfo;
/*     */     }
/*     */ 
/* 865 */     int j = 3;
/* 866 */     int k = 0;
/* 867 */     int m = paramString.charAt(j++);
/* 868 */     if (m == 45)
/* 869 */       k = 1;
/* 870 */     else if (m != 43) {
/* 871 */       return null;
/*     */     }
/*     */ 
/* 874 */     int n = 0;
/* 875 */     int i1 = 0;
/* 876 */     int i2 = 0;
/* 877 */     int i3 = 0;
/* 878 */     while (j < i) {
/* 879 */       m = paramString.charAt(j++);
/* 880 */       if (m == 58) {
/* 881 */         if (i2 > 0) {
/* 882 */           return null;
/*     */         }
/* 884 */         if (i3 > 2) {
/* 885 */           return null;
/*     */         }
/* 887 */         n = i1;
/* 888 */         i2++;
/* 889 */         i1 = 0;
/* 890 */         i3 = 0;
/*     */       }
/*     */       else {
/* 893 */         if ((m < 48) || (m > 57)) {
/* 894 */           return null;
/*     */         }
/* 896 */         i1 = i1 * 10 + (m - 48);
/* 897 */         i3++;
/*     */       }
/*     */     }
/* 899 */     if (j != i) {
/* 900 */       return null;
/*     */     }
/* 902 */     if (i2 == 0) {
/* 903 */       if (i3 <= 2) {
/* 904 */         n = i1;
/* 905 */         i1 = 0;
/*     */       } else {
/* 907 */         n = i1 / 100;
/* 908 */         i1 %= 100;
/*     */       }
/*     */     }
/* 911 */     else if (i3 != 2) {
/* 912 */       return null;
/*     */     }
/*     */ 
/* 915 */     if ((n > 23) || (i1 > 59)) {
/* 916 */       return null;
/*     */     }
/* 918 */     int i4 = (n * 60 + i1) * 60 * 1000;
/*     */ 
/* 920 */     if (i4 == 0) {
/* 921 */       localZoneInfo = ZoneInfoFile.getZoneInfo("GMT");
/* 922 */       if (k != 0)
/* 923 */         localZoneInfo.setID("GMT-00:00");
/*     */       else
/* 925 */         localZoneInfo.setID("GMT+00:00");
/*     */     }
/*     */     else {
/* 928 */       localZoneInfo = ZoneInfoFile.getCustomTimeZone(paramString, k != 0 ? -i4 : i4);
/*     */     }
/* 930 */     return localZoneInfo;
/*     */   }
/*     */ 
/*     */   private static class DisplayNames
/*     */   {
/* 435 */     private static final Map<String, SoftReference<Map<Locale, String[]>>> CACHE = new ConcurrentHashMap();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.TimeZone
 * JD-Core Version:    0.6.2
 */