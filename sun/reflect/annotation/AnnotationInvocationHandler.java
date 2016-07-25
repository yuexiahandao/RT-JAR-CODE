/*     */ package sun.reflect.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.AnnotationFormatError;
/*     */ import java.lang.annotation.IncompleteAnnotationException;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ class AnnotationInvocationHandler
/*     */   implements InvocationHandler, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6182022883658399397L;
/*     */   private final Class<? extends Annotation> type;
/*     */   private final Map<String, Object> memberValues;
/* 296 */   private volatile transient Method[] memberMethods = null;
/*     */ 
/*     */   AnnotationInvocationHandler(Class<? extends Annotation> paramClass, Map<String, Object> paramMap)
/*     */   {
/*  47 */     Class[] arrayOfClass = paramClass.getInterfaces();
/*  48 */     if ((!paramClass.isAnnotation()) || (arrayOfClass.length != 1) || (arrayOfClass[0] != Annotation.class))
/*     */     {
/*  51 */       throw new AnnotationFormatError("Attempt to create proxy for a non-annotation type.");
/*  52 */     }this.type = paramClass;
/*  53 */     this.memberValues = paramMap;
/*     */   }
/*     */ 
/*     */   public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) {
/*  57 */     String str = paramMethod.getName();
/*  58 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/*     */ 
/*  61 */     if ((str.equals("equals")) && (arrayOfClass.length == 1) && (arrayOfClass[0] == Object.class))
/*     */     {
/*  63 */       return equalsImpl(paramArrayOfObject[0]);
/*  64 */     }if (arrayOfClass.length != 0) {
/*  65 */       throw new AssertionError("Too many parameters for an annotation method");
/*     */     }
/*  67 */     Object localObject = str; int i = -1; switch (((String)localObject).hashCode()) { case -1776922004:
/*  67 */       if (((String)localObject).equals("toString")) i = 0; break;
/*     */     case 147696667:
/*  67 */       if (((String)localObject).equals("hashCode")) i = 1; break;
/*     */     case 1444986633:
/*  67 */       if (((String)localObject).equals("annotationType")) i = 2; break; } switch (i) {
/*     */     case 0:
/*  69 */       return toStringImpl();
/*     */     case 1:
/*  71 */       return Integer.valueOf(hashCodeImpl());
/*     */     case 2:
/*  73 */       return this.type;
/*     */     }
/*     */ 
/*  77 */     localObject = this.memberValues.get(str);
/*     */ 
/*  79 */     if (localObject == null) {
/*  80 */       throw new IncompleteAnnotationException(this.type, str);
/*     */     }
/*  82 */     if ((localObject instanceof ExceptionProxy)) {
/*  83 */       throw ((ExceptionProxy)localObject).generateException();
/*     */     }
/*  85 */     if ((localObject.getClass().isArray()) && (Array.getLength(localObject) != 0)) {
/*  86 */       localObject = cloneArray(localObject);
/*     */     }
/*  88 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Object cloneArray(Object paramObject)
/*     */   {
/*  96 */     Class localClass = paramObject.getClass();
/*     */ 
/*  98 */     if (localClass == [B.class) {
/*  99 */       localObject = (byte[])paramObject;
/* 100 */       return ((byte[])localObject).clone();
/*     */     }
/* 102 */     if (localClass == [C.class) {
/* 103 */       localObject = (char[])paramObject;
/* 104 */       return ((char[])localObject).clone();
/*     */     }
/* 106 */     if (localClass == [D.class) {
/* 107 */       localObject = (double[])paramObject;
/* 108 */       return ((double[])localObject).clone();
/*     */     }
/* 110 */     if (localClass == [F.class) {
/* 111 */       localObject = (float[])paramObject;
/* 112 */       return ((float[])localObject).clone();
/*     */     }
/* 114 */     if (localClass == [I.class) {
/* 115 */       localObject = (int[])paramObject;
/* 116 */       return ((int[])localObject).clone();
/*     */     }
/* 118 */     if (localClass == [J.class) {
/* 119 */       localObject = (long[])paramObject;
/* 120 */       return ((long[])localObject).clone();
/*     */     }
/* 122 */     if (localClass == [S.class) {
/* 123 */       localObject = (short[])paramObject;
/* 124 */       return ((short[])localObject).clone();
/*     */     }
/* 126 */     if (localClass == [Z.class) {
/* 127 */       localObject = (boolean[])paramObject;
/* 128 */       return ((boolean[])localObject).clone();
/*     */     }
/*     */ 
/* 131 */     Object localObject = (Object[])paramObject;
/* 132 */     return ((Object[])localObject).clone();
/*     */   }
/*     */ 
/*     */   private String toStringImpl()
/*     */   {
/* 140 */     StringBuilder localStringBuilder = new StringBuilder(128);
/* 141 */     localStringBuilder.append('@');
/* 142 */     localStringBuilder.append(this.type.getName());
/* 143 */     localStringBuilder.append('(');
/* 144 */     int i = 1;
/* 145 */     for (Map.Entry localEntry : this.memberValues.entrySet()) {
/* 146 */       if (i != 0)
/* 147 */         i = 0;
/*     */       else {
/* 149 */         localStringBuilder.append(", ");
/*     */       }
/* 151 */       localStringBuilder.append((String)localEntry.getKey());
/* 152 */       localStringBuilder.append('=');
/* 153 */       localStringBuilder.append(memberValueToString(localEntry.getValue()));
/*     */     }
/* 155 */     localStringBuilder.append(')');
/* 156 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static String memberValueToString(Object paramObject)
/*     */   {
/* 163 */     Class localClass = paramObject.getClass();
/* 164 */     if (!localClass.isArray())
/*     */     {
/* 166 */       return paramObject.toString();
/*     */     }
/* 168 */     if (localClass == [B.class)
/* 169 */       return Arrays.toString((byte[])paramObject);
/* 170 */     if (localClass == [C.class)
/* 171 */       return Arrays.toString((char[])paramObject);
/* 172 */     if (localClass == [D.class)
/* 173 */       return Arrays.toString((double[])paramObject);
/* 174 */     if (localClass == [F.class)
/* 175 */       return Arrays.toString((float[])paramObject);
/* 176 */     if (localClass == [I.class)
/* 177 */       return Arrays.toString((int[])paramObject);
/* 178 */     if (localClass == [J.class)
/* 179 */       return Arrays.toString((long[])paramObject);
/* 180 */     if (localClass == [S.class)
/* 181 */       return Arrays.toString((short[])paramObject);
/* 182 */     if (localClass == [Z.class)
/* 183 */       return Arrays.toString((boolean[])paramObject);
/* 184 */     return Arrays.toString((Object[])paramObject);
/*     */   }
/*     */ 
/*     */   private Boolean equalsImpl(Object paramObject)
/*     */   {
/* 191 */     if (paramObject == this) {
/* 192 */       return Boolean.valueOf(true);
/*     */     }
/* 194 */     if (!this.type.isInstance(paramObject))
/* 195 */       return Boolean.valueOf(false);
/* 196 */     for (Method localMethod : getMemberMethods()) {
/* 197 */       String str = localMethod.getName();
/* 198 */       Object localObject1 = this.memberValues.get(str);
/* 199 */       Object localObject2 = null;
/* 200 */       AnnotationInvocationHandler localAnnotationInvocationHandler = asOneOfUs(paramObject);
/* 201 */       if (localAnnotationInvocationHandler != null)
/* 202 */         localObject2 = localAnnotationInvocationHandler.memberValues.get(str);
/*     */       else {
/*     */         try {
/* 205 */           localObject2 = localMethod.invoke(paramObject, new Object[0]);
/*     */         } catch (InvocationTargetException localInvocationTargetException) {
/* 207 */           return Boolean.valueOf(false);
/*     */         } catch (IllegalAccessException localIllegalAccessException) {
/* 209 */           throw new AssertionError(localIllegalAccessException);
/*     */         }
/*     */       }
/* 212 */       if (!memberValueEquals(localObject1, localObject2))
/* 213 */         return Boolean.valueOf(false);
/*     */     }
/* 215 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   private AnnotationInvocationHandler asOneOfUs(Object paramObject)
/*     */   {
/* 224 */     if (Proxy.isProxyClass(paramObject.getClass())) {
/* 225 */       InvocationHandler localInvocationHandler = Proxy.getInvocationHandler(paramObject);
/* 226 */       if ((localInvocationHandler instanceof AnnotationInvocationHandler))
/* 227 */         return (AnnotationInvocationHandler)localInvocationHandler;
/*     */     }
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   private static boolean memberValueEquals(Object paramObject1, Object paramObject2)
/*     */   {
/* 241 */     Class localClass = paramObject1.getClass();
/*     */ 
/* 245 */     if (!localClass.isArray()) {
/* 246 */       return paramObject1.equals(paramObject2);
/*     */     }
/*     */ 
/* 250 */     if (((paramObject1 instanceof Object[])) && ((paramObject2 instanceof Object[]))) {
/* 251 */       return Arrays.equals((Object[])paramObject1, (Object[])paramObject2);
/*     */     }
/*     */ 
/* 254 */     if (paramObject2.getClass() != localClass) {
/* 255 */       return false;
/*     */     }
/*     */ 
/* 258 */     if (localClass == [B.class)
/* 259 */       return Arrays.equals((byte[])paramObject1, (byte[])paramObject2);
/* 260 */     if (localClass == [C.class)
/* 261 */       return Arrays.equals((char[])paramObject1, (char[])paramObject2);
/* 262 */     if (localClass == [D.class)
/* 263 */       return Arrays.equals((double[])paramObject1, (double[])paramObject2);
/* 264 */     if (localClass == [F.class)
/* 265 */       return Arrays.equals((float[])paramObject1, (float[])paramObject2);
/* 266 */     if (localClass == [I.class)
/* 267 */       return Arrays.equals((int[])paramObject1, (int[])paramObject2);
/* 268 */     if (localClass == [J.class)
/* 269 */       return Arrays.equals((long[])paramObject1, (long[])paramObject2);
/* 270 */     if (localClass == [S.class)
/* 271 */       return Arrays.equals((short[])paramObject1, (short[])paramObject2);
/* 272 */     assert (localClass == [Z.class);
/* 273 */     return Arrays.equals((boolean[])paramObject1, (boolean[])paramObject2);
/*     */   }
/*     */ 
/*     */   private Method[] getMemberMethods()
/*     */   {
/* 283 */     if (this.memberMethods == null) {
/* 284 */       this.memberMethods = ((Method[])AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Method[] run() {
/* 287 */           Method[] arrayOfMethod = AnnotationInvocationHandler.this.type.getDeclaredMethods();
/* 288 */           AnnotationInvocationHandler.this.validateAnnotationMethods(arrayOfMethod);
/* 289 */           AccessibleObject.setAccessible(arrayOfMethod, true);
/* 290 */           return arrayOfMethod;
/*     */         }
/*     */       }));
/*     */     }
/* 294 */     return this.memberMethods;
/*     */   }
/*     */ 
/*     */   private void validateAnnotationMethods(Method[] paramArrayOfMethod)
/*     */   {
/* 310 */     int i = 1;
/* 311 */     for (Method localMethod : paramArrayOfMethod)
/*     */     {
/* 322 */       if ((localMethod.getModifiers() != 1025) || (localMethod.getParameterTypes().length != 0) || (localMethod.getExceptionTypes().length != 0))
/*     */       {
/* 325 */         i = 0;
/* 326 */         break;
/*     */       }
/*     */ 
/* 338 */       Class localClass = localMethod.getReturnType();
/* 339 */       if (localClass.isArray()) {
/* 340 */         localClass = localClass.getComponentType();
/* 341 */         if (localClass.isArray()) {
/* 342 */           i = 0;
/* 343 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 347 */       if (((!localClass.isPrimitive()) || (localClass == Void.TYPE)) && (localClass != String.class) && (localClass != Class.class) && (!localClass.isEnum()) && (!localClass.isAnnotation()))
/*     */       {
/* 352 */         i = 0;
/* 353 */         break;
/*     */       }
/*     */ 
/* 371 */       String str = localMethod.getName();
/* 372 */       if (((str.equals("toString")) && (localClass == String.class)) || ((str.equals("hashCode")) && (localClass == Integer.TYPE)) || ((str.equals("annotationType")) && (localClass == Class.class)))
/*     */       {
/* 375 */         i = 0;
/* 376 */         break;
/*     */       }
/*     */     }
/* 379 */     if (i != 0) {
/* 380 */       return;
/*     */     }
/* 382 */     throw new AnnotationFormatError("Malformed method on an annotation type");
/*     */   }
/*     */ 
/*     */   private int hashCodeImpl()
/*     */   {
/* 389 */     int i = 0;
/* 390 */     for (Map.Entry localEntry : this.memberValues.entrySet()) {
/* 391 */       i += (127 * ((String)localEntry.getKey()).hashCode() ^ memberValueHashCode(localEntry.getValue()));
/*     */     }
/*     */ 
/* 394 */     return i;
/*     */   }
/*     */ 
/*     */   private static int memberValueHashCode(Object paramObject)
/*     */   {
/* 401 */     Class localClass = paramObject.getClass();
/* 402 */     if (!localClass.isArray())
/*     */     {
/* 404 */       return paramObject.hashCode();
/*     */     }
/* 406 */     if (localClass == [B.class)
/* 407 */       return Arrays.hashCode((byte[])paramObject);
/* 408 */     if (localClass == [C.class)
/* 409 */       return Arrays.hashCode((char[])paramObject);
/* 410 */     if (localClass == [D.class)
/* 411 */       return Arrays.hashCode((double[])paramObject);
/* 412 */     if (localClass == [F.class)
/* 413 */       return Arrays.hashCode((float[])paramObject);
/* 414 */     if (localClass == [I.class)
/* 415 */       return Arrays.hashCode((int[])paramObject);
/* 416 */     if (localClass == [J.class)
/* 417 */       return Arrays.hashCode((long[])paramObject);
/* 418 */     if (localClass == [S.class)
/* 419 */       return Arrays.hashCode((short[])paramObject);
/* 420 */     if (localClass == [Z.class)
/* 421 */       return Arrays.hashCode((boolean[])paramObject);
/* 422 */     return Arrays.hashCode((Object[])paramObject);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 427 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 431 */     AnnotationType localAnnotationType = null;
/*     */     try {
/* 433 */       localAnnotationType = AnnotationType.getInstance(this.type);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 436 */       throw new InvalidObjectException("Non-annotation type in annotation serial stream");
/*     */     }
/*     */ 
/* 439 */     Map localMap = localAnnotationType.memberTypes();
/*     */ 
/* 443 */     for (Map.Entry localEntry : this.memberValues.entrySet()) {
/* 444 */       String str = (String)localEntry.getKey();
/* 445 */       Class localClass = (Class)localMap.get(str);
/* 446 */       if (localClass != null) {
/* 447 */         Object localObject = localEntry.getValue();
/* 448 */         if ((!localClass.isInstance(localObject)) && (!(localObject instanceof ExceptionProxy)))
/*     */         {
/* 450 */           localEntry.setValue(new AnnotationTypeMismatchExceptionProxy(localObject.getClass() + "[" + localObject + "]").setMember((Method)localAnnotationType.members().get(str)));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.annotation.AnnotationInvocationHandler
 * JD-Core Version:    0.6.2
 */