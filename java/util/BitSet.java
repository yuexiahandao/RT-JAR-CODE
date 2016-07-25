/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.Serializable;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.LongBuffer;
/*      */ 
/*      */ public class BitSet
/*      */   implements Cloneable, Serializable
/*      */ {
/*      */   private static final int ADDRESS_BITS_PER_WORD = 6;
/*      */   private static final int BITS_PER_WORD = 64;
/*      */   private static final int BIT_INDEX_MASK = 63;
/*      */   private static final long WORD_MASK = -1L;
/*   83 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("bits", [J.class) };
/*      */   private long[] words;
/*   95 */   private transient int wordsInUse = 0;
/*      */ 
/*  101 */   private transient boolean sizeIsSticky = false;
/*      */   private static final long serialVersionUID = 7997698588986878753L;
/*      */ 
/*      */   private static int wordIndex(int paramInt)
/*      */   {
/*  110 */     return paramInt >> 6;
/*      */   }
/*      */ 
/*      */   private void checkInvariants()
/*      */   {
/*  117 */     assert ((this.wordsInUse == 0) || (this.words[(this.wordsInUse - 1)] != 0L));
/*  118 */     assert ((this.wordsInUse >= 0) && (this.wordsInUse <= this.words.length));
/*  119 */     assert ((this.wordsInUse == this.words.length) || (this.words[this.wordsInUse] == 0L));
/*      */   }
/*      */ 
/*      */   private void recalculateWordsInUse()
/*      */   {
/*  130 */     for (int i = this.wordsInUse - 1; (i >= 0) && 
/*  131 */       (this.words[i] == 0L); i--);
/*  134 */     this.wordsInUse = (i + 1);
/*      */   }
/*      */ 
/*      */   public BitSet()
/*      */   {
/*  141 */     initWords(64);
/*  142 */     this.sizeIsSticky = false;
/*      */   }
/*      */ 
/*      */   public BitSet(int paramInt)
/*      */   {
/*  156 */     if (paramInt < 0) {
/*  157 */       throw new NegativeArraySizeException("nbits < 0: " + paramInt);
/*      */     }
/*  159 */     initWords(paramInt);
/*  160 */     this.sizeIsSticky = true;
/*      */   }
/*      */ 
/*      */   private void initWords(int paramInt) {
/*  164 */     this.words = new long[wordIndex(paramInt - 1) + 1];
/*      */   }
/*      */ 
/*      */   private BitSet(long[] paramArrayOfLong)
/*      */   {
/*  172 */     this.words = paramArrayOfLong;
/*  173 */     this.wordsInUse = paramArrayOfLong.length;
/*  174 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public static BitSet valueOf(long[] paramArrayOfLong)
/*      */   {
/*  194 */     for (int i = paramArrayOfLong.length; (i > 0) && (paramArrayOfLong[(i - 1)] == 0L); i--);
/*  196 */     return new BitSet(Arrays.copyOf(paramArrayOfLong, i));
/*      */   }
/*      */ 
/*      */   public static BitSet valueOf(LongBuffer paramLongBuffer)
/*      */   {
/*  216 */     paramLongBuffer = paramLongBuffer.slice();
/*      */ 
/*  218 */     for (int i = paramLongBuffer.remaining(); (i > 0) && (paramLongBuffer.get(i - 1) == 0L); i--);
/*  220 */     long[] arrayOfLong = new long[i];
/*  221 */     paramLongBuffer.get(arrayOfLong);
/*  222 */     return new BitSet(arrayOfLong);
/*      */   }
/*      */ 
/*      */   public static BitSet valueOf(byte[] paramArrayOfByte)
/*      */   {
/*  241 */     return valueOf(ByteBuffer.wrap(paramArrayOfByte));
/*      */   }
/*      */ 
/*      */   public static BitSet valueOf(ByteBuffer paramByteBuffer)
/*      */   {
/*  261 */     paramByteBuffer = paramByteBuffer.slice().order(ByteOrder.LITTLE_ENDIAN);
/*      */ 
/*  263 */     for (int i = paramByteBuffer.remaining(); (i > 0) && (paramByteBuffer.get(i - 1) == 0); i--);
/*  265 */     long[] arrayOfLong = new long[(i + 7) / 8];
/*  266 */     paramByteBuffer.limit(i);
/*  267 */     int j = 0;
/*  268 */     while (paramByteBuffer.remaining() >= 8)
/*  269 */       arrayOfLong[(j++)] = paramByteBuffer.getLong();
/*  270 */     int k = paramByteBuffer.remaining(); for (int m = 0; m < k; m++)
/*  271 */       arrayOfLong[j] |= (paramByteBuffer.get() & 0xFF) << 8 * m;
/*  272 */     return new BitSet(arrayOfLong);
/*      */   }
/*      */ 
/*      */   public byte[] toByteArray()
/*      */   {
/*  289 */     int i = this.wordsInUse;
/*  290 */     if (i == 0)
/*  291 */       return new byte[0];
/*  292 */     int j = 8 * (i - 1);
/*  293 */     for (long l1 = this.words[(i - 1)]; l1 != 0L; l1 >>>= 8)
/*  294 */       j++;
/*  295 */     byte[] arrayOfByte = new byte[j];
/*  296 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte).order(ByteOrder.LITTLE_ENDIAN);
/*  297 */     for (int k = 0; k < i - 1; k++)
/*  298 */       localByteBuffer.putLong(this.words[k]);
/*  299 */     for (long l2 = this.words[(i - 1)]; l2 != 0L; l2 >>>= 8)
/*  300 */       localByteBuffer.put((byte)(int)(l2 & 0xFF));
/*  301 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public long[] toLongArray()
/*      */   {
/*  318 */     return Arrays.copyOf(this.words, this.wordsInUse);
/*      */   }
/*      */ 
/*      */   private void ensureCapacity(int paramInt)
/*      */   {
/*  326 */     if (this.words.length < paramInt)
/*      */     {
/*  328 */       int i = Math.max(2 * this.words.length, paramInt);
/*  329 */       this.words = Arrays.copyOf(this.words, i);
/*  330 */       this.sizeIsSticky = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void expandTo(int paramInt)
/*      */   {
/*  342 */     int i = paramInt + 1;
/*  343 */     if (this.wordsInUse < i) {
/*  344 */       ensureCapacity(i);
/*  345 */       this.wordsInUse = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkRange(int paramInt1, int paramInt2)
/*      */   {
/*  353 */     if (paramInt1 < 0)
/*  354 */       throw new IndexOutOfBoundsException("fromIndex < 0: " + paramInt1);
/*  355 */     if (paramInt2 < 0)
/*  356 */       throw new IndexOutOfBoundsException("toIndex < 0: " + paramInt2);
/*  357 */     if (paramInt1 > paramInt2)
/*  358 */       throw new IndexOutOfBoundsException("fromIndex: " + paramInt1 + " > toIndex: " + paramInt2);
/*      */   }
/*      */ 
/*      */   public void flip(int paramInt)
/*      */   {
/*  371 */     if (paramInt < 0) {
/*  372 */       throw new IndexOutOfBoundsException("bitIndex < 0: " + paramInt);
/*      */     }
/*  374 */     int i = wordIndex(paramInt);
/*  375 */     expandTo(i);
/*      */ 
/*  377 */     this.words[i] ^= 1L << paramInt;
/*      */ 
/*  379 */     recalculateWordsInUse();
/*  380 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void flip(int paramInt1, int paramInt2)
/*      */   {
/*  396 */     checkRange(paramInt1, paramInt2);
/*      */ 
/*  398 */     if (paramInt1 == paramInt2) {
/*  399 */       return;
/*      */     }
/*  401 */     int i = wordIndex(paramInt1);
/*  402 */     int j = wordIndex(paramInt2 - 1);
/*  403 */     expandTo(j);
/*      */ 
/*  405 */     long l1 = -1L << paramInt1;
/*  406 */     long l2 = -1L >>> -paramInt2;
/*  407 */     if (i == j)
/*      */     {
/*  409 */       this.words[i] ^= l1 & l2;
/*      */     }
/*      */     else
/*      */     {
/*  413 */       this.words[i] ^= l1;
/*      */ 
/*  416 */       for (int k = i + 1; k < j; k++) {
/*  417 */         this.words[k] ^= -1L;
/*      */       }
/*      */ 
/*  420 */       this.words[j] ^= l2;
/*      */     }
/*      */ 
/*  423 */     recalculateWordsInUse();
/*  424 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void set(int paramInt)
/*      */   {
/*  435 */     if (paramInt < 0) {
/*  436 */       throw new IndexOutOfBoundsException("bitIndex < 0: " + paramInt);
/*      */     }
/*  438 */     int i = wordIndex(paramInt);
/*  439 */     expandTo(i);
/*      */ 
/*  441 */     this.words[i] |= 1L << paramInt;
/*      */ 
/*  443 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void set(int paramInt, boolean paramBoolean)
/*      */   {
/*  455 */     if (paramBoolean)
/*  456 */       set(paramInt);
/*      */     else
/*  458 */       clear(paramInt);
/*      */   }
/*      */ 
/*      */   public void set(int paramInt1, int paramInt2)
/*      */   {
/*  473 */     checkRange(paramInt1, paramInt2);
/*      */ 
/*  475 */     if (paramInt1 == paramInt2) {
/*  476 */       return;
/*      */     }
/*      */ 
/*  479 */     int i = wordIndex(paramInt1);
/*  480 */     int j = wordIndex(paramInt2 - 1);
/*  481 */     expandTo(j);
/*      */ 
/*  483 */     long l1 = -1L << paramInt1;
/*  484 */     long l2 = -1L >>> -paramInt2;
/*  485 */     if (i == j)
/*      */     {
/*  487 */       this.words[i] |= l1 & l2;
/*      */     }
/*      */     else
/*      */     {
/*  491 */       this.words[i] |= l1;
/*      */ 
/*  494 */       for (int k = i + 1; k < j; k++) {
/*  495 */         this.words[k] = -1L;
/*      */       }
/*      */ 
/*  498 */       this.words[j] |= l2;
/*      */     }
/*      */ 
/*  501 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void set(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/*  517 */     if (paramBoolean)
/*  518 */       set(paramInt1, paramInt2);
/*      */     else
/*  520 */       clear(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void clear(int paramInt)
/*      */   {
/*  531 */     if (paramInt < 0) {
/*  532 */       throw new IndexOutOfBoundsException("bitIndex < 0: " + paramInt);
/*      */     }
/*  534 */     int i = wordIndex(paramInt);
/*  535 */     if (i >= this.wordsInUse) {
/*  536 */       return;
/*      */     }
/*  538 */     this.words[i] &= (1L << paramInt ^ 0xFFFFFFFF);
/*      */ 
/*  540 */     recalculateWordsInUse();
/*  541 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void clear(int paramInt1, int paramInt2)
/*      */   {
/*  556 */     checkRange(paramInt1, paramInt2);
/*      */ 
/*  558 */     if (paramInt1 == paramInt2) {
/*  559 */       return;
/*      */     }
/*  561 */     int i = wordIndex(paramInt1);
/*  562 */     if (i >= this.wordsInUse) {
/*  563 */       return;
/*      */     }
/*  565 */     int j = wordIndex(paramInt2 - 1);
/*  566 */     if (j >= this.wordsInUse) {
/*  567 */       paramInt2 = length();
/*  568 */       j = this.wordsInUse - 1;
/*      */     }
/*      */ 
/*  571 */     long l1 = -1L << paramInt1;
/*  572 */     long l2 = -1L >>> -paramInt2;
/*  573 */     if (i == j)
/*      */     {
/*  575 */       this.words[i] &= (l1 & l2 ^ 0xFFFFFFFF);
/*      */     }
/*      */     else
/*      */     {
/*  579 */       this.words[i] &= (l1 ^ 0xFFFFFFFF);
/*      */ 
/*  582 */       for (int k = i + 1; k < j; k++) {
/*  583 */         this.words[k] = 0L;
/*      */       }
/*      */ 
/*  586 */       this.words[j] &= (l2 ^ 0xFFFFFFFF);
/*      */     }
/*      */ 
/*  589 */     recalculateWordsInUse();
/*  590 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  599 */     while (this.wordsInUse > 0)
/*  600 */       this.words[(--this.wordsInUse)] = 0L;
/*      */   }
/*      */ 
/*      */   public boolean get(int paramInt)
/*      */   {
/*  614 */     if (paramInt < 0) {
/*  615 */       throw new IndexOutOfBoundsException("bitIndex < 0: " + paramInt);
/*      */     }
/*  617 */     checkInvariants();
/*      */ 
/*  619 */     int i = wordIndex(paramInt);
/*  620 */     return (i < this.wordsInUse) && ((this.words[i] & 1L << paramInt) != 0L);
/*      */   }
/*      */ 
/*      */   public BitSet get(int paramInt1, int paramInt2)
/*      */   {
/*  637 */     checkRange(paramInt1, paramInt2);
/*      */ 
/*  639 */     checkInvariants();
/*      */ 
/*  641 */     int i = length();
/*      */ 
/*  644 */     if ((i <= paramInt1) || (paramInt1 == paramInt2)) {
/*  645 */       return new BitSet(0);
/*      */     }
/*      */ 
/*  648 */     if (paramInt2 > i) {
/*  649 */       paramInt2 = i;
/*      */     }
/*  651 */     BitSet localBitSet = new BitSet(paramInt2 - paramInt1);
/*  652 */     int j = wordIndex(paramInt2 - paramInt1 - 1) + 1;
/*  653 */     int k = wordIndex(paramInt1);
/*  654 */     int m = (paramInt1 & 0x3F) == 0 ? 1 : 0;
/*      */ 
/*  657 */     for (int n = 0; n < j - 1; k++) {
/*  658 */       localBitSet.words[n] = (m != 0 ? this.words[k] : this.words[k] >>> paramInt1 | this.words[(k + 1)] << -paramInt1);
/*      */ 
/*  657 */       n++;
/*      */     }
/*      */ 
/*  663 */     long l = -1L >>> -paramInt2;
/*  664 */     localBitSet.words[(j - 1)] = ((paramInt2 - 1 & 0x3F) < (paramInt1 & 0x3F) ? this.words[k] >>> paramInt1 | (this.words[(k + 1)] & l) << -paramInt1 : (this.words[k] & l) >>> paramInt1);
/*      */ 
/*  673 */     localBitSet.wordsInUse = j;
/*  674 */     localBitSet.recalculateWordsInUse();
/*  675 */     localBitSet.checkInvariants();
/*      */ 
/*  677 */     return localBitSet;
/*      */   }
/*      */ 
/*      */   public int nextSetBit(int paramInt)
/*      */   {
/*  700 */     if (paramInt < 0) {
/*  701 */       throw new IndexOutOfBoundsException("fromIndex < 0: " + paramInt);
/*      */     }
/*  703 */     checkInvariants();
/*      */ 
/*  705 */     int i = wordIndex(paramInt);
/*  706 */     if (i >= this.wordsInUse) {
/*  707 */       return -1;
/*      */     }
/*  709 */     long l = this.words[i] & -1L << paramInt;
/*      */     while (true)
/*      */     {
/*  712 */       if (l != 0L)
/*  713 */         return i * 64 + Long.numberOfTrailingZeros(l);
/*  714 */       i++; if (i == this.wordsInUse)
/*  715 */         return -1;
/*  716 */       l = this.words[i];
/*      */     }
/*      */   }
/*      */ 
/*      */   public int nextClearBit(int paramInt)
/*      */   {
/*  732 */     if (paramInt < 0) {
/*  733 */       throw new IndexOutOfBoundsException("fromIndex < 0: " + paramInt);
/*      */     }
/*  735 */     checkInvariants();
/*      */ 
/*  737 */     int i = wordIndex(paramInt);
/*  738 */     if (i >= this.wordsInUse) {
/*  739 */       return paramInt;
/*      */     }
/*  741 */     long l = (this.words[i] ^ 0xFFFFFFFF) & -1L << paramInt;
/*      */     while (true)
/*      */     {
/*  744 */       if (l != 0L)
/*  745 */         return i * 64 + Long.numberOfTrailingZeros(l);
/*  746 */       i++; if (i == this.wordsInUse)
/*  747 */         return this.wordsInUse * 64;
/*  748 */       l = this.words[i] ^ 0xFFFFFFFF;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int previousSetBit(int paramInt)
/*      */   {
/*  774 */     if (paramInt < 0) {
/*  775 */       if (paramInt == -1)
/*  776 */         return -1;
/*  777 */       throw new IndexOutOfBoundsException("fromIndex < -1: " + paramInt);
/*      */     }
/*      */ 
/*  781 */     checkInvariants();
/*      */ 
/*  783 */     int i = wordIndex(paramInt);
/*  784 */     if (i >= this.wordsInUse) {
/*  785 */       return length() - 1;
/*      */     }
/*  787 */     long l = this.words[i] & -1L >>> -(paramInt + 1);
/*      */     while (true)
/*      */     {
/*  790 */       if (l != 0L)
/*  791 */         return (i + 1) * 64 - 1 - Long.numberOfLeadingZeros(l);
/*  792 */       if (i-- == 0)
/*  793 */         return -1;
/*  794 */       l = this.words[i];
/*      */     }
/*      */   }
/*      */ 
/*      */   public int previousClearBit(int paramInt)
/*      */   {
/*  812 */     if (paramInt < 0) {
/*  813 */       if (paramInt == -1)
/*  814 */         return -1;
/*  815 */       throw new IndexOutOfBoundsException("fromIndex < -1: " + paramInt);
/*      */     }
/*      */ 
/*  819 */     checkInvariants();
/*      */ 
/*  821 */     int i = wordIndex(paramInt);
/*  822 */     if (i >= this.wordsInUse) {
/*  823 */       return paramInt;
/*      */     }
/*  825 */     long l = (this.words[i] ^ 0xFFFFFFFF) & -1L >>> -(paramInt + 1);
/*      */     while (true)
/*      */     {
/*  828 */       if (l != 0L)
/*  829 */         return (i + 1) * 64 - 1 - Long.numberOfLeadingZeros(l);
/*  830 */       if (i-- == 0)
/*  831 */         return -1;
/*  832 */       l = this.words[i] ^ 0xFFFFFFFF;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int length()
/*      */   {
/*  845 */     if (this.wordsInUse == 0) {
/*  846 */       return 0;
/*      */     }
/*  848 */     return 64 * (this.wordsInUse - 1) + (64 - Long.numberOfLeadingZeros(this.words[(this.wordsInUse - 1)]));
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  860 */     return this.wordsInUse == 0;
/*      */   }
/*      */ 
/*      */   public boolean intersects(BitSet paramBitSet)
/*      */   {
/*  873 */     for (int i = Math.min(this.wordsInUse, paramBitSet.wordsInUse) - 1; i >= 0; i--)
/*  874 */       if ((this.words[i] & paramBitSet.words[i]) != 0L)
/*  875 */         return true;
/*  876 */     return false;
/*      */   }
/*      */ 
/*      */   public int cardinality()
/*      */   {
/*  886 */     int i = 0;
/*  887 */     for (int j = 0; j < this.wordsInUse; j++)
/*  888 */       i += Long.bitCount(this.words[j]);
/*  889 */     return i;
/*      */   }
/*      */ 
/*      */   public void and(BitSet paramBitSet)
/*      */   {
/*  902 */     if (this == paramBitSet) {
/*  903 */       return;
/*      */     }
/*  905 */     while (this.wordsInUse > paramBitSet.wordsInUse) {
/*  906 */       this.words[(--this.wordsInUse)] = 0L;
/*      */     }
/*      */ 
/*  909 */     for (int i = 0; i < this.wordsInUse; i++) {
/*  910 */       this.words[i] &= paramBitSet.words[i];
/*      */     }
/*  912 */     recalculateWordsInUse();
/*  913 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void or(BitSet paramBitSet)
/*      */   {
/*  926 */     if (this == paramBitSet) {
/*  927 */       return;
/*      */     }
/*  929 */     int i = Math.min(this.wordsInUse, paramBitSet.wordsInUse);
/*      */ 
/*  931 */     if (this.wordsInUse < paramBitSet.wordsInUse) {
/*  932 */       ensureCapacity(paramBitSet.wordsInUse);
/*  933 */       this.wordsInUse = paramBitSet.wordsInUse;
/*      */     }
/*      */ 
/*  937 */     for (int j = 0; j < i; j++) {
/*  938 */       this.words[j] |= paramBitSet.words[j];
/*      */     }
/*      */ 
/*  941 */     if (i < paramBitSet.wordsInUse) {
/*  942 */       System.arraycopy(paramBitSet.words, i, this.words, i, this.wordsInUse - i);
/*      */     }
/*      */ 
/*  947 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void xor(BitSet paramBitSet)
/*      */   {
/*  965 */     int i = Math.min(this.wordsInUse, paramBitSet.wordsInUse);
/*      */ 
/*  967 */     if (this.wordsInUse < paramBitSet.wordsInUse) {
/*  968 */       ensureCapacity(paramBitSet.wordsInUse);
/*  969 */       this.wordsInUse = paramBitSet.wordsInUse;
/*      */     }
/*      */ 
/*  973 */     for (int j = 0; j < i; j++) {
/*  974 */       this.words[j] ^= paramBitSet.words[j];
/*      */     }
/*      */ 
/*  977 */     if (i < paramBitSet.wordsInUse) {
/*  978 */       System.arraycopy(paramBitSet.words, i, this.words, i, paramBitSet.wordsInUse - i);
/*      */     }
/*      */ 
/*  982 */     recalculateWordsInUse();
/*  983 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public void andNot(BitSet paramBitSet)
/*      */   {
/*  996 */     for (int i = Math.min(this.wordsInUse, paramBitSet.wordsInUse) - 1; i >= 0; i--) {
/*  997 */       this.words[i] &= (paramBitSet.words[i] ^ 0xFFFFFFFF);
/*      */     }
/*  999 */     recalculateWordsInUse();
/* 1000 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1022 */     long l = 1234L;
/* 1023 */     int i = this.wordsInUse;
/*      */     while (true) { i--; if (i < 0) break;
/* 1024 */       l ^= this.words[i] * (i + 1);
/*      */     }
/* 1026 */     return (int)(l >> 32 ^ l);
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 1037 */     return this.words.length * 64;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1055 */     if (!(paramObject instanceof BitSet))
/* 1056 */       return false;
/* 1057 */     if (this == paramObject) {
/* 1058 */       return true;
/*      */     }
/* 1060 */     BitSet localBitSet = (BitSet)paramObject;
/*      */ 
/* 1062 */     checkInvariants();
/* 1063 */     localBitSet.checkInvariants();
/*      */ 
/* 1065 */     if (this.wordsInUse != localBitSet.wordsInUse) {
/* 1066 */       return false;
/*      */     }
/*      */ 
/* 1069 */     for (int i = 0; i < this.wordsInUse; i++) {
/* 1070 */       if (this.words[i] != localBitSet.words[i])
/* 1071 */         return false;
/*      */     }
/* 1073 */     return true;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 1086 */     if (!this.sizeIsSticky)
/* 1087 */       trimToSize();
/*      */     try
/*      */     {
/* 1090 */       BitSet localBitSet = (BitSet)super.clone();
/* 1091 */       localBitSet.words = ((long[])this.words.clone());
/* 1092 */       localBitSet.checkInvariants();
/* 1093 */       return localBitSet; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/* 1095 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   private void trimToSize()
/*      */   {
/* 1105 */     if (this.wordsInUse != this.words.length) {
/* 1106 */       this.words = Arrays.copyOf(this.words, this.wordsInUse);
/* 1107 */       checkInvariants();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1118 */     checkInvariants();
/*      */ 
/* 1120 */     if (!this.sizeIsSticky) {
/* 1121 */       trimToSize();
/*      */     }
/* 1123 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 1124 */     localPutField.put("bits", this.words);
/* 1125 */     paramObjectOutputStream.writeFields();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1135 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 1136 */     this.words = ((long[])localGetField.get("bits", null));
/*      */ 
/* 1141 */     this.wordsInUse = this.words.length;
/* 1142 */     recalculateWordsInUse();
/* 1143 */     this.sizeIsSticky = ((this.words.length > 0) && (this.words[(this.words.length - 1)] == 0L));
/* 1144 */     checkInvariants();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1171 */     checkInvariants();
/*      */ 
/* 1173 */     int i = this.wordsInUse > 128 ? cardinality() : this.wordsInUse * 64;
/*      */ 
/* 1175 */     StringBuilder localStringBuilder = new StringBuilder(6 * i + 2);
/* 1176 */     localStringBuilder.append('{');
/*      */ 
/* 1178 */     int j = nextSetBit(0);
/* 1179 */     if (j != -1) {
/* 1180 */       localStringBuilder.append(j);
/* 1181 */       for (j = nextSetBit(j + 1); j >= 0; j = nextSetBit(j + 1)) {
/* 1182 */         int k = nextClearBit(j);
/*      */         do { localStringBuilder.append(", ").append(j);
/* 1184 */           j++; } while (j < k);
/*      */       }
/*      */     }
/*      */ 
/* 1188 */     localStringBuilder.append('}');
/* 1189 */     return localStringBuilder.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.BitSet
 * JD-Core Version:    0.6.2
 */