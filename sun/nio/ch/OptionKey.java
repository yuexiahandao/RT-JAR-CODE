/*    */ package sun.nio.ch;
/*    */ 
/*    */ class OptionKey
/*    */ {
/*    */   private int level;
/*    */   private int name;
/*    */ 
/*    */   OptionKey(int paramInt1, int paramInt2)
/*    */   {
/* 37 */     this.level = paramInt1;
/* 38 */     this.name = paramInt2;
/*    */   }
/*    */ 
/*    */   int level() {
/* 42 */     return this.level;
/*    */   }
/*    */ 
/*    */   int name() {
/* 46 */     return this.name;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.OptionKey
 * JD-Core Version:    0.6.2
 */