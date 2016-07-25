/*      */ package java.nio;
/*      */ 
/*      */ public abstract class IntBuffer extends Buffer
/*      */   implements Comparable<IntBuffer>
/*      */ {
/*      */   final int[] hb;
/*      */   final int offset;
/*      */   boolean isReadOnly;
/*      */ 
/*      */   IntBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5)
/*      */   {
/*  276 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  277 */     this.hb = paramArrayOfInt;
/*  278 */     this.offset = paramInt5;
/*      */   }
/*      */ 
/*      */   IntBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  284 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0);
/*      */   }
/*      */ 
/*      */   public static IntBuffer allocate(int paramInt)
/*      */   {
/*  329 */     if (paramInt < 0)
/*  330 */       throw new IllegalArgumentException();
/*  331 */     return new HeapIntBuffer(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public static IntBuffer wrap(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  369 */       return new HeapIntBuffer(paramArrayOfInt, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  371 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static IntBuffer wrap(int[] paramArrayOfInt)
/*      */   {
/*  392 */     return wrap(paramArrayOfInt, 0, paramArrayOfInt.length);
/*      */   }
/*      */ 
/*      */   public abstract IntBuffer slice();
/*      */ 
/*      */   public abstract IntBuffer duplicate();
/*      */ 
/*      */   public abstract IntBuffer asReadOnlyBuffer();
/*      */ 
/*      */   public abstract int get();
/*      */ 
/*      */   public abstract IntBuffer put(int paramInt);
/*      */ 
/*      */   public abstract int get(int paramInt);
/*      */ 
/*      */   public abstract IntBuffer put(int paramInt1, int paramInt2);
/*      */ 
/*      */   public IntBuffer get(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  668 */     checkBounds(paramInt1, paramInt2, paramArrayOfInt.length);
/*  669 */     if (paramInt2 > remaining())
/*  670 */       throw new BufferUnderflowException();
/*  671 */     int i = paramInt1 + paramInt2;
/*  672 */     for (int j = paramInt1; j < i; j++)
/*  673 */       paramArrayOfInt[j] = get();
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */   public IntBuffer get(int[] paramArrayOfInt)
/*      */   {
/*  694 */     return get(paramArrayOfInt, 0, paramArrayOfInt.length);
/*      */   }
/*      */ 
/*      */   public IntBuffer put(IntBuffer paramIntBuffer)
/*      */   {
/*  742 */     if (paramIntBuffer == this)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     int i = paramIntBuffer.remaining();
/*  745 */     if (i > remaining())
/*  746 */       throw new BufferOverflowException();
/*  747 */     for (int j = 0; j < i; j++)
/*  748 */       put(paramIntBuffer.get());
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   public IntBuffer put(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  803 */     checkBounds(paramInt1, paramInt2, paramArrayOfInt.length);
/*  804 */     if (paramInt2 > remaining())
/*  805 */       throw new BufferOverflowException();
/*  806 */     int i = paramInt1 + paramInt2;
/*  807 */     for (int j = paramInt1; j < i; j++)
/*  808 */       put(paramArrayOfInt[j]);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   public final IntBuffer put(int[] paramArrayOfInt)
/*      */   {
/*  832 */     return put(paramArrayOfInt, 0, paramArrayOfInt.length);
/*      */   }
/*      */ 
/*      */   public final boolean hasArray()
/*      */   {
/*  935 */     return (this.hb != null) && (!this.isReadOnly);
/*      */   }
/*      */ 
/*      */   public final int[] array()
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
/*      */   public abstract IntBuffer compact();
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
/* 1122 */     if (!(paramObject instanceof IntBuffer))
/* 1123 */       return false;
/* 1124 */     IntBuffer localIntBuffer = (IntBuffer)paramObject;
/* 1125 */     if (remaining() != localIntBuffer.remaining())
/* 1126 */       return false;
/* 1127 */     int i = position();
/* 1128 */     int j = limit() - 1; for (int k = localIntBuffer.limit() - 1; j >= i; k--) {
/* 1129 */       if (!equals(get(j), localIntBuffer.get(k)))
/* 1130 */         return false;
/* 1128 */       j--;
/*      */     }
/*      */ 
/* 1131 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean equals(int paramInt1, int paramInt2)
/*      */   {
/* 1138 */     return paramInt1 == paramInt2;
/*      */   }
/*      */ 
/*      */   public int compareTo(IntBuffer paramIntBuffer)
/*      */   {
/* 1166 */     int i = position() + Math.min(remaining(), paramIntBuffer.remaining());
/* 1167 */     int j = position(); for (int k = paramIntBuffer.position(); j < i; k++) {
/* 1168 */       int m = compare(get(j), paramIntBuffer.get(k));
/* 1169 */       if (m != 0)
/* 1170 */         return m;
/* 1167 */       j++;
/*      */     }
/*      */ 
/* 1172 */     return remaining() - paramIntBuffer.remaining();
/*      */   }
/*      */ 
/*      */   private static int compare(int paramInt1, int paramInt2)
/*      */   {
/* 1182 */     return Integer.compare(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public abstract ByteOrder order();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.IntBuffer
 * JD-Core Version:    0.6.2
 */