/*     */ package sun.text.normalizer;
/*     */ 
/*     */ public class TrieIterator
/*     */   implements RangeValueIterator
/*     */ {
/*     */   private static final int BMP_INDEX_LENGTH_ = 2048;
/*     */   private static final int LEAD_SURROGATE_MIN_VALUE_ = 55296;
/*     */   private static final int TRAIL_SURROGATE_MIN_VALUE_ = 56320;
/*     */   private static final int TRAIL_SURROGATE_COUNT_ = 1024;
/*     */   private static final int TRAIL_SURROGATE_INDEX_BLOCK_LENGTH_ = 32;
/*     */   private static final int DATA_BLOCK_LENGTH_ = 32;
/*     */   private Trie m_trie_;
/*     */   private int m_initialValue_;
/*     */   private int m_currentCodepoint_;
/*     */   private int m_nextCodepoint_;
/*     */   private int m_nextValue_;
/*     */   private int m_nextIndex_;
/*     */   private int m_nextBlock_;
/*     */   private int m_nextBlockIndex_;
/*     */   private int m_nextTrailIndexOffset_;
/*     */ 
/*     */   public TrieIterator(Trie paramTrie)
/*     */   {
/* 121 */     if (paramTrie == null) {
/* 122 */       throw new IllegalArgumentException("Argument trie cannot be null");
/*     */     }
/*     */ 
/* 125 */     this.m_trie_ = paramTrie;
/*     */ 
/* 127 */     this.m_initialValue_ = extract(this.m_trie_.getInitialValue());
/* 128 */     reset();
/*     */   }
/*     */ 
/*     */   public final boolean next(RangeValueIterator.Element paramElement)
/*     */   {
/* 145 */     if (this.m_nextCodepoint_ > 1114111) {
/* 146 */       return false;
/*     */     }
/* 148 */     if ((this.m_nextCodepoint_ < 65536) && (calculateNextBMPElement(paramElement)))
/*     */     {
/* 150 */       return true;
/*     */     }
/* 152 */     calculateNextSupplementaryElement(paramElement);
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 161 */     this.m_currentCodepoint_ = 0;
/* 162 */     this.m_nextCodepoint_ = 0;
/* 163 */     this.m_nextIndex_ = 0;
/* 164 */     this.m_nextBlock_ = (this.m_trie_.m_index_[0] << '\002');
/* 165 */     if (this.m_nextBlock_ == 0) {
/* 166 */       this.m_nextValue_ = this.m_initialValue_;
/*     */     }
/*     */     else {
/* 169 */       this.m_nextValue_ = extract(this.m_trie_.getValue(this.m_nextBlock_));
/*     */     }
/* 171 */     this.m_nextBlockIndex_ = 0;
/* 172 */     this.m_nextTrailIndexOffset_ = 32;
/*     */   }
/*     */ 
/*     */   protected int extract(int paramInt)
/*     */   {
/* 188 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private final void setResult(RangeValueIterator.Element paramElement, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 203 */     paramElement.start = paramInt1;
/* 204 */     paramElement.limit = paramInt2;
/* 205 */     paramElement.value = paramInt3;
/*     */   }
/*     */ 
/*     */   private final boolean calculateNextBMPElement(RangeValueIterator.Element paramElement)
/*     */   {
/* 221 */     int i = this.m_nextBlock_;
/* 222 */     int j = this.m_nextValue_;
/* 223 */     this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 224 */     this.m_nextCodepoint_ += 1;
/* 225 */     this.m_nextBlockIndex_ += 1;
/* 226 */     if (!checkBlockDetail(j)) {
/* 227 */       setResult(paramElement, this.m_currentCodepoint_, this.m_nextCodepoint_, j);
/*     */ 
/* 229 */       return true;
/*     */     }
/*     */ 
/* 233 */     while (this.m_nextCodepoint_ < 65536) {
/* 234 */       this.m_nextIndex_ += 1;
/*     */ 
/* 238 */       if (this.m_nextCodepoint_ == 55296)
/*     */       {
/* 241 */         this.m_nextIndex_ = 2048;
/*     */       }
/* 243 */       else if (this.m_nextCodepoint_ == 56320)
/*     */       {
/* 245 */         this.m_nextIndex_ = (this.m_nextCodepoint_ >> 5);
/*     */       }
/*     */ 
/* 248 */       this.m_nextBlockIndex_ = 0;
/* 249 */       if (!checkBlock(i, j)) {
/* 250 */         setResult(paramElement, this.m_currentCodepoint_, this.m_nextCodepoint_, j);
/*     */ 
/* 252 */         return true;
/*     */       }
/*     */     }
/* 255 */     this.m_nextCodepoint_ -= 1;
/* 256 */     this.m_nextBlockIndex_ -= 1;
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */   private final void calculateNextSupplementaryElement(RangeValueIterator.Element paramElement)
/*     */   {
/* 279 */     int i = this.m_nextValue_;
/* 280 */     int j = this.m_nextBlock_;
/* 281 */     this.m_nextCodepoint_ += 1;
/* 282 */     this.m_nextBlockIndex_ += 1;
/*     */ 
/* 284 */     if (UTF16.getTrailSurrogate(this.m_nextCodepoint_) != 56320)
/*     */     {
/* 288 */       if ((!checkNullNextTrailIndex()) && (!checkBlockDetail(i))) {
/* 289 */         setResult(paramElement, this.m_currentCodepoint_, this.m_nextCodepoint_, i);
/*     */ 
/* 291 */         this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 292 */         return;
/*     */       }
/*     */ 
/* 295 */       this.m_nextIndex_ += 1;
/* 296 */       this.m_nextTrailIndexOffset_ += 1;
/* 297 */       if (!checkTrailBlock(j, i)) {
/* 298 */         setResult(paramElement, this.m_currentCodepoint_, this.m_nextCodepoint_, i);
/*     */ 
/* 300 */         this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 301 */         return;
/*     */       }
/*     */     }
/* 304 */     int k = UTF16.getLeadSurrogate(this.m_nextCodepoint_);
/*     */ 
/* 306 */     while (k < 56320)
/*     */     {
/* 308 */       int m = this.m_trie_.m_index_[(k >> 5)] << '\002';
/*     */ 
/* 311 */       if (m == this.m_trie_.m_dataOffset_)
/*     */       {
/* 313 */         if (i != this.m_initialValue_) {
/* 314 */           this.m_nextValue_ = this.m_initialValue_;
/* 315 */           this.m_nextBlock_ = 0;
/* 316 */           this.m_nextBlockIndex_ = 0;
/* 317 */           setResult(paramElement, this.m_currentCodepoint_, this.m_nextCodepoint_, i);
/*     */ 
/* 319 */           this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 320 */           return;
/*     */         }
/*     */ 
/* 323 */         k += 32;
/*     */ 
/* 329 */         this.m_nextCodepoint_ = UCharacterProperty.getRawSupplementary((char)k, 56320);
/*     */       }
/*     */       else
/*     */       {
/* 334 */         if (this.m_trie_.m_dataManipulate_ == null) {
/* 335 */           throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */         }
/*     */ 
/* 339 */         this.m_nextIndex_ = this.m_trie_.m_dataManipulate_.getFoldingOffset(this.m_trie_.getValue(m + (k & 0x1F)));
/*     */ 
/* 342 */         if (this.m_nextIndex_ <= 0)
/*     */         {
/* 344 */           if (i != this.m_initialValue_) {
/* 345 */             this.m_nextValue_ = this.m_initialValue_;
/* 346 */             this.m_nextBlock_ = 0;
/* 347 */             this.m_nextBlockIndex_ = 0;
/* 348 */             setResult(paramElement, this.m_currentCodepoint_, this.m_nextCodepoint_, i);
/*     */ 
/* 350 */             this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 351 */             return;
/*     */           }
/* 353 */           this.m_nextCodepoint_ += 1024;
/*     */         } else {
/* 355 */           this.m_nextTrailIndexOffset_ = 0;
/* 356 */           if (!checkTrailBlock(j, i)) {
/* 357 */             setResult(paramElement, this.m_currentCodepoint_, this.m_nextCodepoint_, i);
/*     */ 
/* 359 */             this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 360 */             return;
/*     */           }
/*     */         }
/* 363 */         k++;
/*     */       }
/*     */     }
/*     */ 
/* 367 */     setResult(paramElement, this.m_currentCodepoint_, 1114112, i);
/*     */   }
/*     */ 
/*     */   private final boolean checkBlockDetail(int paramInt)
/*     */   {
/* 385 */     while (this.m_nextBlockIndex_ < 32) {
/* 386 */       this.m_nextValue_ = extract(this.m_trie_.getValue(this.m_nextBlock_ + this.m_nextBlockIndex_));
/*     */ 
/* 388 */       if (this.m_nextValue_ != paramInt) {
/* 389 */         return false;
/*     */       }
/* 391 */       this.m_nextBlockIndex_ += 1;
/* 392 */       this.m_nextCodepoint_ += 1;
/*     */     }
/* 394 */     return true;
/*     */   }
/*     */ 
/*     */   private final boolean checkBlock(int paramInt1, int paramInt2)
/*     */   {
/* 411 */     this.m_nextBlock_ = (this.m_trie_.m_index_[this.m_nextIndex_] << '\002');
/*     */ 
/* 413 */     if ((this.m_nextBlock_ == paramInt1) && (this.m_nextCodepoint_ - this.m_currentCodepoint_ >= 32))
/*     */     {
/* 417 */       this.m_nextCodepoint_ += 32;
/*     */     }
/* 419 */     else if (this.m_nextBlock_ == 0)
/*     */     {
/* 421 */       if (paramInt2 != this.m_initialValue_) {
/* 422 */         this.m_nextValue_ = this.m_initialValue_;
/* 423 */         this.m_nextBlockIndex_ = 0;
/* 424 */         return false;
/*     */       }
/* 426 */       this.m_nextCodepoint_ += 32;
/*     */     }
/* 429 */     else if (!checkBlockDetail(paramInt2)) {
/* 430 */       return false;
/*     */     }
/*     */ 
/* 433 */     return true;
/*     */   }
/*     */ 
/*     */   private final boolean checkTrailBlock(int paramInt1, int paramInt2)
/*     */   {
/* 452 */     while (this.m_nextTrailIndexOffset_ < 32)
/*     */     {
/* 455 */       this.m_nextBlockIndex_ = 0;
/*     */ 
/* 457 */       if (!checkBlock(paramInt1, paramInt2)) {
/* 458 */         return false;
/*     */       }
/* 460 */       this.m_nextTrailIndexOffset_ += 1;
/* 461 */       this.m_nextIndex_ += 1;
/*     */     }
/* 463 */     return true;
/*     */   }
/*     */ 
/*     */   private final boolean checkNullNextTrailIndex()
/*     */   {
/* 477 */     if (this.m_nextIndex_ <= 0) {
/* 478 */       this.m_nextCodepoint_ += 1023;
/* 479 */       int i = UTF16.getLeadSurrogate(this.m_nextCodepoint_);
/* 480 */       int j = this.m_trie_.m_index_[(i >> 5)] << '\002';
/*     */ 
/* 483 */       if (this.m_trie_.m_dataManipulate_ == null) {
/* 484 */         throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */       }
/*     */ 
/* 487 */       this.m_nextIndex_ = this.m_trie_.m_dataManipulate_.getFoldingOffset(this.m_trie_.getValue(j + (i & 0x1F)));
/*     */ 
/* 490 */       this.m_nextIndex_ -= 1;
/* 491 */       this.m_nextBlockIndex_ = 32;
/* 492 */       return true;
/*     */     }
/* 494 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.TrieIterator
 * JD-Core Version:    0.6.2
 */