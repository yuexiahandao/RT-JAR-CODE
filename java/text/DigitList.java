/*     */ package java.text;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ final class DigitList
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int MAX_COUNT = 19;
/* 102 */   public int decimalAt = 0;
/* 103 */   public int count = 0;
/* 104 */   public char[] digits = new char[19];
/*     */   private char[] data;
/* 107 */   private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
/* 108 */   private boolean isNegative = false;
/*     */ 
/* 678 */   private static final char[] LONG_MIN_REP = "9223372036854775808".toCharArray();
/*     */   private StringBuffer tempBuffer;
/*     */ 
/*     */   boolean isZero()
/*     */   {
/* 114 */     for (int i = 0; i < this.count; i++) {
/* 115 */       if (this.digits[i] != '0') {
/* 116 */         return false;
/*     */       }
/*     */     }
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */   void setRoundingMode(RoundingMode paramRoundingMode)
/*     */   {
/* 126 */     this.roundingMode = paramRoundingMode;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 137 */     this.decimalAt = 0;
/* 138 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public void append(char paramChar)
/*     */   {
/* 145 */     if (this.count == this.digits.length) {
/* 146 */       char[] arrayOfChar = new char[this.count + 100];
/* 147 */       System.arraycopy(this.digits, 0, arrayOfChar, 0, this.count);
/* 148 */       this.digits = arrayOfChar;
/*     */     }
/* 150 */     this.digits[(this.count++)] = paramChar;
/*     */   }
/*     */ 
/*     */   public final double getDouble()
/*     */   {
/* 159 */     if (this.count == 0) {
/* 160 */       return 0.0D;
/*     */     }
/*     */ 
/* 163 */     StringBuffer localStringBuffer = getStringBuffer();
/* 164 */     localStringBuffer.append('.');
/* 165 */     localStringBuffer.append(this.digits, 0, this.count);
/* 166 */     localStringBuffer.append('E');
/* 167 */     localStringBuffer.append(this.decimalAt);
/* 168 */     return Double.parseDouble(localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   public final long getLong()
/*     */   {
/* 178 */     if (this.count == 0) {
/* 179 */       return 0L;
/*     */     }
/*     */ 
/* 185 */     if (isLongMIN_VALUE()) {
/* 186 */       return -9223372036854775808L;
/*     */     }
/*     */ 
/* 189 */     StringBuffer localStringBuffer = getStringBuffer();
/* 190 */     localStringBuffer.append(this.digits, 0, this.count);
/* 191 */     for (int i = this.count; i < this.decimalAt; i++) {
/* 192 */       localStringBuffer.append('0');
/*     */     }
/* 194 */     return Long.parseLong(localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   public final BigDecimal getBigDecimal() {
/* 198 */     if (this.count == 0) {
/* 199 */       if (this.decimalAt == 0) {
/* 200 */         return BigDecimal.ZERO;
/*     */       }
/* 202 */       return new BigDecimal("0E" + this.decimalAt);
/*     */     }
/*     */ 
/* 206 */     if (this.decimalAt == this.count) {
/* 207 */       return new BigDecimal(this.digits, 0, this.count);
/*     */     }
/* 209 */     return new BigDecimal(this.digits, 0, this.count).scaleByPowerOfTen(this.decimalAt - this.count);
/*     */   }
/*     */ 
/*     */   boolean fitsIntoLong(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 229 */     while ((this.count > 0) && (this.digits[(this.count - 1)] == '0')) {
/* 230 */       this.count -= 1;
/*     */     }
/*     */ 
/* 233 */     if (this.count == 0)
/*     */     {
/* 236 */       return (paramBoolean1) || (paramBoolean2);
/*     */     }
/*     */ 
/* 239 */     if ((this.decimalAt < this.count) || (this.decimalAt > 19)) {
/* 240 */       return false;
/*     */     }
/*     */ 
/* 243 */     if (this.decimalAt < 19) return true;
/*     */ 
/* 248 */     for (int i = 0; i < this.count; i++) {
/* 249 */       int j = this.digits[i]; int k = LONG_MIN_REP[i];
/* 250 */       if (j > k) return false;
/* 251 */       if (j < k) return true;
/*     */ 
/*     */     }
/*     */ 
/* 256 */     if (this.count < this.decimalAt) return true;
/*     */ 
/* 261 */     return !paramBoolean1;
/*     */   }
/*     */ 
/*     */   public final void set(boolean paramBoolean, double paramDouble, int paramInt)
/*     */   {
/* 274 */     set(paramBoolean, paramDouble, paramInt, true);
/*     */   }
/*     */ 
/*     */   final void set(boolean paramBoolean1, double paramDouble, int paramInt, boolean paramBoolean2)
/*     */   {
/* 289 */     set(paramBoolean1, Double.toString(paramDouble), paramInt, paramBoolean2);
/*     */   }
/*     */ 
/*     */   final void set(boolean paramBoolean1, String paramString, int paramInt, boolean paramBoolean2)
/*     */   {
/* 297 */     this.isNegative = paramBoolean1;
/* 298 */     int i = paramString.length();
/* 299 */     char[] arrayOfChar = getDataChars(i);
/* 300 */     paramString.getChars(0, i, arrayOfChar, 0);
/*     */ 
/* 302 */     this.decimalAt = -1;
/* 303 */     this.count = 0;
/* 304 */     int j = 0;
/*     */ 
/* 307 */     int k = 0;
/* 308 */     int m = 0;
/*     */ 
/* 310 */     for (int n = 0; n < i; ) {
/* 311 */       int i1 = arrayOfChar[(n++)];
/* 312 */       if (i1 == 46) {
/* 313 */         this.decimalAt = this.count; } else {
/* 314 */         if ((i1 == 101) || (i1 == 69)) {
/* 315 */           j = parseInt(arrayOfChar, n, i);
/* 316 */           break;
/*     */         }
/* 318 */         if (m == 0) {
/* 319 */           m = i1 != 48 ? 1 : 0;
/* 320 */           if ((m == 0) && (this.decimalAt != -1))
/* 321 */             k++;
/*     */         }
/* 323 */         if (m != 0) {
/* 324 */           this.digits[(this.count++)] = i1;
/*     */         }
/*     */       }
/*     */     }
/* 328 */     if (this.decimalAt == -1) {
/* 329 */       this.decimalAt = this.count;
/*     */     }
/* 331 */     if (m != 0) {
/* 332 */       this.decimalAt += j - k;
/*     */     }
/*     */ 
/* 335 */     if (paramBoolean2)
/*     */     {
/* 341 */       if (-this.decimalAt > paramInt)
/*     */       {
/* 344 */         this.count = 0;
/* 345 */         return;
/* 346 */       }if (-this.decimalAt == paramInt)
/*     */       {
/* 349 */         if (shouldRoundUp(0)) {
/* 350 */           this.count = 1;
/* 351 */           this.decimalAt += 1;
/* 352 */           this.digits[0] = '1';
/*     */         } else {
/* 354 */           this.count = 0;
/*     */         }
/* 356 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 362 */     while ((this.count > 1) && (this.digits[(this.count - 1)] == '0')) {
/* 363 */       this.count -= 1;
/*     */     }
/*     */ 
/* 368 */     round(paramBoolean2 ? paramInt + this.decimalAt : paramInt);
/*     */   }
/*     */ 
/*     */   private final void round(int paramInt)
/*     */   {
/* 379 */     if ((paramInt >= 0) && (paramInt < this.count)) {
/* 380 */       if (shouldRoundUp(paramInt))
/*     */       {
/*     */         while (true)
/*     */         {
/* 385 */           paramInt--;
/* 386 */           if (paramInt < 0)
/*     */           {
/* 389 */             this.digits[0] = '1';
/* 390 */             this.decimalAt += 1;
/* 391 */             paramInt = 0;
/*     */           }
/*     */           else
/*     */           {
/* 396 */             int tmp55_54 = paramInt;
/*     */             char[] tmp55_51 = this.digits; tmp55_51[tmp55_54] = ((char)(tmp55_51[tmp55_54] + '\001'));
/* 396 */             if (this.digits[paramInt] <= '9') break;
/*     */           }
/*     */         }
/* 399 */         paramInt++;
/*     */       }
/* 401 */       this.count = paramInt;
/*     */ 
/* 404 */       while ((this.count > 1) && (this.digits[(this.count - 1)] == '0'))
/* 405 */         this.count -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean shouldRoundUp(int paramInt)
/*     */   {
/* 427 */     if (paramInt < this.count)
/*     */     {
/*     */       int i;
/* 428 */       switch (1.$SwitchMap$java$math$RoundingMode[this.roundingMode.ordinal()]) {
/*     */       case 1:
/* 430 */         for (i = paramInt; i < this.count; i++) {
/* 431 */           if (this.digits[i] != '0') {
/* 432 */             return true;
/*     */           }
/*     */         }
/* 435 */         break;
/*     */       case 2:
/* 437 */         break;
/*     */       case 3:
/* 439 */         for (i = paramInt; i < this.count; i++) {
/* 440 */           if (this.digits[i] != '0') {
/* 441 */             return !this.isNegative;
/*     */           }
/*     */         }
/* 444 */         break;
/*     */       case 4:
/* 446 */         for (i = paramInt; i < this.count; i++) {
/* 447 */           if (this.digits[i] != '0') {
/* 448 */             return this.isNegative;
/*     */           }
/*     */         }
/* 451 */         break;
/*     */       case 5:
/* 453 */         if (this.digits[paramInt] >= '5') {
/* 454 */           return true;
/*     */         }
/*     */         break;
/*     */       case 6:
/* 458 */         if (this.digits[paramInt] > '5')
/* 459 */           return true;
/* 460 */         if (this.digits[paramInt] == '5')
/* 461 */           for (i = paramInt + 1; i < this.count; i++)
/* 462 */             if (this.digits[i] != '0')
/* 463 */               return true;
/* 461 */         break;
/*     */       case 7:
/* 470 */         if (this.digits[paramInt] > '5')
/* 471 */           return true;
/* 472 */         if (this.digits[paramInt] == '5') {
/* 473 */           for (i = paramInt + 1; i < this.count; i++) {
/* 474 */             if (this.digits[i] != '0') {
/* 475 */               return true;
/*     */             }
/*     */           }
/* 478 */           return (paramInt > 0) && (this.digits[(paramInt - 1)] % '\002' != 0);
/*     */         }
/*     */         break;
/*     */       case 8:
/* 482 */         for (i = paramInt; i < this.count; i++) {
/* 483 */           if (this.digits[i] != '0') {
/* 484 */             throw new ArithmeticException("Rounding needed with the rounding mode being set to RoundingMode.UNNECESSARY");
/*     */           }
/*     */         }
/*     */ 
/* 488 */         break;
/*     */       default:
/* 490 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*     */       }
/*     */     }
/* 493 */     return false;
/*     */   }
/*     */ 
/*     */   public final void set(boolean paramBoolean, long paramLong)
/*     */   {
/* 500 */     set(paramBoolean, paramLong, 0);
/*     */   }
/*     */ 
/*     */   public final void set(boolean paramBoolean, long paramLong, int paramInt)
/*     */   {
/* 513 */     this.isNegative = paramBoolean;
/*     */ 
/* 521 */     if (paramLong <= 0L) {
/* 522 */       if (paramLong == -9223372036854775808L) {
/* 523 */         this.decimalAt = (this.count = 19);
/* 524 */         System.arraycopy(LONG_MIN_REP, 0, this.digits, 0, this.count);
/*     */       } else {
/* 526 */         this.decimalAt = (this.count = 0);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 531 */       int i = 19;
/*     */ 
/* 533 */       while (paramLong > 0L) {
/* 534 */         this.digits[(--i)] = ((char)(int)(48L + paramLong % 10L));
/* 535 */         paramLong /= 10L;
/*     */       }
/* 537 */       this.decimalAt = (19 - i);
/*     */ 
/* 540 */       for (int j = 18; this.digits[j] == '0'; j--);
/* 542 */       this.count = (j - i + 1);
/* 543 */       System.arraycopy(this.digits, i, this.digits, 0, this.count);
/*     */     }
/* 545 */     if (paramInt > 0) round(paramInt);
/*     */   }
/*     */ 
/*     */   final void set(boolean paramBoolean1, BigDecimal paramBigDecimal, int paramInt, boolean paramBoolean2)
/*     */   {
/* 559 */     String str = paramBigDecimal.toString();
/* 560 */     extendDigits(str.length());
/*     */ 
/* 562 */     set(paramBoolean1, str, paramInt, paramBoolean2);
/*     */   }
/*     */ 
/*     */   final void set(boolean paramBoolean, BigInteger paramBigInteger, int paramInt)
/*     */   {
/* 574 */     this.isNegative = paramBoolean;
/* 575 */     String str = paramBigInteger.toString();
/* 576 */     int i = str.length();
/* 577 */     extendDigits(i);
/* 578 */     str.getChars(0, i, this.digits, 0);
/*     */ 
/* 580 */     this.decimalAt = i;
/*     */ 
/* 582 */     for (int j = i - 1; (j >= 0) && (this.digits[j] == '0'); j--);
/* 584 */     this.count = (j + 1);
/*     */ 
/* 586 */     if (paramInt > 0)
/* 587 */       round(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 595 */     if (this == paramObject)
/* 596 */       return true;
/* 597 */     if (!(paramObject instanceof DigitList))
/* 598 */       return false;
/* 599 */     DigitList localDigitList = (DigitList)paramObject;
/* 600 */     if ((this.count != localDigitList.count) || (this.decimalAt != localDigitList.decimalAt))
/*     */     {
/* 602 */       return false;
/* 603 */     }for (int i = 0; i < this.count; i++)
/* 604 */       if (this.digits[i] != localDigitList.digits[i])
/* 605 */         return false;
/* 606 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 613 */     int i = this.decimalAt;
/*     */ 
/* 615 */     for (int j = 0; j < this.count; j++) {
/* 616 */       i = i * 37 + this.digits[j];
/*     */     }
/*     */ 
/* 619 */     return i;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 628 */       DigitList localDigitList = (DigitList)super.clone();
/* 629 */       char[] arrayOfChar = new char[this.digits.length];
/* 630 */       System.arraycopy(this.digits, 0, arrayOfChar, 0, this.digits.length);
/* 631 */       localDigitList.digits = arrayOfChar;
/* 632 */       localDigitList.tempBuffer = null;
/* 633 */       return localDigitList; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 635 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   private boolean isLongMIN_VALUE()
/*     */   {
/* 644 */     if ((this.decimalAt != this.count) || (this.count != 19)) {
/* 645 */       return false;
/*     */     }
/*     */ 
/* 648 */     for (int i = 0; i < this.count; i++) {
/* 649 */       if (this.digits[i] != LONG_MIN_REP[i]) return false;
/*     */     }
/*     */ 
/* 652 */     return true;
/*     */   }
/*     */ 
/*     */   private static final int parseInt(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 657 */     int j = 1;
/*     */     int i;
/* 658 */     if ((i = paramArrayOfChar[paramInt1]) == '-') {
/* 659 */       j = 0;
/* 660 */       paramInt1++;
/* 661 */     } else if (i == 43) {
/* 662 */       paramInt1++;
/*     */     }
/*     */ 
/* 665 */     int k = 0;
/* 666 */     while (paramInt1 < paramInt2) {
/* 667 */       i = paramArrayOfChar[(paramInt1++)];
/* 668 */       if ((i < 48) || (i > 57)) break;
/* 669 */       k = k * 10 + (i - 48);
/*     */     }
/*     */ 
/* 674 */     return j != 0 ? k : -k;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 681 */     if (isZero()) {
/* 682 */       return "0";
/*     */     }
/* 684 */     StringBuffer localStringBuffer = getStringBuffer();
/* 685 */     localStringBuffer.append("0.");
/* 686 */     localStringBuffer.append(this.digits, 0, this.count);
/* 687 */     localStringBuffer.append("x10^");
/* 688 */     localStringBuffer.append(this.decimalAt);
/* 689 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private StringBuffer getStringBuffer()
/*     */   {
/* 695 */     if (this.tempBuffer == null)
/* 696 */       this.tempBuffer = new StringBuffer(19);
/*     */     else {
/* 698 */       this.tempBuffer.setLength(0);
/*     */     }
/* 700 */     return this.tempBuffer;
/*     */   }
/*     */ 
/*     */   private void extendDigits(int paramInt) {
/* 704 */     if (paramInt > this.digits.length)
/* 705 */       this.digits = new char[paramInt];
/*     */   }
/*     */ 
/*     */   private final char[] getDataChars(int paramInt)
/*     */   {
/* 710 */     if ((this.data == null) || (this.data.length < paramInt)) {
/* 711 */       this.data = new char[paramInt];
/*     */     }
/* 713 */     return this.data;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.DigitList
 * JD-Core Version:    0.6.2
 */