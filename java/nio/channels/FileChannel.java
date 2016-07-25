/*      */ package java.nio.channels;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.MappedByteBuffer;
/*      */ import java.nio.channels.spi.AbstractInterruptibleChannel;
/*      */ import java.nio.file.FileSystem;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.nio.file.spi.FileSystemProvider;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ 
/*      */ public abstract class FileChannel extends AbstractInterruptibleChannel
/*      */   implements SeekableByteChannel, GatheringByteChannel, ScatteringByteChannel
/*      */ {
/*  290 */   private static final FileAttribute<?>[] NO_ATTRIBUTES = new FileAttribute[0];
/*      */ 
/*      */   public static FileChannel open(Path paramPath, Set<? extends OpenOption> paramSet, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  286 */     FileSystemProvider localFileSystemProvider = paramPath.getFileSystem().provider();
/*  287 */     return localFileSystemProvider.newFileChannel(paramPath, paramSet, paramArrayOfFileAttribute);
/*      */   }
/*      */ 
/*      */   public static FileChannel open(Path paramPath, OpenOption[] paramArrayOfOpenOption)
/*      */     throws IOException
/*      */   {
/*  332 */     HashSet localHashSet = new HashSet(paramArrayOfOpenOption.length);
/*  333 */     Collections.addAll(localHashSet, paramArrayOfOpenOption);
/*  334 */     return open(paramPath, localHashSet, NO_ATTRIBUTES);
/*      */   }
/*      */ 
/*      */   public abstract int read(ByteBuffer paramByteBuffer)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract long read(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */ 
/*      */   public final long read(ByteBuffer[] paramArrayOfByteBuffer)
/*      */     throws IOException
/*      */   {
/*  370 */     return read(paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length);
/*      */   }
/*      */ 
/*      */   public abstract int write(ByteBuffer paramByteBuffer)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract long write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */ 
/*      */   public final long write(ByteBuffer[] paramArrayOfByteBuffer)
/*      */     throws IOException
/*      */   {
/*  413 */     return write(paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length);
/*      */   }
/*      */ 
/*      */   public abstract long position()
/*      */     throws IOException;
/*      */ 
/*      */   public abstract FileChannel position(long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract long size()
/*      */     throws IOException;
/*      */ 
/*      */   public abstract FileChannel truncate(long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract void force(boolean paramBoolean)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract long transferTo(long paramLong1, long paramLong2, WritableByteChannel paramWritableByteChannel)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract long transferFrom(ReadableByteChannel paramReadableByteChannel, long paramLong1, long paramLong2)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract int read(ByteBuffer paramByteBuffer, long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract int write(ByteBuffer paramByteBuffer, long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract MappedByteBuffer map(MapMode paramMapMode, long paramLong1, long paramLong2)
/*      */     throws IOException;
/*      */ 
/*      */   public abstract FileLock lock(long paramLong1, long paramLong2, boolean paramBoolean)
/*      */     throws IOException;
/*      */ 
/*      */   public final FileLock lock()
/*      */     throws IOException
/*      */   {
/* 1052 */     return lock(0L, 9223372036854775807L, false);
/*      */   }
/*      */ 
/*      */   public abstract FileLock tryLock(long paramLong1, long paramLong2, boolean paramBoolean)
/*      */     throws IOException;
/*      */ 
/*      */   public final FileLock tryLock()
/*      */     throws IOException
/*      */   {
/* 1154 */     return tryLock(0L, 9223372036854775807L, false);
/*      */   }
/*      */ 
/*      */   public static class MapMode
/*      */   {
/*  794 */     public static final MapMode READ_ONLY = new MapMode("READ_ONLY");
/*      */ 
/*  800 */     public static final MapMode READ_WRITE = new MapMode("READ_WRITE");
/*      */ 
/*  806 */     public static final MapMode PRIVATE = new MapMode("PRIVATE");
/*      */     private final String name;
/*      */ 
/*      */     private MapMode(String paramString)
/*      */     {
/*  812 */       this.name = paramString;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  821 */       return this.name;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.FileChannel
 * JD-Core Version:    0.6.2
 */