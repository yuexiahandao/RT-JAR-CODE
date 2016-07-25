/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.util.EmptyStackException;
/*     */ 
/*     */ public class IntStack extends IntVector
/*     */ {
/*     */   public IntStack()
/*     */   {
/*     */   }
/*     */ 
/*     */   public IntStack(int blocksize)
/*     */   {
/*  55 */     super(blocksize);
/*     */   }
/*     */ 
/*     */   public IntStack(IntStack v)
/*     */   {
/*  65 */     super(v);
/*     */   }
/*     */ 
/*     */   public int push(int i)
/*     */   {
/*  77 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/*  79 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/*  81 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/*  83 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/*  85 */       this.m_map = newMap;
/*     */     }
/*     */ 
/*  88 */     this.m_map[this.m_firstFree] = i;
/*     */ 
/*  90 */     this.m_firstFree += 1;
/*     */ 
/*  92 */     return i;
/*     */   }
/*     */ 
/*     */   public final int pop()
/*     */   {
/* 103 */     return this.m_map[(--this.m_firstFree)];
/*     */   }
/*     */ 
/*     */   public final void quickPop(int n)
/*     */   {
/* 112 */     this.m_firstFree -= n;
/*     */   }
/*     */ 
/*     */   public final int peek()
/*     */   {
/*     */     try
/*     */     {
/* 125 */       return this.m_map[(this.m_firstFree - 1)];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/*     */     }
/* 129 */     throw new EmptyStackException();
/*     */   }
/*     */ 
/*     */   public int peek(int n)
/*     */   {
/*     */     try
/*     */     {
/* 143 */       return this.m_map[(this.m_firstFree - (1 + n))];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/*     */     }
/* 147 */     throw new EmptyStackException();
/*     */   }
/*     */ 
/*     */   public void setTop(int val)
/*     */   {
/*     */     try
/*     */     {
/* 161 */       this.m_map[(this.m_firstFree - 1)] = val;
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e)
/*     */     {
/* 165 */       throw new EmptyStackException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean empty()
/*     */   {
/* 178 */     return this.m_firstFree == 0;
/*     */   }
/*     */ 
/*     */   public int search(int o)
/*     */   {
/* 193 */     int i = lastIndexOf(o);
/*     */ 
/* 195 */     if (i >= 0)
/*     */     {
/* 197 */       return size() - i;
/*     */     }
/*     */ 
/* 200 */     return -1;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 211 */     return (IntStack)super.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.IntStack
 * JD-Core Version:    0.6.2
 */