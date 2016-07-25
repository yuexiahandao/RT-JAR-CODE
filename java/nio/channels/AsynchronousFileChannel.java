/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ 
/*     */ public abstract class AsynchronousFileChannel
/*     */   implements AsynchronousChannel
/*     */ {
/* 251 */   private static final FileAttribute<?>[] NO_ATTRIBUTES = new FileAttribute[0];
/*     */ 
/*     */   public static AsynchronousFileChannel open(Path paramPath, Set<? extends OpenOption> paramSet, ExecutorService paramExecutorService, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 247 */     FileSystemProvider localFileSystemProvider = paramPath.getFileSystem().provider();
/* 248 */     return localFileSystemProvider.newAsynchronousFileChannel(paramPath, paramSet, paramExecutorService, paramArrayOfFileAttribute);
/*     */   }
/*     */ 
/*     */   public static AsynchronousFileChannel open(Path paramPath, OpenOption[] paramArrayOfOpenOption)
/*     */     throws IOException
/*     */   {
/* 298 */     HashSet localHashSet = new HashSet(paramArrayOfOpenOption.length);
/* 299 */     Collections.addAll(localHashSet, paramArrayOfOpenOption);
/* 300 */     return open(paramPath, localHashSet, null, NO_ATTRIBUTES);
/*     */   }
/*     */ 
/*     */   public abstract long size()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract AsynchronousFileChannel truncate(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void force(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <A> void lock(long paramLong1, long paramLong2, boolean paramBoolean, A paramA, CompletionHandler<FileLock, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public final <A> void lock(A paramA, CompletionHandler<FileLock, ? super A> paramCompletionHandler)
/*     */   {
/* 489 */     lock(0L, 9223372036854775807L, false, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   public abstract Future<FileLock> lock(long paramLong1, long paramLong2, boolean paramBoolean);
/*     */ 
/*     */   public final Future<FileLock> lock()
/*     */   {
/* 552 */     return lock(0L, 9223372036854775807L, false);
/*     */   }
/*     */ 
/*     */   public abstract FileLock tryLock(long paramLong1, long paramLong2, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   public final FileLock tryLock()
/*     */     throws IOException
/*     */   {
/* 635 */     return tryLock(0L, 9223372036854775807L, false);
/*     */   }
/*     */ 
/*     */   public abstract <A> void read(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public abstract Future<Integer> read(ByteBuffer paramByteBuffer, long paramLong);
/*     */ 
/*     */   public abstract <A> void write(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public abstract Future<Integer> write(ByteBuffer paramByteBuffer, long paramLong);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.AsynchronousFileChannel
 * JD-Core Version:    0.6.2
 */