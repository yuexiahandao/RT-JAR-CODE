/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class IOVecWrapper
/*     */ {
/*     */   private static final int BASE_OFFSET = 0;
/* 159 */   private static final int LEN_OFFSET = addressSize;
/* 160 */   private static final int SIZE_IOVEC = (short)(addressSize * 2);
/*     */   private final AllocatedNativeObject vecArray;
/*     */   private final int size;
/*     */   private final ByteBuffer[] buf;
/*     */   private final int[] position;
/*     */   private final int[] remaining;
/*     */   private final ByteBuffer[] shadow;
/*     */   final long address;
/*     */   static int addressSize;
/*  82 */   private static final ThreadLocal<IOVecWrapper> cached = new ThreadLocal();
/*     */ 
/*     */   private IOVecWrapper(int paramInt)
/*     */   {
/*  86 */     this.size = paramInt;
/*  87 */     this.buf = new ByteBuffer[paramInt];
/*  88 */     this.position = new int[paramInt];
/*  89 */     this.remaining = new int[paramInt];
/*  90 */     this.shadow = new ByteBuffer[paramInt];
/*  91 */     this.vecArray = new AllocatedNativeObject(paramInt * SIZE_IOVEC, false);
/*  92 */     this.address = this.vecArray.address();
/*     */   }
/*     */ 
/*     */   static IOVecWrapper get(int paramInt) {
/*  96 */     IOVecWrapper localIOVecWrapper = (IOVecWrapper)cached.get();
/*  97 */     if ((localIOVecWrapper != null) && (localIOVecWrapper.size < paramInt))
/*     */     {
/*  99 */       localIOVecWrapper.vecArray.free();
/* 100 */       localIOVecWrapper = null;
/*     */     }
/* 102 */     if (localIOVecWrapper == null) {
/* 103 */       localIOVecWrapper = new IOVecWrapper(paramInt);
/* 104 */       Cleaner.create(localIOVecWrapper, new Deallocator(localIOVecWrapper.vecArray));
/* 105 */       cached.set(localIOVecWrapper);
/*     */     }
/* 107 */     return localIOVecWrapper;
/*     */   }
/*     */ 
/*     */   void setBuffer(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3) {
/* 111 */     this.buf[paramInt1] = paramByteBuffer;
/* 112 */     this.position[paramInt1] = paramInt2;
/* 113 */     this.remaining[paramInt1] = paramInt3;
/*     */   }
/*     */ 
/*     */   void setShadow(int paramInt, ByteBuffer paramByteBuffer) {
/* 117 */     this.shadow[paramInt] = paramByteBuffer;
/*     */   }
/*     */ 
/*     */   ByteBuffer getBuffer(int paramInt) {
/* 121 */     return this.buf[paramInt];
/*     */   }
/*     */ 
/*     */   int getPosition(int paramInt) {
/* 125 */     return this.position[paramInt];
/*     */   }
/*     */ 
/*     */   int getRemaining(int paramInt) {
/* 129 */     return this.remaining[paramInt];
/*     */   }
/*     */ 
/*     */   ByteBuffer getShadow(int paramInt) {
/* 133 */     return this.shadow[paramInt];
/*     */   }
/*     */ 
/*     */   void clearRefs(int paramInt) {
/* 137 */     this.buf[paramInt] = null;
/* 138 */     this.shadow[paramInt] = null;
/*     */   }
/*     */ 
/*     */   void putBase(int paramInt, long paramLong) {
/* 142 */     int i = SIZE_IOVEC * paramInt + 0;
/* 143 */     if (addressSize == 4)
/* 144 */       this.vecArray.putInt(i, (int)paramLong);
/*     */     else
/* 146 */       this.vecArray.putLong(i, paramLong);
/*     */   }
/*     */ 
/*     */   void putLen(int paramInt, long paramLong) {
/* 150 */     int i = SIZE_IOVEC * paramInt + LEN_OFFSET;
/* 151 */     if (addressSize == 4)
/* 152 */       this.vecArray.putInt(i, (int)paramLong);
/*     */     else
/* 154 */       this.vecArray.putLong(i, paramLong);
/*     */   }
/*     */ 
/*     */   static {
/* 158 */     addressSize = Util.unsafe().addressSize();
/*     */   }
/*     */ 
/*     */   private static class Deallocator
/*     */     implements Runnable
/*     */   {
/*     */     private final AllocatedNativeObject obj;
/*     */ 
/*     */     Deallocator(AllocatedNativeObject paramAllocatedNativeObject)
/*     */     {
/*  74 */       this.obj = paramAllocatedNativeObject;
/*     */     }
/*     */     public void run() {
/*  77 */       this.obj.free();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.IOVecWrapper
 * JD-Core Version:    0.6.2
 */