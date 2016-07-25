/*     */ package java.awt.font;
/*     */ 
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ class CharArrayIterator
/*     */   implements CharacterIterator
/*     */ {
/*     */   private char[] chars;
/*     */   private int pos;
/*     */   private int begin;
/*     */ 
/*     */   CharArrayIterator(char[] paramArrayOfChar)
/*     */   {
/*  38 */     reset(paramArrayOfChar, 0);
/*     */   }
/*     */ 
/*     */   CharArrayIterator(char[] paramArrayOfChar, int paramInt)
/*     */   {
/*  43 */     reset(paramArrayOfChar, paramInt);
/*     */   }
/*     */ 
/*     */   public char first()
/*     */   {
/*  54 */     this.pos = 0;
/*  55 */     return current();
/*     */   }
/*     */ 
/*     */   public char last()
/*     */   {
/*  66 */     if (this.chars.length > 0) {
/*  67 */       this.pos = (this.chars.length - 1);
/*     */     }
/*     */     else {
/*  70 */       this.pos = 0;
/*     */     }
/*  72 */     return current();
/*     */   }
/*     */ 
/*     */   public char current()
/*     */   {
/*  83 */     if ((this.pos >= 0) && (this.pos < this.chars.length)) {
/*  84 */       return this.chars[this.pos];
/*     */     }
/*     */ 
/*  87 */     return 65535;
/*     */   }
/*     */ 
/*     */   public char next()
/*     */   {
/* 101 */     if (this.pos < this.chars.length - 1) {
/* 102 */       this.pos += 1;
/* 103 */       return this.chars[this.pos];
/*     */     }
/*     */ 
/* 106 */     this.pos = this.chars.length;
/* 107 */     return 65535;
/*     */   }
/*     */ 
/*     */   public char previous()
/*     */   {
/* 120 */     if (this.pos > 0) {
/* 121 */       this.pos -= 1;
/* 122 */       return this.chars[this.pos];
/*     */     }
/*     */ 
/* 125 */     this.pos = 0;
/* 126 */     return 65535;
/*     */   }
/*     */ 
/*     */   public char setIndex(int paramInt)
/*     */   {
/* 140 */     paramInt -= this.begin;
/* 141 */     if ((paramInt < 0) || (paramInt > this.chars.length)) {
/* 142 */       throw new IllegalArgumentException("Invalid index");
/*     */     }
/* 144 */     this.pos = paramInt;
/* 145 */     return current();
/*     */   }
/*     */ 
/*     */   public int getBeginIndex()
/*     */   {
/* 153 */     return this.begin;
/*     */   }
/*     */ 
/*     */   public int getEndIndex()
/*     */   {
/* 162 */     return this.begin + this.chars.length;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 170 */     return this.begin + this.pos;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 178 */     CharArrayIterator localCharArrayIterator = new CharArrayIterator(this.chars, this.begin);
/* 179 */     localCharArrayIterator.pos = this.pos;
/* 180 */     return localCharArrayIterator;
/*     */   }
/*     */ 
/*     */   void reset(char[] paramArrayOfChar) {
/* 184 */     reset(paramArrayOfChar, 0);
/*     */   }
/*     */ 
/*     */   void reset(char[] paramArrayOfChar, int paramInt)
/*     */   {
/* 189 */     this.chars = paramArrayOfChar;
/* 190 */     this.begin = paramInt;
/* 191 */     this.pos = 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.CharArrayIterator
 * JD-Core Version:    0.6.2
 */