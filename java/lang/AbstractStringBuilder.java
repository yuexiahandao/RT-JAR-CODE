/*      */ package java.lang;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import sun.misc.FloatingDecimal;
/*      */ 
/*      */ abstract class AbstractStringBuilder
/*      */   implements Appendable, CharSequence
/*      */ {
/*      */   char[] value;
/*      */   int count;
/*      */ 
/*      */   AbstractStringBuilder()
/*      */   {
/*      */   }
/*      */ 
/*      */   AbstractStringBuilder(int paramInt)
/*      */   {
/*   64 */     this.value = new char[paramInt];
/*      */   }
/*      */ 
/*      */   public int length()
/*      */   {
/*   74 */     return this.count;
/*      */   }
/*      */ 
/*      */   public int capacity()
/*      */   {
/*   85 */     return this.value.length;
/*      */   }
/*      */ 
/*      */   public void ensureCapacity(int paramInt)
/*      */   {
/*  103 */     if (paramInt > 0)
/*  104 */       ensureCapacityInternal(paramInt);
/*      */   }
/*      */ 
/*      */   private void ensureCapacityInternal(int paramInt)
/*      */   {
/*  113 */     if (paramInt - this.value.length > 0)
/*  114 */       expandCapacity(paramInt);
/*      */   }
/*      */ 
/*      */   void expandCapacity(int paramInt)
/*      */   {
/*  122 */     int i = this.value.length * 2 + 2;
/*  123 */     if (i - paramInt < 0)
/*  124 */       i = paramInt;
/*  125 */     if (i < 0) {
/*  126 */       if (paramInt < 0)
/*  127 */         throw new OutOfMemoryError();
/*  128 */       i = 2147483647;
/*      */     }
/*  130 */     this.value = Arrays.copyOf(this.value, i);
/*      */   }
/*      */ 
/*      */   public void trimToSize()
/*      */   {
/*  141 */     if (this.count < this.value.length)
/*  142 */       this.value = Arrays.copyOf(this.value, this.count);
/*      */   }
/*      */ 
/*      */   public void setLength(int paramInt)
/*      */   {
/*  172 */     if (paramInt < 0)
/*  173 */       throw new StringIndexOutOfBoundsException(paramInt);
/*  174 */     ensureCapacityInternal(paramInt);
/*      */ 
/*  176 */     if (this.count < paramInt) {
/*  177 */       for (; this.count < paramInt; this.count += 1)
/*  178 */         this.value[this.count] = '\000';
/*      */     }
/*  180 */     this.count = paramInt;
/*      */   }
/*      */ 
/*      */   public char charAt(int paramInt)
/*      */   {
/*  202 */     if ((paramInt < 0) || (paramInt >= this.count))
/*  203 */       throw new StringIndexOutOfBoundsException(paramInt);
/*  204 */     return this.value[paramInt];
/*      */   }
/*      */ 
/*      */   public int codePointAt(int paramInt)
/*      */   {
/*  229 */     if ((paramInt < 0) || (paramInt >= this.count)) {
/*  230 */       throw new StringIndexOutOfBoundsException(paramInt);
/*      */     }
/*  232 */     return Character.codePointAt(this.value, paramInt);
/*      */   }
/*      */ 
/*      */   public int codePointBefore(int paramInt)
/*      */   {
/*  257 */     int i = paramInt - 1;
/*  258 */     if ((i < 0) || (i >= this.count)) {
/*  259 */       throw new StringIndexOutOfBoundsException(paramInt);
/*      */     }
/*  261 */     return Character.codePointBefore(this.value, paramInt);
/*      */   }
/*      */ 
/*      */   public int codePointCount(int paramInt1, int paramInt2)
/*      */   {
/*  285 */     if ((paramInt1 < 0) || (paramInt2 > this.count) || (paramInt1 > paramInt2)) {
/*  286 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  288 */     return Character.codePointCountImpl(this.value, paramInt1, paramInt2 - paramInt1);
/*      */   }
/*      */ 
/*      */   public int offsetByCodePoints(int paramInt1, int paramInt2)
/*      */   {
/*  311 */     if ((paramInt1 < 0) || (paramInt1 > this.count)) {
/*  312 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  314 */     return Character.offsetByCodePointsImpl(this.value, 0, this.count, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*      */   {
/*  350 */     if (paramInt1 < 0)
/*  351 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*  352 */     if ((paramInt2 < 0) || (paramInt2 > this.count))
/*  353 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*  354 */     if (paramInt1 > paramInt2)
/*  355 */       throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
/*  356 */     System.arraycopy(this.value, paramInt1, paramArrayOfChar, paramInt3, paramInt2 - paramInt1);
/*      */   }
/*      */ 
/*      */   public void setCharAt(int paramInt, char paramChar)
/*      */   {
/*  374 */     if ((paramInt < 0) || (paramInt >= this.count))
/*  375 */       throw new StringIndexOutOfBoundsException(paramInt);
/*  376 */     this.value[paramInt] = paramChar;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(Object paramObject)
/*      */   {
/*  391 */     return append(String.valueOf(paramObject));
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(String paramString)
/*      */   {
/*  413 */     if (paramString == null) paramString = "null";
/*  414 */     int i = paramString.length();
/*  415 */     ensureCapacityInternal(this.count + i);
/*  416 */     paramString.getChars(0, i, this.value, this.count);
/*  417 */     this.count += i;
/*  418 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(StringBuffer paramStringBuffer)
/*      */   {
/*  423 */     if (paramStringBuffer == null)
/*  424 */       return append("null");
/*  425 */     int i = paramStringBuffer.length();
/*  426 */     ensureCapacityInternal(this.count + i);
/*  427 */     paramStringBuffer.getChars(0, i, this.value, this.count);
/*  428 */     this.count += i;
/*  429 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(CharSequence paramCharSequence)
/*      */   {
/*  434 */     if (paramCharSequence == null)
/*  435 */       paramCharSequence = "null";
/*  436 */     if ((paramCharSequence instanceof String))
/*  437 */       return append((String)paramCharSequence);
/*  438 */     if ((paramCharSequence instanceof StringBuffer))
/*  439 */       return append((StringBuffer)paramCharSequence);
/*  440 */     return append(paramCharSequence, 0, paramCharSequence.length());
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/*  473 */     if (paramCharSequence == null)
/*  474 */       paramCharSequence = "null";
/*  475 */     if ((paramInt1 < 0) || (paramInt1 > paramInt2) || (paramInt2 > paramCharSequence.length())) {
/*  476 */       throw new IndexOutOfBoundsException("start " + paramInt1 + ", end " + paramInt2 + ", s.length() " + paramCharSequence.length());
/*      */     }
/*      */ 
/*  479 */     int i = paramInt2 - paramInt1;
/*  480 */     ensureCapacityInternal(this.count + i);
/*  481 */     int j = paramInt1; for (int k = this.count; j < paramInt2; k++) {
/*  482 */       this.value[k] = paramCharSequence.charAt(j);
/*      */ 
/*  481 */       j++;
/*      */     }
/*  483 */     this.count += i;
/*  484 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(char[] paramArrayOfChar)
/*      */   {
/*  504 */     int i = paramArrayOfChar.length;
/*  505 */     ensureCapacityInternal(this.count + i);
/*  506 */     System.arraycopy(paramArrayOfChar, 0, this.value, this.count, i);
/*  507 */     this.count += i;
/*  508 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  534 */     if (paramInt2 > 0)
/*  535 */       ensureCapacityInternal(this.count + paramInt2);
/*  536 */     System.arraycopy(paramArrayOfChar, paramInt1, this.value, this.count, paramInt2);
/*  537 */     this.count += paramInt2;
/*  538 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(boolean paramBoolean)
/*      */   {
/*  554 */     if (paramBoolean) {
/*  555 */       ensureCapacityInternal(this.count + 4);
/*  556 */       this.value[(this.count++)] = 't';
/*  557 */       this.value[(this.count++)] = 'r';
/*  558 */       this.value[(this.count++)] = 'u';
/*  559 */       this.value[(this.count++)] = 'e';
/*      */     } else {
/*  561 */       ensureCapacityInternal(this.count + 5);
/*  562 */       this.value[(this.count++)] = 'f';
/*  563 */       this.value[(this.count++)] = 'a';
/*  564 */       this.value[(this.count++)] = 'l';
/*  565 */       this.value[(this.count++)] = 's';
/*  566 */       this.value[(this.count++)] = 'e';
/*      */     }
/*  568 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(char paramChar)
/*      */   {
/*  587 */     ensureCapacityInternal(this.count + 1);
/*  588 */     this.value[(this.count++)] = paramChar;
/*  589 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(int paramInt)
/*      */   {
/*  605 */     if (paramInt == -2147483648) {
/*  606 */       append("-2147483648");
/*  607 */       return this;
/*      */     }
/*  609 */     int i = paramInt < 0 ? Integer.stringSize(-paramInt) + 1 : Integer.stringSize(paramInt);
/*      */ 
/*  611 */     int j = this.count + i;
/*  612 */     ensureCapacityInternal(j);
/*  613 */     Integer.getChars(paramInt, j, this.value);
/*  614 */     this.count = j;
/*  615 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(long paramLong)
/*      */   {
/*  631 */     if (paramLong == -9223372036854775808L) {
/*  632 */       append("-9223372036854775808");
/*  633 */       return this;
/*      */     }
/*  635 */     int i = paramLong < 0L ? Long.stringSize(-paramLong) + 1 : Long.stringSize(paramLong);
/*      */ 
/*  637 */     int j = this.count + i;
/*  638 */     ensureCapacityInternal(j);
/*  639 */     Long.getChars(paramLong, j, this.value);
/*  640 */     this.count = j;
/*  641 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(float paramFloat)
/*      */   {
/*  657 */     new FloatingDecimal(paramFloat).appendTo(this);
/*  658 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder append(double paramDouble)
/*      */   {
/*  674 */     new FloatingDecimal(paramDouble).appendTo(this);
/*  675 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder delete(int paramInt1, int paramInt2)
/*      */   {
/*  693 */     if (paramInt1 < 0)
/*  694 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*  695 */     if (paramInt2 > this.count)
/*  696 */       paramInt2 = this.count;
/*  697 */     if (paramInt1 > paramInt2)
/*  698 */       throw new StringIndexOutOfBoundsException();
/*  699 */     int i = paramInt2 - paramInt1;
/*  700 */     if (i > 0) {
/*  701 */       System.arraycopy(this.value, paramInt1 + i, this.value, paramInt1, this.count - paramInt2);
/*  702 */       this.count -= i;
/*      */     }
/*  704 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder appendCodePoint(int paramInt)
/*      */   {
/*  727 */     int i = this.count;
/*      */ 
/*  729 */     if (Character.isBmpCodePoint(paramInt)) {
/*  730 */       ensureCapacityInternal(i + 1);
/*  731 */       this.value[i] = ((char)paramInt);
/*  732 */       this.count = (i + 1);
/*  733 */     } else if (Character.isValidCodePoint(paramInt)) {
/*  734 */       ensureCapacityInternal(i + 2);
/*  735 */       Character.toSurrogates(paramInt, this.value, i);
/*  736 */       this.count = (i + 2);
/*      */     } else {
/*  738 */       throw new IllegalArgumentException();
/*      */     }
/*  740 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder deleteCharAt(int paramInt)
/*      */   {
/*  761 */     if ((paramInt < 0) || (paramInt >= this.count))
/*  762 */       throw new StringIndexOutOfBoundsException(paramInt);
/*  763 */     System.arraycopy(this.value, paramInt + 1, this.value, paramInt, this.count - paramInt - 1);
/*  764 */     this.count -= 1;
/*  765 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder replace(int paramInt1, int paramInt2, String paramString)
/*      */   {
/*  788 */     if (paramInt1 < 0)
/*  789 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*  790 */     if (paramInt1 > this.count)
/*  791 */       throw new StringIndexOutOfBoundsException("start > length()");
/*  792 */     if (paramInt1 > paramInt2) {
/*  793 */       throw new StringIndexOutOfBoundsException("start > end");
/*      */     }
/*  795 */     if (paramInt2 > this.count)
/*  796 */       paramInt2 = this.count;
/*  797 */     int i = paramString.length();
/*  798 */     int j = this.count + i - (paramInt2 - paramInt1);
/*  799 */     ensureCapacityInternal(j);
/*      */ 
/*  801 */     System.arraycopy(this.value, paramInt2, this.value, paramInt1 + i, this.count - paramInt2);
/*  802 */     paramString.getChars(this.value, paramInt1);
/*  803 */     this.count = j;
/*  804 */     return this;
/*      */   }
/*      */ 
/*      */   public String substring(int paramInt)
/*      */   {
/*  819 */     return substring(paramInt, this.count);
/*      */   }
/*      */ 
/*      */   public CharSequence subSequence(int paramInt1, int paramInt2)
/*      */   {
/*  849 */     return substring(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public String substring(int paramInt1, int paramInt2)
/*      */   {
/*  867 */     if (paramInt1 < 0)
/*  868 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*  869 */     if (paramInt2 > this.count)
/*  870 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*  871 */     if (paramInt1 > paramInt2)
/*  872 */       throw new StringIndexOutOfBoundsException(paramInt2 - paramInt1);
/*  873 */     return new String(this.value, paramInt1, paramInt2 - paramInt1);
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
/*      */   {
/*  900 */     if ((paramInt1 < 0) || (paramInt1 > length()))
/*  901 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*  902 */     if ((paramInt2 < 0) || (paramInt3 < 0) || (paramInt2 > paramArrayOfChar.length - paramInt3)) {
/*  903 */       throw new StringIndexOutOfBoundsException("offset " + paramInt2 + ", len " + paramInt3 + ", str.length " + paramArrayOfChar.length);
/*      */     }
/*      */ 
/*  906 */     ensureCapacityInternal(this.count + paramInt3);
/*  907 */     System.arraycopy(this.value, paramInt1, this.value, paramInt1 + paramInt3, this.count - paramInt1);
/*  908 */     System.arraycopy(paramArrayOfChar, paramInt2, this.value, paramInt1, paramInt3);
/*  909 */     this.count += paramInt3;
/*  910 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, Object paramObject)
/*      */   {
/*  933 */     return insert(paramInt, String.valueOf(paramObject));
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, String paramString)
/*      */   {
/*  968 */     if ((paramInt < 0) || (paramInt > length()))
/*  969 */       throw new StringIndexOutOfBoundsException(paramInt);
/*  970 */     if (paramString == null)
/*  971 */       paramString = "null";
/*  972 */     int i = paramString.length();
/*  973 */     ensureCapacityInternal(this.count + i);
/*  974 */     System.arraycopy(this.value, paramInt, this.value, paramInt + i, this.count - paramInt);
/*  975 */     paramString.getChars(this.value, paramInt);
/*  976 */     this.count += i;
/*  977 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, char[] paramArrayOfChar)
/*      */   {
/* 1005 */     if ((paramInt < 0) || (paramInt > length()))
/* 1006 */       throw new StringIndexOutOfBoundsException(paramInt);
/* 1007 */     int i = paramArrayOfChar.length;
/* 1008 */     ensureCapacityInternal(this.count + i);
/* 1009 */     System.arraycopy(this.value, paramInt, this.value, paramInt + i, this.count - paramInt);
/* 1010 */     System.arraycopy(paramArrayOfChar, 0, this.value, paramInt, i);
/* 1011 */     this.count += i;
/* 1012 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, CharSequence paramCharSequence)
/*      */   {
/* 1037 */     if (paramCharSequence == null)
/* 1038 */       paramCharSequence = "null";
/* 1039 */     if ((paramCharSequence instanceof String))
/* 1040 */       return insert(paramInt, (String)paramCharSequence);
/* 1041 */     return insert(paramInt, paramCharSequence, 0, paramCharSequence.length());
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3)
/*      */   {
/* 1090 */     if (paramCharSequence == null)
/* 1091 */       paramCharSequence = "null";
/* 1092 */     if ((paramInt1 < 0) || (paramInt1 > length()))
/* 1093 */       throw new IndexOutOfBoundsException("dstOffset " + paramInt1);
/* 1094 */     if ((paramInt2 < 0) || (paramInt3 < 0) || (paramInt2 > paramInt3) || (paramInt3 > paramCharSequence.length())) {
/* 1095 */       throw new IndexOutOfBoundsException("start " + paramInt2 + ", end " + paramInt3 + ", s.length() " + paramCharSequence.length());
/*      */     }
/*      */ 
/* 1098 */     int i = paramInt3 - paramInt2;
/* 1099 */     ensureCapacityInternal(this.count + i);
/* 1100 */     System.arraycopy(this.value, paramInt1, this.value, paramInt1 + i, this.count - paramInt1);
/*      */ 
/* 1102 */     for (int j = paramInt2; j < paramInt3; j++)
/* 1103 */       this.value[(paramInt1++)] = paramCharSequence.charAt(j);
/* 1104 */     this.count += i;
/* 1105 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, boolean paramBoolean)
/*      */   {
/* 1128 */     return insert(paramInt, String.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, char paramChar)
/*      */   {
/* 1151 */     ensureCapacityInternal(this.count + 1);
/* 1152 */     System.arraycopy(this.value, paramInt, this.value, paramInt + 1, this.count - paramInt);
/* 1153 */     this.value[paramInt] = paramChar;
/* 1154 */     this.count += 1;
/* 1155 */     return this;
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt1, int paramInt2)
/*      */   {
/* 1178 */     return insert(paramInt1, String.valueOf(paramInt2));
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, long paramLong)
/*      */   {
/* 1201 */     return insert(paramInt, String.valueOf(paramLong));
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, float paramFloat)
/*      */   {
/* 1224 */     return insert(paramInt, String.valueOf(paramFloat));
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder insert(int paramInt, double paramDouble)
/*      */   {
/* 1247 */     return insert(paramInt, String.valueOf(paramDouble));
/*      */   }
/*      */ 
/*      */   public int indexOf(String paramString)
/*      */   {
/* 1268 */     return indexOf(paramString, 0);
/*      */   }
/*      */ 
/*      */   public int indexOf(String paramString, int paramInt)
/*      */   {
/* 1289 */     return String.indexOf(this.value, 0, this.count, paramString.toCharArray(), 0, paramString.length(), paramInt);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(String paramString)
/*      */   {
/* 1312 */     return lastIndexOf(paramString, this.count);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(String paramString, int paramInt)
/*      */   {
/* 1333 */     return String.lastIndexOf(this.value, 0, this.count, paramString.toCharArray(), 0, paramString.length(), paramInt);
/*      */   }
/*      */ 
/*      */   public AbstractStringBuilder reverse()
/*      */   {
/* 1360 */     int i = 0;
/* 1361 */     int j = this.count - 1;
/*      */     int m;
/*      */     int n;
/* 1362 */     for (int k = j - 1 >> 1; k >= 0; k--) {
/* 1363 */       m = this.value[k];
/* 1364 */       n = this.value[(j - k)];
/* 1365 */       if (i == 0) {
/* 1366 */         i = ((m >= 55296) && (m <= 57343)) || ((n >= 55296) && (n <= 57343)) ? 1 : 0;
/*      */       }
/*      */ 
/* 1369 */       this.value[k] = n;
/* 1370 */       this.value[(j - k)] = m;
/*      */     }
/* 1372 */     if (i != 0)
/*      */     {
/* 1374 */       for (k = 0; k < this.count - 1; k++) {
/* 1375 */         m = this.value[k];
/* 1376 */         if (Character.isLowSurrogate(m)) {
/* 1377 */           n = this.value[(k + 1)];
/* 1378 */           if (Character.isHighSurrogate(n)) {
/* 1379 */             this.value[(k++)] = n;
/* 1380 */             this.value[k] = m;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1385 */     return this;
/*      */   }
/*      */ 
/*      */   public abstract String toString();
/*      */ 
/*      */   final char[] getValue()
/*      */   {
/* 1404 */     return this.value;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.AbstractStringBuilder
 * JD-Core Version:    0.6.2
 */