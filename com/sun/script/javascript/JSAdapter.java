/*     */ package com.sun.script.javascript;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.NativeArray;
/*     */ import sun.org.mozilla.javascript.internal.NativeJavaArray;
/*     */ import sun.org.mozilla.javascript.internal.RhinoException;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ 
/*     */ public final class JSAdapter
/*     */   implements Scriptable, Function
/*     */ {
/*     */   private Scriptable prototype;
/*     */   private Scriptable parent;
/*     */   private Scriptable adaptee;
/*     */   private boolean isPrototype;
/*     */   private static final String GET_PROP = "__get__";
/*     */   private static final String HAS_PROP = "__has__";
/*     */   private static final String PUT_PROP = "__put__";
/*     */   private static final String DEL_PROP = "__delete__";
/*     */   private static final String GET_PROPIDS = "__getIds__";
/*     */ 
/*     */   private JSAdapter(Scriptable paramScriptable)
/*     */   {
/*  74 */     setAdaptee(paramScriptable);
/*     */   }
/*     */ 
/*     */   public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean)
/*     */     throws RhinoException
/*     */   {
/*  80 */     JSAdapter localJSAdapter = new JSAdapter(paramContext.newObject(paramScriptable));
/*  81 */     localJSAdapter.setParentScope(paramScriptable);
/*  82 */     localJSAdapter.setPrototype(getFunctionPrototype(paramScriptable));
/*  83 */     localJSAdapter.isPrototype = true;
/*  84 */     ScriptableObject.defineProperty(paramScriptable, "JSAdapter", localJSAdapter, 2);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  89 */     return "JSAdapter";
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Scriptable paramScriptable) {
/*  93 */     Function localFunction = getAdapteeFunction("__get__");
/*  94 */     if (localFunction != null) {
/*  95 */       return call(localFunction, new Object[] { paramString });
/*     */     }
/*  97 */     paramScriptable = getAdaptee();
/*  98 */     return paramScriptable.get(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 103 */     Function localFunction = getAdapteeFunction("__get__");
/* 104 */     if (localFunction != null) {
/* 105 */       return call(localFunction, new Object[] { new Integer(paramInt) });
/*     */     }
/* 107 */     paramScriptable = getAdaptee();
/* 108 */     return paramScriptable.get(paramInt, paramScriptable);
/*     */   }
/*     */ 
/*     */   public boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/* 113 */     Function localFunction = getAdapteeFunction("__has__");
/* 114 */     if (localFunction != null) {
/* 115 */       Object localObject = call(localFunction, new Object[] { paramString });
/* 116 */       return Context.toBoolean(localObject);
/*     */     }
/* 118 */     paramScriptable = getAdaptee();
/* 119 */     return paramScriptable.has(paramString, paramScriptable);
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 124 */     Function localFunction = getAdapteeFunction("__has__");
/* 125 */     if (localFunction != null) {
/* 126 */       Object localObject = call(localFunction, new Object[] { new Integer(paramInt) });
/* 127 */       return Context.toBoolean(localObject);
/*     */     }
/* 129 */     paramScriptable = getAdaptee();
/* 130 */     return paramScriptable.has(paramInt, paramScriptable);
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 135 */     if (paramScriptable == this) {
/* 136 */       Function localFunction = getAdapteeFunction("__put__");
/* 137 */       if (localFunction != null) {
/* 138 */         call(localFunction, new Object[] { paramString, paramObject });
/*     */       } else {
/* 140 */         paramScriptable = getAdaptee();
/* 141 */         paramScriptable.put(paramString, paramScriptable, paramObject);
/*     */       }
/*     */     } else {
/* 144 */       paramScriptable.put(paramString, paramScriptable, paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
/* 149 */     if (paramScriptable == this) {
/* 150 */       Function localFunction = getAdapteeFunction("__put__");
/* 151 */       if (localFunction != null) {
/* 152 */         call(localFunction, new Object[] { new Integer(paramInt), paramObject });
/*     */       } else {
/* 154 */         paramScriptable = getAdaptee();
/* 155 */         paramScriptable.put(paramInt, paramScriptable, paramObject);
/*     */       }
/*     */     } else {
/* 158 */       paramScriptable.put(paramInt, paramScriptable, paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(String paramString) {
/* 163 */     Function localFunction = getAdapteeFunction("__delete__");
/* 164 */     if (localFunction != null)
/* 165 */       call(localFunction, new Object[] { paramString });
/*     */     else
/* 167 */       getAdaptee().delete(paramString);
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt)
/*     */   {
/* 172 */     Function localFunction = getAdapteeFunction("__delete__");
/* 173 */     if (localFunction != null)
/* 174 */       call(localFunction, new Object[] { new Integer(paramInt) });
/*     */     else
/* 176 */       getAdaptee().delete(paramInt);
/*     */   }
/*     */ 
/*     */   public Scriptable getPrototype()
/*     */   {
/* 181 */     return this.prototype;
/*     */   }
/*     */ 
/*     */   public void setPrototype(Scriptable paramScriptable) {
/* 185 */     this.prototype = paramScriptable;
/*     */   }
/*     */ 
/*     */   public Scriptable getParentScope() {
/* 189 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParentScope(Scriptable paramScriptable) {
/* 193 */     this.parent = paramScriptable;
/*     */   }
/*     */ 
/*     */   public Object[] getIds() {
/* 197 */     Function localFunction = getAdapteeFunction("__getIds__");
/* 198 */     if (localFunction != null) {
/* 199 */       Object localObject1 = call(localFunction, new Object[0]);
/*     */       Object localObject2;
/*     */       Object[] arrayOfObject1;
/* 201 */       if ((localObject1 instanceof NativeArray)) {
/* 202 */         localObject2 = (NativeArray)localObject1;
/* 203 */         arrayOfObject1 = new Object[(int)((NativeArray)localObject2).getLength()];
/* 204 */         for (int i = 0; i < arrayOfObject1.length; i++) {
/* 205 */           arrayOfObject1[i] = mapToId(((NativeArray)localObject2).get(i, (Function)localObject2));
/*     */         }
/* 207 */         return arrayOfObject1;
/* 208 */       }if ((localObject1 instanceof NativeJavaArray))
/*     */       {
/* 210 */         localObject2 = ((NativeJavaArray)localObject1).unwrap();
/*     */ 
/* 212 */         if (localObject2.getClass() == [Ljava.lang.Object.class) {
/* 213 */           Object[] arrayOfObject2 = (Object[])localObject2;
/* 214 */           arrayOfObject1 = new Object[arrayOfObject2.length];
/* 215 */           for (int j = 0; j < arrayOfObject2.length; j++)
/* 216 */             arrayOfObject1[j] = mapToId(arrayOfObject2[j]);
/*     */         }
/*     */         else
/*     */         {
/* 220 */           arrayOfObject1 = Context.emptyArgs;
/*     */         }
/* 222 */         return arrayOfObject1;
/*     */       }
/*     */ 
/* 225 */       return Context.emptyArgs;
/*     */     }
/*     */ 
/* 228 */     return getAdaptee().getIds();
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 233 */     if ((paramScriptable instanceof JSAdapter)) {
/* 234 */       return true;
/*     */     }
/* 236 */     Scriptable localScriptable = paramScriptable.getPrototype();
/* 237 */     while (localScriptable != null) {
/* 238 */       if (localScriptable.equals(this)) return true;
/* 239 */       localScriptable = localScriptable.getPrototype();
/*     */     }
/* 241 */     return false;
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class paramClass)
/*     */   {
/* 246 */     return getAdaptee().getDefaultValue(paramClass);
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */     throws RhinoException
/*     */   {
/* 252 */     if (this.isPrototype) {
/* 253 */       return construct(paramContext, paramScriptable1, paramArrayOfObject);
/*     */     }
/* 255 */     Scriptable localScriptable = getAdaptee();
/* 256 */     if ((localScriptable instanceof Function)) {
/* 257 */       return ((Function)localScriptable).call(paramContext, paramScriptable1, localScriptable, paramArrayOfObject);
/*     */     }
/* 259 */     throw Context.reportRuntimeError("TypeError: not a function");
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject)
/*     */     throws RhinoException
/*     */   {
/* 266 */     if (this.isPrototype) {
/* 267 */       localScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
/*     */       JSAdapter localJSAdapter;
/* 269 */       if (paramArrayOfObject.length > 0)
/* 270 */         localJSAdapter = new JSAdapter(Context.toObject(paramArrayOfObject[0], localScriptable));
/*     */       else {
/* 272 */         throw Context.reportRuntimeError("JSAdapter requires adaptee");
/*     */       }
/* 274 */       return localJSAdapter;
/*     */     }
/* 276 */     Scriptable localScriptable = getAdaptee();
/* 277 */     if ((localScriptable instanceof Function)) {
/* 278 */       return ((Function)localScriptable).construct(paramContext, paramScriptable, paramArrayOfObject);
/*     */     }
/* 280 */     throw Context.reportRuntimeError("TypeError: not a constructor");
/*     */   }
/*     */ 
/*     */   public Scriptable getAdaptee()
/*     */   {
/* 286 */     return this.adaptee;
/*     */   }
/*     */ 
/*     */   public void setAdaptee(Scriptable paramScriptable) {
/* 290 */     if (paramScriptable == null) {
/* 291 */       throw new NullPointerException("adaptee can not be null");
/*     */     }
/* 293 */     this.adaptee = paramScriptable;
/*     */   }
/*     */ 
/*     */   private Object mapToId(Object paramObject)
/*     */   {
/* 300 */     if ((paramObject instanceof Double)) {
/* 301 */       return new Integer(((Double)paramObject).intValue());
/*     */     }
/* 303 */     return Context.toString(paramObject);
/*     */   }
/*     */ 
/*     */   private static Scriptable getFunctionPrototype(Scriptable paramScriptable)
/*     */   {
/* 308 */     return ScriptableObject.getFunctionPrototype(paramScriptable);
/*     */   }
/*     */ 
/*     */   private Function getAdapteeFunction(String paramString) {
/* 312 */     Object localObject = ScriptableObject.getProperty(getAdaptee(), paramString);
/* 313 */     return (localObject instanceof Function) ? (Function)localObject : null;
/*     */   }
/*     */ 
/*     */   private Object call(Function paramFunction, Object[] paramArrayOfObject) {
/* 317 */     Context localContext = Context.getCurrentContext();
/* 318 */     Scriptable localScriptable1 = getAdaptee();
/* 319 */     Scriptable localScriptable2 = paramFunction.getParentScope();
/*     */     try {
/* 321 */       return paramFunction.call(localContext, localScriptable2, localScriptable1, paramArrayOfObject);
/*     */     } catch (RhinoException localRhinoException) {
/* 323 */       throw Context.reportRuntimeError(localRhinoException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.JSAdapter
 * JD-Core Version:    0.6.2
 */