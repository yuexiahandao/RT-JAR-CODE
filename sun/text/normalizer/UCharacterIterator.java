/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ public abstract class UCharacterIterator
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int DONE = -1;
/*     */ 
/*     */   public static final UCharacterIterator getInstance(String paramString)
/*     */   {
/*  84 */     return new ReplaceableUCharacterIterator(paramString);
/*     */   }
/*     */ 
/*     */   public static final UCharacterIterator getInstance(StringBuffer paramStringBuffer)
/*     */   {
/*  97 */     return new ReplaceableUCharacterIterator(paramStringBuffer);
/*     */   }
/*     */ 
/*     */   public static final UCharacterIterator getInstance(CharacterIterator paramCharacterIterator)
/*     */   {
/* 109 */     return new CharacterIteratorWrapper(paramCharacterIterator);
/*     */   }
/*     */ 
/*     */   public abstract int current();
/*     */ 
/*     */   public abstract int getLength();
/*     */ 
/*     */   public abstract int getIndex();
/*     */ 
/*     */   public abstract int next();
/*     */ 
/*     */   public int nextCodePoint()
/*     */   {
/* 161 */     int i = next();
/* 162 */     if (UTF16.isLeadSurrogate((char)i)) {
/* 163 */       int j = next();
/* 164 */       if (UTF16.isTrailSurrogate((char)j)) {
/* 165 */         return UCharacterProperty.getRawSupplementary((char)i, (char)j);
/*     */       }
/* 167 */       if (j != -1)
/*     */       {
/* 169 */         previous();
/*     */       }
/*     */     }
/* 172 */     return i;
/*     */   }
/*     */ 
/*     */   public abstract int previous();
/*     */ 
/*     */   public abstract void setIndex(int paramInt);
/*     */ 
/*     */   public abstract int getText(char[] paramArrayOfChar, int paramInt);
/*     */ 
/*     */   public final int getText(char[] paramArrayOfChar)
/*     */   {
/* 245 */     return getText(paramArrayOfChar, 0);
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 255 */     char[] arrayOfChar = new char[getLength()];
/* 256 */     getText(arrayOfChar);
/* 257 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public int moveIndex(int paramInt)
/*     */   {
/* 277 */     int i = Math.max(0, Math.min(getIndex() + paramInt, getLength()));
/* 278 */     setIndex(i);
/* 279 */     return i;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 289 */     return super.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.UCharacterIterator
 * JD-Core Version:    0.6.2
 */