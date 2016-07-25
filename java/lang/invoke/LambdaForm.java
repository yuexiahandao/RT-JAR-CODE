/*      */ package java.lang.invoke;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.annotation.Retention;
/*      */ import java.lang.annotation.RetentionPolicy;
/*      */ import java.lang.annotation.Target;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import sun.invoke.util.Wrapper;
/*      */ 
/*      */ class LambdaForm
/*      */ {
/*      */   final int arity;
/*      */   final int result;
/*      */   final Name[] names;
/*      */   final String debugName;
/*      */   MemberName vmentry;
/*      */   private boolean isCompiled;
/*      */   LambdaForm[] bindCache;
/*      */   public static final int VOID_RESULT = -1;
/*      */   public static final int LAST_RESULT = -2;
/*      */   private static final ConcurrentHashMap<String, LambdaForm> PREPARED_FORMS;
/*      */   private static final boolean USE_PREDEFINED_INTERPRET_METHODS = true;
/*      */   private static final int COMPILE_THRESHOLD;
/*  592 */   private int invocationCounter = 0;
/*      */   static final String ALL_TYPES = "LIJFD";
/*      */   static final int INTERNED_ARGUMENT_LIMIT = 10;
/*      */   private static final Name[][] INTERNED_ARGUMENTS;
/*      */   private static final MemberName.Factory IMPL_NAMES;
/*      */   private static final Name[] CONSTANT_ZERO;
/*      */ 
/*      */   LambdaForm(String paramString, int paramInt1, Name[] paramArrayOfName, int paramInt2)
/*      */   {
/*  135 */     assert (namesOK(paramInt1, paramArrayOfName));
/*  136 */     this.arity = paramInt1;
/*  137 */     this.result = fixResult(paramInt2, paramArrayOfName);
/*  138 */     this.names = ((Name[])paramArrayOfName.clone());
/*  139 */     this.debugName = paramString;
/*  140 */     normalize();
/*      */   }
/*      */ 
/*      */   LambdaForm(String paramString, int paramInt, Name[] paramArrayOfName)
/*      */   {
/*  145 */     this(paramString, paramInt, paramArrayOfName, -2);
/*      */   }
/*      */ 
/*      */   LambdaForm(String paramString, Name[] paramArrayOfName1, Name[] paramArrayOfName2, Name paramName)
/*      */   {
/*  151 */     this(paramString, paramArrayOfName1.length, buildNames(paramArrayOfName1, paramArrayOfName2, paramName), -2);
/*      */   }
/*      */ 
/*      */   private static Name[] buildNames(Name[] paramArrayOfName1, Name[] paramArrayOfName2, Name paramName)
/*      */   {
/*  156 */     int i = paramArrayOfName1.length;
/*  157 */     int j = i + paramArrayOfName2.length + (paramName == null ? 0 : 1);
/*  158 */     Name[] arrayOfName = (Name[])Arrays.copyOf(paramArrayOfName1, j);
/*  159 */     System.arraycopy(paramArrayOfName2, 0, arrayOfName, i, paramArrayOfName2.length);
/*  160 */     if (paramName != null)
/*  161 */       arrayOfName[(j - 1)] = paramName;
/*  162 */     return arrayOfName;
/*      */   }
/*      */ 
/*      */   private LambdaForm(String paramString)
/*      */   {
/*  169 */     assert (isValidSignature(paramString));
/*  170 */     this.arity = signatureArity(paramString);
/*  171 */     this.result = (signatureReturn(paramString) == 'V' ? -1 : this.arity);
/*  172 */     this.names = buildEmptyNames(this.arity, paramString);
/*  173 */     this.debugName = "LF.zero";
/*  174 */     assert (nameRefsAreLegal());
/*  175 */     assert (isEmpty());
/*  176 */     assert (paramString.equals(basicTypeSignature()));
/*      */   }
/*      */ 
/*      */   private static Name[] buildEmptyNames(int paramInt, String paramString) {
/*  180 */     assert (isValidSignature(paramString));
/*  181 */     int i = paramInt + 1;
/*  182 */     if ((paramInt < 0) || (paramString.length() != i + 1))
/*  183 */       throw new IllegalArgumentException("bad arity for " + paramString);
/*  184 */     int j = paramString.charAt(i) == 'V' ? 0 : 1;
/*  185 */     Name[] arrayOfName = arguments(j, paramString.substring(0, paramInt));
/*  186 */     for (int k = 0; k < j; k++) {
/*  187 */       arrayOfName[(paramInt + k)] = constantZero(paramInt + k, paramString.charAt(i + k));
/*      */     }
/*  189 */     return arrayOfName;
/*      */   }
/*      */ 
/*      */   private static int fixResult(int paramInt, Name[] paramArrayOfName) {
/*  193 */     if (paramInt >= 0) {
/*  194 */       if (paramArrayOfName[paramInt].type == 'V')
/*  195 */         return -1;
/*  196 */     } else if (paramInt == -2) {
/*  197 */       return paramArrayOfName.length - 1;
/*      */     }
/*  199 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private static boolean namesOK(int paramInt, Name[] paramArrayOfName) {
/*  203 */     for (int i = 0; i < paramArrayOfName.length; i++) {
/*  204 */       Name localName = paramArrayOfName[i];
/*  205 */       assert (localName != null) : "n is null";
/*  206 */       if (i < paramInt) {
/*  207 */         if ((!$assertionsDisabled) && (!localName.isParam())) throw new AssertionError(localName + " is not param at " + i); 
/*      */       }
/*      */       else
/*  209 */         assert (!localName.isParam()) : (localName + " is param at " + i);
/*      */     }
/*  211 */     return true;
/*      */   }
/*      */ 
/*      */   private void normalize()
/*      */   {
/*  216 */     Name[] arrayOfName = null;
/*  217 */     int i = 0;
/*      */     Name localName2;
/*  218 */     for (Name localName1 = 0; localName1 < this.names.length; localName1++) {
/*  219 */       localName2 = this.names[localName1];
/*  220 */       if (!localName2.initIndex(localName1)) {
/*  221 */         if (arrayOfName == null) {
/*  222 */           arrayOfName = (Name[])this.names.clone();
/*  223 */           i = localName1;
/*      */         }
/*  225 */         this.names[localName1] = localName2.cloneWithIndex(localName1);
/*      */       }
/*      */     }
/*  228 */     if (arrayOfName != null) {
/*  229 */       localName1 = this.arity;
/*  230 */       if (localName1 <= i)
/*  231 */         localName1 = i + 1;
/*  232 */       for (localName2 = localName1; localName2 < this.names.length; localName2++) {
/*  233 */         Name localName3 = this.names[localName2].replaceNames(arrayOfName, this.names, i, localName2);
/*  234 */         this.names[localName2] = localName3.newIndex(localName2);
/*      */       }
/*      */     }
/*  237 */     assert (nameRefsAreLegal());
/*  238 */     localName1 = Math.min(this.arity, 10);
/*  239 */     int j = 0;
/*  240 */     for (int k = 0; k < localName1; k++) {
/*  241 */       Name localName4 = this.names[k]; Name localName5 = internArgument(localName4);
/*  242 */       if (localName4 != localName5) {
/*  243 */         this.names[k] = localName5;
/*  244 */         j = 1;
/*      */       }
/*      */     }
/*  247 */     if (j != 0) {
/*  248 */       for (k = this.arity; k < this.names.length; k++) {
/*  249 */         this.names[k].internArguments();
/*      */       }
/*  251 */       assert (nameRefsAreLegal());
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean nameRefsAreLegal()
/*      */   {
/*  265 */     assert ((this.arity >= 0) && (this.arity <= this.names.length));
/*  266 */     assert ((this.result >= -1) && (this.result < this.names.length));
/*      */     Name localName1;
/*  268 */     for (int i = 0; i < this.arity; i++) {
/*  269 */       localName1 = this.names[i];
/*  270 */       if ((!$assertionsDisabled) && (localName1.index() != i)) throw new AssertionError(Arrays.asList(new Integer[] { Integer.valueOf(localName1.index()), Integer.valueOf(i) }));
/*  271 */       assert (localName1.isParam());
/*      */     }
/*      */ 
/*  274 */     for (i = this.arity; i < this.names.length; i++) {
/*  275 */       localName1 = this.names[i];
/*  276 */       assert (localName1.index() == i);
/*  277 */       for (Object localObject : localName1.arguments) {
/*  278 */         if ((localObject instanceof Name)) {
/*  279 */           Name localName2 = (Name)localObject;
/*  280 */           int m = localName2.index;
/*  281 */           assert ((0 <= m) && (m < this.names.length)) : (localName1.debugString() + ": 0 <= i2 && i2 < names.length: 0 <= " + m + " < " + this.names.length);
/*  282 */           if ((!$assertionsDisabled) && (this.names[m] != localName2)) throw new AssertionError(Arrays.asList(new Object[] { "-1-", Integer.valueOf(i), "-2-", localName1.debugString(), "-3-", Integer.valueOf(m), "-4-", localName2.debugString(), "-5-", this.names[m].debugString(), "-6-", this }));
/*  283 */           assert (m < i);
/*      */         }
/*      */       }
/*      */     }
/*  287 */     return true;
/*      */   }
/*      */ 
/*      */   char returnType()
/*      */   {
/*  298 */     if (this.result < 0) return 'V';
/*  299 */     Name localName = this.names[this.result];
/*  300 */     return localName.type;
/*      */   }
/*      */ 
/*      */   char parameterType(int paramInt)
/*      */   {
/*  305 */     assert (paramInt < this.arity);
/*  306 */     return this.names[paramInt].type;
/*      */   }
/*      */ 
/*      */   int arity()
/*      */   {
/*  311 */     return this.arity;
/*      */   }
/*      */ 
/*      */   MethodType methodType()
/*      */   {
/*  316 */     return signatureType(basicTypeSignature());
/*      */   }
/*      */ 
/*      */   final String basicTypeSignature() {
/*  320 */     StringBuilder localStringBuilder = new StringBuilder(arity() + 3);
/*  321 */     int i = 0; for (int j = arity(); i < j; i++)
/*  322 */       localStringBuilder.append(parameterType(i));
/*  323 */     return '_' + returnType();
/*      */   }
/*      */   static int signatureArity(String paramString) {
/*  326 */     assert (isValidSignature(paramString));
/*  327 */     return paramString.indexOf('_');
/*      */   }
/*      */   static char signatureReturn(String paramString) {
/*  330 */     return paramString.charAt(signatureArity(paramString) + 1);
/*      */   }
/*      */   static boolean isValidSignature(String paramString) {
/*  333 */     int i = paramString.indexOf('_');
/*  334 */     if (i < 0) return false;
/*  335 */     int j = paramString.length();
/*  336 */     if (j != i + 2) return false;
/*  337 */     for (int k = 0; k < j; k++)
/*  338 */       if (k != i) {
/*  339 */         int m = paramString.charAt(k);
/*  340 */         if (m == 86)
/*  341 */           return (k == j - 1) && (i == j - 2);
/*  342 */         if ("LIJFD".indexOf(m) < 0) return false;
/*      */       }
/*  344 */     return true;
/*      */   }
/*      */   static Class<?> typeClass(char paramChar) {
/*  347 */     switch (paramChar) { case 'I':
/*  348 */       return Integer.TYPE;
/*      */     case 'J':
/*  349 */       return Long.TYPE;
/*      */     case 'F':
/*  350 */       return Float.TYPE;
/*      */     case 'D':
/*  351 */       return Double.TYPE;
/*      */     case 'L':
/*  352 */       return Object.class;
/*      */     case 'V':
/*  353 */       return Void.TYPE;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'S':
/*      */     case 'T':
/*  354 */     case 'U': } if (!$assertionsDisabled) throw new AssertionError();
/*      */ 
/*  356 */     return null;
/*      */   }
/*      */   static MethodType signatureType(String paramString) {
/*  359 */     Class[] arrayOfClass = new Class[signatureArity(paramString)];
/*  360 */     for (int i = 0; i < arrayOfClass.length; i++)
/*  361 */       arrayOfClass[i] = typeClass(paramString.charAt(i));
/*  362 */     Class localClass = typeClass(signatureReturn(paramString));
/*  363 */     return MethodType.methodType(localClass, arrayOfClass);
/*      */   }
/*      */ 
/*      */   public void prepare()
/*      */   {
/*  434 */     if (COMPILE_THRESHOLD == 0) {
/*  435 */       compileToBytecode();
/*      */     }
/*  437 */     if (this.vmentry != null)
/*      */     {
/*  439 */       return;
/*      */     }
/*  441 */     LambdaForm localLambdaForm = getPreparedForm(basicTypeSignature());
/*  442 */     this.vmentry = localLambdaForm.vmentry;
/*      */   }
/*      */ 
/*      */   MemberName compileToBytecode()
/*      */   {
/*  448 */     MethodType localMethodType = methodType();
/*  449 */     assert ((this.vmentry == null) || (this.vmentry.getMethodType().basicType().equals(localMethodType)));
/*  450 */     if ((this.vmentry != null) && (this.isCompiled))
/*  451 */       return this.vmentry;
/*      */     try
/*      */     {
/*  454 */       this.vmentry = InvokerBytecodeGenerator.generateCustomizedCode(this, localMethodType);
/*  455 */       if (MethodHandleStatics.TRACE_INTERPRETER)
/*  456 */         traceInterpreter("compileToBytecode", this);
/*  457 */       this.isCompiled = true;
/*  458 */       return this.vmentry;
/*      */     } catch (Error|Exception localError) {
/*  460 */       throw MethodHandleStatics.newInternalError(toString(), localError);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Map<String, LambdaForm> computeInitialPreparedForms()
/*      */   {
/*  474 */     HashMap localHashMap = new HashMap();
/*  475 */     for (MemberName localMemberName : MemberName.getFactory().getMethods(LambdaForm.class, false, null, null, null)) {
/*  476 */       if ((localMemberName.isStatic()) && (localMemberName.isPackage())) {
/*  477 */         MethodType localMethodType = localMemberName.getMethodType();
/*  478 */         if ((localMethodType.parameterCount() > 0) && (localMethodType.parameterType(0) == MethodHandle.class) && (localMemberName.getName().startsWith("interpret_")))
/*      */         {
/*  481 */           String str = basicTypeSignature(localMethodType);
/*  482 */           assert (localMemberName.getName().equals("interpret" + str.substring(str.indexOf('_'))));
/*  483 */           LambdaForm localLambdaForm = new LambdaForm(str);
/*  484 */           localLambdaForm.vmentry = localMemberName;
/*  485 */           localMethodType.form().setCachedLambdaForm(7, localLambdaForm);
/*      */ 
/*  487 */           localHashMap.put(str, localLambdaForm);
/*      */         }
/*      */       }
/*      */     }
/*  491 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   static Object interpret_L(MethodHandle paramMethodHandle)
/*      */     throws Throwable
/*      */   {
/*  500 */     Object[] arrayOfObject = { paramMethodHandle };
/*  501 */     String str = null;
/*  502 */     assert (argumentTypesMatch(str = "L_L", arrayOfObject));
/*  503 */     Object localObject = paramMethodHandle.form.interpretWithArguments(arrayOfObject);
/*  504 */     assert (returnTypesMatch(str, arrayOfObject, localObject));
/*  505 */     return localObject;
/*      */   }
/*      */   static Object interpret_L(MethodHandle paramMethodHandle, Object paramObject) throws Throwable {
/*  508 */     Object[] arrayOfObject = { paramMethodHandle, paramObject };
/*  509 */     String str = null;
/*  510 */     assert (argumentTypesMatch(str = "LL_L", arrayOfObject));
/*  511 */     Object localObject = paramMethodHandle.form.interpretWithArguments(arrayOfObject);
/*  512 */     assert (returnTypesMatch(str, arrayOfObject, localObject));
/*  513 */     return localObject;
/*      */   }
/*      */   static Object interpret_L(MethodHandle paramMethodHandle, Object paramObject1, Object paramObject2) throws Throwable {
/*  516 */     Object[] arrayOfObject = { paramMethodHandle, paramObject1, paramObject2 };
/*  517 */     String str = null;
/*  518 */     assert (argumentTypesMatch(str = "LLL_L", arrayOfObject));
/*  519 */     Object localObject = paramMethodHandle.form.interpretWithArguments(arrayOfObject);
/*  520 */     assert (returnTypesMatch(str, arrayOfObject, localObject));
/*  521 */     return localObject;
/*      */   }
/*      */   private static LambdaForm getPreparedForm(String paramString) {
/*  524 */     MethodType localMethodType = signatureType(paramString);
/*      */ 
/*  526 */     LambdaForm localLambdaForm = localMethodType.form().cachedLambdaForm(6);
/*  527 */     if (localLambdaForm != null) return localLambdaForm;
/*  528 */     assert (isValidSignature(paramString));
/*  529 */     localLambdaForm = new LambdaForm(paramString);
/*  530 */     localLambdaForm.vmentry = InvokerBytecodeGenerator.generateLambdaFormInterpreterEntryPoint(paramString);
/*      */ 
/*  532 */     return localMethodType.form().setCachedLambdaForm(6, localLambdaForm);
/*      */   }
/*      */ 
/*      */   private static boolean argumentTypesMatch(String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  538 */     int i = signatureArity(paramString);
/*  539 */     assert (paramArrayOfObject.length == i) : ("av.length == arity: av.length=" + paramArrayOfObject.length + ", arity=" + i);
/*  540 */     assert ((paramArrayOfObject[0] instanceof MethodHandle)) : ("av[0] not instace of MethodHandle: " + paramArrayOfObject[0]);
/*  541 */     MethodHandle localMethodHandle = (MethodHandle)paramArrayOfObject[0];
/*  542 */     MethodType localMethodType = localMethodHandle.type();
/*  543 */     assert (localMethodType.parameterCount() == i - 1);
/*  544 */     for (int j = 0; j < paramArrayOfObject.length; j++) {
/*  545 */       Class localClass = j == 0 ? MethodHandle.class : localMethodType.parameterType(j - 1);
/*  546 */       assert (valueMatches(paramString.charAt(j), localClass, paramArrayOfObject[j]));
/*      */     }
/*  548 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean valueMatches(char paramChar, Class<?> paramClass, Object paramObject) {
/*  552 */     if (paramClass == Void.TYPE) paramChar = 'V';
/*  553 */     assert (paramChar == basicType(paramClass)) : (paramChar + " == basicType(" + paramClass + ")=" + basicType(paramClass));
/*  554 */     switch (paramChar) { case 'I':
/*  555 */       if ((!$assertionsDisabled) && (!checkInt(paramClass, paramObject))) throw new AssertionError("checkInt(" + paramClass + "," + paramObject + ")"); break;
/*      */     case 'J':
/*  556 */       if ((!$assertionsDisabled) && (!(paramObject instanceof Long))) throw new AssertionError("instanceof Long: " + paramObject); break;
/*      */     case 'F':
/*  557 */       if ((!$assertionsDisabled) && (!(paramObject instanceof Float))) throw new AssertionError("instanceof Float: " + paramObject); break;
/*      */     case 'D':
/*  558 */       if ((!$assertionsDisabled) && (!(paramObject instanceof Double))) throw new AssertionError("instanceof Double: " + paramObject); break;
/*      */     case 'L':
/*  559 */       if ((!$assertionsDisabled) && (!checkRef(paramClass, paramObject))) throw new AssertionError("checkRef(" + paramClass + "," + paramObject + ")"); break;
/*      */     case 'V':
/*  560 */       break;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'S':
/*      */     case 'T':
/*      */     case 'U':
/*      */     default:
/*  561 */       if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */     }
/*  563 */     return true;
/*      */   }
/*      */   private static boolean returnTypesMatch(String paramString, Object[] paramArrayOfObject, Object paramObject) {
/*  566 */     MethodHandle localMethodHandle = (MethodHandle)paramArrayOfObject[0];
/*  567 */     return valueMatches(signatureReturn(paramString), localMethodHandle.type().returnType(), paramObject);
/*      */   }
/*      */   private static boolean checkInt(Class<?> paramClass, Object paramObject) {
/*  570 */     assert ((paramObject instanceof Integer));
/*  571 */     if (paramClass == Integer.TYPE) return true;
/*  572 */     Wrapper localWrapper = Wrapper.forBasicType(paramClass);
/*  573 */     assert (localWrapper.isSubwordOrInt());
/*  574 */     Object localObject = Wrapper.INT.wrap(localWrapper.wrap(paramObject));
/*  575 */     return paramObject.equals(localObject);
/*      */   }
/*      */   private static boolean checkRef(Class<?> paramClass, Object paramObject) {
/*  578 */     assert (!paramClass.isPrimitive());
/*  579 */     if (paramObject == null) return true;
/*  580 */     if (paramClass.isInterface()) return true;
/*  581 */     return paramClass.isInstance(paramObject);
/*      */   }
/*      */ 
/*      */   @Hidden
/*      */   @DontInline
/*      */   Object interpretWithArguments(Object[] paramArrayOfObject)
/*      */     throws Throwable
/*      */   {
/*  598 */     if (MethodHandleStatics.TRACE_INTERPRETER)
/*  599 */       return interpretWithArgumentsTracing(paramArrayOfObject);
/*  600 */     checkInvocationCounter();
/*  601 */     assert (arityCheck(paramArrayOfObject));
/*  602 */     Object[] arrayOfObject = Arrays.copyOf(paramArrayOfObject, this.names.length);
/*  603 */     for (int i = paramArrayOfObject.length; i < arrayOfObject.length; i++) {
/*  604 */       arrayOfObject[i] = interpretName(this.names[i], arrayOfObject);
/*      */     }
/*  606 */     return this.result < 0 ? null : arrayOfObject[this.result];
/*      */   }
/*      */ 
/*      */   @Hidden
/*      */   @DontInline
/*      */   Object interpretName(Name paramName, Object[] paramArrayOfObject) throws Throwable {
/*  613 */     if (MethodHandleStatics.TRACE_INTERPRETER)
/*  614 */       traceInterpreter("| interpretName", paramName.debugString(), (Object[])null);
/*  615 */     Object[] arrayOfObject = Arrays.copyOf(paramName.arguments, paramName.arguments.length, [Ljava.lang.Object.class);
/*  616 */     for (int i = 0; i < arrayOfObject.length; i++) {
/*  617 */       Object localObject = arrayOfObject[i];
/*  618 */       if ((localObject instanceof Name)) {
/*  619 */         int j = ((Name)localObject).index();
/*  620 */         assert (this.names[j] == localObject);
/*  621 */         localObject = paramArrayOfObject[j];
/*  622 */         arrayOfObject[i] = localObject;
/*      */       }
/*      */     }
/*  625 */     return paramName.function.invokeWithArguments(arrayOfObject);
/*      */   }
/*      */ 
/*      */   private void checkInvocationCounter() {
/*  629 */     if ((COMPILE_THRESHOLD != 0) && (this.invocationCounter < COMPILE_THRESHOLD))
/*      */     {
/*  631 */       this.invocationCounter += 1;
/*  632 */       if (this.invocationCounter >= COMPILE_THRESHOLD)
/*      */       {
/*  634 */         compileToBytecode();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*  639 */   Object interpretWithArgumentsTracing(Object[] paramArrayOfObject) throws Throwable { traceInterpreter("[ interpretWithArguments", this, paramArrayOfObject);
/*  640 */     if (this.invocationCounter < COMPILE_THRESHOLD) {
/*  641 */       int i = this.invocationCounter++;
/*  642 */       traceInterpreter("| invocationCounter", Integer.valueOf(i));
/*  643 */       if (this.invocationCounter >= COMPILE_THRESHOLD)
/*  644 */         compileToBytecode();
/*      */     }
/*      */     Object localObject;
/*      */     try
/*      */     {
/*  649 */       assert (arityCheck(paramArrayOfObject));
/*  650 */       Object[] arrayOfObject = Arrays.copyOf(paramArrayOfObject, this.names.length);
/*  651 */       for (int j = paramArrayOfObject.length; j < arrayOfObject.length; j++) {
/*  652 */         arrayOfObject[j] = interpretName(this.names[j], arrayOfObject);
/*      */       }
/*  654 */       localObject = this.result < 0 ? null : arrayOfObject[this.result];
/*      */     } catch (Throwable localThrowable) {
/*  656 */       traceInterpreter("] throw =>", localThrowable);
/*  657 */       throw localThrowable;
/*      */     }
/*  659 */     traceInterpreter("] return =>", localObject);
/*  660 */     return localObject;
/*      */   }
/*      */ 
/*      */   static void traceInterpreter(String paramString, Object paramObject, Object[] paramArrayOfObject)
/*      */   {
/*  686 */     if (!MethodHandleStatics.TRACE_INTERPRETER) return;
/*  687 */     System.out.println("LFI: " + paramString + " " + (paramObject != null ? paramObject : "") + ((paramArrayOfObject != null) && (paramArrayOfObject.length != 0) ? Arrays.asList(paramArrayOfObject) : ""));
/*      */   }
/*      */   static void traceInterpreter(String paramString, Object paramObject) {
/*  690 */     traceInterpreter(paramString, paramObject, (Object[])null);
/*      */   }
/*      */   private boolean arityCheck(Object[] paramArrayOfObject) {
/*  693 */     assert (paramArrayOfObject.length == this.arity) : (this.arity + "!=" + Arrays.asList(paramArrayOfObject) + ".length");
/*      */ 
/*  695 */     assert ((paramArrayOfObject[0] instanceof MethodHandle)) : ("not MH: " + paramArrayOfObject[0]);
/*  696 */     assert (((MethodHandle)paramArrayOfObject[0]).internalForm() == this);
/*      */ 
/*  698 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean isEmpty() {
/*  702 */     if (this.result < 0)
/*  703 */       return this.names.length == this.arity;
/*  704 */     if ((this.result == this.arity) && (this.names.length == this.arity + 1)) {
/*  705 */       return this.names[this.arity].isConstantZero();
/*      */     }
/*  707 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString() {
/*  711 */     StringBuilder localStringBuilder = new StringBuilder(this.debugName + "=Lambda(");
/*  712 */     for (int i = 0; i < this.names.length; i++) {
/*  713 */       if (i == this.arity) localStringBuilder.append(")=>{");
/*  714 */       Name localName = this.names[i];
/*  715 */       if (i >= this.arity) localStringBuilder.append("\n    ");
/*  716 */       localStringBuilder.append(localName);
/*  717 */       if (i < this.arity) {
/*  718 */         if (i + 1 < this.arity) localStringBuilder.append(","); 
/*      */       }
/*      */       else
/*      */       {
/*  721 */         localStringBuilder.append("=").append(localName.exprString());
/*  722 */         localStringBuilder.append(";");
/*      */       }
/*      */     }
/*  724 */     localStringBuilder.append(this.result < 0 ? "void" : this.names[this.result]).append("}");
/*  725 */     if (MethodHandleStatics.TRACE_INTERPRETER)
/*      */     {
/*  727 */       localStringBuilder.append(":").append(basicTypeSignature());
/*  728 */       localStringBuilder.append("/").append(this.vmentry);
/*      */     }
/*  730 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   LambdaForm bindImmediate(int paramInt, char paramChar, Object paramObject)
/*      */   {
/*  740 */     assert ((paramInt > 0) && (paramInt < this.arity) && (this.names[paramInt].type == paramChar) && (Name.typesMatch(paramChar, paramObject)));
/*      */ 
/*  742 */     int i = this.arity - 1;
/*  743 */     Name[] arrayOfName = new Name[this.names.length - 1];
/*  744 */     int j = 0; for (int k = 0; j < this.names.length; k++) {
/*  745 */       Name localName = this.names[j];
/*  746 */       if (localName.isParam()) {
/*  747 */         if (localName.index == paramInt)
/*      */         {
/*  750 */           k--;
/*      */         }
/*  752 */         else arrayOfName[k] = new Name(k, localName.type); 
/*      */       }
/*      */       else
/*      */       {
/*  755 */         Object[] arrayOfObject = new Object[localName.arguments.length];
/*  756 */         for (int m = 0; m < localName.arguments.length; m++) {
/*  757 */           Object localObject = localName.arguments[m];
/*  758 */           if ((localObject instanceof Name)) {
/*  759 */             int n = ((Name)localObject).index;
/*  760 */             if (n == paramInt)
/*  761 */               arrayOfObject[m] = paramObject;
/*  762 */             else if (n < paramInt)
/*      */             {
/*  764 */               arrayOfObject[m] = arrayOfName[n];
/*      */             }
/*      */             else
/*  767 */               arrayOfObject[m] = arrayOfName[(n - 1)];
/*      */           }
/*      */           else {
/*  770 */             arrayOfObject[m] = localObject;
/*      */           }
/*      */         }
/*  773 */         arrayOfName[k] = new Name(localName.function, arrayOfObject);
/*  774 */         arrayOfName[k].initIndex(k);
/*      */       }
/*  744 */       j++;
/*      */     }
/*      */ 
/*  778 */     j = this.result == -1 ? -1 : this.result - 1;
/*  779 */     return new LambdaForm(this.debugName, i, arrayOfName, j);
/*      */   }
/*      */ 
/*      */   LambdaForm bind(int paramInt, BoundMethodHandle.SpeciesData paramSpeciesData) {
/*  783 */     Name localName = this.names[paramInt];
/*  784 */     BoundMethodHandle.SpeciesData localSpeciesData = paramSpeciesData.extendWithType(localName.type);
/*  785 */     return bind(localName, localSpeciesData.getterName(this.names[0], paramSpeciesData.fieldCount()), paramSpeciesData, localSpeciesData);
/*      */   }
/*      */ 
/*      */   LambdaForm bind(Name paramName1, Name paramName2, BoundMethodHandle.SpeciesData paramSpeciesData1, BoundMethodHandle.SpeciesData paramSpeciesData2)
/*      */   {
/*  790 */     int i = paramName1.index;
/*  791 */     assert (paramName1.isParam());
/*  792 */     assert (!paramName2.isParam());
/*  793 */     assert (paramName1.type == paramName2.type);
/*  794 */     assert ((0 <= i) && (i < this.arity) && (this.names[i] == paramName1));
/*  795 */     assert (paramName2.function.memberDeclaringClassOrNull() == paramSpeciesData2.clazz);
/*  796 */     assert (paramSpeciesData1.getters.length == paramSpeciesData2.getters.length - 1);
/*  797 */     if (this.bindCache != null) {
/*  798 */       LambdaForm localLambdaForm = this.bindCache[i];
/*  799 */       if (localLambdaForm != null) {
/*  800 */         assert (localLambdaForm.contains(paramName2)) : ("form << " + localLambdaForm + " >> does not contain binding << " + paramName2 + " >>");
/*  801 */         return localLambdaForm;
/*      */       }
/*      */     } else {
/*  804 */       this.bindCache = new LambdaForm[this.arity];
/*      */     }
/*  806 */     assert (nameRefsAreLegal());
/*  807 */     int j = this.arity - 1;
/*  808 */     Name[] arrayOfName = (Name[])this.names.clone();
/*  809 */     arrayOfName[i] = paramName2;
/*      */ 
/*  813 */     int k = -1;
/*      */     Name localName1;
/*  814 */     for (int m = 0; m < arrayOfName.length; m++) {
/*  815 */       localName1 = this.names[m];
/*  816 */       if ((localName1.function != null) && (localName1.function.memberDeclaringClassOrNull() == paramSpeciesData1.clazz))
/*      */       {
/*  818 */         MethodHandle localMethodHandle1 = localName1.function.resolvedHandle;
/*  819 */         MethodHandle localMethodHandle2 = null;
/*  820 */         for (int i1 = 0; i1 < paramSpeciesData1.getters.length; i1++) {
/*  821 */           if (localMethodHandle1 == paramSpeciesData1.getters[i1])
/*  822 */             localMethodHandle2 = paramSpeciesData2.getters[i1];
/*      */         }
/*  824 */         if (localMethodHandle2 != null) {
/*  825 */           if (k < 0) k = m;
/*  826 */           Name localName2 = new Name(localMethodHandle2, localName1.arguments);
/*  827 */           arrayOfName[m] = localName2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  836 */     assert ((k < 0) || (k > i));
/*  837 */     for (m = i + 1; m < arrayOfName.length; m++) {
/*  838 */       if (m > j) {
/*  839 */         arrayOfName[m] = arrayOfName[m].replaceNames(this.names, arrayOfName, i, m);
/*      */       }
/*      */     }
/*      */ 
/*  843 */     for (m = i; 
/*  844 */       m + 1 < arrayOfName.length; m++) {
/*  845 */       localName1 = arrayOfName[(m + 1)];
/*  846 */       if (!localName1.isSiblingBindingBefore(paramName2)) break;
/*  847 */       arrayOfName[m] = localName1;
/*      */     }
/*      */ 
/*  852 */     arrayOfName[m] = paramName2;
/*      */ 
/*  855 */     int n = this.result;
/*  856 */     if (n == i)
/*  857 */       n = m;
/*  858 */     else if ((n > i) && (n <= m)) {
/*  859 */       n--;
/*      */     }
/*  861 */     return this.bindCache[i] =  = new LambdaForm(this.debugName, j, arrayOfName, n);
/*      */   }
/*      */ 
/*      */   boolean contains(Name paramName) {
/*  865 */     int i = paramName.index();
/*  866 */     if (i >= 0) {
/*  867 */       return (i < this.names.length) && (paramName.equals(this.names[i]));
/*      */     }
/*  869 */     for (int j = this.arity; j < this.names.length; j++) {
/*  870 */       if (paramName.equals(this.names[j]))
/*  871 */         return true;
/*      */     }
/*  873 */     return false;
/*      */   }
/*      */ 
/*      */   LambdaForm addArguments(int paramInt, char[] paramArrayOfChar) {
/*  877 */     assert (paramInt <= this.arity);
/*  878 */     int i = this.names.length;
/*  879 */     int j = paramArrayOfChar.length;
/*  880 */     Name[] arrayOfName = (Name[])Arrays.copyOf(this.names, i + j);
/*  881 */     int k = this.arity + j;
/*  882 */     int m = this.result;
/*  883 */     if (m >= this.arity) {
/*  884 */       m += j;
/*      */     }
/*  886 */     int n = paramInt + 1;
/*      */ 
/*  889 */     System.arraycopy(this.names, n, arrayOfName, n + j, i - n);
/*  890 */     for (int i1 = 0; i1 < j; i1++) {
/*  891 */       arrayOfName[(n + i1)] = new Name(paramArrayOfChar[i1]);
/*      */     }
/*  893 */     return new LambdaForm(this.debugName, k, arrayOfName, m);
/*      */   }
/*      */ 
/*      */   LambdaForm addArguments(int paramInt, List<Class<?>> paramList) {
/*  897 */     char[] arrayOfChar = new char[paramList.size()];
/*  898 */     for (int i = 0; i < arrayOfChar.length; i++)
/*  899 */       arrayOfChar[i] = basicType((Class)paramList.get(i));
/*  900 */     return addArguments(paramInt, arrayOfChar);
/*      */   }
/*      */ 
/*      */   LambdaForm permuteArguments(int paramInt, int[] paramArrayOfInt, char[] paramArrayOfChar)
/*      */   {
/*  906 */     int i = this.names.length;
/*  907 */     int j = paramArrayOfChar.length;
/*  908 */     int k = paramArrayOfInt.length;
/*  909 */     assert (paramInt + k == this.arity);
/*  910 */     assert (permutedTypesMatch(paramArrayOfInt, paramArrayOfChar, this.names, paramInt));
/*  911 */     int m = 0;
/*      */ 
/*  913 */     while ((m < k) && (paramArrayOfInt[m] == m)) m++;
/*  914 */     Name[] arrayOfName = new Name[i - k + j];
/*  915 */     System.arraycopy(this.names, 0, arrayOfName, 0, paramInt + m);
/*      */ 
/*  917 */     int n = i - this.arity;
/*  918 */     System.arraycopy(this.names, paramInt + k, arrayOfName, paramInt + j, n);
/*  919 */     int i1 = arrayOfName.length - n;
/*  920 */     int i2 = this.result;
/*  921 */     if (i2 >= 0)
/*  922 */       if (i2 < paramInt + k)
/*      */       {
/*  924 */         i2 = paramArrayOfInt[(i2 - paramInt)];
/*      */       }
/*  926 */       else i2 = i2 - k + j;
/*      */     Name localName3;
/*      */     int i6;
/*  930 */     for (int i3 = m; i3 < k; i3++) {
/*  931 */       Name localName1 = this.names[(paramInt + i3)];
/*  932 */       int i5 = paramArrayOfInt[i3];
/*      */ 
/*  934 */       localName3 = arrayOfName[(paramInt + i5)];
/*  935 */       if (localName3 == null)
/*      */       {
/*      */         void tmp256_253 = new Name(paramArrayOfChar[i5]); localName3 = tmp256_253; arrayOfName[(paramInt + i5)] = tmp256_253;
/*      */       } else {
/*  938 */         assert (localName3.type == paramArrayOfChar[i5]);
/*  939 */       }for (i6 = i1; i6 < arrayOfName.length; i6++) {
/*  940 */         arrayOfName[i6] = arrayOfName[i6].replaceName(localName1, localName3);
/*      */       }
/*      */     }
/*      */ 
/*  944 */     for (i3 = paramInt + m; i3 < i1; i3++) {
/*  945 */       if (arrayOfName[i3] == null)
/*  946 */         arrayOfName[i3] = argument(i3, paramArrayOfChar[(i3 - paramInt)]);
/*      */     }
/*  948 */     for (i3 = this.arity; i3 < this.names.length; i3++) {
/*  949 */       int i4 = i3 - this.arity + i1;
/*      */ 
/*  951 */       Name localName2 = this.names[i3];
/*  952 */       localName3 = arrayOfName[i4];
/*  953 */       if (localName2 != localName3) {
/*  954 */         for (i6 = i4 + 1; i6 < arrayOfName.length; i6++) {
/*  955 */           arrayOfName[i6] = arrayOfName[i6].replaceName(localName2, localName3);
/*      */         }
/*      */       }
/*      */     }
/*  959 */     return new LambdaForm(this.debugName, i1, arrayOfName, i2);
/*      */   }
/*      */ 
/*      */   static boolean permutedTypesMatch(int[] paramArrayOfInt, char[] paramArrayOfChar, Name[] paramArrayOfName, int paramInt) {
/*  963 */     int i = paramArrayOfChar.length;
/*  964 */     int j = paramArrayOfInt.length;
/*  965 */     for (int k = 0; k < j; k++) {
/*  966 */       assert (paramArrayOfName[(paramInt + k)].isParam());
/*  967 */       assert (paramArrayOfName[(paramInt + k)].type == paramArrayOfChar[paramArrayOfInt[k]]);
/*      */     }
/*  969 */     return true;
/*      */   }
/*      */ 
/*      */   void resolve()
/*      */   {
/* 1238 */     for (Name localName : this.names) localName.resolve(); 
/*      */   }
/*      */ 
/*      */   public static char basicType(Class<?> paramClass)
/*      */   {
/* 1242 */     char c = Wrapper.basicTypeChar(paramClass);
/* 1243 */     if ("ZBSC".indexOf(c) >= 0) c = 'I';
/* 1244 */     assert ("LIJFDV".indexOf(c) >= 0);
/* 1245 */     return c;
/*      */   }
/*      */   public static char[] basicTypes(List<Class<?>> paramList) {
/* 1248 */     char[] arrayOfChar = new char[paramList.size()];
/* 1249 */     for (int i = 0; i < arrayOfChar.length; i++) {
/* 1250 */       arrayOfChar[i] = basicType((Class)paramList.get(i));
/*      */     }
/* 1252 */     return arrayOfChar;
/*      */   }
/*      */   public static String basicTypeSignature(MethodType paramMethodType) {
/* 1255 */     char[] arrayOfChar = new char[paramMethodType.parameterCount() + 2];
/* 1256 */     int i = 0;
/* 1257 */     for (Class localClass : paramMethodType.parameterList()) {
/* 1258 */       arrayOfChar[(i++)] = basicType(localClass);
/*      */     }
/* 1260 */     arrayOfChar[(i++)] = '_';
/* 1261 */     arrayOfChar[(i++)] = basicType(paramMethodType.returnType());
/* 1262 */     assert (i == arrayOfChar.length);
/* 1263 */     return String.valueOf(arrayOfChar);
/*      */   }
/*      */ 
/*      */   static Name argument(int paramInt, char paramChar)
/*      */   {
/* 1479 */     int i = "LIJFD".indexOf(paramChar);
/* 1480 */     if ((i < 0) || (paramInt >= 10))
/* 1481 */       return new Name(paramInt, paramChar);
/* 1482 */     return INTERNED_ARGUMENTS[i][paramInt];
/*      */   }
/*      */   static Name internArgument(Name paramName) {
/* 1485 */     assert (paramName.isParam()) : ("not param: " + paramName);
/* 1486 */     assert (paramName.index < 10);
/* 1487 */     return argument(paramName.index, paramName.type);
/*      */   }
/*      */   static Name[] arguments(int paramInt, String paramString) {
/* 1490 */     int i = paramString.length();
/* 1491 */     Name[] arrayOfName = new Name[i + paramInt];
/* 1492 */     for (int j = 0; j < i; j++)
/* 1493 */       arrayOfName[j] = argument(j, paramString.charAt(j));
/* 1494 */     return arrayOfName;
/*      */   }
/*      */   static Name[] arguments(int paramInt, char[] paramArrayOfChar) {
/* 1497 */     int i = paramArrayOfChar.length;
/* 1498 */     Name[] arrayOfName = new Name[i + paramInt];
/* 1499 */     for (int j = 0; j < i; j++)
/* 1500 */       arrayOfName[j] = argument(j, paramArrayOfChar[j]);
/* 1501 */     return arrayOfName;
/*      */   }
/*      */   static Name[] arguments(int paramInt, List<Class<?>> paramList) {
/* 1504 */     int i = paramList.size();
/* 1505 */     Name[] arrayOfName = new Name[i + paramInt];
/* 1506 */     for (int j = 0; j < i; j++)
/* 1507 */       arrayOfName[j] = argument(j, basicType((Class)paramList.get(j)));
/* 1508 */     return arrayOfName;
/*      */   }
/*      */   static Name[] arguments(int paramInt, Class<?>[] paramArrayOfClass) {
/* 1511 */     int i = paramArrayOfClass.length;
/* 1512 */     Name[] arrayOfName = new Name[i + paramInt];
/* 1513 */     for (int j = 0; j < i; j++)
/* 1514 */       arrayOfName[j] = argument(j, basicType(paramArrayOfClass[j]));
/* 1515 */     return arrayOfName;
/*      */   }
/*      */   static Name[] arguments(int paramInt, MethodType paramMethodType) {
/* 1518 */     int i = paramMethodType.parameterCount();
/* 1519 */     Name[] arrayOfName = new Name[i + paramInt];
/* 1520 */     for (int j = 0; j < i; j++)
/* 1521 */       arrayOfName[j] = argument(j, basicType(paramMethodType.parameterType(j)));
/* 1522 */     return arrayOfName;
/*      */   }
/*      */ 
/*      */   static Name constantZero(int paramInt, char paramChar)
/*      */   {
/* 1540 */     return CONSTANT_ZERO["LIJFD".indexOf(paramChar)].newIndex(paramInt);
/*      */   }
/*      */ 
/*      */   private static int zeroI()
/*      */   {
/* 1563 */     return 0; } 
/* 1564 */   private static long zeroJ() { return 0L; } 
/* 1565 */   private static float zeroF() { return 0.0F; } 
/* 1566 */   private static double zeroD() { return 0.0D; } 
/* 1567 */   private static Object zeroL() { return null; }
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*  466 */     int i = 512;
/*  467 */     float f = 0.75F;
/*  468 */     char c = '\001';
/*  469 */     PREPARED_FORMS = new ConcurrentHashMap(i, f, c);
/*      */ 
/*  587 */     if (MethodHandleStatics.COMPILE_THRESHOLD != null)
/*  588 */       COMPILE_THRESHOLD = MethodHandleStatics.COMPILE_THRESHOLD.intValue();
/*      */     else {
/*  590 */       COMPILE_THRESHOLD = 30;
/*      */     }
/*      */ 
/* 1526 */     INTERNED_ARGUMENTS = new Name["LIJFD".length()][10];
/*      */     int j;
/* 1529 */     for (i = 0; i < "LIJFD".length(); i++) {
/* 1530 */       for (j = 0; j < INTERNED_ARGUMENTS[i].length; j++) {
/* 1531 */         c = "LIJFD".charAt(i);
/* 1532 */         INTERNED_ARGUMENTS[i][j] = new Name(j, c);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1537 */     IMPL_NAMES = MemberName.getFactory();
/*      */ 
/* 1542 */     CONSTANT_ZERO = new Name["LIJFD".length()];
/*      */ 
/* 1545 */     for (i = 0; i < "LIJFD".length(); i++) {
/* 1546 */       j = "LIJFD".charAt(i);
/* 1547 */       Wrapper localWrapper = Wrapper.forBasicType(j);
/* 1548 */       MemberName localMemberName = new MemberName(LambdaForm.class, "zero" + j, MethodType.methodType(localWrapper.primitiveType()), (byte)6);
/*      */       try {
/* 1550 */         localMemberName = IMPL_NAMES.resolveOrFail((byte)6, localMemberName, null, NoSuchMethodException.class);
/*      */       } catch (IllegalAccessException|NoSuchMethodException localIllegalAccessException) {
/* 1552 */         throw MethodHandleStatics.newInternalError(localIllegalAccessException);
/*      */       }
/* 1554 */       NamedFunction localNamedFunction = new NamedFunction(localMemberName);
/* 1555 */       Name localName = new Name(localNamedFunction, new Object[0]).newIndex(0);
/* 1556 */       assert (localName.type == "LIJFD".charAt(i));
/* 1557 */       CONSTANT_ZERO[i] = localName;
/* 1558 */       assert (localName.isConstantZero());
/*      */     }
/*      */ 
/* 1572 */     PREPARED_FORMS.putAll(computeInitialPreparedForms());
/*      */ 
/* 1624 */     NamedFunction.initializeInvokers();
/*      */   }
/*      */ 
/*      */   @Target({java.lang.annotation.ElementType.METHOD})
/*      */   @Retention(RetentionPolicy.RUNTIME)
/*      */   static @interface Compiled
/*      */   {
/*      */   }
/*      */ 
/*      */   @Target({java.lang.annotation.ElementType.METHOD})
/*      */   @Retention(RetentionPolicy.RUNTIME)
/*      */   static @interface Hidden
/*      */   {
/*      */   }
/*      */ 
/*      */   static final class Name
/*      */   {
/*      */     final char type;
/*      */     private short index;
/*      */     final LambdaForm.NamedFunction function;
/*      */     final Object[] arguments;
/*      */ 
/*      */     private Name(int paramInt, char paramChar, LambdaForm.NamedFunction paramNamedFunction, Object[] paramArrayOfObject)
/*      */     {
/* 1273 */       this.index = ((short)paramInt);
/* 1274 */       this.type = paramChar;
/* 1275 */       this.function = paramNamedFunction;
/* 1276 */       this.arguments = paramArrayOfObject;
/* 1277 */       assert (this.index == paramInt);
/*      */     }
/*      */     Name(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) {
/* 1280 */       this(new LambdaForm.NamedFunction(paramMethodHandle), paramArrayOfObject);
/*      */     }
/*      */     Name(MemberName paramMemberName, Object[] paramArrayOfObject) {
/* 1283 */       this(new LambdaForm.NamedFunction(paramMemberName), paramArrayOfObject);
/*      */     }
/*      */     Name(LambdaForm.NamedFunction paramNamedFunction, Object[] paramArrayOfObject) {
/* 1286 */       this(-1, paramNamedFunction.returnType(), paramNamedFunction, paramArrayOfObject = (Object[])paramArrayOfObject.clone());
/* 1287 */       assert (paramArrayOfObject.length == paramNamedFunction.arity()) : ("arity mismatch: arguments.length=" + paramArrayOfObject.length + " == function.arity()=" + paramNamedFunction.arity() + " in " + debugString());
/* 1288 */       for (int i = 0; i < paramArrayOfObject.length; i++)
/* 1289 */         assert (typesMatch(paramNamedFunction.parameterType(i), paramArrayOfObject[i])) : ("types don't match: function.parameterType(" + i + ")=" + paramNamedFunction.parameterType(i) + ", arguments[" + i + "]=" + paramArrayOfObject[i] + " in " + debugString()); 
/*      */     }
/*      */ 
/* 1292 */     Name(int paramInt, char paramChar) { this(paramInt, paramChar, null, null); }
/*      */ 
/*      */     Name(char paramChar) {
/* 1295 */       this(-1, paramChar);
/*      */     }
/*      */     char type() {
/* 1298 */       return this.type; } 
/* 1299 */     int index() { return this.index; } 
/*      */     boolean initIndex(int paramInt) {
/* 1301 */       if (this.index != paramInt) {
/* 1302 */         if (this.index != -1) return false;
/* 1303 */         this.index = ((short)paramInt);
/*      */       }
/* 1305 */       return true;
/*      */     }
/*      */ 
/*      */     void resolve()
/*      */     {
/* 1310 */       if (this.function != null)
/* 1311 */         this.function.resolve();
/*      */     }
/*      */ 
/*      */     Name newIndex(int paramInt) {
/* 1315 */       if (initIndex(paramInt)) return this;
/* 1316 */       return cloneWithIndex(paramInt);
/*      */     }
/*      */     Name cloneWithIndex(int paramInt) {
/* 1319 */       Object[] arrayOfObject = this.arguments == null ? null : (Object[])this.arguments.clone();
/* 1320 */       return new Name(paramInt, this.type, this.function, arrayOfObject);
/*      */     }
/*      */     Name replaceName(Name paramName1, Name paramName2) {
/* 1323 */       if (paramName1 == paramName2) return this;
/*      */ 
/* 1325 */       Object[] arrayOfObject = this.arguments;
/* 1326 */       if (arrayOfObject == null) return this;
/* 1327 */       int i = 0;
/* 1328 */       for (int j = 0; j < arrayOfObject.length; j++) {
/* 1329 */         if (arrayOfObject[j] == paramName1) {
/* 1330 */           if (i == 0) {
/* 1331 */             i = 1;
/* 1332 */             arrayOfObject = (Object[])arrayOfObject.clone();
/*      */           }
/* 1334 */           arrayOfObject[j] = paramName2;
/*      */         }
/*      */       }
/* 1337 */       if (i == 0) return this;
/* 1338 */       return new Name(this.function, arrayOfObject);
/*      */     }
/*      */ 
/*      */     Name replaceNames(Name[] paramArrayOfName1, Name[] paramArrayOfName2, int paramInt1, int paramInt2) {
/* 1342 */       Object[] arrayOfObject = this.arguments;
/* 1343 */       int i = 0;
/*      */ 
/* 1345 */       for (int j = 0; j < arrayOfObject.length; j++) {
/* 1346 */         if ((arrayOfObject[j] instanceof Name)) {
/* 1347 */           Name localName = (Name)arrayOfObject[j];
/* 1348 */           int k = localName.index;
/*      */ 
/* 1350 */           if ((k < 0) || (k >= paramArrayOfName2.length) || (localName != paramArrayOfName2[k]))
/*      */           {
/* 1353 */             for (int m = paramInt1; m < paramInt2; m++)
/* 1354 */               if (localName == paramArrayOfName1[m]) {
/* 1355 */                 if (localName == paramArrayOfName2[m])
/*      */                   break;
/* 1357 */                 if (i == 0) {
/* 1358 */                   i = 1;
/* 1359 */                   arrayOfObject = (Object[])arrayOfObject.clone();
/*      */                 }
/* 1361 */                 arrayOfObject[j] = paramArrayOfName2[m];
/* 1362 */                 break;
/*      */               }
/*      */           }
/*      */         }
/*      */       }
/* 1367 */       if (i == 0) return this;
/* 1368 */       return new Name(this.function, arrayOfObject);
/*      */     }
/*      */ 
/*      */     void internArguments() {
/* 1372 */       Object[] arrayOfObject = this.arguments;
/* 1373 */       for (int i = 0; i < arrayOfObject.length; i++)
/* 1374 */         if ((arrayOfObject[i] instanceof Name)) {
/* 1375 */           Name localName = (Name)arrayOfObject[i];
/* 1376 */           if ((localName.isParam()) && (localName.index < 10))
/* 1377 */             arrayOfObject[i] = LambdaForm.internArgument(localName);
/*      */         }
/*      */     }
/*      */ 
/*      */     boolean isParam() {
/* 1382 */       return this.function == null;
/*      */     }
/*      */     boolean isConstantZero() {
/* 1385 */       return (!isParam()) && (this.arguments.length == 0) && (this.function.equals(LambdaForm.constantZero(0, this.type).function));
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1389 */       return (isParam() ? "a" : "t") + (this.index >= 0 ? this.index : System.identityHashCode(this)) + ":" + this.type;
/*      */     }
/*      */     public String debugString() {
/* 1392 */       String str = toString();
/* 1393 */       return str + "=" + exprString();
/*      */     }
/*      */     public String exprString() {
/* 1396 */       if (this.function == null) return "null";
/* 1397 */       StringBuilder localStringBuilder = new StringBuilder(this.function.toString());
/* 1398 */       localStringBuilder.append("(");
/* 1399 */       String str = "";
/* 1400 */       for (Object localObject : this.arguments) {
/* 1401 */         localStringBuilder.append(str); str = ",";
/* 1402 */         if (((localObject instanceof Name)) || ((localObject instanceof Integer)))
/* 1403 */           localStringBuilder.append(localObject);
/*      */         else
/* 1405 */           localStringBuilder.append("(").append(localObject).append(")");
/*      */       }
/* 1407 */       localStringBuilder.append(")");
/* 1408 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     private static boolean typesMatch(char paramChar, Object paramObject) {
/* 1412 */       if ((paramObject instanceof Name)) {
/* 1413 */         return ((Name)paramObject).type == paramChar;
/*      */       }
/* 1415 */       switch (paramChar) { case 'I':
/* 1416 */         return paramObject instanceof Integer;
/*      */       case 'J':
/* 1417 */         return paramObject instanceof Long;
/*      */       case 'F':
/* 1418 */         return paramObject instanceof Float;
/*      */       case 'D':
/* 1419 */         return paramObject instanceof Double;
/*      */       case 'E':
/*      */       case 'G':
/* 1421 */       case 'H': } assert (paramChar == 'L');
/* 1422 */       return true;
/*      */     }
/*      */ 
/*      */     boolean isSiblingBindingBefore(Name paramName)
/*      */     {
/* 1433 */       assert (!paramName.isParam());
/* 1434 */       if (isParam()) return true;
/* 1435 */       if ((this.function.equals(paramName.function)) && (this.arguments.length == paramName.arguments.length))
/*      */       {
/* 1437 */         boolean bool = false;
/* 1438 */         for (int i = 0; i < this.arguments.length; i++) {
/* 1439 */           Object localObject1 = this.arguments[i];
/* 1440 */           Object localObject2 = paramName.arguments[i];
/* 1441 */           if (!localObject1.equals(localObject2)) {
/* 1442 */             if (((localObject1 instanceof Integer)) && ((localObject2 instanceof Integer))) {
/* 1443 */               if (!bool) { bool = true;
/* 1445 */                 if (((Integer)localObject1).intValue() < ((Integer)localObject2).intValue());
/*      */               }
/*      */             } else return false;
/*      */           }
/*      */         }
/* 1450 */         return bool;
/*      */       }
/* 1452 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean equals(Name paramName) {
/* 1456 */       if (this == paramName) return true;
/* 1457 */       if (isParam())
/*      */       {
/* 1459 */         return false;
/* 1460 */       }return (this.type == paramName.type) && (this.function.equals(paramName.function)) && (Arrays.equals(this.arguments, paramName.arguments));
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1468 */       return ((paramObject instanceof Name)) && (equals((Name)paramObject));
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/* 1472 */       if (isParam())
/* 1473 */         return this.index | this.type << '\b';
/* 1474 */       return this.function.hashCode() ^ Arrays.hashCode(this.arguments);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NamedFunction
/*      */   {
/*      */     final MemberName member;
/*      */     MethodHandle resolvedHandle;
/*      */     MethodHandle invoker;
/* 1115 */     static final MethodType INVOKER_METHOD_TYPE = MethodType.methodType(Object.class, MethodHandle.class, new Class[] { [Ljava.lang.Object.class });
/*      */ 
/*      */     NamedFunction(MethodHandle paramMethodHandle)
/*      */     {
/*  978 */       this(paramMethodHandle.internalMemberName(), paramMethodHandle);
/*      */     }
/*      */     NamedFunction(MemberName paramMemberName, MethodHandle paramMethodHandle) {
/*  981 */       this.member = paramMemberName;
/*      */ 
/*  983 */       this.resolvedHandle = paramMethodHandle;
/*      */     }
/*      */ 
/*      */     NamedFunction(Method paramMethod)
/*      */     {
/*  991 */       this(new MemberName(paramMethod));
/*      */     }
/*      */     NamedFunction(Field paramField) {
/*  994 */       this(new MemberName(paramField));
/*      */     }
/*      */     NamedFunction(MemberName paramMemberName) {
/*  997 */       this.member = paramMemberName;
/*  998 */       this.resolvedHandle = null;
/*      */     }
/*      */ 
/*      */     MethodHandle resolvedHandle() {
/* 1002 */       if (this.resolvedHandle == null) resolve();
/* 1003 */       return this.resolvedHandle;
/*      */     }
/*      */ 
/*      */     void resolve() {
/* 1007 */       this.resolvedHandle = DirectMethodHandle.make(this.member);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1012 */       if (this == paramObject) return true;
/* 1013 */       if (paramObject == null) return false;
/* 1014 */       if (!(paramObject instanceof NamedFunction)) return false;
/* 1015 */       NamedFunction localNamedFunction = (NamedFunction)paramObject;
/* 1016 */       return (this.member != null) && (this.member.equals(localNamedFunction.member));
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1021 */       if (this.member != null)
/* 1022 */         return this.member.hashCode();
/* 1023 */       return super.hashCode();
/*      */     }
/*      */ 
/*      */     static void initializeInvokers()
/*      */     {
/* 1028 */       for (MemberName localMemberName : MemberName.getFactory().getMethods(NamedFunction.class, false, null, null, null))
/* 1029 */         if ((localMemberName.isStatic()) && (localMemberName.isPackage())) {
/* 1030 */           MethodType localMethodType1 = localMemberName.getMethodType();
/* 1031 */           if ((localMethodType1.equals(INVOKER_METHOD_TYPE)) && (localMemberName.getName().startsWith("invoke_")))
/*      */           {
/* 1033 */             String str = localMemberName.getName().substring("invoke_".length());
/* 1034 */             int i = LambdaForm.signatureArity(str);
/* 1035 */             MethodType localMethodType2 = MethodType.genericMethodType(i);
/* 1036 */             if (LambdaForm.signatureReturn(str) == 'V')
/* 1037 */               localMethodType2 = localMethodType2.changeReturnType(Void.TYPE);
/* 1038 */             MethodTypeForm localMethodTypeForm = localMethodType2.form();
/* 1039 */             localMethodTypeForm.namedFunctionInvoker = DirectMethodHandle.make(localMemberName);
/*      */           }
/*      */         }
/*      */     }
/*      */ 
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke__V(MethodHandle paramMethodHandle, Object[] paramArrayOfObject)
/*      */       throws Throwable
/*      */     {
/* 1049 */       assert (paramArrayOfObject.length == 0);
/* 1050 */       paramMethodHandle.invokeBasic();
/* 1051 */       return null;
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_L_V(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1055 */       assert (paramArrayOfObject.length == 1);
/* 1056 */       paramMethodHandle.invokeBasic(paramArrayOfObject[0]);
/* 1057 */       return null;
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LL_V(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1061 */       assert (paramArrayOfObject.length == 2);
/* 1062 */       paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1]);
/* 1063 */       return null;
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LLL_V(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1067 */       assert (paramArrayOfObject.length == 3);
/* 1068 */       paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1], paramArrayOfObject[2]);
/* 1069 */       return null;
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LLLL_V(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1073 */       assert (paramArrayOfObject.length == 4);
/* 1074 */       paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1], paramArrayOfObject[2], paramArrayOfObject[3]);
/* 1075 */       return null;
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LLLLL_V(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1079 */       assert (paramArrayOfObject.length == 5);
/* 1080 */       paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1], paramArrayOfObject[2], paramArrayOfObject[3], paramArrayOfObject[4]);
/* 1081 */       return null;
/*      */     }
/*      */ 
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke__L(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1086 */       assert (paramArrayOfObject.length == 0);
/* 1087 */       return paramMethodHandle.invokeBasic();
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_L_L(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1091 */       assert (paramArrayOfObject.length == 1);
/* 1092 */       return paramMethodHandle.invokeBasic(paramArrayOfObject[0]);
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LL_L(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1096 */       assert (paramArrayOfObject.length == 2);
/* 1097 */       return paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1]);
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LLL_L(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1101 */       assert (paramArrayOfObject.length == 3);
/* 1102 */       return paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1], paramArrayOfObject[2]);
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LLLL_L(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1106 */       assert (paramArrayOfObject.length == 4);
/* 1107 */       return paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1], paramArrayOfObject[2], paramArrayOfObject[3]);
/*      */     }
/*      */     @LambdaForm.Hidden
/*      */     static Object invoke_LLLLL_L(MethodHandle paramMethodHandle, Object[] paramArrayOfObject) throws Throwable {
/* 1111 */       assert (paramArrayOfObject.length == 5);
/* 1112 */       return paramMethodHandle.invokeBasic(paramArrayOfObject[0], paramArrayOfObject[1], paramArrayOfObject[2], paramArrayOfObject[3], paramArrayOfObject[4]);
/*      */     }
/*      */ 
/*      */     private static MethodHandle computeInvoker(MethodTypeForm paramMethodTypeForm)
/*      */     {
/* 1119 */       Object localObject = paramMethodTypeForm.namedFunctionInvoker;
/* 1120 */       if (localObject != null) return localObject;
/* 1121 */       MemberName localMemberName = InvokerBytecodeGenerator.generateNamedFunctionInvoker(paramMethodTypeForm);
/* 1122 */       localObject = DirectMethodHandle.make(localMemberName);
/* 1123 */       MethodHandle localMethodHandle = paramMethodTypeForm.namedFunctionInvoker;
/* 1124 */       if (localMethodHandle != null) return localMethodHandle;
/* 1125 */       if (!((MethodHandle)localObject).type().equals(INVOKER_METHOD_TYPE))
/* 1126 */         throw new InternalError(((MethodHandle)localObject).debugString());
/* 1127 */       return paramMethodTypeForm.namedFunctionInvoker = localObject;
/*      */     }
/*      */ 
/*      */     @LambdaForm.Hidden
/*      */     Object invokeWithArguments(Object[] paramArrayOfObject)
/*      */       throws Throwable
/*      */     {
/* 1134 */       if (MethodHandleStatics.TRACE_INTERPRETER) return invokeWithArgumentsTracing(paramArrayOfObject);
/* 1135 */       assert (checkArgumentTypes(paramArrayOfObject, methodType()));
/* 1136 */       return invoker().invokeBasic(resolvedHandle(), paramArrayOfObject);
/*      */     }
/*      */ 
/*      */     @LambdaForm.Hidden
/*      */     Object invokeWithArgumentsTracing(Object[] paramArrayOfObject) throws Throwable {
/*      */       Object localObject;
/*      */       try {
/* 1143 */         LambdaForm.traceInterpreter("[ call", this, paramArrayOfObject);
/* 1144 */         if (this.invoker == null) {
/* 1145 */           LambdaForm.traceInterpreter("| getInvoker", this);
/* 1146 */           invoker();
/*      */         }
/* 1148 */         if (this.resolvedHandle == null) {
/* 1149 */           LambdaForm.traceInterpreter("| resolve", this);
/* 1150 */           resolvedHandle();
/*      */         }
/* 1152 */         assert (checkArgumentTypes(paramArrayOfObject, methodType()));
/* 1153 */         localObject = invoker().invokeBasic(resolvedHandle(), paramArrayOfObject);
/*      */       } catch (Throwable localThrowable) {
/* 1155 */         LambdaForm.traceInterpreter("] throw =>", localThrowable);
/* 1156 */         throw localThrowable;
/*      */       }
/* 1158 */       LambdaForm.traceInterpreter("] return =>", localObject);
/* 1159 */       return localObject;
/*      */     }
/*      */ 
/*      */     private MethodHandle invoker() {
/* 1163 */       if (this.invoker != null) return this.invoker;
/*      */ 
/* 1165 */       return this.invoker = computeInvoker(methodType().form());
/*      */     }
/*      */ 
/*      */     private static boolean checkArgumentTypes(Object[] paramArrayOfObject, MethodType paramMethodType) {
/* 1169 */       return true;
/*      */     }
/*      */ 
/*      */     String basicTypeSignature()
/*      */     {
/* 1187 */       return LambdaForm.basicTypeSignature(methodType());
/*      */     }
/*      */ 
/*      */     MethodType methodType() {
/* 1191 */       if (this.resolvedHandle != null) {
/* 1192 */         return this.resolvedHandle.type();
/*      */       }
/*      */ 
/* 1195 */       return this.member.getInvocationType();
/*      */     }
/*      */ 
/*      */     MemberName member() {
/* 1199 */       assert (assertMemberIsConsistent());
/* 1200 */       return this.member;
/*      */     }
/*      */ 
/*      */     private boolean assertMemberIsConsistent()
/*      */     {
/* 1205 */       if ((this.resolvedHandle instanceof DirectMethodHandle)) {
/* 1206 */         MemberName localMemberName = this.resolvedHandle.internalMemberName();
/* 1207 */         assert (localMemberName.equals(this.member));
/*      */       }
/* 1209 */       return true;
/*      */     }
/*      */ 
/*      */     Class<?> memberDeclaringClassOrNull() {
/* 1213 */       return this.member == null ? null : this.member.getDeclaringClass();
/*      */     }
/*      */ 
/*      */     char returnType() {
/* 1217 */       return LambdaForm.basicType(methodType().returnType());
/*      */     }
/*      */ 
/*      */     char parameterType(int paramInt) {
/* 1221 */       return LambdaForm.basicType(methodType().parameterType(paramInt));
/*      */     }
/*      */ 
/*      */     int arity()
/*      */     {
/* 1228 */       return methodType().parameterCount();
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1232 */       if (this.member == null) return this.resolvedHandle.toString();
/* 1233 */       return this.member.getDeclaringClass().getSimpleName() + "." + this.member.getName();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.LambdaForm
 * JD-Core Version:    0.6.2
 */