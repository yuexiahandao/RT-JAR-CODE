/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.text.BreakIterator;
/*     */ import java.text.CharacterIterator;
/*     */ import java.text.StringCharacterIterator;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class WhitespaceBasedBreakIterator extends BreakIterator
/*     */ {
/*  39 */   private char[] text = new char[0];
/*  40 */   private int[] breaks = { 0 };
/*  41 */   private int pos = 0;
/*     */ 
/*     */   public void setText(CharacterIterator paramCharacterIterator)
/*     */   {
/*  47 */     int i = paramCharacterIterator.getBeginIndex();
/*  48 */     this.text = new char[paramCharacterIterator.getEndIndex() - i];
/*  49 */     int[] arrayOfInt = new int[this.text.length + 1];
/*  50 */     int j = 0;
/*  51 */     arrayOfInt[(j++)] = i;
/*     */ 
/*  53 */     int k = 0;
/*  54 */     int m = 0;
/*     */     int i1;
/*  55 */     for (int n = paramCharacterIterator.first(); n != 65535; i1 = paramCharacterIterator.next()) {
/*  56 */       this.text[k] = n;
/*  57 */       boolean bool = Character.isWhitespace(n);
/*  58 */       if ((m != 0) && (!bool)) {
/*  59 */         arrayOfInt[(j++)] = (k + i);
/*     */       }
/*  61 */       m = bool;
/*  62 */       k++;
/*     */     }
/*  64 */     if (this.text.length > 0) {
/*  65 */       arrayOfInt[(j++)] = (this.text.length + i);
/*     */     }
/*  67 */     System.arraycopy(arrayOfInt, 0, this.breaks = new int[j], 0, j);
/*     */   }
/*     */ 
/*     */   public CharacterIterator getText() {
/*  71 */     return new StringCharacterIterator(new String(this.text));
/*     */   }
/*     */ 
/*     */   public int first() {
/*  75 */     return this.breaks[(this.pos = 0)];
/*     */   }
/*     */ 
/*     */   public int last() {
/*  79 */     return this.breaks[(this.pos = this.breaks.length - 1)];
/*     */   }
/*     */ 
/*     */   public int current() {
/*  83 */     return this.breaks[this.pos];
/*     */   }
/*     */ 
/*     */   public int next() {
/*  87 */     return this.pos == this.breaks.length - 1 ? -1 : this.breaks[(++this.pos)];
/*     */   }
/*     */ 
/*     */   public int previous() {
/*  91 */     return this.pos == 0 ? -1 : this.breaks[(--this.pos)];
/*     */   }
/*     */ 
/*     */   public int next(int paramInt) {
/*  95 */     return checkhit(this.pos + paramInt);
/*     */   }
/*     */ 
/*     */   public int following(int paramInt) {
/*  99 */     return adjacent(paramInt, 1);
/*     */   }
/*     */ 
/*     */   public int preceding(int paramInt) {
/* 103 */     return adjacent(paramInt, -1);
/*     */   }
/*     */ 
/*     */   private int checkhit(int paramInt) {
/* 107 */     if ((paramInt < 0) || (paramInt >= this.breaks.length)) {
/* 108 */       return -1;
/*     */     }
/* 110 */     return this.breaks[(this.pos = paramInt)];
/*     */   }
/*     */ 
/*     */   private int adjacent(int paramInt1, int paramInt2)
/*     */   {
/* 115 */     int i = Arrays.binarySearch(this.breaks, paramInt1);
/* 116 */     int j = i < 0 ? -2 : paramInt2 < 0 ? -1 : 0;
/* 117 */     return checkhit(Math.abs(i) + paramInt2 + j);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.WhitespaceBasedBreakIterator
 * JD-Core Version:    0.6.2
 */