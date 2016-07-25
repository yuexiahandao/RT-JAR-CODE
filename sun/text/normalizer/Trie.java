/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public abstract class Trie
/*     */ {
/*     */   protected static final int LEAD_INDEX_OFFSET_ = 320;
/*     */   protected static final int INDEX_STAGE_1_SHIFT_ = 5;
/*     */   protected static final int INDEX_STAGE_2_SHIFT_ = 2;
/*     */   protected static final int DATA_BLOCK_LENGTH = 32;
/*     */   protected static final int INDEX_STAGE_3_MASK_ = 31;
/*     */   protected static final int SURROGATE_BLOCK_BITS = 5;
/*     */   protected static final int SURROGATE_BLOCK_COUNT = 32;
/*     */   protected static final int BMP_INDEX_LENGTH = 2048;
/*     */   protected static final int SURROGATE_MASK_ = 1023;
/*     */   protected char[] m_index_;
/*     */   protected DataManipulate m_dataManipulate_;
/*     */   protected int m_dataOffset_;
/*     */   protected int m_dataLength_;
/*     */   protected static final int HEADER_OPTIONS_LATIN1_IS_LINEAR_MASK_ = 512;
/*     */   protected static final int HEADER_SIGNATURE_ = 1416784229;
/*     */   private static final int HEADER_OPTIONS_SHIFT_MASK_ = 15;
/*     */   protected static final int HEADER_OPTIONS_INDEX_SHIFT_ = 4;
/*     */   protected static final int HEADER_OPTIONS_DATA_IS_32_BIT_ = 256;
/*     */   private boolean m_isLatin1Linear_;
/*     */   private int m_options_;
/*     */ 
/*     */   protected Trie(InputStream paramInputStream, DataManipulate paramDataManipulate)
/*     */     throws IOException
/*     */   {
/* 117 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/*     */ 
/* 119 */     int i = localDataInputStream.readInt();
/* 120 */     this.m_options_ = localDataInputStream.readInt();
/*     */ 
/* 122 */     if (!checkHeader(i)) {
/* 123 */       throw new IllegalArgumentException("ICU data file error: Trie header authentication failed, please check if you have the most updated ICU data file");
/*     */     }
/*     */ 
/* 126 */     if (paramDataManipulate != null)
/* 127 */       this.m_dataManipulate_ = paramDataManipulate;
/*     */     else {
/* 129 */       this.m_dataManipulate_ = new DefaultGetFoldingOffset(null);
/*     */     }
/* 131 */     this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0);
/*     */ 
/* 133 */     this.m_dataOffset_ = localDataInputStream.readInt();
/* 134 */     this.m_dataLength_ = localDataInputStream.readInt();
/* 135 */     unserialize(paramInputStream);
/*     */   }
/*     */ 
/*     */   protected Trie(char[] paramArrayOfChar, int paramInt, DataManipulate paramDataManipulate)
/*     */   {
/* 147 */     this.m_options_ = paramInt;
/* 148 */     if (paramDataManipulate != null)
/* 149 */       this.m_dataManipulate_ = paramDataManipulate;
/*     */     else {
/* 151 */       this.m_dataManipulate_ = new DefaultGetFoldingOffset(null);
/*     */     }
/* 153 */     this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0);
/*     */ 
/* 155 */     this.m_index_ = paramArrayOfChar;
/* 156 */     this.m_dataOffset_ = this.m_index_.length;
/*     */   }
/*     */ 
/*     */   protected abstract int getSurrogateOffset(char paramChar1, char paramChar2);
/*     */ 
/*     */   protected abstract int getValue(int paramInt);
/*     */ 
/*     */   protected abstract int getInitialValue();
/*     */ 
/*     */   protected final int getRawOffset(int paramInt, char paramChar)
/*     */   {
/* 264 */     return (this.m_index_[(paramInt + (paramChar >> '\005'))] << '\002') + (paramChar & 0x1F);
/*     */   }
/*     */ 
/*     */   protected final int getBMPOffset(char paramChar)
/*     */   {
/* 277 */     return (paramChar >= 55296) && (paramChar <= 56319) ? getRawOffset(320, paramChar) : getRawOffset(0, paramChar);
/*     */   }
/*     */ 
/*     */   protected final int getLeadOffset(char paramChar)
/*     */   {
/* 294 */     return getRawOffset(0, paramChar);
/*     */   }
/*     */ 
/*     */   protected final int getCodePointOffset(int paramInt)
/*     */   {
/* 308 */     if (paramInt < 0)
/* 309 */       return -1;
/* 310 */     if (paramInt < 55296)
/*     */     {
/* 312 */       return getRawOffset(0, (char)paramInt);
/* 313 */     }if (paramInt < 65536)
/*     */     {
/* 315 */       return getBMPOffset((char)paramInt);
/* 316 */     }if (paramInt <= 1114111)
/*     */     {
/* 319 */       return getSurrogateOffset(UTF16.getLeadSurrogate(paramInt), (char)(paramInt & 0x3FF));
/*     */     }
/*     */ 
/* 323 */     return -1;
/*     */   }
/*     */ 
/*     */   protected void unserialize(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 336 */     this.m_index_ = new char[this.m_dataOffset_];
/* 337 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/* 338 */     for (int i = 0; i < this.m_dataOffset_; i++)
/* 339 */       this.m_index_[i] = localDataInputStream.readChar();
/*     */   }
/*     */ 
/*     */   protected final boolean isIntTrie()
/*     */   {
/* 349 */     return (this.m_options_ & 0x100) != 0;
/*     */   }
/*     */ 
/*     */   protected final boolean isCharTrie()
/*     */   {
/* 358 */     return (this.m_options_ & 0x100) == 0;
/*     */   }
/*     */ 
/*     */   private final boolean checkHeader(int paramInt)
/*     */   {
/* 406 */     if (paramInt != 1416784229) {
/* 407 */       return false;
/*     */     }
/*     */ 
/* 410 */     if (((this.m_options_ & 0xF) != 5) || ((this.m_options_ >> 4 & 0xF) != 2))
/*     */     {
/* 415 */       return false;
/*     */     }
/* 417 */     return true;
/*     */   }
/*     */ 
/*     */   public static abstract interface DataManipulate
/*     */   {
/*     */     public abstract int getFoldingOffset(int paramInt);
/*     */   }
/*     */ 
/*     */   private static class DefaultGetFoldingOffset
/*     */     implements Trie.DataManipulate
/*     */   {
/*     */     public int getFoldingOffset(int paramInt)
/*     */     {
/*  99 */       return paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.Trie
 * JD-Core Version:    0.6.2
 */