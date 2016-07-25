/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ class IOUtil
/*     */ {
/* 352 */   static final int IOV_MAX = iovMax();
/*     */ 
/*     */   static int write(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer, long paramLong, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/*  50 */     if ((paramByteBuffer instanceof DirectBuffer)) {
/*  51 */       return writeFromNativeBuffer(paramFileDescriptor, paramByteBuffer, paramLong, paramNativeDispatcher);
/*     */     }
/*     */ 
/*  54 */     int i = paramByteBuffer.position();
/*  55 */     int j = paramByteBuffer.limit();
/*  56 */     assert (i <= j);
/*  57 */     int k = i <= j ? j - i : 0;
/*  58 */     ByteBuffer localByteBuffer = Util.getTemporaryDirectBuffer(k);
/*     */     try {
/*  60 */       localByteBuffer.put(paramByteBuffer);
/*  61 */       localByteBuffer.flip();
/*     */ 
/*  63 */       paramByteBuffer.position(i);
/*     */ 
/*  65 */       int m = writeFromNativeBuffer(paramFileDescriptor, localByteBuffer, paramLong, paramNativeDispatcher);
/*  66 */       if (m > 0)
/*     */       {
/*  68 */         paramByteBuffer.position(i + m);
/*     */       }
/*  70 */       return m;
/*     */     } finally {
/*  72 */       Util.offerFirstTemporaryDirectBuffer(localByteBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int writeFromNativeBuffer(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer, long paramLong, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/*  80 */     int i = paramByteBuffer.position();
/*  81 */     int j = paramByteBuffer.limit();
/*  82 */     assert (i <= j);
/*  83 */     int k = i <= j ? j - i : 0;
/*     */ 
/*  85 */     int m = 0;
/*  86 */     if (k == 0)
/*  87 */       return 0;
/*  88 */     if (paramLong != -1L) {
/*  89 */       m = paramNativeDispatcher.pwrite(paramFileDescriptor, ((DirectBuffer)paramByteBuffer).address() + i, k, paramLong);
/*     */     }
/*     */     else
/*     */     {
/*  93 */       m = paramNativeDispatcher.write(paramFileDescriptor, ((DirectBuffer)paramByteBuffer).address() + i, k);
/*     */     }
/*  95 */     if (m > 0)
/*  96 */       paramByteBuffer.position(i + m);
/*  97 */     return m;
/*     */   }
/*     */ 
/*     */   static long write(FileDescriptor paramFileDescriptor, ByteBuffer[] paramArrayOfByteBuffer, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/* 103 */     return write(paramFileDescriptor, paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length, paramNativeDispatcher);
/*     */   }
/*     */ 
/*     */   static long write(FileDescriptor paramFileDescriptor, ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/* 110 */     IOVecWrapper localIOVecWrapper = IOVecWrapper.get(paramInt2);
/*     */ 
/* 112 */     int i = 0;
/* 113 */     int j = 0;
/*     */     try
/*     */     {
/* 117 */       int k = paramInt1 + paramInt2;
/* 118 */       int m = paramInt1;
/*     */       int i1;
/* 119 */       while ((m < k) && (j < IOV_MAX)) {
/* 120 */         Object localObject1 = paramArrayOfByteBuffer[m];
/* 121 */         int n = ((ByteBuffer)localObject1).position();
/* 122 */         i1 = ((ByteBuffer)localObject1).limit();
/* 123 */         assert (n <= i1);
/* 124 */         int i2 = n <= i1 ? i1 - n : 0;
/* 125 */         if (i2 > 0) {
/* 126 */           localIOVecWrapper.setBuffer(j, (ByteBuffer)localObject1, n, i2);
/*     */ 
/* 129 */           if (!(localObject1 instanceof DirectBuffer)) {
/* 130 */             ByteBuffer localByteBuffer2 = Util.getTemporaryDirectBuffer(i2);
/* 131 */             localByteBuffer2.put((ByteBuffer)localObject1);
/* 132 */             localByteBuffer2.flip();
/* 133 */             localIOVecWrapper.setShadow(j, localByteBuffer2);
/* 134 */             ((ByteBuffer)localObject1).position(n);
/* 135 */             localObject1 = localByteBuffer2;
/* 136 */             n = localByteBuffer2.position();
/*     */           }
/*     */ 
/* 139 */           localIOVecWrapper.putBase(j, ((DirectBuffer)localObject1).address() + n);
/* 140 */           localIOVecWrapper.putLen(j, i2);
/* 141 */           j++;
/*     */         }
/* 143 */         m++;
/*     */       }
/* 145 */       if (j == 0)
/*     */       {
/*     */         ByteBuffer localByteBuffer1;
/* 146 */         return 0L;
/*     */       }
/* 148 */       long l1 = paramNativeDispatcher.writev(paramFileDescriptor, localIOVecWrapper.address, j);
/*     */ 
/* 151 */       long l2 = l1;
/*     */       int i4;
/* 152 */       for (int i3 = 0; i3 < j; i3++) {
/* 153 */         if (l2 > 0L) {
/* 154 */           localByteBuffer3 = localIOVecWrapper.getBuffer(i3);
/* 155 */           i4 = localIOVecWrapper.getPosition(i3);
/* 156 */           int i5 = localIOVecWrapper.getRemaining(i3);
/* 157 */           int i6 = l2 > i5 ? i5 : (int)l2;
/* 158 */           localByteBuffer3.position(i4 + i6);
/* 159 */           l2 -= i6;
/*     */         }
/*     */ 
/* 162 */         ByteBuffer localByteBuffer3 = localIOVecWrapper.getShadow(i3);
/* 163 */         if (localByteBuffer3 != null)
/* 164 */           Util.offerLastTemporaryDirectBuffer(localByteBuffer3);
/* 165 */         localIOVecWrapper.clearRefs(i3);
/*     */       }
/*     */ 
/* 168 */       i = 1;
/*     */       ByteBuffer localByteBuffer4;
/* 169 */       return l1;
/*     */     }
/*     */     finally
/*     */     {
/* 174 */       if (i == 0)
/* 175 */         for (int i7 = 0; i7 < j; i7++) {
/* 176 */           ByteBuffer localByteBuffer5 = localIOVecWrapper.getShadow(i7);
/* 177 */           if (localByteBuffer5 != null)
/* 178 */             Util.offerLastTemporaryDirectBuffer(localByteBuffer5);
/* 179 */           localIOVecWrapper.clearRefs(i7);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   static int read(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer, long paramLong, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/* 189 */     if (paramByteBuffer.isReadOnly())
/* 190 */       throw new IllegalArgumentException("Read-only buffer");
/* 191 */     if ((paramByteBuffer instanceof DirectBuffer)) {
/* 192 */       return readIntoNativeBuffer(paramFileDescriptor, paramByteBuffer, paramLong, paramNativeDispatcher);
/*     */     }
/*     */ 
/* 195 */     ByteBuffer localByteBuffer = Util.getTemporaryDirectBuffer(paramByteBuffer.remaining());
/*     */     try {
/* 197 */       int i = readIntoNativeBuffer(paramFileDescriptor, localByteBuffer, paramLong, paramNativeDispatcher);
/* 198 */       localByteBuffer.flip();
/* 199 */       if (i > 0)
/* 200 */         paramByteBuffer.put(localByteBuffer);
/* 201 */       return i;
/*     */     } finally {
/* 203 */       Util.offerFirstTemporaryDirectBuffer(localByteBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int readIntoNativeBuffer(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer, long paramLong, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/* 211 */     int i = paramByteBuffer.position();
/* 212 */     int j = paramByteBuffer.limit();
/* 213 */     assert (i <= j);
/* 214 */     int k = i <= j ? j - i : 0;
/*     */ 
/* 216 */     if (k == 0)
/* 217 */       return 0;
/* 218 */     int m = 0;
/* 219 */     if (paramLong != -1L) {
/* 220 */       m = paramNativeDispatcher.pread(paramFileDescriptor, ((DirectBuffer)paramByteBuffer).address() + i, k, paramLong);
/*     */     }
/*     */     else {
/* 223 */       m = paramNativeDispatcher.read(paramFileDescriptor, ((DirectBuffer)paramByteBuffer).address() + i, k);
/*     */     }
/* 225 */     if (m > 0)
/* 226 */       paramByteBuffer.position(i + m);
/* 227 */     return m;
/*     */   }
/*     */ 
/*     */   static long read(FileDescriptor paramFileDescriptor, ByteBuffer[] paramArrayOfByteBuffer, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/* 233 */     return read(paramFileDescriptor, paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length, paramNativeDispatcher);
/*     */   }
/*     */ 
/*     */   static long read(FileDescriptor paramFileDescriptor, ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2, NativeDispatcher paramNativeDispatcher)
/*     */     throws IOException
/*     */   {
/* 240 */     IOVecWrapper localIOVecWrapper = IOVecWrapper.get(paramInt2);
/*     */ 
/* 242 */     int i = 0;
/* 243 */     int j = 0;
/*     */     try
/*     */     {
/* 247 */       int k = paramInt1 + paramInt2;
/* 248 */       int m = paramInt1;
/*     */       int i1;
/* 249 */       while ((m < k) && (j < IOV_MAX)) {
/* 250 */         Object localObject1 = paramArrayOfByteBuffer[m];
/* 251 */         if (((ByteBuffer)localObject1).isReadOnly())
/* 252 */           throw new IllegalArgumentException("Read-only buffer");
/* 253 */         int n = ((ByteBuffer)localObject1).position();
/* 254 */         i1 = ((ByteBuffer)localObject1).limit();
/* 255 */         assert (n <= i1);
/* 256 */         int i2 = n <= i1 ? i1 - n : 0;
/*     */ 
/* 258 */         if (i2 > 0) {
/* 259 */           localIOVecWrapper.setBuffer(j, (ByteBuffer)localObject1, n, i2);
/*     */ 
/* 262 */           if (!(localObject1 instanceof DirectBuffer)) {
/* 263 */             ByteBuffer localByteBuffer2 = Util.getTemporaryDirectBuffer(i2);
/* 264 */             localIOVecWrapper.setShadow(j, localByteBuffer2);
/* 265 */             localObject1 = localByteBuffer2;
/* 266 */             n = localByteBuffer2.position();
/*     */           }
/*     */ 
/* 269 */           localIOVecWrapper.putBase(j, ((DirectBuffer)localObject1).address() + n);
/* 270 */           localIOVecWrapper.putLen(j, i2);
/* 271 */           j++;
/*     */         }
/* 273 */         m++;
/*     */       }
/* 275 */       if (j == 0)
/*     */       {
/*     */         ByteBuffer localByteBuffer1;
/* 276 */         return 0L;
/*     */       }
/* 278 */       long l1 = paramNativeDispatcher.readv(paramFileDescriptor, localIOVecWrapper.address, j);
/*     */ 
/* 281 */       long l2 = l1;
/* 282 */       for (int i3 = 0; i3 < j; i3++) {
/* 283 */         ByteBuffer localByteBuffer3 = localIOVecWrapper.getShadow(i3);
/* 284 */         if (l2 > 0L) {
/* 285 */           ByteBuffer localByteBuffer4 = localIOVecWrapper.getBuffer(i3);
/* 286 */           int i5 = localIOVecWrapper.getRemaining(i3);
/* 287 */           int i6 = l2 > i5 ? i5 : (int)l2;
/* 288 */           if (localByteBuffer3 == null) {
/* 289 */             int i7 = localIOVecWrapper.getPosition(i3);
/* 290 */             localByteBuffer4.position(i7 + i6);
/*     */           } else {
/* 292 */             localByteBuffer3.limit(localByteBuffer3.position() + i6);
/* 293 */             localByteBuffer4.put(localByteBuffer3);
/*     */           }
/* 295 */           l2 -= i6;
/*     */         }
/* 297 */         if (localByteBuffer3 != null)
/* 298 */           Util.offerLastTemporaryDirectBuffer(localByteBuffer3);
/* 299 */         localIOVecWrapper.clearRefs(i3);
/*     */       }
/*     */ 
/* 302 */       i = 1;
/*     */       int i4;
/*     */       ByteBuffer localByteBuffer5;
/* 303 */       return l1;
/*     */     }
/*     */     finally
/*     */     {
/* 308 */       if (i == 0)
/* 309 */         for (int i8 = 0; i8 < j; i8++) {
/* 310 */           ByteBuffer localByteBuffer6 = localIOVecWrapper.getShadow(i8);
/* 311 */           if (localByteBuffer6 != null)
/* 312 */             Util.offerLastTemporaryDirectBuffer(localByteBuffer6);
/* 313 */           localIOVecWrapper.clearRefs(i8);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   static FileDescriptor newFD(int paramInt)
/*     */   {
/* 320 */     FileDescriptor localFileDescriptor = new FileDescriptor();
/* 321 */     setfdVal(localFileDescriptor, paramInt);
/* 322 */     return localFileDescriptor;
/*     */   }
/*     */ 
/*     */   static native boolean randomBytes(byte[] paramArrayOfByte);
/*     */ 
/*     */   static native long makePipe(boolean paramBoolean);
/*     */ 
/*     */   static native boolean drain(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native void configureBlocking(FileDescriptor paramFileDescriptor, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   static native int fdVal(FileDescriptor paramFileDescriptor);
/*     */ 
/*     */   static native void setfdVal(FileDescriptor paramFileDescriptor, int paramInt);
/*     */ 
/*     */   static native int iovMax();
/*     */ 
/*     */   static native int fdLimit();
/*     */ 
/*     */   static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/* 351 */     Util.load();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.IOUtil
 * JD-Core Version:    0.6.2
 */