/*      */ package java.nio;
/*      */ 
/*      */ import java.io.FileDescriptor;
/*      */ import sun.misc.Cleaner;
/*      */ import sun.misc.Unsafe;
/*      */ import sun.misc.VM;
/*      */ import sun.nio.ch.DirectBuffer;
/*      */ 
/*      */ class DirectByteBuffer extends MappedByteBuffer
/*      */   implements DirectBuffer
/*      */ {
/*   49 */   protected static final Unsafe unsafe = Bits.unsafe();
/*      */ 
/*   52 */   private static final long arrayBaseOffset = unsafe.arrayBaseOffset([B.class);
/*      */ 
/*   55 */   protected static final boolean unaligned = Bits.unaligned();
/*      */   private final Object att;
/*      */   private final Cleaner cleaner;
/*      */ 
/*      */   public Object attachment()
/*      */   {
/*   67 */     return this.att;
/*      */   }
/*      */ 
/*      */   public Cleaner cleaner()
/*      */   {
/*  103 */     return this.cleaner;
/*      */   }
/*      */ 
/*      */   DirectByteBuffer(int paramInt)
/*      */   {
/*  119 */     super(-1, 0, paramInt, paramInt);
/*  120 */     boolean bool = VM.isDirectMemoryPageAligned();
/*  121 */     int i = Bits.pageSize();
/*  122 */     long l1 = Math.max(1L, paramInt + (bool ? i : 0));
/*  123 */     Bits.reserveMemory(l1, paramInt);
/*      */ 
/*  125 */     long l2 = 0L;
/*      */     try {
/*  127 */       l2 = unsafe.allocateMemory(l1);
/*      */     } catch (OutOfMemoryError localOutOfMemoryError) {
/*  129 */       Bits.unreserveMemory(l1, paramInt);
/*  130 */       throw localOutOfMemoryError;
/*      */     }
/*  132 */     unsafe.setMemory(l2, l1, (byte)0);
/*  133 */     if ((bool) && (l2 % i != 0L))
/*      */     {
/*  135 */       this.address = (l2 + i - (l2 & i - 1));
/*      */     }
/*  137 */     else this.address = l2;
/*      */ 
/*  139 */     this.cleaner = Cleaner.create(this, new Deallocator(l2, l1, paramInt, null));
/*  140 */     this.att = null;
/*      */   }
/*      */ 
/*      */   DirectByteBuffer(long paramLong, int paramInt, Object paramObject)
/*      */   {
/*  152 */     super(-1, 0, paramInt, paramInt);
/*  153 */     this.address = paramLong;
/*  154 */     this.cleaner = null;
/*  155 */     this.att = paramObject;
/*      */   }
/*      */ 
/*      */   private DirectByteBuffer(long paramLong, int paramInt)
/*      */   {
/*  162 */     super(-1, 0, paramInt, paramInt);
/*  163 */     this.address = paramLong;
/*  164 */     this.cleaner = null;
/*  165 */     this.att = null;
/*      */   }
/*      */ 
/*      */   protected DirectByteBuffer(int paramInt, long paramLong, FileDescriptor paramFileDescriptor, Runnable paramRunnable)
/*      */   {
/*  177 */     super(-1, 0, paramInt, paramInt, paramFileDescriptor);
/*  178 */     this.address = paramLong;
/*  179 */     this.cleaner = Cleaner.create(this, paramRunnable);
/*  180 */     this.att = null;
/*      */   }
/*      */ 
/*      */   DirectByteBuffer(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  195 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  196 */     this.address = (paramDirectBuffer.address() + paramInt5);
/*      */ 
/*  198 */     this.cleaner = null;
/*      */ 
/*  200 */     this.att = paramDirectBuffer;
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
/*  213 */     return new DirectByteBuffer(this, -1, 0, k, k, m);
/*      */   }
/*      */ 
/*      */   public ByteBuffer duplicate() {
/*  217 */     return new DirectByteBuffer(this, markValue(), position(), limit(), capacity(), 0);
/*      */   }
/*      */ 
/*      */   public ByteBuffer asReadOnlyBuffer()
/*      */   {
/*  227 */     return new DirectByteBufferR(this, markValue(), position(), limit(), capacity(), 0);
/*      */   }
/*      */ 
/*      */   public long address()
/*      */   {
/*  241 */     return this.address;
/*      */   }
/*      */ 
/*      */   private long ix(int paramInt) {
/*  245 */     return this.address + (paramInt << 0);
/*      */   }
/*      */ 
/*      */   public byte get() {
/*  249 */     return unsafe.getByte(ix(nextGetIndex()));
/*      */   }
/*      */ 
/*      */   public byte get(int paramInt) {
/*  253 */     return unsafe.getByte(ix(checkIndex(paramInt)));
/*      */   }
/*      */ 
/*      */   public ByteBuffer get(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  258 */     if (paramInt2 << 0 > 6) {
/*  259 */       checkBounds(paramInt1, paramInt2, paramArrayOfByte.length);
/*  260 */       int i = position();
/*  261 */       int j = limit();
/*  262 */       assert (i <= j);
/*  263 */       int k = i <= j ? j - i : 0;
/*  264 */       if (paramInt2 > k) {
/*  265 */         throw new BufferUnderflowException();
/*      */       }
/*      */ 
/*  274 */       Bits.copyToArray(ix(i), paramArrayOfByte, arrayBaseOffset, paramInt1 << 0, paramInt2 << 0);
/*      */ 
/*  277 */       position(i + paramInt2);
/*      */     } else {
/*  279 */       super.get(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*  281 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(byte paramByte)
/*      */   {
/*  291 */     unsafe.putByte(ix(nextPutIndex()), paramByte);
/*  292 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(int paramInt, byte paramByte)
/*      */   {
/*  300 */     unsafe.putByte(ix(checkIndex(paramInt)), paramByte);
/*  301 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(ByteBuffer paramByteBuffer)
/*      */   {
/*      */     int j;
/*      */     int k;
/*  309 */     if ((paramByteBuffer instanceof DirectByteBuffer)) {
/*  310 */       if (paramByteBuffer == this)
/*  311 */         throw new IllegalArgumentException();
/*  312 */       DirectByteBuffer localDirectByteBuffer = (DirectByteBuffer)paramByteBuffer;
/*      */ 
/*  314 */       j = localDirectByteBuffer.position();
/*  315 */       k = localDirectByteBuffer.limit();
/*  316 */       assert (j <= k);
/*  317 */       int m = j <= k ? k - j : 0;
/*      */ 
/*  319 */       int n = position();
/*  320 */       int i1 = limit();
/*  321 */       assert (n <= i1);
/*  322 */       int i2 = n <= i1 ? i1 - n : 0;
/*      */ 
/*  324 */       if (m > i2)
/*  325 */         throw new BufferOverflowException();
/*  326 */       unsafe.copyMemory(localDirectByteBuffer.ix(j), ix(n), m << 0);
/*  327 */       localDirectByteBuffer.position(j + m);
/*  328 */       position(n + m);
/*  329 */     } else if (paramByteBuffer.hb != null)
/*      */     {
/*  331 */       int i = paramByteBuffer.position();
/*  332 */       j = paramByteBuffer.limit();
/*  333 */       assert (i <= j);
/*  334 */       k = i <= j ? j - i : 0;
/*      */ 
/*  336 */       put(paramByteBuffer.hb, paramByteBuffer.offset + i, k);
/*  337 */       paramByteBuffer.position(i + k);
/*      */     }
/*      */     else {
/*  340 */       super.put(paramByteBuffer);
/*      */     }
/*  342 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  350 */     if (paramInt2 << 0 > 6) {
/*  351 */       checkBounds(paramInt1, paramInt2, paramArrayOfByte.length);
/*  352 */       int i = position();
/*  353 */       int j = limit();
/*  354 */       assert (i <= j);
/*  355 */       int k = i <= j ? j - i : 0;
/*  356 */       if (paramInt2 > k) {
/*  357 */         throw new BufferOverflowException();
/*      */       }
/*      */ 
/*  365 */       Bits.copyFromArray(paramArrayOfByte, arrayBaseOffset, paramInt1 << 0, ix(i), paramInt2 << 0);
/*      */ 
/*  367 */       position(i + paramInt2);
/*      */     } else {
/*  369 */       super.put(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*  371 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer compact()
/*      */   {
/*  379 */     int i = position();
/*  380 */     int j = limit();
/*  381 */     assert (i <= j);
/*  382 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  384 */     unsafe.copyMemory(ix(i), ix(0), k << 0);
/*  385 */     position(k);
/*  386 */     limit(capacity());
/*  387 */     discardMark();
/*  388 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean isDirect()
/*      */   {
/*  395 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly() {
/*  399 */     return false;
/*      */   }
/*      */ 
/*      */   byte _get(int paramInt)
/*      */   {
/*  466 */     return unsafe.getByte(this.address + paramInt);
/*      */   }
/*      */ 
/*      */   void _put(int paramInt, byte paramByte)
/*      */   {
/*  471 */     unsafe.putByte(this.address + paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   private char getChar(long paramLong)
/*      */   {
/*  481 */     if (unaligned) {
/*  482 */       char c = unsafe.getChar(paramLong);
/*  483 */       return this.nativeByteOrder ? c : Bits.swap(c);
/*      */     }
/*  485 */     return Bits.getChar(paramLong, this.bigEndian);
/*      */   }
/*      */ 
/*      */   public char getChar() {
/*  489 */     return getChar(ix(nextGetIndex(2)));
/*      */   }
/*      */ 
/*      */   public char getChar(int paramInt) {
/*  493 */     return getChar(ix(checkIndex(paramInt, 2)));
/*      */   }
/*      */ 
/*      */   private ByteBuffer putChar(long paramLong, char paramChar)
/*      */   {
/*  500 */     if (unaligned) {
/*  501 */       char c = paramChar;
/*  502 */       unsafe.putChar(paramLong, this.nativeByteOrder ? c : Bits.swap(c));
/*      */     } else {
/*  504 */       Bits.putChar(paramLong, paramChar, this.bigEndian);
/*      */     }
/*  506 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putChar(char paramChar)
/*      */   {
/*  514 */     putChar(ix(nextPutIndex(2)), paramChar);
/*  515 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putChar(int paramInt, char paramChar)
/*      */   {
/*  523 */     putChar(ix(checkIndex(paramInt, 2)), paramChar);
/*  524 */     return this;
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
/*  538 */       return this.bigEndian ? new ByteBufferAsCharBufferB(this, -1, 0, m, m, i) : new ByteBufferAsCharBufferL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  552 */     return this.nativeByteOrder ? new DirectCharBufferU(this, -1, 0, m, m, i) : new DirectCharBufferS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private short getShort(long paramLong)
/*      */   {
/*  572 */     if (unaligned) {
/*  573 */       short s = unsafe.getShort(paramLong);
/*  574 */       return this.nativeByteOrder ? s : Bits.swap(s);
/*      */     }
/*  576 */     return Bits.getShort(paramLong, this.bigEndian);
/*      */   }
/*      */ 
/*      */   public short getShort() {
/*  580 */     return getShort(ix(nextGetIndex(2)));
/*      */   }
/*      */ 
/*      */   public short getShort(int paramInt) {
/*  584 */     return getShort(ix(checkIndex(paramInt, 2)));
/*      */   }
/*      */ 
/*      */   private ByteBuffer putShort(long paramLong, short paramShort)
/*      */   {
/*  591 */     if (unaligned) {
/*  592 */       short s = paramShort;
/*  593 */       unsafe.putShort(paramLong, this.nativeByteOrder ? s : Bits.swap(s));
/*      */     } else {
/*  595 */       Bits.putShort(paramLong, paramShort, this.bigEndian);
/*      */     }
/*  597 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putShort(short paramShort)
/*      */   {
/*  605 */     putShort(ix(nextPutIndex(2)), paramShort);
/*  606 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putShort(int paramInt, short paramShort)
/*      */   {
/*  614 */     putShort(ix(checkIndex(paramInt, 2)), paramShort);
/*  615 */     return this;
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
/*  629 */       return this.bigEndian ? new ByteBufferAsShortBufferB(this, -1, 0, m, m, i) : new ByteBufferAsShortBufferL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  643 */     return this.nativeByteOrder ? new DirectShortBufferU(this, -1, 0, m, m, i) : new DirectShortBufferS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private int getInt(long paramLong)
/*      */   {
/*  663 */     if (unaligned) {
/*  664 */       int i = unsafe.getInt(paramLong);
/*  665 */       return this.nativeByteOrder ? i : Bits.swap(i);
/*      */     }
/*  667 */     return Bits.getInt(paramLong, this.bigEndian);
/*      */   }
/*      */ 
/*      */   public int getInt() {
/*  671 */     return getInt(ix(nextGetIndex(4)));
/*      */   }
/*      */ 
/*      */   public int getInt(int paramInt) {
/*  675 */     return getInt(ix(checkIndex(paramInt, 4)));
/*      */   }
/*      */ 
/*      */   private ByteBuffer putInt(long paramLong, int paramInt)
/*      */   {
/*  682 */     if (unaligned) {
/*  683 */       int i = paramInt;
/*  684 */       unsafe.putInt(paramLong, this.nativeByteOrder ? i : Bits.swap(i));
/*      */     } else {
/*  686 */       Bits.putInt(paramLong, paramInt, this.bigEndian);
/*      */     }
/*  688 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putInt(int paramInt)
/*      */   {
/*  696 */     putInt(ix(nextPutIndex(4)), paramInt);
/*  697 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putInt(int paramInt1, int paramInt2)
/*      */   {
/*  705 */     putInt(ix(checkIndex(paramInt1, 4)), paramInt2);
/*  706 */     return this;
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
/*  720 */       return this.bigEndian ? new ByteBufferAsIntBufferB(this, -1, 0, m, m, i) : new ByteBufferAsIntBufferL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  734 */     return this.nativeByteOrder ? new DirectIntBufferU(this, -1, 0, m, m, i) : new DirectIntBufferS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private long getLong(long paramLong)
/*      */   {
/*  754 */     if (unaligned) {
/*  755 */       long l = unsafe.getLong(paramLong);
/*  756 */       return this.nativeByteOrder ? l : Bits.swap(l);
/*      */     }
/*  758 */     return Bits.getLong(paramLong, this.bigEndian);
/*      */   }
/*      */ 
/*      */   public long getLong() {
/*  762 */     return getLong(ix(nextGetIndex(8)));
/*      */   }
/*      */ 
/*      */   public long getLong(int paramInt) {
/*  766 */     return getLong(ix(checkIndex(paramInt, 8)));
/*      */   }
/*      */ 
/*      */   private ByteBuffer putLong(long paramLong1, long paramLong2)
/*      */   {
/*  773 */     if (unaligned) {
/*  774 */       long l = paramLong2;
/*  775 */       unsafe.putLong(paramLong1, this.nativeByteOrder ? l : Bits.swap(l));
/*      */     } else {
/*  777 */       Bits.putLong(paramLong1, paramLong2, this.bigEndian);
/*      */     }
/*  779 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putLong(long paramLong)
/*      */   {
/*  787 */     putLong(ix(nextPutIndex(8)), paramLong);
/*  788 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putLong(int paramInt, long paramLong)
/*      */   {
/*  796 */     putLong(ix(checkIndex(paramInt, 8)), paramLong);
/*  797 */     return this;
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
/*  811 */       return this.bigEndian ? new ByteBufferAsLongBufferB(this, -1, 0, m, m, i) : new ByteBufferAsLongBufferL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  825 */     return this.nativeByteOrder ? new DirectLongBufferU(this, -1, 0, m, m, i) : new DirectLongBufferS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private float getFloat(long paramLong)
/*      */   {
/*  845 */     if (unaligned) {
/*  846 */       int i = unsafe.getInt(paramLong);
/*  847 */       return Float.intBitsToFloat(this.nativeByteOrder ? i : Bits.swap(i));
/*      */     }
/*  849 */     return Bits.getFloat(paramLong, this.bigEndian);
/*      */   }
/*      */ 
/*      */   public float getFloat() {
/*  853 */     return getFloat(ix(nextGetIndex(4)));
/*      */   }
/*      */ 
/*      */   public float getFloat(int paramInt) {
/*  857 */     return getFloat(ix(checkIndex(paramInt, 4)));
/*      */   }
/*      */ 
/*      */   private ByteBuffer putFloat(long paramLong, float paramFloat)
/*      */   {
/*  864 */     if (unaligned) {
/*  865 */       int i = Float.floatToRawIntBits(paramFloat);
/*  866 */       unsafe.putInt(paramLong, this.nativeByteOrder ? i : Bits.swap(i));
/*      */     } else {
/*  868 */       Bits.putFloat(paramLong, paramFloat, this.bigEndian);
/*      */     }
/*  870 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putFloat(float paramFloat)
/*      */   {
/*  878 */     putFloat(ix(nextPutIndex(4)), paramFloat);
/*  879 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putFloat(int paramInt, float paramFloat)
/*      */   {
/*  887 */     putFloat(ix(checkIndex(paramInt, 4)), paramFloat);
/*  888 */     return this;
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
/*  902 */       return this.bigEndian ? new ByteBufferAsFloatBufferB(this, -1, 0, m, m, i) : new ByteBufferAsFloatBufferL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/*  916 */     return this.nativeByteOrder ? new DirectFloatBufferU(this, -1, 0, m, m, i) : new DirectFloatBufferS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private double getDouble(long paramLong)
/*      */   {
/*  936 */     if (unaligned) {
/*  937 */       long l = unsafe.getLong(paramLong);
/*  938 */       return Double.longBitsToDouble(this.nativeByteOrder ? l : Bits.swap(l));
/*      */     }
/*  940 */     return Bits.getDouble(paramLong, this.bigEndian);
/*      */   }
/*      */ 
/*      */   public double getDouble() {
/*  944 */     return getDouble(ix(nextGetIndex(8)));
/*      */   }
/*      */ 
/*      */   public double getDouble(int paramInt) {
/*  948 */     return getDouble(ix(checkIndex(paramInt, 8)));
/*      */   }
/*      */ 
/*      */   private ByteBuffer putDouble(long paramLong, double paramDouble)
/*      */   {
/*  955 */     if (unaligned) {
/*  956 */       long l = Double.doubleToRawLongBits(paramDouble);
/*  957 */       unsafe.putLong(paramLong, this.nativeByteOrder ? l : Bits.swap(l));
/*      */     } else {
/*  959 */       Bits.putDouble(paramLong, paramDouble, this.bigEndian);
/*      */     }
/*  961 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putDouble(double paramDouble)
/*      */   {
/*  969 */     putDouble(ix(nextPutIndex(8)), paramDouble);
/*  970 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer putDouble(int paramInt, double paramDouble)
/*      */   {
/*  978 */     putDouble(ix(checkIndex(paramInt, 8)), paramDouble);
/*  979 */     return this;
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
/*  993 */       return this.bigEndian ? new ByteBufferAsDoubleBufferB(this, -1, 0, m, m, i) : new ByteBufferAsDoubleBufferL(this, -1, 0, m, m, i);
/*      */     }
/*      */ 
/* 1007 */     return this.nativeByteOrder ? new DirectDoubleBufferU(this, -1, 0, m, m, i) : new DirectDoubleBufferS(this, -1, 0, m, m, i);
/*      */   }
/*      */ 
/*      */   private static class Deallocator
/*      */     implements Runnable
/*      */   {
/*   76 */     private static Unsafe unsafe = Unsafe.getUnsafe();
/*      */     private long address;
/*      */     private long size;
/*      */     private int capacity;
/*      */ 
/*      */     private Deallocator(long paramLong1, long paramLong2, int paramInt)
/*      */     {
/*   83 */       assert (paramLong1 != 0L);
/*   84 */       this.address = paramLong1;
/*   85 */       this.size = paramLong2;
/*   86 */       this.capacity = paramInt;
/*      */     }
/*      */ 
/*      */     public void run() {
/*   90 */       if (this.address == 0L)
/*      */       {
/*   92 */         return;
/*      */       }
/*   94 */       unsafe.freeMemory(this.address);
/*   95 */       this.address = 0L;
/*   96 */       Bits.unreserveMemory(this.size, this.capacity);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectByteBuffer
 * JD-Core Version:    0.6.2
 */