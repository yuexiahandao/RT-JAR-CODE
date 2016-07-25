/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ final class NativeMath extends IdScriptableObject
/*     */ {
/*  50 */   private static final Object MATH_TAG = "Math";
/*     */   private static final int Id_toSource = 1;
/*     */   private static final int Id_abs = 2;
/*     */   private static final int Id_acos = 3;
/*     */   private static final int Id_asin = 4;
/*     */   private static final int Id_atan = 5;
/*     */   private static final int Id_atan2 = 6;
/*     */   private static final int Id_ceil = 7;
/*     */   private static final int Id_cos = 8;
/*     */   private static final int Id_exp = 9;
/*     */   private static final int Id_floor = 10;
/*     */   private static final int Id_log = 11;
/*     */   private static final int Id_max = 12;
/*     */   private static final int Id_min = 13;
/*     */   private static final int Id_pow = 14;
/*     */   private static final int Id_random = 15;
/*     */   private static final int Id_round = 16;
/*     */   private static final int Id_sin = 17;
/*     */   private static final int Id_sqrt = 18;
/*     */   private static final int Id_tan = 19;
/*     */   private static final int LAST_METHOD_ID = 19;
/*     */   private static final int Id_E = 20;
/*     */   private static final int Id_PI = 21;
/*     */   private static final int Id_LN10 = 22;
/*     */   private static final int Id_LN2 = 23;
/*     */   private static final int Id_LOG2E = 24;
/*     */   private static final int Id_LOG10E = 25;
/*     */   private static final int Id_SQRT1_2 = 26;
/*     */   private static final int Id_SQRT2 = 27;
/*     */   private static final int MAX_ID = 27;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  54 */     NativeMath localNativeMath = new NativeMath();
/*  55 */     localNativeMath.activatePrototypeMap(27);
/*  56 */     localNativeMath.setPrototype(getObjectPrototype(paramScriptable));
/*  57 */     localNativeMath.setParentScope(paramScriptable);
/*  58 */     if (paramBoolean) localNativeMath.sealObject();
/*  59 */     ScriptableObject.defineProperty(paramScriptable, "Math", localNativeMath, 2);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  68 */     return "Math";
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     String str;
/*  73 */     if (paramInt <= 19)
/*     */     {
/*     */       int i;
/*  76 */       switch (paramInt) { case 1:
/*  77 */         i = 0; str = "toSource"; break;
/*     */       case 2:
/*  78 */         i = 1; str = "abs"; break;
/*     */       case 3:
/*  79 */         i = 1; str = "acos"; break;
/*     */       case 4:
/*  80 */         i = 1; str = "asin"; break;
/*     */       case 5:
/*  81 */         i = 1; str = "atan"; break;
/*     */       case 6:
/*  82 */         i = 2; str = "atan2"; break;
/*     */       case 7:
/*  83 */         i = 1; str = "ceil"; break;
/*     */       case 8:
/*  84 */         i = 1; str = "cos"; break;
/*     */       case 9:
/*  85 */         i = 1; str = "exp"; break;
/*     */       case 10:
/*  86 */         i = 1; str = "floor"; break;
/*     */       case 11:
/*  87 */         i = 1; str = "log"; break;
/*     */       case 12:
/*  88 */         i = 2; str = "max"; break;
/*     */       case 13:
/*  89 */         i = 2; str = "min"; break;
/*     */       case 14:
/*  90 */         i = 2; str = "pow"; break;
/*     */       case 15:
/*  91 */         i = 0; str = "random"; break;
/*     */       case 16:
/*  92 */         i = 1; str = "round"; break;
/*     */       case 17:
/*  93 */         i = 1; str = "sin"; break;
/*     */       case 18:
/*  94 */         i = 1; str = "sqrt"; break;
/*     */       case 19:
/*  95 */         i = 1; str = "tan"; break;
/*     */       default:
/*  96 */         throw new IllegalStateException(String.valueOf(paramInt));
/*     */       }
/*  98 */       initPrototypeMethod(MATH_TAG, paramInt, str, i);
/*     */     }
/*     */     else
/*     */     {
/*     */       double d;
/* 102 */       switch (paramInt) { case 20:
/* 103 */         d = 2.718281828459045D; str = "E"; break;
/*     */       case 21:
/* 104 */         d = 3.141592653589793D; str = "PI"; break;
/*     */       case 22:
/* 105 */         d = 2.302585092994046D; str = "LN10"; break;
/*     */       case 23:
/* 106 */         d = 0.6931471805599453D; str = "LN2"; break;
/*     */       case 24:
/* 107 */         d = 1.442695040888963D; str = "LOG2E"; break;
/*     */       case 25:
/* 108 */         d = 0.4342944819032518D; str = "LOG10E"; break;
/*     */       case 26:
/* 109 */         d = 0.7071067811865476D; str = "SQRT1_2"; break;
/*     */       case 27:
/* 110 */         d = 1.414213562373095D; str = "SQRT2"; break;
/*     */       default:
/* 111 */         throw new IllegalStateException(String.valueOf(paramInt));
/*     */       }
/* 113 */       initPrototypeValue(paramInt, str, ScriptRuntime.wrapNumber(d), 7);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 122 */     if (!paramIdFunctionObject.hasTag(MATH_TAG)) {
/* 123 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/*     */ 
/* 126 */     int i = paramIdFunctionObject.methodId();
/*     */     double d1;
/* 127 */     switch (i) {
/*     */     case 1:
/* 129 */       return "Math";
/*     */     case 2:
/* 132 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/*     */ 
/* 134 */       d1 = d1 < 0.0D ? -d1 : d1 == 0.0D ? 0.0D : d1;
/* 135 */       break;
/*     */     case 3:
/*     */     case 4:
/* 139 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 140 */       if ((d1 == d1) && (-1.0D <= d1) && (d1 <= 1.0D))
/* 141 */         d1 = i == 3 ? Math.acos(d1) : Math.asin(d1);
/*     */       else {
/* 143 */         d1 = (0.0D / 0.0D);
/*     */       }
/* 145 */       break;
/*     */     case 5:
/* 148 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 149 */       d1 = Math.atan(d1);
/* 150 */       break;
/*     */     case 6:
/* 153 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 154 */       d1 = Math.atan2(d1, ScriptRuntime.toNumber(paramArrayOfObject, 1));
/* 155 */       break;
/*     */     case 7:
/* 158 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 159 */       d1 = Math.ceil(d1);
/* 160 */       break;
/*     */     case 8:
/* 163 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 164 */       d1 = (d1 == (1.0D / 0.0D)) || (d1 == (-1.0D / 0.0D)) ? (0.0D / 0.0D) : Math.cos(d1);
/*     */ 
/* 167 */       break;
/*     */     case 9:
/* 170 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 171 */       d1 = d1 == (-1.0D / 0.0D) ? 0.0D : d1 == (1.0D / 0.0D) ? d1 : Math.exp(d1);
/*     */ 
/* 174 */       break;
/*     */     case 10:
/* 177 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 178 */       d1 = Math.floor(d1);
/* 179 */       break;
/*     */     case 11:
/* 182 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/*     */ 
/* 184 */       d1 = d1 < 0.0D ? (0.0D / 0.0D) : Math.log(d1);
/* 185 */       break;
/*     */     case 12:
/*     */     case 13:
/* 189 */       d1 = i == 12 ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*     */ 
/* 191 */       for (int j = 0; j != paramArrayOfObject.length; j++) {
/* 192 */         double d2 = ScriptRuntime.toNumber(paramArrayOfObject[j]);
/* 193 */         if (d2 != d2) {
/* 194 */           d1 = d2;
/* 195 */           break;
/*     */         }
/* 197 */         if (i == 12)
/*     */         {
/* 199 */           d1 = Math.max(d1, d2);
/*     */         }
/* 201 */         else d1 = Math.min(d1, d2);
/*     */       }
/*     */ 
/* 204 */       break;
/*     */     case 14:
/* 207 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 208 */       d1 = js_pow(d1, ScriptRuntime.toNumber(paramArrayOfObject, 1));
/* 209 */       break;
/*     */     case 15:
/* 212 */       d1 = Math.random();
/* 213 */       break;
/*     */     case 16:
/* 216 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 217 */       if ((d1 == d1) && (d1 != (1.0D / 0.0D)) && (d1 != (-1.0D / 0.0D)))
/*     */       {
/* 221 */         long l = Math.round(d1);
/* 222 */         if (l != 0L) {
/* 223 */           d1 = l;
/*     */         }
/* 226 */         else if (d1 < 0.0D)
/* 227 */           d1 = ScriptRuntime.negativeZero;
/* 228 */         else if (d1 != 0.0D) {
/* 229 */           d1 = 0.0D;
/*     */         }
/*     */       }
/* 232 */       break;
/*     */     case 17:
/* 236 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 237 */       d1 = (d1 == (1.0D / 0.0D)) || (d1 == (-1.0D / 0.0D)) ? (0.0D / 0.0D) : Math.sin(d1);
/*     */ 
/* 240 */       break;
/*     */     case 18:
/* 243 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 244 */       d1 = Math.sqrt(d1);
/* 245 */       break;
/*     */     case 19:
/* 248 */       d1 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/* 249 */       d1 = Math.tan(d1);
/* 250 */       break;
/*     */     default:
/* 252 */       throw new IllegalStateException(String.valueOf(i));
/*     */     }
/* 254 */     return ScriptRuntime.wrapNumber(d1);
/*     */   }
/*     */ 
/*     */   private double js_pow(double paramDouble1, double paramDouble2)
/*     */   {
/*     */     double d;
/* 260 */     if (paramDouble2 != paramDouble2)
/*     */     {
/* 262 */       d = paramDouble2;
/* 263 */     } else if (paramDouble2 == 0.0D)
/*     */     {
/* 265 */       d = 1.0D;
/*     */     }
/*     */     else
/*     */     {
/*     */       long l;
/* 266 */       if (paramDouble1 == 0.0D)
/*     */       {
/* 268 */         if (1.0D / paramDouble1 > 0.0D) {
/* 269 */           d = paramDouble2 > 0.0D ? 0.0D : (1.0D / 0.0D);
/*     */         }
/*     */         else {
/* 272 */           l = ()paramDouble2;
/* 273 */           if ((l == paramDouble2) && ((l & 1L) != 0L))
/* 274 */             d = paramDouble2 > 0.0D ? -0.0D : (-1.0D / 0.0D);
/*     */           else
/* 276 */             d = paramDouble2 > 0.0D ? 0.0D : (1.0D / 0.0D);
/*     */         }
/*     */       }
/*     */       else {
/* 280 */         d = Math.pow(paramDouble1, paramDouble2);
/* 281 */         if (d != d)
/*     */         {
/* 284 */           if (paramDouble2 == (1.0D / 0.0D)) {
/* 285 */             if ((paramDouble1 < -1.0D) || (1.0D < paramDouble1))
/* 286 */               d = (1.0D / 0.0D);
/* 287 */             else if ((-1.0D < paramDouble1) && (paramDouble1 < 1.0D))
/* 288 */               d = 0.0D;
/*     */           }
/* 290 */           else if (paramDouble2 == (-1.0D / 0.0D)) {
/* 291 */             if ((paramDouble1 < -1.0D) || (1.0D < paramDouble1))
/* 292 */               d = 0.0D;
/* 293 */             else if ((-1.0D < paramDouble1) && (paramDouble1 < 1.0D))
/* 294 */               d = (1.0D / 0.0D);
/*     */           }
/* 296 */           else if (paramDouble1 == (1.0D / 0.0D)) {
/* 297 */             d = paramDouble2 > 0.0D ? (1.0D / 0.0D) : 0.0D;
/* 298 */           } else if (paramDouble1 == (-1.0D / 0.0D)) {
/* 299 */             l = ()paramDouble2;
/* 300 */             if ((l == paramDouble2) && ((l & 1L) != 0L))
/*     */             {
/* 302 */               d = paramDouble2 > 0.0D ? (-1.0D / 0.0D) : -0.0D;
/*     */             }
/* 304 */             else d = paramDouble2 > 0.0D ? (1.0D / 0.0D) : 0.0D;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 309 */     return d;
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 319 */     int i = 0; String str = null;
/*     */     int j;
/* 320 */     switch (paramString.length()) { case 1:
/* 321 */       if (paramString.charAt(0) == 'E') i = 20; break;
/*     */     case 2:
/* 322 */       if ((paramString.charAt(0) == 'P') && (paramString.charAt(1) == 'I')) i = 21; break;
/*     */     case 3:
/* 323 */       switch (paramString.charAt(0)) { case 'L':
/* 324 */         if ((paramString.charAt(2) == '2') && (paramString.charAt(1) == 'N')) i = 23; break;
/*     */       case 'a':
/* 325 */         if ((paramString.charAt(2) == 's') && (paramString.charAt(1) == 'b')) i = 2; break;
/*     */       case 'c':
/* 326 */         if ((paramString.charAt(2) == 's') && (paramString.charAt(1) == 'o')) i = 8; break;
/*     */       case 'e':
/* 327 */         if ((paramString.charAt(2) == 'p') && (paramString.charAt(1) == 'x')) i = 9; break;
/*     */       case 'l':
/* 328 */         if ((paramString.charAt(2) == 'g') && (paramString.charAt(1) == 'o')) i = 11; break;
/*     */       case 'm':
/* 329 */         j = paramString.charAt(2);
/* 330 */         if (j == 110) { if (paramString.charAt(1) == 'i') { i = 13; break label743; }
/* 331 */         } else if ((j == 120) && (paramString.charAt(1) == 'a')) i = 12; break;
/*     */       case 'p':
/* 333 */         if ((paramString.charAt(2) == 'w') && (paramString.charAt(1) == 'o')) i = 14; break;
/*     */       case 's':
/* 334 */         if ((paramString.charAt(2) == 'n') && (paramString.charAt(1) == 'i')) i = 17; break;
/*     */       case 't':
/* 335 */         if ((paramString.charAt(2) == 'n') && (paramString.charAt(1) == 'a')) { i = 19; break label743; } break; }
/* 336 */       break;
/*     */     case 4:
/* 337 */       switch (paramString.charAt(1)) { case 'N':
/* 338 */         str = "LN10"; i = 22; break;
/*     */       case 'c':
/* 339 */         str = "acos"; i = 3; break;
/*     */       case 'e':
/* 340 */         str = "ceil"; i = 7; break;
/*     */       case 'q':
/* 341 */         str = "sqrt"; i = 18; break;
/*     */       case 's':
/* 342 */         str = "asin"; i = 4; break;
/*     */       case 't':
/* 343 */         str = "atan"; i = 5; }
/* 344 */       break;
/*     */     case 5:
/* 345 */       switch (paramString.charAt(0)) { case 'L':
/* 346 */         str = "LOG2E"; i = 24; break;
/*     */       case 'S':
/* 347 */         str = "SQRT2"; i = 27; break;
/*     */       case 'a':
/* 348 */         str = "atan2"; i = 6; break;
/*     */       case 'f':
/* 349 */         str = "floor"; i = 10; break;
/*     */       case 'r':
/* 350 */         str = "round"; i = 16; }
/* 351 */       break;
/*     */     case 6:
/* 352 */       j = paramString.charAt(0);
/* 353 */       if (j == 76) { str = "LOG10E"; i = 25;
/* 354 */       } else if (j == 114) { str = "random"; i = 15; } break;
/*     */     case 7:
/* 356 */       str = "SQRT1_2"; i = 26; break;
/*     */     case 8:
/* 357 */       str = "toSource"; i = 1; break;
/*     */     }
/* 359 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 362 */     label743: return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeMath
 * JD-Core Version:    0.6.2
 */