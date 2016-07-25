/*     */ package java.nio;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public abstract class MappedByteBuffer extends ByteBuffer
/*     */ {
/*     */   private final FileDescriptor fd;
/*     */   private static byte unused;
/*     */ 
/*     */   MappedByteBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, FileDescriptor paramFileDescriptor)
/*     */   {
/*  84 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  85 */     this.fd = paramFileDescriptor;
/*     */   }
/*     */ 
/*     */   MappedByteBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  89 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  90 */     this.fd = null;
/*     */   }
/*     */ 
/*     */   private void checkMapped() {
/*  94 */     if (this.fd == null)
/*     */     {
/*  96 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private long mappingOffset()
/*     */   {
/* 102 */     int i = Bits.pageSize();
/* 103 */     long l = this.address % i;
/* 104 */     return l >= 0L ? l : i + l;
/*     */   }
/*     */ 
/*     */   private long mappingAddress(long paramLong) {
/* 108 */     return this.address - paramLong;
/*     */   }
/*     */ 
/*     */   private long mappingLength(long paramLong) {
/* 112 */     return capacity() + paramLong;
/*     */   }
/*     */ 
/*     */   public final boolean isLoaded()
/*     */   {
/* 134 */     checkMapped();
/* 135 */     if ((this.address == 0L) || (capacity() == 0))
/* 136 */       return true;
/* 137 */     long l1 = mappingOffset();
/* 138 */     long l2 = mappingLength(l1);
/* 139 */     return isLoaded0(mappingAddress(l1), l2, Bits.pageCount(l2));
/*     */   }
/*     */ 
/*     */   public final MappedByteBuffer load()
/*     */   {
/* 156 */     checkMapped();
/* 157 */     if ((this.address == 0L) || (capacity() == 0))
/* 158 */       return this;
/* 159 */     long l1 = mappingOffset();
/* 160 */     long l2 = mappingLength(l1);
/* 161 */     load0(mappingAddress(l1), l2);
/*     */ 
/* 166 */     Unsafe localUnsafe = Unsafe.getUnsafe();
/* 167 */     int i = Bits.pageSize();
/* 168 */     int j = Bits.pageCount(l2);
/* 169 */     long l3 = mappingAddress(l1);
/* 170 */     byte b = 0;
/* 171 */     for (int k = 0; k < j; k++) {
/* 172 */       b = (byte)(b ^ localUnsafe.getByte(l3));
/* 173 */       l3 += i;
/*     */     }
/* 175 */     if (unused != 0) {
/* 176 */       unused = b;
/*     */     }
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   public final MappedByteBuffer force()
/*     */   {
/* 200 */     checkMapped();
/* 201 */     if ((this.address != 0L) && (capacity() != 0)) {
/* 202 */       long l = mappingOffset();
/* 203 */       force0(this.fd, mappingAddress(l), mappingLength(l));
/*     */     }
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   private native boolean isLoaded0(long paramLong1, long paramLong2, int paramInt);
/*     */ 
/*     */   private native void load0(long paramLong1, long paramLong2);
/*     */ 
/*     */   private native void force0(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.MappedByteBuffer
 * JD-Core Version:    0.6.2
 */