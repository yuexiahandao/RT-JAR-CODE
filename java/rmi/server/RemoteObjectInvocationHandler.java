/*     */ package java.rmi.server;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.UnexpectedException;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.rmi.server.Util;
/*     */ import sun.rmi.server.WeakClassHashMap;
/*     */ 
/*     */ public class RemoteObjectInvocationHandler extends RemoteObject
/*     */   implements InvocationHandler
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  63 */   private static final MethodToHash_Maps methodToHash_Maps = new MethodToHash_Maps();
/*     */ 
/*     */   public RemoteObjectInvocationHandler(RemoteRef paramRemoteRef)
/*     */   {
/*  75 */     super(paramRemoteRef);
/*  76 */     if (paramRemoteRef == null)
/*  77 */       throw new NullPointerException();
/*     */   }
/*     */ 
/*     */   public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */     throws Throwable
/*     */   {
/* 145 */     if (paramMethod.getDeclaringClass() == Object.class) {
/* 146 */       return invokeObjectMethod(paramObject, paramMethod, paramArrayOfObject);
/*     */     }
/* 148 */     return invokeRemoteMethod(paramObject, paramMethod, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private Object invokeObjectMethod(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */   {
/* 159 */     String str = paramMethod.getName();
/*     */ 
/* 161 */     if (str.equals("hashCode")) {
/* 162 */       return Integer.valueOf(hashCode());
/*     */     }
/* 164 */     if (str.equals("equals")) {
/* 165 */       Object localObject = paramArrayOfObject[0];
/* 166 */       return Boolean.valueOf((paramObject == localObject) || ((localObject != null) && (Proxy.isProxyClass(localObject.getClass())) && (equals(Proxy.getInvocationHandler(localObject)))));
/*     */     }
/*     */ 
/* 172 */     if (str.equals("toString")) {
/* 173 */       return proxyToString(paramObject);
/*     */     }
/*     */ 
/* 176 */     throw new IllegalArgumentException("unexpected Object method: " + paramMethod);
/*     */   }
/*     */ 
/*     */   private Object invokeRemoteMethod(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 190 */       if (!(paramObject instanceof Remote)) {
/* 191 */         throw new IllegalArgumentException("proxy not Remote instance");
/*     */       }
/*     */ 
/* 194 */       return this.ref.invoke((Remote)paramObject, paramMethod, paramArrayOfObject, getMethodHash(paramMethod));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */       UnexpectedException localUnexpectedException;
/* 197 */       if (!(localException instanceof RuntimeException)) {
/* 198 */         Class localClass1 = paramObject.getClass();
/*     */         try {
/* 200 */           paramMethod = localClass1.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException) {
/* 203 */           throw ((IllegalArgumentException)new IllegalArgumentException().initCause(localNoSuchMethodException));
/*     */         }
/*     */ 
/* 206 */         Class localClass2 = localException.getClass();
/* 207 */         for (Class localClass3 : paramMethod.getExceptionTypes()) {
/* 208 */           if (localClass3.isAssignableFrom(localClass2)) {
/* 209 */             throw localException;
/*     */           }
/*     */         }
/* 212 */         localUnexpectedException = new UnexpectedException("unexpected exception", localException);
/*     */       }
/* 214 */       throw localUnexpectedException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private String proxyToString(Object paramObject)
/*     */   {
/* 223 */     Class[] arrayOfClass = paramObject.getClass().getInterfaces();
/* 224 */     if (arrayOfClass.length == 0) {
/* 225 */       return "Proxy[" + this + "]";
/*     */     }
/* 227 */     String str = arrayOfClass[0].getName();
/* 228 */     if ((str.equals("java.rmi.Remote")) && (arrayOfClass.length > 1)) {
/* 229 */       str = arrayOfClass[1].getName();
/*     */     }
/* 231 */     int i = str.lastIndexOf('.');
/* 232 */     if (i >= 0) {
/* 233 */       str = str.substring(i + 1);
/*     */     }
/* 235 */     return "Proxy[" + str + "," + this + "]";
/*     */   }
/*     */ 
/*     */   private void readObjectNoData()
/*     */     throws InvalidObjectException
/*     */   {
/* 242 */     throw new InvalidObjectException("no data in stream; class: " + getClass().getName());
/*     */   }
/*     */ 
/*     */   private static long getMethodHash(Method paramMethod)
/*     */   {
/* 257 */     return ((Long)((Map)methodToHash_Maps.get(paramMethod.getDeclaringClass())).get(paramMethod)).longValue();
/*     */   }
/*     */ 
/*     */   private static class MethodToHash_Maps extends WeakClassHashMap<Map<Method, Long>>
/*     */   {
/*     */     protected Map<Method, Long> computeValue(Class<?> paramClass)
/*     */     {
/* 270 */       return new WeakHashMap() {
/*     */         public synchronized Long get(Object paramAnonymousObject) {
/* 272 */           Long localLong = (Long)super.get(paramAnonymousObject);
/* 273 */           if (localLong == null) {
/* 274 */             Method localMethod = (Method)paramAnonymousObject;
/* 275 */             localLong = Long.valueOf(Util.computeMethodHash(localMethod));
/* 276 */             put(localMethod, localLong);
/*     */           }
/* 278 */           return localLong;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.RemoteObjectInvocationHandler
 * JD-Core Version:    0.6.2
 */