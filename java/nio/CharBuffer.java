/*      */ package java.nio;
/*      */ 
/*      */ import java.io.IOException;
/*      */ 
/*      */ public abstract class CharBuffer extends Buffer
/*      */   implements Comparable<CharBuffer>, Appendable, CharSequence, Readable
/*      */ {
/*      */   final char[] hb;
/*      */   final int offset;
/*      */   boolean isReadOnly;
/*      */ 
/*      */   CharBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, char[] paramArrayOfChar, int paramInt5)
/*      */   {
/*  276 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  277 */     this.hb = paramArrayOfChar;
/*  278 */     this.offset = paramInt5;
/*      */   }
/*      */ 
/*      */   CharBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  284 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0);
/*      */   }
/*      */ 
/*      */   public static CharBuffer allocate(int paramInt)
/*      */   {
/*  329 */     if (paramInt < 0)
/*  330 */       throw new IllegalArgumentException();
/*  331 */     return new HeapCharBuffer(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public static CharBuffer wrap(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  369 */       return new HeapCharBuffer(paramArrayOfChar, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  371 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static CharBuffer wrap(char[] paramArrayOfChar)
/*      */   {
/*  392 */     return wrap(paramArrayOfChar, 0, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   public int read(CharBuffer paramCharBuffer)
/*      */     throws IOException
/*      */   {
/*  413 */     int i = paramCharBuffer.remaining();
/*  414 */     int j = remaining();
/*  415 */     if (j == 0)
/*  416 */       return -1;
/*  417 */     int k = Math.min(j, i);
/*  418 */     int m = limit();
/*      */ 
/*  420 */     if (i < j)
/*  421 */       limit(position() + k);
/*      */     try {
/*  423 */       if (k > 0)
/*  424 */         paramCharBuffer.put(this);
/*      */     } finally {
/*  426 */       limit(m);
/*      */     }
/*  428 */     return k;
/*      */   }
/*      */ 
/*      */   public static CharBuffer wrap(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  462 */       return new StringCharBuffer(paramCharSequence, paramInt1, paramInt2); } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*  464 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   public static CharBuffer wrap(CharSequence paramCharSequence)
/*      */   {
/*  483 */     return wrap(paramCharSequence, 0, paramCharSequence.length());
/*      */   }
/*      */ 
/*      */   public abstract CharBuffer slice();
/*      */ 
/*      */   public abstract CharBuffer duplicate();
/*      */ 
/*      */   public abstract CharBuffer asReadOnlyBuffer();
/*      */ 
/*      */   public abstract char get();
/*      */ 
/*      */   public abstract CharBuffer put(char paramChar);
/*      */ 
/*      */   public abstract char get(int paramInt);
/*      */ 
/*      */   public abstract CharBuffer put(int paramInt, char paramChar);
/*      */ 
/*      */   public CharBuffer get(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  668 */     checkBounds(paramInt1, paramInt2, paramArrayOfChar.length);
/*  669 */     if (paramInt2 > remaining())
/*  670 */       throw new BufferUnderflowException();
/*  671 */     int i = paramInt1 + paramInt2;
/*  672 */     for (int j = paramInt1; j < i; j++)
/*  673 */       paramArrayOfChar[j] = get();
/*  674 */     return this;
/*      */   }
/*      */ 
/*      */   public CharBuffer get(char[] paramArrayOfChar)
/*      */   {
/*  694 */     return get(paramArrayOfChar, 0, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   public CharBuffer put(CharBuffer paramCharBuffer)
/*      */   {
/*  742 */     if (paramCharBuffer == this)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     int i = paramCharBuffer.remaining();
/*  745 */     if (i > remaining())
/*  746 */       throw new BufferOverflowException();
/*  747 */     for (int j = 0; j < i; j++)
/*  748 */       put(paramCharBuffer.get());
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */   public CharBuffer put(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  803 */     checkBounds(paramInt1, paramInt2, paramArrayOfChar.length);
/*  804 */     if (paramInt2 > remaining())
/*  805 */       throw new BufferOverflowException();
/*  806 */     int i = paramInt1 + paramInt2;
/*  807 */     for (int j = paramInt1; j < i; j++)
/*  808 */       put(paramArrayOfChar[j]);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   public final CharBuffer put(char[] paramArrayOfChar)
/*      */   {
/*  832 */     return put(paramArrayOfChar, 0, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   public CharBuffer put(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  890 */     checkBounds(paramInt1, paramInt2 - paramInt1, paramString.length());
/*  891 */     for (int i = paramInt1; i < paramInt2; i++)
/*  892 */       put(paramString.charAt(i));
/*  893 */     return this;
/*      */   }
/*      */ 
/*      */   public final CharBuffer put(String paramString)
/*      */   {
/*  915 */     return put(paramString, 0, paramString.length());
/*      */   }
/*      */ 
/*      */   public final boolean hasArray()
/*      */   {
/*  935 */     return (this.hb != null) && (!this.isReadOnly);
/*      */   }
/*      */ 
/*      */   public final char[] array()
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
/*      */   public abstract CharBuffer compact();
/*      */ 
/*      */   public abstract boolean isDirect();
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
/* 1122 */     if (!(paramObject instanceof CharBuffer))
/* 1123 */       return false;
/* 1124 */     CharBuffer localCharBuffer = (CharBuffer)paramObject;
/* 1125 */     if (remaining() != localCharBuffer.remaining())
/* 1126 */       return false;
/* 1127 */     int i = position();
/* 1128 */     int j = limit() - 1; for (int k = localCharBuffer.limit() - 1; j >= i; k--) {
/* 1129 */       if (!equals(get(j), localCharBuffer.get(k)))
/* 1130 */         return false;
/* 1128 */       j--;
/*      */     }
/*      */ 
/* 1131 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean equals(char paramChar1, char paramChar2)
/*      */   {
/* 1138 */     return paramChar1 == paramChar2;
/*      */   }
/*      */ 
/*      */   public int compareTo(CharBuffer paramCharBuffer)
/*      */   {
/* 1166 */     int i = position() + Math.min(remaining(), paramCharBuffer.remaining());
/* 1167 */     int j = position(); for (int k = paramCharBuffer.position(); j < i; k++) {
/* 1168 */       int m = compare(get(j), paramCharBuffer.get(k));
/* 1169 */       if (m != 0)
/* 1170 */         return m;
/* 1167 */       j++;
/*      */     }
/*      */ 
/* 1172 */     return remaining() - paramCharBuffer.remaining();
/*      */   }
/*      */ 
/*      */   private static int compare(char paramChar1, char paramChar2)
/*      */   {
/* 1182 */     return Character.compare(paramChar1, paramChar2);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1201 */     return toString(position(), limit());
/*      */   }
/*      */ 
/*      */   abstract String toString(int paramInt1, int paramInt2);
/*      */ 
/*      */   public final int length()
/*      */   {
/* 1220 */     return remaining();
/*      */   }
/*      */ 
/*      */   public final char charAt(int paramInt)
/*      */   {
/* 1238 */     return get(position() + checkIndex(paramInt, 1));
/*      */   }
/*      */ 
/*      */   public abstract CharBuffer subSequence(int paramInt1, int paramInt2);
/*      */ 
/*      */   public CharBuffer append(CharSequence paramCharSequence)
/*      */   {
/* 1308 */     if (paramCharSequence == null) {
/* 1309 */       return put("null");
/*      */     }
/* 1311 */     return put(paramCharSequence.toString());
/*      */   }
/*      */ 
/*      */   public CharBuffer append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/* 1347 */     CharSequence localCharSequence = paramCharSequence == null ? "null" : paramCharSequence;
/* 1348 */     return put(localCharSequence.subSequence(paramInt1, paramInt2).toString());
/*      */   }
/*      */ 
/*      */   public CharBuffer append(char paramChar)
/*      */   {
/* 1375 */     return put(paramChar);
/*      */   }
/*      */ 
/*      */   public abstract ByteOrder order();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.CharBuffer
 * JD-Core Version:    0.6.2
 */