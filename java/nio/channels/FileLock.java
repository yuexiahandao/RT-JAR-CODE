/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class FileLock
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final Channel channel;
/*     */   private final long position;
/*     */   private final long size;
/*     */   private final boolean shared;
/*     */ 
/*     */   protected FileLock(FileChannel paramFileChannel, long paramLong1, long paramLong2, boolean paramBoolean)
/*     */   {
/* 150 */     if (paramLong1 < 0L)
/* 151 */       throw new IllegalArgumentException("Negative position");
/* 152 */     if (paramLong2 < 0L)
/* 153 */       throw new IllegalArgumentException("Negative size");
/* 154 */     if (paramLong1 + paramLong2 < 0L)
/* 155 */       throw new IllegalArgumentException("Negative position + size");
/* 156 */     this.channel = paramFileChannel;
/* 157 */     this.position = paramLong1;
/* 158 */     this.size = paramLong2;
/* 159 */     this.shared = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected FileLock(AsynchronousFileChannel paramAsynchronousFileChannel, long paramLong1, long paramLong2, boolean paramBoolean)
/*     */   {
/* 188 */     if (paramLong1 < 0L)
/* 189 */       throw new IllegalArgumentException("Negative position");
/* 190 */     if (paramLong2 < 0L)
/* 191 */       throw new IllegalArgumentException("Negative size");
/* 192 */     if (paramLong1 + paramLong2 < 0L)
/* 193 */       throw new IllegalArgumentException("Negative position + size");
/* 194 */     this.channel = paramAsynchronousFileChannel;
/* 195 */     this.position = paramLong1;
/* 196 */     this.size = paramLong2;
/* 197 */     this.shared = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final FileChannel channel()
/*     */   {
/* 210 */     return (this.channel instanceof FileChannel) ? (FileChannel)this.channel : null;
/*     */   }
/*     */ 
/*     */   public Channel acquiredBy()
/*     */   {
/* 221 */     return this.channel;
/*     */   }
/*     */ 
/*     */   public final long position()
/*     */   {
/* 235 */     return this.position;
/*     */   }
/*     */ 
/*     */   public final long size()
/*     */   {
/* 248 */     return this.size;
/*     */   }
/*     */ 
/*     */   public final boolean isShared()
/*     */   {
/* 258 */     return this.shared;
/*     */   }
/*     */ 
/*     */   public final boolean overlaps(long paramLong1, long paramLong2)
/*     */   {
/* 268 */     if (paramLong1 + paramLong2 <= this.position)
/* 269 */       return false;
/* 270 */     if (this.position + this.size <= paramLong1)
/* 271 */       return false;
/* 272 */     return true;
/*     */   }
/*     */ 
/*     */   public abstract boolean isValid();
/*     */ 
/*     */   public abstract void release()
/*     */     throws IOException;
/*     */ 
/*     */   public final void close()
/*     */     throws IOException
/*     */   {
/* 309 */     release();
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 318 */     return getClass().getName() + "[" + this.position + ":" + this.size + " " + (this.shared ? "shared" : "exclusive") + " " + (isValid() ? "valid" : "invalid") + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.FileLock
 * JD-Core Version:    0.6.2
 */