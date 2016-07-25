/*     */ package com.sun.script.javascript;
/*     */ 
/*     */ import javax.script.Invocable;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.Function;
/*     */ import sun.org.mozilla.javascript.internal.RhinoException;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.ScriptableObject;
/*     */ import sun.org.mozilla.javascript.internal.Wrapper;
/*     */ 
/*     */ final class JavaAdapter extends ScriptableObject
/*     */   implements Function
/*     */ {
/*     */   private Invocable engine;
/*     */ 
/*     */   private JavaAdapter(Invocable paramInvocable)
/*     */   {
/*  52 */     this.engine = paramInvocable;
/*     */   }
/*     */ 
/*     */   static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) throws RhinoException
/*     */   {
/*  57 */     RhinoTopLevel localRhinoTopLevel = (RhinoTopLevel)paramScriptable;
/*  58 */     RhinoScriptEngine localRhinoScriptEngine = localRhinoTopLevel.getScriptEngine();
/*  59 */     JavaAdapter localJavaAdapter = new JavaAdapter(localRhinoScriptEngine);
/*  60 */     localJavaAdapter.setParentScope(paramScriptable);
/*  61 */     localJavaAdapter.setPrototype(getFunctionPrototype(paramScriptable));
/*     */ 
/*  67 */     ScriptableObject.putProperty(localRhinoTopLevel, "JavaAdapter", localJavaAdapter);
/*     */   }
/*     */ 
/*     */   public String getClassName() {
/*  71 */     return "JavaAdapter";
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) throws RhinoException
/*     */   {
/*  76 */     return construct(paramContext, paramScriptable1, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) throws RhinoException
/*     */   {
/*  81 */     if (paramArrayOfObject.length == 2) {
/*  82 */       Class localClass = null;
/*  83 */       Object localObject1 = paramArrayOfObject[0];
/*  84 */       if ((localObject1 instanceof Wrapper)) {
/*  85 */         localObject2 = ((Wrapper)localObject1).unwrap();
/*  86 */         if (((localObject2 instanceof Class)) && (((Class)localObject2).isInterface()))
/*  87 */           localClass = (Class)localObject2;
/*     */       }
/*  89 */       else if (((localObject1 instanceof Class)) && 
/*  90 */         (((Class)localObject1).isInterface())) {
/*  91 */         localClass = (Class)localObject1;
/*     */       }
/*     */ 
/*  94 */       if (localClass == null) {
/*  95 */         throw Context.reportRuntimeError("JavaAdapter: first arg should be interface Class");
/*     */       }
/*     */ 
/*  98 */       Object localObject2 = ScriptableObject.getTopLevelScope(paramScriptable);
/*  99 */       return Context.toObject(this.engine.getInterface(paramArrayOfObject[1], localClass), (Scriptable)localObject2);
/*     */     }
/* 101 */     throw Context.reportRuntimeError("JavaAdapter requires two arguments");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.JavaAdapter
 * JD-Core Version:    0.6.2
 */