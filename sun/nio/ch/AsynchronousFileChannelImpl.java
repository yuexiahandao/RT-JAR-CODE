/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ abstract class AsynchronousFileChannelImpl extends AsynchronousFileChannel
/*     */ {
/*  44 */   protected final ReadWriteLock closeLock = new ReentrantReadWriteLock();
/*     */   protected volatile boolean closed;
/*     */   protected final FileDescriptor fdObj;
/*     */   protected final boolean reading;
/*     */   protected final boolean writing;
/*     */   protected final ExecutorService executor;
/*     */   private volatile FileLockTable fileLockTable;
/*     */ 
/*     */   protected AsynchronousFileChannelImpl(FileDescriptor paramFileDescriptor, boolean paramBoolean1, boolean paramBoolean2, ExecutorService paramExecutorService)
/*     */   {
/*  62 */     this.fdObj = paramFileDescriptor;
/*  63 */     this.reading = paramBoolean1;
/*  64 */     this.writing = paramBoolean2;
/*  65 */     this.executor = paramExecutorService;
/*     */   }
/*     */ 
/*     */   final ExecutorService executor() {
/*  69 */     return this.executor;
/*     */   }
/*     */ 
/*     */   public final boolean isOpen()
/*     */   {
/*  74 */     return !this.closed;
/*     */   }
/*     */ 
/*     */   protected final void begin()
/*     */     throws IOException
/*     */   {
/*  83 */     this.closeLock.readLock().lock();
/*  84 */     if (this.closed)
/*  85 */       throw new ClosedChannelException();
/*     */   }
/*     */ 
/*     */   protected final void end()
/*     */   {
/*  92 */     this.closeLock.readLock().unlock();
/*     */   }
/*     */ 
/*     */   protected final void end(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  99 */     end();
/* 100 */     if ((!paramBoolean) && (!isOpen()))
/* 101 */       throw new AsynchronousCloseException();
/*     */   }
/*     */ 
/*     */   abstract <A> Future<FileLock> implLock(long paramLong1, long paramLong2, boolean paramBoolean, A paramA, CompletionHandler<FileLock, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public final Future<FileLock> lock(long paramLong1, long paramLong2, boolean paramBoolean)
/*     */   {
/* 118 */     return implLock(paramLong1, paramLong2, paramBoolean, null, null);
/*     */   }
/*     */ 
/*     */   public final <A> void lock(long paramLong1, long paramLong2, boolean paramBoolean, A paramA, CompletionHandler<FileLock, ? super A> paramCompletionHandler)
/*     */   {
/* 128 */     if (paramCompletionHandler == null)
/* 129 */       throw new NullPointerException("'handler' is null");
/* 130 */     implLock(paramLong1, paramLong2, paramBoolean, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   final void ensureFileLockTableInitialized()
/*     */     throws IOException
/*     */   {
/* 136 */     if (this.fileLockTable == null)
/* 137 */       synchronized (this) {
/* 138 */         if (this.fileLockTable == null)
/* 139 */           this.fileLockTable = FileLockTable.newSharedFileLockTable(this, this.fdObj);
/*     */       }
/*     */   }
/*     */ 
/*     */   final void invalidateAllLocks()
/*     */     throws IOException
/*     */   {
/* 146 */     if (this.fileLockTable != null)
/* 147 */       for (FileLock localFileLock : this.fileLockTable.removeAll())
/* 148 */         synchronized (localFileLock) {
/* 149 */           if (localFileLock.isValid()) {
/* 150 */             FileLockImpl localFileLockImpl = (FileLockImpl)localFileLock;
/* 151 */             implRelease(localFileLockImpl);
/* 152 */             localFileLockImpl.invalidate();
/*     */           }
/*     */         }
/*     */   }
/*     */ 
/*     */   protected final FileLockImpl addToFileLockTable(long paramLong1, long paramLong2, boolean paramBoolean)
/*     */   {
/*     */     FileLockImpl localFileLockImpl1;
/*     */     try
/*     */     {
/* 166 */       this.closeLock.readLock().lock();
/* 167 */       if (this.closed)
/* 168 */         return null;
/*     */       try
/*     */       {
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 174 */         throw new AssertionError(localIOException);
/*     */       }
/* 176 */       localFileLockImpl1 = new FileLockImpl(this, paramLong1, paramLong2, paramBoolean);
/*     */ 
/* 178 */       this.fileLockTable.add(localFileLockImpl1);
/*     */     } finally {
/* 180 */       end();
/*     */     }
/* 182 */     return localFileLockImpl1;
/*     */   }
/*     */ 
/*     */   protected final void removeFromFileLockTable(FileLockImpl paramFileLockImpl) {
/* 186 */     this.fileLockTable.remove(paramFileLockImpl);
/*     */   }
/*     */ 
/*     */   protected abstract void implRelease(FileLockImpl paramFileLockImpl)
/*     */     throws IOException;
/*     */ 
/*     */   final void release(FileLockImpl paramFileLockImpl)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 200 */       begin();
/* 201 */       implRelease(paramFileLockImpl);
/* 202 */       removeFromFileLockTable(paramFileLockImpl);
/*     */     } finally {
/* 204 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract <A> Future<Integer> implRead(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public final Future<Integer> read(ByteBuffer paramByteBuffer, long paramLong)
/*     */   {
/* 218 */     return implRead(paramByteBuffer, paramLong, null, null);
/*     */   }
/*     */ 
/*     */   public final <A> void read(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler)
/*     */   {
/* 227 */     if (paramCompletionHandler == null)
/* 228 */       throw new NullPointerException("'handler' is null");
/* 229 */     implRead(paramByteBuffer, paramLong, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   abstract <A> Future<Integer> implWrite(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public final Future<Integer> write(ByteBuffer paramByteBuffer, long paramLong)
/*     */   {
/* 240 */     return implWrite(paramByteBuffer, paramLong, null, null);
/*     */   }
/*     */ 
/*     */   public final <A> void write(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler)
/*     */   {
/* 249 */     if (paramCompletionHandler == null)
/* 250 */       throw new NullPointerException("'handler' is null");
/* 251 */     implWrite(paramByteBuffer, paramLong, paramA, paramCompletionHandler);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.AsynchronousFileChannelImpl
 * JD-Core Version:    0.6.2
 */