/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permissions;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanPermission;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.OperationsException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeErrorException;
/*     */ import javax.management.RuntimeMBeanException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import sun.reflect.misc.ConstructorUtil;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class MBeanInstantiator
/*     */ {
/*     */   private final ModifiableClassLoaderRepository clr;
/* 728 */   private static final Map<String, Class<?>> primitiveClasses = Util.newMap();
/*     */ 
/*     */   MBeanInstantiator(ModifiableClassLoaderRepository paramModifiableClassLoaderRepository)
/*     */   {
/*  71 */     this.clr = paramModifiableClassLoaderRepository;
/*     */   }
/*     */ 
/*     */   public void testCreation(Class<?> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/*  81 */     Introspector.testCreation(paramClass);
/*     */   }
/*     */ 
/*     */   public Class<?> findClassWithDefaultLoaderRepository(String paramString)
/*     */     throws ReflectionException
/*     */   {
/*  92 */     if (paramString == null) {
/*  93 */       throw new RuntimeOperationsException(new IllegalArgumentException("The class name cannot be null"), "Exception occurred during object instantiation");
/*     */     }
/*     */ 
/*  98 */     ReflectUtil.checkPackageAccess(paramString);
/*     */     Class localClass;
/*     */     try {
/* 100 */       if (this.clr == null) throw new ClassNotFoundException(paramString);
/* 101 */       localClass = this.clr.loadClass(paramString);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 104 */       throw new ReflectionException(localClassNotFoundException, "The MBean class could not be loaded by the default loader repository");
/*     */     }
/*     */ 
/* 108 */     return localClass;
/*     */   }
/*     */ 
/*     */   public Class<?> findClass(String paramString, ClassLoader paramClassLoader)
/*     */     throws ReflectionException
/*     */   {
/* 119 */     return loadClass(paramString, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public Class<?> findClass(String paramString, ObjectName paramObjectName)
/*     */     throws ReflectionException, InstanceNotFoundException
/*     */   {
/* 129 */     if (paramObjectName == null) {
/* 130 */       throw new RuntimeOperationsException(new IllegalArgumentException(), "Null loader passed in parameter");
/*     */     }
/*     */ 
/* 134 */     ClassLoader localClassLoader = null;
/* 135 */     synchronized (this) {
/* 136 */       localClassLoader = getClassLoader(paramObjectName);
/*     */     }
/* 138 */     if (localClassLoader == null) {
/* 139 */       throw new InstanceNotFoundException("The loader named " + paramObjectName + " is not registered in the MBeanServer");
/*     */     }
/*     */ 
/* 142 */     return findClass(paramString, localClassLoader);
/*     */   }
/*     */ 
/*     */   public Class<?>[] findSignatureClasses(String[] paramArrayOfString, ClassLoader paramClassLoader)
/*     */     throws ReflectionException
/*     */   {
/* 154 */     if (paramArrayOfString == null) return null;
/* 155 */     ClassLoader localClassLoader = paramClassLoader;
/* 156 */     int i = paramArrayOfString.length;
/* 157 */     Class[] arrayOfClass = new Class[i];
/*     */ 
/* 159 */     if (i == 0) return arrayOfClass; try
/*     */     {
/* 161 */       for (int j = 0; j < i; j++)
/*     */       {
/* 166 */         Class localClass = (Class)primitiveClasses.get(paramArrayOfString[j]);
/* 167 */         if (localClass != null) {
/* 168 */           arrayOfClass[j] = localClass;
/*     */         }
/*     */         else
/*     */         {
/* 172 */           ReflectUtil.checkPackageAccess(paramArrayOfString[j]);
/*     */ 
/* 176 */           if (localClassLoader != null)
/*     */           {
/* 180 */             arrayOfClass[j] = Class.forName(paramArrayOfString[j], false, localClassLoader);
/*     */           }
/*     */           else
/*     */           {
/* 184 */             arrayOfClass[j] = findClass(paramArrayOfString[j], getClass().getClassLoader());
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 189 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 190 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "The parameter class could not be found", localClassNotFoundException);
/*     */       }
/*     */ 
/* 195 */       throw new ReflectionException(localClassNotFoundException, "The parameter class could not be found");
/*     */     }
/*     */     catch (RuntimeException localRuntimeException) {
/* 198 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 199 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "Unexpected exception", localRuntimeException);
/*     */       }
/*     */ 
/* 204 */       throw localRuntimeException;
/*     */     }
/* 206 */     return arrayOfClass;
/*     */   }
/*     */ 
/*     */   public Object instantiate(Class<?> paramClass)
/*     */     throws ReflectionException, MBeanException
/*     */   {
/* 217 */     checkMBeanPermission(paramClass, null, null, "instantiate");
/*     */ 
/* 223 */     Constructor localConstructor = findConstructor(paramClass, null);
/* 224 */     if (localConstructor == null) {
/* 225 */       throw new ReflectionException(new NoSuchMethodException("No such constructor"));
/*     */     }
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 230 */       ReflectUtil.checkPackageAccess(paramClass);
/* 231 */       ensureClassAccess(paramClass);
/* 232 */       localObject = localConstructor.newInstance(new Object[0]);
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 235 */       Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 236 */       if ((localThrowable instanceof RuntimeException)) {
/* 237 */         throw new RuntimeMBeanException((RuntimeException)localThrowable, "RuntimeException thrown in the MBean's empty constructor");
/*     */       }
/* 239 */       if ((localThrowable instanceof Error)) {
/* 240 */         throw new RuntimeErrorException((Error)localThrowable, "Error thrown in the MBean's empty constructor");
/*     */       }
/*     */ 
/* 243 */       throw new MBeanException((Exception)localThrowable, "Exception thrown in the MBean's empty constructor");
/*     */     }
/*     */     catch (NoSuchMethodError localNoSuchMethodError)
/*     */     {
/* 247 */       throw new ReflectionException(new NoSuchMethodException("No constructor"), "No such constructor");
/*     */     }
/*     */     catch (InstantiationException localInstantiationException)
/*     */     {
/* 251 */       throw new ReflectionException(localInstantiationException, "Exception thrown trying to invoke the MBean's empty constructor");
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 254 */       throw new ReflectionException(localIllegalAccessException, "Exception thrown trying to invoke the MBean's empty constructor");
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 257 */       throw new ReflectionException(localIllegalArgumentException, "Exception thrown trying to invoke the MBean's empty constructor");
/*     */     }
/*     */ 
/* 260 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Object instantiate(Class<?> paramClass, Object[] paramArrayOfObject, String[] paramArrayOfString, ClassLoader paramClassLoader)
/*     */     throws ReflectionException, MBeanException
/*     */   {
/* 275 */     checkMBeanPermission(paramClass, null, null, "instantiate");
/*     */     Class[] arrayOfClass;
/*     */     try
/*     */     {
/* 285 */       ClassLoader localClassLoader = paramClass.getClassLoader();
/*     */ 
/* 288 */       arrayOfClass = paramArrayOfString == null ? null : findSignatureClasses(paramArrayOfString, localClassLoader);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 294 */       throw new ReflectionException(localIllegalArgumentException, "The constructor parameter classes could not be loaded");
/*     */     }
/*     */ 
/* 299 */     Constructor localConstructor = findConstructor(paramClass, arrayOfClass);
/*     */ 
/* 301 */     if (localConstructor == null)
/* 302 */       throw new ReflectionException(new NoSuchMethodException("No such constructor"));
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 306 */       ReflectUtil.checkPackageAccess(paramClass);
/* 307 */       ensureClassAccess(paramClass);
/* 308 */       localObject = localConstructor.newInstance(paramArrayOfObject);
/*     */     }
/*     */     catch (NoSuchMethodError localNoSuchMethodError) {
/* 311 */       throw new ReflectionException(new NoSuchMethodException("No such constructor found"), "No such constructor");
/*     */     }
/*     */     catch (InstantiationException localInstantiationException)
/*     */     {
/* 316 */       throw new ReflectionException(localInstantiationException, "Exception thrown trying to invoke the MBean's constructor");
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 320 */       throw new ReflectionException(localIllegalAccessException, "Exception thrown trying to invoke the MBean's constructor");
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/* 325 */       Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 326 */       if ((localThrowable instanceof RuntimeException)) {
/* 327 */         throw new RuntimeMBeanException((RuntimeException)localThrowable, "RuntimeException thrown in the MBean's constructor");
/*     */       }
/* 329 */       if ((localThrowable instanceof Error)) {
/* 330 */         throw new RuntimeErrorException((Error)localThrowable, "Error thrown in the MBean's constructor");
/*     */       }
/*     */ 
/* 333 */       throw new MBeanException((Exception)localThrowable, "Exception thrown in the MBean's constructor");
/*     */     }
/*     */ 
/* 337 */     return localObject;
/*     */   }
/*     */ 
/*     */   public ObjectInputStream deserialize(ClassLoader paramClassLoader, byte[] paramArrayOfByte)
/*     */     throws OperationsException
/*     */   {
/* 355 */     if (paramArrayOfByte == null) {
/* 356 */       throw new RuntimeOperationsException(new IllegalArgumentException(), "Null data passed in parameter");
/*     */     }
/*     */ 
/* 359 */     if (paramArrayOfByte.length == 0) {
/* 360 */       throw new RuntimeOperationsException(new IllegalArgumentException(), "Empty data passed in parameter");
/*     */     }
/*     */ 
/* 368 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*     */     ObjectInputStreamWithLoader localObjectInputStreamWithLoader;
/*     */     try
/*     */     {
/* 370 */       localObjectInputStreamWithLoader = new ObjectInputStreamWithLoader(localByteArrayInputStream, paramClassLoader);
/*     */     } catch (IOException localIOException) {
/* 372 */       throw new OperationsException("An IOException occurred trying to de-serialize the data");
/*     */     }
/*     */ 
/* 376 */     return localObjectInputStreamWithLoader;
/*     */   }
/*     */ 
/*     */   public ObjectInputStream deserialize(String paramString, ObjectName paramObjectName, byte[] paramArrayOfByte, ClassLoader paramClassLoader)
/*     */     throws InstanceNotFoundException, OperationsException, ReflectionException
/*     */   {
/* 412 */     if (paramArrayOfByte == null) {
/* 413 */       throw new RuntimeOperationsException(new IllegalArgumentException(), "Null data passed in parameter");
/*     */     }
/*     */ 
/* 416 */     if (paramArrayOfByte.length == 0) {
/* 417 */       throw new RuntimeOperationsException(new IllegalArgumentException(), "Empty data passed in parameter");
/*     */     }
/*     */ 
/* 420 */     if (paramString == null) {
/* 421 */       throw new RuntimeOperationsException(new IllegalArgumentException(), "Null className passed in parameter");
/*     */     }
/*     */ 
/* 425 */     ReflectUtil.checkPackageAccess(paramString);
/*     */     Class localClass;
/* 427 */     if (paramObjectName == null)
/*     */     {
/* 429 */       localClass = findClass(paramString, paramClassLoader);
/*     */     }
/*     */     else {
/*     */       try
/*     */       {
/* 434 */         ClassLoader localClassLoader = null;
/*     */ 
/* 436 */         localClassLoader = getClassLoader(paramObjectName);
/* 437 */         if (localClassLoader == null)
/* 438 */           throw new ClassNotFoundException(paramString);
/* 439 */         localClass = Class.forName(paramString, false, localClassLoader);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/* 442 */         throw new ReflectionException(localClassNotFoundException, "The MBean class could not be loaded by the " + paramObjectName.toString() + " class loader");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 452 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*     */     ObjectInputStreamWithLoader localObjectInputStreamWithLoader;
/*     */     try
/*     */     {
/* 454 */       localObjectInputStreamWithLoader = new ObjectInputStreamWithLoader(localByteArrayInputStream, localClass.getClassLoader());
/*     */     }
/*     */     catch (IOException localIOException) {
/* 457 */       throw new OperationsException("An IOException occurred trying to de-serialize the data");
/*     */     }
/*     */ 
/* 461 */     return localObjectInputStreamWithLoader;
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString)
/*     */     throws ReflectionException, MBeanException
/*     */   {
/* 491 */     return instantiate(paramString, (Object[])null, (String[])null, null);
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString, ObjectName paramObjectName, ClassLoader paramClassLoader)
/*     */     throws ReflectionException, MBeanException, InstanceNotFoundException
/*     */   {
/* 527 */     return instantiate(paramString, paramObjectName, (Object[])null, (String[])null, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString, ClassLoader paramClassLoader)
/*     */     throws ReflectionException, MBeanException
/*     */   {
/* 565 */     Class localClass = findClassWithDefaultLoaderRepository(paramString);
/* 566 */     return instantiate(localClass, paramArrayOfObject, paramArrayOfString, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString, ClassLoader paramClassLoader)
/*     */     throws ReflectionException, MBeanException, InstanceNotFoundException
/*     */   {
/*     */     Class localClass;
/* 615 */     if (paramObjectName == null)
/* 616 */       localClass = findClass(paramString, paramClassLoader);
/*     */     else {
/* 618 */       localClass = findClass(paramString, paramObjectName);
/*     */     }
/* 620 */     return instantiate(localClass, paramArrayOfObject, paramArrayOfString, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public ModifiableClassLoaderRepository getClassLoaderRepository()
/*     */   {
/* 628 */     checkMBeanPermission((String)null, null, null, "getClassLoaderRepository");
/* 629 */     return this.clr;
/*     */   }
/*     */ 
/*     */   static Class<?> loadClass(String paramString, ClassLoader paramClassLoader)
/*     */     throws ReflectionException
/*     */   {
/* 639 */     if (paramString == null) {
/* 640 */       throw new RuntimeOperationsException(new IllegalArgumentException("The class name cannot be null"), "Exception occurred during object instantiation");
/* 644 */     }
/*     */ ReflectUtil.checkPackageAccess(paramString);
/*     */     Class localClass;
/*     */     try {
/* 646 */       if (paramClassLoader == null)
/* 647 */         paramClassLoader = MBeanInstantiator.class.getClassLoader();
/* 648 */       if (paramClassLoader != null)
/* 649 */         localClass = Class.forName(paramString, false, paramClassLoader);
/*     */       else
/* 651 */         localClass = Class.forName(paramString);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 654 */       throw new ReflectionException(localClassNotFoundException, "The MBean class could not be loaded");
/*     */     }
/*     */ 
/* 657 */     return localClass;
/*     */   }
/*     */ 
/*     */   static Class<?>[] loadSignatureClasses(String[] paramArrayOfString, ClassLoader paramClassLoader)
/*     */     throws ReflectionException
/*     */   {
/* 670 */     if (paramArrayOfString == null) return null;
/* 671 */     ClassLoader localClassLoader = paramClassLoader == null ? MBeanInstantiator.class.getClassLoader() : paramClassLoader;
/*     */ 
/* 673 */     int i = paramArrayOfString.length;
/* 674 */     Class[] arrayOfClass = new Class[i];
/*     */ 
/* 676 */     if (i == 0) return arrayOfClass; try
/*     */     {
/* 678 */       for (int j = 0; j < i; j++)
/*     */       {
/* 683 */         Class localClass = (Class)primitiveClasses.get(paramArrayOfString[j]);
/* 684 */         if (localClass != null) {
/* 685 */           arrayOfClass[j] = localClass;
/*     */         }
/*     */         else
/*     */         {
/* 695 */           ReflectUtil.checkPackageAccess(paramArrayOfString[j]);
/* 696 */           arrayOfClass[j] = Class.forName(paramArrayOfString[j], false, localClassLoader);
/*     */         }
/*     */       }
/*     */     } catch (ClassNotFoundException localClassNotFoundException) { if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 700 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "The parameter class could not be found", localClassNotFoundException);
/*     */       }
/*     */ 
/* 705 */       throw new ReflectionException(localClassNotFoundException, "The parameter class could not be found");
/*     */     } catch (RuntimeException localRuntimeException)
/*     */     {
/* 708 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 709 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanInstantiator.class.getName(), "findSignatureClasses", "Unexpected exception", localRuntimeException);
/*     */       }
/*     */ 
/* 714 */       throw localRuntimeException;
/*     */     }
/* 716 */     return arrayOfClass;
/*     */   }
/*     */ 
/*     */   private Constructor<?> findConstructor(Class<?> paramClass, Class<?>[] paramArrayOfClass) {
/*     */     try {
/* 721 */       return ConstructorUtil.getConstructor(paramClass, paramArrayOfClass); } catch (Exception localException) {
/*     */     }
/* 723 */     return null;
/*     */   }
/*     */ 
/*     */   private static void checkMBeanPermission(Class<?> paramClass, String paramString1, ObjectName paramObjectName, String paramString2)
/*     */   {
/* 740 */     if (paramClass != null)
/* 741 */       checkMBeanPermission(paramClass.getName(), paramString1, paramObjectName, paramString2);
/*     */   }
/*     */ 
/*     */   private static void checkMBeanPermission(String paramString1, String paramString2, ObjectName paramObjectName, String paramString3)
/*     */     throws SecurityException
/*     */   {
/* 750 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 751 */     if (localSecurityManager != null) {
/* 752 */       MBeanPermission localMBeanPermission = new MBeanPermission(paramString1, paramString2, paramObjectName, paramString3);
/*     */ 
/* 756 */       localSecurityManager.checkPermission(localMBeanPermission);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void ensureClassAccess(Class paramClass)
/*     */     throws IllegalAccessException
/*     */   {
/* 763 */     int i = paramClass.getModifiers();
/* 764 */     if (!Modifier.isPublic(i))
/* 765 */       throw new IllegalAccessException("Class is not public and can't be instantiated");
/*     */   }
/*     */ 
/*     */   private ClassLoader getClassLoader(final ObjectName paramObjectName)
/*     */   {
/* 770 */     if (this.clr == null) {
/* 771 */       return null;
/*     */     }
/*     */ 
/* 774 */     Permissions localPermissions = new Permissions();
/* 775 */     localPermissions.add(new MBeanPermission("*", null, paramObjectName, "getClassLoader"));
/* 776 */     ProtectionDomain localProtectionDomain = new ProtectionDomain(null, localPermissions);
/* 777 */     ProtectionDomain[] arrayOfProtectionDomain = { localProtectionDomain };
/* 778 */     AccessControlContext localAccessControlContext = new AccessControlContext(arrayOfProtectionDomain);
/* 779 */     ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public ClassLoader run() {
/* 781 */         return MBeanInstantiator.this.clr.getClassLoader(paramObjectName);
/*     */       }
/*     */     }
/*     */     , localAccessControlContext);
/*     */ 
/* 784 */     return localClassLoader;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 730 */     for (Class localClass : new Class[] { Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Character.TYPE, Boolean.TYPE })
/*     */     {
/* 733 */       primitiveClasses.put(localClass.getName(), localClass);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MBeanInstantiator
 * JD-Core Version:    0.6.2
 */