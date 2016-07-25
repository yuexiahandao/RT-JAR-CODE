/*     */ package java.beans;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ 
/*     */ public class EventHandler
/*     */   implements InvocationHandler
/*     */ {
/*     */   private Object target;
/*     */   private String action;
/*     */   private final String eventPropertyName;
/*     */   private final String listenerMethodName;
/* 283 */   private final AccessControlContext acc = AccessController.getContext();
/*     */ 
/*     */   @ConstructorProperties({"target", "action", "eventPropertyName", "listenerMethodName"})
/*     */   public EventHandler(Object paramObject, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 312 */     this.target = paramObject;
/* 313 */     this.action = paramString1;
/* 314 */     if (paramObject == null) {
/* 315 */       throw new NullPointerException("target must be non-null");
/*     */     }
/* 317 */     if (paramString1 == null) {
/* 318 */       throw new NullPointerException("action must be non-null");
/*     */     }
/* 320 */     this.eventPropertyName = paramString2;
/* 321 */     this.listenerMethodName = paramString3;
/*     */   }
/*     */ 
/*     */   public Object getTarget()
/*     */   {
/* 331 */     return this.target;
/*     */   }
/*     */ 
/*     */   public String getAction()
/*     */   {
/* 344 */     return this.action;
/*     */   }
/*     */ 
/*     */   public String getEventPropertyName()
/*     */   {
/* 356 */     return this.eventPropertyName;
/*     */   }
/*     */ 
/*     */   public String getListenerMethodName()
/*     */   {
/* 369 */     return this.listenerMethodName;
/*     */   }
/*     */ 
/*     */   private Object applyGetters(Object paramObject, String paramString) {
/* 373 */     if ((paramString == null) || (paramString.equals(""))) {
/* 374 */       return paramObject;
/*     */     }
/* 376 */     int i = paramString.indexOf('.');
/* 377 */     if (i == -1) {
/* 378 */       i = paramString.length();
/*     */     }
/* 380 */     String str1 = paramString.substring(0, i);
/* 381 */     String str2 = paramString.substring(Math.min(i + 1, paramString.length()));
/*     */     try
/*     */     {
/* 384 */       Method localMethod = null;
/* 385 */       if (paramObject != null) {
/* 386 */         localMethod = Statement.getMethod(paramObject.getClass(), "get" + NameGenerator.capitalize(str1), new Class[0]);
/*     */ 
/* 389 */         if (localMethod == null) {
/* 390 */           localMethod = Statement.getMethod(paramObject.getClass(), "is" + NameGenerator.capitalize(str1), new Class[0]);
/*     */         }
/*     */ 
/* 394 */         if (localMethod == null) {
/* 395 */           localMethod = Statement.getMethod(paramObject.getClass(), str1, new Class[0]);
/*     */         }
/*     */       }
/* 398 */       if (localMethod == null) {
/* 399 */         throw new RuntimeException("No method called: " + str1 + " defined on " + paramObject);
/*     */       }
/*     */ 
/* 402 */       Object localObject = MethodUtil.invoke(localMethod, paramObject, new Object[0]);
/* 403 */       return applyGetters(localObject, str2);
/*     */     }
/*     */     catch (Exception localException) {
/* 406 */       throw new RuntimeException("Failed to call method: " + str1 + " on " + paramObject, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object invoke(final Object paramObject, final Method paramMethod, final Object[] paramArrayOfObject)
/*     */   {
/* 423 */     AccessControlContext localAccessControlContext = this.acc;
/* 424 */     if ((localAccessControlContext == null) && (System.getSecurityManager() != null)) {
/* 425 */       throw new SecurityException("AccessControlContext is not set");
/*     */     }
/* 427 */     return AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 429 */         return EventHandler.this.invokeInternal(paramObject, paramMethod, paramArrayOfObject);
/*     */       }
/*     */     }
/*     */     , localAccessControlContext);
/*     */   }
/*     */ 
/*     */   private Object invokeInternal(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */   {
/* 435 */     String str1 = paramMethod.getName();
/* 436 */     if (paramMethod.getDeclaringClass() == Object.class)
/*     */     {
/* 438 */       if (str1.equals("hashCode"))
/* 439 */         return new Integer(System.identityHashCode(paramObject));
/* 440 */       if (str1.equals("equals"))
/* 441 */         return paramObject == paramArrayOfObject[0] ? Boolean.TRUE : Boolean.FALSE;
/* 442 */       if (str1.equals("toString")) {
/* 443 */         return paramObject.getClass().getName() + '@' + Integer.toHexString(paramObject.hashCode());
/*     */       }
/*     */     }
/*     */ 
/* 447 */     if ((this.listenerMethodName == null) || (this.listenerMethodName.equals(str1))) {
/* 448 */       Class[] arrayOfClass = null;
/* 449 */       Object[] arrayOfObject = null;
/*     */ 
/* 451 */       if (this.eventPropertyName == null) {
/* 452 */         arrayOfObject = new Object[0];
/* 453 */         arrayOfClass = new Class[0];
/*     */       }
/*     */       else {
/* 456 */         Object localObject1 = applyGetters(paramArrayOfObject[0], getEventPropertyName());
/* 457 */         arrayOfObject = new Object[] { localObject1 };
/* 458 */         arrayOfClass = new Class[] { localObject1 == null ? null : localObject1.getClass() };
/*     */       }
/*     */       try
/*     */       {
/* 462 */         int i = this.action.lastIndexOf('.');
/* 463 */         if (i != -1) {
/* 464 */           this.target = applyGetters(this.target, this.action.substring(0, i));
/* 465 */           this.action = this.action.substring(i + 1);
/*     */         }
/* 467 */         localObject2 = Statement.getMethod(this.target.getClass(), this.action, arrayOfClass);
/*     */ 
/* 469 */         if (localObject2 == null) {
/* 470 */           localObject2 = Statement.getMethod(this.target.getClass(), "set" + NameGenerator.capitalize(this.action), arrayOfClass);
/*     */         }
/*     */ 
/* 473 */         if (localObject2 == null) {
/* 474 */           String str2 = " with argument " + arrayOfClass[0];
/*     */ 
/* 477 */           throw new RuntimeException("No method called " + this.action + " on " + this.target.getClass() + str2);
/*     */         }
/*     */ 
/* 481 */         return MethodUtil.invoke((Method)localObject2, this.target, arrayOfObject);
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException) {
/* 484 */         throw new RuntimeException(localIllegalAccessException);
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException) {
/* 487 */         Object localObject2 = localInvocationTargetException.getTargetException();
/* 488 */         throw ((localObject2 instanceof RuntimeException) ? (RuntimeException)localObject2 : new RuntimeException((Throwable)localObject2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 493 */     return null;
/*     */   }
/*     */ 
/*     */   public static <T> T create(Class<T> paramClass, Object paramObject, String paramString)
/*     */   {
/* 533 */     return create(paramClass, paramObject, paramString, null, null);
/*     */   }
/*     */ 
/*     */   public static <T> T create(Class<T> paramClass, Object paramObject, String paramString1, String paramString2)
/*     */   {
/* 591 */     return create(paramClass, paramObject, paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   public static <T> T create(Class<T> paramClass, Object paramObject, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 683 */     EventHandler localEventHandler = new EventHandler(paramObject, paramString1, paramString2, paramString3);
/*     */ 
/* 686 */     if (paramClass == null) {
/* 687 */       throw new NullPointerException("listenerInterface must be non-null");
/*     */     }
/*     */ 
/* 690 */     return Proxy.newProxyInstance(paramObject.getClass().getClassLoader(), new Class[] { paramClass }, localEventHandler);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.EventHandler
 * JD-Core Version:    0.6.2
 */