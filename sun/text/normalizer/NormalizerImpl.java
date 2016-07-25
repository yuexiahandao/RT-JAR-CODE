/*      */ package sun.text.normalizer;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ 
/*      */ public final class NormalizerImpl
/*      */ {
/*      */   static final NormalizerImpl IMPL;
/*      */   static final int UNSIGNED_BYTE_MASK = 255;
/*      */   static final long UNSIGNED_INT_MASK = 4294967295L;
/*      */   private static final String DATA_FILE_NAME = "/sun/text/resources/unorm.icu";
/*      */   public static final int QC_NFC = 17;
/*      */   public static final int QC_NFKC = 34;
/*      */   public static final int QC_NFD = 4;
/*      */   public static final int QC_NFKD = 8;
/*      */   public static final int QC_ANY_NO = 15;
/*      */   public static final int QC_MAYBE = 16;
/*      */   public static final int QC_ANY_MAYBE = 48;
/*      */   public static final int QC_MASK = 63;
/*      */   private static final int COMBINES_FWD = 64;
/*      */   private static final int COMBINES_BACK = 128;
/*      */   public static final int COMBINES_ANY = 192;
/*      */   private static final int CC_SHIFT = 8;
/*      */   public static final int CC_MASK = 65280;
/*      */   private static final int EXTRA_SHIFT = 16;
/*      */   private static final long MIN_SPECIAL = 4227858432L;
/*      */   private static final long SURROGATES_TOP = 4293918720L;
/*      */   private static final long MIN_HANGUL = 4293918720L;
/*      */   private static final long JAMO_V_TOP = 4294115328L;
/*      */   static final int INDEX_TRIE_SIZE = 0;
/*      */   static final int INDEX_CHAR_COUNT = 1;
/*      */   static final int INDEX_COMBINE_DATA_COUNT = 2;
/*      */   public static final int INDEX_MIN_NFC_NO_MAYBE = 6;
/*      */   public static final int INDEX_MIN_NFKC_NO_MAYBE = 7;
/*      */   public static final int INDEX_MIN_NFD_NO_MAYBE = 8;
/*      */   public static final int INDEX_MIN_NFKD_NO_MAYBE = 9;
/*      */   static final int INDEX_FCD_TRIE_SIZE = 10;
/*      */   static final int INDEX_AUX_TRIE_SIZE = 11;
/*      */   static final int INDEX_TOP = 32;
/*      */   private static final int AUX_UNSAFE_SHIFT = 11;
/*      */   private static final int AUX_COMP_EX_SHIFT = 10;
/*      */   private static final int AUX_NFC_SKIPPABLE_F_SHIFT = 12;
/*      */   private static final int AUX_MAX_FNC = 1024;
/*      */   private static final int AUX_UNSAFE_MASK = 2048;
/*      */   private static final int AUX_FNC_MASK = 1023;
/*      */   private static final int AUX_COMP_EX_MASK = 1024;
/*      */   private static final long AUX_NFC_SKIP_F_MASK = 4096L;
/*      */   private static final int MAX_BUFFER_SIZE = 20;
/*      */   private static FCDTrieImpl fcdTrieImpl;
/*      */   private static NormTrieImpl normTrieImpl;
/*      */   private static AuxTrieImpl auxTrieImpl;
/*      */   private static int[] indexes;
/*      */   private static char[] combiningTable;
/*      */   private static char[] extraData;
/*      */   private static boolean isDataLoaded;
/*      */   private static boolean isFormatVersion_2_1;
/*      */   private static boolean isFormatVersion_2_2;
/*      */   private static byte[] unicodeVersion;
/*      */   private static final int DATA_BUFFER_SIZE = 25000;
/*      */   public static final int MIN_WITH_LEAD_CC = 768;
/*      */   private static final int DECOMP_FLAG_LENGTH_HAS_CC = 128;
/*      */   private static final int DECOMP_LENGTH_MASK = 127;
/*      */   private static final int BMP_INDEX_LENGTH = 2048;
/*      */   private static final int SURROGATE_BLOCK_BITS = 5;
/*      */   public static final int JAMO_L_BASE = 4352;
/*      */   public static final int JAMO_V_BASE = 4449;
/*      */   public static final int JAMO_T_BASE = 4519;
/*      */   public static final int HANGUL_BASE = 44032;
/*      */   public static final int JAMO_L_COUNT = 19;
/*      */   public static final int JAMO_V_COUNT = 21;
/*      */   public static final int JAMO_T_COUNT = 28;
/*      */   public static final int HANGUL_COUNT = 11172;
/*      */   private static final int OPTIONS_NX_MASK = 31;
/*      */   private static final int OPTIONS_UNICODE_MASK = 224;
/*      */   public static final int OPTIONS_SETS_MASK = 255;
/* 2284 */   private static final UnicodeSet[] nxCache = new UnicodeSet[256];
/*      */   private static final int NX_HANGUL = 1;
/*      */   private static final int NX_CJK_COMPAT = 2;
/*      */   public static final int BEFORE_PRI_29 = 256;
/*      */   public static final int OPTIONS_COMPAT = 4096;
/*      */   public static final int OPTIONS_COMPOSE_CONTIGUOUS = 8192;
/*      */   public static final int WITHOUT_CORRIGENDUM4_CORRECTIONS = 262144;
/* 2691 */   private static final char[][] corrigendum4MappingTable = { { 55364, 57194 }, { '弳' }, { '䎫' }, { '窮' }, { '䵗' } };
/*      */ 
/*      */   public static int getFromIndexesArr(int paramInt)
/*      */   {
/*  245 */     return indexes[paramInt];
/*      */   }
/*      */ 
/*      */   private NormalizerImpl()
/*      */     throws IOException
/*      */   {
/*  256 */     if (!isDataLoaded)
/*      */     {
/*  259 */       InputStream localInputStream = ICUData.getRequiredStream("/sun/text/resources/unorm.icu");
/*  260 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(localInputStream, 25000);
/*  261 */       NormalizerDataReader localNormalizerDataReader = new NormalizerDataReader(localBufferedInputStream);
/*      */ 
/*  264 */       indexes = localNormalizerDataReader.readIndexes(32);
/*      */ 
/*  266 */       byte[] arrayOfByte1 = new byte[indexes[0]];
/*      */ 
/*  268 */       int i = indexes[2];
/*  269 */       combiningTable = new char[i];
/*      */ 
/*  271 */       int j = indexes[1];
/*  272 */       extraData = new char[j];
/*      */ 
/*  274 */       byte[] arrayOfByte2 = new byte[indexes[10]];
/*  275 */       byte[] arrayOfByte3 = new byte[indexes[11]];
/*      */ 
/*  277 */       fcdTrieImpl = new FCDTrieImpl();
/*  278 */       normTrieImpl = new NormTrieImpl();
/*  279 */       auxTrieImpl = new AuxTrieImpl();
/*      */ 
/*  282 */       localNormalizerDataReader.read(arrayOfByte1, arrayOfByte2, arrayOfByte3, extraData, combiningTable);
/*      */ 
/*  284 */       NormTrieImpl.normTrie = new IntTrie(new ByteArrayInputStream(arrayOfByte1), normTrieImpl);
/*  285 */       FCDTrieImpl.fcdTrie = new CharTrie(new ByteArrayInputStream(arrayOfByte2), fcdTrieImpl);
/*  286 */       AuxTrieImpl.auxTrie = new CharTrie(new ByteArrayInputStream(arrayOfByte3), auxTrieImpl);
/*      */ 
/*  290 */       isDataLoaded = true;
/*      */ 
/*  293 */       byte[] arrayOfByte4 = localNormalizerDataReader.getDataFormatVersion();
/*      */ 
/*  295 */       isFormatVersion_2_1 = (arrayOfByte4[0] > 2) || ((arrayOfByte4[0] == 2) && (arrayOfByte4[1] >= 1));
/*      */ 
/*  299 */       isFormatVersion_2_2 = (arrayOfByte4[0] > 2) || ((arrayOfByte4[0] == 2) && (arrayOfByte4[1] >= 2));
/*      */ 
/*  303 */       unicodeVersion = localNormalizerDataReader.getUnicodeVersion();
/*  304 */       localBufferedInputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isHangulWithoutJamoT(char paramChar)
/*      */   {
/*  324 */     paramChar = (char)(paramChar - 44032);
/*  325 */     return (paramChar < '⮤') && (paramChar % '\034' == 0);
/*      */   }
/*      */ 
/*      */   private static boolean isNorm32Regular(long paramLong)
/*      */   {
/*  332 */     return paramLong < 4227858432L;
/*      */   }
/*      */ 
/*      */   private static boolean isNorm32LeadSurrogate(long paramLong)
/*      */   {
/*  337 */     return (4227858432L <= paramLong) && (paramLong < 4293918720L);
/*      */   }
/*      */ 
/*      */   private static boolean isNorm32HangulOrJamo(long paramLong)
/*      */   {
/*  342 */     return paramLong >= 4293918720L;
/*      */   }
/*      */ 
/*      */   private static boolean isJamoVTNorm32JamoV(long paramLong)
/*      */   {
/*  350 */     return paramLong < 4294115328L;
/*      */   }
/*      */ 
/*      */   public static long getNorm32(char paramChar)
/*      */   {
/*  356 */     return 0xFFFFFFFF & NormTrieImpl.normTrie.getLeadValue(paramChar);
/*      */   }
/*      */ 
/*      */   public static long getNorm32FromSurrogatePair(long paramLong, char paramChar)
/*      */   {
/*  365 */     return 0xFFFFFFFF & NormTrieImpl.normTrie.getTrailValue((int)paramLong, paramChar);
/*      */   }
/*      */ 
/*      */   private static long getNorm32(int paramInt)
/*      */   {
/*  370 */     return 0xFFFFFFFF & NormTrieImpl.normTrie.getCodePointValue(paramInt);
/*      */   }
/*      */ 
/*      */   private static long getNorm32(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  379 */     long l = getNorm32(paramArrayOfChar[paramInt1]);
/*  380 */     if (((l & paramInt2) > 0L) && (isNorm32LeadSurrogate(l)))
/*      */     {
/*  382 */       l = getNorm32FromSurrogatePair(l, paramArrayOfChar[(paramInt1 + 1)]);
/*      */     }
/*  384 */     return l;
/*      */   }
/*      */ 
/*      */   public static VersionInfo getUnicodeVersion()
/*      */   {
/*  389 */     return VersionInfo.getInstance(unicodeVersion[0], unicodeVersion[1], unicodeVersion[2], unicodeVersion[3]);
/*      */   }
/*      */ 
/*      */   public static char getFCD16(char paramChar)
/*      */   {
/*  394 */     return FCDTrieImpl.fcdTrie.getLeadValue(paramChar);
/*      */   }
/*      */ 
/*      */   public static char getFCD16FromSurrogatePair(char paramChar1, char paramChar2)
/*      */   {
/*  401 */     return FCDTrieImpl.fcdTrie.getTrailValue(paramChar1, paramChar2);
/*      */   }
/*      */   public static int getFCD16(int paramInt) {
/*  404 */     return FCDTrieImpl.fcdTrie.getCodePointValue(paramInt);
/*      */   }
/*      */ 
/*      */   private static int getExtraDataIndex(long paramLong) {
/*  408 */     return (int)(paramLong >> 16);
/*      */   }
/*      */ 
/*      */   private static int decompose(long paramLong, int paramInt, DecomposeArgs paramDecomposeArgs)
/*      */   {
/*  425 */     int i = getExtraDataIndex(paramLong);
/*  426 */     paramDecomposeArgs.length = extraData[(i++)];
/*      */ 
/*  428 */     if (((paramLong & paramInt & 0x8) != 0L) && (paramDecomposeArgs.length >= 256))
/*      */     {
/*  430 */       i += (paramDecomposeArgs.length >> 7 & 0x1) + (paramDecomposeArgs.length & 0x7F);
/*  431 */       paramDecomposeArgs.length >>= 8;
/*      */     }
/*      */ 
/*  434 */     if ((paramDecomposeArgs.length & 0x80) > 0)
/*      */     {
/*  436 */       int j = extraData[(i++)];
/*  437 */       paramDecomposeArgs.cc = (0xFF & j >> 8);
/*  438 */       paramDecomposeArgs.trailCC = (0xFF & j);
/*      */     }
/*      */     else {
/*  441 */       paramDecomposeArgs.cc = (paramDecomposeArgs.trailCC = 0);
/*      */     }
/*      */ 
/*  444 */     paramDecomposeArgs.length &= 127;
/*  445 */     return i;
/*      */   }
/*      */ 
/*      */   private static int decompose(long paramLong, DecomposeArgs paramDecomposeArgs)
/*      */   {
/*  456 */     int i = getExtraDataIndex(paramLong);
/*  457 */     paramDecomposeArgs.length = extraData[(i++)];
/*      */ 
/*  459 */     if ((paramDecomposeArgs.length & 0x80) > 0)
/*      */     {
/*  461 */       int j = extraData[(i++)];
/*  462 */       paramDecomposeArgs.cc = (0xFF & j >> 8);
/*  463 */       paramDecomposeArgs.trailCC = (0xFF & j);
/*      */     }
/*      */     else {
/*  466 */       paramDecomposeArgs.cc = (paramDecomposeArgs.trailCC = 0);
/*      */     }
/*      */ 
/*  469 */     paramDecomposeArgs.length &= 127;
/*  470 */     return i;
/*      */   }
/*      */ 
/*      */   private static int getNextCC(NextCCArgs paramNextCCArgs)
/*      */   {
/*  490 */     paramNextCCArgs.c = paramNextCCArgs.source[(paramNextCCArgs.next++)];
/*      */ 
/*  492 */     long l = getNorm32(paramNextCCArgs.c);
/*  493 */     if ((l & 0xFF00) == 0L) {
/*  494 */       paramNextCCArgs.c2 = '\000';
/*  495 */       return 0;
/*      */     }
/*  497 */     if (!isNorm32LeadSurrogate(l)) {
/*  498 */       paramNextCCArgs.c2 = '\000';
/*      */     }
/*  501 */     else if ((paramNextCCArgs.next != paramNextCCArgs.limit) && (UTF16.isTrailSurrogate(paramNextCCArgs.c2 = paramNextCCArgs.source[paramNextCCArgs.next])))
/*      */     {
/*  503 */       paramNextCCArgs.next += 1;
/*  504 */       l = getNorm32FromSurrogatePair(l, paramNextCCArgs.c2);
/*      */     } else {
/*  506 */       paramNextCCArgs.c2 = '\000';
/*  507 */       return 0;
/*      */     }
/*      */ 
/*  511 */     return (int)(0xFF & l >> 8);
/*      */   }
/*      */ 
/*      */   private static long getPrevNorm32(PrevArgs paramPrevArgs, int paramInt1, int paramInt2)
/*      */   {
/*  534 */     paramPrevArgs.c = paramPrevArgs.src[(--paramPrevArgs.current)];
/*  535 */     paramPrevArgs.c2 = '\000';
/*      */ 
/*  540 */     if (paramPrevArgs.c < paramInt1)
/*  541 */       return 0L;
/*  542 */     if (!UTF16.isSurrogate(paramPrevArgs.c))
/*  543 */       return getNorm32(paramPrevArgs.c);
/*  544 */     if (UTF16.isLeadSurrogate(paramPrevArgs.c))
/*      */     {
/*  546 */       return 0L;
/*  547 */     }if ((paramPrevArgs.current != paramPrevArgs.start) && (UTF16.isLeadSurrogate(paramPrevArgs.c2 = paramPrevArgs.src[(paramPrevArgs.current - 1)])))
/*      */     {
/*  549 */       paramPrevArgs.current -= 1;
/*  550 */       long l = getNorm32(paramPrevArgs.c2);
/*      */ 
/*  552 */       if ((l & paramInt2) == 0L)
/*      */       {
/*  556 */         return 0L;
/*      */       }
/*      */ 
/*  559 */       return getNorm32FromSurrogatePair(l, paramPrevArgs.c);
/*      */     }
/*      */ 
/*  563 */     paramPrevArgs.c2 = '\000';
/*  564 */     return 0L;
/*      */   }
/*      */ 
/*      */   private static int getPrevCC(PrevArgs paramPrevArgs)
/*      */   {
/*  574 */     return (int)(0xFF & getPrevNorm32(paramPrevArgs, 768, 65280) >> 8);
/*      */   }
/*      */ 
/*      */   public static boolean isNFDSafe(long paramLong, int paramInt1, int paramInt2)
/*      */   {
/*  585 */     if ((paramLong & paramInt1) == 0L) {
/*  586 */       return true;
/*      */     }
/*      */ 
/*  590 */     if ((isNorm32Regular(paramLong)) && ((paramLong & paramInt2) != 0L)) {
/*  591 */       DecomposeArgs localDecomposeArgs = new DecomposeArgs(null);
/*      */ 
/*  593 */       decompose(paramLong, paramInt2, localDecomposeArgs);
/*  594 */       return localDecomposeArgs.cc == 0;
/*      */     }
/*      */ 
/*  597 */     return (paramLong & 0xFF00) == 0L;
/*      */   }
/*      */ 
/*      */   public static boolean isTrueStarter(long paramLong, int paramInt1, int paramInt2)
/*      */   {
/*  608 */     if ((paramLong & paramInt1) == 0L) {
/*  609 */       return true;
/*      */     }
/*      */ 
/*  613 */     if ((paramLong & paramInt2) != 0L)
/*      */     {
/*  615 */       DecomposeArgs localDecomposeArgs = new DecomposeArgs(null);
/*      */ 
/*  617 */       int i = decompose(paramLong, paramInt2, localDecomposeArgs);
/*      */ 
/*  619 */       if (localDecomposeArgs.cc == 0) {
/*  620 */         int j = paramInt1 & 0x3F;
/*      */ 
/*  623 */         if ((getNorm32(extraData, i, j) & j) == 0L)
/*      */         {
/*  625 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  629 */     return false;
/*      */   }
/*      */ 
/*      */   private static int insertOrdered(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, char paramChar1, char paramChar2, int paramInt4)
/*      */   {
/*  656 */     int n = paramInt4;
/*      */ 
/*  658 */     if ((paramInt1 < paramInt2) && (paramInt4 != 0))
/*      */     {
/*      */       int i;
/*  660 */       int j = i = paramInt2;
/*  661 */       PrevArgs localPrevArgs = new PrevArgs(null);
/*  662 */       localPrevArgs.current = paramInt2;
/*  663 */       localPrevArgs.start = paramInt1;
/*  664 */       localPrevArgs.src = paramArrayOfChar;
/*      */ 
/*  666 */       int m = getPrevCC(localPrevArgs);
/*  667 */       j = localPrevArgs.current;
/*      */ 
/*  669 */       if (paramInt4 < m)
/*      */       {
/*  671 */         n = m;
/*  672 */         i = j;
/*  673 */         while (paramInt1 < j) {
/*  674 */           m = getPrevCC(localPrevArgs);
/*  675 */           j = localPrevArgs.current;
/*  676 */           if (paramInt4 >= m) {
/*      */             break;
/*      */           }
/*  679 */           i = j;
/*      */         }
/*      */ 
/*  690 */         int k = paramInt3;
/*      */         do
/*  692 */           paramArrayOfChar[(--k)] = paramArrayOfChar[(--paramInt2)];
/*  693 */         while (i != paramInt2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  698 */     paramArrayOfChar[paramInt2] = paramChar1;
/*  699 */     if (paramChar2 != 0) {
/*  700 */       paramArrayOfChar[(paramInt2 + 1)] = paramChar2;
/*      */     }
/*      */ 
/*  704 */     return n;
/*      */   }
/*      */ 
/*      */   private static int mergeOrdered(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  738 */     int k = 0;
/*      */ 
/*  741 */     int m = paramInt2 == paramInt3 ? 1 : 0;
/*  742 */     NextCCArgs localNextCCArgs = new NextCCArgs(null);
/*  743 */     localNextCCArgs.source = paramArrayOfChar2;
/*  744 */     localNextCCArgs.next = paramInt3;
/*  745 */     localNextCCArgs.limit = paramInt4;
/*      */ 
/*  747 */     if ((paramInt1 != paramInt2) || (!paramBoolean))
/*      */     {
/*  749 */       while (localNextCCArgs.next < localNextCCArgs.limit) {
/*  750 */         int j = getNextCC(localNextCCArgs);
/*  751 */         if (j == 0)
/*      */         {
/*  753 */           k = 0;
/*  754 */           if (m != 0) {
/*  755 */             paramInt2 = localNextCCArgs.next;
/*      */           } else {
/*  757 */             paramArrayOfChar2[(paramInt2++)] = localNextCCArgs.c;
/*  758 */             if (localNextCCArgs.c2 != 0) {
/*  759 */               paramArrayOfChar2[(paramInt2++)] = localNextCCArgs.c2;
/*      */             }
/*      */           }
/*  762 */           if (paramBoolean) {
/*      */             break;
/*      */           }
/*  765 */           paramInt1 = paramInt2;
/*      */         }
/*      */         else {
/*  768 */           int i = paramInt2 + (localNextCCArgs.c2 == 0 ? 1 : 2);
/*  769 */           k = insertOrdered(paramArrayOfChar1, paramInt1, paramInt2, i, localNextCCArgs.c, localNextCCArgs.c2, j);
/*      */ 
/*  771 */           paramInt2 = i;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  776 */     if (localNextCCArgs.next == localNextCCArgs.limit)
/*      */     {
/*  778 */       return k;
/*      */     }
/*  780 */     if (m == 0)
/*      */     {
/*      */       do
/*  783 */         paramArrayOfChar1[(paramInt2++)] = paramArrayOfChar2[(localNextCCArgs.next++)];
/*  784 */       while (localNextCCArgs.next != localNextCCArgs.limit);
/*  785 */       localNextCCArgs.limit = paramInt2;
/*      */     }
/*  787 */     PrevArgs localPrevArgs = new PrevArgs(null);
/*  788 */     localPrevArgs.src = paramArrayOfChar2;
/*  789 */     localPrevArgs.start = paramInt1;
/*  790 */     localPrevArgs.current = localNextCCArgs.limit;
/*  791 */     return getPrevCC(localPrevArgs);
/*      */   }
/*      */ 
/*      */   private static int mergeOrdered(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4)
/*      */   {
/*  801 */     return mergeOrdered(paramArrayOfChar1, paramInt1, paramInt2, paramArrayOfChar2, paramInt3, paramInt4, true);
/*      */   }
/*      */ 
/*      */   public static NormalizerBase.QuickCheckResult quickCheck(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, UnicodeSet paramUnicodeSet)
/*      */   {
/*  819 */     ComposePartArgs localComposePartArgs = new ComposePartArgs(null);
/*      */ 
/*  821 */     int m = paramInt1;
/*      */ 
/*  823 */     if (!isDataLoaded) {
/*  824 */       return NormalizerBase.MAYBE;
/*      */     }
/*      */ 
/*  827 */     int i = 0xFF00 | paramInt4;
/*  828 */     NormalizerBase.QuickCheckResult localQuickCheckResult = NormalizerBase.YES;
/*  829 */     int k = 0;
/*      */     while (true)
/*      */     {
/*  833 */       if (paramInt1 == paramInt2)
/*  834 */         return localQuickCheckResult;
/*      */       char c1;
/*      */       long l1;
/*  835 */       if (((c1 = paramArrayOfChar[(paramInt1++)]) < paramInt3) || (((l1 = getNorm32(c1)) & i) == 0L))
/*      */       {
/*  839 */         k = 0;
/*      */       }
/*      */       else
/*      */       {
/*      */         char c2;
/*  844 */         if (isNorm32LeadSurrogate(l1))
/*      */         {
/*  846 */           if ((paramInt1 != paramInt2) && (UTF16.isTrailSurrogate(c2 = paramArrayOfChar[paramInt1]))) {
/*  847 */             paramInt1++;
/*  848 */             l1 = getNorm32FromSurrogatePair(l1, c2);
/*      */           } else {
/*  850 */             l1 = 0L;
/*  851 */             c2 = '\000';
/*      */           }
/*      */         }
/*  854 */         else c2 = '\000';
/*      */ 
/*  856 */         if (nx_contains(paramUnicodeSet, c1, c2))
/*      */         {
/*  858 */           l1 = 0L;
/*      */         }
/*      */ 
/*  862 */         int j = (char)(int)(l1 >> 8 & 0xFF);
/*  863 */         if ((j != 0) && (j < k)) {
/*  864 */           return NormalizerBase.NO;
/*      */         }
/*  866 */         k = j;
/*      */ 
/*  869 */         long l2 = l1 & paramInt4;
/*  870 */         if ((l2 & 0xF) >= 1L) {
/*  871 */           localQuickCheckResult = NormalizerBase.NO;
/*  872 */           break;
/*  873 */         }if (l2 != 0L)
/*      */         {
/*  875 */           if (paramBoolean) {
/*  876 */             localQuickCheckResult = NormalizerBase.MAYBE;
/*      */           }
/*      */           else
/*      */           {
/*  883 */             int i1 = paramInt4 << 2 & 0xF;
/*      */ 
/*  888 */             int n = paramInt1 - 1;
/*  889 */             if (UTF16.isTrailSurrogate(paramArrayOfChar[n]))
/*      */             {
/*  892 */               n--;
/*      */             }
/*  894 */             n = findPreviousStarter(paramArrayOfChar, m, n, i, i1, (char)paramInt3);
/*      */ 
/*  900 */             paramInt1 = findNextStarter(paramArrayOfChar, paramInt1, paramInt2, paramInt4, i1, (char)paramInt3);
/*      */ 
/*  904 */             localComposePartArgs.prevCC = k;
/*      */ 
/*  907 */             char[] arrayOfChar = composePart(localComposePartArgs, n, paramArrayOfChar, paramInt1, paramInt2, paramInt5, paramUnicodeSet);
/*      */ 
/*  910 */             if (0 != strCompare(arrayOfChar, 0, localComposePartArgs.length, paramArrayOfChar, n, paramInt1, false)) {
/*  911 */               localQuickCheckResult = NormalizerBase.NO;
/*  912 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  919 */     return localQuickCheckResult;
/*      */   }
/*      */ 
/*      */   public static int decompose(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, boolean paramBoolean, int[] paramArrayOfInt, UnicodeSet paramUnicodeSet)
/*      */   {
/*  932 */     char[] arrayOfChar1 = new char[3];
/*      */ 
/*  941 */     int i8 = paramInt3;
/*  942 */     int i9 = paramInt1;
/*      */     int i3;
/*      */     int k;
/*  943 */     if (!paramBoolean) {
/*  944 */       i3 = (char)indexes[8];
/*  945 */       k = 4;
/*      */     } else {
/*  947 */       i3 = (char)indexes[9];
/*  948 */       k = 8;
/*      */     }
/*      */ 
/*  952 */     int j = 0xFF00 | k;
/*  953 */     int m = 0;
/*  954 */     int i5 = 0;
/*  955 */     long l = 0L;
/*  956 */     int i1 = 0;
/*  957 */     int i7 = 0;
/*      */     int i6;
/*  959 */     int i4 = i6 = -1;
/*      */     while (true)
/*      */     {
/*  965 */       int i = i9;
/*      */ 
/*  967 */       while ((i9 != paramInt2) && (((i1 = paramArrayOfChar1[i9]) < i3) || (((l = getNorm32(i1)) & j) == 0L)))
/*      */       {
/*  969 */         i5 = 0;
/*  970 */         i9++;
/*      */       }
/*      */       int n;
/*  974 */       if (i9 != i) {
/*  975 */         n = i9 - i;
/*  976 */         if (i8 + n <= paramInt4) {
/*  977 */           System.arraycopy(paramArrayOfChar1, i, paramArrayOfChar2, i8, n);
/*      */         }
/*      */ 
/*  980 */         i8 += n;
/*  981 */         m = i8;
/*      */       }
/*      */ 
/*  985 */       if (i9 == paramInt2)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  990 */       i9++;
/*      */       int i2;
/*      */       char[] arrayOfChar2;
/*      */       char c1;
/*      */       char c2;
/* 1009 */       if (isNorm32HangulOrJamo(l)) {
/* 1010 */         if (nx_contains(paramUnicodeSet, i1)) {
/* 1011 */           i2 = 0;
/* 1012 */           arrayOfChar2 = null;
/* 1013 */           n = 1;
/*      */         }
/*      */         else {
/* 1016 */           arrayOfChar2 = arrayOfChar1;
/* 1017 */           i7 = 0;
/* 1018 */           i4 = i6 = 0;
/*      */ 
/* 1020 */           c1 = (char)(i1 - 44032);
/*      */ 
/* 1022 */           i2 = (char)(c1 % '\034');
/* 1023 */           c1 = (char)(c1 / '\034');
/* 1024 */           if (i2 > 0) {
/* 1025 */             arrayOfChar1[2] = ((char)(4519 + i2));
/* 1026 */             n = 3;
/*      */           } else {
/* 1028 */             n = 2;
/*      */           }
/*      */ 
/* 1031 */           arrayOfChar1[1] = ((char)(4449 + c1 % '\025'));
/* 1032 */           arrayOfChar1[0] = ((char)(4352 + c1 / '\025'));
/*      */         }
/*      */       } else {
/* 1035 */         if (isNorm32Regular(l)) {
/* 1036 */           i2 = 0;
/* 1037 */           n = 1;
/*      */         }
/* 1040 */         else if ((i9 != paramInt2) && (UTF16.isTrailSurrogate(i2 = paramArrayOfChar1[i9])))
/*      */         {
/* 1042 */           i9++;
/* 1043 */           n = 2;
/* 1044 */           l = getNorm32FromSurrogatePair(l, i2);
/*      */         } else {
/* 1046 */           c2 = '\000';
/* 1047 */           n = 1;
/* 1048 */           l = 0L;
/*      */         }
/*      */ 
/* 1053 */         if (nx_contains(paramUnicodeSet, c1, c2))
/*      */         {
/* 1055 */           i4 = i6 = 0;
/* 1056 */           arrayOfChar2 = null;
/* 1057 */         } else if ((l & k) == 0L)
/*      */         {
/* 1059 */           i4 = i6 = (int)(0xFF & l >> 8);
/* 1060 */           arrayOfChar2 = null;
/* 1061 */           i7 = -1;
/*      */         } else {
/* 1063 */           DecomposeArgs localDecomposeArgs = new DecomposeArgs(null);
/*      */ 
/* 1067 */           i7 = decompose(l, k, localDecomposeArgs);
/* 1068 */           arrayOfChar2 = extraData;
/* 1069 */           n = localDecomposeArgs.length;
/* 1070 */           i4 = localDecomposeArgs.cc;
/* 1071 */           i6 = localDecomposeArgs.trailCC;
/* 1072 */           if (n == 1)
/*      */           {
/* 1074 */             c1 = arrayOfChar2[i7];
/* 1075 */             c2 = '\000';
/* 1076 */             arrayOfChar2 = null;
/* 1077 */             i7 = -1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1085 */       if (i8 + n <= paramInt4) {
/* 1086 */         int i10 = i8;
/* 1087 */         if (arrayOfChar2 == null)
/*      */         {
/* 1089 */           if ((i4 != 0) && (i4 < i5))
/*      */           {
/* 1093 */             i8 += n;
/* 1094 */             i6 = insertOrdered(paramArrayOfChar2, m, i10, i8, c1, c2, i4);
/*      */           }
/*      */           else
/*      */           {
/* 1098 */             paramArrayOfChar2[(i8++)] = c1;
/* 1099 */             if (c2 != 0) {
/* 1100 */               paramArrayOfChar2[(i8++)] = c2;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/* 1107 */         else if ((i4 != 0) && (i4 < i5))
/*      */         {
/* 1111 */           i8 += n;
/* 1112 */           i6 = mergeOrdered(paramArrayOfChar2, m, i10, arrayOfChar2, i7, i7 + n);
/*      */         }
/*      */         else
/*      */         {
/*      */           do {
/* 1117 */             paramArrayOfChar2[(i8++)] = arrayOfChar2[(i7++)];
/* 1118 */             n--; } while (n > 0);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1124 */         i8 += n;
/*      */       }
/*      */ 
/* 1127 */       i5 = i6;
/* 1128 */       if (i5 == 0) {
/* 1129 */         m = i8;
/*      */       }
/*      */     }
/*      */ 
/* 1133 */     paramArrayOfInt[0] = i5;
/*      */ 
/* 1135 */     return i8 - paramInt3;
/*      */   }
/*      */ 
/*      */   private static int getNextCombining(NextCombiningArgs paramNextCombiningArgs, int paramInt, UnicodeSet paramUnicodeSet)
/*      */   {
/* 1156 */     paramNextCombiningArgs.c = paramNextCombiningArgs.source[(paramNextCombiningArgs.start++)];
/* 1157 */     long l = getNorm32(paramNextCombiningArgs.c);
/*      */ 
/* 1160 */     paramNextCombiningArgs.c2 = '\000';
/* 1161 */     paramNextCombiningArgs.combiningIndex = 0;
/* 1162 */     paramNextCombiningArgs.cc = '\000';
/*      */ 
/* 1164 */     if ((l & 0xFFC0) == 0L) {
/* 1165 */       return 0;
/*      */     }
/* 1167 */     if (!isNorm32Regular(l))
/*      */     {
/* 1169 */       if (isNorm32HangulOrJamo(l))
/*      */       {
/* 1171 */         paramNextCombiningArgs.combiningIndex = ((int)(0xFFFFFFFF & (0xFFF0 | l >> 16)));
/*      */ 
/* 1173 */         return (int)(l & 0xC0);
/*      */       }
/*      */ 
/* 1176 */       if ((paramNextCombiningArgs.start != paramInt) && (UTF16.isTrailSurrogate(paramNextCombiningArgs.c2 = paramNextCombiningArgs.source[paramNextCombiningArgs.start])))
/*      */       {
/* 1178 */         paramNextCombiningArgs.start += 1;
/* 1179 */         l = getNorm32FromSurrogatePair(l, paramNextCombiningArgs.c2);
/*      */       } else {
/* 1181 */         paramNextCombiningArgs.c2 = '\000';
/* 1182 */         return 0;
/*      */       }
/*      */     }
/*      */ 
/* 1186 */     if (nx_contains(paramUnicodeSet, paramNextCombiningArgs.c, paramNextCombiningArgs.c2)) {
/* 1187 */       return 0;
/*      */     }
/*      */ 
/* 1190 */     paramNextCombiningArgs.cc = ((char)(int)(l >> 8 & 0xFF));
/*      */ 
/* 1192 */     int i = (int)(l & 0xC0);
/* 1193 */     if (i != 0) {
/* 1194 */       int j = getExtraDataIndex(l);
/* 1195 */       paramNextCombiningArgs.combiningIndex = (j > 0 ? extraData[(j - 1)] : 0);
/*      */     }
/*      */ 
/* 1198 */     return i;
/*      */   }
/*      */ 
/*      */   private static int getCombiningIndexFromStarter(char paramChar1, char paramChar2)
/*      */   {
/* 1213 */     long l = getNorm32(paramChar1);
/* 1214 */     if (paramChar2 != 0) {
/* 1215 */       l = getNorm32FromSurrogatePair(l, paramChar2);
/*      */     }
/* 1217 */     return extraData[(getExtraDataIndex(l) - 1)];
/*      */   }
/*      */ 
/*      */   private static int combine(char[] paramArrayOfChar, int paramInt1, int paramInt2, int[] paramArrayOfInt)
/*      */   {
/* 1243 */     if (paramArrayOfInt.length < 2) {
/* 1244 */       throw new IllegalArgumentException();
/*      */     }
/*      */     int i;
/*      */     while (true)
/*      */     {
/* 1249 */       i = paramArrayOfChar[(paramInt1++)];
/* 1250 */       if (i >= paramInt2) {
/*      */         break;
/*      */       }
/* 1253 */       paramInt1 += ((paramArrayOfChar[paramInt1] & 0x8000) != 0 ? 2 : 1);
/*      */     }
/*      */ 
/* 1257 */     if ((i & 0x7FFF) == paramInt2)
/*      */     {
/* 1259 */       int j = paramArrayOfChar[paramInt1];
/*      */ 
/* 1262 */       i = (int)(0xFFFFFFFF & (j & 0x2000) + 1);
/*      */       int k;
/* 1267 */       if ((j & 0x8000) != 0) {
/* 1268 */         if ((j & 0x4000) != 0)
/*      */         {
/* 1270 */           j = (int)(0xFFFFFFFF & (j & 0x3FF | 0xD800));
/* 1271 */           k = paramArrayOfChar[(paramInt1 + 1)];
/*      */         }
/*      */         else {
/* 1274 */           j = paramArrayOfChar[(paramInt1 + 1)];
/* 1275 */           k = 0;
/*      */         }
/*      */       }
/*      */       else {
/* 1279 */         j &= 8191;
/* 1280 */         k = 0;
/*      */       }
/* 1282 */       paramArrayOfInt[0] = j;
/* 1283 */       paramArrayOfInt[1] = k;
/* 1284 */       return i;
/*      */     }
/*      */ 
/* 1287 */     return 0;
/*      */   }
/*      */ 
/*      */   private static char recompose(RecomposeArgs paramRecomposeArgs, int paramInt, UnicodeSet paramUnicodeSet)
/*      */   {
/* 1315 */     int i3 = 0; int i4 = 0;
/*      */ 
/* 1319 */     int[] arrayOfInt = new int[2];
/* 1320 */     int i7 = -1;
/* 1321 */     int n = 0;
/* 1322 */     int i6 = 0;
/* 1323 */     int i5 = 0;
/*      */ 
/* 1325 */     NextCombiningArgs localNextCombiningArgs = new NextCombiningArgs(null);
/* 1326 */     localNextCombiningArgs.source = paramRecomposeArgs.source;
/*      */ 
/* 1328 */     localNextCombiningArgs.cc = '\000';
/* 1329 */     localNextCombiningArgs.c2 = '\000';
/*      */     while (true)
/*      */     {
/* 1332 */       localNextCombiningArgs.start = paramRecomposeArgs.start;
/* 1333 */       int m = getNextCombining(localNextCombiningArgs, paramRecomposeArgs.limit, paramUnicodeSet);
/* 1334 */       int i1 = localNextCombiningArgs.combiningIndex;
/* 1335 */       paramRecomposeArgs.start = localNextCombiningArgs.start;
/*      */ 
/* 1337 */       if (((m & 0x80) != 0) && (i7 != -1))
/*      */       {
/*      */         int i;
/*      */         int j;
/*      */         int k;
/* 1338 */         if ((i1 & 0x8000) != 0)
/*      */         {
/* 1343 */           if (((paramInt & 0x100) != 0) || (i5 == 0)) {
/* 1344 */             i = -1;
/* 1345 */             m = 0;
/* 1346 */             localNextCombiningArgs.c2 = paramRecomposeArgs.source[i7];
/* 1347 */             if (i1 == 65522)
/*      */             {
/* 1351 */               localNextCombiningArgs.c2 = ((char)(localNextCombiningArgs.c2 - 'ᄀ'));
/* 1352 */               if (localNextCombiningArgs.c2 < '\023') {
/* 1353 */                 i = paramRecomposeArgs.start - 1;
/* 1354 */                 localNextCombiningArgs.c = ((char)(44032 + (localNextCombiningArgs.c2 * '\025' + (localNextCombiningArgs.c - 'ᅡ')) * 28));
/*      */ 
/* 1356 */                 if ((paramRecomposeArgs.start != paramRecomposeArgs.limit) && ((localNextCombiningArgs.c2 = (char)(paramRecomposeArgs.source[paramRecomposeArgs.start] - 'ᆧ')) < '\034'))
/*      */                 {
/* 1359 */                   paramRecomposeArgs.start += 1;
/*      */                   NextCombiningArgs tmp261_259 = localNextCombiningArgs; tmp261_259.c = ((char)(tmp261_259.c + localNextCombiningArgs.c2));
/*      */                 }
/*      */                 else {
/* 1363 */                   m = 64;
/*      */                 }
/* 1365 */                 if (!nx_contains(paramUnicodeSet, localNextCombiningArgs.c)) {
/* 1366 */                   paramRecomposeArgs.source[i7] = localNextCombiningArgs.c;
/*      */                 }
/*      */                 else {
/* 1369 */                   if (!isHangulWithoutJamoT(localNextCombiningArgs.c)) {
/* 1370 */                     paramRecomposeArgs.start -= 1;
/*      */                   }
/*      */ 
/* 1373 */                   i = paramRecomposeArgs.start;
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/*      */             }
/* 1388 */             else if (isHangulWithoutJamoT(localNextCombiningArgs.c2))
/*      */             {
/*      */               NextCombiningArgs tmp351_349 = localNextCombiningArgs; tmp351_349.c2 = ((char)(tmp351_349.c2 + (localNextCombiningArgs.c - 'ᆧ')));
/* 1390 */               if (!nx_contains(paramUnicodeSet, localNextCombiningArgs.c2)) {
/* 1391 */                 i = paramRecomposeArgs.start - 1;
/* 1392 */                 paramRecomposeArgs.source[i7] = localNextCombiningArgs.c2;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 1397 */             if (i != -1)
/*      */             {
/* 1399 */               j = i;
/* 1400 */               k = paramRecomposeArgs.start;
/* 1401 */               while (k < paramRecomposeArgs.limit) {
/* 1402 */                 paramRecomposeArgs.source[(j++)] = paramRecomposeArgs.source[(k++)];
/*      */               }
/* 1404 */               paramRecomposeArgs.start = i;
/* 1405 */               paramRecomposeArgs.limit = j;
/*      */             }
/*      */ 
/* 1408 */             localNextCombiningArgs.c2 = '\000';
/*      */ 
/* 1410 */             if (m != 0)
/*      */             {
/* 1417 */               if (paramRecomposeArgs.start == paramRecomposeArgs.limit) {
/* 1418 */                 return (char)i5;
/*      */               }
/*      */ 
/* 1422 */               n = 65520;
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           int i2;
/* 1438 */           if (((n & 0x8000) == 0) && ((paramInt & 0x100) != 0 ? (i5 != localNextCombiningArgs.cc) || (i5 == 0) : (i5 < localNextCombiningArgs.cc) || (i5 == 0)) && (0 != (i2 = combine(combiningTable, n, i1, arrayOfInt))) && (!nx_contains(paramUnicodeSet, (char)i3, (char)i4)))
/*      */           {
/* 1451 */             i3 = arrayOfInt[0];
/* 1452 */             i4 = arrayOfInt[1];
/*      */ 
/* 1456 */             i = localNextCombiningArgs.c2 == 0 ? paramRecomposeArgs.start - 1 : paramRecomposeArgs.start - 2;
/*      */ 
/* 1459 */             paramRecomposeArgs.source[i7] = ((char)i3);
/* 1460 */             if (i6 != 0) {
/* 1461 */               if (i4 != 0)
/*      */               {
/* 1463 */                 paramRecomposeArgs.source[(i7 + 1)] = ((char)i4);
/*      */               }
/*      */               else
/*      */               {
/* 1467 */                 i6 = 0;
/* 1468 */                 j = i7 + 1;
/* 1469 */                 k = j + 1;
/* 1470 */                 while (k < i) {
/* 1471 */                   paramRecomposeArgs.source[(j++)] = paramRecomposeArgs.source[(k++)];
/*      */                 }
/* 1473 */                 i--;
/*      */               }
/* 1475 */             } else if (i4 != 0) {
/* 1476 */               i6 = 1;
/* 1477 */               paramRecomposeArgs.source[(i7 + 1)] = ((char)i4);
/*      */             }
/*      */ 
/* 1483 */             if (i < paramRecomposeArgs.start) {
/* 1484 */               j = i;
/* 1485 */               k = paramRecomposeArgs.start;
/* 1486 */               while (k < paramRecomposeArgs.limit) {
/* 1487 */                 paramRecomposeArgs.source[(j++)] = paramRecomposeArgs.source[(k++)];
/*      */               }
/* 1489 */               paramRecomposeArgs.start = i;
/* 1490 */               paramRecomposeArgs.limit = j;
/*      */             }
/*      */ 
/* 1496 */             if (paramRecomposeArgs.start == paramRecomposeArgs.limit) {
/* 1497 */               return (char)i5;
/*      */             }
/*      */ 
/* 1501 */             if (i2 > 1) {
/* 1502 */               n = getCombiningIndexFromStarter((char)i3, (char)i4); continue;
/*      */             }
/*      */ 
/* 1505 */             i7 = -1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1514 */         i5 = localNextCombiningArgs.cc;
/* 1515 */         if (paramRecomposeArgs.start == paramRecomposeArgs.limit) {
/* 1516 */           return (char)i5;
/*      */         }
/*      */ 
/* 1520 */         if (localNextCombiningArgs.cc == 0)
/*      */         {
/* 1522 */           if ((m & 0x40) != 0)
/*      */           {
/* 1524 */             if (localNextCombiningArgs.c2 == 0) {
/* 1525 */               i6 = 0;
/* 1526 */               i7 = paramRecomposeArgs.start - 1;
/*      */             } else {
/* 1528 */               i6 = 0;
/* 1529 */               i7 = paramRecomposeArgs.start - 2;
/*      */             }
/* 1531 */             n = i1;
/*      */           }
/*      */           else {
/* 1534 */             i7 = -1;
/*      */           }
/* 1536 */         } else if ((paramInt & 0x2000) != 0)
/*      */         {
/* 1538 */           i7 = -1;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int findPreviousStarter(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, char paramChar)
/*      */   {
/* 1550 */     PrevArgs localPrevArgs = new PrevArgs(null);
/* 1551 */     localPrevArgs.src = paramArrayOfChar;
/* 1552 */     localPrevArgs.start = paramInt1;
/* 1553 */     localPrevArgs.current = paramInt2;
/*      */ 
/* 1555 */     while (localPrevArgs.start < localPrevArgs.current) {
/* 1556 */       long l = getPrevNorm32(localPrevArgs, paramChar, paramInt3 | paramInt4);
/* 1557 */       if (isTrueStarter(l, paramInt3, paramInt4)) {
/* 1558 */         break;
/*      */       }
/*      */     }
/* 1561 */     return localPrevArgs.current;
/*      */   }
/*      */ 
/*      */   private static int findNextStarter(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, char paramChar)
/*      */   {
/* 1576 */     int j = 0xFF00 | paramInt3;
/*      */ 
/* 1578 */     DecomposeArgs localDecomposeArgs = new DecomposeArgs(null);
/*      */ 
/* 1581 */     while (paramInt1 != paramInt2)
/*      */     {
/* 1584 */       char c1 = paramArrayOfChar[paramInt1];
/* 1585 */       if (c1 < paramChar)
/*      */       {
/*      */         break;
/*      */       }
/* 1589 */       long l = getNorm32(c1);
/* 1590 */       if ((l & j) == 0L)
/*      */         break;
/*      */       char c2;
/* 1594 */       if (isNorm32LeadSurrogate(l))
/*      */       {
/* 1596 */         if ((paramInt1 + 1 == paramInt2) || (!UTF16.isTrailSurrogate(c2 = paramArrayOfChar[(paramInt1 + 1)])))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/* 1601 */         l = getNorm32FromSurrogatePair(l, c2);
/*      */ 
/* 1603 */         if ((l & j) == 0L)
/* 1604 */           break;
/*      */       }
/*      */       else {
/* 1607 */         c2 = '\000';
/*      */       }
/*      */ 
/* 1611 */       if ((l & paramInt4) != 0L)
/*      */       {
/* 1614 */         int i = decompose(l, paramInt4, localDecomposeArgs);
/*      */ 
/* 1618 */         if ((localDecomposeArgs.cc == 0) && ((getNorm32(extraData, i, paramInt3) & paramInt3) == 0L))
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/* 1623 */       paramInt1 += (c2 == 0 ? 1 : 2);
/*      */     }
/*      */ 
/* 1626 */     return paramInt1;
/*      */   }
/*      */ 
/*      */   private static char[] composePart(ComposePartArgs paramComposePartArgs, int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3, int paramInt4, UnicodeSet paramUnicodeSet)
/*      */   {
/* 1642 */     boolean bool = (paramInt4 & 0x1000) != 0;
/*      */ 
/* 1645 */     int[] arrayOfInt = new int[1];
/* 1646 */     char[] arrayOfChar = new char[(paramInt3 - paramInt1) * 20];
/*      */     while (true)
/*      */     {
/* 1649 */       paramComposePartArgs.length = decompose(paramArrayOfChar, paramInt1, paramInt2, arrayOfChar, 0, arrayOfChar.length, bool, arrayOfInt, paramUnicodeSet);
/*      */ 
/* 1652 */       if (paramComposePartArgs.length <= arrayOfChar.length) {
/*      */         break;
/*      */       }
/* 1655 */       arrayOfChar = new char[paramComposePartArgs.length];
/*      */     }
/*      */ 
/* 1660 */     int i = paramComposePartArgs.length;
/*      */ 
/* 1662 */     if (paramComposePartArgs.length >= 2) {
/* 1663 */       RecomposeArgs localRecomposeArgs = new RecomposeArgs(null);
/* 1664 */       localRecomposeArgs.source = arrayOfChar;
/* 1665 */       localRecomposeArgs.start = 0;
/* 1666 */       localRecomposeArgs.limit = i;
/* 1667 */       paramComposePartArgs.prevCC = recompose(localRecomposeArgs, paramInt4, paramUnicodeSet);
/* 1668 */       i = localRecomposeArgs.limit;
/*      */     }
/*      */ 
/* 1672 */     paramComposePartArgs.length = i;
/* 1673 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   private static boolean composeHangul(char paramChar1, char paramChar2, long paramLong, char[] paramArrayOfChar1, int[] paramArrayOfInt, int paramInt1, boolean paramBoolean, char[] paramArrayOfChar2, int paramInt2, UnicodeSet paramUnicodeSet)
/*      */   {
/* 1682 */     int i = paramArrayOfInt[0];
/* 1683 */     if (isJamoVTNorm32JamoV(paramLong))
/*      */     {
/* 1686 */       paramChar1 = (char)(paramChar1 - 'ᄀ');
/* 1687 */       if (paramChar1 < '\023') {
/* 1688 */         paramChar2 = (char)(44032 + (paramChar1 * '\025' + (paramChar2 - 'ᅡ')) * 28);
/*      */ 
/* 1693 */         if (i != paramInt1)
/*      */         {
/* 1696 */           int j = paramArrayOfChar1[i];
/*      */           char c;
/* 1697 */           if ((c = (char)(j - 4519)) < '\034')
/*      */           {
/* 1699 */             i++;
/* 1700 */             paramChar2 = (char)(paramChar2 + c);
/* 1701 */           } else if (paramBoolean)
/*      */           {
/* 1704 */             paramLong = getNorm32(j);
/* 1705 */             if ((isNorm32Regular(paramLong)) && ((paramLong & 0x8) != 0L))
/*      */             {
/* 1707 */               DecomposeArgs localDecomposeArgs = new DecomposeArgs(null);
/* 1708 */               int k = decompose(paramLong, 8, localDecomposeArgs);
/* 1709 */               if ((localDecomposeArgs.length == 1) && ((c = (char)(extraData[k] - 'ᆧ')) < '\034'))
/*      */               {
/* 1713 */                 i++;
/* 1714 */                 paramChar2 = (char)(paramChar2 + c);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1719 */         if (nx_contains(paramUnicodeSet, paramChar2)) {
/* 1720 */           if (!isHangulWithoutJamoT(paramChar2)) {
/* 1721 */             i--;
/*      */           }
/* 1723 */           return false;
/*      */         }
/* 1725 */         paramArrayOfChar2[paramInt2] = paramChar2;
/* 1726 */         paramArrayOfInt[0] = i;
/* 1727 */         return true;
/*      */       }
/* 1729 */     } else if (isHangulWithoutJamoT(paramChar1))
/*      */     {
/* 1732 */       paramChar2 = (char)(paramChar1 + (paramChar2 - 'ᆧ'));
/* 1733 */       if (nx_contains(paramUnicodeSet, paramChar2)) {
/* 1734 */         return false;
/*      */       }
/* 1736 */       paramArrayOfChar2[paramInt2] = paramChar2;
/* 1737 */       paramArrayOfInt[0] = i;
/* 1738 */       return true;
/*      */     }
/* 1740 */     return false;
/*      */   }
/*      */ 
/*      */   public static int compose(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, int paramInt5, UnicodeSet paramUnicodeSet)
/*      */   {
/* 1758 */     int[] arrayOfInt = new int[1];
/* 1759 */     int i4 = paramInt3;
/* 1760 */     int i5 = paramInt1;
/*      */     char c3;
/*      */     int m;
/* 1762 */     if ((paramInt5 & 0x1000) != 0) {
/* 1763 */       c3 = (char)indexes[7];
/* 1764 */       m = 34;
/*      */     } else {
/* 1766 */       c3 = (char)indexes[6];
/* 1767 */       m = 17;
/*      */     }
/*      */ 
/* 1793 */     int j = i5;
/*      */ 
/* 1795 */     int k = 0xFF00 | m;
/* 1796 */     int n = 0;
/* 1797 */     int i3 = 0;
/*      */ 
/* 1800 */     long l = 0L;
/* 1801 */     char c1 = '\000';
/*      */     while (true)
/*      */     {
/* 1806 */       int i = i5;
/*      */ 
/* 1808 */       while ((i5 != paramInt2) && (((c1 = paramArrayOfChar1[i5]) < c3) || (((l = getNorm32(c1)) & k) == 0L)))
/*      */       {
/* 1810 */         i3 = 0;
/* 1811 */         i5++;
/*      */       }
/*      */       int i1;
/* 1816 */       if (i5 != i) {
/* 1817 */         i1 = i5 - i;
/* 1818 */         if (i4 + i1 <= paramInt4) {
/* 1819 */           System.arraycopy(paramArrayOfChar1, i, paramArrayOfChar2, i4, i1);
/*      */         }
/* 1821 */         i4 += i1;
/* 1822 */         n = i4;
/*      */ 
/* 1826 */         j = i5 - 1;
/* 1827 */         if ((UTF16.isTrailSurrogate(paramArrayOfChar1[j])) && (i < j) && (UTF16.isLeadSurrogate(paramArrayOfChar1[(j - 1)])))
/*      */         {
/* 1830 */           j--;
/*      */         }
/*      */ 
/* 1833 */         i = i5;
/*      */       }
/*      */ 
/* 1837 */       if (i5 == paramInt2)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/* 1842 */       i5++;
/*      */       int i2;
/*      */       char c2;
/* 1874 */       if (isNorm32HangulOrJamo(l))
/*      */       {
/* 1881 */         i3 = i2 = 0;
/* 1882 */         n = i4;
/* 1883 */         arrayOfInt[0] = i5;
/* 1884 */         if (i4 > 0) { if (composeHangul(paramArrayOfChar1[(i - 1)], c1, l, paramArrayOfChar1, arrayOfInt, paramInt2, (paramInt5 & 0x1000) != 0, paramArrayOfChar2, i4 <= paramInt4 ? i4 - 1 : 0, paramUnicodeSet))
/*      */           {
/* 1891 */             i5 = arrayOfInt[0];
/* 1892 */             j = i5;
/*      */           }
/*      */         } else
/*      */         {
/* 1896 */           i5 = arrayOfInt[0];
/*      */ 
/* 1900 */           c2 = '\000';
/* 1901 */           i1 = 1;
/* 1902 */           j = i;
/*      */         }
/*      */       } else { if (isNorm32Regular(l)) {
/* 1905 */           c2 = '\000';
/* 1906 */           i1 = 1;
/*      */         }
/* 1909 */         else if ((i5 != paramInt2) && (UTF16.isTrailSurrogate(c2 = paramArrayOfChar1[i5])))
/*      */         {
/* 1911 */           i5++;
/* 1912 */           i1 = 2;
/* 1913 */           l = getNorm32FromSurrogatePair(l, c2);
/*      */         }
/*      */         else {
/* 1916 */           c2 = '\000';
/* 1917 */           i1 = 1;
/* 1918 */           l = 0L;
/*      */         }
/*      */ 
/* 1921 */         ComposePartArgs localComposePartArgs = new ComposePartArgs(null);
/*      */ 
/* 1924 */         if (nx_contains(paramUnicodeSet, c1, c2))
/*      */         {
/* 1926 */           i2 = 0;
/* 1927 */         } else if ((l & m) == 0L) {
/* 1928 */           i2 = (int)(0xFF & l >> 8);
/*      */         }
/*      */         else
/*      */         {
/* 1945 */           int i7 = m << 2 & 0xF;
/*      */ 
/* 1951 */           if (isTrueStarter(l, 0xFF00 | m, i7)) {
/* 1952 */             j = i;
/*      */           }
/*      */           else {
/* 1955 */             i4 -= i - j;
/*      */           }
/*      */ 
/* 1959 */           i5 = findNextStarter(paramArrayOfChar1, i5, paramInt2, m, i7, c3);
/*      */ 
/* 1962 */           localComposePartArgs.prevCC = i3;
/*      */ 
/* 1964 */           localComposePartArgs.length = i1;
/* 1965 */           char[] arrayOfChar = composePart(localComposePartArgs, j, paramArrayOfChar1, i5, paramInt2, paramInt5, paramUnicodeSet);
/*      */ 
/* 1967 */           if (arrayOfChar == null)
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/* 1972 */           i3 = localComposePartArgs.prevCC;
/* 1973 */           i1 = localComposePartArgs.length;
/*      */ 
/* 1977 */           if (i4 + localComposePartArgs.length <= paramInt4) {
/* 1978 */             int i8 = 0;
/* 1979 */             while (i8 < localComposePartArgs.length) {
/* 1980 */               paramArrayOfChar2[(i4++)] = arrayOfChar[(i8++)];
/* 1981 */               i1--;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1986 */             i4 += i1;
/*      */           }
/*      */ 
/* 1989 */           j = i5;
/* 1990 */           continue;
/*      */         }
/*      */ 
/* 1995 */         if (i4 + i1 <= paramInt4) {
/* 1996 */           if ((i2 != 0) && (i2 < i3))
/*      */           {
/* 1999 */             int i6 = i4;
/* 2000 */             i4 += i1;
/* 2001 */             i3 = insertOrdered(paramArrayOfChar2, n, i6, i4, c1, c2, i2);
/*      */           }
/*      */           else
/*      */           {
/* 2005 */             paramArrayOfChar2[(i4++)] = c1;
/* 2006 */             if (c2 != 0) {
/* 2007 */               paramArrayOfChar2[(i4++)] = c2;
/*      */             }
/* 2009 */             i3 = i2;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2014 */           i4 += i1;
/* 2015 */           i3 = i2;
/*      */         }
/*      */       }
/*      */     }
/* 2019 */     return i4 - paramInt3;
/*      */   }
/*      */ 
/*      */   public static int getCombiningClass(int paramInt)
/*      */   {
/* 2024 */     long l = getNorm32(paramInt);
/* 2025 */     return (char)(int)(l >> 8 & 0xFF);
/*      */   }
/*      */ 
/*      */   public static boolean isFullCompositionExclusion(int paramInt) {
/* 2029 */     if (isFormatVersion_2_1) {
/* 2030 */       int i = AuxTrieImpl.auxTrie.getCodePointValue(paramInt);
/* 2031 */       return (i & 0x400) != 0;
/*      */     }
/* 2033 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isCanonSafeStart(int paramInt)
/*      */   {
/* 2038 */     if (isFormatVersion_2_1) {
/* 2039 */       int i = AuxTrieImpl.auxTrie.getCodePointValue(paramInt);
/* 2040 */       return (i & 0x800) == 0;
/*      */     }
/* 2042 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isNFSkippable(int paramInt, NormalizerBase.Mode paramMode, long paramLong)
/*      */   {
/* 2049 */     paramLong &= 4294967295L;
/*      */ 
/* 2053 */     long l = getNorm32(paramInt);
/*      */ 
/* 2055 */     if ((l & paramLong) != 0L) {
/* 2056 */       return false;
/*      */     }
/*      */ 
/* 2059 */     if ((paramMode == NormalizerBase.NFD) || (paramMode == NormalizerBase.NFKD) || (paramMode == NormalizerBase.NONE)) {
/* 2060 */       return true;
/*      */     }
/*      */ 
/* 2065 */     if ((l & 0x4) == 0L) {
/* 2066 */       return true;
/*      */     }
/*      */ 
/* 2070 */     if (isNorm32HangulOrJamo(l))
/*      */     {
/* 2072 */       return !isHangulWithoutJamoT((char)paramInt);
/*      */     }
/*      */ 
/* 2077 */     if (!isFormatVersion_2_2) {
/* 2078 */       return false;
/*      */     }
/*      */ 
/* 2082 */     int i = AuxTrieImpl.auxTrie.getCodePointValue(paramInt);
/* 2083 */     return (i & 0x1000) == 0L;
/*      */   }
/*      */ 
/*      */   public static UnicodeSet addPropertyStarts(UnicodeSet paramUnicodeSet)
/*      */   {
/* 2093 */     TrieIterator localTrieIterator1 = new TrieIterator(NormTrieImpl.normTrie);
/* 2094 */     RangeValueIterator.Element localElement1 = new RangeValueIterator.Element();
/*      */ 
/* 2096 */     while (localTrieIterator1.next(localElement1)) {
/* 2097 */       paramUnicodeSet.add(localElement1.start);
/*      */     }
/*      */ 
/* 2101 */     TrieIterator localTrieIterator2 = new TrieIterator(FCDTrieImpl.fcdTrie);
/* 2102 */     RangeValueIterator.Element localElement2 = new RangeValueIterator.Element();
/*      */ 
/* 2104 */     while (localTrieIterator2.next(localElement2)) {
/* 2105 */       paramUnicodeSet.add(localElement2.start);
/*      */     }
/*      */ 
/* 2108 */     if (isFormatVersion_2_1)
/*      */     {
/* 2110 */       TrieIterator localTrieIterator3 = new TrieIterator(AuxTrieImpl.auxTrie);
/* 2111 */       RangeValueIterator.Element localElement3 = new RangeValueIterator.Element();
/* 2112 */       while (localTrieIterator3.next(localElement3)) {
/* 2113 */         paramUnicodeSet.add(localElement3.start);
/*      */       }
/*      */     }
/*      */ 
/* 2117 */     for (int i = 44032; i < 55204; i += 28) {
/* 2118 */       paramUnicodeSet.add(i);
/* 2119 */       paramUnicodeSet.add(i + 1);
/*      */     }
/* 2121 */     paramUnicodeSet.add(55204);
/* 2122 */     return paramUnicodeSet;
/*      */   }
/*      */ 
/*      */   public static final int quickCheck(int paramInt1, int paramInt2)
/*      */   {
/* 2133 */     int[] arrayOfInt = { 0, 0, 4, 8, 17, 34 };
/*      */ 
/* 2137 */     int i = (int)getNorm32(paramInt1) & arrayOfInt[paramInt2];
/*      */ 
/* 2139 */     if (i == 0)
/* 2140 */       return 1;
/* 2141 */     if ((i & 0xF) != 0) {
/* 2142 */       return 0;
/*      */     }
/* 2144 */     return 2;
/*      */   }
/*      */ 
/*      */   private static int strCompare(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/* 2157 */     int i = paramInt1;
/* 2158 */     int j = paramInt3;
/*      */ 
/* 2162 */     int i4 = paramInt2 - paramInt1;
/* 2163 */     int i5 = paramInt4 - paramInt3;
/*      */     int i6;
/* 2167 */     if (i4 < i5) {
/* 2168 */       i6 = -1;
/* 2169 */       k = i + i4;
/* 2170 */     } else if (i4 == i5) {
/* 2171 */       i6 = 0;
/* 2172 */       k = i + i4;
/*      */     } else {
/* 2174 */       i6 = 1;
/* 2175 */       k = i + i5;
/*      */     }
/*      */ 
/* 2178 */     if (paramArrayOfChar1 == paramArrayOfChar2)
/* 2179 */       return i6;
/*      */     int n;
/*      */     int i2;
/*      */     while (true) {
/* 2184 */       if (paramInt1 == k) {
/* 2185 */         return i6;
/*      */       }
/*      */ 
/* 2188 */       n = paramArrayOfChar1[paramInt1];
/* 2189 */       i2 = paramArrayOfChar2[paramInt3];
/* 2190 */       if (n != i2) {
/*      */         break;
/*      */       }
/* 2193 */       paramInt1++;
/* 2194 */       paramInt3++;
/*      */     }
/*      */ 
/* 2198 */     int k = i + i4;
/* 2199 */     int m = j + i5;
/*      */     int i1;
/*      */     int i3;
/* 2203 */     if ((n >= 55296) && (i2 >= 55296) && (paramBoolean))
/*      */     {
/* 2206 */       if (((n > 56319) || (paramInt1 + 1 == k) || (!UTF16.isTrailSurrogate(paramArrayOfChar1[(paramInt1 + 1)]))) && ((!UTF16.isTrailSurrogate(n)) || (i == paramInt1) || (!UTF16.isLeadSurrogate(paramArrayOfChar1[(paramInt1 - 1)]))))
/*      */       {
/* 2217 */         i1 = (char)(n - 10240);
/*      */       }
/*      */ 
/* 2220 */       if (((i2 > 56319) || (paramInt3 + 1 == m) || (!UTF16.isTrailSurrogate(paramArrayOfChar2[(paramInt3 + 1)]))) && ((!UTF16.isTrailSurrogate(i2)) || (j == paramInt3) || (!UTF16.isLeadSurrogate(paramArrayOfChar2[(paramInt3 - 1)]))))
/*      */       {
/* 2231 */         i3 = (char)(i2 - 10240);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2236 */     return i1 - i3;
/*      */   }
/*      */ 
/*      */   private static final synchronized UnicodeSet internalGetNXHangul()
/*      */   {
/* 2336 */     if (nxCache[1] == null) {
/* 2337 */       nxCache[1] = new UnicodeSet(44032, 55203);
/*      */     }
/* 2339 */     return nxCache[1];
/*      */   }
/*      */ 
/*      */   private static final synchronized UnicodeSet internalGetNXCJKCompat()
/*      */   {
/* 2345 */     if (nxCache[2] == null)
/*      */     {
/* 2350 */       UnicodeSet localUnicodeSet1 = new UnicodeSet("[:Ideographic:]");
/*      */ 
/* 2353 */       UnicodeSet localUnicodeSet2 = new UnicodeSet();
/*      */ 
/* 2356 */       UnicodeSetIterator localUnicodeSetIterator = new UnicodeSetIterator(localUnicodeSet1);
/*      */ 
/* 2360 */       while ((localUnicodeSetIterator.nextRange()) && (localUnicodeSetIterator.codepoint != UnicodeSetIterator.IS_STRING)) {
/* 2361 */         int i = localUnicodeSetIterator.codepoint;
/* 2362 */         int j = localUnicodeSetIterator.codepointEnd;
/* 2363 */         while (i <= j) {
/* 2364 */           long l = getNorm32(i);
/* 2365 */           if ((l & 0x4) > 0L) {
/* 2366 */             localUnicodeSet2.add(i);
/*      */           }
/* 2368 */           i++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2373 */       nxCache[2] = localUnicodeSet2;
/*      */     }
/*      */ 
/* 2377 */     return nxCache[2];
/*      */   }
/*      */ 
/*      */   private static final synchronized UnicodeSet internalGetNXUnicode(int paramInt) {
/* 2381 */     paramInt &= 224;
/* 2382 */     if (paramInt == 0) {
/* 2383 */       return null;
/*      */     }
/*      */ 
/* 2386 */     if (nxCache[paramInt] == null)
/*      */     {
/* 2388 */       UnicodeSet localUnicodeSet = new UnicodeSet();
/*      */ 
/* 2390 */       switch (paramInt) {
/*      */       case 32:
/* 2392 */         localUnicodeSet.applyPattern("[:^Age=3.2:]");
/* 2393 */         break;
/*      */       default:
/* 2395 */         return null;
/*      */       }
/*      */ 
/* 2398 */       nxCache[paramInt] = localUnicodeSet;
/*      */     }
/*      */ 
/* 2401 */     return nxCache[paramInt];
/*      */   }
/*      */ 
/*      */   private static final synchronized UnicodeSet internalGetNX(int paramInt)
/*      */   {
/* 2406 */     paramInt &= 255;
/*      */ 
/* 2408 */     if (nxCache[paramInt] == null)
/*      */     {
/* 2410 */       if (paramInt == 1) {
/* 2411 */         return internalGetNXHangul();
/*      */       }
/* 2413 */       if (paramInt == 2) {
/* 2414 */         return internalGetNXCJKCompat();
/*      */       }
/* 2416 */       if (((paramInt & 0xE0) != 0) && ((paramInt & 0x1F) == 0)) {
/* 2417 */         return internalGetNXUnicode(paramInt);
/*      */       }
/*      */ 
/* 2424 */       UnicodeSet localUnicodeSet1 = new UnicodeSet();
/*      */       UnicodeSet localUnicodeSet2;
/* 2427 */       if (((paramInt & 0x1) != 0) && (null != (localUnicodeSet2 = internalGetNXHangul()))) {
/* 2428 */         localUnicodeSet1.addAll(localUnicodeSet2);
/*      */       }
/* 2430 */       if (((paramInt & 0x2) != 0) && (null != (localUnicodeSet2 = internalGetNXCJKCompat()))) {
/* 2431 */         localUnicodeSet1.addAll(localUnicodeSet2);
/*      */       }
/* 2433 */       if (((paramInt & 0xE0) != 0) && (null != (localUnicodeSet2 = internalGetNXUnicode(paramInt)))) {
/* 2434 */         localUnicodeSet1.addAll(localUnicodeSet2);
/*      */       }
/*      */ 
/* 2437 */       nxCache[paramInt] = localUnicodeSet1;
/*      */     }
/* 2439 */     return nxCache[paramInt];
/*      */   }
/*      */ 
/*      */   public static final UnicodeSet getNX(int paramInt) {
/* 2443 */     if ((paramInt &= 255) == 0)
/*      */     {
/* 2445 */       return null;
/*      */     }
/* 2447 */     return internalGetNX(paramInt);
/*      */   }
/*      */ 
/*      */   private static final boolean nx_contains(UnicodeSet paramUnicodeSet, int paramInt)
/*      */   {
/* 2452 */     return (paramUnicodeSet != null) && (paramUnicodeSet.contains(paramInt));
/*      */   }
/*      */ 
/*      */   private static final boolean nx_contains(UnicodeSet paramUnicodeSet, char paramChar1, char paramChar2) {
/* 2456 */     if (paramUnicodeSet != null);
/* 2456 */     return paramUnicodeSet.contains(paramChar2 == 0 ? paramChar1 : UCharacterProperty.getRawSupplementary(paramChar1, paramChar2));
/*      */   }
/*      */ 
/*      */   public static int getDecompose(int[] paramArrayOfInt, String[] paramArrayOfString)
/*      */   {
/* 2467 */     DecomposeArgs localDecomposeArgs = new DecomposeArgs(null);
/* 2468 */     int i = 0;
/* 2469 */     long l = 0L;
/* 2470 */     int j = -1;
/* 2471 */     int k = 0;
/* 2472 */     int m = 0;
/*      */     while (true) {
/* 2474 */       j++; if (j >= 195102) {
/*      */         break;
/*      */       }
/* 2477 */       if (j == 12543)
/* 2478 */         j = 63744;
/* 2479 */       else if (j == 65536)
/* 2480 */         j = 119134;
/* 2481 */       else if (j == 119233) {
/* 2482 */         j = 194560;
/*      */       }
/* 2484 */       l = getNorm32(j);
/* 2485 */       if (((l & 0x4) != 0L) && (m < paramArrayOfInt.length)) {
/* 2486 */         paramArrayOfInt[m] = j;
/* 2487 */         k = decompose(l, localDecomposeArgs);
/* 2488 */         paramArrayOfString[(m++)] = new String(extraData, k, localDecomposeArgs.length);
/*      */       }
/*      */     }
/* 2491 */     return m;
/*      */   }
/*      */ 
/*      */   private static boolean needSingleQuotation(char paramChar)
/*      */   {
/* 2498 */     return ((paramChar >= '\t') && (paramChar <= '\r')) || ((paramChar >= ' ') && (paramChar <= '/')) || ((paramChar >= ':') && (paramChar <= '@')) || ((paramChar >= '[') && (paramChar <= '`')) || ((paramChar >= '{') && (paramChar <= '~'));
/*      */   }
/*      */ 
/*      */   public static String canonicalDecomposeWithSingleQuotation(String paramString)
/*      */   {
/* 2506 */     char[] arrayOfChar1 = paramString.toCharArray();
/* 2507 */     int i = 0;
/* 2508 */     int j = arrayOfChar1.length;
/* 2509 */     Object localObject1 = new char[arrayOfChar1.length * 3];
/* 2510 */     Object localObject2 = 0;
/* 2511 */     int k = localObject1.length;
/*      */ 
/* 2513 */     char[] arrayOfChar2 = new char[3];
/*      */ 
/* 2517 */     int i1 = 4;
/*      */ 
/* 2520 */     int i5 = (char)indexes[8];
/*      */ 
/* 2527 */     int n = 0xFF00 | i1;
/* 2528 */     int i2 = 0;
/* 2529 */     int i7 = 0;
/* 2530 */     long l = 0L;
/* 2531 */     int i4 = 0;
/* 2532 */     int i9 = 0;
/*      */     int i8;
/* 2534 */     int i6 = i8 = -1;
/*      */     while (true) {
/* 2536 */       int m = i;
/*      */ 
/* 2538 */       while ((i != j) && (((i4 = arrayOfChar1[i]) < i5) || (((l = getNorm32(i4)) & n) == 0L) || ((i4 >= 44032) && (i4 <= 55203))))
/*      */       {
/* 2543 */         i7 = 0;
/* 2544 */         i++;
/*      */       }
/*      */       int i3;
/* 2548 */       if (i != m) {
/* 2549 */         i3 = i - m;
/* 2550 */         if (localObject2 + i3 <= k) {
/* 2551 */           System.arraycopy(arrayOfChar1, m, localObject1, localObject2, i3);
/*      */         }
/*      */ 
/* 2554 */         localObject2 += i3;
/* 2555 */         i2 = localObject2;
/*      */       }
/*      */ 
/* 2559 */       if (i == j)
/*      */       {
/*      */         break;
/*      */       }
/* 2563 */       i++;
/*      */       char c2;
/* 2565 */       if (isNorm32Regular(l)) {
/* 2566 */         c2 = '\000';
/* 2567 */         i3 = 1;
/*      */       }
/* 2570 */       else if ((i != j) && (Character.isLowSurrogate(c2 = arrayOfChar1[i])))
/*      */       {
/* 2572 */         i++;
/* 2573 */         i3 = 2;
/* 2574 */         l = getNorm32FromSurrogatePair(l, c2);
/*      */       } else {
/* 2576 */         c2 = '\000';
/* 2577 */         i3 = 1;
/* 2578 */         l = 0L;
/*      */       }
/*      */       char[] arrayOfChar3;
/*      */       char c1;
/* 2583 */       if ((l & i1) == 0L)
/*      */       {
/* 2585 */         i6 = i8 = (int)(0xFF & l >> 8);
/* 2586 */         arrayOfChar3 = null;
/* 2587 */         i9 = -1;
/*      */       } else {
/* 2589 */         localObject3 = new DecomposeArgs(null);
/*      */ 
/* 2592 */         i9 = decompose(l, i1, (DecomposeArgs)localObject3);
/* 2593 */         arrayOfChar3 = extraData;
/* 2594 */         i3 = ((DecomposeArgs)localObject3).length;
/* 2595 */         i6 = ((DecomposeArgs)localObject3).cc;
/* 2596 */         i8 = ((DecomposeArgs)localObject3).trailCC;
/* 2597 */         if (i3 == 1)
/*      */         {
/* 2599 */           c1 = arrayOfChar3[i9];
/* 2600 */           c2 = '\000';
/* 2601 */           arrayOfChar3 = null;
/* 2602 */           i9 = -1;
/*      */         }
/*      */       }
/*      */ 
/* 2606 */       if (localObject2 + i3 * 3 >= k)
/*      */       {
/* 2608 */         localObject3 = new char[k * 2];
/* 2609 */         System.arraycopy(localObject1, 0, localObject3, 0, localObject2);
/* 2610 */         localObject1 = localObject3;
/* 2611 */         k = localObject1.length;
/*      */       }
/*      */ 
/* 2615 */       Object localObject3 = localObject2;
/* 2616 */       if (arrayOfChar3 == null)
/*      */       {
/* 2618 */         if (needSingleQuotation(c1))
/*      */         {
/* 2621 */           localObject1[(localObject2++)] = 39;
/* 2622 */           localObject1[(localObject2++)] = c1;
/* 2623 */           localObject1[(localObject2++)] = 39;
/* 2624 */           i8 = 0;
/* 2625 */         } else if ((i6 != 0) && (i6 < i7))
/*      */         {
/* 2628 */           localObject2 += i3;
/* 2629 */           i8 = insertOrdered((char[])localObject1, i2, localObject3, localObject2, c1, c2, i6);
/*      */         }
/*      */         else
/*      */         {
/* 2633 */           localObject1[(localObject2++)] = c1;
/* 2634 */           if (c2 != 0) {
/* 2635 */             localObject1[(localObject2++)] = c2;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/* 2641 */       else if (needSingleQuotation(arrayOfChar3[i9])) {
/* 2642 */         localObject1[(localObject2++)] = 39;
/* 2643 */         localObject1[(localObject2++)] = arrayOfChar3[(i9++)];
/* 2644 */         localObject1[(localObject2++)] = 39;
/* 2645 */         i3--;
/*      */         do {
/* 2647 */           localObject1[(localObject2++)] = arrayOfChar3[(i9++)];
/* 2648 */           i3--; } while (i3 > 0);
/*      */       }
/* 2650 */       else if ((i6 != 0) && (i6 < i7)) {
/* 2651 */         localObject2 += i3;
/* 2652 */         i8 = mergeOrdered((char[])localObject1, i2, localObject3, arrayOfChar3, i9, i9 + i3);
/*      */       }
/*      */       else
/*      */       {
/*      */         do {
/* 2657 */           localObject1[(localObject2++)] = arrayOfChar3[(i9++)];
/* 2658 */           i3--; } while (i3 > 0);
/*      */       }
/*      */ 
/* 2662 */       i7 = i8;
/* 2663 */       if (i7 == 0) {
/* 2664 */         i2 = localObject2;
/*      */       }
/*      */     }
/* 2667 */     return new String((char[])localObject1, 0, localObject2);
/*      */   }
/*      */ 
/*      */   public static String convert(String paramString)
/*      */   {
/* 2703 */     if (paramString == null) {
/* 2704 */       return null;
/*      */     }
/*      */ 
/* 2707 */     int i = -1;
/* 2708 */     StringBuffer localStringBuffer = new StringBuffer();
/* 2709 */     UCharacterIterator localUCharacterIterator = UCharacterIterator.getInstance(paramString);
/*      */ 
/* 2711 */     while ((i = localUCharacterIterator.nextCodePoint()) != -1) {
/* 2712 */       switch (i) {
/*      */       case 194664:
/* 2714 */         localStringBuffer.append(corrigendum4MappingTable[0]);
/* 2715 */         break;
/*      */       case 194676:
/* 2717 */         localStringBuffer.append(corrigendum4MappingTable[1]);
/* 2718 */         break;
/*      */       case 194847:
/* 2720 */         localStringBuffer.append(corrigendum4MappingTable[2]);
/* 2721 */         break;
/*      */       case 194911:
/* 2723 */         localStringBuffer.append(corrigendum4MappingTable[3]);
/* 2724 */         break;
/*      */       case 195007:
/* 2726 */         localStringBuffer.append(corrigendum4MappingTable[4]);
/* 2727 */         break;
/*      */       default:
/* 2729 */         UTF16.append(localStringBuffer, i);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2734 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*   56 */       IMPL = new NormalizerImpl();
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*   60 */       throw new RuntimeException(localException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class AuxTrieImpl
/*      */     implements Trie.DataManipulate
/*      */   {
/*  181 */     static CharTrie auxTrie = null;
/*      */ 
/*      */     public int getFoldingOffset(int paramInt)
/*      */     {
/*  191 */       return (paramInt & 0x3FF) << 5;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class ComposePartArgs
/*      */   {
/*      */     int prevCC;
/*      */     int length;
/*      */   }
/*      */ 
/*      */   private static final class DecomposeArgs
/*      */   {
/*      */     int cc;
/*      */     int trailCC;
/*      */     int length;
/*      */   }
/*      */ 
/*      */   static final class FCDTrieImpl
/*      */     implements Trie.DataManipulate
/*      */   {
/*  166 */     static CharTrie fcdTrie = null;
/*      */ 
/*      */     public int getFoldingOffset(int paramInt)
/*      */     {
/*  176 */       return paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class NextCCArgs
/*      */   {
/*      */     char[] source;
/*      */     int next;
/*      */     int limit;
/*      */     char c;
/*      */     char c2;
/*      */   }
/*      */ 
/*      */   private static final class NextCombiningArgs
/*      */   {
/*      */     char[] source;
/*      */     int start;
/*      */     char c;
/*      */     char c2;
/*      */     int combiningIndex;
/*      */     char cc;
/*      */   }
/*      */ 
/*      */   static final class NormTrieImpl
/*      */     implements Trie.DataManipulate
/*      */   {
/*  149 */     static IntTrie normTrie = null;
/*      */ 
/*      */     public int getFoldingOffset(int paramInt)
/*      */     {
/*  159 */       return 2048 + (paramInt >> 11 & 0x7FE0);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class PrevArgs
/*      */   {
/*      */     char[] src;
/*      */     int start;
/*      */     int current;
/*      */     char c;
/*      */     char c2;
/*      */   }
/*      */ 
/*      */   private static final class RecomposeArgs
/*      */   {
/*      */     char[] source;
/*      */     int start;
/*      */     int limit;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.NormalizerImpl
 * JD-Core Version:    0.6.2
 */