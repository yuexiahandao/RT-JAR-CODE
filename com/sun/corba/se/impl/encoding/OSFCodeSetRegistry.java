/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ public final class OSFCodeSetRegistry
/*     */ {
/*     */   public static final int ISO_8859_1_VALUE = 65537;
/*     */   public static final int UTF_16_VALUE = 65801;
/*     */   public static final int UTF_8_VALUE = 83951617;
/*     */   public static final int UCS_2_VALUE = 65792;
/*     */   public static final int ISO_646_VALUE = 65568;
/* 132 */   public static final Entry ISO_8859_1 = new Entry("ISO-8859-1", 65537, true, 1, null);
/*     */ 
/* 145 */   static final Entry UTF_16BE = new Entry("UTF-16BE", -1, true, 2, null);
/*     */ 
/* 151 */   static final Entry UTF_16LE = new Entry("UTF-16LE", -2, true, 2, null);
/*     */ 
/* 164 */   public static final Entry UTF_16 = new Entry("UTF-16", 65801, true, 4, null);
/*     */ 
/* 175 */   public static final Entry UTF_8 = new Entry("UTF-8", 83951617, false, 6, null);
/*     */ 
/* 192 */   public static final Entry UCS_2 = new Entry("UCS-2", 65792, true, 2, null);
/*     */ 
/* 204 */   public static final Entry ISO_646 = new Entry("US-ASCII", 65568, true, 1, null);
/*     */ 
/*     */   public static Entry lookupEntry(int paramInt)
/*     */   {
/* 215 */     switch (paramInt) {
/*     */     case 65537:
/* 217 */       return ISO_8859_1;
/*     */     case 65801:
/* 219 */       return UTF_16;
/*     */     case 83951617:
/* 221 */       return UTF_8;
/*     */     case 65568:
/* 223 */       return ISO_646;
/*     */     case 65792:
/* 225 */       return UCS_2;
/*     */     }
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */   public static final class Entry
/*     */   {
/*     */     private String javaName;
/*     */     private int encodingNum;
/*     */     private boolean isFixedWidth;
/*     */     private int maxBytesPerChar;
/*     */ 
/*     */     private Entry(String paramString, int paramInt1, boolean paramBoolean, int paramInt2)
/*     */     {
/*  65 */       this.javaName = paramString;
/*  66 */       this.encodingNum = paramInt1;
/*  67 */       this.isFixedWidth = paramBoolean;
/*  68 */       this.maxBytesPerChar = paramInt2;
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/*  77 */       return this.javaName;
/*     */     }
/*     */ 
/*     */     public int getNumber()
/*     */     {
/*  84 */       return this.encodingNum;
/*     */     }
/*     */ 
/*     */     public boolean isFixedWidth()
/*     */     {
/*  93 */       return this.isFixedWidth;
/*     */     }
/*     */ 
/*     */     public int getMaxBytesPerChar() {
/*  97 */       return this.maxBytesPerChar;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 105 */       if (this == paramObject) {
/* 106 */         return true;
/*     */       }
/* 108 */       if (!(paramObject instanceof Entry)) {
/* 109 */         return false;
/*     */       }
/* 111 */       Entry localEntry = (Entry)paramObject;
/*     */ 
/* 114 */       return (this.javaName.equals(localEntry.javaName)) && (this.encodingNum == localEntry.encodingNum) && (this.isFixedWidth == localEntry.isFixedWidth) && (this.maxBytesPerChar == localEntry.maxBytesPerChar);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 124 */       return this.encodingNum;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.OSFCodeSetRegistry
 * JD-Core Version:    0.6.2
 */