/*     */ package sun.util.calendar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.SimpleTimeZone;
/*     */ import java.util.TimeZone;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class ZoneInfo extends TimeZone
/*     */ {
/*     */   private static final int UTC_TIME = 0;
/*     */   private static final int STANDARD_TIME = 1;
/*     */   private static final int WALL_TIME = 2;
/*     */   private static final long OFFSET_MASK = 15L;
/*     */   private static final long DST_MASK = 240L;
/*     */   private static final int DST_NSHIFT = 4;
/*     */   private static final long ABBR_MASK = 3840L;
/*     */   private static final int TRANSITION_NSHIFT = 12;
/*  87 */   static final boolean USE_OLDMAPPING = (str.equals("yes")) || (str.equals("true"));
/*     */ 
/*  91 */   static final String[] conflictingIDs = { "EST", "MST", "HST" };
/*     */ 
/*  95 */   private static final CalendarSystem gcal = CalendarSystem.getGregorianCalendar();
/*     */   private int rawOffset;
/* 111 */   private int rawOffsetDiff = 0;
/*     */   private int checksum;
/*     */   private int dstSavings;
/*     */   private long[] transitions;
/*     */   private int[] offsets;
/*     */   private int[] simpleTimeZoneParams;
/* 180 */   private boolean willGMTOffsetChange = false;
/*     */ 
/* 185 */   private transient boolean dirty = false;
/*     */   private static final long serialVersionUID = 2653134537216586139L;
/*     */   private transient SimpleTimeZone lastRule;
/*     */   private static SoftReference<Map<String, String>> aliasTable;
/*     */ 
/*     */   public ZoneInfo()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ZoneInfo(String paramString, int paramInt)
/*     */   {
/* 199 */     this(paramString, paramInt, 0, 0, null, null, null, false);
/*     */   }
/*     */ 
/*     */   ZoneInfo(String paramString, int paramInt1, int paramInt2, int paramInt3, long[] paramArrayOfLong, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean)
/*     */   {
/* 225 */     setID(paramString);
/* 226 */     this.rawOffset = paramInt1;
/* 227 */     this.dstSavings = paramInt2;
/* 228 */     this.checksum = paramInt3;
/* 229 */     this.transitions = paramArrayOfLong;
/* 230 */     this.offsets = paramArrayOfInt1;
/* 231 */     this.simpleTimeZoneParams = paramArrayOfInt2;
/* 232 */     this.willGMTOffsetChange = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getOffset(long paramLong)
/*     */   {
/* 244 */     return getOffsets(paramLong, null, 0);
/*     */   }
/*     */ 
/*     */   public int getOffsets(long paramLong, int[] paramArrayOfInt) {
/* 248 */     return getOffsets(paramLong, paramArrayOfInt, 0);
/*     */   }
/*     */ 
/*     */   public int getOffsetsByStandard(long paramLong, int[] paramArrayOfInt) {
/* 252 */     return getOffsets(paramLong, paramArrayOfInt, 1);
/*     */   }
/*     */ 
/*     */   public int getOffsetsByWall(long paramLong, int[] paramArrayOfInt) {
/* 256 */     return getOffsets(paramLong, paramArrayOfInt, 2);
/*     */   }
/*     */ 
/*     */   private int getOffsets(long paramLong, int[] paramArrayOfInt, int paramInt)
/*     */   {
/* 261 */     if (this.transitions == null) {
/* 262 */       i = getLastRawOffset();
/* 263 */       if (paramArrayOfInt != null) {
/* 264 */         paramArrayOfInt[0] = i;
/* 265 */         paramArrayOfInt[1] = 0;
/*     */       }
/* 267 */       return i;
/*     */     }
/*     */ 
/* 270 */     paramLong -= this.rawOffsetDiff;
/* 271 */     int i = getTransitionIndex(paramLong, paramInt);
/*     */ 
/* 275 */     if (i < 0) {
/* 276 */       int j = getLastRawOffset();
/* 277 */       if (paramArrayOfInt != null) {
/* 278 */         paramArrayOfInt[0] = j;
/* 279 */         paramArrayOfInt[1] = 0;
/*     */       }
/* 281 */       return j;
/*     */     }
/*     */     int i1;
/* 284 */     if (i < this.transitions.length) {
/* 285 */       long l1 = this.transitions[i];
/* 286 */       int m = this.offsets[((int)(l1 & 0xF))] + this.rawOffsetDiff;
/* 287 */       if (paramArrayOfInt != null) {
/* 288 */         int n = (int)(l1 >>> 4 & 0xF);
/* 289 */         i1 = n == 0 ? 0 : this.offsets[n];
/* 290 */         paramArrayOfInt[0] = (m - i1);
/* 291 */         paramArrayOfInt[1] = i1;
/*     */       }
/* 293 */       return m;
/*     */     }
/*     */ 
/* 298 */     SimpleTimeZone localSimpleTimeZone = getLastRule();
/* 299 */     if (localSimpleTimeZone != null) {
/* 300 */       k = localSimpleTimeZone.getRawOffset();
/* 301 */       long l2 = paramLong;
/* 302 */       if (paramInt != 0) {
/* 303 */         l2 -= this.rawOffset;
/*     */       }
/* 305 */       i1 = localSimpleTimeZone.getOffset(l2) - this.rawOffset;
/*     */ 
/* 308 */       if ((i1 > 0) && (localSimpleTimeZone.getOffset(l2 - i1) == k)) {
/* 309 */         i1 = 0;
/*     */       }
/*     */ 
/* 312 */       if (paramArrayOfInt != null) {
/* 313 */         paramArrayOfInt[0] = k;
/* 314 */         paramArrayOfInt[1] = i1;
/*     */       }
/* 316 */       return k + i1;
/*     */     }
/* 318 */     int k = getLastRawOffset();
/* 319 */     if (paramArrayOfInt != null) {
/* 320 */       paramArrayOfInt[0] = k;
/* 321 */       paramArrayOfInt[1] = 0;
/*     */     }
/* 323 */     return k;
/*     */   }
/*     */ 
/*     */   private final int getTransitionIndex(long paramLong, int paramInt) {
/* 327 */     int i = 0;
/* 328 */     int j = this.transitions.length - 1;
/*     */ 
/* 330 */     while (i <= j) {
/* 331 */       int k = (i + j) / 2;
/* 332 */       long l1 = this.transitions[k];
/* 333 */       long l2 = l1 >> 12;
/* 334 */       if (paramInt != 0) {
/* 335 */         l2 += this.offsets[((int)(l1 & 0xF))];
/*     */       }
/* 337 */       if (paramInt == 1) {
/* 338 */         int m = (int)(l1 >>> 4 & 0xF);
/* 339 */         if (m != 0) {
/* 340 */           l2 -= this.offsets[m];
/*     */         }
/*     */       }
/*     */ 
/* 344 */       if (l2 < paramLong)
/* 345 */         i = k + 1;
/* 346 */       else if (l2 > paramLong)
/* 347 */         j = k - 1;
/*     */       else {
/* 349 */         return k;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 354 */     if (i >= this.transitions.length) {
/* 355 */       return i;
/*     */     }
/* 357 */     return i - 1;
/*     */   }
/*     */ 
/*     */   public int getOffset(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 384 */     if ((paramInt6 < 0) || (paramInt6 >= 86400000)) {
/* 385 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 388 */     if (paramInt1 == 0)
/* 389 */       paramInt2 = 1 - paramInt2;
/* 390 */     else if (paramInt1 != 1) {
/* 391 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 394 */     CalendarDate localCalendarDate = gcal.newCalendarDate(null);
/* 395 */     localCalendarDate.setDate(paramInt2, paramInt3 + 1, paramInt4);
/* 396 */     if (!gcal.validate(localCalendarDate)) {
/* 397 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 401 */     if ((paramInt5 < 1) || (paramInt5 > 7))
/*     */     {
/* 403 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 406 */     if (this.transitions == null) {
/* 407 */       return getLastRawOffset();
/*     */     }
/*     */ 
/* 410 */     long l = gcal.getTime(localCalendarDate) + paramInt6;
/* 411 */     l -= this.rawOffset;
/* 412 */     return getOffsets(l, null, 0);
/*     */   }
/*     */ 
/*     */   public synchronized void setRawOffset(int paramInt)
/*     */   {
/* 424 */     if (paramInt == this.rawOffset + this.rawOffsetDiff) {
/* 425 */       return;
/*     */     }
/* 427 */     this.rawOffsetDiff = (paramInt - this.rawOffset);
/* 428 */     if (this.lastRule != null) {
/* 429 */       this.lastRule.setRawOffset(paramInt);
/*     */     }
/* 431 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   public int getRawOffset()
/*     */   {
/* 442 */     if (!this.willGMTOffsetChange) {
/* 443 */       return this.rawOffset + this.rawOffsetDiff;
/*     */     }
/*     */ 
/* 446 */     int[] arrayOfInt = new int[2];
/* 447 */     getOffsets(System.currentTimeMillis(), arrayOfInt, 0);
/* 448 */     return arrayOfInt[0];
/*     */   }
/*     */ 
/*     */   public boolean isDirty() {
/* 452 */     return this.dirty;
/*     */   }
/*     */ 
/*     */   private int getLastRawOffset() {
/* 456 */     return this.rawOffset + this.rawOffsetDiff;
/*     */   }
/*     */ 
/*     */   public boolean useDaylightTime()
/*     */   {
/* 463 */     return this.simpleTimeZoneParams != null;
/*     */   }
/*     */ 
/*     */   public boolean observesDaylightTime()
/*     */   {
/* 468 */     if (this.simpleTimeZoneParams != null) {
/* 469 */       return true;
/*     */     }
/* 471 */     if (this.transitions == null) {
/* 472 */       return false;
/*     */     }
/*     */ 
/* 478 */     long l = System.currentTimeMillis() - this.rawOffsetDiff;
/* 479 */     int i = getTransitionIndex(l, 0);
/*     */ 
/* 482 */     if (i < 0) {
/* 483 */       return false;
/*     */     }
/*     */ 
/* 487 */     for (int j = i; j < this.transitions.length; j++) {
/* 488 */       if ((this.transitions[j] & 0xF0) != 0L) {
/* 489 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 493 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inDaylightTime(Date paramDate)
/*     */   {
/* 500 */     if (paramDate == null) {
/* 501 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 504 */     if (this.transitions == null) {
/* 505 */       return false;
/*     */     }
/*     */ 
/* 508 */     long l = paramDate.getTime() - this.rawOffsetDiff;
/* 509 */     int i = getTransitionIndex(l, 0);
/*     */ 
/* 512 */     if (i < 0) {
/* 513 */       return false;
/*     */     }
/*     */ 
/* 517 */     if (i < this.transitions.length) {
/* 518 */       return (this.transitions[i] & 0xF0) != 0L;
/*     */     }
/*     */ 
/* 522 */     SimpleTimeZone localSimpleTimeZone = getLastRule();
/* 523 */     if (localSimpleTimeZone != null) {
/* 524 */       return localSimpleTimeZone.inDaylightTime(paramDate);
/*     */     }
/* 526 */     return false;
/*     */   }
/*     */ 
/*     */   public int getDSTSavings()
/*     */   {
/* 537 */     return this.dstSavings;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 560 */     return getClass().getName() + "[id=\"" + getID() + "\"" + ",offset=" + getLastRawOffset() + ",dstSavings=" + this.dstSavings + ",useDaylight=" + useDaylightTime() + ",transitions=" + (this.transitions != null ? this.transitions.length : 0) + ",lastRule=" + (this.lastRule == null ? getLastRuleInstance() : this.lastRule) + "]";
/*     */   }
/*     */ 
/*     */   public static String[] getAvailableIDs()
/*     */   {
/* 576 */     Object localObject1 = ZoneInfoFile.getZoneIDs();
/* 577 */     List localList = ZoneInfoFile.getExcludedZones();
/* 578 */     if (localList != null)
/*     */     {
/* 580 */       localObject2 = new ArrayList(((List)localObject1).size() + localList.size());
/* 581 */       ((List)localObject2).addAll((Collection)localObject1);
/* 582 */       ((List)localObject2).addAll(localList);
/* 583 */       localObject1 = localObject2;
/*     */     }
/* 585 */     Object localObject2 = new String[((List)localObject1).size()];
/* 586 */     return (String[])((List)localObject1).toArray((Object[])localObject2);
/*     */   }
/*     */ 
/*     */   public static String[] getAvailableIDs(int paramInt)
/*     */   {
/* 600 */     ArrayList localArrayList = new ArrayList();
/* 601 */     List localList1 = ZoneInfoFile.getZoneIDs();
/* 602 */     int[] arrayOfInt = ZoneInfoFile.getRawOffsets();
/*     */     Object localObject;
/* 605 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 606 */       if (arrayOfInt[i] == paramInt) {
/* 607 */         localObject = ZoneInfoFile.getRawOffsetIndices();
/* 608 */         for (int j = 0; j < localObject.length; j++) {
/* 609 */           if (localObject[j] == i) {
/* 610 */             localArrayList.add(localList1.get(j++));
/* 611 */             while ((j < localObject.length) && (localObject[j] == i)) {
/* 612 */               localArrayList.add(localList1.get(j++));
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 625 */     List localList2 = ZoneInfoFile.getExcludedZones();
/* 626 */     if (localList2 != null) {
/* 627 */       for (localObject = localList2.iterator(); ((Iterator)localObject).hasNext(); ) { String str = (String)((Iterator)localObject).next();
/* 628 */         TimeZone localTimeZone = getTimeZone(str);
/* 629 */         if ((localTimeZone != null) && (localTimeZone.getRawOffset() == paramInt)) {
/* 630 */           localArrayList.add(str);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 635 */     String[] arrayOfString = new String[localArrayList.size()];
/* 636 */     localArrayList.toArray(arrayOfString);
/* 637 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public static TimeZone getTimeZone(String paramString)
/*     */   {
/* 649 */     String str1 = null;
/*     */ 
/* 655 */     if (USE_OLDMAPPING) {
/* 656 */       localObject = (String)TzIDOldMapping.MAP.get(paramString);
/* 657 */       if (localObject != null) {
/* 658 */         str1 = paramString;
/* 659 */         paramString = (String)localObject;
/*     */       }
/*     */     }
/*     */ 
/* 663 */     Object localObject = ZoneInfoFile.getZoneInfo(paramString);
/* 664 */     if (localObject == null) {
/*     */       try
/*     */       {
/* 667 */         Map localMap = getAliasTable();
/* 668 */         String str2 = paramString;
/* 669 */         while ((str2 = (String)localMap.get(str2)) != null) {
/* 670 */           localObject = ZoneInfoFile.getZoneInfo(str2);
/* 671 */           if (localObject != null) {
/* 672 */             ((ZoneInfo)localObject).setID(paramString);
/* 673 */             localObject = ZoneInfoFile.addToCache(paramString, (ZoneInfo)localObject);
/* 674 */             localObject = (ZoneInfo)((ZoneInfo)localObject).clone();
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 683 */     if ((str1 != null) && (localObject != null)) {
/* 684 */       ((ZoneInfo)localObject).setID(str1);
/*     */     }
/* 686 */     return localObject;
/*     */   }
/*     */ 
/*     */   private synchronized SimpleTimeZone getLastRule()
/*     */   {
/* 697 */     if (this.lastRule == null) {
/* 698 */       this.lastRule = getLastRuleInstance();
/*     */     }
/* 700 */     return this.lastRule;
/*     */   }
/*     */ 
/*     */   public SimpleTimeZone getLastRuleInstance()
/*     */   {
/* 711 */     if (this.simpleTimeZoneParams == null) {
/* 712 */       return null;
/*     */     }
/* 714 */     if (this.simpleTimeZoneParams.length == 10) {
/* 715 */       return new SimpleTimeZone(getLastRawOffset(), getID(), this.simpleTimeZoneParams[0], this.simpleTimeZoneParams[1], this.simpleTimeZoneParams[2], this.simpleTimeZoneParams[3], this.simpleTimeZoneParams[4], this.simpleTimeZoneParams[5], this.simpleTimeZoneParams[6], this.simpleTimeZoneParams[7], this.simpleTimeZoneParams[8], this.simpleTimeZoneParams[9], this.dstSavings);
/*     */     }
/*     */ 
/* 728 */     return new SimpleTimeZone(getLastRawOffset(), getID(), this.simpleTimeZoneParams[0], this.simpleTimeZoneParams[1], this.simpleTimeZoneParams[2], this.simpleTimeZoneParams[3], this.simpleTimeZoneParams[4], this.simpleTimeZoneParams[5], this.simpleTimeZoneParams[6], this.simpleTimeZoneParams[7], this.dstSavings);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 744 */     ZoneInfo localZoneInfo = (ZoneInfo)super.clone();
/* 745 */     localZoneInfo.lastRule = null;
/* 746 */     return localZoneInfo;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 755 */     return getLastRawOffset() ^ this.checksum;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 766 */     if (this == paramObject) {
/* 767 */       return true;
/*     */     }
/* 769 */     if (!(paramObject instanceof ZoneInfo)) {
/* 770 */       return false;
/*     */     }
/* 772 */     ZoneInfo localZoneInfo = (ZoneInfo)paramObject;
/* 773 */     return (getID().equals(localZoneInfo.getID())) && (getLastRawOffset() == localZoneInfo.getLastRawOffset()) && (this.checksum == localZoneInfo.checksum);
/*     */   }
/*     */ 
/*     */   public boolean hasSameRules(TimeZone paramTimeZone)
/*     */   {
/* 790 */     if (this == paramTimeZone) {
/* 791 */       return true;
/*     */     }
/* 793 */     if (paramTimeZone == null) {
/* 794 */       return false;
/*     */     }
/* 796 */     if (!(paramTimeZone instanceof ZoneInfo)) {
/* 797 */       if (getRawOffset() != paramTimeZone.getRawOffset()) {
/* 798 */         return false;
/*     */       }
/*     */ 
/* 802 */       if ((this.transitions == null) && (!useDaylightTime()) && (!paramTimeZone.useDaylightTime()))
/*     */       {
/* 805 */         return true;
/*     */       }
/* 807 */       return false;
/*     */     }
/* 809 */     if (getLastRawOffset() != ((ZoneInfo)paramTimeZone).getLastRawOffset()) {
/* 810 */       return false;
/*     */     }
/* 812 */     return this.checksum == ((ZoneInfo)paramTimeZone).checksum;
/*     */   }
/*     */ 
/*     */   static Map<String, String> getCachedAliasTable()
/*     */   {
/* 818 */     Map localMap = null;
/*     */ 
/* 820 */     SoftReference localSoftReference = aliasTable;
/* 821 */     if (localSoftReference != null) {
/* 822 */       localMap = (Map)localSoftReference.get();
/*     */     }
/* 824 */     return localMap;
/*     */   }
/*     */ 
/*     */   public static synchronized Map<String, String> getAliasTable()
/*     */   {
/* 836 */     Map localMap = getCachedAliasTable();
/* 837 */     if (localMap == null) {
/* 838 */       localMap = ZoneInfoFile.getZoneAliases();
/* 839 */       if (localMap != null) {
/* 840 */         if (!USE_OLDMAPPING)
/*     */         {
/* 842 */           for (String str : conflictingIDs) {
/* 843 */             localMap.remove(str);
/*     */           }
/*     */         }
/* 846 */         aliasTable = new SoftReference(localMap);
/*     */       }
/*     */     }
/* 849 */     return localMap;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 854 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 857 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  85 */     String str = ((String)AccessController.doPrivileged(new GetPropertyAction("sun.timezone.ids.oldmapping", "false"))).toLowerCase(Locale.ROOT);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.calendar.ZoneInfo
 * JD-Core Version:    0.6.2
 */