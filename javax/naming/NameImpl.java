/*     */ package javax.naming;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class NameImpl
/*     */ {
/*     */   private static final byte LEFT_TO_RIGHT = 1;
/*     */   private static final byte RIGHT_TO_LEFT = 2;
/*     */   private static final byte FLAT = 0;
/*     */   private Vector components;
/*  50 */   private byte syntaxDirection = 1;
/*  51 */   private String syntaxSeparator = "/";
/*  52 */   private String syntaxSeparator2 = null;
/*  53 */   private boolean syntaxCaseInsensitive = false;
/*  54 */   private boolean syntaxTrimBlanks = false;
/*  55 */   private String syntaxEscape = "\\";
/*  56 */   private String syntaxBeginQuote1 = "\"";
/*  57 */   private String syntaxEndQuote1 = "\"";
/*  58 */   private String syntaxBeginQuote2 = "'";
/*  59 */   private String syntaxEndQuote2 = "'";
/*  60 */   private String syntaxAvaSeparator = null;
/*  61 */   private String syntaxTypevalSeparator = null;
/*     */   private static final int STYLE_NONE = 0;
/*     */   private static final int STYLE_QUOTE1 = 1;
/*     */   private static final int STYLE_QUOTE2 = 2;
/*     */   private static final int STYLE_ESCAPE = 3;
/*  71 */   private int escapingStyle = 0;
/*     */ 
/*     */   private final boolean isA(String paramString1, int paramInt, String paramString2)
/*     */   {
/*  76 */     return (paramString2 != null) && (paramString1.startsWith(paramString2, paramInt));
/*     */   }
/*     */ 
/*     */   private final boolean isMeta(String paramString, int paramInt) {
/*  80 */     return (isA(paramString, paramInt, this.syntaxEscape)) || (isA(paramString, paramInt, this.syntaxBeginQuote1)) || (isA(paramString, paramInt, this.syntaxBeginQuote2)) || (isSeparator(paramString, paramInt));
/*     */   }
/*     */ 
/*     */   private final boolean isSeparator(String paramString, int paramInt)
/*     */   {
/*  87 */     return (isA(paramString, paramInt, this.syntaxSeparator)) || (isA(paramString, paramInt, this.syntaxSeparator2));
/*     */   }
/*     */ 
/*     */   private final int skipSeparator(String paramString, int paramInt)
/*     */   {
/*  92 */     if (isA(paramString, paramInt, this.syntaxSeparator))
/*  93 */       paramInt += this.syntaxSeparator.length();
/*  94 */     else if (isA(paramString, paramInt, this.syntaxSeparator2)) {
/*  95 */       paramInt += this.syntaxSeparator2.length();
/*     */     }
/*  97 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private final int extractComp(String paramString, int paramInt1, int paramInt2, Vector paramVector)
/*     */     throws InvalidNameException
/*     */   {
/* 104 */     int i = 1;
/* 105 */     boolean bool = false;
/* 106 */     StringBuffer localStringBuffer = new StringBuffer(paramInt2);
/*     */ 
/* 108 */     while (paramInt1 < paramInt2)
/*     */     {
/*     */       String str1;
/*     */       String str2;
/* 110 */       if ((i != 0) && (((bool = isA(paramString, paramInt1, this.syntaxBeginQuote1))) || (isA(paramString, paramInt1, this.syntaxBeginQuote2))))
/*     */       {
/* 114 */         str1 = bool ? this.syntaxBeginQuote1 : this.syntaxBeginQuote2;
/* 115 */         str2 = bool ? this.syntaxEndQuote1 : this.syntaxEndQuote2;
/* 116 */         if (this.escapingStyle == 0) {
/* 117 */           this.escapingStyle = (bool ? 1 : 2);
/*     */         }
/*     */ 
/* 121 */         paramInt1 += str1.length();
/*     */ 
/* 123 */         for (; (paramInt1 < paramInt2) && (!paramString.startsWith(str2, paramInt1)); 
/* 123 */           paramInt1++)
/*     */         {
/* 126 */           if ((isA(paramString, paramInt1, this.syntaxEscape)) && (isA(paramString, paramInt1 + this.syntaxEscape.length(), str2)))
/*     */           {
/* 128 */             paramInt1 += this.syntaxEscape.length();
/*     */           }
/* 130 */           localStringBuffer.append(paramString.charAt(paramInt1));
/*     */         }
/*     */ 
/* 134 */         if (paramInt1 >= paramInt2) {
/* 135 */           throw new InvalidNameException(paramString + ": no close quote");
/*     */         }
/*     */ 
/* 139 */         paramInt1 += str2.length();
/*     */ 
/* 142 */         if ((paramInt1 == paramInt2) || (isSeparator(paramString, paramInt1)))
/*     */         {
/*     */           break;
/*     */         }
/* 146 */         throw new InvalidNameException(paramString + ": close quote appears before end of component");
/*     */       }
/*     */ 
/* 149 */       if (isSeparator(paramString, paramInt1)) {
/*     */         break;
/*     */       }
/* 152 */       if (isA(paramString, paramInt1, this.syntaxEscape)) {
/* 153 */         if (isMeta(paramString, paramInt1 + this.syntaxEscape.length()))
/*     */         {
/* 156 */           paramInt1 += this.syntaxEscape.length();
/* 157 */           if (this.escapingStyle == 0)
/* 158 */             this.escapingStyle = 3;
/*     */         }
/* 160 */         else if (paramInt1 + this.syntaxEscape.length() >= paramInt2) {
/* 161 */           throw new InvalidNameException(paramString + ": unescaped " + this.syntaxEscape + " at end of component");
/*     */         }
/*     */       }
/* 164 */       else if ((isA(paramString, paramInt1, this.syntaxTypevalSeparator)) && (((bool = isA(paramString, paramInt1 + this.syntaxTypevalSeparator.length(), this.syntaxBeginQuote1))) || (isA(paramString, paramInt1 + this.syntaxTypevalSeparator.length(), this.syntaxBeginQuote2))))
/*     */       {
/* 168 */         str1 = bool ? this.syntaxBeginQuote1 : this.syntaxBeginQuote2;
/* 169 */         str2 = bool ? this.syntaxEndQuote1 : this.syntaxEndQuote2;
/*     */ 
/* 171 */         paramInt1 += this.syntaxTypevalSeparator.length();
/* 172 */         localStringBuffer.append(this.syntaxTypevalSeparator + str1);
/*     */ 
/* 175 */         paramInt1 += str1.length();
/*     */ 
/* 177 */         for (; (paramInt1 < paramInt2) && (!paramString.startsWith(str2, paramInt1)); 
/* 177 */           paramInt1++)
/*     */         {
/* 180 */           if ((isA(paramString, paramInt1, this.syntaxEscape)) && (isA(paramString, paramInt1 + this.syntaxEscape.length(), str2)))
/*     */           {
/* 182 */             paramInt1 += this.syntaxEscape.length();
/*     */           }
/* 184 */           localStringBuffer.append(paramString.charAt(paramInt1));
/*     */         }
/*     */ 
/* 188 */         if (paramInt1 >= paramInt2) {
/* 189 */           throw new InvalidNameException(paramString + ": typeval no close quote");
/*     */         }
/*     */ 
/* 192 */         paramInt1 += str2.length();
/* 193 */         localStringBuffer.append(str2);
/*     */ 
/* 196 */         if ((paramInt1 == paramInt2) || (isSeparator(paramString, paramInt1))) {
/*     */           break;
/*     */         }
/* 199 */         throw new InvalidNameException(paramString.substring(paramInt1) + ": typeval close quote appears before end of component");
/*     */       }
/*     */ 
/* 203 */       localStringBuffer.append(paramString.charAt(paramInt1++));
/* 204 */       i = 0;
/*     */     }
/*     */ 
/* 207 */     if (this.syntaxDirection == 2)
/* 208 */       paramVector.insertElementAt(localStringBuffer.toString(), 0);
/*     */     else
/* 210 */       paramVector.addElement(localStringBuffer.toString());
/* 211 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   private static boolean getBoolean(Properties paramProperties, String paramString) {
/* 215 */     return toBoolean(paramProperties.getProperty(paramString));
/*     */   }
/*     */ 
/*     */   private static boolean toBoolean(String paramString) {
/* 219 */     return (paramString != null) && (paramString.toLowerCase().equals("true"));
/*     */   }
/*     */ 
/*     */   private final void recordNamingConvention(Properties paramProperties) {
/* 223 */     String str = paramProperties.getProperty("jndi.syntax.direction", "flat");
/*     */ 
/* 225 */     if (str.equals("left_to_right"))
/* 226 */       this.syntaxDirection = 1;
/* 227 */     else if (str.equals("right_to_left"))
/* 228 */       this.syntaxDirection = 2;
/* 229 */     else if (str.equals("flat"))
/* 230 */       this.syntaxDirection = 0;
/*     */     else {
/* 232 */       throw new IllegalArgumentException(str + "is not a valid value for the jndi.syntax.direction property");
/*     */     }
/*     */ 
/* 236 */     if (this.syntaxDirection != 0) {
/* 237 */       this.syntaxSeparator = paramProperties.getProperty("jndi.syntax.separator");
/* 238 */       this.syntaxSeparator2 = paramProperties.getProperty("jndi.syntax.separator2");
/* 239 */       if (this.syntaxSeparator == null)
/* 240 */         throw new IllegalArgumentException("jndi.syntax.separator property required for non-flat syntax");
/*     */     }
/*     */     else
/*     */     {
/* 244 */       this.syntaxSeparator = null;
/*     */     }
/* 246 */     this.syntaxEscape = paramProperties.getProperty("jndi.syntax.escape");
/*     */ 
/* 248 */     this.syntaxCaseInsensitive = getBoolean(paramProperties, "jndi.syntax.ignorecase");
/* 249 */     this.syntaxTrimBlanks = getBoolean(paramProperties, "jndi.syntax.trimblanks");
/*     */ 
/* 251 */     this.syntaxBeginQuote1 = paramProperties.getProperty("jndi.syntax.beginquote");
/* 252 */     this.syntaxEndQuote1 = paramProperties.getProperty("jndi.syntax.endquote");
/* 253 */     if ((this.syntaxEndQuote1 == null) && (this.syntaxBeginQuote1 != null))
/* 254 */       this.syntaxEndQuote1 = this.syntaxBeginQuote1;
/* 255 */     else if ((this.syntaxBeginQuote1 == null) && (this.syntaxEndQuote1 != null))
/* 256 */       this.syntaxBeginQuote1 = this.syntaxEndQuote1;
/* 257 */     this.syntaxBeginQuote2 = paramProperties.getProperty("jndi.syntax.beginquote2");
/* 258 */     this.syntaxEndQuote2 = paramProperties.getProperty("jndi.syntax.endquote2");
/* 259 */     if ((this.syntaxEndQuote2 == null) && (this.syntaxBeginQuote2 != null))
/* 260 */       this.syntaxEndQuote2 = this.syntaxBeginQuote2;
/* 261 */     else if ((this.syntaxBeginQuote2 == null) && (this.syntaxEndQuote2 != null)) {
/* 262 */       this.syntaxBeginQuote2 = this.syntaxEndQuote2;
/*     */     }
/* 264 */     this.syntaxAvaSeparator = paramProperties.getProperty("jndi.syntax.separator.ava");
/* 265 */     this.syntaxTypevalSeparator = paramProperties.getProperty("jndi.syntax.separator.typeval");
/*     */   }
/*     */ 
/*     */   NameImpl(Properties paramProperties)
/*     */   {
/* 270 */     if (paramProperties != null) {
/* 271 */       recordNamingConvention(paramProperties);
/*     */     }
/* 273 */     this.components = new Vector();
/*     */   }
/*     */ 
/*     */   NameImpl(Properties paramProperties, String paramString) throws InvalidNameException {
/* 277 */     this(paramProperties);
/*     */ 
/* 279 */     int i = this.syntaxDirection == 2 ? 1 : 0;
/* 280 */     int j = 1;
/* 281 */     int k = paramString.length();
/*     */ 
/* 283 */     for (int m = 0; m < k; ) {
/* 284 */       m = extractComp(paramString, m, k, this.components);
/*     */ 
/* 286 */       String str = i != 0 ? (String)this.components.firstElement() : (String)this.components.lastElement();
/*     */ 
/* 289 */       if (str.length() >= 1) {
/* 290 */         j = 0;
/*     */       }
/*     */ 
/* 293 */       if (m < k) {
/* 294 */         m = skipSeparator(paramString, m);
/* 295 */         if ((m == k) && (j == 0))
/*     */         {
/* 297 */           if (i != 0)
/* 298 */             this.components.insertElementAt("", 0);
/*     */           else
/* 300 */             this.components.addElement("");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   NameImpl(Properties paramProperties, Enumeration paramEnumeration)
/*     */   {
/* 308 */     this(paramProperties);
/*     */ 
/* 311 */     while (paramEnumeration.hasMoreElements())
/* 312 */       this.components.addElement(paramEnumeration.nextElement());
/*     */   }
/*     */ 
/*     */   private final String stringifyComp(String paramString)
/*     */   {
/* 339 */     int i = paramString.length();
/* 340 */     int j = 0; int k = 0;
/* 341 */     String str1 = null; String str2 = null;
/* 342 */     StringBuffer localStringBuffer = new StringBuffer(i);
/*     */ 
/* 346 */     if ((this.syntaxSeparator != null) && (paramString.indexOf(this.syntaxSeparator) >= 0))
/*     */     {
/* 348 */       if (this.syntaxBeginQuote1 != null) {
/* 349 */         str1 = this.syntaxBeginQuote1;
/* 350 */         str2 = this.syntaxEndQuote1;
/* 351 */       } else if (this.syntaxBeginQuote2 != null) {
/* 352 */         str1 = this.syntaxBeginQuote2;
/* 353 */         str2 = this.syntaxEndQuote2;
/* 354 */       } else if (this.syntaxEscape != null) {
/* 355 */         j = 1;
/*     */       }
/*     */     }
/* 357 */     if ((this.syntaxSeparator2 != null) && (paramString.indexOf(this.syntaxSeparator2) >= 0))
/*     */     {
/* 359 */       if (this.syntaxBeginQuote1 != null) {
/* 360 */         if (str1 == null) {
/* 361 */           str1 = this.syntaxBeginQuote1;
/* 362 */           str2 = this.syntaxEndQuote1;
/*     */         }
/* 364 */       } else if (this.syntaxBeginQuote2 != null) {
/* 365 */         if (str1 == null) {
/* 366 */           str1 = this.syntaxBeginQuote2;
/* 367 */           str2 = this.syntaxEndQuote2;
/*     */         }
/* 369 */       } else if (this.syntaxEscape != null)
/* 370 */         k = 1;
/*     */     }
/*     */     int m;
/*     */     int n;
/* 374 */     if (str1 != null)
/*     */     {
/* 377 */       localStringBuffer = localStringBuffer.append(str1);
/*     */ 
/* 381 */       for (m = 0; m < i; ) {
/* 382 */         if (paramString.startsWith(str2, m))
/*     */         {
/* 384 */           localStringBuffer.append(this.syntaxEscape).append(str2);
/* 385 */           m += str2.length();
/*     */         }
/*     */         else {
/* 388 */           localStringBuffer.append(paramString.charAt(m++));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 393 */       localStringBuffer.append(str2);
/*     */     }
/*     */     else
/*     */     {
/* 404 */       m = 1;
/* 405 */       for (n = 0; n < i; )
/*     */       {
/* 407 */         if ((m != 0) && (isA(paramString, n, this.syntaxBeginQuote1))) {
/* 408 */           localStringBuffer.append(this.syntaxEscape).append(this.syntaxBeginQuote1);
/* 409 */           n += this.syntaxBeginQuote1.length();
/* 410 */         } else if ((m != 0) && (isA(paramString, n, this.syntaxBeginQuote2))) {
/* 411 */           localStringBuffer.append(this.syntaxEscape).append(this.syntaxBeginQuote2);
/* 412 */           n += this.syntaxBeginQuote2.length();
/*     */         }
/* 417 */         else if (isA(paramString, n, this.syntaxEscape)) {
/* 418 */           if (n + this.syntaxEscape.length() >= i)
/*     */           {
/* 420 */             localStringBuffer.append(this.syntaxEscape);
/* 421 */           } else if (isMeta(paramString, n + this.syntaxEscape.length()))
/*     */           {
/* 423 */             localStringBuffer.append(this.syntaxEscape);
/*     */           }
/* 425 */           localStringBuffer.append(this.syntaxEscape);
/* 426 */           n += this.syntaxEscape.length();
/*     */         }
/* 430 */         else if ((j != 0) && (paramString.startsWith(this.syntaxSeparator, n)))
/*     */         {
/* 432 */           localStringBuffer.append(this.syntaxEscape).append(this.syntaxSeparator);
/* 433 */           n += this.syntaxSeparator.length();
/* 434 */         } else if ((k != 0) && (paramString.startsWith(this.syntaxSeparator2, n)))
/*     */         {
/* 437 */           localStringBuffer.append(this.syntaxEscape).append(this.syntaxSeparator2);
/* 438 */           n += this.syntaxSeparator2.length();
/*     */         }
/*     */         else {
/* 441 */           localStringBuffer.append(paramString.charAt(n++));
/*     */         }
/* 443 */         m = 0;
/*     */       }
/*     */     }
/* 446 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 450 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 452 */     int i = 1;
/* 453 */     int j = this.components.size();
/*     */ 
/* 455 */     for (int k = 0; k < j; k++)
/*     */     {
/*     */       String str;
/* 456 */       if (this.syntaxDirection == 2) {
/* 457 */         str = stringifyComp((String)this.components.elementAt(j - 1 - k));
/*     */       }
/*     */       else {
/* 460 */         str = stringifyComp((String)this.components.elementAt(k));
/*     */       }
/* 462 */       if ((k != 0) && (this.syntaxSeparator != null))
/* 463 */         localStringBuffer.append(this.syntaxSeparator);
/* 464 */       if (str.length() >= 1)
/* 465 */         i = 0;
/* 466 */       localStringBuffer = localStringBuffer.append(str);
/*     */     }
/* 468 */     if ((i != 0) && (j >= 1) && (this.syntaxSeparator != null))
/* 469 */       localStringBuffer = localStringBuffer.append(this.syntaxSeparator);
/* 470 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 474 */     if ((paramObject != null) && ((paramObject instanceof NameImpl))) {
/* 475 */       NameImpl localNameImpl = (NameImpl)paramObject;
/* 476 */       if (localNameImpl.size() == size()) {
/* 477 */         Enumeration localEnumeration1 = getAll();
/* 478 */         Enumeration localEnumeration2 = localNameImpl.getAll();
/* 479 */         while (localEnumeration1.hasMoreElements())
/*     */         {
/* 481 */           String str1 = (String)localEnumeration1.nextElement();
/* 482 */           String str2 = (String)localEnumeration2.nextElement();
/* 483 */           if (this.syntaxTrimBlanks) {
/* 484 */             str1 = str1.trim();
/* 485 */             str2 = str2.trim();
/*     */           }
/* 487 */           if (this.syntaxCaseInsensitive) {
/* 488 */             if (!str1.equalsIgnoreCase(str2))
/* 489 */               return false;
/*     */           }
/* 491 */           else if (!str1.equals(str2)) {
/* 492 */             return false;
/*     */           }
/*     */         }
/* 495 */         return true;
/*     */       }
/*     */     }
/* 498 */     return false;
/*     */   }
/*     */ 
/*     */   public int compareTo(NameImpl paramNameImpl)
/*     */   {
/* 510 */     if (this == paramNameImpl) {
/* 511 */       return 0;
/*     */     }
/*     */ 
/* 514 */     int i = size();
/* 515 */     int j = paramNameImpl.size();
/* 516 */     int k = Math.min(i, j);
/*     */ 
/* 518 */     int m = 0; int n = 0;
/*     */ 
/* 520 */     while (k-- != 0) {
/* 521 */       String str1 = get(m++);
/* 522 */       String str2 = paramNameImpl.get(n++);
/*     */ 
/* 525 */       if (this.syntaxTrimBlanks) {
/* 526 */         str1 = str1.trim();
/* 527 */         str2 = str2.trim();
/*     */       }
/* 529 */       if (this.syntaxCaseInsensitive) {
/* 530 */         str1 = str1.toLowerCase();
/* 531 */         str2 = str2.toLowerCase();
/*     */       }
/* 533 */       int i1 = str1.compareTo(str2);
/* 534 */       if (i1 != 0) {
/* 535 */         return i1;
/*     */       }
/*     */     }
/*     */ 
/* 539 */     return i - j;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 543 */     return this.components.size();
/*     */   }
/*     */ 
/*     */   public Enumeration getAll() {
/* 547 */     return this.components.elements();
/*     */   }
/*     */ 
/*     */   public String get(int paramInt) {
/* 551 */     return (String)this.components.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public Enumeration getPrefix(int paramInt) {
/* 555 */     if ((paramInt < 0) || (paramInt > size())) {
/* 556 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*     */     }
/* 558 */     return new NameImplEnumerator(this.components, 0, paramInt);
/*     */   }
/*     */ 
/*     */   public Enumeration getSuffix(int paramInt) {
/* 562 */     int i = size();
/* 563 */     if ((paramInt < 0) || (paramInt > i)) {
/* 564 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*     */     }
/* 566 */     return new NameImplEnumerator(this.components, paramInt, i);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 570 */     return this.components.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean startsWith(int paramInt, Enumeration paramEnumeration) {
/* 574 */     if ((paramInt < 0) || (paramInt > size()))
/* 575 */       return false;
/*     */     try
/*     */     {
/* 578 */       Enumeration localEnumeration = getPrefix(paramInt);
/* 579 */       while (localEnumeration.hasMoreElements()) {
/* 580 */         String str1 = (String)localEnumeration.nextElement();
/* 581 */         String str2 = (String)paramEnumeration.nextElement();
/* 582 */         if (this.syntaxTrimBlanks) {
/* 583 */           str1 = str1.trim();
/* 584 */           str2 = str2.trim();
/*     */         }
/* 586 */         if (this.syntaxCaseInsensitive) {
/* 587 */           if (!str1.equalsIgnoreCase(str2))
/* 588 */             return false;
/*     */         }
/* 590 */         else if (!str1.equals(str2))
/* 591 */           return false;
/*     */       }
/*     */     }
/*     */     catch (NoSuchElementException localNoSuchElementException) {
/* 595 */       return false;
/*     */     }
/* 597 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean endsWith(int paramInt, Enumeration paramEnumeration)
/*     */   {
/* 605 */     int i = size() - paramInt;
/* 606 */     if ((i < 0) || (i > size()))
/* 607 */       return false;
/*     */     try
/*     */     {
/* 610 */       Enumeration localEnumeration = getSuffix(i);
/* 611 */       while (localEnumeration.hasMoreElements()) {
/* 612 */         String str1 = (String)localEnumeration.nextElement();
/* 613 */         String str2 = (String)paramEnumeration.nextElement();
/* 614 */         if (this.syntaxTrimBlanks) {
/* 615 */           str1 = str1.trim();
/* 616 */           str2 = str2.trim();
/*     */         }
/* 618 */         if (this.syntaxCaseInsensitive) {
/* 619 */           if (!str1.equalsIgnoreCase(str2))
/* 620 */             return false;
/*     */         }
/* 622 */         else if (!str1.equals(str2))
/* 623 */           return false;
/*     */       }
/*     */     }
/*     */     catch (NoSuchElementException localNoSuchElementException) {
/* 627 */       return false;
/*     */     }
/* 629 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Enumeration paramEnumeration) throws InvalidNameException {
/* 633 */     boolean bool = false;
/*     */     while (true) if (paramEnumeration.hasMoreElements())
/*     */         try {
/* 636 */           Object localObject = paramEnumeration.nextElement();
/* 637 */           if ((size() > 0) && (this.syntaxDirection == 0)) {
/* 638 */             throw new InvalidNameException("A flat name can only have a single component");
/*     */           }
/*     */ 
/* 641 */           this.components.addElement(localObject);
/* 642 */           bool = true;
/*     */         }
/*     */         catch (NoSuchElementException localNoSuchElementException)
/*     */         {
/*     */         }
/* 647 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Enumeration paramEnumeration) throws InvalidNameException
/*     */   {
/* 652 */     boolean bool = false;
/* 653 */     for (int i = paramInt; paramEnumeration.hasMoreElements(); i++) {
/*     */       try {
/* 655 */         Object localObject = paramEnumeration.nextElement();
/* 656 */         if ((size() > 0) && (this.syntaxDirection == 0)) {
/* 657 */           throw new InvalidNameException("A flat name can only have a single component");
/*     */         }
/*     */ 
/* 660 */         this.components.insertElementAt(localObject, i);
/* 661 */         bool = true;
/*     */       } catch (NoSuchElementException localNoSuchElementException) {
/* 663 */         break;
/*     */       }
/*     */     }
/* 666 */     return bool;
/*     */   }
/*     */ 
/*     */   public void add(String paramString) throws InvalidNameException {
/* 670 */     if ((size() > 0) && (this.syntaxDirection == 0)) {
/* 671 */       throw new InvalidNameException("A flat name can only have a single component");
/*     */     }
/*     */ 
/* 674 */     this.components.addElement(paramString);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, String paramString) throws InvalidNameException {
/* 678 */     if ((size() > 0) && (this.syntaxDirection == 0)) {
/* 679 */       throw new InvalidNameException("A flat name can only zero or one component");
/*     */     }
/*     */ 
/* 682 */     this.components.insertElementAt(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt) {
/* 686 */     Object localObject = this.components.elementAt(paramInt);
/* 687 */     this.components.removeElementAt(paramInt);
/* 688 */     return localObject;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 692 */     int i = 0;
/* 693 */     for (Enumeration localEnumeration = getAll(); localEnumeration.hasMoreElements(); ) {
/* 694 */       String str = (String)localEnumeration.nextElement();
/* 695 */       if (this.syntaxTrimBlanks) {
/* 696 */         str = str.trim();
/*     */       }
/* 698 */       if (this.syntaxCaseInsensitive) {
/* 699 */         str = str.toLowerCase();
/*     */       }
/*     */ 
/* 702 */       i += str.hashCode();
/*     */     }
/* 704 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.NameImpl
 * JD-Core Version:    0.6.2
 */