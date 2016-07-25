/*      */ package java.nio;
/*      */ 
/*      */ public abstract class DoubleBuffer extends Buffer
/*      */   implements Comparable<DoubleBuffer>
/*      */ {
/*      */   final double[] hb;
/*      */   final int offset;
/*      */   boolean isReadOnly;
/*      */ 
/*      */   DoubleBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double[] paramArrayOfDouble, int paramInt5)
/*      */   {
/*  276 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  277 */     this.hb = paramArrayOfDouble;
/*  278 */     this.offset = paramInt5;
/*      */   }
/*      */ 
/*      */   DoubleBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  284 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0);
/*      */   }
/*      */ 
/*      */   public static DoubleBuffer allocate(int paramInt)
/*      */   {
/*  329 */     if (paramInt < 0)
/*  330 */       throw new IllegalArgumentException();
/*  331 */     return new HeapDoubleBuffer(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public static DoubleBuffer wrap(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  369 */       return new HeapDoubleBuffer(paramArrayOfDouble, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  371 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static DoubleBuffer wrap(double[] paramArrayOfDouble)
/*      */   {
/*  392 */     return wrap(paramArrayOfDouble, 0, paramArrayOfDouble.length);
/*      */   }
/*      */ 
/*      */   public abstract DoubleBuffer slice();
/*      */ 
/*      */   public abstract DoubleBuffer duplicate();
/*      */ 
/*      */   public abstract DoubleBuffer asReadOnlyBuffer();
/*      */ 
/*      */   public abstract double get();
/*      */ 
/*      */   public abstract DoubleBuffer put(double paramDouble);
/*      */ 
/*      */   public abstract double get(int paramInt);
/*      */ 
/*      */   public abstract DoubleBuffer put(int paramInt, double paramDouble);
/*      */ 
/*      */   public DoubleBuffer get(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/*  668 */     checkBounds(paramInt1, paramInt2, paramArrayOfDouble.length);
/*  669 */     if (paramInt2 > remaining())
/*  670 */       throw new BufferUnderflowException();
/*  671 */     int i = paramInt1 + paramInt2;
/*  672 */     for (int j = paramInt1; j < i; j++)
/*  673 */       paramArrayOfDouble[j] = get();
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */   public DoubleBuffer get(double[] paramArrayOfDouble)
/*      */   {
/*  694 */     return get(paramArrayOfDouble, 0, paramArrayOfDouble.length);
/*      */   }
/*      */ 
/*      */   public DoubleBuffer put(DoubleBuffer paramDoubleBuffer)
/*      */   {
/*  742 */     if (paramDoubleBuffer == this)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     int i = paramDoubleBuffer.remaining();
/*  745 */     if (i > remaining())
/*  746 */       throw new BufferOverflowException();
/*  747 */     for (int j = 0; j < i; j++)
/*  748 */       put(paramDoubleBuffer.get());
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   public DoubleBuffer put(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/*  803 */     checkBounds(paramInt1, paramInt2, paramArrayOfDouble.length);
/*  804 */     if (paramInt2 > remaining())
/*  805 */       throw new BufferOverflowException();
/*  806 */     int i = paramInt1 + paramInt2;
/*  807 */     for (int j = paramInt1; j < i; j++)
/*  808 */       put(paramArrayOfDouble[j]);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   public final DoubleBuffer put(double[] paramArrayOfDouble)
/*      */   {
/*  832 */     return put(paramArrayOfDouble, 0, paramArrayOfDouble.length);
/*      */   }
/*      */ 
/*      */   public final boolean hasArray()
/*      */   {
/*  935 */     return (this.hb != null) && (!this.isReadOnly);
/*      */   }
/*      */ 
/*      */   public final double[] array()
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
/*      */   public abstract DoubleBuffer compact();
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
/* 1122 */     if (!(paramObject instanceof DoubleBuffer))
/* 1123 */       return false;
/* 1124 */     DoubleBuffer localDoubleBuffer = (DoubleBuffer)paramObject;
/* 1125 */     if (remaining() != localDoubleBuffer.remaining())
/* 1126 */       return false;
/* 1127 */     int i = position();
/* 1128 */     int j = limit() - 1; for (int k = localDoubleBuffer.limit() - 1; j >= i; k--) {
/* 1129 */       if (!equals(get(j), localDoubleBuffer.get(k)))
/* 1130 */         return false;
/* 1128 */       j--;
/*      */     }
/*      */ 
/* 1131 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean equals(double paramDouble1, double paramDouble2)
/*      */   {
/* 1136 */     return (paramDouble1 == paramDouble2) || ((Double.isNaN(paramDouble1)) && (Double.isNaN(paramDouble2)));
/*      */   }
/*      */ 
/*      */   public int compareTo(DoubleBuffer paramDoubleBuffer)
/*      */   {
/* 1166 */     int i = position() + Math.min(remaining(), paramDoubleBuffer.remaining());
/* 1167 */     int j = position(); for (int k = paramDoubleBuffer.position(); j < i; k++) {
/* 1168 */       int m = compare(get(j), paramDoubleBuffer.get(k));
/* 1169 */       if (m != 0)
/* 1170 */         return m;
/* 1167 */       j++;
/*      */     }
/*      */ 
/* 1172 */     return remaining() - paramDoubleBuffer.remaining();
/*      */   }
/*      */ 
/*      */   private static int compare(double paramDouble1, double paramDouble2)
/*      */   {
/* 1177 */     return Double.isNaN(paramDouble1) ? 1 : Double.isNaN(paramDouble2) ? 0 : paramDouble1 == paramDouble2 ? 0 : paramDouble1 > paramDouble2 ? 1 : paramDouble1 < paramDouble2 ? -1 : -1;
/*      */   }
/*      */ 
/*      */   public abstract ByteOrder order();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DoubleBuffer
 * JD-Core Version:    0.6.2
 */