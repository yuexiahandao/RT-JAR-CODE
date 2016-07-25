/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ 
/*     */ public class XMLStringBuffer extends XMLString
/*     */ {
/*     */   public static final int DEFAULT_SIZE = 32;
/*     */ 
/*     */   public XMLStringBuffer()
/*     */   {
/* 109 */     this(32);
/*     */   }
/*     */ 
/*     */   public XMLStringBuffer(int size)
/*     */   {
/* 118 */     this.ch = new char[size];
/*     */   }
/*     */ 
/*     */   public XMLStringBuffer(char c)
/*     */   {
/* 123 */     this(1);
/* 124 */     append(c);
/*     */   }
/*     */ 
/*     */   public XMLStringBuffer(String s)
/*     */   {
/* 129 */     this(s.length());
/* 130 */     append(s);
/*     */   }
/*     */ 
/*     */   public XMLStringBuffer(char[] ch, int offset, int length)
/*     */   {
/* 135 */     this(length);
/* 136 */     append(ch, offset, length);
/*     */   }
/*     */ 
/*     */   public XMLStringBuffer(XMLString s)
/*     */   {
/* 141 */     this(s.length);
/* 142 */     append(s);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 151 */     this.offset = 0;
/* 152 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   public void append(char c)
/*     */   {
/* 161 */     if (this.length + 1 > this.ch.length) {
/* 162 */       int newLength = this.ch.length * 2;
/* 163 */       if (newLength < this.ch.length + 32) {
/* 164 */         newLength = this.ch.length + 32;
/*     */       }
/* 166 */       char[] tmp = new char[newLength];
/* 167 */       System.arraycopy(this.ch, 0, tmp, 0, this.length);
/* 168 */       this.ch = tmp;
/*     */     }
/* 170 */     this.ch[this.length] = c;
/* 171 */     this.length += 1;
/*     */   }
/*     */ 
/*     */   public void append(String s)
/*     */   {
/* 180 */     int length = s.length();
/* 181 */     if (this.length + length > this.ch.length) {
/* 182 */       int newLength = this.ch.length * 2;
/* 183 */       if (newLength < this.ch.length + length + 32) {
/* 184 */         newLength = this.ch.length + length + 32;
/*     */       }
/*     */ 
/* 187 */       char[] newch = new char[newLength];
/* 188 */       System.arraycopy(this.ch, 0, newch, 0, this.length);
/* 189 */       this.ch = newch;
/*     */     }
/* 191 */     s.getChars(0, length, this.ch, this.length);
/* 192 */     this.length += length;
/*     */   }
/*     */ 
/*     */   public void append(char[] ch, int offset, int length)
/*     */   {
/* 203 */     if (this.length + length > this.ch.length) {
/* 204 */       int newLength = this.ch.length * 2;
/* 205 */       if (newLength < this.ch.length + length + 32) {
/* 206 */         newLength = this.ch.length + length + 32;
/*     */       }
/* 208 */       char[] newch = new char[newLength];
/* 209 */       System.arraycopy(this.ch, 0, newch, 0, this.length);
/* 210 */       this.ch = newch;
/*     */     }
/*     */ 
/* 214 */     if ((ch != null) && (length > 0)) {
/* 215 */       System.arraycopy(ch, offset, this.ch, this.length, length);
/* 216 */       this.length += length;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void append(XMLString s)
/*     */   {
/* 226 */     append(s.ch, s.offset, s.length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLStringBuffer
 * JD-Core Version:    0.6.2
 */