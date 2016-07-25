/*      */ package sun.nio.ch;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.FileDescriptor;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.MappedByteBuffer;
/*      */ import java.nio.channels.ClosedByInterruptException;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.FileChannel.MapMode;
/*      */ import java.nio.channels.FileLock;
/*      */ import java.nio.channels.FileLockInterruptionException;
/*      */ import java.nio.channels.NonReadableChannelException;
/*      */ import java.nio.channels.NonWritableChannelException;
/*      */ import java.nio.channels.OverlappingFileLockException;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.nio.channels.WritableByteChannel;
/*      */ import java.security.AccessController;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import sun.misc.Cleaner;
/*      */ import sun.misc.IoTrace;
/*      */ import sun.misc.JavaNioAccess.BufferPool;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class FileChannelImpl extends FileChannel
/*      */ {
/* 1172 */   private static final long allocationGranularity = initIDs();
/*      */   private final FileDispatcher nd;
/*      */   private final FileDescriptor fd;
/*      */   private final boolean writable;
/*      */   private final boolean readable;
/*      */   private final boolean append;
/*      */   private final Object parent;
/*      */   private final String path;
/*   64 */   private final NativeThreadSet threads = new NativeThreadSet(2);
/*      */ 
/*   67 */   private final Object positionLock = new Object();
/*      */   private static volatile boolean transferSupported;
/*      */   private static volatile boolean pipeSupported;
/*      */   private static volatile boolean fileSupported;
/*      */   private static final long MAPPED_TRANSFER_SIZE = 8388608L;
/*      */   private static final int TRANSFER_SIZE = 8192;
/*      */   private static final int MAP_RO = 0;
/*      */   private static final int MAP_RW = 1;
/*      */   private static final int MAP_PV = 2;
/*      */   private volatile FileLockTable fileLockTable;
/*      */   private static boolean isSharedFileLockTable;
/*      */   private static volatile boolean propertyChecked;
/*      */ 
/*      */   private FileChannelImpl(FileDescriptor paramFileDescriptor, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Object paramObject)
/*      */   {
/*   72 */     this.fd = paramFileDescriptor;
/*   73 */     this.readable = paramBoolean1;
/*   74 */     this.writable = paramBoolean2;
/*   75 */     this.append = paramBoolean3;
/*   76 */     this.parent = paramObject;
/*   77 */     this.path = paramString;
/*   78 */     this.nd = new FileDispatcherImpl(paramBoolean3);
/*      */   }
/*      */ 
/*      */   public static FileChannel open(FileDescriptor paramFileDescriptor, String paramString, boolean paramBoolean1, boolean paramBoolean2, Object paramObject)
/*      */   {
/*   86 */     return new FileChannelImpl(paramFileDescriptor, paramString, paramBoolean1, paramBoolean2, false, paramObject);
/*      */   }
/*      */ 
/*      */   public static FileChannel open(FileDescriptor paramFileDescriptor, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Object paramObject)
/*      */   {
/*   94 */     return new FileChannelImpl(paramFileDescriptor, paramString, paramBoolean1, paramBoolean2, paramBoolean3, paramObject);
/*      */   }
/*      */ 
/*      */   private void ensureOpen() throws IOException {
/*   98 */     if (!isOpen())
/*   99 */       throw new ClosedChannelException();
/*      */   }
/*      */ 
/*      */   protected void implCloseChannel()
/*      */     throws IOException
/*      */   {
/*  107 */     if (this.fileLockTable != null) {
/*  108 */       for (FileLock localFileLock : this.fileLockTable.removeAll()) {
/*  109 */         synchronized (localFileLock) {
/*  110 */           if (localFileLock.isValid()) {
/*  111 */             this.nd.release(this.fd, localFileLock.position(), localFileLock.size());
/*  112 */             ((FileLockImpl)localFileLock).invalidate();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  118 */     this.nd.preClose(this.fd);
/*  119 */     this.threads.signalAndWait();
/*      */ 
/*  121 */     if (this.parent != null)
/*      */     {
/*  128 */       ((Closeable)this.parent).close();
/*      */     }
/*  130 */     else this.nd.close(this.fd);
/*      */   }
/*      */ 
/*      */   public int read(ByteBuffer paramByteBuffer)
/*      */     throws IOException
/*      */   {
/*  136 */     ensureOpen();
/*  137 */     if (!this.readable)
/*  138 */       throw new NonReadableChannelException();
/*  139 */     synchronized (this.positionLock) {
/*  140 */       int i = 0;
/*  141 */       int j = -1;
/*  142 */       Object localObject1 = IoTrace.fileReadBegin(this.path);
/*      */       try {
/*  144 */         begin();
/*  145 */         j = this.threads.add();
/*  146 */         if (!isOpen()) {
/*  147 */           k = 0;
/*      */ 
/*  153 */           this.threads.remove(j);
/*  154 */           IoTrace.fileReadEnd(localObject1, i > 0 ? i : 0L);
/*  155 */           end(i > 0);
/*  156 */           assert (IOStatus.check(i)); return k;
/*      */         }
/*      */         do
/*  149 */           i = IOUtil.read(this.fd, paramByteBuffer, -1L, this.nd);
/*  150 */         while ((i == -3) && (isOpen()));
/*  151 */         int k = IOStatus.normalize(i);
/*      */ 
/*  153 */         this.threads.remove(j);
/*  154 */         IoTrace.fileReadEnd(localObject1, i > 0 ? i : 0L);
/*  155 */         end(i > 0);
/*  156 */         assert (IOStatus.check(i)); return k;
/*      */       }
/*      */       finally
/*      */       {
/*  153 */         this.threads.remove(j);
/*  154 */         IoTrace.fileReadEnd(localObject1, i > 0 ? i : 0L);
/*  155 */         end(i > 0);
/*  156 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long read(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  164 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/*  165 */       throw new IndexOutOfBoundsException();
/*  166 */     ensureOpen();
/*  167 */     if (!this.readable)
/*  168 */       throw new NonReadableChannelException();
/*  169 */     synchronized (this.positionLock) {
/*  170 */       long l1 = 0L;
/*  171 */       int i = -1;
/*  172 */       Object localObject1 = IoTrace.fileReadBegin(this.path);
/*      */       try {
/*  174 */         begin();
/*  175 */         i = this.threads.add();
/*  176 */         if (!isOpen()) {
/*  177 */           l2 = 0L;
/*      */ 
/*  183 */           this.threads.remove(i);
/*  184 */           IoTrace.fileReadEnd(localObject1, l1 > 0L ? l1 : 0L);
/*  185 */           end(l1 > 0L);
/*  186 */           assert (IOStatus.check(l1)); return l2;
/*      */         }
/*      */         do
/*  179 */           l1 = IOUtil.read(this.fd, paramArrayOfByteBuffer, paramInt1, paramInt2, this.nd);
/*  180 */         while ((l1 == -3L) && (isOpen()));
/*  181 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  183 */         this.threads.remove(i);
/*  184 */         IoTrace.fileReadEnd(localObject1, l1 > 0L ? l1 : 0L);
/*  185 */         end(l1 > 0L);
/*  186 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  183 */         this.threads.remove(i);
/*  184 */         IoTrace.fileReadEnd(localObject1, l1 > 0L ? l1 : 0L);
/*  185 */         end(l1 > 0L);
/*  186 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int write(ByteBuffer paramByteBuffer) throws IOException {
/*  192 */     ensureOpen();
/*  193 */     if (!this.writable)
/*  194 */       throw new NonWritableChannelException();
/*  195 */     synchronized (this.positionLock) {
/*  196 */       int i = 0;
/*  197 */       int j = -1;
/*  198 */       Object localObject1 = IoTrace.fileWriteBegin(this.path);
/*      */       try {
/*  200 */         begin();
/*  201 */         j = this.threads.add();
/*  202 */         if (!isOpen()) {
/*  203 */           k = 0;
/*      */ 
/*  209 */           this.threads.remove(j);
/*  210 */           end(i > 0);
/*  211 */           IoTrace.fileWriteEnd(localObject1, i > 0 ? i : 0L);
/*  212 */           assert (IOStatus.check(i)); return k;
/*      */         }
/*      */         do
/*  205 */           i = IOUtil.write(this.fd, paramByteBuffer, -1L, this.nd);
/*  206 */         while ((i == -3) && (isOpen()));
/*  207 */         int k = IOStatus.normalize(i);
/*      */ 
/*  209 */         this.threads.remove(j);
/*  210 */         end(i > 0);
/*  211 */         IoTrace.fileWriteEnd(localObject1, i > 0 ? i : 0L);
/*  212 */         assert (IOStatus.check(i)); return k;
/*      */       }
/*      */       finally
/*      */       {
/*  209 */         this.threads.remove(j);
/*  210 */         end(i > 0);
/*  211 */         IoTrace.fileWriteEnd(localObject1, i > 0 ? i : 0L);
/*  212 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  220 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/*  221 */       throw new IndexOutOfBoundsException();
/*  222 */     ensureOpen();
/*  223 */     if (!this.writable)
/*  224 */       throw new NonWritableChannelException();
/*  225 */     synchronized (this.positionLock) {
/*  226 */       long l1 = 0L;
/*  227 */       int i = -1;
/*  228 */       Object localObject1 = IoTrace.fileWriteBegin(this.path);
/*      */       try {
/*  230 */         begin();
/*  231 */         i = this.threads.add();
/*  232 */         if (!isOpen()) {
/*  233 */           l2 = 0L;
/*      */ 
/*  239 */           this.threads.remove(i);
/*  240 */           IoTrace.fileWriteEnd(localObject1, l1 > 0L ? l1 : 0L);
/*  241 */           end(l1 > 0L);
/*  242 */           assert (IOStatus.check(l1)); return l2;
/*      */         }
/*      */         do
/*  235 */           l1 = IOUtil.write(this.fd, paramArrayOfByteBuffer, paramInt1, paramInt2, this.nd);
/*  236 */         while ((l1 == -3L) && (isOpen()));
/*  237 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  239 */         this.threads.remove(i);
/*  240 */         IoTrace.fileWriteEnd(localObject1, l1 > 0L ? l1 : 0L);
/*  241 */         end(l1 > 0L);
/*  242 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  239 */         this.threads.remove(i);
/*  240 */         IoTrace.fileWriteEnd(localObject1, l1 > 0L ? l1 : 0L);
/*  241 */         end(l1 > 0L);
/*  242 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long position()
/*      */     throws IOException
/*      */   {
/*  250 */     ensureOpen();
/*  251 */     synchronized (this.positionLock) {
/*  252 */       long l1 = -1L;
/*  253 */       int i = -1;
/*      */       try {
/*  255 */         begin();
/*  256 */         i = this.threads.add();
/*  257 */         if (!isOpen()) {
/*  258 */           l2 = 0L;
/*      */ 
/*  265 */           this.threads.remove(i);
/*  266 */           end(l1 > -1L);
/*  267 */           assert (IOStatus.check(l1)); return l2;
/*      */         }
/*      */         do
/*  261 */           l1 = this.append ? this.nd.size(this.fd) : position0(this.fd, -1L);
/*  262 */         while ((l1 == -3L) && (isOpen()));
/*  263 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  265 */         this.threads.remove(i);
/*  266 */         end(l1 > -1L);
/*  267 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  265 */         this.threads.remove(i);
/*  266 */         end(l1 > -1L);
/*  267 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public FileChannel position(long paramLong) throws IOException {
/*  273 */     ensureOpen();
/*  274 */     if (paramLong < 0L)
/*  275 */       throw new IllegalArgumentException();
/*  276 */     synchronized (this.positionLock) {
/*  277 */       long l = -1L;
/*  278 */       int i = -1;
/*      */       try {
/*  280 */         begin();
/*  281 */         i = this.threads.add();
/*  282 */         if (!isOpen()) {
/*  283 */           localObject1 = null;
/*      */ 
/*  289 */           this.threads.remove(i);
/*  290 */           end(l > -1L);
/*  291 */           assert (IOStatus.check(l)); return localObject1;
/*      */         }
/*      */         do
/*  285 */           l = position0(this.fd, paramLong);
/*  286 */         while ((l == -3L) && (isOpen()));
/*  287 */         Object localObject1 = this;
/*      */ 
/*  289 */         this.threads.remove(i);
/*  290 */         end(l > -1L);
/*  291 */         assert (IOStatus.check(l)); return localObject1;
/*      */       }
/*      */       finally
/*      */       {
/*  289 */         this.threads.remove(i);
/*  290 */         end(l > -1L);
/*  291 */         if ((!$assertionsDisabled) && (!IOStatus.check(l))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long size() throws IOException {
/*  297 */     ensureOpen();
/*  298 */     synchronized (this.positionLock) {
/*  299 */       long l1 = -1L;
/*  300 */       int i = -1;
/*      */       try {
/*  302 */         begin();
/*  303 */         i = this.threads.add();
/*  304 */         if (!isOpen()) {
/*  305 */           l2 = -1L;
/*      */ 
/*  311 */           this.threads.remove(i);
/*  312 */           end(l1 > -1L);
/*  313 */           assert (IOStatus.check(l1)); return l2;
/*      */         }
/*      */         do
/*  307 */           l1 = this.nd.size(this.fd);
/*  308 */         while ((l1 == -3L) && (isOpen()));
/*  309 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  311 */         this.threads.remove(i);
/*  312 */         end(l1 > -1L);
/*  313 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  311 */         this.threads.remove(i);
/*  312 */         end(l1 > -1L);
/*  313 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public FileChannel truncate(long paramLong) throws IOException {
/*  319 */     ensureOpen();
/*  320 */     if (paramLong < 0L)
/*  321 */       throw new IllegalArgumentException();
/*  322 */     if (paramLong > size())
/*  323 */       return this;
/*  324 */     if (!this.writable)
/*  325 */       throw new NonWritableChannelException();
/*  326 */     synchronized (this.positionLock) {
/*  327 */       int i = -1;
/*  328 */       long l = -1L;
/*  329 */       int j = -1;
/*      */       try {
/*  331 */         begin();
/*  332 */         j = this.threads.add();
/*  333 */         if (!isOpen()) {
/*  334 */           localObject1 = null;
/*      */ 
/*  359 */           this.threads.remove(j);
/*  360 */           end(i > -1);
/*  361 */           assert (IOStatus.check(i)); return localObject1;
/*      */         }
/*      */         do
/*  338 */           l = position0(this.fd, -1L);
/*  339 */         while ((l == -3L) && (isOpen()));
/*  340 */         if (!isOpen()) {
/*  341 */           localObject1 = null;
/*      */ 
/*  359 */           this.threads.remove(j);
/*  360 */           end(i > -1);
/*  361 */           assert (IOStatus.check(i)); return localObject1;
/*      */         }
/*  342 */         assert (l >= 0L);
/*      */         do
/*      */         {
/*  346 */           i = this.nd.truncate(this.fd, paramLong);
/*  347 */         }while ((i == -3) && (isOpen()));
/*  348 */         if (!isOpen()) {
/*  349 */           localObject1 = null;
/*      */ 
/*  359 */           this.threads.remove(j);
/*  360 */           end(i > -1);
/*  361 */           assert (IOStatus.check(i)); return localObject1;
/*      */         }
/*  352 */         if (l > paramLong)
/*  353 */           l = paramLong;
/*      */         do
/*  355 */           i = (int)position0(this.fd, l);
/*  356 */         while ((i == -3) && (isOpen()));
/*  357 */         Object localObject1 = this;
/*      */ 
/*  359 */         this.threads.remove(j);
/*  360 */         end(i > -1);
/*  361 */         assert (IOStatus.check(i)); return localObject1;
/*      */       }
/*      */       finally
/*      */       {
/*  359 */         this.threads.remove(j);
/*  360 */         end(i > -1);
/*  361 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void force(boolean paramBoolean) throws IOException {
/*  367 */     ensureOpen();
/*  368 */     int i = -1;
/*  369 */     int j = -1;
/*      */     try {
/*  371 */       begin();
/*  372 */       j = this.threads.add();
/*  373 */       if (!isOpen())
/*      */         return;
/*      */       do {
/*  376 */         i = this.nd.force(this.fd, paramBoolean);
/*  377 */         if (i != -3) break;  } while (isOpen());
/*      */     } finally {
/*  379 */       this.threads.remove(j);
/*  380 */       end(i > -1);
/*  381 */       if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */   private long transferToDirectly(long paramLong, int paramInt, WritableByteChannel paramWritableByteChannel)
/*      */     throws IOException
/*      */   {
/*  404 */     if (!transferSupported) {
/*  405 */       return -4L;
/*      */     }
/*  407 */     FileDescriptor localFileDescriptor = null;
/*  408 */     if ((paramWritableByteChannel instanceof FileChannelImpl)) {
/*  409 */       if (!fileSupported)
/*  410 */         return -6L;
/*  411 */       localFileDescriptor = ((FileChannelImpl)paramWritableByteChannel).fd;
/*  412 */     } else if ((paramWritableByteChannel instanceof SelChImpl))
/*      */     {
/*  414 */       if (((paramWritableByteChannel instanceof SinkChannelImpl)) && (!pipeSupported))
/*  415 */         return -6L;
/*  416 */       localFileDescriptor = ((SelChImpl)paramWritableByteChannel).getFD();
/*      */     }
/*  418 */     if (localFileDescriptor == null)
/*  419 */       return -4L;
/*  420 */     int i = IOUtil.fdVal(this.fd);
/*  421 */     int j = IOUtil.fdVal(localFileDescriptor);
/*  422 */     if (i == j) {
/*  423 */       return -4L;
/*      */     }
/*  425 */     long l1 = -1L;
/*  426 */     int k = -1;
/*      */     try {
/*  428 */       begin();
/*  429 */       k = this.threads.add();
/*      */       long l2;
/*  430 */       if (!isOpen())
/*  431 */         return -1L;
/*      */       do
/*  433 */         l1 = transferTo0(i, paramLong, paramInt, j);
/*  434 */       while ((l1 == -3L) && (isOpen()));
/*  435 */       if (l1 == -6L) {
/*  436 */         if ((paramWritableByteChannel instanceof SinkChannelImpl))
/*  437 */           pipeSupported = false;
/*  438 */         if ((paramWritableByteChannel instanceof FileChannelImpl))
/*  439 */           fileSupported = false;
/*  440 */         return -6L;
/*      */       }
/*  442 */       if (l1 == -4L)
/*      */       {
/*  444 */         transferSupported = false;
/*  445 */         return -4L;
/*      */       }
/*  447 */       return IOStatus.normalize(l1);
/*      */     } finally {
/*  449 */       this.threads.remove(k);
/*  450 */       end(l1 > -1L);
/*      */     }
/*      */   }
/*      */ 
/*      */   private long transferToTrustedChannel(long paramLong1, long paramLong2, WritableByteChannel paramWritableByteChannel)
/*      */     throws IOException
/*      */   {
/*  461 */     boolean bool = paramWritableByteChannel instanceof SelChImpl;
/*  462 */     if ((!(paramWritableByteChannel instanceof FileChannelImpl)) && (!bool)) {
/*  463 */       return -4L;
/*      */     }
/*      */ 
/*  466 */     long l1 = paramLong2;
/*  467 */     while (l1 > 0L) {
/*  468 */       long l2 = Math.min(l1, 8388608L);
/*      */       try {
/*  470 */         MappedByteBuffer localMappedByteBuffer = map(FileChannel.MapMode.READ_ONLY, paramLong1, l2);
/*      */         try
/*      */         {
/*  473 */           int i = paramWritableByteChannel.write(localMappedByteBuffer);
/*  474 */           assert (i >= 0);
/*  475 */           l1 -= i;
/*  476 */           if (bool)
/*      */           {
/*  483 */             unmap(localMappedByteBuffer); break;
/*      */           }
/*  480 */           assert (i > 0);
/*  481 */           paramLong1 += i;
/*      */         } finally {
/*  483 */           unmap(localMappedByteBuffer);
/*      */         }
/*      */       }
/*      */       catch (ClosedByInterruptException localClosedByInterruptException)
/*      */       {
/*  488 */         assert (!paramWritableByteChannel.isOpen());
/*      */         try {
/*  490 */           close();
/*      */         } catch (Throwable localThrowable) {
/*  492 */           localClosedByInterruptException.addSuppressed(localThrowable);
/*      */         }
/*  494 */         throw localClosedByInterruptException;
/*      */       }
/*      */       catch (IOException localIOException) {
/*  497 */         if (l1 == paramLong2)
/*  498 */           throw localIOException;
/*  499 */         break;
/*      */       }
/*      */     }
/*  502 */     return paramLong2 - l1;
/*      */   }
/*      */ 
/*      */   private long transferToArbitraryChannel(long paramLong, int paramInt, WritableByteChannel paramWritableByteChannel)
/*      */     throws IOException
/*      */   {
/*  510 */     int i = Math.min(paramInt, 8192);
/*  511 */     ByteBuffer localByteBuffer = Util.getTemporaryDirectBuffer(i);
/*  512 */     long l1 = 0L;
/*  513 */     long l2 = paramLong;
/*      */     try {
/*  515 */       Util.erase(localByteBuffer);
/*  516 */       while (l1 < paramInt) {
/*  517 */         localByteBuffer.limit(Math.min((int)(paramInt - l1), 8192));
/*  518 */         int j = read(localByteBuffer, l2);
/*  519 */         if (j <= 0)
/*      */           break;
/*  521 */         localByteBuffer.flip();
/*      */ 
/*  524 */         int k = paramWritableByteChannel.write(localByteBuffer);
/*  525 */         l1 += k;
/*  526 */         if (k != j)
/*      */           break;
/*  528 */         l2 += k;
/*  529 */         localByteBuffer.clear();
/*      */       }
/*  531 */       return l1;
/*      */     } catch (IOException localIOException) {
/*  533 */       if (l1 > 0L)
/*  534 */         return l1;
/*  535 */       throw localIOException;
/*      */     } finally {
/*  537 */       Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long transferTo(long paramLong1, long paramLong2, WritableByteChannel paramWritableByteChannel)
/*      */     throws IOException
/*      */   {
/*  545 */     ensureOpen();
/*  546 */     if (!paramWritableByteChannel.isOpen())
/*  547 */       throw new ClosedChannelException();
/*  548 */     if (!this.readable)
/*  549 */       throw new NonReadableChannelException();
/*  550 */     if (((paramWritableByteChannel instanceof FileChannelImpl)) && (!((FileChannelImpl)paramWritableByteChannel).writable))
/*      */     {
/*  552 */       throw new NonWritableChannelException();
/*  553 */     }if ((paramLong1 < 0L) || (paramLong2 < 0L))
/*  554 */       throw new IllegalArgumentException();
/*  555 */     long l1 = size();
/*  556 */     if (paramLong1 > l1)
/*  557 */       return 0L;
/*  558 */     int i = (int)Math.min(paramLong2, 2147483647L);
/*  559 */     if (l1 - paramLong1 < i)
/*  560 */       i = (int)(l1 - paramLong1);
/*      */     long l2;
/*  565 */     if ((l2 = transferToDirectly(paramLong1, i, paramWritableByteChannel)) >= 0L) {
/*  566 */       return l2;
/*      */     }
/*      */ 
/*  569 */     if ((l2 = transferToTrustedChannel(paramLong1, i, paramWritableByteChannel)) >= 0L) {
/*  570 */       return l2;
/*      */     }
/*      */ 
/*  573 */     return transferToArbitraryChannel(paramLong1, i, paramWritableByteChannel);
/*      */   }
/*      */ 
/*      */   private long transferFromFileChannel(FileChannelImpl paramFileChannelImpl, long paramLong1, long paramLong2)
/*      */     throws IOException
/*      */   {
/*  580 */     if (!paramFileChannelImpl.readable)
/*  581 */       throw new NonReadableChannelException();
/*  582 */     synchronized (paramFileChannelImpl.positionLock) {
/*  583 */       long l1 = paramFileChannelImpl.position();
/*  584 */       long l2 = Math.min(paramLong2, paramFileChannelImpl.size() - l1);
/*      */ 
/*  586 */       long l3 = l2;
/*  587 */       long l4 = l1;
/*  588 */       if (l3 > 0L) {
/*  589 */         l5 = Math.min(l3, 8388608L);
/*      */ 
/*  591 */         MappedByteBuffer localMappedByteBuffer = paramFileChannelImpl.map(FileChannel.MapMode.READ_ONLY, l4, l5);
/*      */         try {
/*  593 */           long l6 = write(localMappedByteBuffer, paramLong1);
/*  594 */           assert (l6 > 0L);
/*  595 */           l4 += l6;
/*  596 */           paramLong1 += l6;
/*  597 */           l3 -= l6;
/*      */ 
/*  604 */           unmap(localMappedByteBuffer);
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*  600 */           if (l3 == l2)
/*  601 */             throw localIOException;
/*      */         }
/*      */         finally {
/*  604 */           unmap(localMappedByteBuffer);
/*      */         }
/*      */       }
/*  607 */       long l5 = l2 - l3;
/*  608 */       paramFileChannelImpl.position(l1 + l5);
/*  609 */       return l5;
/*      */     }
/*      */   }
/*      */ 
/*      */   private long transferFromArbitraryChannel(ReadableByteChannel paramReadableByteChannel, long paramLong1, long paramLong2)
/*      */     throws IOException
/*      */   {
/*  620 */     int i = (int)Math.min(paramLong2, 8192L);
/*  621 */     ByteBuffer localByteBuffer = Util.getTemporaryDirectBuffer(i);
/*  622 */     long l1 = 0L;
/*  623 */     long l2 = paramLong1;
/*      */     try {
/*  625 */       Util.erase(localByteBuffer);
/*  626 */       while (l1 < paramLong2) {
/*  627 */         localByteBuffer.limit((int)Math.min(paramLong2 - l1, 8192L));
/*      */ 
/*  630 */         int j = paramReadableByteChannel.read(localByteBuffer);
/*  631 */         if (j <= 0)
/*      */           break;
/*  633 */         localByteBuffer.flip();
/*  634 */         int k = write(localByteBuffer, l2);
/*  635 */         l1 += k;
/*  636 */         if (k != j)
/*      */           break;
/*  638 */         l2 += k;
/*  639 */         localByteBuffer.clear();
/*      */       }
/*  641 */       return l1;
/*      */     } catch (IOException localIOException) {
/*  643 */       if (l1 > 0L)
/*  644 */         return l1;
/*  645 */       throw localIOException;
/*      */     } finally {
/*  647 */       Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long transferFrom(ReadableByteChannel paramReadableByteChannel, long paramLong1, long paramLong2)
/*      */     throws IOException
/*      */   {
/*  655 */     ensureOpen();
/*  656 */     if (!paramReadableByteChannel.isOpen())
/*  657 */       throw new ClosedChannelException();
/*  658 */     if (!this.writable)
/*  659 */       throw new NonWritableChannelException();
/*  660 */     if ((paramLong1 < 0L) || (paramLong2 < 0L))
/*  661 */       throw new IllegalArgumentException();
/*  662 */     if (paramLong1 > size())
/*  663 */       return 0L;
/*  664 */     if ((paramReadableByteChannel instanceof FileChannelImpl)) {
/*  665 */       return transferFromFileChannel((FileChannelImpl)paramReadableByteChannel, paramLong1, paramLong2);
/*      */     }
/*      */ 
/*  668 */     return transferFromArbitraryChannel(paramReadableByteChannel, paramLong1, paramLong2);
/*      */   }
/*      */ 
/*      */   public int read(ByteBuffer paramByteBuffer, long paramLong) throws IOException {
/*  672 */     if (paramByteBuffer == null)
/*  673 */       throw new NullPointerException();
/*  674 */     if (paramLong < 0L)
/*  675 */       throw new IllegalArgumentException("Negative position");
/*  676 */     if (!this.readable)
/*  677 */       throw new NonReadableChannelException();
/*  678 */     ensureOpen();
/*  679 */     if (this.nd.needsPositionLock()) {
/*  680 */       synchronized (this.positionLock) {
/*  681 */         return readInternal(paramByteBuffer, paramLong);
/*      */       }
/*      */     }
/*  684 */     return readInternal(paramByteBuffer, paramLong);
/*      */   }
/*      */ 
/*      */   private int readInternal(ByteBuffer paramByteBuffer, long paramLong) throws IOException
/*      */   {
/*  689 */     assert ((!this.nd.needsPositionLock()) || (Thread.holdsLock(this.positionLock)));
/*  690 */     int i = 0;
/*  691 */     int j = -1;
/*  692 */     Object localObject1 = IoTrace.fileReadBegin(this.path);
/*      */     try {
/*  694 */       begin();
/*  695 */       j = this.threads.add();
/*      */       int k;
/*  696 */       if (!isOpen())
/*  697 */         return -1;
/*      */       do
/*  699 */         i = IOUtil.read(this.fd, paramByteBuffer, paramLong, this.nd);
/*  700 */       while ((i == -3) && (isOpen()));
/*  701 */       return IOStatus.normalize(i);
/*      */     } finally {
/*  703 */       this.threads.remove(j);
/*  704 */       IoTrace.fileReadEnd(localObject1, i > 0 ? i : 0L);
/*  705 */       end(i > 0);
/*  706 */       if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError(); 
/*      */     }
/*      */   }
/*      */ 
/*      */   public int write(ByteBuffer paramByteBuffer, long paramLong) throws IOException {
/*  711 */     if (paramByteBuffer == null)
/*  712 */       throw new NullPointerException();
/*  713 */     if (paramLong < 0L)
/*  714 */       throw new IllegalArgumentException("Negative position");
/*  715 */     if (!this.writable)
/*  716 */       throw new NonWritableChannelException();
/*  717 */     ensureOpen();
/*  718 */     if (this.nd.needsPositionLock()) {
/*  719 */       synchronized (this.positionLock) {
/*  720 */         return writeInternal(paramByteBuffer, paramLong);
/*      */       }
/*      */     }
/*  723 */     return writeInternal(paramByteBuffer, paramLong);
/*      */   }
/*      */ 
/*      */   private int writeInternal(ByteBuffer paramByteBuffer, long paramLong) throws IOException
/*      */   {
/*  728 */     assert ((!this.nd.needsPositionLock()) || (Thread.holdsLock(this.positionLock)));
/*  729 */     int i = 0;
/*  730 */     int j = -1;
/*  731 */     Object localObject1 = IoTrace.fileWriteBegin(this.path);
/*      */     try {
/*  733 */       begin();
/*  734 */       j = this.threads.add();
/*      */       int k;
/*  735 */       if (!isOpen())
/*  736 */         return -1;
/*      */       do
/*  738 */         i = IOUtil.write(this.fd, paramByteBuffer, paramLong, this.nd);
/*  739 */       while ((i == -3) && (isOpen()));
/*  740 */       return IOStatus.normalize(i);
/*      */     } finally {
/*  742 */       this.threads.remove(j);
/*  743 */       end(i > 0);
/*  744 */       IoTrace.fileWriteEnd(localObject1, i > 0 ? i : 0L);
/*  745 */       if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void unmap(MappedByteBuffer paramMappedByteBuffer)
/*      */   {
/*  808 */     Cleaner localCleaner = ((DirectBuffer)paramMappedByteBuffer).cleaner();
/*  809 */     if (localCleaner != null)
/*  810 */       localCleaner.clean();
/*      */   }
/*      */ 
/*      */   public MappedByteBuffer map(FileChannel.MapMode paramMapMode, long paramLong1, long paramLong2)
/*      */     throws IOException
/*      */   {
/*  820 */     ensureOpen();
/*  821 */     if (paramLong1 < 0L)
/*  822 */       throw new IllegalArgumentException("Negative position");
/*  823 */     if (paramLong2 < 0L)
/*  824 */       throw new IllegalArgumentException("Negative size");
/*  825 */     if (paramLong1 + paramLong2 < 0L)
/*  826 */       throw new IllegalArgumentException("Position + size overflow");
/*  827 */     if (paramLong2 > 2147483647L)
/*  828 */       throw new IllegalArgumentException("Size exceeds Integer.MAX_VALUE");
/*  829 */     int i = -1;
/*  830 */     if (paramMapMode == FileChannel.MapMode.READ_ONLY)
/*  831 */       i = 0;
/*  832 */     else if (paramMapMode == FileChannel.MapMode.READ_WRITE)
/*  833 */       i = 1;
/*  834 */     else if (paramMapMode == FileChannel.MapMode.PRIVATE)
/*  835 */       i = 2;
/*  836 */     assert (i >= 0);
/*  837 */     if ((paramMapMode != FileChannel.MapMode.READ_ONLY) && (!this.writable))
/*  838 */       throw new NonWritableChannelException();
/*  839 */     if (!this.readable) {
/*  840 */       throw new NonReadableChannelException();
/*      */     }
/*  842 */     long l1 = -1L;
/*  843 */     int j = -1;
/*      */     try {
/*  845 */       begin();
/*  846 */       j = this.threads.add();
/*  847 */       if (!isOpen())
/*  848 */         return null;
/*  849 */       if (size() < paramLong1 + paramLong2) {
/*  850 */         if (!this.writable) {
/*  851 */           throw new IOException("Channel not open for writing - cannot extend file to required size");
/*      */         }
/*      */         int k;
/*      */         do
/*      */         {
/*  856 */           k = this.nd.truncate(this.fd, paramLong1 + paramLong2);
/*  857 */         }while ((k == -3) && (isOpen()));
/*      */       }
/*  859 */       if (paramLong2 == 0L) {
/*  860 */         l1 = 0L;
/*      */ 
/*  862 */         FileDescriptor localFileDescriptor1 = new FileDescriptor();
/*      */         MappedByteBuffer localMappedByteBuffer2;
/*  863 */         if ((!this.writable) || (i == 0)) {
/*  864 */           return Util.newMappedByteBufferR(0, 0L, localFileDescriptor1, null);
/*      */         }
/*  866 */         return Util.newMappedByteBuffer(0, 0L, localFileDescriptor1, null);
/*      */       }
/*      */ 
/*  869 */       int m = (int)(paramLong1 % allocationGranularity);
/*  870 */       long l2 = paramLong1 - m;
/*  871 */       long l3 = paramLong2 + m;
/*      */       try
/*      */       {
/*  874 */         l1 = map0(i, l2, l3);
/*      */       }
/*      */       catch (OutOfMemoryError localOutOfMemoryError1)
/*      */       {
/*  878 */         System.gc();
/*      */         try {
/*  880 */           Thread.sleep(100L);
/*      */         } catch (InterruptedException localInterruptedException) {
/*  882 */           Thread.currentThread().interrupt();
/*      */         }
/*      */         try {
/*  885 */           l1 = map0(i, l2, l3);
/*      */         }
/*      */         catch (OutOfMemoryError localOutOfMemoryError2) {
/*  888 */           throw new IOException("Map failed", localOutOfMemoryError2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       FileDescriptor localFileDescriptor2;
/*      */       try
/*      */       {
/*  896 */         localFileDescriptor2 = this.nd.duplicateForMapping(this.fd);
/*      */       } catch (IOException localIOException) {
/*  898 */         unmap0(l1, l3);
/*  899 */         throw localIOException;
/*      */       }
/*      */ 
/*  902 */       assert (IOStatus.checkAll(l1));
/*  903 */       assert (l1 % allocationGranularity == 0L);
/*  904 */       int n = (int)paramLong2;
/*  905 */       Unmapper localUnmapper = new Unmapper(l1, l3, n, localFileDescriptor2, null);
/*      */       MappedByteBuffer localMappedByteBuffer3;
/*  906 */       if ((!this.writable) || (i == 0)) {
/*  907 */         return Util.newMappedByteBufferR(n, l1 + m, localFileDescriptor2, localUnmapper);
/*      */       }
/*      */ 
/*  912 */       return Util.newMappedByteBuffer(n, l1 + m, localFileDescriptor2, localUnmapper);
/*      */     }
/*      */     finally
/*      */     {
/*  918 */       this.threads.remove(j);
/*  919 */       end(IOStatus.checkAll(l1));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static JavaNioAccess.BufferPool getMappedBufferPool()
/*      */   {
/*  928 */     return new JavaNioAccess.BufferPool()
/*      */     {
/*      */       public String getName() {
/*  931 */         return "mapped";
/*      */       }
/*      */ 
/*      */       public long getCount() {
/*  935 */         return FileChannelImpl.Unmapper.count;
/*      */       }
/*      */ 
/*      */       public long getTotalCapacity() {
/*  939 */         return FileChannelImpl.Unmapper.totalCapacity;
/*      */       }
/*      */ 
/*      */       public long getMemoryUsed() {
/*  943 */         return FileChannelImpl.Unmapper.totalSize;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private static boolean isSharedFileLockTable()
/*      */   {
/*  966 */     if (!propertyChecked) {
/*  967 */       synchronized (FileChannelImpl.class) {
/*  968 */         if (!propertyChecked) {
/*  969 */           String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.nio.ch.disableSystemWideOverlappingFileLockCheck"));
/*      */ 
/*  972 */           isSharedFileLockTable = (str == null) || (str.equals("false"));
/*  973 */           propertyChecked = true;
/*      */         }
/*      */       }
/*      */     }
/*  977 */     return isSharedFileLockTable;
/*      */   }
/*      */ 
/*      */   private FileLockTable fileLockTable() throws IOException {
/*  981 */     if (this.fileLockTable == null) {
/*  982 */       synchronized (this) {
/*  983 */         if (this.fileLockTable == null) {
/*  984 */           if (isSharedFileLockTable()) {
/*  985 */             int i = this.threads.add();
/*      */             try {
/*  987 */               ensureOpen();
/*  988 */               this.fileLockTable = FileLockTable.newSharedFileLockTable(this, this.fd);
/*      */             } finally {
/*  990 */               this.threads.remove(i);
/*      */             }
/*      */           } else {
/*  993 */             this.fileLockTable = new SimpleFileLockTable();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  998 */     return this.fileLockTable;
/*      */   }
/*      */ 
/*      */   public FileLock lock(long paramLong1, long paramLong2, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1004 */     ensureOpen();
/* 1005 */     if ((paramBoolean) && (!this.readable))
/* 1006 */       throw new NonReadableChannelException();
/* 1007 */     if ((!paramBoolean) && (!this.writable))
/* 1008 */       throw new NonWritableChannelException();
/* 1009 */     Object localObject1 = new FileLockImpl(this, paramLong1, paramLong2, paramBoolean);
/* 1010 */     FileLockTable localFileLockTable = fileLockTable();
/* 1011 */     localFileLockTable.add((FileLock)localObject1);
/* 1012 */     boolean bool = false;
/* 1013 */     int i = -1;
/*      */     try {
/* 1015 */       begin();
/* 1016 */       i = this.threads.add();
/* 1017 */       if (!isOpen())
/* 1018 */         return null;
/*      */       int j;
/*      */       do
/* 1021 */         j = this.nd.lock(this.fd, true, paramLong1, paramLong2, paramBoolean);
/* 1022 */       while ((j == 2) && (isOpen()));
/* 1023 */       if (isOpen()) {
/* 1024 */         if (j == 1) {
/* 1025 */           assert (paramBoolean);
/* 1026 */           FileLockImpl localFileLockImpl = new FileLockImpl(this, paramLong1, paramLong2, false);
/*      */ 
/* 1028 */           localFileLockTable.replace((FileLock)localObject1, localFileLockImpl);
/* 1029 */           localObject1 = localFileLockImpl;
/*      */         }
/* 1031 */         bool = true;
/*      */       }
/*      */     } finally {
/* 1034 */       if (!bool)
/* 1035 */         localFileLockTable.remove((FileLock)localObject1);
/* 1036 */       this.threads.remove(i);
/*      */       try {
/* 1038 */         end(bool);
/*      */       } catch (ClosedByInterruptException localClosedByInterruptException3) {
/* 1040 */         throw new FileLockInterruptionException();
/*      */       }
/*      */     }
/* 1043 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public FileLock tryLock(long paramLong1, long paramLong2, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1049 */     ensureOpen();
/* 1050 */     if ((paramBoolean) && (!this.readable))
/* 1051 */       throw new NonReadableChannelException();
/* 1052 */     if ((!paramBoolean) && (!this.writable))
/* 1053 */       throw new NonWritableChannelException();
/* 1054 */     FileLockImpl localFileLockImpl = new FileLockImpl(this, paramLong1, paramLong2, paramBoolean);
/* 1055 */     FileLockTable localFileLockTable = fileLockTable();
/* 1056 */     localFileLockTable.add(localFileLockImpl);
/*      */ 
/* 1059 */     int j = this.threads.add();
/*      */     try {
/*      */       int i;
/*      */       try { ensureOpen();
/* 1063 */         i = this.nd.lock(this.fd, false, paramLong1, paramLong2, paramBoolean);
/*      */       } catch (IOException localIOException) {
/* 1065 */         localFileLockTable.remove(localFileLockImpl);
/* 1066 */         throw localIOException;
/*      */       }
/*      */       Object localObject1;
/* 1068 */       if (i == -1) {
/* 1069 */         localFileLockTable.remove(localFileLockImpl);
/* 1070 */         return null;
/*      */       }
/* 1072 */       if (i == 1) {
/* 1073 */         assert (paramBoolean);
/* 1074 */         localObject1 = new FileLockImpl(this, paramLong1, paramLong2, false);
/*      */ 
/* 1076 */         localFileLockTable.replace(localFileLockImpl, (FileLock)localObject1);
/* 1077 */         return localObject1;
/*      */       }
/* 1079 */       return localFileLockImpl;
/*      */     } finally {
/* 1081 */       this.threads.remove(j);
/*      */     }
/*      */   }
/*      */ 
/*      */   void release(FileLockImpl paramFileLockImpl) throws IOException {
/* 1086 */     int i = this.threads.add();
/*      */     try {
/* 1088 */       ensureOpen();
/* 1089 */       this.nd.release(this.fd, paramFileLockImpl.position(), paramFileLockImpl.size());
/*      */     } finally {
/* 1091 */       this.threads.remove(i);
/*      */     }
/* 1093 */     assert (this.fileLockTable != null);
/* 1094 */     this.fileLockTable.remove(paramFileLockImpl);
/*      */   }
/*      */ 
/*      */   private native long map0(int paramInt, long paramLong1, long paramLong2)
/*      */     throws IOException;
/*      */ 
/*      */   private static native int unmap0(long paramLong1, long paramLong2);
/*      */ 
/*      */   private native long transferTo0(int paramInt1, long paramLong1, long paramLong2, int paramInt2);
/*      */ 
/*      */   private native long position0(FileDescriptor paramFileDescriptor, long paramLong);
/*      */ 
/*      */   private static native long initIDs();
/*      */ 
/*      */   static
/*      */   {
/*  388 */     transferSupported = true;
/*      */ 
/*  393 */     pipeSupported = true;
/*      */ 
/*  398 */     fileSupported = true;
/*      */ 
/* 1171 */     Util.load();
/*      */   }
/*      */ 
/*      */   private static class SimpleFileLockTable extends FileLockTable
/*      */   {
/* 1105 */     private final List<FileLock> lockList = new ArrayList(2);
/*      */ 
/*      */     private void checkList(long paramLong1, long paramLong2)
/*      */       throws OverlappingFileLockException
/*      */     {
/* 1113 */       assert (Thread.holdsLock(this.lockList));
/* 1114 */       for (FileLock localFileLock : this.lockList)
/* 1115 */         if (localFileLock.overlaps(paramLong1, paramLong2))
/* 1116 */           throw new OverlappingFileLockException();
/*      */     }
/*      */ 
/*      */     public void add(FileLock paramFileLock)
/*      */       throws OverlappingFileLockException
/*      */     {
/* 1122 */       synchronized (this.lockList) {
/* 1123 */         checkList(paramFileLock.position(), paramFileLock.size());
/* 1124 */         this.lockList.add(paramFileLock);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void remove(FileLock paramFileLock) {
/* 1129 */       synchronized (this.lockList) {
/* 1130 */         this.lockList.remove(paramFileLock);
/*      */       }
/*      */     }
/*      */ 
/*      */     public List<FileLock> removeAll() {
/* 1135 */       synchronized (this.lockList) {
/* 1136 */         ArrayList localArrayList = new ArrayList(this.lockList);
/* 1137 */         this.lockList.clear();
/* 1138 */         return localArrayList;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void replace(FileLock paramFileLock1, FileLock paramFileLock2) {
/* 1143 */       synchronized (this.lockList) {
/* 1144 */         this.lockList.remove(paramFileLock1);
/* 1145 */         this.lockList.add(paramFileLock2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Unmapper
/*      */     implements Runnable
/*      */   {
/*  756 */     private static final NativeDispatcher nd = new FileDispatcherImpl();
/*      */     static volatile int count;
/*      */     static volatile long totalSize;
/*      */     static volatile long totalCapacity;
/*      */     private volatile long address;
/*      */     private final long size;
/*      */     private final int cap;
/*      */     private final FileDescriptor fd;
/*      */ 
/*      */     private Unmapper(long paramLong1, long paramLong2, int paramInt, FileDescriptor paramFileDescriptor)
/*      */     {
/*  771 */       assert (paramLong1 != 0L);
/*  772 */       this.address = paramLong1;
/*  773 */       this.size = paramLong2;
/*  774 */       this.cap = paramInt;
/*  775 */       this.fd = paramFileDescriptor;
/*      */ 
/*  777 */       synchronized (Unmapper.class) {
/*  778 */         count += 1;
/*  779 */         totalSize += paramLong2;
/*  780 */         totalCapacity += paramInt;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void run() {
/*  785 */       if (this.address == 0L)
/*  786 */         return;
/*  787 */       FileChannelImpl.unmap0(this.address, this.size);
/*  788 */       this.address = 0L;
/*      */ 
/*  791 */       if (this.fd.valid()) {
/*      */         try {
/*  793 */           nd.close(this.fd);
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*      */         }
/*      */       }
/*  799 */       synchronized (Unmapper.class) {
/*  800 */         count -= 1;
/*  801 */         totalSize -= this.size;
/*  802 */         totalCapacity -= this.cap;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.FileChannelImpl
 * JD-Core Version:    0.6.2
 */