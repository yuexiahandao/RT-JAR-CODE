/*    */ package sun.swing;
/*    */ 
/*    */ public class StringUIClientPropertyKey
/*    */   implements UIClientPropertyKey
/*    */ {
/*    */   private final String key;
/*    */ 
/*    */   public StringUIClientPropertyKey(String paramString)
/*    */   {
/* 38 */     this.key = paramString;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 42 */     return this.key;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.StringUIClientPropertyKey
 * JD-Core Version:    0.6.2
 */