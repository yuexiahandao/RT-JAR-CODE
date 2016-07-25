/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.MissingResourceException;
/*     */ 
/*     */ public final class UCharacter
/*     */ {
/*     */   public static final int MIN_VALUE = 0;
/*     */   public static final int MAX_VALUE = 1114111;
/*     */   public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
/*     */   private static final UCharacterProperty PROPERTY_;
/*     */   private static final char[] PROPERTY_TRIE_INDEX_;
/*     */   private static final char[] PROPERTY_TRIE_DATA_;
/*     */   private static final int PROPERTY_INITIAL_VALUE_;
/* 333 */   private static final UBiDiProps gBdp = localUBiDiProps;
/*     */   private static final int NUMERIC_TYPE_SHIFT_ = 5;
/*     */   private static final int NUMERIC_TYPE_MASK_ = 224;
/*     */ 
/*     */   public static int digit(int paramInt1, int paramInt2)
/*     */   {
/* 235 */     int i = getProperty(paramInt1);
/*     */     int j;
/* 237 */     if (getNumericType(i) == 1)
/* 238 */       j = UCharacterProperty.getUnsignedValue(i);
/*     */     else {
/* 240 */       j = getEuropeanDigit(paramInt1);
/*     */     }
/* 242 */     return (0 <= j) && (j < paramInt2) ? j : -1;
/*     */   }
/*     */ 
/*     */   public static int getDirection(int paramInt)
/*     */   {
/* 257 */     return gBdp.getClass(paramInt);
/*     */   }
/*     */ 
/*     */   public static int getCodePoint(char paramChar1, char paramChar2)
/*     */   {
/* 271 */     if ((UTF16.isLeadSurrogate(paramChar1)) && (UTF16.isTrailSurrogate(paramChar2))) {
/* 272 */       return UCharacterProperty.getRawSupplementary(paramChar1, paramChar2);
/*     */     }
/* 274 */     throw new IllegalArgumentException("Illegal surrogate characters");
/*     */   }
/*     */ 
/*     */   public static VersionInfo getAge(int paramInt)
/*     */   {
/* 291 */     if ((paramInt < 0) || (paramInt > 1114111)) {
/* 292 */       throw new IllegalArgumentException("Codepoint out of bounds");
/*     */     }
/* 294 */     return PROPERTY_.getAge(paramInt);
/*     */   }
/*     */ 
/*     */   private static int getEuropeanDigit(int paramInt)
/*     */   {
/* 356 */     if (((paramInt > 122) && (paramInt < 65313)) || (paramInt < 65) || ((paramInt > 90) && (paramInt < 97)) || (paramInt > 65370) || ((paramInt > 65338) && (paramInt < 65345)))
/*     */     {
/* 359 */       return -1;
/*     */     }
/* 361 */     if (paramInt <= 122)
/*     */     {
/* 363 */       return paramInt + 10 - (paramInt <= 90 ? 65 : 97);
/*     */     }
/*     */ 
/* 366 */     if (paramInt <= 65338) {
/* 367 */       return paramInt + 10 - 65313;
/*     */     }
/*     */ 
/* 370 */     return paramInt + 10 - 65345;
/*     */   }
/*     */ 
/*     */   private static int getNumericType(int paramInt)
/*     */   {
/* 380 */     return (paramInt & 0xE0) >> 5;
/*     */   }
/*     */ 
/*     */   private static final int getProperty(int paramInt)
/*     */   {
/* 397 */     if ((paramInt < 55296) || ((paramInt > 56319) && (paramInt < 65536)))
/*     */     {
/*     */       try
/*     */       {
/* 402 */         return PROPERTY_TRIE_DATA_[((PROPERTY_TRIE_INDEX_[(paramInt >> 5)] << '\002') + (paramInt & 0x1F))];
/*     */       }
/*     */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */       {
/* 406 */         return PROPERTY_INITIAL_VALUE_;
/*     */       }
/*     */     }
/* 409 */     if (paramInt <= 56319)
/*     */     {
/* 411 */       return PROPERTY_TRIE_DATA_[((PROPERTY_TRIE_INDEX_[(320 + (paramInt >> 5))] << '\002') + (paramInt & 0x1F))];
/*     */     }
/*     */ 
/* 416 */     if (paramInt <= 1114111)
/*     */     {
/* 420 */       return PROPERTY_.m_trie_.getSurrogateValue(UTF16.getLeadSurrogate(paramInt), (char)(paramInt & 0x3FF));
/*     */     }
/*     */ 
/* 428 */     return PROPERTY_INITIAL_VALUE_;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 317 */       PROPERTY_ = UCharacterProperty.getInstance();
/* 318 */       PROPERTY_TRIE_INDEX_ = PROPERTY_.m_trieIndex_;
/* 319 */       PROPERTY_TRIE_DATA_ = PROPERTY_.m_trieData_;
/* 320 */       PROPERTY_INITIAL_VALUE_ = PROPERTY_.m_trieInitialValue_;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 324 */       throw new MissingResourceException(localException.getMessage(), "", "");
/*     */     }
/*     */     UBiDiProps localUBiDiProps;
/*     */     try
/*     */     {
/* 329 */       localUBiDiProps = UBiDiProps.getSingleton();
/*     */     } catch (IOException localIOException) {
/* 331 */       localUBiDiProps = UBiDiProps.getDummy();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface NumericType
/*     */   {
/*     */     public static final int DECIMAL = 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.UCharacter
 * JD-Core Version:    0.6.2
 */