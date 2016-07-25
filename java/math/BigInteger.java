/*      */ package java.math;
/*      */ 
/*      */ import [I;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.security.SecureRandom;
/*      */ import java.util.Arrays;
/*      */ import java.util.Random;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class BigInteger extends Number
/*      */   implements Comparable<BigInteger>
/*      */ {
/*      */   final int signum;
/*      */   final int[] mag;
/*      */ 
/*      */   @Deprecated
/*      */   private int bitCount;
/*      */ 
/*      */   @Deprecated
/*      */   private int bitLength;
/*      */ 
/*      */   @Deprecated
/*      */   private int lowestSetBit;
/*      */ 
/*      */   @Deprecated
/*      */   private int firstNonzeroIntNum;
/*      */   static final long LONG_MASK = 4294967295L;
/*      */   private static long[] bitsPerDigit;
/*      */   private static final int SMALL_PRIME_THRESHOLD = 95;
/*      */   private static final int DEFAULT_PRIME_CERTAINTY = 100;
/*      */   private static final BigInteger SMALL_PRIME_PRODUCT;
/*      */   private static volatile Random staticRandom;
/*      */   private static final int MAX_CONSTANT = 16;
/*      */   private static BigInteger[] posConst;
/*      */   private static BigInteger[] negConst;
/*      */   public static final BigInteger ZERO;
/*      */   public static final BigInteger ONE;
/*      */   private static final BigInteger TWO;
/*      */   public static final BigInteger TEN;
/*      */   static int[] bnExpModThreshTable;
/*      */   private static String[] zeros;
/*      */   private static int[] digitsPerLong;
/*      */   private static BigInteger[] longRadix;
/*      */   private static int[] digitsPerInt;
/*      */   private static int[] intRadix;
/*      */   private static final long serialVersionUID = -8287574255936472291L;
/*      */   private static final ObjectStreamField[] serialPersistentFields;
/*      */   private static final Unsafe unsafe;
/*      */   private static final long signumOffset;
/*      */   private static final long magOffset;
/*      */ 
/*      */   public BigInteger(byte[] paramArrayOfByte)
/*      */   {
/*  189 */     if (paramArrayOfByte.length == 0) {
/*  190 */       throw new NumberFormatException("Zero length BigInteger");
/*      */     }
/*  192 */     if (paramArrayOfByte[0] < 0) {
/*  193 */       this.mag = makePositive(paramArrayOfByte);
/*  194 */       this.signum = -1;
/*      */     } else {
/*  196 */       this.mag = stripLeadingZeroBytes(paramArrayOfByte);
/*  197 */       this.signum = (this.mag.length == 0 ? 0 : 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private BigInteger(int[] paramArrayOfInt)
/*      */   {
/*  208 */     if (paramArrayOfInt.length == 0) {
/*  209 */       throw new NumberFormatException("Zero length BigInteger");
/*      */     }
/*  211 */     if (paramArrayOfInt[0] < 0) {
/*  212 */       this.mag = makePositive(paramArrayOfInt);
/*  213 */       this.signum = -1;
/*      */     } else {
/*  215 */       this.mag = trustedStripLeadingZeroInts(paramArrayOfInt);
/*  216 */       this.signum = (this.mag.length == 0 ? 0 : 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public BigInteger(int paramInt, byte[] paramArrayOfByte)
/*      */   {
/*  237 */     this.mag = stripLeadingZeroBytes(paramArrayOfByte);
/*      */ 
/*  239 */     if ((paramInt < -1) || (paramInt > 1)) {
/*  240 */       throw new NumberFormatException("Invalid signum value");
/*      */     }
/*  242 */     if (this.mag.length == 0) {
/*  243 */       this.signum = 0;
/*      */     } else {
/*  245 */       if (paramInt == 0)
/*  246 */         throw new NumberFormatException("signum-magnitude mismatch");
/*  247 */       this.signum = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   private BigInteger(int paramInt, int[] paramArrayOfInt)
/*      */   {
/*  258 */     this.mag = stripLeadingZeroInts(paramArrayOfInt);
/*      */ 
/*  260 */     if ((paramInt < -1) || (paramInt > 1)) {
/*  261 */       throw new NumberFormatException("Invalid signum value");
/*      */     }
/*  263 */     if (this.mag.length == 0) {
/*  264 */       this.signum = 0;
/*      */     } else {
/*  266 */       if (paramInt == 0)
/*  267 */         throw new NumberFormatException("signum-magnitude mismatch");
/*  268 */       this.signum = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   public BigInteger(String paramString, int paramInt)
/*      */   {
/*  290 */     int i = 0;
/*  291 */     int k = paramString.length();
/*      */ 
/*  293 */     if ((paramInt < 2) || (paramInt > 36))
/*  294 */       throw new NumberFormatException("Radix out of range");
/*  295 */     if (k == 0) {
/*  296 */       throw new NumberFormatException("Zero length BigInteger");
/*      */     }
/*      */ 
/*  299 */     int m = 1;
/*  300 */     int n = paramString.lastIndexOf('-');
/*  301 */     int i1 = paramString.lastIndexOf('+');
/*  302 */     if (n + i1 <= -1)
/*      */     {
/*  304 */       if ((n == 0) || (i1 == 0)) {
/*  305 */         i = 1;
/*  306 */         if (k == 1)
/*  307 */           throw new NumberFormatException("Zero length BigInteger");
/*      */       }
/*  309 */       if (n == 0)
/*  310 */         m = -1;
/*      */     } else {
/*  312 */       throw new NumberFormatException("Illegal embedded sign character");
/*      */     }
/*      */ 
/*  315 */     while ((i < k) && (Character.digit(paramString.charAt(i), paramInt) == 0))
/*      */     {
/*  317 */       i++;
/*  318 */     }if (i == k) {
/*  319 */       this.signum = 0;
/*  320 */       this.mag = ZERO.mag;
/*  321 */       return;
/*      */     }
/*      */ 
/*  324 */     int j = k - i;
/*  325 */     this.signum = m;
/*      */ 
/*  329 */     int i2 = (int)((j * bitsPerDigit[paramInt] >>> 10) + 1L);
/*  330 */     int i3 = i2 + 31 >>> 5;
/*  331 */     int[] arrayOfInt = new int[i3];
/*      */ 
/*  334 */     int i4 = j % digitsPerInt[paramInt];
/*  335 */     if (i4 == 0)
/*  336 */       i4 = digitsPerInt[paramInt];
/*  337 */     String str = paramString.substring(i, i += i4);
/*  338 */     arrayOfInt[(i3 - 1)] = Integer.parseInt(str, paramInt);
/*  339 */     if (arrayOfInt[(i3 - 1)] < 0) {
/*  340 */       throw new NumberFormatException("Illegal digit");
/*      */     }
/*      */ 
/*  343 */     int i5 = intRadix[paramInt];
/*  344 */     int i6 = 0;
/*  345 */     while (i < k) {
/*  346 */       str = paramString.substring(i, i += digitsPerInt[paramInt]);
/*  347 */       i6 = Integer.parseInt(str, paramInt);
/*  348 */       if (i6 < 0)
/*  349 */         throw new NumberFormatException("Illegal digit");
/*  350 */       destructiveMulAdd(arrayOfInt, i5, i6);
/*      */     }
/*      */ 
/*  353 */     this.mag = trustedStripLeadingZeroInts(arrayOfInt);
/*      */   }
/*      */ 
/*      */   BigInteger(char[] paramArrayOfChar)
/*      */   {
/*  358 */     int i = 0;
/*  359 */     int k = paramArrayOfChar.length;
/*      */ 
/*  362 */     int m = 1;
/*  363 */     if (paramArrayOfChar[0] == '-') {
/*  364 */       if (k == 1)
/*  365 */         throw new NumberFormatException("Zero length BigInteger");
/*  366 */       m = -1;
/*  367 */       i = 1;
/*  368 */     } else if (paramArrayOfChar[0] == '+') {
/*  369 */       if (k == 1)
/*  370 */         throw new NumberFormatException("Zero length BigInteger");
/*  371 */       i = 1;
/*      */     }
/*      */ 
/*  375 */     while ((i < k) && (Character.digit(paramArrayOfChar[i], 10) == 0))
/*  376 */       i++;
/*  377 */     if (i == k) {
/*  378 */       this.signum = 0;
/*  379 */       this.mag = ZERO.mag;
/*  380 */       return;
/*      */     }
/*      */ 
/*  383 */     int j = k - i;
/*  384 */     this.signum = m;
/*      */     int n;
/*  388 */     if (k < 10) {
/*  389 */       n = 1;
/*      */     } else {
/*  391 */       int i1 = (int)((j * bitsPerDigit[10] >>> 10) + 1L);
/*  392 */       n = i1 + 31 >>> 5;
/*      */     }
/*  394 */     int[] arrayOfInt = new int[n];
/*      */ 
/*  397 */     int i2 = j % digitsPerInt[10];
/*  398 */     if (i2 == 0)
/*  399 */       i2 = digitsPerInt[10];
/*      */     int tmp208_207 = (i + i2); i = tmp208_207; arrayOfInt[(n - 1)] = parseInt(paramArrayOfChar, i, tmp208_207);
/*      */ 
/*  403 */     while (i < k) {
/*  404 */       int i3 = parseInt(paramArrayOfChar, i, i += digitsPerInt[10]);
/*  405 */       destructiveMulAdd(arrayOfInt, intRadix[10], i3);
/*      */     }
/*  407 */     this.mag = trustedStripLeadingZeroInts(arrayOfInt);
/*      */   }
/*      */ 
/*      */   private int parseInt(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  414 */     int i = Character.digit(paramArrayOfChar[(paramInt1++)], 10);
/*  415 */     if (i == -1) {
/*  416 */       throw new NumberFormatException(new String(paramArrayOfChar));
/*      */     }
/*  418 */     for (int j = paramInt1; j < paramInt2; j++) {
/*  419 */       int k = Character.digit(paramArrayOfChar[j], 10);
/*  420 */       if (k == -1)
/*  421 */         throw new NumberFormatException(new String(paramArrayOfChar));
/*  422 */       i = 10 * i + k;
/*      */     }
/*      */ 
/*  425 */     return i;
/*      */   }
/*      */ 
/*      */   private static void destructiveMulAdd(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  439 */     long l1 = paramInt1 & 0xFFFFFFFF;
/*  440 */     long l2 = paramInt2 & 0xFFFFFFFF;
/*  441 */     int i = paramArrayOfInt.length;
/*      */ 
/*  443 */     long l3 = 0L;
/*  444 */     long l4 = 0L;
/*  445 */     for (int j = i - 1; j >= 0; j--) {
/*  446 */       l3 = l1 * (paramArrayOfInt[j] & 0xFFFFFFFF) + l4;
/*  447 */       paramArrayOfInt[j] = ((int)l3);
/*  448 */       l4 = l3 >>> 32;
/*      */     }
/*      */ 
/*  452 */     long l5 = (paramArrayOfInt[(i - 1)] & 0xFFFFFFFF) + l2;
/*  453 */     paramArrayOfInt[(i - 1)] = ((int)l5);
/*  454 */     l4 = l5 >>> 32;
/*  455 */     for (int k = i - 2; k >= 0; k--) {
/*  456 */       l5 = (paramArrayOfInt[k] & 0xFFFFFFFF) + l4;
/*  457 */       paramArrayOfInt[k] = ((int)l5);
/*  458 */       l4 = l5 >>> 32;
/*      */     }
/*      */   }
/*      */ 
/*      */   public BigInteger(String paramString)
/*      */   {
/*  476 */     this(paramString, 10);
/*      */   }
/*      */ 
/*      */   public BigInteger(int paramInt, Random paramRandom)
/*      */   {
/*  493 */     this(1, randomBits(paramInt, paramRandom));
/*      */   }
/*      */ 
/*      */   private static byte[] randomBits(int paramInt, Random paramRandom) {
/*  497 */     if (paramInt < 0)
/*  498 */       throw new IllegalArgumentException("numBits must be non-negative");
/*  499 */     int i = (int)((paramInt + 7L) / 8L);
/*  500 */     byte[] arrayOfByte = new byte[i];
/*      */ 
/*  503 */     if (i > 0) {
/*  504 */       paramRandom.nextBytes(arrayOfByte);
/*  505 */       int j = 8 * i - paramInt;
/*      */       int tmp49_48 = 0;
/*      */       byte[] tmp49_47 = arrayOfByte; tmp49_47[tmp49_48] = ((byte)(tmp49_47[tmp49_48] & (1 << 8 - j) - 1));
/*      */     }
/*  508 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public BigInteger(int paramInt1, int paramInt2, Random paramRandom)
/*      */   {
/*  533 */     if (paramInt1 < 2) {
/*  534 */       throw new ArithmeticException("bitLength < 2");
/*      */     }
/*  536 */     BigInteger localBigInteger = paramInt1 < 95 ? smallPrime(paramInt1, paramInt2, paramRandom) : largePrime(paramInt1, paramInt2, paramRandom);
/*      */ 
/*  538 */     this.signum = 1;
/*  539 */     this.mag = localBigInteger.mag;
/*      */   }
/*      */ 
/*      */   public static BigInteger probablePrime(int paramInt, Random paramRandom)
/*      */   {
/*  563 */     if (paramInt < 2) {
/*  564 */       throw new ArithmeticException("bitLength < 2");
/*      */     }
/*      */ 
/*  567 */     return paramInt < 95 ? smallPrime(paramInt, 100, paramRandom) : largePrime(paramInt, 100, paramRandom);
/*      */   }
/*      */ 
/*      */   private static BigInteger smallPrime(int paramInt1, int paramInt2, Random paramRandom)
/*      */   {
/*  580 */     int i = paramInt1 + 31 >>> 5;
/*  581 */     int[] arrayOfInt = new int[i];
/*  582 */     int j = 1 << (paramInt1 + 31 & 0x1F);
/*  583 */     int k = (j << 1) - 1;
/*      */     while (true)
/*      */     {
/*  587 */       for (int m = 0; m < i; m++)
/*  588 */         arrayOfInt[m] = paramRandom.nextInt();
/*  589 */       arrayOfInt[0] = (arrayOfInt[0] & k | j);
/*  590 */       if (paramInt1 > 2) {
/*  591 */         arrayOfInt[(i - 1)] |= 1;
/*      */       }
/*  593 */       BigInteger localBigInteger = new BigInteger(arrayOfInt, 1);
/*      */ 
/*  596 */       if (paramInt1 > 6)
/*      */       {
/*  597 */         long l = localBigInteger.remainder(SMALL_PRIME_PRODUCT).longValue();
/*  598 */         if ((l % 3L == 0L) || (l % 5L == 0L) || (l % 7L == 0L) || (l % 11L == 0L) || (l % 13L == 0L) || (l % 17L == 0L) || (l % 19L == 0L) || (l % 23L == 0L) || (l % 29L == 0L) || (l % 31L == 0L) || (l % 37L == 0L) || (l % 41L == 0L));
/*      */       }
/*      */       else
/*      */       {
/*  605 */         if (paramInt1 < 4) {
/*  606 */           return localBigInteger;
/*      */         }
/*      */ 
/*  609 */         if (localBigInteger.primeToCertainty(paramInt2, paramRandom))
/*  610 */           return localBigInteger;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static BigInteger largePrime(int paramInt1, int paramInt2, Random paramRandom)
/*      */   {
/*  625 */     BigInteger localBigInteger1 = new BigInteger(paramInt1, paramRandom).setBit(paramInt1 - 1);
/*  626 */     localBigInteger1.mag[(localBigInteger1.mag.length - 1)] &= -2;
/*      */ 
/*  629 */     int i = paramInt1 / 20 * 64;
/*  630 */     BitSieve localBitSieve = new BitSieve(localBigInteger1, i);
/*  631 */     BigInteger localBigInteger2 = localBitSieve.retrieve(localBigInteger1, paramInt2, paramRandom);
/*      */ 
/*  633 */     while ((localBigInteger2 == null) || (localBigInteger2.bitLength() != paramInt1)) {
/*  634 */       localBigInteger1 = localBigInteger1.add(valueOf(2 * i));
/*  635 */       if (localBigInteger1.bitLength() != paramInt1)
/*  636 */         localBigInteger1 = new BigInteger(paramInt1, paramRandom).setBit(paramInt1 - 1);
/*  637 */       localBigInteger1.mag[(localBigInteger1.mag.length - 1)] &= -2;
/*  638 */       localBitSieve = new BitSieve(localBigInteger1, i);
/*  639 */       localBigInteger2 = localBitSieve.retrieve(localBigInteger1, paramInt2, paramRandom);
/*      */     }
/*  641 */     return localBigInteger2;
/*      */   }
/*      */ 
/*      */   public BigInteger nextProbablePrime()
/*      */   {
/*  657 */     if (this.signum < 0) {
/*  658 */       throw new ArithmeticException("start < 0: " + this);
/*      */     }
/*      */ 
/*  661 */     if ((this.signum == 0) || (equals(ONE))) {
/*  662 */       return TWO;
/*      */     }
/*  664 */     BigInteger localBigInteger1 = add(ONE);
/*      */ 
/*  667 */     if (localBigInteger1.bitLength() < 95)
/*      */     {
/*  670 */       if (!localBigInteger1.testBit(0)) {
/*  671 */         localBigInteger1 = localBigInteger1.add(ONE);
/*      */       }
/*      */       while (true)
/*      */       {
/*  675 */         if (localBigInteger1.bitLength() > 6) {
/*  676 */           long l = localBigInteger1.remainder(SMALL_PRIME_PRODUCT).longValue();
/*  677 */           if ((l % 3L == 0L) || (l % 5L == 0L) || (l % 7L == 0L) || (l % 11L == 0L) || (l % 13L == 0L) || (l % 17L == 0L) || (l % 19L == 0L) || (l % 23L == 0L) || (l % 29L == 0L) || (l % 31L == 0L) || (l % 37L == 0L) || (l % 41L == 0L))
/*      */           {
/*  680 */             localBigInteger1 = localBigInteger1.add(TWO);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  686 */           if (localBigInteger1.bitLength() < 4) {
/*  687 */             return localBigInteger1;
/*      */           }
/*      */ 
/*  690 */           if (localBigInteger1.primeToCertainty(100, null)) {
/*  691 */             return localBigInteger1;
/*      */           }
/*  693 */           localBigInteger1 = localBigInteger1.add(TWO);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  698 */     if (localBigInteger1.testBit(0)) {
/*  699 */       localBigInteger1 = localBigInteger1.subtract(ONE);
/*      */     }
/*      */ 
/*  702 */     int i = localBigInteger1.bitLength() / 20 * 64;
/*      */     while (true)
/*      */     {
/*  705 */       BitSieve localBitSieve = new BitSieve(localBigInteger1, i);
/*  706 */       BigInteger localBigInteger2 = localBitSieve.retrieve(localBigInteger1, 100, null);
/*      */ 
/*  708 */       if (localBigInteger2 != null)
/*  709 */         return localBigInteger2;
/*  710 */       localBigInteger1 = localBigInteger1.add(valueOf(2 * i));
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean primeToCertainty(int paramInt, Random paramRandom)
/*      */   {
/*  729 */     int i = 0;
/*  730 */     int j = (Math.min(paramInt, 2147483646) + 1) / 2;
/*      */ 
/*  735 */     int k = bitLength();
/*  736 */     if (k < 100) {
/*  737 */       i = 50;
/*  738 */       i = j < i ? j : i;
/*  739 */       return passesMillerRabin(i, paramRandom);
/*      */     }
/*      */ 
/*  742 */     if (k < 256)
/*  743 */       i = 27;
/*  744 */     else if (k < 512)
/*  745 */       i = 15;
/*  746 */     else if (k < 768)
/*  747 */       i = 8;
/*  748 */     else if (k < 1024)
/*  749 */       i = 4;
/*      */     else {
/*  751 */       i = 2;
/*      */     }
/*  753 */     i = j < i ? j : i;
/*      */ 
/*  755 */     return (passesMillerRabin(i, paramRandom)) && (passesLucasLehmer());
/*      */   }
/*      */ 
/*      */   private boolean passesLucasLehmer()
/*      */   {
/*  765 */     BigInteger localBigInteger1 = add(ONE);
/*      */ 
/*  768 */     int i = 5;
/*  769 */     while (jacobiSymbol(i, this) != -1)
/*      */     {
/*  771 */       i = i < 0 ? Math.abs(i) + 2 : -(i + 2);
/*      */     }
/*      */ 
/*  775 */     BigInteger localBigInteger2 = lucasLehmerSequence(i, localBigInteger1, this);
/*      */ 
/*  778 */     return localBigInteger2.mod(this).equals(ZERO);
/*      */   }
/*      */ 
/*      */   private static int jacobiSymbol(int paramInt, BigInteger paramBigInteger)
/*      */   {
/*  786 */     if (paramInt == 0) {
/*  787 */       return 0;
/*      */     }
/*      */ 
/*  790 */     int i = 1;
/*  791 */     int j = paramBigInteger.mag[(paramBigInteger.mag.length - 1)];
/*      */     int k;
/*  794 */     if (paramInt < 0) {
/*  795 */       paramInt = -paramInt;
/*  796 */       k = j & 0x7;
/*  797 */       if ((k == 3) || (k == 7)) {
/*  798 */         i = -i;
/*      */       }
/*      */     }
/*      */ 
/*  802 */     while ((paramInt & 0x3) == 0)
/*  803 */       paramInt >>= 2;
/*  804 */     if ((paramInt & 0x1) == 0) {
/*  805 */       paramInt >>= 1;
/*  806 */       if (((j ^ j >> 1) & 0x2) != 0)
/*  807 */         i = -i;
/*      */     }
/*  809 */     if (paramInt == 1) {
/*  810 */       return i;
/*      */     }
/*  812 */     if ((paramInt & j & 0x2) != 0) {
/*  813 */       i = -i;
/*      */     }
/*  815 */     j = paramBigInteger.mod(valueOf(paramInt)).intValue();
/*      */ 
/*  818 */     while (j != 0) {
/*  819 */       while ((j & 0x3) == 0)
/*  820 */         j >>= 2;
/*  821 */       if ((j & 0x1) == 0) {
/*  822 */         j >>= 1;
/*  823 */         if (((paramInt ^ paramInt >> 1) & 0x2) != 0)
/*  824 */           i = -i;
/*      */       }
/*  826 */       if (j == 1) {
/*  827 */         return i;
/*      */       }
/*  829 */       assert (j < paramInt);
/*  830 */       k = j; j = paramInt; paramInt = k;
/*  831 */       if ((j & paramInt & 0x2) != 0) {
/*  832 */         i = -i;
/*      */       }
/*  834 */       j %= paramInt;
/*      */     }
/*  836 */     return 0;
/*      */   }
/*      */ 
/*      */   private static BigInteger lucasLehmerSequence(int paramInt, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  840 */     BigInteger localBigInteger1 = valueOf(paramInt);
/*  841 */     Object localObject1 = ONE;
/*  842 */     Object localObject2 = ONE;
/*      */ 
/*  844 */     for (int i = paramBigInteger1.bitLength() - 2; i >= 0; i--) {
/*  845 */       BigInteger localBigInteger2 = ((BigInteger)localObject1).multiply((BigInteger)localObject2).mod(paramBigInteger2);
/*      */ 
/*  847 */       BigInteger localBigInteger3 = ((BigInteger)localObject2).square().add(localBigInteger1.multiply(((BigInteger)localObject1).square())).mod(paramBigInteger2);
/*  848 */       if (localBigInteger3.testBit(0)) {
/*  849 */         localBigInteger3 = localBigInteger3.subtract(paramBigInteger2);
/*      */       }
/*  851 */       localBigInteger3 = localBigInteger3.shiftRight(1);
/*      */ 
/*  853 */       localObject1 = localBigInteger2; localObject2 = localBigInteger3;
/*  854 */       if (paramBigInteger1.testBit(i)) {
/*  855 */         localBigInteger2 = ((BigInteger)localObject1).add((BigInteger)localObject2).mod(paramBigInteger2);
/*  856 */         if (localBigInteger2.testBit(0)) {
/*  857 */           localBigInteger2 = localBigInteger2.subtract(paramBigInteger2);
/*      */         }
/*  859 */         localBigInteger2 = localBigInteger2.shiftRight(1);
/*  860 */         localBigInteger3 = ((BigInteger)localObject2).add(localBigInteger1.multiply((BigInteger)localObject1)).mod(paramBigInteger2);
/*  861 */         if (localBigInteger3.testBit(0))
/*  862 */           localBigInteger3 = localBigInteger3.subtract(paramBigInteger2);
/*  863 */         localBigInteger3 = localBigInteger3.shiftRight(1);
/*      */ 
/*  865 */         localObject1 = localBigInteger2; localObject2 = localBigInteger3;
/*      */       }
/*      */     }
/*  868 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static Random getSecureRandom()
/*      */   {
/*  874 */     if (staticRandom == null) {
/*  875 */       staticRandom = new SecureRandom();
/*      */     }
/*  877 */     return staticRandom;
/*      */   }
/*      */ 
/*      */   private boolean passesMillerRabin(int paramInt, Random paramRandom)
/*      */   {
/*  891 */     BigInteger localBigInteger1 = subtract(ONE);
/*  892 */     BigInteger localBigInteger2 = localBigInteger1;
/*  893 */     int i = localBigInteger2.getLowestSetBit();
/*  894 */     localBigInteger2 = localBigInteger2.shiftRight(i);
/*      */ 
/*  897 */     if (paramRandom == null) {
/*  898 */       paramRandom = getSecureRandom();
/*      */     }
/*  900 */     for (int j = 0; j < paramInt; j++)
/*      */     {
/*      */       BigInteger localBigInteger3;
/*      */       do
/*  904 */         localBigInteger3 = new BigInteger(bitLength(), paramRandom);
/*  905 */       while ((localBigInteger3.compareTo(ONE) <= 0) || (localBigInteger3.compareTo(this) >= 0));
/*      */ 
/*  907 */       int k = 0;
/*  908 */       BigInteger localBigInteger4 = localBigInteger3.modPow(localBigInteger2, this);
/*  909 */       while (((k != 0) || (!localBigInteger4.equals(ONE))) && (!localBigInteger4.equals(localBigInteger1))) {
/*  910 */         if ((k <= 0) || (!localBigInteger4.equals(ONE))) { k++; if (k != i); } else { return false; }
/*  912 */         localBigInteger4 = localBigInteger4.modPow(TWO, this);
/*      */       }
/*      */     }
/*  915 */     return true;
/*      */   }
/*      */ 
/*      */   BigInteger(int[] paramArrayOfInt, int paramInt)
/*      */   {
/*  924 */     this.signum = (paramArrayOfInt.length == 0 ? 0 : paramInt);
/*  925 */     this.mag = paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   private BigInteger(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/*  933 */     this.signum = (paramArrayOfByte.length == 0 ? 0 : paramInt);
/*  934 */     this.mag = stripLeadingZeroBytes(paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public static BigInteger valueOf(long paramLong)
/*      */   {
/*  950 */     if (paramLong == 0L)
/*  951 */       return ZERO;
/*  952 */     if ((paramLong > 0L) && (paramLong <= 16L))
/*  953 */       return posConst[((int)paramLong)];
/*  954 */     if ((paramLong < 0L) && (paramLong >= -16L)) {
/*  955 */       return negConst[((int)-paramLong)];
/*      */     }
/*  957 */     return new BigInteger(paramLong);
/*      */   }
/*      */ 
/*      */   private BigInteger(long paramLong)
/*      */   {
/*  964 */     if (paramLong < 0L) {
/*  965 */       paramLong = -paramLong;
/*  966 */       this.signum = -1;
/*      */     } else {
/*  968 */       this.signum = 1;
/*      */     }
/*      */ 
/*  971 */     int i = (int)(paramLong >>> 32);
/*  972 */     if (i == 0) {
/*  973 */       this.mag = new int[1];
/*  974 */       this.mag[0] = ((int)paramLong);
/*      */     } else {
/*  976 */       this.mag = new int[2];
/*  977 */       this.mag[0] = i;
/*  978 */       this.mag[1] = ((int)paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static BigInteger valueOf(int[] paramArrayOfInt)
/*      */   {
/*  988 */     return paramArrayOfInt[0] > 0 ? new BigInteger(paramArrayOfInt, 1) : new BigInteger(paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   public BigInteger add(BigInteger paramBigInteger)
/*      */   {
/* 1043 */     if (paramBigInteger.signum == 0)
/* 1044 */       return this;
/* 1045 */     if (this.signum == 0)
/* 1046 */       return paramBigInteger;
/* 1047 */     if (paramBigInteger.signum == this.signum) {
/* 1048 */       return new BigInteger(add(this.mag, paramBigInteger.mag), this.signum);
/*      */     }
/* 1050 */     int i = compareMagnitude(paramBigInteger);
/* 1051 */     if (i == 0)
/* 1052 */       return ZERO;
/* 1053 */     int[] arrayOfInt = i > 0 ? subtract(this.mag, paramBigInteger.mag) : subtract(paramBigInteger.mag, this.mag);
/*      */ 
/* 1055 */     arrayOfInt = trustedStripLeadingZeroInts(arrayOfInt);
/*      */ 
/* 1057 */     return new BigInteger(arrayOfInt, i == this.signum ? 1 : -1);
/*      */   }
/*      */ 
/*      */   private static int[] add(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/* 1067 */     if (paramArrayOfInt1.length < paramArrayOfInt2.length) {
/* 1068 */       int[] arrayOfInt1 = paramArrayOfInt1;
/* 1069 */       paramArrayOfInt1 = paramArrayOfInt2;
/* 1070 */       paramArrayOfInt2 = arrayOfInt1;
/*      */     }
/*      */ 
/* 1073 */     int i = paramArrayOfInt1.length;
/* 1074 */     int j = paramArrayOfInt2.length;
/* 1075 */     int[] arrayOfInt2 = new int[i];
/* 1076 */     long l = 0L;
/*      */ 
/* 1079 */     while (j > 0) {
/* 1080 */       l = (paramArrayOfInt1[(--i)] & 0xFFFFFFFF) + (paramArrayOfInt2[(--j)] & 0xFFFFFFFF) + (l >>> 32);
/*      */ 
/* 1082 */       arrayOfInt2[i] = ((int)l);
/*      */     }
/*      */ 
/* 1086 */     int k = l >>> 32 != 0L ? 1 : 0;
/* 1087 */     while ((i > 0) && (k != 0)) {
/* 1088 */       k = (arrayOfInt2[(--i)] = paramArrayOfInt1[i] + 1) == 0 ? 1 : 0;
/*      */     }
/*      */ 
/* 1091 */     while (i > 0) {
/* 1092 */       arrayOfInt2[(--i)] = paramArrayOfInt1[i];
/*      */     }
/*      */ 
/* 1095 */     if (k != 0) {
/* 1096 */       int[] arrayOfInt3 = new int[arrayOfInt2.length + 1];
/* 1097 */       System.arraycopy(arrayOfInt2, 0, arrayOfInt3, 1, arrayOfInt2.length);
/* 1098 */       arrayOfInt3[0] = 1;
/* 1099 */       return arrayOfInt3;
/*      */     }
/* 1101 */     return arrayOfInt2;
/*      */   }
/*      */ 
/*      */   public BigInteger subtract(BigInteger paramBigInteger)
/*      */   {
/* 1111 */     if (paramBigInteger.signum == 0)
/* 1112 */       return this;
/* 1113 */     if (this.signum == 0)
/* 1114 */       return paramBigInteger.negate();
/* 1115 */     if (paramBigInteger.signum != this.signum) {
/* 1116 */       return new BigInteger(add(this.mag, paramBigInteger.mag), this.signum);
/*      */     }
/* 1118 */     int i = compareMagnitude(paramBigInteger);
/* 1119 */     if (i == 0)
/* 1120 */       return ZERO;
/* 1121 */     int[] arrayOfInt = i > 0 ? subtract(this.mag, paramBigInteger.mag) : subtract(paramBigInteger.mag, this.mag);
/*      */ 
/* 1123 */     arrayOfInt = trustedStripLeadingZeroInts(arrayOfInt);
/* 1124 */     return new BigInteger(arrayOfInt, i == this.signum ? 1 : -1);
/*      */   }
/*      */ 
/*      */   private static int[] subtract(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/* 1134 */     int i = paramArrayOfInt1.length;
/* 1135 */     int[] arrayOfInt = new int[i];
/* 1136 */     int j = paramArrayOfInt2.length;
/* 1137 */     long l = 0L;
/*      */ 
/* 1140 */     while (j > 0) {
/* 1141 */       l = (paramArrayOfInt1[(--i)] & 0xFFFFFFFF) - (paramArrayOfInt2[(--j)] & 0xFFFFFFFF) + (l >> 32);
/*      */ 
/* 1144 */       arrayOfInt[i] = ((int)l);
/*      */     }
/*      */ 
/* 1148 */     int k = l >> 32 != 0L ? 1 : 0;
/* 1149 */     while ((i > 0) && (k != 0)) {
/* 1150 */       k = (arrayOfInt[(--i)] = paramArrayOfInt1[i] - 1) == -1 ? 1 : 0;
/*      */     }
/*      */ 
/* 1153 */     while (i > 0) {
/* 1154 */       arrayOfInt[(--i)] = paramArrayOfInt1[i];
/*      */     }
/* 1156 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public BigInteger multiply(BigInteger paramBigInteger)
/*      */   {
/* 1166 */     if ((paramBigInteger.signum == 0) || (this.signum == 0)) {
/* 1167 */       return ZERO;
/*      */     }
/* 1169 */     int[] arrayOfInt = multiplyToLen(this.mag, this.mag.length, paramBigInteger.mag, paramBigInteger.mag.length, null);
/*      */ 
/* 1171 */     arrayOfInt = trustedStripLeadingZeroInts(arrayOfInt);
/* 1172 */     return new BigInteger(arrayOfInt, this.signum == paramBigInteger.signum ? 1 : -1);
/*      */   }
/*      */ 
/*      */   BigInteger multiply(long paramLong)
/*      */   {
/* 1180 */     if ((paramLong == 0L) || (this.signum == 0))
/* 1181 */       return ZERO;
/* 1182 */     if (paramLong == -9223372036854775808L)
/* 1183 */       return multiply(valueOf(paramLong));
/* 1184 */     int i = paramLong > 0L ? this.signum : -this.signum;
/* 1185 */     if (paramLong < 0L)
/* 1186 */       paramLong = -paramLong;
/* 1187 */     long l1 = paramLong >>> 32;
/* 1188 */     long l2 = paramLong & 0xFFFFFFFF;
/*      */ 
/* 1190 */     int j = this.mag.length;
/* 1191 */     int[] arrayOfInt1 = this.mag;
/* 1192 */     int[] arrayOfInt2 = l1 == 0L ? new int[j + 1] : new int[j + 2];
/* 1193 */     long l3 = 0L;
/* 1194 */     int k = arrayOfInt2.length - 1;
/*      */     long l4;
/* 1195 */     for (int m = j - 1; m >= 0; m--) {
/* 1196 */       l4 = (arrayOfInt1[m] & 0xFFFFFFFF) * l2 + l3;
/* 1197 */       arrayOfInt2[(k--)] = ((int)l4);
/* 1198 */       l3 = l4 >>> 32;
/*      */     }
/* 1200 */     arrayOfInt2[k] = ((int)l3);
/* 1201 */     if (l1 != 0L) {
/* 1202 */       l3 = 0L;
/* 1203 */       k = arrayOfInt2.length - 2;
/* 1204 */       for (m = j - 1; m >= 0; m--) {
/* 1205 */         l4 = (arrayOfInt1[m] & 0xFFFFFFFF) * l1 + (arrayOfInt2[k] & 0xFFFFFFFF) + l3;
/*      */ 
/* 1207 */         arrayOfInt2[(k--)] = ((int)l4);
/* 1208 */         l3 = l4 >>> 32;
/*      */       }
/* 1210 */       arrayOfInt2[0] = ((int)l3);
/*      */     }
/* 1212 */     if (l3 == 0L)
/* 1213 */       arrayOfInt2 = Arrays.copyOfRange(arrayOfInt2, 1, arrayOfInt2.length);
/* 1214 */     return new BigInteger(arrayOfInt2, i);
/*      */   }
/*      */ 
/*      */   private int[] multiplyToLen(int[] paramArrayOfInt1, int paramInt1, int[] paramArrayOfInt2, int paramInt2, int[] paramArrayOfInt3)
/*      */   {
/* 1222 */     int i = paramInt1 - 1;
/* 1223 */     int j = paramInt2 - 1;
/*      */ 
/* 1225 */     if ((paramArrayOfInt3 == null) || (paramArrayOfInt3.length < paramInt1 + paramInt2)) {
/* 1226 */       paramArrayOfInt3 = new int[paramInt1 + paramInt2];
/*      */     }
/* 1228 */     long l1 = 0L;
/* 1229 */     int k = j; for (int m = j + 1 + i; k >= 0; m--) {
/* 1230 */       long l2 = (paramArrayOfInt2[k] & 0xFFFFFFFF) * (paramArrayOfInt1[i] & 0xFFFFFFFF) + l1;
/*      */ 
/* 1232 */       paramArrayOfInt3[m] = ((int)l2);
/* 1233 */       l1 = l2 >>> 32;
/*      */ 
/* 1229 */       k--;
/*      */     }
/*      */ 
/* 1235 */     paramArrayOfInt3[i] = ((int)l1);
/*      */ 
/* 1237 */     for (k = i - 1; k >= 0; k--) {
/* 1238 */       l1 = 0L;
/* 1239 */       m = j; for (int n = j + 1 + k; m >= 0; n--) {
/* 1240 */         long l3 = (paramArrayOfInt2[m] & 0xFFFFFFFF) * (paramArrayOfInt1[k] & 0xFFFFFFFF) + (paramArrayOfInt3[n] & 0xFFFFFFFF) + l1;
/*      */ 
/* 1243 */         paramArrayOfInt3[n] = ((int)l3);
/* 1244 */         l1 = l3 >>> 32;
/*      */ 
/* 1239 */         m--;
/*      */       }
/*      */ 
/* 1246 */       paramArrayOfInt3[k] = ((int)l1);
/*      */     }
/* 1248 */     return paramArrayOfInt3;
/*      */   }
/*      */ 
/*      */   private BigInteger square()
/*      */   {
/* 1257 */     if (this.signum == 0)
/* 1258 */       return ZERO;
/* 1259 */     int[] arrayOfInt = squareToLen(this.mag, this.mag.length, null);
/* 1260 */     return new BigInteger(trustedStripLeadingZeroInts(arrayOfInt), 1);
/*      */   }
/*      */ 
/*      */   private static final int[] squareToLen(int[] paramArrayOfInt1, int paramInt, int[] paramArrayOfInt2)
/*      */   {
/* 1302 */     int i = paramInt << 1;
/* 1303 */     if ((paramArrayOfInt2 == null) || (paramArrayOfInt2.length < i)) {
/* 1304 */       paramArrayOfInt2 = new int[i];
/*      */     }
/*      */ 
/* 1307 */     int j = 0;
/* 1308 */     int k = 0; for (int m = 0; k < paramInt; k++) {
/* 1309 */       long l1 = paramArrayOfInt1[k] & 0xFFFFFFFF;
/* 1310 */       long l2 = l1 * l1;
/* 1311 */       paramArrayOfInt2[(m++)] = (j << 31 | (int)(l2 >>> 33));
/* 1312 */       paramArrayOfInt2[(m++)] = ((int)(l2 >>> 1));
/* 1313 */       j = (int)l2;
/*      */     }
/*      */ 
/* 1317 */     k = paramInt; for (m = 1; k > 0; m += 2) {
/* 1318 */       int n = paramArrayOfInt1[(k - 1)];
/* 1319 */       n = mulAdd(paramArrayOfInt2, paramArrayOfInt1, m, k - 1, n);
/* 1320 */       addOne(paramArrayOfInt2, m - 1, k, n);
/*      */ 
/* 1317 */       k--;
/*      */     }
/*      */ 
/* 1324 */     primitiveLeftShift(paramArrayOfInt2, i, 1);
/* 1325 */     paramArrayOfInt2[(i - 1)] |= paramArrayOfInt1[(paramInt - 1)] & 0x1;
/*      */ 
/* 1327 */     return paramArrayOfInt2;
/*      */   }
/*      */ 
/*      */   public BigInteger divide(BigInteger paramBigInteger)
/*      */   {
/* 1338 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger();
/* 1339 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger(this.mag);
/* 1340 */     MutableBigInteger localMutableBigInteger3 = new MutableBigInteger(paramBigInteger.mag);
/*      */ 
/* 1342 */     localMutableBigInteger2.divide(localMutableBigInteger3, localMutableBigInteger1);
/* 1343 */     return localMutableBigInteger1.toBigInteger(this.signum == paramBigInteger.signum ? 1 : -1);
/*      */   }
/*      */ 
/*      */   public BigInteger[] divideAndRemainder(BigInteger paramBigInteger)
/*      */   {
/* 1358 */     BigInteger[] arrayOfBigInteger = new BigInteger[2];
/* 1359 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger();
/* 1360 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger(this.mag);
/* 1361 */     MutableBigInteger localMutableBigInteger3 = new MutableBigInteger(paramBigInteger.mag);
/* 1362 */     MutableBigInteger localMutableBigInteger4 = localMutableBigInteger2.divide(localMutableBigInteger3, localMutableBigInteger1);
/* 1363 */     arrayOfBigInteger[0] = localMutableBigInteger1.toBigInteger(this.signum == paramBigInteger.signum ? 1 : -1);
/* 1364 */     arrayOfBigInteger[1] = localMutableBigInteger4.toBigInteger(this.signum);
/* 1365 */     return arrayOfBigInteger;
/*      */   }
/*      */ 
/*      */   public BigInteger remainder(BigInteger paramBigInteger)
/*      */   {
/* 1377 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger();
/* 1378 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger(this.mag);
/* 1379 */     MutableBigInteger localMutableBigInteger3 = new MutableBigInteger(paramBigInteger.mag);
/*      */ 
/* 1381 */     return localMutableBigInteger2.divide(localMutableBigInteger3, localMutableBigInteger1).toBigInteger(this.signum);
/*      */   }
/*      */ 
/*      */   public BigInteger pow(int paramInt)
/*      */   {
/* 1394 */     if (paramInt < 0)
/* 1395 */       throw new ArithmeticException("Negative exponent");
/* 1396 */     if (this.signum == 0) {
/* 1397 */       return paramInt == 0 ? ONE : this;
/*      */     }
/*      */ 
/* 1400 */     int i = (this.signum < 0) && ((paramInt & 0x1) == 1) ? -1 : 1;
/* 1401 */     int[] arrayOfInt1 = this.mag;
/* 1402 */     int[] arrayOfInt2 = { 1 };
/*      */ 
/* 1404 */     while (paramInt != 0) {
/* 1405 */       if ((paramInt & 0x1) == 1) {
/* 1406 */         arrayOfInt2 = multiplyToLen(arrayOfInt2, arrayOfInt2.length, arrayOfInt1, arrayOfInt1.length, null);
/*      */ 
/* 1408 */         arrayOfInt2 = trustedStripLeadingZeroInts(arrayOfInt2);
/*      */       }
/* 1410 */       if (paramInt >>>= 1 != 0) {
/* 1411 */         arrayOfInt1 = squareToLen(arrayOfInt1, arrayOfInt1.length, null);
/* 1412 */         arrayOfInt1 = trustedStripLeadingZeroInts(arrayOfInt1);
/*      */       }
/*      */     }
/* 1415 */     return new BigInteger(arrayOfInt2, i);
/*      */   }
/*      */ 
/*      */   public BigInteger gcd(BigInteger paramBigInteger)
/*      */   {
/* 1427 */     if (paramBigInteger.signum == 0)
/* 1428 */       return abs();
/* 1429 */     if (this.signum == 0) {
/* 1430 */       return paramBigInteger.abs();
/*      */     }
/* 1432 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger(this);
/* 1433 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger(paramBigInteger);
/*      */ 
/* 1435 */     MutableBigInteger localMutableBigInteger3 = localMutableBigInteger1.hybridGCD(localMutableBigInteger2);
/*      */ 
/* 1437 */     return localMutableBigInteger3.toBigInteger(1);
/*      */   }
/*      */ 
/*      */   static int bitLengthForInt(int paramInt)
/*      */   {
/* 1444 */     return 32 - Integer.numberOfLeadingZeros(paramInt);
/*      */   }
/*      */ 
/*      */   private static int[] leftShift(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 1452 */     int i = paramInt2 >>> 5;
/* 1453 */     int j = paramInt2 & 0x1F;
/* 1454 */     int k = bitLengthForInt(paramArrayOfInt[0]);
/*      */ 
/* 1457 */     if (paramInt2 <= 32 - k) {
/* 1458 */       primitiveLeftShift(paramArrayOfInt, paramInt1, j);
/* 1459 */       return paramArrayOfInt;
/*      */     }
/* 1461 */     if (j <= 32 - k) {
/* 1462 */       arrayOfInt = new int[i + paramInt1];
/* 1463 */       for (m = 0; m < paramInt1; m++)
/* 1464 */         arrayOfInt[m] = paramArrayOfInt[m];
/* 1465 */       primitiveLeftShift(arrayOfInt, arrayOfInt.length, j);
/* 1466 */       return arrayOfInt;
/*      */     }
/* 1468 */     int[] arrayOfInt = new int[i + paramInt1 + 1];
/* 1469 */     for (int m = 0; m < paramInt1; m++)
/* 1470 */       arrayOfInt[m] = paramArrayOfInt[m];
/* 1471 */     primitiveRightShift(arrayOfInt, arrayOfInt.length, 32 - j);
/* 1472 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   static void primitiveRightShift(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 1479 */     int i = 32 - paramInt2;
/* 1480 */     int j = paramInt1 - 1; for (int k = paramArrayOfInt[j]; j > 0; j--) {
/* 1481 */       int m = k;
/* 1482 */       k = paramArrayOfInt[(j - 1)];
/* 1483 */       paramArrayOfInt[j] = (k << i | m >>> paramInt2);
/*      */     }
/* 1485 */     paramArrayOfInt[0] >>>= paramInt2;
/*      */   }
/*      */ 
/*      */   static void primitiveLeftShift(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 1490 */     if ((paramInt1 == 0) || (paramInt2 == 0)) {
/* 1491 */       return;
/*      */     }
/* 1493 */     int i = 32 - paramInt2;
/* 1494 */     int j = 0; int k = paramArrayOfInt[j]; for (int m = j + paramInt1 - 1; j < m; j++) {
/* 1495 */       int n = k;
/* 1496 */       k = paramArrayOfInt[(j + 1)];
/* 1497 */       paramArrayOfInt[j] = (n << paramInt2 | k >>> i);
/*      */     }
/* 1499 */     paramArrayOfInt[(paramInt1 - 1)] <<= paramInt2;
/*      */   }
/*      */ 
/*      */   private static int bitLength(int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1507 */     if (paramInt == 0)
/* 1508 */       return 0;
/* 1509 */     return (paramInt - 1 << 5) + bitLengthForInt(paramArrayOfInt[0]);
/*      */   }
/*      */ 
/*      */   public BigInteger abs()
/*      */   {
/* 1519 */     return this.signum >= 0 ? this : negate();
/*      */   }
/*      */ 
/*      */   public BigInteger negate()
/*      */   {
/* 1528 */     return new BigInteger(this.mag, -this.signum);
/*      */   }
/*      */ 
/*      */   public int signum()
/*      */   {
/* 1538 */     return this.signum;
/*      */   }
/*      */ 
/*      */   public BigInteger mod(BigInteger paramBigInteger)
/*      */   {
/* 1554 */     if (paramBigInteger.signum <= 0) {
/* 1555 */       throw new ArithmeticException("BigInteger: modulus not positive");
/*      */     }
/* 1557 */     BigInteger localBigInteger = remainder(paramBigInteger);
/* 1558 */     return localBigInteger.signum >= 0 ? localBigInteger : localBigInteger.add(paramBigInteger);
/*      */   }
/*      */ 
/*      */   public BigInteger modPow(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*      */   {
/* 1575 */     if (paramBigInteger2.signum <= 0) {
/* 1576 */       throw new ArithmeticException("BigInteger: modulus not positive");
/*      */     }
/*      */ 
/* 1579 */     if (paramBigInteger1.signum == 0) {
/* 1580 */       return paramBigInteger2.equals(ONE) ? ZERO : ONE;
/*      */     }
/* 1582 */     if (equals(ONE)) {
/* 1583 */       return paramBigInteger2.equals(ONE) ? ZERO : ONE;
/*      */     }
/* 1585 */     if ((equals(ZERO)) && (paramBigInteger1.signum >= 0)) {
/* 1586 */       return ZERO;
/*      */     }
/* 1588 */     if ((equals(negConst[1])) && (!paramBigInteger1.testBit(0)))
/* 1589 */       return paramBigInteger2.equals(ONE) ? ZERO : ONE;
/*      */     int i;
/* 1592 */     if ((i = paramBigInteger1.signum < 0 ? 1 : 0) != 0) {
/* 1593 */       paramBigInteger1 = paramBigInteger1.negate();
/*      */     }
/* 1595 */     BigInteger localBigInteger1 = (this.signum < 0) || (compareTo(paramBigInteger2) >= 0) ? mod(paramBigInteger2) : this;
/*      */     BigInteger localBigInteger2;
/* 1598 */     if (paramBigInteger2.testBit(0)) {
/* 1599 */       localBigInteger2 = localBigInteger1.oddModPow(paramBigInteger1, paramBigInteger2);
/*      */     }
/*      */     else
/*      */     {
/* 1608 */       int j = paramBigInteger2.getLowestSetBit();
/*      */ 
/* 1610 */       BigInteger localBigInteger3 = paramBigInteger2.shiftRight(j);
/* 1611 */       BigInteger localBigInteger4 = ONE.shiftLeft(j);
/*      */ 
/* 1614 */       BigInteger localBigInteger5 = (this.signum < 0) || (compareTo(localBigInteger3) >= 0) ? mod(localBigInteger3) : this;
/*      */ 
/* 1618 */       BigInteger localBigInteger6 = localBigInteger3.equals(ONE) ? ZERO : localBigInteger5.oddModPow(paramBigInteger1, localBigInteger3);
/*      */ 
/* 1622 */       BigInteger localBigInteger7 = localBigInteger1.modPow2(paramBigInteger1, j);
/*      */ 
/* 1625 */       BigInteger localBigInteger8 = localBigInteger4.modInverse(localBigInteger3);
/* 1626 */       BigInteger localBigInteger9 = localBigInteger3.modInverse(localBigInteger4);
/*      */ 
/* 1628 */       localBigInteger2 = localBigInteger6.multiply(localBigInteger4).multiply(localBigInteger8).add(localBigInteger7.multiply(localBigInteger3).multiply(localBigInteger9)).mod(paramBigInteger2);
/*      */     }
/*      */ 
/* 1632 */     return i != 0 ? localBigInteger2.modInverse(paramBigInteger2) : localBigInteger2;
/*      */   }
/*      */ 
/*      */   private BigInteger oddModPow(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*      */   {
/* 1701 */     if (paramBigInteger1.equals(ONE)) {
/* 1702 */       return this;
/*      */     }
/*      */ 
/* 1705 */     if (this.signum == 0) {
/* 1706 */       return ZERO;
/*      */     }
/* 1708 */     int[] arrayOfInt1 = (int[])this.mag.clone();
/* 1709 */     int[] arrayOfInt2 = paramBigInteger1.mag;
/* 1710 */     int[] arrayOfInt3 = paramBigInteger2.mag;
/* 1711 */     int i = arrayOfInt3.length;
/*      */ 
/* 1714 */     int j = 0;
/* 1715 */     int k = bitLength(arrayOfInt2, arrayOfInt2.length);
/*      */ 
/* 1717 */     if ((k != 17) || (arrayOfInt2[0] != 65537)) {
/* 1718 */       while (k > bnExpModThreshTable[j]) {
/* 1719 */         j++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1724 */     int m = 1 << j;
/*      */ 
/* 1727 */     int[][] arrayOfInt = new int[m][];
/* 1728 */     for (int n = 0; n < m; n++) {
/* 1729 */       arrayOfInt[n] = new int[i];
/*      */     }
/*      */ 
/* 1732 */     n = -MutableBigInteger.inverseMod32(arrayOfInt3[(i - 1)]);
/*      */ 
/* 1735 */     Object localObject1 = leftShift(arrayOfInt1, arrayOfInt1.length, i << 5);
/*      */ 
/* 1737 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger();
/* 1738 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger((int[])localObject1);
/* 1739 */     MutableBigInteger localMutableBigInteger3 = new MutableBigInteger(arrayOfInt3);
/*      */ 
/* 1741 */     MutableBigInteger localMutableBigInteger4 = localMutableBigInteger2.divide(localMutableBigInteger3, localMutableBigInteger1);
/* 1742 */     arrayOfInt[0] = localMutableBigInteger4.toIntArray();
/*      */ 
/* 1745 */     if (arrayOfInt[0].length < i) {
/* 1746 */       int i1 = i - arrayOfInt[0].length;
/* 1747 */       localObject3 = new int[i];
/* 1748 */       for (i2 = 0; i2 < arrayOfInt[0].length; i2++)
/* 1749 */         localObject3[(i2 + i1)] = arrayOfInt[0][i2];
/* 1750 */       arrayOfInt[0] = localObject3;
/*      */     }
/*      */ 
/* 1754 */     Object localObject2 = squareToLen(arrayOfInt[0], i, null);
/* 1755 */     localObject2 = montReduce((int[])localObject2, arrayOfInt3, i, n);
/*      */ 
/* 1758 */     Object localObject3 = new int[i];
/* 1759 */     for (int i2 = 0; i2 < i; i2++) {
/* 1760 */       localObject3[i2] = localObject2[i2];
/*      */     }
/*      */ 
/* 1763 */     for (i2 = 1; i2 < m; i2++) {
/* 1764 */       int[] arrayOfInt4 = multiplyToLen((int[])localObject3, i, arrayOfInt[(i2 - 1)], i, null);
/* 1765 */       arrayOfInt[i2] = montReduce(arrayOfInt4, arrayOfInt3, i, n);
/*      */     }
/*      */ 
/* 1769 */     i2 = 1 << (k - 1 & 0x1F);
/*      */ 
/* 1771 */     int i3 = 0;
/* 1772 */     int i4 = arrayOfInt2.length;
/* 1773 */     int i5 = 0;
/* 1774 */     for (int i6 = 0; i6 <= j; i6++) {
/* 1775 */       i3 = i3 << 1 | ((arrayOfInt2[i5] & i2) != 0 ? 1 : 0);
/* 1776 */       i2 >>>= 1;
/* 1777 */       if (i2 == 0) {
/* 1778 */         i5++;
/* 1779 */         i2 = -2147483648;
/* 1780 */         i4--;
/*      */       }
/*      */     }
/*      */ 
/* 1784 */     i6 = k;
/*      */ 
/* 1787 */     k--;
/* 1788 */     int i7 = 1;
/*      */ 
/* 1790 */     i6 = k - j;
/* 1791 */     while ((i3 & 0x1) == 0) {
/* 1792 */       i3 >>>= 1;
/* 1793 */       i6++;
/*      */     }
/*      */ 
/* 1796 */     [I local[I = arrayOfInt[(i3 >>> 1)];
/*      */ 
/* 1798 */     i3 = 0;
/* 1799 */     if (i6 == k) {
/* 1800 */       i7 = 0;
/*      */     }
/*      */     while (true)
/*      */     {
/* 1804 */       k--;
/*      */ 
/* 1806 */       i3 <<= 1;
/*      */ 
/* 1808 */       if (i4 != 0) {
/* 1809 */         i3 |= ((arrayOfInt2[i5] & i2) != 0 ? 1 : 0);
/* 1810 */         i2 >>>= 1;
/* 1811 */         if (i2 == 0) {
/* 1812 */           i5++;
/* 1813 */           i2 = -2147483648;
/* 1814 */           i4--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1819 */       if ((i3 & m) != 0) {
/* 1820 */         i6 = k - j;
/* 1821 */         while ((i3 & 0x1) == 0) {
/* 1822 */           i3 >>>= 1;
/* 1823 */           i6++;
/*      */         }
/* 1825 */         local[I = arrayOfInt[(i3 >>> 1)];
/* 1826 */         i3 = 0;
/*      */       }
/*      */ 
/* 1830 */       if (k == i6) {
/* 1831 */         if (i7 != 0) {
/* 1832 */           localObject2 = (int[])local[I.clone();
/* 1833 */           i7 = 0;
/*      */         } else {
/* 1835 */           localObject3 = localObject2;
/* 1836 */           localObject1 = multiplyToLen((int[])localObject3, i, local[I, i, (int[])localObject1);
/* 1837 */           localObject1 = montReduce((int[])localObject1, arrayOfInt3, i, n);
/* 1838 */           localObject3 = localObject1; localObject1 = localObject2; localObject2 = localObject3;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1843 */       if (k == 0)
/*      */       {
/*      */         break;
/*      */       }
/* 1847 */       if (i7 == 0) {
/* 1848 */         localObject3 = localObject2;
/* 1849 */         localObject1 = squareToLen((int[])localObject3, i, (int[])localObject1);
/* 1850 */         localObject1 = montReduce((int[])localObject1, arrayOfInt3, i, n);
/* 1851 */         localObject3 = localObject1; localObject1 = localObject2; localObject2 = localObject3;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1856 */     int[] arrayOfInt5 = new int[2 * i];
/* 1857 */     for (int i8 = 0; i8 < i; i8++) {
/* 1858 */       arrayOfInt5[(i8 + i)] = localObject2[i8];
/*      */     }
/* 1860 */     localObject2 = montReduce(arrayOfInt5, arrayOfInt3, i, n);
/*      */ 
/* 1862 */     arrayOfInt5 = new int[i];
/* 1863 */     for (i8 = 0; i8 < i; i8++) {
/* 1864 */       arrayOfInt5[i8] = localObject2[i8];
/*      */     }
/* 1866 */     return new BigInteger(1, arrayOfInt5);
/*      */   }
/*      */ 
/*      */   private static int[] montReduce(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2)
/*      */   {
/* 1874 */     int i = 0;
/* 1875 */     int j = paramInt1;
/* 1876 */     int k = 0;
/*      */     do
/*      */     {
/* 1879 */       int m = paramArrayOfInt1[(paramArrayOfInt1.length - 1 - k)];
/* 1880 */       int n = mulAdd(paramArrayOfInt1, paramArrayOfInt2, k, paramInt1, paramInt2 * m);
/* 1881 */       i += addOne(paramArrayOfInt1, k, paramInt1, n);
/* 1882 */       k++;
/* 1883 */       j--; } while (j > 0);
/*      */ 
/* 1885 */     while (i > 0) {
/* 1886 */       i += subN(paramArrayOfInt1, paramArrayOfInt2, paramInt1);
/*      */     }
/* 1888 */     while (intArrayCmpToLen(paramArrayOfInt1, paramArrayOfInt2, paramInt1) >= 0) {
/* 1889 */       subN(paramArrayOfInt1, paramArrayOfInt2, paramInt1);
/*      */     }
/* 1891 */     return paramArrayOfInt1;
/*      */   }
/*      */ 
/*      */   private static int intArrayCmpToLen(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/* 1900 */     for (int i = 0; i < paramInt; i++) {
/* 1901 */       long l1 = paramArrayOfInt1[i] & 0xFFFFFFFF;
/* 1902 */       long l2 = paramArrayOfInt2[i] & 0xFFFFFFFF;
/* 1903 */       if (l1 < l2)
/* 1904 */         return -1;
/* 1905 */       if (l1 > l2)
/* 1906 */         return 1;
/*      */     }
/* 1908 */     return 0;
/*      */   }
/*      */ 
/*      */   private static int subN(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/* 1915 */     long l = 0L;
/*      */     while (true) {
/* 1917 */       paramInt--; if (paramInt < 0) break;
/* 1918 */       l = (paramArrayOfInt1[paramInt] & 0xFFFFFFFF) - (paramArrayOfInt2[paramInt] & 0xFFFFFFFF) + (l >> 32);
/*      */ 
/* 1920 */       paramArrayOfInt1[paramInt] = ((int)l);
/*      */     }
/*      */ 
/* 1923 */     return (int)(l >> 32);
/*      */   }
/*      */ 
/*      */   static int mulAdd(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1930 */     long l1 = paramInt3 & 0xFFFFFFFF;
/* 1931 */     long l2 = 0L;
/*      */ 
/* 1933 */     paramInt1 = paramArrayOfInt1.length - paramInt1 - 1;
/* 1934 */     for (int i = paramInt2 - 1; i >= 0; i--) {
/* 1935 */       long l3 = (paramArrayOfInt2[i] & 0xFFFFFFFF) * l1 + (paramArrayOfInt1[paramInt1] & 0xFFFFFFFF) + l2;
/*      */ 
/* 1937 */       paramArrayOfInt1[(paramInt1--)] = ((int)l3);
/* 1938 */       l2 = l3 >>> 32;
/*      */     }
/* 1940 */     return (int)l2;
/*      */   }
/*      */ 
/*      */   static int addOne(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1948 */     paramInt1 = paramArrayOfInt.length - 1 - paramInt2 - paramInt1;
/* 1949 */     long l = (paramArrayOfInt[paramInt1] & 0xFFFFFFFF) + (paramInt3 & 0xFFFFFFFF);
/*      */ 
/* 1951 */     paramArrayOfInt[paramInt1] = ((int)l);
/* 1952 */     if (l >>> 32 == 0L)
/* 1953 */       return 0; do {
/* 1954 */       paramInt2--; if (paramInt2 < 0) break;
/* 1955 */       paramInt1--; if (paramInt1 < 0) {
/* 1956 */         return 1;
/*      */       }
/* 1958 */       paramArrayOfInt[paramInt1] += 1;
/* 1959 */     }while (paramArrayOfInt[paramInt1] == 0);
/* 1960 */     return 0;
/*      */ 
/* 1963 */     return 1;
/*      */   }
/*      */ 
/*      */   private BigInteger modPow2(BigInteger paramBigInteger, int paramInt)
/*      */   {
/* 1974 */     BigInteger localBigInteger1 = valueOf(1L);
/* 1975 */     BigInteger localBigInteger2 = mod2(paramInt);
/* 1976 */     int i = 0;
/*      */ 
/* 1978 */     int j = paramBigInteger.bitLength();
/*      */ 
/* 1980 */     if (testBit(0)) {
/* 1981 */       j = paramInt - 1 < j ? paramInt - 1 : j;
/*      */     }
/* 1983 */     while (i < j) {
/* 1984 */       if (paramBigInteger.testBit(i))
/* 1985 */         localBigInteger1 = localBigInteger1.multiply(localBigInteger2).mod2(paramInt);
/* 1986 */       i++;
/* 1987 */       if (i < j) {
/* 1988 */         localBigInteger2 = localBigInteger2.square().mod2(paramInt);
/*      */       }
/*      */     }
/* 1991 */     return localBigInteger1;
/*      */   }
/*      */ 
/*      */   private BigInteger mod2(int paramInt)
/*      */   {
/* 1999 */     if (bitLength() <= paramInt) {
/* 2000 */       return this;
/*      */     }
/*      */ 
/* 2003 */     int i = paramInt + 31 >>> 5;
/* 2004 */     int[] arrayOfInt = new int[i];
/* 2005 */     for (int j = 0; j < i; j++) {
/* 2006 */       arrayOfInt[j] = this.mag[(j + (this.mag.length - i))];
/*      */     }
/*      */ 
/* 2009 */     j = (i << 5) - paramInt;
/*      */     int tmp64_63 = 0;
/*      */     int[] tmp64_62 = arrayOfInt; tmp64_62[tmp64_63] = ((int)(tmp64_62[tmp64_63] & (1L << 32 - j) - 1L));
/*      */ 
/* 2012 */     return arrayOfInt[0] == 0 ? new BigInteger(1, arrayOfInt) : new BigInteger(arrayOfInt, 1);
/*      */   }
/*      */ 
/*      */   public BigInteger modInverse(BigInteger paramBigInteger)
/*      */   {
/* 2025 */     if (paramBigInteger.signum != 1) {
/* 2026 */       throw new ArithmeticException("BigInteger: modulus not positive");
/*      */     }
/* 2028 */     if (paramBigInteger.equals(ONE)) {
/* 2029 */       return ZERO;
/*      */     }
/*      */ 
/* 2032 */     BigInteger localBigInteger = this;
/* 2033 */     if ((this.signum < 0) || (compareMagnitude(paramBigInteger) >= 0)) {
/* 2034 */       localBigInteger = mod(paramBigInteger);
/*      */     }
/* 2036 */     if (localBigInteger.equals(ONE)) {
/* 2037 */       return ONE;
/*      */     }
/* 2039 */     MutableBigInteger localMutableBigInteger1 = new MutableBigInteger(localBigInteger);
/* 2040 */     MutableBigInteger localMutableBigInteger2 = new MutableBigInteger(paramBigInteger);
/*      */ 
/* 2042 */     MutableBigInteger localMutableBigInteger3 = localMutableBigInteger1.mutableModInverse(localMutableBigInteger2);
/* 2043 */     return localMutableBigInteger3.toBigInteger(1);
/*      */   }
/*      */ 
/*      */   public BigInteger shiftLeft(int paramInt)
/*      */   {
/* 2061 */     if (this.signum == 0)
/* 2062 */       return ZERO;
/* 2063 */     if (paramInt == 0)
/* 2064 */       return this;
/* 2065 */     if (paramInt < 0) {
/* 2066 */       if (paramInt == -2147483648) {
/* 2067 */         throw new ArithmeticException("Shift distance of Integer.MIN_VALUE not supported.");
/*      */       }
/* 2069 */       return shiftRight(-paramInt);
/*      */     }
/*      */ 
/* 2073 */     int i = paramInt >>> 5;
/* 2074 */     int j = paramInt & 0x1F;
/* 2075 */     int k = this.mag.length;
/* 2076 */     int[] arrayOfInt = null;
/*      */     int m;
/* 2078 */     if (j == 0) {
/* 2079 */       arrayOfInt = new int[k + i];
/* 2080 */       for (m = 0; m < k; m++)
/* 2081 */         arrayOfInt[m] = this.mag[m];
/*      */     } else {
/* 2083 */       m = 0;
/* 2084 */       int n = 32 - j;
/* 2085 */       int i1 = this.mag[0] >>> n;
/* 2086 */       if (i1 != 0) {
/* 2087 */         arrayOfInt = new int[k + i + 1];
/* 2088 */         arrayOfInt[(m++)] = i1;
/*      */       } else {
/* 2090 */         arrayOfInt = new int[k + i];
/*      */       }
/* 2092 */       int i2 = 0;
/* 2093 */       while (i2 < k - 1)
/* 2094 */         arrayOfInt[(m++)] = (this.mag[(i2++)] << j | this.mag[i2] >>> n);
/* 2095 */       arrayOfInt[m] = (this.mag[i2] << j);
/*      */     }
/*      */ 
/* 2098 */     return new BigInteger(arrayOfInt, this.signum);
/*      */   }
/*      */ 
/*      */   public BigInteger shiftRight(int paramInt)
/*      */   {
/* 2114 */     if (paramInt == 0)
/* 2115 */       return this;
/* 2116 */     if (paramInt < 0) {
/* 2117 */       if (paramInt == -2147483648) {
/* 2118 */         throw new ArithmeticException("Shift distance of Integer.MIN_VALUE not supported.");
/*      */       }
/* 2120 */       return shiftLeft(-paramInt);
/*      */     }
/*      */ 
/* 2124 */     int i = paramInt >>> 5;
/* 2125 */     int j = paramInt & 0x1F;
/* 2126 */     int k = this.mag.length;
/* 2127 */     int[] arrayOfInt = null;
/*      */ 
/* 2130 */     if (i >= k)
/* 2131 */       return this.signum >= 0 ? ZERO : negConst[1];
/*      */     int m;
/*      */     int n;
/*      */     int i1;
/* 2133 */     if (j == 0) {
/* 2134 */       m = k - i;
/* 2135 */       arrayOfInt = new int[m];
/* 2136 */       for (n = 0; n < m; n++)
/* 2137 */         arrayOfInt[n] = this.mag[n];
/*      */     } else {
/* 2139 */       m = 0;
/* 2140 */       n = this.mag[0] >>> j;
/* 2141 */       if (n != 0) {
/* 2142 */         arrayOfInt = new int[k - i];
/* 2143 */         arrayOfInt[(m++)] = n;
/*      */       } else {
/* 2145 */         arrayOfInt = new int[k - i - 1];
/*      */       }
/*      */ 
/* 2148 */       i1 = 32 - j;
/* 2149 */       int i2 = 0;
/* 2150 */       while (i2 < k - i - 1) {
/* 2151 */         arrayOfInt[(m++)] = (this.mag[(i2++)] << i1 | this.mag[i2] >>> j);
/*      */       }
/*      */     }
/* 2154 */     if (this.signum < 0)
/*      */     {
/* 2156 */       m = 0;
/* 2157 */       n = k - 1; for (i1 = k - i; (n >= i1) && (m == 0); n--)
/* 2158 */         m = this.mag[n] != 0 ? 1 : 0;
/* 2159 */       if ((m == 0) && (j != 0)) {
/* 2160 */         m = this.mag[(k - i - 1)] << 32 - j != 0 ? 1 : 0;
/*      */       }
/* 2162 */       if (m != 0) {
/* 2163 */         arrayOfInt = javaIncrement(arrayOfInt);
/*      */       }
/*      */     }
/* 2166 */     return new BigInteger(arrayOfInt, this.signum);
/*      */   }
/*      */ 
/*      */   int[] javaIncrement(int[] paramArrayOfInt) {
/* 2170 */     int i = 0;
/* 2171 */     for (int j = paramArrayOfInt.length - 1; (j >= 0) && (i == 0); j--)
/* 2172 */       i = paramArrayOfInt[j] += 1;
/* 2173 */     if (i == 0) {
/* 2174 */       paramArrayOfInt = new int[paramArrayOfInt.length + 1];
/* 2175 */       paramArrayOfInt[0] = 1;
/*      */     }
/* 2177 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public BigInteger and(BigInteger paramBigInteger)
/*      */   {
/* 2191 */     int[] arrayOfInt = new int[Math.max(intLength(), paramBigInteger.intLength())];
/* 2192 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 2193 */       arrayOfInt[i] = (getInt(arrayOfInt.length - i - 1) & paramBigInteger.getInt(arrayOfInt.length - i - 1));
/*      */     }
/*      */ 
/* 2196 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public BigInteger or(BigInteger paramBigInteger)
/*      */   {
/* 2208 */     int[] arrayOfInt = new int[Math.max(intLength(), paramBigInteger.intLength())];
/* 2209 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 2210 */       arrayOfInt[i] = (getInt(arrayOfInt.length - i - 1) | paramBigInteger.getInt(arrayOfInt.length - i - 1));
/*      */     }
/*      */ 
/* 2213 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public BigInteger xor(BigInteger paramBigInteger)
/*      */   {
/* 2225 */     int[] arrayOfInt = new int[Math.max(intLength(), paramBigInteger.intLength())];
/* 2226 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 2227 */       arrayOfInt[i] = (getInt(arrayOfInt.length - i - 1) ^ paramBigInteger.getInt(arrayOfInt.length - i - 1));
/*      */     }
/*      */ 
/* 2230 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public BigInteger not()
/*      */   {
/* 2241 */     int[] arrayOfInt = new int[intLength()];
/* 2242 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 2243 */       arrayOfInt[i] = (getInt(arrayOfInt.length - i - 1) ^ 0xFFFFFFFF);
/*      */     }
/* 2245 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public BigInteger andNot(BigInteger paramBigInteger)
/*      */   {
/* 2259 */     int[] arrayOfInt = new int[Math.max(intLength(), paramBigInteger.intLength())];
/* 2260 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 2261 */       arrayOfInt[i] = (getInt(arrayOfInt.length - i - 1) & (paramBigInteger.getInt(arrayOfInt.length - i - 1) ^ 0xFFFFFFFF));
/*      */     }
/*      */ 
/* 2264 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public boolean testBit(int paramInt)
/*      */   {
/* 2279 */     if (paramInt < 0) {
/* 2280 */       throw new ArithmeticException("Negative bit address");
/*      */     }
/* 2282 */     return (getInt(paramInt >>> 5) & 1 << (paramInt & 0x1F)) != 0;
/*      */   }
/*      */ 
/*      */   public BigInteger setBit(int paramInt)
/*      */   {
/* 2294 */     if (paramInt < 0) {
/* 2295 */       throw new ArithmeticException("Negative bit address");
/*      */     }
/* 2297 */     int i = paramInt >>> 5;
/* 2298 */     int[] arrayOfInt = new int[Math.max(intLength(), i + 2)];
/*      */ 
/* 2300 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 2301 */       arrayOfInt[(arrayOfInt.length - j - 1)] = getInt(j);
/*      */     }
/* 2303 */     arrayOfInt[(arrayOfInt.length - i - 1)] |= 1 << (paramInt & 0x1F);
/*      */ 
/* 2305 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public BigInteger clearBit(int paramInt)
/*      */   {
/* 2318 */     if (paramInt < 0) {
/* 2319 */       throw new ArithmeticException("Negative bit address");
/*      */     }
/* 2321 */     int i = paramInt >>> 5;
/* 2322 */     int[] arrayOfInt = new int[Math.max(intLength(), (paramInt + 1 >>> 5) + 1)];
/*      */ 
/* 2324 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 2325 */       arrayOfInt[(arrayOfInt.length - j - 1)] = getInt(j);
/*      */     }
/* 2327 */     arrayOfInt[(arrayOfInt.length - i - 1)] &= (1 << (paramInt & 0x1F) ^ 0xFFFFFFFF);
/*      */ 
/* 2329 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public BigInteger flipBit(int paramInt)
/*      */   {
/* 2342 */     if (paramInt < 0) {
/* 2343 */       throw new ArithmeticException("Negative bit address");
/*      */     }
/* 2345 */     int i = paramInt >>> 5;
/* 2346 */     int[] arrayOfInt = new int[Math.max(intLength(), i + 2)];
/*      */ 
/* 2348 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 2349 */       arrayOfInt[(arrayOfInt.length - j - 1)] = getInt(j);
/*      */     }
/* 2351 */     arrayOfInt[(arrayOfInt.length - i - 1)] ^= 1 << (paramInt & 0x1F);
/*      */ 
/* 2353 */     return valueOf(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public int getLowestSetBit()
/*      */   {
/* 2365 */     int i = this.lowestSetBit - 2;
/* 2366 */     if (i == -2) {
/* 2367 */       i = 0;
/* 2368 */       if (this.signum == 0) {
/* 2369 */         i--;
/*      */       }
/*      */       else
/*      */       {
/*      */         int k;
/* 2373 */         for (int j = 0; (k = getInt(j)) == 0; j++);
/* 2375 */         i += (j << 5) + Integer.numberOfTrailingZeros(k);
/*      */       }
/* 2377 */       this.lowestSetBit = (i + 2);
/*      */     }
/* 2379 */     return i;
/*      */   }
/*      */ 
/*      */   public int bitLength()
/*      */   {
/* 2396 */     int i = this.bitLength - 1;
/* 2397 */     if (i == -1) {
/* 2398 */       int[] arrayOfInt = this.mag;
/* 2399 */       int j = arrayOfInt.length;
/* 2400 */       if (j == 0) {
/* 2401 */         i = 0;
/*      */       }
/*      */       else {
/* 2404 */         int k = (j - 1 << 5) + bitLengthForInt(this.mag[0]);
/* 2405 */         if (this.signum < 0)
/*      */         {
/* 2407 */           int m = Integer.bitCount(this.mag[0]) == 1 ? 1 : 0;
/* 2408 */           for (int n = 1; (n < j) && (m != 0); n++) {
/* 2409 */             m = this.mag[n] == 0 ? 1 : 0;
/*      */           }
/* 2411 */           i = m != 0 ? k - 1 : k;
/*      */         } else {
/* 2413 */           i = k;
/*      */         }
/*      */       }
/* 2416 */       this.bitLength = (i + 1);
/*      */     }
/* 2418 */     return i;
/*      */   }
/*      */ 
/*      */   public int bitCount()
/*      */   {
/* 2430 */     int i = this.bitCount - 1;
/* 2431 */     if (i == -1) {
/* 2432 */       i = 0;
/*      */ 
/* 2434 */       for (int j = 0; j < this.mag.length; j++)
/* 2435 */         i += Integer.bitCount(this.mag[j]);
/* 2436 */       if (this.signum < 0)
/*      */       {
/* 2438 */         j = 0;
/* 2439 */         for (int k = this.mag.length - 1; this.mag[k] == 0; k--)
/* 2440 */           j += 32;
/* 2441 */         j += Integer.numberOfTrailingZeros(this.mag[k]);
/* 2442 */         i += j - 1;
/*      */       }
/* 2444 */       this.bitCount = (i + 1);
/*      */     }
/* 2446 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean isProbablePrime(int paramInt)
/*      */   {
/* 2466 */     if (paramInt <= 0)
/* 2467 */       return true;
/* 2468 */     BigInteger localBigInteger = abs();
/* 2469 */     if (localBigInteger.equals(TWO))
/* 2470 */       return true;
/* 2471 */     if ((!localBigInteger.testBit(0)) || (localBigInteger.equals(ONE))) {
/* 2472 */       return false;
/*      */     }
/* 2474 */     return localBigInteger.primeToCertainty(paramInt, null);
/*      */   }
/*      */ 
/*      */   public int compareTo(BigInteger paramBigInteger)
/*      */   {
/* 2493 */     if (this.signum == paramBigInteger.signum) {
/* 2494 */       switch (this.signum) {
/*      */       case 1:
/* 2496 */         return compareMagnitude(paramBigInteger);
/*      */       case -1:
/* 2498 */         return paramBigInteger.compareMagnitude(this);
/*      */       }
/* 2500 */       return 0;
/*      */     }
/*      */ 
/* 2503 */     return this.signum > paramBigInteger.signum ? 1 : -1;
/*      */   }
/*      */ 
/*      */   final int compareMagnitude(BigInteger paramBigInteger)
/*      */   {
/* 2515 */     int[] arrayOfInt1 = this.mag;
/* 2516 */     int i = arrayOfInt1.length;
/* 2517 */     int[] arrayOfInt2 = paramBigInteger.mag;
/* 2518 */     int j = arrayOfInt2.length;
/* 2519 */     if (i < j)
/* 2520 */       return -1;
/* 2521 */     if (i > j)
/* 2522 */       return 1;
/* 2523 */     for (int k = 0; k < i; k++) {
/* 2524 */       int m = arrayOfInt1[k];
/* 2525 */       int n = arrayOfInt2[k];
/* 2526 */       if (m != n)
/* 2527 */         return (m & 0xFFFFFFFF) < (n & 0xFFFFFFFF) ? -1 : 1;
/*      */     }
/* 2529 */     return 0;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 2541 */     if (paramObject == this) {
/* 2542 */       return true;
/*      */     }
/* 2544 */     if (!(paramObject instanceof BigInteger)) {
/* 2545 */       return false;
/*      */     }
/* 2547 */     BigInteger localBigInteger = (BigInteger)paramObject;
/* 2548 */     if (localBigInteger.signum != this.signum) {
/* 2549 */       return false;
/*      */     }
/* 2551 */     int[] arrayOfInt1 = this.mag;
/* 2552 */     int i = arrayOfInt1.length;
/* 2553 */     int[] arrayOfInt2 = localBigInteger.mag;
/* 2554 */     if (i != arrayOfInt2.length) {
/* 2555 */       return false;
/*      */     }
/* 2557 */     for (int j = 0; j < i; j++) {
/* 2558 */       if (arrayOfInt2[j] != arrayOfInt1[j])
/* 2559 */         return false;
/*      */     }
/* 2561 */     return true;
/*      */   }
/*      */ 
/*      */   public BigInteger min(BigInteger paramBigInteger)
/*      */   {
/* 2572 */     return compareTo(paramBigInteger) < 0 ? this : paramBigInteger;
/*      */   }
/*      */ 
/*      */   public BigInteger max(BigInteger paramBigInteger)
/*      */   {
/* 2583 */     return compareTo(paramBigInteger) > 0 ? this : paramBigInteger;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2595 */     int i = 0;
/*      */ 
/* 2597 */     for (int j = 0; j < this.mag.length; j++) {
/* 2598 */       i = (int)(31 * i + (this.mag[j] & 0xFFFFFFFF));
/*      */     }
/* 2600 */     return i * this.signum;
/*      */   }
/*      */ 
/*      */   public String toString(int paramInt)
/*      */   {
/* 2621 */     if (this.signum == 0)
/* 2622 */       return "0";
/* 2623 */     if ((paramInt < 2) || (paramInt > 36)) {
/* 2624 */       paramInt = 10;
/*      */     }
/*      */ 
/* 2627 */     int i = (4 * this.mag.length + 6) / 7;
/* 2628 */     String[] arrayOfString = new String[i];
/*      */ 
/* 2631 */     Object localObject1 = abs();
/* 2632 */     int j = 0;
/* 2633 */     while (((BigInteger)localObject1).signum != 0) {
/* 2634 */       localObject2 = longRadix[paramInt];
/*      */ 
/* 2636 */       MutableBigInteger localMutableBigInteger1 = new MutableBigInteger();
/* 2637 */       MutableBigInteger localMutableBigInteger2 = new MutableBigInteger(((BigInteger)localObject1).mag);
/* 2638 */       MutableBigInteger localMutableBigInteger3 = new MutableBigInteger(((BigInteger)localObject2).mag);
/* 2639 */       MutableBigInteger localMutableBigInteger4 = localMutableBigInteger2.divide(localMutableBigInteger3, localMutableBigInteger1);
/* 2640 */       BigInteger localBigInteger1 = localMutableBigInteger1.toBigInteger(((BigInteger)localObject1).signum * ((BigInteger)localObject2).signum);
/* 2641 */       BigInteger localBigInteger2 = localMutableBigInteger4.toBigInteger(((BigInteger)localObject1).signum * ((BigInteger)localObject2).signum);
/*      */ 
/* 2643 */       arrayOfString[(j++)] = Long.toString(localBigInteger2.longValue(), paramInt);
/* 2644 */       localObject1 = localBigInteger1;
/*      */     }
/*      */ 
/* 2648 */     Object localObject2 = new StringBuilder(j * digitsPerLong[paramInt] + 1);
/* 2649 */     if (this.signum < 0)
/* 2650 */       ((StringBuilder)localObject2).append('-');
/* 2651 */     ((StringBuilder)localObject2).append(arrayOfString[(j - 1)]);
/*      */ 
/* 2654 */     for (int k = j - 2; k >= 0; k--)
/*      */     {
/* 2656 */       int m = digitsPerLong[paramInt] - arrayOfString[k].length();
/* 2657 */       if (m != 0)
/* 2658 */         ((StringBuilder)localObject2).append(zeros[m]);
/* 2659 */       ((StringBuilder)localObject2).append(arrayOfString[k]);
/*      */     }
/* 2661 */     return ((StringBuilder)localObject2).toString();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2686 */     return toString(10);
/*      */   }
/*      */ 
/*      */   public byte[] toByteArray()
/*      */   {
/* 2704 */     int i = bitLength() / 8 + 1;
/* 2705 */     byte[] arrayOfByte = new byte[i];
/*      */ 
/* 2707 */     int j = i - 1; int k = 4; int m = 0; for (int n = 0; j >= 0; j--) {
/* 2708 */       if (k == 4) {
/* 2709 */         m = getInt(n++);
/* 2710 */         k = 1;
/*      */       } else {
/* 2712 */         m >>>= 8;
/* 2713 */         k++;
/*      */       }
/* 2715 */       arrayOfByte[j] = ((byte)m);
/*      */     }
/* 2717 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public int intValue()
/*      */   {
/* 2735 */     int i = 0;
/* 2736 */     i = getInt(0);
/* 2737 */     return i;
/*      */   }
/*      */ 
/*      */   public long longValue()
/*      */   {
/* 2755 */     long l = 0L;
/*      */ 
/* 2757 */     for (int i = 1; i >= 0; i--)
/* 2758 */       l = (l << 32) + (getInt(i) & 0xFFFFFFFF);
/* 2759 */     return l;
/*      */   }
/*      */ 
/*      */   public float floatValue()
/*      */   {
/* 2779 */     return Float.parseFloat(toString());
/*      */   }
/*      */ 
/*      */   public double doubleValue()
/*      */   {
/* 2799 */     return Double.parseDouble(toString());
/*      */   }
/*      */ 
/*      */   private static int[] stripLeadingZeroInts(int[] paramArrayOfInt)
/*      */   {
/* 2806 */     int i = paramArrayOfInt.length;
/*      */ 
/* 2810 */     for (int j = 0; (j < i) && (paramArrayOfInt[j] == 0); j++);
/* 2812 */     return Arrays.copyOfRange(paramArrayOfInt, j, i);
/*      */   }
/*      */ 
/*      */   private static int[] trustedStripLeadingZeroInts(int[] paramArrayOfInt)
/*      */   {
/* 2820 */     int i = paramArrayOfInt.length;
/*      */ 
/* 2824 */     for (int j = 0; (j < i) && (paramArrayOfInt[j] == 0); j++);
/* 2826 */     return j == 0 ? paramArrayOfInt : Arrays.copyOfRange(paramArrayOfInt, j, i);
/*      */   }
/*      */ 
/*      */   private static int[] stripLeadingZeroBytes(byte[] paramArrayOfByte)
/*      */   {
/* 2833 */     int i = paramArrayOfByte.length;
/*      */ 
/* 2837 */     for (int j = 0; (j < i) && (paramArrayOfByte[j] == 0); j++);
/* 2841 */     int k = i - j + 3 >>> 2;
/* 2842 */     int[] arrayOfInt = new int[k];
/* 2843 */     int m = i - 1;
/* 2844 */     for (int n = k - 1; n >= 0; n--) {
/* 2845 */       arrayOfInt[n] = (paramArrayOfByte[(m--)] & 0xFF);
/* 2846 */       int i1 = m - j + 1;
/* 2847 */       int i2 = Math.min(3, i1);
/* 2848 */       for (int i3 = 8; i3 <= i2 << 3; i3 += 8)
/* 2849 */         arrayOfInt[n] |= (paramArrayOfByte[(m--)] & 0xFF) << i3;
/*      */     }
/* 2851 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static int[] makePositive(byte[] paramArrayOfByte)
/*      */   {
/* 2860 */     int k = paramArrayOfByte.length;
/*      */ 
/* 2863 */     for (int i = 0; (i < k) && (paramArrayOfByte[i] == -1); i++);
/* 2869 */     for (int j = i; (j < k) && (paramArrayOfByte[j] == 0); j++);
/* 2872 */     int m = j == k ? 1 : 0;
/* 2873 */     int n = (k - i + m + 3) / 4;
/* 2874 */     int[] arrayOfInt = new int[n];
/*      */ 
/* 2878 */     int i1 = k - 1;
/* 2879 */     for (int i2 = n - 1; i2 >= 0; i2--) {
/* 2880 */       arrayOfInt[i2] = (paramArrayOfByte[(i1--)] & 0xFF);
/* 2881 */       int i3 = Math.min(3, i1 - i + 1);
/* 2882 */       if (i3 < 0)
/* 2883 */         i3 = 0;
/* 2884 */       for (int i4 = 8; i4 <= 8 * i3; i4 += 8) {
/* 2885 */         arrayOfInt[i2] |= (paramArrayOfByte[(i1--)] & 0xFF) << i4;
/*      */       }
/*      */ 
/* 2888 */       i4 = -1 >>> 8 * (3 - i3);
/* 2889 */       arrayOfInt[i2] = ((arrayOfInt[i2] ^ 0xFFFFFFFF) & i4);
/*      */     }
/*      */ 
/* 2893 */     for (i2 = arrayOfInt.length - 1; i2 >= 0; i2--) {
/* 2894 */       arrayOfInt[i2] = ((int)((arrayOfInt[i2] & 0xFFFFFFFF) + 1L));
/* 2895 */       if (arrayOfInt[i2] != 0) {
/*      */         break;
/*      */       }
/*      */     }
/* 2899 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static int[] makePositive(int[] paramArrayOfInt)
/*      */   {
/* 2910 */     for (int i = 0; (i < paramArrayOfInt.length) && (paramArrayOfInt[i] == -1); i++);
/* 2915 */     for (int j = i; (j < paramArrayOfInt.length) && (paramArrayOfInt[j] == 0); j++);
/* 2917 */     int k = j == paramArrayOfInt.length ? 1 : 0;
/* 2918 */     int[] arrayOfInt = new int[paramArrayOfInt.length - i + k];
/*      */ 
/* 2922 */     for (int m = i; m < paramArrayOfInt.length; m++) {
/* 2923 */       arrayOfInt[(m - i + k)] = (paramArrayOfInt[m] ^ 0xFFFFFFFF);
/*      */     }
/*      */ 
/* 2926 */     for (m = arrayOfInt.length - 1; ; m--) if (arrayOfInt[m] += 1 != 0) {
/*      */         break;
/*      */       }
/* 2929 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private int intLength()
/*      */   {
/* 2994 */     return (bitLength() >>> 5) + 1;
/*      */   }
/*      */ 
/*      */   private int signBit()
/*      */   {
/* 2999 */     return this.signum < 0 ? 1 : 0;
/*      */   }
/*      */ 
/*      */   private int signInt()
/*      */   {
/* 3004 */     return this.signum < 0 ? -1 : 0;
/*      */   }
/*      */ 
/*      */   private int getInt(int paramInt)
/*      */   {
/* 3014 */     if (paramInt < 0)
/* 3015 */       return 0;
/* 3016 */     if (paramInt >= this.mag.length) {
/* 3017 */       return signInt();
/*      */     }
/* 3019 */     int i = this.mag[(this.mag.length - paramInt - 1)];
/*      */ 
/* 3021 */     return paramInt <= firstNonzeroIntNum() ? -i : this.signum >= 0 ? i : i ^ 0xFFFFFFFF;
/*      */   }
/*      */ 
/*      */   private int firstNonzeroIntNum()
/*      */   {
/* 3031 */     int i = this.firstNonzeroIntNum - 2;
/* 3032 */     if (i == -2) {
/* 3033 */       i = 0;
/*      */ 
/* 3037 */       int k = this.mag.length;
/* 3038 */       for (int j = k - 1; (j >= 0) && (this.mag[j] == 0); j--);
/* 3040 */       i = k - j - 1;
/* 3041 */       this.firstNonzeroIntNum = (i + 2);
/*      */     }
/* 3043 */     return i;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 3096 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*      */ 
/* 3099 */     int i = localGetField.get("signum", -2);
/* 3100 */     byte[] arrayOfByte = (byte[])localGetField.get("magnitude", null);
/*      */     String str;
/* 3103 */     if ((i < -1) || (i > 1)) {
/* 3104 */       str = "BigInteger: Invalid signum value";
/* 3105 */       if (localGetField.defaulted("signum"))
/* 3106 */         str = "BigInteger: Signum not present in stream";
/* 3107 */       throw new StreamCorruptedException(str);
/*      */     }
/* 3109 */     if ((arrayOfByte.length == 0 ? 1 : 0) != (i == 0 ? 1 : 0)) {
/* 3110 */       str = "BigInteger: signum-magnitude mismatch";
/* 3111 */       if (localGetField.defaulted("magnitude"))
/* 3112 */         str = "BigInteger: Magnitude not present in stream";
/* 3113 */       throw new StreamCorruptedException(str);
/*      */     }
/*      */ 
/* 3117 */     unsafe.putIntVolatile(this, signumOffset, i);
/*      */ 
/* 3120 */     unsafe.putObjectVolatile(this, magOffset, stripLeadingZeroBytes(arrayOfByte));
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 3149 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 3150 */     localPutField.put("signum", this.signum);
/* 3151 */     localPutField.put("magnitude", magSerializedForm());
/*      */ 
/* 3154 */     localPutField.put("bitCount", -1);
/* 3155 */     localPutField.put("bitLength", -1);
/* 3156 */     localPutField.put("lowestSetBit", -2);
/* 3157 */     localPutField.put("firstNonzeroByteNum", -2);
/*      */ 
/* 3160 */     paramObjectOutputStream.writeFields();
/*      */   }
/*      */ 
/*      */   private byte[] magSerializedForm()
/*      */   {
/* 3167 */     int i = this.mag.length;
/*      */ 
/* 3169 */     int j = i == 0 ? 0 : (i - 1 << 5) + bitLengthForInt(this.mag[0]);
/* 3170 */     int k = j + 7 >>> 3;
/* 3171 */     byte[] arrayOfByte = new byte[k];
/*      */ 
/* 3173 */     int m = k - 1; int n = 4; int i1 = i - 1; int i2 = 0;
/* 3174 */     for (; m >= 0; m--) {
/* 3175 */       if (n == 4) {
/* 3176 */         i2 = this.mag[(i1--)];
/* 3177 */         n = 1;
/*      */       } else {
/* 3179 */         i2 >>>= 8;
/* 3180 */         n++;
/*      */       }
/* 3182 */       arrayOfByte[m] = ((byte)i2);
/*      */     }
/* 3184 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  430 */     bitsPerDigit = new long[] { 0L, 0L, 1024L, 1624L, 2048L, 2378L, 2648L, 2875L, 3072L, 3247L, 3402L, 3543L, 3672L, 3790L, 3899L, 4001L, 4096L, 4186L, 4271L, 4350L, 4426L, 4498L, 4567L, 4633L, 4696L, 4756L, 4814L, 4870L, 4923L, 4975L, 5025L, 5074L, 5120L, 5166L, 5210L, 5253L, 5295L };
/*      */ 
/*  614 */     SMALL_PRIME_PRODUCT = valueOf(152125131763605L);
/*      */ 
/*  997 */     posConst = new BigInteger[17];
/*  998 */     negConst = new BigInteger[17];
/*      */ 
/* 1000 */     for (int i = 1; i <= 16; i++) {
/* 1001 */       int[] arrayOfInt = new int[1];
/* 1002 */       arrayOfInt[0] = i;
/* 1003 */       posConst[i] = new BigInteger(arrayOfInt, 1);
/* 1004 */       negConst[i] = new BigInteger(arrayOfInt, -1);
/*      */     }
/*      */ 
/* 1013 */     ZERO = new BigInteger(new int[0], 0);
/*      */ 
/* 1020 */     ONE = valueOf(1L);
/*      */ 
/* 1025 */     TWO = valueOf(2L);
/*      */ 
/* 1032 */     TEN = valueOf(10L);
/*      */ 
/* 1635 */     bnExpModThreshTable = new int[] { 7, 25, 81, 241, 673, 1793, 2147483647 };
/*      */ 
/* 2665 */     zeros = new String[64];
/*      */ 
/* 2667 */     zeros[63] = "000000000000000000000000000000000000000000000000000000000000000";
/*      */ 
/* 2669 */     for (i = 0; i < 63; i++) {
/* 2670 */       zeros[i] = zeros[63].substring(0, i);
/*      */     }
/*      */ 
/* 2943 */     digitsPerLong = new int[] { 0, 0, 62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12 };
/*      */ 
/* 2947 */     longRadix = new BigInteger[] { null, null, valueOf(4611686018427387904L), valueOf(4052555153018976267L), valueOf(4611686018427387904L), valueOf(7450580596923828125L), valueOf(4738381338321616896L), valueOf(3909821048582988049L), valueOf(1152921504606846976L), valueOf(1350851717672992089L), valueOf(1000000000000000000L), valueOf(5559917313492231481L), valueOf(2218611106740436992L), valueOf(8650415919381337933L), valueOf(2177953337809371136L), valueOf(6568408355712890625L), valueOf(1152921504606846976L), valueOf(2862423051509815793L), valueOf(6746640616477458432L), valueOf(799006685782884121L), valueOf(1638400000000000000L), valueOf(3243919932521508681L), valueOf(6221821273427820544L), valueOf(504036361936467383L), valueOf(876488338465357824L), valueOf(1490116119384765625L), valueOf(2481152873203736576L), valueOf(4052555153018976267L), valueOf(6502111422497947648L), valueOf(353814783205469041L), valueOf(531441000000000000L), valueOf(787662783788549761L), valueOf(1152921504606846976L), valueOf(1667889514952984961L), valueOf(2386420683693101056L), valueOf(3379220508056640625L), valueOf(4738381338321616896L) };
/*      */ 
/* 2970 */     digitsPerInt = new int[] { 0, 0, 30, 19, 15, 13, 11, 11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5 };
/*      */ 
/* 2974 */     intRadix = new int[] { 0, 0, 1073741824, 1162261467, 1073741824, 1220703125, 362797056, 1977326743, 1073741824, 387420489, 1000000000, 214358881, 429981696, 815730721, 1475789056, 170859375, 268435456, 410338673, 612220032, 893871739, 1280000000, 1801088541, 113379904, 148035889, 191102976, 244140625, 308915776, 387420489, 481890304, 594823321, 729000000, 887503681, 1073741824, 1291467969, 1544804416, 1838265625, 60466176 };
/*      */ 
/* 3064 */     serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("signum", Integer.TYPE), new ObjectStreamField("magnitude", [B.class), new ObjectStreamField("bitCount", Integer.TYPE), new ObjectStreamField("bitLength", Integer.TYPE), new ObjectStreamField("firstNonzeroByteNum", Integer.TYPE), new ObjectStreamField("lowestSetBit", Integer.TYPE) };
/*      */ 
/* 3125 */     unsafe = Unsafe.getUnsafe();
/*      */     try
/*      */     {
/* 3130 */       signumOffset = unsafe.objectFieldOffset(BigInteger.class.getDeclaredField("signum"));
/*      */ 
/* 3132 */       magOffset = unsafe.objectFieldOffset(BigInteger.class.getDeclaredField("mag"));
/*      */     }
/*      */     catch (Exception localException) {
/* 3135 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.math.BigInteger
 * JD-Core Version:    0.6.2
 */