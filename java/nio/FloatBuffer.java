/*      */ package java.nio;
/*      */ 
/*      */ public abstract class FloatBuffer extends Buffer
/*      */   implements Comparable<FloatBuffer>
/*      */ {
/*      */   final float[] hb;
/*      */   final int offset;
/*      */   boolean isReadOnly;
/*      */ 
/*      */   FloatBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float[] paramArrayOfFloat, int paramInt5)
/*      */   {
/*  276 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  277 */     this.hb = paramArrayOfFloat;
/*  278 */     this.offset = paramInt5;
/*      */   }
/*      */ 
/*      */   FloatBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  284 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0);
/*      */   }
/*      */ 
/*      */   public static FloatBuffer allocate(int paramInt)
/*      */   {
/*  329 */     if (paramInt < 0)
/*  330 */       throw new IllegalArgumentException();
/*  331 */     return new HeapFloatBuffer(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public static FloatBuffer wrap(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  369 */       return new HeapFloatBuffer(paramArrayOfFloat, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  371 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static FloatBuffer wrap(float[] paramArrayOfFloat)
/*      */   {
/*  392 */     return wrap(paramArrayOfFloat, 0, paramArrayOfFloat.length);
/*      */   }
/*      */ 
/*      */   public abstract FloatBuffer slice();
/*      */ 
/*      */   public abstract FloatBuffer duplicate();
/*      */ 
/*      */   public abstract FloatBuffer asReadOnlyBuffer();
/*      */ 
/*      */   public abstract float get();
/*      */ 
/*      */   public abstract FloatBuffer put(float paramFloat);
/*      */ 
/*      */   public abstract float get(int paramInt);
/*      */ 
/*      */   public abstract FloatBuffer put(int paramInt, float paramFloat);
/*      */ 
/*      */   public FloatBuffer get(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/*  668 */     checkBounds(paramInt1, paramInt2, paramArrayOfFloat.length);
/*  669 */     if (paramInt2 > remaining())
/*  670 */       throw new BufferUnderflowException();
/*  671 */     int i = paramInt1 + paramInt2;
/*  672 */     for (int j = paramInt1; j < i; j++)
/*  673 */       paramArrayOfFloat[j] = get();
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */   public FloatBuffer get(float[] paramArrayOfFloat)
/*      */   {
/*  694 */     return get(paramArrayOfFloat, 0, paramArrayOfFloat.length);
/*      */   }
/*      */ 
/*      */   public FloatBuffer put(FloatBuffer paramFloatBuffer)
/*      */   {
/*  742 */     if (paramFloatBuffer == this)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     int i = paramFloatBuffer.remaining();
/*  745 */     if (i > remaining())
/*  746 */       throw new BufferOverflowException();
/*  747 */     for (int j = 0; j < i; j++)
/*  748 */       put(paramFloatBuffer.get());
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   public FloatBuffer put(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/*  803 */     checkBounds(paramInt1, paramInt2, paramArrayOfFloat.length);
/*  804 */     if (paramInt2 > remaining())
/*  805 */       throw new BufferOverflowException();
/*  806 */     int i = paramInt1 + paramInt2;
/*  807 */     for (int j = paramInt1; j < i; j++)
/*  808 */       put(paramArrayOfFloat[j]);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   public final FloatBuffer put(float[] paramArrayOfFloat)
/*      */   {
/*  832 */     return put(paramArrayOfFloat, 0, paramArrayOfFloat.length);
/*      */   }
/*      */ 
/*      */   public final boolean hasArray()
/*      */   {
/*  935 */     return (this.hb != null) && (!this.isReadOnly);
/*      */   }
/*      */ 
/*      */   public final float[] array()
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
/*      */   public abstract FloatBuffer compact();
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
/* 1122 */     if (!(paramObject instanceof FloatBuffer))
/* 1123 */       return false;
/* 1124 */     FloatBuffer localFloatBuffer = (FloatBuffer)paramObject;
/* 1125 */     if (remaining() != localFloatBuffer.remaining())
/* 1126 */       return false;
/* 1127 */     int i = position();
/* 1128 */     int j = limit() - 1; for (int k = localFloatBuffer.limit() - 1; j >= i; k--) {
/* 1129 */       if (!equals(get(j), localFloatBuffer.get(k)))
/* 1130 */         return false;
/* 1128 */       j--;
/*      */     }
/*      */ 
/* 1131 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean equals(float paramFloat1, float paramFloat2)
/*      */   {
/* 1136 */     return (paramFloat1 == paramFloat2) || ((Float.isNaN(paramFloat1)) && (Float.isNaN(paramFloat2)));
/*      */   }
/*      */ 
/*      */   public int compareTo(FloatBuffer paramFloatBuffer)
/*      */   {
/* 1166 */     int i = position() + Math.min(remaining(), paramFloatBuffer.remaining());
/* 1167 */     int j = position(); for (int k = paramFloatBuffer.position(); j < i; k++) {
/* 1168 */       int m = compare(get(j), paramFloatBuffer.get(k));
/* 1169 */       if (m != 0)
/* 1170 */         return m;
/* 1167 */       j++;
/*      */     }
/*      */ 
/* 1172 */     return remaining() - paramFloatBuffer.remaining();
/*      */   }
/*      */ 
/*      */   private static int compare(float paramFloat1, float paramFloat2)
/*      */   {
/* 1177 */     return Float.isNaN(paramFloat1) ? 1 : Float.isNaN(paramFloat2) ? 0 : paramFloat1 == paramFloat2 ? 0 : paramFloat1 > paramFloat2 ? 1 : paramFloat1 < paramFloat2 ? -1 : -1;
/*      */   }
/*      */ 
/*      */   public abstract ByteOrder order();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.FloatBuffer
 * JD-Core Version:    0.6.2
 */