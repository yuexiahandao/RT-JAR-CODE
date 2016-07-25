/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ public class CharacterIteratorWrapper extends UCharacterIterator
/*     */ {
/*     */   private CharacterIterator iterator;
/*     */ 
/*     */   public CharacterIteratorWrapper(CharacterIterator paramCharacterIterator)
/*     */   {
/*  53 */     if (paramCharacterIterator == null) {
/*  54 */       throw new IllegalArgumentException();
/*     */     }
/*  56 */     this.iterator = paramCharacterIterator;
/*     */   }
/*     */ 
/*     */   public int current()
/*     */   {
/*  63 */     int i = this.iterator.current();
/*  64 */     if (i == 65535) {
/*  65 */       return -1;
/*     */     }
/*  67 */     return i;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  74 */     return this.iterator.getEndIndex() - this.iterator.getBeginIndex();
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  81 */     return this.iterator.getIndex();
/*     */   }
/*     */ 
/*     */   public int next()
/*     */   {
/*  88 */     int i = this.iterator.current();
/*  89 */     this.iterator.next();
/*  90 */     if (i == 65535) {
/*  91 */       return -1;
/*     */     }
/*  93 */     return i;
/*     */   }
/*     */ 
/*     */   public int previous()
/*     */   {
/* 100 */     int i = this.iterator.previous();
/* 101 */     if (i == 65535) {
/* 102 */       return -1;
/*     */     }
/* 104 */     return i;
/*     */   }
/*     */ 
/*     */   public void setIndex(int paramInt)
/*     */   {
/* 111 */     this.iterator.setIndex(paramInt);
/*     */   }
/*     */ 
/*     */   public int getText(char[] paramArrayOfChar, int paramInt)
/*     */   {
/* 119 */     int i = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
/* 120 */     int j = this.iterator.getIndex();
/* 121 */     if ((paramInt < 0) || (paramInt + i > paramArrayOfChar.length)) {
/* 122 */       throw new IndexOutOfBoundsException(Integer.toString(i));
/*     */     }
/*     */ 
/* 125 */     for (int k = this.iterator.first(); k != 65535; k = this.iterator.next()) {
/* 126 */       paramArrayOfChar[(paramInt++)] = k;
/*     */     }
/* 128 */     this.iterator.setIndex(j);
/*     */ 
/* 130 */     return i;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 139 */       CharacterIteratorWrapper localCharacterIteratorWrapper = (CharacterIteratorWrapper)super.clone();
/* 140 */       localCharacterIteratorWrapper.iterator = ((CharacterIterator)this.iterator.clone());
/* 141 */       return localCharacterIteratorWrapper; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 143 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.CharacterIteratorWrapper
 * JD-Core Version:    0.6.2
 */