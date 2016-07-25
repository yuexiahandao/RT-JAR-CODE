/*     */ package com.sun.script.javascript;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.ScriptContext;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.NativeJavaClass;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.Wrapper;
/*     */ 
/*     */ final class ExternalScriptable
/*     */   implements Scriptable
/*     */ {
/*     */   private ScriptContext context;
/*     */   private Map<Object, Object> indexedProps;
/*     */   private Scriptable prototype;
/*     */   private Scriptable parent;
/*     */ 
/*     */   ExternalScriptable(ScriptContext paramScriptContext)
/*     */   {
/*  63 */     this(paramScriptContext, new HashMap());
/*     */   }
/*     */ 
/*     */   ExternalScriptable(ScriptContext paramScriptContext, Map<Object, Object> paramMap) {
/*  67 */     if (paramScriptContext == null) {
/*  68 */       throw new NullPointerException("context is null");
/*     */     }
/*  70 */     this.context = paramScriptContext;
/*  71 */     this.indexedProps = paramMap;
/*     */   }
/*     */ 
/*     */   ScriptContext getContext() {
/*  75 */     return this.context;
/*     */   }
/*     */ 
/*     */   private boolean isEmpty(String paramString) {
/*  79 */     return paramString.equals("");
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  86 */     return "Global";
/*     */   }
/*     */ 
/*     */   public synchronized Object get(String paramString, Scriptable paramScriptable)
/*     */   {
/* 100 */     if (isEmpty(paramString)) {
/* 101 */       if (this.indexedProps.containsKey(paramString)) {
/* 102 */         return this.indexedProps.get(paramString);
/*     */       }
/* 104 */       return NOT_FOUND;
/*     */     }
/*     */ 
/* 107 */     synchronized (this.context) {
/* 108 */       int i = this.context.getAttributesScope(paramString);
/* 109 */       if (i != -1) {
/* 110 */         Object localObject1 = this.context.getAttribute(paramString, i);
/* 111 */         return Context.javaToJS(localObject1, this);
/*     */       }
/* 113 */       return NOT_FOUND;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized Object get(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 127 */     Integer localInteger = new Integer(paramInt);
/* 128 */     if (this.indexedProps.containsKey(Integer.valueOf(paramInt))) {
/* 129 */       return this.indexedProps.get(localInteger);
/*     */     }
/* 131 */     return NOT_FOUND;
/*     */   }
/*     */ 
/*     */   public synchronized boolean has(String paramString, Scriptable paramScriptable)
/*     */   {
/* 143 */     if (isEmpty(paramString)) {
/* 144 */       return this.indexedProps.containsKey(paramString);
/*     */     }
/* 146 */     synchronized (this.context) {
/* 147 */       return this.context.getAttributesScope(paramString) != -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean has(int paramInt, Scriptable paramScriptable)
/*     */   {
/* 160 */     Integer localInteger = new Integer(paramInt);
/* 161 */     return this.indexedProps.containsKey(localInteger);
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 172 */     if (paramScriptable == this) {
/* 173 */       synchronized (this) {
/* 174 */         if (isEmpty(paramString))
/* 175 */           this.indexedProps.put(paramString, paramObject);
/*     */         else
/* 177 */           synchronized (this.context) {
/* 178 */             int i = this.context.getAttributesScope(paramString);
/* 179 */             if (i == -1) {
/* 180 */               i = 100;
/*     */             }
/* 182 */             this.context.setAttribute(paramString, jsToJava(paramObject), i);
/*     */           }
/*     */       }
/*     */     }
/*     */     else
/* 187 */       paramScriptable.put(paramString, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 199 */     if (paramScriptable == this)
/* 200 */       synchronized (this) {
/* 201 */         this.indexedProps.put(new Integer(paramInt), paramObject);
/*     */       }
/*     */     else
/* 204 */       paramScriptable.put(paramInt, paramScriptable, paramObject);
/*     */   }
/*     */ 
/*     */   public synchronized void delete(String paramString)
/*     */   {
/* 216 */     if (isEmpty(paramString))
/* 217 */       this.indexedProps.remove(paramString);
/*     */     else
/* 219 */       synchronized (this.context) {
/* 220 */         int i = this.context.getAttributesScope(paramString);
/* 221 */         if (i != -1)
/* 222 */           this.context.removeAttribute(paramString, i);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void delete(int paramInt)
/*     */   {
/* 236 */     this.indexedProps.remove(new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public Scriptable getPrototype()
/*     */   {
/* 244 */     return this.prototype;
/*     */   }
/*     */ 
/*     */   public void setPrototype(Scriptable paramScriptable)
/*     */   {
/* 252 */     this.prototype = paramScriptable;
/*     */   }
/*     */ 
/*     */   public Scriptable getParentScope()
/*     */   {
/* 260 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParentScope(Scriptable paramScriptable)
/*     */   {
/* 268 */     this.parent = paramScriptable;
/*     */   }
/*     */ 
/*     */   public synchronized Object[] getIds()
/*     */   {
/* 281 */     String[] arrayOfString = getAllKeys();
/* 282 */     int i = arrayOfString.length + this.indexedProps.size();
/* 283 */     Object[] arrayOfObject = new Object[i];
/* 284 */     System.arraycopy(arrayOfString, 0, arrayOfObject, 0, arrayOfString.length);
/* 285 */     int j = arrayOfString.length;
/*     */ 
/* 287 */     for (Iterator localIterator = this.indexedProps.keySet().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 288 */       arrayOfObject[(j++)] = localObject;
/*     */     }
/* 290 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class paramClass)
/*     */   {
/* 307 */     for (int i = 0; i < 2; i++)
/*     */     {
/*     */       int j;
/* 309 */       if (paramClass == ScriptRuntime.StringClass)
/* 310 */         j = i == 0 ? 1 : 0;
/*     */       else
/* 312 */         j = i == 1 ? 1 : 0;
/*     */       String str2;
/*     */       Object[] arrayOfObject;
/* 317 */       if (j != 0) {
/* 318 */         str2 = "toString";
/* 319 */         arrayOfObject = ScriptRuntime.emptyArgs;
/*     */       } else {
/* 321 */         str2 = "valueOf";
/* 322 */         arrayOfObject = new Object[1];
/*     */ 
/* 324 */         if (paramClass == null)
/* 325 */           localObject1 = "undefined";
/* 326 */         else if (paramClass == ScriptRuntime.StringClass)
/* 327 */           localObject1 = "string";
/* 328 */         else if (paramClass == ScriptRuntime.ScriptableClass)
/* 329 */           localObject1 = "object";
/* 330 */         else if (paramClass == ScriptRuntime.FunctionClass)
/* 331 */           localObject1 = "function";
/* 332 */         else if ((paramClass == ScriptRuntime.BooleanClass) || (paramClass == Boolean.TYPE))
/*     */         {
/* 335 */           localObject1 = "boolean";
/* 336 */         } else if ((paramClass == ScriptRuntime.NumberClass) || (paramClass == ScriptRuntime.ByteClass) || (paramClass == Byte.TYPE) || (paramClass == ScriptRuntime.ShortClass) || (paramClass == Short.TYPE) || (paramClass == ScriptRuntime.IntegerClass) || (paramClass == Integer.TYPE) || (paramClass == ScriptRuntime.FloatClass) || (paramClass == Float.TYPE) || (paramClass == ScriptRuntime.DoubleClass) || (paramClass == Double.TYPE))
/*     */         {
/* 348 */           localObject1 = "number";
/*     */         }
/* 350 */         else throw Context.reportRuntimeError("Invalid JavaScript value of type " + paramClass.toString());
/*     */ 
/* 354 */         arrayOfObject[0] = localObject1;
/*     */       }
/* 356 */       Object localObject1 = ScriptableObject.getProperty(this, str2);
/* 357 */       if ((localObject1 instanceof Function))
/*     */       {
/* 359 */         Function localFunction = (Function)localObject1;
/* 360 */         Context localContext = RhinoScriptEngine.enterContext();
/*     */         try {
/* 362 */           localObject1 = localFunction.call(localContext, localFunction.getParentScope(), this, arrayOfObject);
/*     */         } finally {
/* 364 */           Context.exit();
/*     */         }
/* 366 */         if (localObject1 != null) {
/* 367 */           if (!(localObject1 instanceof Scriptable)) {
/* 368 */             return localObject1;
/*     */           }
/* 370 */           if ((paramClass == ScriptRuntime.ScriptableClass) || (paramClass == ScriptRuntime.FunctionClass))
/*     */           {
/* 373 */             return localObject1;
/*     */           }
/* 375 */           if ((j != 0) && ((localObject1 instanceof Wrapper)))
/*     */           {
/* 378 */             Object localObject3 = ((Wrapper)localObject1).unwrap();
/* 379 */             if ((localObject3 instanceof String))
/* 380 */               return localObject3;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 385 */     String str1 = paramClass == null ? "undefined" : paramClass.getName();
/* 386 */     throw Context.reportRuntimeError("Cannot find default value for object " + str1);
/*     */   }
/*     */ 
/*     */   public boolean hasInstance(Scriptable paramScriptable)
/*     */   {
/* 401 */     Scriptable localScriptable = paramScriptable.getPrototype();
/* 402 */     while (localScriptable != null) {
/* 403 */       if (localScriptable.equals(this)) return true;
/* 404 */       localScriptable = localScriptable.getPrototype();
/*     */     }
/* 406 */     return false;
/*     */   }
/*     */ 
/*     */   private String[] getAllKeys() {
/* 410 */     ArrayList localArrayList = new ArrayList();
/*     */     Iterator localIterator1;
/* 411 */     synchronized (this.context) {
/* 412 */       for (localIterator1 = this.context.getScopes().iterator(); localIterator1.hasNext(); ) { int i = ((Integer)localIterator1.next()).intValue();
/* 413 */         Bindings localBindings = this.context.getBindings(i);
/* 414 */         if (localBindings != null) {
/* 415 */           localArrayList.ensureCapacity(localBindings.size());
/* 416 */           for (String str : localBindings.keySet()) {
/* 417 */             localArrayList.add(str);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 422 */     ??? = new String[localArrayList.size()];
/* 423 */     localArrayList.toArray((Object[])???);
/* 424 */     return ???;
/*     */   }
/*     */ 
/*     */   private Object jsToJava(Object paramObject)
/*     */   {
/* 435 */     if ((paramObject instanceof Wrapper)) {
/* 436 */       Wrapper localWrapper = (Wrapper)paramObject;
/*     */ 
/* 441 */       if ((localWrapper instanceof NativeJavaClass)) {
/* 442 */         return localWrapper;
/*     */       }
/*     */ 
/* 454 */       Object localObject = localWrapper.unwrap();
/* 455 */       if (((localObject instanceof Number)) || ((localObject instanceof String)) || ((localObject instanceof Boolean)) || ((localObject instanceof Character)))
/*     */       {
/* 458 */         return localWrapper;
/*     */       }
/*     */ 
/* 461 */       return localObject;
/*     */     }
/*     */ 
/* 464 */     return paramObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.ExternalScriptable
 * JD-Core Version:    0.6.2
 */