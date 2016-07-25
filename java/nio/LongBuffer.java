/*      */ package java.nio;
/*      */ 
/*      */ public abstract class LongBuffer extends Buffer
/*      */   implements Comparable<LongBuffer>
/*      */ {
/*      */   final long[] hb;
/*      */   final int offset;
/*      */   boolean isReadOnly;
/*      */ 
/*      */   LongBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, int paramInt5)
/*      */   {
/*  276 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  277 */     this.hb = paramArrayOfLong;
/*  278 */     this.offset = paramInt5;
/*      */   }
/*      */ 
/*      */   LongBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  284 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0);
/*      */   }
/*      */ 
/*      */   public static LongBuffer allocate(int paramInt)
/*      */   {
/*  329 */     if (paramInt < 0)
/*  330 */       throw new IllegalArgumentException();
/*  331 */     return new HeapLongBuffer(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public static LongBuffer wrap(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  369 */       return new HeapLongBuffer(paramArrayOfLong, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  371 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static LongBuffer wrap(long[] paramArrayOfLong)
/*      */   {
/*  392 */     return wrap(paramArrayOfLong, 0, paramArrayOfLong.length);
/*      */   }
/*      */ 
/*      */   public abstract LongBuffer slice();
/*      */ 
/*      */   public abstract LongBuffer duplicate();
/*      */ 
/*      */   public abstract LongBuffer asReadOnlyBuffer();
/*      */ 
/*      */   public abstract long get();
/*      */ 
/*      */   public abstract LongBuffer put(long paramLong);
/*      */ 
/*      */   public abstract long get(int paramInt);
/*      */ 
/*      */   public abstract LongBuffer put(int paramInt, long paramLong);
/*      */ 
/*      */   public LongBuffer get(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/*  668 */     checkBounds(paramInt1, paramInt2, paramArrayOfLong.length);
/*  669 */     if (paramInt2 > remaining())
/*  670 */       throw new BufferUnderflowException();
/*  671 */     int i = paramInt1 + paramInt2;
/*  672 */     for (int j = paramInt1; j < i; j++)
/*  673 */       paramArrayOfLong[j] = get();
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */   public LongBuffer get(long[] paramArrayOfLong)
/*      */   {
/*  694 */     return get(paramArrayOfLong, 0, paramArrayOfLong.length);
/*      */   }
/*      */ 
/*      */   public LongBuffer put(LongBuffer paramLongBuffer)
/*      */   {
/*  742 */     if (paramLongBuffer == this)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     int i = paramLongBuffer.remaining();
/*  745 */     if (i > remaining())
/*  746 */       throw new BufferOverflowException();
/*  747 */     for (int j = 0; j < i; j++)
/*  748 */       put(paramLongBuffer.get());
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   public LongBuffer put(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/*  803 */     checkBounds(paramInt1, paramInt2, paramArrayOfLong.length);
/*  804 */     if (paramInt2 > remaining())
/*  805 */       throw new BufferOverflowException();
/*  806 */     int i = paramInt1 + paramInt2;
/*  807 */     for (int j = paramInt1; j < i; j++)
/*  808 */       put(paramArrayOfLong[j]);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   public final LongBuffer put(long[] paramArrayOfLong)
/*      */   {
/*  832 */     return put(paramArrayOfLong, 0, paramArrayOfLong.length);
/*      */   }
/*      */ 
/*      */   public final boolean hasArray()
/*      */   {
/*  935 */     return (this.hb != null) && (!this.isReadOnly);
/*      */   }
/*      */ 
/*      */   public final long[] array()
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
/*      */   public abstract LongBuffer compact();
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
/* 1083 */       i = 31 * i + (int)get(k);
/* 1084 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1120 */     if (this == paramObject)
/* 1121 */       return true;
/* 1122 */     if (!(paramObject instanceof LongBuffer))
/* 1123 */       return false;
/* 1124 */     LongBuffer localLongBuffer = (LongBuffer)paramObject;
/* 1125 */     if (remaining() != localLongBuffer.remaining())
/* 1126 */       return false;
/* 1127 */     int i = position();
/* 1128 */     int j = limit() - 1; for (int k = localLongBuffer.limit() - 1; j >= i; k--) {
/* 1129 */       if (!equals(get(j), localLongBuffer.get(k)))
/* 1130 */         return false;
/* 1128 */       j--;
/*      */     }
/*      */ 
/* 1131 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean equals(long paramLong1, long paramLong2)
/*      */   {
/* 1138 */     return paramLong1 == paramLong2;
/*      */   }
/*      */ 
/*      */   public int compareTo(LongBuffer paramLongBuffer)
/*      */   {
/* 1166 */     int i = position() + Math.min(remaining(), paramLongBuffer.remaining());
/* 1167 */     int j = position(); for (int k = paramLongBuffer.position(); j < i; k++) {
/* 1168 */       int m = compare(get(j), paramLongBuffer.get(k));
/* 1169 */       if (m != 0)
/* 1170 */         return m;
/* 1167 */       j++;
/*      */     }
/*      */ 
/* 1172 */     return remaining() - paramLongBuffer.remaining();
/*      */   }
/*      */ 
/*      */   private static int compare(long paramLong1, long paramLong2)
/*      */   {
/* 1182 */     return Long.compare(paramLong1, paramLong2);
/*      */   }
/*      */ 
/*      */   public abstract ByteOrder order();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.LongBuffer
 * JD-Core Version:    0.6.2
 */