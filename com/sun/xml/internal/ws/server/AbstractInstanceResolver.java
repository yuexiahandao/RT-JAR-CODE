/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.InstanceResolver;
/*     */ import com.sun.xml.internal.ws.api.server.ResourceInjector;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ abstract class AbstractInstanceResolver<T> extends InstanceResolver<T>
/*     */ {
/*     */   protected static ResourceInjector getResourceInjector(WSEndpoint endpoint)
/*     */   {
/* 143 */     ResourceInjector ri = (ResourceInjector)endpoint.getContainer().getSPI(ResourceInjector.class);
/* 144 */     if (ri == null)
/* 145 */       ri = ResourceInjector.STANDALONE;
/* 146 */     return ri;
/*     */   }
/*     */ 
/*     */   protected static void invokeMethod(@Nullable Method method, final Object instance, final Object[] args)
/*     */   {
/* 153 */     if (method == null) return;
/* 154 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/*     */         try {
/* 157 */           if (!this.val$method.isAccessible()) {
/* 158 */             this.val$method.setAccessible(true);
/*     */           }
/* 160 */           MethodUtil.invoke(instance, this.val$method, args);
/*     */         } catch (IllegalAccessException e) {
/* 162 */           throw new ServerRtException("server.rt.err", new Object[] { e });
/*     */         } catch (InvocationTargetException e) {
/* 164 */           throw new ServerRtException("server.rt.err", new Object[] { e });
/*     */         }
/* 166 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   protected final Method findAnnotatedMethod(Class clazz, Class<? extends Annotation> annType)
/*     */   {
/* 176 */     boolean once = false;
/* 177 */     Method r = null;
/* 178 */     for (Method method : clazz.getDeclaredMethods()) {
/* 179 */       if (method.getAnnotation(annType) != null) {
/* 180 */         if (once)
/* 181 */           throw new ServerRtException(ServerMessages.ANNOTATION_ONLY_ONCE(annType), new Object[0]);
/* 182 */         if (method.getParameterTypes().length != 0)
/* 183 */           throw new ServerRtException(ServerMessages.NOT_ZERO_PARAMETERS(method), new Object[0]);
/* 184 */         r = method;
/* 185 */         once = true;
/*     */       }
/*     */     }
/* 188 */     return r;
/*     */   }
/*     */ 
/*     */   protected static <T, R> InjectionPlan<T, R> buildInjectionPlan(Class<? extends T> clazz, Class<R> resourceType, boolean isStatic)
/*     */   {
/* 200 */     List plan = new ArrayList();
/*     */ 
/* 202 */     Class cl = clazz;
/* 203 */     while (cl != Object.class) {
/* 204 */       for (Field field : cl.getDeclaredFields()) {
/* 205 */         Resource resource = (Resource)field.getAnnotation(Resource.class);
/* 206 */         if ((resource != null) && 
/* 207 */           (isInjectionPoint(resource, field.getType(), ServerMessages.localizableWRONG_FIELD_TYPE(field.getName()), resourceType)))
/*     */         {
/* 210 */           if ((isStatic) && (!Modifier.isStatic(field.getModifiers()))) {
/* 211 */             throw new WebServiceException(ServerMessages.STATIC_RESOURCE_INJECTION_ONLY(resourceType, field));
/*     */           }
/* 213 */           plan.add(new FieldInjectionPlan(field));
/*     */         }
/*     */       }
/*     */ 
/* 217 */       cl = cl.getSuperclass();
/*     */     }
/*     */ 
/* 220 */     cl = clazz;
/* 221 */     while (cl != Object.class) {
/* 222 */       for (Method method : cl.getDeclaredMethods()) {
/* 223 */         Resource resource = (Resource)method.getAnnotation(Resource.class);
/* 224 */         if (resource != null) {
/* 225 */           Class[] paramTypes = method.getParameterTypes();
/* 226 */           if (paramTypes.length != 1)
/* 227 */             throw new ServerRtException(ServerMessages.WRONG_NO_PARAMETERS(method), new Object[0]);
/* 228 */           if (isInjectionPoint(resource, paramTypes[0], ServerMessages.localizableWRONG_PARAMETER_TYPE(method.getName()), resourceType))
/*     */           {
/* 231 */             if ((isStatic) && (!Modifier.isStatic(method.getModifiers()))) {
/* 232 */               throw new WebServiceException(ServerMessages.STATIC_RESOURCE_INJECTION_ONLY(resourceType, method));
/*     */             }
/* 234 */             plan.add(new MethodInjectionPlan(method));
/*     */           }
/*     */         }
/*     */       }
/* 238 */       cl = cl.getSuperclass();
/*     */     }
/*     */ 
/* 241 */     return new Compositor(plan);
/*     */   }
/*     */ 
/*     */   private static boolean isInjectionPoint(Resource resource, Class fieldType, Localizable errorMessage, Class resourceType)
/*     */   {
/* 249 */     Class t = resource.type();
/* 250 */     if (t.equals(Object.class))
/* 251 */       return fieldType.equals(resourceType);
/* 252 */     if (t.equals(resourceType)) {
/* 253 */       if (fieldType.isAssignableFrom(resourceType)) {
/* 254 */         return true;
/*     */       }
/*     */ 
/* 257 */       throw new ServerRtException(errorMessage);
/*     */     }
/*     */ 
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   private static class Compositor<T, R>
/*     */     implements AbstractInstanceResolver.InjectionPlan<T, R>
/*     */   {
/*     */     private final AbstractInstanceResolver.InjectionPlan<T, R>[] children;
/*     */ 
/*     */     public Compositor(Collection<AbstractInstanceResolver.InjectionPlan<T, R>> children)
/*     */     {
/* 126 */       this.children = ((AbstractInstanceResolver.InjectionPlan[])children.toArray(new AbstractInstanceResolver.InjectionPlan[children.size()]));
/*     */     }
/*     */ 
/*     */     public void inject(T instance, R res) {
/* 130 */       for (AbstractInstanceResolver.InjectionPlan plan : this.children)
/* 131 */         plan.inject(instance, res);
/*     */     }
/*     */ 
/*     */     public int count() {
/* 135 */       int r = 0;
/* 136 */       for (AbstractInstanceResolver.InjectionPlan plan : this.children)
/* 137 */         r += plan.count();
/* 138 */       return r;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class FieldInjectionPlan<T, R>
/*     */     implements AbstractInstanceResolver.InjectionPlan<T, R>
/*     */   {
/*     */     private final Field field;
/*     */ 
/*     */     public FieldInjectionPlan(Field field)
/*     */     {
/*  76 */       this.field = field;
/*     */     }
/*     */ 
/*     */     public void inject(final T instance, final R resource) {
/*  80 */       AccessController.doPrivileged(new PrivilegedAction() { public Object run() { // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: getfield 64	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan$1:this$0	Lcom/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan;
/*     */           //   4: invokestatic 67	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan:access$000	(Lcom/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan;)Ljava/lang/reflect/Field;
/*     */           //   7: invokevirtual 70	java/lang/reflect/Field:isAccessible	()Z
/*     */           //   10: ifne +14 -> 24
/*     */           //   13: aload_0
/*     */           //   14: getfield 64	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan$1:this$0	Lcom/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan;
/*     */           //   17: invokestatic 67	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan:access$000	(Lcom/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan;)Ljava/lang/reflect/Field;
/*     */           //   20: iconst_1
/*     */           //   21: invokevirtual 71	java/lang/reflect/Field:setAccessible	(Z)V
/*     */           //   24: aload_0
/*     */           //   25: getfield 64	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan$1:this$0	Lcom/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan;
/*     */           //   28: invokestatic 67	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan:access$000	(Lcom/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan;)Ljava/lang/reflect/Field;
/*     */           //   31: aload_0
/*     */           //   32: getfield 65	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan$1:val$instance	Ljava/lang/Object;
/*     */           //   35: aload_0
/*     */           //   36: getfield 66	com/sun/xml/internal/ws/server/AbstractInstanceResolver$FieldInjectionPlan$1:val$resource	Ljava/lang/Object;
/*     */           //   39: invokevirtual 72	java/lang/reflect/Field:set	(Ljava/lang/Object;Ljava/lang/Object;)V
/*     */           //   42: aconst_null
/*     */           //   43: areturn
/*     */           //   44: astore_1
/*     */           //   45: new 39	com/sun/xml/internal/ws/server/ServerRtException
/*     */           //   48: dup
/*     */           //   49: ldc 1
/*     */           //   51: iconst_1
/*     */           //   52: anewarray 41	java/lang/Object
/*     */           //   55: dup
/*     */           //   56: iconst_0
/*     */           //   57: aload_1
/*     */           //   58: aastore
/*     */           //   59: invokespecial 68	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */           //   62: athrow
/*     */           //
/*     */           // Exception table:
/*     */           //   from	to	target	type
/*     */           //   0	43	44	java/lang/IllegalAccessException }  } ); } 
/*  96 */     public int count() { return 1; } 
/*     */   }
/*     */   protected static abstract interface InjectionPlan<T, R> {
/*     */     public abstract void inject(T paramT, R paramR);
/*     */ 
/*     */     public abstract int count();
/*     */   }
/*     */ 
/*     */   protected static class MethodInjectionPlan<T, R> implements AbstractInstanceResolver.InjectionPlan<T, R> {
/*     */     private final Method method;
/*     */ 
/* 107 */     public MethodInjectionPlan(Method method) { this.method = method; }
/*     */ 
/*     */     public void inject(T instance, R resource)
/*     */     {
/* 111 */       AbstractInstanceResolver.invokeMethod(this.method, instance, new Object[] { resource });
/*     */     }
/*     */ 
/*     */     public int count() {
/* 115 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.AbstractInstanceResolver
 * JD-Core Version:    0.6.2
 */