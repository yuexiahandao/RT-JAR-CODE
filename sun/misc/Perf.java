/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public final class Perf
/*     */ {
/* 537 */   private static Perf instance = new Perf();
/*     */   private static final int PERF_MODE_RO = 0;
/*     */   private static final int PERF_MODE_RW = 1;
/*     */ 
/*     */   public static Perf getPerf()
/*     */   {
/* 135 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 136 */     if (localSecurityManager != null) {
/* 137 */       RuntimePermission localRuntimePermission = new RuntimePermission("sun.misc.Perf.getPerf");
/* 138 */       localSecurityManager.checkPermission(localRuntimePermission);
/*     */     }
/*     */ 
/* 141 */     return instance;
/*     */   }
/*     */ 
/*     */   public ByteBuffer attach(int paramInt, String paramString)
/*     */     throws IllegalArgumentException, IOException
/*     */   {
/* 199 */     if (paramString.compareTo("r") == 0) {
/* 200 */       return attachImpl(null, paramInt, 0);
/*     */     }
/* 202 */     if (paramString.compareTo("rw") == 0) {
/* 203 */       return attachImpl(null, paramInt, 1);
/*     */     }
/*     */ 
/* 206 */     throw new IllegalArgumentException("unknown mode");
/*     */   }
/*     */ 
/*     */   public ByteBuffer attach(String paramString1, int paramInt, String paramString2)
/*     */     throws IllegalArgumentException, IOException
/*     */   {
/* 235 */     if (paramString2.compareTo("r") == 0) {
/* 236 */       return attachImpl(paramString1, paramInt, 0);
/*     */     }
/* 238 */     if (paramString2.compareTo("rw") == 0) {
/* 239 */       return attachImpl(paramString1, paramInt, 1);
/*     */     }
/*     */ 
/* 242 */     throw new IllegalArgumentException("unknown mode");
/*     */   }
/*     */ 
/*     */   private ByteBuffer attachImpl(String paramString, int paramInt1, int paramInt2)
/*     */     throws IllegalArgumentException, IOException
/*     */   {
/* 270 */     final ByteBuffer localByteBuffer1 = attach(paramString, paramInt1, paramInt2);
/*     */ 
/* 272 */     if (paramInt1 == 0)
/*     */     {
/* 275 */       return localByteBuffer1;
/*     */     }
/*     */ 
/* 285 */     ByteBuffer localByteBuffer2 = localByteBuffer1.duplicate();
/* 286 */     Cleaner.create(localByteBuffer2, new Runnable() {
/*     */       public void run() {
/*     */         try {
/* 289 */           Perf.instance.detach(localByteBuffer1);
/*     */         }
/*     */         catch (Throwable localThrowable)
/*     */         {
/* 294 */           if (!$assertionsDisabled) throw new AssertionError(localThrowable.toString());
/*     */         }
/*     */       }
/*     */     });
/* 298 */     return localByteBuffer2;
/*     */   }
/*     */ 
/*     */   private native ByteBuffer attach(String paramString, int paramInt1, int paramInt2)
/*     */     throws IllegalArgumentException, IOException;
/*     */ 
/*     */   private native void detach(ByteBuffer paramByteBuffer);
/*     */ 
/*     */   public native ByteBuffer createLong(String paramString, int paramInt1, int paramInt2, long paramLong);
/*     */ 
/*     */   public ByteBuffer createString(String paramString1, int paramInt1, int paramInt2, String paramString2, int paramInt3)
/*     */   {
/* 408 */     byte[] arrayOfByte1 = getBytes(paramString2);
/* 409 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 1];
/* 410 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
/* 411 */     arrayOfByte2[arrayOfByte1.length] = 0;
/* 412 */     return createByteArray(paramString1, paramInt1, paramInt2, arrayOfByte2, Math.max(arrayOfByte2.length, paramInt3));
/*     */   }
/*     */ 
/*     */   public ByteBuffer createString(String paramString1, int paramInt1, int paramInt2, String paramString2)
/*     */   {
/* 447 */     byte[] arrayOfByte1 = getBytes(paramString2);
/* 448 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 1];
/* 449 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
/* 450 */     arrayOfByte2[arrayOfByte1.length] = 0;
/* 451 */     return createByteArray(paramString1, paramInt1, paramInt2, arrayOfByte2, arrayOfByte2.length);
/*     */   }
/*     */ 
/*     */   public native ByteBuffer createByteArray(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3);
/*     */ 
/*     */   private static byte[] getBytes(String paramString)
/*     */   {
/* 492 */     byte[] arrayOfByte = null;
/*     */     try
/*     */     {
/* 495 */       arrayOfByte = paramString.getBytes("UTF-8");
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/*     */     }
/*     */ 
/* 501 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public native long highResCounter();
/*     */ 
/*     */   public native long highResFrequency();
/*     */ 
/*     */   private static native void registerNatives();
/*     */ 
/*     */   static
/*     */   {
/* 536 */     registerNatives();
/*     */   }
/*     */ 
/*     */   public static class GetPerfAction
/*     */     implements PrivilegedAction<Perf>
/*     */   {
/*     */     public Perf run()
/*     */     {
/*  97 */       return Perf.getPerf();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Perf
 * JD-Core Version:    0.6.2
 */