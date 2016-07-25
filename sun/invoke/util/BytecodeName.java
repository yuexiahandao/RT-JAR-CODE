/*     */ package sun.invoke.util;
/*     */ 
/*     */ public class BytecodeName
/*     */ {
/*     */   static char ESCAPE_C;
/*     */   static char NULL_ESCAPE_C;
/*     */   static String NULL_ESCAPE;
/*     */   static final String DANGEROUS_CHARS = "\\/.;:$[]<>";
/*     */   static final String REPLACEMENT_CHARS = "-|,?!%{}^_";
/*     */   static final int DANGEROUS_CHAR_FIRST_INDEX = 1;
/*     */   static char[] DANGEROUS_CHARS_A;
/*     */   static char[] REPLACEMENT_CHARS_A;
/*     */   static final Character[] DANGEROUS_CHARS_CA;
/*     */   static final long[] SPECIAL_BITMAP;
/*     */ 
/*     */   public static String toBytecodeName(String paramString)
/*     */   {
/* 266 */     String str = mangle(paramString);
/* 267 */     assert ((str == paramString) || (looksMangled(str))) : str;
/* 268 */     assert (paramString.equals(toSourceName(str))) : paramString;
/* 269 */     return str;
/*     */   }
/*     */ 
/*     */   public static String toSourceName(String paramString)
/*     */   {
/* 281 */     checkSafeBytecodeName(paramString);
/* 282 */     String str = paramString;
/* 283 */     if (looksMangled(paramString)) {
/* 284 */       str = demangle(paramString);
/* 285 */       assert (paramString.equals(mangle(str))) : (paramString + " => " + str + " => " + mangle(str));
/*     */     }
/* 287 */     return str;
/*     */   }
/*     */ 
/*     */   public static Object[] parseBytecodeName(String paramString)
/*     */   {
/* 305 */     int i = paramString.length();
/* 306 */     Object[] arrayOfObject = null;
/* 307 */     for (int j = 0; j <= 1; j++) {
/* 308 */       int k = 0;
/* 309 */       int m = 0;
/* 310 */       for (int n = 0; n <= i; n++) {
/* 311 */         int i1 = -1;
/* 312 */         if (n < i) { i1 = "\\/.;:$[]<>".indexOf(paramString.charAt(n));
/* 314 */           if (i1 < 1);
/*     */         }
/*     */         else {
/* 317 */           if (m < n)
/*     */           {
/* 319 */             if (j != 0)
/* 320 */               arrayOfObject[k] = toSourceName(paramString.substring(m, n));
/* 321 */             k++;
/* 322 */             m = n + 1;
/*     */           }
/* 324 */           if (i1 >= 1) {
/* 325 */             if (j != 0)
/* 326 */               arrayOfObject[k] = DANGEROUS_CHARS_CA[i1];
/* 327 */             k++;
/* 328 */             m = n + 1;
/*     */           }
/*     */         }
/*     */       }
/* 331 */       if (j != 0)
/*     */         break;
/* 333 */       arrayOfObject = new Object[k];
/* 334 */       if ((k <= 1) && (m == 0)) {
/* 335 */         if (k == 0) break; arrayOfObject[0] = toSourceName(paramString); break;
/*     */       }
/*     */     }
/*     */ 
/* 339 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public static String unparseBytecodeName(Object[] paramArrayOfObject)
/*     */   {
/* 354 */     Object[] arrayOfObject = paramArrayOfObject;
/* 355 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 356 */       Object localObject = paramArrayOfObject[i];
/* 357 */       if ((localObject instanceof String)) {
/* 358 */         String str = toBytecodeName((String)localObject);
/* 359 */         if ((i == 0) && (paramArrayOfObject.length == 1))
/* 360 */           return str;
/* 361 */         if (str != localObject) {
/* 362 */           if (paramArrayOfObject == arrayOfObject)
/* 363 */             paramArrayOfObject = (Object[])paramArrayOfObject.clone();
/*     */           String tmp66_64 = str; localObject = tmp66_64; paramArrayOfObject[i] = tmp66_64;
/*     */         }
/*     */       }
/*     */     }
/* 368 */     return appendAll(paramArrayOfObject);
/*     */   }
/*     */   private static String appendAll(Object[] paramArrayOfObject) {
/* 371 */     if (paramArrayOfObject.length <= 1) {
/* 372 */       if (paramArrayOfObject.length == 1) {
/* 373 */         return String.valueOf(paramArrayOfObject[0]);
/*     */       }
/* 375 */       return "";
/*     */     }
/* 377 */     int i = 0;
/* 378 */     for (Object localObject2 : paramArrayOfObject) {
/* 379 */       if ((localObject2 instanceof String))
/* 380 */         i += String.valueOf(localObject2).length();
/*     */       else
/* 382 */         i++;
/*     */     }
/* 384 */     ??? = new StringBuilder(i);
/* 385 */     for (Object localObject3 : paramArrayOfObject) {
/* 386 */       ((StringBuilder)???).append(localObject3);
/*     */     }
/* 388 */     return ((StringBuilder)???).toString();
/*     */   }
/*     */ 
/*     */   public static String toDisplayName(String paramString)
/*     */   {
/* 409 */     Object[] arrayOfObject = parseBytecodeName(paramString);
/* 410 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 411 */       if ((arrayOfObject[i] instanceof String))
/*     */       {
/* 413 */         String str = (String)arrayOfObject[i];
/*     */ 
/* 416 */         if ((!isJavaIdent(str)) || (str.indexOf('$') >= 0))
/* 417 */           arrayOfObject[i] = quoteDisplay(str);
/*     */       }
/*     */     }
/* 420 */     return appendAll(arrayOfObject);
/*     */   }
/*     */   private static boolean isJavaIdent(String paramString) {
/* 423 */     int i = paramString.length();
/* 424 */     if (i == 0) return false;
/* 425 */     if (!Character.isJavaIdentifierStart(paramString.charAt(0)))
/* 426 */       return false;
/* 427 */     for (int j = 1; j < i; j++) {
/* 428 */       if (!Character.isJavaIdentifierPart(paramString.charAt(j)))
/* 429 */         return false;
/*     */     }
/* 431 */     return true;
/*     */   }
/*     */ 
/*     */   private static String quoteDisplay(String paramString) {
/* 435 */     return "'" + paramString.replaceAll("['\\\\]", "\\\\$0") + "'";
/*     */   }
/*     */ 
/*     */   private static void checkSafeBytecodeName(String paramString) throws IllegalArgumentException
/*     */   {
/* 440 */     if (!isSafeBytecodeName(paramString))
/* 441 */       throw new IllegalArgumentException(paramString);
/*     */   }
/*     */ 
/*     */   public static boolean isSafeBytecodeName(String paramString)
/*     */   {
/* 454 */     if (paramString.length() == 0) return false;
/*     */ 
/* 456 */     for (int k : DANGEROUS_CHARS_A) {
/* 457 */       if ((k != ESCAPE_C) && 
/* 458 */         (paramString.indexOf(k) >= 0)) return false;
/*     */     }
/* 460 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isSafeBytecodeChar(char paramChar)
/*     */   {
/* 471 */     return "\\/.;:$[]<>".indexOf(paramChar) < 1;
/*     */   }
/*     */ 
/*     */   private static boolean looksMangled(String paramString) {
/* 475 */     return paramString.charAt(0) == ESCAPE_C;
/*     */   }
/*     */ 
/*     */   private static String mangle(String paramString) {
/* 479 */     if (paramString.length() == 0) {
/* 480 */       return NULL_ESCAPE;
/*     */     }
/*     */ 
/* 483 */     StringBuilder localStringBuilder = null;
/*     */ 
/* 485 */     int i = 0; for (int j = paramString.length(); i < j; i++) {
/* 486 */       char c1 = paramString.charAt(i);
/*     */ 
/* 488 */       boolean bool = false;
/* 489 */       if (c1 == ESCAPE_C) {
/* 490 */         if (i + 1 < j) {
/* 491 */           char c2 = paramString.charAt(i + 1);
/* 492 */           if (((i == 0) && (c2 == NULL_ESCAPE_C)) || (c2 != originalOfReplacement(c2)))
/*     */           {
/* 495 */             bool = true;
/*     */           }
/*     */         }
/*     */       }
/* 499 */       else bool = isDangerous(c1);
/*     */ 
/* 502 */       if (!bool) {
/* 503 */         if (localStringBuilder != null) localStringBuilder.append(c1);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 508 */         if (localStringBuilder == null) {
/* 509 */           localStringBuilder = new StringBuilder(paramString.length() + 10);
/*     */ 
/* 511 */           if ((paramString.charAt(0) != ESCAPE_C) && (i > 0)) {
/* 512 */             localStringBuilder.append(NULL_ESCAPE);
/*     */           }
/* 514 */           localStringBuilder.append(paramString.substring(0, i));
/*     */         }
/*     */ 
/* 518 */         localStringBuilder.append(ESCAPE_C);
/* 519 */         localStringBuilder.append(replacementOf(c1));
/*     */       }
/*     */     }
/* 522 */     if (localStringBuilder != null) return localStringBuilder.toString();
/*     */ 
/* 524 */     return paramString;
/*     */   }
/*     */ 
/*     */   private static String demangle(String paramString)
/*     */   {
/* 529 */     StringBuilder localStringBuilder = null;
/*     */ 
/* 531 */     int i = 0;
/* 532 */     if (paramString.startsWith(NULL_ESCAPE)) {
/* 533 */       i = 2;
/*     */     }
/* 535 */     int j = i; for (int k = paramString.length(); j < k; j++) {
/* 536 */       char c1 = paramString.charAt(j);
/*     */ 
/* 538 */       if ((c1 == ESCAPE_C) && (j + 1 < k))
/*     */       {
/* 540 */         char c2 = paramString.charAt(j + 1);
/* 541 */         char c3 = originalOfReplacement(c2);
/* 542 */         if (c3 != c2)
/*     */         {
/* 544 */           if (localStringBuilder == null) {
/* 545 */             localStringBuilder = new StringBuilder(paramString.length());
/*     */ 
/* 547 */             localStringBuilder.append(paramString.substring(i, j));
/*     */           }
/* 549 */           j++;
/* 550 */           c1 = c3;
/*     */         }
/*     */       }
/*     */ 
/* 554 */       if (localStringBuilder != null) {
/* 555 */         localStringBuilder.append(c1);
/*     */       }
/*     */     }
/* 558 */     if (localStringBuilder != null) return localStringBuilder.toString();
/*     */ 
/* 560 */     return paramString.substring(i);
/*     */   }
/*     */ 
/*     */   static boolean isSpecial(char paramChar)
/*     */   {
/* 590 */     if (paramChar >>> '\006' < SPECIAL_BITMAP.length) {
/* 591 */       return (SPECIAL_BITMAP[(paramChar >>> '\006')] >> paramChar & 1L) != 0L;
/*     */     }
/* 593 */     return false;
/*     */   }
/*     */   static char replacementOf(char paramChar) {
/* 596 */     if (!isSpecial(paramChar)) return paramChar;
/* 597 */     int i = "\\/.;:$[]<>".indexOf(paramChar);
/* 598 */     if (i < 0) return paramChar;
/* 599 */     return "-|,?!%{}^_".charAt(i);
/*     */   }
/*     */   static char originalOfReplacement(char paramChar) {
/* 602 */     if (!isSpecial(paramChar)) return paramChar;
/* 603 */     int i = "-|,?!%{}^_".indexOf(paramChar);
/* 604 */     if (i < 0) return paramChar;
/* 605 */     return "\\/.;:$[]<>".charAt(i);
/*     */   }
/*     */   static boolean isDangerous(char paramChar) {
/* 608 */     if (!isSpecial(paramChar)) return false;
/* 609 */     return "\\/.;:$[]<>".indexOf(paramChar) >= 1;
/*     */   }
/*     */   static int indexOfDangerousChar(String paramString, int paramInt) {
/* 612 */     int i = paramInt; for (int j = paramString.length(); i < j; i++) {
/* 613 */       if (isDangerous(paramString.charAt(i)))
/* 614 */         return i;
/*     */     }
/* 616 */     return -1;
/*     */   }
/*     */   static int lastIndexOfDangerousChar(String paramString, int paramInt) {
/* 619 */     for (int i = Math.min(paramInt, paramString.length() - 1); i >= 0; i--) {
/* 620 */       if (isDangerous(paramString.charAt(i)))
/* 621 */         return i;
/*     */     }
/* 623 */     return -1;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 563 */     ESCAPE_C = '\\';
/*     */ 
/* 565 */     NULL_ESCAPE_C = '=';
/* 566 */     NULL_ESCAPE = ESCAPE_C + "" + NULL_ESCAPE_C;
/*     */ 
/* 571 */     DANGEROUS_CHARS_A = "\\/.;:$[]<>".toCharArray();
/* 572 */     REPLACEMENT_CHARS_A = "-|,?!%{}^_".toCharArray();
/*     */ 
/* 575 */     Object localObject = new Character["\\/.;:$[]<>".length()];
/* 576 */     for (int i = 0; i < localObject.length; i++)
/* 577 */       localObject[i] = Character.valueOf("\\/.;:$[]<>".charAt(i));
/* 578 */     DANGEROUS_CHARS_CA = (Character[])localObject;
/*     */ 
/* 581 */     SPECIAL_BITMAP = new long[2];
/*     */ 
/* 583 */     localObject = "\\/.;:$[]<>-|,?!%{}^_";
/*     */ 
/* 585 */     for (int m : ((String)localObject).toCharArray())
/* 586 */       SPECIAL_BITMAP[(m >>> 6)] |= 1L << m;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.util.BytecodeName
 * JD-Core Version:    0.6.2
 */