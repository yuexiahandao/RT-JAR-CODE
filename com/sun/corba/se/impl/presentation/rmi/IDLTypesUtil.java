/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.omg.CORBA.portable.IDLEntity;
/*     */ 
/*     */ public final class IDLTypesUtil
/*     */ {
/*     */   private static final String GET_PROPERTY_PREFIX = "get";
/*     */   private static final String SET_PROPERTY_PREFIX = "set";
/*     */   private static final String IS_PROPERTY_PREFIX = "is";
/*     */   public static final int VALID_TYPE = 0;
/*     */   public static final int INVALID_TYPE = 1;
/*     */   public static final boolean FOLLOW_RMIC = true;
/*     */ 
/*     */   public void validateRemoteInterface(Class paramClass)
/*     */     throws IDLTypeException
/*     */   {
/*  66 */     if (paramClass == null) {
/*  67 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*  70 */     if (!paramClass.isInterface()) {
/*  71 */       localObject = "Class " + paramClass + " must be a java interface.";
/*  72 */       throw new IDLTypeException((String)localObject);
/*     */     }
/*     */ 
/*  75 */     if (!Remote.class.isAssignableFrom(paramClass)) {
/*  76 */       localObject = "Class " + paramClass + " must extend java.rmi.Remote, " + "either directly or indirectly.";
/*     */ 
/*  78 */       throw new IDLTypeException((String)localObject);
/*     */     }
/*     */ 
/*  82 */     java.lang.Object localObject = paramClass.getMethods();
/*     */ 
/*  84 */     for (int i = 0; i < localObject.length; i++) {
/*  85 */       Method localMethod = localObject[i];
/*  86 */       validateExceptions(localMethod);
/*     */     }
/*     */ 
/*  91 */     validateConstants(paramClass);
/*     */   }
/*     */ 
/*     */   public boolean isRemoteInterface(Class paramClass)
/*     */   {
/*  98 */     boolean bool = true;
/*     */     try {
/* 100 */       validateRemoteInterface(paramClass);
/*     */     } catch (IDLTypeException localIDLTypeException) {
/* 102 */       bool = false;
/*     */     }
/*     */ 
/* 105 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive(Class paramClass)
/*     */   {
/* 113 */     if (paramClass == null) {
/* 114 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 117 */     return paramClass.isPrimitive();
/*     */   }
/*     */ 
/*     */   public boolean isValue(Class paramClass)
/*     */   {
/* 125 */     if (paramClass == null) {
/* 126 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 129 */     return (!paramClass.isInterface()) && (Serializable.class.isAssignableFrom(paramClass)) && (!Remote.class.isAssignableFrom(paramClass));
/*     */   }
/*     */ 
/*     */   public boolean isArray(Class paramClass)
/*     */   {
/* 140 */     boolean bool = false;
/*     */ 
/* 142 */     if (paramClass == null) {
/* 143 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 146 */     if (paramClass.isArray()) {
/* 147 */       Class localClass = paramClass.getComponentType();
/* 148 */       bool = (isPrimitive(localClass)) || (isRemoteInterface(localClass)) || (isEntity(localClass)) || (isException(localClass)) || (isValue(localClass)) || (isObjectReference(localClass));
/*     */     }
/*     */ 
/* 154 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isException(Class paramClass)
/*     */   {
/* 162 */     if (paramClass == null) {
/* 163 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 168 */     return (isCheckedException(paramClass)) && (!isRemoteException(paramClass)) && (isValue(paramClass));
/*     */   }
/*     */ 
/*     */   public boolean isRemoteException(Class paramClass)
/*     */   {
/* 173 */     if (paramClass == null) {
/* 174 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 177 */     return RemoteException.class.isAssignableFrom(paramClass);
/*     */   }
/*     */ 
/*     */   public boolean isCheckedException(Class paramClass)
/*     */   {
/* 182 */     if (paramClass == null) {
/* 183 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 186 */     return (Throwable.class.isAssignableFrom(paramClass)) && (!RuntimeException.class.isAssignableFrom(paramClass)) && (!Error.class.isAssignableFrom(paramClass));
/*     */   }
/*     */ 
/*     */   public boolean isObjectReference(Class paramClass)
/*     */   {
/* 196 */     if (paramClass == null) {
/* 197 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 200 */     return (paramClass.isInterface()) && (org.omg.CORBA.Object.class.isAssignableFrom(paramClass));
/*     */   }
/*     */ 
/*     */   public boolean isEntity(Class paramClass)
/*     */   {
/* 209 */     if (paramClass == null) {
/* 210 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 213 */     Class localClass = paramClass.getSuperclass();
/* 214 */     return (!paramClass.isInterface()) && (localClass != null) && (IDLEntity.class.isAssignableFrom(paramClass));
/*     */   }
/*     */ 
/*     */   public boolean isPropertyAccessorMethod(Method paramMethod, Class paramClass)
/*     */   {
/* 225 */     String str1 = paramMethod.getName();
/* 226 */     Class localClass = paramMethod.getReturnType();
/* 227 */     Class[] arrayOfClass1 = paramMethod.getParameterTypes();
/* 228 */     Class[] arrayOfClass2 = paramMethod.getExceptionTypes();
/* 229 */     String str2 = null;
/*     */ 
/* 231 */     if (str1.startsWith("get"))
/*     */     {
/* 233 */       if ((arrayOfClass1.length == 0) && (localClass != Void.TYPE) && (!readHasCorrespondingIsProperty(paramMethod, paramClass)))
/*     */       {
/* 235 */         str2 = "get";
/*     */       }
/*     */     }
/* 238 */     else if (str1.startsWith("set"))
/*     */     {
/* 240 */       if ((localClass == Void.TYPE) && (arrayOfClass1.length == 1) && (
/* 241 */         (hasCorrespondingReadProperty(paramMethod, paramClass, "get")) || (hasCorrespondingReadProperty(paramMethod, paramClass, "is"))))
/*     */       {
/* 243 */         str2 = "set";
/*     */       }
/*     */ 
/*     */     }
/* 247 */     else if ((str1.startsWith("is")) && 
/* 248 */       (arrayOfClass1.length == 0) && (localClass == Boolean.TYPE) && (!isHasCorrespondingReadProperty(paramMethod, paramClass)))
/*     */     {
/* 250 */       str2 = "is";
/*     */     }
/*     */ 
/* 255 */     if ((str2 != null) && (
/* 256 */       (!validPropertyExceptions(paramMethod)) || (str1.length() <= str2.length())))
/*     */     {
/* 258 */       str2 = null;
/*     */     }
/*     */ 
/* 262 */     return str2 != null;
/*     */   }
/*     */ 
/*     */   private boolean hasCorrespondingReadProperty(Method paramMethod, Class paramClass, String paramString)
/*     */   {
/* 268 */     String str1 = paramMethod.getName();
/* 269 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 270 */     boolean bool = false;
/*     */     try
/*     */     {
/* 274 */       String str2 = str1.replaceFirst("set", paramString);
/*     */ 
/* 277 */       Method localMethod = paramClass.getMethod(str2, new Class[0]);
/*     */ 
/* 279 */       bool = (isPropertyAccessorMethod(localMethod, paramClass)) && (localMethod.getReturnType() == arrayOfClass[0]);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 287 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean readHasCorrespondingIsProperty(Method paramMethod, Class paramClass)
/*     */   {
/* 294 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isHasCorrespondingReadProperty(Method paramMethod, Class paramClass)
/*     */   {
/* 321 */     String str1 = paramMethod.getName();
/* 322 */     boolean bool = false;
/*     */     try
/*     */     {
/* 326 */       String str2 = str1.replaceFirst("is", "get");
/*     */ 
/* 329 */       Method localMethod = paramClass.getMethod(str2, new Class[0]);
/*     */ 
/* 331 */       bool = isPropertyAccessorMethod(localMethod, paramClass);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 337 */     return bool;
/*     */   }
/*     */ 
/*     */   public String getAttributeNameForProperty(String paramString) {
/* 341 */     java.lang.Object localObject = null;
/* 342 */     String str1 = null;
/*     */ 
/* 344 */     if (paramString.startsWith("get"))
/* 345 */       str1 = "get";
/* 346 */     else if (paramString.startsWith("set"))
/* 347 */       str1 = "set";
/* 348 */     else if (paramString.startsWith("is")) {
/* 349 */       str1 = "is";
/*     */     }
/*     */ 
/* 352 */     if ((str1 != null) && (str1.length() < paramString.length())) {
/* 353 */       String str2 = paramString.substring(str1.length());
/* 354 */       if ((str2.length() >= 2) && (Character.isUpperCase(str2.charAt(0))) && (Character.isUpperCase(str2.charAt(1))))
/*     */       {
/* 359 */         localObject = str2;
/*     */       }
/* 361 */       else localObject = Character.toLowerCase(str2.charAt(0)) + str2.substring(1);
/*     */ 
/*     */     }
/*     */ 
/* 366 */     return localObject;
/*     */   }
/*     */ 
/*     */   public IDLType getPrimitiveIDLTypeMapping(Class paramClass)
/*     */   {
/* 375 */     if (paramClass == null) {
/* 376 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 379 */     if (paramClass.isPrimitive()) {
/* 380 */       if (paramClass == Void.TYPE)
/* 381 */         return new IDLType(paramClass, "void");
/* 382 */       if (paramClass == Boolean.TYPE)
/* 383 */         return new IDLType(paramClass, "boolean");
/* 384 */       if (paramClass == Character.TYPE)
/* 385 */         return new IDLType(paramClass, "wchar");
/* 386 */       if (paramClass == Byte.TYPE)
/* 387 */         return new IDLType(paramClass, "octet");
/* 388 */       if (paramClass == Short.TYPE)
/* 389 */         return new IDLType(paramClass, "short");
/* 390 */       if (paramClass == Integer.TYPE)
/* 391 */         return new IDLType(paramClass, "long");
/* 392 */       if (paramClass == Long.TYPE)
/* 393 */         return new IDLType(paramClass, "long_long");
/* 394 */       if (paramClass == Float.TYPE)
/* 395 */         return new IDLType(paramClass, "float");
/* 396 */       if (paramClass == Double.TYPE) {
/* 397 */         return new IDLType(paramClass, "double");
/*     */       }
/*     */     }
/*     */ 
/* 401 */     return null;
/*     */   }
/*     */ 
/*     */   public IDLType getSpecialCaseIDLTypeMapping(Class paramClass)
/*     */   {
/* 411 */     if (paramClass == null) {
/* 412 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 415 */     if (paramClass == java.lang.Object.class) {
/* 416 */       return new IDLType(paramClass, new String[] { "java", "lang" }, "Object");
/*     */     }
/* 418 */     if (paramClass == String.class) {
/* 419 */       return new IDLType(paramClass, new String[] { "CORBA" }, "WStringValue");
/*     */     }
/* 421 */     if (paramClass == Class.class) {
/* 422 */       return new IDLType(paramClass, new String[] { "javax", "rmi", "CORBA" }, "ClassDesc");
/*     */     }
/* 424 */     if (paramClass == Serializable.class) {
/* 425 */       return new IDLType(paramClass, new String[] { "java", "io" }, "Serializable");
/*     */     }
/* 427 */     if (paramClass == Externalizable.class) {
/* 428 */       return new IDLType(paramClass, new String[] { "java", "io" }, "Externalizable");
/*     */     }
/* 430 */     if (paramClass == Remote.class) {
/* 431 */       return new IDLType(paramClass, new String[] { "java", "rmi" }, "Remote");
/*     */     }
/* 433 */     if (paramClass == org.omg.CORBA.Object.class) {
/* 434 */       return new IDLType(paramClass, "Object");
/*     */     }
/* 436 */     return null;
/*     */   }
/*     */ 
/*     */   private void validateExceptions(Method paramMethod)
/*     */     throws IDLTypeException
/*     */   {
/* 445 */     Class[] arrayOfClass = paramMethod.getExceptionTypes();
/*     */ 
/* 447 */     int i = 0;
/*     */     Class localClass;
/* 450 */     for (int j = 0; j < arrayOfClass.length; j++) {
/* 451 */       localClass = arrayOfClass[j];
/* 452 */       if (isRemoteExceptionOrSuperClass(localClass)) {
/* 453 */         i = 1;
/* 454 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 458 */     if (i == 0) {
/* 459 */       String str1 = "Method '" + paramMethod + "' must throw at least one " + "exception of type java.rmi.RemoteException or one of its " + "super-classes";
/*     */ 
/* 462 */       throw new IDLTypeException(str1);
/*     */     }
/*     */ 
/* 469 */     for (int k = 0; k < arrayOfClass.length; k++) {
/* 470 */       localClass = arrayOfClass[k];
/*     */ 
/* 472 */       if ((isCheckedException(localClass)) && (!isValue(localClass)) && (!isRemoteException(localClass)))
/*     */       {
/* 475 */         String str2 = "Exception '" + localClass + "' on method '" + paramMethod + "' is not a allowed RMI/IIOP exception type";
/*     */ 
/* 477 */         throw new IDLTypeException(str2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean validPropertyExceptions(Method paramMethod)
/*     */   {
/* 492 */     Class[] arrayOfClass = paramMethod.getExceptionTypes();
/*     */ 
/* 494 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 495 */       Class localClass = arrayOfClass[i];
/*     */ 
/* 497 */       if ((isCheckedException(localClass)) && (!isRemoteException(localClass))) {
/* 498 */         return false;
/*     */       }
/*     */     }
/* 501 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean isRemoteExceptionOrSuperClass(Class paramClass)
/*     */   {
/* 508 */     return (paramClass == RemoteException.class) || (paramClass == IOException.class) || (paramClass == Exception.class) || (paramClass == Throwable.class);
/*     */   }
/*     */ 
/*     */   private void validateDirectInterfaces(Class paramClass)
/*     */     throws IDLTypeException
/*     */   {
/* 520 */     Class[] arrayOfClass = paramClass.getInterfaces();
/*     */ 
/* 522 */     if (arrayOfClass.length < 2) {
/* 523 */       return;
/*     */     }
/*     */ 
/* 526 */     HashSet localHashSet1 = new HashSet();
/* 527 */     HashSet localHashSet2 = new HashSet();
/*     */     Iterator localIterator;
/* 529 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 530 */       Class localClass = arrayOfClass[i];
/* 531 */       Method[] arrayOfMethod = localClass.getMethods();
/*     */ 
/* 536 */       localHashSet2.clear();
/* 537 */       for (int j = 0; j < arrayOfMethod.length; j++) {
/* 538 */         localHashSet2.add(arrayOfMethod[j].getName());
/*     */       }
/*     */ 
/* 543 */       for (localIterator = localHashSet2.iterator(); localIterator.hasNext(); ) {
/* 544 */         String str1 = (String)localIterator.next();
/* 545 */         if (localHashSet1.contains(str1)) {
/* 546 */           String str2 = "Class " + paramClass + " inherits method " + str1 + " from multiple direct interfaces.";
/*     */ 
/* 548 */           throw new IDLTypeException(str2);
/*     */         }
/* 550 */         localHashSet1.add(str1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void validateConstants(final Class paramClass)
/*     */     throws IDLTypeException
/*     */   {
/* 564 */     Field[] arrayOfField = null;
/*     */     java.lang.Object localObject;
/*     */     try
/*     */     {
/* 567 */       arrayOfField = (Field[])AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public java.lang.Object run() throws Exception
/*     */         {
/* 571 */           return paramClass.getFields();
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 575 */       localObject = new IDLTypeException();
/* 576 */       ((IDLTypeException)localObject).initCause(localPrivilegedActionException);
/* 577 */       throw ((Throwable)localObject);
/*     */     }
/*     */ 
/* 580 */     for (int i = 0; i < arrayOfField.length; i++) {
/* 581 */       localObject = arrayOfField[i];
/* 582 */       Class localClass = ((Field)localObject).getType();
/* 583 */       if ((localClass != String.class) && (!isPrimitive(localClass)))
/*     */       {
/* 585 */         String str = "Constant field '" + ((Field)localObject).getName() + "' in class '" + ((Field)localObject).getDeclaringClass().getName() + "' has invalid type' " + ((Field)localObject).getType() + "'. Constants" + " in RMI/IIOP interfaces can only have primitive" + " types and java.lang.String types.";
/*     */ 
/* 590 */         throw new IDLTypeException(str);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.IDLTypesUtil
 * JD-Core Version:    0.6.2
 */