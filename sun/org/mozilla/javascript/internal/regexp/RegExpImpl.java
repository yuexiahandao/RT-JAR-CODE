/*     */ package sun.org.mozilla.javascript.internal.regexp;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.Kit;
/*     */ import sun.org.mozilla.javascript.internal.RegExpProxy;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ 
/*     */ public class RegExpImpl
/*     */   implements RegExpProxy
/*     */ {
/*     */   protected String input;
/*     */   protected boolean multiline;
/*     */   protected SubString[] parens;
/*     */   protected SubString lastMatch;
/*     */   protected SubString lastParen;
/*     */   protected SubString leftContext;
/*     */   protected SubString rightContext;
/*     */ 
/*     */   public boolean isRegExp(Scriptable paramScriptable)
/*     */   {
/*  48 */     return paramScriptable instanceof NativeRegExp;
/*     */   }
/*     */ 
/*     */   public Object compileRegExp(Context paramContext, String paramString1, String paramString2)
/*     */   {
/*  53 */     return NativeRegExp.compileRE(paramContext, paramString1, paramString2, false);
/*     */   }
/*     */ 
/*     */   public Scriptable wrapRegExp(Context paramContext, Scriptable paramScriptable, Object paramObject)
/*     */   {
/*  59 */     return new NativeRegExp(paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public Object action(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, int paramInt)
/*     */   {
/*  66 */     GlobData localGlobData = new GlobData();
/*  67 */     localGlobData.mode = paramInt;
/*     */     Object localObject1;
/*  69 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/*  73 */       localGlobData.optarg = 1;
/*  74 */       localObject1 = matchOrReplace(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, this, localGlobData, false);
/*     */ 
/*  76 */       return localGlobData.arrayobj == null ? localObject1 : localGlobData.arrayobj;
/*     */     case 3:
/*  80 */       localGlobData.optarg = 1;
/*  81 */       return matchOrReplace(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, this, localGlobData, false);
/*     */     case 2:
/*  86 */       localObject1 = paramArrayOfObject.length < 2 ? Undefined.instance : paramArrayOfObject[1];
/*  87 */       String str = null;
/*  88 */       Function localFunction = null;
/*  89 */       if ((localObject1 instanceof Function))
/*  90 */         localFunction = (Function)localObject1;
/*     */       else {
/*  92 */         str = ScriptRuntime.toString(localObject1);
/*     */       }
/*     */ 
/*  95 */       localGlobData.optarg = 2;
/*  96 */       localGlobData.lambda = localFunction;
/*  97 */       localGlobData.repstr = str;
/*  98 */       localGlobData.dollar = (str == null ? -1 : str.indexOf('$'));
/*  99 */       localGlobData.charBuf = null;
/* 100 */       localGlobData.leftIndex = 0;
/* 101 */       Object localObject2 = matchOrReplace(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, this, localGlobData, true);
/*     */ 
/* 103 */       SubString localSubString1 = this.rightContext;
/*     */ 
/* 105 */       if (localGlobData.charBuf == null) {
/* 106 */         if ((localGlobData.global) || (localObject2 == null) || (!localObject2.equals(Boolean.TRUE)))
/*     */         {
/* 110 */           return localGlobData.str;
/*     */         }
/* 112 */         SubString localSubString2 = this.leftContext;
/* 113 */         replace_glob(localGlobData, paramContext, paramScriptable1, this, localSubString2.index, localSubString2.length);
/*     */       }
/* 115 */       localGlobData.charBuf.append(localSubString1.charArray, localSubString1.index, localSubString1.length);
/* 116 */       return localGlobData.charBuf.toString();
/*     */     }
/*     */ 
/* 120 */     throw Kit.codeBug();
/*     */   }
/*     */ 
/*     */   private static Object matchOrReplace(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, RegExpImpl paramRegExpImpl, GlobData paramGlobData, boolean paramBoolean)
/*     */   {
/* 134 */     String str = ScriptRuntime.toString(paramScriptable2);
/* 135 */     paramGlobData.str = str;
/* 136 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(paramScriptable1);
/*     */     NativeRegExp localNativeRegExp;
/* 138 */     if (paramArrayOfObject.length == 0) {
/* 139 */       localObject1 = NativeRegExp.compileRE(paramContext, "", "", false);
/* 140 */       localNativeRegExp = new NativeRegExp(localScriptable, localObject1);
/* 141 */     } else if ((paramArrayOfObject[0] instanceof NativeRegExp)) {
/* 142 */       localNativeRegExp = (NativeRegExp)paramArrayOfObject[0];
/*     */     } else {
/* 144 */       localObject1 = ScriptRuntime.toString(paramArrayOfObject[0]);
/*     */ 
/* 146 */       if (paramGlobData.optarg < paramArrayOfObject.length) {
/* 147 */         paramArrayOfObject[0] = localObject1;
/* 148 */         localObject2 = ScriptRuntime.toString(paramArrayOfObject[paramGlobData.optarg]);
/*     */       } else {
/* 150 */         localObject2 = null;
/*     */       }
/* 152 */       Object localObject3 = NativeRegExp.compileRE(paramContext, (String)localObject1, (String)localObject2, paramBoolean);
/* 153 */       localNativeRegExp = new NativeRegExp(localScriptable, localObject3);
/*     */     }
/*     */ 
/* 156 */     paramGlobData.global = ((localNativeRegExp.getFlags() & 0x1) != 0);
/* 157 */     Object localObject1 = { 0 };
/* 158 */     Object localObject2 = null;
/* 159 */     if (paramGlobData.mode == 3) {
/* 160 */       localObject2 = localNativeRegExp.executeRegExp(paramContext, paramScriptable1, paramRegExpImpl, str, (int[])localObject1, 0);
/*     */ 
/* 162 */       if ((localObject2 != null) && (localObject2.equals(Boolean.TRUE)))
/* 163 */         localObject2 = Integer.valueOf(paramRegExpImpl.leftContext.length);
/*     */       else
/* 165 */         localObject2 = Integer.valueOf(-1);
/* 166 */     } else if (paramGlobData.global) {
/* 167 */       localNativeRegExp.lastIndex = 0.0D;
/* 168 */       for (int i = 0; localObject1[0] <= str.length(); i++) {
/* 169 */         localObject2 = localNativeRegExp.executeRegExp(paramContext, paramScriptable1, paramRegExpImpl, str, (int[])localObject1, 0);
/*     */ 
/* 171 */         if ((localObject2 == null) || (!localObject2.equals(Boolean.TRUE)))
/*     */           break;
/* 173 */         if (paramGlobData.mode == 1) {
/* 174 */           match_glob(paramGlobData, paramContext, paramScriptable1, i, paramRegExpImpl);
/*     */         } else {
/* 176 */           if (paramGlobData.mode != 2) Kit.codeBug();
/* 177 */           SubString localSubString = paramRegExpImpl.lastMatch;
/* 178 */           int j = paramGlobData.leftIndex;
/* 179 */           int k = localSubString.index - j;
/* 180 */           paramGlobData.leftIndex = (localSubString.index + localSubString.length);
/* 181 */           replace_glob(paramGlobData, paramContext, paramScriptable1, paramRegExpImpl, j, k);
/*     */         }
/* 183 */         if (paramRegExpImpl.lastMatch.length == 0) {
/* 184 */           if (localObject1[0] == str.length())
/*     */             break;
/* 186 */           localObject1[0] += 1;
/*     */         }
/*     */       }
/*     */     } else {
/* 190 */       localObject2 = localNativeRegExp.executeRegExp(paramContext, paramScriptable1, paramRegExpImpl, str, (int[])localObject1, paramGlobData.mode == 2 ? 0 : 1);
/*     */     }
/*     */ 
/* 196 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public int find_split(Context paramContext, Scriptable paramScriptable1, String paramString1, String paramString2, Scriptable paramScriptable2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean[] paramArrayOfBoolean, String[][] paramArrayOfString)
/*     */   {
/* 206 */     int i = paramArrayOfInt1[0];
/* 207 */     int j = paramString1.length();
/*     */ 
/* 210 */     int m = paramContext.getLanguageVersion();
/* 211 */     NativeRegExp localNativeRegExp = (NativeRegExp)paramScriptable2;
/*     */     SubString localSubString;
/*     */     while (true)
/*     */     {
/* 215 */       n = paramArrayOfInt1[0];
/* 216 */       paramArrayOfInt1[0] = i;
/* 217 */       Object localObject = localNativeRegExp.executeRegExp(paramContext, paramScriptable1, this, paramString1, paramArrayOfInt1, 0);
/*     */ 
/* 219 */       if (localObject != Boolean.TRUE)
/*     */       {
/* 221 */         paramArrayOfInt1[0] = n;
/* 222 */         paramArrayOfInt2[0] = 1;
/* 223 */         paramArrayOfBoolean[0] = false;
/* 224 */         return j;
/*     */       }
/* 226 */       i = paramArrayOfInt1[0];
/* 227 */       paramArrayOfInt1[0] = n;
/* 228 */       paramArrayOfBoolean[0] = true;
/*     */ 
/* 230 */       localSubString = this.lastMatch;
/* 231 */       paramArrayOfInt2[0] = localSubString.length;
/* 232 */       if (paramArrayOfInt2[0] != 0)
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 239 */       if (i != paramArrayOfInt1[0])
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 246 */       if (i == j) {
/* 247 */         if (m == 120) {
/* 248 */           paramArrayOfInt2[0] = 1;
/* 249 */           k = i; break label176;
/*     */         }
/*     */ 
/* 252 */         k = -1;
/* 253 */         break label176;
/*     */       }
/* 255 */       i++;
/*     */     }
/*     */ 
/* 260 */     int k = i - paramArrayOfInt2[0];
/*     */ 
/* 263 */     label176: int n = this.parens == null ? 0 : this.parens.length;
/* 264 */     paramArrayOfString[0] = new String[n];
/* 265 */     for (int i1 = 0; i1 < n; i1++) {
/* 266 */       localSubString = getParenSubString(i1);
/* 267 */       paramArrayOfString[0][i1] = localSubString.toString();
/*     */     }
/* 269 */     return k;
/*     */   }
/*     */ 
/*     */   SubString getParenSubString(int paramInt)
/*     */   {
/* 278 */     if ((this.parens != null) && (paramInt < this.parens.length)) {
/* 279 */       SubString localSubString = this.parens[paramInt];
/* 280 */       if (localSubString != null) {
/* 281 */         return localSubString;
/*     */       }
/*     */     }
/* 284 */     return SubString.emptySubString;
/*     */   }
/*     */ 
/*     */   private static void match_glob(GlobData paramGlobData, Context paramContext, Scriptable paramScriptable, int paramInt, RegExpImpl paramRegExpImpl)
/*     */   {
/* 294 */     if (paramGlobData.arrayobj == null) {
/* 295 */       localObject = ScriptableObject.getTopLevelScope(paramScriptable);
/* 296 */       paramGlobData.arrayobj = ScriptRuntime.newObject(paramContext, (Scriptable)localObject, "Array", null);
/*     */     }
/* 298 */     Object localObject = paramRegExpImpl.lastMatch;
/* 299 */     String str = ((SubString)localObject).toString();
/* 300 */     paramGlobData.arrayobj.put(paramInt, paramGlobData.arrayobj, str);
/*     */   }
/*     */ 
/*     */   private static void replace_glob(GlobData paramGlobData, Context paramContext, Scriptable paramScriptable, RegExpImpl paramRegExpImpl, int paramInt1, int paramInt2)
/*     */   {
/*     */     Object localObject1;
/*     */     int k;
/*     */     Object localObject2;
/*     */     String str;
/*     */     int i;
/* 312 */     if (paramGlobData.lambda != null)
/*     */     {
/* 315 */       localObject1 = paramRegExpImpl.parens;
/* 316 */       k = localObject1 == null ? 0 : localObject1.length;
/* 317 */       localObject2 = new Object[k + 3];
/* 318 */       localObject2[0] = paramRegExpImpl.lastMatch.toString();
/*     */       Scriptable localScriptable;
/* 319 */       for (int m = 0; m < k; m++) {
/* 320 */         localScriptable = localObject1[m];
/* 321 */         if (localScriptable != null)
/* 322 */           localObject2[(m + 1)] = localScriptable.toString();
/*     */         else {
/* 324 */           localObject2[(m + 1)] = Undefined.instance;
/*     */         }
/*     */       }
/* 327 */       localObject2[(k + 1)] = Integer.valueOf(paramRegExpImpl.leftContext.length);
/* 328 */       localObject2[(k + 2)] = paramGlobData.str;
/*     */ 
/* 333 */       if (paramRegExpImpl != ScriptRuntime.getRegExpProxy(paramContext)) Kit.codeBug();
/* 334 */       RegExpImpl localRegExpImpl = new RegExpImpl();
/* 335 */       localRegExpImpl.multiline = paramRegExpImpl.multiline;
/* 336 */       localRegExpImpl.input = paramRegExpImpl.input;
/* 337 */       ScriptRuntime.setRegExpProxy(paramContext, localRegExpImpl);
/*     */       try {
/* 339 */         localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 340 */         Object localObject3 = paramGlobData.lambda.call(paramContext, localScriptable, localScriptable, (Object[])localObject2);
/* 341 */         str = ScriptRuntime.toString(localObject3);
/*     */       } finally {
/* 343 */         ScriptRuntime.setRegExpProxy(paramContext, paramRegExpImpl);
/*     */       }
/* 345 */       i = str.length();
/*     */     } else {
/* 347 */       str = null;
/* 348 */       i = paramGlobData.repstr.length();
/* 349 */       if (paramGlobData.dollar >= 0) {
/* 350 */         localObject1 = new int[1];
/* 351 */         k = paramGlobData.dollar;
/*     */         do {
/* 353 */           localObject2 = interpretDollar(paramContext, paramRegExpImpl, paramGlobData.repstr, k, (int[])localObject1);
/*     */ 
/* 355 */           if (localObject2 != null) {
/* 356 */             i += ((SubString)localObject2).length - localObject1[0];
/* 357 */             k += localObject1[0];
/*     */           } else {
/* 359 */             k++;
/*     */           }
/* 361 */           k = paramGlobData.repstr.indexOf('$', k);
/* 362 */         }while (k >= 0);
/*     */       }
/*     */     }
/*     */ 
/* 366 */     int j = paramInt2 + i + paramRegExpImpl.rightContext.length;
/* 367 */     StringBuffer localStringBuffer = paramGlobData.charBuf;
/* 368 */     if (localStringBuffer == null) {
/* 369 */       localStringBuffer = new StringBuffer(j);
/* 370 */       paramGlobData.charBuf = localStringBuffer;
/*     */     } else {
/* 372 */       localStringBuffer.ensureCapacity(paramGlobData.charBuf.length() + j);
/*     */     }
/*     */ 
/* 375 */     localStringBuffer.append(paramRegExpImpl.leftContext.charArray, paramInt1, paramInt2);
/* 376 */     if (paramGlobData.lambda != null)
/* 377 */       localStringBuffer.append(str);
/*     */     else
/* 379 */       do_replace(paramGlobData, paramContext, paramRegExpImpl);
/*     */   }
/*     */ 
/*     */   private static SubString interpretDollar(Context paramContext, RegExpImpl paramRegExpImpl, String paramString, int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 389 */     if (paramString.charAt(paramInt) != '$') Kit.codeBug();
/*     */ 
/* 392 */     int k = paramContext.getLanguageVersion();
/* 393 */     if ((k != 0) && (k <= 140))
/*     */     {
/* 396 */       if ((paramInt > 0) && (paramString.charAt(paramInt - 1) == '\\'))
/* 397 */         return null;
/*     */     }
/* 399 */     int m = paramString.length();
/* 400 */     if (paramInt + 1 >= m) {
/* 401 */       return null;
/*     */     }
/* 403 */     char c = paramString.charAt(paramInt + 1);
/* 404 */     if (NativeRegExp.isDigit(c))
/*     */     {
/*     */       int j;
/* 406 */       if ((k != 0) && (k <= 140))
/*     */       {
/* 409 */         if (c == '0') {
/* 410 */           return null;
/*     */         }
/* 412 */         i = 0;
/* 413 */         n = paramInt;
/*     */         while (true) { n++; if ((n >= m) || (!NativeRegExp.isDigit(c = paramString.charAt(n))))
/*     */             break;
/* 416 */           j = 10 * i + (c - '0');
/* 417 */           if (j < i)
/*     */             break;
/* 419 */           i = j;
/*     */         }
/*     */       }
/*     */ 
/* 423 */       int i1 = paramRegExpImpl.parens == null ? 0 : paramRegExpImpl.parens.length;
/* 424 */       int i = c - '0';
/* 425 */       if (i > i1)
/* 426 */         return null;
/* 427 */       int n = paramInt + 2;
/* 428 */       if (paramInt + 2 < m) {
/* 429 */         c = paramString.charAt(paramInt + 2);
/* 430 */         if (NativeRegExp.isDigit(c)) {
/* 431 */           j = 10 * i + (c - '0');
/* 432 */           if (j <= i1) {
/* 433 */             n++;
/* 434 */             i = j;
/*     */           }
/*     */         }
/*     */       }
/* 438 */       if (i == 0) return null;
/*     */ 
/* 441 */       i--;
/* 442 */       paramArrayOfInt[0] = (n - paramInt);
/* 443 */       return paramRegExpImpl.getParenSubString(i);
/*     */     }
/*     */ 
/* 446 */     paramArrayOfInt[0] = 2;
/* 447 */     switch (c) {
/*     */     case '$':
/* 449 */       return new SubString("$");
/*     */     case '&':
/* 451 */       return paramRegExpImpl.lastMatch;
/*     */     case '+':
/* 453 */       return paramRegExpImpl.lastParen;
/*     */     case '`':
/* 455 */       if (k == 120)
/*     */       {
/* 463 */         paramRegExpImpl.leftContext.index = 0;
/* 464 */         paramRegExpImpl.leftContext.length = paramRegExpImpl.lastMatch.index;
/*     */       }
/* 466 */       return paramRegExpImpl.leftContext;
/*     */     case '\'':
/* 468 */       return paramRegExpImpl.rightContext;
/*     */     }
/* 470 */     return null;
/*     */   }
/*     */ 
/*     */   private static void do_replace(GlobData paramGlobData, Context paramContext, RegExpImpl paramRegExpImpl)
/*     */   {
/* 479 */     StringBuffer localStringBuffer = paramGlobData.charBuf;
/* 480 */     int i = 0;
/* 481 */     String str = paramGlobData.repstr;
/* 482 */     int j = paramGlobData.dollar;
/* 483 */     if (j != -1) {
/* 484 */       int[] arrayOfInt = new int[1];
/*     */       do {
/* 486 */         int m = j - i;
/* 487 */         localStringBuffer.append(str.substring(i, j));
/* 488 */         i = j;
/* 489 */         SubString localSubString = interpretDollar(paramContext, paramRegExpImpl, str, j, arrayOfInt);
/*     */ 
/* 491 */         if (localSubString != null) {
/* 492 */           m = localSubString.length;
/* 493 */           if (m > 0) {
/* 494 */             localStringBuffer.append(localSubString.charArray, localSubString.index, m);
/*     */           }
/* 496 */           i += arrayOfInt[0];
/* 497 */           j += arrayOfInt[0];
/*     */         } else {
/* 499 */           j++;
/*     */         }
/* 501 */         j = str.indexOf('$', j);
/* 502 */       }while (j >= 0);
/*     */     }
/* 504 */     int k = str.length();
/* 505 */     if (k > i)
/* 506 */       localStringBuffer.append(str.substring(i, k));
/*     */   }
/*     */ 
/*     */   public Object js_split(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 519 */     Scriptable localScriptable1 = ScriptableObject.getTopLevelScope(paramScriptable);
/* 520 */     Scriptable localScriptable2 = ScriptRuntime.newObject(paramContext, localScriptable1, "Array", null);
/*     */ 
/* 525 */     if (paramArrayOfObject.length < 1) {
/* 526 */       localScriptable2.put(0, localScriptable2, paramString);
/* 527 */       return localScriptable2;
/*     */     }
/*     */ 
/* 531 */     int i = (paramArrayOfObject.length > 1) && (paramArrayOfObject[1] != Undefined.instance) ? 1 : 0;
/* 532 */     long l = 0L;
/* 533 */     if (i != 0)
/*     */     {
/* 535 */       l = ScriptRuntime.toUint32(paramArrayOfObject[1]);
/* 536 */       if (l > paramString.length()) {
/* 537 */         l = 1 + paramString.length();
/*     */       }
/*     */     }
/* 540 */     String str1 = null;
/* 541 */     int[] arrayOfInt = new int[1];
/* 542 */     Object localObject1 = null;
/* 543 */     RegExpProxy localRegExpProxy = null;
/* 544 */     if ((paramArrayOfObject[0] instanceof Scriptable)) {
/* 545 */       localRegExpProxy = ScriptRuntime.getRegExpProxy(paramContext);
/* 546 */       if (localRegExpProxy != null) {
/* 547 */         localObject2 = (Scriptable)paramArrayOfObject[0];
/* 548 */         if (localRegExpProxy.isRegExp((Scriptable)localObject2)) {
/* 549 */           localObject1 = localObject2;
/*     */         }
/*     */       }
/*     */     }
/* 553 */     if (localObject1 == null) {
/* 554 */       str1 = ScriptRuntime.toString(paramArrayOfObject[0]);
/* 555 */       arrayOfInt[0] = str1.length();
/*     */     }
/*     */ 
/* 559 */     Object localObject2 = { 0 };
/*     */ 
/* 561 */     int k = 0;
/* 562 */     boolean[] arrayOfBoolean = { false };
/* 563 */     String[][] arrayOfString; = { null };
/* 564 */     int m = paramContext.getLanguageVersion();
/*     */     int j;
/* 567 */     while ((j = find_split(paramContext, paramScriptable, paramString, str1, m, localRegExpProxy, localObject1, (int[])localObject2, arrayOfInt, arrayOfBoolean, arrayOfString;)) >= 0)
/*     */     {
/* 569 */       if (((i != 0) && (k >= l)) || (j > paramString.length()))
/*     */         break;
/*     */       String str2;
/* 573 */       if (paramString.length() == 0)
/* 574 */         str2 = paramString;
/*     */       else {
/* 576 */         str2 = paramString.substring(localObject2[0], j);
/*     */       }
/* 578 */       localScriptable2.put(k, localScriptable2, str2);
/* 579 */       k++;
/*     */ 
/* 585 */       if ((localObject1 != null) && (arrayOfBoolean[0] == 1)) {
/* 586 */         int n = arrayOfString;[0].length;
/* 587 */         for (int i1 = 0; (i1 < n) && (
/* 588 */           (i == 0) || (k < l)); i1++)
/*     */         {
/* 590 */           localScriptable2.put(k, localScriptable2, arrayOfString;[0][i1]);
/* 591 */           k++;
/*     */         }
/* 593 */         arrayOfBoolean[0] = false;
/*     */       }
/* 595 */       localObject2[0] = (j + arrayOfInt[0]);
/*     */ 
/* 597 */       if ((m < 130) && (m != 0) && 
/* 604 */         (i == 0) && (localObject2[0] == paramString.length())) {
/*     */         break;
/*     */       }
/*     */     }
/* 608 */     return localScriptable2;
/*     */   }
/*     */ 
/*     */   private static int find_split(Context paramContext, Scriptable paramScriptable1, String paramString1, String paramString2, int paramInt, RegExpProxy paramRegExpProxy, Scriptable paramScriptable2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean[] paramArrayOfBoolean, String[][] paramArrayOfString)
/*     */   {
/* 629 */     int i = paramArrayOfInt1[0];
/* 630 */     int j = paramString1.length();
/*     */ 
/* 637 */     if ((paramInt == 120) && (paramScriptable2 == null) && (paramString2.length() == 1) && (paramString2.charAt(0) == ' '))
/*     */     {
/* 641 */       if (i == 0) {
/* 642 */         while ((i < j) && (Character.isWhitespace(paramString1.charAt(i))))
/* 643 */           i++;
/* 644 */         paramArrayOfInt1[0] = i;
/*     */       }
/*     */ 
/* 648 */       if (i == j) {
/* 649 */         return -1;
/*     */       }
/*     */ 
/* 653 */       while ((i < j) && (!Character.isWhitespace(paramString1.charAt(i)))) {
/* 654 */         i++;
/*     */       }
/*     */ 
/* 657 */       int k = i;
/* 658 */       while ((k < j) && (Character.isWhitespace(paramString1.charAt(k)))) {
/* 659 */         k++;
/*     */       }
/*     */ 
/* 662 */       paramArrayOfInt2[0] = (k - i);
/* 663 */       return i;
/*     */     }
/*     */ 
/* 676 */     if (i > j) {
/* 677 */       return -1;
/*     */     }
/*     */ 
/* 684 */     if (paramScriptable2 != null) {
/* 685 */       return paramRegExpProxy.find_split(paramContext, paramScriptable1, paramString1, paramString2, paramScriptable2, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfBoolean, paramArrayOfString);
/*     */     }
/*     */ 
/* 694 */     if ((paramInt != 0) && (paramInt < 130) && (j == 0))
/*     */     {
/* 696 */       return -1;
/*     */     }
/*     */ 
/* 708 */     if (paramString2.length() == 0) {
/* 709 */       if (paramInt == 120) {
/* 710 */         if (i == j) {
/* 711 */           paramArrayOfInt2[0] = 1;
/* 712 */           return i;
/*     */         }
/* 714 */         return i + 1;
/*     */       }
/* 716 */       return i == j ? -1 : i + 1;
/*     */     }
/*     */ 
/* 722 */     if (paramArrayOfInt1[0] >= j) {
/* 723 */       return j;
/*     */     }
/* 725 */     i = paramString1.indexOf(paramString2, paramArrayOfInt1[0]);
/*     */ 
/* 727 */     return i != -1 ? i : j;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.RegExpImpl
 * JD-Core Version:    0.6.2
 */