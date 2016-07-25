/*      */ package java.nio;
/*      */ 
/*      */ import java.io.FileDescriptor;
/*      */ import sun.misc.Unsafe;
/*      */ import sun.nio.ch.DirectBuffer;
/*      */ 
/*      */ class DirectByteBufferR extends DirectByteBuffer
/*      */   implements DirectBuffer
/*      */ {
/*      */   DirectByteBufferR(int paramInt)
/*      */   {
/*  142 */     super(paramInt);
/*      */   }
/*      */ 
/*      */   protected DirectByteBufferR(int paramInt, long paramLong, FileDescriptor paramFileDescriptor, Runnable paramRunnable)
/*      */   {
/*  182 */     super(paramInt, paramLong, paramFileDescriptor, paramRunnable);
/*      */   }
/*      */ 
/*      */   DirectByteBufferR(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  202 */     super(paramDirectBuffer, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public ByteBuffer slice()
/*      */   {
/*  207 */     int i = position();
/*  208 */     int j = limit();
/*  209 */     assert (i <= j);
/*  210 */     int k = i <= j ? j - i : 0;
/*  211 */     int m = i << 0;
/*  212 */     assert (m >= 0);
/*  213 */     return new DirectByteBufferR(this, -1, 0, k, k, m);
/*      */   }
/*      */ 
/*      */   public ByteBuffer duplicate() {
/*  217 */     return new DirectByteBufferR(this, markValue(), position(), limit(), capacity(), 0);
/*      */   }
/*      */ 
/*      */   public ByteBuffer asReadOnlyBuffer()
/*      */   {
/*  234 */     return duplicate();
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(byte paramByte)
/*      */   {
/*  294 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(int paramInt, byte paramByte)
/*      */   {
/*  303 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(ByteBuffer paramByteBuffer)
/*      */   {
/*  344 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  373 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer compact()
/*      */   {
/*  390 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public boolean isDirect()
/*      */   {
/*  395 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly() {
/*  399 */     return true;
/*      */   }
/*      */ 
/*      */   byte _get(int paramInt)
/*      */   {
/*  466 */     return unsafe.getByte(this.address + paramInt);
/*      */   }
/*      */ 
/*      */   void _put(int paramInt, byte paramByte)
/*      */   {
/*  473 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   private ByteBuffer putChar(long paramLong, char paramChar)
/*      */   {
/*  508 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putChar(char paramChar)
/*      */   {
/*  517 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putChar(int paramInt, char paramChar)
/*      */   {
/*  526 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public CharBuffer asCharBuffer()
/*      */   {
/*  531 */     int i = position();
/*  532 */     int j = limit();
/*  533 */     assert (i <= j);
/*  534 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  536 */     int m = k >> 1;
/*  537 */     if ((!unaligned) && ((this.address + i) % 2L != 0L)) {
/*  538 */       return this.bigEndian ? new ByteBufferAsCharBufferRB(this, -1, 0, m, m, i) : new ByteBufferAsCharBufferRL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  552 */     return this.nativeByteOrder ? new DirectCharBufferRU(this, -1, 0, m, m, i) : new DirectCharBufferRS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private ByteBuffer putShort(long paramLong, short paramShort)
/*      */   {
/*  599 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putShort(short paramShort)
/*      */   {
/*  608 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putShort(int paramInt, short paramShort)
/*      */   {
/*  617 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ShortBuffer asShortBuffer()
/*      */   {
/*  622 */     int i = position();
/*  623 */     int j = limit();
/*  624 */     assert (i <= j);
/*  625 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  627 */     int m = k >> 1;
/*  628 */     if ((!unaligned) && ((this.address + i) % 2L != 0L)) {
/*  629 */       return this.bigEndian ? new ByteBufferAsShortBufferRB(this, -1, 0, m, m, i) : new ByteBufferAsShortBufferRL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  643 */     return this.nativeByteOrder ? new DirectShortBufferRU(this, -1, 0, m, m, i) : new DirectShortBufferRS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private ByteBuffer putInt(long paramLong, int paramInt)
/*      */   {
/*  690 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putInt(int paramInt)
/*      */   {
/*  699 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putInt(int paramInt1, int paramInt2)
/*      */   {
/*  708 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public IntBuffer asIntBuffer()
/*      */   {
/*  713 */     int i = position();
/*  714 */     int j = limit();
/*  715 */     assert (i <= j);
/*  716 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  718 */     int m = k >> 2;
/*  719 */     if ((!unaligned) && ((this.address + i) % 4L != 0L)) {
/*  720 */       return this.bigEndian ? new ByteBufferAsIntBufferRB(this, -1, 0, m, m, i) : new ByteBufferAsIntBufferRL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  734 */     return this.nativeByteOrder ? new DirectIntBufferRU(this, -1, 0, m, m, i) : new DirectIntBufferRS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private ByteBuffer putLong(long paramLong1, long paramLong2)
/*      */   {
/*  781 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putLong(long paramLong)
/*      */   {
/*  790 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putLong(int paramInt, long paramLong)
/*      */   {
/*  799 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public LongBuffer asLongBuffer()
/*      */   {
/*  804 */     int i = position();
/*  805 */     int j = limit();
/*  806 */     assert (i <= j);
/*  807 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  809 */     int m = k >> 3;
/*  810 */     if ((!unaligned) && ((this.address + i) % 8L != 0L)) {
/*  811 */       return this.bigEndian ? new ByteBufferAsLongBufferRB(this, -1, 0, m, m, i) : new ByteBufferAsLongBufferRL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  825 */     return this.nativeByteOrder ? new DirectLongBufferRU(this, -1, 0, m, m, i) : new DirectLongBufferRS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private ByteBuffer putFloat(long paramLong, float paramFloat)
/*      */   {
/*  872 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putFloat(float paramFloat)
/*      */   {
/*  881 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putFloat(int paramInt, float paramFloat)
/*      */   {
/*  890 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public FloatBuffer asFloatBuffer()
/*      */   {
/*  895 */     int i = position();
/*  896 */     int j = limit();
/*  897 */     assert (i <= j);
/*  898 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  900 */     int m = k >> 2;
/*  901 */     if ((!unaligned) && ((this.address + i) % 4L != 0L)) {
/*  902 */       return this.bigEndian ? new ByteBufferAsFloatBufferRB(this, -1, 0, m, m, i) : new ByteBufferAsFloatBufferRL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  916 */     return this.nativeByteOrder ? new DirectFloatBufferRU(this, -1, 0, m, m, i) : new DirectFloatBufferRS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private ByteBuffer putDouble(long paramLong, double paramDouble)
/*      */   {
/*  963 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putDouble(double paramDouble)
/*      */   {
/*  972 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public ByteBuffer putDouble(int paramInt, double paramDouble)
/*      */   {
/*  981 */     throw new ReadOnlyBufferException();
/*      */   }
/*      */ 
/*      */   public DoubleBuffer asDoubleBuffer()
/*      */   {
/*  986 */     int i = position();
/*  987 */     int j = limit();
/*  988 */     assert (i <= j);
/*  989 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  991 */     int m = k >> 3;
/*  992 */     if ((!unaligned) && ((this.address + i) % 8L != 0L)) {
/*  993 */       return this.bigEndian ? new ByteBufferAsDoubleBufferRB(this, -1, 0, m, m, i) : new ByteBufferAsDoubleBufferRL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/* 1007 */     return this.nativeByteOrder ? new DirectDoubleBufferRU(this, -1, 0, m, m, i) : new DirectDoubleBufferRS(this, -1, 0, m, m, i);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectByteBufferR
 * JD-Core Version:    0.6.2
 */