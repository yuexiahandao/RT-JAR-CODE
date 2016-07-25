/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLLib;
/*     */ 
/*     */ public class NativeGlobal
/*     */   implements IdFunctionCall
/*     */ {
/*     */   private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";
/* 786 */   private static final Object FTAG = "Global";
/*     */   private static final int Id_decodeURI = 1;
/*     */   private static final int Id_decodeURIComponent = 2;
/*     */   private static final int Id_encodeURI = 3;
/*     */   private static final int Id_encodeURIComponent = 4;
/*     */   private static final int Id_escape = 5;
/*     */   private static final int Id_eval = 6;
/*     */   private static final int Id_isFinite = 7;
/*     */   private static final int Id_isNaN = 8;
/*     */   private static final int Id_isXMLName = 9;
/*     */   private static final int Id_parseFloat = 10;
/*     */   private static final int Id_parseInt = 11;
/*     */   private static final int Id_unescape = 12;
/*     */   private static final int Id_uneval = 13;
/*     */   private static final int LAST_SCOPE_FUNCTION_ID = 13;
/*     */   private static final int Id_new_CommonError = 14;
/*     */ 
/*     */   public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  60 */     NativeGlobal localNativeGlobal = new NativeGlobal();
/*     */     Object localObject;
/*  62 */     for (int i = 1; i <= 13; i++)
/*     */     {
/*  64 */       int k = 1;
/*     */       String str1;
/*  65 */       switch (i) {
/*     */       case 1:
/*  67 */         str1 = "decodeURI";
/*  68 */         break;
/*     */       case 2:
/*  70 */         str1 = "decodeURIComponent";
/*  71 */         break;
/*     */       case 3:
/*  73 */         str1 = "encodeURI";
/*  74 */         break;
/*     */       case 4:
/*  76 */         str1 = "encodeURIComponent";
/*  77 */         break;
/*     */       case 5:
/*  79 */         str1 = "escape";
/*  80 */         break;
/*     */       case 6:
/*  82 */         str1 = "eval";
/*  83 */         break;
/*     */       case 7:
/*  85 */         str1 = "isFinite";
/*  86 */         break;
/*     */       case 8:
/*  88 */         str1 = "isNaN";
/*  89 */         break;
/*     */       case 9:
/*  91 */         str1 = "isXMLName";
/*  92 */         break;
/*     */       case 10:
/*  94 */         str1 = "parseFloat";
/*  95 */         break;
/*     */       case 11:
/*  97 */         str1 = "parseInt";
/*  98 */         k = 2;
/*  99 */         break;
/*     */       case 12:
/* 101 */         str1 = "unescape";
/* 102 */         break;
/*     */       case 13:
/* 104 */         str1 = "uneval";
/* 105 */         break;
/*     */       default:
/* 107 */         throw Kit.codeBug();
/*     */       }
/* 109 */       localObject = new IdFunctionObject(localNativeGlobal, FTAG, i, str1, k, paramScriptable);
/*     */ 
/* 111 */       if (paramBoolean) {
/* 112 */         ((IdFunctionObject)localObject).sealObject();
/*     */       }
/* 114 */       ((IdFunctionObject)localObject).exportAsScopeProperty();
/*     */     }
/*     */ 
/* 117 */     ScriptableObject.defineProperty(paramScriptable, "NaN", ScriptRuntime.NaNobj, 7);
/*     */ 
/* 120 */     ScriptableObject.defineProperty(paramScriptable, "Infinity", ScriptRuntime.wrapNumber((1.0D / 0.0D)), 7);
/*     */ 
/* 124 */     ScriptableObject.defineProperty(paramScriptable, "undefined", Undefined.instance, 7);
/*     */ 
/* 128 */     String[] arrayOfString = { "ConversionError", "EvalError", "RangeError", "ReferenceError", "SyntaxError", "TypeError", "URIError", "InternalError", "JavaException" };
/*     */ 
/* 144 */     for (int j = 0; j < arrayOfString.length; j++) {
/* 145 */       String str2 = arrayOfString[j];
/* 146 */       localObject = (ScriptableObject)ScriptRuntime.newObject(paramContext, paramScriptable, "Error", ScriptRuntime.emptyArgs);
/*     */ 
/* 149 */       ((ScriptableObject)localObject).put("name", (Scriptable)localObject, str2);
/* 150 */       ((ScriptableObject)localObject).put("message", (Scriptable)localObject, "");
/* 151 */       IdFunctionObject localIdFunctionObject = new IdFunctionObject(localNativeGlobal, FTAG, 14, str2, 1, paramScriptable);
/*     */ 
/* 154 */       localIdFunctionObject.markAsConstructor((Scriptable)localObject);
/* 155 */       ((ScriptableObject)localObject).put("constructor", (Scriptable)localObject, localIdFunctionObject);
/* 156 */       ((ScriptableObject)localObject).setAttributes("constructor", 2);
/* 157 */       if (paramBoolean) {
/* 158 */         ((ScriptableObject)localObject).sealObject();
/* 159 */         localIdFunctionObject.sealObject();
/*     */       }
/* 161 */       localIdFunctionObject.exportAsScopeProperty();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 168 */     if (paramIdFunctionObject.hasTag(FTAG)) {
/* 169 */       int i = paramIdFunctionObject.methodId();
/*     */       String str;
/*     */       boolean bool;
/*     */       double d;
/*     */       Object localObject;
/* 170 */       switch (i) {
/*     */       case 1:
/*     */       case 2:
/* 173 */         str = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 174 */         return decode(str, i == 1);
/*     */       case 3:
/*     */       case 4:
/* 179 */         str = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 180 */         return encode(str, i == 3);
/*     */       case 5:
/* 184 */         return js_escape(paramArrayOfObject);
/*     */       case 6:
/* 187 */         return js_eval(paramContext, paramScriptable1, paramArrayOfObject);
/*     */       case 7:
/* 191 */         if (paramArrayOfObject.length < 1) {
/* 192 */           bool = false;
/*     */         } else {
/* 194 */           d = ScriptRuntime.toNumber(paramArrayOfObject[0]);
/* 195 */           bool = (d == d) && (d != (1.0D / 0.0D)) && (d != (-1.0D / 0.0D));
/*     */         }
/*     */ 
/* 199 */         return ScriptRuntime.wrapBoolean(bool);
/*     */       case 8:
/* 205 */         if (paramArrayOfObject.length < 1) {
/* 206 */           bool = true;
/*     */         } else {
/* 208 */           d = ScriptRuntime.toNumber(paramArrayOfObject[0]);
/* 209 */           bool = d != d;
/*     */         }
/* 211 */         return ScriptRuntime.wrapBoolean(bool);
/*     */       case 9:
/* 215 */         localObject = paramArrayOfObject.length == 0 ? Undefined.instance : paramArrayOfObject[0];
/*     */ 
/* 217 */         XMLLib localXMLLib = XMLLib.extractFromScope(paramScriptable1);
/* 218 */         return ScriptRuntime.wrapBoolean(localXMLLib.isXMLName(paramContext, localObject));
/*     */       case 10:
/* 223 */         return js_parseFloat(paramArrayOfObject);
/*     */       case 11:
/* 226 */         return js_parseInt(paramArrayOfObject);
/*     */       case 12:
/* 229 */         return js_unescape(paramArrayOfObject);
/*     */       case 13:
/* 232 */         localObject = paramArrayOfObject.length != 0 ? paramArrayOfObject[0] : Undefined.instance;
/*     */ 
/* 234 */         return ScriptRuntime.uneval(paramContext, paramScriptable1, localObject);
/*     */       case 14:
/* 240 */         return NativeError.make(paramContext, paramScriptable1, paramIdFunctionObject, paramArrayOfObject);
/*     */       }
/*     */     }
/* 243 */     throw paramIdFunctionObject.unknown();
/*     */   }
/*     */ 
/*     */   private Object js_parseInt(Object[] paramArrayOfObject)
/*     */   {
/* 250 */     String str = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 251 */     int i = ScriptRuntime.toInt32(paramArrayOfObject, 1);
/*     */ 
/* 253 */     int j = str.length();
/* 254 */     if (j == 0) {
/* 255 */       return ScriptRuntime.NaNobj;
/* 257 */     }int k = 0;
/* 258 */     int m = 0;
/*     */     int n;
/*     */     do { n = str.charAt(m);
/* 262 */       if (!ScriptRuntime.isStrWhiteSpaceChar(n))
/*     */         break;
/* 264 */       m++; }
/* 265 */     while (m < j);
/*     */ 
/* 267 */     if (n != 43) { if ((k = n == 45 ? 1 : 0) == 0);
/*     */     } else m++;
/*     */ 
/* 271 */     if (i == 0) {
/* 272 */       i = -1; } else {
/* 273 */       if ((i < 2) || (i > 36))
/* 274 */         return ScriptRuntime.NaNobj;
/* 275 */       if ((i == 16) && (j - m > 1) && (str.charAt(m) == '0')) {
/* 276 */         n = str.charAt(m + 1);
/* 277 */         if ((n == 120) || (n == 88))
/* 278 */           m += 2;
/*     */       }
/*     */     }
/* 281 */     if (i == -1) {
/* 282 */       i = 10;
/* 283 */       if ((j - m > 1) && (str.charAt(m) == '0')) {
/* 284 */         n = str.charAt(m + 1);
/* 285 */         if ((n == 120) || (n == 88)) {
/* 286 */           i = 16;
/* 287 */           m += 2;
/* 288 */         } else if ((48 <= n) && (n <= 57)) {
/* 289 */           i = 8;
/* 290 */           m++;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 295 */     double d = ScriptRuntime.stringToNumber(str, m, i);
/* 296 */     return ScriptRuntime.wrapNumber(k != 0 ? -d : d);
/*     */   }
/*     */ 
/*     */   private Object js_parseFloat(Object[] paramArrayOfObject)
/*     */   {
/* 306 */     if (paramArrayOfObject.length < 1) {
/* 307 */       return ScriptRuntime.NaNobj;
/* 309 */     }String str = ScriptRuntime.toString(paramArrayOfObject[0]);
/* 310 */     int i = str.length();
/* 311 */     int j = 0;
/*     */     int k;
/*     */     while (true) { if (j == i) {
/* 316 */         return ScriptRuntime.NaNobj;
/*     */       }
/* 318 */       k = str.charAt(j);
/* 319 */       if (!ScriptRuntime.isStrWhiteSpaceChar(k)) {
/*     */         break;
/*     */       }
/* 322 */       j++;
/*     */     }
/*     */ 
/* 325 */     int m = j;
/* 326 */     if ((k == 43) || (k == 45)) {
/* 327 */       m++;
/* 328 */       if (m == i) {
/* 329 */         return ScriptRuntime.NaNobj;
/*     */       }
/* 331 */       k = str.charAt(m);
/*     */     }
/*     */ 
/* 334 */     if (k == 73)
/*     */     {
/* 336 */       if ((m + 8 <= i) && (str.regionMatches(m, "Infinity", 0, 8)))
/*     */       {
/*     */         double d;
/* 338 */         if (str.charAt(j) == '-')
/* 339 */           d = (-1.0D / 0.0D);
/*     */         else {
/* 341 */           d = (1.0D / 0.0D);
/*     */         }
/* 343 */         return ScriptRuntime.wrapNumber(d);
/*     */       }
/* 345 */       return ScriptRuntime.NaNobj;
/*     */     }
/*     */ 
/* 349 */     int n = -1;
/* 350 */     int i1 = -1;
/* 351 */     int i2 = 0;
/* 352 */     for (; m < i; m++) {
/* 353 */       switch (str.charAt(m)) {
/*     */       case '.':
/* 355 */         if (n != -1)
/*     */           break label526;
/* 357 */         n = m;
/* 358 */         break;
/*     */       case 'E':
/*     */       case 'e':
/* 362 */         if (i1 != -1)
/*     */           break label526;
/* 364 */         if (m == i - 1) {
/*     */           break label526;
/*     */         }
/* 367 */         i1 = m;
/* 368 */         break;
/*     */       case '+':
/*     */       case '-':
/* 373 */         if (i1 != m - 1)
/*     */           break label526;
/* 375 */         if (m == i - 1)
/* 376 */           m--;
/* 377 */         break;
/*     */       case '0':
/*     */       case '1':
/*     */       case '2':
/*     */       case '3':
/*     */       case '4':
/*     */       case '5':
/*     */       case '6':
/*     */       case '7':
/*     */       case '8':
/*     */       case '9':
/* 383 */         if (i1 != -1)
/* 384 */           i2 = 1; break;
/*     */       case ',':
/*     */       case '/':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '?':
/*     */       case '@':
/*     */       case 'A':
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'D':
/*     */       case 'F':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'I':
/*     */       case 'J':
/*     */       case 'K':
/*     */       case 'L':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'S':
/*     */       case 'T':
/*     */       case 'U':
/*     */       case 'V':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y':
/*     */       case 'Z':
/*     */       case '[':
/*     */       case '\\':
/*     */       case ']':
/*     */       case '^':
/*     */       case '_':
/*     */       case '`':
/*     */       case 'a':
/*     */       case 'b':
/*     */       case 'c':
/*     */       case 'd':
/*     */       default:
/* 389 */         break label526;
/*     */       }
/*     */     }
/*     */ 
/* 393 */     label526: if ((i1 != -1) && (i2 == 0)) {
/* 394 */       m = i1;
/*     */     }
/* 396 */     str = str.substring(j, m);
/*     */     try {
/* 398 */       return Double.valueOf(str);
/*     */     } catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 401 */     return ScriptRuntime.NaNobj;
/*     */   }
/*     */ 
/*     */   private Object js_escape(Object[] paramArrayOfObject)
/*     */   {
/* 419 */     String str = ScriptRuntime.toString(paramArrayOfObject, 0);
/*     */ 
/* 421 */     int i = 7;
/* 422 */     if (paramArrayOfObject.length > 1) {
/* 423 */       double d = ScriptRuntime.toNumber(paramArrayOfObject[1]);
/* 424 */       if ((d != d) || ((i = (int)d) != d) || (0 != (i & 0xFFFFFFF8)))
/*     */       {
/* 427 */         throw Context.reportRuntimeError0("msg.bad.esc.mask");
/*     */       }
/*     */     }
/*     */ 
/* 431 */     StringBuffer localStringBuffer = null;
/* 432 */     int j = 0; for (int k = str.length(); j != k; j++) {
/* 433 */       int m = str.charAt(j);
/* 434 */       if ((i != 0) && (((m >= 48) && (m <= 57)) || ((m >= 65) && (m <= 90)) || ((m >= 97) && (m <= 122)) || (m == 64) || (m == 42) || (m == 95) || (m == 45) || (m == 46) || ((0 != (i & 0x4)) && ((m == 47) || (m == 43)))))
/*     */       {
/* 440 */         if (localStringBuffer != null)
/* 441 */           localStringBuffer.append((char)m);
/*     */       }
/*     */       else {
/* 444 */         if (localStringBuffer == null) {
/* 445 */           localStringBuffer = new StringBuffer(k + 3);
/* 446 */           localStringBuffer.append(str);
/* 447 */           localStringBuffer.setLength(j);
/*     */         }
/*     */         int n;
/* 451 */         if (m < 256) {
/* 452 */           if ((m == 32) && (i == 2)) {
/* 453 */             localStringBuffer.append('+');
/* 454 */             continue;
/*     */           }
/* 456 */           localStringBuffer.append('%');
/* 457 */           n = 2;
/*     */         } else {
/* 459 */           localStringBuffer.append('%');
/* 460 */           localStringBuffer.append('u');
/* 461 */           n = 4;
/*     */         }
/*     */ 
/* 465 */         for (int i1 = (n - 1) * 4; i1 >= 0; i1 -= 4) {
/* 466 */           int i2 = 0xF & m >> i1;
/* 467 */           int i3 = i2 < 10 ? 48 + i2 : 55 + i2;
/* 468 */           localStringBuffer.append((char)i3);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 473 */     return localStringBuffer == null ? str : localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private Object js_unescape(Object[] paramArrayOfObject)
/*     */   {
/* 482 */     String str = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 483 */     int i = str.indexOf('%');
/* 484 */     if (i >= 0) {
/* 485 */       int j = str.length();
/* 486 */       char[] arrayOfChar = str.toCharArray();
/* 487 */       int k = i;
/* 488 */       for (int m = i; m != j; ) {
/* 489 */         int n = arrayOfChar[m];
/* 490 */         m++;
/* 491 */         if ((n == 37) && (m != j))
/*     */         {
/*     */           int i2;
/*     */           int i1;
/* 493 */           if (arrayOfChar[m] == 'u') {
/* 494 */             i2 = m + 1;
/* 495 */             i1 = m + 5;
/*     */           } else {
/* 497 */             i2 = m;
/* 498 */             i1 = m + 2;
/*     */           }
/* 500 */           if (i1 <= j) {
/* 501 */             int i3 = 0;
/* 502 */             for (int i4 = i2; i4 != i1; i4++) {
/* 503 */               i3 = Kit.xDigitToInt(arrayOfChar[i4], i3);
/*     */             }
/* 505 */             if (i3 >= 0) {
/* 506 */               n = (char)i3;
/* 507 */               m = i1;
/*     */             }
/*     */           }
/*     */         }
/* 511 */         arrayOfChar[k] = n;
/* 512 */         k++;
/*     */       }
/* 514 */       str = new String(arrayOfChar, 0, k);
/*     */     }
/* 516 */     return str;
/*     */   }
/*     */ 
/*     */   private Object js_eval(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */   {
/* 525 */     Scriptable localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 526 */     return ScriptRuntime.evalSpecial(paramContext, localScriptable, localScriptable, paramArrayOfObject, "eval code", 1);
/*     */   }
/*     */ 
/*     */   static boolean isEvalFunction(Object paramObject)
/*     */   {
/* 531 */     if ((paramObject instanceof IdFunctionObject)) {
/* 532 */       IdFunctionObject localIdFunctionObject = (IdFunctionObject)paramObject;
/* 533 */       if ((localIdFunctionObject.hasTag(FTAG)) && (localIdFunctionObject.methodId() == 6)) {
/* 534 */         return true;
/*     */       }
/*     */     }
/* 537 */     return false;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static EcmaError constructError(Context paramContext, String paramString1, String paramString2, Scriptable paramScriptable)
/*     */   {
/* 549 */     return ScriptRuntime.constructError(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static EcmaError constructError(Context paramContext, String paramString1, String paramString2, Scriptable paramScriptable, String paramString3, int paramInt1, int paramInt2, String paramString4)
/*     */   {
/* 566 */     return ScriptRuntime.constructError(paramString1, paramString2, paramString3, paramInt1, paramString4, paramInt2);
/*     */   }
/*     */ 
/*     */   private static String encode(String paramString, boolean paramBoolean)
/*     */   {
/* 579 */     byte[] arrayOfByte = null;
/* 580 */     StringBuffer localStringBuffer = null;
/*     */ 
/* 582 */     int i = 0; for (int j = paramString.length(); i != j; i++) {
/* 583 */       int k = paramString.charAt(i);
/* 584 */       if (encodeUnescaped(k, paramBoolean)) {
/* 585 */         if (localStringBuffer != null)
/* 586 */           localStringBuffer.append(k);
/*     */       }
/*     */       else {
/* 589 */         if (localStringBuffer == null) {
/* 590 */           localStringBuffer = new StringBuffer(j + 3);
/* 591 */           localStringBuffer.append(paramString);
/* 592 */           localStringBuffer.setLength(i);
/* 593 */           arrayOfByte = new byte[6];
/*     */         }
/* 595 */         if ((56320 <= k) && (k <= 57343))
/* 596 */           throw Context.reportRuntimeError0("msg.bad.uri");
/*     */         int n;
/* 599 */         if ((k < 55296) || (56319 < k)) {
/* 600 */           int m = k;
/*     */         } else {
/* 602 */           i++;
/* 603 */           if (i == j) {
/* 604 */             throw Context.reportRuntimeError0("msg.bad.uri");
/*     */           }
/* 606 */           i1 = paramString.charAt(i);
/* 607 */           if ((56320 > i1) || (i1 > 57343)) {
/* 608 */             throw Context.reportRuntimeError0("msg.bad.uri");
/*     */           }
/* 610 */           n = (k - 55296 << 10) + (i1 - 56320) + 65536;
/*     */         }
/* 612 */         int i1 = oneUcs4ToUtf8Char(arrayOfByte, n);
/* 613 */         for (int i2 = 0; i2 < i1; i2++) {
/* 614 */           int i3 = 0xFF & arrayOfByte[i2];
/* 615 */           localStringBuffer.append('%');
/* 616 */           localStringBuffer.append(toHexChar(i3 >>> 4));
/* 617 */           localStringBuffer.append(toHexChar(i3 & 0xF));
/*     */         }
/*     */       }
/*     */     }
/* 621 */     return localStringBuffer == null ? paramString : localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static char toHexChar(int paramInt) {
/* 625 */     if (paramInt >> 4 != 0) Kit.codeBug();
/* 626 */     return (char)(paramInt < 10 ? paramInt + 48 : paramInt - 10 + 65);
/*     */   }
/*     */ 
/*     */   private static int unHex(char paramChar) {
/* 630 */     if (('A' <= paramChar) && (paramChar <= 'F'))
/* 631 */       return paramChar - 'A' + 10;
/* 632 */     if (('a' <= paramChar) && (paramChar <= 'f'))
/* 633 */       return paramChar - 'a' + 10;
/* 634 */     if (('0' <= paramChar) && (paramChar <= '9')) {
/* 635 */       return paramChar - '0';
/*     */     }
/* 637 */     return -1;
/*     */   }
/*     */ 
/*     */   private static int unHex(char paramChar1, char paramChar2)
/*     */   {
/* 642 */     int i = unHex(paramChar1);
/* 643 */     int j = unHex(paramChar2);
/* 644 */     if ((i >= 0) && (j >= 0)) {
/* 645 */       return i << 4 | j;
/*     */     }
/* 647 */     return -1;
/*     */   }
/*     */ 
/*     */   private static String decode(String paramString, boolean paramBoolean) {
/* 651 */     char[] arrayOfChar = null;
/* 652 */     int i = 0;
/*     */ 
/* 654 */     int j = 0; for (int k = paramString.length(); j != k; ) {
/* 655 */       int m = paramString.charAt(j);
/* 656 */       if (m != 37) {
/* 657 */         if (arrayOfChar != null) {
/* 658 */           arrayOfChar[(i++)] = m;
/*     */         }
/* 660 */         j++;
/*     */       } else {
/* 662 */         if (arrayOfChar == null)
/*     */         {
/* 665 */           arrayOfChar = new char[k];
/* 666 */           paramString.getChars(0, j, arrayOfChar, 0);
/* 667 */           i = j;
/*     */         }
/* 669 */         int n = j;
/* 670 */         if (j + 3 > k)
/* 671 */           throw Context.reportRuntimeError0("msg.bad.uri");
/* 672 */         int i1 = unHex(paramString.charAt(j + 1), paramString.charAt(j + 2));
/* 673 */         if (i1 < 0) throw Context.reportRuntimeError0("msg.bad.uri");
/* 674 */         j += 3;
/*     */         int i2;
/* 675 */         if ((i1 & 0x80) == 0) {
/* 676 */           m = (char)i1;
/*     */         }
/*     */         else
/*     */         {
/* 681 */           if ((i1 & 0xC0) == 128)
/*     */           {
/* 683 */             throw Context.reportRuntimeError0("msg.bad.uri");
/*     */           }
/*     */           int i3;
/*     */           int i4;
/* 684 */           if ((i1 & 0x20) == 0) {
/* 685 */             i2 = 1; i3 = i1 & 0x1F;
/* 686 */             i4 = 128;
/* 687 */           } else if ((i1 & 0x10) == 0) {
/* 688 */             i2 = 2; i3 = i1 & 0xF;
/* 689 */             i4 = 2048;
/* 690 */           } else if ((i1 & 0x8) == 0) {
/* 691 */             i2 = 3; i3 = i1 & 0x7;
/* 692 */             i4 = 65536;
/* 693 */           } else if ((i1 & 0x4) == 0) {
/* 694 */             i2 = 4; i3 = i1 & 0x3;
/* 695 */             i4 = 2097152;
/* 696 */           } else if ((i1 & 0x2) == 0) {
/* 697 */             i2 = 5; i3 = i1 & 0x1;
/* 698 */             i4 = 67108864;
/*     */           }
/*     */           else {
/* 701 */             throw Context.reportRuntimeError0("msg.bad.uri");
/*     */           }
/* 703 */           if (j + 3 * i2 > k)
/* 704 */             throw Context.reportRuntimeError0("msg.bad.uri");
/* 705 */           for (int i5 = 0; i5 != i2; i5++) {
/* 706 */             if (paramString.charAt(j) != '%')
/* 707 */               throw Context.reportRuntimeError0("msg.bad.uri");
/* 708 */             i1 = unHex(paramString.charAt(j + 1), paramString.charAt(j + 2));
/* 709 */             if ((i1 < 0) || ((i1 & 0xC0) != 128))
/* 710 */               throw Context.reportRuntimeError0("msg.bad.uri");
/* 711 */             i3 = i3 << 6 | i1 & 0x3F;
/* 712 */             j += 3;
/*     */           }
/*     */ 
/* 715 */           if ((i3 < i4) || (i3 == 65534) || (i3 == 65535))
/*     */           {
/* 718 */             i3 = 65533;
/*     */           }
/* 720 */           if (i3 >= 65536) {
/* 721 */             i3 -= 65536;
/* 722 */             if (i3 > 1048575)
/* 723 */               throw Context.reportRuntimeError0("msg.bad.uri");
/* 724 */             i5 = (char)((i3 >>> 10) + 55296);
/* 725 */             m = (char)((i3 & 0x3FF) + 56320);
/* 726 */             arrayOfChar[(i++)] = i5;
/*     */           } else {
/* 728 */             m = (char)i3;
/*     */           }
/*     */         }
/* 731 */         if ((paramBoolean) && (";/?:@&=+$,#".indexOf(m) >= 0)) {
/* 732 */           for (i2 = n; i2 != j; i2++)
/* 733 */             arrayOfChar[(i++)] = paramString.charAt(i2);
/*     */         }
/*     */         else {
/* 736 */           arrayOfChar[(i++)] = m;
/*     */         }
/*     */       }
/*     */     }
/* 740 */     return arrayOfChar == null ? paramString : new String(arrayOfChar, 0, i);
/*     */   }
/*     */ 
/*     */   private static boolean encodeUnescaped(char paramChar, boolean paramBoolean) {
/* 744 */     if ((('A' <= paramChar) && (paramChar <= 'Z')) || (('a' <= paramChar) && (paramChar <= 'z')) || (('0' <= paramChar) && (paramChar <= '9')))
/*     */     {
/* 747 */       return true;
/*     */     }
/* 749 */     if ("-_.!~*'()".indexOf(paramChar) >= 0)
/* 750 */       return true;
/* 751 */     if (paramBoolean) {
/* 752 */       return ";/?:@&=+$,#".indexOf(paramChar) >= 0;
/*     */     }
/* 754 */     return false;
/*     */   }
/*     */ 
/*     */   private static int oneUcs4ToUtf8Char(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 763 */     int i = 1;
/*     */ 
/* 766 */     if ((paramInt & 0xFFFFFF80) == 0) {
/* 767 */       paramArrayOfByte[0] = ((byte)paramInt);
/*     */     }
/*     */     else {
/* 770 */       int k = paramInt >>> 11;
/* 771 */       i = 2;
/* 772 */       while (k != 0) {
/* 773 */         k >>>= 5;
/* 774 */         i++;
/*     */       }
/* 776 */       int j = i;
/*     */       while (true) { j--; if (j <= 0) break;
/* 778 */         paramArrayOfByte[j] = ((byte)(paramInt & 0x3F | 0x80));
/* 779 */         paramInt >>>= 6;
/*     */       }
/* 781 */       paramArrayOfByte[0] = ((byte)(256 - (1 << 8 - i) + paramInt));
/*     */     }
/* 783 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeGlobal
 * JD-Core Version:    0.6.2
 */