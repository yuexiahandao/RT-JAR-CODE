/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.text.Collator;
/*     */ 
/*     */ final class NativeString extends IdScriptableObject
/*     */ {
/*  60 */   private static final Object STRING_TAG = "String";
/*     */   private static final int Id_length = 1;
/*     */   private static final int MAX_INSTANCE_ID = 1;
/*     */   private static final int ConstructorId_fromCharCode = -1;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int Id_valueOf = 4;
/*     */   private static final int Id_charAt = 5;
/*     */   private static final int Id_charCodeAt = 6;
/*     */   private static final int Id_indexOf = 7;
/*     */   private static final int Id_lastIndexOf = 8;
/*     */   private static final int Id_split = 9;
/*     */   private static final int Id_substring = 10;
/*     */   private static final int Id_toLowerCase = 11;
/*     */   private static final int Id_toUpperCase = 12;
/*     */   private static final int Id_substr = 13;
/*     */   private static final int Id_concat = 14;
/*     */   private static final int Id_slice = 15;
/*     */   private static final int Id_bold = 16;
/*     */   private static final int Id_italics = 17;
/*     */   private static final int Id_fixed = 18;
/*     */   private static final int Id_strike = 19;
/*     */   private static final int Id_small = 20;
/*     */   private static final int Id_big = 21;
/*     */   private static final int Id_blink = 22;
/*     */   private static final int Id_sup = 23;
/*     */   private static final int Id_sub = 24;
/*     */   private static final int Id_fontsize = 25;
/*     */   private static final int Id_fontcolor = 26;
/*     */   private static final int Id_link = 27;
/*     */   private static final int Id_anchor = 28;
/*     */   private static final int Id_equals = 29;
/*     */   private static final int Id_equalsIgnoreCase = 30;
/*     */   private static final int Id_match = 31;
/*     */   private static final int Id_search = 32;
/*     */   private static final int Id_replace = 33;
/*     */   private static final int Id_localeCompare = 34;
/*     */   private static final int Id_toLocaleLowerCase = 35;
/*     */   private static final int Id_toLocaleUpperCase = 36;
/*     */   private static final int Id_trim = 37;
/*     */   private static final int MAX_PROTOTYPE_ID = 37;
/*     */   private static final int ConstructorId_charAt = -5;
/*     */   private static final int ConstructorId_charCodeAt = -6;
/*     */   private static final int ConstructorId_indexOf = -7;
/*     */   private static final int ConstructorId_lastIndexOf = -8;
/*     */   private static final int ConstructorId_split = -9;
/*     */   private static final int ConstructorId_substring = -10;
/*     */   private static final int ConstructorId_toLowerCase = -11;
/*     */   private static final int ConstructorId_toUpperCase = -12;
/*     */   private static final int ConstructorId_substr = -13;
/*     */   private static final int ConstructorId_concat = -14;
/*     */   private static final int ConstructorId_slice = -15;
/*     */   private static final int ConstructorId_equalsIgnoreCase = -30;
/*     */   private static final int ConstructorId_match = -31;
/*     */   private static final int ConstructorId_search = -32;
/*     */   private static final int ConstructorId_replace = -33;
/*     */   private static final int ConstructorId_localeCompare = -34;
/*     */   private static final int ConstructorId_toLocaleLowerCase = -35;
/*     */   private String string;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  64 */     NativeString localNativeString = new NativeString("");
/*  65 */     localNativeString.exportAsJSClass(37, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   private NativeString(String paramString) {
/*  69 */     this.string = paramString;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  74 */     return "String";
/*     */   }
/*     */ 
/*     */   protected int getMaxInstanceId()
/*     */   {
/*  84 */     return 1;
/*     */   }
/*     */ 
/*     */   protected int findInstanceIdInfo(String paramString)
/*     */   {
/*  90 */     if (paramString.equals("length")) {
/*  91 */       return instanceIdInfo(7, 1);
/*     */     }
/*  93 */     return super.findInstanceIdInfo(paramString);
/*     */   }
/*     */ 
/*     */   protected String getInstanceIdName(int paramInt)
/*     */   {
/*  99 */     if (paramInt == 1) return "length";
/* 100 */     return super.getInstanceIdName(paramInt);
/*     */   }
/*     */ 
/*     */   protected Object getInstanceIdValue(int paramInt)
/*     */   {
/* 106 */     if (paramInt == 1) {
/* 107 */       return ScriptRuntime.wrapInt(this.string.length());
/*     */     }
/* 109 */     return super.getInstanceIdValue(paramInt);
/*     */   }
/*     */ 
/*     */   protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 115 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -1, "fromCharCode", 1);
/*     */ 
/* 117 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -5, "charAt", 2);
/*     */ 
/* 119 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -6, "charCodeAt", 2);
/*     */ 
/* 121 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -7, "indexOf", 2);
/*     */ 
/* 123 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -8, "lastIndexOf", 2);
/*     */ 
/* 125 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -9, "split", 3);
/*     */ 
/* 127 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -10, "substring", 3);
/*     */ 
/* 129 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -11, "toLowerCase", 1);
/*     */ 
/* 131 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -12, "toUpperCase", 1);
/*     */ 
/* 133 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -13, "substr", 3);
/*     */ 
/* 135 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -14, "concat", 2);
/*     */ 
/* 137 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -15, "slice", 3);
/*     */ 
/* 139 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -30, "equalsIgnoreCase", 2);
/*     */ 
/* 141 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -31, "match", 2);
/*     */ 
/* 143 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -32, "search", 2);
/*     */ 
/* 145 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -33, "replace", 2);
/*     */ 
/* 147 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -34, "localeCompare", 2);
/*     */ 
/* 149 */     addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -35, "toLocaleLowerCase", 1);
/*     */ 
/* 151 */     super.fillConstructorProperties(paramIdFunctionObject);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 159 */     switch (paramInt) { case 1:
/* 160 */       i = 1; str = "constructor"; break;
/*     */     case 2:
/* 161 */       i = 0; str = "toString"; break;
/*     */     case 3:
/* 162 */       i = 0; str = "toSource"; break;
/*     */     case 4:
/* 163 */       i = 0; str = "valueOf"; break;
/*     */     case 5:
/* 164 */       i = 1; str = "charAt"; break;
/*     */     case 6:
/* 165 */       i = 1; str = "charCodeAt"; break;
/*     */     case 7:
/* 166 */       i = 1; str = "indexOf"; break;
/*     */     case 8:
/* 167 */       i = 1; str = "lastIndexOf"; break;
/*     */     case 9:
/* 168 */       i = 2; str = "split"; break;
/*     */     case 10:
/* 169 */       i = 2; str = "substring"; break;
/*     */     case 11:
/* 170 */       i = 0; str = "toLowerCase"; break;
/*     */     case 12:
/* 171 */       i = 0; str = "toUpperCase"; break;
/*     */     case 13:
/* 172 */       i = 2; str = "substr"; break;
/*     */     case 14:
/* 173 */       i = 1; str = "concat"; break;
/*     */     case 15:
/* 174 */       i = 2; str = "slice"; break;
/*     */     case 16:
/* 175 */       i = 0; str = "bold"; break;
/*     */     case 17:
/* 176 */       i = 0; str = "italics"; break;
/*     */     case 18:
/* 177 */       i = 0; str = "fixed"; break;
/*     */     case 19:
/* 178 */       i = 0; str = "strike"; break;
/*     */     case 20:
/* 179 */       i = 0; str = "small"; break;
/*     */     case 21:
/* 180 */       i = 0; str = "big"; break;
/*     */     case 22:
/* 181 */       i = 0; str = "blink"; break;
/*     */     case 23:
/* 182 */       i = 0; str = "sup"; break;
/*     */     case 24:
/* 183 */       i = 0; str = "sub"; break;
/*     */     case 25:
/* 184 */       i = 0; str = "fontsize"; break;
/*     */     case 26:
/* 185 */       i = 0; str = "fontcolor"; break;
/*     */     case 27:
/* 186 */       i = 0; str = "link"; break;
/*     */     case 28:
/* 187 */       i = 0; str = "anchor"; break;
/*     */     case 29:
/* 188 */       i = 1; str = "equals"; break;
/*     */     case 30:
/* 189 */       i = 1; str = "equalsIgnoreCase"; break;
/*     */     case 31:
/* 190 */       i = 1; str = "match"; break;
/*     */     case 32:
/* 191 */       i = 1; str = "search"; break;
/*     */     case 33:
/* 192 */       i = 1; str = "replace"; break;
/*     */     case 34:
/* 193 */       i = 1; str = "localeCompare"; break;
/*     */     case 35:
/* 194 */       i = 0; str = "toLocaleLowerCase"; break;
/*     */     case 36:
/* 195 */       i = 0; str = "toLocaleUpperCase"; break;
/*     */     case 37:
/* 196 */       i = 0; str = "trim"; break;
/*     */     default:
/* 197 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 199 */     initPrototypeMethod(STRING_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 206 */     if (!paramIdFunctionObject.hasTag(STRING_TAG)) {
/* 207 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 209 */     int i = paramIdFunctionObject.methodId();
/*     */     while (true)
/*     */     {
/* 212 */       switch (i) {
/*     */       case -35:
/*     */       case -34:
/*     */       case -33:
/*     */       case -32:
/*     */       case -31:
/*     */       case -30:
/*     */       case -15:
/*     */       case -14:
/*     */       case -13:
/*     */       case -12:
/*     */       case -11:
/*     */       case -10:
/*     */       case -9:
/*     */       case -8:
/*     */       case -7:
/*     */       case -6:
/*     */       case -5:
/* 230 */         if (paramArrayOfObject.length > 0) {
/* 231 */           paramScriptable2 = ScriptRuntime.toObject(paramScriptable1, ScriptRuntime.toString(paramArrayOfObject[0]));
/*     */ 
/* 233 */           Object[] arrayOfObject = new Object[paramArrayOfObject.length - 1];
/* 234 */           for (int m = 0; m < arrayOfObject.length; m++)
/* 235 */             arrayOfObject[m] = paramArrayOfObject[(m + 1)];
/* 236 */           paramArrayOfObject = arrayOfObject;
/*     */         } else {
/* 238 */           paramScriptable2 = ScriptRuntime.toObject(paramScriptable1, ScriptRuntime.toString(paramScriptable2));
/*     */         }
/*     */ 
/* 241 */         i = -i;
/*     */       case -1:
/*     */       case 1:
/*     */       case 2:
/*     */       case 4:
/*     */       case 3:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/*     */       case 28:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 32:
/*     */       case 33:
/*     */       case 34:
/*     */       case 35:
/*     */       case 36:
/*     */       case 37:
/*     */       case -29:
/*     */       case -28:
/*     */       case -27:
/*     */       case -26:
/*     */       case -25:
/*     */       case -24:
/*     */       case -23:
/*     */       case -22:
/*     */       case -21:
/*     */       case -20:
/*     */       case -19:
/*     */       case -18:
/*     */       case -17:
/*     */       case -16:
/*     */       case -4:
/*     */       case -3:
/*     */       case -2:
/* 246 */       case 0: }  } int j = paramArrayOfObject.length;
/* 247 */     if (j < 1)
/* 248 */       return "";
/* 249 */     StringBuffer localStringBuffer = new StringBuffer(j);
/* 250 */     for (int n = 0; n != j; n++) {
/* 251 */       localStringBuffer.append(ScriptRuntime.toUint16(paramArrayOfObject[n]));
/*     */     }
/* 253 */     return localStringBuffer.toString();
/*     */ 
/* 257 */     String str = paramArrayOfObject.length >= 1 ? ScriptRuntime.toString(paramArrayOfObject[0]) : "";
/*     */ 
/* 259 */     if (paramScriptable2 == null)
/*     */     {
/* 261 */       return new NativeString(str);
/*     */     }
/*     */ 
/* 264 */     return str;
/*     */ 
/* 270 */     return realThis(paramScriptable2, paramIdFunctionObject).string;
/*     */ 
/* 273 */     str = realThis(paramScriptable2, paramIdFunctionObject).string;
/* 274 */     return "(new String(\"" + ScriptRuntime.escapeString(str) + "\"))";
/*     */ 
/* 280 */     str = ScriptRuntime.toString(paramScriptable2);
/* 281 */     double d = ScriptRuntime.toInteger(paramArrayOfObject, 0);
/* 282 */     if ((d < 0.0D) || (d >= str.length())) {
/* 283 */       if (i == 5) return "";
/* 284 */       return ScriptRuntime.NaNobj;
/*     */     }
/* 286 */     char c = str.charAt((int)d);
/* 287 */     if (i == 5) return String.valueOf(c);
/* 288 */     return ScriptRuntime.wrapInt(c);
/*     */ 
/* 292 */     return ScriptRuntime.wrapInt(js_indexOf(ScriptRuntime.toString(paramScriptable2), paramArrayOfObject));
/*     */ 
/* 296 */     return ScriptRuntime.wrapInt(js_lastIndexOf(ScriptRuntime.toString(paramScriptable2), paramArrayOfObject));
/*     */ 
/* 300 */     return ScriptRuntime.checkRegExpProxy(paramContext).js_split(paramContext, paramScriptable1, ScriptRuntime.toString(paramScriptable2), paramArrayOfObject);
/*     */ 
/* 305 */     return js_substring(paramContext, ScriptRuntime.toString(paramScriptable2), paramArrayOfObject);
/*     */ 
/* 309 */     return ScriptRuntime.toString(paramScriptable2).toLowerCase(ScriptRuntime.ROOT_LOCALE);
/*     */ 
/* 314 */     return ScriptRuntime.toString(paramScriptable2).toUpperCase(ScriptRuntime.ROOT_LOCALE);
/*     */ 
/* 318 */     return js_substr(ScriptRuntime.toString(paramScriptable2), paramArrayOfObject);
/*     */ 
/* 321 */     return js_concat(ScriptRuntime.toString(paramScriptable2), paramArrayOfObject);
/*     */ 
/* 324 */     return js_slice(ScriptRuntime.toString(paramScriptable2), paramArrayOfObject);
/*     */ 
/* 327 */     return tagify(paramScriptable2, "b", null, null);
/*     */ 
/* 330 */     return tagify(paramScriptable2, "i", null, null);
/*     */ 
/* 333 */     return tagify(paramScriptable2, "tt", null, null);
/*     */ 
/* 336 */     return tagify(paramScriptable2, "strike", null, null);
/*     */ 
/* 339 */     return tagify(paramScriptable2, "small", null, null);
/*     */ 
/* 342 */     return tagify(paramScriptable2, "big", null, null);
/*     */ 
/* 345 */     return tagify(paramScriptable2, "blink", null, null);
/*     */ 
/* 348 */     return tagify(paramScriptable2, "sup", null, null);
/*     */ 
/* 351 */     return tagify(paramScriptable2, "sub", null, null);
/*     */ 
/* 354 */     return tagify(paramScriptable2, "font", "size", paramArrayOfObject);
/*     */ 
/* 357 */     return tagify(paramScriptable2, "font", "color", paramArrayOfObject);
/*     */ 
/* 360 */     return tagify(paramScriptable2, "a", "href", paramArrayOfObject);
/*     */ 
/* 363 */     return tagify(paramScriptable2, "a", "name", paramArrayOfObject);
/*     */ 
/* 367 */     str = ScriptRuntime.toString(paramScriptable2);
/* 368 */     Object localObject2 = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 369 */     return ScriptRuntime.wrapBoolean(i == 29 ? str.equals(localObject2) : str.equalsIgnoreCase((String)localObject2));
/*     */     int k;
/* 379 */     if (i == 31)
/* 380 */       k = 1;
/* 381 */     else if (i == 32)
/* 382 */       k = 3;
/*     */     else {
/* 384 */       k = 2;
/*     */     }
/* 386 */     return ScriptRuntime.checkRegExpProxy(paramContext).action(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, k);
/*     */ 
/* 396 */     Object localObject1 = Collator.getInstance(paramContext.getLocale());
/* 397 */     ((Collator)localObject1).setStrength(3);
/* 398 */     ((Collator)localObject1).setDecomposition(1);
/* 399 */     return ScriptRuntime.wrapNumber(((Collator)localObject1).compare(ScriptRuntime.toString(paramScriptable2), ScriptRuntime.toString(paramArrayOfObject, 0)));
/*     */ 
/* 405 */     return ScriptRuntime.toString(paramScriptable2).toLowerCase(paramContext.getLocale());
/*     */ 
/* 410 */     return ScriptRuntime.toString(paramScriptable2).toUpperCase(paramContext.getLocale());
/*     */ 
/* 415 */     localObject1 = ScriptRuntime.toString(paramScriptable2);
/* 416 */     localObject2 = ((String)localObject1).toCharArray();
/*     */ 
/* 418 */     n = 0;
/* 419 */     while ((n < localObject2.length) && (ScriptRuntime.isJSWhitespaceOrLineTerminator(localObject2[n]))) {
/* 420 */       n++;
/*     */     }
/* 422 */     int i1 = localObject2.length;
/* 423 */     while ((i1 > n) && (ScriptRuntime.isJSWhitespaceOrLineTerminator(localObject2[(i1 - 1)]))) {
/* 424 */       i1--;
/*     */     }
/*     */ 
/* 427 */     return ((String)localObject1).substring(n, i1);
/*     */ 
/* 430 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   private static NativeString realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject)
/*     */   {
/* 436 */     if (!(paramScriptable instanceof NativeString))
/* 437 */       throw incompatibleCallError(paramIdFunctionObject);
/* 438 */     return (NativeString)paramScriptable;
/*     */   }
/*     */ 
/*     */   private static String tagify(Object paramObject, String paramString1, String paramString2, Object[] paramArrayOfObject)
/*     */   {
/* 447 */     String str = ScriptRuntime.toString(paramObject);
/* 448 */     StringBuffer localStringBuffer = new StringBuffer();
/* 449 */     localStringBuffer.append('<');
/* 450 */     localStringBuffer.append(paramString1);
/* 451 */     if (paramString2 != null) {
/* 452 */       localStringBuffer.append(' ');
/* 453 */       localStringBuffer.append(paramString2);
/* 454 */       localStringBuffer.append("=\"");
/* 455 */       localStringBuffer.append(ScriptRuntime.toString(paramArrayOfObject, 0));
/* 456 */       localStringBuffer.append('"');
/*     */     }
/* 458 */     localStringBuffer.append('>');
/* 459 */     localStringBuffer.append(str);
/* 460 */     localStringBuffer.append("</");
/* 461 */     localStringBuffer.append(paramString1);
/* 462 */     localStringBuffer.append('>');
/* 463 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 468 */     return this.string;
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 476 */     if ((0 <= paramInt) && (paramInt < this.string.length())) {
/* 477 */       return this.string.substring(paramInt, paramInt + 1);
/*     */     }
/* 479 */     return super.get(paramInt, paramScriptable);
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 484 */     if ((0 <= paramInt) && (paramInt < this.string.length())) {
/* 485 */       return;
/*     */     }
/* 487 */     super.put(paramInt, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   private static int js_indexOf(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 496 */     String str = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 497 */     double d = ScriptRuntime.toInteger(paramArrayOfObject, 1);
/*     */ 
/* 499 */     if (d > paramString.length()) {
/* 500 */       return -1;
/*     */     }
/* 502 */     if (d < 0.0D)
/* 503 */       d = 0.0D;
/* 504 */     return paramString.indexOf(str, (int)d);
/*     */   }
/*     */ 
/*     */   private static int js_lastIndexOf(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 514 */     String str = ScriptRuntime.toString(paramArrayOfObject, 0);
/* 515 */     double d = ScriptRuntime.toNumber(paramArrayOfObject, 1);
/*     */ 
/* 517 */     if ((d != d) || (d > paramString.length()))
/* 518 */       d = paramString.length();
/* 519 */     else if (d < 0.0D) {
/* 520 */       d = 0.0D;
/*     */     }
/* 522 */     return paramString.lastIndexOf(str, (int)d);
/*     */   }
/*     */ 
/*     */   private static String js_substring(Context paramContext, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 532 */     int i = paramString.length();
/* 533 */     double d1 = ScriptRuntime.toInteger(paramArrayOfObject, 0);
/*     */ 
/* 536 */     if (d1 < 0.0D)
/* 537 */       d1 = 0.0D;
/* 538 */     else if (d1 > i)
/* 539 */       d1 = i;
/*     */     double d2;
/* 541 */     if ((paramArrayOfObject.length <= 1) || (paramArrayOfObject[1] == Undefined.instance)) {
/* 542 */       d2 = i;
/*     */     } else {
/* 544 */       d2 = ScriptRuntime.toInteger(paramArrayOfObject[1]);
/* 545 */       if (d2 < 0.0D)
/* 546 */         d2 = 0.0D;
/* 547 */       else if (d2 > i) {
/* 548 */         d2 = i;
/*     */       }
/*     */ 
/* 551 */       if (d2 < d1) {
/* 552 */         if (paramContext.getLanguageVersion() != 120) {
/* 553 */           double d3 = d1;
/* 554 */           d1 = d2;
/* 555 */           d2 = d3;
/*     */         }
/*     */         else {
/* 558 */           d2 = d1;
/*     */         }
/*     */       }
/*     */     }
/* 562 */     return paramString.substring((int)d1, (int)d2);
/*     */   }
/*     */ 
/*     */   int getLength() {
/* 566 */     return this.string.length();
/*     */   }
/*     */ 
/*     */   private static String js_substr(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 573 */     if (paramArrayOfObject.length < 1) {
/* 574 */       return paramString;
/*     */     }
/* 576 */     double d1 = ScriptRuntime.toInteger(paramArrayOfObject[0]);
/*     */ 
/* 578 */     int i = paramString.length();
/*     */ 
/* 580 */     if (d1 < 0.0D) {
/* 581 */       d1 += i;
/* 582 */       if (d1 < 0.0D)
/* 583 */         d1 = 0.0D;
/* 584 */     } else if (d1 > i) {
/* 585 */       d1 = i;
/*     */     }
/*     */     double d2;
/* 588 */     if (paramArrayOfObject.length == 1) {
/* 589 */       d2 = i;
/*     */     } else {
/* 591 */       d2 = ScriptRuntime.toInteger(paramArrayOfObject[1]);
/* 592 */       if (d2 < 0.0D)
/* 593 */         d2 = 0.0D;
/* 594 */       d2 += d1;
/* 595 */       if (d2 > i) {
/* 596 */         d2 = i;
/*     */       }
/*     */     }
/* 599 */     return paramString.substring((int)d1, (int)d2);
/*     */   }
/*     */ 
/*     */   private static String js_concat(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 606 */     int i = paramArrayOfObject.length;
/* 607 */     if (i == 0) return paramString;
/* 608 */     if (i == 1) {
/* 609 */       String str1 = ScriptRuntime.toString(paramArrayOfObject[0]);
/* 610 */       return paramString.concat(str1);
/*     */     }
/*     */ 
/* 615 */     int j = paramString.length();
/* 616 */     String[] arrayOfString = new String[i];
/* 617 */     for (int k = 0; k != i; k++) {
/* 618 */       String str2 = ScriptRuntime.toString(paramArrayOfObject[k]);
/* 619 */       arrayOfString[k] = str2;
/* 620 */       j += str2.length();
/*     */     }
/*     */ 
/* 623 */     StringBuffer localStringBuffer = new StringBuffer(j);
/* 624 */     localStringBuffer.append(paramString);
/* 625 */     for (int m = 0; m != i; m++) {
/* 626 */       localStringBuffer.append(arrayOfString[m]);
/*     */     }
/* 628 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String js_slice(String paramString, Object[] paramArrayOfObject) {
/* 632 */     if (paramArrayOfObject.length != 0) {
/* 633 */       double d1 = ScriptRuntime.toInteger(paramArrayOfObject[0]);
/*     */ 
/* 635 */       int i = paramString.length();
/* 636 */       if (d1 < 0.0D) {
/* 637 */         d1 += i;
/* 638 */         if (d1 < 0.0D)
/* 639 */           d1 = 0.0D;
/* 640 */       } else if (d1 > i) {
/* 641 */         d1 = i;
/*     */       }
/*     */       double d2;
/* 644 */       if (paramArrayOfObject.length == 1) {
/* 645 */         d2 = i;
/*     */       } else {
/* 647 */         d2 = ScriptRuntime.toInteger(paramArrayOfObject[1]);
/* 648 */         if (d2 < 0.0D) {
/* 649 */           d2 += i;
/* 650 */           if (d2 < 0.0D)
/* 651 */             d2 = 0.0D;
/* 652 */         } else if (d2 > i) {
/* 653 */           d2 = i;
/*     */         }
/* 655 */         if (d2 < d1)
/* 656 */           d2 = d1;
/*     */       }
/* 658 */       return paramString.substring((int)d1, (int)d2);
/*     */     }
/* 660 */     return paramString;
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 670 */     int i = 0; String str = null;
/*     */     int j;
/* 671 */     switch (paramString.length()) { case 3:
/* 672 */       j = paramString.charAt(2);
/* 673 */       if (j == 98) { if ((paramString.charAt(0) == 's') && (paramString.charAt(1) == 'u')) { i = 24; break label894; }
/* 674 */       } else if (j == 103) { if ((paramString.charAt(0) == 'b') && (paramString.charAt(1) == 'i')) { i = 21; break label894; }
/* 675 */       } else if ((j == 112) && (paramString.charAt(0) == 's') && (paramString.charAt(1) == 'u')) i = 23; break;
/*     */     case 4:
/* 677 */       j = paramString.charAt(0);
/* 678 */       if (j == 98) { str = "bold"; i = 16;
/* 679 */       } else if (j == 108) { str = "link"; i = 27;
/* 680 */       } else if (j == 116) { str = "trim"; i = 37; } break;
/*     */     case 5:
/* 682 */       switch (paramString.charAt(4)) { case 'd':
/* 683 */         str = "fixed"; i = 18; break;
/*     */       case 'e':
/* 684 */         str = "slice"; i = 15; break;
/*     */       case 'h':
/* 685 */         str = "match"; i = 31; break;
/*     */       case 'k':
/* 686 */         str = "blink"; i = 22; break;
/*     */       case 'l':
/* 687 */         str = "small"; i = 20; break;
/*     */       case 't':
/* 688 */         str = "split"; i = 9;
/*     */       case 'f':
/*     */       case 'g':
/*     */       case 'i':
/*     */       case 'j':
/*     */       case 'm':
/*     */       case 'n':
/*     */       case 'o':
/*     */       case 'p':
/*     */       case 'q':
/*     */       case 'r':
/* 689 */       case 's': } break;
/*     */     case 6:
/* 690 */       switch (paramString.charAt(1)) { case 'e':
/* 691 */         str = "search"; i = 32; break;
/*     */       case 'h':
/* 692 */         str = "charAt"; i = 5; break;
/*     */       case 'n':
/* 693 */         str = "anchor"; i = 28; break;
/*     */       case 'o':
/* 694 */         str = "concat"; i = 14; break;
/*     */       case 'q':
/* 695 */         str = "equals"; i = 29; break;
/*     */       case 't':
/* 696 */         str = "strike"; i = 19; break;
/*     */       case 'u':
/* 697 */         str = "substr"; i = 13;
/*     */       case 'f':
/*     */       case 'g':
/*     */       case 'i':
/*     */       case 'j':
/*     */       case 'k':
/*     */       case 'l':
/*     */       case 'm':
/*     */       case 'p':
/*     */       case 'r':
/* 698 */       case 's': } break;
/*     */     case 7:
/* 699 */       switch (paramString.charAt(1)) { case 'a':
/* 700 */         str = "valueOf"; i = 4; break;
/*     */       case 'e':
/* 701 */         str = "replace"; i = 33; break;
/*     */       case 'n':
/* 702 */         str = "indexOf"; i = 7; break;
/*     */       case 't':
/* 703 */         str = "italics"; i = 17; }
/* 704 */       break;
/*     */     case 8:
/* 705 */       j = paramString.charAt(4);
/* 706 */       if (j == 114) { str = "toString"; i = 2;
/* 707 */       } else if (j == 115) { str = "fontsize"; i = 25;
/* 708 */       } else if (j == 117) { str = "toSource"; i = 3; } break;
/*     */     case 9:
/* 710 */       j = paramString.charAt(0);
/* 711 */       if (j == 102) { str = "fontcolor"; i = 26;
/* 712 */       } else if (j == 115) { str = "substring"; i = 10; } break;
/*     */     case 10:
/* 714 */       str = "charCodeAt"; i = 6; break;
/*     */     case 11:
/* 715 */       switch (paramString.charAt(2)) { case 'L':
/* 716 */         str = "toLowerCase"; i = 11; break;
/*     */       case 'U':
/* 717 */         str = "toUpperCase"; i = 12; break;
/*     */       case 'n':
/* 718 */         str = "constructor"; i = 1; break;
/*     */       case 's':
/* 719 */         str = "lastIndexOf"; i = 8; }
/* 720 */       break;
/*     */     case 13:
/* 721 */       str = "localeCompare"; i = 34; break;
/*     */     case 16:
/* 722 */       str = "equalsIgnoreCase"; i = 30; break;
/*     */     case 17:
/* 723 */       j = paramString.charAt(8);
/* 724 */       if (j == 76) { str = "toLocaleLowerCase"; i = 35;
/* 725 */       } else if (j == 85) { str = "toLocaleUpperCase"; i = 36; } break;
/*     */     case 12:
/*     */     case 14:
/* 728 */     case 15: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 732 */     label894: return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeString
 * JD-Core Version:    0.6.2
 */