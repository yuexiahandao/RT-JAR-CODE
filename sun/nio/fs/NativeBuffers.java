/*     */ package sun.nio.fs;
/*     */ 
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class NativeBuffers
/*     */ {
/*  37 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final int TEMP_BUF_POOL_SIZE = 3;
/*  40 */   private static ThreadLocal<NativeBuffer[]> threadLocal = new ThreadLocal();
/*     */ 
/*     */   static NativeBuffer allocNativeBuffer(int paramInt)
/*     */   {
/*  48 */     if (paramInt < 2048) paramInt = 2048;
/*  49 */     return new NativeBuffer(paramInt);
/*     */   }
/*     */ 
/*     */   static NativeBuffer getNativeBufferFromCache(int paramInt)
/*     */   {
/*  58 */     NativeBuffer[] arrayOfNativeBuffer = (NativeBuffer[])threadLocal.get();
/*  59 */     if (arrayOfNativeBuffer != null) {
/*  60 */       for (int i = 0; i < 3; i++) {
/*  61 */         NativeBuffer localNativeBuffer = arrayOfNativeBuffer[i];
/*  62 */         if ((localNativeBuffer != null) && (localNativeBuffer.size() >= paramInt)) {
/*  63 */           arrayOfNativeBuffer[i] = null;
/*  64 */           return localNativeBuffer;
/*     */         }
/*     */       }
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   static NativeBuffer getNativeBuffer(int paramInt)
/*     */   {
/*  77 */     NativeBuffer localNativeBuffer = getNativeBufferFromCache(paramInt);
/*  78 */     if (localNativeBuffer != null) {
/*  79 */       localNativeBuffer.setOwner(null);
/*  80 */       return localNativeBuffer;
/*     */     }
/*  82 */     return allocNativeBuffer(paramInt);
/*     */   }
/*     */ 
/*     */   static void releaseNativeBuffer(NativeBuffer paramNativeBuffer)
/*     */   {
/*  92 */     NativeBuffer[] arrayOfNativeBuffer = (NativeBuffer[])threadLocal.get();
/*  93 */     if (arrayOfNativeBuffer == null) {
/*  94 */       arrayOfNativeBuffer = new NativeBuffer[3];
/*  95 */       arrayOfNativeBuffer[0] = paramNativeBuffer;
/*  96 */       threadLocal.set(arrayOfNativeBuffer);
/*  97 */       return;
/*     */     }
/*     */ 
/* 100 */     for (int i = 0; i < 3; i++) {
/* 101 */       if (arrayOfNativeBuffer[i] == null) {
/* 102 */         arrayOfNativeBuffer[i] = paramNativeBuffer;
/* 103 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 107 */     for (i = 0; i < 3; i++) {
/* 108 */       NativeBuffer localNativeBuffer = arrayOfNativeBuffer[i];
/* 109 */       if (localNativeBuffer.size() < paramNativeBuffer.size()) {
/* 110 */         localNativeBuffer.cleaner().clean();
/* 111 */         arrayOfNativeBuffer[i] = paramNativeBuffer;
/* 112 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 117 */     paramNativeBuffer.cleaner().clean();
/*     */   }
/*     */ 
/*     */   static void copyCStringToNativeBuffer(byte[] paramArrayOfByte, NativeBuffer paramNativeBuffer)
/*     */   {
/* 124 */     long l1 = Unsafe.ARRAY_BYTE_BASE_OFFSET;
/* 125 */     long l2 = paramArrayOfByte.length;
/* 126 */     assert (paramNativeBuffer.size() >= l2 + 1L);
/* 127 */     unsafe.copyMemory(paramArrayOfByte, l1, null, paramNativeBuffer.address(), l2);
/* 128 */     unsafe.putByte(paramNativeBuffer.address() + l2, (byte)0);
/*     */   }
/*     */ 
/*     */   static NativeBuffer asNativeBuffer(byte[] paramArrayOfByte)
/*     */   {
/* 136 */     NativeBuffer localNativeBuffer = getNativeBuffer(paramArrayOfByte.length + 1);
/* 137 */     copyCStringToNativeBuffer(paramArrayOfByte, localNativeBuffer);
/* 138 */     return localNativeBuffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.NativeBuffers
 * JD-Core Version:    0.6.2
 */