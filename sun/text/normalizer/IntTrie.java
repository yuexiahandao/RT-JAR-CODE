/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class IntTrie extends Trie
/*     */ {
/*     */   private int m_initialValue_;
/*     */   private int[] m_data_;
/*     */ 
/*     */   public IntTrie(InputStream paramInputStream, Trie.DataManipulate paramDataManipulate)
/*     */     throws IOException
/*     */   {
/*  69 */     super(paramInputStream, paramDataManipulate);
/*  70 */     if (!isIntTrie())
/*  71 */       throw new IllegalArgumentException("Data given does not belong to a int trie.");
/*     */   }
/*     */ 
/*     */   public final int getCodePointValue(int paramInt)
/*     */   {
/*  88 */     int i = getCodePointOffset(paramInt);
/*  89 */     return i >= 0 ? this.m_data_[i] : this.m_initialValue_;
/*     */   }
/*     */ 
/*     */   public final int getLeadValue(char paramChar)
/*     */   {
/* 104 */     return this.m_data_[getLeadOffset(paramChar)];
/*     */   }
/*     */ 
/*     */   public final int getTrailValue(int paramInt, char paramChar)
/*     */   {
/* 118 */     if (this.m_dataManipulate_ == null) {
/* 119 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */ 
/* 122 */     int i = this.m_dataManipulate_.getFoldingOffset(paramInt);
/* 123 */     if (i > 0) {
/* 124 */       return this.m_data_[getRawOffset(i, (char)(paramChar & 0x3FF))];
/*     */     }
/*     */ 
/* 127 */     return this.m_initialValue_;
/*     */   }
/*     */ 
/*     */   protected final void unserialize(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 141 */     super.unserialize(paramInputStream);
/*     */ 
/* 143 */     this.m_data_ = new int[this.m_dataLength_];
/* 144 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/* 145 */     for (int i = 0; i < this.m_dataLength_; i++) {
/* 146 */       this.m_data_[i] = localDataInputStream.readInt();
/*     */     }
/* 148 */     this.m_initialValue_ = this.m_data_[0];
/*     */   }
/*     */ 
/*     */   protected final int getSurrogateOffset(char paramChar1, char paramChar2)
/*     */   {
/* 160 */     if (this.m_dataManipulate_ == null) {
/* 161 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */ 
/* 165 */     int i = this.m_dataManipulate_.getFoldingOffset(getLeadValue(paramChar1));
/*     */ 
/* 168 */     if (i > 0) {
/* 169 */       return getRawOffset(i, (char)(paramChar2 & 0x3FF));
/*     */     }
/*     */ 
/* 174 */     return -1;
/*     */   }
/*     */ 
/*     */   protected final int getValue(int paramInt)
/*     */   {
/* 187 */     return this.m_data_[paramInt];
/*     */   }
/*     */ 
/*     */   protected final int getInitialValue()
/*     */   {
/* 197 */     return this.m_initialValue_;
/*     */   }
/*     */ 
/*     */   IntTrie(char[] paramArrayOfChar, int[] paramArrayOfInt, int paramInt1, int paramInt2, Trie.DataManipulate paramDataManipulate)
/*     */   {
/* 213 */     super(paramArrayOfChar, paramInt2, paramDataManipulate);
/* 214 */     this.m_data_ = paramArrayOfInt;
/* 215 */     this.m_dataLength_ = this.m_data_.length;
/* 216 */     this.m_initialValue_ = paramInt1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.IntTrie
 * JD-Core Version:    0.6.2
 */