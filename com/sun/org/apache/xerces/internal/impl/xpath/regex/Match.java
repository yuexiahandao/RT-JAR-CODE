/*     */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*     */ 
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ public class Match
/*     */   implements Cloneable
/*     */ {
/*  38 */   int[] beginpos = null;
/*  39 */   int[] endpos = null;
/*  40 */   int nofgroups = 0;
/*     */ 
/*  42 */   CharacterIterator ciSource = null;
/*  43 */   String strSource = null;
/*  44 */   char[] charSource = null;
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/*  56 */     Match ma = new Match();
/*  57 */     if (this.nofgroups > 0) {
/*  58 */       ma.setNumberOfGroups(this.nofgroups);
/*  59 */       if (this.ciSource != null) ma.setSource(this.ciSource);
/*  60 */       if (this.strSource != null) ma.setSource(this.strSource);
/*  61 */       for (int i = 0; i < this.nofgroups; i++) {
/*  62 */         ma.setBeginning(i, getBeginning(i));
/*  63 */         ma.setEnd(i, getEnd(i));
/*     */       }
/*     */     }
/*  66 */     return ma;
/*     */   }
/*     */ 
/*     */   protected void setNumberOfGroups(int n)
/*     */   {
/*  73 */     int oldn = this.nofgroups;
/*  74 */     this.nofgroups = n;
/*  75 */     if ((oldn <= 0) || (oldn < n) || (n * 2 < oldn))
/*     */     {
/*  77 */       this.beginpos = new int[n];
/*  78 */       this.endpos = new int[n];
/*     */     }
/*  80 */     for (int i = 0; i < n; i++) {
/*  81 */       this.beginpos[i] = -1;
/*  82 */       this.endpos[i] = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setSource(CharacterIterator ci)
/*     */   {
/*  90 */     this.ciSource = ci;
/*  91 */     this.strSource = null;
/*  92 */     this.charSource = null;
/*     */   }
/*     */ 
/*     */   protected void setSource(String str)
/*     */   {
/*  98 */     this.ciSource = null;
/*  99 */     this.strSource = str;
/* 100 */     this.charSource = null;
/*     */   }
/*     */ 
/*     */   protected void setSource(char[] chars)
/*     */   {
/* 106 */     this.ciSource = null;
/* 107 */     this.strSource = null;
/* 108 */     this.charSource = chars;
/*     */   }
/*     */ 
/*     */   protected void setBeginning(int index, int v)
/*     */   {
/* 115 */     this.beginpos[index] = v;
/*     */   }
/*     */ 
/*     */   protected void setEnd(int index, int v)
/*     */   {
/* 122 */     this.endpos[index] = v;
/*     */   }
/*     */ 
/*     */   public int getNumberOfGroups()
/*     */   {
/* 130 */     if (this.nofgroups <= 0)
/* 131 */       throw new IllegalStateException("A result is not set.");
/* 132 */     return this.nofgroups;
/*     */   }
/*     */ 
/*     */   public int getBeginning(int index)
/*     */   {
/* 141 */     if (this.beginpos == null)
/* 142 */       throw new IllegalStateException("A result is not set.");
/* 143 */     if ((index < 0) || (this.nofgroups <= index)) {
/* 144 */       throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + index);
/*     */     }
/* 146 */     return this.beginpos[index];
/*     */   }
/*     */ 
/*     */   public int getEnd(int index)
/*     */   {
/* 155 */     if (this.endpos == null)
/* 156 */       throw new IllegalStateException("A result is not set.");
/* 157 */     if ((index < 0) || (this.nofgroups <= index)) {
/* 158 */       throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + index);
/*     */     }
/* 160 */     return this.endpos[index];
/*     */   }
/*     */ 
/*     */   public String getCapturedText(int index)
/*     */   {
/* 169 */     if (this.beginpos == null)
/* 170 */       throw new IllegalStateException("match() has never been called.");
/* 171 */     if ((index < 0) || (this.nofgroups <= index)) {
/* 172 */       throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + index);
/*     */     }
/*     */ 
/* 175 */     int begin = this.beginpos[index]; int end = this.endpos[index];
/* 176 */     if ((begin < 0) || (end < 0)) return null;
/*     */     String ret;
/*     */     String ret;
/* 177 */     if (this.ciSource != null) {
/* 178 */       ret = REUtil.substring(this.ciSource, begin, end);
/*     */     }
/*     */     else
/*     */     {
/*     */       String ret;
/* 179 */       if (this.strSource != null)
/* 180 */         ret = this.strSource.substring(begin, end);
/*     */       else
/* 182 */         ret = new String(this.charSource, begin, end - begin);
/*     */     }
/* 184 */     return ret;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.Match
 * JD-Core Version:    0.6.2
 */