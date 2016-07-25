/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class BerDecoder
/*     */ {
/*     */   public static final int BooleanTag = 1;
/*     */   public static final int IntegerTag = 2;
/*     */   public static final int OctetStringTag = 4;
/*     */   public static final int NullTag = 5;
/*     */   public static final int OidTag = 6;
/*     */   public static final int SequenceTag = 48;
/*     */   private final byte[] bytes;
/* 746 */   private int next = 0;
/*     */ 
/* 754 */   private final int[] stackBuf = new int['È'];
/* 755 */   private int stackTop = 0;
/*     */ 
/*     */   public BerDecoder(byte[] paramArrayOfByte)
/*     */   {
/*  60 */     this.bytes = paramArrayOfByte;
/*  61 */     reset();
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  65 */     this.next = 0;
/*  66 */     this.stackTop = 0;
/*     */   }
/*     */ 
/*     */   public int fetchInteger()
/*     */     throws BerException
/*     */   {
/*  78 */     return fetchInteger(2);
/*     */   }
/*     */ 
/*     */   public int fetchInteger(int paramInt)
/*     */     throws BerException
/*     */   {
/*  94 */     int i = 0;
/*  95 */     int j = this.next;
/*     */     try {
/*  97 */       if (fetchTag() != paramInt) {
/*  98 */         throw new BerException();
/*     */       }
/* 100 */       i = fetchIntegerValue();
/*     */     }
/*     */     catch (BerException localBerException) {
/* 103 */       this.next = j;
/* 104 */       throw localBerException;
/*     */     }
/*     */ 
/* 107 */     return i;
/*     */   }
/*     */ 
/*     */   public long fetchIntegerAsLong()
/*     */     throws BerException
/*     */   {
/* 121 */     return fetchIntegerAsLong(2);
/*     */   }
/*     */ 
/*     */   public long fetchIntegerAsLong(int paramInt)
/*     */     throws BerException
/*     */   {
/* 137 */     long l = 0L;
/* 138 */     int i = this.next;
/*     */     try {
/* 140 */       if (fetchTag() != paramInt) {
/* 141 */         throw new BerException();
/*     */       }
/* 143 */       l = fetchIntegerValueAsLong();
/*     */     }
/*     */     catch (BerException localBerException) {
/* 146 */       this.next = i;
/* 147 */       throw localBerException;
/*     */     }
/*     */ 
/* 150 */     return l;
/*     */   }
/*     */ 
/*     */   public byte[] fetchOctetString()
/*     */     throws BerException
/*     */   {
/* 164 */     return fetchOctetString(4);
/*     */   }
/*     */ 
/*     */   public byte[] fetchOctetString(int paramInt)
/*     */     throws BerException
/*     */   {
/* 180 */     byte[] arrayOfByte = null;
/* 181 */     int i = this.next;
/*     */     try {
/* 183 */       if (fetchTag() != paramInt) {
/* 184 */         throw new BerException();
/*     */       }
/* 186 */       arrayOfByte = fetchStringValue();
/*     */     }
/*     */     catch (BerException localBerException) {
/* 189 */       this.next = i;
/* 190 */       throw localBerException;
/*     */     }
/*     */ 
/* 193 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public long[] fetchOid()
/*     */     throws BerException
/*     */   {
/* 204 */     return fetchOid(6);
/*     */   }
/*     */ 
/*     */   public long[] fetchOid(int paramInt)
/*     */     throws BerException
/*     */   {
/* 220 */     long[] arrayOfLong = null;
/* 221 */     int i = this.next;
/*     */     try {
/* 223 */       if (fetchTag() != paramInt) {
/* 224 */         throw new BerException();
/*     */       }
/* 226 */       arrayOfLong = fetchOidValue();
/*     */     }
/*     */     catch (BerException localBerException) {
/* 229 */       this.next = i;
/* 230 */       throw localBerException;
/*     */     }
/*     */ 
/* 233 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public void fetchNull()
/*     */     throws BerException
/*     */   {
/* 244 */     fetchNull(5);
/*     */   }
/*     */ 
/*     */   public void fetchNull(int paramInt)
/*     */     throws BerException
/*     */   {
/* 258 */     int i = this.next;
/*     */     try {
/* 260 */       if (fetchTag() != paramInt) {
/* 261 */         throw new BerException();
/*     */       }
/* 263 */       int j = fetchLength();
/* 264 */       if (j != 0) throw new BerException(); 
/*     */     }
/*     */     catch (BerException localBerException)
/*     */     {
/* 267 */       this.next = i;
/* 268 */       throw localBerException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] fetchAny()
/*     */     throws BerException
/*     */   {
/* 284 */     Object localObject = null;
/* 285 */     int i = this.next;
/*     */     try {
/* 287 */       int j = fetchTag();
/* 288 */       int k = fetchLength();
/* 289 */       if (k < 0) throw new BerException();
/* 290 */       int m = this.next + k - i;
/* 291 */       if (k > this.bytes.length - this.next)
/* 292 */         throw new IndexOutOfBoundsException("Decoded length exceeds buffer");
/* 293 */       byte[] arrayOfByte = new byte[m];
/* 294 */       System.arraycopy(this.bytes, i, arrayOfByte, 0, m);
/*     */ 
/* 298 */       this.next += k;
/* 299 */       localObject = arrayOfByte;
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 302 */       this.next = i;
/* 303 */       throw new BerException();
/*     */     }
/*     */ 
/* 310 */     return localObject;
/*     */   }
/*     */ 
/*     */   public byte[] fetchAny(int paramInt)
/*     */     throws BerException
/*     */   {
/* 325 */     if (getTag() != paramInt) {
/* 326 */       throw new BerException();
/*     */     }
/* 328 */     return fetchAny();
/*     */   }
/*     */ 
/*     */   public void openSequence()
/*     */     throws BerException
/*     */   {
/* 342 */     openSequence(48);
/*     */   }
/*     */ 
/*     */   public void openSequence(int paramInt)
/*     */     throws BerException
/*     */   {
/* 356 */     int i = this.next;
/*     */     try {
/* 358 */       if (fetchTag() != paramInt) {
/* 359 */         throw new BerException();
/*     */       }
/* 361 */       int j = fetchLength();
/* 362 */       if (j < 0) throw new BerException();
/* 363 */       if (j > this.bytes.length - this.next) throw new BerException();
/* 364 */       this.stackBuf[(this.stackTop++)] = (this.next + j);
/*     */     }
/*     */     catch (BerException localBerException) {
/* 367 */       this.next = i;
/* 368 */       throw localBerException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void closeSequence()
/*     */     throws BerException
/*     */   {
/* 383 */     if (this.stackBuf[(this.stackTop - 1)] == this.next) {
/* 384 */       this.stackTop -= 1;
/*     */     }
/*     */     else
/* 387 */       throw new BerException();
/*     */   }
/*     */ 
/*     */   public boolean cannotCloseSequence()
/*     */   {
/* 401 */     return this.next < this.stackBuf[(this.stackTop - 1)];
/*     */   }
/*     */ 
/*     */   public int getTag()
/*     */     throws BerException
/*     */   {
/* 413 */     int i = 0;
/* 414 */     int j = this.next;
/*     */     try {
/* 416 */       i = fetchTag();
/*     */     }
/*     */     finally {
/* 419 */       this.next = j;
/*     */     }
/*     */ 
/* 422 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 428 */     StringBuffer localStringBuffer = new StringBuffer(this.bytes.length * 2);
/* 429 */     for (int i = 0; i < this.bytes.length; i++) {
/* 430 */       int j = this.bytes[i] > 0 ? this.bytes[i] : this.bytes[i] + 256;
/* 431 */       if (i == this.next) {
/* 432 */         localStringBuffer.append("(");
/*     */       }
/* 434 */       localStringBuffer.append(Character.forDigit(j / 16, 16));
/* 435 */       localStringBuffer.append(Character.forDigit(j % 16, 16));
/* 436 */       if (i == this.next) {
/* 437 */         localStringBuffer.append(")");
/*     */       }
/*     */     }
/* 440 */     if (this.bytes.length == this.next) {
/* 441 */       localStringBuffer.append("()");
/*     */     }
/*     */ 
/* 444 */     return new String(localStringBuffer);
/*     */   }
/*     */ 
/*     */   private final int fetchTag()
/*     */     throws BerException
/*     */   {
/* 472 */     int i = 0;
/* 473 */     int j = this.next;
/*     */     try
/*     */     {
/* 476 */       int k = this.bytes[(this.next++)];
/* 477 */       i = k >= 0 ? k : k + 256;
/* 478 */       if ((i & 0x1F) == 31)
/* 479 */         while ((this.bytes[this.next] & 0x80) != 0) {
/* 480 */           i <<= 7;
/* 481 */           i |= this.bytes[(this.next++)] & 0x7F;
/*     */         }
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */     {
/* 486 */       this.next = j;
/* 487 */       throw new BerException();
/*     */     }
/*     */ 
/* 490 */     return i;
/*     */   }
/*     */ 
/*     */   private final int fetchLength()
/*     */     throws BerException
/*     */   {
/* 501 */     int i = 0;
/* 502 */     int j = this.next;
/*     */     try
/*     */     {
/* 505 */       int k = this.bytes[(this.next++)];
/* 506 */       if (k >= 0) {
/* 507 */         i = k;
/*     */       }
/*     */       else
/* 510 */         for (int m = 128 + k; m > 0; m--) {
/* 511 */           int n = this.bytes[(this.next++)];
/* 512 */           i <<= 8;
/* 513 */           i |= (n >= 0 ? n : n + 256);
/*     */         }
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */     {
/* 518 */       this.next = j;
/* 519 */       throw new BerException();
/*     */     }
/*     */ 
/* 522 */     return i;
/*     */   }
/*     */ 
/*     */   private int fetchIntegerValue()
/*     */     throws BerException
/*     */   {
/* 533 */     int i = 0;
/* 534 */     int j = this.next;
/*     */     try
/*     */     {
/* 537 */       int k = fetchLength();
/* 538 */       if (k <= 0) throw new BerException();
/* 539 */       if (k > this.bytes.length - this.next) throw new IndexOutOfBoundsException("Decoded length exceeds buffer");
/*     */ 
/* 541 */       int m = this.next + k;
/* 542 */       i = this.bytes[(this.next++)];
/* 543 */       while (this.next < m) {
/* 544 */         int n = this.bytes[(this.next++)];
/* 545 */         if (n < 0) {
/* 546 */           i = i << 8 | 256 + n;
/*     */         }
/*     */         else
/* 549 */           i = i << 8 | n;
/*     */       }
/*     */     }
/*     */     catch (BerException localBerException)
/*     */     {
/* 554 */       this.next = j;
/* 555 */       throw localBerException;
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 558 */       this.next = j;
/* 559 */       throw new BerException();
/*     */     }
/*     */     catch (ArithmeticException localArithmeticException) {
/* 562 */       this.next = j;
/* 563 */       throw new BerException();
/*     */     }
/* 565 */     return i;
/*     */   }
/*     */ 
/*     */   private final long fetchIntegerValueAsLong()
/*     */     throws BerException
/*     */   {
/* 578 */     long l = 0L;
/* 579 */     int i = this.next;
/*     */     try
/*     */     {
/* 582 */       int j = fetchLength();
/* 583 */       if (j <= 0) throw new BerException();
/* 584 */       if (j > this.bytes.length - this.next) throw new IndexOutOfBoundsException("Decoded length exceeds buffer");
/*     */ 
/* 587 */       int k = this.next + j;
/* 588 */       l = this.bytes[(this.next++)];
/* 589 */       while (this.next < k) {
/* 590 */         int m = this.bytes[(this.next++)];
/* 591 */         if (m < 0) {
/* 592 */           l = l << 8 | 256 + m;
/*     */         }
/*     */         else
/* 595 */           l = l << 8 | m;
/*     */       }
/*     */     }
/*     */     catch (BerException localBerException)
/*     */     {
/* 600 */       this.next = i;
/* 601 */       throw localBerException;
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 604 */       this.next = i;
/* 605 */       throw new BerException();
/*     */     }
/*     */     catch (ArithmeticException localArithmeticException) {
/* 608 */       this.next = i;
/* 609 */       throw new BerException();
/*     */     }
/* 611 */     return l;
/*     */   }
/*     */ 
/*     */   private byte[] fetchStringValue()
/*     */     throws BerException
/*     */   {
/* 622 */     Object localObject = null;
/* 623 */     int i = this.next;
/*     */     try
/*     */     {
/* 626 */       int j = fetchLength();
/* 627 */       if (j < 0) throw new BerException();
/* 628 */       if (j > this.bytes.length - this.next)
/* 629 */         throw new IndexOutOfBoundsException("Decoded length exceeds buffer");
/* 630 */       byte[] arrayOfByte = new byte[j];
/* 631 */       System.arraycopy(this.bytes, this.next, arrayOfByte, 0, j);
/* 632 */       this.next += j;
/*     */ 
/* 637 */       localObject = arrayOfByte;
/*     */     }
/*     */     catch (BerException localBerException) {
/* 640 */       this.next = i;
/* 641 */       throw localBerException;
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 644 */       this.next = i;
/* 645 */       throw new BerException();
/*     */     }
/*     */     catch (ArithmeticException localArithmeticException) {
/* 648 */       this.next = i;
/* 649 */       throw new BerException();
/*     */     }
/*     */ 
/* 656 */     return localObject;
/*     */   }
/*     */ 
/*     */   private final long[] fetchOidValue()
/*     */     throws BerException
/*     */   {
/* 668 */     Object localObject = null;
/* 669 */     int i = this.next;
/*     */     try
/*     */     {
/* 672 */       int j = fetchLength();
/* 673 */       if (j <= 0) throw new BerException();
/* 674 */       if (j > this.bytes.length - this.next) {
/* 675 */         throw new IndexOutOfBoundsException("Decoded length exceeds buffer");
/*     */       }
/*     */ 
/* 678 */       int k = 2;
/* 679 */       for (int m = 1; m < j; m++) {
/* 680 */         if ((this.bytes[(this.next + m)] & 0x80) == 0) {
/* 681 */           k++;
/*     */         }
/*     */       }
/* 684 */       m = k;
/* 685 */       long[] arrayOfLong = new long[m];
/* 686 */       int n = this.bytes[(this.next++)];
/*     */ 
/* 690 */       if (n < 0) throw new BerException();
/*     */ 
/* 694 */       long l1 = n / 40;
/* 695 */       if (l1 > 2L) throw new BerException();
/*     */ 
/* 697 */       long l2 = n % 40;
/* 698 */       arrayOfLong[0] = l1;
/* 699 */       arrayOfLong[1] = l2;
/* 700 */       int i1 = 2;
/* 701 */       while (i1 < m) {
/* 702 */         long l3 = 0L;
/* 703 */         int i2 = this.bytes[(this.next++)];
/* 704 */         while ((i2 & 0x80) != 0) {
/* 705 */           l3 = l3 << 7 | i2 & 0x7F;
/*     */ 
/* 707 */           if (l3 < 0L) throw new BerException();
/* 708 */           i2 = this.bytes[(this.next++)];
/*     */         }
/* 710 */         l3 = l3 << 7 | i2;
/*     */ 
/* 712 */         if (l3 < 0L) throw new BerException();
/* 713 */         arrayOfLong[(i1++)] = l3;
/*     */       }
/* 715 */       localObject = arrayOfLong;
/*     */     }
/*     */     catch (BerException localBerException) {
/* 718 */       this.next = i;
/* 719 */       throw localBerException;
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 722 */       this.next = i;
/* 723 */       throw new BerException();
/*     */     }
/*     */ 
/* 730 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.BerDecoder
 * JD-Core Version:    0.6.2
 */