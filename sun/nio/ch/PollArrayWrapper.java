/*     */ package sun.nio.ch;
/*     */ 
/*     */ class PollArrayWrapper
/*     */ {
/*     */   private AllocatedNativeObject pollArray;
/*     */   long pollArrayAddress;
/*     */   private static final short FD_OFFSET = 0;
/*     */   private static final short EVENT_OFFSET = 4;
/*  52 */   static short SIZE_POLLFD = 8;
/*     */   static final short POLLIN = 1;
/*     */   static final short POLLOUT = 4;
/*     */   static final short POLLERR = 8;
/*     */   static final short POLLHUP = 16;
/*     */   static final short POLLNVAL = 32;
/*     */   static final short POLLREMOVE = 2048;
/*     */   static final short POLLCONN = 2;
/*     */   private int size;
/*     */ 
/*     */   PollArrayWrapper(int paramInt)
/*     */   {
/*  66 */     int i = paramInt * SIZE_POLLFD;
/*  67 */     this.pollArray = new AllocatedNativeObject(i, true);
/*  68 */     this.pollArrayAddress = this.pollArray.address();
/*  69 */     this.size = paramInt;
/*     */   }
/*     */ 
/*     */   void addEntry(int paramInt, SelectionKeyImpl paramSelectionKeyImpl)
/*     */   {
/*  74 */     putDescriptor(paramInt, paramSelectionKeyImpl.channel.getFDVal());
/*     */   }
/*     */ 
/*     */   void replaceEntry(PollArrayWrapper paramPollArrayWrapper1, int paramInt1, PollArrayWrapper paramPollArrayWrapper2, int paramInt2)
/*     */   {
/*  81 */     paramPollArrayWrapper2.putDescriptor(paramInt2, paramPollArrayWrapper1.getDescriptor(paramInt1));
/*  82 */     paramPollArrayWrapper2.putEventOps(paramInt2, paramPollArrayWrapper1.getEventOps(paramInt1));
/*     */   }
/*     */ 
/*     */   void grow(int paramInt)
/*     */   {
/*  87 */     PollArrayWrapper localPollArrayWrapper = new PollArrayWrapper(paramInt);
/*  88 */     for (int i = 0; i < this.size; i++)
/*  89 */       replaceEntry(this, i, localPollArrayWrapper, i);
/*  90 */     this.pollArray.free();
/*  91 */     this.pollArray = localPollArrayWrapper.pollArray;
/*  92 */     this.size = localPollArrayWrapper.size;
/*  93 */     this.pollArrayAddress = this.pollArray.address();
/*     */   }
/*     */ 
/*     */   void free() {
/*  97 */     this.pollArray.free();
/*     */   }
/*     */ 
/*     */   void putDescriptor(int paramInt1, int paramInt2)
/*     */   {
/* 102 */     this.pollArray.putInt(SIZE_POLLFD * paramInt1 + 0, paramInt2);
/*     */   }
/*     */ 
/*     */   void putEventOps(int paramInt1, int paramInt2) {
/* 106 */     this.pollArray.putShort(SIZE_POLLFD * paramInt1 + 4, (short)paramInt2);
/*     */   }
/*     */ 
/*     */   int getEventOps(int paramInt) {
/* 110 */     return this.pollArray.getShort(SIZE_POLLFD * paramInt + 4);
/*     */   }
/*     */ 
/*     */   int getDescriptor(int paramInt) {
/* 114 */     return this.pollArray.getInt(SIZE_POLLFD * paramInt + 0);
/*     */   }
/*     */ 
/*     */   void addWakeupSocket(int paramInt1, int paramInt2)
/*     */   {
/* 119 */     putDescriptor(paramInt2, paramInt1);
/* 120 */     putEventOps(paramInt2, 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.PollArrayWrapper
 * JD-Core Version:    0.6.2
 */