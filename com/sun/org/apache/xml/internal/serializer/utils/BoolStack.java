/*     */ package com.sun.org.apache.xml.internal.serializer.utils;
/*     */ 
/*     */ public final class BoolStack
/*     */ {
/*     */   private boolean[] m_values;
/*     */   private int m_allocatedSize;
/*     */   private int m_index;
/*     */ 
/*     */   public BoolStack()
/*     */   {
/*  57 */     this(32);
/*     */   }
/*     */ 
/*     */   public BoolStack(int size)
/*     */   {
/*  68 */     this.m_allocatedSize = size;
/*  69 */     this.m_values = new boolean[size];
/*  70 */     this.m_index = -1;
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/*  80 */     return this.m_index + 1;
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/*  89 */     this.m_index = -1;
/*     */   }
/*     */ 
/*     */   public final boolean push(boolean val)
/*     */   {
/* 102 */     if (this.m_index == this.m_allocatedSize - 1) {
/* 103 */       grow();
/*     */     }
/* 105 */     return this.m_values[(++this.m_index)] = val;
/*     */   }
/*     */ 
/*     */   public final boolean pop()
/*     */   {
/* 117 */     return this.m_values[(this.m_index--)];
/*     */   }
/*     */ 
/*     */   public final boolean popAndTop()
/*     */   {
/* 130 */     this.m_index -= 1;
/*     */ 
/* 132 */     return this.m_index >= 0 ? this.m_values[this.m_index] : false;
/*     */   }
/*     */ 
/*     */   public final void setTop(boolean b)
/*     */   {
/* 143 */     this.m_values[this.m_index] = b;
/*     */   }
/*     */ 
/*     */   public final boolean peek()
/*     */   {
/* 155 */     return this.m_values[this.m_index];
/*     */   }
/*     */ 
/*     */   public final boolean peekOrFalse()
/*     */   {
/* 166 */     return this.m_index > -1 ? this.m_values[this.m_index] : false;
/*     */   }
/*     */ 
/*     */   public final boolean peekOrTrue()
/*     */   {
/* 177 */     return this.m_index > -1 ? this.m_values[this.m_index] : true;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 188 */     return this.m_index == -1;
/*     */   }
/*     */ 
/*     */   private void grow()
/*     */   {
/* 198 */     this.m_allocatedSize *= 2;
/*     */ 
/* 200 */     boolean[] newVector = new boolean[this.m_allocatedSize];
/*     */ 
/* 202 */     System.arraycopy(this.m_values, 0, newVector, 0, this.m_index + 1);
/*     */ 
/* 204 */     this.m_values = newVector;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.BoolStack
 * JD-Core Version:    0.6.2
 */