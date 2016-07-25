/*     */ package sun.net.idn;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.Normalizer.Form;
/*     */ import java.text.ParseException;
/*     */ import sun.text.Normalizer;
/*     */ import sun.text.normalizer.CharTrie;
/*     */ import sun.text.normalizer.NormalizerImpl;
/*     */ import sun.text.normalizer.Trie.DataManipulate;
/*     */ import sun.text.normalizer.UCharacter;
/*     */ import sun.text.normalizer.UCharacterIterator;
/*     */ import sun.text.normalizer.UTF16;
/*     */ import sun.text.normalizer.VersionInfo;
/*     */ 
/*     */ public final class StringPrep
/*     */ {
/*     */   public static final int DEFAULT = 0;
/*     */   public static final int ALLOW_UNASSIGNED = 1;
/*     */   private static final int UNASSIGNED = 0;
/*     */   private static final int MAP = 1;
/*     */   private static final int PROHIBITED = 2;
/*     */   private static final int DELETE = 3;
/*     */   private static final int TYPE_LIMIT = 4;
/*     */   private static final int NORMALIZATION_ON = 1;
/*     */   private static final int CHECK_BIDI_ON = 2;
/*     */   private static final int TYPE_THRESHOLD = 65520;
/*     */   private static final int MAX_INDEX_VALUE = 16319;
/*     */   private static final int MAX_INDEX_TOP_LENGTH = 3;
/*     */   private static final int INDEX_TRIE_SIZE = 0;
/*     */   private static final int INDEX_MAPPING_DATA_SIZE = 1;
/*     */   private static final int NORM_CORRECTNS_LAST_UNI_VERSION = 2;
/*     */   private static final int ONE_UCHAR_MAPPING_INDEX_START = 3;
/*     */   private static final int TWO_UCHARS_MAPPING_INDEX_START = 4;
/*     */   private static final int THREE_UCHARS_MAPPING_INDEX_START = 5;
/*     */   private static final int FOUR_UCHARS_MAPPING_INDEX_START = 6;
/*     */   private static final int OPTIONS = 7;
/*     */   private static final int INDEX_TOP = 16;
/*     */   private static final int DATA_BUFFER_SIZE = 25000;
/*     */   private StringPrepTrieImpl sprepTrieImpl;
/*     */   private int[] indexes;
/*     */   private char[] mappingData;
/*     */   private byte[] formatVersion;
/*     */   private VersionInfo sprepUniVer;
/*     */   private VersionInfo normCorrVer;
/*     */   private boolean doNFKC;
/*     */   private boolean checkBiDi;
/*     */ 
/*     */   private char getCodePointValue(int paramInt)
/*     */   {
/* 177 */     return this.sprepTrieImpl.sprepTrie.getCodePointValue(paramInt);
/*     */   }
/*     */ 
/*     */   private static VersionInfo getVersionInfo(int paramInt) {
/* 181 */     int i = paramInt & 0xFF;
/* 182 */     int j = paramInt >> 8 & 0xFF;
/* 183 */     int k = paramInt >> 16 & 0xFF;
/* 184 */     int m = paramInt >> 24 & 0xFF;
/* 185 */     return VersionInfo.getInstance(m, k, j, i);
/*     */   }
/*     */   private static VersionInfo getVersionInfo(byte[] paramArrayOfByte) {
/* 188 */     if (paramArrayOfByte.length != 4) {
/* 189 */       return null;
/*     */     }
/* 191 */     return VersionInfo.getInstance(paramArrayOfByte[0], paramArrayOfByte[1], paramArrayOfByte[2], paramArrayOfByte[3]);
/*     */   }
/*     */ 
/*     */   public StringPrep(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 204 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 25000);
/*     */ 
/* 206 */     StringPrepDataReader localStringPrepDataReader = new StringPrepDataReader(localBufferedInputStream);
/*     */ 
/* 209 */     this.indexes = localStringPrepDataReader.readIndexes(16);
/*     */ 
/* 211 */     byte[] arrayOfByte = new byte[this.indexes[0]];
/*     */ 
/* 215 */     this.mappingData = new char[this.indexes[1] / 2];
/*     */ 
/* 217 */     localStringPrepDataReader.read(arrayOfByte, this.mappingData);
/*     */ 
/* 219 */     this.sprepTrieImpl = new StringPrepTrieImpl(null);
/* 220 */     this.sprepTrieImpl.sprepTrie = new CharTrie(new ByteArrayInputStream(arrayOfByte), this.sprepTrieImpl);
/*     */ 
/* 223 */     this.formatVersion = localStringPrepDataReader.getDataFormatVersion();
/*     */ 
/* 226 */     this.doNFKC = ((this.indexes[7] & 0x1) > 0);
/* 227 */     this.checkBiDi = ((this.indexes[7] & 0x2) > 0);
/* 228 */     this.sprepUniVer = getVersionInfo(localStringPrepDataReader.getUnicodeVersion());
/* 229 */     this.normCorrVer = getVersionInfo(this.indexes[2]);
/* 230 */     VersionInfo localVersionInfo = NormalizerImpl.getUnicodeVersion();
/* 231 */     if ((localVersionInfo.compareTo(this.sprepUniVer) < 0) && (localVersionInfo.compareTo(this.normCorrVer) < 0) && ((this.indexes[7] & 0x1) > 0))
/*     */     {
/* 235 */       throw new IOException("Normalization Correction version not supported");
/*     */     }
/* 237 */     localBufferedInputStream.close();
/*     */   }
/*     */ 
/*     */   private static final void getValues(char paramChar, Values paramValues)
/*     */   {
/* 252 */     paramValues.reset();
/* 253 */     if (paramChar == 0)
/*     */     {
/* 259 */       paramValues.type = 4;
/* 260 */     } else if (paramChar >= 65520) {
/* 261 */       paramValues.type = (paramChar - 65520);
/*     */     }
/*     */     else {
/* 264 */       paramValues.type = 1;
/*     */ 
/* 266 */       if ((paramChar & 0x2) > 0) {
/* 267 */         paramValues.isIndex = true;
/* 268 */         paramValues.value = (paramChar >> '\002');
/*     */       }
/*     */       else {
/* 271 */         paramValues.isIndex = false;
/* 272 */         paramValues.value = (paramChar << '\020' >> 16);
/* 273 */         paramValues.value >>= 2;
/*     */       }
/*     */ 
/* 277 */       if (paramChar >> '\002' == 16319) {
/* 278 */         paramValues.type = 3;
/* 279 */         paramValues.isIndex = false;
/* 280 */         paramValues.value = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private StringBuffer map(UCharacterIterator paramUCharacterIterator, int paramInt)
/*     */     throws ParseException
/*     */   {
/* 290 */     Values localValues = new Values(null);
/* 291 */     char c = '\000';
/* 292 */     int i = -1;
/* 293 */     StringBuffer localStringBuffer = new StringBuffer();
/* 294 */     int j = (paramInt & 0x1) > 0 ? 1 : 0;
/*     */ 
/* 296 */     while ((i = paramUCharacterIterator.nextCodePoint()) != -1)
/*     */     {
/* 298 */       c = getCodePointValue(i);
/* 299 */       getValues(c, localValues);
/*     */ 
/* 302 */       if ((localValues.type == 0) && (j == 0)) {
/* 303 */         throw new ParseException("An unassigned code point was found in the input " + paramUCharacterIterator.getText(), paramUCharacterIterator.getIndex());
/*     */       }
/* 305 */       if (localValues.type == 1)
/*     */       {
/* 308 */         if (localValues.isIndex) {
/* 309 */           int k = localValues.value;
/*     */           int m;
/* 310 */           if ((k >= this.indexes[3]) && (k < this.indexes[4]))
/*     */           {
/* 312 */             m = 1;
/* 313 */           } else if ((k >= this.indexes[4]) && (k < this.indexes[5]))
/*     */           {
/* 315 */             m = 2;
/* 316 */           } else if ((k >= this.indexes[5]) && (k < this.indexes[6]))
/*     */           {
/* 318 */             m = 3;
/*     */           }
/* 320 */           else m = this.mappingData[(k++)];
/*     */ 
/* 323 */           localStringBuffer.append(this.mappingData, k, m);
/*     */         }
/*     */         else
/*     */         {
/* 327 */           i -= localValues.value;
/*     */         }
/* 329 */       } else if (localValues.type != 3)
/*     */       {
/* 334 */         UTF16.append(localStringBuffer, i);
/*     */       }
/*     */     }
/* 337 */     return localStringBuffer;
/*     */   }
/*     */ 
/*     */   private StringBuffer normalize(StringBuffer paramStringBuffer)
/*     */   {
/* 353 */     return new StringBuffer(Normalizer.normalize(paramStringBuffer.toString(), Normalizer.Form.NFKC, 262432));
/*     */   }
/*     */ 
/*     */   public StringBuffer prepare(UCharacterIterator paramUCharacterIterator, int paramInt)
/*     */     throws ParseException
/*     */   {
/* 427 */     StringBuffer localStringBuffer1 = map(paramUCharacterIterator, paramInt);
/* 428 */     StringBuffer localStringBuffer2 = localStringBuffer1;
/*     */ 
/* 430 */     if (this.doNFKC)
/*     */     {
/* 432 */       localStringBuffer2 = normalize(localStringBuffer1);
/*     */     }
/*     */ 
/* 437 */     UCharacterIterator localUCharacterIterator = UCharacterIterator.getInstance(localStringBuffer2);
/* 438 */     Values localValues = new Values(null);
/* 439 */     int j = 19;
/* 440 */     int k = 19;
/* 441 */     int m = -1; int n = -1;
/* 442 */     int i1 = 0; int i2 = 0;
/*     */     int i;
/* 444 */     while ((i = localUCharacterIterator.nextCodePoint()) != -1) {
/* 445 */       char c = getCodePointValue(i);
/* 446 */       getValues(c, localValues);
/*     */ 
/* 448 */       if (localValues.type == 2) {
/* 449 */         throw new ParseException("A prohibited code point was found in the input" + localUCharacterIterator.getText(), localValues.value);
/*     */       }
/*     */ 
/* 453 */       j = UCharacter.getDirection(i);
/* 454 */       if (k == 19) {
/* 455 */         k = j;
/*     */       }
/* 457 */       if (j == 0) {
/* 458 */         i2 = 1;
/* 459 */         n = localUCharacterIterator.getIndex() - 1;
/*     */       }
/* 461 */       if ((j == 1) || (j == 13)) {
/* 462 */         i1 = 1;
/* 463 */         m = localUCharacterIterator.getIndex() - 1;
/*     */       }
/*     */     }
/* 466 */     if (this.checkBiDi == true)
/*     */     {
/* 468 */       if ((i2 == 1) && (i1 == 1)) {
/* 469 */         throw new ParseException("The input does not conform to the rules for BiDi code points." + localUCharacterIterator.getText(), m > n ? m : n);
/*     */       }
/*     */ 
/* 475 */       if ((i1 == 1) && (((k != 1) && (k != 13)) || ((j != 1) && (j != 13))))
/*     */       {
/* 479 */         throw new ParseException("The input does not conform to the rules for BiDi code points." + localUCharacterIterator.getText(), m > n ? m : n);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 484 */     return localStringBuffer2;
/*     */   }
/*     */ 
/*     */   private static final class StringPrepTrieImpl
/*     */     implements Trie.DataManipulate
/*     */   {
/* 143 */     private CharTrie sprepTrie = null;
/*     */ 
/*     */     public int getFoldingOffset(int paramInt)
/*     */     {
/* 152 */       return paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Values
/*     */   {
/*     */     boolean isIndex;
/*     */     int value;
/*     */     int type;
/*     */ 
/*     */     public void reset()
/*     */     {
/* 245 */       this.isIndex = false;
/* 246 */       this.value = 0;
/* 247 */       this.type = -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.idn.StringPrep
 * JD-Core Version:    0.6.2
 */