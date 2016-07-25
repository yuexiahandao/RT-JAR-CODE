/*     */ package com.sun.corba.se.impl.orbutil;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.DigestOutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public final class ObjectStreamClassUtil_1_3
/*     */ {
/* 312 */   private static Comparator compareClassByName = new CompareClassByName(null);
/*     */ 
/* 326 */   private static Comparator compareMemberByName = new CompareMemberByName(null);
/*     */ 
/* 496 */   private static Method hasStaticInitializerMethod = null;
/*     */ 
/*     */   public static long computeSerialVersionUID(Class paramClass)
/*     */   {
/*  60 */     long l = com.sun.corba.se.impl.io.ObjectStreamClass.getSerialVersionUID(paramClass);
/*  61 */     if (l == 0L) {
/*  62 */       return l;
/*     */     }
/*  64 */     l = getSerialVersion(l, paramClass).longValue();
/*  65 */     return l;
/*     */   }
/*     */ 
/*     */   private static Long getSerialVersion(final long paramLong, Class paramClass)
/*     */   {
/*  75 */     return (Long)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*     */         long l;
/*     */         try {
/*  79 */           Field localField = this.val$cl.getDeclaredField("serialVersionUID");
/*  80 */           int i = localField.getModifiers();
/*  81 */           if ((Modifier.isStatic(i)) && (Modifier.isFinal(i)) && (Modifier.isPrivate(i)))
/*     */           {
/*  83 */             l = paramLong;
/*     */           }
/*  85 */           else l = ObjectStreamClassUtil_1_3._computeSerialVersionUID(this.val$cl); 
/*     */         }
/*     */         catch (NoSuchFieldException localNoSuchFieldException)
/*     */         {
/*  88 */           l = ObjectStreamClassUtil_1_3._computeSerialVersionUID(this.val$cl);
/*     */         }
/*     */ 
/*  92 */         return new Long(l);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static long computeStructuralUID(boolean paramBoolean, Class<?> paramClass) {
/*  98 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */ 
/* 100 */     long l = 0L;
/*     */     try
/*     */     {
/* 103 */       if ((!Serializable.class.isAssignableFrom(paramClass)) || (paramClass.isInterface()))
/*     */       {
/* 105 */         return 0L;
/*     */       }
/*     */ 
/* 108 */       if (Externalizable.class.isAssignableFrom(paramClass)) {
/* 109 */         return 1L;
/*     */       }
/*     */ 
/* 112 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 113 */       DigestOutputStream localDigestOutputStream = new DigestOutputStream(localByteArrayOutputStream, localMessageDigest);
/* 114 */       DataOutputStream localDataOutputStream = new DataOutputStream(localDigestOutputStream);
/*     */ 
/* 122 */       Class localClass = paramClass.getSuperclass();
/*     */       Object localObject;
/* 123 */       if ((localClass != null) && (localClass != Object.class)) {
/* 124 */         boolean bool = false;
/* 125 */         Class[] arrayOfClass = { ObjectOutputStream.class };
/* 126 */         localObject = getDeclaredMethod(localClass, "writeObject", arrayOfClass, 2, 8);
/*     */ 
/* 128 */         if (localObject != null)
/* 129 */           bool = true;
/* 130 */         localDataOutputStream.writeLong(computeStructuralUID(bool, localClass));
/*     */       }
/*     */ 
/* 133 */       if (paramBoolean)
/* 134 */         localDataOutputStream.writeInt(2);
/*     */       else {
/* 136 */         localDataOutputStream.writeInt(1);
/*     */       }
/*     */ 
/* 139 */       Field[] arrayOfField = getDeclaredFields(paramClass);
/* 140 */       Arrays.sort(arrayOfField, compareMemberByName);
/*     */ 
/* 142 */       for (int i = 0; i < arrayOfField.length; i++) {
/* 143 */         localObject = arrayOfField[i];
/*     */ 
/* 148 */         k = ((Field)localObject).getModifiers();
/* 149 */         if ((!Modifier.isTransient(k)) && (!Modifier.isStatic(k)))
/*     */         {
/* 152 */           localDataOutputStream.writeUTF(((Field)localObject).getName());
/* 153 */           localDataOutputStream.writeUTF(getSignature(((Field)localObject).getType()));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 159 */       localDataOutputStream.flush();
/* 160 */       byte[] arrayOfByte = localMessageDigest.digest();
/* 161 */       int j = Math.min(8, arrayOfByte.length);
/* 162 */       for (int k = j; k > 0; k--)
/* 163 */         l += ((arrayOfByte[k] & 0xFF) << k * 8);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 167 */       l = -1L;
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 169 */       throw new SecurityException(localNoSuchAlgorithmException.getMessage());
/*     */     }
/* 171 */     return l;
/*     */   }
/*     */ 
/*     */   private static long _computeSerialVersionUID(Class paramClass)
/*     */   {
/* 180 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */ 
/* 182 */     long l = 0L;
/*     */     try {
/* 184 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 185 */       DigestOutputStream localDigestOutputStream = new DigestOutputStream(localByteArrayOutputStream, localMessageDigest);
/* 186 */       DataOutputStream localDataOutputStream = new DataOutputStream(localDigestOutputStream);
/*     */ 
/* 189 */       localDataOutputStream.writeUTF(paramClass.getName());
/*     */ 
/* 191 */       int i = paramClass.getModifiers();
/* 192 */       i &= 1553;
/*     */ 
/* 202 */       Method[] arrayOfMethod = paramClass.getDeclaredMethods();
/* 203 */       if ((i & 0x200) != 0) {
/* 204 */         i &= -1025;
/* 205 */         if (arrayOfMethod.length > 0) {
/* 206 */           i |= 1024;
/*     */         }
/*     */       }
/*     */ 
/* 210 */       localDataOutputStream.writeInt(i);
/*     */ 
/* 217 */       if (!paramClass.isArray())
/*     */       {
/* 225 */         localObject1 = paramClass.getInterfaces();
/* 226 */         Arrays.sort((Object[])localObject1, compareClassByName);
/*     */ 
/* 228 */         for (j = 0; j < localObject1.length; j++) {
/* 229 */           localDataOutputStream.writeUTF(localObject1[j].getName());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 234 */       Object localObject1 = paramClass.getDeclaredFields();
/* 235 */       Arrays.sort((Object[])localObject1, compareMemberByName);
/*     */ 
/* 237 */       for (int j = 0; j < localObject1.length; j++) {
/* 238 */         Object localObject2 = localObject1[j];
/*     */ 
/* 243 */         int m = localObject2.getModifiers();
/* 244 */         if ((!Modifier.isPrivate(m)) || ((!Modifier.isTransient(m)) && (!Modifier.isStatic(m))))
/*     */         {
/* 248 */           localDataOutputStream.writeUTF(localObject2.getName());
/* 249 */           localDataOutputStream.writeInt(m);
/* 250 */           localDataOutputStream.writeUTF(getSignature(localObject2.getType()));
/*     */         }
/*     */       }
/*     */ 
/* 254 */       if (hasStaticInitializer(paramClass)) {
/* 255 */         localDataOutputStream.writeUTF("<clinit>");
/* 256 */         localDataOutputStream.writeInt(8);
/* 257 */         localDataOutputStream.writeUTF("()V");
/*     */       }
/*     */ 
/* 266 */       MethodSignature[] arrayOfMethodSignature1 = MethodSignature.removePrivateAndSort(paramClass.getDeclaredConstructors());
/*     */       Object localObject3;
/*     */       String str;
/* 268 */       for (int k = 0; k < arrayOfMethodSignature1.length; k++) {
/* 269 */         MethodSignature localMethodSignature = arrayOfMethodSignature1[k];
/* 270 */         localObject3 = "<init>";
/* 271 */         str = localMethodSignature.signature;
/* 272 */         str = str.replace('/', '.');
/* 273 */         localDataOutputStream.writeUTF((String)localObject3);
/* 274 */         localDataOutputStream.writeInt(localMethodSignature.member.getModifiers());
/* 275 */         localDataOutputStream.writeUTF(str);
/*     */       }
/*     */ 
/* 281 */       MethodSignature[] arrayOfMethodSignature2 = MethodSignature.removePrivateAndSort(arrayOfMethod);
/*     */ 
/* 283 */       for (int n = 0; n < arrayOfMethodSignature2.length; n++) {
/* 284 */         localObject3 = arrayOfMethodSignature2[n];
/* 285 */         str = ((MethodSignature)localObject3).signature;
/* 286 */         str = str.replace('/', '.');
/* 287 */         localDataOutputStream.writeUTF(((MethodSignature)localObject3).member.getName());
/* 288 */         localDataOutputStream.writeInt(((MethodSignature)localObject3).member.getModifiers());
/* 289 */         localDataOutputStream.writeUTF(str);
/*     */       }
/*     */ 
/* 295 */       localDataOutputStream.flush();
/* 296 */       byte[] arrayOfByte = localMessageDigest.digest();
/* 297 */       for (int i1 = 0; i1 < Math.min(8, arrayOfByte.length); i1++)
/* 298 */         l += ((arrayOfByte[i1] & 0xFF) << i1 * 8);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 302 */       l = -1L;
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 304 */       throw new SecurityException(localNoSuchAlgorithmException.getMessage());
/*     */     }
/* 306 */     return l;
/*     */   }
/*     */ 
/*     */   private static String getSignature(Class paramClass)
/*     */   {
/* 349 */     String str = null;
/* 350 */     if (paramClass.isArray()) {
/* 351 */       Class localClass = paramClass;
/* 352 */       int i = 0;
/* 353 */       while (localClass.isArray()) {
/* 354 */         i++;
/* 355 */         localClass = localClass.getComponentType();
/*     */       }
/* 357 */       StringBuffer localStringBuffer = new StringBuffer();
/* 358 */       for (int j = 0; j < i; j++) {
/* 359 */         localStringBuffer.append("[");
/*     */       }
/* 361 */       localStringBuffer.append(getSignature(localClass));
/* 362 */       str = localStringBuffer.toString();
/* 363 */     } else if (paramClass.isPrimitive()) {
/* 364 */       if (paramClass == Integer.TYPE)
/* 365 */         str = "I";
/* 366 */       else if (paramClass == Byte.TYPE)
/* 367 */         str = "B";
/* 368 */       else if (paramClass == Long.TYPE)
/* 369 */         str = "J";
/* 370 */       else if (paramClass == Float.TYPE)
/* 371 */         str = "F";
/* 372 */       else if (paramClass == Double.TYPE)
/* 373 */         str = "D";
/* 374 */       else if (paramClass == Short.TYPE)
/* 375 */         str = "S";
/* 376 */       else if (paramClass == Character.TYPE)
/* 377 */         str = "C";
/* 378 */       else if (paramClass == Boolean.TYPE)
/* 379 */         str = "Z";
/* 380 */       else if (paramClass == Void.TYPE)
/* 381 */         str = "V";
/*     */     }
/*     */     else {
/* 384 */       str = "L" + paramClass.getName().replace('.', '/') + ";";
/*     */     }
/* 386 */     return str;
/*     */   }
/*     */ 
/*     */   private static String getSignature(Method paramMethod)
/*     */   {
/* 393 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 395 */     localStringBuffer.append("(");
/*     */ 
/* 397 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 398 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 399 */       localStringBuffer.append(getSignature(arrayOfClass[i]));
/*     */     }
/* 401 */     localStringBuffer.append(")");
/* 402 */     localStringBuffer.append(getSignature(paramMethod.getReturnType()));
/* 403 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String getSignature(Constructor paramConstructor)
/*     */   {
/* 410 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 412 */     localStringBuffer.append("(");
/*     */ 
/* 414 */     Class[] arrayOfClass = paramConstructor.getParameterTypes();
/* 415 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 416 */       localStringBuffer.append(getSignature(arrayOfClass[i]));
/*     */     }
/* 418 */     localStringBuffer.append(")V");
/* 419 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static Field[] getDeclaredFields(Class paramClass) {
/* 423 */     return (Field[])AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 425 */         return this.val$clz.getDeclaredFields();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static boolean hasStaticInitializer(Class paramClass)
/*     */   {
/*     */     Object localObject;
/* 502 */     if (hasStaticInitializerMethod == null) {
/* 503 */       localObject = null;
/*     */       try
/*     */       {
/* 506 */         if (localObject == null) {
/* 507 */           localObject = java.io.ObjectStreamClass.class;
/*     */         }
/* 509 */         hasStaticInitializerMethod = ((Class)localObject).getDeclaredMethod("hasStaticInitializer", new Class[] { Class.class });
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException)
/*     */       {
/*     */       }
/*     */ 
/* 515 */       if (hasStaticInitializerMethod == null) {
/* 516 */         throw new InternalError("Can't find hasStaticInitializer method on " + ((Class)localObject).getName());
/*     */       }
/*     */ 
/* 519 */       hasStaticInitializerMethod.setAccessible(true);
/*     */     }
/*     */     try {
/* 522 */       localObject = (Boolean)hasStaticInitializerMethod.invoke(null, new Object[] { paramClass });
/*     */ 
/* 524 */       return ((Boolean)localObject).booleanValue();
/*     */     } catch (Exception localException) {
/* 526 */       throw new InternalError("Error invoking hasStaticInitializer: " + localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Method getDeclaredMethod(Class paramClass, final String paramString, final Class[] paramArrayOfClass, final int paramInt1, final int paramInt2)
/*     */   {
/* 534 */     return (Method)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 536 */         Method localMethod = null;
/*     */         try {
/* 538 */           localMethod = this.val$cl.getDeclaredMethod(paramString, paramArrayOfClass);
/*     */ 
/* 540 */           int i = localMethod.getModifiers();
/* 541 */           if (((i & paramInt2) != 0) || ((i & paramInt1) != paramInt1))
/*     */           {
/* 543 */             localMethod = null;
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException)
/*     */         {
/*     */         }
/*     */ 
/* 553 */         return localMethod;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static class CompareClassByName
/*     */     implements Comparator
/*     */   {
/*     */     public int compare(Object paramObject1, Object paramObject2)
/*     */     {
/* 317 */       Class localClass1 = (Class)paramObject1;
/* 318 */       Class localClass2 = (Class)paramObject2;
/* 319 */       return localClass1.getName().compareTo(localClass2.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class CompareMemberByName
/*     */     implements Comparator
/*     */   {
/*     */     public int compare(Object paramObject1, Object paramObject2)
/*     */     {
/* 331 */       String str1 = ((Member)paramObject1).getName();
/* 332 */       String str2 = ((Member)paramObject2).getName();
/*     */ 
/* 334 */       if ((paramObject1 instanceof Method)) {
/* 335 */         str1 = str1 + ObjectStreamClassUtil_1_3.getSignature((Method)paramObject1);
/* 336 */         str2 = str2 + ObjectStreamClassUtil_1_3.getSignature((Method)paramObject2);
/* 337 */       } else if ((paramObject1 instanceof Constructor)) {
/* 338 */         str1 = str1 + ObjectStreamClassUtil_1_3.getSignature((Constructor)paramObject1);
/* 339 */         str2 = str2 + ObjectStreamClassUtil_1_3.getSignature((Constructor)paramObject2);
/*     */       }
/* 341 */       return str1.compareTo(str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MethodSignature
/*     */     implements Comparator
/*     */   {
/*     */     Member member;
/*     */     String signature;
/*     */ 
/*     */     static MethodSignature[] removePrivateAndSort(Member[] paramArrayOfMember)
/*     */     {
/* 439 */       int i = 0;
/* 440 */       for (int j = 0; j < paramArrayOfMember.length; j++) {
/* 441 */         if (!Modifier.isPrivate(paramArrayOfMember[j].getModifiers())) {
/* 442 */           i++;
/*     */         }
/*     */       }
/* 445 */       MethodSignature[] arrayOfMethodSignature = new MethodSignature[i];
/* 446 */       int k = 0;
/* 447 */       for (int m = 0; m < paramArrayOfMember.length; m++) {
/* 448 */         if (!Modifier.isPrivate(paramArrayOfMember[m].getModifiers())) {
/* 449 */           arrayOfMethodSignature[k] = new MethodSignature(paramArrayOfMember[m]);
/* 450 */           k++;
/*     */         }
/*     */       }
/* 453 */       if (k > 0)
/* 454 */         Arrays.sort(arrayOfMethodSignature, arrayOfMethodSignature[0]);
/* 455 */       return arrayOfMethodSignature;
/*     */     }
/*     */ 
/*     */     public int compare(Object paramObject1, Object paramObject2)
/*     */     {
/* 462 */       if (paramObject1 == paramObject2) {
/* 463 */         return 0;
/*     */       }
/* 465 */       MethodSignature localMethodSignature1 = (MethodSignature)paramObject1;
/* 466 */       MethodSignature localMethodSignature2 = (MethodSignature)paramObject2;
/*     */       int i;
/* 469 */       if (isConstructor()) {
/* 470 */         i = localMethodSignature1.signature.compareTo(localMethodSignature2.signature);
/*     */       } else {
/* 472 */         i = localMethodSignature1.member.getName().compareTo(localMethodSignature2.member.getName());
/* 473 */         if (i == 0)
/* 474 */           i = localMethodSignature1.signature.compareTo(localMethodSignature2.signature);
/*     */       }
/* 476 */       return i;
/*     */     }
/*     */ 
/*     */     private final boolean isConstructor() {
/* 480 */       return this.member instanceof Constructor;
/*     */     }
/*     */     private MethodSignature(Member paramMember) {
/* 483 */       this.member = paramMember;
/* 484 */       if (isConstructor())
/* 485 */         this.signature = ObjectStreamClassUtil_1_3.getSignature((Constructor)paramMember);
/*     */       else
/* 487 */         this.signature = ObjectStreamClassUtil_1_3.getSignature((Method)paramMember);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3
 * JD-Core Version:    0.6.2
 */