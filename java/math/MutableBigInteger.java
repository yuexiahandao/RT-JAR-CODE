/*      */ package java.math;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ 
/*      */ class MutableBigInteger
/*      */ {
/*      */   int[] value;
/*      */   int intLen;
/*   68 */   int offset = 0;
/*      */ 
/*   76 */   static final MutableBigInteger ONE = new MutableBigInteger(1);
/*      */ 
/*      */   MutableBigInteger()
/*      */   {
/*   85 */     this.value = new int[1];
/*   86 */     this.intLen = 0;
/*      */   }
/*      */ 
/*      */   MutableBigInteger(int paramInt)
/*      */   {
/*   94 */     this.value = new int[1];
/*   95 */     this.intLen = 1;
/*   96 */     this.value[0] = paramInt;
/*      */   }
/*      */ 
/*      */   MutableBigInteger(int[] paramArrayOfInt)
/*      */   {
/*  104 */     this.value = paramArrayOfInt;
/*  105 */     this.intLen = paramArrayOfInt.length;
/*      */   }
/*      */ 
/*      */   MutableBigInteger(BigInteger paramBigInteger)
/*      */   {
/*  113 */     this.intLen = paramBigInteger.mag.length;
/*  114 */     this.value = Arrays.copyOf(paramBigInteger.mag, this.intLen);
/*      */   }
/*      */ 
/*      */   MutableBigInteger(MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  122 */     this.intLen = paramMutableBigInteger.intLen;
/*  123 */     this.value = Arrays.copyOfRange(paramMutableBigInteger.value, paramMutableBigInteger.offset, paramMutableBigInteger.offset + this.intLen);
/*      */   }
/*      */ 
/*      */   private int[] getMagnitudeArray()
/*      */   {
/*  131 */     if ((this.offset > 0) || (this.value.length != this.intLen))
/*  132 */       return Arrays.copyOfRange(this.value, this.offset, this.offset + this.intLen);
/*  133 */     return this.value;
/*      */   }
/*      */ 
/*      */   private long toLong()
/*      */   {
/*  141 */     assert (this.intLen <= 2) : "this MutableBigInteger exceeds the range of long";
/*  142 */     if (this.intLen == 0)
/*  143 */       return 0L;
/*  144 */     long l = this.value[this.offset] & 0xFFFFFFFF;
/*  145 */     return this.intLen == 2 ? l << 32 | this.value[(this.offset + 1)] & 0xFFFFFFFF : l;
/*      */   }
/*      */ 
/*      */   BigInteger toBigInteger(int paramInt)
/*      */   {
/*  152 */     if ((this.intLen == 0) || (paramInt == 0))
/*  153 */       return BigInteger.ZERO;
/*  154 */     return new BigInteger(getMagnitudeArray(), paramInt);
/*      */   }
/*      */ 
/*      */   BigDecimal toBigDecimal(int paramInt1, int paramInt2)
/*      */   {
/*  162 */     if ((this.intLen == 0) || (paramInt1 == 0))
/*  163 */       return BigDecimal.valueOf(0L, paramInt2);
/*  164 */     int[] arrayOfInt = getMagnitudeArray();
/*  165 */     int i = arrayOfInt.length;
/*  166 */     int j = arrayOfInt[0];
/*      */ 
/*  169 */     if ((i > 2) || ((j < 0) && (i == 2)))
/*  170 */       return new BigDecimal(new BigInteger(arrayOfInt, paramInt1), -9223372036854775808L, paramInt2, 0);
/*  171 */     long l = i == 2 ? arrayOfInt[1] & 0xFFFFFFFF | (j & 0xFFFFFFFF) << 32 : j & 0xFFFFFFFF;
/*      */ 
/*  174 */     return new BigDecimal(null, paramInt1 == -1 ? -l : l, paramInt2, 0);
/*      */   }
/*      */ 
/*      */   void clear()
/*      */   {
/*  181 */     this.offset = (this.intLen = 0);
/*  182 */     int i = 0; for (int j = this.value.length; i < j; i++)
/*  183 */       this.value[i] = 0;
/*      */   }
/*      */ 
/*      */   void reset()
/*      */   {
/*  190 */     this.offset = (this.intLen = 0);
/*      */   }
/*      */ 
/*      */   final int compare(MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  199 */     int i = paramMutableBigInteger.intLen;
/*  200 */     if (this.intLen < i)
/*  201 */       return -1;
/*  202 */     if (this.intLen > i) {
/*  203 */       return 1;
/*      */     }
/*      */ 
/*  207 */     int[] arrayOfInt = paramMutableBigInteger.value;
/*  208 */     int j = this.offset; for (int k = paramMutableBigInteger.offset; j < this.intLen + this.offset; k++) {
/*  209 */       int m = this.value[j] + -2147483648;
/*  210 */       int n = arrayOfInt[k] + -2147483648;
/*  211 */       if (m < n)
/*  212 */         return -1;
/*  213 */       if (m > n)
/*  214 */         return 1;
/*  208 */       j++;
/*      */     }
/*      */ 
/*  216 */     return 0;
/*      */   }
/*      */ 
/*      */   final int compareHalf(MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  226 */     int i = paramMutableBigInteger.intLen;
/*  227 */     int j = this.intLen;
/*  228 */     if (j <= 0)
/*  229 */       return i <= 0 ? 0 : -1;
/*  230 */     if (j > i)
/*  231 */       return 1;
/*  232 */     if (j < i - 1)
/*  233 */       return -1;
/*  234 */     int[] arrayOfInt1 = paramMutableBigInteger.value;
/*  235 */     int k = 0;
/*  236 */     int m = 0;
/*      */ 
/*  238 */     if (j != i) {
/*  239 */       if (arrayOfInt1[k] == 1) {
/*  240 */         k++;
/*  241 */         m = -2147483648;
/*      */       } else {
/*  243 */         return -1;
/*      */       }
/*      */     }
/*      */ 
/*  247 */     int[] arrayOfInt2 = this.value;
/*  248 */     int n = this.offset; for (int i1 = k; n < j + this.offset; ) {
/*  249 */       int i2 = arrayOfInt1[(i1++)];
/*  250 */       long l1 = (i2 >>> 1) + m & 0xFFFFFFFF;
/*  251 */       long l2 = arrayOfInt2[(n++)] & 0xFFFFFFFF;
/*  252 */       if (l2 != l1)
/*  253 */         return l2 < l1 ? -1 : 1;
/*  254 */       m = (i2 & 0x1) << 31;
/*      */     }
/*  256 */     return m == 0 ? 0 : -1;
/*      */   }
/*      */ 
/*      */   private final int getLowestSetBit()
/*      */   {
/*  264 */     if (this.intLen == 0) {
/*  265 */       return -1;
/*      */     }
/*  267 */     for (int i = this.intLen - 1; (i > 0) && (this.value[(i + this.offset)] == 0); i--);
/*  269 */     int j = this.value[(i + this.offset)];
/*  270 */     if (j == 0)
/*  271 */       return -1;
/*  272 */     return (this.intLen - 1 - i << 5) + Integer.numberOfTrailingZeros(j);
/*      */   }
/*      */ 
/*      */   private final int getInt(int paramInt)
/*      */   {
/*  281 */     return this.value[(this.offset + paramInt)];
/*      */   }
/*      */ 
/*      */   private final long getLong(int paramInt)
/*      */   {
/*  290 */     return this.value[(this.offset + paramInt)] & 0xFFFFFFFF;
/*      */   }
/*      */ 
/*      */   final void normalize()
/*      */   {
/*  299 */     if (this.intLen == 0) {
/*  300 */       this.offset = 0;
/*  301 */       return;
/*      */     }
/*      */ 
/*  304 */     int i = this.offset;
/*  305 */     if (this.value[i] != 0) {
/*  306 */       return;
/*      */     }
/*  308 */     int j = i + this.intLen;
/*      */     do
/*  310 */       i++;
/*  311 */     while ((i < j) && (this.value[i] == 0));
/*      */ 
/*  313 */     int k = i - this.offset;
/*  314 */     this.intLen -= k;
/*  315 */     this.offset = (this.intLen == 0 ? 0 : this.offset + k);
/*      */   }
/*      */ 
/*      */   private final void ensureCapacity(int paramInt)
/*      */   {
/*  323 */     if (this.value.length < paramInt) {
/*  324 */       this.value = new int[paramInt];
/*  325 */       this.offset = 0;
/*  326 */       this.intLen = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   int[] toIntArray()
/*      */   {
/*  335 */     int[] arrayOfInt = new int[this.intLen];
/*  336 */     for (int i = 0; i < this.intLen; i++)
/*  337 */       arrayOfInt[i] = this.value[(this.offset + i)];
/*  338 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   void setInt(int paramInt1, int paramInt2)
/*      */   {
/*  347 */     this.value[(this.offset + paramInt1)] = paramInt2;
/*      */   }
/*      */ 
/*      */   void setValue(int[] paramArrayOfInt, int paramInt)
/*      */   {
/*  355 */     this.value = paramArrayOfInt;
/*  356 */     this.intLen = paramInt;
/*  357 */     this.offset = 0;
/*      */   }
/*      */ 
/*      */   void copyValue(MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  365 */     int i = paramMutableBigInteger.intLen;
/*  366 */     if (this.value.length < i)
/*  367 */       this.value = new int[i];
/*  368 */     System.arraycopy(paramMutableBigInteger.value, paramMutableBigInteger.offset, this.value, 0, i);
/*  369 */     this.intLen = i;
/*  370 */     this.offset = 0;
/*      */   }
/*      */ 
/*      */   void copyValue(int[] paramArrayOfInt)
/*      */   {
/*  378 */     int i = paramArrayOfInt.length;
/*  379 */     if (this.value.length < i)
/*  380 */       this.value = new int[i];
/*  381 */     System.arraycopy(paramArrayOfInt, 0, this.value, 0, i);
/*  382 */     this.intLen = i;
/*  383 */     this.offset = 0;
/*      */   }
/*      */ 
/*      */   boolean isOne()
/*      */   {
/*  390 */     return (this.intLen == 1) && (this.value[this.offset] == 1);
/*      */   }
/*      */ 
/*      */   boolean isZero()
/*      */   {
/*  397 */     return this.intLen == 0;
/*      */   }
/*      */ 
/*      */   boolean isEven()
/*      */   {
/*  404 */     return (this.intLen == 0) || ((this.value[(this.offset + this.intLen - 1)] & 0x1) == 0);
/*      */   }
/*      */ 
/*      */   boolean isOdd()
/*      */   {
/*  411 */     return !isZero();
/*      */   }
/*      */ 
/*      */   boolean isNormal()
/*      */   {
/*  420 */     if (this.intLen + this.offset > this.value.length)
/*  421 */       return false;
/*  422 */     if (this.intLen == 0)
/*  423 */       return true;
/*  424 */     return this.value[this.offset] != 0;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  431 */     BigInteger localBigInteger = toBigInteger(1);
/*  432 */     return localBigInteger.toString();
/*      */   }
/*      */ 
/*      */   void rightShift(int paramInt)
/*      */   {
/*  440 */     if (this.intLen == 0)
/*  441 */       return;
/*  442 */     int i = paramInt >>> 5;
/*  443 */     int j = paramInt & 0x1F;
/*  444 */     this.intLen -= i;
/*  445 */     if (j == 0)
/*  446 */       return;
/*  447 */     int k = BigInteger.bitLengthForInt(this.value[this.offset]);
/*  448 */     if (j >= k) {
/*  449 */       primitiveLeftShift(32 - j);
/*  450 */       this.intLen -= 1;
/*      */     } else {
/*  452 */       primitiveRightShift(j);
/*      */     }
/*      */   }
/*      */ 
/*      */   void leftShift(int paramInt)
/*      */   {
/*  466 */     if (this.intLen == 0)
/*  467 */       return;
/*  468 */     int i = paramInt >>> 5;
/*  469 */     int j = paramInt & 0x1F;
/*  470 */     int k = BigInteger.bitLengthForInt(this.value[this.offset]);
/*      */ 
/*  473 */     if (paramInt <= 32 - k) {
/*  474 */       primitiveLeftShift(j);
/*  475 */       return;
/*      */     }
/*      */ 
/*  478 */     int m = this.intLen + i + 1;
/*  479 */     if (j <= 32 - k)
/*  480 */       m--;
/*  481 */     if (this.value.length < m)
/*      */     {
/*  483 */       int[] arrayOfInt = new int[m];
/*  484 */       for (int i1 = 0; i1 < this.intLen; i1++)
/*  485 */         arrayOfInt[i1] = this.value[(this.offset + i1)];
/*  486 */       setValue(arrayOfInt, m);
/*      */     }
/*      */     else
/*      */     {
/*      */       int n;
/*  487 */       if (this.value.length - this.offset >= m)
/*      */       {
/*  489 */         for (n = 0; n < m - this.intLen; n++)
/*  490 */           this.value[(this.offset + this.intLen + n)] = 0;
/*      */       }
/*      */       else {
/*  493 */         for (n = 0; n < this.intLen; n++)
/*  494 */           this.value[n] = this.value[(this.offset + n)];
/*  495 */         for (n = this.intLen; n < m; n++)
/*  496 */           this.value[n] = 0;
/*  497 */         this.offset = 0;
/*      */       }
/*      */     }
/*  499 */     this.intLen = m;
/*  500 */     if (j == 0)
/*  501 */       return;
/*  502 */     if (j <= 32 - k)
/*  503 */       primitiveLeftShift(j);
/*      */     else
/*  505 */       primitiveRightShift(32 - j);
/*      */   }
/*      */ 
/*      */   private int divadd(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  514 */     long l1 = 0L;
/*      */ 
/*  516 */     for (int i = paramArrayOfInt1.length - 1; i >= 0; i--) {
/*  517 */       long l2 = (paramArrayOfInt1[i] & 0xFFFFFFFF) + (paramArrayOfInt2[(i + paramInt)] & 0xFFFFFFFF) + l1;
/*      */ 
/*  519 */       paramArrayOfInt2[(i + paramInt)] = ((int)l2);
/*  520 */       l1 = l2 >>> 32;
/*      */     }
/*  522 */     return (int)l1;
/*      */   }
/*      */ 
/*      */   private int mulsub(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  531 */     long l1 = paramInt1 & 0xFFFFFFFF;
/*  532 */     long l2 = 0L;
/*  533 */     paramInt3 += paramInt2;
/*      */ 
/*  535 */     for (int i = paramInt2 - 1; i >= 0; i--) {
/*  536 */       long l3 = (paramArrayOfInt2[i] & 0xFFFFFFFF) * l1 + l2;
/*  537 */       long l4 = paramArrayOfInt1[paramInt3] - l3;
/*  538 */       paramArrayOfInt1[(paramInt3--)] = ((int)l4);
/*  539 */       l2 = (l3 >>> 32) + ((l4 & 0xFFFFFFFF) > (((int)l3 ^ 0xFFFFFFFF) & 0xFFFFFFFF) ? 1 : 0);
/*      */     }
/*      */ 
/*  543 */     return (int)l2;
/*      */   }
/*      */ 
/*      */   private final void primitiveRightShift(int paramInt)
/*      */   {
/*  552 */     int[] arrayOfInt = this.value;
/*  553 */     int i = 32 - paramInt;
/*  554 */     int j = this.offset + this.intLen - 1; for (int k = arrayOfInt[j]; j > this.offset; j--) {
/*  555 */       int m = k;
/*  556 */       k = arrayOfInt[(j - 1)];
/*  557 */       arrayOfInt[j] = (k << i | m >>> paramInt);
/*      */     }
/*  559 */     arrayOfInt[this.offset] >>>= paramInt;
/*      */   }
/*      */ 
/*      */   private final void primitiveLeftShift(int paramInt)
/*      */   {
/*  568 */     int[] arrayOfInt = this.value;
/*  569 */     int i = 32 - paramInt;
/*  570 */     int j = this.offset; int k = arrayOfInt[j]; for (int m = j + this.intLen - 1; j < m; j++) {
/*  571 */       int n = k;
/*  572 */       k = arrayOfInt[(j + 1)];
/*  573 */       arrayOfInt[j] = (n << paramInt | k >>> i);
/*      */     }
/*  575 */     arrayOfInt[(this.offset + this.intLen - 1)] <<= paramInt;
/*      */   }
/*      */ 
/*      */   void add(MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  584 */     int i = this.intLen;
/*  585 */     int j = paramMutableBigInteger.intLen;
/*  586 */     int k = this.intLen > paramMutableBigInteger.intLen ? this.intLen : paramMutableBigInteger.intLen;
/*  587 */     Object localObject = this.value.length < k ? new int[k] : this.value;
/*      */ 
/*  589 */     int m = localObject.length - 1;
/*      */ 
/*  591 */     long l2 = 0L;
/*      */     long l1;
/*  594 */     while ((i > 0) && (j > 0)) {
/*  595 */       i--; j--;
/*  596 */       l1 = (this.value[(i + this.offset)] & 0xFFFFFFFF) + (paramMutableBigInteger.value[(j + paramMutableBigInteger.offset)] & 0xFFFFFFFF) + l2;
/*      */ 
/*  598 */       localObject[(m--)] = ((int)l1);
/*  599 */       l2 = l1 >>> 32;
/*      */     }
/*      */ 
/*  603 */     while (i > 0) {
/*  604 */       i--;
/*  605 */       if ((l2 == 0L) && (localObject == this.value) && (m == i + this.offset))
/*  606 */         return;
/*  607 */       l1 = (this.value[(i + this.offset)] & 0xFFFFFFFF) + l2;
/*  608 */       localObject[(m--)] = ((int)l1);
/*  609 */       l2 = l1 >>> 32;
/*      */     }
/*  611 */     while (j > 0) {
/*  612 */       j--;
/*  613 */       l1 = (paramMutableBigInteger.value[(j + paramMutableBigInteger.offset)] & 0xFFFFFFFF) + l2;
/*  614 */       localObject[(m--)] = ((int)l1);
/*  615 */       l2 = l1 >>> 32;
/*      */     }
/*      */ 
/*  618 */     if (l2 > 0L) {
/*  619 */       k++;
/*  620 */       if (localObject.length < k) {
/*  621 */         int[] arrayOfInt = new int[k];
/*      */ 
/*  624 */         System.arraycopy(localObject, 0, arrayOfInt, 1, localObject.length);
/*  625 */         arrayOfInt[0] = 1;
/*  626 */         localObject = arrayOfInt;
/*      */       } else {
/*  628 */         localObject[(m--)] = 1;
/*      */       }
/*      */     }
/*      */ 
/*  632 */     this.value = ((int[])localObject);
/*  633 */     this.intLen = k;
/*  634 */     this.offset = (localObject.length - k);
/*      */   }
/*      */ 
/*      */   int subtract(MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  643 */     MutableBigInteger localMutableBigInteger1 = this;
/*      */ 
/*  645 */     int[] arrayOfInt = this.value;
/*  646 */     int i = localMutableBigInteger1.compare(paramMutableBigInteger);
/*      */ 
/*  648 */     if (i == 0) {
/*  649 */       reset();
/*  650 */       return 0;
/*      */     }
/*  652 */     if (i < 0) {
/*  653 */       MutableBigInteger localMutableBigInteger2 = localMutableBigInteger1;
/*  654 */       localMutableBigInteger1 = paramMutableBigInteger;
/*  655 */       paramMutableBigInteger = localMutableBigInteger2;
/*      */     }
/*      */ 
/*  658 */     int j = localMutableBigInteger1.intLen;
/*  659 */     if (arrayOfInt.length < j) {
/*  660 */       arrayOfInt = new int[j];
/*      */     }
/*  662 */     long l = 0L;
/*  663 */     int k = localMutableBigInteger1.intLen;
/*  664 */     int m = paramMutableBigInteger.intLen;
/*  665 */     int n = arrayOfInt.length - 1;
/*      */ 
/*  668 */     while (m > 0) {
/*  669 */       k--; m--;
/*      */ 
/*  671 */       l = (localMutableBigInteger1.value[(k + localMutableBigInteger1.offset)] & 0xFFFFFFFF) - (paramMutableBigInteger.value[(m + paramMutableBigInteger.offset)] & 0xFFFFFFFF) - (int)-(l >> 32);
/*      */ 
/*  673 */       arrayOfInt[(n--)] = ((int)l);
/*      */     }
/*      */ 
/*  676 */     while (k > 0) {
/*  677 */       k--;
/*  678 */       l = (localMutableBigInteger1.value[(k + localMutableBigInteger1.offset)] & 0xFFFFFFFF) - (int)-(l >> 32);
/*  679 */       arrayOfInt[(n--)] = ((int)l);
/*      */     }
/*      */ 
/*  682 */     this.value = arrayOfInt;
/*  683 */     this.intLen = j;
/*  684 */     this.offset = (this.value.length - j);
/*  685 */     normalize();
/*  686 */     return i;
/*      */   }
/*      */ 
/*      */   private int difference(MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  695 */     MutableBigInteger localMutableBigInteger1 = this;
/*  696 */     int i = localMutableBigInteger1.compare(paramMutableBigInteger);
/*  697 */     if (i == 0)
/*  698 */       return 0;
/*  699 */     if (i < 0) {
/*  700 */       MutableBigInteger localMutableBigInteger2 = localMutableBigInteger1;
/*  701 */       localMutableBigInteger1 = paramMutableBigInteger;
/*  702 */       paramMutableBigInteger = localMutableBigInteger2;
/*      */     }
/*      */ 
/*  705 */     long l = 0L;
/*  706 */     int j = localMutableBigInteger1.intLen;
/*  707 */     int k = paramMutableBigInteger.intLen;
/*      */ 
/*  710 */     while (k > 0) {
/*  711 */       j--; k--;
/*  712 */       l = (localMutableBigInteger1.value[(localMutableBigInteger1.offset + j)] & 0xFFFFFFFF) - (paramMutableBigInteger.value[(paramMutableBigInteger.offset + k)] & 0xFFFFFFFF) - (int)-(l >> 32);
/*      */ 
/*  714 */       localMutableBigInteger1.value[(localMutableBigInteger1.offset + j)] = ((int)l);
/*      */     }
/*      */ 
/*  717 */     while (j > 0) {
/*  718 */       j--;
/*  719 */       l = (localMutableBigInteger1.value[(localMutableBigInteger1.offset + j)] & 0xFFFFFFFF) - (int)-(l >> 32);
/*  720 */       localMutableBigInteger1.value[(localMutableBigInteger1.offset + j)] = ((int)l);
/*      */     }
/*      */ 
/*  723 */     localMutableBigInteger1.normalize();
/*  724 */     return i;
/*      */   }
/*      */ 
/*      */   void multiply(MutableBigInteger paramMutableBigInteger1, MutableBigInteger paramMutableBigInteger2)
/*      */   {
/*  732 */     int i = this.intLen;
/*  733 */     int j = paramMutableBigInteger1.intLen;
/*  734 */     int k = i + j;
/*      */ 
/*  737 */     if (paramMutableBigInteger2.value.length < k)
/*  738 */       paramMutableBigInteger2.value = new int[k];
/*  739 */     paramMutableBigInteger2.offset = 0;
/*  740 */     paramMutableBigInteger2.intLen = k;
/*      */ 
/*  743 */     long l1 = 0L;
/*  744 */     int m = j - 1; for (int n = j + i - 1; m >= 0; n--) {
/*  745 */       long l2 = (paramMutableBigInteger1.value[(m + paramMutableBigInteger1.offset)] & 0xFFFFFFFF) * (this.value[(i - 1 + this.offset)] & 0xFFFFFFFF) + l1;
/*      */ 
/*  747 */       paramMutableBigInteger2.value[n] = ((int)l2);
/*  748 */       l1 = l2 >>> 32;
/*      */ 
/*  744 */       m--;
/*      */     }
/*      */ 
/*  750 */     paramMutableBigInteger2.value[(i - 1)] = ((int)l1);
/*      */ 
/*  753 */     for (m = i - 2; m >= 0; m--) {
/*  754 */       l1 = 0L;
/*  755 */       n = j - 1; for (int i1 = j + m; n >= 0; i1--) {
/*  756 */         long l3 = (paramMutableBigInteger1.value[(n + paramMutableBigInteger1.offset)] & 0xFFFFFFFF) * (this.value[(m + this.offset)] & 0xFFFFFFFF) + (paramMutableBigInteger2.value[i1] & 0xFFFFFFFF) + l1;
/*      */ 
/*  759 */         paramMutableBigInteger2.value[i1] = ((int)l3);
/*  760 */         l1 = l3 >>> 32;
/*      */ 
/*  755 */         n--;
/*      */       }
/*      */ 
/*  762 */       paramMutableBigInteger2.value[m] = ((int)l1);
/*      */     }
/*      */ 
/*  766 */     paramMutableBigInteger2.normalize();
/*      */   }
/*      */ 
/*      */   void mul(int paramInt, MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  774 */     if (paramInt == 1) {
/*  775 */       paramMutableBigInteger.copyValue(this);
/*  776 */       return;
/*      */     }
/*      */ 
/*  779 */     if (paramInt == 0) {
/*  780 */       paramMutableBigInteger.clear();
/*  781 */       return;
/*      */     }
/*      */ 
/*  785 */     long l1 = paramInt & 0xFFFFFFFF;
/*  786 */     int[] arrayOfInt = paramMutableBigInteger.value.length < this.intLen + 1 ? new int[this.intLen + 1] : paramMutableBigInteger.value;
/*      */ 
/*  788 */     long l2 = 0L;
/*  789 */     for (int i = this.intLen - 1; i >= 0; i--) {
/*  790 */       long l3 = l1 * (this.value[(i + this.offset)] & 0xFFFFFFFF) + l2;
/*  791 */       arrayOfInt[(i + 1)] = ((int)l3);
/*  792 */       l2 = l3 >>> 32;
/*      */     }
/*      */ 
/*  795 */     if (l2 == 0L) {
/*  796 */       paramMutableBigInteger.offset = 1;
/*  797 */       paramMutableBigInteger.intLen = this.intLen;
/*      */     } else {
/*  799 */       paramMutableBigInteger.offset = 0;
/*  800 */       this.intLen += 1;
/*  801 */       arrayOfInt[0] = ((int)l2);
/*      */     }
/*  803 */     paramMutableBigInteger.value = arrayOfInt;
/*      */   }
/*      */ 
/*      */   int divideOneWord(int paramInt, MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  815 */     long l1 = paramInt & 0xFFFFFFFF;
/*      */ 
/*  818 */     if (this.intLen == 1) {
/*  819 */       long l2 = this.value[this.offset] & 0xFFFFFFFF;
/*  820 */       int k = (int)(l2 / l1);
/*  821 */       int m = (int)(l2 - k * l1);
/*  822 */       paramMutableBigInteger.value[0] = k;
/*  823 */       paramMutableBigInteger.intLen = (k == 0 ? 0 : 1);
/*  824 */       paramMutableBigInteger.offset = 0;
/*  825 */       return m;
/*      */     }
/*      */ 
/*  828 */     if (paramMutableBigInteger.value.length < this.intLen)
/*  829 */       paramMutableBigInteger.value = new int[this.intLen];
/*  830 */     paramMutableBigInteger.offset = 0;
/*  831 */     paramMutableBigInteger.intLen = this.intLen;
/*      */ 
/*  834 */     int i = Integer.numberOfLeadingZeros(paramInt);
/*      */ 
/*  836 */     int j = this.value[this.offset];
/*  837 */     long l3 = j & 0xFFFFFFFF;
/*  838 */     if (l3 < l1) {
/*  839 */       paramMutableBigInteger.value[0] = 0;
/*      */     } else {
/*  841 */       paramMutableBigInteger.value[0] = ((int)(l3 / l1));
/*  842 */       j = (int)(l3 - paramMutableBigInteger.value[0] * l1);
/*  843 */       l3 = j & 0xFFFFFFFF;
/*      */     }
/*      */ 
/*  846 */     int n = this.intLen;
/*  847 */     int[] arrayOfInt = new int[2];
/*      */     while (true) { n--; if (n <= 0) break;
/*  849 */       long l4 = l3 << 32 | this.value[(this.offset + this.intLen - n)] & 0xFFFFFFFF;
/*      */ 
/*  851 */       if (l4 >= 0L) {
/*  852 */         arrayOfInt[0] = ((int)(l4 / l1));
/*  853 */         arrayOfInt[1] = ((int)(l4 - arrayOfInt[0] * l1));
/*      */       } else {
/*  855 */         divWord(arrayOfInt, l4, paramInt);
/*      */       }
/*  857 */       paramMutableBigInteger.value[(this.intLen - n)] = arrayOfInt[0];
/*  858 */       j = arrayOfInt[1];
/*  859 */       l3 = j & 0xFFFFFFFF;
/*      */     }
/*      */ 
/*  862 */     paramMutableBigInteger.normalize();
/*      */ 
/*  864 */     if (i > 0) {
/*  865 */       return j % paramInt;
/*      */     }
/*  867 */     return j;
/*      */   }
/*      */ 
/*      */   MutableBigInteger divide(MutableBigInteger paramMutableBigInteger1, MutableBigInteger paramMutableBigInteger2)
/*      */   {
/*  882 */     if (paramMutableBigInteger1.intLen == 0) {
/*  883 */       throw new ArithmeticException("BigInteger divide by zero");
/*      */     }
/*      */ 
/*  886 */     if (this.intLen == 0) {
/*  887 */       paramMutableBigInteger2.intLen = paramMutableBigInteger2.offset;
/*  888 */       return new MutableBigInteger();
/*      */     }
/*      */ 
/*  891 */     int i = compare(paramMutableBigInteger1);
/*      */ 
/*  893 */     if (i < 0) {
/*  894 */       paramMutableBigInteger2.intLen = (paramMutableBigInteger2.offset = 0);
/*  895 */       return new MutableBigInteger(this);
/*      */     }
/*      */ 
/*  898 */     if (i == 0)
/*      */     {
/*      */       int tmp80_79 = 1; paramMutableBigInteger2.intLen = tmp80_79; paramMutableBigInteger2.value[0] = tmp80_79;
/*  900 */       paramMutableBigInteger2.offset = 0;
/*  901 */       return new MutableBigInteger();
/*      */     }
/*      */ 
/*  904 */     paramMutableBigInteger2.clear();
/*      */ 
/*  906 */     if (paramMutableBigInteger1.intLen == 1) {
/*  907 */       int j = divideOneWord(paramMutableBigInteger1.value[paramMutableBigInteger1.offset], paramMutableBigInteger2);
/*  908 */       if (j == 0)
/*  909 */         return new MutableBigInteger();
/*  910 */       return new MutableBigInteger(j);
/*      */     }
/*      */ 
/*  914 */     int[] arrayOfInt = Arrays.copyOfRange(paramMutableBigInteger1.value, paramMutableBigInteger1.offset, paramMutableBigInteger1.offset + paramMutableBigInteger1.intLen);
/*  915 */     return divideMagnitude(arrayOfInt, paramMutableBigInteger2);
/*      */   }
/*      */ 
/*      */   long divide(long paramLong, MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  926 */     if (paramLong == 0L) {
/*  927 */       throw new ArithmeticException("BigInteger divide by zero");
/*      */     }
/*      */ 
/*  930 */     if (this.intLen == 0) {
/*  931 */       paramMutableBigInteger.intLen = (paramMutableBigInteger.offset = 0);
/*  932 */       return 0L;
/*      */     }
/*  934 */     if (paramLong < 0L) {
/*  935 */       paramLong = -paramLong;
/*      */     }
/*  937 */     int i = (int)(paramLong >>> 32);
/*  938 */     paramMutableBigInteger.clear();
/*      */ 
/*  940 */     if (i == 0) {
/*  941 */       return divideOneWord((int)paramLong, paramMutableBigInteger) & 0xFFFFFFFF;
/*      */     }
/*  943 */     int[] arrayOfInt = { i, (int)(paramLong & 0xFFFFFFFF) };
/*  944 */     return divideMagnitude(arrayOfInt, paramMutableBigInteger).toLong();
/*      */   }
/*      */ 
/*      */   private MutableBigInteger divideMagnitude(int[] paramArrayOfInt, MutableBigInteger paramMutableBigInteger)
/*      */   {
/*  957 */     MutableBigInteger localMutableBigInteger = new MutableBigInteger(new int[this.intLen + 1]);
/*  958 */     System.arraycopy(this.value, this.offset, localMutableBigInteger.value, 1, this.intLen);
/*  959 */     localMutableBigInteger.intLen = this.intLen;
/*  960 */     localMutableBigInteger.offset = 1;
/*      */ 
/*  962 */     int i = localMutableBigInteger.intLen;
/*      */ 
/*  965 */     int j = paramArrayOfInt.length;
/*  966 */     int k = i - j + 1;
/*  967 */     if (paramMutableBigInteger.value.length < k) {
/*  968 */       paramMutableBigInteger.value = new int[k];
/*  969 */       paramMutableBigInteger.offset = 0;
/*      */     }
/*  971 */     paramMutableBigInteger.intLen = k;
/*  972 */     int[] arrayOfInt1 = paramMutableBigInteger.value;
/*      */ 
/*  975 */     int m = Integer.numberOfLeadingZeros(paramArrayOfInt[0]);
/*  976 */     if (m > 0)
/*      */     {
/*  978 */       BigInteger.primitiveLeftShift(paramArrayOfInt, j, m);
/*      */ 
/*  980 */       localMutableBigInteger.leftShift(m);
/*      */     }
/*      */ 
/*  984 */     if (localMutableBigInteger.intLen == i) {
/*  985 */       localMutableBigInteger.offset = 0;
/*  986 */       localMutableBigInteger.value[0] = 0;
/*  987 */       localMutableBigInteger.intLen += 1;
/*      */     }
/*      */ 
/*  990 */     int n = paramArrayOfInt[0];
/*  991 */     long l1 = n & 0xFFFFFFFF;
/*  992 */     int i1 = paramArrayOfInt[1];
/*  993 */     int[] arrayOfInt2 = new int[2];
/*      */ 
/*  996 */     for (int i2 = 0; i2 < k; i2++)
/*      */     {
/*  999 */       int i3 = 0;
/* 1000 */       int i4 = 0;
/* 1001 */       int i5 = 0;
/* 1002 */       int i6 = localMutableBigInteger.value[(i2 + localMutableBigInteger.offset)];
/* 1003 */       int i7 = i6 + -2147483648;
/* 1004 */       int i8 = localMutableBigInteger.value[(i2 + 1 + localMutableBigInteger.offset)];
/*      */       long l2;
/* 1006 */       if (i6 == n) {
/* 1007 */         i3 = -1;
/* 1008 */         i4 = i6 + i8;
/* 1009 */         i5 = i4 + -2147483648 < i7 ? 1 : 0;
/*      */       } else {
/* 1011 */         l2 = i6 << 32 | i8 & 0xFFFFFFFF;
/* 1012 */         if (l2 >= 0L) {
/* 1013 */           i3 = (int)(l2 / l1);
/* 1014 */           i4 = (int)(l2 - i3 * l1);
/*      */         } else {
/* 1016 */           divWord(arrayOfInt2, l2, n);
/* 1017 */           i3 = arrayOfInt2[0];
/* 1018 */           i4 = arrayOfInt2[1];
/*      */         }
/*      */       }
/*      */ 
/* 1022 */       if (i3 != 0)
/*      */       {
/* 1025 */         if (i5 == 0) {
/* 1026 */           l2 = localMutableBigInteger.value[(i2 + 2 + localMutableBigInteger.offset)] & 0xFFFFFFFF;
/* 1027 */           long l3 = (i4 & 0xFFFFFFFF) << 32 | l2;
/* 1028 */           long l4 = (i1 & 0xFFFFFFFF) * (i3 & 0xFFFFFFFF);
/*      */ 
/* 1030 */           if (unsignedLongCompare(l4, l3)) {
/* 1031 */             i3--;
/* 1032 */             i4 = (int)((i4 & 0xFFFFFFFF) + l1);
/* 1033 */             if ((i4 & 0xFFFFFFFF) >= l1) {
/* 1034 */               l4 -= (i1 & 0xFFFFFFFF);
/* 1035 */               l3 = (i4 & 0xFFFFFFFF) << 32 | l2;
/* 1036 */               if (unsignedLongCompare(l4, l3)) {
/* 1037 */                 i3--;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1043 */         localMutableBigInteger.value[(i2 + localMutableBigInteger.offset)] = 0;
/* 1044 */         int i9 = mulsub(localMutableBigInteger.value, paramArrayOfInt, i3, j, i2 + localMutableBigInteger.offset);
/*      */ 
/* 1047 */         if (i9 + -2147483648 > i7)
/*      */         {
/* 1049 */           divadd(paramArrayOfInt, localMutableBigInteger.value, i2 + 1 + localMutableBigInteger.offset);
/* 1050 */           i3--;
/*      */         }
/*      */ 
/* 1054 */         arrayOfInt1[i2] = i3;
/*      */       }
/*      */     }
/*      */ 
/* 1058 */     if (m > 0) {
/* 1059 */       localMutableBigInteger.rightShift(m);
/*      */     }
/* 1061 */     paramMutableBigInteger.normalize();
/* 1062 */     localMutableBigInteger.normalize();
/* 1063 */     return localMutableBigInteger;
/*      */   }
/*      */ 
/*      */   private boolean unsignedLongCompare(long paramLong1, long paramLong2)
/*      */   {
/* 1071 */     return paramLong1 + -9223372036854775808L > paramLong2 + -9223372036854775808L;
/*      */   }
/*      */ 
/*      */   private void divWord(int[] paramArrayOfInt, long paramLong, int paramInt)
/*      */   {
/* 1080 */     long l1 = paramInt & 0xFFFFFFFF;
/*      */ 
/* 1082 */     if (l1 == 1L) {
/* 1083 */       paramArrayOfInt[0] = ((int)paramLong);
/* 1084 */       paramArrayOfInt[1] = 0;
/* 1085 */       return;
/*      */     }
/*      */ 
/* 1089 */     long l2 = (paramLong >>> 1) / (l1 >>> 1);
/* 1090 */     long l3 = paramLong - l2 * l1;
/*      */ 
/* 1093 */     while (l3 < 0L) {
/* 1094 */       l3 += l1;
/* 1095 */       l2 -= 1L;
/*      */     }
/* 1097 */     while (l3 >= l1) {
/* 1098 */       l3 -= l1;
/* 1099 */       l2 += 1L;
/*      */     }
/*      */ 
/* 1103 */     paramArrayOfInt[0] = ((int)l2);
/* 1104 */     paramArrayOfInt[1] = ((int)l3);
/*      */   }
/*      */ 
/*      */   MutableBigInteger hybridGCD(MutableBigInteger paramMutableBigInteger)
/*      */   {
/* 1113 */     MutableBigInteger localMutableBigInteger1 = this;
/* 1114 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger();
/*      */ 
/* 1116 */     while (paramMutableBigInteger.intLen != 0) {
/* 1117 */       if (Math.abs(localMutableBigInteger1.intLen - paramMutableBigInteger.intLen) < 2) {
/* 1118 */         return localMutableBigInteger1.binaryGCD(paramMutableBigInteger);
/*      */       }
/* 1120 */       MutableBigInteger localMutableBigInteger3 = localMutableBigInteger1.divide(paramMutableBigInteger, localMutableBigInteger2);
/* 1121 */       localMutableBigInteger1 = paramMutableBigInteger;
/* 1122 */       paramMutableBigInteger = localMutableBigInteger3;
/*      */     }
/* 1124 */     return localMutableBigInteger1;
/*      */   }
/*      */ 
/*      */   private MutableBigInteger binaryGCD(MutableBigInteger paramMutableBigInteger)
/*      */   {
/* 1133 */     Object localObject1 = this;
/* 1134 */     MutableBigInteger localMutableBigInteger = new MutableBigInteger();
/*      */ 
/* 1137 */     int i = ((MutableBigInteger)localObject1).getLowestSetBit();
/* 1138 */     int j = paramMutableBigInteger.getLowestSetBit();
/* 1139 */     int k = i < j ? i : j;
/* 1140 */     if (k != 0) {
/* 1141 */       ((MutableBigInteger)localObject1).rightShift(k);
/* 1142 */       paramMutableBigInteger.rightShift(k);
/*      */     }
/*      */ 
/* 1146 */     int m = k == i ? 1 : 0;
/* 1147 */     Object localObject2 = m != 0 ? paramMutableBigInteger : localObject1;
/* 1148 */     int n = m != 0 ? -1 : 1;
/*      */     int i1;
/* 1151 */     while ((i1 = ((MutableBigInteger)localObject2).getLowestSetBit()) >= 0)
/*      */     {
/* 1153 */       ((MutableBigInteger)localObject2).rightShift(i1);
/*      */ 
/* 1155 */       if (n > 0)
/* 1156 */         localObject1 = localObject2;
/*      */       else {
/* 1158 */         paramMutableBigInteger = (MutableBigInteger)localObject2;
/*      */       }
/*      */ 
/* 1161 */       if ((((MutableBigInteger)localObject1).intLen < 2) && (paramMutableBigInteger.intLen < 2)) {
/* 1162 */         int i2 = localObject1.value[localObject1.offset];
/* 1163 */         int i3 = paramMutableBigInteger.value[paramMutableBigInteger.offset];
/* 1164 */         i2 = binaryGcd(i2, i3);
/* 1165 */         localMutableBigInteger.value[0] = i2;
/* 1166 */         localMutableBigInteger.intLen = 1;
/* 1167 */         localMutableBigInteger.offset = 0;
/* 1168 */         if (k > 0)
/* 1169 */           localMutableBigInteger.leftShift(k);
/* 1170 */         return localMutableBigInteger;
/*      */       }
/*      */ 
/* 1174 */       if ((n = ((MutableBigInteger)localObject1).difference(paramMutableBigInteger)) == 0)
/*      */         break;
/* 1176 */       localObject2 = n >= 0 ? localObject1 : paramMutableBigInteger;
/*      */     }
/*      */ 
/* 1179 */     if (k > 0)
/* 1180 */       ((MutableBigInteger)localObject1).leftShift(k);
/* 1181 */     return localObject1;
/*      */   }
/*      */ 
/*      */   static int binaryGcd(int paramInt1, int paramInt2)
/*      */   {
/* 1188 */     if (paramInt2 == 0)
/* 1189 */       return paramInt1;
/* 1190 */     if (paramInt1 == 0) {
/* 1191 */       return paramInt2;
/*      */     }
/*      */ 
/* 1194 */     int i = Integer.numberOfTrailingZeros(paramInt1);
/* 1195 */     int j = Integer.numberOfTrailingZeros(paramInt2);
/* 1196 */     paramInt1 >>>= i;
/* 1197 */     paramInt2 >>>= j;
/*      */ 
/* 1199 */     int k = i < j ? i : j;
/*      */ 
/* 1201 */     while (paramInt1 != paramInt2) {
/* 1202 */       if (paramInt1 + -2147483648 > paramInt2 + -2147483648) {
/* 1203 */         paramInt1 -= paramInt2;
/* 1204 */         paramInt1 >>>= Integer.numberOfTrailingZeros(paramInt1);
/*      */       } else {
/* 1206 */         paramInt2 -= paramInt1;
/* 1207 */         paramInt2 >>>= Integer.numberOfTrailingZeros(paramInt2);
/*      */       }
/*      */     }
/* 1210 */     return paramInt1 << k;
/*      */   }
/*      */ 
/*      */   MutableBigInteger mutableModInverse(MutableBigInteger paramMutableBigInteger)
/*      */   {
/* 1219 */     if (paramMutableBigInteger.isOdd()) {
/* 1220 */       return modInverse(paramMutableBigInteger);
/*      */     }
/*      */ 
/* 1223 */     if (isEven()) {
/* 1224 */       throw new ArithmeticException("BigInteger not invertible.");
/*      */     }
/*      */ 
/* 1227 */     int i = paramMutableBigInteger.getLowestSetBit();
/*      */ 
/* 1230 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger(paramMutableBigInteger);
/* 1231 */     localMutableBigInteger1.rightShift(i);
/*      */ 
/* 1233 */     if (localMutableBigInteger1.isOne()) {
/* 1234 */       return modInverseMP2(i);
/*      */     }
/*      */ 
/* 1237 */     MutableBigInteger localMutableBigInteger2 = modInverse(localMutableBigInteger1);
/*      */ 
/* 1240 */     MutableBigInteger localMutableBigInteger3 = modInverseMP2(i);
/*      */ 
/* 1243 */     MutableBigInteger localMutableBigInteger4 = modInverseBP2(localMutableBigInteger1, i);
/* 1244 */     MutableBigInteger localMutableBigInteger5 = localMutableBigInteger1.modInverseMP2(i);
/*      */ 
/* 1246 */     MutableBigInteger localMutableBigInteger6 = new MutableBigInteger();
/* 1247 */     MutableBigInteger localMutableBigInteger7 = new MutableBigInteger();
/* 1248 */     MutableBigInteger localMutableBigInteger8 = new MutableBigInteger();
/*      */ 
/* 1250 */     localMutableBigInteger2.leftShift(i);
/* 1251 */     localMutableBigInteger2.multiply(localMutableBigInteger4, localMutableBigInteger8);
/*      */ 
/* 1253 */     localMutableBigInteger3.multiply(localMutableBigInteger1, localMutableBigInteger6);
/* 1254 */     localMutableBigInteger6.multiply(localMutableBigInteger5, localMutableBigInteger7);
/*      */ 
/* 1256 */     localMutableBigInteger8.add(localMutableBigInteger7);
/* 1257 */     return localMutableBigInteger8.divide(paramMutableBigInteger, localMutableBigInteger6);
/*      */   }
/*      */ 
/*      */   MutableBigInteger modInverseMP2(int paramInt)
/*      */   {
/* 1264 */     if (isEven()) {
/* 1265 */       throw new ArithmeticException("Non-invertible. (GCD != 1)");
/*      */     }
/* 1267 */     if (paramInt > 64) {
/* 1268 */       return euclidModInverse(paramInt);
/*      */     }
/* 1270 */     int i = inverseMod32(this.value[(this.offset + this.intLen - 1)]);
/*      */ 
/* 1272 */     if (paramInt < 33) {
/* 1273 */       i = paramInt == 32 ? i : i & (1 << paramInt) - 1;
/* 1274 */       return new MutableBigInteger(i);
/*      */     }
/*      */ 
/* 1277 */     long l1 = this.value[(this.offset + this.intLen - 1)] & 0xFFFFFFFF;
/* 1278 */     if (this.intLen > 1)
/* 1279 */       l1 |= this.value[(this.offset + this.intLen - 2)] << 32;
/* 1280 */     long l2 = i & 0xFFFFFFFF;
/* 1281 */     l2 *= (2L - l1 * l2);
/* 1282 */     l2 = paramInt == 64 ? l2 : l2 & (1L << paramInt) - 1L;
/*      */ 
/* 1284 */     MutableBigInteger localMutableBigInteger = new MutableBigInteger(new int[2]);
/* 1285 */     localMutableBigInteger.value[0] = ((int)(l2 >>> 32));
/* 1286 */     localMutableBigInteger.value[1] = ((int)l2);
/* 1287 */     localMutableBigInteger.intLen = 2;
/* 1288 */     localMutableBigInteger.normalize();
/* 1289 */     return localMutableBigInteger;
/*      */   }
/*      */ 
/*      */   static int inverseMod32(int paramInt)
/*      */   {
/* 1297 */     int i = paramInt;
/* 1298 */     i *= (2 - paramInt * i);
/* 1299 */     i *= (2 - paramInt * i);
/* 1300 */     i *= (2 - paramInt * i);
/* 1301 */     i *= (2 - paramInt * i);
/* 1302 */     return i;
/*      */   }
/*      */ 
/*      */   static MutableBigInteger modInverseBP2(MutableBigInteger paramMutableBigInteger, int paramInt)
/*      */   {
/* 1310 */     return fixup(new MutableBigInteger(1), new MutableBigInteger(paramMutableBigInteger), paramInt);
/*      */   }
/*      */ 
/*      */   private MutableBigInteger modInverse(MutableBigInteger paramMutableBigInteger)
/*      */   {
/* 1323 */     MutableBigInteger localMutableBigInteger = new MutableBigInteger(paramMutableBigInteger);
/* 1324 */     Object localObject1 = new MutableBigInteger(this);
/* 1325 */     Object localObject2 = new MutableBigInteger(localMutableBigInteger);
/* 1326 */     Object localObject3 = new SignedMutableBigInteger(1);
/* 1327 */     Object localObject4 = new SignedMutableBigInteger();
/* 1328 */     Object localObject5 = null;
/* 1329 */     Object localObject6 = null;
/*      */ 
/* 1331 */     int i = 0;
/*      */     int j;
/* 1333 */     if (((MutableBigInteger)localObject1).isEven()) {
/* 1334 */       j = ((MutableBigInteger)localObject1).getLowestSetBit();
/* 1335 */       ((MutableBigInteger)localObject1).rightShift(j);
/* 1336 */       ((SignedMutableBigInteger)localObject4).leftShift(j);
/* 1337 */       i = j;
/*      */     }
/*      */ 
/* 1341 */     while (!((MutableBigInteger)localObject1).isOne())
/*      */     {
/* 1343 */       if (((MutableBigInteger)localObject1).isZero()) {
/* 1344 */         throw new ArithmeticException("BigInteger not invertible.");
/*      */       }
/*      */ 
/* 1347 */       if (((MutableBigInteger)localObject1).compare((MutableBigInteger)localObject2) < 0) {
/* 1348 */         localObject5 = localObject1; localObject1 = localObject2; localObject2 = localObject5;
/* 1349 */         localObject6 = localObject4; localObject4 = localObject3; localObject3 = localObject6;
/*      */       }
/*      */ 
/* 1353 */       if (((localObject1.value[(localObject1.offset + localObject1.intLen - 1)] ^ localObject2.value[(localObject2.offset + localObject2.intLen - 1)]) & 0x3) == 0)
/*      */       {
/* 1355 */         ((MutableBigInteger)localObject1).subtract((MutableBigInteger)localObject2);
/* 1356 */         ((SignedMutableBigInteger)localObject3).signedSubtract((SignedMutableBigInteger)localObject4);
/*      */       } else {
/* 1358 */         ((MutableBigInteger)localObject1).add((MutableBigInteger)localObject2);
/* 1359 */         ((SignedMutableBigInteger)localObject3).signedAdd((SignedMutableBigInteger)localObject4);
/*      */       }
/*      */ 
/* 1363 */       j = ((MutableBigInteger)localObject1).getLowestSetBit();
/* 1364 */       ((MutableBigInteger)localObject1).rightShift(j);
/* 1365 */       ((SignedMutableBigInteger)localObject4).leftShift(j);
/* 1366 */       i += j;
/*      */     }
/*      */ 
/* 1369 */     while (((SignedMutableBigInteger)localObject3).sign < 0) {
/* 1370 */       ((SignedMutableBigInteger)localObject3).signedAdd(localMutableBigInteger);
/*      */     }
/* 1372 */     return fixup((MutableBigInteger)localObject3, localMutableBigInteger, i);
/*      */   }
/*      */ 
/*      */   static MutableBigInteger fixup(MutableBigInteger paramMutableBigInteger1, MutableBigInteger paramMutableBigInteger2, int paramInt)
/*      */   {
/* 1382 */     MutableBigInteger localMutableBigInteger = new MutableBigInteger();
/*      */ 
/* 1384 */     int i = -inverseMod32(paramMutableBigInteger2.value[(paramMutableBigInteger2.offset + paramMutableBigInteger2.intLen - 1)]);
/*      */ 
/* 1386 */     int j = 0; for (int k = paramInt >> 5; j < k; j++)
/*      */     {
/* 1388 */       int m = i * paramMutableBigInteger1.value[(paramMutableBigInteger1.offset + paramMutableBigInteger1.intLen - 1)];
/*      */ 
/* 1390 */       paramMutableBigInteger2.mul(m, localMutableBigInteger);
/* 1391 */       paramMutableBigInteger1.add(localMutableBigInteger);
/*      */ 
/* 1393 */       paramMutableBigInteger1.intLen -= 1;
/*      */     }
/* 1395 */     j = paramInt & 0x1F;
/* 1396 */     if (j != 0)
/*      */     {
/* 1398 */       k = i * paramMutableBigInteger1.value[(paramMutableBigInteger1.offset + paramMutableBigInteger1.intLen - 1)];
/* 1399 */       k &= (1 << j) - 1;
/*      */ 
/* 1401 */       paramMutableBigInteger2.mul(k, localMutableBigInteger);
/* 1402 */       paramMutableBigInteger1.add(localMutableBigInteger);
/*      */ 
/* 1404 */       paramMutableBigInteger1.rightShift(j);
/*      */     }
/*      */ 
/* 1408 */     while (paramMutableBigInteger1.compare(paramMutableBigInteger2) >= 0) {
/* 1409 */       paramMutableBigInteger1.subtract(paramMutableBigInteger2);
/*      */     }
/* 1411 */     return paramMutableBigInteger1;
/*      */   }
/*      */ 
/*      */   MutableBigInteger euclidModInverse(int paramInt)
/*      */   {
/* 1419 */     Object localObject1 = new MutableBigInteger(1);
/* 1420 */     ((MutableBigInteger)localObject1).leftShift(paramInt);
/* 1421 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger((MutableBigInteger)localObject1);
/*      */ 
/* 1423 */     Object localObject2 = new MutableBigInteger(this);
/* 1424 */     Object localObject3 = new MutableBigInteger();
/* 1425 */     Object localObject4 = ((MutableBigInteger)localObject1).divide((MutableBigInteger)localObject2, (MutableBigInteger)localObject3);
/*      */ 
/* 1427 */     Object localObject5 = localObject1;
/*      */ 
/* 1429 */     localObject1 = localObject4;
/* 1430 */     localObject4 = localObject5;
/*      */ 
/* 1432 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger((MutableBigInteger)localObject3);
/* 1433 */     MutableBigInteger localMutableBigInteger3 = new MutableBigInteger(1);
/* 1434 */     Object localObject6 = new MutableBigInteger();
/*      */ 
/* 1436 */     while (!((MutableBigInteger)localObject1).isOne()) {
/* 1437 */       localObject4 = ((MutableBigInteger)localObject2).divide((MutableBigInteger)localObject1, (MutableBigInteger)localObject3);
/*      */ 
/* 1439 */       if (((MutableBigInteger)localObject4).intLen == 0) {
/* 1440 */         throw new ArithmeticException("BigInteger not invertible.");
/*      */       }
/* 1442 */       localObject5 = localObject4;
/* 1443 */       localObject2 = localObject5;
/*      */ 
/* 1445 */       if (((MutableBigInteger)localObject3).intLen == 1)
/* 1446 */         localMutableBigInteger2.mul(localObject3.value[localObject3.offset], (MutableBigInteger)localObject6);
/*      */       else
/* 1448 */         ((MutableBigInteger)localObject3).multiply(localMutableBigInteger2, (MutableBigInteger)localObject6);
/* 1449 */       localObject5 = localObject3;
/* 1450 */       localObject3 = localObject6;
/* 1451 */       localObject6 = localObject5;
/* 1452 */       localMutableBigInteger3.add((MutableBigInteger)localObject3);
/*      */ 
/* 1454 */       if (((MutableBigInteger)localObject2).isOne()) {
/* 1455 */         return localMutableBigInteger3;
/*      */       }
/* 1457 */       localObject4 = ((MutableBigInteger)localObject1).divide((MutableBigInteger)localObject2, (MutableBigInteger)localObject3);
/*      */ 
/* 1459 */       if (((MutableBigInteger)localObject4).intLen == 0) {
/* 1460 */         throw new ArithmeticException("BigInteger not invertible.");
/*      */       }
/* 1462 */       localObject5 = localObject1;
/* 1463 */       localObject1 = localObject4;
/*      */ 
/* 1465 */       if (((MutableBigInteger)localObject3).intLen == 1)
/* 1466 */         localMutableBigInteger3.mul(localObject3.value[localObject3.offset], (MutableBigInteger)localObject6);
/*      */       else
/* 1468 */         ((MutableBigInteger)localObject3).multiply(localMutableBigInteger3, (MutableBigInteger)localObject6);
/* 1469 */       localObject5 = localObject3; localObject3 = localObject6; localObject6 = localObject5;
/*      */ 
/* 1471 */       localMutableBigInteger2.add((MutableBigInteger)localObject3);
/*      */     }
/* 1473 */     localMutableBigInteger1.subtract(localMutableBigInteger2);
/* 1474 */     return localMutableBigInteger1;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.math.MutableBigInteger
 * JD-Core Version:    0.6.2
 */