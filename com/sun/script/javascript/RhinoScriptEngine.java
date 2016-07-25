/*     */ package com.sun.script.javascript;
/*     */ 
/*     */ import com.sun.script.util.InterfaceImplementor;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AccessController;
/*     */ import java.security.AllPermission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.script.AbstractScriptEngine;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.Compilable;
/*     */ import javax.script.CompiledScript;
/*     */ import javax.script.Invocable;
/*     */ import javax.script.ScriptContext;
/*     */ import javax.script.ScriptEngineFactory;
/*     */ import javax.script.ScriptException;
/*     */ import javax.script.SimpleBindings;
/*     */ import sun.org.mozilla.javascript.internal.Callable;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.ContextFactory;
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.JavaScriptException;
/*     */ import sun.org.mozilla.javascript.internal.RhinoException;
/*     */ import sun.org.mozilla.javascript.internal.Script;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ import sun.org.mozilla.javascript.internal.Wrapper;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public final class RhinoScriptEngine extends AbstractScriptEngine
/*     */   implements Invocable, Compilable
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private AccessControlContext accCtxt;
/*     */   private RhinoTopLevel topLevel;
/*     */   private Map<Object, Object> indexedProps;
/*     */   private ScriptEngineFactory factory;
/*     */   private InterfaceImplementor implementor;
/*  67 */   private static final int languageVersion = getLanguageVersion();
/*  68 */   private static final int optimizationLevel = getOptimizationLevel();
/*     */   private static final String RHINO_JS_VERSION = "rhino.js.version";
/*     */   private static final String RHINO_OPT_LEVEL = "rhino.opt.level";
/*     */   private static final String printSource = "function print(str, newline) {                \n    if (typeof(str) == 'undefined') {         \n        str = 'undefined';                    \n    } else if (str == null) {                 \n        str = 'null';                         \n    }                                         \n    var out = context.getWriter();            \n    if (!(out instanceof java.io.PrintWriter))\n        out = new java.io.PrintWriter(out);   \n    out.print(String(str));                   \n    if (newline) out.print('\\n');            \n    out.flush();                              \n}\nfunction println(str) {                       \n    print(str, true);                         \n}";
/*     */ 
/*     */   private static int getLanguageVersion()
/*     */   {
/* 124 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("rhino.js.version"));
/*     */     int i;
/* 126 */     if (str != null)
/* 127 */       i = Integer.parseInt(str);
/*     */     else {
/* 129 */       i = 180;
/*     */     }
/* 131 */     return i;
/*     */   }
/*     */ 
/*     */   private static int getOptimizationLevel()
/*     */   {
/* 136 */     int i = -1;
/*     */ 
/* 138 */     if (System.getSecurityManager() == null) {
/* 139 */       i = Integer.getInteger("rhino.opt.level", -1).intValue();
/*     */     }
/* 141 */     return i;
/*     */   }
/*     */ 
/*     */   public RhinoScriptEngine()
/*     */   {
/* 148 */     if (System.getSecurityManager() != null) {
/*     */       try {
/* 150 */         AccessController.checkPermission(new AllPermission());
/*     */       } catch (AccessControlException localAccessControlException) {
/* 152 */         this.accCtxt = AccessController.getContext();
/*     */       }
/*     */     }
/*     */ 
/* 156 */     Context localContext = enterContext();
/*     */     try {
/* 158 */       this.topLevel = new RhinoTopLevel(localContext, this);
/*     */     } finally {
/* 160 */       Context.exit();
/*     */     }
/*     */ 
/* 163 */     this.indexedProps = new HashMap();
/*     */ 
/* 166 */     this.implementor = new InterfaceImplementor(this) {
/*     */       protected boolean isImplemented(Object paramAnonymousObject, Class<?> paramAnonymousClass) {
/* 168 */         Context localContext = RhinoScriptEngine.enterContext();
/*     */         try {
/* 170 */           if ((paramAnonymousObject != null) && (!(paramAnonymousObject instanceof Scriptable))) {
/* 171 */             paramAnonymousObject = Context.toObject(paramAnonymousObject, RhinoScriptEngine.this.topLevel);
/*     */           }
/* 173 */           Scriptable localScriptable1 = RhinoScriptEngine.this.getRuntimeScope(RhinoScriptEngine.this.context);
/* 174 */           Scriptable localScriptable2 = paramAnonymousObject != null ? (Scriptable)paramAnonymousObject : localScriptable1;
/*     */ 
/* 176 */           for (Method localMethod : paramAnonymousClass.getMethods())
/*     */           {
/* 178 */             if (localMethod.getDeclaringClass() != Object.class)
/*     */             {
/* 181 */               Object localObject1 = ScriptableObject.getProperty(localScriptable2, localMethod.getName());
/* 182 */               if (!(localObject1 instanceof Function))
/* 183 */                 return false;
/*     */             }
/*     */           }
/* 186 */           return true;
/*     */         } finally {
/* 188 */           Context.exit();
/*     */         }
/*     */       }
/*     */ 
/*     */       protected Object convertResult(Method paramAnonymousMethod, Object paramAnonymousObject) throws ScriptException
/*     */       {
/* 194 */         Class localClass = paramAnonymousMethod.getReturnType();
/* 195 */         if (localClass == Void.TYPE) {
/* 196 */           return null;
/*     */         }
/* 198 */         return Context.jsToJava(paramAnonymousObject, localClass);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Object eval(Reader paramReader, ScriptContext paramScriptContext) throws ScriptException
/*     */   {
/* 208 */     Context localContext = enterContext();
/*     */     Object localObject1;
/*     */     try
/*     */     {
/* 210 */       Scriptable localScriptable = getRuntimeScope(paramScriptContext);
/* 211 */       String str1 = (String)get("javax.script.filename");
/* 212 */       str1 = str1 == null ? "<Unknown source>" : str1;
/*     */ 
/* 214 */       localObject1 = localContext.evaluateReader(localScriptable, paramReader, str1, 1, null);
/*     */     }
/*     */     catch (RhinoException localRhinoException) {
/* 217 */       int i = (i = localRhinoException.lineNumber()) == 0 ? -1 : i;
/*     */       String str2;
/* 219 */       if ((localRhinoException instanceof JavaScriptException))
/* 220 */         str2 = String.valueOf(((JavaScriptException)localRhinoException).getValue());
/*     */       else {
/* 222 */         str2 = localRhinoException.toString();
/*     */       }
/* 224 */       ScriptException localScriptException = new ScriptException(str2, localRhinoException.sourceName(), i);
/* 225 */       localScriptException.initCause(localRhinoException);
/* 226 */       throw localScriptException;
/*     */     } catch (IOException localIOException) {
/* 228 */       throw new ScriptException(localIOException);
/*     */     } finally {
/* 230 */       Context.exit();
/*     */     }
/*     */ 
/* 233 */     return unwrapReturnValue(localObject1);
/*     */   }
/*     */ 
/*     */   public Object eval(String paramString, ScriptContext paramScriptContext) throws ScriptException {
/* 237 */     if (paramString == null) {
/* 238 */       throw new NullPointerException("null script");
/*     */     }
/* 240 */     return eval(new StringReader(paramString), paramScriptContext);
/*     */   }
/*     */ 
/*     */   public ScriptEngineFactory getFactory() {
/* 244 */     if (this.factory != null) {
/* 245 */       return this.factory;
/*     */     }
/* 247 */     return new RhinoScriptEngineFactory();
/*     */   }
/*     */ 
/*     */   public Bindings createBindings()
/*     */   {
/* 252 */     return new SimpleBindings();
/*     */   }
/*     */ 
/*     */   public Object invokeFunction(String paramString, Object[] paramArrayOfObject)
/*     */     throws ScriptException, NoSuchMethodException
/*     */   {
/* 258 */     return invoke(null, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Object invokeMethod(Object paramObject, String paramString, Object[] paramArrayOfObject) throws ScriptException, NoSuchMethodException
/*     */   {
/* 263 */     if (paramObject == null) {
/* 264 */       throw new IllegalArgumentException("script object can not be null");
/*     */     }
/* 266 */     return invoke(paramObject, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private Object invoke(Object paramObject, String paramString, Object[] paramArrayOfObject) throws ScriptException, NoSuchMethodException
/*     */   {
/* 271 */     Context localContext = enterContext();
/*     */     try {
/* 273 */       if (paramString == null) {
/* 274 */         throw new NullPointerException("method name is null");
/*     */       }
/*     */ 
/* 277 */       if ((paramObject != null) && (!(paramObject instanceof Scriptable))) {
/* 278 */         paramObject = Context.toObject(paramObject, this.topLevel);
/*     */       }
/*     */ 
/* 281 */       Scriptable localScriptable1 = getRuntimeScope(this.context);
/* 282 */       Scriptable localScriptable2 = paramObject != null ? (Scriptable)paramObject : localScriptable1;
/*     */ 
/* 284 */       localObject1 = ScriptableObject.getProperty(localScriptable2, paramString);
/* 285 */       if (!(localObject1 instanceof Function)) {
/* 286 */         throw new NoSuchMethodException("no such method: " + paramString);
/*     */       }
/*     */ 
/* 289 */       Function localFunction = (Function)localObject1;
/* 290 */       Scriptable localScriptable3 = localFunction.getParentScope();
/* 291 */       if (localScriptable3 == null) {
/* 292 */         localScriptable3 = localScriptable1;
/*     */       }
/* 294 */       Object localObject2 = localFunction.call(localContext, localScriptable3, localScriptable2, wrapArguments(paramArrayOfObject));
/*     */ 
/* 296 */       return unwrapReturnValue(localObject2);
/*     */     }
/*     */     catch (RhinoException localRhinoException) {
/* 299 */       int i = (i = localRhinoException.lineNumber()) == 0 ? -1 : i;
/* 300 */       Object localObject1 = new ScriptException(localRhinoException.toString(), localRhinoException.sourceName(), i);
/* 301 */       ((ScriptException)localObject1).initCause(localRhinoException);
/* 302 */       throw ((Throwable)localObject1);
/*     */     } finally {
/* 304 */       Context.exit();
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T getInterface(Class<T> paramClass) {
/*     */     try {
/* 310 */       return this.implementor.getInterface(null, paramClass); } catch (ScriptException localScriptException) {
/*     */     }
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   public <T> T getInterface(Object paramObject, Class<T> paramClass)
/*     */   {
/* 317 */     if (paramObject == null) {
/* 318 */       throw new IllegalArgumentException("script object can not be null");
/*     */     }
/*     */     try
/*     */     {
/* 322 */       return this.implementor.getInterface(paramObject, paramClass); } catch (ScriptException localScriptException) {
/*     */     }
/* 324 */     return null;
/*     */   }
/*     */ 
/*     */   Scriptable getRuntimeScope(ScriptContext paramScriptContext)
/*     */   {
/* 347 */     if (paramScriptContext == null) {
/* 348 */       throw new NullPointerException("null script context");
/*     */     }
/*     */ 
/* 352 */     ExternalScriptable localExternalScriptable = new ExternalScriptable(paramScriptContext, this.indexedProps);
/*     */ 
/* 356 */     localExternalScriptable.setPrototype(this.topLevel);
/*     */ 
/* 359 */     localExternalScriptable.put("context", localExternalScriptable, paramScriptContext);
/*     */ 
/* 362 */     Context localContext = enterContext();
/*     */     try {
/* 364 */       localContext.evaluateString(localExternalScriptable, "function print(str, newline) {                \n    if (typeof(str) == 'undefined') {         \n        str = 'undefined';                    \n    } else if (str == null) {                 \n        str = 'null';                         \n    }                                         \n    var out = context.getWriter();            \n    if (!(out instanceof java.io.PrintWriter))\n        out = new java.io.PrintWriter(out);   \n    out.print(String(str));                   \n    if (newline) out.print('\\n');            \n    out.flush();                              \n}\nfunction println(str) {                       \n    print(str, true);                         \n}", "print", 1, null);
/*     */     } finally {
/* 366 */       Context.exit();
/*     */     }
/* 368 */     return localExternalScriptable;
/*     */   }
/*     */ 
/*     */   public CompiledScript compile(String paramString)
/*     */     throws ScriptException
/*     */   {
/* 374 */     return compile(new StringReader(paramString));
/*     */   }
/*     */ 
/*     */   public CompiledScript compile(Reader paramReader) throws ScriptException {
/* 378 */     RhinoCompiledScript localRhinoCompiledScript = null;
/* 379 */     Context localContext = enterContext();
/*     */     try
/*     */     {
/* 382 */       String str = (String)get("javax.script.filename");
/* 383 */       if (str == null) {
/* 384 */         str = "<Unknown Source>";
/*     */       }
/*     */ 
/* 387 */       Scriptable localScriptable = getRuntimeScope(this.context);
/* 388 */       Script localScript = localContext.compileReader(localScriptable, paramReader, str, 1, null);
/* 389 */       localRhinoCompiledScript = new RhinoCompiledScript(this, localScript);
/*     */     }
/*     */     catch (Exception localException) {
/* 392 */       throw new ScriptException(localException);
/*     */     } finally {
/* 394 */       Context.exit();
/*     */     }
/* 396 */     return localRhinoCompiledScript;
/*     */   }
/*     */ 
/*     */   static Context enterContext()
/*     */   {
/* 405 */     return Context.enter();
/*     */   }
/*     */ 
/*     */   void setEngineFactory(ScriptEngineFactory paramScriptEngineFactory) {
/* 409 */     this.factory = paramScriptEngineFactory;
/*     */   }
/*     */ 
/*     */   AccessControlContext getAccessContext() {
/* 413 */     return this.accCtxt;
/*     */   }
/*     */ 
/*     */   Object[] wrapArguments(Object[] paramArrayOfObject) {
/* 417 */     if (paramArrayOfObject == null) {
/* 418 */       return Context.emptyArgs;
/*     */     }
/* 420 */     Object[] arrayOfObject = new Object[paramArrayOfObject.length];
/* 421 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 422 */       arrayOfObject[i] = Context.javaToJS(paramArrayOfObject[i], this.topLevel);
/*     */     }
/* 424 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   Object unwrapReturnValue(Object paramObject) {
/* 428 */     if ((paramObject instanceof Wrapper)) {
/* 429 */       paramObject = ((Wrapper)paramObject).unwrap();
/*     */     }
/*     */ 
/* 432 */     return (paramObject instanceof Undefined) ? null : paramObject;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  71 */     ContextFactory.initGlobal(new ContextFactory()
/*     */     {
/*     */       protected Context makeContext()
/*     */       {
/*  77 */         Context localContext = super.makeContext();
/*  78 */         localContext.setLanguageVersion(RhinoScriptEngine.languageVersion);
/*  79 */         localContext.setOptimizationLevel(RhinoScriptEngine.optimizationLevel);
/*  80 */         localContext.setClassShutter(RhinoClassShutter.getInstance());
/*  81 */         localContext.setWrapFactory(RhinoWrapFactory.getInstance());
/*  82 */         return localContext;
/*     */       }
/*     */ 
/*     */       protected Object doTopCall(final Callable paramAnonymousCallable, final Context paramAnonymousContext, final Scriptable paramAnonymousScriptable1, final Scriptable paramAnonymousScriptable2, final Object[] paramAnonymousArrayOfObject)
/*     */       {
/*  95 */         AccessControlContext localAccessControlContext = null;
/*  96 */         Scriptable localScriptable1 = ScriptableObject.getTopLevelScope(paramAnonymousScriptable1);
/*  97 */         Scriptable localScriptable2 = localScriptable1.getPrototype();
/*  98 */         if ((localScriptable2 instanceof RhinoTopLevel)) {
/*  99 */           localAccessControlContext = ((RhinoTopLevel)localScriptable2).getAccessContext();
/*     */         }
/*     */ 
/* 102 */         if (localAccessControlContext != null) {
/* 103 */           return AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public Object run() {
/* 105 */               return RhinoScriptEngine.1.this.superDoTopCall(paramAnonymousCallable, paramAnonymousContext, paramAnonymousScriptable1, paramAnonymousScriptable2, paramAnonymousArrayOfObject);
/*     */             }
/*     */           }
/*     */           , localAccessControlContext);
/*     */         }
/*     */ 
/* 109 */         return superDoTopCall(paramAnonymousCallable, paramAnonymousContext, paramAnonymousScriptable1, paramAnonymousScriptable2, paramAnonymousArrayOfObject);
/*     */       }
/*     */ 
/*     */       private Object superDoTopCall(Callable paramAnonymousCallable, Context paramAnonymousContext, Scriptable paramAnonymousScriptable1, Scriptable paramAnonymousScriptable2, Object[] paramAnonymousArrayOfObject)
/*     */       {
/* 116 */         return super.doTopCall(paramAnonymousCallable, paramAnonymousContext, paramAnonymousScriptable1, paramAnonymousScriptable2, paramAnonymousArrayOfObject);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.RhinoScriptEngine
 * JD-Core Version:    0.6.2
 */