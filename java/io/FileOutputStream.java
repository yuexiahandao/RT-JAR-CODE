/*     */ package java.io;
/*     */ 
/*     */ import java.nio.channels.FileChannel;
/*     */ import sun.misc.IoTrace;
/*     */ import sun.nio.ch.FileChannelImpl;
/*     */ 
/*     */ public class FileOutputStream extends OutputStream
/*     */ {
/*     */   private final FileDescriptor fd;
/*     */   private final String path;
/*     */   private final boolean append;
/*     */   private FileChannel channel;
/*  76 */   private final Object closeLock = new Object();
/*  77 */   private volatile boolean closed = false;
/*  78 */   private static final ThreadLocal<Boolean> runningFinalize = new ThreadLocal();
/*     */ 
/*     */   private static boolean isRunningFinalize()
/*     */   {
/*     */     Boolean localBoolean;
/*  83 */     if ((localBoolean = (Boolean)runningFinalize.get()) != null)
/*  84 */       return localBoolean.booleanValue();
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   public FileOutputStream(String paramString)
/*     */     throws FileNotFoundException
/*     */   {
/* 110 */     this(paramString != null ? new File(paramString) : null, false);
/*     */   }
/*     */ 
/*     */   public FileOutputStream(String paramString, boolean paramBoolean)
/*     */     throws FileNotFoundException
/*     */   {
/* 142 */     this(paramString != null ? new File(paramString) : null, paramBoolean);
/*     */   }
/*     */ 
/*     */   public FileOutputStream(File paramFile)
/*     */     throws FileNotFoundException
/*     */   {
/* 171 */     this(paramFile, false);
/*     */   }
/*     */ 
/*     */   public FileOutputStream(File paramFile, boolean paramBoolean)
/*     */     throws FileNotFoundException
/*     */   {
/* 206 */     String str = paramFile != null ? paramFile.getPath() : null;
/* 207 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 208 */     if (localSecurityManager != null) {
/* 209 */       localSecurityManager.checkWrite(str);
/*     */     }
/* 211 */     if (str == null) {
/* 212 */       throw new NullPointerException();
/*     */     }
/* 214 */     if (paramFile.isInvalid()) {
/* 215 */       throw new FileNotFoundException("Invalid file path");
/*     */     }
/* 217 */     this.fd = new FileDescriptor();
/* 218 */     this.append = paramBoolean;
/* 219 */     this.path = str;
/* 220 */     this.fd.incrementAndGetUseCount();
/* 221 */     open(str, paramBoolean);
/*     */   }
/*     */ 
/*     */   public FileOutputStream(FileDescriptor paramFileDescriptor)
/*     */   {
/* 248 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 249 */     if (paramFileDescriptor == null) {
/* 250 */       throw new NullPointerException();
/*     */     }
/* 252 */     if (localSecurityManager != null) {
/* 253 */       localSecurityManager.checkWrite(paramFileDescriptor);
/*     */     }
/* 255 */     this.fd = paramFileDescriptor;
/* 256 */     this.path = null;
/* 257 */     this.append = false;
/*     */ 
/* 264 */     this.fd.incrementAndGetUseCount();
/*     */   }
/*     */ 
/*     */   private native void open(String paramString, boolean paramBoolean)
/*     */     throws FileNotFoundException;
/*     */ 
/*     */   private native void write(int paramInt, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 292 */     Object localObject1 = IoTrace.fileWriteBegin(this.path);
/* 293 */     int i = 0;
/*     */     try {
/* 295 */       write(paramInt, this.append);
/* 296 */       i = 1;
/*     */     } finally {
/* 298 */       IoTrace.fileWriteEnd(localObject1, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 322 */     Object localObject1 = IoTrace.fileWriteBegin(this.path);
/* 323 */     int i = 0;
/*     */     try {
/* 325 */       writeBytes(paramArrayOfByte, 0, paramArrayOfByte.length, this.append);
/* 326 */       i = paramArrayOfByte.length;
/*     */     } finally {
/* 328 */       IoTrace.fileWriteEnd(localObject1, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 342 */     Object localObject1 = IoTrace.fileWriteBegin(this.path);
/* 343 */     int i = 0;
/*     */     try {
/* 345 */       writeBytes(paramArrayOfByte, paramInt1, paramInt2, this.append);
/* 346 */       i = paramInt2;
/*     */     } finally {
/* 348 */       IoTrace.fileWriteEnd(localObject1, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 366 */     synchronized (this.closeLock) {
/* 367 */       if (this.closed) {
/* 368 */         return;
/*     */       }
/* 370 */       this.closed = true;
/*     */     }
/*     */ 
/* 373 */     if (this.channel != null)
/*     */     {
/* 379 */       this.fd.decrementAndGetUseCount();
/* 380 */       this.channel.close();
/*     */     }
/*     */ 
/* 386 */     int i = this.fd.decrementAndGetUseCount();
/*     */ 
/* 392 */     if ((i <= 0) || (!isRunningFinalize()))
/* 393 */       close0();
/*     */   }
/*     */ 
/*     */   public final FileDescriptor getFD()
/*     */     throws IOException
/*     */   {
/* 408 */     if (this.fd != null) return this.fd;
/* 409 */     throw new IOException();
/*     */   }
/*     */ 
/*     */   public FileChannel getChannel()
/*     */   {
/* 430 */     synchronized (this) {
/* 431 */       if (this.channel == null) {
/* 432 */         this.channel = FileChannelImpl.open(this.fd, this.path, false, true, this.append, this);
/*     */ 
/* 439 */         this.fd.incrementAndGetUseCount();
/*     */       }
/* 441 */       return this.channel;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws IOException
/*     */   {
/* 454 */     if (this.fd != null)
/* 455 */       if ((this.fd == FileDescriptor.out) || (this.fd == FileDescriptor.err)) {
/* 456 */         flush();
/*     */       }
/*     */       else
/*     */       {
/* 464 */         runningFinalize.set(Boolean.TRUE);
/*     */         try {
/* 466 */           close();
/*     */         } finally {
/* 468 */           runningFinalize.set(Boolean.FALSE);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private native void close0() throws IOException;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/* 479 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FileOutputStream
 * JD-Core Version:    0.6.2
 */