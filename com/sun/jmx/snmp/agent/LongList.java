/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ final class LongList
/*     */ {
/*  56 */   public static int DEFAULT_CAPACITY = 10;
/*     */ 
/*  58 */   public static int DEFAULT_INCREMENT = 10;
/*     */   private final int DELTA;
/*     */   private int size;
/*     */   public long[] list;
/*     */ 
/*     */   LongList()
/*     */   {
/*  72 */     this(DEFAULT_CAPACITY, DEFAULT_INCREMENT);
/*     */   }
/*     */ 
/*     */   LongList(int paramInt) {
/*  76 */     this(paramInt, DEFAULT_INCREMENT);
/*     */   }
/*     */ 
/*     */   LongList(int paramInt1, int paramInt2) {
/*  80 */     this.size = 0;
/*  81 */     this.DELTA = paramInt2;
/*  82 */     this.list = allocate(paramInt1);
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/*  88 */     return this.size;
/*     */   }
/*     */ 
/*     */   public final boolean add(long paramLong)
/*     */   {
/*  96 */     if (this.size >= this.list.length)
/*  97 */       resize();
/*  98 */     this.list[(this.size++)] = paramLong;
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   public final void add(int paramInt, long paramLong)
/*     */   {
/* 109 */     if (paramInt > this.size) throw new IndexOutOfBoundsException();
/* 110 */     if (paramInt >= this.list.length) resize();
/* 111 */     if (paramInt == this.size) {
/* 112 */       this.list[(this.size++)] = paramLong;
/* 113 */       return;
/*     */     }
/*     */ 
/* 116 */     System.arraycopy(this.list, paramInt, this.list, paramInt + 1, this.size - paramInt);
/* 117 */     this.list[paramInt] = paramLong;
/* 118 */     this.size += 1;
/*     */   }
/*     */ 
/*     */   public final void add(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3)
/*     */   {
/* 134 */     if (paramInt3 <= 0) return;
/* 135 */     if (paramInt1 > this.size) throw new IndexOutOfBoundsException();
/* 136 */     ensure(this.size + paramInt3);
/* 137 */     if (paramInt1 < this.size) {
/* 138 */       System.arraycopy(this.list, paramInt1, this.list, paramInt1 + paramInt3, this.size - paramInt1);
/*     */     }
/* 140 */     System.arraycopy(paramArrayOfLong, paramInt2, this.list, paramInt1, paramInt3);
/* 141 */     this.size += paramInt3;
/*     */   }
/*     */ 
/*     */   public final long remove(int paramInt1, int paramInt2)
/*     */   {
/* 149 */     if ((paramInt2 < 1) || (paramInt1 < 0)) return -1L;
/* 150 */     if (paramInt1 + paramInt2 > this.size) return -1L;
/*     */ 
/* 152 */     long l = this.list[paramInt1];
/* 153 */     int i = this.size;
/* 154 */     this.size -= paramInt2;
/*     */ 
/* 156 */     if (paramInt1 == this.size) return l;
/*     */ 
/* 158 */     System.arraycopy(this.list, paramInt1 + paramInt2, this.list, paramInt1, this.size - paramInt1);
/*     */ 
/* 160 */     return l;
/*     */   }
/*     */ 
/*     */   public final long remove(int paramInt)
/*     */   {
/* 169 */     if (paramInt >= this.size) return -1L;
/* 170 */     long l = this.list[paramInt];
/* 171 */     this.list[paramInt] = 0L;
/* 172 */     if (paramInt == --this.size) return l;
/*     */ 
/* 174 */     System.arraycopy(this.list, paramInt + 1, this.list, paramInt, this.size - paramInt);
/*     */ 
/* 176 */     return l;
/*     */   }
/*     */ 
/*     */   public final long[] toArray(long[] paramArrayOfLong)
/*     */   {
/* 186 */     System.arraycopy(this.list, 0, paramArrayOfLong, 0, this.size);
/* 187 */     return paramArrayOfLong;
/*     */   }
/*     */ 
/*     */   public final long[] toArray()
/*     */   {
/* 197 */     return toArray(new long[this.size]);
/*     */   }
/*     */ 
/*     */   private final void resize()
/*     */   {
/* 206 */     long[] arrayOfLong = allocate(this.list.length + this.DELTA);
/* 207 */     System.arraycopy(this.list, 0, arrayOfLong, 0, this.size);
/* 208 */     this.list = arrayOfLong;
/*     */   }
/*     */ 
/*     */   private final void ensure(int paramInt)
/*     */   {
/* 219 */     if (this.list.length < paramInt) {
/* 220 */       int i = this.list.length + this.DELTA;
/* 221 */       paramInt = paramInt < i ? i : paramInt;
/* 222 */       long[] arrayOfLong = allocate(paramInt);
/* 223 */       System.arraycopy(this.list, 0, arrayOfLong, 0, this.size);
/* 224 */       this.list = arrayOfLong;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final long[] allocate(int paramInt)
/*     */   {
/* 232 */     return new long[paramInt];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.LongList
 * JD-Core Version:    0.6.2
 */