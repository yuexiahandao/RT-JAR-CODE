/*    */ package java.util.zip;
/*    */ 
/*    */ class ZStreamRef
/*    */ {
/*    */   private long address;
/*    */ 
/*    */   ZStreamRef(long paramLong)
/*    */   {
/* 36 */     this.address = paramLong;
/*    */   }
/*    */ 
/*    */   long address() {
/* 40 */     return this.address;
/*    */   }
/*    */ 
/*    */   void clear() {
/* 44 */     this.address = 0L;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.ZStreamRef
 * JD-Core Version:    0.6.2
 */