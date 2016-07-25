/*      */ package java.nio;
/*      */ 
/*      */ public abstract class ShortBuffer extends Buffer
/*      */   implements Comparable<ShortBuffer>
/*      */ {
/*      */   final short[] hb;
/*      */   final int offset;
/*      */   boolean isReadOnly;
/*      */ 
/*      */   ShortBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort, int paramInt5)
/*      */   {
/*  276 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  277 */     this.hb = paramArrayOfShort;
/*  278 */     this.offset = paramInt5;
/*      */   }
/*      */ 
/*      */   ShortBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  284 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0);
/*      */   }
/*      */ 
/*      */   public static ShortBuffer allocate(int paramInt)
/*      */   {
/*  329 */     if (paramInt < 0)
/*  330 */       throw new IllegalArgumentException();
/*  331 */     return new HeapShortBuffer(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public static ShortBuffer wrap(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  369 */       return new HeapShortBuffer(paramArrayOfShort, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  371 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static ShortBuffer wrap(short[] paramArrayOfShort)
/*      */   {
/*  392 */     return wrap(paramArrayOfShort, 0, paramArrayOfShort.length);
/*      */   }
/*      */ 
/*      */   public abstract ShortBuffer slice();
/*      */ 
/*      */   public abstract ShortBuffer duplicate();
/*      */ 
/*      */   public abstract ShortBuffer asReadOnlyBuffer();
/*      */ 
/*      */   public abstract short get();
/*      */ 
/*      */   public abstract ShortBuffer put(short paramShort);
/*      */ 
/*      */   public abstract short get(int paramInt);
/*      */ 
/*      */   public abstract ShortBuffer put(int paramInt, short paramShort);
/*      */ 
/*      */   public ShortBuffer get(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/*  668 */     checkBounds(paramInt1, paramInt2, paramArrayOfShort.length);
/*  669 */     if (paramInt2 > remaining())
/*  670 */       throw new BufferUnderflowException();
/*  671 */     int i = paramInt1 + paramInt2;
/*  672 */     for (int j = paramInt1; j < i; j++)
/*  673 */       paramArrayOfShort[j] = get();
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */   public ShortBuffer get(short[] paramArrayOfShort)
/*      */   {
/*  694 */     return get(paramArrayOfShort, 0, paramArrayOfShort.length);
/*      */   }
/*      */ 
/*      */   public ShortBuffer put(ShortBuffer paramShortBuffer)
/*      */   {
/*  742 */     if (paramShortBuffer == this)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     int i = paramShortBuffer.remaining();
/*  745 */     if (i > remaining())
/*  746 */       throw new BufferOverflowException();
/*  747 */     for (int j = 0; j < i; j++)
/*  748 */       put(paramShortBuffer.get());
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   public ShortBuffer put(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/*  803 */     checkBounds(paramInt1, paramInt2, paramArrayOfShort.length);
/*  804 */     if (paramInt2 > remaining())
/*  805 */       throw new BufferOverflowException();
/*  806 */     int i = paramInt1 + paramInt2;
/*  807 */     for (int j = paramInt1; j < i; j++)
/*  808 */       put(paramArrayOfShort[j]);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   public final ShortBuffer put(short[] paramArrayOfShort)
/*      */   {
/*  832 */     return put(paramArrayOfShort, 0, paramArrayOfShort.length);
/*      */   }
/*      */ 
/*      */   public final boolean hasArray()
/*      */   {
/*  935 */     return (this.hb != null) && (!this.isReadOnly);
/*      */   }
/*      */ 
/*      */   public final short[] array()
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
/*      */   public abstract ShortBuffer compact();
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
/* 1122 */     if (!(paramObject instanceof ShortBuffer))
/* 1123 */       return false;
/* 1124 */     ShortBuffer localShortBuffer = (ShortBuffer)paramObject;
/* 1125 */     if (remaining() != localShortBuffer.remaining())
/* 1126 */       return false;
/* 1127 */     int i = position();
/* 1128 */     int j = limit() - 1; for (int k = localShortBuffer.limit() - 1; j >= i; k--) {
/* 1129 */       if (!equals(get(j), localShortBuffer.get(k)))
/* 1130 */         return false;
/* 1128 */       j--;
/*      */     }
/*      */ 
/* 1131 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean equals(short paramShort1, short paramShort2)
/*      */   {
/* 1138 */     return paramShort1 == paramShort2;
/*      */   }
/*      */ 
/*      */   public int compareTo(ShortBuffer paramShortBuffer)
/*      */   {
/* 1166 */     int i = position() + Math.min(remaining(), paramShortBuffer.remaining());
/* 1167 */     int j = position(); for (int k = paramShortBuffer.position(); j < i; k++) {
/* 1168 */       int m = compare(get(j), paramShortBuffer.get(k));
/* 1169 */       if (m != 0)
/* 1170 */         return m;
/* 1167 */       j++;
/*      */     }
/*      */ 
/* 1172 */     return remaining() - paramShortBuffer.remaining();
/*      */   }
/*      */ 
/*      */   private static int compare(short paramShort1, short paramShort2)
/*      */   {
/* 1182 */     return Short.compare(paramShort1, paramShort2);
/*      */   }
/*      */ 
/*      */   public abstract ByteOrder order();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.ShortBuffer
 * JD-Core Version:    0.6.2
 */