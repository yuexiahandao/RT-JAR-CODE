/*     */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*     */ 
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ public class BMPattern
/*     */ {
/*     */   char[] pattern;
/*     */   int[] shiftTable;
/*     */   boolean ignoreCase;
/*     */ 
/*     */   public BMPattern(String pat, boolean ignoreCase)
/*     */   {
/*  37 */     this(pat, 256, ignoreCase);
/*     */   }
/*     */ 
/*     */   public BMPattern(String pat, int tableSize, boolean ignoreCase) {
/*  41 */     this.pattern = pat.toCharArray();
/*  42 */     this.shiftTable = new int[tableSize];
/*  43 */     this.ignoreCase = ignoreCase;
/*     */ 
/*  45 */     int length = this.pattern.length;
/*  46 */     for (int i = 0; i < this.shiftTable.length; i++) {
/*  47 */       this.shiftTable[i] = length;
/*     */     }
/*  49 */     for (int i = 0; i < length; i++) {
/*  50 */       char ch = this.pattern[i];
/*  51 */       int diff = length - i - 1;
/*  52 */       int index = ch % this.shiftTable.length;
/*  53 */       if (diff < this.shiftTable[index])
/*  54 */         this.shiftTable[index] = diff;
/*  55 */       if (this.ignoreCase) {
/*  56 */         ch = Character.toUpperCase(ch);
/*  57 */         index = ch % this.shiftTable.length;
/*  58 */         if (diff < this.shiftTable[index])
/*  59 */           this.shiftTable[index] = diff;
/*  60 */         ch = Character.toLowerCase(ch);
/*  61 */         index = ch % this.shiftTable.length;
/*  62 */         if (diff < this.shiftTable[index])
/*  63 */           this.shiftTable[index] = diff;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int matches(CharacterIterator iterator, int start, int limit)
/*     */   {
/*  73 */     if (this.ignoreCase) return matchesIgnoreCase(iterator, start, limit);
/*  74 */     int plength = this.pattern.length;
/*  75 */     if (plength == 0) return start;
/*  76 */     int index = start + plength;
/*  77 */     while (index <= limit) { int pindex = plength;
/*  79 */       int nindex = index + 1;
/*     */       char ch;
/*     */       do { if ((ch = iterator.setIndex(--index)) != this.pattern[(--pindex)])
/*     */           break;
/*  84 */         if (pindex == 0)
/*  85 */           return index; }
/*  86 */       while (pindex > 0);
/*  87 */       index += this.shiftTable[(ch % this.shiftTable.length)] + 1;
/*  88 */       if (index < nindex) index = nindex;
/*     */     }
/*  90 */     return -1;
/*     */   }
/*     */ 
/*     */   public int matches(String str, int start, int limit)
/*     */   {
/*  98 */     if (this.ignoreCase) return matchesIgnoreCase(str, start, limit);
/*  99 */     int plength = this.pattern.length;
/* 100 */     if (plength == 0) return start;
/* 101 */     int index = start + plength;
/* 102 */     while (index <= limit) {
/* 104 */       int pindex = plength;
/* 105 */       int nindex = index + 1;
/*     */       char ch;
/*     */       do { if ((ch = str.charAt(--index)) != this.pattern[(--pindex)])
/*     */           break;
/* 110 */         if (pindex == 0)
/* 111 */           return index; }
/* 112 */       while (pindex > 0);
/* 113 */       index += this.shiftTable[(ch % this.shiftTable.length)] + 1;
/* 114 */       if (index < nindex) index = nindex;
/*     */     }
/* 116 */     return -1;
/*     */   }
/*     */ 
/*     */   public int matches(char[] chars, int start, int limit)
/*     */   {
/* 123 */     if (this.ignoreCase) return matchesIgnoreCase(chars, start, limit);
/* 124 */     int plength = this.pattern.length;
/* 125 */     if (plength == 0) return start;
/* 126 */     int index = start + plength;
/* 127 */     while (index <= limit) {
/* 129 */       int pindex = plength;
/* 130 */       int nindex = index + 1;
/*     */       char ch;
/*     */       do { if ((ch = chars[(--index)]) != this.pattern[(--pindex)])
/*     */           break;
/* 135 */         if (pindex == 0)
/* 136 */           return index; }
/* 137 */       while (pindex > 0);
/* 138 */       index += this.shiftTable[(ch % this.shiftTable.length)] + 1;
/* 139 */       if (index < nindex) index = nindex;
/*     */     }
/* 141 */     return -1;
/*     */   }
/*     */ 
/*     */   int matchesIgnoreCase(CharacterIterator iterator, int start, int limit) {
/* 145 */     int plength = this.pattern.length;
/* 146 */     if (plength == 0) return start;
/* 147 */     int index = start + plength;
/* 148 */     while (index <= limit) { int pindex = plength;
/* 150 */       int nindex = index + 1;
/*     */       char ch;
/*     */       do { char ch1 = ch = iterator.setIndex(--index);
/* 154 */         char ch2 = this.pattern[(--pindex)];
/* 155 */         if (ch1 != ch2) {
/* 156 */           ch1 = Character.toUpperCase(ch1);
/* 157 */           ch2 = Character.toUpperCase(ch2);
/* 158 */           if ((ch1 != ch2) && (Character.toLowerCase(ch1) != Character.toLowerCase(ch2)))
/*     */             break;
/*     */         }
/* 161 */         if (pindex == 0)
/* 162 */           return index; }
/* 163 */       while (pindex > 0);
/* 164 */       index += this.shiftTable[(ch % this.shiftTable.length)] + 1;
/* 165 */       if (index < nindex) index = nindex;
/*     */     }
/* 167 */     return -1;
/*     */   }
/*     */ 
/*     */   int matchesIgnoreCase(String text, int start, int limit) {
/* 171 */     int plength = this.pattern.length;
/* 172 */     if (plength == 0) return start;
/* 173 */     int index = start + plength;
/* 174 */     while (index <= limit) { int pindex = plength;
/* 176 */       int nindex = index + 1;
/*     */       char ch;
/*     */       do { char ch1 = ch = text.charAt(--index);
/* 180 */         char ch2 = this.pattern[(--pindex)];
/* 181 */         if (ch1 != ch2) {
/* 182 */           ch1 = Character.toUpperCase(ch1);
/* 183 */           ch2 = Character.toUpperCase(ch2);
/* 184 */           if ((ch1 != ch2) && (Character.toLowerCase(ch1) != Character.toLowerCase(ch2)))
/*     */             break;
/*     */         }
/* 187 */         if (pindex == 0)
/* 188 */           return index; }
/* 189 */       while (pindex > 0);
/* 190 */       index += this.shiftTable[(ch % this.shiftTable.length)] + 1;
/* 191 */       if (index < nindex) index = nindex;
/*     */     }
/* 193 */     return -1;
/*     */   }
/*     */   int matchesIgnoreCase(char[] chars, int start, int limit) {
/* 196 */     int plength = this.pattern.length;
/* 197 */     if (plength == 0) return start;
/* 198 */     int index = start + plength;
/* 199 */     while (index <= limit) { int pindex = plength;
/* 201 */       int nindex = index + 1;
/*     */       char ch;
/*     */       do { char ch1 = ch = chars[(--index)];
/* 205 */         char ch2 = this.pattern[(--pindex)];
/* 206 */         if (ch1 != ch2) {
/* 207 */           ch1 = Character.toUpperCase(ch1);
/* 208 */           ch2 = Character.toUpperCase(ch2);
/* 209 */           if ((ch1 != ch2) && (Character.toLowerCase(ch1) != Character.toLowerCase(ch2)))
/*     */             break;
/*     */         }
/* 212 */         if (pindex == 0)
/* 213 */           return index; }
/* 214 */       while (pindex > 0);
/* 215 */       index += this.shiftTable[(ch % this.shiftTable.length)] + 1;
/* 216 */       if (index < nindex) index = nindex;
/*     */     }
/* 218 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.BMPattern
 * JD-Core Version:    0.6.2
 */