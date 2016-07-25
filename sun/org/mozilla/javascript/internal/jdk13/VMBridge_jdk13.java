/*     */ package sun.org.mozilla.javascript.internal.jdk13;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.ContextFactory;
/*     */ import sun.org.mozilla.javascript.internal.InterfaceAdapter;
/*     */ import sun.org.mozilla.javascript.internal.Kit;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.VMBridge;
/*     */ 
/*     */ public class VMBridge_jdk13 extends VMBridge
/*     */ {
/*  52 */   private ThreadLocal<Object[]> contextLocal = new ThreadLocal();
/*     */ 
/*     */   protected Object getThreadContextHelper()
/*     */   {
/*  66 */     Object[] arrayOfObject = (Object[])this.contextLocal.get();
/*  67 */     if (arrayOfObject == null) {
/*  68 */       arrayOfObject = new Object[1];
/*  69 */       this.contextLocal.set(arrayOfObject);
/*     */     }
/*  71 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   protected Context getContext(Object paramObject)
/*     */   {
/*  77 */     Object[] arrayOfObject = (Object[])paramObject;
/*  78 */     return (Context)arrayOfObject[0];
/*     */   }
/*     */ 
/*     */   protected void setContext(Object paramObject, Context paramContext)
/*     */   {
/*  84 */     Object[] arrayOfObject = (Object[])paramObject;
/*  85 */     arrayOfObject[0] = paramContext;
/*     */   }
/*     */ 
/*     */   protected ClassLoader getCurrentThreadClassLoader()
/*     */   {
/*  91 */     return Thread.currentThread().getContextClassLoader();
/*     */   }
/*     */ 
/*     */   protected boolean tryToMakeAccessible(Object paramObject)
/*     */   {
/*  97 */     if (!(paramObject instanceof AccessibleObject)) {
/*  98 */       return false;
/*     */     }
/* 100 */     AccessibleObject localAccessibleObject = (AccessibleObject)paramObject;
/* 101 */     if (localAccessibleObject.isAccessible())
/* 102 */       return true;
/*     */     try
/*     */     {
/* 105 */       localAccessibleObject.setAccessible(true);
/*     */     } catch (Exception localException) {
/*     */     }
/* 108 */     return localAccessibleObject.isAccessible();
/*     */   }
/*     */ 
/*     */   protected Object getInterfaceProxyHelper(ContextFactory paramContextFactory, Class<?>[] paramArrayOfClass)
/*     */   {
/* 117 */     ClassLoader localClassLoader = paramArrayOfClass[0].getClassLoader();
/* 118 */     Class localClass = Proxy.getProxyClass(localClassLoader, paramArrayOfClass);
/*     */     Constructor localConstructor;
/*     */     try
/*     */     {
/* 121 */       localConstructor = localClass.getConstructor(new Class[] { InvocationHandler.class });
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException) {
/* 124 */       throw Kit.initCause(new IllegalStateException(), localNoSuchMethodException);
/*     */     }
/* 126 */     return localConstructor;
/*     */   }
/*     */ 
/*     */   protected Object newInterfaceProxy(Object paramObject1, final ContextFactory paramContextFactory, final InterfaceAdapter paramInterfaceAdapter, final Object paramObject2, final Scriptable paramScriptable)
/*     */   {
/* 136 */     Constructor localConstructor = (Constructor)paramObject1;
/*     */ 
/* 138 */     InvocationHandler local1 = new InvocationHandler()
/*     */     {
/*     */       public Object invoke(Object paramAnonymousObject, Method paramAnonymousMethod, Object[] paramAnonymousArrayOfObject)
/*     */       {
/* 143 */         return paramInterfaceAdapter.invoke(paramContextFactory, paramObject2, paramScriptable, paramAnonymousMethod, paramAnonymousArrayOfObject);
/*     */       } } ;
/*     */     Object localObject;
/*     */     try {
/* 148 */       localObject = localConstructor.newInstance(new Object[] { local1 });
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 150 */       throw Context.throwAsScriptRuntimeEx(localInvocationTargetException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 153 */       throw Kit.initCause(new IllegalStateException(), localIllegalAccessException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 156 */       throw Kit.initCause(new IllegalStateException(), localInstantiationException);
/*     */     }
/* 158 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected boolean isVarArgs(Member paramMember)
/*     */   {
/* 163 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.jdk13.VMBridge_jdk13
 * JD-Core Version:    0.6.2
 */