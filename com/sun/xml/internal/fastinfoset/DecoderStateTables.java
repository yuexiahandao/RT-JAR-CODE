/*     */ package com.sun.xml.internal.fastinfoset;
/*     */ 
/*     */ public class DecoderStateTables
/*     */ {
/*  31 */   private static int RANGE_INDEX_END = 0;
/*  32 */   private static int RANGE_INDEX_VALUE = 1;
/*     */   public static final int STATE_ILLEGAL = 255;
/*     */   public static final int STATE_UNSUPPORTED = 254;
/*     */   public static final int EII_NO_AIIS_INDEX_SMALL = 0;
/*     */   public static final int EII_AIIS_INDEX_SMALL = 1;
/*     */   public static final int EII_INDEX_MEDIUM = 2;
/*     */   public static final int EII_INDEX_LARGE = 3;
/*     */   public static final int EII_NAMESPACES = 4;
/*     */   public static final int EII_LITERAL = 5;
/*     */   public static final int CII_UTF8_SMALL_LENGTH = 6;
/*     */   public static final int CII_UTF8_MEDIUM_LENGTH = 7;
/*     */   public static final int CII_UTF8_LARGE_LENGTH = 8;
/*     */   public static final int CII_UTF16_SMALL_LENGTH = 9;
/*     */   public static final int CII_UTF16_MEDIUM_LENGTH = 10;
/*     */   public static final int CII_UTF16_LARGE_LENGTH = 11;
/*     */   public static final int CII_RA = 12;
/*     */   public static final int CII_EA = 13;
/*     */   public static final int CII_INDEX_SMALL = 14;
/*     */   public static final int CII_INDEX_MEDIUM = 15;
/*     */   public static final int CII_INDEX_LARGE = 16;
/*     */   public static final int CII_INDEX_LARGE_LARGE = 17;
/*     */   public static final int COMMENT_II = 18;
/*     */   public static final int PROCESSING_INSTRUCTION_II = 19;
/*     */   public static final int DOCUMENT_TYPE_DECLARATION_II = 20;
/*     */   public static final int UNEXPANDED_ENTITY_REFERENCE_II = 21;
/*     */   public static final int TERMINATOR_SINGLE = 22;
/*     */   public static final int TERMINATOR_DOUBLE = 23;
/*  63 */   private static final int[] DII = new int[256];
/*     */ 
/*  65 */   private static final int[][] DII_RANGES = { { 31, 0 }, { 39, 2 }, { 48, 3 }, { 55, 255 }, { 56, 4 }, { 59, 255 }, { 60, 5 }, { 61, 5 }, { 62, 255 }, { 63, 5 }, { 95, 1 }, { 103, 2 }, { 112, 3 }, { 119, 255 }, { 120, 4 }, { 123, 255 }, { 124, 5 }, { 125, 5 }, { 126, 255 }, { 127, 5 }, { 195, 255 }, { 199, 20 }, { 224, 255 }, { 225, 19 }, { 226, 18 }, { 239, 255 }, { 240, 22 }, { 254, 255 }, { 255, 23 } };
/*     */ 
/* 162 */   private static final int[] EII = new int[256];
/*     */ 
/* 164 */   private static final int[][] EII_RANGES = { { 31, 0 }, { 39, 2 }, { 48, 3 }, { 55, 255 }, { 56, 4 }, { 59, 255 }, { 60, 5 }, { 61, 5 }, { 62, 255 }, { 63, 5 }, { 95, 1 }, { 103, 2 }, { 112, 3 }, { 119, 255 }, { 120, 4 }, { 123, 255 }, { 124, 5 }, { 125, 5 }, { 126, 255 }, { 127, 5 }, { 129, 6 }, { 130, 7 }, { 131, 8 }, { 133, 9 }, { 134, 10 }, { 135, 11 }, { 139, 12 }, { 143, 13 }, { 145, 6 }, { 146, 7 }, { 147, 8 }, { 149, 9 }, { 150, 10 }, { 151, 11 }, { 155, 12 }, { 159, 13 }, { 175, 14 }, { 179, 15 }, { 183, 16 }, { 184, 17 }, { 199, 255 }, { 203, 21 }, { 224, 255 }, { 225, 19 }, { 226, 18 }, { 239, 255 }, { 240, 22 }, { 254, 255 }, { 255, 23 } };
/*     */   public static final int AII_INDEX_SMALL = 0;
/*     */   public static final int AII_INDEX_MEDIUM = 1;
/*     */   public static final int AII_INDEX_LARGE = 2;
/*     */   public static final int AII_LITERAL = 3;
/*     */   public static final int AII_TERMINATOR_SINGLE = 4;
/*     */   public static final int AII_TERMINATOR_DOUBLE = 5;
/* 350 */   private static final int[] AII = new int[256];
/*     */ 
/* 352 */   private static final int[][] AII_RANGES = { { 63, 0 }, { 95, 1 }, { 111, 2 }, { 119, 255 }, { 121, 3 }, { 122, 255 }, { 123, 3 }, { 239, 255 }, { 240, 4 }, { 254, 255 }, { 255, 5 } };
/*     */   public static final int NISTRING_UTF8_SMALL_LENGTH = 0;
/*     */   public static final int NISTRING_UTF8_MEDIUM_LENGTH = 1;
/*     */   public static final int NISTRING_UTF8_LARGE_LENGTH = 2;
/*     */   public static final int NISTRING_UTF16_SMALL_LENGTH = 3;
/*     */   public static final int NISTRING_UTF16_MEDIUM_LENGTH = 4;
/*     */   public static final int NISTRING_UTF16_LARGE_LENGTH = 5;
/*     */   public static final int NISTRING_RA = 6;
/*     */   public static final int NISTRING_EA = 7;
/*     */   public static final int NISTRING_INDEX_SMALL = 8;
/*     */   public static final int NISTRING_INDEX_MEDIUM = 9;
/*     */   public static final int NISTRING_INDEX_LARGE = 10;
/*     */   public static final int NISTRING_EMPTY = 11;
/* 405 */   private static final int[] NISTRING = new int[256];
/*     */ 
/* 407 */   private static final int[][] NISTRING_RANGES = { { 7, 0 }, { 8, 1 }, { 11, 255 }, { 12, 2 }, { 15, 255 }, { 23, 3 }, { 24, 4 }, { 27, 255 }, { 28, 5 }, { 31, 255 }, { 47, 6 }, { 63, 7 }, { 71, 0 }, { 72, 1 }, { 75, 255 }, { 76, 2 }, { 79, 255 }, { 87, 3 }, { 88, 4 }, { 91, 255 }, { 92, 5 }, { 95, 255 }, { 111, 6 }, { 127, 7 }, { 191, 8 }, { 223, 9 }, { 239, 10 }, { 254, 255 }, { 255, 11 } };
/*     */   static final int ISTRING_SMALL_LENGTH = 0;
/*     */   static final int ISTRING_MEDIUM_LENGTH = 1;
/*     */   static final int ISTRING_LARGE_LENGTH = 2;
/*     */   static final int ISTRING_INDEX_SMALL = 3;
/*     */   static final int ISTRING_INDEX_MEDIUM = 4;
/*     */   static final int ISTRING_INDEX_LARGE = 5;
/* 522 */   private static final int[] ISTRING = new int[256];
/*     */ 
/* 524 */   private static final int[][] ISTRING_RANGES = { { 63, 0 }, { 64, 1 }, { 95, 255 }, { 96, 2 }, { 127, 255 }, { 191, 3 }, { 223, 4 }, { 239, 5 }, { 255, 255 } };
/*     */   static final int ISTRING_PREFIX_NAMESPACE_LENGTH_3 = 6;
/*     */   static final int ISTRING_PREFIX_NAMESPACE_LENGTH_5 = 7;
/*     */   static final int ISTRING_PREFIX_NAMESPACE_LENGTH_29 = 8;
/*     */   static final int ISTRING_PREFIX_NAMESPACE_LENGTH_36 = 9;
/*     */   static final int ISTRING_PREFIX_NAMESPACE_INDEX_ZERO = 10;
/* 560 */   private static final int[] ISTRING_PREFIX_NAMESPACE = new int[256];
/*     */ 
/* 562 */   private static final int[][] ISTRING_PREFIX_NAMESPACE_RANGES = { { 1, 0 }, { 2, 6 }, { 3, 0 }, { 4, 7 }, { 27, 0 }, { 28, 8 }, { 34, 0 }, { 35, 9 }, { 63, 0 }, { 64, 1 }, { 95, 255 }, { 96, 2 }, { 127, 255 }, { 128, 10 }, { 191, 3 }, { 223, 4 }, { 239, 5 }, { 255, 255 } };
/*     */   static final int UTF8_NCNAME_NCNAME = 0;
/*     */   static final int UTF8_NCNAME_NCNAME_CHAR = 1;
/*     */   static final int UTF8_TWO_BYTES = 2;
/*     */   static final int UTF8_THREE_BYTES = 3;
/*     */   static final int UTF8_FOUR_BYTES = 4;
/* 628 */   private static final int[] UTF8_NCNAME = new int[256];
/*     */ 
/* 630 */   private static final int[][] UTF8_NCNAME_RANGES = { { 44, 255 }, { 46, 1 }, { 47, 255 }, { 57, 1 }, { 64, 255 }, { 90, 0 }, { 94, 255 }, { 95, 0 }, { 96, 255 }, { 122, 0 }, { 127, 255 }, { 193, 255 }, { 223, 2 }, { 239, 3 }, { 247, 4 }, { 255, 255 } };
/*     */   static final int UTF8_ONE_BYTE = 1;
/* 700 */   private static final int[] UTF8 = new int[256];
/*     */ 
/* 702 */   private static final int[][] UTF8_RANGES = { { 8, 255 }, { 10, 1 }, { 12, 255 }, { 13, 1 }, { 31, 255 }, { 127, 1 }, { 193, 255 }, { 223, 2 }, { 239, 3 }, { 247, 4 }, { 255, 255 } };
/*     */ 
/*     */   private static void constructTable(int[] table, int[][] ranges)
/*     */   {
/* 753 */     int start = 0;
/* 754 */     for (int range = 0; range < ranges.length; range++) {
/* 755 */       int end = ranges[range][RANGE_INDEX_END];
/* 756 */       int value = ranges[range][RANGE_INDEX_VALUE];
/* 757 */       for (int i = start; i <= end; i++) {
/* 758 */         table[i] = value;
/*     */       }
/* 760 */       start = end + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final int DII(int index) {
/* 765 */     return DII[index];
/*     */   }
/*     */ 
/*     */   public static final int EII(int index) {
/* 769 */     return EII[index];
/*     */   }
/*     */ 
/*     */   public static final int AII(int index) {
/* 773 */     return AII[index];
/*     */   }
/*     */ 
/*     */   public static final int NISTRING(int index) {
/* 777 */     return NISTRING[index];
/*     */   }
/*     */ 
/*     */   public static final int ISTRING(int index) {
/* 781 */     return ISTRING[index];
/*     */   }
/*     */ 
/*     */   public static final int ISTRING_PREFIX_NAMESPACE(int index) {
/* 785 */     return ISTRING_PREFIX_NAMESPACE[index];
/*     */   }
/*     */ 
/*     */   public static final int UTF8(int index) {
/* 789 */     return UTF8[index];
/*     */   }
/*     */ 
/*     */   public static final int UTF8_NCNAME(int index) {
/* 793 */     return UTF8_NCNAME[index];
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 798 */     constructTable(DII, DII_RANGES);
/*     */ 
/* 801 */     constructTable(EII, EII_RANGES);
/*     */ 
/* 804 */     constructTable(AII, AII_RANGES);
/*     */ 
/* 807 */     constructTable(NISTRING, NISTRING_RANGES);
/*     */ 
/* 810 */     constructTable(ISTRING, ISTRING_RANGES);
/*     */ 
/* 813 */     constructTable(ISTRING_PREFIX_NAMESPACE, ISTRING_PREFIX_NAMESPACE_RANGES);
/*     */ 
/* 816 */     constructTable(UTF8_NCNAME, UTF8_NCNAME_RANGES);
/*     */ 
/* 819 */     constructTable(UTF8, UTF8_RANGES);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.DecoderStateTables
 * JD-Core Version:    0.6.2
 */