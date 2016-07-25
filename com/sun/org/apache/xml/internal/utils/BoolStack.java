/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public final class BoolStack
/*     */   implements Cloneable
/*     */ {
/*     */   private boolean[] m_values;
/*     */   private int m_allocatedSize;
/*     */   private int m_index;
/*     */ 
/*     */   public BoolStack()
/*     */   {
/*  48 */     this(32);
/*     */   }
/*     */ 
/*     */   public BoolStack(int size)
/*     */   {
/*  59 */     this.m_allocatedSize = size;
/*  60 */     this.m_values = new boolean[size];
/*  61 */     this.m_index = -1;
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/*  71 */     return this.m_index + 1;
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/*  80 */     this.m_index = -1;
/*     */   }
/*     */ 
/*     */   public final boolean push(boolean val)
/*     */   {
/*  93 */     if (this.m_index == this.m_allocatedSize - 1) {
/*  94 */       grow();
/*     */     }
/*  96 */     return this.m_values[(++this.m_index)] = val;
/*     */   }
/*     */ 
/*     */   public final boolean pop()
/*     */   {
/* 108 */     return this.m_values[(this.m_index--)];
/*     */   }
/*     */ 
/*     */   public final boolean popAndTop()
/*     */   {
/* 121 */     this.m_index -= 1;
/*     */ 
/* 123 */     return this.m_index >= 0 ? this.m_values[this.m_index] : false;
/*     */   }
/*     */ 
/*     */   public final void setTop(boolean b)
/*     */   {
/* 134 */     this.m_values[this.m_index] = b;
/*     */   }
/*     */ 
/*     */   public final boolean peek()
/*     */   {
/* 146 */     return this.m_values[this.m_index];
/*     */   }
/*     */ 
/*     */   public final boolean peekOrFalse()
/*     */   {
/* 157 */     return this.m_index > -1 ? this.m_values[this.m_index] : false;
/*     */   }
/*     */ 
/*     */   public final boolean peekOrTrue()
/*     */   {
/* 168 */     return this.m_index > -1 ? this.m_values[this.m_index] : true;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 179 */     return this.m_index == -1;
/*     */   }
/*     */ 
/*     */   private void grow()
/*     */   {
/* 189 */     this.m_allocatedSize *= 2;
/*     */ 
/* 191 */     boolean[] newVector = new boolean[this.m_allocatedSize];
/*     */ 
/* 193 */     System.arraycopy(this.m_values, 0, newVector, 0, this.m_index + 1);
/*     */ 
/* 195 */     this.m_values = newVector;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 201 */     return super.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.BoolStack
 * JD-Core Version:    0.6.2
 */