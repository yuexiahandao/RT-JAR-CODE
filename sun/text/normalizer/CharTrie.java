/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class CharTrie extends Trie
/*     */ {
/*     */   private char m_initialValue_;
/*     */   private char[] m_data_;
/*     */   private FriendAgent m_friendAgent_;
/*     */ 
/*     */   public CharTrie(InputStream paramInputStream, Trie.DataManipulate paramDataManipulate)
/*     */     throws IOException
/*     */   {
/*  70 */     super(paramInputStream, paramDataManipulate);
/*     */ 
/*  72 */     if (!isCharTrie()) {
/*  73 */       throw new IllegalArgumentException("Data given does not belong to a char trie.");
/*     */     }
/*     */ 
/*  76 */     this.m_friendAgent_ = new FriendAgent();
/*     */   }
/*     */ 
/*     */   public CharTrie(int paramInt1, int paramInt2, Trie.DataManipulate paramDataManipulate)
/*     */   {
/*  94 */     super(new char[2080], 512, paramDataManipulate);
/*     */     int j;
/* 102 */     int i = j = 256;
/* 103 */     if (paramInt2 != paramInt1) {
/* 104 */       i += 32;
/*     */     }
/* 106 */     this.m_data_ = new char[i];
/* 107 */     this.m_dataLength_ = i;
/*     */ 
/* 109 */     this.m_initialValue_ = ((char)paramInt1);
/*     */ 
/* 116 */     for (int k = 0; k < j; k++) {
/* 117 */       this.m_data_[k] = ((char)paramInt1);
/*     */     }
/*     */ 
/* 120 */     if (paramInt2 != paramInt1)
/*     */     {
/* 122 */       int n = (char)(j >> 2);
/* 123 */       k = 1728;
/* 124 */       int m = 1760;
/* 125 */       for (; k < m; k++) {
/* 126 */         this.m_index_[k] = n;
/*     */       }
/*     */ 
/* 130 */       m = j + 32;
/* 131 */       for (k = j; k < m; k++) {
/* 132 */         this.m_data_[k] = ((char)paramInt2);
/*     */       }
/*     */     }
/*     */ 
/* 136 */     this.m_friendAgent_ = new FriendAgent();
/*     */   }
/*     */ 
/*     */   public void putIndexData(UCharacterProperty paramUCharacterProperty)
/*     */   {
/* 179 */     paramUCharacterProperty.setIndexData(this.m_friendAgent_);
/*     */   }
/*     */ 
/*     */   public final char getCodePointValue(int paramInt)
/*     */   {
/* 195 */     if ((0 <= paramInt) && (paramInt < 55296))
/*     */     {
/* 197 */       i = (this.m_index_[(paramInt >> 5)] << '\002') + (paramInt & 0x1F);
/*     */ 
/* 199 */       return this.m_data_[i];
/*     */     }
/*     */ 
/* 203 */     int i = getCodePointOffset(paramInt);
/*     */ 
/* 207 */     return i >= 0 ? this.m_data_[i] : this.m_initialValue_;
/*     */   }
/*     */ 
/*     */   public final char getLeadValue(char paramChar)
/*     */   {
/* 222 */     return this.m_data_[getLeadOffset(paramChar)];
/*     */   }
/*     */ 
/*     */   public final char getSurrogateValue(char paramChar1, char paramChar2)
/*     */   {
/* 233 */     int i = getSurrogateOffset(paramChar1, paramChar2);
/* 234 */     if (i > 0) {
/* 235 */       return this.m_data_[i];
/*     */     }
/* 237 */     return this.m_initialValue_;
/*     */   }
/*     */ 
/*     */   public final char getTrailValue(int paramInt, char paramChar)
/*     */   {
/* 252 */     if (this.m_dataManipulate_ == null) {
/* 253 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */ 
/* 256 */     int i = this.m_dataManipulate_.getFoldingOffset(paramInt);
/* 257 */     if (i > 0) {
/* 258 */       return this.m_data_[getRawOffset(i, (char)(paramChar & 0x3FF))];
/*     */     }
/*     */ 
/* 261 */     return this.m_initialValue_;
/*     */   }
/*     */ 
/*     */   protected final void unserialize(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 275 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/* 276 */     int i = this.m_dataOffset_ + this.m_dataLength_;
/* 277 */     this.m_index_ = new char[i];
/* 278 */     for (int j = 0; j < i; j++) {
/* 279 */       this.m_index_[j] = localDataInputStream.readChar();
/*     */     }
/* 281 */     this.m_data_ = this.m_index_;
/* 282 */     this.m_initialValue_ = this.m_data_[this.m_dataOffset_];
/*     */   }
/*     */ 
/*     */   protected final int getSurrogateOffset(char paramChar1, char paramChar2)
/*     */   {
/* 294 */     if (this.m_dataManipulate_ == null) {
/* 295 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */ 
/* 300 */     int i = this.m_dataManipulate_.getFoldingOffset(getLeadValue(paramChar1));
/*     */ 
/* 303 */     if (i > 0) {
/* 304 */       return getRawOffset(i, (char)(paramChar2 & 0x3FF));
/*     */     }
/*     */ 
/* 309 */     return -1;
/*     */   }
/*     */ 
/*     */   protected final int getValue(int paramInt)
/*     */   {
/* 322 */     return this.m_data_[paramInt];
/*     */   }
/*     */ 
/*     */   protected final int getInitialValue()
/*     */   {
/* 332 */     return this.m_initialValue_;
/*     */   }
/*     */ 
/*     */   public class FriendAgent
/*     */   {
/*     */     public FriendAgent()
/*     */     {
/*     */     }
/*     */ 
/*     */     public char[] getPrivateIndex()
/*     */     {
/* 150 */       return CharTrie.this.m_index_;
/*     */     }
/*     */ 
/*     */     public char[] getPrivateData()
/*     */     {
/* 158 */       return CharTrie.this.m_data_;
/*     */     }
/*     */ 
/*     */     public int getPrivateInitialValue()
/*     */     {
/* 166 */       return CharTrie.this.m_initialValue_;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.CharTrie
 * JD-Core Version:    0.6.2
 */