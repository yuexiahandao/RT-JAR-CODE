/*    */ package java.text;
/*    */ 
/*    */ final class EntryPair
/*    */ {
/*    */   public String entryName;
/*    */   public int value;
/*    */   public boolean fwd;
/*    */ 
/*    */   public EntryPair(String paramString, int paramInt)
/*    */   {
/* 53 */     this(paramString, paramInt, true);
/*    */   }
/*    */   public EntryPair(String paramString, int paramInt, boolean paramBoolean) {
/* 56 */     this.entryName = paramString;
/* 57 */     this.value = paramInt;
/* 58 */     this.fwd = paramBoolean;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.EntryPair
 * JD-Core Version:    0.6.2
 */