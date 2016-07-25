/*     */ package java.beans;
/*     */ 
/*     */ import com.sun.beans.finder.ClassFinder;
/*     */ import com.sun.beans.finder.ConstructorFinder;
/*     */ import com.sun.beans.finder.MethodFinder;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ 
/*     */ public class Statement
/*     */ {
/*  60 */   private static Object[] emptyArray = new Object[0];
/*     */ 
/*  62 */   static ExceptionListener defaultExceptionListener = new ExceptionListener() {
/*     */     public void exceptionThrown(Exception paramAnonymousException) {
/*  64 */       System.err.println(paramAnonymousException);
/*     */ 
/*  66 */       System.err.println("Continuing ...");
/*     */     }
/*  62 */   };
/*     */ 
/*  70 */   private final AccessControlContext acc = AccessController.getContext();
/*     */   private final Object target;
/*     */   private final String methodName;
/*     */   private final Object[] arguments;
/*     */   ClassLoader loader;
/*     */ 
/*     */   @ConstructorProperties({"target", "methodName", "arguments"})
/*     */   public Statement(Object paramObject, String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  93 */     this.target = paramObject;
/*  94 */     this.methodName = paramString;
/*  95 */     this.arguments = (paramArrayOfObject == null ? emptyArray : paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Object getTarget()
/*     */   {
/* 107 */     return this.target;
/*     */   }
/*     */ 
/*     */   public String getMethodName()
/*     */   {
/* 119 */     return this.methodName;
/*     */   }
/*     */ 
/*     */   public Object[] getArguments()
/*     */   {
/* 131 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   public void execute()
/*     */     throws Exception
/*     */   {
/* 173 */     invoke();
/*     */   }
/*     */ 
/*     */   Object invoke() throws Exception {
/* 177 */     AccessControlContext localAccessControlContext = this.acc;
/* 178 */     if ((localAccessControlContext == null) && (System.getSecurityManager() != null))
/* 179 */       throw new SecurityException("AccessControlContext is not set");
/*     */     try
/*     */     {
/* 182 */       return AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws Exception {
/* 185 */           return Statement.this.invokeInternal();
/*     */         }
/*     */       }
/*     */       , localAccessControlContext);
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 192 */       throw localPrivilegedActionException.getException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object invokeInternal() throws Exception {
/* 197 */     Object localObject1 = getTarget();
/* 198 */     String str = getMethodName();
/*     */ 
/* 200 */     if ((localObject1 == null) || (str == null)) {
/* 201 */       throw new NullPointerException((localObject1 == null ? "target" : "methodName") + " should not be null");
/*     */     }
/*     */ 
/* 205 */     Object[] arrayOfObject = getArguments();
/* 206 */     if (arrayOfObject == null) {
/* 207 */       arrayOfObject = emptyArray;
/*     */     }
/*     */ 
/* 212 */     if ((localObject1 == Class.class) && (str.equals("forName"))) {
/* 213 */       return ClassFinder.resolveClass((String)arrayOfObject[0], this.loader);
/*     */     }
/* 215 */     Class[] arrayOfClass = new Class[arrayOfObject.length];
/* 216 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 217 */       arrayOfClass[i] = (arrayOfObject[i] == null ? null : arrayOfObject[i].getClass());
/*     */     }
/*     */ 
/* 220 */     Object localObject2 = null;
/* 221 */     if ((localObject1 instanceof Class))
/*     */     {
/* 232 */       if (str.equals("new")) {
/* 233 */         str = "newInstance";
/*     */       }
/*     */ 
/* 236 */       if ((str.equals("newInstance")) && (((Class)localObject1).isArray())) {
/* 237 */         Object localObject3 = Array.newInstance(((Class)localObject1).getComponentType(), arrayOfObject.length);
/* 238 */         for (int k = 0; k < arrayOfObject.length; k++) {
/* 239 */           Array.set(localObject3, k, arrayOfObject[k]);
/*     */         }
/* 241 */         return localObject3;
/*     */       }
/* 243 */       if ((str.equals("newInstance")) && (arrayOfObject.length != 0))
/*     */       {
/* 249 */         if ((localObject1 == Character.class) && (arrayOfObject.length == 1) && (arrayOfClass[0] == String.class))
/*     */         {
/* 251 */           return new Character(((String)arrayOfObject[0]).charAt(0));
/*     */         }
/*     */         try {
/* 254 */           localObject2 = ConstructorFinder.findConstructor((Class)localObject1, arrayOfClass);
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException) {
/* 257 */           localObject2 = null;
/*     */         }
/*     */       }
/* 260 */       if ((localObject2 == null) && (localObject1 != Class.class)) {
/* 261 */         localObject2 = getMethod((Class)localObject1, str, arrayOfClass);
/*     */       }
/* 263 */       if (localObject2 == null) {
/* 264 */         localObject2 = getMethod(Class.class, str, arrayOfClass);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 276 */       if ((localObject1.getClass().isArray()) && ((str.equals("set")) || (str.equals("get"))))
/*     */       {
/* 278 */         int j = ((Integer)arrayOfObject[0]).intValue();
/* 279 */         if (str.equals("get")) {
/* 280 */           return Array.get(localObject1, j);
/*     */         }
/*     */ 
/* 283 */         Array.set(localObject1, j, arrayOfObject[1]);
/* 284 */         return null;
/*     */       }
/*     */ 
/* 287 */       localObject2 = getMethod(localObject1.getClass(), str, arrayOfClass);
/*     */     }
/* 289 */     if (localObject2 != null) {
/*     */       try {
/* 291 */         if ((localObject2 instanceof Method)) {
/* 292 */           return MethodUtil.invoke((Method)localObject2, localObject1, arrayOfObject);
/*     */         }
/*     */ 
/* 295 */         return ((Constructor)localObject2).newInstance(arrayOfObject);
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException)
/*     */       {
/* 299 */         throw new Exception("Statement cannot invoke: " + str + " on " + localObject1.getClass(), localIllegalAccessException);
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException)
/*     */       {
/* 304 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 305 */         if ((localThrowable instanceof Exception)) {
/* 306 */           throw ((Exception)localThrowable);
/*     */         }
/*     */ 
/* 309 */         throw localInvocationTargetException;
/*     */       }
/*     */     }
/*     */ 
/* 313 */     throw new NoSuchMethodException(toString());
/*     */   }
/*     */ 
/*     */   String instanceName(Object paramObject) {
/* 317 */     if (paramObject == null)
/* 318 */       return "null";
/* 319 */     if (paramObject.getClass() == String.class) {
/* 320 */       return "\"" + (String)paramObject + "\"";
/*     */     }
/*     */ 
/* 328 */     return NameGenerator.unqualifiedClassName(paramObject.getClass());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 337 */     Object localObject = getTarget();
/* 338 */     String str = getMethodName();
/* 339 */     Object[] arrayOfObject = getArguments();
/* 340 */     if (arrayOfObject == null) {
/* 341 */       arrayOfObject = emptyArray;
/*     */     }
/* 343 */     StringBuffer localStringBuffer = new StringBuffer(instanceName(localObject) + "." + str + "(");
/* 344 */     int i = arrayOfObject.length;
/* 345 */     for (int j = 0; j < i; j++) {
/* 346 */       localStringBuffer.append(instanceName(arrayOfObject[j]));
/* 347 */       if (j != i - 1) {
/* 348 */         localStringBuffer.append(", ");
/*     */       }
/*     */     }
/* 351 */     localStringBuffer.append(");");
/* 352 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static Method getMethod(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass) {
/*     */     try {
/* 357 */       return MethodFinder.findMethod(paramClass, paramString, paramArrayOfClass);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */     }
/* 360 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.Statement
 * JD-Core Version:    0.6.2
 */