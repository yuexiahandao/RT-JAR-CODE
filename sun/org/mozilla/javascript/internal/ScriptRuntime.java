/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import sun.org.mozilla.javascript.internal.xml.XMLLib;
/*      */ import sun.org.mozilla.javascript.internal.xml.XMLLib.Factory;
/*      */ import sun.org.mozilla.javascript.internal.xml.XMLObject;
/*      */ 
/*      */ public class ScriptRuntime
/*      */ {
/*   99 */   private static BaseFunction THROW_TYPE_ERROR = null;
/*      */ 
/*  141 */   public static final Class<?> BooleanClass = Kit.classOrNull("java.lang.Boolean");
/*  142 */   public static final Class<?> ByteClass = Kit.classOrNull("java.lang.Byte");
/*  143 */   public static final Class<?> CharacterClass = Kit.classOrNull("java.lang.Character");
/*  144 */   public static final Class<?> ClassClass = Kit.classOrNull("java.lang.Class");
/*  145 */   public static final Class<?> DoubleClass = Kit.classOrNull("java.lang.Double");
/*  146 */   public static final Class<?> FloatClass = Kit.classOrNull("java.lang.Float");
/*  147 */   public static final Class<?> IntegerClass = Kit.classOrNull("java.lang.Integer");
/*  148 */   public static final Class<?> LongClass = Kit.classOrNull("java.lang.Long");
/*  149 */   public static final Class<?> NumberClass = Kit.classOrNull("java.lang.Number");
/*  150 */   public static final Class<?> ObjectClass = Kit.classOrNull("java.lang.Object");
/*  151 */   public static final Class<?> ShortClass = Kit.classOrNull("java.lang.Short");
/*  152 */   public static final Class<?> StringClass = Kit.classOrNull("java.lang.String");
/*  153 */   public static final Class<?> DateClass = Kit.classOrNull("java.util.Date");
/*      */ 
/*  156 */   public static final Class<?> ContextClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.Context");
/*      */ 
/*  158 */   public static final Class<?> ContextFactoryClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.ContextFactory");
/*      */ 
/*  160 */   public static final Class<?> FunctionClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.Function");
/*      */ 
/*  162 */   public static final Class<?> ScriptableObjectClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.ScriptableObject");
/*      */ 
/*  164 */   public static final Class<Scriptable> ScriptableClass = Scriptable.class;
/*      */ 
/*  167 */   private static final String[] lazilyNames = { "RegExp", "sun.org.mozilla.javascript.internal.regexp.NativeRegExp", "Packages", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "java", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "javax", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "org", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "com", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "edu", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "net", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "getClass", "sun.org.mozilla.javascript.internal.NativeJavaTopPackage", "JavaImporter", "sun.org.mozilla.javascript.internal.ImporterTopLevel", "Continuation", "sun.org.mozilla.javascript.internal.NativeContinuation", "XML", "(xml)", "XMLList", "(xml)", "Namespace", "(xml)", "QName", "(xml)" };
/*      */ 
/*  188 */   public static Locale ROOT_LOCALE = new Locale("");
/*      */ 
/*  190 */   private static final Object LIBRARY_SCOPE_KEY = "LIBRARY_SCOPE";
/*      */ 
/*  420 */   public static final double NaN = Double.longBitsToDouble(9221120237041090560L);
/*      */ 
/*  424 */   public static final double negativeZero = Double.longBitsToDouble(-9223372036854775808L);
/*      */ 
/*  426 */   public static final Double NaNobj = new Double(NaN);
/*      */   private static final boolean MSJVM_BUG_WORKAROUNDS = true;
/*      */   private static final String DEFAULT_NS_TAG = "__default_namespace__";
/*      */   public static final int ENUMERATE_KEYS = 0;
/*      */   public static final int ENUMERATE_VALUES = 1;
/*      */   public static final int ENUMERATE_ARRAY = 2;
/*      */   public static final int ENUMERATE_KEYS_NO_ITERATOR = 3;
/*      */   public static final int ENUMERATE_VALUES_NO_ITERATOR = 4;
/*      */   public static final int ENUMERATE_ARRAY_NO_ITERATOR = 5;
/* 3694 */   public static MessageProvider messageProvider = new DefaultMessageProvider(null);
/*      */ 
/* 4061 */   public static final Object[] emptyArgs = new Object[0];
/* 4062 */   public static final String[] emptyStrings = new String[0];
/*      */ 
/*      */   public static BaseFunction typeErrorThrower()
/*      */   {
/*   83 */     if (THROW_TYPE_ERROR == null) {
/*   84 */       BaseFunction local1 = new BaseFunction()
/*      */       {
/*      */         public Object call(Context paramAnonymousContext, Scriptable paramAnonymousScriptable1, Scriptable paramAnonymousScriptable2, Object[] paramAnonymousArrayOfObject) {
/*   87 */           throw ScriptRuntime.typeError0("msg.op.not.allowed");
/*      */         }
/*      */ 
/*      */         public int getLength() {
/*   91 */           return 0;
/*      */         }
/*      */       };
/*   94 */       local1.preventExtensions();
/*   95 */       THROW_TYPE_ERROR = local1;
/*      */     }
/*   97 */     return THROW_TYPE_ERROR;
/*      */   }
/*      */ 
/*      */   public static boolean isRhinoRuntimeType(Class<?> paramClass)
/*      */   {
/*  194 */     if (paramClass.isPrimitive()) {
/*  195 */       return paramClass != Character.TYPE;
/*      */     }
/*  197 */     return (paramClass == StringClass) || (paramClass == BooleanClass) || (NumberClass.isAssignableFrom(paramClass)) || (ScriptableClass.isAssignableFrom(paramClass));
/*      */   }
/*      */ 
/*      */   public static ScriptableObject initStandardObjects(Context paramContext, ScriptableObject paramScriptableObject, boolean paramBoolean)
/*      */   {
/*  207 */     if (paramScriptableObject == null) {
/*  208 */       paramScriptableObject = new NativeObject();
/*      */     }
/*  210 */     paramScriptableObject.associateValue(LIBRARY_SCOPE_KEY, paramScriptableObject);
/*  211 */     new ClassCache().associate(paramScriptableObject);
/*      */ 
/*  213 */     BaseFunction.init(paramScriptableObject, paramBoolean);
/*  214 */     NativeObject.init(paramScriptableObject, paramBoolean);
/*      */ 
/*  216 */     Scriptable localScriptable1 = ScriptableObject.getObjectPrototype(paramScriptableObject);
/*      */ 
/*  219 */     Scriptable localScriptable2 = ScriptableObject.getFunctionPrototype(paramScriptableObject);
/*  220 */     localScriptable2.setPrototype(localScriptable1);
/*      */ 
/*  223 */     if (paramScriptableObject.getPrototype() == null) {
/*  224 */       paramScriptableObject.setPrototype(localScriptable1);
/*      */     }
/*      */ 
/*  227 */     NativeError.init(paramScriptableObject, paramBoolean);
/*  228 */     NativeGlobal.init(paramContext, paramScriptableObject, paramBoolean);
/*      */ 
/*  230 */     NativeArray.init(paramScriptableObject, paramBoolean);
/*  231 */     if (paramContext.getOptimizationLevel() > 0)
/*      */     {
/*  235 */       NativeArray.setMaximumInitialCapacity(200000);
/*      */     }
/*  237 */     NativeString.init(paramScriptableObject, paramBoolean);
/*  238 */     NativeBoolean.init(paramScriptableObject, paramBoolean);
/*  239 */     NativeNumber.init(paramScriptableObject, paramBoolean);
/*  240 */     NativeDate.init(paramContext, paramScriptableObject, paramBoolean);
/*  241 */     NativeMath.init(paramScriptableObject, paramBoolean);
/*  242 */     NativeJSON.init(paramScriptableObject, paramBoolean);
/*      */ 
/*  244 */     NativeWith.init(paramScriptableObject, paramBoolean);
/*  245 */     NativeCall.init(paramScriptableObject, paramBoolean);
/*  246 */     NativeScript.init(paramScriptableObject, paramBoolean);
/*      */ 
/*  248 */     NativeIterator.init(paramScriptableObject, paramBoolean);
/*      */ 
/*  250 */     int i = (paramContext.hasFeature(6)) && (paramContext.getE4xImplementationFactory() != null) ? 1 : 0;
/*      */ 
/*  253 */     for (int j = 0; j != lazilyNames.length; j += 2) {
/*  254 */       String str1 = lazilyNames[j];
/*  255 */       String str2 = lazilyNames[(j + 1)];
/*  256 */       if ((i != 0) || (!str2.equals("(xml)")))
/*      */       {
/*  258 */         if ((i != 0) && (str2.equals("(xml)"))) {
/*  259 */           str2 = paramContext.getE4xImplementationFactory().getImplementationClassName();
/*      */         }
/*      */ 
/*  262 */         new LazilyLoadedCtor(paramScriptableObject, str1, str2, paramBoolean, true);
/*      */       }
/*      */     }
/*  265 */     return paramScriptableObject;
/*      */   }
/*      */ 
/*      */   public static ScriptableObject getLibraryScopeOrNull(Scriptable paramScriptable)
/*      */   {
/*  271 */     ScriptableObject localScriptableObject = (ScriptableObject)ScriptableObject.getTopScopeValue(paramScriptable, LIBRARY_SCOPE_KEY);
/*      */ 
/*  273 */     return localScriptableObject;
/*      */   }
/*      */ 
/*      */   public static boolean isJSLineTerminator(int paramInt)
/*      */   {
/*  281 */     if ((paramInt & 0xDFD0) != 0) {
/*  282 */       return false;
/*      */     }
/*  284 */     return (paramInt == 10) || (paramInt == 13) || (paramInt == 8232) || (paramInt == 8233);
/*      */   }
/*      */ 
/*      */   public static boolean isJSWhitespaceOrLineTerminator(int paramInt) {
/*  288 */     return (isStrWhiteSpaceChar(paramInt)) || (isJSLineTerminator(paramInt));
/*      */   }
/*      */ 
/*      */   static boolean isStrWhiteSpaceChar(int paramInt)
/*      */   {
/*  308 */     switch (paramInt) {
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 32:
/*      */     case 160:
/*      */     case 8232:
/*      */     case 8233:
/*      */     case 65279:
/*  319 */       return true;
/*      */     }
/*  321 */     return Character.getType(paramInt) == 12;
/*      */   }
/*      */ 
/*      */   public static Boolean wrapBoolean(boolean paramBoolean)
/*      */   {
/*  327 */     return paramBoolean ? Boolean.TRUE : Boolean.FALSE;
/*      */   }
/*      */ 
/*      */   public static Integer wrapInt(int paramInt)
/*      */   {
/*  332 */     return Integer.valueOf(paramInt);
/*      */   }
/*      */ 
/*      */   public static Number wrapNumber(double paramDouble)
/*      */   {
/*  337 */     if (paramDouble != paramDouble) {
/*  338 */       return NaNobj;
/*      */     }
/*  340 */     return new Double(paramDouble);
/*      */   }
/*      */ 
/*      */   public static boolean toBoolean(Object paramObject)
/*      */   {
/*      */     do
/*      */     {
/*  351 */       if ((paramObject instanceof Boolean))
/*  352 */         return ((Boolean)paramObject).booleanValue();
/*  353 */       if ((paramObject == null) || (paramObject == Undefined.instance))
/*  354 */         return false;
/*  355 */       if ((paramObject instanceof String))
/*  356 */         return ((String)paramObject).length() != 0;
/*  357 */       if ((paramObject instanceof Number)) {
/*  358 */         double d = ((Number)paramObject).doubleValue();
/*  359 */         return (d == d) && (d != 0.0D);
/*      */       }
/*  361 */       if (!(paramObject instanceof Scriptable)) break;
/*  362 */       if (((paramObject instanceof ScriptableObject)) && (((ScriptableObject)paramObject).avoidObjectDetection()))
/*      */       {
/*  365 */         return false;
/*      */       }
/*  367 */       if (Context.getContext().isVersionECMA1())
/*      */       {
/*  369 */         return true;
/*      */       }
/*      */ 
/*  372 */       paramObject = ((Scriptable)paramObject).getDefaultValue(BooleanClass);
/*  373 */     }while (!(paramObject instanceof Scriptable));
/*  374 */     throw errorWithClassName("msg.primitive.expected", paramObject);
/*      */ 
/*  377 */     warnAboutNonJSObject(paramObject);
/*  378 */     return true;
/*      */   }
/*      */ 
/*      */   public static double toNumber(Object paramObject)
/*      */   {
/*      */     do
/*      */     {
/*  390 */       if ((paramObject instanceof Number))
/*  391 */         return ((Number)paramObject).doubleValue();
/*  392 */       if (paramObject == null)
/*  393 */         return 0.0D;
/*  394 */       if (paramObject == Undefined.instance)
/*  395 */         return NaN;
/*  396 */       if ((paramObject instanceof String))
/*  397 */         return toNumber((String)paramObject);
/*  398 */       if ((paramObject instanceof Boolean))
/*  399 */         return ((Boolean)paramObject).booleanValue() ? 1.0D : 0.0D;
/*  400 */       if (!(paramObject instanceof Scriptable)) break;
/*  401 */       paramObject = ((Scriptable)paramObject).getDefaultValue(NumberClass);
/*  402 */     }while (!(paramObject instanceof Scriptable));
/*  403 */     throw errorWithClassName("msg.primitive.expected", paramObject);
/*      */ 
/*  406 */     warnAboutNonJSObject(paramObject);
/*  407 */     return NaN;
/*      */   }
/*      */ 
/*      */   public static double toNumber(Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*  412 */     return paramInt < paramArrayOfObject.length ? toNumber(paramArrayOfObject[paramInt]) : NaN;
/*      */   }
/*      */ 
/*      */   static double stringToNumber(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  432 */     int i = 57;
/*  433 */     int j = 97;
/*  434 */     int k = 65;
/*  435 */     int m = paramString.length();
/*  436 */     if (paramInt2 < 10) {
/*  437 */       i = (char)(48 + paramInt2 - 1);
/*      */     }
/*  439 */     if (paramInt2 > 10) {
/*  440 */       j = (char)(97 + paramInt2 - 10);
/*  441 */       k = (char)(65 + paramInt2 - 10);
/*      */     }
/*      */ 
/*  444 */     double d1 = 0.0D;
/*      */     int i3;
/*  445 */     for (int n = paramInt1; n < m; n++) {
/*  446 */       int i1 = paramString.charAt(n);
/*      */ 
/*  448 */       if ((48 <= i1) && (i1 <= i)) {
/*  449 */         i3 = i1 - 48;
/*  450 */       } else if ((97 <= i1) && (i1 < j)) {
/*  451 */         i3 = i1 - 97 + 10; } else {
/*  452 */         if ((65 > i1) || (i1 >= k)) break;
/*  453 */         i3 = i1 - 65 + 10;
/*      */       }
/*      */ 
/*  456 */       d1 = d1 * paramInt2 + i3;
/*      */     }
/*  458 */     if (paramInt1 == n) {
/*  459 */       return NaN;
/*      */     }
/*  461 */     if (d1 >= 9007199254740992.0D) {
/*  462 */       if (paramInt2 == 10)
/*      */       {
/*      */         try
/*      */         {
/*  469 */           return Double.valueOf(paramString.substring(paramInt1, n)).doubleValue();
/*      */         } catch (NumberFormatException localNumberFormatException) {
/*  471 */           return NaN;
/*      */         }
/*      */       }
/*  473 */       if ((paramInt2 == 2) || (paramInt2 == 4) || (paramInt2 == 8) || (paramInt2 == 16) || (paramInt2 == 32))
/*      */       {
/*  486 */         int i2 = 1;
/*  487 */         i3 = 0;
/*      */ 
/*  495 */         int i4 = 0;
/*  496 */         int i5 = 53;
/*  497 */         double d2 = 0.0D;
/*  498 */         int i6 = 0;
/*      */ 
/*  500 */         int i7 = 0;
/*      */         while (true)
/*      */         {
/*  503 */           if (i2 == 1) {
/*  504 */             if (paramInt1 == n)
/*      */               break;
/*  506 */             i3 = paramString.charAt(paramInt1++);
/*  507 */             if ((48 <= i3) && (i3 <= 57))
/*  508 */               i3 -= 48;
/*  509 */             else if ((97 <= i3) && (i3 <= 122))
/*  510 */               i3 -= 87;
/*      */             else
/*  512 */               i3 -= 55;
/*  513 */             i2 = paramInt2;
/*      */           }
/*  515 */           i2 >>= 1;
/*  516 */           int i8 = (i3 & i2) != 0 ? 1 : 0;
/*      */ 
/*  518 */           switch (i4) {
/*      */           case 0:
/*  520 */             if (i8 != 0) {
/*  521 */               i5--;
/*  522 */               d1 = 1.0D;
/*  523 */               i4 = 1; } break;
/*      */           case 1:
/*  527 */             d1 *= 2.0D;
/*  528 */             if (i8 != 0)
/*  529 */               d1 += 1.0D;
/*  530 */             i5--;
/*  531 */             if (i5 == 0) {
/*  532 */               i6 = i8;
/*  533 */               i4 = 2; } break;
/*      */           case 2:
/*  537 */             i7 = i8;
/*  538 */             d2 = 2.0D;
/*  539 */             i4 = 3;
/*  540 */             break;
/*      */           case 3:
/*  542 */             if (i8 != 0) {
/*  543 */               i4 = 4;
/*      */             }
/*      */ 
/*      */           case 4:
/*  547 */             d2 *= 2.0D;
/*      */           }
/*      */         }
/*      */ 
/*  551 */         switch (i4) {
/*      */         case 0:
/*  553 */           d1 = 0.0D;
/*  554 */           break;
/*      */         case 1:
/*      */         case 2:
/*  558 */           break;
/*      */         case 3:
/*  562 */           if ((i7 & i6) != 0)
/*  563 */             d1 += 1.0D;
/*  564 */           d1 *= d2;
/*  565 */           break;
/*      */         case 4:
/*  569 */           if (i7 != 0)
/*  570 */             d1 += 1.0D;
/*  571 */           d1 *= d2;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  577 */     return d1;
/*      */   }
/*      */ 
/*      */   public static double toNumber(String paramString)
/*      */   {
/*  587 */     int i = paramString.length();
/*  588 */     int j = 0;
/*      */     int k;
/*      */     while (true)
/*      */     {
/*  591 */       if (j == i)
/*      */       {
/*  593 */         return 0.0D;
/*      */       }
/*  595 */       k = paramString.charAt(j);
/*  596 */       if (!isStrWhiteSpaceChar(k))
/*      */         break;
/*  598 */       j++;
/*      */     }
/*      */ 
/*  601 */     if (k == 48) {
/*  602 */       if (j + 2 < i) {
/*  603 */         m = paramString.charAt(j + 1);
/*  604 */         if ((m == 120) || (m == 88))
/*      */         {
/*  606 */           return stringToNumber(paramString, j + 2, 16);
/*      */         }
/*      */       }
/*  609 */     } else if (((k == 43) || (k == 45)) && 
/*  610 */       (j + 3 < i) && (paramString.charAt(j + 1) == '0')) {
/*  611 */       m = paramString.charAt(j + 2);
/*  612 */       if ((m == 120) || (m == 88))
/*      */       {
/*  614 */         double d = stringToNumber(paramString, j + 3, 16);
/*  615 */         return k == 45 ? -d : d;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  620 */     int m = i - 1;
/*      */     int n;
/*  622 */     while (isStrWhiteSpaceChar(n = paramString.charAt(m)))
/*  623 */       m--;
/*  624 */     if (n == 121)
/*      */     {
/*  626 */       if ((k == 43) || (k == 45))
/*  627 */         j++;
/*  628 */       if ((j + 7 == m) && (paramString.regionMatches(j, "Infinity", 0, 8))) {
/*  629 */         return k == 45 ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*      */       }
/*      */ 
/*  632 */       return NaN;
/*      */     }
/*      */ 
/*  636 */     String str = paramString.substring(j, m + 1);
/*      */ 
/*  641 */     for (int i1 = str.length() - 1; i1 >= 0; i1--) {
/*  642 */       int i2 = str.charAt(i1);
/*  643 */       if (((48 > i2) || (i2 > 57)) && (i2 != 46) && (i2 != 101) && (i2 != 69) && (i2 != 43) && (i2 != 45))
/*      */       {
/*  647 */         return NaN;
/*      */       }
/*      */     }
/*      */     try {
/*  651 */       return Double.valueOf(str).doubleValue(); } catch (NumberFormatException localNumberFormatException) {
/*      */     }
/*  653 */     return NaN;
/*      */   }
/*      */ 
/*      */   public static Object[] padArguments(Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*  664 */     if (paramInt < paramArrayOfObject.length) {
/*  665 */       return paramArrayOfObject;
/*      */     }
/*      */ 
/*  668 */     Object[] arrayOfObject = new Object[paramInt];
/*  669 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/*  670 */       arrayOfObject[i] = paramArrayOfObject[i];
/*      */     }
/*      */ 
/*  673 */     for (; i < paramInt; i++) {
/*  674 */       arrayOfObject[i] = Undefined.instance;
/*      */     }
/*      */ 
/*  677 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public static String escapeString(String paramString)
/*      */   {
/*  685 */     return escapeString(paramString, '"');
/*      */   }
/*      */ 
/*      */   public static String escapeString(String paramString, char paramChar)
/*      */   {
/*  694 */     if ((paramChar != '"') && (paramChar != '\'')) Kit.codeBug();
/*  695 */     StringBuffer localStringBuffer = null;
/*      */ 
/*  697 */     int i = 0; for (int j = paramString.length(); i != j; i++) {
/*  698 */       char c1 = paramString.charAt(i);
/*      */ 
/*  700 */       if ((' ' <= c1) && (c1 <= '~') && (c1 != paramChar) && (c1 != '\\'))
/*      */       {
/*  703 */         if (localStringBuffer != null)
/*  704 */           localStringBuffer.append((char)c1);
/*      */       }
/*      */       else
/*      */       {
/*  708 */         if (localStringBuffer == null) {
/*  709 */           localStringBuffer = new StringBuffer(j + 3);
/*  710 */           localStringBuffer.append(paramString);
/*  711 */           localStringBuffer.setLength(i);
/*      */         }
/*      */ 
/*  714 */         int k = -1;
/*  715 */         switch (c1) { case '\b':
/*  716 */           k = 98; break;
/*      */         case '\f':
/*  717 */           k = 102; break;
/*      */         case '\n':
/*  718 */           k = 110; break;
/*      */         case '\r':
/*  719 */           k = 114; break;
/*      */         case '\t':
/*  720 */           k = 116; break;
/*      */         case '\013':
/*  721 */           k = 118; break;
/*      */         case ' ':
/*  722 */           k = 32; break;
/*      */         case '\\':
/*  723 */           k = 92;
/*      */         }
/*  725 */         if (k >= 0)
/*      */         {
/*  727 */           localStringBuffer.append('\\');
/*  728 */           localStringBuffer.append((char)k);
/*  729 */         } else if (c1 == paramChar) {
/*  730 */           localStringBuffer.append('\\');
/*  731 */           localStringBuffer.append(paramChar);
/*      */         }
/*      */         else
/*      */         {
/*      */           int m;
/*  734 */           if (c1 < 'Ā')
/*      */           {
/*  736 */             localStringBuffer.append("\\x");
/*  737 */             m = 2;
/*      */           }
/*      */           else {
/*  740 */             localStringBuffer.append("\\u");
/*  741 */             m = 4;
/*      */           }
/*      */ 
/*  744 */           for (char c2 = (m - 1) * 4; c2 >= 0; c2 -= 4) {
/*  745 */             int n = 0xF & c1 >> c2;
/*  746 */             int i1 = n < 10 ? 48 + n : 87 + n;
/*  747 */             localStringBuffer.append((char)i1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  751 */     return localStringBuffer == null ? paramString : localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   static boolean isValidIdentifierName(String paramString)
/*      */   {
/*  756 */     int i = paramString.length();
/*  757 */     if (i == 0)
/*  758 */       return false;
/*  759 */     if (!Character.isJavaIdentifierStart(paramString.charAt(0)))
/*  760 */       return false;
/*  761 */     for (int j = 1; j != i; j++) {
/*  762 */       if (!Character.isJavaIdentifierPart(paramString.charAt(j)))
/*  763 */         return false;
/*      */     }
/*  765 */     return !TokenStream.isKeyword(paramString);
/*      */   }
/*      */ 
/*      */   public static String toString(Object paramObject)
/*      */   {
/*      */     do
/*      */     {
/*  775 */       if (paramObject == null) {
/*  776 */         return "null";
/*      */       }
/*  778 */       if (paramObject == Undefined.instance) {
/*  779 */         return "undefined";
/*      */       }
/*  781 */       if ((paramObject instanceof String)) {
/*  782 */         return (String)paramObject;
/*      */       }
/*  784 */       if ((paramObject instanceof Number))
/*      */       {
/*  787 */         return numberToString(((Number)paramObject).doubleValue(), 10);
/*      */       }
/*  789 */       if (!(paramObject instanceof Scriptable)) break;
/*  790 */       paramObject = ((Scriptable)paramObject).getDefaultValue(StringClass);
/*  791 */     }while (!(paramObject instanceof Scriptable));
/*  792 */     throw errorWithClassName("msg.primitive.expected", paramObject);
/*      */ 
/*  796 */     return paramObject.toString();
/*      */   }
/*      */ 
/*      */   static String defaultObjectToString(Scriptable paramScriptable)
/*      */   {
/*  802 */     return "[object " + paramScriptable.getClassName() + ']';
/*      */   }
/*      */ 
/*      */   public static String toString(Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*  807 */     return paramInt < paramArrayOfObject.length ? toString(paramArrayOfObject[paramInt]) : "undefined";
/*      */   }
/*      */ 
/*      */   public static String toString(double paramDouble)
/*      */   {
/*  814 */     return numberToString(paramDouble, 10);
/*      */   }
/*      */ 
/*      */   public static String numberToString(double paramDouble, int paramInt) {
/*  818 */     if (paramDouble != paramDouble)
/*  819 */       return "NaN";
/*  820 */     if (paramDouble == (1.0D / 0.0D))
/*  821 */       return "Infinity";
/*  822 */     if (paramDouble == (-1.0D / 0.0D))
/*  823 */       return "-Infinity";
/*  824 */     if (paramDouble == 0.0D) {
/*  825 */       return "0";
/*      */     }
/*  827 */     if ((paramInt < 2) || (paramInt > 36)) {
/*  828 */       throw Context.reportRuntimeError1("msg.bad.radix", Integer.toString(paramInt));
/*      */     }
/*      */ 
/*  832 */     if (paramInt != 10) {
/*  833 */       return DToA.JS_dtobasestr(paramInt, paramDouble);
/*      */     }
/*  835 */     StringBuffer localStringBuffer = new StringBuffer();
/*  836 */     DToA.JS_dtostr(localStringBuffer, 0, 0, paramDouble);
/*  837 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   static String uneval(Context paramContext, Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  844 */     if (paramObject == null) {
/*  845 */       return "null";
/*      */     }
/*  847 */     if (paramObject == Undefined.instance)
/*  848 */       return "undefined";
/*      */     Object localObject;
/*  850 */     if ((paramObject instanceof String)) {
/*  851 */       String str = escapeString((String)paramObject);
/*  852 */       localObject = new StringBuffer(str.length() + 2);
/*  853 */       ((StringBuffer)localObject).append('"');
/*  854 */       ((StringBuffer)localObject).append(str);
/*  855 */       ((StringBuffer)localObject).append('"');
/*  856 */       return ((StringBuffer)localObject).toString();
/*      */     }
/*  858 */     if ((paramObject instanceof Number)) {
/*  859 */       double d = ((Number)paramObject).doubleValue();
/*  860 */       if ((d == 0.0D) && (1.0D / d < 0.0D)) {
/*  861 */         return "-0";
/*      */       }
/*  863 */       return toString(d);
/*      */     }
/*  865 */     if ((paramObject instanceof Boolean)) {
/*  866 */       return toString(paramObject);
/*      */     }
/*  868 */     if ((paramObject instanceof Scriptable)) {
/*  869 */       Scriptable localScriptable = (Scriptable)paramObject;
/*      */ 
/*  872 */       if (ScriptableObject.hasProperty(localScriptable, "toSource")) {
/*  873 */         localObject = ScriptableObject.getProperty(localScriptable, "toSource");
/*  874 */         if ((localObject instanceof Function)) {
/*  875 */           Function localFunction = (Function)localObject;
/*  876 */           return toString(localFunction.call(paramContext, paramScriptable, localScriptable, emptyArgs));
/*      */         }
/*      */       }
/*  879 */       return toString(paramObject);
/*      */     }
/*  881 */     warnAboutNonJSObject(paramObject);
/*  882 */     return paramObject.toString();
/*      */   }
/*      */ 
/*      */   static String defaultObjectToSource(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/*      */     int i;
/*      */     boolean bool;
/*  889 */     if (paramContext.iterating == null) {
/*  890 */       i = 1;
/*  891 */       bool = false;
/*  892 */       paramContext.iterating = new ObjToIntMap(31);
/*      */     } else {
/*  894 */       i = 0;
/*  895 */       bool = paramContext.iterating.has(paramScriptable2);
/*      */     }
/*      */ 
/*  898 */     StringBuffer localStringBuffer = new StringBuffer(128);
/*  899 */     if (i != 0) {
/*  900 */       localStringBuffer.append("(");
/*      */     }
/*  902 */     localStringBuffer.append('{');
/*      */     try
/*      */     {
/*  907 */       if (!bool) {
/*  908 */         paramContext.iterating.intern(paramScriptable2);
/*  909 */         Object[] arrayOfObject = paramScriptable2.getIds();
/*  910 */         for (int j = 0; j < arrayOfObject.length; j++) {
/*  911 */           Object localObject1 = arrayOfObject[j];
/*      */           Object localObject2;
/*  913 */           if ((localObject1 instanceof Integer)) {
/*  914 */             int k = ((Integer)localObject1).intValue();
/*  915 */             localObject2 = paramScriptable2.get(k, paramScriptable2);
/*  916 */             if (localObject2 == Scriptable.NOT_FOUND)
/*      */               continue;
/*  918 */             if (j > 0)
/*  919 */               localStringBuffer.append(", ");
/*  920 */             localStringBuffer.append(k);
/*      */           } else {
/*  922 */             String str = (String)localObject1;
/*  923 */             localObject2 = paramScriptable2.get(str, paramScriptable2);
/*  924 */             if (localObject2 == Scriptable.NOT_FOUND)
/*      */               continue;
/*  926 */             if (j > 0)
/*  927 */               localStringBuffer.append(", ");
/*  928 */             if (isValidIdentifierName(str)) {
/*  929 */               localStringBuffer.append(str);
/*      */             } else {
/*  931 */               localStringBuffer.append('\'');
/*  932 */               localStringBuffer.append(escapeString(str, '\''));
/*      */ 
/*  934 */               localStringBuffer.append('\'');
/*      */             }
/*      */           }
/*  937 */           localStringBuffer.append(':');
/*  938 */           localStringBuffer.append(uneval(paramContext, paramScriptable1, localObject2));
/*      */         }
/*      */       }
/*      */     } finally {
/*  942 */       if (i != 0) {
/*  943 */         paramContext.iterating = null;
/*      */       }
/*      */     }
/*      */ 
/*  947 */     localStringBuffer.append('}');
/*  948 */     if (i != 0) {
/*  949 */       localStringBuffer.append(')');
/*      */     }
/*  951 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public static Scriptable toObject(Scriptable paramScriptable, Object paramObject)
/*      */   {
/*  956 */     if ((paramObject instanceof Scriptable)) {
/*  957 */       return (Scriptable)paramObject;
/*      */     }
/*  959 */     return toObject(Context.getContext(), paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public static Scriptable toObjectOrNull(Context paramContext, Object paramObject)
/*      */   {
/*  967 */     if ((paramObject instanceof Scriptable))
/*  968 */       return (Scriptable)paramObject;
/*  969 */     if ((paramObject != null) && (paramObject != Undefined.instance)) {
/*  970 */       return toObject(paramContext, getTopCallScope(paramContext), paramObject);
/*      */     }
/*  972 */     return null;
/*      */   }
/*      */ 
/*      */   public static Scriptable toObjectOrNull(Context paramContext, Object paramObject, Scriptable paramScriptable)
/*      */   {
/*  981 */     if ((paramObject instanceof Scriptable))
/*  982 */       return (Scriptable)paramObject;
/*  983 */     if ((paramObject != null) && (paramObject != Undefined.instance)) {
/*  984 */       return toObject(paramContext, paramScriptable, paramObject);
/*      */     }
/*  986 */     return null;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Scriptable toObject(Scriptable paramScriptable, Object paramObject, Class<?> paramClass)
/*      */   {
/*  995 */     if ((paramObject instanceof Scriptable)) {
/*  996 */       return (Scriptable)paramObject;
/*      */     }
/*  998 */     return toObject(Context.getContext(), paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public static Scriptable toObject(Context paramContext, Scriptable paramScriptable, Object paramObject)
/*      */   {
/* 1008 */     if ((paramObject instanceof Scriptable)) {
/* 1009 */       return (Scriptable)paramObject;
/*      */     }
/* 1011 */     if (paramObject == null) {
/* 1012 */       throw typeError0("msg.null.to.object");
/*      */     }
/* 1014 */     if (paramObject == Undefined.instance) {
/* 1015 */       throw typeError0("msg.undef.to.object");
/*      */     }
/* 1017 */     String str = (paramObject instanceof Boolean) ? "Boolean" : (paramObject instanceof Number) ? "Number" : (paramObject instanceof String) ? "String" : null;
/*      */ 
/* 1021 */     if (str != null) {
/* 1022 */       localObject = new Object[] { paramObject };
/* 1023 */       paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 1024 */       return newObject(paramContext, paramScriptable, str, (Object[])localObject);
/*      */     }
/*      */ 
/* 1028 */     Object localObject = paramContext.getWrapFactory().wrap(paramContext, paramScriptable, paramObject, null);
/* 1029 */     if ((localObject instanceof Scriptable))
/* 1030 */       return (Scriptable)localObject;
/* 1031 */     throw errorWithClassName("msg.invalid.type", paramObject);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Scriptable toObject(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass)
/*      */   {
/* 1040 */     return toObject(paramContext, paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Object call(Context paramContext, Object paramObject1, Object paramObject2, Object[] paramArrayOfObject, Scriptable paramScriptable)
/*      */   {
/* 1049 */     if (!(paramObject1 instanceof Function)) {
/* 1050 */       throw notFunctionError(toString(paramObject1));
/*      */     }
/* 1052 */     Function localFunction = (Function)paramObject1;
/* 1053 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject2);
/* 1054 */     if (localScriptable == null) {
/* 1055 */       throw undefCallError(localScriptable, "function");
/*      */     }
/* 1057 */     return localFunction.call(paramContext, paramScriptable, localScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static Scriptable newObject(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 1063 */     paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 1064 */     Function localFunction = getExistingCtor(paramContext, paramScriptable, paramString);
/* 1065 */     if (paramArrayOfObject == null) paramArrayOfObject = emptyArgs;
/* 1066 */     return localFunction.construct(paramContext, paramScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static double toInteger(Object paramObject)
/*      */   {
/* 1074 */     return toInteger(toNumber(paramObject));
/*      */   }
/*      */ 
/*      */   public static double toInteger(double paramDouble)
/*      */   {
/* 1080 */     if (paramDouble != paramDouble) {
/* 1081 */       return 0.0D;
/*      */     }
/* 1083 */     if ((paramDouble == 0.0D) || (paramDouble == (1.0D / 0.0D)) || (paramDouble == (-1.0D / 0.0D)))
/*      */     {
/* 1086 */       return paramDouble;
/*      */     }
/* 1088 */     if (paramDouble > 0.0D) {
/* 1089 */       return Math.floor(paramDouble);
/*      */     }
/* 1091 */     return Math.ceil(paramDouble);
/*      */   }
/*      */ 
/*      */   public static double toInteger(Object[] paramArrayOfObject, int paramInt) {
/* 1095 */     return paramInt < paramArrayOfObject.length ? toInteger(paramArrayOfObject[paramInt]) : 0.0D;
/*      */   }
/*      */ 
/*      */   public static int toInt32(Object paramObject)
/*      */   {
/* 1105 */     if ((paramObject instanceof Integer)) {
/* 1106 */       return ((Integer)paramObject).intValue();
/*      */     }
/* 1108 */     return toInt32(toNumber(paramObject));
/*      */   }
/*      */ 
/*      */   public static int toInt32(Object[] paramArrayOfObject, int paramInt) {
/* 1112 */     return paramInt < paramArrayOfObject.length ? toInt32(paramArrayOfObject[paramInt]) : 0;
/*      */   }
/*      */ 
/*      */   public static int toInt32(double paramDouble) {
/* 1116 */     int i = (int)paramDouble;
/* 1117 */     if (i == paramDouble)
/*      */     {
/* 1119 */       return i;
/*      */     }
/*      */ 
/* 1122 */     if ((paramDouble != paramDouble) || (paramDouble == (1.0D / 0.0D)) || (paramDouble == (-1.0D / 0.0D)))
/*      */     {
/* 1126 */       return 0;
/*      */     }
/*      */ 
/* 1129 */     paramDouble = paramDouble >= 0.0D ? Math.floor(paramDouble) : Math.ceil(paramDouble);
/*      */ 
/* 1131 */     double d = 4294967296.0D;
/* 1132 */     paramDouble = Math.IEEEremainder(paramDouble, d);
/*      */ 
/* 1135 */     long l = ()paramDouble;
/*      */ 
/* 1138 */     return (int)l;
/*      */   }
/*      */ 
/*      */   public static long toUint32(double paramDouble)
/*      */   {
/* 1146 */     long l = ()paramDouble;
/* 1147 */     if (l == paramDouble)
/*      */     {
/* 1149 */       return l & 0xFFFFFFFF;
/*      */     }
/*      */ 
/* 1152 */     if ((paramDouble != paramDouble) || (paramDouble == (1.0D / 0.0D)) || (paramDouble == (-1.0D / 0.0D)))
/*      */     {
/* 1156 */       return 0L;
/*      */     }
/*      */ 
/* 1159 */     paramDouble = paramDouble >= 0.0D ? Math.floor(paramDouble) : Math.ceil(paramDouble);
/*      */ 
/* 1162 */     double d = 4294967296.0D;
/* 1163 */     l = ()Math.IEEEremainder(paramDouble, d);
/*      */ 
/* 1165 */     return l & 0xFFFFFFFF;
/*      */   }
/*      */ 
/*      */   public static long toUint32(Object paramObject) {
/* 1169 */     return toUint32(toNumber(paramObject));
/*      */   }
/*      */ 
/*      */   public static char toUint16(Object paramObject)
/*      */   {
/* 1177 */     double d = toNumber(paramObject);
/*      */ 
/* 1179 */     int i = (int)d;
/* 1180 */     if (i == d) {
/* 1181 */       return (char)i;
/*      */     }
/*      */ 
/* 1184 */     if ((d != d) || (d == (1.0D / 0.0D)) || (d == (-1.0D / 0.0D)))
/*      */     {
/* 1188 */       return '\000';
/*      */     }
/*      */ 
/* 1191 */     d = d >= 0.0D ? Math.floor(d) : Math.ceil(d);
/*      */ 
/* 1193 */     int j = 65536;
/* 1194 */     i = (int)Math.IEEEremainder(d, j);
/*      */ 
/* 1196 */     return (char)i;
/*      */   }
/*      */ 
/*      */   public static Object setDefaultNamespace(Object paramObject, Context paramContext)
/*      */   {
/* 1205 */     Object localObject1 = paramContext.currentActivationCall;
/* 1206 */     if (localObject1 == null) {
/* 1207 */       localObject1 = getTopCallScope(paramContext);
/*      */     }
/*      */ 
/* 1210 */     XMLLib localXMLLib = currentXMLLib(paramContext);
/* 1211 */     Object localObject2 = localXMLLib.toDefaultXmlNamespace(paramContext, paramObject);
/*      */ 
/* 1214 */     if (!((Scriptable)localObject1).has("__default_namespace__", (Scriptable)localObject1))
/*      */     {
/* 1216 */       ScriptableObject.defineProperty((Scriptable)localObject1, "__default_namespace__", localObject2, 6);
/*      */     }
/*      */     else
/*      */     {
/* 1220 */       ((Scriptable)localObject1).put("__default_namespace__", (Scriptable)localObject1, localObject2);
/*      */     }
/*      */ 
/* 1223 */     return Undefined.instance;
/*      */   }
/*      */ 
/*      */   public static Object searchDefaultNamespace(Context paramContext)
/*      */   {
/* 1228 */     Object localObject1 = paramContext.currentActivationCall;
/* 1229 */     if (localObject1 == null)
/* 1230 */       localObject1 = getTopCallScope(paramContext);
/*      */     Object localObject2;
/*      */     while (true)
/*      */     {
/* 1234 */       Scriptable localScriptable = ((Scriptable)localObject1).getParentScope();
/* 1235 */       if (localScriptable == null) {
/* 1236 */         localObject2 = ScriptableObject.getProperty((Scriptable)localObject1, "__default_namespace__");
/* 1237 */         if (localObject2 != Scriptable.NOT_FOUND) break;
/* 1238 */         return null;
/*      */       }
/*      */ 
/* 1242 */       localObject2 = ((Scriptable)localObject1).get("__default_namespace__", (Scriptable)localObject1);
/* 1243 */       if (localObject2 != Scriptable.NOT_FOUND) {
/*      */         break;
/*      */       }
/* 1246 */       localObject1 = localScriptable;
/*      */     }
/* 1248 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static Object getTopLevelProp(Scriptable paramScriptable, String paramString) {
/* 1252 */     paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 1253 */     return ScriptableObject.getProperty(paramScriptable, paramString);
/*      */   }
/*      */ 
/*      */   static Function getExistingCtor(Context paramContext, Scriptable paramScriptable, String paramString)
/*      */   {
/* 1259 */     Object localObject = ScriptableObject.getProperty(paramScriptable, paramString);
/* 1260 */     if ((localObject instanceof Function)) {
/* 1261 */       return (Function)localObject;
/*      */     }
/* 1263 */     if (localObject == Scriptable.NOT_FOUND) {
/* 1264 */       throw Context.reportRuntimeError1("msg.ctor.not.found", paramString);
/*      */     }
/*      */ 
/* 1267 */     throw Context.reportRuntimeError1("msg.not.ctor", paramString);
/*      */   }
/*      */ 
/*      */   private static long indexFromString(String paramString)
/*      */   {
/* 1282 */     int i = paramString.length();
/* 1283 */     if (i > 0) {
/* 1284 */       int j = 0;
/* 1285 */       int k = 0;
/* 1286 */       int m = paramString.charAt(0);
/* 1287 */       if ((m == 45) && 
/* 1288 */         (i > 1)) {
/* 1289 */         m = paramString.charAt(1);
/* 1290 */         j = 1;
/* 1291 */         k = 1;
/*      */       }
/*      */ 
/* 1294 */       m -= 48;
/* 1295 */       if ((0 <= m) && (m <= 9)) if (i <= (k != 0 ? 11 : 10))
/*      */         {
/* 1301 */           int n = -m;
/* 1302 */           int i1 = 0;
/* 1303 */           j++;
/* 1304 */           if (n != 0)
/*      */           {
/* 1306 */             while ((j != i) && (0 <= (m = paramString.charAt(j) - '0')) && (m <= 9))
/*      */             {
/* 1308 */               i1 = n;
/* 1309 */               n = 10 * n - m;
/* 1310 */               j++;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1315 */           if (j == i) if (i1 <= -214748364) { if (i1 == -214748364)
/* 1315 */                 if (m > (k != 0 ? 8 : 7));
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1321 */               return 0xFFFFFFFF & (k != 0 ? n : -n);
/*      */             }
/*      */         }
/*      */     }
/* 1325 */     return -1L;
/*      */   }
/*      */ 
/*      */   public static long testUint32String(String paramString)
/*      */   {
/* 1338 */     int i = paramString.length();
/* 1339 */     if ((1 <= i) && (i <= 10)) {
/* 1340 */       int j = paramString.charAt(0);
/* 1341 */       j -= 48;
/* 1342 */       if (j == 0)
/*      */       {
/* 1344 */         return i == 1 ? 0L : -1L;
/*      */       }
/* 1346 */       if ((1 <= j) && (j <= 9)) {
/* 1347 */         long l = j;
/* 1348 */         for (int k = 1; k != i; k++) {
/* 1349 */           j = paramString.charAt(k) - '0';
/* 1350 */           if ((0 > j) || (j > 9)) {
/* 1351 */             return -1L;
/*      */           }
/* 1353 */           l = 10L * l + j;
/*      */         }
/*      */ 
/* 1356 */         if (l >>> 32 == 0L) {
/* 1357 */           return l;
/*      */         }
/*      */       }
/*      */     }
/* 1361 */     return -1L;
/*      */   }
/*      */ 
/*      */   static Object getIndexObject(String paramString)
/*      */   {
/* 1370 */     long l = indexFromString(paramString);
/* 1371 */     if (l >= 0L) {
/* 1372 */       return Integer.valueOf((int)l);
/*      */     }
/* 1374 */     return paramString;
/*      */   }
/*      */ 
/*      */   static Object getIndexObject(double paramDouble)
/*      */   {
/* 1383 */     int i = (int)paramDouble;
/* 1384 */     if (i == paramDouble) {
/* 1385 */       return Integer.valueOf(i);
/*      */     }
/* 1387 */     return toString(paramDouble);
/*      */   }
/*      */ 
/*      */   static String toStringIdOrIndex(Context paramContext, Object paramObject)
/*      */   {
/* 1397 */     if ((paramObject instanceof Number)) {
/* 1398 */       double d = ((Number)paramObject).doubleValue();
/* 1399 */       int i = (int)d;
/* 1400 */       if (i == d) {
/* 1401 */         storeIndexResult(paramContext, i);
/* 1402 */         return null;
/*      */       }
/* 1404 */       return toString(paramObject);
/*      */     }
/*      */     String str;
/* 1407 */     if ((paramObject instanceof String))
/* 1408 */       str = (String)paramObject;
/*      */     else {
/* 1410 */       str = toString(paramObject);
/*      */     }
/* 1412 */     long l = indexFromString(str);
/* 1413 */     if (l >= 0L) {
/* 1414 */       storeIndexResult(paramContext, (int)l);
/* 1415 */       return null;
/*      */     }
/* 1417 */     return str;
/*      */   }
/*      */ 
/*      */   public static Object getObjectElem(Object paramObject1, Object paramObject2, Context paramContext)
/*      */   {
/* 1426 */     return getObjectElem(paramObject1, paramObject2, paramContext, getTopCallScope(paramContext));
/*      */   }
/*      */ 
/*      */   public static Object getObjectElem(Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 1434 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject1, paramScriptable);
/* 1435 */     if (localScriptable == null) {
/* 1436 */       throw undefReadError(paramObject1, paramObject2);
/*      */     }
/* 1438 */     return getObjectElem(localScriptable, paramObject2, paramContext);
/*      */   }
/*      */ 
/*      */   public static Object getObjectElem(Scriptable paramScriptable, Object paramObject, Context paramContext)
/*      */   {
/*      */     Object localObject;
/* 1444 */     if ((paramScriptable instanceof XMLObject)) {
/* 1445 */       localObject = (XMLObject)paramScriptable;
/* 1446 */       return ((XMLObject)localObject).ecmaGet(paramContext, paramObject);
/*      */     }
/*      */ 
/* 1451 */     String str = toStringIdOrIndex(paramContext, paramObject);
/* 1452 */     if (str == null) {
/* 1453 */       int i = lastIndexResult(paramContext);
/* 1454 */       localObject = ScriptableObject.getProperty(paramScriptable, i);
/*      */     } else {
/* 1456 */       localObject = ScriptableObject.getProperty(paramScriptable, str);
/*      */     }
/*      */ 
/* 1459 */     if (localObject == Scriptable.NOT_FOUND) {
/* 1460 */       localObject = Undefined.instance;
/*      */     }
/*      */ 
/* 1463 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Object getObjectProp(Object paramObject, String paramString, Context paramContext)
/*      */   {
/* 1472 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject);
/* 1473 */     if (localScriptable == null) {
/* 1474 */       throw undefReadError(paramObject, paramString);
/*      */     }
/* 1476 */     return getObjectProp(localScriptable, paramString, paramContext);
/*      */   }
/*      */ 
/*      */   public static Object getObjectProp(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 1485 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject, paramScriptable);
/* 1486 */     if (localScriptable == null) {
/* 1487 */       throw undefReadError(paramObject, paramString);
/*      */     }
/* 1489 */     return getObjectProp(localScriptable, paramString, paramContext);
/*      */   }
/*      */ 
/*      */   public static Object getObjectProp(Scriptable paramScriptable, String paramString, Context paramContext)
/*      */   {
/* 1495 */     if ((paramScriptable instanceof XMLObject))
/*      */     {
/* 1499 */       localObject = (XMLObject)paramScriptable;
/* 1500 */       return ((XMLObject)localObject).ecmaGet(paramContext, paramString);
/*      */     }
/*      */ 
/* 1503 */     Object localObject = ScriptableObject.getProperty(paramScriptable, paramString);
/* 1504 */     if (localObject == Scriptable.NOT_FOUND) {
/* 1505 */       if (paramContext.hasFeature(11)) {
/* 1506 */         Context.reportWarning(getMessage1("msg.ref.undefined.prop", paramString));
/*      */       }
/*      */ 
/* 1509 */       localObject = Undefined.instance;
/*      */     }
/*      */ 
/* 1512 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Object getObjectPropNoWarn(Object paramObject, String paramString, Context paramContext)
/*      */   {
/* 1518 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject);
/* 1519 */     if (localScriptable == null) {
/* 1520 */       throw undefReadError(paramObject, paramString);
/*      */     }
/* 1522 */     if ((paramObject instanceof XMLObject))
/*      */     {
/* 1524 */       getObjectProp(localScriptable, paramString, paramContext);
/*      */     }
/* 1526 */     Object localObject = ScriptableObject.getProperty(localScriptable, paramString);
/* 1527 */     if (localObject == Scriptable.NOT_FOUND) {
/* 1528 */       return Undefined.instance;
/*      */     }
/* 1530 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Object getObjectIndex(Object paramObject, double paramDouble, Context paramContext)
/*      */   {
/* 1540 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject);
/* 1541 */     if (localScriptable == null) {
/* 1542 */       throw undefReadError(paramObject, toString(paramDouble));
/*      */     }
/*      */ 
/* 1545 */     int i = (int)paramDouble;
/* 1546 */     if (i == paramDouble) {
/* 1547 */       return getObjectIndex(localScriptable, i, paramContext);
/*      */     }
/* 1549 */     String str = toString(paramDouble);
/* 1550 */     return getObjectProp(localScriptable, str, paramContext);
/*      */   }
/*      */ 
/*      */   public static Object getObjectIndex(Scriptable paramScriptable, int paramInt, Context paramContext)
/*      */   {
/* 1557 */     if ((paramScriptable instanceof XMLObject)) {
/* 1558 */       localObject = (XMLObject)paramScriptable;
/* 1559 */       return ((XMLObject)localObject).ecmaGet(paramContext, Integer.valueOf(paramInt));
/*      */     }
/*      */ 
/* 1562 */     Object localObject = ScriptableObject.getProperty(paramScriptable, paramInt);
/* 1563 */     if (localObject == Scriptable.NOT_FOUND) {
/* 1564 */       localObject = Undefined.instance;
/*      */     }
/*      */ 
/* 1567 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Object setObjectElem(Object paramObject1, Object paramObject2, Object paramObject3, Context paramContext)
/*      */   {
/* 1576 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject1);
/* 1577 */     if (localScriptable == null) {
/* 1578 */       throw undefWriteError(paramObject1, paramObject2, paramObject3);
/*      */     }
/* 1580 */     return setObjectElem(localScriptable, paramObject2, paramObject3, paramContext);
/*      */   }
/*      */ 
/*      */   public static Object setObjectElem(Scriptable paramScriptable, Object paramObject1, Object paramObject2, Context paramContext)
/*      */   {
/* 1586 */     if ((paramScriptable instanceof XMLObject)) {
/* 1587 */       localObject = (XMLObject)paramScriptable;
/* 1588 */       ((XMLObject)localObject).ecmaPut(paramContext, paramObject1, paramObject2);
/* 1589 */       return paramObject2;
/*      */     }
/*      */ 
/* 1592 */     Object localObject = toStringIdOrIndex(paramContext, paramObject1);
/* 1593 */     if (localObject == null) {
/* 1594 */       int i = lastIndexResult(paramContext);
/* 1595 */       ScriptableObject.putProperty(paramScriptable, i, paramObject2);
/*      */     } else {
/* 1597 */       ScriptableObject.putProperty(paramScriptable, (String)localObject, paramObject2);
/*      */     }
/*      */ 
/* 1600 */     return paramObject2;
/*      */   }
/*      */ 
/*      */   public static Object setObjectProp(Object paramObject1, String paramString, Object paramObject2, Context paramContext)
/*      */   {
/* 1609 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject1);
/* 1610 */     if (localScriptable == null) {
/* 1611 */       throw undefWriteError(paramObject1, paramString, paramObject2);
/*      */     }
/* 1613 */     return setObjectProp(localScriptable, paramString, paramObject2, paramContext);
/*      */   }
/*      */ 
/*      */   public static Object setObjectProp(Scriptable paramScriptable, String paramString, Object paramObject, Context paramContext)
/*      */   {
/* 1619 */     if ((paramScriptable instanceof XMLObject)) {
/* 1620 */       XMLObject localXMLObject = (XMLObject)paramScriptable;
/* 1621 */       localXMLObject.ecmaPut(paramContext, paramString, paramObject);
/*      */     } else {
/* 1623 */       ScriptableObject.putProperty(paramScriptable, paramString, paramObject);
/*      */     }
/* 1625 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public static Object setObjectIndex(Object paramObject1, double paramDouble, Object paramObject2, Context paramContext)
/*      */   {
/* 1635 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject1);
/* 1636 */     if (localScriptable == null) {
/* 1637 */       throw undefWriteError(paramObject1, String.valueOf(paramDouble), paramObject2);
/*      */     }
/*      */ 
/* 1640 */     int i = (int)paramDouble;
/* 1641 */     if (i == paramDouble) {
/* 1642 */       return setObjectIndex(localScriptable, i, paramObject2, paramContext);
/*      */     }
/* 1644 */     String str = toString(paramDouble);
/* 1645 */     return setObjectProp(localScriptable, str, paramObject2, paramContext);
/*      */   }
/*      */ 
/*      */   public static Object setObjectIndex(Scriptable paramScriptable, int paramInt, Object paramObject, Context paramContext)
/*      */   {
/* 1652 */     if ((paramScriptable instanceof XMLObject)) {
/* 1653 */       XMLObject localXMLObject = (XMLObject)paramScriptable;
/* 1654 */       localXMLObject.ecmaPut(paramContext, Integer.valueOf(paramInt), paramObject);
/*      */     } else {
/* 1656 */       ScriptableObject.putProperty(paramScriptable, paramInt, paramObject);
/*      */     }
/* 1658 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public static boolean deleteObjectElem(Scriptable paramScriptable, Object paramObject, Context paramContext)
/*      */   {
/*      */     Object localObject;
/*      */     boolean bool;
/* 1665 */     if ((paramScriptable instanceof XMLObject)) {
/* 1666 */       localObject = (XMLObject)paramScriptable;
/* 1667 */       bool = ((XMLObject)localObject).ecmaDelete(paramContext, paramObject);
/*      */     } else {
/* 1669 */       localObject = toStringIdOrIndex(paramContext, paramObject);
/* 1670 */       if (localObject == null) {
/* 1671 */         int i = lastIndexResult(paramContext);
/* 1672 */         paramScriptable.delete(i);
/* 1673 */         return !paramScriptable.has(i, paramScriptable);
/*      */       }
/* 1675 */       paramScriptable.delete((String)localObject);
/* 1676 */       return !paramScriptable.has((String)localObject, paramScriptable);
/*      */     }
/*      */ 
/* 1679 */     return bool;
/*      */   }
/*      */ 
/*      */   public static boolean hasObjectElem(Scriptable paramScriptable, Object paramObject, Context paramContext)
/*      */   {
/*      */     Object localObject;
/*      */     boolean bool;
/* 1687 */     if ((paramScriptable instanceof XMLObject)) {
/* 1688 */       localObject = (XMLObject)paramScriptable;
/* 1689 */       bool = ((XMLObject)localObject).ecmaHas(paramContext, paramObject);
/*      */     } else {
/* 1691 */       localObject = toStringIdOrIndex(paramContext, paramObject);
/* 1692 */       if (localObject == null) {
/* 1693 */         int i = lastIndexResult(paramContext);
/* 1694 */         bool = ScriptableObject.hasProperty(paramScriptable, i);
/*      */       } else {
/* 1696 */         bool = ScriptableObject.hasProperty(paramScriptable, (String)localObject);
/*      */       }
/*      */     }
/*      */ 
/* 1700 */     return bool;
/*      */   }
/*      */ 
/*      */   public static Object refGet(Ref paramRef, Context paramContext)
/*      */   {
/* 1705 */     return paramRef.get(paramContext);
/*      */   }
/*      */ 
/*      */   public static Object refSet(Ref paramRef, Object paramObject, Context paramContext)
/*      */   {
/* 1710 */     return paramRef.set(paramContext, paramObject);
/*      */   }
/*      */ 
/*      */   public static Object refDel(Ref paramRef, Context paramContext)
/*      */   {
/* 1715 */     return wrapBoolean(paramRef.delete(paramContext));
/*      */   }
/*      */ 
/*      */   static boolean isSpecialProperty(String paramString)
/*      */   {
/* 1720 */     return (paramString.equals("__proto__")) || (paramString.equals("__parent__"));
/*      */   }
/*      */ 
/*      */   public static Ref specialRef(Object paramObject, String paramString, Context paramContext)
/*      */   {
/* 1726 */     return SpecialRef.createSpecial(paramContext, paramObject, paramString);
/*      */   }
/*      */ 
/*      */   public static Object delete(Object paramObject1, Object paramObject2, Context paramContext)
/*      */   {
/* 1742 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject1);
/* 1743 */     if (localScriptable == null) {
/* 1744 */       String str = paramObject2 == null ? "null" : paramObject2.toString();
/* 1745 */       throw typeError2("msg.undef.prop.delete", toString(paramObject1), str);
/*      */     }
/* 1747 */     boolean bool = deleteObjectElem(localScriptable, paramObject2, paramContext);
/* 1748 */     return wrapBoolean(bool);
/*      */   }
/*      */ 
/*      */   public static Object name(Context paramContext, Scriptable paramScriptable, String paramString)
/*      */   {
/* 1756 */     Scriptable localScriptable = paramScriptable.getParentScope();
/* 1757 */     if (localScriptable == null) {
/* 1758 */       Object localObject = topScopeName(paramContext, paramScriptable, paramString);
/* 1759 */       if (localObject == Scriptable.NOT_FOUND) {
/* 1760 */         throw notFoundError(paramScriptable, paramString);
/*      */       }
/* 1762 */       return localObject;
/*      */     }
/*      */ 
/* 1765 */     return nameOrFunction(paramContext, paramScriptable, localScriptable, paramString, false);
/*      */   }
/*      */ 
/*      */   private static Object nameOrFunction(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, String paramString, boolean paramBoolean)
/*      */   {
/* 1773 */     Object localObject2 = paramScriptable1;
/*      */ 
/* 1775 */     Object localObject3 = null;
/*      */     do {
/* 1777 */       if ((paramScriptable1 instanceof NativeWith)) {
/* 1778 */         Scriptable localScriptable = paramScriptable1.getPrototype();
/* 1779 */         if ((localScriptable instanceof XMLObject)) {
/* 1780 */           XMLObject localXMLObject = (XMLObject)localScriptable;
/* 1781 */           if (localXMLObject.ecmaHas(paramContext, paramString))
/*      */           {
/* 1783 */             localObject2 = localXMLObject;
/* 1784 */             localObject1 = localXMLObject.ecmaGet(paramContext, paramString);
/* 1785 */             break;
/*      */           }
/* 1787 */           if (localObject3 == null)
/* 1788 */             localObject3 = localXMLObject;
/*      */         }
/*      */         else {
/* 1791 */           localObject1 = ScriptableObject.getProperty(localScriptable, paramString);
/* 1792 */           if (localObject1 != Scriptable.NOT_FOUND)
/*      */           {
/* 1794 */             localObject2 = localScriptable;
/* 1795 */             break;
/*      */           }
/*      */         }
/* 1798 */       } else if ((paramScriptable1 instanceof NativeCall))
/*      */       {
/* 1801 */         localObject1 = paramScriptable1.get(paramString, paramScriptable1);
/* 1802 */         if (localObject1 != Scriptable.NOT_FOUND) {
/* 1803 */           if (!paramBoolean) {
/*      */             break;
/*      */           }
/* 1806 */           localObject2 = ScriptableObject.getTopLevelScope(paramScriptable2); break;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1814 */         localObject1 = ScriptableObject.getProperty(paramScriptable1, paramString);
/* 1815 */         if (localObject1 != Scriptable.NOT_FOUND) {
/* 1816 */           localObject2 = paramScriptable1;
/* 1817 */           break;
/*      */         }
/*      */       }
/* 1820 */       paramScriptable1 = paramScriptable2;
/* 1821 */       paramScriptable2 = paramScriptable2.getParentScope();
/* 1822 */     }while (paramScriptable2 != null);
/* 1823 */     Object localObject1 = topScopeName(paramContext, paramScriptable1, paramString);
/* 1824 */     if (localObject1 == Scriptable.NOT_FOUND) {
/* 1825 */       if ((localObject3 == null) || (paramBoolean)) {
/* 1826 */         throw notFoundError(paramScriptable1, paramString);
/*      */       }
/*      */ 
/* 1832 */       localObject1 = localObject3.ecmaGet(paramContext, paramString);
/*      */     }
/*      */ 
/* 1835 */     localObject2 = paramScriptable1;
/*      */ 
/* 1840 */     if (paramBoolean) {
/* 1841 */       if (!(localObject1 instanceof Callable)) {
/* 1842 */         throw notFunctionError(localObject1, paramString);
/*      */       }
/* 1844 */       storeScriptable(paramContext, (Scriptable)localObject2);
/*      */     }
/*      */ 
/* 1847 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static Object topScopeName(Context paramContext, Scriptable paramScriptable, String paramString)
/*      */   {
/* 1853 */     if (paramContext.useDynamicScope) {
/* 1854 */       paramScriptable = checkDynamicScope(paramContext.topCallScope, paramScriptable);
/*      */     }
/* 1856 */     return ScriptableObject.getProperty(paramScriptable, paramString);
/*      */   }
/*      */ 
/*      */   public static Scriptable bind(Context paramContext, Scriptable paramScriptable, String paramString)
/*      */   {
/* 1875 */     Object localObject = null;
/* 1876 */     Scriptable localScriptable1 = paramScriptable.getParentScope();
/* 1877 */     if (localScriptable1 != null)
/*      */     {
/* 1879 */       while ((paramScriptable instanceof NativeWith)) {
/* 1880 */         Scriptable localScriptable2 = paramScriptable.getPrototype();
/* 1881 */         if ((localScriptable2 instanceof XMLObject)) {
/* 1882 */           XMLObject localXMLObject = (XMLObject)localScriptable2;
/* 1883 */           if (localXMLObject.ecmaHas(paramContext, paramString)) {
/* 1884 */             return localXMLObject;
/*      */           }
/* 1886 */           if (localObject == null) {
/* 1887 */             localObject = localXMLObject;
/*      */           }
/*      */         }
/* 1890 */         else if (ScriptableObject.hasProperty(localScriptable2, paramString)) {
/* 1891 */           return localScriptable2;
/*      */         }
/*      */ 
/* 1894 */         paramScriptable = localScriptable1;
/* 1895 */         localScriptable1 = localScriptable1.getParentScope();
/* 1896 */         if (localScriptable1 == null)
/*      */           break label133;
/*      */       }
/*      */       while (true)
/*      */       {
/* 1901 */         if (ScriptableObject.hasProperty(paramScriptable, paramString)) {
/* 1902 */           return paramScriptable;
/*      */         }
/* 1904 */         paramScriptable = localScriptable1;
/* 1905 */         localScriptable1 = localScriptable1.getParentScope();
/* 1906 */         if (localScriptable1 == null) {
/* 1907 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1912 */     label133: if (paramContext.useDynamicScope) {
/* 1913 */       paramScriptable = checkDynamicScope(paramContext.topCallScope, paramScriptable);
/*      */     }
/* 1915 */     if (ScriptableObject.hasProperty(paramScriptable, paramString)) {
/* 1916 */       return paramScriptable;
/*      */     }
/*      */ 
/* 1920 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Object setName(Scriptable paramScriptable1, Object paramObject, Context paramContext, Scriptable paramScriptable2, String paramString)
/*      */   {
/* 1926 */     if (paramScriptable1 != null) {
/* 1927 */       if ((paramScriptable1 instanceof XMLObject)) {
/* 1928 */         XMLObject localXMLObject = (XMLObject)paramScriptable1;
/* 1929 */         localXMLObject.ecmaPut(paramContext, paramString, paramObject);
/*      */       } else {
/* 1931 */         ScriptableObject.putProperty(paramScriptable1, paramString, paramObject);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1937 */       if ((paramContext.hasFeature(11)) || (paramContext.hasFeature(8)))
/*      */       {
/* 1940 */         Context.reportWarning(getMessage1("msg.assn.create.strict", paramString));
/*      */       }
/*      */ 
/* 1944 */       paramScriptable1 = ScriptableObject.getTopLevelScope(paramScriptable2);
/* 1945 */       if (paramContext.useDynamicScope) {
/* 1946 */         paramScriptable1 = checkDynamicScope(paramContext.topCallScope, paramScriptable1);
/*      */       }
/* 1948 */       paramScriptable1.put(paramString, paramScriptable1, paramObject);
/*      */     }
/* 1950 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public static Object strictSetName(Scriptable paramScriptable1, Object paramObject, Context paramContext, Scriptable paramScriptable2, String paramString)
/*      */   {
/*      */     Object localObject;
/* 1955 */     if (paramScriptable1 != null)
/*      */     {
/* 1962 */       if ((paramScriptable1 instanceof XMLObject)) {
/* 1963 */         localObject = (XMLObject)paramScriptable1;
/* 1964 */         ((XMLObject)localObject).ecmaPut(paramContext, paramString, paramObject);
/*      */       } else {
/* 1966 */         ScriptableObject.putProperty(paramScriptable1, paramString, paramObject);
/*      */       }
/*      */     }
/*      */     else {
/* 1970 */       localObject = new int[1];
/* 1971 */       String str = Context.getSourcePositionFromStack((int[])localObject);
/* 1972 */       throw new JavaScriptException(paramContext.newObject(paramScriptable2, "ReferenceError", new Object[] { paramString }), str, localObject[0]);
/*      */     }
/*      */ 
/* 1977 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public static Object setConst(Scriptable paramScriptable, Object paramObject, Context paramContext, String paramString)
/*      */   {
/* 1983 */     if ((paramScriptable instanceof XMLObject)) {
/* 1984 */       XMLObject localXMLObject = (XMLObject)paramScriptable;
/* 1985 */       localXMLObject.ecmaPut(paramContext, paramString, paramObject);
/*      */     } else {
/* 1987 */       ScriptableObject.putConstProperty(paramScriptable, paramString, paramObject);
/*      */     }
/* 1989 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public static Scriptable toIterator(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, boolean paramBoolean)
/*      */   {
/* 2026 */     if (ScriptableObject.hasProperty(paramScriptable2, "__iterator__"))
/*      */     {
/* 2029 */       Object localObject = ScriptableObject.getProperty(paramScriptable2, "__iterator__");
/*      */ 
/* 2031 */       if (!(localObject instanceof Callable)) {
/* 2032 */         throw typeError0("msg.invalid.iterator");
/*      */       }
/* 2034 */       Callable localCallable = (Callable)localObject;
/* 2035 */       Object[] arrayOfObject = { paramBoolean ? Boolean.TRUE : Boolean.FALSE };
/*      */ 
/* 2037 */       localObject = localCallable.call(paramContext, paramScriptable1, paramScriptable2, arrayOfObject);
/* 2038 */       if (!(localObject instanceof Scriptable)) {
/* 2039 */         throw typeError0("msg.iterator.primitive");
/*      */       }
/* 2041 */       return (Scriptable)localObject;
/*      */     }
/* 2043 */     return null;
/*      */   }
/*      */ 
/*      */   public static Object enumInit(Object paramObject, Context paramContext, boolean paramBoolean)
/*      */   {
/* 2049 */     return enumInit(paramObject, paramContext, paramBoolean ? 1 : 0);
/*      */   }
/*      */ 
/*      */   public static Object enumInit(Object paramObject, Context paramContext, int paramInt)
/*      */   {
/* 2062 */     IdEnumeration localIdEnumeration = new IdEnumeration(null);
/* 2063 */     localIdEnumeration.obj = toObjectOrNull(paramContext, paramObject);
/* 2064 */     if (localIdEnumeration.obj == null)
/*      */     {
/* 2067 */       return localIdEnumeration;
/*      */     }
/* 2069 */     localIdEnumeration.enumType = paramInt;
/* 2070 */     localIdEnumeration.iterator = null;
/* 2071 */     if ((paramInt != 3) && (paramInt != 4) && (paramInt != 5))
/*      */     {
/* 2075 */       localIdEnumeration.iterator = toIterator(paramContext, localIdEnumeration.obj.getParentScope(), localIdEnumeration.obj, paramInt == 0);
/*      */     }
/*      */ 
/* 2078 */     if (localIdEnumeration.iterator == null)
/*      */     {
/* 2081 */       enumChangeObject(localIdEnumeration);
/*      */     }
/*      */ 
/* 2084 */     return localIdEnumeration;
/*      */   }
/*      */ 
/*      */   public static void setEnumNumbers(Object paramObject, boolean paramBoolean) {
/* 2088 */     ((IdEnumeration)paramObject).enumNumbers = paramBoolean;
/*      */   }
/*      */ 
/*      */   public static Boolean enumNext(Object paramObject)
/*      */   {
/* 2093 */     IdEnumeration localIdEnumeration = (IdEnumeration)paramObject;
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 2094 */     if (localIdEnumeration.iterator != null) {
/* 2095 */       localObject1 = ScriptableObject.getProperty(localIdEnumeration.iterator, "next");
/* 2096 */       if (!(localObject1 instanceof Callable))
/* 2097 */         return Boolean.FALSE;
/* 2098 */       localObject2 = (Callable)localObject1;
/* 2099 */       Context localContext = Context.getContext();
/*      */       try {
/* 2101 */         localIdEnumeration.currentId = ((Callable)localObject2).call(localContext, localIdEnumeration.iterator.getParentScope(), localIdEnumeration.iterator, emptyArgs);
/*      */ 
/* 2103 */         return Boolean.TRUE;
/*      */       } catch (JavaScriptException localJavaScriptException) {
/* 2105 */         if ((localJavaScriptException.getValue() instanceof NativeIterator.StopIteration)) {
/* 2106 */           return Boolean.FALSE;
/*      */         }
/* 2108 */         throw localJavaScriptException;
/*      */       }
/*      */     }int i;
/*      */     do {
/*      */       do { do { while (true) { if (localIdEnumeration.obj == null) {
/* 2113 */               return Boolean.FALSE;
/*      */             }
/* 2115 */             if (localIdEnumeration.index != localIdEnumeration.ids.length) break;
/* 2116 */             localIdEnumeration.obj = localIdEnumeration.obj.getPrototype();
/* 2117 */             enumChangeObject(localIdEnumeration);
/*      */           }
/*      */ 
/* 2120 */           localObject1 = localIdEnumeration.ids[(localIdEnumeration.index++)]; }
/* 2121 */         while ((localIdEnumeration.used != null) && (localIdEnumeration.used.has(localObject1)));
/*      */ 
/* 2124 */         if (!(localObject1 instanceof String)) break;
/* 2125 */         localObject2 = (String)localObject1; }
/* 2126 */       while (!localIdEnumeration.obj.has((String)localObject2, localIdEnumeration.obj));
/*      */ 
/* 2128 */       localIdEnumeration.currentId = localObject2;
/* 2129 */       break;
/* 2130 */       i = ((Number)localObject1).intValue();
/* 2131 */     }while (!localIdEnumeration.obj.has(i, localIdEnumeration.obj));
/*      */ 
/* 2133 */     localIdEnumeration.currentId = (localIdEnumeration.enumNumbers ? Integer.valueOf(i) : String.valueOf(i));
/*      */ 
/* 2136 */     return Boolean.TRUE;
/*      */   }
/*      */ 
/*      */   public static Object enumId(Object paramObject, Context paramContext)
/*      */   {
/* 2142 */     IdEnumeration localIdEnumeration = (IdEnumeration)paramObject;
/* 2143 */     if (localIdEnumeration.iterator != null) {
/* 2144 */       return localIdEnumeration.currentId;
/*      */     }
/* 2146 */     switch (localIdEnumeration.enumType) {
/*      */     case 0:
/*      */     case 3:
/* 2149 */       return localIdEnumeration.currentId;
/*      */     case 1:
/*      */     case 4:
/* 2152 */       return enumValue(paramObject, paramContext);
/*      */     case 2:
/*      */     case 5:
/* 2155 */       Object[] arrayOfObject = { localIdEnumeration.currentId, enumValue(paramObject, paramContext) };
/* 2156 */       return paramContext.newArray(ScriptableObject.getTopLevelScope(localIdEnumeration.obj), arrayOfObject);
/*      */     }
/* 2158 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   public static Object enumValue(Object paramObject, Context paramContext)
/*      */   {
/* 2163 */     IdEnumeration localIdEnumeration = (IdEnumeration)paramObject;
/*      */ 
/* 2167 */     String str = toStringIdOrIndex(paramContext, localIdEnumeration.currentId);
/*      */     Object localObject;
/* 2168 */     if (str == null) {
/* 2169 */       int i = lastIndexResult(paramContext);
/* 2170 */       localObject = localIdEnumeration.obj.get(i, localIdEnumeration.obj);
/*      */     } else {
/* 2172 */       localObject = localIdEnumeration.obj.get(str, localIdEnumeration.obj);
/*      */     }
/*      */ 
/* 2175 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static void enumChangeObject(IdEnumeration paramIdEnumeration)
/*      */   {
/* 2180 */     Object[] arrayOfObject1 = null;
/* 2181 */     while (paramIdEnumeration.obj != null) {
/* 2182 */       arrayOfObject1 = paramIdEnumeration.obj.getIds();
/* 2183 */       if (arrayOfObject1.length != 0) {
/*      */         break;
/*      */       }
/* 2186 */       paramIdEnumeration.obj = paramIdEnumeration.obj.getPrototype();
/*      */     }
/* 2188 */     if ((paramIdEnumeration.obj != null) && (paramIdEnumeration.ids != null)) {
/* 2189 */       Object[] arrayOfObject2 = paramIdEnumeration.ids;
/* 2190 */       int i = arrayOfObject2.length;
/* 2191 */       if (paramIdEnumeration.used == null) {
/* 2192 */         paramIdEnumeration.used = new ObjToIntMap(i);
/*      */       }
/* 2194 */       for (int j = 0; j != i; j++) {
/* 2195 */         paramIdEnumeration.used.intern(arrayOfObject2[j]);
/*      */       }
/*      */     }
/* 2198 */     paramIdEnumeration.ids = arrayOfObject1;
/* 2199 */     paramIdEnumeration.index = 0;
/*      */   }
/*      */ 
/*      */   public static Callable getNameFunctionAndThis(String paramString, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 2213 */     Scriptable localScriptable1 = paramScriptable.getParentScope();
/* 2214 */     if (localScriptable1 == null) {
/* 2215 */       Object localObject = topScopeName(paramContext, paramScriptable, paramString);
/* 2216 */       if (!(localObject instanceof Callable)) {
/* 2217 */         if (localObject == Scriptable.NOT_FOUND) {
/* 2218 */           throw notFoundError(paramScriptable, paramString);
/*      */         }
/* 2220 */         throw notFunctionError(localObject, paramString);
/*      */       }
/*      */ 
/* 2224 */       Scriptable localScriptable2 = paramScriptable;
/* 2225 */       storeScriptable(paramContext, localScriptable2);
/* 2226 */       return (Callable)localObject;
/*      */     }
/*      */ 
/* 2230 */     return (Callable)nameOrFunction(paramContext, paramScriptable, localScriptable1, paramString, true);
/*      */   }
/*      */ 
/*      */   public static Callable getElemFunctionAndThis(Object paramObject1, Object paramObject2, Context paramContext)
/*      */   {
/* 2244 */     String str = toStringIdOrIndex(paramContext, paramObject2);
/* 2245 */     if (str != null) {
/* 2246 */       return getPropFunctionAndThis(paramObject1, str, paramContext);
/*      */     }
/* 2248 */     int i = lastIndexResult(paramContext);
/*      */ 
/* 2250 */     Object localObject1 = toObjectOrNull(paramContext, paramObject1);
/* 2251 */     if (localObject1 == null) {
/* 2252 */       throw undefCallError(paramObject1, String.valueOf(i));
/*      */     }
/*      */ 
/*      */     Object localObject2;
/*      */     while (true)
/*      */     {
/* 2258 */       localObject2 = ScriptableObject.getProperty((Scriptable)localObject1, i);
/* 2259 */       if (localObject2 != Scriptable.NOT_FOUND) {
/*      */         break;
/*      */       }
/* 2262 */       if (!(localObject1 instanceof XMLObject)) {
/*      */         break;
/*      */       }
/* 2265 */       XMLObject localXMLObject = (XMLObject)localObject1;
/* 2266 */       Scriptable localScriptable = localXMLObject.getExtraMethodSource(paramContext);
/* 2267 */       if (localScriptable == null) {
/*      */         break;
/*      */       }
/* 2270 */       localObject1 = localScriptable;
/*      */     }
/* 2272 */     if (!(localObject2 instanceof Callable)) {
/* 2273 */       throw notFunctionError(localObject2, paramObject2);
/*      */     }
/*      */ 
/* 2276 */     storeScriptable(paramContext, (Scriptable)localObject1);
/* 2277 */     return (Callable)localObject2;
/*      */   }
/*      */ 
/*      */   public static Callable getPropFunctionAndThis(Object paramObject, String paramString, Context paramContext)
/*      */   {
/* 2293 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject);
/* 2294 */     return getPropFunctionAndThisHelper(paramObject, paramString, paramContext, localScriptable);
/*      */   }
/*      */ 
/*      */   public static Callable getPropFunctionAndThis(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 2308 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject, paramScriptable);
/* 2309 */     return getPropFunctionAndThisHelper(paramObject, paramString, paramContext, localScriptable);
/*      */   }
/*      */ 
/*      */   private static Callable getPropFunctionAndThisHelper(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 2315 */     if (paramScriptable == null)
/* 2316 */       throw undefCallError(paramObject, paramString);
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     while (true)
/*      */     {
/* 2322 */       localObject1 = ScriptableObject.getProperty(paramScriptable, paramString);
/* 2323 */       if (localObject1 != Scriptable.NOT_FOUND) {
/*      */         break;
/*      */       }
/* 2326 */       if (!(paramScriptable instanceof XMLObject)) {
/*      */         break;
/*      */       }
/* 2329 */       localObject2 = (XMLObject)paramScriptable;
/* 2330 */       Scriptable localScriptable = ((XMLObject)localObject2).getExtraMethodSource(paramContext);
/* 2331 */       if (localScriptable == null) {
/*      */         break;
/*      */       }
/* 2334 */       paramScriptable = localScriptable;
/*      */     }
/*      */ 
/* 2337 */     if (!(localObject1 instanceof Callable)) {
/* 2338 */       localObject2 = ScriptableObject.getProperty(paramScriptable, "__noSuchMethod__");
/* 2339 */       if ((localObject2 instanceof Callable))
/* 2340 */         localObject1 = new NoSuchMethodShim((Callable)localObject2, paramString);
/*      */       else {
/* 2342 */         throw notFunctionError(paramScriptable, localObject1, paramString);
/*      */       }
/*      */     }
/* 2345 */     storeScriptable(paramContext, paramScriptable);
/* 2346 */     return (Callable)localObject1;
/*      */   }
/*      */ 
/*      */   public static Callable getValueFunctionAndThis(Object paramObject, Context paramContext)
/*      */   {
/* 2358 */     if (!(paramObject instanceof Callable)) {
/* 2359 */       throw notFunctionError(paramObject);
/*      */     }
/*      */ 
/* 2362 */     Callable localCallable = (Callable)paramObject;
/* 2363 */     Scriptable localScriptable = null;
/* 2364 */     if ((localCallable instanceof Scriptable)) {
/* 2365 */       localScriptable = ((Scriptable)localCallable).getParentScope();
/*      */     }
/* 2367 */     if (localScriptable == null) {
/* 2368 */       if (paramContext.topCallScope == null) throw new IllegalStateException();
/* 2369 */       localScriptable = paramContext.topCallScope;
/*      */     }
/* 2371 */     if ((localScriptable.getParentScope() != null) && 
/* 2372 */       (!(localScriptable instanceof NativeWith)))
/*      */     {
/* 2375 */       if ((localScriptable instanceof NativeCall))
/*      */       {
/* 2377 */         localScriptable = ScriptableObject.getTopLevelScope(localScriptable);
/*      */       }
/*      */     }
/* 2380 */     storeScriptable(paramContext, localScriptable);
/* 2381 */     return localCallable;
/*      */   }
/*      */ 
/*      */   public static Ref callRef(Callable paramCallable, Scriptable paramScriptable, Object[] paramArrayOfObject, Context paramContext)
/*      */   {
/* 2396 */     if ((paramCallable instanceof RefCallable)) {
/* 2397 */       localObject = (RefCallable)paramCallable;
/* 2398 */       Ref localRef = ((RefCallable)localObject).refCall(paramContext, paramScriptable, paramArrayOfObject);
/* 2399 */       if (localRef == null) {
/* 2400 */         throw new IllegalStateException(localObject.getClass().getName() + ".refCall() returned null");
/*      */       }
/* 2402 */       return localRef;
/*      */     }
/*      */ 
/* 2405 */     Object localObject = getMessage1("msg.no.ref.from.function", toString(paramCallable));
/*      */ 
/* 2407 */     throw constructError("ReferenceError", (String)localObject);
/*      */   }
/*      */ 
/*      */   public static Scriptable newObject(Object paramObject, Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/* 2418 */     if (!(paramObject instanceof Function)) {
/* 2419 */       throw notFunctionError(paramObject);
/*      */     }
/* 2421 */     Function localFunction = (Function)paramObject;
/* 2422 */     return localFunction.construct(paramContext, paramScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static Object callSpecial(Context paramContext, Callable paramCallable, Scriptable paramScriptable1, Object[] paramArrayOfObject, Scriptable paramScriptable2, Scriptable paramScriptable3, int paramInt1, String paramString, int paramInt2)
/*      */   {
/* 2431 */     if (paramInt1 == 1) {
/* 2432 */       if ((paramScriptable1.getParentScope() == null) && (NativeGlobal.isEvalFunction(paramCallable))) {
/* 2433 */         return evalSpecial(paramContext, paramScriptable2, paramScriptable3, paramArrayOfObject, paramString, paramInt2);
/*      */       }
/*      */     }
/* 2436 */     else if (paramInt1 == 2) {
/* 2437 */       if (NativeWith.isWithFunction(paramCallable)) {
/* 2438 */         throw Context.reportRuntimeError1("msg.only.from.new", "With");
/*      */       }
/*      */     }
/*      */     else {
/* 2442 */       throw Kit.codeBug();
/*      */     }
/*      */ 
/* 2445 */     return paramCallable.call(paramContext, paramScriptable2, paramScriptable1, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static Object newSpecial(Context paramContext, Object paramObject, Object[] paramArrayOfObject, Scriptable paramScriptable, int paramInt)
/*      */   {
/* 2452 */     if (paramInt == 1) {
/* 2453 */       if (NativeGlobal.isEvalFunction(paramObject))
/* 2454 */         throw typeError1("msg.not.ctor", "eval");
/*      */     }
/* 2456 */     else if (paramInt == 2) {
/* 2457 */       if (NativeWith.isWithFunction(paramObject))
/* 2458 */         return NativeWith.newWithSpecial(paramContext, paramScriptable, paramArrayOfObject);
/*      */     }
/*      */     else {
/* 2461 */       throw Kit.codeBug();
/*      */     }
/*      */ 
/* 2464 */     return newObject(paramObject, paramContext, paramScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static Object applyOrCall(boolean paramBoolean, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/* 2476 */     int i = paramArrayOfObject.length;
/* 2477 */     Callable localCallable = getCallable(paramScriptable2);
/*      */ 
/* 2479 */     Scriptable localScriptable = null;
/* 2480 */     if (i != 0) {
/* 2481 */       localScriptable = toObjectOrNull(paramContext, paramArrayOfObject[0]);
/*      */     }
/* 2483 */     if (localScriptable == null)
/*      */     {
/* 2485 */       localScriptable = getTopCallScope(paramContext);
/*      */     }
/*      */     Object[] arrayOfObject;
/* 2489 */     if (paramBoolean)
/*      */     {
/* 2491 */       arrayOfObject = i <= 1 ? emptyArgs : getApplyArguments(paramContext, paramArrayOfObject[1]);
/*      */     }
/* 2495 */     else if (i <= 1) {
/* 2496 */       arrayOfObject = emptyArgs;
/*      */     } else {
/* 2498 */       arrayOfObject = new Object[i - 1];
/* 2499 */       System.arraycopy(paramArrayOfObject, 1, arrayOfObject, 0, i - 1);
/*      */     }
/*      */ 
/* 2503 */     return localCallable.call(paramContext, paramScriptable1, localScriptable, arrayOfObject);
/*      */   }
/*      */ 
/*      */   static Object[] getApplyArguments(Context paramContext, Object paramObject)
/*      */   {
/* 2508 */     if ((paramObject == null) || (paramObject == Undefined.instance))
/* 2509 */       return emptyArgs;
/* 2510 */     if (((paramObject instanceof NativeArray)) || ((paramObject instanceof Arguments))) {
/* 2511 */       return paramContext.getElements((Scriptable)paramObject);
/*      */     }
/* 2513 */     throw typeError0("msg.arg.isnt.array");
/*      */   }
/*      */ 
/*      */   static Callable getCallable(Scriptable paramScriptable)
/*      */   {
/*      */     Callable localCallable;
/* 2520 */     if ((paramScriptable instanceof Callable)) {
/* 2521 */       localCallable = (Callable)paramScriptable;
/*      */     } else {
/* 2523 */       Object localObject = paramScriptable.getDefaultValue(FunctionClass);
/* 2524 */       if (!(localObject instanceof Callable)) {
/* 2525 */         throw notFunctionError(localObject, paramScriptable);
/*      */       }
/* 2527 */       localCallable = (Callable)localObject;
/*      */     }
/* 2529 */     return localCallable;
/*      */   }
/*      */ 
/*      */   public static Object evalSpecial(Context paramContext, Scriptable paramScriptable, Object paramObject, Object[] paramArrayOfObject, String paramString, int paramInt)
/*      */   {
/* 2541 */     if (paramArrayOfObject.length < 1)
/* 2542 */       return Undefined.instance;
/* 2543 */     Object localObject1 = paramArrayOfObject[0];
/* 2544 */     if (!(localObject1 instanceof String)) {
/* 2545 */       if ((paramContext.hasFeature(11)) || (paramContext.hasFeature(9)))
/*      */       {
/* 2548 */         throw Context.reportRuntimeError0("msg.eval.nonstring.strict");
/*      */       }
/* 2550 */       localObject2 = getMessage0("msg.eval.nonstring");
/* 2551 */       Context.reportWarning((String)localObject2);
/* 2552 */       return localObject1;
/*      */     }
/* 2554 */     if (paramString == null) {
/* 2555 */       localObject2 = new int[1];
/* 2556 */       paramString = Context.getSourcePositionFromStack((int[])localObject2);
/* 2557 */       if (paramString != null)
/* 2558 */         paramInt = localObject2[0];
/*      */       else {
/* 2560 */         paramString = "";
/*      */       }
/*      */     }
/* 2563 */     Object localObject2 = makeUrlForGeneratedScript(true, paramString, paramInt);
/*      */ 
/* 2567 */     ErrorReporter localErrorReporter = DefaultErrorReporter.forEval(paramContext.getErrorReporter());
/*      */ 
/* 2569 */     Evaluator localEvaluator = Context.createInterpreter();
/* 2570 */     if (localEvaluator == null) {
/* 2571 */       throw new JavaScriptException("Interpreter not present", paramString, paramInt);
/*      */     }
/*      */ 
/* 2577 */     Script localScript = paramContext.compileString((String)localObject1, localEvaluator, localErrorReporter, (String)localObject2, 1, null);
/*      */ 
/* 2579 */     localEvaluator.setEvalScriptFlag(localScript);
/* 2580 */     Callable localCallable = (Callable)localScript;
/* 2581 */     return localCallable.call(paramContext, paramScriptable, (Scriptable)paramObject, emptyArgs);
/*      */   }
/*      */ 
/*      */   public static String typeof(Object paramObject)
/*      */   {
/* 2589 */     if (paramObject == null)
/* 2590 */       return "object";
/* 2591 */     if (paramObject == Undefined.instance)
/* 2592 */       return "undefined";
/* 2593 */     if ((paramObject instanceof ScriptableObject))
/* 2594 */       return ((ScriptableObject)paramObject).getTypeOf();
/* 2595 */     if ((paramObject instanceof Scriptable))
/* 2596 */       return (paramObject instanceof Callable) ? "function" : "object";
/* 2597 */     if ((paramObject instanceof String))
/* 2598 */       return "string";
/* 2599 */     if ((paramObject instanceof Number))
/* 2600 */       return "number";
/* 2601 */     if ((paramObject instanceof Boolean))
/* 2602 */       return "boolean";
/* 2603 */     throw errorWithClassName("msg.invalid.type", paramObject);
/*      */   }
/*      */ 
/*      */   public static String typeofName(Scriptable paramScriptable, String paramString)
/*      */   {
/* 2611 */     Context localContext = Context.getContext();
/* 2612 */     Scriptable localScriptable = bind(localContext, paramScriptable, paramString);
/* 2613 */     if (localScriptable == null)
/* 2614 */       return "undefined";
/* 2615 */     return typeof(getObjectProp(localScriptable, paramString, localContext));
/*      */   }
/*      */ 
/*      */   public static Object add(Object paramObject1, Object paramObject2, Context paramContext)
/*      */   {
/* 2632 */     if (((paramObject1 instanceof Number)) && ((paramObject2 instanceof Number)))
/* 2633 */       return wrapNumber(((Number)paramObject1).doubleValue() + ((Number)paramObject2).doubleValue());
/*      */     Object localObject;
/* 2636 */     if ((paramObject1 instanceof XMLObject)) {
/* 2637 */       localObject = ((XMLObject)paramObject1).addValues(paramContext, true, paramObject2);
/* 2638 */       if (localObject != Scriptable.NOT_FOUND) {
/* 2639 */         return localObject;
/*      */       }
/*      */     }
/* 2642 */     if ((paramObject2 instanceof XMLObject)) {
/* 2643 */       localObject = ((XMLObject)paramObject2).addValues(paramContext, false, paramObject1);
/* 2644 */       if (localObject != Scriptable.NOT_FOUND) {
/* 2645 */         return localObject;
/*      */       }
/*      */     }
/* 2648 */     if ((paramObject1 instanceof Scriptable))
/* 2649 */       paramObject1 = ((Scriptable)paramObject1).getDefaultValue(null);
/* 2650 */     if ((paramObject2 instanceof Scriptable))
/* 2651 */       paramObject2 = ((Scriptable)paramObject2).getDefaultValue(null);
/* 2652 */     if ((!(paramObject1 instanceof String)) && (!(paramObject2 instanceof String))) {
/* 2653 */       if (((paramObject1 instanceof Number)) && ((paramObject2 instanceof Number))) {
/* 2654 */         return wrapNumber(((Number)paramObject1).doubleValue() + ((Number)paramObject2).doubleValue());
/*      */       }
/*      */ 
/* 2657 */       return wrapNumber(toNumber(paramObject1) + toNumber(paramObject2));
/* 2658 */     }return toString(paramObject1).concat(toString(paramObject2));
/*      */   }
/*      */ 
/*      */   public static String add(String paramString, Object paramObject) {
/* 2662 */     return paramString.concat(toString(paramObject));
/*      */   }
/*      */ 
/*      */   public static String add(Object paramObject, String paramString) {
/* 2666 */     return toString(paramObject).concat(paramString);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Object nameIncrDecr(Scriptable paramScriptable, String paramString, int paramInt)
/*      */   {
/* 2675 */     return nameIncrDecr(paramScriptable, paramString, Context.getContext(), paramInt);
/*      */   }
/*      */ 
/*      */   public static Object nameIncrDecr(Scriptable paramScriptable, String paramString, Context paramContext, int paramInt)
/*      */   {
/*      */     Scriptable localScriptable;
/*      */     Object localObject;
/*      */     do
/*      */     {
/* 2685 */       if ((paramContext.useDynamicScope) && (paramScriptable.getParentScope() == null)) {
/* 2686 */         paramScriptable = checkDynamicScope(paramContext.topCallScope, paramScriptable);
/*      */       }
/* 2688 */       localScriptable = paramScriptable;
/*      */       do {
/* 2690 */         localObject = localScriptable.get(paramString, paramScriptable);
/* 2691 */         if (localObject != Scriptable.NOT_FOUND) {
/*      */           break;
/*      */         }
/* 2694 */         localScriptable = localScriptable.getPrototype();
/* 2695 */       }while (localScriptable != null);
/* 2696 */       paramScriptable = paramScriptable.getParentScope();
/* 2697 */     }while (paramScriptable != null);
/* 2698 */     throw notFoundError(paramScriptable, paramString);
/*      */ 
/* 2700 */     return doScriptableIncrDecr(localScriptable, paramString, paramScriptable, localObject, paramInt);
/*      */   }
/*      */ 
/*      */   public static Object propIncrDecr(Object paramObject, String paramString, Context paramContext, int paramInt)
/*      */   {
/* 2707 */     Scriptable localScriptable1 = toObjectOrNull(paramContext, paramObject);
/* 2708 */     if (localScriptable1 == null) {
/* 2709 */       throw undefReadError(paramObject, paramString);
/*      */     }
/*      */ 
/* 2712 */     Scriptable localScriptable2 = localScriptable1;
/*      */     Object localObject;
/*      */     do {
/* 2716 */       localObject = localScriptable2.get(paramString, localScriptable1);
/* 2717 */       if (localObject != Scriptable.NOT_FOUND) {
/*      */         break;
/*      */       }
/* 2720 */       localScriptable2 = localScriptable2.getPrototype();
/* 2721 */     }while (localScriptable2 != null);
/* 2722 */     localScriptable1.put(paramString, localScriptable1, NaNobj);
/* 2723 */     return NaNobj;
/*      */ 
/* 2725 */     return doScriptableIncrDecr(localScriptable2, paramString, localScriptable1, localObject, paramInt);
/*      */   }
/*      */ 
/*      */   private static Object doScriptableIncrDecr(Scriptable paramScriptable1, String paramString, Scriptable paramScriptable2, Object paramObject, int paramInt)
/*      */   {
/* 2735 */     int i = (paramInt & 0x2) != 0 ? 1 : 0;
/*      */     double d;
/* 2737 */     if ((paramObject instanceof Number)) {
/* 2738 */       d = ((Number)paramObject).doubleValue();
/*      */     } else {
/* 2740 */       d = toNumber(paramObject);
/* 2741 */       if (i != 0)
/*      */       {
/* 2743 */         paramObject = wrapNumber(d);
/*      */       }
/*      */     }
/* 2746 */     if ((paramInt & 0x1) == 0)
/* 2747 */       d += 1.0D;
/*      */     else {
/* 2749 */       d -= 1.0D;
/*      */     }
/* 2751 */     Number localNumber = wrapNumber(d);
/* 2752 */     paramScriptable1.put(paramString, paramScriptable2, localNumber);
/* 2753 */     if (i != 0) {
/* 2754 */       return paramObject;
/*      */     }
/* 2756 */     return localNumber;
/*      */   }
/*      */ 
/*      */   public static Object elemIncrDecr(Object paramObject1, Object paramObject2, Context paramContext, int paramInt)
/*      */   {
/* 2763 */     Object localObject = getObjectElem(paramObject1, paramObject2, paramContext);
/* 2764 */     int i = (paramInt & 0x2) != 0 ? 1 : 0;
/*      */     double d;
/* 2766 */     if ((localObject instanceof Number)) {
/* 2767 */       d = ((Number)localObject).doubleValue();
/*      */     } else {
/* 2769 */       d = toNumber(localObject);
/* 2770 */       if (i != 0)
/*      */       {
/* 2772 */         localObject = wrapNumber(d);
/*      */       }
/*      */     }
/* 2775 */     if ((paramInt & 0x1) == 0)
/* 2776 */       d += 1.0D;
/*      */     else {
/* 2778 */       d -= 1.0D;
/*      */     }
/* 2780 */     Number localNumber = wrapNumber(d);
/* 2781 */     setObjectElem(paramObject1, paramObject2, localNumber, paramContext);
/* 2782 */     if (i != 0) {
/* 2783 */       return localObject;
/*      */     }
/* 2785 */     return localNumber;
/*      */   }
/*      */ 
/*      */   public static Object refIncrDecr(Ref paramRef, Context paramContext, int paramInt)
/*      */   {
/* 2791 */     Object localObject = paramRef.get(paramContext);
/* 2792 */     int i = (paramInt & 0x2) != 0 ? 1 : 0;
/*      */     double d;
/* 2794 */     if ((localObject instanceof Number)) {
/* 2795 */       d = ((Number)localObject).doubleValue();
/*      */     } else {
/* 2797 */       d = toNumber(localObject);
/* 2798 */       if (i != 0)
/*      */       {
/* 2800 */         localObject = wrapNumber(d);
/*      */       }
/*      */     }
/* 2803 */     if ((paramInt & 0x1) == 0)
/* 2804 */       d += 1.0D;
/*      */     else {
/* 2806 */       d -= 1.0D;
/*      */     }
/* 2808 */     Number localNumber = wrapNumber(d);
/* 2809 */     paramRef.set(paramContext, localNumber);
/* 2810 */     if (i != 0) {
/* 2811 */       return localObject;
/*      */     }
/* 2813 */     return localNumber;
/*      */   }
/*      */ 
/*      */   public static Object toPrimitive(Object paramObject)
/*      */   {
/* 2818 */     return toPrimitive(paramObject, null);
/*      */   }
/*      */ 
/*      */   public static Object toPrimitive(Object paramObject, Class<?> paramClass)
/*      */   {
/* 2823 */     if (!(paramObject instanceof Scriptable)) {
/* 2824 */       return paramObject;
/*      */     }
/* 2826 */     Scriptable localScriptable = (Scriptable)paramObject;
/* 2827 */     Object localObject = localScriptable.getDefaultValue(paramClass);
/* 2828 */     if ((localObject instanceof Scriptable))
/* 2829 */       throw typeError0("msg.bad.default.value");
/* 2830 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static boolean eq(Object paramObject1, Object paramObject2)
/*      */   {
/* 2840 */     if ((paramObject1 == null) || (paramObject1 == Undefined.instance)) {
/* 2841 */       if ((paramObject2 == null) || (paramObject2 == Undefined.instance)) {
/* 2842 */         return true;
/*      */       }
/* 2844 */       if ((paramObject2 instanceof ScriptableObject)) {
/* 2845 */         Object localObject1 = ((ScriptableObject)paramObject2).equivalentValues(paramObject1);
/* 2846 */         if (localObject1 != Scriptable.NOT_FOUND) {
/* 2847 */           return ((Boolean)localObject1).booleanValue();
/*      */         }
/*      */       }
/* 2850 */       return false;
/* 2851 */     }if ((paramObject1 instanceof Number))
/* 2852 */       return eqNumber(((Number)paramObject1).doubleValue(), paramObject2);
/* 2853 */     if ((paramObject1 instanceof String))
/* 2854 */       return eqString((String)paramObject1, paramObject2);
/*      */     Object localObject3;
/* 2855 */     if ((paramObject1 instanceof Boolean)) {
/* 2856 */       boolean bool = ((Boolean)paramObject1).booleanValue();
/* 2857 */       if ((paramObject2 instanceof Boolean)) {
/* 2858 */         return bool == ((Boolean)paramObject2).booleanValue();
/*      */       }
/* 2860 */       if ((paramObject2 instanceof ScriptableObject)) {
/* 2861 */         localObject3 = ((ScriptableObject)paramObject2).equivalentValues(paramObject1);
/* 2862 */         if (localObject3 != Scriptable.NOT_FOUND) {
/* 2863 */           return ((Boolean)localObject3).booleanValue();
/*      */         }
/*      */       }
/* 2866 */       return eqNumber(bool ? 1.0D : 0.0D, paramObject2);
/* 2867 */     }if ((paramObject1 instanceof Scriptable))
/*      */     {
/*      */       Object localObject2;
/* 2868 */       if ((paramObject2 instanceof Scriptable)) {
/* 2869 */         if (paramObject1 == paramObject2) {
/* 2870 */           return true;
/*      */         }
/* 2872 */         if ((paramObject1 instanceof ScriptableObject)) {
/* 2873 */           localObject2 = ((ScriptableObject)paramObject1).equivalentValues(paramObject2);
/* 2874 */           if (localObject2 != Scriptable.NOT_FOUND) {
/* 2875 */             return ((Boolean)localObject2).booleanValue();
/*      */           }
/*      */         }
/* 2878 */         if ((paramObject2 instanceof ScriptableObject)) {
/* 2879 */           localObject2 = ((ScriptableObject)paramObject2).equivalentValues(paramObject1);
/* 2880 */           if (localObject2 != Scriptable.NOT_FOUND) {
/* 2881 */             return ((Boolean)localObject2).booleanValue();
/*      */           }
/*      */         }
/* 2884 */         if (((paramObject1 instanceof Wrapper)) && ((paramObject2 instanceof Wrapper)))
/*      */         {
/* 2887 */           localObject2 = ((Wrapper)paramObject1).unwrap();
/* 2888 */           localObject3 = ((Wrapper)paramObject2).unwrap();
/* 2889 */           return (localObject2 == localObject3) || ((isPrimitive(localObject2)) && (isPrimitive(localObject3)) && (eq(localObject2, localObject3)));
/*      */         }
/*      */ 
/* 2894 */         return false;
/* 2895 */       }if ((paramObject2 instanceof Boolean)) {
/* 2896 */         if ((paramObject1 instanceof ScriptableObject)) {
/* 2897 */           localObject2 = ((ScriptableObject)paramObject1).equivalentValues(paramObject2);
/* 2898 */           if (localObject2 != Scriptable.NOT_FOUND) {
/* 2899 */             return ((Boolean)localObject2).booleanValue();
/*      */           }
/*      */         }
/* 2902 */         double d = ((Boolean)paramObject2).booleanValue() ? 1.0D : 0.0D;
/* 2903 */         return eqNumber(d, paramObject1);
/* 2904 */       }if ((paramObject2 instanceof Number))
/* 2905 */         return eqNumber(((Number)paramObject2).doubleValue(), paramObject1);
/* 2906 */       if ((paramObject2 instanceof String)) {
/* 2907 */         return eqString((String)paramObject2, paramObject1);
/*      */       }
/*      */ 
/* 2910 */       return false;
/*      */     }
/* 2912 */     warnAboutNonJSObject(paramObject1);
/* 2913 */     return paramObject1 == paramObject2;
/*      */   }
/*      */ 
/*      */   public static boolean isPrimitive(Object paramObject)
/*      */   {
/* 2918 */     return (paramObject == null) || (paramObject == Undefined.instance) || ((paramObject instanceof Number)) || ((paramObject instanceof String)) || ((paramObject instanceof Boolean));
/*      */   }
/*      */ 
/*      */   static boolean eqNumber(double paramDouble, Object paramObject)
/*      */   {
/*      */     while (true)
/*      */     {
/* 2926 */       if ((paramObject == null) || (paramObject == Undefined.instance))
/* 2927 */         return false;
/* 2928 */       if ((paramObject instanceof Number))
/* 2929 */         return paramDouble == ((Number)paramObject).doubleValue();
/* 2930 */       if ((paramObject instanceof String))
/* 2931 */         return paramDouble == toNumber(paramObject);
/* 2932 */       if ((paramObject instanceof Boolean))
/* 2933 */         return paramDouble == (((Boolean)paramObject).booleanValue() ? 1.0D : 0.0D);
/* 2934 */       if (!(paramObject instanceof Scriptable)) break;
/* 2935 */       if ((paramObject instanceof ScriptableObject)) {
/* 2936 */         Number localNumber = wrapNumber(paramDouble);
/* 2937 */         Object localObject = ((ScriptableObject)paramObject).equivalentValues(localNumber);
/* 2938 */         if (localObject != Scriptable.NOT_FOUND) {
/* 2939 */           return ((Boolean)localObject).booleanValue();
/*      */         }
/*      */       }
/* 2942 */       paramObject = toPrimitive(paramObject);
/*      */     }
/* 2944 */     warnAboutNonJSObject(paramObject);
/* 2945 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean eqString(String paramString, Object paramObject)
/*      */   {
/*      */     while (true)
/*      */     {
/* 2953 */       if ((paramObject == null) || (paramObject == Undefined.instance))
/* 2954 */         return false;
/* 2955 */       if ((paramObject instanceof String))
/* 2956 */         return paramString.equals(paramObject);
/* 2957 */       if ((paramObject instanceof Number))
/* 2958 */         return toNumber(paramString) == ((Number)paramObject).doubleValue();
/* 2959 */       if ((paramObject instanceof Boolean))
/* 2960 */         return toNumber(paramString) == (((Boolean)paramObject).booleanValue() ? 1.0D : 0.0D);
/* 2961 */       if (!(paramObject instanceof Scriptable)) break;
/* 2962 */       if ((paramObject instanceof ScriptableObject)) {
/* 2963 */         Object localObject = ((ScriptableObject)paramObject).equivalentValues(paramString);
/* 2964 */         if (localObject != Scriptable.NOT_FOUND) {
/* 2965 */           return ((Boolean)localObject).booleanValue();
/*      */         }
/*      */       }
/* 2968 */       paramObject = toPrimitive(paramObject);
/*      */     }
/*      */ 
/* 2971 */     warnAboutNonJSObject(paramObject);
/* 2972 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean shallowEq(Object paramObject1, Object paramObject2)
/*      */   {
/* 2978 */     if (paramObject1 == paramObject2) {
/* 2979 */       if (!(paramObject1 instanceof Number)) {
/* 2980 */         return true;
/*      */       }
/*      */ 
/* 2983 */       double d = ((Number)paramObject1).doubleValue();
/* 2984 */       return d == d;
/*      */     }
/* 2986 */     if ((paramObject1 == null) || (paramObject1 == Undefined.instance))
/* 2987 */       return false;
/* 2988 */     if ((paramObject1 instanceof Number)) {
/* 2989 */       if ((paramObject2 instanceof Number))
/* 2990 */         return ((Number)paramObject1).doubleValue() == ((Number)paramObject2).doubleValue();
/*      */     }
/* 2992 */     else if ((paramObject1 instanceof String)) {
/* 2993 */       if ((paramObject2 instanceof String))
/* 2994 */         return paramObject1.equals(paramObject2);
/*      */     }
/* 2996 */     else if ((paramObject1 instanceof Boolean)) {
/* 2997 */       if ((paramObject2 instanceof Boolean))
/* 2998 */         return paramObject1.equals(paramObject2);
/*      */     }
/* 3000 */     else if ((paramObject1 instanceof Scriptable)) {
/* 3001 */       if (((paramObject1 instanceof Wrapper)) && ((paramObject2 instanceof Wrapper)))
/* 3002 */         return ((Wrapper)paramObject1).unwrap() == ((Wrapper)paramObject2).unwrap();
/*      */     }
/*      */     else {
/* 3005 */       warnAboutNonJSObject(paramObject1);
/* 3006 */       return paramObject1 == paramObject2;
/*      */     }
/* 3008 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean instanceOf(Object paramObject1, Object paramObject2, Context paramContext)
/*      */   {
/* 3019 */     if (!(paramObject2 instanceof Scriptable)) {
/* 3020 */       throw typeError0("msg.instanceof.not.object");
/*      */     }
/*      */ 
/* 3024 */     if (!(paramObject1 instanceof Scriptable)) {
/* 3025 */       return false;
/*      */     }
/* 3027 */     return ((Scriptable)paramObject2).hasInstance((Scriptable)paramObject1);
/*      */   }
/*      */ 
/*      */   public static boolean jsDelegatesTo(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*      */   {
/* 3036 */     Scriptable localScriptable = paramScriptable1.getPrototype();
/*      */ 
/* 3038 */     while (localScriptable != null) {
/* 3039 */       if (localScriptable.equals(paramScriptable2)) return true;
/* 3040 */       localScriptable = localScriptable.getPrototype();
/*      */     }
/*      */ 
/* 3043 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean in(Object paramObject1, Object paramObject2, Context paramContext)
/*      */   {
/* 3062 */     if (!(paramObject2 instanceof Scriptable)) {
/* 3063 */       throw typeError0("msg.instanceof.not.object");
/*      */     }
/*      */ 
/* 3066 */     return hasObjectElem((Scriptable)paramObject2, paramObject1, paramContext);
/*      */   }
/*      */ 
/*      */   public static boolean cmp_LT(Object paramObject1, Object paramObject2)
/*      */   {
/*      */     double d1;
/*      */     double d2;
/* 3072 */     if (((paramObject1 instanceof Number)) && ((paramObject2 instanceof Number))) {
/* 3073 */       d1 = ((Number)paramObject1).doubleValue();
/* 3074 */       d2 = ((Number)paramObject2).doubleValue();
/*      */     } else {
/* 3076 */       if ((paramObject1 instanceof Scriptable))
/* 3077 */         paramObject1 = ((Scriptable)paramObject1).getDefaultValue(NumberClass);
/* 3078 */       if ((paramObject2 instanceof Scriptable))
/* 3079 */         paramObject2 = ((Scriptable)paramObject2).getDefaultValue(NumberClass);
/* 3080 */       if (((paramObject1 instanceof String)) && ((paramObject2 instanceof String))) {
/* 3081 */         return ((String)paramObject1).compareTo((String)paramObject2) < 0;
/*      */       }
/* 3083 */       d1 = toNumber(paramObject1);
/* 3084 */       d2 = toNumber(paramObject2);
/*      */     }
/* 3086 */     return d1 < d2;
/*      */   }
/*      */ 
/*      */   public static boolean cmp_LE(Object paramObject1, Object paramObject2)
/*      */   {
/*      */     double d1;
/*      */     double d2;
/* 3092 */     if (((paramObject1 instanceof Number)) && ((paramObject2 instanceof Number))) {
/* 3093 */       d1 = ((Number)paramObject1).doubleValue();
/* 3094 */       d2 = ((Number)paramObject2).doubleValue();
/*      */     } else {
/* 3096 */       if ((paramObject1 instanceof Scriptable))
/* 3097 */         paramObject1 = ((Scriptable)paramObject1).getDefaultValue(NumberClass);
/* 3098 */       if ((paramObject2 instanceof Scriptable))
/* 3099 */         paramObject2 = ((Scriptable)paramObject2).getDefaultValue(NumberClass);
/* 3100 */       if (((paramObject1 instanceof String)) && ((paramObject2 instanceof String))) {
/* 3101 */         return ((String)paramObject1).compareTo((String)paramObject2) <= 0;
/*      */       }
/* 3103 */       d1 = toNumber(paramObject1);
/* 3104 */       d2 = toNumber(paramObject2);
/*      */     }
/* 3106 */     return d1 <= d2;
/*      */   }
/*      */ 
/*      */   public static ScriptableObject getGlobal(Context paramContext)
/*      */   {
/* 3115 */     Class localClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.tools.shell.Global");
/* 3116 */     if (localClass != null) {
/*      */       try {
/* 3118 */         Class[] arrayOfClass = { ContextClass };
/* 3119 */         Constructor localConstructor = localClass.getConstructor(arrayOfClass);
/* 3120 */         Object[] arrayOfObject = { paramContext };
/* 3121 */         return (ScriptableObject)localConstructor.newInstance(arrayOfObject);
/*      */       }
/*      */       catch (RuntimeException localRuntimeException) {
/* 3124 */         throw localRuntimeException;
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/* 3130 */     return new ImporterTopLevel(paramContext);
/*      */   }
/*      */ 
/*      */   public static boolean hasTopCall(Context paramContext)
/*      */   {
/* 3135 */     return paramContext.topCallScope != null;
/*      */   }
/*      */ 
/*      */   public static Scriptable getTopCallScope(Context paramContext)
/*      */   {
/* 3140 */     Scriptable localScriptable = paramContext.topCallScope;
/* 3141 */     if (localScriptable == null) {
/* 3142 */       throw new IllegalStateException();
/*      */     }
/* 3144 */     return localScriptable;
/*      */   }
/*      */ 
/*      */   public static Object doTopCall(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */   {
/* 3151 */     if (paramScriptable1 == null)
/* 3152 */       throw new IllegalArgumentException();
/* 3153 */     if (paramContext.topCallScope != null) throw new IllegalStateException(); 
/*      */ paramContext.topCallScope = ScriptableObject.getTopLevelScope(paramScriptable1);
/* 3157 */     paramContext.useDynamicScope = paramContext.hasFeature(7);
/* 3158 */     ContextFactory localContextFactory = paramContext.getFactory();
/*      */     Object localObject1;
/*      */     try { localObject1 = localContextFactory.doTopCall(paramCallable, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */     } finally {
/* 3162 */       paramContext.topCallScope = null;
/*      */ 
/* 3164 */       paramContext.cachedXMLLib = null;
/*      */ 
/* 3166 */       if (paramContext.currentActivationCall != null)
/*      */       {
/* 3169 */         throw new IllegalStateException();
/*      */       }
/*      */     }
/* 3172 */     return localObject1;
/*      */   }
/*      */ 
/*      */   static Scriptable checkDynamicScope(Scriptable paramScriptable1, Scriptable paramScriptable2)
/*      */   {
/* 3185 */     if (paramScriptable1 == paramScriptable2) {
/* 3186 */       return paramScriptable1;
/*      */     }
/* 3188 */     Scriptable localScriptable = paramScriptable1;
/*      */     do {
/* 3190 */       localScriptable = localScriptable.getPrototype();
/* 3191 */       if (localScriptable == paramScriptable2)
/* 3192 */         return paramScriptable1;
/*      */     }
/* 3194 */     while (localScriptable != null);
/* 3195 */     return paramScriptable2;
/*      */   }
/*      */ 
/*      */   public static void addInstructionCount(Context paramContext, int paramInt)
/*      */   {
/* 3202 */     paramContext.instructionCount += paramInt;
/* 3203 */     if (paramContext.instructionCount > paramContext.instructionThreshold)
/*      */     {
/* 3205 */       paramContext.observeInstructionCount(paramContext.instructionCount);
/* 3206 */       paramContext.instructionCount = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void initScript(NativeFunction paramNativeFunction, Scriptable paramScriptable1, Context paramContext, Scriptable paramScriptable2, boolean paramBoolean)
/*      */   {
/* 3214 */     if (paramContext.topCallScope == null) {
/* 3215 */       throw new IllegalStateException();
/*      */     }
/* 3217 */     int i = paramNativeFunction.getParamAndVarCount();
/*      */     Scriptable localScriptable;
/*      */     int j;
/* 3218 */     if (i != 0)
/*      */     {
/* 3220 */       localScriptable = paramScriptable2;
/*      */ 
/* 3223 */       while ((localScriptable instanceof NativeWith)) {
/* 3224 */         localScriptable = localScriptable.getParentScope();
/*      */       }
/*      */ 
/* 3227 */       for (j = i; j-- != 0; ) {
/* 3228 */         String str = paramNativeFunction.getParamOrVarName(j);
/* 3229 */         boolean bool = paramNativeFunction.getParamOrVarConst(j);
/*      */ 
/* 3232 */         if (!ScriptableObject.hasProperty(paramScriptable2, str)) {
/* 3233 */           if (!paramBoolean)
/*      */           {
/* 3235 */             if (bool)
/* 3236 */               ScriptableObject.defineConstProperty(localScriptable, str);
/*      */             else {
/* 3238 */               ScriptableObject.defineProperty(localScriptable, str, Undefined.instance, 4);
/*      */             }
/*      */           }
/*      */           else
/* 3242 */             localScriptable.put(str, localScriptable, Undefined.instance);
/*      */         }
/*      */         else
/* 3245 */           ScriptableObject.redefineProperty(paramScriptable2, str, bool);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Scriptable createFunctionActivation(NativeFunction paramNativeFunction, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/* 3255 */     return new NativeCall(paramNativeFunction, paramScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static void enterActivationFunction(Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 3262 */     if (paramContext.topCallScope == null)
/* 3263 */       throw new IllegalStateException();
/* 3264 */     NativeCall localNativeCall = (NativeCall)paramScriptable;
/* 3265 */     localNativeCall.parentActivationCall = paramContext.currentActivationCall;
/* 3266 */     paramContext.currentActivationCall = localNativeCall;
/*      */   }
/*      */ 
/*      */   public static void exitActivationFunction(Context paramContext)
/*      */   {
/* 3271 */     NativeCall localNativeCall = paramContext.currentActivationCall;
/* 3272 */     paramContext.currentActivationCall = localNativeCall.parentActivationCall;
/* 3273 */     localNativeCall.parentActivationCall = null;
/*      */   }
/*      */ 
/*      */   static NativeCall findFunctionActivation(Context paramContext, Function paramFunction)
/*      */   {
/* 3278 */     NativeCall localNativeCall = paramContext.currentActivationCall;
/* 3279 */     while (localNativeCall != null) {
/* 3280 */       if (localNativeCall.function == paramFunction)
/* 3281 */         return localNativeCall;
/* 3282 */       localNativeCall = localNativeCall.parentActivationCall;
/*      */     }
/* 3284 */     return null;
/*      */   }
/*      */ 
/*      */   public static Scriptable newCatchScope(Throwable paramThrowable, Scriptable paramScriptable1, String paramString, Context paramContext, Scriptable paramScriptable2)
/*      */   {
/*      */     int i;
/*      */     Object localObject1;
/* 3296 */     if ((paramThrowable instanceof JavaScriptException)) {
/* 3297 */       i = 0;
/* 3298 */       localObject1 = ((JavaScriptException)paramThrowable).getValue();
/*      */     } else {
/* 3300 */       i = 1;
/*      */ 
/* 3305 */       if (paramScriptable1 != null) {
/* 3306 */         localObject2 = (NativeObject)paramScriptable1;
/* 3307 */         localObject1 = ((NativeObject)localObject2).getAssociatedValue(paramThrowable);
/* 3308 */         if (localObject1 == null) Kit.codeBug();
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 3315 */         Throwable localThrowable = null;
/*      */         String str1;
/*      */         String str2;
/* 3317 */         if ((paramThrowable instanceof EcmaError)) {
/* 3318 */           localObject3 = (EcmaError)paramThrowable;
/* 3319 */           localObject2 = localObject3;
/* 3320 */           str1 = ((EcmaError)localObject3).getName();
/* 3321 */           str2 = ((EcmaError)localObject3).getErrorMessage();
/* 3322 */         } else if ((paramThrowable instanceof WrappedException)) {
/* 3323 */           localObject3 = (WrappedException)paramThrowable;
/* 3324 */           localObject2 = localObject3;
/* 3325 */           localThrowable = ((WrappedException)localObject3).getWrappedException();
/* 3326 */           str1 = "JavaException";
/* 3327 */           str2 = localThrowable.getClass().getName() + ": " + localThrowable.getMessage();
/*      */         }
/* 3329 */         else if ((paramThrowable instanceof EvaluatorException))
/*      */         {
/* 3331 */           localObject3 = (EvaluatorException)paramThrowable;
/* 3332 */           localObject2 = localObject3;
/* 3333 */           str1 = "InternalError";
/* 3334 */           str2 = ((EvaluatorException)localObject3).getMessage();
/* 3335 */         } else if (paramContext.hasFeature(13))
/*      */         {
/* 3338 */           localObject2 = new WrappedException(paramThrowable);
/* 3339 */           str1 = "JavaException";
/* 3340 */           str2 = paramThrowable.toString();
/*      */         }
/*      */         else
/*      */         {
/* 3344 */           throw Kit.codeBug();
/*      */         }
/*      */ 
/* 3347 */         Object localObject3 = ((RhinoException)localObject2).sourceName();
/* 3348 */         if (localObject3 == null) {
/* 3349 */           localObject3 = "";
/*      */         }
/* 3351 */         int j = ((RhinoException)localObject2).lineNumber();
/*      */         Object[] arrayOfObject;
/* 3353 */         if (j > 0)
/* 3354 */           arrayOfObject = new Object[] { str2, localObject3, Integer.valueOf(j) };
/*      */         else {
/* 3356 */           arrayOfObject = new Object[] { str2, localObject3 };
/*      */         }
/*      */ 
/* 3359 */         Scriptable localScriptable = paramContext.newObject(paramScriptable2, str1, arrayOfObject);
/* 3360 */         ScriptableObject.putProperty(localScriptable, "name", str1);
/*      */ 
/* 3362 */         if ((localScriptable instanceof NativeError))
/* 3363 */           ((NativeError)localScriptable).setStackProvider((RhinoException)localObject2);
/*      */         Object localObject4;
/* 3366 */         if ((localThrowable != null) && (isVisible(paramContext, localThrowable))) {
/* 3367 */           localObject4 = paramContext.getWrapFactory().wrap(paramContext, paramScriptable2, localThrowable, null);
/*      */ 
/* 3369 */           ScriptableObject.defineProperty(localScriptable, "javaException", localObject4, 5);
/*      */         }
/*      */ 
/* 3373 */         if (isVisible(paramContext, localObject2)) {
/* 3374 */           localObject4 = paramContext.getWrapFactory().wrap(paramContext, paramScriptable2, localObject2, null);
/* 3375 */           ScriptableObject.defineProperty(localScriptable, "rhinoException", localObject4, 5);
/*      */         }
/*      */ 
/* 3379 */         localObject1 = localScriptable;
/*      */       }
/*      */     }
/* 3382 */     Object localObject2 = new NativeObject();
/*      */ 
/* 3384 */     ((NativeObject)localObject2).defineProperty(paramString, localObject1, 4);
/*      */ 
/* 3387 */     if (isVisible(paramContext, paramThrowable))
/*      */     {
/* 3391 */       ((NativeObject)localObject2).defineProperty("__exception__", Context.javaToJS(paramThrowable, paramScriptable2), 6);
/*      */     }
/*      */ 
/* 3396 */     if (i != 0) {
/* 3397 */       ((NativeObject)localObject2).associateValue(paramThrowable, localObject1);
/*      */     }
/* 3399 */     return localObject2;
/*      */   }
/*      */ 
/*      */   private static boolean isVisible(Context paramContext, Object paramObject) {
/* 3403 */     ClassShutter localClassShutter = paramContext.getClassShutter();
/* 3404 */     return (localClassShutter == null) || (localClassShutter.visibleToScripts(paramObject.getClass().getName()));
/*      */   }
/*      */ 
/*      */   public static Scriptable enterWith(Object paramObject, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 3411 */     Scriptable localScriptable = toObjectOrNull(paramContext, paramObject);
/* 3412 */     if (localScriptable == null) {
/* 3413 */       throw typeError1("msg.undef.with", toString(paramObject));
/*      */     }
/* 3415 */     if ((localScriptable instanceof XMLObject)) {
/* 3416 */       XMLObject localXMLObject = (XMLObject)localScriptable;
/* 3417 */       return localXMLObject.enterWith(paramScriptable);
/*      */     }
/* 3419 */     return new NativeWith(paramScriptable, localScriptable);
/*      */   }
/*      */ 
/*      */   public static Scriptable leaveWith(Scriptable paramScriptable)
/*      */   {
/* 3424 */     NativeWith localNativeWith = (NativeWith)paramScriptable;
/* 3425 */     return localNativeWith.getParentScope();
/*      */   }
/*      */ 
/*      */   public static Scriptable enterDotQuery(Object paramObject, Scriptable paramScriptable)
/*      */   {
/* 3430 */     if (!(paramObject instanceof XMLObject)) {
/* 3431 */       throw notXmlError(paramObject);
/*      */     }
/* 3433 */     XMLObject localXMLObject = (XMLObject)paramObject;
/* 3434 */     return localXMLObject.enterDotQuery(paramScriptable);
/*      */   }
/*      */ 
/*      */   public static Object updateDotQuery(boolean paramBoolean, Scriptable paramScriptable)
/*      */   {
/* 3440 */     NativeWith localNativeWith = (NativeWith)paramScriptable;
/* 3441 */     return localNativeWith.updateDotQuery(paramBoolean);
/*      */   }
/*      */ 
/*      */   public static Scriptable leaveDotQuery(Scriptable paramScriptable)
/*      */   {
/* 3446 */     NativeWith localNativeWith = (NativeWith)paramScriptable;
/* 3447 */     return localNativeWith.getParentScope();
/*      */   }
/*      */ 
/*      */   public static void setFunctionProtoAndParent(BaseFunction paramBaseFunction, Scriptable paramScriptable)
/*      */   {
/* 3453 */     paramBaseFunction.setParentScope(paramScriptable);
/* 3454 */     paramBaseFunction.setPrototype(ScriptableObject.getFunctionPrototype(paramScriptable));
/*      */   }
/*      */ 
/*      */   public static void setObjectProtoAndParent(ScriptableObject paramScriptableObject, Scriptable paramScriptable)
/*      */   {
/* 3461 */     paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 3462 */     paramScriptableObject.setParentScope(paramScriptable);
/* 3463 */     Scriptable localScriptable = ScriptableObject.getClassPrototype(paramScriptable, paramScriptableObject.getClassName());
/*      */ 
/* 3465 */     paramScriptableObject.setPrototype(localScriptable);
/*      */   }
/*      */ 
/*      */   public static void initFunction(Context paramContext, Scriptable paramScriptable, NativeFunction paramNativeFunction, int paramInt, boolean paramBoolean)
/*      */   {
/*      */     String str;
/* 3472 */     if (paramInt == 1) {
/* 3473 */       str = paramNativeFunction.getFunctionName();
/* 3474 */       if ((str != null) && (str.length() != 0)) {
/* 3475 */         if (!paramBoolean)
/*      */         {
/* 3478 */           ScriptableObject.defineProperty(paramScriptable, str, paramNativeFunction, 4);
/*      */         }
/*      */         else
/* 3481 */           paramScriptable.put(str, paramScriptable, paramNativeFunction);
/*      */       }
/*      */     }
/* 3484 */     else if (paramInt == 3) {
/* 3485 */       str = paramNativeFunction.getFunctionName();
/* 3486 */       if ((str != null) && (str.length() != 0))
/*      */       {
/* 3490 */         while ((paramScriptable instanceof NativeWith)) {
/* 3491 */           paramScriptable = paramScriptable.getParentScope();
/*      */         }
/* 3493 */         paramScriptable.put(str, paramScriptable, paramNativeFunction);
/*      */       }
/*      */     } else {
/* 3496 */       throw Kit.codeBug();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Scriptable newArrayLiteral(Object[] paramArrayOfObject, int[] paramArrayOfInt, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 3505 */     int i = paramArrayOfObject.length;
/* 3506 */     int j = 0;
/* 3507 */     if (paramArrayOfInt != null) {
/* 3508 */       j = paramArrayOfInt.length;
/*      */     }
/* 3510 */     int k = i + j;
/* 3511 */     if ((k > 1) && (j * 2 < k))
/*      */     {
/* 3514 */       if (j == 0) {
/* 3515 */         localObject = paramArrayOfObject;
/*      */       } else {
/* 3517 */         localObject = new Object[k];
/* 3518 */         int m = 0;
/* 3519 */         i1 = 0; for (i2 = 0; i1 != k; i1++)
/* 3520 */           if ((m != j) && (paramArrayOfInt[m] == i1)) {
/* 3521 */             localObject[i1] = Scriptable.NOT_FOUND;
/* 3522 */             m++;
/*      */           }
/*      */           else {
/* 3525 */             localObject[i1] = paramArrayOfObject[i2];
/* 3526 */             i2++;
/*      */           }
/*      */       }
/* 3529 */       NativeArray localNativeArray = new NativeArray((Object[])localObject);
/* 3530 */       setObjectProtoAndParent(localNativeArray, paramScriptable);
/* 3531 */       return localNativeArray;
/*      */     }
/*      */ 
/* 3534 */     Object localObject = new NativeArray(k);
/* 3535 */     setObjectProtoAndParent((ScriptableObject)localObject, paramScriptable);
/*      */ 
/* 3537 */     int n = 0;
/* 3538 */     int i1 = 0; for (int i2 = 0; i1 != k; i1++)
/* 3539 */       if ((n != j) && (paramArrayOfInt[n] == i1)) {
/* 3540 */         n++;
/*      */       }
/*      */       else {
/* 3543 */         ScriptableObject.putProperty((Scriptable)localObject, i1, paramArrayOfObject[i2]);
/* 3544 */         i2++;
/*      */       }
/* 3546 */     return localObject;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Scriptable newObjectLiteral(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 3561 */     int[] arrayOfInt = new int[paramArrayOfObject1.length];
/* 3562 */     return newObjectLiteral(paramArrayOfObject1, paramArrayOfObject2, arrayOfInt, paramContext, paramScriptable);
/*      */   }
/*      */ 
/*      */   public static Scriptable newObjectLiteral(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int[] paramArrayOfInt, Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 3571 */     Scriptable localScriptable = paramContext.newObject(paramScriptable);
/* 3572 */     int i = 0; for (int j = paramArrayOfObject1.length; i != j; i++) {
/* 3573 */       Object localObject1 = paramArrayOfObject1[i];
/* 3574 */       int k = paramArrayOfInt[i];
/* 3575 */       Object localObject2 = paramArrayOfObject2[i];
/* 3576 */       if ((localObject1 instanceof String)) {
/* 3577 */         if (k == 0) {
/* 3578 */           if (isSpecialProperty((String)localObject1))
/* 3579 */             specialRef(localScriptable, (String)localObject1, paramContext).set(paramContext, localObject2);
/*      */           else
/* 3581 */             ScriptableObject.putProperty(localScriptable, (String)localObject1, localObject2);
/*      */         }
/*      */         else
/*      */         {
/*      */           String str;
/* 3586 */           if (k < 0)
/* 3587 */             str = "__defineGetter__";
/*      */           else
/* 3589 */             str = "__defineSetter__";
/* 3590 */           Callable localCallable = getPropFunctionAndThis(localScriptable, str, paramContext);
/*      */ 
/* 3592 */           lastStoredScriptable(paramContext);
/* 3593 */           Object[] arrayOfObject = new Object[2];
/* 3594 */           arrayOfObject[0] = localObject1;
/* 3595 */           arrayOfObject[1] = localObject2;
/* 3596 */           localCallable.call(paramContext, paramScriptable, localScriptable, arrayOfObject);
/*      */         }
/*      */       } else {
/* 3599 */         int m = ((Integer)localObject1).intValue();
/* 3600 */         ScriptableObject.putProperty(localScriptable, m, localObject2);
/*      */       }
/*      */     }
/* 3603 */     return localScriptable;
/*      */   }
/*      */ 
/*      */   public static boolean isArrayObject(Object paramObject)
/*      */   {
/* 3608 */     return ((paramObject instanceof NativeArray)) || ((paramObject instanceof Arguments));
/*      */   }
/*      */ 
/*      */   public static Object[] getArrayElements(Scriptable paramScriptable)
/*      */   {
/* 3613 */     Context localContext = Context.getContext();
/* 3614 */     long l = NativeArray.getLengthProperty(localContext, paramScriptable);
/* 3615 */     if (l > 2147483647L)
/*      */     {
/* 3617 */       throw new IllegalArgumentException();
/*      */     }
/* 3619 */     int i = (int)l;
/* 3620 */     if (i == 0) {
/* 3621 */       return emptyArgs;
/*      */     }
/* 3623 */     Object[] arrayOfObject = new Object[i];
/* 3624 */     for (int j = 0; j < i; j++) {
/* 3625 */       Object localObject = ScriptableObject.getProperty(paramScriptable, j);
/* 3626 */       arrayOfObject[j] = (localObject == Scriptable.NOT_FOUND ? Undefined.instance : localObject);
/*      */     }
/*      */ 
/* 3629 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   static void checkDeprecated(Context paramContext, String paramString)
/*      */   {
/* 3634 */     int i = paramContext.getLanguageVersion();
/* 3635 */     if ((i >= 140) || (i == 0)) {
/* 3636 */       String str = getMessage1("msg.deprec.ctor", paramString);
/* 3637 */       if (i == 0)
/* 3638 */         Context.reportWarning(str);
/*      */       else
/* 3640 */         throw Context.reportRuntimeError(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String getMessage0(String paramString)
/*      */   {
/* 3646 */     return getMessage(paramString, null);
/*      */   }
/*      */ 
/*      */   public static String getMessage1(String paramString, Object paramObject)
/*      */   {
/* 3651 */     Object[] arrayOfObject = { paramObject };
/* 3652 */     return getMessage(paramString, arrayOfObject);
/*      */   }
/*      */ 
/*      */   public static String getMessage2(String paramString, Object paramObject1, Object paramObject2)
/*      */   {
/* 3658 */     Object[] arrayOfObject = { paramObject1, paramObject2 };
/* 3659 */     return getMessage(paramString, arrayOfObject);
/*      */   }
/*      */ 
/*      */   public static String getMessage3(String paramString, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 3665 */     Object[] arrayOfObject = { paramObject1, paramObject2, paramObject3 };
/* 3666 */     return getMessage(paramString, arrayOfObject);
/*      */   }
/*      */ 
/*      */   public static String getMessage4(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4)
/*      */   {
/* 3672 */     Object[] arrayOfObject = { paramObject1, paramObject2, paramObject3, paramObject4 };
/* 3673 */     return getMessage(paramString, arrayOfObject);
/*      */   }
/*      */ 
/*      */   public static String getMessage(String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 3698 */     return messageProvider.getMessage(paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public static EcmaError constructError(String paramString1, String paramString2)
/*      */   {
/* 3746 */     int[] arrayOfInt = new int[1];
/* 3747 */     String str = Context.getSourcePositionFromStack(arrayOfInt);
/* 3748 */     return constructError(paramString1, paramString2, str, arrayOfInt[0], null, 0);
/*      */   }
/*      */ 
/*      */   public static EcmaError constructError(String paramString1, String paramString2, int paramInt)
/*      */   {
/* 3755 */     int[] arrayOfInt = new int[1];
/* 3756 */     String str = Context.getSourcePositionFromStack(arrayOfInt);
/* 3757 */     if (arrayOfInt[0] != 0) {
/* 3758 */       arrayOfInt[0] += paramInt;
/*      */     }
/* 3760 */     return constructError(paramString1, paramString2, str, arrayOfInt[0], null, 0);
/*      */   }
/*      */ 
/*      */   public static EcmaError constructError(String paramString1, String paramString2, String paramString3, int paramInt1, String paramString4, int paramInt2)
/*      */   {
/* 3770 */     return new EcmaError(paramString1, paramString2, paramString3, paramInt1, paramString4, paramInt2);
/*      */   }
/*      */ 
/*      */   public static EcmaError typeError(String paramString)
/*      */   {
/* 3776 */     return constructError("TypeError", paramString);
/*      */   }
/*      */ 
/*      */   public static EcmaError typeError0(String paramString)
/*      */   {
/* 3781 */     String str = getMessage0(paramString);
/* 3782 */     return typeError(str);
/*      */   }
/*      */ 
/*      */   public static EcmaError typeError1(String paramString1, String paramString2)
/*      */   {
/* 3787 */     String str = getMessage1(paramString1, paramString2);
/* 3788 */     return typeError(str);
/*      */   }
/*      */ 
/*      */   public static EcmaError typeError2(String paramString1, String paramString2, String paramString3)
/*      */   {
/* 3794 */     String str = getMessage2(paramString1, paramString2, paramString3);
/* 3795 */     return typeError(str);
/*      */   }
/*      */ 
/*      */   public static EcmaError typeError3(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/* 3801 */     String str = getMessage3(paramString1, paramString2, paramString3, paramString4);
/* 3802 */     return typeError(str);
/*      */   }
/*      */ 
/*      */   public static RuntimeException undefReadError(Object paramObject1, Object paramObject2)
/*      */   {
/* 3807 */     String str = paramObject2 == null ? "null" : paramObject2.toString();
/* 3808 */     return typeError2("msg.undef.prop.read", toString(paramObject1), str);
/*      */   }
/*      */ 
/*      */   public static RuntimeException undefCallError(Object paramObject1, Object paramObject2)
/*      */   {
/* 3813 */     String str = paramObject2 == null ? "null" : paramObject2.toString();
/* 3814 */     return typeError2("msg.undef.method.call", toString(paramObject1), str);
/*      */   }
/*      */ 
/*      */   public static RuntimeException undefWriteError(Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 3821 */     String str1 = paramObject2 == null ? "null" : paramObject2.toString();
/* 3822 */     String str2 = (paramObject3 instanceof Scriptable) ? paramObject3.toString() : toString(paramObject3);
/*      */ 
/* 3824 */     return typeError3("msg.undef.prop.write", toString(paramObject1), str1, str2);
/*      */   }
/*      */ 
/*      */   public static RuntimeException notFoundError(Scriptable paramScriptable, String paramString)
/*      */   {
/* 3832 */     String str = getMessage1("msg.is.not.defined", paramString);
/* 3833 */     throw constructError("ReferenceError", str);
/*      */   }
/*      */ 
/*      */   public static RuntimeException notFunctionError(Object paramObject)
/*      */   {
/* 3838 */     return notFunctionError(paramObject, paramObject);
/*      */   }
/*      */ 
/*      */   public static RuntimeException notFunctionError(Object paramObject1, Object paramObject2)
/*      */   {
/* 3845 */     String str = paramObject2 == null ? "null" : paramObject2.toString();
/*      */ 
/* 3847 */     if (paramObject1 == Scriptable.NOT_FOUND) {
/* 3848 */       return typeError1("msg.function.not.found", str);
/*      */     }
/* 3850 */     return typeError2("msg.isnt.function", str, typeof(paramObject1));
/*      */   }
/*      */ 
/*      */   public static RuntimeException notFunctionError(Object paramObject1, Object paramObject2, String paramString)
/*      */   {
/* 3857 */     String str = toString(paramObject1);
/* 3858 */     if ((paramObject1 instanceof NativeFunction))
/*      */     {
/* 3860 */       int i = str.indexOf('{');
/* 3861 */       if (i > -1) {
/* 3862 */         str = str.substring(0, i + 1) + "...}";
/*      */       }
/*      */     }
/* 3865 */     if (paramObject2 == Scriptable.NOT_FOUND) {
/* 3866 */       return typeError2("msg.function.not.found.in", paramString, str);
/*      */     }
/*      */ 
/* 3869 */     return typeError3("msg.isnt.function.in", paramString, str, typeof(paramObject2));
/*      */   }
/*      */ 
/*      */   private static RuntimeException notXmlError(Object paramObject)
/*      */   {
/* 3875 */     throw typeError1("msg.isnt.xml.object", toString(paramObject));
/*      */   }
/*      */ 
/*      */   private static void warnAboutNonJSObject(Object paramObject)
/*      */   {
/* 3880 */     String str = "RHINO USAGE WARNING: Missed Context.javaToJS() conversion:\nRhino runtime detected object " + paramObject + " of class " + paramObject.getClass().getName() + " where it expected String, Number, Boolean or Scriptable instance. Please check your code for missing Context.javaToJS() call.";
/*      */ 
/* 3883 */     Context.reportWarning(str);
/*      */ 
/* 3885 */     System.err.println(str);
/*      */   }
/*      */ 
/*      */   public static RegExpProxy getRegExpProxy(Context paramContext)
/*      */   {
/* 3890 */     return paramContext.getRegExpProxy();
/*      */   }
/*      */ 
/*      */   public static void setRegExpProxy(Context paramContext, RegExpProxy paramRegExpProxy)
/*      */   {
/* 3895 */     if (paramRegExpProxy == null) throw new IllegalArgumentException();
/* 3896 */     paramContext.regExpProxy = paramRegExpProxy;
/*      */   }
/*      */ 
/*      */   public static RegExpProxy checkRegExpProxy(Context paramContext)
/*      */   {
/* 3901 */     RegExpProxy localRegExpProxy = getRegExpProxy(paramContext);
/* 3902 */     if (localRegExpProxy == null) {
/* 3903 */       throw Context.reportRuntimeError0("msg.no.regexp");
/*      */     }
/* 3905 */     return localRegExpProxy;
/*      */   }
/*      */ 
/*      */   private static XMLLib currentXMLLib(Context paramContext)
/*      */   {
/* 3911 */     if (paramContext.topCallScope == null) {
/* 3912 */       throw new IllegalStateException();
/*      */     }
/* 3914 */     XMLLib localXMLLib = paramContext.cachedXMLLib;
/* 3915 */     if (localXMLLib == null) {
/* 3916 */       localXMLLib = XMLLib.extractFromScope(paramContext.topCallScope);
/* 3917 */       if (localXMLLib == null)
/* 3918 */         throw new IllegalStateException();
/* 3919 */       paramContext.cachedXMLLib = localXMLLib;
/*      */     }
/*      */ 
/* 3922 */     return localXMLLib;
/*      */   }
/*      */ 
/*      */   public static String escapeAttributeValue(Object paramObject, Context paramContext)
/*      */   {
/* 3933 */     XMLLib localXMLLib = currentXMLLib(paramContext);
/* 3934 */     return localXMLLib.escapeAttributeValue(paramObject);
/*      */   }
/*      */ 
/*      */   public static String escapeTextValue(Object paramObject, Context paramContext)
/*      */   {
/* 3945 */     XMLLib localXMLLib = currentXMLLib(paramContext);
/* 3946 */     return localXMLLib.escapeTextValue(paramObject);
/*      */   }
/*      */ 
/*      */   public static Ref memberRef(Object paramObject1, Object paramObject2, Context paramContext, int paramInt)
/*      */   {
/* 3952 */     if (!(paramObject1 instanceof XMLObject)) {
/* 3953 */       throw notXmlError(paramObject1);
/*      */     }
/* 3955 */     XMLObject localXMLObject = (XMLObject)paramObject1;
/* 3956 */     return localXMLObject.memberRef(paramContext, paramObject2, paramInt);
/*      */   }
/*      */ 
/*      */   public static Ref memberRef(Object paramObject1, Object paramObject2, Object paramObject3, Context paramContext, int paramInt)
/*      */   {
/* 3962 */     if (!(paramObject1 instanceof XMLObject)) {
/* 3963 */       throw notXmlError(paramObject1);
/*      */     }
/* 3965 */     XMLObject localXMLObject = (XMLObject)paramObject1;
/* 3966 */     return localXMLObject.memberRef(paramContext, paramObject2, paramObject3, paramInt);
/*      */   }
/*      */ 
/*      */   public static Ref nameRef(Object paramObject, Context paramContext, Scriptable paramScriptable, int paramInt)
/*      */   {
/* 3972 */     XMLLib localXMLLib = currentXMLLib(paramContext);
/* 3973 */     return localXMLLib.nameRef(paramContext, paramObject, paramScriptable, paramInt);
/*      */   }
/*      */ 
/*      */   public static Ref nameRef(Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable, int paramInt)
/*      */   {
/* 3979 */     XMLLib localXMLLib = currentXMLLib(paramContext);
/* 3980 */     return localXMLLib.nameRef(paramContext, paramObject1, paramObject2, paramScriptable, paramInt);
/*      */   }
/*      */ 
/*      */   private static void storeIndexResult(Context paramContext, int paramInt)
/*      */   {
/* 3985 */     paramContext.scratchIndex = paramInt;
/*      */   }
/*      */ 
/*      */   static int lastIndexResult(Context paramContext)
/*      */   {
/* 3990 */     return paramContext.scratchIndex;
/*      */   }
/*      */ 
/*      */   public static void storeUint32Result(Context paramContext, long paramLong)
/*      */   {
/* 3995 */     if (paramLong >>> 32 != 0L)
/* 3996 */       throw new IllegalArgumentException();
/* 3997 */     paramContext.scratchUint32 = paramLong;
/*      */   }
/*      */ 
/*      */   public static long lastUint32Result(Context paramContext)
/*      */   {
/* 4002 */     long l = paramContext.scratchUint32;
/* 4003 */     if (l >>> 32 != 0L)
/* 4004 */       throw new IllegalStateException();
/* 4005 */     return l;
/*      */   }
/*      */ 
/*      */   private static void storeScriptable(Context paramContext, Scriptable paramScriptable)
/*      */   {
/* 4011 */     if (paramContext.scratchScriptable != null)
/* 4012 */       throw new IllegalStateException();
/* 4013 */     paramContext.scratchScriptable = paramScriptable;
/*      */   }
/*      */ 
/*      */   public static Scriptable lastStoredScriptable(Context paramContext)
/*      */   {
/* 4018 */     Scriptable localScriptable = paramContext.scratchScriptable;
/* 4019 */     paramContext.scratchScriptable = null;
/* 4020 */     return localScriptable;
/*      */   }
/*      */ 
/*      */   static String makeUrlForGeneratedScript(boolean paramBoolean, String paramString, int paramInt)
/*      */   {
/* 4026 */     if (paramBoolean) {
/* 4027 */       return paramString + '#' + paramInt + "(eval)";
/*      */     }
/* 4029 */     return paramString + '#' + paramInt + "(Function)";
/*      */   }
/*      */ 
/*      */   static boolean isGeneratedScript(String paramString)
/*      */   {
/* 4036 */     return (paramString.indexOf("(eval)") >= 0) || (paramString.indexOf("(Function)") >= 0);
/*      */   }
/*      */ 
/*      */   private static RuntimeException errorWithClassName(String paramString, Object paramObject)
/*      */   {
/* 4042 */     return Context.reportRuntimeError1(paramString, paramObject.getClass().getName());
/*      */   }
/*      */ 
/*      */   public static JavaScriptException throwError(Context paramContext, Scriptable paramScriptable, String paramString)
/*      */   {
/* 4054 */     Scriptable localScriptable = paramContext.newObject(paramScriptable, "Error", new Object[] { paramString });
/*      */ 
/* 4056 */     return new JavaScriptException(localScriptable, (String)ScriptableObject.getTypedProperty(localScriptable, "fileName", String.class), ((Number)ScriptableObject.getTypedProperty(localScriptable, "lineNumber", Number.class)).intValue());
/*      */   }
/*      */ 
/*      */   private static class DefaultMessageProvider
/*      */     implements ScriptRuntime.MessageProvider
/*      */   {
/*      */     public String getMessage(String paramString, Object[] paramArrayOfObject)
/*      */     {
/* 3710 */       Context localContext = Context.getCurrentContext();
/* 3711 */       final Locale localLocale = localContext != null ? localContext.getLocale() : Locale.getDefault();
/*      */ 
/* 3719 */       ResourceBundle localResourceBundle = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public ResourceBundle run() {
/* 3722 */           return ResourceBundle.getBundle("sun.org.mozilla.javascript.internal.resources.Messages", localLocale);
/*      */         }
/*      */       });
/*      */       String str;
/*      */       try {
/* 3728 */         str = localResourceBundle.getString(paramString);
/*      */       } catch (MissingResourceException localMissingResourceException) {
/* 3730 */         throw new RuntimeException("no message resource found for message property " + paramString);
/*      */       }
/*      */ 
/* 3739 */       MessageFormat localMessageFormat = new MessageFormat(str);
/* 3740 */       return localMessageFormat.format(paramArrayOfObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class IdEnumeration
/*      */   {
/*      */     Scriptable obj;
/*      */     Object[] ids;
/*      */     int index;
/*      */     ObjToIntMap used;
/*      */     Object currentId;
/*      */     int enumType;
/*      */     boolean enumNumbers;
/*      */     Scriptable iterator;
/*      */   }
/*      */ 
/*      */   public static abstract interface MessageProvider
/*      */   {
/*      */     public abstract String getMessage(String paramString, Object[] paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   static class NoSuchMethodShim
/*      */     implements Callable
/*      */   {
/*      */     String methodName;
/*      */     Callable noSuchMethodMethod;
/*      */ 
/*      */     NoSuchMethodShim(Callable paramCallable, String paramString)
/*      */     {
/*  107 */       this.noSuchMethodMethod = paramCallable;
/*  108 */       this.methodName = paramString;
/*      */     }
/*      */ 
/*      */     public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*      */     {
/*  122 */       Object[] arrayOfObject = new Object[2];
/*      */ 
/*  124 */       arrayOfObject[0] = this.methodName;
/*  125 */       arrayOfObject[1] = ScriptRuntime.newArrayLiteral(paramArrayOfObject, null, paramContext, paramScriptable1);
/*  126 */       return this.noSuchMethodMethod.call(paramContext, paramScriptable1, paramScriptable2, arrayOfObject);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ScriptRuntime
 * JD-Core Version:    0.6.2
 */