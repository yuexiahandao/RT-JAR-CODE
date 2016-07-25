/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ public class CharArray
/*     */   implements CharSequence
/*     */ {
/*     */   public char[] ch;
/*     */   public int start;
/*     */   public int length;
/*     */   protected int _hash;
/*     */ 
/*     */   protected CharArray()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CharArray(char[] _ch, int _start, int _length, boolean copy)
/*     */   {
/*  41 */     set(_ch, _start, _length, copy);
/*     */   }
/*     */ 
/*     */   public final void set(char[] _ch, int _start, int _length, boolean copy) {
/*  45 */     if (copy) {
/*  46 */       this.ch = new char[_length];
/*  47 */       this.start = 0;
/*  48 */       this.length = _length;
/*  49 */       System.arraycopy(_ch, _start, this.ch, 0, _length);
/*     */     } else {
/*  51 */       this.ch = _ch;
/*  52 */       this.start = _start;
/*  53 */       this.length = _length;
/*     */     }
/*  55 */     this._hash = 0;
/*     */   }
/*     */ 
/*     */   public final void cloneArray() {
/*  59 */     char[] _ch = new char[this.length];
/*  60 */     System.arraycopy(this.ch, this.start, _ch, 0, this.length);
/*  61 */     this.ch = _ch;
/*  62 */     this.start = 0;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  66 */     return new String(this.ch, this.start, this.length);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  70 */     if (this._hash == 0)
/*     */     {
/*  73 */       for (int i = this.start; i < this.start + this.length; i++) {
/*  74 */         this._hash = (31 * this._hash + this.ch[i]);
/*     */       }
/*     */     }
/*  77 */     return this._hash;
/*     */   }
/*     */ 
/*     */   public static final int hashCode(char[] ch, int start, int length)
/*     */   {
/*  83 */     int hash = 0;
/*  84 */     for (int i = start; i < start + length; i++) {
/*  85 */       hash = 31 * hash + ch[i];
/*     */     }
/*     */ 
/*  88 */     return hash;
/*     */   }
/*     */ 
/*     */   public final boolean equalsCharArray(CharArray cha) {
/*  92 */     if (this == cha) {
/*  93 */       return true;
/*     */     }
/*     */ 
/*  96 */     if (this.length == cha.length) {
/*  97 */       int n = this.length;
/*  98 */       int i = this.start;
/*  99 */       int j = cha.start;
/* 100 */       while (n-- != 0) {
/* 101 */         if (this.ch[(i++)] != cha.ch[(j++)])
/* 102 */           return false;
/*     */       }
/* 104 */       return true;
/*     */     }
/*     */ 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean equalsCharArray(char[] ch, int start, int length) {
/* 111 */     if (this.length == length) {
/* 112 */       int n = this.length;
/* 113 */       int i = this.start;
/* 114 */       int j = start;
/* 115 */       while (n-- != 0) {
/* 116 */         if (this.ch[(i++)] != ch[(j++)])
/* 117 */           return false;
/*     */       }
/* 119 */       return true;
/*     */     }
/*     */ 
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/* 126 */     if (this == obj) {
/* 127 */       return true;
/*     */     }
/* 129 */     if ((obj instanceof CharArray)) {
/* 130 */       CharArray cha = (CharArray)obj;
/* 131 */       if (this.length == cha.length) {
/* 132 */         int n = this.length;
/* 133 */         int i = this.start;
/* 134 */         int j = cha.start;
/* 135 */         while (n-- != 0) {
/* 136 */           if (this.ch[(i++)] != cha.ch[(j++)])
/* 137 */             return false;
/*     */         }
/* 139 */         return true;
/*     */       }
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   public final int length()
/*     */   {
/* 148 */     return this.length;
/*     */   }
/*     */ 
/*     */   public final char charAt(int index) {
/* 152 */     return this.ch[(this.start + index)];
/*     */   }
/*     */ 
/*     */   public final CharSequence subSequence(int start, int end) {
/* 156 */     return new CharArray(this.ch, this.start + start, end - start, false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.CharArray
 * JD-Core Version:    0.6.2
 */