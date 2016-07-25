/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class IntArrayData extends Pcdata
/*     */ {
/*     */   private int[] data;
/*     */   private int start;
/*     */   private int len;
/*     */   private StringBuilder literal;
/*     */ 
/*     */   public IntArrayData(int[] data, int start, int len)
/*     */   {
/*  60 */     set(data, start, len);
/*     */   }
/*     */ 
/*     */   public IntArrayData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void set(int[] data, int start, int len)
/*     */   {
/*  76 */     this.data = data;
/*  77 */     this.start = start;
/*  78 */     this.len = len;
/*  79 */     this.literal = null;
/*     */   }
/*     */ 
/*     */   public int length() {
/*  83 */     return getLiteral().length();
/*     */   }
/*     */ 
/*     */   public char charAt(int index) {
/*  87 */     return getLiteral().charAt(index);
/*     */   }
/*     */ 
/*     */   public CharSequence subSequence(int start, int end) {
/*  91 */     return getLiteral().subSequence(start, end);
/*     */   }
/*     */ 
/*     */   private StringBuilder getLiteral()
/*     */   {
/*  98 */     if (this.literal != null) return this.literal;
/*     */ 
/* 100 */     this.literal = new StringBuilder();
/* 101 */     int p = this.start;
/* 102 */     for (int i = this.len; i > 0; i--) {
/* 103 */       if (this.literal.length() > 0) this.literal.append(' ');
/* 104 */       this.literal.append(this.data[(p++)]);
/*     */     }
/*     */ 
/* 107 */     return this.literal;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 111 */     return this.literal.toString();
/*     */   }
/*     */ 
/*     */   public void writeTo(UTF8XmlOutput output) throws IOException {
/* 115 */     int p = this.start;
/* 116 */     for (int i = this.len; i > 0; i--) {
/* 117 */       if (i != this.len)
/* 118 */         output.write(32);
/* 119 */       output.text(this.data[(p++)]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntArrayData
 * JD-Core Version:    0.6.2
 */