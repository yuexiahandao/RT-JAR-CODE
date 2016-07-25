/*    */ package sun.nio.ch;
/*    */ 
/*    */ abstract class AbstractPollArrayWrapper
/*    */ {
/*    */   static final short POLLIN = 1;
/*    */   static final short POLLOUT = 4;
/*    */   static final short POLLERR = 8;
/*    */   static final short POLLHUP = 16;
/*    */   static final short POLLNVAL = 32;
/*    */   static final short POLLREMOVE = 2048;
/*    */   static final short SIZE_POLLFD = 8;
/*    */   static final short FD_OFFSET = 0;
/*    */   static final short EVENT_OFFSET = 4;
/*    */   static final short REVENT_OFFSET = 6;
/*    */   protected AllocatedNativeObject pollArray;
/* 58 */   protected int totalChannels = 0;
/*    */   protected long pollArrayAddress;
/*    */ 
/*    */   int getEventOps(int paramInt)
/*    */   {
/* 65 */     int i = 8 * paramInt + 4;
/* 66 */     return this.pollArray.getShort(i);
/*    */   }
/*    */ 
/*    */   int getReventOps(int paramInt) {
/* 70 */     int i = 8 * paramInt + 6;
/* 71 */     return this.pollArray.getShort(i);
/*    */   }
/*    */ 
/*    */   int getDescriptor(int paramInt) {
/* 75 */     int i = 8 * paramInt + 0;
/* 76 */     return this.pollArray.getInt(i);
/*    */   }
/*    */ 
/*    */   void putEventOps(int paramInt1, int paramInt2) {
/* 80 */     int i = 8 * paramInt1 + 4;
/* 81 */     this.pollArray.putShort(i, (short)paramInt2);
/*    */   }
/*    */ 
/*    */   void putReventOps(int paramInt1, int paramInt2) {
/* 85 */     int i = 8 * paramInt1 + 6;
/* 86 */     this.pollArray.putShort(i, (short)paramInt2);
/*    */   }
/*    */ 
/*    */   void putDescriptor(int paramInt1, int paramInt2) {
/* 90 */     int i = 8 * paramInt1 + 0;
/* 91 */     this.pollArray.putInt(i, paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.AbstractPollArrayWrapper
 * JD-Core Version:    0.6.2
 */