/*    */ package sun.awt;
/*    */ 
/*    */ public class CharsetString
/*    */ {
/*    */   public char[] charsetChars;
/*    */   public int offset;
/*    */   public int length;
/*    */   public FontDescriptor fontDescriptor;
/*    */ 
/*    */   public CharsetString(char[] paramArrayOfChar, int paramInt1, int paramInt2, FontDescriptor paramFontDescriptor)
/*    */   {
/* 54 */     this.charsetChars = paramArrayOfChar;
/* 55 */     this.offset = paramInt1;
/* 56 */     this.length = paramInt2;
/* 57 */     this.fontDescriptor = paramFontDescriptor;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.CharsetString
 * JD-Core Version:    0.6.2
 */