/*    */ package sun.java2d;
/*    */ 
/*    */ public class DefaultDisposerRecord
/*    */   implements DisposerRecord
/*    */ {
/*    */   private long dataPointer;
/*    */   private long disposerMethodPointer;
/*    */ 
/*    */   public DefaultDisposerRecord(long paramLong1, long paramLong2)
/*    */   {
/* 37 */     this.disposerMethodPointer = paramLong1;
/* 38 */     this.dataPointer = paramLong2;
/*    */   }
/*    */ 
/*    */   public void dispose() {
/* 42 */     invokeNativeDispose(this.disposerMethodPointer, this.dataPointer);
/*    */   }
/*    */ 
/*    */   public long getDataPointer()
/*    */   {
/* 47 */     return this.dataPointer;
/*    */   }
/*    */ 
/*    */   public long getDisposerMethodPointer() {
/* 51 */     return this.disposerMethodPointer;
/*    */   }
/*    */ 
/*    */   public static native void invokeNativeDispose(long paramLong1, long paramLong2);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.DefaultDisposerRecord
 * JD-Core Version:    0.6.2
 */