/*      */ package sun.text.normalizer;
/*      */ 
/*      */ import java.text.CharacterIterator;
/*      */ import java.text.Normalizer.Form;
/*      */ 
/*      */ public final class NormalizerBase
/*      */   implements Cloneable
/*      */ {
/*  149 */   private char[] buffer = new char[100];
/*  150 */   private int bufferStart = 0;
/*  151 */   private int bufferPos = 0;
/*  152 */   private int bufferLimit = 0;
/*      */   private UCharacterIterator text;
/*  156 */   private Mode mode = NFC;
/*  157 */   private int options = 0;
/*      */   private int currentIndex;
/*      */   private int nextIndex;
/*      */   public static final int UNICODE_3_2 = 32;
/*      */   public static final int DONE = -1;
/*  280 */   public static final Mode NONE = new Mode(1, null);
/*      */ 
/*  286 */   public static final Mode NFD = new NFDMode(2, null);
/*      */ 
/*  348 */   public static final Mode NFKD = new NFKDMode(3, null);
/*      */ 
/*  410 */   public static final Mode NFC = new NFCMode(4, null);
/*      */ 
/*  469 */   public static final Mode NFKC = new NFKCMode(5, null);
/*      */ 
/*  538 */   public static final QuickCheckResult NO = new QuickCheckResult(0, null);
/*      */ 
/*  544 */   public static final QuickCheckResult YES = new QuickCheckResult(1, null);
/*      */ 
/*  551 */   public static final QuickCheckResult MAYBE = new QuickCheckResult(2, null);
/*      */   private static final int MAX_BUF_SIZE_COMPOSE = 2;
/*      */   private static final int MAX_BUF_SIZE_DECOMPOSE = 3;
/*      */   public static final int UNICODE_3_2_0_ORIGINAL = 262432;
/*      */   public static final int UNICODE_LATEST = 0;
/*      */ 
/*      */   public NormalizerBase(String paramString, Mode paramMode, int paramInt)
/*      */   {
/*  576 */     this.text = UCharacterIterator.getInstance(paramString);
/*  577 */     this.mode = paramMode;
/*  578 */     this.options = paramInt;
/*      */   }
/*      */ 
/*      */   public NormalizerBase(CharacterIterator paramCharacterIterator, Mode paramMode)
/*      */   {
/*  591 */     this(paramCharacterIterator, paramMode, 0);
/*      */   }
/*      */ 
/*      */   public NormalizerBase(CharacterIterator paramCharacterIterator, Mode paramMode, int paramInt)
/*      */   {
/*  610 */     this.text = UCharacterIterator.getInstance((CharacterIterator)paramCharacterIterator.clone());
/*      */ 
/*  613 */     this.mode = paramMode;
/*  614 */     this.options = paramInt;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  629 */       NormalizerBase localNormalizerBase = (NormalizerBase)super.clone();
/*  630 */       localNormalizerBase.text = ((UCharacterIterator)this.text.clone());
/*      */ 
/*  632 */       if (this.buffer != null) {
/*  633 */         localNormalizerBase.buffer = new char[this.buffer.length];
/*  634 */         System.arraycopy(this.buffer, 0, localNormalizerBase.buffer, 0, this.buffer.length);
/*      */       }
/*  636 */       return localNormalizerBase;
/*      */     }
/*      */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  639 */       throw new InternalError(localCloneNotSupportedException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String compose(String paramString, boolean paramBoolean, int paramInt)
/*      */   {
/*      */     char[] arrayOfChar1;
/*      */     char[] arrayOfChar2;
/*  661 */     if (paramInt == 262432) {
/*  662 */       String str = NormalizerImpl.convert(paramString);
/*  663 */       arrayOfChar1 = new char[str.length() * 2];
/*  664 */       arrayOfChar2 = str.toCharArray();
/*      */     } else {
/*  666 */       arrayOfChar1 = new char[paramString.length() * 2];
/*  667 */       arrayOfChar2 = paramString.toCharArray();
/*      */     }
/*  669 */     int i = 0;
/*      */ 
/*  671 */     UnicodeSet localUnicodeSet = NormalizerImpl.getNX(paramInt);
/*      */ 
/*  674 */     paramInt &= -12544;
/*      */ 
/*  676 */     if (paramBoolean) {
/*  677 */       paramInt |= 4096;
/*      */     }
/*      */     while (true)
/*      */     {
/*  681 */       i = NormalizerImpl.compose(arrayOfChar2, 0, arrayOfChar2.length, arrayOfChar1, 0, arrayOfChar1.length, paramInt, localUnicodeSet);
/*      */ 
/*  684 */       if (i <= arrayOfChar1.length) {
/*  685 */         return new String(arrayOfChar1, 0, i);
/*      */       }
/*  687 */       arrayOfChar1 = new char[i];
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String decompose(String paramString, boolean paramBoolean)
/*      */   {
/*  706 */     return decompose(paramString, paramBoolean, 0);
/*      */   }
/*      */ 
/*      */   public static String decompose(String paramString, boolean paramBoolean, int paramInt)
/*      */   {
/*  722 */     int[] arrayOfInt = new int[1];
/*  723 */     int i = 0;
/*  724 */     UnicodeSet localUnicodeSet = NormalizerImpl.getNX(paramInt);
/*      */ 
/*  727 */     if (paramInt == 262432) {
/*  728 */       String str = NormalizerImpl.convert(paramString);
/*  729 */       arrayOfChar = new char[str.length() * 3];
/*      */       while (true)
/*      */       {
/*  732 */         i = NormalizerImpl.decompose(str.toCharArray(), 0, str.length(), arrayOfChar, 0, arrayOfChar.length, paramBoolean, arrayOfInt, localUnicodeSet);
/*      */ 
/*  735 */         if (i <= arrayOfChar.length) {
/*  736 */           return new String(arrayOfChar, 0, i);
/*      */         }
/*  738 */         arrayOfChar = new char[i];
/*      */       }
/*      */     }
/*      */ 
/*  742 */     char[] arrayOfChar = new char[paramString.length() * 3];
/*      */     while (true)
/*      */     {
/*  745 */       i = NormalizerImpl.decompose(paramString.toCharArray(), 0, paramString.length(), arrayOfChar, 0, arrayOfChar.length, paramBoolean, arrayOfInt, localUnicodeSet);
/*      */ 
/*  748 */       if (i <= arrayOfChar.length) {
/*  749 */         return new String(arrayOfChar, 0, i);
/*      */       }
/*  751 */       arrayOfChar = new char[i];
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int normalize(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, Mode paramMode, int paramInt5)
/*      */   {
/*  780 */     int i = paramMode.normalize(paramArrayOfChar1, paramInt1, paramInt2, paramArrayOfChar2, paramInt3, paramInt4, paramInt5);
/*      */ 
/*  782 */     if (i <= paramInt4 - paramInt3) {
/*  783 */       return i;
/*      */     }
/*  785 */     throw new IndexOutOfBoundsException(Integer.toString(i));
/*      */   }
/*      */ 
/*      */   public int current()
/*      */   {
/*  799 */     if ((this.bufferPos < this.bufferLimit) || (nextNormalize())) {
/*  800 */       return getCodePointAt(this.bufferPos);
/*      */     }
/*  802 */     return -1;
/*      */   }
/*      */ 
/*      */   public int next()
/*      */   {
/*  814 */     if ((this.bufferPos < this.bufferLimit) || (nextNormalize())) {
/*  815 */       int i = getCodePointAt(this.bufferPos);
/*  816 */       this.bufferPos += (i > 65535 ? 2 : 1);
/*  817 */       return i;
/*      */     }
/*  819 */     return -1;
/*      */   }
/*      */ 
/*      */   public int previous()
/*      */   {
/*  832 */     if ((this.bufferPos > 0) || (previousNormalize())) {
/*  833 */       int i = getCodePointAt(this.bufferPos - 1);
/*  834 */       this.bufferPos -= (i > 65535 ? 2 : 1);
/*  835 */       return i;
/*      */     }
/*  837 */     return -1;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  847 */     this.text.setIndex(0);
/*  848 */     this.currentIndex = (this.nextIndex = 0);
/*  849 */     clearBuffer();
/*      */   }
/*      */ 
/*      */   public void setIndexOnly(int paramInt)
/*      */   {
/*  862 */     this.text.setIndex(paramInt);
/*  863 */     this.currentIndex = (this.nextIndex = paramInt);
/*  864 */     clearBuffer();
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public int setIndex(int paramInt)
/*      */   {
/*  890 */     setIndexOnly(paramInt);
/*  891 */     return current();
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public int getBeginIndex()
/*      */   {
/*  903 */     return 0;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public int getEndIndex()
/*      */   {
/*  915 */     return endIndex();
/*      */   }
/*      */ 
/*      */   public int getIndex()
/*      */   {
/*  934 */     if (this.bufferPos < this.bufferLimit) {
/*  935 */       return this.currentIndex;
/*      */     }
/*  937 */     return this.nextIndex;
/*      */   }
/*      */ 
/*      */   public int endIndex()
/*      */   {
/*  949 */     return this.text.getLength();
/*      */   }
/*      */ 
/*      */   public void setMode(Mode paramMode)
/*      */   {
/*  982 */     this.mode = paramMode;
/*      */   }
/*      */ 
/*      */   public Mode getMode()
/*      */   {
/*  991 */     return this.mode;
/*      */   }
/*      */ 
/*      */   public void setText(String paramString)
/*      */   {
/* 1002 */     UCharacterIterator localUCharacterIterator = UCharacterIterator.getInstance(paramString);
/* 1003 */     if (localUCharacterIterator == null) {
/* 1004 */       throw new InternalError("Could not create a new UCharacterIterator");
/*      */     }
/* 1006 */     this.text = localUCharacterIterator;
/* 1007 */     reset();
/*      */   }
/*      */ 
/*      */   public void setText(CharacterIterator paramCharacterIterator)
/*      */   {
/* 1018 */     UCharacterIterator localUCharacterIterator = UCharacterIterator.getInstance(paramCharacterIterator);
/* 1019 */     if (localUCharacterIterator == null) {
/* 1020 */       throw new InternalError("Could not create a new UCharacterIterator");
/*      */     }
/* 1022 */     this.text = localUCharacterIterator;
/* 1023 */     this.currentIndex = (this.nextIndex = 0);
/* 1024 */     clearBuffer();
/*      */   }
/*      */ 
/*      */   private static long getPrevNorm32(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, char[] paramArrayOfChar)
/*      */   {
/* 1046 */     int i = 0;
/*      */ 
/* 1048 */     if ((i = paramUCharacterIterator.previous()) == -1) {
/* 1049 */       return 0L;
/*      */     }
/* 1051 */     paramArrayOfChar[0] = ((char)i);
/* 1052 */     paramArrayOfChar[1] = '\000';
/*      */ 
/* 1056 */     if (paramArrayOfChar[0] < paramInt1)
/* 1057 */       return 0L;
/* 1058 */     if (!UTF16.isSurrogate(paramArrayOfChar[0]))
/* 1059 */       return NormalizerImpl.getNorm32(paramArrayOfChar[0]);
/* 1060 */     if ((UTF16.isLeadSurrogate(paramArrayOfChar[0])) || (paramUCharacterIterator.getIndex() == 0))
/*      */     {
/* 1062 */       paramArrayOfChar[1] = ((char)paramUCharacterIterator.current());
/* 1063 */       return 0L;
/* 1064 */     }if (UTF16.isLeadSurrogate(paramArrayOfChar[1] = (char)paramUCharacterIterator.previous())) {
/* 1065 */       long l = NormalizerImpl.getNorm32(paramArrayOfChar[1]);
/* 1066 */       if ((l & paramInt2) == 0L)
/*      */       {
/* 1069 */         return 0L;
/*      */       }
/*      */ 
/* 1072 */       return NormalizerImpl.getNorm32FromSurrogatePair(l, paramArrayOfChar[0]);
/*      */     }
/*      */ 
/* 1076 */     paramUCharacterIterator.moveIndex(1);
/* 1077 */     return 0L;
/*      */   }
/*      */ 
/*      */   private static int findPreviousIterationBoundary(UCharacterIterator paramUCharacterIterator, IsPrevBoundary paramIsPrevBoundary, int paramInt1, int paramInt2, char[] paramArrayOfChar, int[] paramArrayOfInt)
/*      */   {
/* 1132 */     char[] arrayOfChar1 = new char[2];
/*      */ 
/* 1136 */     paramArrayOfInt[0] = paramArrayOfChar.length;
/* 1137 */     arrayOfChar1[0] = '\000';
/* 1138 */     while ((paramUCharacterIterator.getIndex() > 0) && (arrayOfChar1[0] != 'èøø')) {
/* 1139 */       boolean bool = paramIsPrevBoundary.isPrevBoundary(paramUCharacterIterator, paramInt1, paramInt2, arrayOfChar1);
/*      */ 
/* 1143 */       if (paramArrayOfInt[0] < (arrayOfChar1[1] == 0 ? 1 : 2))
/*      */       {
/* 1146 */         char[] arrayOfChar2 = new char[paramArrayOfChar.length * 2];
/*      */ 
/* 1148 */         System.arraycopy(paramArrayOfChar, paramArrayOfInt[0], arrayOfChar2, arrayOfChar2.length - (paramArrayOfChar.length - paramArrayOfInt[0]), paramArrayOfChar.length - paramArrayOfInt[0]);
/*      */ 
/* 1152 */         paramArrayOfInt[0] += arrayOfChar2.length - paramArrayOfChar.length;
/*      */ 
/* 1154 */         paramArrayOfChar = arrayOfChar2;
/* 1155 */         arrayOfChar2 = null;
/*      */       }
/*      */       int tmp130_129 = 0;
/*      */       int[] tmp130_127 = paramArrayOfInt;
/*      */       int tmp134_133 = (tmp130_127[tmp130_129] - 1); tmp130_127[tmp130_129] = tmp134_133; paramArrayOfChar[tmp134_133] = arrayOfChar1[0];
/* 1160 */       if (arrayOfChar1[1] != 0)
/*      */       {
/*      */         int tmp153_152 = 0;
/*      */         int[] tmp153_150 = paramArrayOfInt;
/*      */         int tmp157_156 = (tmp153_150[tmp153_152] - 1); tmp153_150[tmp153_152] = tmp157_156; paramArrayOfChar[tmp157_156] = arrayOfChar1[1];
/*      */       }
/*      */ 
/* 1165 */       if (bool) {
/* 1166 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1171 */     return paramArrayOfChar.length - paramArrayOfInt[0];
/*      */   }
/*      */ 
/*      */   private static int previous(UCharacterIterator paramUCharacterIterator, char[] paramArrayOfChar, int paramInt1, int paramInt2, Mode paramMode, boolean paramBoolean, boolean[] paramArrayOfBoolean, int paramInt3)
/*      */   {
/* 1187 */     int i2 = paramInt2 - paramInt1;
/* 1188 */     int i = 0;
/*      */ 
/* 1190 */     if (paramArrayOfBoolean != null) {
/* 1191 */       paramArrayOfBoolean[0] = false;
/*      */     }
/* 1193 */     int i1 = (char)paramMode.getMinC();
/* 1194 */     int k = paramMode.getMask();
/* 1195 */     IsPrevBoundary localIsPrevBoundary = paramMode.getPrevBoundary();
/*      */ 
/* 1197 */     if (localIsPrevBoundary == null) {
/* 1198 */       i = 0;
/*      */       int m;
/* 1199 */       if ((m = paramUCharacterIterator.previous()) >= 0) {
/* 1200 */         i = 1;
/* 1201 */         if (UTF16.isTrailSurrogate((char)m)) {
/* 1202 */           int n = paramUCharacterIterator.previous();
/* 1203 */           if (n != -1) {
/* 1204 */             if (UTF16.isLeadSurrogate((char)n)) {
/* 1205 */               if (i2 >= 2) {
/* 1206 */                 paramArrayOfChar[1] = ((char)m);
/* 1207 */                 i = 2;
/*      */               }
/*      */ 
/* 1210 */               m = n;
/*      */             } else {
/* 1212 */               paramUCharacterIterator.moveIndex(1);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1217 */         if (i2 > 0) {
/* 1218 */           paramArrayOfChar[0] = ((char)m);
/*      */         }
/*      */       }
/* 1221 */       return i;
/*      */     }
/*      */ 
/* 1224 */     char[] arrayOfChar = new char[100];
/* 1225 */     int[] arrayOfInt = new int[1];
/* 1226 */     int j = findPreviousIterationBoundary(paramUCharacterIterator, localIsPrevBoundary, i1, k, arrayOfChar, arrayOfInt);
/*      */ 
/* 1230 */     if (j > 0) {
/* 1231 */       if (paramBoolean) {
/* 1232 */         i = normalize(arrayOfChar, arrayOfInt[0], arrayOfInt[0] + j, paramArrayOfChar, paramInt1, paramInt2, paramMode, paramInt3);
/*      */ 
/* 1237 */         if (paramArrayOfBoolean != null) {
/* 1238 */           paramArrayOfBoolean[0] = ((i != j) || (Utility.arrayRegionMatches(arrayOfChar, 0, paramArrayOfChar, paramInt1, paramInt2)) ? 1 : false);
/*      */         }
/*      */ 
/*      */       }
/* 1246 */       else if (i2 > 0) {
/* 1247 */         System.arraycopy(arrayOfChar, arrayOfInt[0], paramArrayOfChar, 0, j < i2 ? j : i2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1256 */     return i;
/*      */   }
/*      */ 
/*      */   private static long getNextNorm32(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, int[] paramArrayOfInt)
/*      */   {
/* 1285 */     paramArrayOfInt[0] = paramUCharacterIterator.next();
/* 1286 */     paramArrayOfInt[1] = 0;
/*      */ 
/* 1288 */     if (paramArrayOfInt[0] < paramInt1) {
/* 1289 */       return 0L;
/*      */     }
/*      */ 
/* 1292 */     long l = NormalizerImpl.getNorm32((char)paramArrayOfInt[0]);
/* 1293 */     if (UTF16.isLeadSurrogate((char)paramArrayOfInt[0])) {
/* 1294 */       if ((paramUCharacterIterator.current() != -1) && (UTF16.isTrailSurrogate((char)(paramArrayOfInt[1] = paramUCharacterIterator.current()))))
/*      */       {
/* 1296 */         paramUCharacterIterator.moveIndex(1);
/* 1297 */         if ((l & paramInt2) == 0L)
/*      */         {
/* 1299 */           return 0L;
/*      */         }
/*      */ 
/* 1302 */         return NormalizerImpl.getNorm32FromSurrogatePair(l, (char)paramArrayOfInt[1]);
/*      */       }
/*      */ 
/* 1306 */       return 0L;
/*      */     }
/*      */ 
/* 1309 */     return l;
/*      */   }
/*      */ 
/*      */   private static int findNextIterationBoundary(UCharacterIterator paramUCharacterIterator, IsNextBoundary paramIsNextBoundary, int paramInt1, int paramInt2, char[] paramArrayOfChar)
/*      */   {
/* 1353 */     if (paramUCharacterIterator.current() == -1) {
/* 1354 */       return 0;
/*      */     }
/*      */ 
/* 1358 */     int[] arrayOfInt = new int[2];
/* 1359 */     arrayOfInt[0] = paramUCharacterIterator.next();
/* 1360 */     paramArrayOfChar[0] = ((char)arrayOfInt[0]);
/* 1361 */     int i = 1;
/*      */ 
/* 1363 */     if ((UTF16.isLeadSurrogate((char)arrayOfInt[0])) && (paramUCharacterIterator.current() != -1))
/*      */     {
/* 1365 */       if (UTF16.isTrailSurrogate((char)(arrayOfInt[1] = paramUCharacterIterator.next())))
/* 1366 */         paramArrayOfChar[(i++)] = ((char)arrayOfInt[1]);
/*      */       else {
/* 1368 */         paramUCharacterIterator.moveIndex(-1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1375 */     while (paramUCharacterIterator.current() != -1) {
/* 1376 */       if (paramIsNextBoundary.isNextBoundary(paramUCharacterIterator, paramInt1, paramInt2, arrayOfInt))
/*      */       {
/* 1378 */         paramUCharacterIterator.moveIndex(arrayOfInt[1] == 0 ? -1 : -2);
/* 1379 */         break;
/*      */       }
/* 1381 */       if (i + (arrayOfInt[1] == 0 ? 1 : 2) <= paramArrayOfChar.length) {
/* 1382 */         paramArrayOfChar[(i++)] = ((char)arrayOfInt[0]);
/* 1383 */         if (arrayOfInt[1] != 0)
/* 1384 */           paramArrayOfChar[(i++)] = ((char)arrayOfInt[1]);
/*      */       }
/*      */       else {
/* 1387 */         char[] arrayOfChar = new char[paramArrayOfChar.length * 2];
/* 1388 */         System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, i);
/* 1389 */         paramArrayOfChar = arrayOfChar;
/* 1390 */         paramArrayOfChar[(i++)] = ((char)arrayOfInt[0]);
/* 1391 */         if (arrayOfInt[1] != 0) {
/* 1392 */           paramArrayOfChar[(i++)] = ((char)arrayOfInt[1]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1399 */     return i;
/*      */   }
/*      */ 
/*      */   private static int next(UCharacterIterator paramUCharacterIterator, char[] paramArrayOfChar, int paramInt1, int paramInt2, Mode paramMode, boolean paramBoolean, boolean[] paramArrayOfBoolean, int paramInt3)
/*      */   {
/* 1414 */     int i1 = paramInt2 - paramInt1;
/* 1415 */     int i2 = 0;
/* 1416 */     if (paramArrayOfBoolean != null) {
/* 1417 */       paramArrayOfBoolean[0] = false;
/*      */     }
/*      */ 
/* 1420 */     int n = (char)paramMode.getMinC();
/* 1421 */     int i = paramMode.getMask();
/* 1422 */     IsNextBoundary localIsNextBoundary = paramMode.getNextBoundary();
/*      */ 
/* 1424 */     if (localIsNextBoundary == null) {
/* 1425 */       i2 = 0;
/* 1426 */       int k = paramUCharacterIterator.next();
/* 1427 */       if (k != -1) {
/* 1428 */         i2 = 1;
/* 1429 */         if (UTF16.isLeadSurrogate((char)k)) {
/* 1430 */           int m = paramUCharacterIterator.next();
/* 1431 */           if (m != -1) {
/* 1432 */             if (UTF16.isTrailSurrogate((char)m)) {
/* 1433 */               if (i1 >= 2) {
/* 1434 */                 paramArrayOfChar[1] = ((char)m);
/* 1435 */                 i2 = 2;
/*      */               }
/*      */             }
/*      */             else {
/* 1439 */               paramUCharacterIterator.moveIndex(-1);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1444 */         if (i1 > 0) {
/* 1445 */           paramArrayOfChar[0] = ((char)k);
/*      */         }
/*      */       }
/* 1448 */       return i2;
/*      */     }
/*      */ 
/* 1451 */     char[] arrayOfChar = new char[100];
/* 1452 */     int[] arrayOfInt = new int[1];
/* 1453 */     int j = findNextIterationBoundary(paramUCharacterIterator, localIsNextBoundary, n, i, arrayOfChar);
/*      */ 
/* 1455 */     if (j > 0) {
/* 1456 */       if (paramBoolean) {
/* 1457 */         i2 = paramMode.normalize(arrayOfChar, arrayOfInt[0], j, paramArrayOfChar, paramInt1, paramInt2, paramInt3);
/*      */ 
/* 1460 */         if (paramArrayOfBoolean != null) {
/* 1461 */           paramArrayOfBoolean[0] = ((i2 != j) || (Utility.arrayRegionMatches(arrayOfChar, arrayOfInt[0], paramArrayOfChar, paramInt1, i2)) ? 1 : false);
/*      */         }
/*      */ 
/*      */       }
/* 1468 */       else if (i1 > 0) {
/* 1469 */         System.arraycopy(arrayOfChar, 0, paramArrayOfChar, paramInt1, Math.min(j, i1));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1477 */     return i2;
/*      */   }
/*      */ 
/*      */   private void clearBuffer() {
/* 1481 */     this.bufferLimit = (this.bufferStart = this.bufferPos = 0);
/*      */   }
/*      */ 
/*      */   private boolean nextNormalize()
/*      */   {
/* 1486 */     clearBuffer();
/* 1487 */     this.currentIndex = this.nextIndex;
/* 1488 */     this.text.setIndex(this.nextIndex);
/*      */ 
/* 1490 */     this.bufferLimit = next(this.text, this.buffer, this.bufferStart, this.buffer.length, this.mode, true, null, this.options);
/*      */ 
/* 1492 */     this.nextIndex = this.text.getIndex();
/* 1493 */     return this.bufferLimit > 0;
/*      */   }
/*      */ 
/*      */   private boolean previousNormalize()
/*      */   {
/* 1498 */     clearBuffer();
/* 1499 */     this.nextIndex = this.currentIndex;
/* 1500 */     this.text.setIndex(this.currentIndex);
/* 1501 */     this.bufferLimit = previous(this.text, this.buffer, this.bufferStart, this.buffer.length, this.mode, true, null, this.options);
/*      */ 
/* 1503 */     this.currentIndex = this.text.getIndex();
/* 1504 */     this.bufferPos = this.bufferLimit;
/* 1505 */     return this.bufferLimit > 0;
/*      */   }
/*      */ 
/*      */   private int getCodePointAt(int paramInt) {
/* 1509 */     if (UTF16.isSurrogate(this.buffer[paramInt])) {
/* 1510 */       if (UTF16.isLeadSurrogate(this.buffer[paramInt])) {
/* 1511 */         if ((paramInt + 1 < this.bufferLimit) && (UTF16.isTrailSurrogate(this.buffer[(paramInt + 1)])))
/*      */         {
/* 1513 */           return UCharacterProperty.getRawSupplementary(this.buffer[paramInt], this.buffer[(paramInt + 1)]);
/*      */         }
/*      */ 
/*      */       }
/* 1518 */       else if ((UTF16.isTrailSurrogate(this.buffer[paramInt])) && 
/* 1519 */         (paramInt > 0) && (UTF16.isLeadSurrogate(this.buffer[(paramInt - 1)]))) {
/* 1520 */         return UCharacterProperty.getRawSupplementary(this.buffer[(paramInt - 1)], this.buffer[paramInt]);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1527 */     return this.buffer[paramInt];
/*      */   }
/*      */ 
/*      */   public static boolean isNFSkippable(int paramInt, Mode paramMode)
/*      */   {
/* 1536 */     return paramMode.isNFSkippable(paramInt);
/*      */   }
/*      */ 
/*      */   public NormalizerBase(String paramString, Mode paramMode)
/*      */   {
/* 1580 */     this(paramString, paramMode, 0);
/*      */   }
/*      */ 
/*      */   public static String normalize(String paramString, Normalizer.Form paramForm)
/*      */   {
/* 1590 */     return normalize(paramString, paramForm, 0);
/*      */   }
/*      */ 
/*      */   public static String normalize(String paramString, Normalizer.Form paramForm, int paramInt)
/*      */   {
/* 1601 */     int i = paramString.length();
/* 1602 */     int j = 1;
/* 1603 */     if (i < 80) {
/* 1604 */       for (int k = 0; k < i; k++)
/* 1605 */         if (paramString.charAt(k) > '') {
/* 1606 */           j = 0;
/* 1607 */           break;
/*      */         }
/*      */     }
/*      */     else {
/* 1611 */       char[] arrayOfChar = paramString.toCharArray();
/* 1612 */       for (int m = 0; m < i; m++) {
/* 1613 */         if (arrayOfChar[m] > '') {
/* 1614 */           j = 0;
/* 1615 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1620 */     switch (1.$SwitchMap$java$text$Normalizer$Form[paramForm.ordinal()]) {
/*      */     case 1:
/* 1622 */       return j != 0 ? paramString : NFC.normalize(paramString, paramInt);
/*      */     case 2:
/* 1624 */       return j != 0 ? paramString : NFD.normalize(paramString, paramInt);
/*      */     case 3:
/* 1626 */       return j != 0 ? paramString : NFKC.normalize(paramString, paramInt);
/*      */     case 4:
/* 1628 */       return j != 0 ? paramString : NFKD.normalize(paramString, paramInt);
/*      */     }
/*      */ 
/* 1631 */     throw new IllegalArgumentException("Unexpected normalization form: " + paramForm);
/*      */   }
/*      */ 
/*      */   public static boolean isNormalized(String paramString, Normalizer.Form paramForm)
/*      */   {
/* 1649 */     return isNormalized(paramString, paramForm, 0);
/*      */   }
/*      */ 
/*      */   public static boolean isNormalized(String paramString, Normalizer.Form paramForm, int paramInt)
/*      */   {
/* 1666 */     switch (1.$SwitchMap$java$text$Normalizer$Form[paramForm.ordinal()]) {
/*      */     case 1:
/* 1668 */       return NFC.quickCheck(paramString.toCharArray(), 0, paramString.length(), false, NormalizerImpl.getNX(paramInt)) == YES;
/*      */     case 2:
/* 1670 */       return NFD.quickCheck(paramString.toCharArray(), 0, paramString.length(), false, NormalizerImpl.getNX(paramInt)) == YES;
/*      */     case 3:
/* 1672 */       return NFKC.quickCheck(paramString.toCharArray(), 0, paramString.length(), false, NormalizerImpl.getNX(paramInt)) == YES;
/*      */     case 4:
/* 1674 */       return NFKD.quickCheck(paramString.toCharArray(), 0, paramString.length(), false, NormalizerImpl.getNX(paramInt)) == YES;
/*      */     }
/*      */ 
/* 1677 */     throw new IllegalArgumentException("Unexpected normalization form: " + paramForm);
/*      */   }
/*      */ 
/*      */   private static abstract interface IsNextBoundary
/*      */   {
/*      */     public abstract boolean isNextBoundary(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, int[] paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   private static final class IsNextNFDSafe
/*      */     implements NormalizerBase.IsNextBoundary
/*      */   {
/*      */     public boolean isNextBoundary(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, int[] paramArrayOfInt)
/*      */     {
/* 1323 */       return NormalizerImpl.isNFDSafe(NormalizerBase.getNextNorm32(paramUCharacterIterator, paramInt1, paramInt2, paramArrayOfInt), paramInt2, paramInt2 & 0x3F);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class IsNextTrueStarter
/*      */     implements NormalizerBase.IsNextBoundary
/*      */   {
/*      */     public boolean isNextBoundary(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, int[] paramArrayOfInt)
/*      */     {
/* 1342 */       int i = paramInt2 << 2 & 0xF;
/* 1343 */       long l = NormalizerBase.getNextNorm32(paramUCharacterIterator, paramInt1, paramInt2 | i, paramArrayOfInt);
/* 1344 */       return NormalizerImpl.isTrueStarter(l, paramInt2, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract interface IsPrevBoundary
/*      */   {
/*      */     public abstract boolean isPrevBoundary(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, char[] paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   private static final class IsPrevNFDSafe
/*      */     implements NormalizerBase.IsPrevBoundary
/*      */   {
/*      */     public boolean isPrevBoundary(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, char[] paramArrayOfChar)
/*      */     {
/* 1099 */       return NormalizerImpl.isNFDSafe(NormalizerBase.getPrevNorm32(paramUCharacterIterator, paramInt1, paramInt2, paramArrayOfChar), paramInt2, paramInt2 & 0x3F);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class IsPrevTrueStarter
/*      */     implements NormalizerBase.IsPrevBoundary
/*      */   {
/*      */     public boolean isPrevBoundary(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, char[] paramArrayOfChar)
/*      */     {
/* 1120 */       int i = paramInt2 << 2 & 0xF;
/* 1121 */       long l = NormalizerBase.getPrevNorm32(paramUCharacterIterator, paramInt1, paramInt2 | i, paramArrayOfChar);
/* 1122 */       return NormalizerImpl.isTrueStarter(l, paramInt2, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Mode
/*      */   {
/*      */     private int modeValue;
/*      */ 
/*      */     private Mode(int paramInt)
/*      */     {
/*  183 */       this.modeValue = paramInt;
/*      */     }
/*      */ 
/*      */     protected int normalize(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, UnicodeSet paramUnicodeSet)
/*      */     {
/*  193 */       int i = paramInt2 - paramInt1;
/*  194 */       int j = paramInt4 - paramInt3;
/*  195 */       if (i > j) {
/*  196 */         return i;
/*      */       }
/*  198 */       System.arraycopy(paramArrayOfChar1, paramInt1, paramArrayOfChar2, paramInt3, i);
/*  199 */       return i;
/*      */     }
/*      */ 
/*      */     protected int normalize(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  209 */       return normalize(paramArrayOfChar1, paramInt1, paramInt2, paramArrayOfChar2, paramInt3, paramInt4, NormalizerImpl.getNX(paramInt5));
/*      */     }
/*      */ 
/*      */     protected String normalize(String paramString, int paramInt)
/*      */     {
/*  220 */       return paramString;
/*      */     }
/*      */ 
/*      */     protected int getMinC()
/*      */     {
/*  228 */       return -1;
/*      */     }
/*      */ 
/*      */     protected int getMask()
/*      */     {
/*  236 */       return -1;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsPrevBoundary getPrevBoundary()
/*      */     {
/*  244 */       return null;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsNextBoundary getNextBoundary()
/*      */     {
/*  252 */       return null;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.QuickCheckResult quickCheck(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean, UnicodeSet paramUnicodeSet)
/*      */     {
/*  261 */       if (paramBoolean) {
/*  262 */         return NormalizerBase.MAYBE;
/*      */       }
/*  264 */       return NormalizerBase.NO;
/*      */     }
/*      */ 
/*      */     protected boolean isNFSkippable(int paramInt)
/*      */     {
/*  272 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class NFCMode extends NormalizerBase.Mode
/*      */   {
/*      */     private NFCMode(int paramInt)
/*      */     {
/*  414 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int normalize(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, UnicodeSet paramUnicodeSet)
/*      */     {
/*  419 */       return NormalizerImpl.compose(paramArrayOfChar1, paramInt1, paramInt2, paramArrayOfChar2, paramInt3, paramInt4, 0, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected String normalize(String paramString, int paramInt)
/*      */     {
/*  425 */       return NormalizerBase.compose(paramString, false, paramInt);
/*      */     }
/*      */ 
/*      */     protected int getMinC() {
/*  429 */       return NormalizerImpl.getFromIndexesArr(6);
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsPrevBoundary getPrevBoundary()
/*      */     {
/*  434 */       return new NormalizerBase.IsPrevTrueStarter(null);
/*      */     }
/*      */     protected NormalizerBase.IsNextBoundary getNextBoundary() {
/*  437 */       return new NormalizerBase.IsNextTrueStarter(null);
/*      */     }
/*      */     protected int getMask() {
/*  440 */       return 65297;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.QuickCheckResult quickCheck(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean, UnicodeSet paramUnicodeSet)
/*      */     {
/*  445 */       return NormalizerImpl.quickCheck(paramArrayOfChar, paramInt1, paramInt2, NormalizerImpl.getFromIndexesArr(6), 17, 0, paramBoolean, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected boolean isNFSkippable(int paramInt)
/*      */     {
/*  457 */       return NormalizerImpl.isNFSkippable(paramInt, this, 65473L);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class NFDMode extends NormalizerBase.Mode
/*      */   {
/*      */     private NFDMode(int paramInt)
/*      */     {
/*  290 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int normalize(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, UnicodeSet paramUnicodeSet)
/*      */     {
/*  296 */       int[] arrayOfInt = new int[1];
/*  297 */       return NormalizerImpl.decompose(paramArrayOfChar1, paramInt1, paramInt2, paramArrayOfChar2, paramInt3, paramInt4, false, arrayOfInt, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected String normalize(String paramString, int paramInt)
/*      */     {
/*  303 */       return NormalizerBase.decompose(paramString, false, paramInt);
/*      */     }
/*      */ 
/*      */     protected int getMinC() {
/*  307 */       return 768;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsPrevBoundary getPrevBoundary() {
/*  311 */       return new NormalizerBase.IsPrevNFDSafe(null);
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsNextBoundary getNextBoundary() {
/*  315 */       return new NormalizerBase.IsNextNFDSafe(null);
/*      */     }
/*      */ 
/*      */     protected int getMask() {
/*  319 */       return 65284;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.QuickCheckResult quickCheck(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean, UnicodeSet paramUnicodeSet)
/*      */     {
/*  325 */       return NormalizerImpl.quickCheck(paramArrayOfChar, paramInt1, paramInt2, NormalizerImpl.getFromIndexesArr(8), 4, 0, paramBoolean, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected boolean isNFSkippable(int paramInt)
/*      */     {
/*  338 */       return NormalizerImpl.isNFSkippable(paramInt, this, 65284L);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class NFKCMode extends NormalizerBase.Mode
/*      */   {
/*      */     private NFKCMode(int paramInt)
/*      */     {
/*  473 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int normalize(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, UnicodeSet paramUnicodeSet)
/*      */     {
/*  478 */       return NormalizerImpl.compose(paramArrayOfChar1, paramInt1, paramInt2, paramArrayOfChar2, paramInt3, paramInt4, 4096, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected String normalize(String paramString, int paramInt)
/*      */     {
/*  484 */       return NormalizerBase.compose(paramString, true, paramInt);
/*      */     }
/*      */     protected int getMinC() {
/*  487 */       return NormalizerImpl.getFromIndexesArr(7);
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsPrevBoundary getPrevBoundary()
/*      */     {
/*  492 */       return new NormalizerBase.IsPrevTrueStarter(null);
/*      */     }
/*      */     protected NormalizerBase.IsNextBoundary getNextBoundary() {
/*  495 */       return new NormalizerBase.IsNextTrueStarter(null);
/*      */     }
/*      */     protected int getMask() {
/*  498 */       return 65314;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.QuickCheckResult quickCheck(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean, UnicodeSet paramUnicodeSet)
/*      */     {
/*  503 */       return NormalizerImpl.quickCheck(paramArrayOfChar, paramInt1, paramInt2, NormalizerImpl.getFromIndexesArr(7), 34, 4096, paramBoolean, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected boolean isNFSkippable(int paramInt)
/*      */     {
/*  515 */       return NormalizerImpl.isNFSkippable(paramInt, this, 65474L);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class NFKDMode extends NormalizerBase.Mode
/*      */   {
/*      */     private NFKDMode(int paramInt)
/*      */     {
/*  352 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int normalize(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2, int paramInt3, int paramInt4, UnicodeSet paramUnicodeSet)
/*      */     {
/*  358 */       int[] arrayOfInt = new int[1];
/*  359 */       return NormalizerImpl.decompose(paramArrayOfChar1, paramInt1, paramInt2, paramArrayOfChar2, paramInt3, paramInt4, true, arrayOfInt, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected String normalize(String paramString, int paramInt)
/*      */     {
/*  365 */       return NormalizerBase.decompose(paramString, true, paramInt);
/*      */     }
/*      */ 
/*      */     protected int getMinC() {
/*  369 */       return 768;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsPrevBoundary getPrevBoundary() {
/*  373 */       return new NormalizerBase.IsPrevNFDSafe(null);
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.IsNextBoundary getNextBoundary() {
/*  377 */       return new NormalizerBase.IsNextNFDSafe(null);
/*      */     }
/*      */ 
/*      */     protected int getMask() {
/*  381 */       return 65288;
/*      */     }
/*      */ 
/*      */     protected NormalizerBase.QuickCheckResult quickCheck(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean, UnicodeSet paramUnicodeSet)
/*      */     {
/*  387 */       return NormalizerImpl.quickCheck(paramArrayOfChar, paramInt1, paramInt2, NormalizerImpl.getFromIndexesArr(9), 8, 4096, paramBoolean, paramUnicodeSet);
/*      */     }
/*      */ 
/*      */     protected boolean isNFSkippable(int paramInt)
/*      */     {
/*  400 */       return NormalizerImpl.isNFSkippable(paramInt, this, 65288L);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class QuickCheckResult
/*      */   {
/*      */     private int resultValue;
/*      */ 
/*      */     private QuickCheckResult(int paramInt)
/*      */     {
/*  531 */       this.resultValue = paramInt;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.NormalizerBase
 * JD-Core Version:    0.6.2
 */