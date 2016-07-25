/*      */ package com.sun.xml.internal.fastinfoset.org.apache.xerces.util;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ 
/*      */ public class XMLChar
/*      */ {
/*  124 */   private static final byte[] CHARS = new byte[65536];
/*      */   public static final int MASK_VALID = 1;
/*      */   public static final int MASK_SPACE = 2;
/*      */   public static final int MASK_NAME_START = 4;
/*      */   public static final int MASK_NAME = 8;
/*      */   public static final int MASK_PUBID = 16;
/*      */   public static final int MASK_CONTENT = 32;
/*      */   public static final int MASK_NCNAME_START = 64;
/*      */   public static final int MASK_NCNAME = 128;
/*      */ 
/*      */   public static boolean isSupplemental(int c)
/*      */   {
/*  798 */     return (c >= 65536) && (c <= 1114111);
/*      */   }
/*      */ 
/*      */   public static int supplemental(char h, char l)
/*      */   {
/*  809 */     return (h - 55296) * 1024 + (l - 56320) + 65536;
/*      */   }
/*      */ 
/*      */   public static char highSurrogate(int c)
/*      */   {
/*  818 */     return (char)((c - 65536 >> 10) + 55296);
/*      */   }
/*      */ 
/*      */   public static char lowSurrogate(int c)
/*      */   {
/*  827 */     return (char)((c - 65536 & 0x3FF) + 56320);
/*      */   }
/*      */ 
/*      */   public static boolean isHighSurrogate(int c)
/*      */   {
/*  836 */     return (55296 <= c) && (c <= 56319);
/*      */   }
/*      */ 
/*      */   public static boolean isLowSurrogate(int c)
/*      */   {
/*  845 */     return (56320 <= c) && (c <= 57343);
/*      */   }
/*      */ 
/*      */   public static boolean isValid(int c)
/*      */   {
/*  860 */     return ((c < 65536) && ((CHARS[c] & 0x1) != 0)) || ((65536 <= c) && (c <= 1114111));
/*      */   }
/*      */ 
/*      */   public static boolean isInvalid(int c)
/*      */   {
/*  870 */     return !isValid(c);
/*      */   }
/*      */ 
/*      */   public static boolean isContent(int c)
/*      */   {
/*  879 */     return ((c < 65536) && ((CHARS[c] & 0x20) != 0)) || ((65536 <= c) && (c <= 1114111));
/*      */   }
/*      */ 
/*      */   public static boolean isMarkup(int c)
/*      */   {
/*  890 */     return (c == 60) || (c == 38) || (c == 37);
/*      */   }
/*      */ 
/*      */   public static boolean isSpace(int c)
/*      */   {
/*  900 */     return (c <= 32) && ((CHARS[c] & 0x2) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean isNameStart(int c)
/*      */   {
/*  911 */     return (c < 65536) && ((CHARS[c] & 0x4) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean isName(int c)
/*      */   {
/*  922 */     return (c < 65536) && ((CHARS[c] & 0x8) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean isNCNameStart(int c)
/*      */   {
/*  933 */     return (c < 65536) && ((CHARS[c] & 0x40) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean isNCName(int c)
/*      */   {
/*  944 */     return (c < 65536) && ((CHARS[c] & 0x80) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean isPubid(int c)
/*      */   {
/*  955 */     return (c < 65536) && ((CHARS[c] & 0x10) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean isValidName(String name)
/*      */   {
/*  969 */     if (name.length() == 0)
/*  970 */       return false;
/*  971 */     char ch = name.charAt(0);
/*  972 */     if (!isNameStart(ch))
/*  973 */       return false;
/*  974 */     for (int i = 1; i < name.length(); i++) {
/*  975 */       ch = name.charAt(i);
/*  976 */       if (!isName(ch)) {
/*  977 */         return false;
/*      */       }
/*      */     }
/*  980 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isValidNCName(String ncName)
/*      */   {
/*  996 */     if (ncName.length() == 0)
/*  997 */       return false;
/*  998 */     char ch = ncName.charAt(0);
/*  999 */     if (!isNCNameStart(ch))
/* 1000 */       return false;
/* 1001 */     for (int i = 1; i < ncName.length(); i++) {
/* 1002 */       ch = ncName.charAt(i);
/* 1003 */       if (!isNCName(ch)) {
/* 1004 */         return false;
/*      */       }
/*      */     }
/* 1007 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isValidNmtoken(String nmtoken)
/*      */   {
/* 1021 */     if (nmtoken.length() == 0)
/* 1022 */       return false;
/* 1023 */     for (int i = 0; i < nmtoken.length(); i++) {
/* 1024 */       char ch = nmtoken.charAt(i);
/* 1025 */       if (!isName(ch)) {
/* 1026 */         return false;
/*      */       }
/*      */     }
/* 1029 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isValidIANAEncoding(String ianaEncoding)
/*      */   {
/* 1047 */     if (ianaEncoding != null) {
/* 1048 */       int length = ianaEncoding.length();
/* 1049 */       if (length > 0) {
/* 1050 */         char c = ianaEncoding.charAt(0);
/* 1051 */         if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))) {
/* 1052 */           for (int i = 1; i < length; i++) {
/* 1053 */             c = ianaEncoding.charAt(i);
/* 1054 */             if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9')) && (c != '.') && (c != '_') && (c != '-'))
/*      */             {
/* 1057 */               return false;
/*      */             }
/*      */           }
/* 1060 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1064 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isValidJavaEncoding(String javaEncoding)
/*      */   {
/* 1076 */     if (javaEncoding != null) {
/* 1077 */       int length = javaEncoding.length();
/* 1078 */       if (length > 0) {
/* 1079 */         for (int i = 1; i < length; i++) {
/* 1080 */           char c = javaEncoding.charAt(i);
/* 1081 */           if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9')) && (c != '.') && (c != '_') && (c != '-'))
/*      */           {
/* 1084 */             return false;
/*      */           }
/*      */         }
/* 1087 */         return true;
/*      */       }
/*      */     }
/* 1090 */     return false;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  166 */     CHARS[9] = 35;
/*  167 */     CHARS[10] = 19;
/*  168 */     CHARS[13] = 19;
/*  169 */     CHARS[32] = 51;
/*  170 */     CHARS[33] = 49;
/*  171 */     CHARS[34] = 33;
/*  172 */     Arrays.fill(CHARS, 35, 38, (byte)49);
/*  173 */     CHARS[38] = 1;
/*  174 */     Arrays.fill(CHARS, 39, 45, (byte)49);
/*  175 */     Arrays.fill(CHARS, 45, 47, (byte)-71);
/*  176 */     CHARS[47] = 49;
/*  177 */     Arrays.fill(CHARS, 48, 58, (byte)-71);
/*  178 */     CHARS[58] = 61;
/*  179 */     CHARS[59] = 49;
/*  180 */     CHARS[60] = 1;
/*  181 */     CHARS[61] = 49;
/*  182 */     CHARS[62] = 33;
/*  183 */     Arrays.fill(CHARS, 63, 65, (byte)49);
/*  184 */     Arrays.fill(CHARS, 65, 91, (byte)-3);
/*  185 */     Arrays.fill(CHARS, 91, 93, (byte)33);
/*  186 */     CHARS[93] = 1;
/*  187 */     CHARS[94] = 33;
/*  188 */     CHARS[95] = -3;
/*  189 */     CHARS[96] = 33;
/*  190 */     Arrays.fill(CHARS, 97, 123, (byte)-3);
/*  191 */     Arrays.fill(CHARS, 123, 183, (byte)33);
/*  192 */     CHARS['·'] = -87;
/*  193 */     Arrays.fill(CHARS, 184, 192, (byte)33);
/*  194 */     Arrays.fill(CHARS, 192, 215, (byte)-19);
/*  195 */     CHARS['×'] = 33;
/*  196 */     Arrays.fill(CHARS, 216, 247, (byte)-19);
/*  197 */     CHARS['÷'] = 33;
/*  198 */     Arrays.fill(CHARS, 248, 306, (byte)-19);
/*  199 */     Arrays.fill(CHARS, 306, 308, (byte)33);
/*  200 */     Arrays.fill(CHARS, 308, 319, (byte)-19);
/*  201 */     Arrays.fill(CHARS, 319, 321, (byte)33);
/*  202 */     Arrays.fill(CHARS, 321, 329, (byte)-19);
/*  203 */     CHARS[329] = 33;
/*  204 */     Arrays.fill(CHARS, 330, 383, (byte)-19);
/*  205 */     CHARS[383] = 33;
/*  206 */     Arrays.fill(CHARS, 384, 452, (byte)-19);
/*  207 */     Arrays.fill(CHARS, 452, 461, (byte)33);
/*  208 */     Arrays.fill(CHARS, 461, 497, (byte)-19);
/*  209 */     Arrays.fill(CHARS, 497, 500, (byte)33);
/*  210 */     Arrays.fill(CHARS, 500, 502, (byte)-19);
/*  211 */     Arrays.fill(CHARS, 502, 506, (byte)33);
/*  212 */     Arrays.fill(CHARS, 506, 536, (byte)-19);
/*  213 */     Arrays.fill(CHARS, 536, 592, (byte)33);
/*  214 */     Arrays.fill(CHARS, 592, 681, (byte)-19);
/*  215 */     Arrays.fill(CHARS, 681, 699, (byte)33);
/*  216 */     Arrays.fill(CHARS, 699, 706, (byte)-19);
/*  217 */     Arrays.fill(CHARS, 706, 720, (byte)33);
/*  218 */     Arrays.fill(CHARS, 720, 722, (byte)-87);
/*  219 */     Arrays.fill(CHARS, 722, 768, (byte)33);
/*  220 */     Arrays.fill(CHARS, 768, 838, (byte)-87);
/*  221 */     Arrays.fill(CHARS, 838, 864, (byte)33);
/*  222 */     Arrays.fill(CHARS, 864, 866, (byte)-87);
/*  223 */     Arrays.fill(CHARS, 866, 902, (byte)33);
/*  224 */     CHARS[902] = -19;
/*  225 */     CHARS[903] = -87;
/*  226 */     Arrays.fill(CHARS, 904, 907, (byte)-19);
/*  227 */     CHARS[907] = 33;
/*  228 */     CHARS[908] = -19;
/*  229 */     CHARS[909] = 33;
/*  230 */     Arrays.fill(CHARS, 910, 930, (byte)-19);
/*  231 */     CHARS[930] = 33;
/*  232 */     Arrays.fill(CHARS, 931, 975, (byte)-19);
/*  233 */     CHARS[975] = 33;
/*  234 */     Arrays.fill(CHARS, 976, 983, (byte)-19);
/*  235 */     Arrays.fill(CHARS, 983, 986, (byte)33);
/*  236 */     CHARS[986] = -19;
/*  237 */     CHARS[987] = 33;
/*  238 */     CHARS[988] = -19;
/*  239 */     CHARS[989] = 33;
/*  240 */     CHARS[990] = -19;
/*  241 */     CHARS[991] = 33;
/*  242 */     CHARS[992] = -19;
/*  243 */     CHARS[993] = 33;
/*  244 */     Arrays.fill(CHARS, 994, 1012, (byte)-19);
/*  245 */     Arrays.fill(CHARS, 1012, 1025, (byte)33);
/*  246 */     Arrays.fill(CHARS, 1025, 1037, (byte)-19);
/*  247 */     CHARS[1037] = 33;
/*  248 */     Arrays.fill(CHARS, 1038, 1104, (byte)-19);
/*  249 */     CHARS[1104] = 33;
/*  250 */     Arrays.fill(CHARS, 1105, 1117, (byte)-19);
/*  251 */     CHARS[1117] = 33;
/*  252 */     Arrays.fill(CHARS, 1118, 1154, (byte)-19);
/*  253 */     CHARS[1154] = 33;
/*  254 */     Arrays.fill(CHARS, 1155, 1159, (byte)-87);
/*  255 */     Arrays.fill(CHARS, 1159, 1168, (byte)33);
/*  256 */     Arrays.fill(CHARS, 1168, 1221, (byte)-19);
/*  257 */     Arrays.fill(CHARS, 1221, 1223, (byte)33);
/*  258 */     Arrays.fill(CHARS, 1223, 1225, (byte)-19);
/*  259 */     Arrays.fill(CHARS, 1225, 1227, (byte)33);
/*  260 */     Arrays.fill(CHARS, 1227, 1229, (byte)-19);
/*  261 */     Arrays.fill(CHARS, 1229, 1232, (byte)33);
/*  262 */     Arrays.fill(CHARS, 1232, 1260, (byte)-19);
/*  263 */     Arrays.fill(CHARS, 1260, 1262, (byte)33);
/*  264 */     Arrays.fill(CHARS, 1262, 1270, (byte)-19);
/*  265 */     Arrays.fill(CHARS, 1270, 1272, (byte)33);
/*  266 */     Arrays.fill(CHARS, 1272, 1274, (byte)-19);
/*  267 */     Arrays.fill(CHARS, 1274, 1329, (byte)33);
/*  268 */     Arrays.fill(CHARS, 1329, 1367, (byte)-19);
/*  269 */     Arrays.fill(CHARS, 1367, 1369, (byte)33);
/*  270 */     CHARS[1369] = -19;
/*  271 */     Arrays.fill(CHARS, 1370, 1377, (byte)33);
/*  272 */     Arrays.fill(CHARS, 1377, 1415, (byte)-19);
/*  273 */     Arrays.fill(CHARS, 1415, 1425, (byte)33);
/*  274 */     Arrays.fill(CHARS, 1425, 1442, (byte)-87);
/*  275 */     CHARS[1442] = 33;
/*  276 */     Arrays.fill(CHARS, 1443, 1466, (byte)-87);
/*  277 */     CHARS[1466] = 33;
/*  278 */     Arrays.fill(CHARS, 1467, 1470, (byte)-87);
/*  279 */     CHARS[1470] = 33;
/*  280 */     CHARS[1471] = -87;
/*  281 */     CHARS[1472] = 33;
/*  282 */     Arrays.fill(CHARS, 1473, 1475, (byte)-87);
/*  283 */     CHARS[1475] = 33;
/*  284 */     CHARS[1476] = -87;
/*  285 */     Arrays.fill(CHARS, 1477, 1488, (byte)33);
/*  286 */     Arrays.fill(CHARS, 1488, 1515, (byte)-19);
/*  287 */     Arrays.fill(CHARS, 1515, 1520, (byte)33);
/*  288 */     Arrays.fill(CHARS, 1520, 1523, (byte)-19);
/*  289 */     Arrays.fill(CHARS, 1523, 1569, (byte)33);
/*  290 */     Arrays.fill(CHARS, 1569, 1595, (byte)-19);
/*  291 */     Arrays.fill(CHARS, 1595, 1600, (byte)33);
/*  292 */     CHARS[1600] = -87;
/*  293 */     Arrays.fill(CHARS, 1601, 1611, (byte)-19);
/*  294 */     Arrays.fill(CHARS, 1611, 1619, (byte)-87);
/*  295 */     Arrays.fill(CHARS, 1619, 1632, (byte)33);
/*  296 */     Arrays.fill(CHARS, 1632, 1642, (byte)-87);
/*  297 */     Arrays.fill(CHARS, 1642, 1648, (byte)33);
/*  298 */     CHARS[1648] = -87;
/*  299 */     Arrays.fill(CHARS, 1649, 1720, (byte)-19);
/*  300 */     Arrays.fill(CHARS, 1720, 1722, (byte)33);
/*  301 */     Arrays.fill(CHARS, 1722, 1727, (byte)-19);
/*  302 */     CHARS[1727] = 33;
/*  303 */     Arrays.fill(CHARS, 1728, 1743, (byte)-19);
/*  304 */     CHARS[1743] = 33;
/*  305 */     Arrays.fill(CHARS, 1744, 1748, (byte)-19);
/*  306 */     CHARS[1748] = 33;
/*  307 */     CHARS[1749] = -19;
/*  308 */     Arrays.fill(CHARS, 1750, 1765, (byte)-87);
/*  309 */     Arrays.fill(CHARS, 1765, 1767, (byte)-19);
/*  310 */     Arrays.fill(CHARS, 1767, 1769, (byte)-87);
/*  311 */     CHARS[1769] = 33;
/*  312 */     Arrays.fill(CHARS, 1770, 1774, (byte)-87);
/*  313 */     Arrays.fill(CHARS, 1774, 1776, (byte)33);
/*  314 */     Arrays.fill(CHARS, 1776, 1786, (byte)-87);
/*  315 */     Arrays.fill(CHARS, 1786, 2305, (byte)33);
/*  316 */     Arrays.fill(CHARS, 2305, 2308, (byte)-87);
/*  317 */     CHARS[2308] = 33;
/*  318 */     Arrays.fill(CHARS, 2309, 2362, (byte)-19);
/*  319 */     Arrays.fill(CHARS, 2362, 2364, (byte)33);
/*  320 */     CHARS[2364] = -87;
/*  321 */     CHARS[2365] = -19;
/*  322 */     Arrays.fill(CHARS, 2366, 2382, (byte)-87);
/*  323 */     Arrays.fill(CHARS, 2382, 2385, (byte)33);
/*  324 */     Arrays.fill(CHARS, 2385, 2389, (byte)-87);
/*  325 */     Arrays.fill(CHARS, 2389, 2392, (byte)33);
/*  326 */     Arrays.fill(CHARS, 2392, 2402, (byte)-19);
/*  327 */     Arrays.fill(CHARS, 2402, 2404, (byte)-87);
/*  328 */     Arrays.fill(CHARS, 2404, 2406, (byte)33);
/*  329 */     Arrays.fill(CHARS, 2406, 2416, (byte)-87);
/*  330 */     Arrays.fill(CHARS, 2416, 2433, (byte)33);
/*  331 */     Arrays.fill(CHARS, 2433, 2436, (byte)-87);
/*  332 */     CHARS[2436] = 33;
/*  333 */     Arrays.fill(CHARS, 2437, 2445, (byte)-19);
/*  334 */     Arrays.fill(CHARS, 2445, 2447, (byte)33);
/*  335 */     Arrays.fill(CHARS, 2447, 2449, (byte)-19);
/*  336 */     Arrays.fill(CHARS, 2449, 2451, (byte)33);
/*  337 */     Arrays.fill(CHARS, 2451, 2473, (byte)-19);
/*  338 */     CHARS[2473] = 33;
/*  339 */     Arrays.fill(CHARS, 2474, 2481, (byte)-19);
/*  340 */     CHARS[2481] = 33;
/*  341 */     CHARS[2482] = -19;
/*  342 */     Arrays.fill(CHARS, 2483, 2486, (byte)33);
/*  343 */     Arrays.fill(CHARS, 2486, 2490, (byte)-19);
/*  344 */     Arrays.fill(CHARS, 2490, 2492, (byte)33);
/*  345 */     CHARS[2492] = -87;
/*  346 */     CHARS[2493] = 33;
/*  347 */     Arrays.fill(CHARS, 2494, 2501, (byte)-87);
/*  348 */     Arrays.fill(CHARS, 2501, 2503, (byte)33);
/*  349 */     Arrays.fill(CHARS, 2503, 2505, (byte)-87);
/*  350 */     Arrays.fill(CHARS, 2505, 2507, (byte)33);
/*  351 */     Arrays.fill(CHARS, 2507, 2510, (byte)-87);
/*  352 */     Arrays.fill(CHARS, 2510, 2519, (byte)33);
/*  353 */     CHARS[2519] = -87;
/*  354 */     Arrays.fill(CHARS, 2520, 2524, (byte)33);
/*  355 */     Arrays.fill(CHARS, 2524, 2526, (byte)-19);
/*  356 */     CHARS[2526] = 33;
/*  357 */     Arrays.fill(CHARS, 2527, 2530, (byte)-19);
/*  358 */     Arrays.fill(CHARS, 2530, 2532, (byte)-87);
/*  359 */     Arrays.fill(CHARS, 2532, 2534, (byte)33);
/*  360 */     Arrays.fill(CHARS, 2534, 2544, (byte)-87);
/*  361 */     Arrays.fill(CHARS, 2544, 2546, (byte)-19);
/*  362 */     Arrays.fill(CHARS, 2546, 2562, (byte)33);
/*  363 */     CHARS[2562] = -87;
/*  364 */     Arrays.fill(CHARS, 2563, 2565, (byte)33);
/*  365 */     Arrays.fill(CHARS, 2565, 2571, (byte)-19);
/*  366 */     Arrays.fill(CHARS, 2571, 2575, (byte)33);
/*  367 */     Arrays.fill(CHARS, 2575, 2577, (byte)-19);
/*  368 */     Arrays.fill(CHARS, 2577, 2579, (byte)33);
/*  369 */     Arrays.fill(CHARS, 2579, 2601, (byte)-19);
/*  370 */     CHARS[2601] = 33;
/*  371 */     Arrays.fill(CHARS, 2602, 2609, (byte)-19);
/*  372 */     CHARS[2609] = 33;
/*  373 */     Arrays.fill(CHARS, 2610, 2612, (byte)-19);
/*  374 */     CHARS[2612] = 33;
/*  375 */     Arrays.fill(CHARS, 2613, 2615, (byte)-19);
/*  376 */     CHARS[2615] = 33;
/*  377 */     Arrays.fill(CHARS, 2616, 2618, (byte)-19);
/*  378 */     Arrays.fill(CHARS, 2618, 2620, (byte)33);
/*  379 */     CHARS[2620] = -87;
/*  380 */     CHARS[2621] = 33;
/*  381 */     Arrays.fill(CHARS, 2622, 2627, (byte)-87);
/*  382 */     Arrays.fill(CHARS, 2627, 2631, (byte)33);
/*  383 */     Arrays.fill(CHARS, 2631, 2633, (byte)-87);
/*  384 */     Arrays.fill(CHARS, 2633, 2635, (byte)33);
/*  385 */     Arrays.fill(CHARS, 2635, 2638, (byte)-87);
/*  386 */     Arrays.fill(CHARS, 2638, 2649, (byte)33);
/*  387 */     Arrays.fill(CHARS, 2649, 2653, (byte)-19);
/*  388 */     CHARS[2653] = 33;
/*  389 */     CHARS[2654] = -19;
/*  390 */     Arrays.fill(CHARS, 2655, 2662, (byte)33);
/*  391 */     Arrays.fill(CHARS, 2662, 2674, (byte)-87);
/*  392 */     Arrays.fill(CHARS, 2674, 2677, (byte)-19);
/*  393 */     Arrays.fill(CHARS, 2677, 2689, (byte)33);
/*  394 */     Arrays.fill(CHARS, 2689, 2692, (byte)-87);
/*  395 */     CHARS[2692] = 33;
/*  396 */     Arrays.fill(CHARS, 2693, 2700, (byte)-19);
/*  397 */     CHARS[2700] = 33;
/*  398 */     CHARS[2701] = -19;
/*  399 */     CHARS[2702] = 33;
/*  400 */     Arrays.fill(CHARS, 2703, 2706, (byte)-19);
/*  401 */     CHARS[2706] = 33;
/*  402 */     Arrays.fill(CHARS, 2707, 2729, (byte)-19);
/*  403 */     CHARS[2729] = 33;
/*  404 */     Arrays.fill(CHARS, 2730, 2737, (byte)-19);
/*  405 */     CHARS[2737] = 33;
/*  406 */     Arrays.fill(CHARS, 2738, 2740, (byte)-19);
/*  407 */     CHARS[2740] = 33;
/*  408 */     Arrays.fill(CHARS, 2741, 2746, (byte)-19);
/*  409 */     Arrays.fill(CHARS, 2746, 2748, (byte)33);
/*  410 */     CHARS[2748] = -87;
/*  411 */     CHARS[2749] = -19;
/*  412 */     Arrays.fill(CHARS, 2750, 2758, (byte)-87);
/*  413 */     CHARS[2758] = 33;
/*  414 */     Arrays.fill(CHARS, 2759, 2762, (byte)-87);
/*  415 */     CHARS[2762] = 33;
/*  416 */     Arrays.fill(CHARS, 2763, 2766, (byte)-87);
/*  417 */     Arrays.fill(CHARS, 2766, 2784, (byte)33);
/*  418 */     CHARS[2784] = -19;
/*  419 */     Arrays.fill(CHARS, 2785, 2790, (byte)33);
/*  420 */     Arrays.fill(CHARS, 2790, 2800, (byte)-87);
/*  421 */     Arrays.fill(CHARS, 2800, 2817, (byte)33);
/*  422 */     Arrays.fill(CHARS, 2817, 2820, (byte)-87);
/*  423 */     CHARS[2820] = 33;
/*  424 */     Arrays.fill(CHARS, 2821, 2829, (byte)-19);
/*  425 */     Arrays.fill(CHARS, 2829, 2831, (byte)33);
/*  426 */     Arrays.fill(CHARS, 2831, 2833, (byte)-19);
/*  427 */     Arrays.fill(CHARS, 2833, 2835, (byte)33);
/*  428 */     Arrays.fill(CHARS, 2835, 2857, (byte)-19);
/*  429 */     CHARS[2857] = 33;
/*  430 */     Arrays.fill(CHARS, 2858, 2865, (byte)-19);
/*  431 */     CHARS[2865] = 33;
/*  432 */     Arrays.fill(CHARS, 2866, 2868, (byte)-19);
/*  433 */     Arrays.fill(CHARS, 2868, 2870, (byte)33);
/*  434 */     Arrays.fill(CHARS, 2870, 2874, (byte)-19);
/*  435 */     Arrays.fill(CHARS, 2874, 2876, (byte)33);
/*  436 */     CHARS[2876] = -87;
/*  437 */     CHARS[2877] = -19;
/*  438 */     Arrays.fill(CHARS, 2878, 2884, (byte)-87);
/*  439 */     Arrays.fill(CHARS, 2884, 2887, (byte)33);
/*  440 */     Arrays.fill(CHARS, 2887, 2889, (byte)-87);
/*  441 */     Arrays.fill(CHARS, 2889, 2891, (byte)33);
/*  442 */     Arrays.fill(CHARS, 2891, 2894, (byte)-87);
/*  443 */     Arrays.fill(CHARS, 2894, 2902, (byte)33);
/*  444 */     Arrays.fill(CHARS, 2902, 2904, (byte)-87);
/*  445 */     Arrays.fill(CHARS, 2904, 2908, (byte)33);
/*  446 */     Arrays.fill(CHARS, 2908, 2910, (byte)-19);
/*  447 */     CHARS[2910] = 33;
/*  448 */     Arrays.fill(CHARS, 2911, 2914, (byte)-19);
/*  449 */     Arrays.fill(CHARS, 2914, 2918, (byte)33);
/*  450 */     Arrays.fill(CHARS, 2918, 2928, (byte)-87);
/*  451 */     Arrays.fill(CHARS, 2928, 2946, (byte)33);
/*  452 */     Arrays.fill(CHARS, 2946, 2948, (byte)-87);
/*  453 */     CHARS[2948] = 33;
/*  454 */     Arrays.fill(CHARS, 2949, 2955, (byte)-19);
/*  455 */     Arrays.fill(CHARS, 2955, 2958, (byte)33);
/*  456 */     Arrays.fill(CHARS, 2958, 2961, (byte)-19);
/*  457 */     CHARS[2961] = 33;
/*  458 */     Arrays.fill(CHARS, 2962, 2966, (byte)-19);
/*  459 */     Arrays.fill(CHARS, 2966, 2969, (byte)33);
/*  460 */     Arrays.fill(CHARS, 2969, 2971, (byte)-19);
/*  461 */     CHARS[2971] = 33;
/*  462 */     CHARS[2972] = -19;
/*  463 */     CHARS[2973] = 33;
/*  464 */     Arrays.fill(CHARS, 2974, 2976, (byte)-19);
/*  465 */     Arrays.fill(CHARS, 2976, 2979, (byte)33);
/*  466 */     Arrays.fill(CHARS, 2979, 2981, (byte)-19);
/*  467 */     Arrays.fill(CHARS, 2981, 2984, (byte)33);
/*  468 */     Arrays.fill(CHARS, 2984, 2987, (byte)-19);
/*  469 */     Arrays.fill(CHARS, 2987, 2990, (byte)33);
/*  470 */     Arrays.fill(CHARS, 2990, 2998, (byte)-19);
/*  471 */     CHARS[2998] = 33;
/*  472 */     Arrays.fill(CHARS, 2999, 3002, (byte)-19);
/*  473 */     Arrays.fill(CHARS, 3002, 3006, (byte)33);
/*  474 */     Arrays.fill(CHARS, 3006, 3011, (byte)-87);
/*  475 */     Arrays.fill(CHARS, 3011, 3014, (byte)33);
/*  476 */     Arrays.fill(CHARS, 3014, 3017, (byte)-87);
/*  477 */     CHARS[3017] = 33;
/*  478 */     Arrays.fill(CHARS, 3018, 3022, (byte)-87);
/*  479 */     Arrays.fill(CHARS, 3022, 3031, (byte)33);
/*  480 */     CHARS[3031] = -87;
/*  481 */     Arrays.fill(CHARS, 3032, 3047, (byte)33);
/*  482 */     Arrays.fill(CHARS, 3047, 3056, (byte)-87);
/*  483 */     Arrays.fill(CHARS, 3056, 3073, (byte)33);
/*  484 */     Arrays.fill(CHARS, 3073, 3076, (byte)-87);
/*  485 */     CHARS[3076] = 33;
/*  486 */     Arrays.fill(CHARS, 3077, 3085, (byte)-19);
/*  487 */     CHARS[3085] = 33;
/*  488 */     Arrays.fill(CHARS, 3086, 3089, (byte)-19);
/*  489 */     CHARS[3089] = 33;
/*  490 */     Arrays.fill(CHARS, 3090, 3113, (byte)-19);
/*  491 */     CHARS[3113] = 33;
/*  492 */     Arrays.fill(CHARS, 3114, 3124, (byte)-19);
/*  493 */     CHARS[3124] = 33;
/*  494 */     Arrays.fill(CHARS, 3125, 3130, (byte)-19);
/*  495 */     Arrays.fill(CHARS, 3130, 3134, (byte)33);
/*  496 */     Arrays.fill(CHARS, 3134, 3141, (byte)-87);
/*  497 */     CHARS[3141] = 33;
/*  498 */     Arrays.fill(CHARS, 3142, 3145, (byte)-87);
/*  499 */     CHARS[3145] = 33;
/*  500 */     Arrays.fill(CHARS, 3146, 3150, (byte)-87);
/*  501 */     Arrays.fill(CHARS, 3150, 3157, (byte)33);
/*  502 */     Arrays.fill(CHARS, 3157, 3159, (byte)-87);
/*  503 */     Arrays.fill(CHARS, 3159, 3168, (byte)33);
/*  504 */     Arrays.fill(CHARS, 3168, 3170, (byte)-19);
/*  505 */     Arrays.fill(CHARS, 3170, 3174, (byte)33);
/*  506 */     Arrays.fill(CHARS, 3174, 3184, (byte)-87);
/*  507 */     Arrays.fill(CHARS, 3184, 3202, (byte)33);
/*  508 */     Arrays.fill(CHARS, 3202, 3204, (byte)-87);
/*  509 */     CHARS[3204] = 33;
/*  510 */     Arrays.fill(CHARS, 3205, 3213, (byte)-19);
/*  511 */     CHARS[3213] = 33;
/*  512 */     Arrays.fill(CHARS, 3214, 3217, (byte)-19);
/*  513 */     CHARS[3217] = 33;
/*  514 */     Arrays.fill(CHARS, 3218, 3241, (byte)-19);
/*  515 */     CHARS[3241] = 33;
/*  516 */     Arrays.fill(CHARS, 3242, 3252, (byte)-19);
/*  517 */     CHARS[3252] = 33;
/*  518 */     Arrays.fill(CHARS, 3253, 3258, (byte)-19);
/*  519 */     Arrays.fill(CHARS, 3258, 3262, (byte)33);
/*  520 */     Arrays.fill(CHARS, 3262, 3269, (byte)-87);
/*  521 */     CHARS[3269] = 33;
/*  522 */     Arrays.fill(CHARS, 3270, 3273, (byte)-87);
/*  523 */     CHARS[3273] = 33;
/*  524 */     Arrays.fill(CHARS, 3274, 3278, (byte)-87);
/*  525 */     Arrays.fill(CHARS, 3278, 3285, (byte)33);
/*  526 */     Arrays.fill(CHARS, 3285, 3287, (byte)-87);
/*  527 */     Arrays.fill(CHARS, 3287, 3294, (byte)33);
/*  528 */     CHARS[3294] = -19;
/*  529 */     CHARS[3295] = 33;
/*  530 */     Arrays.fill(CHARS, 3296, 3298, (byte)-19);
/*  531 */     Arrays.fill(CHARS, 3298, 3302, (byte)33);
/*  532 */     Arrays.fill(CHARS, 3302, 3312, (byte)-87);
/*  533 */     Arrays.fill(CHARS, 3312, 3330, (byte)33);
/*  534 */     Arrays.fill(CHARS, 3330, 3332, (byte)-87);
/*  535 */     CHARS[3332] = 33;
/*  536 */     Arrays.fill(CHARS, 3333, 3341, (byte)-19);
/*  537 */     CHARS[3341] = 33;
/*  538 */     Arrays.fill(CHARS, 3342, 3345, (byte)-19);
/*  539 */     CHARS[3345] = 33;
/*  540 */     Arrays.fill(CHARS, 3346, 3369, (byte)-19);
/*  541 */     CHARS[3369] = 33;
/*  542 */     Arrays.fill(CHARS, 3370, 3386, (byte)-19);
/*  543 */     Arrays.fill(CHARS, 3386, 3390, (byte)33);
/*  544 */     Arrays.fill(CHARS, 3390, 3396, (byte)-87);
/*  545 */     Arrays.fill(CHARS, 3396, 3398, (byte)33);
/*  546 */     Arrays.fill(CHARS, 3398, 3401, (byte)-87);
/*  547 */     CHARS[3401] = 33;
/*  548 */     Arrays.fill(CHARS, 3402, 3406, (byte)-87);
/*  549 */     Arrays.fill(CHARS, 3406, 3415, (byte)33);
/*  550 */     CHARS[3415] = -87;
/*  551 */     Arrays.fill(CHARS, 3416, 3424, (byte)33);
/*  552 */     Arrays.fill(CHARS, 3424, 3426, (byte)-19);
/*  553 */     Arrays.fill(CHARS, 3426, 3430, (byte)33);
/*  554 */     Arrays.fill(CHARS, 3430, 3440, (byte)-87);
/*  555 */     Arrays.fill(CHARS, 3440, 3585, (byte)33);
/*  556 */     Arrays.fill(CHARS, 3585, 3631, (byte)-19);
/*  557 */     CHARS[3631] = 33;
/*  558 */     CHARS[3632] = -19;
/*  559 */     CHARS[3633] = -87;
/*  560 */     Arrays.fill(CHARS, 3634, 3636, (byte)-19);
/*  561 */     Arrays.fill(CHARS, 3636, 3643, (byte)-87);
/*  562 */     Arrays.fill(CHARS, 3643, 3648, (byte)33);
/*  563 */     Arrays.fill(CHARS, 3648, 3654, (byte)-19);
/*  564 */     Arrays.fill(CHARS, 3654, 3663, (byte)-87);
/*  565 */     CHARS[3663] = 33;
/*  566 */     Arrays.fill(CHARS, 3664, 3674, (byte)-87);
/*  567 */     Arrays.fill(CHARS, 3674, 3713, (byte)33);
/*  568 */     Arrays.fill(CHARS, 3713, 3715, (byte)-19);
/*  569 */     CHARS[3715] = 33;
/*  570 */     CHARS[3716] = -19;
/*  571 */     Arrays.fill(CHARS, 3717, 3719, (byte)33);
/*  572 */     Arrays.fill(CHARS, 3719, 3721, (byte)-19);
/*  573 */     CHARS[3721] = 33;
/*  574 */     CHARS[3722] = -19;
/*  575 */     Arrays.fill(CHARS, 3723, 3725, (byte)33);
/*  576 */     CHARS[3725] = -19;
/*  577 */     Arrays.fill(CHARS, 3726, 3732, (byte)33);
/*  578 */     Arrays.fill(CHARS, 3732, 3736, (byte)-19);
/*  579 */     CHARS[3736] = 33;
/*  580 */     Arrays.fill(CHARS, 3737, 3744, (byte)-19);
/*  581 */     CHARS[3744] = 33;
/*  582 */     Arrays.fill(CHARS, 3745, 3748, (byte)-19);
/*  583 */     CHARS[3748] = 33;
/*  584 */     CHARS[3749] = -19;
/*  585 */     CHARS[3750] = 33;
/*  586 */     CHARS[3751] = -19;
/*  587 */     Arrays.fill(CHARS, 3752, 3754, (byte)33);
/*  588 */     Arrays.fill(CHARS, 3754, 3756, (byte)-19);
/*  589 */     CHARS[3756] = 33;
/*  590 */     Arrays.fill(CHARS, 3757, 3759, (byte)-19);
/*  591 */     CHARS[3759] = 33;
/*  592 */     CHARS[3760] = -19;
/*  593 */     CHARS[3761] = -87;
/*  594 */     Arrays.fill(CHARS, 3762, 3764, (byte)-19);
/*  595 */     Arrays.fill(CHARS, 3764, 3770, (byte)-87);
/*  596 */     CHARS[3770] = 33;
/*  597 */     Arrays.fill(CHARS, 3771, 3773, (byte)-87);
/*  598 */     CHARS[3773] = -19;
/*  599 */     Arrays.fill(CHARS, 3774, 3776, (byte)33);
/*  600 */     Arrays.fill(CHARS, 3776, 3781, (byte)-19);
/*  601 */     CHARS[3781] = 33;
/*  602 */     CHARS[3782] = -87;
/*  603 */     CHARS[3783] = 33;
/*  604 */     Arrays.fill(CHARS, 3784, 3790, (byte)-87);
/*  605 */     Arrays.fill(CHARS, 3790, 3792, (byte)33);
/*  606 */     Arrays.fill(CHARS, 3792, 3802, (byte)-87);
/*  607 */     Arrays.fill(CHARS, 3802, 3864, (byte)33);
/*  608 */     Arrays.fill(CHARS, 3864, 3866, (byte)-87);
/*  609 */     Arrays.fill(CHARS, 3866, 3872, (byte)33);
/*  610 */     Arrays.fill(CHARS, 3872, 3882, (byte)-87);
/*  611 */     Arrays.fill(CHARS, 3882, 3893, (byte)33);
/*  612 */     CHARS[3893] = -87;
/*  613 */     CHARS[3894] = 33;
/*  614 */     CHARS[3895] = -87;
/*  615 */     CHARS[3896] = 33;
/*  616 */     CHARS[3897] = -87;
/*  617 */     Arrays.fill(CHARS, 3898, 3902, (byte)33);
/*  618 */     Arrays.fill(CHARS, 3902, 3904, (byte)-87);
/*  619 */     Arrays.fill(CHARS, 3904, 3912, (byte)-19);
/*  620 */     CHARS[3912] = 33;
/*  621 */     Arrays.fill(CHARS, 3913, 3946, (byte)-19);
/*  622 */     Arrays.fill(CHARS, 3946, 3953, (byte)33);
/*  623 */     Arrays.fill(CHARS, 3953, 3973, (byte)-87);
/*  624 */     CHARS[3973] = 33;
/*  625 */     Arrays.fill(CHARS, 3974, 3980, (byte)-87);
/*  626 */     Arrays.fill(CHARS, 3980, 3984, (byte)33);
/*  627 */     Arrays.fill(CHARS, 3984, 3990, (byte)-87);
/*  628 */     CHARS[3990] = 33;
/*  629 */     CHARS[3991] = -87;
/*  630 */     CHARS[3992] = 33;
/*  631 */     Arrays.fill(CHARS, 3993, 4014, (byte)-87);
/*  632 */     Arrays.fill(CHARS, 4014, 4017, (byte)33);
/*  633 */     Arrays.fill(CHARS, 4017, 4024, (byte)-87);
/*  634 */     CHARS[4024] = 33;
/*  635 */     CHARS[4025] = -87;
/*  636 */     Arrays.fill(CHARS, 4026, 4256, (byte)33);
/*  637 */     Arrays.fill(CHARS, 4256, 4294, (byte)-19);
/*  638 */     Arrays.fill(CHARS, 4294, 4304, (byte)33);
/*  639 */     Arrays.fill(CHARS, 4304, 4343, (byte)-19);
/*  640 */     Arrays.fill(CHARS, 4343, 4352, (byte)33);
/*  641 */     CHARS[4352] = -19;
/*  642 */     CHARS[4353] = 33;
/*  643 */     Arrays.fill(CHARS, 4354, 4356, (byte)-19);
/*  644 */     CHARS[4356] = 33;
/*  645 */     Arrays.fill(CHARS, 4357, 4360, (byte)-19);
/*  646 */     CHARS[4360] = 33;
/*  647 */     CHARS[4361] = -19;
/*  648 */     CHARS[4362] = 33;
/*  649 */     Arrays.fill(CHARS, 4363, 4365, (byte)-19);
/*  650 */     CHARS[4365] = 33;
/*  651 */     Arrays.fill(CHARS, 4366, 4371, (byte)-19);
/*  652 */     Arrays.fill(CHARS, 4371, 4412, (byte)33);
/*  653 */     CHARS[4412] = -19;
/*  654 */     CHARS[4413] = 33;
/*  655 */     CHARS[4414] = -19;
/*  656 */     CHARS[4415] = 33;
/*  657 */     CHARS[4416] = -19;
/*  658 */     Arrays.fill(CHARS, 4417, 4428, (byte)33);
/*  659 */     CHARS[4428] = -19;
/*  660 */     CHARS[4429] = 33;
/*  661 */     CHARS[4430] = -19;
/*  662 */     CHARS[4431] = 33;
/*  663 */     CHARS[4432] = -19;
/*  664 */     Arrays.fill(CHARS, 4433, 4436, (byte)33);
/*  665 */     Arrays.fill(CHARS, 4436, 4438, (byte)-19);
/*  666 */     Arrays.fill(CHARS, 4438, 4441, (byte)33);
/*  667 */     CHARS[4441] = -19;
/*  668 */     Arrays.fill(CHARS, 4442, 4447, (byte)33);
/*  669 */     Arrays.fill(CHARS, 4447, 4450, (byte)-19);
/*  670 */     CHARS[4450] = 33;
/*  671 */     CHARS[4451] = -19;
/*  672 */     CHARS[4452] = 33;
/*  673 */     CHARS[4453] = -19;
/*  674 */     CHARS[4454] = 33;
/*  675 */     CHARS[4455] = -19;
/*  676 */     CHARS[4456] = 33;
/*  677 */     CHARS[4457] = -19;
/*  678 */     Arrays.fill(CHARS, 4458, 4461, (byte)33);
/*  679 */     Arrays.fill(CHARS, 4461, 4463, (byte)-19);
/*  680 */     Arrays.fill(CHARS, 4463, 4466, (byte)33);
/*  681 */     Arrays.fill(CHARS, 4466, 4468, (byte)-19);
/*  682 */     CHARS[4468] = 33;
/*  683 */     CHARS[4469] = -19;
/*  684 */     Arrays.fill(CHARS, 4470, 4510, (byte)33);
/*  685 */     CHARS[4510] = -19;
/*  686 */     Arrays.fill(CHARS, 4511, 4520, (byte)33);
/*  687 */     CHARS[4520] = -19;
/*  688 */     Arrays.fill(CHARS, 4521, 4523, (byte)33);
/*  689 */     CHARS[4523] = -19;
/*  690 */     Arrays.fill(CHARS, 4524, 4526, (byte)33);
/*  691 */     Arrays.fill(CHARS, 4526, 4528, (byte)-19);
/*  692 */     Arrays.fill(CHARS, 4528, 4535, (byte)33);
/*  693 */     Arrays.fill(CHARS, 4535, 4537, (byte)-19);
/*  694 */     CHARS[4537] = 33;
/*  695 */     CHARS[4538] = -19;
/*  696 */     CHARS[4539] = 33;
/*  697 */     Arrays.fill(CHARS, 4540, 4547, (byte)-19);
/*  698 */     Arrays.fill(CHARS, 4547, 4587, (byte)33);
/*  699 */     CHARS[4587] = -19;
/*  700 */     Arrays.fill(CHARS, 4588, 4592, (byte)33);
/*  701 */     CHARS[4592] = -19;
/*  702 */     Arrays.fill(CHARS, 4593, 4601, (byte)33);
/*  703 */     CHARS[4601] = -19;
/*  704 */     Arrays.fill(CHARS, 4602, 7680, (byte)33);
/*  705 */     Arrays.fill(CHARS, 7680, 7836, (byte)-19);
/*  706 */     Arrays.fill(CHARS, 7836, 7840, (byte)33);
/*  707 */     Arrays.fill(CHARS, 7840, 7930, (byte)-19);
/*  708 */     Arrays.fill(CHARS, 7930, 7936, (byte)33);
/*  709 */     Arrays.fill(CHARS, 7936, 7958, (byte)-19);
/*  710 */     Arrays.fill(CHARS, 7958, 7960, (byte)33);
/*  711 */     Arrays.fill(CHARS, 7960, 7966, (byte)-19);
/*  712 */     Arrays.fill(CHARS, 7966, 7968, (byte)33);
/*  713 */     Arrays.fill(CHARS, 7968, 8006, (byte)-19);
/*  714 */     Arrays.fill(CHARS, 8006, 8008, (byte)33);
/*  715 */     Arrays.fill(CHARS, 8008, 8014, (byte)-19);
/*  716 */     Arrays.fill(CHARS, 8014, 8016, (byte)33);
/*  717 */     Arrays.fill(CHARS, 8016, 8024, (byte)-19);
/*  718 */     CHARS[8024] = 33;
/*  719 */     CHARS[8025] = -19;
/*  720 */     CHARS[8026] = 33;
/*  721 */     CHARS[8027] = -19;
/*  722 */     CHARS[8028] = 33;
/*  723 */     CHARS[8029] = -19;
/*  724 */     CHARS[8030] = 33;
/*  725 */     Arrays.fill(CHARS, 8031, 8062, (byte)-19);
/*  726 */     Arrays.fill(CHARS, 8062, 8064, (byte)33);
/*  727 */     Arrays.fill(CHARS, 8064, 8117, (byte)-19);
/*  728 */     CHARS[8117] = 33;
/*  729 */     Arrays.fill(CHARS, 8118, 8125, (byte)-19);
/*  730 */     CHARS[8125] = 33;
/*  731 */     CHARS[8126] = -19;
/*  732 */     Arrays.fill(CHARS, 8127, 8130, (byte)33);
/*  733 */     Arrays.fill(CHARS, 8130, 8133, (byte)-19);
/*  734 */     CHARS[8133] = 33;
/*  735 */     Arrays.fill(CHARS, 8134, 8141, (byte)-19);
/*  736 */     Arrays.fill(CHARS, 8141, 8144, (byte)33);
/*  737 */     Arrays.fill(CHARS, 8144, 8148, (byte)-19);
/*  738 */     Arrays.fill(CHARS, 8148, 8150, (byte)33);
/*  739 */     Arrays.fill(CHARS, 8150, 8156, (byte)-19);
/*  740 */     Arrays.fill(CHARS, 8156, 8160, (byte)33);
/*  741 */     Arrays.fill(CHARS, 8160, 8173, (byte)-19);
/*  742 */     Arrays.fill(CHARS, 8173, 8178, (byte)33);
/*  743 */     Arrays.fill(CHARS, 8178, 8181, (byte)-19);
/*  744 */     CHARS[8181] = 33;
/*  745 */     Arrays.fill(CHARS, 8182, 8189, (byte)-19);
/*  746 */     Arrays.fill(CHARS, 8189, 8400, (byte)33);
/*  747 */     Arrays.fill(CHARS, 8400, 8413, (byte)-87);
/*  748 */     Arrays.fill(CHARS, 8413, 8417, (byte)33);
/*  749 */     CHARS[8417] = -87;
/*  750 */     Arrays.fill(CHARS, 8418, 8486, (byte)33);
/*  751 */     CHARS[8486] = -19;
/*  752 */     Arrays.fill(CHARS, 8487, 8490, (byte)33);
/*  753 */     Arrays.fill(CHARS, 8490, 8492, (byte)-19);
/*  754 */     Arrays.fill(CHARS, 8492, 8494, (byte)33);
/*  755 */     CHARS[8494] = -19;
/*  756 */     Arrays.fill(CHARS, 8495, 8576, (byte)33);
/*  757 */     Arrays.fill(CHARS, 8576, 8579, (byte)-19);
/*  758 */     Arrays.fill(CHARS, 8579, 12293, (byte)33);
/*  759 */     CHARS[12293] = -87;
/*  760 */     CHARS[12294] = 33;
/*  761 */     CHARS[12295] = -19;
/*  762 */     Arrays.fill(CHARS, 12296, 12321, (byte)33);
/*  763 */     Arrays.fill(CHARS, 12321, 12330, (byte)-19);
/*  764 */     Arrays.fill(CHARS, 12330, 12336, (byte)-87);
/*  765 */     CHARS[12336] = 33;
/*  766 */     Arrays.fill(CHARS, 12337, 12342, (byte)-87);
/*  767 */     Arrays.fill(CHARS, 12342, 12353, (byte)33);
/*  768 */     Arrays.fill(CHARS, 12353, 12437, (byte)-19);
/*  769 */     Arrays.fill(CHARS, 12437, 12441, (byte)33);
/*  770 */     Arrays.fill(CHARS, 12441, 12443, (byte)-87);
/*  771 */     Arrays.fill(CHARS, 12443, 12445, (byte)33);
/*  772 */     Arrays.fill(CHARS, 12445, 12447, (byte)-87);
/*  773 */     Arrays.fill(CHARS, 12447, 12449, (byte)33);
/*  774 */     Arrays.fill(CHARS, 12449, 12539, (byte)-19);
/*  775 */     CHARS[12539] = 33;
/*  776 */     Arrays.fill(CHARS, 12540, 12543, (byte)-87);
/*  777 */     Arrays.fill(CHARS, 12543, 12549, (byte)33);
/*  778 */     Arrays.fill(CHARS, 12549, 12589, (byte)-19);
/*  779 */     Arrays.fill(CHARS, 12589, 19968, (byte)33);
/*  780 */     Arrays.fill(CHARS, 19968, 40870, (byte)-19);
/*  781 */     Arrays.fill(CHARS, 40870, 44032, (byte)33);
/*  782 */     Arrays.fill(CHARS, 44032, 55204, (byte)-19);
/*  783 */     Arrays.fill(CHARS, 55204, 55296, (byte)33);
/*  784 */     Arrays.fill(CHARS, 57344, 65534, (byte)33);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar
 * JD-Core Version:    0.6.2
 */