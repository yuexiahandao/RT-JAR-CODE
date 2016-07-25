/*     */ package java.io;
/*     */ 
/*     */ import java.nio.channels.FileChannel;
/*     */ import sun.misc.IoTrace;
/*     */ import sun.nio.ch.FileChannelImpl;
/*     */ 
/*     */ public class FileInputStream extends InputStream
/*     */ {
/*     */   private final FileDescriptor fd;
/*     */   private final String path;
/*  58 */   private FileChannel channel = null;
/*     */ 
/*  60 */   private final Object closeLock = new Object();
/*  61 */   private volatile boolean closed = false;
/*     */ 
/*  63 */   private static final ThreadLocal<Boolean> runningFinalize = new ThreadLocal();
/*     */ 
/*     */   private static boolean isRunningFinalize()
/*     */   {
/*     */     Boolean localBoolean;
/*  68 */     if ((localBoolean = (Boolean)runningFinalize.get()) != null)
/*  69 */       return localBoolean.booleanValue();
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   public FileInputStream(String paramString)
/*     */     throws FileNotFoundException
/*     */   {
/* 101 */     this(paramString != null ? new File(paramString) : null);
/*     */   }
/*     */ 
/*     */   public FileInputStream(File paramFile)
/*     */     throws FileNotFoundException
/*     */   {
/* 132 */     String str = paramFile != null ? paramFile.getPath() : null;
/* 133 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 134 */     if (localSecurityManager != null) {
/* 135 */       localSecurityManager.checkRead(str);
/*     */     }
/* 137 */     if (str == null) {
/* 138 */       throw new NullPointerException();
/*     */     }
/* 140 */     if (paramFile.isInvalid()) {
/* 141 */       throw new FileNotFoundException("Invalid file path");
/*     */     }
/* 143 */     this.fd = new FileDescriptor();
/* 144 */     this.fd.incrementAndGetUseCount();
/* 145 */     this.path = str;
/* 146 */     open(str);
/*     */   }
/*     */ 
/*     */   public FileInputStream(FileDescriptor paramFileDescriptor)
/*     */   {
/* 174 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 175 */     if (paramFileDescriptor == null) {
/* 176 */       throw new NullPointerException();
/*     */     }
/* 178 */     if (localSecurityManager != null) {
/* 179 */       localSecurityManager.checkRead(paramFileDescriptor);
/*     */     }
/* 181 */     this.fd = paramFileDescriptor;
/* 182 */     this.path = null;
/*     */ 
/* 189 */     this.fd.incrementAndGetUseCount();
/*     */   }
/*     */ 
/*     */   private native void open(String paramString)
/*     */     throws FileNotFoundException;
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 207 */     Object localObject1 = IoTrace.fileReadBegin(this.path);
/* 208 */     int i = 0;
/*     */     try {
/* 210 */       i = read0();
/*     */     } finally {
/* 212 */       IoTrace.fileReadEnd(localObject1, i == -1 ? 0L : 1L);
/*     */     }
/* 214 */     return i;
/*     */   }
/*     */ 
/*     */   private native int read0()
/*     */     throws IOException;
/*     */ 
/*     */   private native int readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 240 */     Object localObject1 = IoTrace.fileReadBegin(this.path);
/* 241 */     int i = 0;
/*     */     try {
/* 243 */       i = readBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     } finally {
/* 245 */       IoTrace.fileReadEnd(localObject1, i == -1 ? 0L : i);
/*     */     }
/* 247 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 269 */     Object localObject1 = IoTrace.fileReadBegin(this.path);
/* 270 */     int i = 0;
/*     */     try {
/* 272 */       i = readBytes(paramArrayOfByte, paramInt1, paramInt2);
/*     */     } finally {
/* 274 */       IoTrace.fileReadEnd(localObject1, i == -1 ? 0L : i);
/*     */     }
/* 276 */     return i;
/*     */   }
/*     */ 
/*     */   public native long skip(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public native int available()
/*     */     throws IOException;
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 334 */     synchronized (this.closeLock) {
/* 335 */       if (this.closed) {
/* 336 */         return;
/*     */       }
/* 338 */       this.closed = true;
/*     */     }
/* 340 */     if (this.channel != null)
/*     */     {
/* 346 */       this.fd.decrementAndGetUseCount();
/* 347 */       this.channel.close();
/*     */     }
/*     */ 
/* 353 */     int i = this.fd.decrementAndGetUseCount();
/*     */ 
/* 359 */     if ((i <= 0) || (!isRunningFinalize()))
/* 360 */       close0();
/*     */   }
/*     */ 
/*     */   public final FileDescriptor getFD()
/*     */     throws IOException
/*     */   {
/* 375 */     if (this.fd != null) return this.fd;
/* 376 */     throw new IOException();
/*     */   }
/*     */ 
/*     */   public FileChannel getChannel()
/*     */   {
/* 396 */     synchronized (this) {
/* 397 */       if (this.channel == null) {
/* 398 */         this.channel = FileChannelImpl.open(this.fd, this.path, true, false, this);
/*     */ 
/* 405 */         this.fd.incrementAndGetUseCount();
/*     */       }
/* 407 */       return this.channel;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private native void close0()
/*     */     throws IOException;
/*     */ 
/*     */   protected void finalize()
/*     */     throws IOException
/*     */   {
/* 427 */     if ((this.fd != null) && (this.fd != FileDescriptor.in))
/*     */     {
/* 434 */       runningFinalize.set(Boolean.TRUE);
/*     */       try {
/* 436 */         close();
/*     */       } finally {
/* 438 */         runningFinalize.set(Boolean.FALSE);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 416 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FileInputStream
 * JD-Core Version:    0.6.2
 */