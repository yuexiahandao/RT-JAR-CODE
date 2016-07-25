/*     */ package java.awt.im;
/*     */ 
/*     */ public final class InputSubset extends Character.Subset
/*     */ {
/*  49 */   public static final InputSubset LATIN = new InputSubset("LATIN");
/*     */ 
/*  56 */   public static final InputSubset LATIN_DIGITS = new InputSubset("LATIN_DIGITS");
/*     */ 
/*  64 */   public static final InputSubset TRADITIONAL_HANZI = new InputSubset("TRADITIONAL_HANZI");
/*     */ 
/*  72 */   public static final InputSubset SIMPLIFIED_HANZI = new InputSubset("SIMPLIFIED_HANZI");
/*     */ 
/*  80 */   public static final InputSubset KANJI = new InputSubset("KANJI");
/*     */ 
/*  88 */   public static final InputSubset HANJA = new InputSubset("HANJA");
/*     */ 
/*  95 */   public static final InputSubset HALFWIDTH_KATAKANA = new InputSubset("HALFWIDTH_KATAKANA");
/*     */ 
/* 103 */   public static final InputSubset FULLWIDTH_LATIN = new InputSubset("FULLWIDTH_LATIN");
/*     */ 
/* 111 */   public static final InputSubset FULLWIDTH_DIGITS = new InputSubset("FULLWIDTH_DIGITS");
/*     */ 
/*     */   private InputSubset(String paramString)
/*     */   {
/*  41 */     super(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.im.InputSubset
 * JD-Core Version:    0.6.2
 */