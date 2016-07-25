/*     */ package java.text;
/*     */ 
/*     */ import sun.text.bidi.BidiBase;
/*     */ 
/*     */ public final class Bidi
/*     */ {
/*     */   public static final int DIRECTION_LEFT_TO_RIGHT = 0;
/*     */   public static final int DIRECTION_RIGHT_TO_LEFT = 1;
/*     */   public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = -2;
/*     */   public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = -1;
/*     */   private BidiBase bidiBase;
/*     */ 
/*     */   public Bidi(String paramString, int paramInt)
/*     */   {
/*  96 */     if (paramString == null) {
/*  97 */       throw new IllegalArgumentException("paragraph is null");
/*     */     }
/*     */ 
/* 100 */     this.bidiBase = new BidiBase(paramString.toCharArray(), 0, null, 0, paramString.length(), paramInt);
/*     */   }
/*     */ 
/*     */   public Bidi(AttributedCharacterIterator paramAttributedCharacterIterator)
/*     */   {
/* 129 */     if (paramAttributedCharacterIterator == null) {
/* 130 */       throw new IllegalArgumentException("paragraph is null");
/*     */     }
/*     */ 
/* 133 */     this.bidiBase = new BidiBase(0, 0);
/* 134 */     this.bidiBase.setPara(paramAttributedCharacterIterator);
/*     */   }
/*     */ 
/*     */   public Bidi(char[] paramArrayOfChar, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 155 */     if (paramArrayOfChar == null) {
/* 156 */       throw new IllegalArgumentException("text is null");
/*     */     }
/* 158 */     if (paramInt3 < 0) {
/* 159 */       throw new IllegalArgumentException("bad length: " + paramInt3);
/*     */     }
/* 161 */     if ((paramInt1 < 0) || (paramInt3 > paramArrayOfChar.length - paramInt1)) {
/* 162 */       throw new IllegalArgumentException("bad range: " + paramInt1 + " length: " + paramInt3 + " for text of length: " + paramArrayOfChar.length);
/*     */     }
/*     */ 
/* 166 */     if ((paramArrayOfByte != null) && ((paramInt2 < 0) || (paramInt3 > paramArrayOfByte.length - paramInt2))) {
/* 167 */       throw new IllegalArgumentException("bad range: " + paramInt2 + " length: " + paramInt3 + " for embeddings of length: " + paramArrayOfChar.length);
/*     */     }
/*     */ 
/* 172 */     this.bidiBase = new BidiBase(paramArrayOfChar, paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public Bidi createLineBidi(int paramInt1, int paramInt2)
/*     */   {
/* 183 */     AttributedString localAttributedString = new AttributedString("");
/* 184 */     Bidi localBidi = new Bidi(localAttributedString.getIterator());
/*     */ 
/* 186 */     return this.bidiBase.setLine(this, this.bidiBase, localBidi, localBidi.bidiBase, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public boolean isMixed()
/*     */   {
/* 195 */     return this.bidiBase.isMixed();
/*     */   }
/*     */ 
/*     */   public boolean isLeftToRight()
/*     */   {
/* 203 */     return this.bidiBase.isLeftToRight();
/*     */   }
/*     */ 
/*     */   public boolean isRightToLeft()
/*     */   {
/* 211 */     return this.bidiBase.isRightToLeft();
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 219 */     return this.bidiBase.getLength();
/*     */   }
/*     */ 
/*     */   public boolean baseIsLeftToRight()
/*     */   {
/* 227 */     return this.bidiBase.baseIsLeftToRight();
/*     */   }
/*     */ 
/*     */   public int getBaseLevel()
/*     */   {
/* 235 */     return this.bidiBase.getParaLevel();
/*     */   }
/*     */ 
/*     */   public int getLevelAt(int paramInt)
/*     */   {
/* 245 */     return this.bidiBase.getLevelAt(paramInt);
/*     */   }
/*     */ 
/*     */   public int getRunCount()
/*     */   {
/* 253 */     return this.bidiBase.countRuns();
/*     */   }
/*     */ 
/*     */   public int getRunLevel(int paramInt)
/*     */   {
/* 262 */     return this.bidiBase.getRunLevel(paramInt);
/*     */   }
/*     */ 
/*     */   public int getRunStart(int paramInt)
/*     */   {
/* 272 */     return this.bidiBase.getRunStart(paramInt);
/*     */   }
/*     */ 
/*     */   public int getRunLimit(int paramInt)
/*     */   {
/* 283 */     return this.bidiBase.getRunLimit(paramInt);
/*     */   }
/*     */ 
/*     */   public static boolean requiresBidi(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 298 */     return BidiBase.requiresBidi(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static void reorderVisually(byte[] paramArrayOfByte, int paramInt1, Object[] paramArrayOfObject, int paramInt2, int paramInt3)
/*     */   {
/* 318 */     BidiBase.reorderVisually(paramArrayOfByte, paramInt1, paramArrayOfObject, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 325 */     return this.bidiBase.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.Bidi
 * JD-Core Version:    0.6.2
 */