/*      */ package java.nio;
/*      */ 
/*      */ public abstract class ByteBuffer extends Buffer
/*      */   implements Comparable<ByteBuffer>
/*      */ {
/*      */   final byte[] hb;
/*      */   final int offset;
/*      */   boolean isReadOnly;
/* 1403 */   boolean bigEndian = true;
/*      */ 
/* 1405 */   boolean nativeByteOrder = Bits.byteOrder() == ByteOrder.BIG_ENDIAN;
/*      */ 
/*      */   ByteBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5)
/*      */   {
/*  276 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  277 */     this.hb = paramArrayOfByte;
/*  278 */     this.offset = paramInt5;
/*      */   }
/*      */ 
/*      */   ByteBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  284 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0);
/*      */   }
/*      */ 
/*      */   public static ByteBuffer allocateDirect(int paramInt)
/*      */   {
/*  306 */     return new DirectByteBuffer(paramInt);
/*      */   }
/*      */ 
/*      */   public static ByteBuffer allocate(int paramInt)
/*      */   {
/*  329 */     if (paramInt < 0)
/*  330 */       throw new IllegalArgumentException();
/*  331 */     return new HeapByteBuffer(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public static ByteBuffer wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  369 */       return new HeapByteBuffer(paramArrayOfByte, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  371 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static ByteBuffer wrap(byte[] paramArrayOfByte)
/*      */   {
/*  392 */     return wrap(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public abstract ByteBuffer slice();
/*      */ 
/*      */   public abstract ByteBuffer duplicate();
/*      */ 
/*      */   public abstract ByteBuffer asReadOnlyBuffer();
/*      */ 
/*      */   public abstract byte get();
/*      */ 
/*      */   public abstract ByteBuffer put(byte paramByte);
/*      */ 
/*      */   public abstract byte get(int paramInt);
/*      */ 
/*      */   public abstract ByteBuffer put(int paramInt, byte paramByte);
/*      */ 
/*      */   public ByteBuffer get(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  668 */     checkBounds(paramInt1, paramInt2, paramArrayOfByte.length);
/*  669 */     if (paramInt2 > remaining())
/*  670 */       throw new BufferUnderflowException();
/*  671 */     int i = paramInt1 + paramInt2;
/*  672 */     for (int j = paramInt1; j < i; j++)
/*  673 */       paramArrayOfByte[j] = get();
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer get(byte[] paramArrayOfByte)
/*      */   {
/*  694 */     return get(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(ByteBuffer paramByteBuffer)
/*      */   {
/*  742 */     if (paramByteBuffer == this)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     int i = paramByteBuffer.remaining();
/*  745 */     if (i > remaining())
/*  746 */       throw new BufferOverflowException();
/*  747 */     for (int j = 0; j < i; j++)
/*  748 */       put(paramByteBuffer.get());
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   public ByteBuffer put(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  803 */     checkBounds(paramInt1, paramInt2, paramArrayOfByte.length);
/*  804 */     if (paramInt2 > remaining())
/*  805 */       throw new BufferOverflowException();
/*  806 */     int i = paramInt1 + paramInt2;
/*  807 */     for (int j = paramInt1; j < i; j++)
/*  808 */       put(paramArrayOfByte[j]);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   public final ByteBuffer put(byte[] paramArrayOfByte)
/*      */   {
/*  832 */     return put(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public final boolean hasArray()
/*      */   {
/*  935 */     return (this.hb != null) && (!this.isReadOnly);
/*      */   }
/*      */ 
/*      */   public final byte[] array()
/*      */   {
/*  958 */     if (this.hb == null)
/*  959 */       throw new UnsupportedOperationException();
/*  960 */     if (this.isReadOnly)
/*  961 */       throw new ReadOnlyBufferException();
/*  962 */     return this.hb;
/*      */   }
/*      */ 
/*      */   public final int arrayOffset()
/*      */   {
/*  986 */     if (this.hb == null)
/*  987 */       throw new UnsupportedOperationException();
/*  988 */     if (this.isReadOnly)
/*  989 */       throw new ReadOnlyBufferException();
/*  990 */     return this.offset;
/*      */   }
/*      */ 
/*      */   public abstract ByteBuffer compact();
/*      */ 
/*      */   public abstract boolean isDirect();
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1049 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1050 */     localStringBuffer.append(getClass().getName());
/* 1051 */     localStringBuffer.append("[pos=");
/* 1052 */     localStringBuffer.append(position());
/* 1053 */     localStringBuffer.append(" lim=");
/* 1054 */     localStringBuffer.append(limit());
/* 1055 */     localStringBuffer.append(" cap=");
/* 1056 */     localStringBuffer.append(capacity());
/* 1057 */     localStringBuffer.append("]");
/* 1058 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1080 */     int i = 1;
/* 1081 */     int j = position();
/* 1082 */     for (int k = limit() - 1; k >= j; k--)
/* 1083 */       i = 31 * i + get(k);
/* 1084 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1120 */     if (this == paramObject)
/* 1121 */       return true;
/* 1122 */     if (!(paramObject instanceof ByteBuffer))
/* 1123 */       return false;
/* 1124 */     ByteBuffer localByteBuffer = (ByteBuffer)paramObject;
/* 1125 */     if (remaining() != localByteBuffer.remaining())
/* 1126 */       return false;
/* 1127 */     int i = position();
/* 1128 */     int j = limit() - 1; for (int k = localByteBuffer.limit() - 1; j >= i; k--) {
/* 1129 */       if (!equals(get(j), localByteBuffer.get(k)))
/* 1130 */         return false;
/* 1128 */       j--;
/*      */     }
/*      */ 
/* 1131 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean equals(byte paramByte1, byte paramByte2)
/*      */   {
/* 1138 */     return paramByte1 == paramByte2;
/*      */   }
/*      */ 
/*      */   public int compareTo(ByteBuffer paramByteBuffer)
/*      */   {
/* 1166 */     int i = position() + Math.min(remaining(), paramByteBuffer.remaining());
/* 1167 */     int j = position(); for (int k = paramByteBuffer.position(); j < i; k++) {
/* 1168 */       int m = compare(get(j), paramByteBuffer.get(k));
/* 1169 */       if (m != 0)
/* 1170 */         return m;
/* 1167 */       j++;
/*      */     }
/*      */ 
/* 1172 */     return remaining() - paramByteBuffer.remaining();
/*      */   }
/*      */ 
/*      */   private static int compare(byte paramByte1, byte paramByte2)
/*      */   {
/* 1182 */     return Byte.compare(paramByte1, paramByte2);
/*      */   }
/*      */ 
/*      */   public final ByteOrder order()
/*      */   {
/* 1419 */     return this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
/*      */   }
/*      */ 
/*      */   public final ByteBuffer order(ByteOrder paramByteOrder)
/*      */   {
/* 1433 */     this.bigEndian = (paramByteOrder == ByteOrder.BIG_ENDIAN);
/* 1434 */     this.nativeByteOrder = (this.bigEndian == (Bits.byteOrder() == ByteOrder.BIG_ENDIAN));
/*      */ 
/* 1436 */     return this;
/*      */   }
/*      */ 
/*      */   abstract byte _get(int paramInt);
/*      */ 
/*      */   abstract void _put(int paramInt, byte paramByte);
/*      */ 
/*      */   public abstract char getChar();
/*      */ 
/*      */   public abstract ByteBuffer putChar(char paramChar);
/*      */ 
/*      */   public abstract char getChar(int paramInt);
/*      */ 
/*      */   public abstract ByteBuffer putChar(int paramInt, char paramChar);
/*      */ 
/*      */   public abstract CharBuffer asCharBuffer();
/*      */ 
/*      */   public abstract short getShort();
/*      */ 
/*      */   public abstract ByteBuffer putShort(short paramShort);
/*      */ 
/*      */   public abstract short getShort(int paramInt);
/*      */ 
/*      */   public abstract ByteBuffer putShort(int paramInt, short paramShort);
/*      */ 
/*      */   public abstract ShortBuffer asShortBuffer();
/*      */ 
/*      */   public abstract int getInt();
/*      */ 
/*      */   public abstract ByteBuffer putInt(int paramInt);
/*      */ 
/*      */   public abstract int getInt(int paramInt);
/*      */ 
/*      */   public abstract ByteBuffer putInt(int paramInt1, int paramInt2);
/*      */ 
/*      */   public abstract IntBuffer asIntBuffer();
/*      */ 
/*      */   public abstract long getLong();
/*      */ 
/*      */   public abstract ByteBuffer putLong(long paramLong);
/*      */ 
/*      */   public abstract long getLong(int paramInt);
/*      */ 
/*      */   public abstract ByteBuffer putLong(int paramInt, long paramLong);
/*      */ 
/*      */   public abstract LongBuffer asLongBuffer();
/*      */ 
/*      */   public abstract float getFloat();
/*      */ 
/*      */   public abstract ByteBuffer putFloat(float paramFloat);
/*      */ 
/*      */   public abstract float getFloat(int paramInt);
/*      */ 
/*      */   public abstract ByteBuffer putFloat(int paramInt, float paramFloat);
/*      */ 
/*      */   public abstract FloatBuffer asFloatBuffer();
/*      */ 
/*      */   public abstract double getDouble();
/*      */ 
/*      */   public abstract ByteBuffer putDouble(double paramDouble);
/*      */ 
/*      */   public abstract double getDouble(int paramInt);
/*      */ 
/*      */   public abstract ByteBuffer putDouble(int paramInt, double paramDouble);
/*      */ 
/*      */   public abstract DoubleBuffer asDoubleBuffer();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.ByteBuffer
 * JD-Core Version:    0.6.2
 */