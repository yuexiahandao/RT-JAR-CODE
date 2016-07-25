/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ final class NativeNumber extends IdScriptableObject
/*     */ {
/*  52 */   private static final Object NUMBER_TAG = "Number";
/*     */   private static final int MAX_PRECISION = 100;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toLocaleString = 3;
/*     */   private static final int Id_toSource = 4;
/*     */   private static final int Id_valueOf = 5;
/*     */   private static final int Id_toFixed = 6;
/*     */   private static final int Id_toExponential = 7;
/*     */   private static final int Id_toPrecision = 8;
/*     */   private static final int MAX_PROTOTYPE_ID = 8;
/*     */   private double doubleValue;
/*     */ 
/*     */   static void init(Scriptable paramScriptable, boolean paramBoolean)
/*     */   {
/*  58 */     NativeNumber localNativeNumber = new NativeNumber(0.0D);
/*  59 */     localNativeNumber.exportAsJSClass(8, paramScriptable, paramBoolean);
/*     */   }
/*     */ 
/*     */   private NativeNumber(double paramDouble)
/*     */   {
/*  64 */     this.doubleValue = paramDouble;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  70 */     return "Number";
/*     */   }
/*     */ 
/*     */   protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject)
/*     */   {
/*  80 */     paramIdFunctionObject.defineProperty("NaN", ScriptRuntime.NaNobj, 7);
/*  81 */     paramIdFunctionObject.defineProperty("POSITIVE_INFINITY", ScriptRuntime.wrapNumber((1.0D / 0.0D)), 7);
/*     */ 
/*  84 */     paramIdFunctionObject.defineProperty("NEGATIVE_INFINITY", ScriptRuntime.wrapNumber((-1.0D / 0.0D)), 7);
/*     */ 
/*  87 */     paramIdFunctionObject.defineProperty("MAX_VALUE", ScriptRuntime.wrapNumber(1.7976931348623157E+308D), 7);
/*     */ 
/*  90 */     paramIdFunctionObject.defineProperty("MIN_VALUE", ScriptRuntime.wrapNumber(4.9E-324D), 7);
/*     */ 
/*  94 */     super.fillConstructorProperties(paramIdFunctionObject);
/*     */   }
/*     */ 
/*     */   protected void initPrototypeId(int paramInt)
/*     */   {
/*     */     int i;
/*     */     String str;
/* 102 */     switch (paramInt) { case 1:
/* 103 */       i = 1; str = "constructor"; break;
/*     */     case 2:
/* 104 */       i = 1; str = "toString"; break;
/*     */     case 3:
/* 105 */       i = 1; str = "toLocaleString"; break;
/*     */     case 4:
/* 106 */       i = 0; str = "toSource"; break;
/*     */     case 5:
/* 107 */       i = 0; str = "valueOf"; break;
/*     */     case 6:
/* 108 */       i = 1; str = "toFixed"; break;
/*     */     case 7:
/* 109 */       i = 1; str = "toExponential"; break;
/*     */     case 8:
/* 110 */       i = 1; str = "toPrecision"; break;
/*     */     default:
/* 111 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */     }
/* 113 */     initPrototypeMethod(NUMBER_TAG, paramInt, str, i);
/*     */   }
/*     */ 
/*     */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 120 */     if (!paramIdFunctionObject.hasTag(NUMBER_TAG)) {
/* 121 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */     }
/* 123 */     int i = paramIdFunctionObject.methodId();
/* 124 */     if (i == 1) {
/* 125 */       d = paramArrayOfObject.length >= 1 ? ScriptRuntime.toNumber(paramArrayOfObject[0]) : 0.0D;
/*     */ 
/* 127 */       if (paramScriptable2 == null)
/*     */       {
/* 129 */         return new NativeNumber(d);
/*     */       }
/*     */ 
/* 132 */       return ScriptRuntime.wrapNumber(d);
/*     */     }
/*     */ 
/* 137 */     if (!(paramScriptable2 instanceof NativeNumber))
/* 138 */       throw incompatibleCallError(paramIdFunctionObject);
/* 139 */     double d = ((NativeNumber)paramScriptable2).doubleValue;
/*     */ 
/* 141 */     switch (i)
/*     */     {
/*     */     case 2:
/*     */     case 3:
/* 147 */       int j = paramArrayOfObject.length == 0 ? 10 : ScriptRuntime.toInt32(paramArrayOfObject[0]);
/*     */ 
/* 149 */       return ScriptRuntime.numberToString(d, j);
/*     */     case 4:
/* 153 */       return "(new Number(" + ScriptRuntime.toString(d) + "))";
/*     */     case 5:
/* 156 */       return ScriptRuntime.wrapNumber(d);
/*     */     case 6:
/* 159 */       return num_to(d, paramArrayOfObject, 2, 2, -20, 0);
/*     */     case 7:
/* 164 */       if (Double.isNaN(d)) {
/* 165 */         return "NaN";
/*     */       }
/* 167 */       if (Double.isInfinite(d)) {
/* 168 */         if (d >= 0.0D) {
/* 169 */           return "Infinity";
/*     */         }
/*     */ 
/* 172 */         return "-Infinity";
/*     */       }
/*     */ 
/* 176 */       return num_to(d, paramArrayOfObject, 1, 3, 0, 1);
/*     */     case 8:
/* 182 */       if ((paramArrayOfObject.length == 0) || (paramArrayOfObject[0] == Undefined.instance)) {
/* 183 */         return ScriptRuntime.numberToString(d, 10);
/*     */       }
/*     */ 
/* 186 */       if (Double.isNaN(d)) {
/* 187 */         return "NaN";
/*     */       }
/* 189 */       if (Double.isInfinite(d)) {
/* 190 */         if (d >= 0.0D) {
/* 191 */           return "Infinity";
/*     */         }
/*     */ 
/* 194 */         return "-Infinity";
/*     */       }
/*     */ 
/* 197 */       return num_to(d, paramArrayOfObject, 0, 4, 1, 0);
/*     */     }
/*     */ 
/* 201 */     throw new IllegalArgumentException(String.valueOf(i));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 207 */     return ScriptRuntime.numberToString(this.doubleValue, 10);
/*     */   }
/*     */ 
/*     */   private static String num_to(double paramDouble, Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */     int i;
/* 216 */     if (paramArrayOfObject.length == 0) {
/* 217 */       i = 0;
/* 218 */       paramInt2 = paramInt1;
/*     */     }
/*     */     else
/*     */     {
/* 222 */       i = ScriptRuntime.toInt32(paramArrayOfObject[0]);
/* 223 */       if ((i < paramInt3) || (i > 100)) {
/* 224 */         localObject = ScriptRuntime.getMessage1("msg.bad.precision", ScriptRuntime.toString(paramArrayOfObject[0]));
/*     */ 
/* 226 */         throw ScriptRuntime.constructError("RangeError", (String)localObject);
/*     */       }
/*     */     }
/* 229 */     Object localObject = new StringBuffer();
/* 230 */     DToA.JS_dtostr((StringBuffer)localObject, paramInt2, i + paramInt4, paramDouble);
/* 231 */     return ((StringBuffer)localObject).toString();
/*     */   }
/*     */ 
/*     */   protected int findPrototypeId(String paramString)
/*     */   {
/* 241 */     int i = 0; String str = null;
/*     */     int j;
/* 242 */     switch (paramString.length()) { case 7:
/* 243 */       j = paramString.charAt(0);
/* 244 */       if (j == 116) { str = "toFixed"; i = 6;
/* 245 */       } else if (j == 118) { str = "valueOf"; i = 5; } break;
/*     */     case 8:
/* 247 */       j = paramString.charAt(3);
/* 248 */       if (j == 111) { str = "toSource"; i = 4;
/* 249 */       } else if (j == 116) { str = "toString"; i = 2; } break;
/*     */     case 11:
/* 251 */       j = paramString.charAt(0);
/* 252 */       if (j == 99) { str = "constructor"; i = 1;
/* 253 */       } else if (j == 116) { str = "toPrecision"; i = 8; } break;
/*     */     case 13:
/* 255 */       str = "toExponential"; i = 7; break;
/*     */     case 14:
/* 256 */       str = "toLocaleString"; i = 3; break;
/*     */     case 9:
/*     */     case 10:
/* 258 */     case 12: } if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*     */ 
/* 262 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeNumber
 * JD-Core Version:    0.6.2
 */