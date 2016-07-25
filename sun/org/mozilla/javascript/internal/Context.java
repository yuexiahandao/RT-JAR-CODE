/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.CharArrayWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import sun.org.mozilla.javascript.internal.ast.AstRoot;
/*      */ import sun.org.mozilla.javascript.internal.ast.ScriptNode;
/*      */ import sun.org.mozilla.javascript.internal.debug.DebuggableScript;
/*      */ import sun.org.mozilla.javascript.internal.debug.Debugger;
/*      */ import sun.org.mozilla.javascript.internal.xml.XMLLib;
/*      */ import sun.org.mozilla.javascript.internal.xml.XMLLib.Factory;
/*      */ 
/*      */ public final class Context
/*      */ {
/*   91 */   private static final boolean RHINO_DISABLED = Boolean.parseBoolean(SecurityUtilities.getSystemProperty("jdk.rhino.engine.disabled"));
/*      */   public static final int VERSION_UNKNOWN = -1;
/*      */   public static final int VERSION_DEFAULT = 0;
/*      */   public static final int VERSION_1_0 = 100;
/*      */   public static final int VERSION_1_1 = 110;
/*      */   public static final int VERSION_1_2 = 120;
/*      */   public static final int VERSION_1_3 = 130;
/*      */   public static final int VERSION_1_4 = 140;
/*      */   public static final int VERSION_1_5 = 150;
/*      */   public static final int VERSION_1_6 = 160;
/*      */   public static final int VERSION_1_7 = 170;
/*      */   public static final int VERSION_1_8 = 180;
/*      */   public static final int FEATURE_NON_ECMA_GET_YEAR = 1;
/*      */   public static final int FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME = 2;
/*      */   public static final int FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER = 3;
/*      */   public static final int FEATURE_TO_STRING_AS_SOURCE = 4;
/*      */   public static final int FEATURE_PARENT_PROTO_PROPERTIES = 5;
/*      */ 
/*      */   /** @deprecated */
/*      */   public static final int FEATURE_PARENT_PROTO_PROPRTIES = 5;
/*      */   public static final int FEATURE_E4X = 6;
/*      */   public static final int FEATURE_DYNAMIC_SCOPE = 7;
/*      */   public static final int FEATURE_STRICT_VARS = 8;
/*      */   public static final int FEATURE_STRICT_EVAL = 9;
/*      */   public static final int FEATURE_LOCATION_INFORMATION_IN_ERROR = 10;
/*      */   public static final int FEATURE_STRICT_MODE = 11;
/*      */   public static final int FEATURE_WARNING_AS_ERROR = 12;
/*      */   public static final int FEATURE_ENHANCED_JAVA_ACCESS = 13;
/*      */   public static final String languageVersionProperty = "language version";
/*      */   public static final String errorReporterProperty = "error reporter";
/*  333 */   public static final Object[] emptyArgs = ScriptRuntime.emptyArgs;
/*      */   private TimeZone thisContextTimeZone;
/*      */   private double LocalTZA;
/* 2497 */   private static Class<?> codegenClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.optimizer.Codegen");
/*      */ 
/* 2499 */   private static Class<?> interpreterClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.Interpreter");
/*      */   private static String implementationVersion;
/*      */   private final ContextFactory factory;
/*      */   private boolean sealed;
/*      */   private Object sealKey;
/*      */   Scriptable topCallScope;
/*      */   boolean isContinuationsTopCall;
/*      */   NativeCall currentActivationCall;
/*      */   XMLLib cachedXMLLib;
/*      */   ObjToIntMap iterating;
/*      */   Object interpreterSecurityDomain;
/*      */   int version;
/*      */   private SecurityController securityController;
/*      */   private boolean hasClassShutter;
/*      */   private ClassShutter classShutter;
/*      */   private ErrorReporter errorReporter;
/*      */   RegExpProxy regExpProxy;
/*      */   private Locale locale;
/*      */   private boolean generatingDebug;
/*      */   private boolean generatingDebugChanged;
/* 2672 */   private boolean generatingSource = true;
/*      */   boolean compileFunctionsWithDynamicScopeFlag;
/*      */   boolean useDynamicScope;
/*      */   private int optimizationLevel;
/*      */   private int maximumInterpreterStackDepth;
/*      */   private WrapFactory wrapFactory;
/*      */   Debugger debugger;
/*      */   private Object debuggerData;
/*      */   private int enterCount;
/*      */   private Object propertyListeners;
/*      */   private Map<Object, Object> threadLocalMap;
/*      */   private ClassLoader applicationClassLoader;
/*      */   Set<String> activationNames;
/*      */   Object lastInterpreterFrame;
/*      */   ObjArray previousInterpreterInvocations;
/*      */   int instructionCount;
/*      */   int instructionThreshold;
/*      */   int scratchIndex;
/*      */   long scratchUint32;
/*      */   Scriptable scratchScriptable;
/* 2712 */   public boolean generateObserverCount = false;
/*      */ 
/*      */   static void checkRhinoDisabled()
/*      */   {
/*   94 */     if (RHINO_DISABLED)
/*   95 */       throw new RuntimeException("rhino disabled");
/*      */   }
/*      */ 
/*      */   public void setTimeZone()
/*      */   {
/*  340 */     this.thisContextTimeZone = TimeZone.getDefault();
/*      */   }
/*      */   public TimeZone getTimeZone() {
/*  343 */     return this.thisContextTimeZone;
/*      */   }
/*      */   public void setLocalTZA() {
/*  346 */     this.LocalTZA = this.thisContextTimeZone.getRawOffset();
/*      */   }
/*      */   public double getLocalTZA() {
/*  349 */     return this.LocalTZA;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public Context()
/*      */   {
/*  367 */     this(ContextFactory.getGlobal());
/*      */   }
/*      */ 
/*      */   protected Context(ContextFactory paramContextFactory)
/*      */   {
/*  381 */     checkRhinoDisabled();
/*  382 */     if (paramContextFactory == null) {
/*  383 */       throw new IllegalArgumentException("factory == null");
/*      */     }
/*  385 */     this.factory = paramContextFactory;
/*  386 */     setLanguageVersion(0);
/*  387 */     this.optimizationLevel = (codegenClass != null ? 0 : -1);
/*  388 */     this.maximumInterpreterStackDepth = 2147483647;
/*      */   }
/*      */ 
/*      */   public static Context getCurrentContext()
/*      */   {
/*  405 */     Object localObject = VMBridge.instance.getThreadContextHelper();
/*  406 */     return VMBridge.instance.getContext(localObject);
/*      */   }
/*      */ 
/*      */   public static Context enter()
/*      */   {
/*  419 */     return enter(null);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Context enter(Context paramContext)
/*      */   {
/*  439 */     return enter(paramContext, ContextFactory.getGlobal());
/*      */   }
/*      */ 
/*      */   static final Context enter(Context paramContext, ContextFactory paramContextFactory)
/*      */   {
/*  444 */     Object localObject = VMBridge.instance.getThreadContextHelper();
/*  445 */     Context localContext = VMBridge.instance.getContext(localObject);
/*  446 */     if (localContext != null) {
/*  447 */       paramContext = localContext;
/*      */     } else {
/*  449 */       if (paramContext == null) {
/*  450 */         paramContext = paramContextFactory.makeContext();
/*  451 */         if (paramContext.enterCount != 0) {
/*  452 */           throw new IllegalStateException("factory.makeContext() returned Context instance already associated with some thread");
/*      */         }
/*  454 */         paramContextFactory.onContextCreated(paramContext);
/*  455 */         if ((paramContextFactory.isSealed()) && (!paramContext.isSealed())) {
/*  456 */           paramContext.seal(null);
/*      */         }
/*      */       }
/*  459 */       else if (paramContext.enterCount != 0) {
/*  460 */         throw new IllegalStateException("can not use Context instance already associated with some thread");
/*      */       }
/*      */ 
/*  463 */       VMBridge.instance.setContext(localObject, paramContext);
/*      */     }
/*  465 */     paramContext.enterCount += 1;
/*  466 */     return paramContext;
/*      */   }
/*      */ 
/*      */   public static void exit()
/*      */   {
/*  482 */     Object localObject = VMBridge.instance.getThreadContextHelper();
/*  483 */     Context localContext = VMBridge.instance.getContext(localObject);
/*  484 */     if (localContext == null) {
/*  485 */       throw new IllegalStateException("Calling Context.exit without previous Context.enter");
/*      */     }
/*      */ 
/*  488 */     if (localContext.enterCount < 1) Kit.codeBug();
/*  489 */     if (--localContext.enterCount == 0) {
/*  490 */       VMBridge.instance.setContext(localObject, null);
/*  491 */       localContext.factory.onContextReleased(localContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Object call(ContextAction paramContextAction)
/*      */   {
/*  510 */     return call(ContextFactory.getGlobal(), paramContextAction);
/*      */   }
/*      */ 
/*      */   public static Object call(ContextFactory paramContextFactory, Callable paramCallable, final Scriptable paramScriptable1, final Scriptable paramScriptable2, final Object[] paramArrayOfObject)
/*      */   {
/*  532 */     if (paramContextFactory == null) {
/*  533 */       paramContextFactory = ContextFactory.getGlobal();
/*      */     }
/*  535 */     return call(paramContextFactory, new ContextAction() {
/*      */       public Object run(Context paramAnonymousContext) {
/*  537 */         return this.val$callable.call(paramAnonymousContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   static Object call(ContextFactory paramContextFactory, ContextAction paramContextAction)
/*      */   {
/*  546 */     Context localContext = enter(null, paramContextFactory);
/*      */     try {
/*  548 */       return paramContextAction.run(localContext);
/*      */     }
/*      */     finally {
/*  551 */       exit();
/*      */     }
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static void addContextListener(ContextListener paramContextListener)
/*      */   {
/*  563 */     String str = "sun.org.mozilla.javascript.internal.tools.debugger.Main";
/*  564 */     if (str.equals(paramContextListener.getClass().getName())) {
/*  565 */       Class localClass1 = paramContextListener.getClass();
/*  566 */       Class localClass2 = Kit.classOrNull("sun.org.mozilla.javascript.internal.ContextFactory");
/*      */ 
/*  568 */       Class[] arrayOfClass = { localClass2 };
/*  569 */       Object[] arrayOfObject = { ContextFactory.getGlobal() };
/*      */       try {
/*  571 */         Method localMethod = localClass1.getMethod("attachTo", arrayOfClass);
/*  572 */         localMethod.invoke(paramContextListener, arrayOfObject);
/*      */       } catch (Exception localException) {
/*  574 */         RuntimeException localRuntimeException = new RuntimeException();
/*  575 */         Kit.initCause(localRuntimeException, localException);
/*  576 */         throw localRuntimeException;
/*      */       }
/*  578 */       return;
/*      */     }
/*      */ 
/*  581 */     ContextFactory.getGlobal().addListener(paramContextListener);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static void removeContextListener(ContextListener paramContextListener)
/*      */   {
/*  591 */     ContextFactory.getGlobal().addListener(paramContextListener);
/*      */   }
/*      */ 
/*      */   public final ContextFactory getFactory()
/*      */   {
/*  599 */     return this.factory;
/*      */   }
/*      */ 
/*      */   public final boolean isSealed()
/*      */   {
/*  610 */     return this.sealed;
/*      */   }
/*      */ 
/*      */   public final void seal(Object paramObject)
/*      */   {
/*  627 */     if (this.sealed) onSealedMutation();
/*  628 */     this.sealed = true;
/*  629 */     this.sealKey = paramObject;
/*      */   }
/*      */ 
/*      */   public final void unseal(Object paramObject)
/*      */   {
/*  643 */     if (paramObject == null) throw new IllegalArgumentException();
/*  644 */     if (this.sealKey != paramObject) throw new IllegalArgumentException();
/*  645 */     if (!this.sealed) throw new IllegalStateException();
/*  646 */     this.sealed = false;
/*  647 */     this.sealKey = null;
/*      */   }
/*      */ 
/*      */   static void onSealedMutation()
/*      */   {
/*  652 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   public final int getLanguageVersion()
/*      */   {
/*  665 */     return this.version;
/*      */   }
/*      */ 
/*      */   public void setLanguageVersion(int paramInt)
/*      */   {
/*  680 */     if (this.sealed) onSealedMutation();
/*  681 */     checkLanguageVersion(paramInt);
/*  682 */     Object localObject = this.propertyListeners;
/*  683 */     if ((localObject != null) && (paramInt != this.version)) {
/*  684 */       firePropertyChangeImpl(localObject, "language version", Integer.valueOf(this.version), Integer.valueOf(paramInt));
/*      */     }
/*      */ 
/*  688 */     this.version = paramInt;
/*      */   }
/*      */ 
/*      */   public static boolean isValidLanguageVersion(int paramInt)
/*      */   {
/*  693 */     switch (paramInt) {
/*      */     case 0:
/*      */     case 100:
/*      */     case 110:
/*      */     case 120:
/*      */     case 130:
/*      */     case 140:
/*      */     case 150:
/*      */     case 160:
/*      */     case 170:
/*      */     case 180:
/*  704 */       return true;
/*      */     }
/*  706 */     return false;
/*      */   }
/*      */ 
/*      */   public static void checkLanguageVersion(int paramInt)
/*      */   {
/*  711 */     if (isValidLanguageVersion(paramInt)) {
/*  712 */       return;
/*      */     }
/*  714 */     throw new IllegalArgumentException("Bad language version: " + paramInt);
/*      */   }
/*      */ 
/*      */   public final String getImplementationVersion()
/*      */   {
/*  738 */     if (implementationVersion == null) {
/*  739 */       implementationVersion = ScriptRuntime.getMessage0("implementation.version");
/*      */     }
/*      */ 
/*  742 */     return implementationVersion;
/*      */   }
/*      */ 
/*      */   public final ErrorReporter getErrorReporter()
/*      */   {
/*  752 */     if (this.errorReporter == null) {
/*  753 */       return DefaultErrorReporter.instance;
/*      */     }
/*  755 */     return this.errorReporter;
/*      */   }
/*      */ 
/*      */   public final ErrorReporter setErrorReporter(ErrorReporter paramErrorReporter)
/*      */   {
/*  766 */     if (this.sealed) onSealedMutation();
/*  767 */     if (paramErrorReporter == null) throw new IllegalArgumentException();
/*  768 */     ErrorReporter localErrorReporter = getErrorReporter();
/*  769 */     if (paramErrorReporter == localErrorReporter) {
/*  770 */       return localErrorReporter;
/*      */     }
/*  772 */     Object localObject = this.propertyListeners;
/*  773 */     if (localObject != null) {
/*  774 */       firePropertyChangeImpl(localObject, "error reporter", localErrorReporter, paramErrorReporter);
/*      */     }
/*      */ 
/*  777 */     this.errorReporter = paramErrorReporter;
/*  778 */     return localErrorReporter;
/*      */   }
/*      */ 
/*      */   public final Locale getLocale()
/*      */   {
/*  790 */     if (this.locale == null)
/*  791 */       this.locale = Locale.getDefault();
/*  792 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public final Locale setLocale(Locale paramLocale)
/*      */   {
/*  802 */     if (this.sealed) onSealedMutation();
/*  803 */     Locale localLocale = this.locale;
/*  804 */     this.locale = paramLocale;
/*  805 */     return localLocale;
/*      */   }
/*      */ 
/*      */   public final void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/*  817 */     if (this.sealed) onSealedMutation();
/*  818 */     this.propertyListeners = Kit.addListener(this.propertyListeners, paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public final void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/*  830 */     if (this.sealed) onSealedMutation();
/*  831 */     this.propertyListeners = Kit.removeListener(this.propertyListeners, paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   final void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*      */   {
/*  847 */     Object localObject = this.propertyListeners;
/*  848 */     if (localObject != null)
/*  849 */       firePropertyChangeImpl(localObject, paramString, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   private void firePropertyChangeImpl(Object paramObject1, String paramString, Object paramObject2, Object paramObject3)
/*      */   {
/*  856 */     for (int i = 0; ; i++) {
/*  857 */       Object localObject = Kit.getListener(paramObject1, i);
/*  858 */       if (localObject == null)
/*      */         break;
/*  860 */       if ((localObject instanceof PropertyChangeListener)) {
/*  861 */         PropertyChangeListener localPropertyChangeListener = (PropertyChangeListener)localObject;
/*  862 */         localPropertyChangeListener.propertyChange(new PropertyChangeEvent(this, paramString, paramObject2, paramObject3));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void reportWarning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*      */   {
/*  882 */     Context localContext = getContext();
/*  883 */     if (localContext.hasFeature(12))
/*  884 */       reportError(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*      */     else
/*  886 */       localContext.getErrorReporter().warning(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*      */   }
/*      */ 
/*      */   public static void reportWarning(String paramString)
/*      */   {
/*  898 */     int[] arrayOfInt = { 0 };
/*  899 */     String str = getSourcePositionFromStack(arrayOfInt);
/*  900 */     reportWarning(paramString, str, arrayOfInt[0], null, 0);
/*      */   }
/*      */ 
/*      */   public static void reportWarning(String paramString, Throwable paramThrowable)
/*      */   {
/*  905 */     int[] arrayOfInt = { 0 };
/*  906 */     String str = getSourcePositionFromStack(arrayOfInt);
/*  907 */     StringWriter localStringWriter = new StringWriter();
/*  908 */     PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
/*  909 */     localPrintWriter.println(paramString);
/*  910 */     paramThrowable.printStackTrace(localPrintWriter);
/*  911 */     localPrintWriter.flush();
/*  912 */     reportWarning(localStringWriter.toString(), str, arrayOfInt[0], null, 0);
/*      */   }
/*      */ 
/*      */   public static void reportError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*      */   {
/*  929 */     Context localContext = getCurrentContext();
/*  930 */     if (localContext != null) {
/*  931 */       localContext.getErrorReporter().error(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*      */     }
/*      */     else
/*  934 */       throw new EvaluatorException(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*      */   }
/*      */ 
/*      */   public static void reportError(String paramString)
/*      */   {
/*  947 */     int[] arrayOfInt = { 0 };
/*  948 */     String str = getSourcePositionFromStack(arrayOfInt);
/*  949 */     reportError(paramString, str, arrayOfInt[0], null, 0);
/*      */   }
/*      */ 
/*      */   public static EvaluatorException reportRuntimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
/*      */   {
/*  970 */     Context localContext = getCurrentContext();
/*  971 */     if (localContext != null) {
/*  972 */       return localContext.getErrorReporter().runtimeError(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*      */     }
/*      */ 
/*  976 */     throw new EvaluatorException(paramString1, paramString2, paramInt1, paramString3, paramInt2);
/*      */   }
/*      */ 
/*      */   static EvaluatorException reportRuntimeError0(String paramString)
/*      */   {
/*  983 */     String str = ScriptRuntime.getMessage0(paramString);
/*  984 */     return reportRuntimeError(str);
/*      */   }
/*      */ 
/*      */   static EvaluatorException reportRuntimeError1(String paramString, Object paramObject)
/*      */   {
/*  990 */     String str = ScriptRuntime.getMessage1(paramString, paramObject);
/*  991 */     return reportRuntimeError(str);
/*      */   }
/*      */ 
/*      */   static EvaluatorException reportRuntimeError2(String paramString, Object paramObject1, Object paramObject2)
/*      */   {
/*  997 */     String str = ScriptRuntime.getMessage2(paramString, paramObject1, paramObject2);
/*  998 */     return reportRuntimeError(str);
/*      */   }
/*      */ 
/*      */   static EvaluatorException reportRuntimeError3(String paramString, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 1005 */     String str = ScriptRuntime.getMessage3(paramString, paramObject1, paramObject2, paramObject3);
/* 1006 */     return reportRuntimeError(str);
/*      */   }
/*      */ 
/*      */   static EvaluatorException reportRuntimeError4(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4)
/*      */   {
/* 1013 */     String str = ScriptRuntime.getMessage4(paramString, paramObject1, paramObject2, paramObject3, paramObject4);
/*      */ 
/* 1015 */     return reportRuntimeError(str);
/*      */   }
/*      */ 
/*      */   public static EvaluatorException reportRuntimeError(String paramString)
/*      */   {
/* 1026 */     int[] arrayOfInt = { 0 };
/* 1027 */     String str = getSourcePositionFromStack(arrayOfInt);
/* 1028 */     return reportRuntimeError(paramString, str, arrayOfInt[0], null, 0);
/*      */   }
/*      */ 
/*      */   public final ScriptableObject initStandardObjects()
/*      */   {
/* 1047 */     return initStandardObjects(null, false);
/*      */   }
/*      */ 
/*      */   public final Scriptable initStandardObjects(ScriptableObject paramScriptableObject)
/*      */   {
/* 1070 */     return initStandardObjects(paramScriptableObject, false);
/*      */   }
/*      */ 
/*      */   public ScriptableObject initStandardObjects(ScriptableObject paramScriptableObject, boolean paramBoolean)
/*      */   {
/* 1103 */     return ScriptRuntime.initStandardObjects(this, paramScriptableObject, paramBoolean);
/*      */   }
/*      */ 
/*      */   public static Object getUndefinedValue()
/*      */   {
/* 1111 */     return Undefined.instance;
/*      */   }
/*      */ 
/*      */   public final Object evaluateString(Scriptable paramScriptable, String paramString1, String paramString2, int paramInt, Object paramObject)
/*      */   {
/* 1135 */     Script localScript = compileString(paramString1, paramString2, paramInt, paramObject);
/*      */ 
/* 1137 */     if (localScript != null) {
/* 1138 */       return localScript.exec(this, paramScriptable);
/*      */     }
/* 1140 */     return null;
/*      */   }
/*      */ 
/*      */   public final Object evaluateReader(Scriptable paramScriptable, Reader paramReader, String paramString, int paramInt, Object paramObject)
/*      */     throws IOException
/*      */   {
/* 1166 */     Script localScript = compileReader(paramScriptable, paramReader, paramString, paramInt, paramObject);
/*      */ 
/* 1168 */     if (localScript != null) {
/* 1169 */       return localScript.exec(this, paramScriptable);
/*      */     }
/* 1171 */     return null;
/*      */   }
/*      */ 
/*      */   public Object executeScriptWithContinuations(Script paramScript, Scriptable paramScriptable)
/*      */     throws ContinuationPending
/*      */   {
/* 1191 */     if ((!(paramScript instanceof InterpretedFunction)) || (!((InterpretedFunction)paramScript).isScript()))
/*      */     {
/* 1195 */       throw new IllegalArgumentException("Script argument was not a script or was not created by interpreted mode ");
/*      */     }
/*      */ 
/* 1198 */     return callFunctionWithContinuations((InterpretedFunction)paramScript, paramScriptable, ScriptRuntime.emptyArgs);
/*      */   }
/*      */ 
/*      */   public Object callFunctionWithContinuations(Callable paramCallable, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */     throws ContinuationPending
/*      */   {
/* 1219 */     if (!(paramCallable instanceof InterpretedFunction))
/*      */     {
/* 1221 */       throw new IllegalArgumentException("Function argument was not created by interpreted mode ");
/*      */     }
/*      */ 
/* 1224 */     if (ScriptRuntime.hasTopCall(this)) {
/* 1225 */       throw new IllegalStateException("Cannot have any pending top calls when executing a script with continuations");
/*      */     }
/*      */ 
/* 1230 */     this.isContinuationsTopCall = true;
/* 1231 */     return ScriptRuntime.doTopCall(paramCallable, this, paramScriptable, paramScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public ContinuationPending captureContinuation()
/*      */   {
/* 1248 */     return new ContinuationPending(Interpreter.captureContinuation(this));
/*      */   }
/*      */ 
/*      */   public Object resumeContinuation(Object paramObject1, Scriptable paramScriptable, Object paramObject2)
/*      */     throws ContinuationPending
/*      */   {
/* 1272 */     Object[] arrayOfObject = { paramObject2 };
/* 1273 */     return Interpreter.restartContinuation((NativeContinuation)paramObject1, this, paramScriptable, arrayOfObject);
/*      */   }
/*      */ 
/*      */   public final boolean stringIsCompilableUnit(String paramString)
/*      */   {
/* 1296 */     int i = 0;
/* 1297 */     CompilerEnvirons localCompilerEnvirons = new CompilerEnvirons();
/* 1298 */     localCompilerEnvirons.initFromContext(this);
/*      */ 
/* 1301 */     localCompilerEnvirons.setGeneratingSource(false);
/* 1302 */     Parser localParser = new Parser(localCompilerEnvirons, DefaultErrorReporter.instance);
/*      */     try {
/* 1304 */       localParser.parse(paramString, null, 1);
/*      */     } catch (EvaluatorException localEvaluatorException) {
/* 1306 */       i = 1;
/*      */     }
/*      */ 
/* 1311 */     if ((i != 0) && (localParser.eof())) {
/* 1312 */       return false;
/*      */     }
/* 1314 */     return true;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public final Script compileReader(Scriptable paramScriptable, Reader paramReader, String paramString, int paramInt, Object paramObject)
/*      */     throws IOException
/*      */   {
/* 1327 */     return compileReader(paramReader, paramString, paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   public final Script compileReader(Reader paramReader, String paramString, int paramInt, Object paramObject)
/*      */     throws IOException
/*      */   {
/* 1351 */     if (paramInt < 0)
/*      */     {
/* 1353 */       paramInt = 0;
/*      */     }
/* 1355 */     return (Script)compileImpl(null, paramReader, null, paramString, paramInt, paramObject, false, null, null);
/*      */   }
/*      */ 
/*      */   public final Script compileString(String paramString1, String paramString2, int paramInt, Object paramObject)
/*      */   {
/* 1379 */     if (paramInt < 0)
/*      */     {
/* 1381 */       paramInt = 0;
/*      */     }
/* 1383 */     return compileString(paramString1, null, null, paramString2, paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   final Script compileString(String paramString1, Evaluator paramEvaluator, ErrorReporter paramErrorReporter, String paramString2, int paramInt, Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1394 */       return (Script)compileImpl(null, null, paramString1, paramString2, paramInt, paramObject, false, paramEvaluator, paramErrorReporter);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/* 1399 */     throw new RuntimeException();
/*      */   }
/*      */ 
/*      */   public final Function compileFunction(Scriptable paramScriptable, String paramString1, String paramString2, int paramInt, Object paramObject)
/*      */   {
/* 1424 */     return compileFunction(paramScriptable, paramString1, null, null, paramString2, paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   final Function compileFunction(Scriptable paramScriptable, String paramString1, Evaluator paramEvaluator, ErrorReporter paramErrorReporter, String paramString2, int paramInt, Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1435 */       return (Function)compileImpl(paramScriptable, null, paramString1, paramString2, paramInt, paramObject, true, paramEvaluator, paramErrorReporter);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */ 
/* 1442 */     throw new RuntimeException();
/*      */   }
/*      */ 
/*      */   public final String decompileScript(Script paramScript, int paramInt)
/*      */   {
/* 1457 */     NativeFunction localNativeFunction = (NativeFunction)paramScript;
/* 1458 */     return localNativeFunction.decompile(paramInt, 0);
/*      */   }
/*      */ 
/*      */   public final String decompileFunction(Function paramFunction, int paramInt)
/*      */   {
/* 1476 */     if ((paramFunction instanceof BaseFunction)) {
/* 1477 */       return ((BaseFunction)paramFunction).decompile(paramInt, 0);
/*      */     }
/* 1479 */     return "function " + paramFunction.getClassName() + "() {\n\t[native code]\n}\n";
/*      */   }
/*      */ 
/*      */   public final String decompileFunctionBody(Function paramFunction, int paramInt)
/*      */   {
/* 1498 */     if ((paramFunction instanceof BaseFunction)) {
/* 1499 */       BaseFunction localBaseFunction = (BaseFunction)paramFunction;
/* 1500 */       return localBaseFunction.decompile(paramInt, 1);
/*      */     }
/*      */ 
/* 1503 */     return "[native code]\n";
/*      */   }
/*      */ 
/*      */   public final Scriptable newObject(Scriptable paramScriptable)
/*      */   {
/* 1516 */     return newObject(paramScriptable, "Object", ScriptRuntime.emptyArgs);
/*      */   }
/*      */ 
/*      */   public final Scriptable newObject(Scriptable paramScriptable, String paramString)
/*      */   {
/* 1531 */     return newObject(paramScriptable, paramString, ScriptRuntime.emptyArgs);
/*      */   }
/*      */ 
/*      */   public final Scriptable newObject(Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 1556 */     paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/* 1557 */     Function localFunction = ScriptRuntime.getExistingCtor(this, paramScriptable, paramString);
/*      */ 
/* 1559 */     if (paramArrayOfObject == null) paramArrayOfObject = ScriptRuntime.emptyArgs;
/* 1560 */     return localFunction.construct(this, paramScriptable, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public final Scriptable newArray(Scriptable paramScriptable, int paramInt)
/*      */   {
/* 1573 */     NativeArray localNativeArray = new NativeArray(paramInt);
/* 1574 */     ScriptRuntime.setObjectProtoAndParent(localNativeArray, paramScriptable);
/* 1575 */     return localNativeArray;
/*      */   }
/*      */ 
/*      */   public final Scriptable newArray(Scriptable paramScriptable, Object[] paramArrayOfObject)
/*      */   {
/* 1590 */     if (paramArrayOfObject.getClass().getComponentType() != ScriptRuntime.ObjectClass)
/* 1591 */       throw new IllegalArgumentException();
/* 1592 */     NativeArray localNativeArray = new NativeArray(paramArrayOfObject);
/* 1593 */     ScriptRuntime.setObjectProtoAndParent(localNativeArray, paramScriptable);
/* 1594 */     return localNativeArray;
/*      */   }
/*      */ 
/*      */   public final Object[] getElements(Scriptable paramScriptable)
/*      */   {
/* 1616 */     return ScriptRuntime.getArrayElements(paramScriptable);
/*      */   }
/*      */ 
/*      */   public static boolean toBoolean(Object paramObject)
/*      */   {
/* 1630 */     return ScriptRuntime.toBoolean(paramObject);
/*      */   }
/*      */ 
/*      */   public static double toNumber(Object paramObject)
/*      */   {
/* 1646 */     return ScriptRuntime.toNumber(paramObject);
/*      */   }
/*      */ 
/*      */   public static String toString(Object paramObject)
/*      */   {
/* 1660 */     return ScriptRuntime.toString(paramObject);
/*      */   }
/*      */ 
/*      */   public static Scriptable toObject(Object paramObject, Scriptable paramScriptable)
/*      */   {
/* 1682 */     return ScriptRuntime.toObject(paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Scriptable toObject(Object paramObject, Scriptable paramScriptable, Class<?> paramClass)
/*      */   {
/* 1692 */     return ScriptRuntime.toObject(paramScriptable, paramObject);
/*      */   }
/*      */ 
/*      */   public static Object javaToJS(Object paramObject, Scriptable paramScriptable)
/*      */   {
/* 1725 */     if (((paramObject instanceof String)) || ((paramObject instanceof Number)) || ((paramObject instanceof Boolean)) || ((paramObject instanceof Scriptable)))
/*      */     {
/* 1728 */       return paramObject;
/* 1729 */     }if ((paramObject instanceof Character)) {
/* 1730 */       return String.valueOf(((Character)paramObject).charValue());
/*      */     }
/* 1732 */     Context localContext = getContext();
/* 1733 */     return localContext.getWrapFactory().wrap(localContext, paramScriptable, paramObject, null);
/*      */   }
/*      */ 
/*      */   public static Object jsToJava(Object paramObject, Class<?> paramClass)
/*      */     throws EvaluatorException
/*      */   {
/* 1751 */     return NativeJavaObject.coerceTypeImpl(paramClass, paramObject);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static Object toType(Object paramObject, Class<?> paramClass)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     try
/*      */     {
/* 1765 */       return jsToJava(paramObject, paramClass);
/*      */     }
/*      */     catch (EvaluatorException localEvaluatorException) {
/* 1768 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(localEvaluatorException.getMessage());
/* 1769 */       Kit.initCause(localIllegalArgumentException, localEvaluatorException);
/* 1770 */       throw localIllegalArgumentException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static RuntimeException throwAsScriptRuntimeEx(Throwable paramThrowable)
/*      */   {
/* 1793 */     while ((paramThrowable instanceof InvocationTargetException)) {
/* 1794 */       paramThrowable = ((InvocationTargetException)paramThrowable).getTargetException();
/*      */     }
/*      */ 
/* 1797 */     if ((paramThrowable instanceof Error)) {
/* 1798 */       Context localContext = getContext();
/* 1799 */       if ((localContext == null) || (!localContext.hasFeature(13)))
/*      */       {
/* 1802 */         throw ((Error)paramThrowable);
/*      */       }
/*      */     }
/* 1805 */     if ((paramThrowable instanceof RhinoException)) {
/* 1806 */       throw ((RhinoException)paramThrowable);
/*      */     }
/* 1808 */     throw new WrappedException(paramThrowable);
/*      */   }
/*      */ 
/*      */   public final boolean isGeneratingDebug()
/*      */   {
/* 1817 */     return this.generatingDebug;
/*      */   }
/*      */ 
/*      */   public final void setGeneratingDebug(boolean paramBoolean)
/*      */   {
/* 1829 */     if (this.sealed) onSealedMutation();
/* 1830 */     this.generatingDebugChanged = true;
/* 1831 */     if ((paramBoolean) && (getOptimizationLevel() > 0))
/* 1832 */       setOptimizationLevel(0);
/* 1833 */     this.generatingDebug = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean isGeneratingSource()
/*      */   {
/* 1842 */     return this.generatingSource;
/*      */   }
/*      */ 
/*      */   public final void setGeneratingSource(boolean paramBoolean)
/*      */   {
/* 1857 */     if (this.sealed) onSealedMutation();
/* 1858 */     this.generatingSource = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final int getOptimizationLevel()
/*      */   {
/* 1871 */     return this.optimizationLevel;
/*      */   }
/*      */ 
/*      */   public final void setOptimizationLevel(int paramInt)
/*      */   {
/* 1893 */     if (this.sealed) onSealedMutation();
/* 1894 */     if (paramInt == -2)
/*      */     {
/* 1896 */       paramInt = -1;
/*      */     }
/* 1898 */     checkOptimizationLevel(paramInt);
/* 1899 */     if (codegenClass == null)
/* 1900 */       paramInt = -1;
/* 1901 */     this.optimizationLevel = paramInt;
/*      */   }
/*      */ 
/*      */   public static boolean isValidOptimizationLevel(int paramInt)
/*      */   {
/* 1906 */     return (-1 <= paramInt) && (paramInt <= 9);
/*      */   }
/*      */ 
/*      */   public static void checkOptimizationLevel(int paramInt)
/*      */   {
/* 1911 */     if (isValidOptimizationLevel(paramInt)) {
/* 1912 */       return;
/*      */     }
/* 1914 */     throw new IllegalArgumentException("Optimization level outside [-1..9]: " + paramInt);
/*      */   }
/*      */ 
/*      */   public final int getMaximumInterpreterStackDepth()
/*      */   {
/* 1934 */     return this.maximumInterpreterStackDepth;
/*      */   }
/*      */ 
/*      */   public final void setMaximumInterpreterStackDepth(int paramInt)
/*      */   {
/* 1956 */     if (this.sealed) onSealedMutation();
/* 1957 */     if (this.optimizationLevel != -1) {
/* 1958 */       throw new IllegalStateException("Cannot set maximumInterpreterStackDepth when optimizationLevel != -1");
/*      */     }
/* 1960 */     if (paramInt < 1) {
/* 1961 */       throw new IllegalArgumentException("Cannot set maximumInterpreterStackDepth to less than 1");
/*      */     }
/* 1963 */     this.maximumInterpreterStackDepth = paramInt;
/*      */   }
/*      */ 
/*      */   public final void setSecurityController(SecurityController paramSecurityController)
/*      */   {
/* 1979 */     if (this.sealed) onSealedMutation();
/* 1980 */     if (paramSecurityController == null) throw new IllegalArgumentException();
/* 1981 */     if (this.securityController != null) {
/* 1982 */       throw new SecurityException("Can not overwrite existing SecurityController object");
/*      */     }
/* 1984 */     if (SecurityController.hasGlobal()) {
/* 1985 */       throw new SecurityException("Can not overwrite existing global SecurityController object");
/*      */     }
/* 1987 */     this.securityController = paramSecurityController;
/*      */   }
/*      */ 
/*      */   public final synchronized void setClassShutter(ClassShutter paramClassShutter)
/*      */   {
/* 2000 */     if (this.sealed) onSealedMutation();
/* 2001 */     if (paramClassShutter == null) throw new IllegalArgumentException();
/* 2002 */     if (this.hasClassShutter) {
/* 2003 */       throw new SecurityException("Cannot overwrite existing ClassShutter object");
/*      */     }
/*      */ 
/* 2006 */     this.classShutter = paramClassShutter;
/* 2007 */     this.hasClassShutter = true;
/*      */   }
/*      */ 
/*      */   final synchronized ClassShutter getClassShutter()
/*      */   {
/* 2012 */     return this.classShutter;
/*      */   }
/*      */ 
/*      */   public final synchronized ClassShutterSetter getClassShutterSetter()
/*      */   {
/* 2021 */     if (this.hasClassShutter)
/* 2022 */       return null;
/* 2023 */     this.hasClassShutter = true;
/* 2024 */     return new ClassShutterSetter() {
/*      */       public void setClassShutter(ClassShutter paramAnonymousClassShutter) {
/* 2026 */         Context.this.classShutter = paramAnonymousClassShutter;
/*      */       }
/*      */       public ClassShutter getClassShutter() {
/* 2029 */         return Context.this.classShutter;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public final Object getThreadLocal(Object paramObject)
/*      */   {
/* 2051 */     if (this.threadLocalMap == null)
/* 2052 */       return null;
/* 2053 */     return this.threadLocalMap.get(paramObject);
/*      */   }
/*      */ 
/*      */   public final synchronized void putThreadLocal(Object paramObject1, Object paramObject2)
/*      */   {
/* 2064 */     if (this.sealed) onSealedMutation();
/* 2065 */     if (this.threadLocalMap == null)
/* 2066 */       this.threadLocalMap = new HashMap();
/* 2067 */     this.threadLocalMap.put(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public final void removeThreadLocal(Object paramObject)
/*      */   {
/* 2077 */     if (this.sealed) onSealedMutation();
/* 2078 */     if (this.threadLocalMap == null)
/* 2079 */       return;
/* 2080 */     this.threadLocalMap.remove(paramObject);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public final boolean hasCompileFunctionsWithDynamicScope()
/*      */   {
/* 2090 */     return this.compileFunctionsWithDynamicScopeFlag;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public final void setCompileFunctionsWithDynamicScope(boolean paramBoolean)
/*      */   {
/* 2100 */     if (this.sealed) onSealedMutation();
/* 2101 */     this.compileFunctionsWithDynamicScopeFlag = paramBoolean;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static void setCachingEnabled(boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final void setWrapFactory(WrapFactory paramWrapFactory)
/*      */   {
/* 2123 */     if (this.sealed) onSealedMutation();
/* 2124 */     if (paramWrapFactory == null) throw new IllegalArgumentException();
/* 2125 */     this.wrapFactory = paramWrapFactory;
/*      */   }
/*      */ 
/*      */   public final WrapFactory getWrapFactory()
/*      */   {
/* 2135 */     if (this.wrapFactory == null) {
/* 2136 */       this.wrapFactory = new WrapFactory();
/*      */     }
/* 2138 */     return this.wrapFactory;
/*      */   }
/*      */ 
/*      */   public final Debugger getDebugger()
/*      */   {
/* 2147 */     return this.debugger;
/*      */   }
/*      */ 
/*      */   public final Object getDebuggerContextData()
/*      */   {
/* 2156 */     return this.debuggerData;
/*      */   }
/*      */ 
/*      */   public final void setDebugger(Debugger paramDebugger, Object paramObject)
/*      */   {
/* 2168 */     if (this.sealed) onSealedMutation();
/* 2169 */     this.debugger = paramDebugger;
/* 2170 */     this.debuggerData = paramObject;
/*      */   }
/*      */ 
/*      */   public static DebuggableScript getDebuggableView(Script paramScript)
/*      */   {
/* 2180 */     if ((paramScript instanceof NativeFunction)) {
/* 2181 */       return ((NativeFunction)paramScript).getDebuggableView();
/*      */     }
/* 2183 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean hasFeature(int paramInt)
/*      */   {
/* 2214 */     ContextFactory localContextFactory = getFactory();
/* 2215 */     return localContextFactory.hasFeature(this, paramInt);
/*      */   }
/*      */ 
/*      */   public XMLLib.Factory getE4xImplementationFactory()
/*      */   {
/* 2230 */     return getFactory().getE4xImplementationFactory();
/*      */   }
/*      */ 
/*      */   public final int getInstructionObserverThreshold()
/*      */   {
/* 2243 */     return this.instructionThreshold;
/*      */   }
/*      */ 
/*      */   public final void setInstructionObserverThreshold(int paramInt)
/*      */   {
/* 2263 */     if (this.sealed) onSealedMutation();
/* 2264 */     if (paramInt < 0) throw new IllegalArgumentException();
/* 2265 */     this.instructionThreshold = paramInt;
/* 2266 */     setGenerateObserverCount(paramInt > 0);
/*      */   }
/*      */ 
/*      */   public void setGenerateObserverCount(boolean paramBoolean)
/*      */   {
/* 2281 */     this.generateObserverCount = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected void observeInstructionCount(int paramInt)
/*      */   {
/* 2305 */     ContextFactory localContextFactory = getFactory();
/* 2306 */     localContextFactory.observeInstructionCount(this, paramInt);
/*      */   }
/*      */ 
/*      */   public GeneratedClassLoader createClassLoader(ClassLoader paramClassLoader)
/*      */   {
/* 2316 */     ContextFactory localContextFactory = getFactory();
/* 2317 */     return localContextFactory.createClassLoader(paramClassLoader);
/*      */   }
/*      */ 
/*      */   public final ClassLoader getApplicationClassLoader()
/*      */   {
/* 2322 */     if (this.applicationClassLoader == null) {
/* 2323 */       ContextFactory localContextFactory = getFactory();
/* 2324 */       ClassLoader localClassLoader1 = localContextFactory.getApplicationClassLoader();
/* 2325 */       if (localClassLoader1 == null) {
/* 2326 */         ClassLoader localClassLoader2 = VMBridge.instance.getCurrentThreadClassLoader();
/*      */ 
/* 2340 */         if (localClassLoader2 != null)
/*      */         {
/* 2346 */           return localClassLoader2;
/*      */         }
/*      */ 
/* 2351 */         Class localClass = localContextFactory.getClass();
/* 2352 */         if (localClass != ScriptRuntime.ContextFactoryClass)
/* 2353 */           localClassLoader1 = localClass.getClassLoader();
/*      */         else {
/* 2355 */           localClassLoader1 = getClass().getClassLoader();
/*      */         }
/*      */       }
/* 2358 */       this.applicationClassLoader = localClassLoader1;
/*      */     }
/* 2360 */     return this.applicationClassLoader;
/*      */   }
/*      */ 
/*      */   public final void setApplicationClassLoader(ClassLoader paramClassLoader)
/*      */   {
/* 2365 */     if (this.sealed) onSealedMutation();
/* 2366 */     if (paramClassLoader == null)
/*      */     {
/* 2368 */       this.applicationClassLoader = null;
/* 2369 */       return;
/*      */     }
/*      */ 
/* 2377 */     this.applicationClassLoader = paramClassLoader;
/*      */   }
/*      */ 
/*      */   static Context getContext()
/*      */   {
/* 2388 */     Context localContext = getCurrentContext();
/* 2389 */     if (localContext == null) {
/* 2390 */       throw new RuntimeException("No Context associated with current Thread");
/*      */     }
/*      */ 
/* 2393 */     return localContext;
/*      */   }
/*      */ 
/*      */   private Object compileImpl(Scriptable paramScriptable, Reader paramReader, String paramString1, String paramString2, int paramInt, Object paramObject, boolean paramBoolean, Evaluator paramEvaluator, ErrorReporter paramErrorReporter)
/*      */     throws IOException
/*      */   {
/* 2404 */     if (paramString2 == null) {
/* 2405 */       paramString2 = "unnamed script";
/*      */     }
/* 2407 */     if ((paramObject != null) && (getSecurityController() == null)) {
/* 2408 */       throw new IllegalArgumentException("securityDomain should be null if setSecurityController() was never called");
/*      */     }
/*      */ 
/* 2413 */     if (((paramReader == null ? 1 : 0) ^ (paramString1 == null ? 1 : 0)) == 0) Kit.codeBug();
/*      */ 
/* 2415 */     if (!(paramScriptable == null ^ paramBoolean)) Kit.codeBug();
/*      */ 
/* 2417 */     CompilerEnvirons localCompilerEnvirons = new CompilerEnvirons();
/* 2418 */     localCompilerEnvirons.initFromContext(this);
/* 2419 */     if (paramErrorReporter == null) {
/* 2420 */       paramErrorReporter = localCompilerEnvirons.getErrorReporter();
/*      */     }
/*      */ 
/* 2423 */     if ((this.debugger != null) && 
/* 2424 */       (paramReader != null)) {
/* 2425 */       paramString1 = Kit.readReader(paramReader);
/* 2426 */       paramReader = null;
/*      */     }
/*      */ 
/* 2430 */     Parser localParser = new Parser(localCompilerEnvirons, paramErrorReporter);
/* 2431 */     if (paramBoolean) {
/* 2432 */       localParser.calledByCompileFunction = true;
/*      */     }
/*      */ 
/* 2435 */     if (paramString1 != null)
/* 2436 */       localAstRoot = localParser.parse(paramString1, paramString2, paramInt);
/*      */     else {
/* 2438 */       localAstRoot = localParser.parse(paramReader, paramString2, paramInt);
/*      */     }
/* 2440 */     if (paramBoolean)
/*      */     {
/* 2442 */       if ((localAstRoot.getFirstChild() == null) || (localAstRoot.getFirstChild().getType() != 109))
/*      */       {
/* 2448 */         throw new IllegalArgumentException("compileFunction only accepts source with single JS function: " + paramString1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2453 */     IRFactory localIRFactory = new IRFactory(localCompilerEnvirons, paramErrorReporter);
/* 2454 */     ScriptNode localScriptNode = localIRFactory.transformTree(localAstRoot);
/*      */ 
/* 2457 */     localParser = null;
/* 2458 */     AstRoot localAstRoot = null;
/* 2459 */     localIRFactory = null;
/*      */ 
/* 2461 */     if (paramEvaluator == null) {
/* 2462 */       paramEvaluator = createCompiler();
/*      */     }
/*      */ 
/* 2465 */     Object localObject1 = paramEvaluator.compile(localCompilerEnvirons, localScriptNode, localScriptNode.getEncodedSource(), paramBoolean);
/*      */     Object localObject2;
/* 2468 */     if (this.debugger != null) {
/* 2469 */       if (paramString1 == null) Kit.codeBug();
/* 2470 */       if ((localObject1 instanceof DebuggableScript)) {
/* 2471 */         localObject2 = (DebuggableScript)localObject1;
/* 2472 */         notifyDebugger_r(this, (DebuggableScript)localObject2, paramString1);
/*      */       } else {
/* 2474 */         throw new RuntimeException("NOT SUPPORTED");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2479 */     if (paramBoolean)
/* 2480 */       localObject2 = paramEvaluator.createFunctionObject(this, paramScriptable, localObject1, paramObject);
/*      */     else {
/* 2482 */       localObject2 = paramEvaluator.createScriptObject(localObject1, paramObject);
/*      */     }
/*      */ 
/* 2485 */     return localObject2;
/*      */   }
/*      */ 
/*      */   private static void notifyDebugger_r(Context paramContext, DebuggableScript paramDebuggableScript, String paramString)
/*      */   {
/* 2491 */     paramContext.debugger.handleCompilationDone(paramContext, paramDebuggableScript, paramString);
/* 2492 */     for (int i = 0; i != paramDebuggableScript.getFunctionCount(); i++)
/* 2493 */       notifyDebugger_r(paramContext, paramDebuggableScript.getFunction(i), paramString);
/*      */   }
/*      */ 
/*      */   private Evaluator createCompiler()
/*      */   {
/* 2504 */     Evaluator localEvaluator = null;
/* 2505 */     if ((this.optimizationLevel >= 0) && (codegenClass != null)) {
/* 2506 */       localEvaluator = (Evaluator)Kit.newInstanceOrNull(codegenClass);
/*      */     }
/* 2508 */     if (localEvaluator == null) {
/* 2509 */       localEvaluator = createInterpreter();
/*      */     }
/* 2511 */     return localEvaluator;
/*      */   }
/*      */ 
/*      */   static Evaluator createInterpreter()
/*      */   {
/* 2516 */     return (Evaluator)Kit.newInstanceOrNull(interpreterClass);
/*      */   }
/*      */ 
/*      */   static String getSourcePositionFromStack(int[] paramArrayOfInt)
/*      */   {
/* 2521 */     Context localContext = getCurrentContext();
/* 2522 */     if (localContext == null)
/* 2523 */       return null;
/* 2524 */     if (localContext.lastInterpreterFrame != null) {
/* 2525 */       localObject = createInterpreter();
/* 2526 */       if (localObject != null) {
/* 2527 */         return ((Evaluator)localObject).getSourcePositionFromStack(localContext, paramArrayOfInt);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2533 */     Object localObject = new CharArrayWriter();
/* 2534 */     RuntimeException localRuntimeException = new RuntimeException();
/* 2535 */     localRuntimeException.printStackTrace(new PrintWriter((Writer)localObject));
/* 2536 */     String str1 = ((CharArrayWriter)localObject).toString();
/* 2537 */     int i = -1;
/* 2538 */     int j = -1;
/* 2539 */     int k = -1;
/* 2540 */     for (int m = 0; m < str1.length(); m++) {
/* 2541 */       int n = str1.charAt(m);
/* 2542 */       if (n == 58) {
/* 2543 */         k = m;
/* 2544 */       } else if (n == 40) {
/* 2545 */         i = m;
/* 2546 */       } else if (n == 41) {
/* 2547 */         j = m;
/* 2548 */       } else if ((n == 10) && (i != -1) && (j != -1) && (k != -1) && (i < k) && (k < j))
/*      */       {
/* 2551 */         String str2 = str1.substring(i + 1, k);
/* 2552 */         if (!str2.endsWith(".java")) {
/* 2553 */           String str3 = str1.substring(k + 1, j);
/*      */           try {
/* 2555 */             paramArrayOfInt[0] = Integer.parseInt(str3);
/* 2556 */             if (paramArrayOfInt[0] < 0) {
/* 2557 */               paramArrayOfInt[0] = 0;
/*      */             }
/* 2559 */             return str2;
/*      */           }
/*      */           catch (NumberFormatException localNumberFormatException)
/*      */           {
/*      */           }
/*      */         }
/* 2565 */         i = j = k = -1;
/*      */       }
/*      */     }
/*      */ 
/* 2569 */     return null;
/*      */   }
/*      */ 
/*      */   RegExpProxy getRegExpProxy()
/*      */   {
/* 2574 */     if (this.regExpProxy == null) {
/* 2575 */       Class localClass = Kit.classOrNull("sun.org.mozilla.javascript.internal.regexp.RegExpImpl");
/*      */ 
/* 2577 */       if (localClass != null) {
/* 2578 */         this.regExpProxy = ((RegExpProxy)Kit.newInstanceOrNull(localClass));
/*      */       }
/*      */     }
/* 2581 */     return this.regExpProxy;
/*      */   }
/*      */ 
/*      */   final boolean isVersionECMA1()
/*      */   {
/* 2586 */     return (this.version == 0) || (this.version >= 130);
/*      */   }
/*      */ 
/*      */   SecurityController getSecurityController()
/*      */   {
/* 2592 */     SecurityController localSecurityController = SecurityController.global();
/* 2593 */     if (localSecurityController != null) {
/* 2594 */       return localSecurityController;
/*      */     }
/* 2596 */     return this.securityController;
/*      */   }
/*      */ 
/*      */   public final boolean isGeneratingDebugChanged()
/*      */   {
/* 2601 */     return this.generatingDebugChanged;
/*      */   }
/*      */ 
/*      */   public void addActivationName(String paramString)
/*      */   {
/* 2612 */     if (this.sealed) onSealedMutation();
/* 2613 */     if (this.activationNames == null)
/* 2614 */       this.activationNames = new HashSet();
/* 2615 */     this.activationNames.add(paramString);
/*      */   }
/*      */ 
/*      */   public final boolean isActivationNeeded(String paramString)
/*      */   {
/* 2628 */     return (this.activationNames != null) && (this.activationNames.contains(paramString));
/*      */   }
/*      */ 
/*      */   public void removeActivationName(String paramString)
/*      */   {
/* 2639 */     if (this.sealed) onSealedMutation();
/* 2640 */     if (this.activationNames != null)
/* 2641 */       this.activationNames.remove(paramString);
/*      */   }
/*      */ 
/*      */   public static abstract interface ClassShutterSetter
/*      */   {
/*      */     public abstract void setClassShutter(ClassShutter paramClassShutter);
/*      */ 
/*      */     public abstract ClassShutter getClassShutter();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Context
 * JD-Core Version:    0.6.2
 */