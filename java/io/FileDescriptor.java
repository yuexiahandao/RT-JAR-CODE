/*     */ package java.io;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import sun.misc.JavaIOFileDescriptorAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public final class FileDescriptor
/*     */ {
/*     */   private int fd;
/*     */   private long handle;
/*     */   private AtomicInteger useCount;
/* 100 */   public static final FileDescriptor in = standardStream(0);
/*     */ 
/* 108 */   public static final FileDescriptor out = standardStream(1);
/*     */ 
/* 117 */   public static final FileDescriptor err = standardStream(2);
/*     */ 
/*     */   public FileDescriptor()
/*     */   {
/*  61 */     this.fd = -1;
/*  62 */     this.handle = -1L;
/*  63 */     this.useCount = new AtomicInteger();
/*     */   }
/*     */ 
/*     */   public boolean valid()
/*     */   {
/* 127 */     return (this.handle != -1L) || (this.fd != -1);
/*     */   }
/*     */ 
/*     */   public native void sync()
/*     */     throws SyncFailedException;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private static native long set(int paramInt);
/*     */ 
/*     */   private static FileDescriptor standardStream(int paramInt)
/*     */   {
/* 166 */     FileDescriptor localFileDescriptor = new FileDescriptor();
/* 167 */     localFileDescriptor.handle = set(paramInt);
/* 168 */     return localFileDescriptor;
/*     */   }
/*     */ 
/*     */   int incrementAndGetUseCount()
/*     */   {
/* 174 */     return this.useCount.incrementAndGet();
/*     */   }
/*     */ 
/*     */   int decrementAndGetUseCount() {
/* 178 */     return this.useCount.decrementAndGet();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  67 */     initIDs();
/*     */ 
/*  72 */     SharedSecrets.setJavaIOFileDescriptorAccess(new JavaIOFileDescriptorAccess()
/*     */     {
/*     */       public void set(FileDescriptor paramAnonymousFileDescriptor, int paramAnonymousInt) {
/*  75 */         paramAnonymousFileDescriptor.fd = paramAnonymousInt;
/*     */       }
/*     */ 
/*     */       public int get(FileDescriptor paramAnonymousFileDescriptor) {
/*  79 */         return paramAnonymousFileDescriptor.fd;
/*     */       }
/*     */ 
/*     */       public void setHandle(FileDescriptor paramAnonymousFileDescriptor, long paramAnonymousLong) {
/*  83 */         paramAnonymousFileDescriptor.handle = paramAnonymousLong;
/*     */       }
/*     */ 
/*     */       public long getHandle(FileDescriptor paramAnonymousFileDescriptor) {
/*  87 */         return paramAnonymousFileDescriptor.handle;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FileDescriptor
 * JD-Core Version:    0.6.2
 */