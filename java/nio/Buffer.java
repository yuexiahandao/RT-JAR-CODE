/*     */ package java.nio;
/*     */ 
/*     */ public abstract class Buffer
/*     */ {
/* 177 */   private int mark = -1;
/* 178 */   private int position = 0;
/*     */   private int limit;
/*     */   private int capacity;
/*     */   long address;
/*     */ 
/*     */   Buffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 190 */     if (paramInt4 < 0)
/* 191 */       throw new IllegalArgumentException("Negative capacity: " + paramInt4);
/* 192 */     this.capacity = paramInt4;
/* 193 */     limit(paramInt3);
/* 194 */     position(paramInt2);
/* 195 */     if (paramInt1 >= 0) {
/* 196 */       if (paramInt1 > paramInt2) {
/* 197 */         throw new IllegalArgumentException("mark > position: (" + paramInt1 + " > " + paramInt2 + ")");
/*     */       }
/* 199 */       this.mark = paramInt1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int capacity()
/*     */   {
/* 209 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   public final int position()
/*     */   {
/* 218 */     return this.position;
/*     */   }
/*     */ 
/*     */   public final Buffer position(int paramInt)
/*     */   {
/* 235 */     if ((paramInt > this.limit) || (paramInt < 0))
/* 236 */       throw new IllegalArgumentException();
/* 237 */     this.position = paramInt;
/* 238 */     if (this.mark > this.position) this.mark = -1;
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   public final int limit()
/*     */   {
/* 248 */     return this.limit;
/*     */   }
/*     */ 
/*     */   public final Buffer limit(int paramInt)
/*     */   {
/* 266 */     if ((paramInt > this.capacity) || (paramInt < 0))
/* 267 */       throw new IllegalArgumentException();
/* 268 */     this.limit = paramInt;
/* 269 */     if (this.position > this.limit) this.position = this.limit;
/* 270 */     if (this.mark > this.limit) this.mark = -1;
/* 271 */     return this;
/*     */   }
/*     */ 
/*     */   public final Buffer mark()
/*     */   {
/* 280 */     this.mark = this.position;
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   public final Buffer reset()
/*     */   {
/* 296 */     int i = this.mark;
/* 297 */     if (i < 0)
/* 298 */       throw new InvalidMarkException();
/* 299 */     this.position = i;
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */   public final Buffer clear()
/*     */   {
/* 321 */     this.position = 0;
/* 322 */     this.limit = this.capacity;
/* 323 */     this.mark = -1;
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */   public final Buffer flip()
/*     */   {
/* 349 */     this.limit = this.position;
/* 350 */     this.position = 0;
/* 351 */     this.mark = -1;
/* 352 */     return this;
/*     */   }
/*     */ 
/*     */   public final Buffer rewind()
/*     */   {
/* 371 */     this.position = 0;
/* 372 */     this.mark = -1;
/* 373 */     return this;
/*     */   }
/*     */ 
/*     */   public final int remaining()
/*     */   {
/* 383 */     return this.limit - this.position;
/*     */   }
/*     */ 
/*     */   public final boolean hasRemaining()
/*     */   {
/* 394 */     return this.position < this.limit;
/*     */   }
/*     */ 
/*     */   public abstract boolean isReadOnly();
/*     */ 
/*     */   public abstract boolean hasArray();
/*     */ 
/*     */   public abstract Object array();
/*     */ 
/*     */   public abstract int arrayOffset();
/*     */ 
/*     */   public abstract boolean isDirect();
/*     */ 
/*     */   final int nextGetIndex()
/*     */   {
/* 491 */     if (this.position >= this.limit)
/* 492 */       throw new BufferUnderflowException();
/* 493 */     return this.position++;
/*     */   }
/*     */ 
/*     */   final int nextGetIndex(int paramInt) {
/* 497 */     if (this.limit - this.position < paramInt)
/* 498 */       throw new BufferUnderflowException();
/* 499 */     int i = this.position;
/* 500 */     this.position += paramInt;
/* 501 */     return i;
/*     */   }
/*     */ 
/*     */   final int nextPutIndex()
/*     */   {
/* 512 */     if (this.position >= this.limit)
/* 513 */       throw new BufferOverflowException();
/* 514 */     return this.position++;
/*     */   }
/*     */ 
/*     */   final int nextPutIndex(int paramInt) {
/* 518 */     if (this.limit - this.position < paramInt)
/* 519 */       throw new BufferOverflowException();
/* 520 */     int i = this.position;
/* 521 */     this.position += paramInt;
/* 522 */     return i;
/*     */   }
/*     */ 
/*     */   final int checkIndex(int paramInt)
/*     */   {
/* 531 */     if ((paramInt < 0) || (paramInt >= this.limit))
/* 532 */       throw new IndexOutOfBoundsException();
/* 533 */     return paramInt;
/*     */   }
/*     */ 
/*     */   final int checkIndex(int paramInt1, int paramInt2) {
/* 537 */     if ((paramInt1 < 0) || (paramInt2 > this.limit - paramInt1))
/* 538 */       throw new IndexOutOfBoundsException();
/* 539 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   final int markValue() {
/* 543 */     return this.mark;
/*     */   }
/*     */ 
/*     */   final void truncate() {
/* 547 */     this.mark = -1;
/* 548 */     this.position = 0;
/* 549 */     this.limit = 0;
/* 550 */     this.capacity = 0;
/*     */   }
/*     */ 
/*     */   final void discardMark() {
/* 554 */     this.mark = -1;
/*     */   }
/*     */ 
/*     */   static void checkBounds(int paramInt1, int paramInt2, int paramInt3) {
/* 558 */     if ((paramInt1 | paramInt2 | paramInt1 + paramInt2 | paramInt3 - (paramInt1 + paramInt2)) < 0)
/* 559 */       throw new IndexOutOfBoundsException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.Buffer
 * JD-Core Version:    0.6.2
 */