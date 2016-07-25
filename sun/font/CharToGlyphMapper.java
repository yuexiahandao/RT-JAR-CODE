/*    */ package sun.font;
/*    */ 
/*    */ public abstract class CharToGlyphMapper
/*    */ {
/*    */   public static final int HI_SURROGATE_START = 55296;
/*    */   public static final int HI_SURROGATE_END = 56319;
/*    */   public static final int LO_SURROGATE_START = 56320;
/*    */   public static final int LO_SURROGATE_END = 57343;
/*    */   public static final int UNINITIALIZED_GLYPH = -1;
/*    */   public static final int INVISIBLE_GLYPH_ID = 65535;
/*    */   public static final int INVISIBLE_GLYPHS = 65534;
/* 44 */   protected int missingGlyph = -1;
/*    */ 
/*    */   public int getMissingGlyphCode() {
/* 47 */     return this.missingGlyph;
/*    */   }
/*    */ 
/*    */   public boolean canDisplay(char paramChar)
/*    */   {
/* 55 */     int i = charToGlyph(paramChar);
/* 56 */     return i != this.missingGlyph;
/*    */   }
/*    */ 
/*    */   public boolean canDisplay(int paramInt) {
/* 60 */     int i = charToGlyph(paramInt);
/* 61 */     return i != this.missingGlyph;
/*    */   }
/*    */ 
/*    */   public int charToGlyph(char paramChar) {
/* 65 */     char[] arrayOfChar = new char[1];
/* 66 */     int[] arrayOfInt = new int[1];
/* 67 */     arrayOfChar[0] = paramChar;
/* 68 */     charsToGlyphs(1, arrayOfChar, arrayOfInt);
/* 69 */     return arrayOfInt[0];
/*    */   }
/*    */ 
/*    */   public int charToGlyph(int paramInt) {
/* 73 */     int[] arrayOfInt1 = new int[1];
/* 74 */     int[] arrayOfInt2 = new int[1];
/* 75 */     arrayOfInt1[0] = paramInt;
/* 76 */     charsToGlyphs(1, arrayOfInt1, arrayOfInt2);
/* 77 */     return arrayOfInt2[0];
/*    */   }
/*    */ 
/*    */   public abstract int getNumGlyphs();
/*    */ 
/*    */   public abstract void charsToGlyphs(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt);
/*    */ 
/*    */   public abstract boolean charsToGlyphsNS(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt);
/*    */ 
/*    */   public abstract void charsToGlyphs(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.CharToGlyphMapper
 * JD-Core Version:    0.6.2
 */