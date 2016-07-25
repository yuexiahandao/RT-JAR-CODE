/*    */ package sun.font;
/*    */ 
/*    */ public final class TextRecord
/*    */ {
/*    */   public char[] text;
/*    */   public int start;
/*    */   public int limit;
/*    */   public int min;
/*    */   public int max;
/*    */ 
/*    */   public void init(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 44 */     this.text = paramArrayOfChar;
/* 45 */     this.start = paramInt1;
/* 46 */     this.limit = paramInt2;
/* 47 */     this.min = paramInt3;
/* 48 */     this.max = paramInt4;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.TextRecord
 * JD-Core Version:    0.6.2
 */