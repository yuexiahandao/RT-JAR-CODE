/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class XMLChar
/*     */ {
/*  55 */   private static final byte[] CHARS = new byte[65536];
/*     */   public static final int MASK_VALID = 1;
/*     */   public static final int MASK_SPACE = 2;
/*     */   public static final int MASK_NAME_START = 4;
/*     */   public static final int MASK_NAME = 8;
/*     */   public static final int MASK_PUBID = 16;
/*     */   public static final int MASK_CONTENT = 32;
/*     */   public static final int MASK_NCNAME_START = 64;
/*     */   public static final int MASK_NCNAME = 128;
/*     */ 
/*     */   public static boolean isSupplemental(int c)
/*     */   {
/* 351 */     return (c >= 65536) && (c <= 1114111);
/*     */   }
/*     */ 
/*     */   public static int supplemental(char h, char l)
/*     */   {
/* 362 */     return (h - 55296) * 1024 + (l - 56320) + 65536;
/*     */   }
/*     */ 
/*     */   public static char highSurrogate(int c)
/*     */   {
/* 371 */     return (char)((c - 65536 >> 10) + 55296);
/*     */   }
/*     */ 
/*     */   public static char lowSurrogate(int c)
/*     */   {
/* 380 */     return (char)((c - 65536 & 0x3FF) + 56320);
/*     */   }
/*     */ 
/*     */   public static boolean isHighSurrogate(int c)
/*     */   {
/* 389 */     return (55296 <= c) && (c <= 56319);
/*     */   }
/*     */ 
/*     */   public static boolean isLowSurrogate(int c)
/*     */   {
/* 398 */     return (56320 <= c) && (c <= 57343);
/*     */   }
/*     */ 
/*     */   public static boolean isValid(int c)
/*     */   {
/* 413 */     return ((c < 65536) && ((CHARS[c] & 0x1) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isInvalid(int c)
/*     */   {
/* 423 */     return !isValid(c);
/*     */   }
/*     */ 
/*     */   public static boolean isContent(int c)
/*     */   {
/* 432 */     return ((c < 65536) && ((CHARS[c] & 0x20) != 0)) || ((65536 <= c) && (c <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isMarkup(int c)
/*     */   {
/* 443 */     return (c == 60) || (c == 38) || (c == 37);
/*     */   }
/*     */ 
/*     */   public static boolean isSpace(int c)
/*     */   {
/* 453 */     return (c < 65536) && ((CHARS[c] & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isNameStart(int c)
/*     */   {
/* 464 */     return (c < 65536) && ((CHARS[c] & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isName(int c)
/*     */   {
/* 475 */     return (c < 65536) && ((CHARS[c] & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isNCNameStart(int c)
/*     */   {
/* 486 */     return (c < 65536) && ((CHARS[c] & 0x40) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isNCName(int c)
/*     */   {
/* 497 */     return (c < 65536) && ((CHARS[c] & 0x80) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isPubid(int c)
/*     */   {
/* 508 */     return (c < 65536) && ((CHARS[c] & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   public static boolean isValidName(String name)
/*     */   {
/* 522 */     if (name.length() == 0)
/* 523 */       return false;
/* 524 */     char ch = name.charAt(0);
/* 525 */     if (!isNameStart(ch))
/* 526 */       return false;
/* 527 */     for (int i = 1; i < name.length(); i++) {
/* 528 */       ch = name.charAt(i);
/* 529 */       if (!isName(ch)) {
/* 530 */         return false;
/*     */       }
/*     */     }
/* 533 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isValidNCName(String ncName)
/*     */   {
/* 549 */     if (ncName.length() == 0)
/* 550 */       return false;
/* 551 */     char ch = ncName.charAt(0);
/* 552 */     if (!isNCNameStart(ch))
/* 553 */       return false;
/* 554 */     for (int i = 1; i < ncName.length(); i++) {
/* 555 */       ch = ncName.charAt(i);
/* 556 */       if (!isNCName(ch)) {
/* 557 */         return false;
/*     */       }
/*     */     }
/* 560 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isValidNmtoken(String nmtoken)
/*     */   {
/* 574 */     if (nmtoken.length() == 0)
/* 575 */       return false;
/* 576 */     for (int i = 0; i < nmtoken.length(); i++) {
/* 577 */       char ch = nmtoken.charAt(i);
/* 578 */       if (!isName(ch)) {
/* 579 */         return false;
/*     */       }
/*     */     }
/* 582 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isValidIANAEncoding(String ianaEncoding)
/*     */   {
/* 600 */     if (ianaEncoding != null) {
/* 601 */       int length = ianaEncoding.length();
/* 602 */       if (length > 0) {
/* 603 */         char c = ianaEncoding.charAt(0);
/* 604 */         if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))) {
/* 605 */           for (int i = 1; i < length; i++) {
/* 606 */             c = ianaEncoding.charAt(i);
/* 607 */             if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9')) && (c != '.') && (c != '_') && (c != '-'))
/*     */             {
/* 610 */               return false;
/*     */             }
/*     */           }
/* 613 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 617 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isValidJavaEncoding(String javaEncoding)
/*     */   {
/* 629 */     if (javaEncoding != null) {
/* 630 */       int length = javaEncoding.length();
/* 631 */       if (length > 0) {
/* 632 */         for (int i = 1; i < length; i++) {
/* 633 */           char c = javaEncoding.charAt(i);
/* 634 */           if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9')) && (c != '.') && (c != '_') && (c != '-'))
/*     */           {
/* 637 */             return false;
/*     */           }
/*     */         }
/* 640 */         return true;
/*     */       }
/*     */     }
/* 643 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isValidQName(String str)
/*     */   {
/* 653 */     int colon = str.indexOf(':');
/*     */ 
/* 655 */     if ((colon == 0) || (colon == str.length() - 1)) {
/* 656 */       return false;
/*     */     }
/*     */ 
/* 659 */     if (colon > 0) {
/* 660 */       String prefix = str.substring(0, colon);
/* 661 */       String localPart = str.substring(colon + 1);
/* 662 */       return (isValidNCName(prefix)) && (isValidNCName(localPart));
/*     */     }
/*     */ 
/* 665 */     return isValidNCName(str);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  99 */     int[] charRange = { 9, 10, 13, 13, 32, 55295, 57344, 65533 };
/*     */ 
/* 107 */     int[] spaceChar = { 32, 9, 13, 10 };
/*     */ 
/* 116 */     int[] nameChar = { 45, 46 };
/*     */ 
/* 124 */     int[] nameStartChar = { 58, 95 };
/*     */ 
/* 132 */     int[] pubidChar = { 10, 13, 32, 33, 35, 36, 37, 61, 95 };
/*     */ 
/* 137 */     int[] pubidRange = { 39, 59, 63, 90, 97, 122 };
/*     */ 
/* 145 */     int[] letterRange = { 65, 90, 97, 122, 192, 214, 216, 246, 248, 305, 308, 318, 321, 328, 330, 382, 384, 451, 461, 496, 500, 501, 506, 535, 592, 680, 699, 705, 904, 906, 910, 929, 931, 974, 976, 982, 994, 1011, 1025, 1036, 1038, 1103, 1105, 1116, 1118, 1153, 1168, 1220, 1223, 1224, 1227, 1228, 1232, 1259, 1262, 1269, 1272, 1273, 1329, 1366, 1377, 1414, 1488, 1514, 1520, 1522, 1569, 1594, 1601, 1610, 1649, 1719, 1722, 1726, 1728, 1742, 1744, 1747, 1765, 1766, 2309, 2361, 2392, 2401, 2437, 2444, 2447, 2448, 2451, 2472, 2474, 2480, 2486, 2489, 2524, 2525, 2527, 2529, 2544, 2545, 2565, 2570, 2575, 2576, 2579, 2600, 2602, 2608, 2610, 2611, 2613, 2614, 2616, 2617, 2649, 2652, 2674, 2676, 2693, 2699, 2703, 2705, 2707, 2728, 2730, 2736, 2738, 2739, 2741, 2745, 2821, 2828, 2831, 2832, 2835, 2856, 2858, 2864, 2866, 2867, 2870, 2873, 2908, 2909, 2911, 2913, 2949, 2954, 2958, 2960, 2962, 2965, 2969, 2970, 2974, 2975, 2979, 2980, 2984, 2986, 2990, 2997, 2999, 3001, 3077, 3084, 3086, 3088, 3090, 3112, 3114, 3123, 3125, 3129, 3168, 3169, 3205, 3212, 3214, 3216, 3218, 3240, 3242, 3251, 3253, 3257, 3296, 3297, 3333, 3340, 3342, 3344, 3346, 3368, 3370, 3385, 3424, 3425, 3585, 3630, 3634, 3635, 3648, 3653, 3713, 3714, 3719, 3720, 3732, 3735, 3737, 3743, 3745, 3747, 3754, 3755, 3757, 3758, 3762, 3763, 3776, 3780, 3904, 3911, 3913, 3945, 4256, 4293, 4304, 4342, 4354, 4355, 4357, 4359, 4363, 4364, 4366, 4370, 4436, 4437, 4447, 4449, 4461, 4462, 4466, 4467, 4526, 4527, 4535, 4536, 4540, 4546, 7680, 7835, 7840, 7929, 7936, 7957, 7960, 7965, 7968, 8005, 8008, 8013, 8016, 8023, 8031, 8061, 8064, 8116, 8118, 8124, 8130, 8132, 8134, 8140, 8144, 8147, 8150, 8155, 8160, 8172, 8178, 8180, 8182, 8188, 8490, 8491, 8576, 8578, 12353, 12436, 12449, 12538, 12549, 12588, 44032, 55203, 12321, 12329, 19968, 40869 };
/*     */ 
/* 188 */     int[] letterChar = { 902, 908, 986, 988, 990, 992, 1369, 1749, 2365, 2482, 2654, 2701, 2749, 2784, 2877, 2972, 3294, 3632, 3716, 3722, 3725, 3749, 3751, 3760, 3773, 4352, 4361, 4412, 4414, 4416, 4428, 4430, 4432, 4441, 4451, 4453, 4455, 4457, 4469, 4510, 4520, 4523, 4538, 4587, 4592, 4601, 8025, 8027, 8029, 8126, 8486, 8494, 12295 };
/*     */ 
/* 205 */     int[] combiningCharRange = { 768, 837, 864, 865, 1155, 1158, 1425, 1441, 1443, 1465, 1467, 1469, 1473, 1474, 1611, 1618, 1750, 1756, 1757, 1759, 1760, 1764, 1767, 1768, 1770, 1773, 2305, 2307, 2366, 2380, 2385, 2388, 2402, 2403, 2433, 2435, 2496, 2500, 2503, 2504, 2507, 2509, 2530, 2531, 2624, 2626, 2631, 2632, 2635, 2637, 2672, 2673, 2689, 2691, 2750, 2757, 2759, 2761, 2763, 2765, 2817, 2819, 2878, 2883, 2887, 2888, 2891, 2893, 2902, 2903, 2946, 2947, 3006, 3010, 3014, 3016, 3018, 3021, 3073, 3075, 3134, 3140, 3142, 3144, 3146, 3149, 3157, 3158, 3202, 3203, 3262, 3268, 3270, 3272, 3274, 3277, 3285, 3286, 3330, 3331, 3390, 3395, 3398, 3400, 3402, 3405, 3636, 3642, 3655, 3662, 3764, 3769, 3771, 3772, 3784, 3789, 3864, 3865, 3953, 3972, 3974, 3979, 3984, 3989, 3993, 4013, 4017, 4023, 8400, 8412, 12330, 12335 };
/*     */ 
/* 225 */     int[] combiningCharChar = { 1471, 1476, 1648, 2364, 2381, 2492, 2494, 2495, 2519, 2562, 2620, 2622, 2623, 2748, 2876, 3031, 3415, 3633, 3761, 3893, 3895, 3897, 3902, 3903, 3991, 4025, 8417, 12441, 12442 };
/*     */ 
/* 236 */     int[] digitRange = { 48, 57, 1632, 1641, 1776, 1785, 2406, 2415, 2534, 2543, 2662, 2671, 2790, 2799, 2918, 2927, 3047, 3055, 3174, 3183, 3302, 3311, 3430, 3439, 3664, 3673, 3792, 3801, 3872, 3881 };
/*     */ 
/* 247 */     int[] extenderRange = { 12337, 12341, 12445, 12446, 12540, 12542 };
/*     */ 
/* 251 */     int[] extenderChar = { 183, 720, 721, 903, 1600, 3654, 3782, 12293 };
/*     */ 
/* 259 */     int[] specialChar = { 60, 38, 10, 13, 93 };
/*     */ 
/* 268 */     for (int i = 0; i < charRange.length; i += 2) {
/* 269 */       for (int j = charRange[i]; j <= charRange[(i + 1)]; j++)
/*     */       {
/*     */         int tmp4349_4347 = j;
/*     */         byte[] tmp4349_4344 = CHARS; tmp4349_4344[tmp4349_4347] = ((byte)(tmp4349_4344[tmp4349_4347] | 0x21));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 275 */     for (int i = 0; i < specialChar.length; i++) {
/* 276 */       CHARS[specialChar[i]] = ((byte)(CHARS[specialChar[i]] & 0xFFFFFFDF));
/*     */     }
/*     */ 
/* 280 */     for (int i = 0; i < spaceChar.length; i++)
/*     */     {
/*     */       int tmp4424_4423 = spaceChar[i];
/*     */       byte[] tmp4424_4417 = CHARS; tmp4424_4417[tmp4424_4423] = ((byte)(tmp4424_4417[tmp4424_4423] | 0x2));
/*     */     }
/*     */ 
/* 285 */     for (int i = 0; i < nameStartChar.length; i++)
/*     */     {
/*     */       int tmp4453_4452 = nameStartChar[i];
/*     */       byte[] tmp4453_4446 = CHARS; tmp4453_4446[tmp4453_4452] = ((byte)(tmp4453_4446[tmp4453_4452] | 0xCC));
/*     */     }
/*     */ 
/* 289 */     for (int i = 0; i < letterRange.length; i += 2) {
/* 290 */       for (int j = letterRange[i]; j <= letterRange[(i + 1)]; j++)
/*     */       {
/*     */         int tmp4502_4500 = j;
/*     */         byte[] tmp4502_4497 = CHARS; tmp4502_4497[tmp4502_4500] = ((byte)(tmp4502_4497[tmp4502_4500] | 0xCC));
/*     */       }
/*     */     }
/*     */ 
/* 295 */     for (int i = 0; i < letterChar.length; i++)
/*     */     {
/*     */       int tmp4541_4540 = letterChar[i];
/*     */       byte[] tmp4541_4533 = CHARS; tmp4541_4533[tmp4541_4540] = ((byte)(tmp4541_4533[tmp4541_4540] | 0xCC));
/*     */     }
/*     */ 
/* 301 */     for (int i = 0; i < nameChar.length; i++)
/*     */     {
/*     */       int tmp4572_4571 = nameChar[i];
/*     */       byte[] tmp4572_4565 = CHARS; tmp4572_4565[tmp4572_4571] = ((byte)(tmp4572_4565[tmp4572_4571] | 0x88));
/*     */     }
/* 304 */     for (int i = 0; i < digitRange.length; i += 2) {
/* 305 */       for (int j = digitRange[i]; j <= digitRange[(i + 1)]; j++)
/*     */       {
/*     */         int tmp4621_4619 = j;
/*     */         byte[] tmp4621_4616 = CHARS; tmp4621_4616[tmp4621_4619] = ((byte)(tmp4621_4616[tmp4621_4619] | 0x88));
/*     */       }
/*     */     }
/* 309 */     for (int i = 0; i < combiningCharRange.length; i += 2) {
/* 310 */       for (int j = combiningCharRange[i]; j <= combiningCharRange[(i + 1)]; j++)
/*     */       {
/*     */         int tmp4676_4674 = j;
/*     */         byte[] tmp4676_4671 = CHARS; tmp4676_4671[tmp4676_4674] = ((byte)(tmp4676_4671[tmp4676_4674] | 0x88));
/*     */       }
/*     */     }
/* 314 */     for (int i = 0; i < combiningCharChar.length; i++)
/*     */     {
/*     */       int tmp4715_4714 = combiningCharChar[i];
/*     */       byte[] tmp4715_4707 = CHARS; tmp4715_4707[tmp4715_4714] = ((byte)(tmp4715_4707[tmp4715_4714] | 0x88));
/*     */     }
/* 317 */     for (int i = 0; i < extenderRange.length; i += 2) {
/* 318 */       for (int j = extenderRange[i]; j <= extenderRange[(i + 1)]; j++)
/*     */       {
/*     */         int tmp4764_4762 = j;
/*     */         byte[] tmp4764_4759 = CHARS; tmp4764_4759[tmp4764_4762] = ((byte)(tmp4764_4759[tmp4764_4762] | 0x88));
/*     */       }
/*     */     }
/* 322 */     for (int i = 0; i < extenderChar.length; i++)
/*     */     {
/*     */       int tmp4803_4802 = extenderChar[i];
/*     */       byte[] tmp4803_4795 = CHARS; tmp4803_4795[tmp4803_4802] = ((byte)(tmp4803_4795[tmp4803_4802] | 0x88));
/*     */     }
/*     */     byte[] tmp4822_4817 = CHARS; tmp4822_4817[58] = ((byte)(tmp4822_4817[58] & 0xFFFFFF3F));
/*     */ 
/* 330 */     for (int i = 0; i < pubidChar.length; i++)
/*     */     {
/*     */       int tmp4849_4848 = pubidChar[i];
/*     */       byte[] tmp4849_4841 = CHARS; tmp4849_4841[tmp4849_4848] = ((byte)(tmp4849_4841[tmp4849_4848] | 0x10));
/*     */     }
/* 333 */     for (int i = 0; i < pubidRange.length; i += 2)
/* 334 */       for (int j = pubidRange[i]; j <= pubidRange[(i + 1)]; j++)
/*     */       {
/*     */         int tmp4897_4895 = j;
/*     */         byte[] tmp4897_4892 = CHARS; tmp4897_4892[tmp4897_4895] = ((byte)(tmp4897_4892[tmp4897_4895] | 0x10));
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.XMLChar
 * JD-Core Version:    0.6.2
 */