/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.InvalidSearchFilterException;
/*     */ 
/*     */ final class Filter
/*     */ {
/*     */   private static final boolean dbg = false;
/* 824 */   private static int dbgIndent = 0;
/*     */   static final int LDAP_FILTER_AND = 160;
/*     */   static final int LDAP_FILTER_OR = 161;
/*     */   static final int LDAP_FILTER_NOT = 162;
/*     */   static final int LDAP_FILTER_EQUALITY = 163;
/*     */   static final int LDAP_FILTER_SUBSTRINGS = 164;
/*     */   static final int LDAP_FILTER_GE = 165;
/*     */   static final int LDAP_FILTER_LE = 166;
/*     */   static final int LDAP_FILTER_PRESENT = 135;
/*     */   static final int LDAP_FILTER_APPROX = 168;
/*     */   static final int LDAP_FILTER_EXT = 169;
/*     */   static final int LDAP_FILTER_EXT_RULE = 129;
/*     */   static final int LDAP_FILTER_EXT_TYPE = 130;
/*     */   static final int LDAP_FILTER_EXT_VAL = 131;
/*     */   static final int LDAP_FILTER_EXT_DN = 132;
/*     */   static final int LDAP_SUBSTRING_INITIAL = 128;
/*     */   static final int LDAP_SUBSTRING_ANY = 129;
/*     */   static final int LDAP_SUBSTRING_FINAL = 130;
/*     */ 
/*     */   static void encodeFilterString(BerEncoder paramBerEncoder, String paramString, boolean paramBoolean)
/*     */     throws IOException, NamingException
/*     */   {
/*  56 */     if ((paramString == null) || (paramString.equals("")))
/*  57 */       throw new InvalidSearchFilterException("Empty filter");
/*     */     byte[] arrayOfByte;
/*  61 */     if (paramBoolean)
/*  62 */       arrayOfByte = paramString.getBytes("UTF8");
/*     */     else {
/*  64 */       arrayOfByte = paramString.getBytes("8859_1");
/*     */     }
/*  66 */     int i = arrayOfByte.length;
/*     */ 
/*  74 */     encodeFilter(paramBerEncoder, arrayOfByte, 0, i);
/*     */   }
/*     */ 
/*     */   private static void encodeFilter(BerEncoder paramBerEncoder, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException, NamingException
/*     */   {
/*  85 */     if (paramInt2 - paramInt1 <= 0) {
/*  86 */       throw new InvalidSearchFilterException("Empty filter");
/*     */     }
/*     */ 
/*  93 */     int j = 0;
/*     */ 
/*  95 */     int[] arrayOfInt = new int[1];
/*     */ 
/*  97 */     for (arrayOfInt[0] = paramInt1; arrayOfInt[0] < paramInt2; ) {
/*  98 */       switch (paramArrayOfByte[arrayOfInt[0]]) {
/*     */       case 40:
/* 100 */         arrayOfInt[0] += 1;
/* 101 */         j++;
/* 102 */         switch (paramArrayOfByte[arrayOfInt[0]]) {
/*     */         case 38:
/* 104 */           encodeComplexFilter(paramBerEncoder, paramArrayOfByte, 160, arrayOfInt, paramInt2);
/*     */ 
/* 107 */           j--;
/* 108 */           break;
/*     */         case 124:
/* 111 */           encodeComplexFilter(paramBerEncoder, paramArrayOfByte, 161, arrayOfInt, paramInt2);
/*     */ 
/* 114 */           j--;
/* 115 */           break;
/*     */         case 33:
/* 118 */           encodeComplexFilter(paramBerEncoder, paramArrayOfByte, 162, arrayOfInt, paramInt2);
/*     */ 
/* 121 */           j--;
/* 122 */           break;
/*     */         default:
/* 125 */           int k = 1;
/* 126 */           int m = 0;
/* 127 */           int i = arrayOfInt[0];
/* 128 */           while ((i < paramInt2) && (k > 0)) {
/* 129 */             if (m == 0) {
/* 130 */               if (paramArrayOfByte[i] == 40)
/* 131 */                 k++;
/* 132 */               else if (paramArrayOfByte[i] == 41)
/* 133 */                 k--;
/*     */             }
/* 135 */             if ((paramArrayOfByte[i] == 92) && (m == 0))
/* 136 */               m = 1;
/*     */             else
/* 138 */               m = 0;
/* 139 */             if (k > 0)
/* 140 */               i++;
/*     */           }
/* 142 */           if (k != 0) {
/* 143 */             throw new InvalidSearchFilterException("Unbalanced parenthesis");
/*     */           }
/*     */ 
/* 146 */           encodeSimpleFilter(paramBerEncoder, paramArrayOfByte, arrayOfInt[0], i);
/*     */ 
/* 149 */           arrayOfInt[0] = (i + 1);
/*     */ 
/* 151 */           j--;
/* 152 */         }break;
/*     */       case 41:
/* 161 */         paramBerEncoder.endSeq();
/* 162 */         arrayOfInt[0] += 1;
/* 163 */         j--;
/* 164 */         break;
/*     */       case 32:
/* 167 */         arrayOfInt[0] += 1;
/* 168 */         break;
/*     */       default:
/* 171 */         encodeSimpleFilter(paramBerEncoder, paramArrayOfByte, arrayOfInt[0], paramInt2);
/* 172 */         arrayOfInt[0] = paramInt2;
/*     */       }
/*     */ 
/* 176 */       if (j < 0) {
/* 177 */         throw new InvalidSearchFilterException("Unbalanced parenthesis");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 182 */     if (j != 0)
/* 183 */       throw new InvalidSearchFilterException("Unbalanced parenthesis");
/*     */   }
/*     */ 
/*     */   private static int hexchar2int(byte paramByte)
/*     */   {
/* 198 */     if ((paramByte >= 48) && (paramByte <= 57)) {
/* 199 */       return paramByte - 48;
/*     */     }
/* 201 */     if ((paramByte >= 65) && (paramByte <= 70)) {
/* 202 */       return paramByte - 65 + 10;
/*     */     }
/* 204 */     if ((paramByte >= 97) && (paramByte <= 102)) {
/* 205 */       return paramByte - 97 + 10;
/*     */     }
/* 207 */     return -1;
/*     */   }
/*     */ 
/*     */   static byte[] unescapeFilterValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws NamingException
/*     */   {
/* 213 */     int i = 0; int j = 0;
/*     */ 
/* 221 */     int m = paramInt2 - paramInt1;
/* 222 */     byte[] arrayOfByte1 = new byte[m];
/* 223 */     int n = 0;
/* 224 */     for (int i1 = paramInt1; i1 < paramInt2; i1++) {
/* 225 */       byte b = paramArrayOfByte[i1];
/* 226 */       if (i != 0)
/*     */       {
/*     */         int k;
/* 228 */         if ((k = hexchar2int(b)) < 0)
/*     */         {
/* 235 */           if (j != 0)
/*     */           {
/* 237 */             i = 0;
/* 238 */             arrayOfByte1[(n++)] = b;
/*     */           }
/*     */           else {
/* 241 */             throw new InvalidSearchFilterException("invalid escape sequence: " + paramArrayOfByte);
/*     */           }
/*     */         }
/* 244 */         else if (j != 0) {
/* 245 */           arrayOfByte1[n] = ((byte)(k << 4));
/* 246 */           j = 0;
/*     */         }
/*     */         else
/*     */         {
/*     */           int tmp124_121 = (n++);
/*     */           byte[] tmp124_117 = arrayOfByte1; tmp124_117[tmp124_121] = ((byte)(tmp124_117[tmp124_121] | (byte)k));
/* 249 */           i = 0;
/*     */         }
/*     */       }
/* 252 */       else if (b != 92) {
/* 253 */         arrayOfByte1[(n++)] = b;
/* 254 */         i = 0;
/*     */       } else {
/* 256 */         j = i = 1;
/*     */       }
/*     */     }
/* 259 */     byte[] arrayOfByte2 = new byte[n];
/* 260 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, n);
/*     */ 
/* 264 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private static int indexOf(byte[] paramArrayOfByte, char paramChar, int paramInt1, int paramInt2) {
/* 268 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 269 */       if (paramArrayOfByte[i] == paramChar)
/* 270 */         return i;
/*     */     }
/* 272 */     return -1;
/*     */   }
/*     */ 
/*     */   private static int indexOf(byte[] paramArrayOfByte, String paramString, int paramInt1, int paramInt2) {
/* 276 */     int i = indexOf(paramArrayOfByte, paramString.charAt(0), paramInt1, paramInt2);
/* 277 */     if (i >= 0) {
/* 278 */       for (int j = 1; j < paramString.length(); j++) {
/* 279 */         if (paramArrayOfByte[(i + j)] != paramString.charAt(j)) {
/* 280 */           return -1;
/*     */         }
/*     */       }
/*     */     }
/* 284 */     return i;
/*     */   }
/*     */ 
/*     */   private static int findUnescaped(byte[] paramArrayOfByte, char paramChar, int paramInt1, int paramInt2) {
/* 288 */     while (paramInt1 < paramInt2) {
/* 289 */       int i = indexOf(paramArrayOfByte, paramChar, paramInt1, paramInt2);
/*     */ 
/* 301 */       int k = 0;
/* 302 */       int j = i - 1;
/*     */ 
/* 304 */       for (; (j >= paramInt1) && (paramArrayOfByte[j] == 92); 
/* 304 */         k++) j--;
/*     */ 
/* 307 */       if ((i == paramInt1) || (i == -1) || (k % 2 == 0)) {
/* 308 */         return i;
/*     */       }
/*     */ 
/* 311 */       paramInt1 = i + 1;
/*     */     }
/* 313 */     return -1;
/*     */   }
/*     */ 
/*     */   private static void encodeSimpleFilter(BerEncoder paramBerEncoder, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException, NamingException
/*     */   {
/*     */     int n;
/* 329 */     if ((n = indexOf(paramArrayOfByte, '=', paramInt1, paramInt2)) == -1) {
/* 330 */       throw new InvalidSearchFilterException("Missing 'equals'");
/*     */     }
/*     */ 
/* 334 */     int i = n + 1;
/* 335 */     int j = paramInt2;
/* 336 */     int k = paramInt1;
/*     */     int i1;
/*     */     int m;
/* 340 */     switch (paramArrayOfByte[(n - 1)]) {
/*     */     case 60:
/* 342 */       i1 = 166;
/* 343 */       m = n - 1;
/* 344 */       break;
/*     */     case 62:
/* 346 */       i1 = 165;
/* 347 */       m = n - 1;
/* 348 */       break;
/*     */     case 126:
/* 350 */       i1 = 168;
/* 351 */       m = n - 1;
/* 352 */       break;
/*     */     case 58:
/* 354 */       i1 = 169;
/* 355 */       m = n - 1;
/* 356 */       break;
/*     */     default:
/* 358 */       m = n;
/*     */ 
/* 360 */       i1 = 0;
/*     */     }
/*     */ 
/* 387 */     int i2 = -1;
/* 388 */     int i3 = -1;
/*     */     int i4;
/*     */     int i5;
/* 389 */     if (((paramArrayOfByte[k] >= 48) && (paramArrayOfByte[k] <= 57)) || ((paramArrayOfByte[k] >= 65) && (paramArrayOfByte[k] <= 90)) || ((paramArrayOfByte[k] >= 97) && (paramArrayOfByte[k] <= 122)))
/*     */     {
/* 393 */       i4 = (paramArrayOfByte[k] >= 48) && (paramArrayOfByte[k] <= 57) ? 1 : 0;
/*     */ 
/* 395 */       for (i5 = k + 1; i5 < m; i5++)
/*     */       {
/* 397 */         if (paramArrayOfByte[i5] == 59) {
/* 398 */           if ((i4 != 0) && (paramArrayOfByte[(i5 - 1)] == 46)) {
/* 399 */             throw new InvalidSearchFilterException("invalid attribute description");
/*     */           }
/*     */ 
/* 404 */           i2 = i5;
/* 405 */           break;
/*     */         }
/*     */ 
/* 409 */         if ((paramArrayOfByte[i5] == 58) && (i1 == 169)) {
/* 410 */           if ((i4 != 0) && (paramArrayOfByte[(i5 - 1)] == 46)) {
/* 411 */             throw new InvalidSearchFilterException("invalid attribute description");
/*     */           }
/*     */ 
/* 416 */           i3 = i5;
/* 417 */           break;
/*     */         }
/*     */ 
/* 420 */         if (i4 != 0)
/*     */         {
/* 422 */           if (((paramArrayOfByte[i5] == 46) && (paramArrayOfByte[(i5 - 1)] == 46)) || ((paramArrayOfByte[i5] != 46) && ((paramArrayOfByte[i5] < 48) || (paramArrayOfByte[i5] > 57))))
/*     */           {
/* 425 */             throw new InvalidSearchFilterException("invalid attribute description");
/*     */           }
/*     */ 
/*     */         }
/* 433 */         else if ((paramArrayOfByte[i5] != 45) && (paramArrayOfByte[i5] != 95) && ((paramArrayOfByte[i5] < 48) || (paramArrayOfByte[i5] > 57)) && ((paramArrayOfByte[i5] < 65) || (paramArrayOfByte[i5] > 90)) && ((paramArrayOfByte[i5] < 97) || (paramArrayOfByte[i5] > 122)))
/*     */         {
/* 437 */           throw new InvalidSearchFilterException("invalid attribute description");
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/* 442 */     else if ((i1 == 169) && (paramArrayOfByte[k] == 58))
/*     */     {
/* 444 */       i3 = k;
/*     */     } else {
/* 446 */       throw new InvalidSearchFilterException("invalid attribute description");
/*     */     }
/*     */ 
/* 451 */     if (i2 > 0) {
/* 452 */       for (i4 = i2 + 1; i4 < m; i4++) {
/* 453 */         if (paramArrayOfByte[i4] == 59) {
/* 454 */           if (paramArrayOfByte[(i4 - 1)] == 59) {
/* 455 */             throw new InvalidSearchFilterException("invalid attribute description");
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 462 */           if ((paramArrayOfByte[i4] == 58) && (i1 == 169)) {
/* 463 */             if (paramArrayOfByte[(i4 - 1)] == 59) {
/* 464 */               throw new InvalidSearchFilterException("invalid attribute description");
/*     */             }
/*     */ 
/* 469 */             i3 = i4;
/* 470 */             break;
/*     */           }
/*     */ 
/* 476 */           if ((paramArrayOfByte[i4] != 45) && (paramArrayOfByte[i4] != 95) && ((paramArrayOfByte[i4] < 48) || (paramArrayOfByte[i4] > 57)) && ((paramArrayOfByte[i4] < 65) || (paramArrayOfByte[i4] > 90)) && ((paramArrayOfByte[i4] < 97) || (paramArrayOfByte[i4] > 122)))
/*     */           {
/* 480 */             throw new InvalidSearchFilterException("invalid attribute description");
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 487 */     if (i3 > 0) {
/* 488 */       i4 = 0;
/* 489 */       for (i5 = i3 + 1; i5 < m; i5++) {
/* 490 */         if (paramArrayOfByte[i5] == 58) {
/* 491 */           throw new InvalidSearchFilterException("invalid attribute description");
/*     */         }
/* 493 */         if (((paramArrayOfByte[i5] >= 48) && (paramArrayOfByte[i5] <= 57)) || ((paramArrayOfByte[i5] >= 65) && (paramArrayOfByte[i5] <= 90)) || ((paramArrayOfByte[i5] >= 97) && (paramArrayOfByte[i5] <= 122)))
/*     */         {
/* 496 */           int i6 = (paramArrayOfByte[i5] >= 48) && (paramArrayOfByte[i5] <= 57) ? 1 : 0;
/* 497 */           i5++;
/* 498 */           for (int i7 = i5; i7 < m; i5++)
/*     */           {
/* 500 */             if (paramArrayOfByte[i7] == 58) {
/* 501 */               if (i4 != 0) {
/* 502 */                 throw new InvalidSearchFilterException("invalid attribute description");
/*     */               }
/*     */ 
/* 505 */               if ((i6 != 0) && (paramArrayOfByte[(i7 - 1)] == 46)) {
/* 506 */                 throw new InvalidSearchFilterException("invalid attribute description");
/*     */               }
/*     */ 
/* 510 */               i4 = 1;
/* 511 */               break;
/*     */             }
/*     */ 
/* 514 */             if (i6 != 0)
/*     */             {
/* 516 */               if (((paramArrayOfByte[i7] == 46) && (paramArrayOfByte[(i7 - 1)] == 46)) || ((paramArrayOfByte[i7] != 46) && ((paramArrayOfByte[i7] < 48) || (paramArrayOfByte[i7] > 57))))
/*     */               {
/* 519 */                 throw new InvalidSearchFilterException("invalid attribute description");
/*     */               }
/*     */ 
/*     */             }
/* 527 */             else if ((paramArrayOfByte[i7] != 45) && (paramArrayOfByte[i7] != 95) && ((paramArrayOfByte[i7] < 48) || (paramArrayOfByte[i7] > 57)) && ((paramArrayOfByte[i7] < 65) || (paramArrayOfByte[i7] > 90)) && ((paramArrayOfByte[i7] < 97) || (paramArrayOfByte[i7] > 122)))
/*     */             {
/* 531 */               throw new InvalidSearchFilterException("invalid attribute description");
/*     */             }
/* 498 */             i7++;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 537 */           throw new InvalidSearchFilterException("invalid attribute description");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 544 */     if ((paramArrayOfByte[(m - 1)] == 46) || (paramArrayOfByte[(m - 1)] == 59) || (paramArrayOfByte[(m - 1)] == 58))
/*     */     {
/* 546 */       throw new InvalidSearchFilterException("invalid attribute description");
/*     */     }
/*     */ 
/* 550 */     if (m == n) {
/* 551 */       if (findUnescaped(paramArrayOfByte, '*', i, j) == -1) {
/* 552 */         i1 = 163;
/* 553 */       } else if ((paramArrayOfByte[i] == 42) && (i == j - 1))
/*     */       {
/* 555 */         i1 = 135;
/*     */       } else {
/* 557 */         encodeSubstringFilter(paramBerEncoder, paramArrayOfByte, k, m, i, j);
/*     */ 
/* 559 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 563 */     if (i1 == 135) {
/* 564 */       paramBerEncoder.encodeOctetString(paramArrayOfByte, i1, k, m - k);
/* 565 */     } else if (i1 == 169) {
/* 566 */       encodeExtensibleMatch(paramBerEncoder, paramArrayOfByte, k, m, i, j);
/*     */     }
/*     */     else {
/* 569 */       paramBerEncoder.beginSeq(i1);
/* 570 */       paramBerEncoder.encodeOctetString(paramArrayOfByte, 4, k, m - k);
/*     */ 
/* 572 */       paramBerEncoder.encodeOctetString(unescapeFilterValue(paramArrayOfByte, i, j), 4);
/*     */ 
/* 575 */       paramBerEncoder.endSeq();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void encodeSubstringFilter(BerEncoder paramBerEncoder, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws IOException, NamingException
/*     */   {
/* 593 */     paramBerEncoder.beginSeq(164);
/* 594 */     paramBerEncoder.encodeOctetString(paramArrayOfByte, 4, paramInt1, paramInt2 - paramInt1);
/*     */ 
/* 596 */     paramBerEncoder.beginSeq(48);
/*     */ 
/* 598 */     int j = paramInt3;
/*     */     int i;
/* 599 */     while ((i = findUnescaped(paramArrayOfByte, '*', j, paramInt4)) != -1) {
/* 600 */       if (j == paramInt3) {
/* 601 */         if (j < i)
/*     */         {
/* 605 */           paramBerEncoder.encodeOctetString(unescapeFilterValue(paramArrayOfByte, j, i), 128);
/*     */         }
/*     */ 
/*     */       }
/* 610 */       else if (j < i)
/*     */       {
/* 613 */         paramBerEncoder.encodeOctetString(unescapeFilterValue(paramArrayOfByte, j, i), 129);
/*     */       }
/*     */ 
/* 618 */       j = i + 1;
/*     */     }
/* 620 */     if (j < paramInt4)
/*     */     {
/* 623 */       paramBerEncoder.encodeOctetString(unescapeFilterValue(paramArrayOfByte, j, paramInt4), 130);
/*     */     }
/*     */ 
/* 627 */     paramBerEncoder.endSeq();
/* 628 */     paramBerEncoder.endSeq();
/*     */   }
/*     */ 
/*     */   private static void encodeComplexFilter(BerEncoder paramBerEncoder, byte[] paramArrayOfByte, int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws IOException, NamingException
/*     */   {
/* 652 */     paramArrayOfInt[0] += 1;
/*     */ 
/* 654 */     paramBerEncoder.beginSeq(paramInt1);
/*     */ 
/* 656 */     int[] arrayOfInt = findRightParen(paramArrayOfByte, paramArrayOfInt, paramInt2);
/* 657 */     encodeFilterList(paramBerEncoder, paramArrayOfByte, paramInt1, arrayOfInt[0], arrayOfInt[1]);
/*     */ 
/* 659 */     paramBerEncoder.endSeq();
/*     */   }
/*     */ 
/*     */   private static int[] findRightParen(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt)
/*     */     throws IOException, NamingException
/*     */   {
/* 675 */     int i = 1;
/* 676 */     int j = 0;
/* 677 */     int k = paramArrayOfInt[0];
/*     */ 
/* 679 */     while ((k < paramInt) && (i > 0)) {
/* 680 */       if (j == 0) {
/* 681 */         if (paramArrayOfByte[k] == 40)
/* 682 */           i++;
/* 683 */         else if (paramArrayOfByte[k] == 41)
/* 684 */           i--;
/*     */       }
/* 686 */       if ((paramArrayOfByte[k] == 92) && (j == 0))
/* 687 */         j = 1;
/*     */       else
/* 689 */         j = 0;
/* 690 */       if (i > 0)
/* 691 */         k++;
/*     */     }
/* 693 */     if (i != 0) {
/* 694 */       throw new InvalidSearchFilterException("Unbalanced parenthesis");
/*     */     }
/*     */ 
/* 699 */     int[] arrayOfInt = { paramArrayOfInt[0], k };
/*     */ 
/* 701 */     paramArrayOfInt[0] = (k + 1);
/*     */ 
/* 703 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static void encodeFilterList(BerEncoder paramBerEncoder, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException, NamingException
/*     */   {
/* 718 */     int[] arrayOfInt1 = new int[1];
/* 719 */     int i = 0;
/* 720 */     for (arrayOfInt1[0] = paramInt2; arrayOfInt1[0] < paramInt3; arrayOfInt1[0] += 1)
/* 721 */       if (!Character.isSpaceChar((char)paramArrayOfByte[arrayOfInt1[0]]))
/*     */       {
/* 724 */         if ((paramInt1 == 162) && (i > 0)) {
/* 725 */           throw new InvalidSearchFilterException("Filter (!) cannot be followed by more than one filters");
/*     */         }
/*     */ 
/* 729 */         if (paramArrayOfByte[arrayOfInt1[0]] != 40)
/*     */         {
/* 733 */           int[] arrayOfInt2 = findRightParen(paramArrayOfByte, arrayOfInt1, paramInt3);
/*     */ 
/* 736 */           int j = arrayOfInt2[1] - arrayOfInt2[0];
/* 737 */           byte[] arrayOfByte = new byte[j + 2];
/* 738 */           System.arraycopy(paramArrayOfByte, arrayOfInt2[0], arrayOfByte, 1, j);
/* 739 */           arrayOfByte[0] = 40;
/* 740 */           arrayOfByte[(j + 1)] = 41;
/* 741 */           encodeFilter(paramBerEncoder, arrayOfByte, 0, arrayOfByte.length);
/*     */ 
/* 743 */           i++;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private static void encodeExtensibleMatch(BerEncoder paramBerEncoder, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws IOException, NamingException
/*     */   {
/* 759 */     boolean bool = false;
/*     */ 
/* 764 */     paramBerEncoder.beginSeq(169);
/*     */     int i;
/* 767 */     if ((i = indexOf(paramArrayOfByte, ':', paramInt1, paramInt2)) >= 0)
/*     */     {
/*     */       int k;
/* 770 */       if ((k = indexOf(paramArrayOfByte, ":dn", i, paramInt2)) >= 0)
/* 771 */         bool = true;
/*     */       int j;
/* 775 */       if (((j = indexOf(paramArrayOfByte, ':', i + 1, paramInt2)) >= 0) || (k == -1))
/*     */       {
/* 778 */         if (k == i) {
/* 779 */           paramBerEncoder.encodeOctetString(paramArrayOfByte, 129, j + 1, paramInt2 - (j + 1));
/*     */         }
/* 782 */         else if ((k == j) && (k >= 0)) {
/* 783 */           paramBerEncoder.encodeOctetString(paramArrayOfByte, 129, i + 1, j - (i + 1));
/*     */         }
/*     */         else
/*     */         {
/* 787 */           paramBerEncoder.encodeOctetString(paramArrayOfByte, 129, i + 1, paramInt2 - (i + 1));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 793 */       if (i > paramInt1)
/* 794 */         paramBerEncoder.encodeOctetString(paramArrayOfByte, 130, paramInt1, i - paramInt1);
/*     */     }
/*     */     else
/*     */     {
/* 798 */       paramBerEncoder.encodeOctetString(paramArrayOfByte, 130, paramInt1, paramInt2 - paramInt1);
/*     */     }
/*     */ 
/* 802 */     paramBerEncoder.encodeOctetString(unescapeFilterValue(paramArrayOfByte, paramInt3, paramInt4), 131);
/*     */ 
/* 811 */     paramBerEncoder.encodeBoolean(bool, 132);
/*     */ 
/* 813 */     paramBerEncoder.endSeq();
/*     */   }
/*     */ 
/*     */   private static void dprint(String paramString)
/*     */   {
/* 827 */     dprint(paramString, new byte[0], 0, 0);
/*     */   }
/*     */ 
/*     */   private static void dprint(String paramString, byte[] paramArrayOfByte) {
/* 831 */     dprint(paramString, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   private static void dprint(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 835 */     String str = "  ";
/* 836 */     int i = dbgIndent;
/* 837 */     while (i-- > 0) {
/* 838 */       str = str + "  ";
/*     */     }
/* 840 */     str = str + paramString;
/*     */ 
/* 842 */     System.err.print(str);
/* 843 */     for (int j = paramInt1; j < paramInt2; j++) {
/* 844 */       System.err.print((char)paramArrayOfByte[j]);
/*     */     }
/* 846 */     System.err.println();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.Filter
 * JD-Core Version:    0.6.2
 */