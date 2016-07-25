/*    */ package sun.text;
/*    */ 
/*    */ import sun.text.normalizer.NormalizerImpl;
/*    */ 
/*    */ public final class ComposedCharIter
/*    */ {
/*    */   public static final int DONE = -1;
/* 47 */   private static int[] chars = new int[i];
/* 48 */   private static String[] decomps = new String[i];
/* 49 */   private static int decompNum = NormalizerImpl.getDecompose(chars, decomps);
/*    */ 
/* 82 */   private int curChar = -1;
/*    */ 
/*    */   public int next()
/*    */   {
/* 67 */     if (this.curChar == decompNum - 1) {
/* 68 */       return -1;
/*    */     }
/* 70 */     return chars[(++this.curChar)];
/*    */   }
/*    */ 
/*    */   public String decomposition()
/*    */   {
/* 80 */     return decomps[this.curChar];
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 46 */     int i = 2000;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.ComposedCharIter
 * JD-Core Version:    0.6.2
 */