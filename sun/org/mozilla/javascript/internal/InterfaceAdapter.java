/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class InterfaceAdapter
/*     */ {
/*     */   private final Object proxyHelper;
/*     */ 
/*     */   static Object create(Context paramContext, Class<?> paramClass, Callable paramCallable)
/*     */   {
/*  61 */     if (!paramClass.isInterface()) throw new IllegalArgumentException();
/*     */ 
/*  63 */     Method[] arrayOfMethod = paramClass.getMethods();
/*  64 */     if (arrayOfMethod.length == 0) {
/*  65 */       throw Context.reportRuntimeError2("msg.no.empty.interface.conversion", String.valueOf(paramCallable), paramClass.getClass().getName());
/*     */     }
/*     */ 
/*  70 */     int i = 0;
/*     */ 
/*  72 */     Object localObject1 = arrayOfMethod[0].getParameterTypes();
/*     */     Object localObject3;
/*  74 */     for (int j = 1; j != arrayOfMethod.length; j++) {
/*  75 */       localObject3 = arrayOfMethod[j].getParameterTypes();
/*  76 */       if (localObject3.length != localObject1.length) {
/*     */         break label126;
/*     */       }
/*  79 */       for (int k = 0; k != localObject1.length; k++) {
/*  80 */         if (localObject3[k] != localObject1[k]) {
/*     */           break label126;
/*     */         }
/*     */       }
/*     */     }
/*  85 */     i = 1;
/*     */ 
/*  87 */     label126: if (i == 0) {
/*  88 */       throw Context.reportRuntimeError2("msg.no.function.interface.conversion", String.valueOf(paramCallable), paramClass.getClass().getName());
/*     */     }
/*     */ 
/*  95 */     localObject1 = ScriptRuntime.getTopCallScope(paramContext);
/*  96 */     Object localObject2 = ScriptableObject.getProperty((Scriptable)localObject1, "JavaAdapter");
/*  97 */     if (localObject2 != Scriptable.NOT_FOUND) {
/*  98 */       localObject3 = (Function)localObject2;
/*  99 */       Scriptable localScriptable = ScriptRuntime.newObject(paramContext, (Scriptable)localObject1, "Object", new Object[0]);
/*     */ 
/* 101 */       for (int m = 0; m < arrayOfMethod.length; m++) {
/* 102 */         localObject4 = arrayOfMethod[m].getName();
/* 103 */         ScriptableObject.putProperty(localScriptable, (String)localObject4, paramCallable);
/*     */       }
/* 105 */       Object[] arrayOfObject = { paramClass, localScriptable };
/* 106 */       Object localObject4 = ((Function)localObject3).construct(paramContext, (Scriptable)localObject1, arrayOfObject);
/* 107 */       if ((localObject4 instanceof Wrapper)) {
/* 108 */         localObject4 = ((Wrapper)localObject4).unwrap();
/*     */       }
/* 110 */       return localObject4;
/*     */     }
/* 112 */     throw Context.reportRuntimeError2("msg.conversion.not.allowed", String.valueOf(paramCallable), paramClass.getName());
/*     */   }
/*     */ 
/*     */   private InterfaceAdapter(ContextFactory paramContextFactory, Class<?> paramClass)
/*     */   {
/* 121 */     this.proxyHelper = VMBridge.instance.getInterfaceProxyHelper(paramContextFactory, new Class[] { paramClass });
/*     */   }
/*     */ 
/*     */   public Object invoke(ContextFactory paramContextFactory, final Object paramObject, final Scriptable paramScriptable, final Method paramMethod, final Object[] paramArrayOfObject)
/*     */   {
/* 132 */     ContextAction local1 = new ContextAction()
/*     */     {
/*     */       public Object run(Context paramAnonymousContext) {
/* 135 */         return InterfaceAdapter.this.invokeImpl(paramAnonymousContext, paramObject, paramScriptable, paramMethod, paramArrayOfObject);
/*     */       }
/*     */     };
/* 138 */     return paramContextFactory.call(local1);
/*     */   }
/*     */ 
/*     */   Object invokeImpl(Context paramContext, Object paramObject, Scriptable paramScriptable, Method paramMethod, Object[] paramArrayOfObject)
/*     */   {
/* 147 */     int i = paramArrayOfObject == null ? 0 : paramArrayOfObject.length;
/*     */ 
/* 149 */     Callable localCallable = (Callable)paramObject;
/* 150 */     Scriptable localScriptable = paramScriptable;
/* 151 */     Object[] arrayOfObject = new Object[i + 1];
/* 152 */     arrayOfObject[i] = paramMethod.getName();
/* 153 */     if (i != 0) {
/* 154 */       localObject = paramContext.getWrapFactory();
/* 155 */       for (int j = 0; j != i; j++) {
/* 156 */         arrayOfObject[j] = ((WrapFactory)localObject).wrap(paramContext, paramScriptable, paramArrayOfObject[j], null);
/*     */       }
/*     */     }
/*     */ 
/* 160 */     Object localObject = localCallable.call(paramContext, paramScriptable, localScriptable, arrayOfObject);
/* 161 */     Class localClass = paramMethod.getReturnType();
/* 162 */     if (localClass == Void.TYPE)
/* 163 */       localObject = null;
/*     */     else {
/* 165 */       localObject = Context.jsToJava(localObject, localClass);
/*     */     }
/* 167 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.InterfaceAdapter
 * JD-Core Version:    0.6.2
 */