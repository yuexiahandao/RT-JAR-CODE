/*     */ package com.sun.script.javascript;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.ScriptContext;
/*     */ import javax.script.SimpleScriptContext;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.ImporterTopLevel;
/*     */ import sun.org.mozilla.javascript.internal.LazilyLoadedCtor;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.Synchronizer;
/*     */ import sun.org.mozilla.javascript.internal.Wrapper;
/*     */ 
/*     */ public final class RhinoTopLevel extends ImporterTopLevel
/*     */ {
/*     */   private RhinoScriptEngine engine;
/*     */ 
/*     */   RhinoTopLevel(Context paramContext, RhinoScriptEngine paramRhinoScriptEngine)
/*     */   {
/*  45 */     super(paramContext, System.getSecurityManager() != null);
/*  46 */     this.engine = paramRhinoScriptEngine;
/*     */ 
/*  49 */     new LazilyLoadedCtor(this, "JSAdapter", "com.sun.script.javascript.JSAdapter", false);
/*     */ 
/*  58 */     JavaAdapter.init(paramContext, this, false);
/*     */ 
/*  61 */     String[] arrayOfString = { "bindings", "scope", "sync" };
/*  62 */     defineFunctionProperties(arrayOfString, RhinoTopLevel.class, 2);
/*     */   }
/*     */ 
/*     */   public static Object bindings(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction)
/*     */   {
/*  80 */     if (paramArrayOfObject.length == 1) {
/*  81 */       Object localObject = paramArrayOfObject[0];
/*  82 */       if ((localObject instanceof Wrapper)) {
/*  83 */         localObject = ((Wrapper)localObject).unwrap();
/*     */       }
/*  85 */       if ((localObject instanceof ExternalScriptable)) {
/*  86 */         ScriptContext localScriptContext = ((ExternalScriptable)localObject).getContext();
/*  87 */         Bindings localBindings = localScriptContext.getBindings(100);
/*  88 */         return Context.javaToJS(localBindings, ScriptableObject.getTopLevelScope(paramScriptable));
/*     */       }
/*     */     }
/*     */ 
/*  92 */     return Context.getUndefinedValue();
/*     */   }
/*     */ 
/*     */   public static Object scope(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction)
/*     */   {
/* 110 */     if (paramArrayOfObject.length == 1) {
/* 111 */       Object localObject = paramArrayOfObject[0];
/* 112 */       if ((localObject instanceof Wrapper)) {
/* 113 */         localObject = ((Wrapper)localObject).unwrap();
/*     */       }
/* 115 */       if ((localObject instanceof Bindings)) {
/* 116 */         SimpleScriptContext localSimpleScriptContext = new SimpleScriptContext();
/* 117 */         localSimpleScriptContext.setBindings((Bindings)localObject, 100);
/* 118 */         ExternalScriptable localExternalScriptable = new ExternalScriptable(localSimpleScriptContext);
/* 119 */         localExternalScriptable.setPrototype(ScriptableObject.getObjectPrototype(paramScriptable));
/* 120 */         localExternalScriptable.setParentScope(ScriptableObject.getTopLevelScope(paramScriptable));
/* 121 */         return localExternalScriptable;
/*     */       }
/*     */     }
/* 124 */     return Context.getUndefinedValue();
/*     */   }
/*     */ 
/*     */   public static Object sync(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction)
/*     */   {
/* 147 */     if ((paramArrayOfObject.length == 1) && ((paramArrayOfObject[0] instanceof Function))) {
/* 148 */       return new Synchronizer((Function)paramArrayOfObject[0]);
/*     */     }
/* 150 */     throw Context.reportRuntimeError("wrong argument(s) for sync");
/*     */   }
/*     */ 
/*     */   RhinoScriptEngine getScriptEngine()
/*     */   {
/* 155 */     return this.engine;
/*     */   }
/*     */ 
/*     */   AccessControlContext getAccessContext() {
/* 159 */     return this.engine.getAccessContext();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.RhinoTopLevel
 * JD-Core Version:    0.6.2
 */