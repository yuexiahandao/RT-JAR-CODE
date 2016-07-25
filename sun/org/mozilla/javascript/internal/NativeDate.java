/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.SimpleTimeZone;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ final class NativeDate extends IdScriptableObject
/*      */ {
/*   59 */   private static final Object DATE_TAG = "Date";
/*      */   private static final String js_NaN_date_str = "Invalid Date";
/*   65 */   private static final DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
/*      */   private static final double HalfTimeDomain = 8640000000000000.0D;
/*      */   private static final double HoursPerDay = 24.0D;
/*      */   private static final double MinutesPerHour = 60.0D;
/*      */   private static final double SecondsPerMinute = 60.0D;
/*      */   private static final double msPerSecond = 1000.0D;
/*      */   private static final double MinutesPerDay = 1440.0D;
/*      */   private static final double SecondsPerDay = 86400.0D;
/*      */   private static final double SecondsPerHour = 3600.0D;
/*      */   private static final double msPerDay = 86400000.0D;
/*      */   private static final double msPerHour = 3600000.0D;
/*      */   private static final double msPerMinute = 60000.0D;
/*      */   private static final boolean TZO_WORKAROUND = false;
/*      */   private static final int MAXARGS = 7;
/*      */   private static final int ConstructorId_now = -3;
/*      */   private static final int ConstructorId_parse = -2;
/*      */   private static final int ConstructorId_UTC = -1;
/*      */   private static final int Id_constructor = 1;
/*      */   private static final int Id_toString = 2;
/*      */   private static final int Id_toTimeString = 3;
/*      */   private static final int Id_toDateString = 4;
/*      */   private static final int Id_toLocaleString = 5;
/*      */   private static final int Id_toLocaleTimeString = 6;
/*      */   private static final int Id_toLocaleDateString = 7;
/*      */   private static final int Id_toUTCString = 8;
/*      */   private static final int Id_toSource = 9;
/*      */   private static final int Id_valueOf = 10;
/*      */   private static final int Id_getTime = 11;
/*      */   private static final int Id_getYear = 12;
/*      */   private static final int Id_getFullYear = 13;
/*      */   private static final int Id_getUTCFullYear = 14;
/*      */   private static final int Id_getMonth = 15;
/*      */   private static final int Id_getUTCMonth = 16;
/*      */   private static final int Id_getDate = 17;
/*      */   private static final int Id_getUTCDate = 18;
/*      */   private static final int Id_getDay = 19;
/*      */   private static final int Id_getUTCDay = 20;
/*      */   private static final int Id_getHours = 21;
/*      */   private static final int Id_getUTCHours = 22;
/*      */   private static final int Id_getMinutes = 23;
/*      */   private static final int Id_getUTCMinutes = 24;
/*      */   private static final int Id_getSeconds = 25;
/*      */   private static final int Id_getUTCSeconds = 26;
/*      */   private static final int Id_getMilliseconds = 27;
/*      */   private static final int Id_getUTCMilliseconds = 28;
/*      */   private static final int Id_getTimezoneOffset = 29;
/*      */   private static final int Id_setTime = 30;
/*      */   private static final int Id_setMilliseconds = 31;
/*      */   private static final int Id_setUTCMilliseconds = 32;
/*      */   private static final int Id_setSeconds = 33;
/*      */   private static final int Id_setUTCSeconds = 34;
/*      */   private static final int Id_setMinutes = 35;
/*      */   private static final int Id_setUTCMinutes = 36;
/*      */   private static final int Id_setHours = 37;
/*      */   private static final int Id_setUTCHours = 38;
/*      */   private static final int Id_setDate = 39;
/*      */   private static final int Id_setUTCDate = 40;
/*      */   private static final int Id_setMonth = 41;
/*      */   private static final int Id_setUTCMonth = 42;
/*      */   private static final int Id_setFullYear = 43;
/*      */   private static final int Id_setUTCFullYear = 44;
/*      */   private static final int Id_setYear = 45;
/*      */   private static final int Id_toISOString = 46;
/*      */   private static final int Id_toJSON = 47;
/*      */   private static final int MAX_PROTOTYPE_ID = 47;
/*      */   private static final int Id_toGMTString = 8;
/*      */   private static DateFormat timeZoneFormatter;
/*      */   private static DateFormat localeDateTimeFormatter;
/*      */   private static DateFormat localeDateFormatter;
/*      */   private static DateFormat localeTimeFormatter;
/*      */   private double date;
/*      */ 
/*      */   static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*      */   {
/*   72 */     NativeDate localNativeDate = new NativeDate(paramContext);
/*      */ 
/*   74 */     localNativeDate.date = ScriptRuntime.NaN;
/*   75 */     localNativeDate.exportAsJSClass(47, paramScriptable, paramBoolean);
/*      */   }
/*      */ 
/*      */   private NativeDate(Context paramContext)
/*      */   {
/*   82 */     paramContext.setTimeZone();
/*   83 */     paramContext.setLocalTZA();
/*      */   }
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*   89 */     return "Date";
/*      */   }
/*      */ 
/*      */   public Object getDefaultValue(Class<?> paramClass)
/*      */   {
/*   95 */     if (paramClass == null)
/*   96 */       paramClass = ScriptRuntime.StringClass;
/*   97 */     return super.getDefaultValue(paramClass);
/*      */   }
/*      */ 
/*      */   double getJSTimeValue()
/*      */   {
/*  102 */     return this.date;
/*      */   }
/*      */ 
/*      */   protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject)
/*      */   {
/*  108 */     addIdFunctionProperty(paramIdFunctionObject, DATE_TAG, -3, "now", 0);
/*      */ 
/*  110 */     addIdFunctionProperty(paramIdFunctionObject, DATE_TAG, -2, "parse", 1);
/*      */ 
/*  112 */     addIdFunctionProperty(paramIdFunctionObject, DATE_TAG, -1, "UTC", 1);
/*      */ 
/*  114 */     super.fillConstructorProperties(paramIdFunctionObject);
/*      */   }
/*      */ 
/*      */   protected void initPrototypeId(int paramInt)
/*      */   {
/*      */     int i;
/*      */     String str;
/*  122 */     switch (paramInt) { case 1:
/*  123 */       i = 1; str = "constructor"; break;
/*      */     case 2:
/*  124 */       i = 0; str = "toString"; break;
/*      */     case 3:
/*  125 */       i = 0; str = "toTimeString"; break;
/*      */     case 4:
/*  126 */       i = 0; str = "toDateString"; break;
/*      */     case 5:
/*  127 */       i = 0; str = "toLocaleString"; break;
/*      */     case 6:
/*  128 */       i = 0; str = "toLocaleTimeString"; break;
/*      */     case 7:
/*  129 */       i = 0; str = "toLocaleDateString"; break;
/*      */     case 8:
/*  130 */       i = 0; str = "toUTCString"; break;
/*      */     case 9:
/*  131 */       i = 0; str = "toSource"; break;
/*      */     case 10:
/*  132 */       i = 0; str = "valueOf"; break;
/*      */     case 11:
/*  133 */       i = 0; str = "getTime"; break;
/*      */     case 12:
/*  134 */       i = 0; str = "getYear"; break;
/*      */     case 13:
/*  135 */       i = 0; str = "getFullYear"; break;
/*      */     case 14:
/*  136 */       i = 0; str = "getUTCFullYear"; break;
/*      */     case 15:
/*  137 */       i = 0; str = "getMonth"; break;
/*      */     case 16:
/*  138 */       i = 0; str = "getUTCMonth"; break;
/*      */     case 17:
/*  139 */       i = 0; str = "getDate"; break;
/*      */     case 18:
/*  140 */       i = 0; str = "getUTCDate"; break;
/*      */     case 19:
/*  141 */       i = 0; str = "getDay"; break;
/*      */     case 20:
/*  142 */       i = 0; str = "getUTCDay"; break;
/*      */     case 21:
/*  143 */       i = 0; str = "getHours"; break;
/*      */     case 22:
/*  144 */       i = 0; str = "getUTCHours"; break;
/*      */     case 23:
/*  145 */       i = 0; str = "getMinutes"; break;
/*      */     case 24:
/*  146 */       i = 0; str = "getUTCMinutes"; break;
/*      */     case 25:
/*  147 */       i = 0; str = "getSeconds"; break;
/*      */     case 26:
/*  148 */       i = 0; str = "getUTCSeconds"; break;
/*      */     case 27:
/*  149 */       i = 0; str = "getMilliseconds"; break;
/*      */     case 28:
/*  150 */       i = 0; str = "getUTCMilliseconds"; break;
/*      */     case 29:
/*  151 */       i = 0; str = "getTimezoneOffset"; break;
/*      */     case 30:
/*  152 */       i = 1; str = "setTime"; break;
/*      */     case 31:
/*  153 */       i = 1; str = "setMilliseconds"; break;
/*      */     case 32:
/*  154 */       i = 1; str = "setUTCMilliseconds"; break;
/*      */     case 33:
/*  155 */       i = 2; str = "setSeconds"; break;
/*      */     case 34:
/*  156 */       i = 2; str = "setUTCSeconds"; break;
/*      */     case 35:
/*  157 */       i = 3; str = "setMinutes"; break;
/*      */     case 36:
/*  158 */       i = 3; str = "setUTCMinutes"; break;
/*      */     case 37:
/*  159 */       i = 4; str = "setHours"; break;
/*      */     case 38:
/*  160 */       i = 4; str = "setUTCHours"; break;
/*      */     case 39:
/*  161 */       i = 1; str = "setDate"; break;
/*      */     case 40:
/*  162 */       i = 1; str = "setUTCDate"; break;
/*      */     case 41:
/*  163 */       i = 2; str = "setMonth"; break;
/*      */     case 42:
/*  164 */       i = 2; str = "setUTCMonth"; break;
/*      */     case 43:
/*  165 */       i = 3; str = "setFullYear"; break;
/*      */     case 44:
/*  166 */       i = 3; str = "setUTCFullYear"; break;
/*      */     case 45:
/*  167 */       i = 1; str = "setYear"; break;
/*      */     case 46:
/*  168 */       i = 0; str = "toISOString"; break;
/*      */     case 47:
/*  169 */       i = 1; str = "toJSON"; break;
/*      */     default:
/*  170 */       throw new IllegalArgumentException(String.valueOf(paramInt));
/*      */     }
/*  172 */     initPrototypeMethod(DATE_TAG, paramInt, str, i);
/*      */   }
/*      */ 
/*      */   public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/*  179 */     if (!paramIdFunctionObject.hasTag(DATE_TAG)) {
/*  180 */       return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */     }
/*  182 */     int i = paramIdFunctionObject.methodId();
/*  183 */     switch (i) {
/*      */     case -3:
/*  185 */       return ScriptRuntime.wrapNumber(now());
/*      */     case -2:
/*  189 */       localObject1 = ScriptRuntime.toString(paramArrayOfObject, 0);
/*  190 */       return ScriptRuntime.wrapNumber(date_parseString(paramContext, (String)localObject1));
/*      */     case -1:
/*  194 */       return ScriptRuntime.wrapNumber(jsStaticFunction_UTC(paramArrayOfObject));
/*      */     case 1:
/*  200 */       if (paramScriptable2 != null)
/*  201 */         return date_format(paramContext, now(), 2);
/*  202 */       return jsConstructor(paramContext, paramArrayOfObject);
/*      */     case 47:
/*  207 */       if ((paramScriptable2 instanceof NativeDate)) {
/*  208 */         return ((NativeDate)paramScriptable2).toISOString();
/*      */       }
/*      */ 
/*  213 */       Scriptable localScriptable = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
/*  214 */       Object localObject2 = ScriptRuntime.toPrimitive(localScriptable, ScriptRuntime.NumberClass);
/*  215 */       if ((localObject2 instanceof Number)) {
/*  216 */         double d2 = ((Number)localObject2).doubleValue();
/*  217 */         if ((d2 != d2) || (Double.isInfinite(d2))) {
/*  218 */           return null;
/*      */         }
/*      */       }
/*  221 */       Object localObject3 = localScriptable.get("toISOString", localScriptable);
/*  222 */       if (localObject3 == NOT_FOUND) {
/*  223 */         throw ScriptRuntime.typeError2("msg.function.not.found.in", "toISOString", ScriptRuntime.toString(localScriptable));
/*      */       }
/*      */ 
/*  227 */       if (!(localObject3 instanceof Callable)) {
/*  228 */         throw ScriptRuntime.typeError3("msg.isnt.function.in", "toISOString", ScriptRuntime.toString(localScriptable), ScriptRuntime.toString(localObject3));
/*      */       }
/*      */ 
/*  233 */       Object localObject4 = ((Callable)localObject3).call(paramContext, paramScriptable1, localScriptable, ScriptRuntime.emptyArgs);
/*      */ 
/*  235 */       if (!ScriptRuntime.isPrimitive(localObject4)) {
/*  236 */         throw ScriptRuntime.typeError1("msg.toisostring.must.return.primitive", ScriptRuntime.toString(localObject4));
/*      */       }
/*      */ 
/*  239 */       return localObject4;
/*      */     }
/*      */ 
/*  246 */     if (!(paramScriptable2 instanceof NativeDate))
/*  247 */       throw incompatibleCallError(paramIdFunctionObject);
/*  248 */     Object localObject1 = (NativeDate)paramScriptable2;
/*  249 */     double d1 = ((NativeDate)localObject1).date;
/*      */ 
/*  251 */     switch (i)
/*      */     {
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*  256 */       if (d1 == d1) {
/*  257 */         return date_format(paramContext, d1, i);
/*      */       }
/*  259 */       return "Invalid Date";
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*  264 */       if (d1 == d1) {
/*  265 */         return toLocale_helper(d1, i);
/*      */       }
/*  267 */       return "Invalid Date";
/*      */     case 8:
/*  270 */       if (d1 == d1) {
/*  271 */         return js_toUTCString(d1);
/*      */       }
/*  273 */       return "Invalid Date";
/*      */     case 9:
/*  276 */       return "(new Date(" + ScriptRuntime.toString(d1) + "))";
/*      */     case 10:
/*      */     case 11:
/*  280 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*  285 */       if (d1 == d1) {
/*  286 */         if (i != 14) d1 = LocalTime(paramContext, d1);
/*  287 */         d1 = YearFromTime(d1);
/*  288 */         if (i == 12) {
/*  289 */           if (paramContext.hasFeature(1)) {
/*  290 */             if ((1900.0D <= d1) && (d1 < 2000.0D))
/*  291 */               d1 -= 1900.0D;
/*      */           }
/*      */           else {
/*  294 */             d1 -= 1900.0D;
/*      */           }
/*      */         }
/*      */       }
/*  298 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 15:
/*      */     case 16:
/*  302 */       if (d1 == d1) {
/*  303 */         if (i == 15) d1 = LocalTime(paramContext, d1);
/*  304 */         d1 = MonthFromTime(d1);
/*      */       }
/*  306 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 17:
/*      */     case 18:
/*  310 */       if (d1 == d1) {
/*  311 */         if (i == 17) d1 = LocalTime(paramContext, d1);
/*  312 */         d1 = DateFromTime(d1);
/*      */       }
/*  314 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 19:
/*      */     case 20:
/*  318 */       if (d1 == d1) {
/*  319 */         if (i == 19) d1 = LocalTime(paramContext, d1);
/*  320 */         d1 = WeekDay(d1);
/*      */       }
/*  322 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 21:
/*      */     case 22:
/*  326 */       if (d1 == d1) {
/*  327 */         if (i == 21) d1 = LocalTime(paramContext, d1);
/*  328 */         d1 = HourFromTime(d1);
/*      */       }
/*  330 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 23:
/*      */     case 24:
/*  334 */       if (d1 == d1) {
/*  335 */         if (i == 23) d1 = LocalTime(paramContext, d1);
/*  336 */         d1 = MinFromTime(d1);
/*      */       }
/*  338 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 25:
/*      */     case 26:
/*  342 */       if (d1 == d1) {
/*  343 */         if (i == 25) d1 = LocalTime(paramContext, d1);
/*  344 */         d1 = SecFromTime(d1);
/*      */       }
/*  346 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 27:
/*      */     case 28:
/*  350 */       if (d1 == d1) {
/*  351 */         if (i == 27) d1 = LocalTime(paramContext, d1);
/*  352 */         d1 = msFromTime(d1);
/*      */       }
/*  354 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 29:
/*  357 */       if (d1 == d1) {
/*  358 */         d1 = (d1 - LocalTime(paramContext, d1)) / 60000.0D;
/*      */       }
/*  360 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 30:
/*  363 */       d1 = TimeClip(ScriptRuntime.toNumber(paramArrayOfObject, 0));
/*  364 */       ((NativeDate)localObject1).date = d1;
/*  365 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 38:
/*  375 */       d1 = makeTime(paramContext, d1, paramArrayOfObject, i);
/*  376 */       ((NativeDate)localObject1).date = d1;
/*  377 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*  385 */       d1 = makeDate(paramContext, d1, paramArrayOfObject, i);
/*  386 */       ((NativeDate)localObject1).date = d1;
/*  387 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 45:
/*  391 */       double d3 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
/*      */ 
/*  393 */       if ((d3 != d3) || (Double.isInfinite(d3))) {
/*  394 */         d1 = ScriptRuntime.NaN;
/*      */       } else {
/*  396 */         if (d1 != d1)
/*  397 */           d1 = 0.0D;
/*      */         else {
/*  399 */           d1 = LocalTime(paramContext, d1);
/*      */         }
/*      */ 
/*  402 */         if ((d3 >= 0.0D) && (d3 <= 99.0D)) {
/*  403 */           d3 += 1900.0D;
/*      */         }
/*  405 */         double d4 = MakeDay(d3, MonthFromTime(d1), DateFromTime(d1));
/*      */ 
/*  407 */         d1 = MakeDate(d4, TimeWithinDay(d1));
/*  408 */         d1 = internalUTC(paramContext, d1);
/*  409 */         d1 = TimeClip(d1);
/*      */       }
/*      */ 
/*  412 */       ((NativeDate)localObject1).date = d1;
/*  413 */       return ScriptRuntime.wrapNumber(d1);
/*      */     case 46:
/*  416 */       return ((NativeDate)localObject1).toISOString();
/*      */     }
/*  418 */     throw new IllegalArgumentException(String.valueOf(i));
/*      */   }
/*      */ 
/*      */   private String toISOString()
/*      */   {
/*  424 */     if (this.date == this.date) {
/*  425 */       synchronized (isoFormat) {
/*  426 */         return isoFormat.format(new Date(()this.date));
/*      */       }
/*      */     }
/*  429 */     ??? = ScriptRuntime.getMessage0("msg.invalid.date");
/*  430 */     throw ScriptRuntime.constructError("RangeError", (String)???);
/*      */   }
/*      */ 
/*      */   private static double Day(double paramDouble)
/*      */   {
/*  449 */     return Math.floor(paramDouble / 86400000.0D);
/*      */   }
/*      */ 
/*      */   private static double TimeWithinDay(double paramDouble)
/*      */   {
/*  455 */     double d = paramDouble % 86400000.0D;
/*  456 */     if (d < 0.0D)
/*  457 */       d += 86400000.0D;
/*  458 */     return d;
/*      */   }
/*      */ 
/*      */   private static boolean IsLeapYear(int paramInt)
/*      */   {
/*  463 */     return (paramInt % 4 == 0) && ((paramInt % 100 != 0) || (paramInt % 400 == 0));
/*      */   }
/*      */ 
/*      */   private static double DayFromYear(double paramDouble)
/*      */   {
/*  471 */     return 365.0D * (paramDouble - 1970.0D) + Math.floor((paramDouble - 1969.0D) / 4.0D) - Math.floor((paramDouble - 1901.0D) / 100.0D) + Math.floor((paramDouble - 1601.0D) / 400.0D);
/*      */   }
/*      */ 
/*      */   private static double TimeFromYear(double paramDouble)
/*      */   {
/*  477 */     return DayFromYear(paramDouble) * 86400000.0D;
/*      */   }
/*      */ 
/*      */   private static int YearFromTime(double paramDouble)
/*      */   {
/*  482 */     int i = (int)Math.floor(paramDouble / 86400000.0D / 366.0D) + 1970;
/*  483 */     int j = (int)Math.floor(paramDouble / 86400000.0D / 365.0D) + 1970;
/*      */ 
/*  487 */     if (j < i) {
/*  488 */       int m = i;
/*  489 */       i = j;
/*  490 */       j = m;
/*      */     }
/*      */ 
/*  499 */     while (j > i) {
/*  500 */       int k = (j + i) / 2;
/*  501 */       if (TimeFromYear(k) > paramDouble) {
/*  502 */         j = k - 1;
/*      */       } else {
/*  504 */         i = k + 1;
/*  505 */         if (TimeFromYear(i) > paramDouble) {
/*  506 */           return k;
/*      */         }
/*      */       }
/*      */     }
/*  510 */     return i;
/*      */   }
/*      */ 
/*      */   private static double DayFromMonth(int paramInt1, int paramInt2)
/*      */   {
/*  515 */     int i = paramInt1 * 30;
/*      */ 
/*  517 */     if (paramInt1 >= 7) i += paramInt1 / 2 - 1;
/*  518 */     else if (paramInt1 >= 2) i += (paramInt1 - 1) / 2 - 1; else {
/*  519 */       i += paramInt1;
/*      */     }
/*  521 */     if ((paramInt1 >= 2) && (IsLeapYear(paramInt2))) i++;
/*      */ 
/*  523 */     return i;
/*      */   }
/*      */ 
/*      */   private static int MonthFromTime(double paramDouble)
/*      */   {
/*  528 */     int i = YearFromTime(paramDouble);
/*  529 */     int j = (int)(Day(paramDouble) - DayFromYear(i));
/*      */ 
/*  531 */     j -= 59;
/*  532 */     if (j < 0) {
/*  533 */       return j < -28 ? 0 : 1;
/*      */     }
/*      */ 
/*  536 */     if (IsLeapYear(i)) {
/*  537 */       if (j == 0)
/*  538 */         return 1;
/*  539 */       j--;
/*      */     }
/*      */ 
/*  543 */     int k = j / 30;
/*      */     int m;
/*  545 */     switch (k) { case 0:
/*  546 */       return 2;
/*      */     case 1:
/*  547 */       m = 31; break;
/*      */     case 2:
/*  548 */       m = 61; break;
/*      */     case 3:
/*  549 */       m = 92; break;
/*      */     case 4:
/*  550 */       m = 122; break;
/*      */     case 5:
/*  551 */       m = 153; break;
/*      */     case 6:
/*  552 */       m = 184; break;
/*      */     case 7:
/*  553 */       m = 214; break;
/*      */     case 8:
/*  554 */       m = 245; break;
/*      */     case 9:
/*  555 */       m = 275; break;
/*      */     case 10:
/*  556 */       return 11;
/*      */     default:
/*  557 */       throw Kit.codeBug();
/*      */     }
/*      */ 
/*  560 */     return j >= m ? k + 2 : k + 1;
/*      */   }
/*      */ 
/*      */   private static int DateFromTime(double paramDouble)
/*      */   {
/*  565 */     int i = YearFromTime(paramDouble);
/*  566 */     int j = (int)(Day(paramDouble) - DayFromYear(i));
/*      */ 
/*  568 */     j -= 59;
/*  569 */     if (j < 0) {
/*  570 */       return j < -28 ? j + 31 + 28 + 1 : j + 28 + 1;
/*      */     }
/*      */ 
/*  573 */     if (IsLeapYear(i)) {
/*  574 */       if (j == 0)
/*  575 */         return 29;
/*  576 */       j--;
/*      */     }
/*      */     int k;
/*      */     int m;
/*  581 */     switch (j / 30) { case 0:
/*  582 */       return j + 1;
/*      */     case 1:
/*  583 */       k = 31; m = 31; break;
/*      */     case 2:
/*  584 */       k = 30; m = 61; break;
/*      */     case 3:
/*  585 */       k = 31; m = 92; break;
/*      */     case 4:
/*  586 */       k = 30; m = 122; break;
/*      */     case 5:
/*  587 */       k = 31; m = 153; break;
/*      */     case 6:
/*  588 */       k = 31; m = 184; break;
/*      */     case 7:
/*  589 */       k = 30; m = 214; break;
/*      */     case 8:
/*  590 */       k = 31; m = 245; break;
/*      */     case 9:
/*  591 */       k = 30; m = 275; break;
/*      */     case 10:
/*  593 */       return j - 275 + 1;
/*      */     default:
/*  594 */       throw Kit.codeBug();
/*      */     }
/*  596 */     j -= m;
/*  597 */     if (j < 0)
/*      */     {
/*  599 */       j += k;
/*      */     }
/*  601 */     return j + 1;
/*      */   }
/*      */ 
/*      */   private static int WeekDay(double paramDouble)
/*      */   {
/*  607 */     double d = Day(paramDouble) + 4.0D;
/*  608 */     d %= 7.0D;
/*  609 */     if (d < 0.0D)
/*  610 */       d += 7.0D;
/*  611 */     return (int)d;
/*      */   }
/*      */ 
/*      */   private static double now()
/*      */   {
/*  616 */     return System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */   private static double DaylightSavingTA(Context paramContext, double paramDouble)
/*      */   {
/*  633 */     if ((paramDouble < 0.0D) || (paramDouble > 2145916800000.0D)) {
/*  634 */       int i = EquivalentYear(YearFromTime(paramDouble));
/*  635 */       double d = MakeDay(i, MonthFromTime(paramDouble), DateFromTime(paramDouble));
/*  636 */       paramDouble = MakeDate(d, TimeWithinDay(paramDouble));
/*      */     }
/*      */ 
/*  639 */     Date localDate = new Date(()paramDouble);
/*  640 */     if (paramContext.getTimeZone().inDaylightTime(localDate)) {
/*  641 */       return 3600000.0D;
/*      */     }
/*  643 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   private static int EquivalentYear(int paramInt)
/*      */   {
/*  681 */     int i = (int)DayFromYear(paramInt) + 4;
/*  682 */     i %= 7;
/*  683 */     if (i < 0) {
/*  684 */       i += 7;
/*      */     }
/*  686 */     if (IsLeapYear(paramInt))
/*  687 */       switch (i) { case 0:
/*  688 */         return 1984;
/*      */       case 1:
/*  689 */         return 1996;
/*      */       case 2:
/*  690 */         return 1980;
/*      */       case 3:
/*  691 */         return 1992;
/*      */       case 4:
/*  692 */         return 1976;
/*      */       case 5:
/*  693 */         return 1988;
/*      */       case 6:
/*  694 */         return 1972;
/*      */       }
/*      */     else {
/*  697 */       switch (i) { case 0:
/*  698 */         return 1978;
/*      */       case 1:
/*  699 */         return 1973;
/*      */       case 2:
/*  700 */         return 1974;
/*      */       case 3:
/*  701 */         return 1975;
/*      */       case 4:
/*  702 */         return 1981;
/*      */       case 5:
/*  703 */         return 1971;
/*      */       case 6:
/*  704 */         return 1977;
/*      */       }
/*      */     }
/*      */ 
/*  708 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   private static double LocalTime(Context paramContext, double paramDouble)
/*      */   {
/*  713 */     return paramDouble + paramContext.getLocalTZA() + DaylightSavingTA(paramContext, paramDouble);
/*      */   }
/*      */ 
/*      */   private static double internalUTC(Context paramContext, double paramDouble)
/*      */   {
/*  718 */     return paramDouble - paramContext.getLocalTZA() - DaylightSavingTA(paramContext, paramDouble - paramContext.getLocalTZA());
/*      */   }
/*      */ 
/*      */   private static int HourFromTime(double paramDouble)
/*      */   {
/*  724 */     double d = Math.floor(paramDouble / 3600000.0D) % 24.0D;
/*  725 */     if (d < 0.0D)
/*  726 */       d += 24.0D;
/*  727 */     return (int)d;
/*      */   }
/*      */ 
/*      */   private static int MinFromTime(double paramDouble)
/*      */   {
/*  733 */     double d = Math.floor(paramDouble / 60000.0D) % 60.0D;
/*  734 */     if (d < 0.0D)
/*  735 */       d += 60.0D;
/*  736 */     return (int)d;
/*      */   }
/*      */ 
/*      */   private static int SecFromTime(double paramDouble)
/*      */   {
/*  742 */     double d = Math.floor(paramDouble / 1000.0D) % 60.0D;
/*  743 */     if (d < 0.0D)
/*  744 */       d += 60.0D;
/*  745 */     return (int)d;
/*      */   }
/*      */ 
/*      */   private static int msFromTime(double paramDouble)
/*      */   {
/*  751 */     double d = paramDouble % 1000.0D;
/*  752 */     if (d < 0.0D)
/*  753 */       d += 1000.0D;
/*  754 */     return (int)d;
/*      */   }
/*      */ 
/*      */   private static double MakeTime(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*  760 */     return ((paramDouble1 * 60.0D + paramDouble2) * 60.0D + paramDouble3) * 1000.0D + paramDouble4;
/*      */   }
/*      */ 
/*      */   private static double MakeDay(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/*  766 */     paramDouble1 += Math.floor(paramDouble2 / 12.0D);
/*      */ 
/*  768 */     paramDouble2 %= 12.0D;
/*  769 */     if (paramDouble2 < 0.0D) {
/*  770 */       paramDouble2 += 12.0D;
/*      */     }
/*  772 */     double d1 = Math.floor(TimeFromYear(paramDouble1) / 86400000.0D);
/*  773 */     double d2 = DayFromMonth((int)paramDouble2, (int)paramDouble1);
/*      */ 
/*  775 */     return d1 + d2 + paramDouble3 - 1.0D;
/*      */   }
/*      */ 
/*      */   private static double MakeDate(double paramDouble1, double paramDouble2)
/*      */   {
/*  780 */     return paramDouble1 * 86400000.0D + paramDouble2;
/*      */   }
/*      */ 
/*      */   private static double TimeClip(double paramDouble)
/*      */   {
/*  785 */     if ((paramDouble != paramDouble) || (paramDouble == (1.0D / 0.0D)) || (paramDouble == (-1.0D / 0.0D)) || (Math.abs(paramDouble) > 8640000000000000.0D))
/*      */     {
/*  790 */       return ScriptRuntime.NaN;
/*      */     }
/*  792 */     if (paramDouble > 0.0D) {
/*  793 */       return Math.floor(paramDouble + 0.0D);
/*      */     }
/*  795 */     return Math.ceil(paramDouble + 0.0D);
/*      */   }
/*      */ 
/*      */   private static double date_msecFromDate(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7)
/*      */   {
/*  810 */     double d1 = MakeDay(paramDouble1, paramDouble2, paramDouble3);
/*  811 */     double d2 = MakeTime(paramDouble4, paramDouble5, paramDouble6, paramDouble7);
/*  812 */     double d3 = MakeDate(d1, d2);
/*  813 */     return d3;
/*      */   }
/*      */ 
/*      */   private static double date_msecFromArgs(Object[] paramArrayOfObject)
/*      */   {
/*  820 */     double[] arrayOfDouble = new double[7];
/*      */ 
/*  824 */     for (int i = 0; i < 7; i++) {
/*  825 */       if (i < paramArrayOfObject.length) {
/*  826 */         double d = ScriptRuntime.toNumber(paramArrayOfObject[i]);
/*  827 */         if ((d != d) || (Double.isInfinite(d))) {
/*  828 */           return ScriptRuntime.NaN;
/*      */         }
/*  830 */         arrayOfDouble[i] = ScriptRuntime.toInteger(paramArrayOfObject[i]);
/*      */       }
/*  832 */       else if (i == 2) {
/*  833 */         arrayOfDouble[i] = 1.0D;
/*      */       } else {
/*  835 */         arrayOfDouble[i] = 0.0D;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  841 */     if ((arrayOfDouble[0] >= 0.0D) && (arrayOfDouble[0] <= 99.0D)) {
/*  842 */       arrayOfDouble[0] += 1900.0D;
/*      */     }
/*  844 */     return date_msecFromDate(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2], arrayOfDouble[3], arrayOfDouble[4], arrayOfDouble[5], arrayOfDouble[6]);
/*      */   }
/*      */ 
/*      */   private static double jsStaticFunction_UTC(Object[] paramArrayOfObject)
/*      */   {
/*  850 */     return TimeClip(date_msecFromArgs(paramArrayOfObject));
/*      */   }
/*      */ 
/*      */   private static double date_parseString(Context paramContext, String paramString)
/*      */   {
/*      */     try {
/*  856 */       if (paramString.length() == 24)
/*      */       {
/*      */         Date localDate;
/*  858 */         synchronized (isoFormat) {
/*  859 */           localDate = isoFormat.parse(paramString);
/*      */         }
/*  861 */         return localDate.getTime();
/*      */       }
/*      */     } catch (ParseException localParseException) {
/*      */     }
/*  865 */     int i = -1;
/*  866 */     int j = -1;
/*  867 */     int k = -1;
/*  868 */     int m = -1;
/*  869 */     int n = -1;
/*  870 */     int i1 = -1;
/*  871 */     int i2 = 0;
/*  872 */     int i3 = 0;
/*  873 */     int i4 = 0;
/*  874 */     int i5 = -1;
/*  875 */     double d1 = -1.0D;
/*  876 */     int i6 = 0;
/*  877 */     int i7 = 0;
/*  878 */     int i8 = 0;
/*      */ 
/*  880 */     i7 = paramString.length();
/*      */     while (true) { if (i4 >= i7) break label1093;
/*  882 */       i2 = paramString.charAt(i4);
/*  883 */       i4++;
/*  884 */       if ((i2 <= 32) || (i2 == 44) || (i2 == 45)) {
/*  885 */         if (i4 < i7) {
/*  886 */           i3 = paramString.charAt(i4);
/*  887 */           if ((i2 == 45) && (48 <= i3) && (i3 <= 57))
/*  888 */             i6 = i2;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i9;
/*  893 */         if (i2 == 40) {
/*  894 */           i9 = 1;
/*  895 */           while (i4 < i7) {
/*  896 */             i2 = paramString.charAt(i4);
/*  897 */             i4++;
/*  898 */             if (i2 != 40) break label216;
/*  899 */             i9++;
/*      */           }
/*      */           continue;
/*  900 */           label216: if (i2 != 41) break;
/*  901 */           i9--; if (i9 > 0) break;
/*  902 */           continue;
/*      */         }
/*      */ 
/*  906 */         if ((48 <= i2) && (i2 <= 57)) {
/*  907 */           i5 = i2 - 48;
/*  908 */           while ((i4 < i7) && ('0' <= (i2 = paramString.charAt(i4))) && (i2 <= 57)) {
/*  909 */             i5 = i5 * 10 + i2 - 48;
/*  910 */             i4++;
/*      */           }
/*      */ 
/*  920 */           if ((i6 == 43) || (i6 == 45))
/*      */           {
/*  922 */             i8 = 1;
/*      */ 
/*  925 */             if (i5 < 24)
/*  926 */               i5 *= 60;
/*      */             else
/*  928 */               i5 = i5 % 100 + i5 / 100 * 60;
/*  929 */             if (i6 == 43)
/*  930 */               i5 = -i5;
/*  931 */             if ((d1 != 0.0D) && (d1 != -1.0D))
/*  932 */               return ScriptRuntime.NaN;
/*  933 */             d1 = i5;
/*  934 */           } else if ((i5 >= 70) || ((i6 == 47) && (j >= 0) && (k >= 0) && (i < 0)))
/*      */           {
/*  938 */             if (i >= 0)
/*  939 */               return ScriptRuntime.NaN;
/*  940 */             if ((i2 <= 32) || (i2 == 44) || (i2 == 47) || (i4 >= i7))
/*  941 */               i = i5 < 100 ? i5 + 1900 : i5;
/*      */             else
/*  943 */               return ScriptRuntime.NaN;
/*  944 */           } else if (i2 == 58) {
/*  945 */             if (m < 0)
/*  946 */               m = i5;
/*  947 */             else if (n < 0)
/*  948 */               n = i5;
/*      */             else
/*  950 */               return ScriptRuntime.NaN;
/*  951 */           } else if (i2 == 47) {
/*  952 */             if (j < 0)
/*  953 */               j = i5 - 1;
/*  954 */             else if (k < 0)
/*  955 */               k = i5;
/*      */             else
/*  957 */               return ScriptRuntime.NaN; 
/*      */           } else { if ((i4 < i7) && (i2 != 44) && (i2 > 32) && (i2 != 45))
/*  959 */               return ScriptRuntime.NaN;
/*  960 */             if ((i8 != 0) && (i5 < 60)) {
/*  961 */               if (d1 < 0.0D)
/*  962 */                 d1 -= i5;
/*      */               else
/*  964 */                 d1 += i5;
/*  965 */             } else if ((m >= 0) && (n < 0))
/*  966 */               n = i5;
/*  967 */             else if ((n >= 0) && (i1 < 0))
/*  968 */               i1 = i5;
/*  969 */             else if (k < 0)
/*  970 */               k = i5;
/*      */             else
/*  972 */               return ScriptRuntime.NaN;
/*      */           }
/*  974 */           i6 = 0;
/*  975 */         } else if ((i2 == 47) || (i2 == 58) || (i2 == 43) || (i2 == 45)) {
/*  976 */           i6 = i2;
/*      */         } else {
/*  978 */           i9 = i4 - 1;
/*  979 */           while (i4 < i7) {
/*  980 */             i2 = paramString.charAt(i4);
/*  981 */             if (((65 > i2) || (i2 > 90)) && ((97 > i2) || (i2 > 122)))
/*      */               break;
/*  983 */             i4++;
/*      */           }
/*  985 */           int i10 = i4 - i9;
/*  986 */           if (i10 < 2) {
/*  987 */             return ScriptRuntime.NaN;
/*      */           }
/*      */ 
/*  993 */           String str = "am;pm;monday;tuesday;wednesday;thursday;friday;saturday;sunday;january;february;march;april;may;june;july;august;september;october;november;december;gmt;ut;utc;est;edt;cst;cdt;mst;mdt;pst;pdt;";
/*      */ 
/*  999 */           int i11 = 0;
/* 1000 */           int i12 = 0;
/*      */           while (true) { int i13 = str.indexOf(';', i12);
/* 1002 */             if (i13 < 0)
/* 1003 */               return ScriptRuntime.NaN;
/* 1004 */             if (str.regionMatches(true, i12, paramString, i9, i10))
/*      */               break;
/* 1006 */             i12 = i13 + 1;
/* 1007 */             i11++;
/*      */           }
/* 1009 */           if (i11 < 2)
/*      */           {
/* 1014 */             if ((m > 12) || (m < 0))
/* 1015 */               return ScriptRuntime.NaN;
/* 1016 */             if (i11 == 0)
/*      */             {
/* 1018 */               if (m == 12) {
/* 1019 */                 m = 0;
/*      */               }
/*      */             }
/* 1022 */             else if (m != 12)
/* 1023 */               m += 12;
/*      */           } else {
/* 1025 */             i11 -= 2; if (i11 >= 7)
/*      */             {
/* 1027 */               i11 -= 7; if (i11 < 12)
/*      */               {
/* 1029 */                 if (j < 0)
/* 1030 */                   j = i11;
/*      */                 else
/* 1032 */                   return ScriptRuntime.NaN;
/*      */               }
/*      */               else {
/* 1035 */                 i11 -= 12;
/*      */ 
/* 1037 */                 switch (i11) { case 0:
/* 1038 */                   d1 = 0.0D; break;
/*      */                 case 1:
/* 1039 */                   d1 = 0.0D; break;
/*      */                 case 2:
/* 1040 */                   d1 = 0.0D; break;
/*      */                 case 3:
/* 1041 */                   d1 = 300.0D; break;
/*      */                 case 4:
/* 1042 */                   d1 = 240.0D; break;
/*      */                 case 5:
/* 1043 */                   d1 = 360.0D; break;
/*      */                 case 6:
/* 1044 */                   d1 = 300.0D; break;
/*      */                 case 7:
/* 1045 */                   d1 = 420.0D; break;
/*      */                 case 8:
/* 1046 */                   d1 = 360.0D; break;
/*      */                 case 9:
/* 1047 */                   d1 = 480.0D; break;
/*      */                 case 10:
/* 1048 */                   d1 = 420.0D; break;
/*      */                 default:
/* 1049 */                   Kit.codeBug(); } 
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } } label1093: if ((i < 0) || (j < 0) || (k < 0))
/* 1055 */       return ScriptRuntime.NaN;
/* 1056 */     if (i1 < 0)
/* 1057 */       i1 = 0;
/* 1058 */     if (n < 0)
/* 1059 */       n = 0;
/* 1060 */     if (m < 0) {
/* 1061 */       m = 0;
/*      */     }
/* 1063 */     double d2 = date_msecFromDate(i, j, k, m, n, i1, 0.0D);
/* 1064 */     if (d1 == -1.0D) {
/* 1065 */       return internalUTC(paramContext, d2);
/*      */     }
/* 1067 */     return d2 + d1 * 60000.0D;
/*      */   }
/*      */ 
/*      */   private static String date_format(Context paramContext, double paramDouble, int paramInt)
/*      */   {
/* 1073 */     StringBuffer localStringBuffer = new StringBuffer(60);
/* 1074 */     double d1 = LocalTime(paramContext, paramDouble);
/*      */     int i;
/* 1080 */     if (paramInt != 3) {
/* 1081 */       appendWeekDayName(localStringBuffer, WeekDay(d1));
/* 1082 */       localStringBuffer.append(' ');
/* 1083 */       appendMonthName(localStringBuffer, MonthFromTime(d1));
/* 1084 */       localStringBuffer.append(' ');
/* 1085 */       append0PaddedUint(localStringBuffer, DateFromTime(d1), 2);
/* 1086 */       localStringBuffer.append(' ');
/* 1087 */       i = YearFromTime(d1);
/* 1088 */       if (i < 0) {
/* 1089 */         localStringBuffer.append('-');
/* 1090 */         i = -i;
/*      */       }
/* 1092 */       append0PaddedUint(localStringBuffer, i, 4);
/* 1093 */       if (paramInt != 4) {
/* 1094 */         localStringBuffer.append(' ');
/*      */       }
/*      */     }
/* 1097 */     if (paramInt != 4) {
/* 1098 */       append0PaddedUint(localStringBuffer, HourFromTime(d1), 2);
/* 1099 */       localStringBuffer.append(':');
/* 1100 */       append0PaddedUint(localStringBuffer, MinFromTime(d1), 2);
/* 1101 */       localStringBuffer.append(':');
/* 1102 */       append0PaddedUint(localStringBuffer, SecFromTime(d1), 2);
/*      */ 
/* 1106 */       i = (int)Math.floor((paramContext.getLocalTZA() + DaylightSavingTA(paramContext, paramDouble)) / 60000.0D);
/*      */ 
/* 1109 */       int j = i / 60 * 100 + i % 60;
/* 1110 */       if (j > 0) {
/* 1111 */         localStringBuffer.append(" GMT+");
/*      */       } else {
/* 1113 */         localStringBuffer.append(" GMT-");
/* 1114 */         j = -j;
/*      */       }
/* 1116 */       append0PaddedUint(localStringBuffer, j, 4);
/*      */ 
/* 1118 */       if (timeZoneFormatter == null) {
/* 1119 */         timeZoneFormatter = new SimpleDateFormat("zzz");
/*      */       }
/*      */ 
/* 1123 */       if ((paramDouble < 0.0D) || (paramDouble > 2145916800000.0D)) {
/* 1124 */         int k = EquivalentYear(YearFromTime(d1));
/* 1125 */         double d2 = MakeDay(k, MonthFromTime(paramDouble), DateFromTime(paramDouble));
/* 1126 */         paramDouble = MakeDate(d2, TimeWithinDay(paramDouble));
/*      */       }
/* 1128 */       localStringBuffer.append(" (");
/* 1129 */       Date localDate = new Date(()paramDouble);
/* 1130 */       synchronized (timeZoneFormatter) {
/* 1131 */         localStringBuffer.append(timeZoneFormatter.format(localDate));
/*      */       }
/* 1133 */       localStringBuffer.append(')');
/*      */     }
/* 1135 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static Object jsConstructor(Context paramContext, Object[] paramArrayOfObject)
/*      */   {
/* 1141 */     NativeDate localNativeDate = new NativeDate(paramContext);
/*      */ 
/* 1145 */     if (paramArrayOfObject.length == 0) {
/* 1146 */       localNativeDate.date = now();
/* 1147 */       return localNativeDate;
/*      */     }
/*      */ 
/* 1151 */     if (paramArrayOfObject.length == 1) {
/* 1152 */       Object localObject = paramArrayOfObject[0];
/* 1153 */       if ((localObject instanceof Scriptable))
/* 1154 */         localObject = ((Scriptable)localObject).getDefaultValue(null);
/*      */       double d2;
/* 1156 */       if ((localObject instanceof String))
/*      */       {
/* 1158 */         d2 = date_parseString(paramContext, (String)localObject);
/*      */       }
/*      */       else {
/* 1161 */         d2 = ScriptRuntime.toNumber(localObject);
/*      */       }
/* 1163 */       localNativeDate.date = TimeClip(d2);
/* 1164 */       return localNativeDate;
/*      */     }
/*      */ 
/* 1167 */     double d1 = date_msecFromArgs(paramArrayOfObject);
/*      */ 
/* 1169 */     if ((!Double.isNaN(d1)) && (!Double.isInfinite(d1))) {
/* 1170 */       d1 = TimeClip(internalUTC(paramContext, d1));
/*      */     }
/* 1172 */     localNativeDate.date = d1;
/*      */ 
/* 1174 */     return localNativeDate;
/*      */   }
/*      */ 
/*      */   private static String toLocale_helper(double paramDouble, int paramInt)
/*      */   {
/*      */     DateFormat localDateFormat;
/* 1180 */     switch (paramInt) {
/*      */     case 5:
/* 1182 */       if (localeDateTimeFormatter == null) {
/* 1183 */         localeDateTimeFormatter = DateFormat.getDateTimeInstance(1, 1);
/*      */       }
/*      */ 
/* 1187 */       localDateFormat = localeDateTimeFormatter;
/* 1188 */       break;
/*      */     case 6:
/* 1190 */       if (localeTimeFormatter == null) {
/* 1191 */         localeTimeFormatter = DateFormat.getTimeInstance(1);
/*      */       }
/*      */ 
/* 1194 */       localDateFormat = localeTimeFormatter;
/* 1195 */       break;
/*      */     case 7:
/* 1197 */       if (localeDateFormatter == null) {
/* 1198 */         localeDateFormatter = DateFormat.getDateInstance(1);
/*      */       }
/*      */ 
/* 1201 */       localDateFormat = localeDateFormatter;
/* 1202 */       break;
/*      */     default:
/* 1203 */       throw new AssertionError();
/*      */     }
/*      */ 
/* 1206 */     synchronized (localDateFormat) {
/* 1207 */       return localDateFormat.format(new Date(()paramDouble));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String js_toUTCString(double paramDouble)
/*      */   {
/* 1213 */     StringBuffer localStringBuffer = new StringBuffer(60);
/*      */ 
/* 1215 */     appendWeekDayName(localStringBuffer, WeekDay(paramDouble));
/* 1216 */     localStringBuffer.append(", ");
/* 1217 */     append0PaddedUint(localStringBuffer, DateFromTime(paramDouble), 2);
/* 1218 */     localStringBuffer.append(' ');
/* 1219 */     appendMonthName(localStringBuffer, MonthFromTime(paramDouble));
/* 1220 */     localStringBuffer.append(' ');
/* 1221 */     int i = YearFromTime(paramDouble);
/* 1222 */     if (i < 0) {
/* 1223 */       localStringBuffer.append('-'); i = -i;
/*      */     }
/* 1225 */     append0PaddedUint(localStringBuffer, i, 4);
/* 1226 */     localStringBuffer.append(' ');
/* 1227 */     append0PaddedUint(localStringBuffer, HourFromTime(paramDouble), 2);
/* 1228 */     localStringBuffer.append(':');
/* 1229 */     append0PaddedUint(localStringBuffer, MinFromTime(paramDouble), 2);
/* 1230 */     localStringBuffer.append(':');
/* 1231 */     append0PaddedUint(localStringBuffer, SecFromTime(paramDouble), 2);
/* 1232 */     localStringBuffer.append(" GMT");
/* 1233 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static void append0PaddedUint(StringBuffer paramStringBuffer, int paramInt1, int paramInt2)
/*      */   {
/* 1238 */     if (paramInt1 < 0) Kit.codeBug();
/* 1239 */     int i = 1;
/* 1240 */     paramInt2--;
/* 1241 */     if (paramInt1 >= 10) {
/* 1242 */       if (paramInt1 < 1000000000) {
/*      */         while (true) {
/* 1244 */           int j = i * 10;
/* 1245 */           if (paramInt1 < j) break;
/* 1246 */           paramInt2--;
/* 1247 */           i = j;
/*      */         }
/*      */       }
/*      */ 
/* 1251 */       paramInt2 -= 9;
/* 1252 */       i = 1000000000;
/*      */     }
/*      */ 
/* 1255 */     while (paramInt2 > 0) {
/* 1256 */       paramStringBuffer.append('0');
/* 1257 */       paramInt2--;
/*      */     }
/* 1259 */     while (i != 1) {
/* 1260 */       paramStringBuffer.append((char)(48 + paramInt1 / i));
/* 1261 */       paramInt1 %= i;
/* 1262 */       i /= 10;
/*      */     }
/* 1264 */     paramStringBuffer.append((char)(48 + paramInt1));
/*      */   }
/*      */ 
/*      */   private static void appendMonthName(StringBuffer paramStringBuffer, int paramInt)
/*      */   {
/* 1272 */     String str = "JanFebMarAprMayJunJulAugSepOctNovDec";
/*      */ 
/* 1274 */     paramInt *= 3;
/* 1275 */     for (int i = 0; i != 3; i++)
/* 1276 */       paramStringBuffer.append(str.charAt(paramInt + i));
/*      */   }
/*      */ 
/*      */   private static void appendWeekDayName(StringBuffer paramStringBuffer, int paramInt)
/*      */   {
/* 1282 */     String str = "SunMonTueWedThuFriSat";
/* 1283 */     paramInt *= 3;
/* 1284 */     for (int i = 0; i != 3; i++)
/* 1285 */       paramStringBuffer.append(str.charAt(paramInt + i));
/*      */   }
/*      */ 
/*      */   private static double makeTime(Context paramContext, double paramDouble, Object[] paramArrayOfObject, int paramInt)
/*      */   {
/* 1292 */     int j = 1;
/*      */     int i;
/* 1293 */     switch (paramInt) {
/*      */     case 32:
/* 1295 */       j = 0;
/*      */     case 31:
/* 1298 */       i = 1;
/* 1299 */       break;
/*      */     case 34:
/* 1302 */       j = 0;
/*      */     case 33:
/* 1305 */       i = 2;
/* 1306 */       break;
/*      */     case 36:
/* 1309 */       j = 0;
/*      */     case 35:
/* 1312 */       i = 3;
/* 1313 */       break;
/*      */     case 38:
/* 1316 */       j = 0;
/*      */     case 37:
/* 1319 */       i = 4;
/* 1320 */       break;
/*      */     default:
/* 1323 */       Kit.codeBug();
/* 1324 */       i = 0;
/*      */     }
/*      */ 
/* 1328 */     double[] arrayOfDouble = new double[4];
/*      */ 
/* 1336 */     if (paramDouble != paramDouble) {
/* 1337 */       return paramDouble;
/*      */     }
/*      */ 
/* 1347 */     if (paramArrayOfObject.length == 0) {
/* 1348 */       paramArrayOfObject = ScriptRuntime.padArguments(paramArrayOfObject, 1);
/*      */     }
/* 1350 */     for (int k = 0; (k < paramArrayOfObject.length) && (k < i); k++) {
/* 1351 */       arrayOfDouble[k] = ScriptRuntime.toNumber(paramArrayOfObject[k]);
/*      */ 
/* 1354 */       if ((arrayOfDouble[k] != arrayOfDouble[k]) || (Double.isInfinite(arrayOfDouble[k]))) {
/* 1355 */         return ScriptRuntime.NaN;
/*      */       }
/* 1357 */       arrayOfDouble[k] = ScriptRuntime.toInteger(arrayOfDouble[k]);
/*      */     }
/*      */     double d5;
/* 1360 */     if (j != 0)
/* 1361 */       d5 = LocalTime(paramContext, paramDouble);
/*      */     else {
/* 1363 */       d5 = paramDouble;
/*      */     }
/* 1365 */     k = 0;
/* 1366 */     int m = paramArrayOfObject.length;
/*      */     double d1;
/* 1368 */     if ((i >= 4) && (k < m))
/* 1369 */       d1 = arrayOfDouble[(k++)];
/*      */     else
/* 1371 */       d1 = HourFromTime(d5);
/*      */     double d2;
/* 1373 */     if ((i >= 3) && (k < m))
/* 1374 */       d2 = arrayOfDouble[(k++)];
/*      */     else
/* 1376 */       d2 = MinFromTime(d5);
/*      */     double d3;
/* 1378 */     if ((i >= 2) && (k < m))
/* 1379 */       d3 = arrayOfDouble[(k++)];
/*      */     else
/* 1381 */       d3 = SecFromTime(d5);
/*      */     double d4;
/* 1383 */     if ((i >= 1) && (k < m))
/* 1384 */       d4 = arrayOfDouble[(k++)];
/*      */     else {
/* 1386 */       d4 = msFromTime(d5);
/*      */     }
/* 1388 */     double d6 = MakeTime(d1, d2, d3, d4);
/* 1389 */     double d7 = MakeDate(Day(d5), d6);
/*      */ 
/* 1391 */     if (j != 0)
/* 1392 */       d7 = internalUTC(paramContext, d7);
/* 1393 */     paramDouble = TimeClip(d7);
/*      */ 
/* 1395 */     return paramDouble;
/*      */   }
/*      */ 
/*      */   private static double makeDate(Context paramContext, double paramDouble, Object[] paramArrayOfObject, int paramInt)
/*      */   {
/* 1401 */     int j = 1;
/*      */     int i;
/* 1402 */     switch (paramInt) {
/*      */     case 40:
/* 1404 */       j = 0;
/*      */     case 39:
/* 1407 */       i = 1;
/* 1408 */       break;
/*      */     case 42:
/* 1411 */       j = 0;
/*      */     case 41:
/* 1414 */       i = 2;
/* 1415 */       break;
/*      */     case 44:
/* 1418 */       j = 0;
/*      */     case 43:
/* 1421 */       i = 3;
/* 1422 */       break;
/*      */     default:
/* 1425 */       Kit.codeBug();
/* 1426 */       i = 0;
/*      */     }
/*      */ 
/* 1430 */     double[] arrayOfDouble = new double[3];
/*      */ 
/* 1436 */     if (paramArrayOfObject.length == 0) {
/* 1437 */       paramArrayOfObject = ScriptRuntime.padArguments(paramArrayOfObject, 1);
/*      */     }
/* 1439 */     for (int k = 0; (k < paramArrayOfObject.length) && (k < i); k++) {
/* 1440 */       arrayOfDouble[k] = ScriptRuntime.toNumber(paramArrayOfObject[k]);
/*      */ 
/* 1443 */       if ((arrayOfDouble[k] != arrayOfDouble[k]) || (Double.isInfinite(arrayOfDouble[k]))) {
/* 1444 */         return ScriptRuntime.NaN;
/*      */       }
/* 1446 */       arrayOfDouble[k] = ScriptRuntime.toInteger(arrayOfDouble[k]);
/*      */     }
/*      */     double d4;
/* 1451 */     if (paramDouble != paramDouble) {
/* 1452 */       if (paramArrayOfObject.length < 3) {
/* 1453 */         return ScriptRuntime.NaN;
/*      */       }
/* 1455 */       d4 = 0.0D;
/*      */     }
/* 1458 */     else if (j != 0) {
/* 1459 */       d4 = LocalTime(paramContext, paramDouble);
/*      */     } else {
/* 1461 */       d4 = paramDouble;
/*      */     }
/*      */ 
/* 1464 */     k = 0;
/* 1465 */     int m = paramArrayOfObject.length;
/*      */     double d1;
/* 1467 */     if ((i >= 3) && (k < m))
/* 1468 */       d1 = arrayOfDouble[(k++)];
/*      */     else
/* 1470 */       d1 = YearFromTime(d4);
/*      */     double d2;
/* 1472 */     if ((i >= 2) && (k < m))
/* 1473 */       d2 = arrayOfDouble[(k++)];
/*      */     else {
/* 1475 */       d2 = MonthFromTime(d4);
/*      */     }
/* 1477 */     if ((i >= 1) && (k < m))
/* 1478 */       d3 = arrayOfDouble[(k++)];
/*      */     else {
/* 1480 */       d3 = DateFromTime(d4);
/*      */     }
/* 1482 */     double d3 = MakeDay(d1, d2, d3);
/* 1483 */     double d5 = MakeDate(d3, TimeWithinDay(d4));
/*      */ 
/* 1485 */     if (j != 0) {
/* 1486 */       d5 = internalUTC(paramContext, d5);
/*      */     }
/* 1488 */     paramDouble = TimeClip(d5);
/*      */ 
/* 1490 */     return paramDouble;
/*      */   }
/*      */ 
/*      */   protected int findPrototypeId(String paramString)
/*      */   {
/* 1500 */     int i = 0; String str = null;
/*      */     int j;
/* 1501 */     switch (paramString.length()) { case 6:
/* 1502 */       j = paramString.charAt(0);
/* 1503 */       if (j == 103) { str = "getDay"; i = 19;
/* 1504 */       } else if (j == 116) { str = "toJSON"; i = 47; } break;
/*      */     case 7:
/* 1506 */       switch (paramString.charAt(3)) { case 'D':
/* 1507 */         j = paramString.charAt(0);
/* 1508 */         if (j == 103) { str = "getDate"; i = 17;
/* 1509 */         } else if (j == 115) { str = "setDate"; i = 39; } break;
/*      */       case 'T':
/* 1511 */         j = paramString.charAt(0);
/* 1512 */         if (j == 103) { str = "getTime"; i = 11;
/* 1513 */         } else if (j == 115) { str = "setTime"; i = 30; } break;
/*      */       case 'Y':
/* 1515 */         j = paramString.charAt(0);
/* 1516 */         if (j == 103) { str = "getYear"; i = 12;
/* 1517 */         } else if (j == 115) { str = "setYear"; i = 45; } break;
/*      */       case 'u':
/* 1519 */         str = "valueOf"; i = 10; }
/* 1520 */       break;
/*      */     case 8:
/* 1521 */       switch (paramString.charAt(3)) { case 'H':
/* 1522 */         j = paramString.charAt(0);
/* 1523 */         if (j == 103) { str = "getHours"; i = 21;
/* 1524 */         } else if (j == 115) { str = "setHours"; i = 37; } break;
/*      */       case 'M':
/* 1526 */         j = paramString.charAt(0);
/* 1527 */         if (j == 103) { str = "getMonth"; i = 15;
/* 1528 */         } else if (j == 115) { str = "setMonth"; i = 41; } break;
/*      */       case 'o':
/* 1530 */         str = "toSource"; i = 9; break;
/*      */       case 't':
/* 1531 */         str = "toString"; i = 2; }
/* 1532 */       break;
/*      */     case 9:
/* 1533 */       str = "getUTCDay"; i = 20; break;
/*      */     case 10:
/* 1534 */       j = paramString.charAt(3);
/* 1535 */       if (j == 77) {
/* 1536 */         j = paramString.charAt(0);
/* 1537 */         if (j == 103) { str = "getMinutes"; i = 23;
/* 1538 */         } else if (j == 115) { str = "setMinutes"; i = 35; }
/*      */       }
/* 1540 */       else if (j == 83) {
/* 1541 */         j = paramString.charAt(0);
/* 1542 */         if (j == 103) { str = "getSeconds"; i = 25;
/* 1543 */         } else if (j == 115) { str = "setSeconds"; i = 33; }
/*      */       }
/* 1545 */       else if (j == 85) {
/* 1546 */         j = paramString.charAt(0);
/* 1547 */         if (j == 103) { str = "getUTCDate"; i = 18;
/* 1548 */         } else if (j == 115) { str = "setUTCDate"; i = 40; }  } break;
/*      */     case 11:
/* 1551 */       switch (paramString.charAt(3)) { case 'F':
/* 1552 */         j = paramString.charAt(0);
/* 1553 */         if (j == 103) { str = "getFullYear"; i = 13;
/* 1554 */         } else if (j == 115) { str = "setFullYear"; i = 43; } break;
/*      */       case 'M':
/* 1556 */         str = "toGMTString"; i = 8; break;
/*      */       case 'S':
/* 1557 */         str = "toISOString"; i = 46; break;
/*      */       case 'T':
/* 1558 */         str = "toUTCString"; i = 8; break;
/*      */       case 'U':
/* 1559 */         j = paramString.charAt(0);
/* 1560 */         if (j == 103) {
/* 1561 */           j = paramString.charAt(9);
/* 1562 */           if (j == 114) { str = "getUTCHours"; i = 22;
/* 1563 */           } else if (j == 116) { str = "getUTCMonth"; i = 16; }
/*      */         }
/* 1565 */         else if (j == 115) {
/* 1566 */           j = paramString.charAt(9);
/* 1567 */           if (j == 114) { str = "setUTCHours"; i = 38;
/* 1568 */           } else if (j == 116) { str = "setUTCMonth"; i = 42; }  } break;
/*      */       case 's':
/* 1571 */         str = "constructor"; i = 1; }
/* 1572 */       break;
/*      */     case 12:
/* 1573 */       j = paramString.charAt(2);
/* 1574 */       if (j == 68) { str = "toDateString"; i = 4;
/* 1575 */       } else if (j == 84) { str = "toTimeString"; i = 3; } break;
/*      */     case 13:
/* 1577 */       j = paramString.charAt(0);
/* 1578 */       if (j == 103) {
/* 1579 */         j = paramString.charAt(6);
/* 1580 */         if (j == 77) { str = "getUTCMinutes"; i = 24;
/* 1581 */         } else if (j == 83) { str = "getUTCSeconds"; i = 26; }
/*      */       }
/* 1583 */       else if (j == 115) {
/* 1584 */         j = paramString.charAt(6);
/* 1585 */         if (j == 77) { str = "setUTCMinutes"; i = 36;
/* 1586 */         } else if (j == 83) { str = "setUTCSeconds"; i = 34; }  } break;
/*      */     case 14:
/* 1589 */       j = paramString.charAt(0);
/* 1590 */       if (j == 103) { str = "getUTCFullYear"; i = 14;
/* 1591 */       } else if (j == 115) { str = "setUTCFullYear"; i = 44;
/* 1592 */       } else if (j == 116) { str = "toLocaleString"; i = 5; } break;
/*      */     case 15:
/* 1594 */       j = paramString.charAt(0);
/* 1595 */       if (j == 103) { str = "getMilliseconds"; i = 27;
/* 1596 */       } else if (j == 115) { str = "setMilliseconds"; i = 31; } break;
/*      */     case 17:
/* 1598 */       str = "getTimezoneOffset"; i = 29; break;
/*      */     case 18:
/* 1599 */       j = paramString.charAt(0);
/* 1600 */       if (j == 103) { str = "getUTCMilliseconds"; i = 28;
/* 1601 */       } else if (j == 115) { str = "setUTCMilliseconds"; i = 32;
/* 1602 */       } else if (j == 116) {
/* 1603 */         j = paramString.charAt(8);
/* 1604 */         if (j == 68) { str = "toLocaleDateString"; i = 7;
/* 1605 */         } else if (j == 84) { str = "toLocaleTimeString"; i = 6; }  } break;
/*      */     case 16:
/*      */     }
/*      */ 
/* 1609 */     if ((str != null) && (str != paramString) && (!str.equals(paramString))) i = 0;
/*      */ 
/* 1613 */     return i;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   66 */     isoFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
/*   67 */     isoFormat.setLenient(false);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeDate
 * JD-Core Version:    0.6.2
 */