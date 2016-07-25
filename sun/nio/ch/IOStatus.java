/*    */ package sun.nio.ch;
/*    */ 
/*    */ final class IOStatus
/*    */ {
/*    */   static final int EOF = -1;
/*    */   static final int UNAVAILABLE = -2;
/*    */   static final int INTERRUPTED = -3;
/*    */   static final int UNSUPPORTED = -4;
/*    */   static final int THROWN = -5;
/*    */   static final int UNSUPPORTED_CASE = -6;
/*    */ 
/*    */   static int normalize(int paramInt)
/*    */   {
/* 59 */     if (paramInt == -2)
/* 60 */       return 0;
/* 61 */     return paramInt;
/*    */   }
/*    */ 
/*    */   static boolean check(int paramInt) {
/* 65 */     return paramInt >= -2;
/*    */   }
/*    */ 
/*    */   static long normalize(long paramLong) {
/* 69 */     if (paramLong == -2L)
/* 70 */       return 0L;
/* 71 */     return paramLong;
/*    */   }
/*    */ 
/*    */   static boolean check(long paramLong) {
/* 75 */     return paramLong >= -2L;
/*    */   }
/*    */ 
/*    */   static boolean checkAll(long paramLong)
/*    */   {
/* 80 */     return (paramLong > -1L) || (paramLong < -6L);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.IOStatus
 * JD-Core Version:    0.6.2
 */