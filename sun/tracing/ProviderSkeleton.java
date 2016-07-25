/*     */ package sun.tracing;
/*     */ 
/*     */ import com.sun.tracing.Probe;
/*     */ import com.sun.tracing.Provider;
/*     */ import com.sun.tracing.ProviderName;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public abstract class ProviderSkeleton
/*     */   implements InvocationHandler, Provider
/*     */ {
/*     */   protected boolean active;
/*     */   protected Class<? extends Provider> providerType;
/*     */   protected HashMap<Method, ProbeSkeleton> probes;
/*     */ 
/*     */   protected abstract ProbeSkeleton createProbe(Method paramMethod);
/*     */ 
/*     */   protected ProviderSkeleton(Class<? extends Provider> paramClass)
/*     */   {
/*  92 */     this.active = false;
/*  93 */     this.providerType = paramClass;
/*  94 */     this.probes = new HashMap();
/*     */   }
/*     */ 
/*     */   public void init()
/*     */   {
/* 104 */     Method[] arrayOfMethod1 = (Method[])AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Method[] run() {
/* 106 */         return ProviderSkeleton.this.providerType.getDeclaredMethods();
/*     */       }
/*     */     });
/* 110 */     for (Method localMethod : arrayOfMethod1) {
/* 111 */       if (localMethod.getReturnType() != Void.TYPE) {
/* 112 */         throw new IllegalArgumentException("Return value of method is not void");
/*     */       }
/*     */ 
/* 115 */       this.probes.put(localMethod, createProbe(localMethod));
/*     */     }
/*     */ 
/* 118 */     this.active = true;
/*     */   }
/*     */ 
/*     */   public <T extends Provider> T newProxyInstance()
/*     */   {
/* 133 */     return (Provider)Proxy.newProxyInstance(this.providerType.getClassLoader(), new Class[] { this.providerType }, this);
/*     */   }
/*     */ 
/*     */   public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */   {
/* 153 */     Class localClass = paramMethod.getDeclaringClass();
/*     */ 
/* 155 */     if (localClass != this.providerType)
/*     */     {
/*     */       try
/*     */       {
/* 159 */         if ((localClass == Provider.class) || (localClass == Object.class))
/*     */         {
/* 161 */           return paramMethod.invoke(this, paramArrayOfObject);
/*     */         }
/* 163 */         if (!$assertionsDisabled) throw new AssertionError(); 
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException)
/*     */       {
/* 166 */         if (!$assertionsDisabled) throw new AssertionError(); 
/*     */       }
/* 168 */       catch (InvocationTargetException localInvocationTargetException) { if (!$assertionsDisabled) throw new AssertionError(); 
/*     */       }
/*     */     }
/*     */     else {
/* 171 */       triggerProbe(paramMethod, paramArrayOfObject);
/*     */     }
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */   public Probe getProbe(Method paramMethod)
/*     */   {
/* 183 */     return this.active ? (ProbeSkeleton)this.probes.get(paramMethod) : null;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 192 */     this.active = false;
/* 193 */     this.probes.clear();
/*     */   }
/*     */ 
/*     */   protected String getProviderName()
/*     */   {
/* 204 */     return getAnnotationString(this.providerType, ProviderName.class, this.providerType.getSimpleName());
/*     */   }
/*     */ 
/*     */   protected static String getAnnotationString(AnnotatedElement paramAnnotatedElement, Class<? extends Annotation> paramClass, String paramString)
/*     */   {
/* 221 */     String str = (String)getAnnotationValue(paramAnnotatedElement, paramClass, "value", paramString);
/*     */ 
/* 223 */     return str.isEmpty() ? paramString : str;
/*     */   }
/*     */ 
/*     */   protected static Object getAnnotationValue(AnnotatedElement paramAnnotatedElement, Class<? extends Annotation> paramClass, String paramString, Object paramObject)
/*     */   {
/* 240 */     Object localObject = paramObject;
/*     */     try {
/* 242 */       Method localMethod = paramClass.getMethod(paramString, new Class[0]);
/* 243 */       Annotation localAnnotation = paramAnnotatedElement.getAnnotation(paramClass);
/* 244 */       localObject = localMethod.invoke(localAnnotation, new Object[0]);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 246 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/* 248 */     catch (IllegalAccessException localIllegalAccessException) { if (!$assertionsDisabled) throw new AssertionError();  } catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/* 250 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/* 252 */     catch (NullPointerException localNullPointerException) { if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 254 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected void triggerProbe(Method paramMethod, Object[] paramArrayOfObject) {
/* 258 */     if (this.active) {
/* 259 */       ProbeSkeleton localProbeSkeleton = (ProbeSkeleton)this.probes.get(paramMethod);
/* 260 */       if (localProbeSkeleton != null)
/*     */       {
/* 262 */         localProbeSkeleton.uncheckedTrigger(paramArrayOfObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.ProviderSkeleton
 * JD-Core Version:    0.6.2
 */