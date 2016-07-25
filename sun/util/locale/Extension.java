/*    */ package sun.util.locale;
/*    */ 
/*    */ class Extension
/*    */ {
/*    */   private final char key;
/*    */   private String value;
/*    */   private String id;
/*    */ 
/*    */   protected Extension(char paramChar)
/*    */   {
/* 40 */     this.key = paramChar;
/*    */   }
/*    */ 
/*    */   Extension(char paramChar, String paramString) {
/* 44 */     this.key = paramChar;
/* 45 */     setValue(paramString);
/*    */   }
/*    */ 
/*    */   protected void setValue(String paramString) {
/* 49 */     this.value = paramString;
/* 50 */     this.id = (this.key + "-" + paramString);
/*    */   }
/*    */ 
/*    */   public char getKey() {
/* 54 */     return this.key;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 58 */     return this.value;
/*    */   }
/*    */ 
/*    */   public String getID() {
/* 62 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 66 */     return getID();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.Extension
 * JD-Core Version:    0.6.2
 */