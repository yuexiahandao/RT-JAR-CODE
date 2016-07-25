/*     */ package com.sun.org.apache.xerces.internal.xni;
/*     */ 
/*     */ public class XMLString
/*     */ {
/*     */   public char[] ch;
/*     */   public int offset;
/*     */   public int length;
/*     */ 
/*     */   public XMLString()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLString(char[] ch, int offset, int length)
/*     */   {
/*  78 */     setValues(ch, offset, length);
/*     */   }
/*     */ 
/*     */   public XMLString(XMLString string)
/*     */   {
/*  91 */     setValues(string);
/*     */   }
/*     */ 
/*     */   public void setValues(char[] ch, int offset, int length)
/*     */   {
/* 107 */     this.ch = ch;
/* 108 */     this.offset = offset;
/* 109 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public void setValues(XMLString s)
/*     */   {
/* 122 */     setValues(s.ch, s.offset, s.length);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 127 */     this.ch = null;
/* 128 */     this.offset = 0;
/* 129 */     this.length = -1;
/*     */   }
/*     */ 
/*     */   public boolean equals(char[] ch, int offset, int length)
/*     */   {
/* 141 */     if (ch == null) {
/* 142 */       return false;
/*     */     }
/* 144 */     if (this.length != length) {
/* 145 */       return false;
/*     */     }
/*     */ 
/* 148 */     for (int i = 0; i < length; i++) {
/* 149 */       if (this.ch[(this.offset + i)] != ch[(offset + i)]) {
/* 150 */         return false;
/*     */       }
/*     */     }
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(String s)
/*     */   {
/* 163 */     if (s == null) {
/* 164 */       return false;
/*     */     }
/* 166 */     if (this.length != s.length()) {
/* 167 */       return false;
/*     */     }
/*     */ 
/* 173 */     for (int i = 0; i < this.length; i++) {
/* 174 */       if (this.ch[(this.offset + i)] != s.charAt(i)) {
/* 175 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 188 */     return this.length > 0 ? new String(this.ch, this.offset, this.length) : "";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.XMLString
 * JD-Core Version:    0.6.2
 */