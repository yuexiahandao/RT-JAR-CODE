/*      */ package sun.util.calendar;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.nio.file.FileSystem;
/*      */ import java.nio.file.FileSystems;
/*      */ import java.nio.file.LinkOption;
/*      */ import java.nio.file.Path;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class ZoneInfoFile
/*      */ {
/*  378 */   public static final byte[] JAVAZI_LABEL = { 106, 97, 118, 97, 122, 105, 0 };
/*      */ 
/*  381 */   private static final int JAVAZI_LABEL_LENGTH = JAVAZI_LABEL.length;
/*      */   public static final byte JAVAZI_VERSION = 1;
/*      */   public static final byte TAG_RawOffset = 1;
/*      */   public static final byte TAG_LastDSTSaving = 2;
/*      */   public static final byte TAG_CRC32 = 3;
/*      */   public static final byte TAG_Transition = 4;
/*      */   public static final byte TAG_Offset = 5;
/*      */   public static final byte TAG_SimpleTimeZone = 6;
/*      */   public static final byte TAG_GMTOffsetWillChange = 7;
/*      */   public static final String JAVAZM_FILE_NAME = "ZoneInfoMappings";
/*  433 */   public static final byte[] JAVAZM_LABEL = { 106, 97, 118, 97, 122, 109, 0 };
/*      */ 
/*  436 */   private static final int JAVAZM_LABEL_LENGTH = JAVAZM_LABEL.length;
/*      */   public static final byte JAVAZM_VERSION = 1;
/*      */   public static final byte TAG_ZoneIDs = 64;
/*      */   public static final byte TAG_RawOffsets = 65;
/*      */   public static final byte TAG_RawOffsetIndices = 66;
/*      */   public static final byte TAG_ZoneAliases = 67;
/*      */   public static final byte TAG_TZDataVersion = 68;
/*      */   public static final byte TAG_ExcludedZones = 69;
/*  474 */   private static Map<String, ZoneInfo> zoneInfoObjects = null;
/*  475 */   private static final ZoneInfo GMT = new ZoneInfo("GMT", 0);
/*      */ 
/*  477 */   private static final String ziDir = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */   {
/*      */     public String run() {
/*  480 */       String str = System.getProperty("java.home") + File.separator + "lib" + File.separator + "zi";
/*      */       try
/*      */       {
/*  483 */         str = FileSystems.getDefault().getPath(str, new String[0]).toRealPath(new LinkOption[0]).toString();
/*      */       } catch (Exception localException) {
/*      */       }
/*  486 */       return str;
/*      */     }
/*      */   });
/*      */ 
/*  741 */   private static volatile SoftReference<List<String>> zoneIDs = null;
/*      */ 
/*  838 */   private static volatile SoftReference<List<String>> excludedIDs = null;
/*  839 */   private static volatile boolean hasNoExcludeList = false;
/*      */ 
/*  904 */   private static volatile SoftReference<byte[]> rawOffsetIndices = null;
/*      */ 
/*  950 */   private static volatile SoftReference<int[]> rawOffsets = null;
/*      */ 
/* 1001 */   private static volatile SoftReference<byte[]> zoneInfoMappings = null;
/*      */ 
/*      */   public static String getFileName(String paramString)
/*      */   {
/*  498 */     if (File.separatorChar == '/') {
/*  499 */       return paramString;
/*      */     }
/*  501 */     return paramString.replace('/', File.separatorChar);
/*      */   }
/*      */ 
/*      */   public static ZoneInfo getCustomTimeZone(String paramString, int paramInt)
/*      */   {
/*  513 */     String str = toCustomID(paramInt);
/*      */ 
/*  515 */     ZoneInfo localZoneInfo = getFromCache(str);
/*  516 */     if (localZoneInfo == null) {
/*  517 */       localZoneInfo = new ZoneInfo(str, paramInt);
/*  518 */       localZoneInfo = addToCache(str, localZoneInfo);
/*  519 */       if (!str.equals(paramString)) {
/*  520 */         localZoneInfo = addToCache(paramString, localZoneInfo);
/*      */       }
/*      */     }
/*  523 */     return (ZoneInfo)localZoneInfo.clone();
/*      */   }
/*      */ 
/*      */   public static String toCustomID(int paramInt)
/*      */   {
/*  528 */     int j = paramInt / 60000;
/*      */     int i;
/*  530 */     if (j >= 0) {
/*  531 */       i = 43;
/*      */     } else {
/*  533 */       i = 45;
/*  534 */       j = -j;
/*      */     }
/*  536 */     int k = j / 60;
/*  537 */     int m = j % 60;
/*      */ 
/*  539 */     char[] arrayOfChar = { 'G', 'M', 'T', i, '0', '0', ':', '0', '0' };
/*  540 */     if (k >= 10)
/*      */     {
/*      */       int tmp94_93 = 4;
/*      */       char[] tmp94_91 = arrayOfChar; tmp94_91[tmp94_93] = ((char)(tmp94_91[tmp94_93] + k / 10));
/*      */     }
/*      */     int tmp106_105 = 5;
/*      */     char[] tmp106_103 = arrayOfChar; tmp106_103[tmp106_105] = ((char)(tmp106_103[tmp106_105] + k % 10));
/*  544 */     if (m != 0)
/*      */     {
/*      */       char[] tmp124_120 = arrayOfChar; tmp124_120[7] = ((char)(tmp124_120[7] + m / 10));
/*      */       char[] tmp138_134 = arrayOfChar; tmp138_134[8] = ((char)(tmp138_134[8] + m % 10));
/*      */     }
/*  548 */     return new String(arrayOfChar);
/*      */   }
/*      */ 
/*      */   public static ZoneInfo getZoneInfo(String paramString)
/*      */   {
/*  558 */     if ("GMT".equals(paramString))
/*  559 */       return (ZoneInfo)GMT.clone();
/*  560 */     ZoneInfo localZoneInfo = getFromCache(paramString);
/*  561 */     if (localZoneInfo == null) {
/*  562 */       Map localMap = ZoneInfo.getCachedAliasTable();
/*  563 */       if ((localMap != null) && (localMap.get(paramString) != null)) {
/*  564 */         return null;
/*      */       }
/*  566 */       localZoneInfo = createZoneInfo(paramString);
/*  567 */       if (localZoneInfo == null) {
/*  568 */         return null;
/*      */       }
/*  570 */       localZoneInfo = addToCache(paramString, localZoneInfo);
/*      */     }
/*  572 */     return (ZoneInfo)localZoneInfo.clone();
/*      */   }
/*      */ 
/*      */   static synchronized ZoneInfo getFromCache(String paramString) {
/*  576 */     if (zoneInfoObjects == null) {
/*  577 */       return null;
/*      */     }
/*  579 */     return (ZoneInfo)zoneInfoObjects.get(paramString);
/*      */   }
/*      */ 
/*      */   static synchronized ZoneInfo addToCache(String paramString, ZoneInfo paramZoneInfo) {
/*  583 */     if (zoneInfoObjects == null) {
/*  584 */       zoneInfoObjects = new HashMap();
/*      */     } else {
/*  586 */       ZoneInfo localZoneInfo = (ZoneInfo)zoneInfoObjects.get(paramString);
/*  587 */       if (localZoneInfo != null) {
/*  588 */         return localZoneInfo;
/*      */       }
/*      */     }
/*  591 */     zoneInfoObjects.put(paramString, paramZoneInfo);
/*  592 */     return paramZoneInfo;
/*      */   }
/*      */ 
/*      */   private static ZoneInfo createZoneInfo(String paramString) {
/*  596 */     byte[] arrayOfByte = readZoneInfoFile(getFileName(paramString));
/*  597 */     if (arrayOfByte == null) {
/*  598 */       return null;
/*      */     }
/*      */ 
/*  601 */     int i = 0;
/*  602 */     int j = arrayOfByte.length;
/*  603 */     int k = 0;
/*  604 */     int m = 0;
/*  605 */     int n = 0;
/*  606 */     boolean bool = false;
/*  607 */     long[] arrayOfLong = null;
/*  608 */     int[] arrayOfInt1 = null;
/*  609 */     int[] arrayOfInt2 = null;
/*      */     try
/*      */     {
/*  612 */       for (i = 0; i < JAVAZI_LABEL.length; i++) {
/*  613 */         if (arrayOfByte[i] != JAVAZI_LABEL[i]) {
/*  614 */           System.err.println("ZoneInfo: wrong magic number: " + paramString);
/*  615 */           return null;
/*      */         }
/*      */       }
/*  618 */       if (arrayOfByte[(i++)] > 1) {
/*  619 */         System.err.println("ZoneInfo: incompatible version (" + arrayOfByte[(i - 1)] + "): " + paramString);
/*      */ 
/*  621 */         return null;
/*      */       }
/*      */ 
/*  624 */       while (i < j) {
/*  625 */         int i1 = arrayOfByte[(i++)];
/*  626 */         int i2 = ((arrayOfByte[(i++)] & 0xFF) << 8) + (arrayOfByte[(i++)] & 0xFF);
/*      */ 
/*  628 */         if (j < i + i2)
/*      */           break;
/*      */         int i3;
/*      */         int i4;
/*      */         int i5;
/*  632 */         switch (i1)
/*      */         {
/*      */         case 3:
/*  635 */           i3 = arrayOfByte[(i++)] & 0xFF;
/*  636 */           i3 = (i3 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  637 */           i3 = (i3 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  638 */           i3 = (i3 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  639 */           n = i3;
/*      */ 
/*  641 */           break;
/*      */         case 2:
/*  645 */           i3 = (short)(arrayOfByte[(i++)] & 0xFF);
/*  646 */           i3 = (short)((i3 << 8) + (arrayOfByte[(i++)] & 0xFF));
/*  647 */           m = i3 * 1000;
/*      */ 
/*  649 */           break;
/*      */         case 1:
/*  653 */           i3 = arrayOfByte[(i++)] & 0xFF;
/*  654 */           i3 = (i3 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  655 */           i3 = (i3 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  656 */           i3 = (i3 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  657 */           k = i3;
/*      */ 
/*  659 */           break;
/*      */         case 4:
/*  663 */           i3 = i2 / 8;
/*  664 */           arrayOfLong = new long[i3];
/*  665 */           for (i4 = 0; i4 < i3; i4++) {
/*  666 */             long l = arrayOfByte[(i++)] & 0xFF;
/*  667 */             l = (l << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  668 */             l = (l << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  669 */             l = (l << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  670 */             l = (l << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  671 */             l = (l << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  672 */             l = (l << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  673 */             l = (l << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  674 */             arrayOfLong[i4] = l;
/*      */           }
/*      */ 
/*  677 */           break;
/*      */         case 5:
/*  681 */           i3 = i2 / 4;
/*  682 */           arrayOfInt1 = new int[i3];
/*  683 */           for (i4 = 0; i4 < i3; i4++) {
/*  684 */             i5 = arrayOfByte[(i++)] & 0xFF;
/*  685 */             i5 = (i5 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  686 */             i5 = (i5 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  687 */             i5 = (i5 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  688 */             arrayOfInt1[i4] = i5;
/*      */           }
/*      */ 
/*  691 */           break;
/*      */         case 6:
/*  695 */           if ((i2 != 32) && (i2 != 40)) {
/*  696 */             System.err.println("ZoneInfo: wrong SimpleTimeZone parameter size");
/*  697 */             return null;
/*      */           }
/*  699 */           i3 = i2 / 4;
/*  700 */           arrayOfInt2 = new int[i3];
/*  701 */           for (i4 = 0; i4 < i3; i4++) {
/*  702 */             i5 = arrayOfByte[(i++)] & 0xFF;
/*  703 */             i5 = (i5 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  704 */             i5 = (i5 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  705 */             i5 = (i5 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  706 */             arrayOfInt2[i4] = i5;
/*      */           }
/*      */ 
/*  709 */           break;
/*      */         case 7:
/*  713 */           if (i2 != 1) {
/*  714 */             System.err.println("ZoneInfo: wrong byte length for TAG_GMTOffsetWillChange");
/*      */           }
/*  716 */           bool = arrayOfByte[(i++)] == 1;
/*      */ 
/*  718 */           break;
/*      */         default:
/*  721 */           System.err.println("ZoneInfo: unknown tag < " + i1 + ">. ignored.");
/*  722 */           i += i2;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/*  727 */       System.err.println("ZoneInfo: corrupted zoneinfo file: " + paramString);
/*  728 */       return null;
/*      */     }
/*      */ 
/*  731 */     if (i != j) {
/*  732 */       System.err.println("ZoneInfo: wrong file size: " + paramString);
/*  733 */       return null;
/*      */     }
/*      */ 
/*  736 */     return new ZoneInfo(paramString, k, m, n, arrayOfLong, arrayOfInt1, arrayOfInt2, bool);
/*      */   }
/*      */ 
/*      */   static List<String> getZoneIDs()
/*      */   {
/*  744 */     Object localObject = null;
/*      */ 
/*  746 */     SoftReference localSoftReference = zoneIDs;
/*  747 */     if (localSoftReference != null) {
/*  748 */       localObject = (List)localSoftReference.get();
/*  749 */       if (localObject != null) {
/*  750 */         return localObject;
/*      */       }
/*      */     }
/*      */ 
/*  754 */     byte[] arrayOfByte = null;
/*  755 */     arrayOfByte = getZoneInfoMappings();
/*  756 */     int i = JAVAZM_LABEL_LENGTH + 1;
/*  757 */     int j = arrayOfByte.length;
/*      */     try
/*      */     {
/*  761 */       while (i < j) {
/*  762 */         int k = arrayOfByte[(i++)];
/*  763 */         int m = ((arrayOfByte[(i++)] & 0xFF) << 8) + (arrayOfByte[(i++)] & 0xFF);
/*      */ 
/*  765 */         switch (k)
/*      */         {
/*      */         case 64:
/*  768 */           int n = (arrayOfByte[(i++)] << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  769 */           localObject = new ArrayList(n);
/*      */ 
/*  771 */           for (int i1 = 0; i1 < n; i1++) {
/*  772 */             int i2 = arrayOfByte[(i++)];
/*  773 */             ((List)localObject).add(new String(arrayOfByte, i, i2, "UTF-8"));
/*  774 */             i += i2;
/*      */           }
/*      */ 
/*  777 */           break;
/*      */         default:
/*  780 */           i += m;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/*  785 */       System.err.println("ZoneInfo: corrupted ZoneInfoMappings");
/*      */     }
/*      */ 
/*  788 */     zoneIDs = new SoftReference(localObject);
/*  789 */     return localObject;
/*      */   }
/*      */ 
/*      */   static Map<String, String> getZoneAliases()
/*      */   {
/*  798 */     byte[] arrayOfByte = getZoneInfoMappings();
/*  799 */     int i = JAVAZM_LABEL_LENGTH + 1;
/*  800 */     int j = arrayOfByte.length;
/*  801 */     HashMap localHashMap = null;
/*      */     try
/*      */     {
/*  805 */       while (i < j) {
/*  806 */         int k = arrayOfByte[(i++)];
/*  807 */         int m = ((arrayOfByte[(i++)] & 0xFF) << 8) + (arrayOfByte[(i++)] & 0xFF);
/*      */ 
/*  809 */         switch (k)
/*      */         {
/*      */         case 67:
/*  812 */           int n = (arrayOfByte[(i++)] << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  813 */           localHashMap = new HashMap(n);
/*  814 */           for (int i1 = 0; i1 < n; i1++) {
/*  815 */             int i2 = arrayOfByte[(i++)];
/*  816 */             String str1 = new String(arrayOfByte, i, i2, "UTF-8");
/*  817 */             i += i2;
/*  818 */             i2 = arrayOfByte[(i++)];
/*  819 */             String str2 = new String(arrayOfByte, i, i2, "UTF-8");
/*  820 */             i += i2;
/*  821 */             localHashMap.put(str1, str2);
/*      */           }
/*      */ 
/*  824 */           break;
/*      */         default:
/*  827 */           i += m;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/*  832 */       System.err.println("ZoneInfo: corrupted ZoneInfoMappings");
/*  833 */       return null;
/*      */     }
/*  835 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   static List<String> getExcludedZones()
/*      */   {
/*  848 */     if (hasNoExcludeList) {
/*  849 */       return null;
/*      */     }
/*      */ 
/*  852 */     Object localObject = null;
/*      */ 
/*  854 */     SoftReference localSoftReference = excludedIDs;
/*  855 */     if (localSoftReference != null) {
/*  856 */       localObject = (List)localSoftReference.get();
/*  857 */       if (localObject != null) {
/*  858 */         return localObject;
/*      */       }
/*      */     }
/*      */ 
/*  862 */     byte[] arrayOfByte = getZoneInfoMappings();
/*  863 */     int i = JAVAZM_LABEL_LENGTH + 1;
/*  864 */     int j = arrayOfByte.length;
/*      */     try
/*      */     {
/*  868 */       while (i < j) {
/*  869 */         int k = arrayOfByte[(i++)];
/*  870 */         int m = ((arrayOfByte[(i++)] & 0xFF) << 8) + (arrayOfByte[(i++)] & 0xFF);
/*      */ 
/*  872 */         switch (k)
/*      */         {
/*      */         case 69:
/*  875 */           int n = (arrayOfByte[(i++)] << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  876 */           localObject = new ArrayList();
/*  877 */           for (int i1 = 0; i1 < n; i1++) {
/*  878 */             int i2 = arrayOfByte[(i++)];
/*  879 */             String str = new String(arrayOfByte, i, i2, "UTF-8");
/*  880 */             i += i2;
/*  881 */             ((List)localObject).add(str);
/*      */           }
/*      */ 
/*  884 */           break;
/*      */         default:
/*  887 */           i += m;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/*  892 */       System.err.println("ZoneInfo: corrupted ZoneInfoMappings");
/*  893 */       return null;
/*      */     }
/*      */ 
/*  896 */     if (localObject != null)
/*  897 */       excludedIDs = new SoftReference(localObject);
/*      */     else {
/*  899 */       hasNoExcludeList = true;
/*      */     }
/*  901 */     return localObject;
/*      */   }
/*      */ 
/*      */   static byte[] getRawOffsetIndices()
/*      */   {
/*  907 */     byte[] arrayOfByte1 = null;
/*      */ 
/*  909 */     SoftReference localSoftReference = rawOffsetIndices;
/*  910 */     if (localSoftReference != null) {
/*  911 */       arrayOfByte1 = (byte[])localSoftReference.get();
/*  912 */       if (arrayOfByte1 != null) {
/*  913 */         return arrayOfByte1;
/*      */       }
/*      */     }
/*      */ 
/*  917 */     byte[] arrayOfByte2 = getZoneInfoMappings();
/*  918 */     int i = JAVAZM_LABEL_LENGTH + 1;
/*  919 */     int j = arrayOfByte2.length;
/*      */     try
/*      */     {
/*  923 */       while (i < j) {
/*  924 */         int k = arrayOfByte2[(i++)];
/*  925 */         int m = ((arrayOfByte2[(i++)] & 0xFF) << 8) + (arrayOfByte2[(i++)] & 0xFF);
/*      */ 
/*  927 */         switch (k)
/*      */         {
/*      */         case 66:
/*  930 */           arrayOfByte1 = new byte[m];
/*  931 */           for (int n = 0; n < m; n++) {
/*  932 */             arrayOfByte1[n] = arrayOfByte2[(i++)];
/*      */           }
/*      */ 
/*  935 */           break;
/*      */         default:
/*  938 */           i += m;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  943 */       System.err.println("ZoneInfo: corrupted ZoneInfoMappings");
/*      */     }
/*      */ 
/*  946 */     rawOffsetIndices = new SoftReference(arrayOfByte1);
/*  947 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   static int[] getRawOffsets()
/*      */   {
/*  953 */     int[] arrayOfInt = null;
/*      */ 
/*  955 */     SoftReference localSoftReference = rawOffsets;
/*  956 */     if (localSoftReference != null) {
/*  957 */       arrayOfInt = (int[])localSoftReference.get();
/*  958 */       if (arrayOfInt != null) {
/*  959 */         return arrayOfInt;
/*      */       }
/*      */     }
/*      */ 
/*  963 */     byte[] arrayOfByte = getZoneInfoMappings();
/*  964 */     int i = JAVAZM_LABEL_LENGTH + 1;
/*  965 */     int j = arrayOfByte.length;
/*      */     try
/*      */     {
/*  969 */       while (i < j) {
/*  970 */         int k = arrayOfByte[(i++)];
/*  971 */         int m = ((arrayOfByte[(i++)] & 0xFF) << 8) + (arrayOfByte[(i++)] & 0xFF);
/*      */ 
/*  973 */         switch (k)
/*      */         {
/*      */         case 65:
/*  976 */           int n = m / 4;
/*  977 */           arrayOfInt = new int[n];
/*  978 */           for (int i1 = 0; i1 < n; i1++) {
/*  979 */             int i2 = arrayOfByte[(i++)] & 0xFF;
/*  980 */             i2 = (i2 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  981 */             i2 = (i2 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  982 */             i2 = (i2 << 8) + (arrayOfByte[(i++)] & 0xFF);
/*  983 */             arrayOfInt[i1] = i2;
/*      */           }
/*      */ 
/*  986 */           break;
/*      */         default:
/*  989 */           i += m;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  994 */       System.err.println("ZoneInfo: corrupted ZoneInfoMappings");
/*      */     }
/*      */ 
/*  997 */     rawOffsets = new SoftReference(arrayOfInt);
/*  998 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static byte[] getZoneInfoMappings()
/*      */   {
/* 1006 */     SoftReference localSoftReference = zoneInfoMappings;
/* 1007 */     if (localSoftReference != null) {
/* 1008 */       arrayOfByte = (byte[])localSoftReference.get();
/* 1009 */       if (arrayOfByte != null) {
/* 1010 */         return arrayOfByte;
/*      */       }
/*      */     }
/*      */ 
/* 1014 */     byte[] arrayOfByte = readZoneInfoFile("ZoneInfoMappings");
/*      */ 
/* 1016 */     if (arrayOfByte == null) {
/* 1017 */       return null;
/*      */     }
/*      */ 
/* 1021 */     for (int i = 0; i < JAVAZM_LABEL.length; i++) {
/* 1022 */       if (arrayOfByte[i] != JAVAZM_LABEL[i]) {
/* 1023 */         System.err.println("ZoneInfo: wrong magic number: ZoneInfoMappings");
/* 1024 */         return null;
/*      */       }
/*      */     }
/* 1027 */     if (arrayOfByte[(i++)] > 1) {
/* 1028 */       System.err.println("ZoneInfo: incompatible version (" + arrayOfByte[(i - 1)] + "): " + "ZoneInfoMappings");
/*      */ 
/* 1030 */       return null;
/*      */     }
/*      */ 
/* 1033 */     zoneInfoMappings = new SoftReference(arrayOfByte);
/* 1034 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private static byte[] readZoneInfoFile(String paramString)
/*      */   {
/* 1042 */     if (paramString.indexOf("..") >= 0) {
/* 1043 */       return null;
/*      */     }
/* 1045 */     byte[] arrayOfByte = null;
/*      */     try
/*      */     {
/* 1048 */       arrayOfByte = (byte[])AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*      */         public Object run() throws IOException {
/* 1050 */           File localFile = new File(ZoneInfoFile.ziDir, this.val$fileName);
/* 1051 */           byte[] arrayOfByte = null;
/* 1052 */           int i = (int)localFile.length();
/* 1053 */           if (i > 0) {
/* 1054 */             FileInputStream localFileInputStream = new FileInputStream(localFile);
/* 1055 */             arrayOfByte = new byte[i];
/*      */             try {
/* 1057 */               if (localFileInputStream.read(arrayOfByte) != i)
/* 1058 */                 throw new IOException("read error on " + this.val$fileName);
/*      */             }
/*      */             finally {
/* 1061 */               localFileInputStream.close();
/*      */             }
/*      */           }
/* 1064 */           return arrayOfByte;
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 1068 */       Exception localException = localPrivilegedActionException.getException();
/* 1069 */       if ((!(localException instanceof FileNotFoundException)) || ("ZoneInfoMappings".equals(paramString))) {
/* 1070 */         System.err.println("ZoneInfo: " + localException.getMessage());
/*      */       }
/*      */     }
/* 1073 */     return arrayOfByte;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.calendar.ZoneInfoFile
 * JD-Core Version:    0.6.2
 */