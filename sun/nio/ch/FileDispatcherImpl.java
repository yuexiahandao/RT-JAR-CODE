/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import sun.misc.JavaIOFileDescriptorAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ class FileDispatcherImpl extends FileDispatcher
/*     */ {
/*     */   private final boolean append;
/*     */ 
/*     */   FileDispatcherImpl(boolean paramBoolean)
/*     */   {
/*  45 */     this.append = paramBoolean;
/*     */   }
/*     */ 
/*     */   FileDispatcherImpl() {
/*  49 */     this(false);
/*     */   }
/*     */ 
/*     */   boolean needsPositionLock()
/*     */   {
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   int read(FileDescriptor paramFileDescriptor, long paramLong, int paramInt)
/*     */     throws IOException
/*     */   {
/*  60 */     return read0(paramFileDescriptor, paramLong, paramInt);
/*     */   }
/*     */ 
/*     */   int pread(FileDescriptor paramFileDescriptor, long paramLong1, int paramInt, long paramLong2)
/*     */     throws IOException
/*     */   {
/*  66 */     return pread0(paramFileDescriptor, paramLong1, paramInt, paramLong2);
/*     */   }
/*     */ 
/*     */   long readv(FileDescriptor paramFileDescriptor, long paramLong, int paramInt) throws IOException {
/*  70 */     return readv0(paramFileDescriptor, paramLong, paramInt);
/*     */   }
/*     */ 
/*     */   int write(FileDescriptor paramFileDescriptor, long paramLong, int paramInt) throws IOException {
/*  74 */     return write0(paramFileDescriptor, paramLong, paramInt, this.append);
/*     */   }
/*     */ 
/*     */   int pwrite(FileDescriptor paramFileDescriptor, long paramLong1, int paramInt, long paramLong2)
/*     */     throws IOException
/*     */   {
/*  80 */     return pwrite0(paramFileDescriptor, paramLong1, paramInt, paramLong2);
/*     */   }
/*     */ 
/*     */   long writev(FileDescriptor paramFileDescriptor, long paramLong, int paramInt) throws IOException {
/*  84 */     return writev0(paramFileDescriptor, paramLong, paramInt, this.append);
/*     */   }
/*     */ 
/*     */   int force(FileDescriptor paramFileDescriptor, boolean paramBoolean) throws IOException {
/*  88 */     return force0(paramFileDescriptor, paramBoolean);
/*     */   }
/*     */ 
/*     */   int truncate(FileDescriptor paramFileDescriptor, long paramLong) throws IOException {
/*  92 */     return truncate0(paramFileDescriptor, paramLong);
/*     */   }
/*     */ 
/*     */   long size(FileDescriptor paramFileDescriptor) throws IOException {
/*  96 */     return size0(paramFileDescriptor);
/*     */   }
/*     */ 
/*     */   int lock(FileDescriptor paramFileDescriptor, boolean paramBoolean1, long paramLong1, long paramLong2, boolean paramBoolean2)
/*     */     throws IOException
/*     */   {
/* 102 */     return lock0(paramFileDescriptor, paramBoolean1, paramLong1, paramLong2, paramBoolean2);
/*     */   }
/*     */ 
/*     */   void release(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2) throws IOException {
/* 106 */     release0(paramFileDescriptor, paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   void close(FileDescriptor paramFileDescriptor) throws IOException {
/* 110 */     close0(paramFileDescriptor);
/*     */   }
/*     */ 
/*     */   FileDescriptor duplicateForMapping(FileDescriptor paramFileDescriptor) throws IOException
/*     */   {
/* 115 */     JavaIOFileDescriptorAccess localJavaIOFileDescriptorAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
/*     */ 
/* 117 */     FileDescriptor localFileDescriptor = new FileDescriptor();
/* 118 */     long l = duplicateHandle(localJavaIOFileDescriptorAccess.getHandle(paramFileDescriptor));
/* 119 */     localJavaIOFileDescriptorAccess.setHandle(localFileDescriptor, l);
/* 120 */     return localFileDescriptor;
/*     */   }
/*     */ 
/*     */   static native int read0(FileDescriptor paramFileDescriptor, long paramLong, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native int pread0(FileDescriptor paramFileDescriptor, long paramLong1, int paramInt, long paramLong2)
/*     */     throws IOException;
/*     */ 
/*     */   static native long readv0(FileDescriptor paramFileDescriptor, long paramLong, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native int write0(FileDescriptor paramFileDescriptor, long paramLong, int paramInt, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   static native int pwrite0(FileDescriptor paramFileDescriptor, long paramLong1, int paramInt, long paramLong2)
/*     */     throws IOException;
/*     */ 
/*     */   static native long writev0(FileDescriptor paramFileDescriptor, long paramLong, int paramInt, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   static native int force0(FileDescriptor paramFileDescriptor, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   static native int truncate0(FileDescriptor paramFileDescriptor, long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   static native long size0(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   static native int lock0(FileDescriptor paramFileDescriptor, boolean paramBoolean1, long paramLong1, long paramLong2, boolean paramBoolean2)
/*     */     throws IOException;
/*     */ 
/*     */   static native void release0(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
/*     */     throws IOException;
/*     */ 
/*     */   static native void close0(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   static native void closeByHandle(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   static native long duplicateHandle(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   static
/*     */   {
/*  35 */     Util.load();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.FileDispatcherImpl
 * JD-Core Version:    0.6.2
 */