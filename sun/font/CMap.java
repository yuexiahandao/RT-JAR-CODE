/*      */ package sun.font;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.CodingErrorAction;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ abstract class CMap
/*      */ {
/*      */   static final short ShiftJISEncoding = 2;
/*      */   static final short GBKEncoding = 3;
/*      */   static final short Big5Encoding = 4;
/*      */   static final short WansungEncoding = 5;
/*      */   static final short JohabEncoding = 6;
/*      */   static final short MSUnicodeSurrogateEncoding = 10;
/*      */   static final char noSuchChar = '�';
/*      */   static final int SHORTMASK = 65535;
/*      */   static final int INTMASK = -1;
/*  135 */   static final char[][] converterMaps = new char[7][];
/*      */   char[] xlat;
/* 1042 */   public static final NullCMapClass theNullCmap = new NullCMapClass();
/*      */ 
/*      */   static CMap initialize(TrueTypeFont paramTrueTypeFont)
/*      */   {
/*  146 */     CMap localCMap = null;
/*      */ 
/*  148 */     int k = -1;
/*      */ 
/*  150 */     int m = 0; int n = 0; int i1 = 0; int i2 = 0; int i3 = 0; int i4 = 0;
/*  151 */     int i5 = 0; int i6 = 0;
/*  152 */     int i7 = 0;
/*      */ 
/*  154 */     ByteBuffer localByteBuffer = paramTrueTypeFont.getTableBuffer(1668112752);
/*  155 */     int i8 = paramTrueTypeFont.getTableSize(1668112752);
/*  156 */     int i9 = localByteBuffer.getShort(2);
/*      */ 
/*  159 */     for (int i10 = 0; i10 < i9; i10++) {
/*  160 */       localByteBuffer.position(i10 * 8 + 4);
/*  161 */       int j = localByteBuffer.getShort();
/*  162 */       if (j == 3) {
/*  163 */         i7 = 1;
/*  164 */         k = localByteBuffer.getShort();
/*  165 */         int i = localByteBuffer.getInt();
/*  166 */         switch (k) { case 0:
/*  167 */           m = i; break;
/*      */         case 1:
/*  168 */           n = i; break;
/*      */         case 2:
/*  169 */           i1 = i; break;
/*      */         case 3:
/*  170 */           i2 = i; break;
/*      */         case 4:
/*  171 */           i3 = i; break;
/*      */         case 5:
/*  172 */           i4 = i; break;
/*      */         case 6:
/*  173 */           i5 = i; break;
/*      */         case 10:
/*  174 */           i6 = i;
/*      */         case 7:
/*      */         case 8:
/*      */         case 9: }
/*      */       }
/*      */     }
/*  180 */     if (i7 != 0) {
/*  181 */       if (i6 != 0) {
/*  182 */         localCMap = createCMap(localByteBuffer, i6, null);
/*      */       }
/*  184 */       else if (m != 0)
/*      */       {
/*  214 */         localCMap = createCMap(localByteBuffer, m, null);
/*      */       }
/*  217 */       else if (n != 0) {
/*  218 */         localCMap = createCMap(localByteBuffer, n, null);
/*      */       }
/*  220 */       else if (i1 != 0) {
/*  221 */         localCMap = createCMap(localByteBuffer, i1, getConverterMap((short)2));
/*      */       }
/*  224 */       else if (i2 != 0) {
/*  225 */         localCMap = createCMap(localByteBuffer, i2, getConverterMap((short)3));
/*      */       }
/*  228 */       else if (i3 != 0)
/*      */       {
/*  235 */         if ((FontUtilities.isSolaris) && (paramTrueTypeFont.platName != null) && ((paramTrueTypeFont.platName.startsWith("/usr/openwin/lib/locale/zh_CN.EUC/X11/fonts/TrueType")) || (paramTrueTypeFont.platName.startsWith("/usr/openwin/lib/locale/zh_CN/X11/fonts/TrueType")) || (paramTrueTypeFont.platName.startsWith("/usr/openwin/lib/locale/zh/X11/fonts/TrueType"))))
/*      */         {
/*  242 */           localCMap = createCMap(localByteBuffer, i3, getConverterMap((short)3));
/*      */         }
/*      */         else
/*      */         {
/*  246 */           localCMap = createCMap(localByteBuffer, i3, getConverterMap((short)4));
/*      */         }
/*      */ 
/*      */       }
/*  250 */       else if (i4 != 0) {
/*  251 */         localCMap = createCMap(localByteBuffer, i4, getConverterMap((short)5));
/*      */       }
/*  254 */       else if (i5 != 0) {
/*  255 */         localCMap = createCMap(localByteBuffer, i5, getConverterMap((short)6));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  263 */       localCMap = createCMap(localByteBuffer, localByteBuffer.getInt(8), null);
/*      */     }
/*  265 */     return localCMap;
/*      */   }
/*      */ 
/*      */   static char[] getConverter(short paramShort)
/*      */   {
/*  272 */     int i = 32768;
/*  273 */     int j = 65535;
/*      */     String str;
/*  276 */     switch (paramShort) {
/*      */     case 2:
/*  278 */       i = 33088;
/*  279 */       j = 64764;
/*  280 */       str = "SJIS";
/*  281 */       break;
/*      */     case 3:
/*  283 */       i = 33088;
/*  284 */       j = 65184;
/*  285 */       str = "GBK";
/*  286 */       break;
/*      */     case 4:
/*  288 */       i = 41280;
/*  289 */       j = 65278;
/*  290 */       str = "Big5";
/*  291 */       break;
/*      */     case 5:
/*  293 */       i = 41377;
/*  294 */       j = 65246;
/*  295 */       str = "EUC_KR";
/*  296 */       break;
/*      */     case 6:
/*  298 */       i = 33089;
/*  299 */       j = 65022;
/*  300 */       str = "Johab";
/*  301 */       break;
/*      */     default:
/*  303 */       return null;
/*      */     }
/*      */     try
/*      */     {
/*  307 */       char[] arrayOfChar1 = new char[65536];
/*  308 */       for (int k = 0; k < 65536; k++) {
/*  309 */         arrayOfChar1[k] = 65533;
/*      */       }
/*      */ 
/*  312 */       byte[] arrayOfByte = new byte[(j - i + 1) * 2];
/*  313 */       char[] arrayOfChar2 = new char[j - i + 1];
/*      */ 
/*  315 */       int m = 0;
/*      */ 
/*  317 */       if (paramShort == 2)
/*  318 */         for (i1 = i; i1 <= j; i1++) {
/*  319 */           int n = i1 >> 8 & 0xFF;
/*  320 */           if ((n >= 161) && (n <= 223))
/*      */           {
/*  322 */             arrayOfByte[(m++)] = -1;
/*  323 */             arrayOfByte[(m++)] = -1;
/*      */           } else {
/*  325 */             arrayOfByte[(m++)] = ((byte)n);
/*  326 */             arrayOfByte[(m++)] = ((byte)(i1 & 0xFF));
/*      */           }
/*      */         }
/*      */       else {
/*  330 */         for (i1 = i; i1 <= j; i1++) {
/*  331 */           arrayOfByte[(m++)] = ((byte)(i1 >> 8 & 0xFF));
/*  332 */           arrayOfByte[(m++)] = ((byte)(i1 & 0xFF));
/*      */         }
/*      */       }
/*      */ 
/*  336 */       Charset.forName(str).newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("").decode(ByteBuffer.wrap(arrayOfByte, 0, arrayOfByte.length), CharBuffer.wrap(arrayOfChar2, 0, arrayOfChar2.length), true);
/*      */ 
/*  345 */       for (int i1 = 32; i1 <= 126; i1++) {
/*  346 */         arrayOfChar1[i1] = ((char)i1);
/*      */       }
/*      */ 
/*  350 */       if (paramShort == 2) {
/*  351 */         for (i1 = 161; i1 <= 223; i1++) {
/*  352 */           arrayOfChar1[i1] = ((char)(i1 - 161 + 65377));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  364 */       System.arraycopy(arrayOfChar2, 0, arrayOfChar1, i, arrayOfChar2.length);
/*      */ 
/*  371 */       char[] arrayOfChar3 = new char[65536];
/*  372 */       for (int i2 = 0; i2 < 65536; i2++) {
/*  373 */         if (arrayOfChar1[i2] != 65533) {
/*  374 */           arrayOfChar3[arrayOfChar1[i2]] = ((char)i2);
/*      */         }
/*      */       }
/*  377 */       return arrayOfChar3;
/*      */     }
/*      */     catch (Exception localException) {
/*  380 */       localException.printStackTrace();
/*      */     }
/*  382 */     return null;
/*      */   }
/*      */ 
/*      */   static char[] getConverterMap(short paramShort)
/*      */   {
/*  391 */     if (converterMaps[paramShort] == null) {
/*  392 */       converterMaps[paramShort] = getConverter(paramShort);
/*      */     }
/*  394 */     return converterMaps[paramShort];
/*      */   }
/*      */ 
/*      */   static CMap createCMap(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar)
/*      */   {
/*  402 */     int i = paramByteBuffer.getChar(paramInt);
/*      */     long l;
/*  404 */     if (i < 8)
/*  405 */       l = paramByteBuffer.getChar(paramInt + 2);
/*      */     else {
/*  407 */       l = paramByteBuffer.getInt(paramInt + 4) & 0xFFFFFFFF;
/*      */     }
/*  409 */     if ((paramInt + l > paramByteBuffer.capacity()) && 
/*  410 */       (FontUtilities.isLogging())) {
/*  411 */       FontUtilities.getLogger().warning("Cmap subtable overflows buffer.");
/*      */     }
/*      */ 
/*  414 */     switch (i) { case 0:
/*  415 */       return new CMapFormat0(paramByteBuffer, paramInt);
/*      */     case 2:
/*  416 */       return new CMapFormat2(paramByteBuffer, paramInt, paramArrayOfChar);
/*      */     case 4:
/*  417 */       return new CMapFormat4(paramByteBuffer, paramInt, paramArrayOfChar);
/*      */     case 6:
/*  418 */       return new CMapFormat6(paramByteBuffer, paramInt, paramArrayOfChar);
/*      */     case 8:
/*  419 */       return new CMapFormat8(paramByteBuffer, paramInt, paramArrayOfChar);
/*      */     case 10:
/*  420 */       return new CMapFormat10(paramByteBuffer, paramInt, paramArrayOfChar);
/*      */     case 12:
/*  421 */       return new CMapFormat12(paramByteBuffer, paramInt, paramArrayOfChar);
/*      */     case 1:
/*      */     case 3:
/*      */     case 5:
/*      */     case 7:
/*      */     case 9:
/*  422 */     case 11: } throw new RuntimeException("Cmap format unimplemented: " + paramByteBuffer.getChar(paramInt));
/*      */   }
/*      */ 
/*      */   abstract char getGlyph(int paramInt);
/*      */ 
/*      */   final int getControlCodeGlyph(int paramInt, boolean paramBoolean)
/*      */   {
/* 1045 */     if (paramInt < 16) {
/* 1046 */       switch (paramInt) { case 9:
/*      */       case 10:
/*      */       case 13:
/* 1049 */         return 65535;
/*      */       case 11:
/* 1051 */       case 12: }  } else if (paramInt >= 8204) {
/* 1052 */       if ((paramInt <= 8207) || ((paramInt >= 8232) && (paramInt <= 8238)) || ((paramInt >= 8298) && (paramInt <= 8303)))
/*      */       {
/* 1055 */         return 65535;
/* 1056 */       }if ((paramBoolean) && (paramInt >= 65535)) {
/* 1057 */         return 0;
/*      */       }
/*      */     }
/* 1060 */     return -1;
/*      */   }
/*      */ 
/*      */   static class CMapFormat0 extends CMap
/*      */   {
/*      */     byte[] cmap;
/*      */ 
/*      */     CMapFormat0(ByteBuffer paramByteBuffer, int paramInt)
/*      */     {
/*  623 */       int i = paramByteBuffer.getChar(paramInt + 2);
/*  624 */       this.cmap = new byte[i - 6];
/*  625 */       paramByteBuffer.position(paramInt + 6);
/*  626 */       paramByteBuffer.get(this.cmap);
/*      */     }
/*      */ 
/*      */     char getGlyph(int paramInt) {
/*  630 */       if (paramInt < 256) {
/*  631 */         if (paramInt < 16)
/*  632 */           switch (paramInt) { case 9:
/*      */           case 10:
/*      */           case 13:
/*  635 */             return 65535;
/*      */           case 11:
/*      */           case 12: }
/*  638 */         return (char)(0xFF & this.cmap[paramInt]);
/*      */       }
/*  640 */       return '\000';
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CMapFormat10 extends CMap
/*      */   {
/*      */     long firstCode;
/*      */     int entryCount;
/*      */     char[] glyphIdArray;
/*      */ 
/*      */     CMapFormat10(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar)
/*      */     {
/*  916 */       this.firstCode = (paramByteBuffer.getInt() & 0xFFFFFFFF);
/*  917 */       this.entryCount = (paramByteBuffer.getInt() & 0xFFFFFFFF);
/*  918 */       paramByteBuffer.position(paramInt + 20);
/*  919 */       CharBuffer localCharBuffer = paramByteBuffer.asCharBuffer();
/*  920 */       this.glyphIdArray = new char[this.entryCount];
/*  921 */       for (int i = 0; i < this.entryCount; i++)
/*  922 */         this.glyphIdArray[i] = localCharBuffer.get();
/*      */     }
/*      */ 
/*      */     char getGlyph(int paramInt)
/*      */     {
/*  928 */       if (this.xlat != null) {
/*  929 */         throw new RuntimeException("xlat array for cmap fmt=10");
/*      */       }
/*      */ 
/*  932 */       int i = (int)(paramInt - this.firstCode);
/*  933 */       if ((i < 0) || (i >= this.entryCount)) {
/*  934 */         return '\000';
/*      */       }
/*  936 */       return this.glyphIdArray[i];
/*      */     }
/*      */   }
/*      */   static class CMapFormat12 extends CMap {
/*      */     int numGroups;
/*  946 */     int highBit = 0;
/*      */     int power;
/*      */     int extra;
/*      */     long[] startCharCode;
/*      */     long[] endCharCode;
/*      */     int[] startGlyphID;
/*      */ 
/*  954 */     CMapFormat12(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar) { if (paramArrayOfChar != null) {
/*  955 */         throw new RuntimeException("xlat array for cmap fmt=12");
/*      */       }
/*      */ 
/*  958 */       this.numGroups = paramByteBuffer.getInt(paramInt + 12);
/*  959 */       this.startCharCode = new long[this.numGroups];
/*  960 */       this.endCharCode = new long[this.numGroups];
/*  961 */       this.startGlyphID = new int[this.numGroups];
/*  962 */       paramByteBuffer.position(paramInt + 16);
/*  963 */       paramByteBuffer = paramByteBuffer.slice();
/*  964 */       IntBuffer localIntBuffer = paramByteBuffer.asIntBuffer();
/*  965 */       for (int i = 0; i < this.numGroups; i++) {
/*  966 */         this.startCharCode[i] = (localIntBuffer.get() & 0xFFFFFFFF);
/*  967 */         this.endCharCode[i] = (localIntBuffer.get() & 0xFFFFFFFF);
/*  968 */         this.startGlyphID[i] = (localIntBuffer.get() & 0xFFFFFFFF);
/*      */       }
/*      */ 
/*  972 */       i = this.numGroups;
/*      */ 
/*  974 */       if (i >= 65536) {
/*  975 */         i >>= 16;
/*  976 */         this.highBit += 16;
/*      */       }
/*      */ 
/*  979 */       if (i >= 256) {
/*  980 */         i >>= 8;
/*  981 */         this.highBit += 8;
/*      */       }
/*      */ 
/*  984 */       if (i >= 16) {
/*  985 */         i >>= 4;
/*  986 */         this.highBit += 4;
/*      */       }
/*      */ 
/*  989 */       if (i >= 4) {
/*  990 */         i >>= 2;
/*  991 */         this.highBit += 2;
/*      */       }
/*      */ 
/*  994 */       if (i >= 2) {
/*  995 */         i >>= 1;
/*  996 */         this.highBit += 1;
/*      */       }
/*      */ 
/*  999 */       this.power = (1 << this.highBit);
/* 1000 */       this.extra = (this.numGroups - this.power); }
/*      */ 
/*      */     char getGlyph(int paramInt)
/*      */     {
/* 1004 */       int i = getControlCodeGlyph(paramInt, false);
/* 1005 */       if (i >= 0) {
/* 1006 */         return (char)i;
/*      */       }
/* 1008 */       int j = this.power;
/* 1009 */       int k = 0;
/*      */ 
/* 1011 */       if (this.startCharCode[this.extra] <= paramInt) {
/* 1012 */         k = this.extra;
/*      */       }
/*      */ 
/* 1015 */       while (j > 1) {
/* 1016 */         j >>= 1;
/*      */ 
/* 1018 */         if (this.startCharCode[(k + j)] <= paramInt) {
/* 1019 */           k += j;
/*      */         }
/*      */       }
/*      */ 
/* 1023 */       if ((this.startCharCode[k] <= paramInt) && (this.endCharCode[k] >= paramInt))
/*      */       {
/* 1025 */         return (char)(int)(this.startGlyphID[k] + (paramInt - this.startCharCode[k]));
/*      */       }
/*      */ 
/* 1029 */       return '\000';
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CMapFormat2 extends CMap
/*      */   {
/*  718 */     char[] subHeaderKey = new char[256];
/*      */     char[] firstCodeArray;
/*      */     char[] entryCountArray;
/*      */     short[] idDeltaArray;
/*      */     char[] idRangeOffSetArray;
/*      */     char[] glyphIndexArray;
/*      */ 
/*      */     CMapFormat2(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar)
/*      */     {
/*  736 */       this.xlat = paramArrayOfChar;
/*      */ 
/*  738 */       int i = paramByteBuffer.getChar(paramInt + 2);
/*  739 */       paramByteBuffer.position(paramInt + 6);
/*  740 */       CharBuffer localCharBuffer = paramByteBuffer.asCharBuffer();
/*  741 */       int j = 0;
/*  742 */       for (int k = 0; k < 256; k++) {
/*  743 */         this.subHeaderKey[k] = localCharBuffer.get();
/*  744 */         if (this.subHeaderKey[k] > j) {
/*  745 */           j = this.subHeaderKey[k];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  752 */       k = (j >> 3) + 1;
/*  753 */       this.firstCodeArray = new char[k];
/*  754 */       this.entryCountArray = new char[k];
/*  755 */       this.idDeltaArray = new short[k];
/*  756 */       this.idRangeOffSetArray = new char[k];
/*  757 */       for (int m = 0; m < k; m++) {
/*  758 */         this.firstCodeArray[m] = localCharBuffer.get();
/*  759 */         this.entryCountArray[m] = localCharBuffer.get();
/*  760 */         this.idDeltaArray[m] = ((short)localCharBuffer.get());
/*  761 */         this.idRangeOffSetArray[m] = localCharBuffer.get();
/*      */       }
/*      */ 
/*  768 */       m = (i - 518 - k * 8) / 2;
/*  769 */       this.glyphIndexArray = new char[m];
/*  770 */       for (int n = 0; n < m; n++)
/*  771 */         this.glyphIndexArray[n] = localCharBuffer.get();
/*      */     }
/*      */ 
/*      */     char getGlyph(int paramInt)
/*      */     {
/*  776 */       int i = getControlCodeGlyph(paramInt, true);
/*  777 */       if (i >= 0) {
/*  778 */         return (char)i;
/*      */       }
/*      */ 
/*  781 */       if (this.xlat != null) {
/*  782 */         paramInt = this.xlat[paramInt];
/*      */       }
/*      */ 
/*  785 */       int j = (char)(paramInt >> 8);
/*  786 */       int k = (char)(paramInt & 0xFF);
/*  787 */       int m = this.subHeaderKey[j] >> '\003';
/*      */ 
/*  790 */       if (m != 0) {
/*  791 */         n = k;
/*      */       } else {
/*  793 */         n = j;
/*  794 */         if (n == 0) {
/*  795 */           n = k;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  801 */       int i1 = this.firstCodeArray[m];
/*  802 */       if (n < i1) {
/*  803 */         return '\000';
/*      */       }
/*  805 */       int n = (char)(n - i1);
/*      */ 
/*  808 */       if (n < this.entryCountArray[m])
/*      */       {
/*  822 */         int i2 = (this.idRangeOffSetArray.length - m) * 8 - 6;
/*  823 */         int i3 = (this.idRangeOffSetArray[m] - i2) / 2;
/*      */ 
/*  825 */         int i4 = this.glyphIndexArray[(i3 + n)];
/*  826 */         if (i4 != 0) {
/*  827 */           i4 = (char)(i4 + this.idDeltaArray[m]);
/*  828 */           return i4;
/*      */         }
/*      */       }
/*  831 */       return '\000';
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CMapFormat4 extends CMap
/*      */   {
/*      */     int segCount;
/*      */     int entrySelector;
/*      */     int rangeShift;
/*      */     char[] endCount;
/*      */     char[] startCount;
/*      */     short[] idDelta;
/*      */     char[] idRangeOffset;
/*      */     char[] glyphIds;
/*      */ 
/*      */     CMapFormat4(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar)
/*      */     {
/*  465 */       this.xlat = paramArrayOfChar;
/*      */ 
/*  467 */       paramByteBuffer.position(paramInt);
/*  468 */       CharBuffer localCharBuffer = paramByteBuffer.asCharBuffer();
/*  469 */       localCharBuffer.get();
/*  470 */       int i = localCharBuffer.get();
/*      */ 
/*  479 */       if (paramInt + i > paramByteBuffer.capacity()) {
/*  480 */         i = paramByteBuffer.capacity() - paramInt;
/*      */       }
/*  482 */       localCharBuffer.get();
/*  483 */       this.segCount = (localCharBuffer.get() / '\002');
/*  484 */       int j = localCharBuffer.get();
/*  485 */       this.entrySelector = localCharBuffer.get();
/*  486 */       this.rangeShift = (localCharBuffer.get() / '\002');
/*  487 */       this.startCount = new char[this.segCount];
/*  488 */       this.endCount = new char[this.segCount];
/*  489 */       this.idDelta = new short[this.segCount];
/*  490 */       this.idRangeOffset = new char[this.segCount];
/*      */ 
/*  492 */       for (int k = 0; k < this.segCount; k++) {
/*  493 */         this.endCount[k] = localCharBuffer.get();
/*      */       }
/*  495 */       localCharBuffer.get();
/*  496 */       for (k = 0; k < this.segCount; k++) {
/*  497 */         this.startCount[k] = localCharBuffer.get();
/*      */       }
/*      */ 
/*  500 */       for (k = 0; k < this.segCount; k++) {
/*  501 */         this.idDelta[k] = ((short)localCharBuffer.get());
/*      */       }
/*      */ 
/*  504 */       for (k = 0; k < this.segCount; k++) {
/*  505 */         m = localCharBuffer.get();
/*  506 */         this.idRangeOffset[k] = ((char)(m >> 1 & 0xFFFF));
/*      */       }
/*      */ 
/*  511 */       k = (this.segCount * 8 + 16) / 2;
/*  512 */       localCharBuffer.position(k);
/*  513 */       int m = i / 2 - k;
/*  514 */       this.glyphIds = new char[m];
/*  515 */       for (int n = 0; n < m; n++)
/*  516 */         this.glyphIds[n] = localCharBuffer.get();
/*      */     }
/*      */ 
/*      */     char getGlyph(int paramInt)
/*      */     {
/*  538 */       int i = 0;
/*  539 */       int j = 0;
/*      */ 
/*  541 */       int k = getControlCodeGlyph(paramInt, true);
/*  542 */       if (k >= 0) {
/*  543 */         return (char)k;
/*      */       }
/*      */ 
/*  553 */       if (this.xlat != null) {
/*  554 */         paramInt = this.xlat[paramInt];
/*      */       }
/*      */ 
/*  575 */       int m = 0; int n = this.startCount.length;
/*  576 */       i = this.startCount.length >> 1;
/*  577 */       while (m < n) {
/*  578 */         if (this.endCount[i] < paramInt)
/*  579 */           m = i + 1;
/*      */         else {
/*  581 */           n = i;
/*      */         }
/*  583 */         i = m + n >> 1;
/*      */       }
/*      */ 
/*  586 */       if ((paramInt >= this.startCount[i]) && (paramInt <= this.endCount[i])) {
/*  587 */         int i1 = this.idRangeOffset[i];
/*      */ 
/*  589 */         if (i1 == 0) {
/*  590 */           j = (char)(paramInt + this.idDelta[i]);
/*      */         }
/*      */         else
/*      */         {
/*  601 */           int i2 = i1 - this.segCount + i + (paramInt - this.startCount[i]);
/*      */ 
/*  603 */           j = this.glyphIds[i2];
/*  604 */           if (j != 0) {
/*  605 */             j = (char)(j + this.idDelta[i]);
/*      */           }
/*      */         }
/*      */       }
/*  609 */       if (j != 0);
/*  612 */       return j;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CMapFormat6 extends CMap
/*      */   {
/*      */     char firstCode;
/*      */     char entryCount;
/*      */     char[] glyphIdArray;
/*      */ 
/*      */     CMapFormat6(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar)
/*      */     {
/*  844 */       paramByteBuffer.position(paramInt + 6);
/*  845 */       CharBuffer localCharBuffer = paramByteBuffer.asCharBuffer();
/*  846 */       this.firstCode = localCharBuffer.get();
/*  847 */       this.entryCount = localCharBuffer.get();
/*  848 */       this.glyphIdArray = new char[this.entryCount];
/*  849 */       for (int i = 0; i < this.entryCount; i++)
/*  850 */         this.glyphIdArray[i] = localCharBuffer.get();
/*      */     }
/*      */ 
/*      */     char getGlyph(int paramInt)
/*      */     {
/*  855 */       int i = getControlCodeGlyph(paramInt, true);
/*  856 */       if (i >= 0) {
/*  857 */         return (char)i;
/*      */       }
/*      */ 
/*  860 */       if (this.xlat != null) {
/*  861 */         paramInt = this.xlat[paramInt];
/*      */       }
/*      */ 
/*  864 */       paramInt -= this.firstCode;
/*  865 */       if ((paramInt < 0) || (paramInt >= this.entryCount)) {
/*  866 */         return '\000';
/*      */       }
/*  868 */       return this.glyphIdArray[paramInt];
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CMapFormat8 extends CMap
/*      */   {
/*  878 */     byte[] is32 = new byte[8192];
/*      */     int nGroups;
/*      */     int[] startCharCode;
/*      */     int[] endCharCode;
/*      */     int[] startGlyphID;
/*      */ 
/*      */     CMapFormat8(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar)
/*      */     {
/*  886 */       paramByteBuffer.position(12);
/*  887 */       paramByteBuffer.get(this.is32);
/*  888 */       this.nGroups = paramByteBuffer.getInt();
/*  889 */       this.startCharCode = new int[this.nGroups];
/*  890 */       this.endCharCode = new int[this.nGroups];
/*  891 */       this.startGlyphID = new int[this.nGroups];
/*      */     }
/*      */ 
/*      */     char getGlyph(int paramInt) {
/*  895 */       if (this.xlat != null) {
/*  896 */         throw new RuntimeException("xlat array for cmap fmt=8");
/*      */       }
/*  898 */       return '\000';
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NullCMapClass extends CMap
/*      */   {
/*      */     char getGlyph(int paramInt)
/*      */     {
/* 1038 */       return '\000';
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.CMap
 * JD-Core Version:    0.6.2
 */