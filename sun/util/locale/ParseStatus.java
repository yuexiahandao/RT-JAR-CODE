/*    */ package sun.util.locale;
/*    */ 
/*    */ public class ParseStatus
/*    */ {
/*    */   int parseLength;
/*    */   int errorIndex;
/*    */   String errorMsg;
/*    */ 
/*    */   public ParseStatus()
/*    */   {
/* 40 */     reset();
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 44 */     this.parseLength = 0;
/* 45 */     this.errorIndex = -1;
/* 46 */     this.errorMsg = null;
/*    */   }
/*    */ 
/*    */   public boolean isError() {
/* 50 */     return this.errorIndex >= 0;
/*    */   }
/*    */ 
/*    */   public int getErrorIndex() {
/* 54 */     return this.errorIndex;
/*    */   }
/*    */ 
/*    */   public int getParseLength() {
/* 58 */     return this.parseLength;
/*    */   }
/*    */ 
/*    */   public String getErrorMessage() {
/* 62 */     return this.errorMsg;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.ParseStatus
 * JD-Core Version:    0.6.2
 */