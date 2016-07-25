/*      */ package java.lang;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.GenericDeclaration;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permissions;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import sun.misc.Unsafe;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.ConstantPool;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.reflect.ReflectionFactory;
/*      */ import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
/*      */ import sun.reflect.annotation.AnnotationParser;
/*      */ import sun.reflect.annotation.AnnotationType;
/*      */ import sun.reflect.generics.factory.CoreReflectionFactory;
/*      */ import sun.reflect.generics.factory.GenericsFactory;
/*      */ import sun.reflect.generics.repository.ClassRepository;
/*      */ import sun.reflect.generics.repository.ConstructorRepository;
/*      */ import sun.reflect.generics.repository.MethodRepository;
/*      */ import sun.reflect.generics.scope.ClassScope;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public final class Class<T>
/*      */   implements Serializable, GenericDeclaration, Type, AnnotatedElement
/*      */ {
/*      */   private static final int ANNOTATION = 8192;
/*      */   private static final int ENUM = 16384;
/*      */   private static final int SYNTHETIC = 4096;
/*      */   private volatile transient Constructor<T> cachedConstructor;
/*      */   private volatile transient Class<?> newInstanceCallerCache;
/*      */   private transient String name;
/*      */   private static ProtectionDomain allPermDomain;
/*      */   private static volatile SecurityManagerHelper smHelper;
/* 2389 */   private static boolean useCaches = true;
/*      */   private volatile transient SoftReference<ReflectionData<T>> reflectionData;
/* 2414 */   private volatile transient int classRedefinedCount = 0;
/*      */   private transient ClassRepository genericInfo;
/*      */   private static final long serialVersionUID = 3206093459760846163L;
/* 2989 */   private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
/*      */   private static ReflectionFactory reflectionFactory;
/* 3064 */   private static boolean initted = false;
/*      */ 
/* 3138 */   private volatile transient T[] enumConstants = null;
/*      */ 
/* 3160 */   private volatile transient Map<String, T> enumConstantDirectory = null;
/*      */   private transient Map<Class<? extends Annotation>, Annotation> annotations;
/*      */   private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;
/* 3256 */   private transient int lastAnnotationsRedefinedCount = 0;
/*      */   private volatile transient AnnotationType annotationType;
/*      */   transient ClassValue.ClassValueMap classValueMap;
/*      */ 
/*      */   private static native void registerNatives();
/*      */ 
/*      */   public String toString()
/*      */   {
/*  151 */     return (isPrimitive() ? "" : isInterface() ? "interface " : "class ") + getName();
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static Class<?> forName(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*  190 */     Class localClass = Reflection.getCallerClass();
/*  191 */     return forName0(paramString, true, ClassLoader.getClassLoader(localClass), localClass);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static Class<?> forName(String paramString, boolean paramBoolean, ClassLoader paramClassLoader)
/*      */     throws ClassNotFoundException
/*      */   {
/*  260 */     Class localClass = null;
/*  261 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  262 */     if (localSecurityManager != null)
/*      */     {
/*  265 */       localClass = Reflection.getCallerClass();
/*  266 */       if (paramClassLoader == null) {
/*  267 */         ClassLoader localClassLoader = ClassLoader.getClassLoader(localClass);
/*  268 */         if (localClassLoader != null) {
/*  269 */           localSecurityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  274 */     return forName0(paramString, paramBoolean, paramClassLoader, localClass);
/*      */   }
/*      */ 
/*      */   private static native Class<?> forName0(String paramString, boolean paramBoolean, ClassLoader paramClassLoader, Class<?> paramClass)
/*      */     throws ClassNotFoundException;
/*      */ 
/*      */   @CallerSensitive
/*      */   public T newInstance()
/*      */     throws InstantiationException, IllegalAccessException
/*      */   {
/*  334 */     if (System.getSecurityManager() != null) {
/*  335 */       checkMemberAccess(0, Reflection.getCallerClass(), false);
/*      */     }
/*      */ 
/*  342 */     if (this.cachedConstructor == null) {
/*  343 */       if (this == Class.class) {
/*  344 */         throw new IllegalAccessException("Can not call newInstance() on the Class for java.lang.Class");
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  349 */         Class[] arrayOfClass = new Class[0];
/*  350 */         final Constructor localConstructor2 = getConstructor0(arrayOfClass, 1);
/*      */ 
/*  355 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Void run() {
/*  358 */             localConstructor2.setAccessible(true);
/*  359 */             return null;
/*      */           }
/*      */         });
/*  362 */         this.cachedConstructor = localConstructor2;
/*      */       } catch (NoSuchMethodException localNoSuchMethodException) {
/*  364 */         throw new InstantiationException(getName());
/*      */       }
/*      */     }
/*  367 */     Constructor localConstructor1 = this.cachedConstructor;
/*      */ 
/*  369 */     int i = localConstructor1.getModifiers();
/*  370 */     if (!Reflection.quickCheckMemberAccess(this, i)) {
/*  371 */       Class localClass = Reflection.getCallerClass();
/*  372 */       if (this.newInstanceCallerCache != localClass) {
/*  373 */         Reflection.ensureMemberAccess(localClass, this, null, i);
/*  374 */         this.newInstanceCallerCache = localClass;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  379 */       return localConstructor1.newInstance((Object[])null);
/*      */     } catch (InvocationTargetException localInvocationTargetException) {
/*  381 */       Unsafe.getUnsafe().throwException(localInvocationTargetException.getTargetException());
/*      */     }
/*  383 */     return null;
/*      */   }
/*      */ 
/*      */   public native boolean isInstance(Object paramObject);
/*      */ 
/*      */   public native boolean isAssignableFrom(Class<?> paramClass);
/*      */ 
/*      */   public native boolean isInterface();
/*      */ 
/*      */   public native boolean isArray();
/*      */ 
/*      */   public native boolean isPrimitive();
/*      */ 
/*      */   public boolean isAnnotation()
/*      */   {
/*  510 */     return (getModifiers() & 0x2000) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isSynthetic()
/*      */   {
/*  521 */     return (getModifiers() & 0x1000) != 0;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  576 */     String str = this.name;
/*  577 */     if (str == null)
/*  578 */       this.name = (str = getName0());
/*  579 */     return str;
/*      */   }
/*      */ 
/*      */   private native String getName0();
/*      */ 
/*      */   @CallerSensitive
/*      */   public ClassLoader getClassLoader()
/*      */   {
/*  614 */     ClassLoader localClassLoader = getClassLoader0();
/*  615 */     if (localClassLoader == null)
/*  616 */       return null;
/*  617 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  618 */     if (localSecurityManager != null) {
/*  619 */       ClassLoader.checkClassLoaderPermission(localClassLoader, Reflection.getCallerClass());
/*      */     }
/*  621 */     return localClassLoader;
/*      */   }
/*      */ 
/*      */   native ClassLoader getClassLoader0();
/*      */ 
/*      */   public TypeVariable<Class<T>>[] getTypeParameters()
/*      */   {
/*  644 */     if (getGenericSignature() != null) {
/*  645 */       return (TypeVariable[])getGenericInfo().getTypeParameters();
/*      */     }
/*  647 */     return (TypeVariable[])new TypeVariable[0];
/*      */   }
/*      */ 
/*      */   public native Class<? super T> getSuperclass();
/*      */ 
/*      */   public Type getGenericSuperclass()
/*      */   {
/*  695 */     if (getGenericSignature() != null)
/*      */     {
/*  699 */       if (isInterface())
/*  700 */         return null;
/*  701 */       return getGenericInfo().getSuperclass();
/*      */     }
/*  703 */     return getSuperclass();
/*      */   }
/*      */ 
/*      */   public Package getPackage()
/*      */   {
/*  722 */     return Package.getPackage(this);
/*      */   }
/*      */ 
/*      */   public native Class<?>[] getInterfaces();
/*      */ 
/*      */   public Type[] getGenericInterfaces()
/*      */   {
/*  818 */     if (getGenericSignature() != null) {
/*  819 */       return getGenericInfo().getSuperInterfaces();
/*      */     }
/*  821 */     return getInterfaces();
/*      */   }
/*      */ 
/*      */   public native Class<?> getComponentType();
/*      */ 
/*      */   public native int getModifiers();
/*      */ 
/*      */   public native Object[] getSigners();
/*      */ 
/*      */   native void setSigners(Object[] paramArrayOfObject);
/*      */ 
/*      */   @CallerSensitive
/*      */   public Method getEnclosingMethod()
/*      */   {
/*  902 */     EnclosingMethodInfo localEnclosingMethodInfo = getEnclosingMethodInfo();
/*      */ 
/*  904 */     if (localEnclosingMethodInfo == null) {
/*  905 */       return null;
/*      */     }
/*  907 */     if (!localEnclosingMethodInfo.isMethod()) {
/*  908 */       return null;
/*      */     }
/*  910 */     MethodRepository localMethodRepository = MethodRepository.make(localEnclosingMethodInfo.getDescriptor(), getFactory());
/*      */ 
/*  912 */     Class localClass1 = toClass(localMethodRepository.getReturnType());
/*  913 */     Type[] arrayOfType = localMethodRepository.getParameterTypes();
/*  914 */     Class[] arrayOfClass1 = new Class[arrayOfType.length];
/*      */ 
/*  919 */     for (int i = 0; i < arrayOfClass1.length; i++) {
/*  920 */       arrayOfClass1[i] = toClass(arrayOfType[i]);
/*      */     }
/*      */ 
/*  923 */     Class localClass2 = localEnclosingMethodInfo.getEnclosingClass();
/*      */ 
/*  929 */     localClass2.checkMemberAccess(1, Reflection.getCallerClass(), true);
/*      */ 
/*  937 */     for (Method localMethod : localClass2.getDeclaredMethods()) {
/*  938 */       if (localMethod.getName().equals(localEnclosingMethodInfo.getName())) {
/*  939 */         Class[] arrayOfClass2 = localMethod.getParameterTypes();
/*  940 */         if (arrayOfClass2.length == arrayOfClass1.length) {
/*  941 */           int m = 1;
/*  942 */           for (int n = 0; n < arrayOfClass2.length; n++) {
/*  943 */             if (!arrayOfClass2[n].equals(arrayOfClass1[n])) {
/*  944 */               m = 0;
/*  945 */               break;
/*      */             }
/*      */           }
/*      */ 
/*  949 */           if ((m != 0) && 
/*  950 */             (localMethod.getReturnType().equals(localClass1))) {
/*  951 */             return localMethod;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  957 */     throw new InternalError("Enclosing method not found");
/*      */   }
/*      */ 
/*      */   private native Object[] getEnclosingMethod0();
/*      */ 
/*      */   private EnclosingMethodInfo getEnclosingMethodInfo()
/*      */   {
/*  964 */     Object[] arrayOfObject = getEnclosingMethod0();
/*  965 */     if (arrayOfObject == null) {
/*  966 */       return null;
/*      */     }
/*  968 */     return new EnclosingMethodInfo(arrayOfObject, null);
/*      */   }
/*      */ 
/*      */   private static Class<?> toClass(Type paramType)
/*      */   {
/* 1017 */     if ((paramType instanceof GenericArrayType)) {
/* 1018 */       return Array.newInstance(toClass(((GenericArrayType)paramType).getGenericComponentType()), 0).getClass();
/*      */     }
/*      */ 
/* 1021 */     return (Class)paramType;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Constructor<?> getEnclosingConstructor()
/*      */   {
/* 1040 */     EnclosingMethodInfo localEnclosingMethodInfo = getEnclosingMethodInfo();
/*      */ 
/* 1042 */     if (localEnclosingMethodInfo == null) {
/* 1043 */       return null;
/*      */     }
/* 1045 */     if (!localEnclosingMethodInfo.isConstructor()) {
/* 1046 */       return null;
/*      */     }
/* 1048 */     ConstructorRepository localConstructorRepository = ConstructorRepository.make(localEnclosingMethodInfo.getDescriptor(), getFactory());
/*      */ 
/* 1050 */     Type[] arrayOfType = localConstructorRepository.getParameterTypes();
/* 1051 */     Class[] arrayOfClass1 = new Class[arrayOfType.length];
/*      */ 
/* 1056 */     for (int i = 0; i < arrayOfClass1.length; i++) {
/* 1057 */       arrayOfClass1[i] = toClass(arrayOfType[i]);
/*      */     }
/*      */ 
/* 1060 */     Class localClass = localEnclosingMethodInfo.getEnclosingClass();
/*      */ 
/* 1066 */     localClass.checkMemberAccess(1, Reflection.getCallerClass(), true);
/*      */ 
/* 1072 */     for (Constructor localConstructor : localClass.getDeclaredConstructors()) {
/* 1073 */       Class[] arrayOfClass2 = localConstructor.getParameterTypes();
/* 1074 */       if (arrayOfClass2.length == arrayOfClass1.length) {
/* 1075 */         int m = 1;
/* 1076 */         for (int n = 0; n < arrayOfClass2.length; n++) {
/* 1077 */           if (!arrayOfClass2[n].equals(arrayOfClass1[n])) {
/* 1078 */             m = 0;
/* 1079 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1083 */         if (m != 0) {
/* 1084 */           return localConstructor;
/*      */         }
/*      */       }
/*      */     }
/* 1088 */     throw new InternalError("Enclosing constructor not found");
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Class<?> getDeclaringClass()
/*      */   {
/* 1106 */     Class localClass = getDeclaringClass0();
/*      */ 
/* 1108 */     if (localClass != null) {
/* 1109 */       localClass.checkPackageAccess(ClassLoader.getClassLoader(Reflection.getCallerClass()), true);
/*      */     }
/* 1111 */     return localClass;
/*      */   }
/*      */ 
/*      */   private native Class<?> getDeclaringClass0();
/*      */ 
/*      */   @CallerSensitive
/*      */   public Class<?> getEnclosingClass()
/*      */   {
/* 1137 */     EnclosingMethodInfo localEnclosingMethodInfo = getEnclosingMethodInfo();
/*      */     Object localObject;
/* 1140 */     if (localEnclosingMethodInfo == null)
/*      */     {
/* 1142 */       localObject = getDeclaringClass();
/*      */     } else {
/* 1144 */       Class localClass = localEnclosingMethodInfo.getEnclosingClass();
/*      */ 
/* 1146 */       if ((localClass == this) || (localClass == null)) {
/* 1147 */         throw new InternalError("Malformed enclosing method information");
/*      */       }
/* 1149 */       localObject = localClass;
/*      */     }
/*      */ 
/* 1152 */     if (localObject != null) {
/* 1153 */       ((Class)localObject).checkPackageAccess(ClassLoader.getClassLoader(Reflection.getCallerClass()), true);
/*      */     }
/* 1155 */     return localObject;
/*      */   }
/*      */ 
/*      */   public String getSimpleName()
/*      */   {
/* 1171 */     if (isArray()) {
/* 1172 */       return getComponentType().getSimpleName() + "[]";
/*      */     }
/* 1174 */     String str = getSimpleBinaryName();
/* 1175 */     if (str == null) {
/* 1176 */       str = getName();
/* 1177 */       return str.substring(str.lastIndexOf(".") + 1);
/*      */     }
/*      */ 
/* 1193 */     int i = str.length();
/* 1194 */     if ((i < 1) || (str.charAt(0) != '$'))
/* 1195 */       throw new InternalError("Malformed class name");
/* 1196 */     int j = 1;
/* 1197 */     while ((j < i) && (isAsciiDigit(str.charAt(j)))) {
/* 1198 */       j++;
/*      */     }
/* 1200 */     return str.substring(j);
/*      */   }
/*      */ 
/*      */   private static boolean isAsciiDigit(char paramChar)
/*      */   {
/* 1208 */     return ('0' <= paramChar) && (paramChar <= '9');
/*      */   }
/*      */ 
/*      */   public String getCanonicalName()
/*      */   {
/* 1222 */     if (isArray()) {
/* 1223 */       localObject = getComponentType().getCanonicalName();
/* 1224 */       if (localObject != null) {
/* 1225 */         return (String)localObject + "[]";
/*      */       }
/* 1227 */       return null;
/*      */     }
/* 1229 */     if (isLocalOrAnonymousClass())
/* 1230 */       return null;
/* 1231 */     Object localObject = getEnclosingClass();
/* 1232 */     if (localObject == null) {
/* 1233 */       return getName();
/*      */     }
/* 1235 */     String str = ((Class)localObject).getCanonicalName();
/* 1236 */     if (str == null)
/* 1237 */       return null;
/* 1238 */     return str + "." + getSimpleName();
/*      */   }
/*      */ 
/*      */   public boolean isAnonymousClass()
/*      */   {
/* 1250 */     return "".equals(getSimpleName());
/*      */   }
/*      */ 
/*      */   public boolean isLocalClass()
/*      */   {
/* 1261 */     return (isLocalOrAnonymousClass()) && (!isAnonymousClass());
/*      */   }
/*      */ 
/*      */   public boolean isMemberClass()
/*      */   {
/* 1272 */     return (getSimpleBinaryName() != null) && (!isLocalOrAnonymousClass());
/*      */   }
/*      */ 
/*      */   private String getSimpleBinaryName()
/*      */   {
/* 1282 */     Class localClass = getEnclosingClass();
/* 1283 */     if (localClass == null)
/* 1284 */       return null;
/*      */     try
/*      */     {
/* 1287 */       return getName().substring(localClass.getName().length()); } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*      */     }
/* 1289 */     throw new InternalError("Malformed class name");
/*      */   }
/*      */ 
/*      */   private boolean isLocalOrAnonymousClass()
/*      */   {
/* 1301 */     return getEnclosingMethodInfo() != null;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Class<?>[] getClasses()
/*      */   {
/* 1343 */     checkMemberAccess(0, Reflection.getCallerClass(), false);
/*      */ 
/* 1351 */     return (Class[])AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Class[] run() {
/* 1354 */         ArrayList localArrayList = new ArrayList();
/* 1355 */         Class localClass = Class.this;
/* 1356 */         while (localClass != null) {
/* 1357 */           Class[] arrayOfClass = localClass.getDeclaredClasses();
/* 1358 */           for (int i = 0; i < arrayOfClass.length; i++) {
/* 1359 */             if (Modifier.isPublic(arrayOfClass[i].getModifiers())) {
/* 1360 */               localArrayList.add(arrayOfClass[i]);
/*      */             }
/*      */           }
/* 1363 */           localClass = localClass.getSuperclass();
/*      */         }
/* 1365 */         return (Class[])localArrayList.toArray(new Class[0]);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Field[] getFields()
/*      */     throws SecurityException
/*      */   {
/* 1419 */     checkMemberAccess(0, Reflection.getCallerClass(), true);
/* 1420 */     return copyFields(privateGetPublicFields(null));
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Method[] getMethods()
/*      */     throws SecurityException
/*      */   {
/* 1471 */     checkMemberAccess(0, Reflection.getCallerClass(), true);
/* 1472 */     return copyMethods(privateGetPublicMethods());
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Constructor<?>[] getConstructors()
/*      */     throws SecurityException
/*      */   {
/* 1521 */     checkMemberAccess(0, Reflection.getCallerClass(), true);
/* 1522 */     return copyConstructors(privateGetDeclaredConstructors(true));
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Field getField(String paramString)
/*      */     throws NoSuchFieldException, SecurityException
/*      */   {
/* 1581 */     checkMemberAccess(0, Reflection.getCallerClass(), true);
/* 1582 */     Field localField = getField0(paramString);
/* 1583 */     if (localField == null) {
/* 1584 */       throw new NoSuchFieldException(paramString);
/*      */     }
/* 1586 */     return localField;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Method getMethod(String paramString, Class<?>[] paramArrayOfClass)
/*      */     throws NoSuchMethodException, SecurityException
/*      */   {
/* 1667 */     checkMemberAccess(0, Reflection.getCallerClass(), true);
/* 1668 */     Method localMethod = getMethod0(paramString, paramArrayOfClass);
/* 1669 */     if (localMethod == null) {
/* 1670 */       throw new NoSuchMethodException(getName() + "." + paramString + argumentTypesToString(paramArrayOfClass));
/*      */     }
/* 1672 */     return localMethod;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Constructor<T> getConstructor(Class<?>[] paramArrayOfClass)
/*      */     throws NoSuchMethodException, SecurityException
/*      */   {
/* 1722 */     checkMemberAccess(0, Reflection.getCallerClass(), true);
/* 1723 */     return getConstructor0(paramArrayOfClass, 0);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Class<?>[] getDeclaredClasses()
/*      */     throws SecurityException
/*      */   {
/* 1765 */     checkMemberAccess(1, Reflection.getCallerClass(), false);
/* 1766 */     return getDeclaredClasses0();
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Field[] getDeclaredFields()
/*      */     throws SecurityException
/*      */   {
/* 1810 */     checkMemberAccess(1, Reflection.getCallerClass(), true);
/* 1811 */     return copyFields(privateGetDeclaredFields(false));
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Method[] getDeclaredMethods()
/*      */     throws SecurityException
/*      */   {
/* 1859 */     checkMemberAccess(1, Reflection.getCallerClass(), true);
/* 1860 */     return copyMethods(privateGetDeclaredMethods(false));
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Constructor<?>[] getDeclaredConstructors()
/*      */     throws SecurityException
/*      */   {
/* 1905 */     checkMemberAccess(1, Reflection.getCallerClass(), true);
/* 1906 */     return copyConstructors(privateGetDeclaredConstructors(false));
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Field getDeclaredField(String paramString)
/*      */     throws NoSuchFieldException, SecurityException
/*      */   {
/* 1950 */     checkMemberAccess(1, Reflection.getCallerClass(), true);
/* 1951 */     Field localField = searchFields(privateGetDeclaredFields(false), paramString);
/* 1952 */     if (localField == null) {
/* 1953 */       throw new NoSuchFieldException(paramString);
/*      */     }
/* 1955 */     return localField;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Method getDeclaredMethod(String paramString, Class<?>[] paramArrayOfClass)
/*      */     throws NoSuchMethodException, SecurityException
/*      */   {
/* 2006 */     checkMemberAccess(1, Reflection.getCallerClass(), true);
/* 2007 */     Method localMethod = searchMethods(privateGetDeclaredMethods(false), paramString, paramArrayOfClass);
/* 2008 */     if (localMethod == null) {
/* 2009 */       throw new NoSuchMethodException(getName() + "." + paramString + argumentTypesToString(paramArrayOfClass));
/*      */     }
/* 2011 */     return localMethod;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Constructor<T> getDeclaredConstructor(Class<?>[] paramArrayOfClass)
/*      */     throws NoSuchMethodException, SecurityException
/*      */   {
/* 2057 */     checkMemberAccess(1, Reflection.getCallerClass(), true);
/* 2058 */     return getConstructor0(paramArrayOfClass, 1);
/*      */   }
/*      */ 
/*      */   public InputStream getResourceAsStream(String paramString)
/*      */   {
/* 2097 */     paramString = resolveName(paramString);
/* 2098 */     ClassLoader localClassLoader = getClassLoader0();
/* 2099 */     if (localClassLoader == null)
/*      */     {
/* 2101 */       return ClassLoader.getSystemResourceAsStream(paramString);
/*      */     }
/* 2103 */     return localClassLoader.getResourceAsStream(paramString);
/*      */   }
/*      */ 
/*      */   public URL getResource(String paramString)
/*      */   {
/* 2141 */     paramString = resolveName(paramString);
/* 2142 */     ClassLoader localClassLoader = getClassLoader0();
/* 2143 */     if (localClassLoader == null)
/*      */     {
/* 2145 */       return ClassLoader.getSystemResource(paramString);
/*      */     }
/* 2147 */     return localClassLoader.getResource(paramString);
/*      */   }
/*      */ 
/*      */   public ProtectionDomain getProtectionDomain()
/*      */   {
/* 2177 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2178 */     if (localSecurityManager != null) {
/* 2179 */       localSecurityManager.checkPermission(SecurityConstants.GET_PD_PERMISSION);
/*      */     }
/* 2181 */     ProtectionDomain localProtectionDomain = getProtectionDomain0();
/* 2182 */     if (localProtectionDomain == null) {
/* 2183 */       if (allPermDomain == null) {
/* 2184 */         Permissions localPermissions = new Permissions();
/*      */ 
/* 2186 */         localPermissions.add(SecurityConstants.ALL_PERMISSION);
/* 2187 */         allPermDomain = new ProtectionDomain(null, localPermissions);
/*      */       }
/*      */ 
/* 2190 */       localProtectionDomain = allPermDomain;
/*      */     }
/* 2192 */     return localProtectionDomain;
/*      */   }
/*      */ 
/*      */   private native ProtectionDomain getProtectionDomain0();
/*      */ 
/*      */   native void setProtectionDomain0(ProtectionDomain paramProtectionDomain);
/*      */ 
/*      */   static native Class getPrimitiveClass(String paramString);
/*      */ 
/*      */   private static boolean isCheckMemberAccessOverridden(SecurityManager paramSecurityManager)
/*      */   {
/* 2237 */     if (paramSecurityManager.getClass() == SecurityManager.class) return false;
/*      */ 
/* 2239 */     SecurityManagerHelper localSecurityManagerHelper = smHelper;
/* 2240 */     if ((localSecurityManagerHelper == null) || (localSecurityManagerHelper.sm != paramSecurityManager)) {
/* 2241 */       localSecurityManagerHelper = new SecurityManagerHelper(paramSecurityManager);
/* 2242 */       smHelper = localSecurityManagerHelper;
/*      */     }
/* 2244 */     return localSecurityManagerHelper.overrideCheckMemberAccess;
/*      */   }
/*      */ 
/*      */   private static native Method getCheckMemberAccessMethod(Class<? extends SecurityManager> paramClass)
/*      */     throws NoSuchMethodError;
/*      */ 
/*      */   private void checkMemberAccess(int paramInt, Class<?> paramClass, boolean paramBoolean)
/*      */   {
/* 2273 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2274 */     if (localSecurityManager != null) {
/* 2275 */       ClassLoader localClassLoader1 = ClassLoader.getClassLoader(paramClass);
/* 2276 */       ClassLoader localClassLoader2 = getClassLoader0();
/* 2277 */       if (!isCheckMemberAccessOverridden(localSecurityManager))
/*      */       {
/* 2279 */         if ((paramInt != 0) && 
/* 2280 */           (localClassLoader1 != localClassLoader2)) {
/* 2281 */           localSecurityManager.checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2287 */         localSecurityManager.checkMemberAccess(this, paramInt);
/*      */       }
/* 2289 */       checkPackageAccess(localClassLoader1, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkPackageAccess(ClassLoader paramClassLoader, boolean paramBoolean)
/*      */   {
/* 2299 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2300 */     if (localSecurityManager != null) {
/* 2301 */       ClassLoader localClassLoader = getClassLoader0();
/* 2302 */       if (ReflectUtil.needsPackageAccessCheck(paramClassLoader, localClassLoader)) {
/* 2303 */         String str1 = getName();
/* 2304 */         int i = str1.lastIndexOf('.');
/* 2305 */         if (i != -1)
/*      */         {
/* 2307 */           String str2 = str1.substring(0, i);
/* 2308 */           if ((!Proxy.isProxyClass(this)) || (ReflectUtil.isNonPublicProxyClass(this))) {
/* 2309 */             localSecurityManager.checkPackageAccess(str2);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2314 */       if ((paramBoolean) && (Proxy.isProxyClass(this)))
/* 2315 */         ReflectUtil.checkProxyPackageAccess(paramClassLoader, getInterfaces());
/*      */     }
/*      */   }
/*      */ 
/*      */   private String resolveName(String paramString)
/*      */   {
/* 2325 */     if (paramString == null) {
/* 2326 */       return paramString;
/*      */     }
/* 2328 */     if (!paramString.startsWith("/")) {
/* 2329 */       Class localClass = this;
/* 2330 */       while (localClass.isArray()) {
/* 2331 */         localClass = localClass.getComponentType();
/*      */       }
/* 2333 */       String str = localClass.getName();
/* 2334 */       int i = str.lastIndexOf('.');
/* 2335 */       if (i != -1)
/* 2336 */         paramString = str.substring(0, i).replace('.', '/') + "/" + paramString;
/*      */     }
/*      */     else
/*      */     {
/* 2340 */       paramString = paramString.substring(1);
/*      */     }
/* 2342 */     return paramString;
/*      */   }
/*      */ 
/*      */   private ReflectionData<T> reflectionData()
/*      */   {
/* 2418 */     SoftReference localSoftReference = this.reflectionData;
/* 2419 */     int i = this.classRedefinedCount;
/*      */     ReflectionData localReflectionData;
/* 2421 */     if ((useCaches) && (localSoftReference != null) && ((localReflectionData = (ReflectionData)localSoftReference.get()) != null) && (localReflectionData.redefinedCount == i))
/*      */     {
/* 2425 */       return localReflectionData;
/*      */     }
/*      */ 
/* 2429 */     return newReflectionData(localSoftReference, i);
/*      */   }
/*      */ 
/*      */   private ReflectionData<T> newReflectionData(SoftReference<ReflectionData<T>> paramSoftReference, int paramInt)
/*      */   {
/* 2434 */     if (!useCaches) return null;
/*      */     while (true)
/*      */     {
/* 2437 */       ReflectionData localReflectionData = new ReflectionData(paramInt);
/*      */ 
/* 2439 */       if (Atomic.casReflectionData(this, paramSoftReference, new SoftReference(localReflectionData))) {
/* 2440 */         return localReflectionData;
/*      */       }
/*      */ 
/* 2443 */       paramSoftReference = this.reflectionData;
/* 2444 */       paramInt = this.classRedefinedCount;
/* 2445 */       if ((paramSoftReference != null) && ((localReflectionData = (ReflectionData)paramSoftReference.get()) != null) && (localReflectionData.redefinedCount == paramInt))
/*      */       {
/* 2448 */         return localReflectionData;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private native String getGenericSignature();
/*      */ 
/*      */   private GenericsFactory getFactory()
/*      */   {
/* 2462 */     return CoreReflectionFactory.make(this, ClassScope.make(this));
/*      */   }
/*      */ 
/*      */   private ClassRepository getGenericInfo()
/*      */   {
/* 2468 */     if (this.genericInfo == null)
/*      */     {
/* 2470 */       this.genericInfo = ClassRepository.make(getGenericSignature(), getFactory());
/*      */     }
/*      */ 
/* 2473 */     return this.genericInfo;
/*      */   }
/*      */ 
/*      */   native byte[] getRawAnnotations();
/*      */ 
/*      */   native ConstantPool getConstantPool();
/*      */ 
/*      */   private Field[] privateGetDeclaredFields(boolean paramBoolean)
/*      */   {
/* 2491 */     checkInitted();
/*      */ 
/* 2493 */     ReflectionData localReflectionData = reflectionData();
/* 2494 */     if (localReflectionData != null) {
/* 2495 */       arrayOfField = paramBoolean ? localReflectionData.declaredPublicFields : localReflectionData.declaredFields;
/* 2496 */       if (arrayOfField != null) return arrayOfField;
/*      */     }
/*      */ 
/* 2499 */     Field[] arrayOfField = Reflection.filterFields(this, getDeclaredFields0(paramBoolean));
/* 2500 */     if (localReflectionData != null) {
/* 2501 */       if (paramBoolean)
/* 2502 */         localReflectionData.declaredPublicFields = arrayOfField;
/*      */       else {
/* 2504 */         localReflectionData.declaredFields = arrayOfField;
/*      */       }
/*      */     }
/* 2507 */     return arrayOfField;
/*      */   }
/*      */ 
/*      */   private Field[] privateGetPublicFields(Set<Class<?>> paramSet)
/*      */   {
/* 2514 */     checkInitted();
/*      */ 
/* 2516 */     ReflectionData localReflectionData = reflectionData();
/* 2517 */     if (localReflectionData != null) {
/* 2518 */       arrayOfField1 = localReflectionData.publicFields;
/* 2519 */       if (arrayOfField1 != null) return arrayOfField1;
/*      */ 
/*      */     }
/*      */ 
/* 2524 */     ArrayList localArrayList = new ArrayList();
/* 2525 */     if (paramSet == null) {
/* 2526 */       paramSet = new HashSet();
/*      */     }
/*      */ 
/* 2530 */     Field[] arrayOfField2 = privateGetDeclaredFields(true);
/* 2531 */     addAll(localArrayList, arrayOfField2);
/*      */ 
/* 2534 */     for (Object localObject2 : getInterfaces()) {
/* 2535 */       if (!paramSet.contains(localObject2)) {
/* 2536 */         paramSet.add(localObject2);
/* 2537 */         addAll(localArrayList, localObject2.privateGetPublicFields(paramSet));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2542 */     if (!isInterface()) {
/* 2543 */       ??? = getSuperclass();
/* 2544 */       if (??? != null) {
/* 2545 */         addAll(localArrayList, ((Class)???).privateGetPublicFields(paramSet));
/*      */       }
/*      */     }
/*      */ 
/* 2549 */     Field[] arrayOfField1 = new Field[localArrayList.size()];
/* 2550 */     localArrayList.toArray(arrayOfField1);
/* 2551 */     if (localReflectionData != null) {
/* 2552 */       localReflectionData.publicFields = arrayOfField1;
/*      */     }
/* 2554 */     return arrayOfField1;
/*      */   }
/*      */ 
/*      */   private static void addAll(Collection<Field> paramCollection, Field[] paramArrayOfField) {
/* 2558 */     for (int i = 0; i < paramArrayOfField.length; i++)
/* 2559 */       paramCollection.add(paramArrayOfField[i]);
/*      */   }
/*      */ 
/*      */   private Constructor<T>[] privateGetDeclaredConstructors(boolean paramBoolean)
/*      */   {
/* 2574 */     checkInitted();
/*      */ 
/* 2576 */     ReflectionData localReflectionData = reflectionData();
/*      */     Constructor[] arrayOfConstructor;
/* 2577 */     if (localReflectionData != null) {
/* 2578 */       arrayOfConstructor = paramBoolean ? localReflectionData.publicConstructors : localReflectionData.declaredConstructors;
/* 2579 */       if (arrayOfConstructor != null) return arrayOfConstructor;
/*      */     }
/*      */ 
/* 2582 */     if (isInterface())
/* 2583 */       arrayOfConstructor = new Constructor[0];
/*      */     else {
/* 2585 */       arrayOfConstructor = getDeclaredConstructors0(paramBoolean);
/*      */     }
/* 2587 */     if (localReflectionData != null) {
/* 2588 */       if (paramBoolean)
/* 2589 */         localReflectionData.publicConstructors = arrayOfConstructor;
/*      */       else {
/* 2591 */         localReflectionData.declaredConstructors = arrayOfConstructor;
/*      */       }
/*      */     }
/* 2594 */     return arrayOfConstructor;
/*      */   }
/*      */ 
/*      */   private Method[] privateGetDeclaredMethods(boolean paramBoolean)
/*      */   {
/* 2607 */     checkInitted();
/*      */ 
/* 2609 */     ReflectionData localReflectionData = reflectionData();
/* 2610 */     if (localReflectionData != null) {
/* 2611 */       arrayOfMethod = paramBoolean ? localReflectionData.declaredPublicMethods : localReflectionData.declaredMethods;
/* 2612 */       if (arrayOfMethod != null) return arrayOfMethod;
/*      */     }
/*      */ 
/* 2615 */     Method[] arrayOfMethod = Reflection.filterMethods(this, getDeclaredMethods0(paramBoolean));
/* 2616 */     if (localReflectionData != null) {
/* 2617 */       if (paramBoolean)
/* 2618 */         localReflectionData.declaredPublicMethods = arrayOfMethod;
/*      */       else {
/* 2620 */         localReflectionData.declaredMethods = arrayOfMethod;
/*      */       }
/*      */     }
/* 2623 */     return arrayOfMethod;
/*      */   }
/*      */ 
/*      */   private Method[] privateGetPublicMethods()
/*      */   {
/* 2721 */     checkInitted();
/*      */ 
/* 2723 */     ReflectionData localReflectionData = reflectionData();
/* 2724 */     if (localReflectionData != null) {
/* 2725 */       arrayOfMethod = localReflectionData.publicMethods;
/* 2726 */       if (arrayOfMethod != null) return arrayOfMethod;
/*      */ 
/*      */     }
/*      */ 
/* 2731 */     MethodArray localMethodArray = new MethodArray();
/*      */ 
/* 2733 */     Object localObject1 = privateGetDeclaredMethods(true);
/* 2734 */     localMethodArray.addAll((Method[])localObject1);
/*      */ 
/* 2740 */     localObject1 = new MethodArray();
/* 2741 */     Class[] arrayOfClass = getInterfaces();
/* 2742 */     for (int i = 0; i < arrayOfClass.length; i++)
/* 2743 */       ((MethodArray)localObject1).addAll(arrayOfClass[i].privateGetPublicMethods());
/*      */     Object localObject2;
/* 2745 */     if (!isInterface()) {
/* 2746 */       Class localClass = getSuperclass();
/* 2747 */       if (localClass != null) {
/* 2748 */         localObject2 = new MethodArray();
/* 2749 */         ((MethodArray)localObject2).addAll(localClass.privateGetPublicMethods());
/*      */ 
/* 2752 */         for (int k = 0; k < ((MethodArray)localObject2).length(); k++) {
/* 2753 */           Method localMethod = ((MethodArray)localObject2).get(k);
/* 2754 */           if ((localMethod != null) && (!Modifier.isAbstract(localMethod.getModifiers()))) {
/* 2755 */             ((MethodArray)localObject1).removeByNameAndSignature(localMethod);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2761 */         ((MethodArray)localObject2).addAll((MethodArray)localObject1);
/* 2762 */         localObject1 = localObject2;
/*      */       }
/*      */     }
/*      */ 
/* 2766 */     for (int j = 0; j < localMethodArray.length(); j++) {
/* 2767 */       localObject2 = localMethodArray.get(j);
/* 2768 */       ((MethodArray)localObject1).removeByNameAndSignature((Method)localObject2);
/*      */     }
/* 2770 */     localMethodArray.addAllIfNotPresent((MethodArray)localObject1);
/* 2771 */     localMethodArray.compactAndTrim();
/* 2772 */     Method[] arrayOfMethod = localMethodArray.getArray();
/* 2773 */     if (localReflectionData != null) {
/* 2774 */       localReflectionData.publicMethods = arrayOfMethod;
/*      */     }
/* 2776 */     return arrayOfMethod;
/*      */   }
/*      */ 
/*      */   private static Field searchFields(Field[] paramArrayOfField, String paramString)
/*      */   {
/* 2785 */     String str = paramString.intern();
/* 2786 */     for (int i = 0; i < paramArrayOfField.length; i++) {
/* 2787 */       if (paramArrayOfField[i].getName() == str) {
/* 2788 */         return getReflectionFactory().copyField(paramArrayOfField[i]);
/*      */       }
/*      */     }
/* 2791 */     return null;
/*      */   }
/*      */ 
/*      */   private Field getField0(String paramString)
/*      */     throws NoSuchFieldException
/*      */   {
/*      */     Field localField;
/* 2804 */     if ((localField = searchFields(privateGetDeclaredFields(true), paramString)) != null) {
/* 2805 */       return localField;
/*      */     }
/*      */ 
/* 2808 */     Class[] arrayOfClass = getInterfaces();
/* 2809 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 2810 */       Class localClass2 = arrayOfClass[i];
/* 2811 */       if ((localField = localClass2.getField0(paramString)) != null) {
/* 2812 */         return localField;
/*      */       }
/*      */     }
/*      */ 
/* 2816 */     if (!isInterface()) {
/* 2817 */       Class localClass1 = getSuperclass();
/* 2818 */       if ((localClass1 != null) && 
/* 2819 */         ((localField = localClass1.getField0(paramString)) != null)) {
/* 2820 */         return localField;
/*      */       }
/*      */     }
/*      */ 
/* 2824 */     return null;
/*      */   }
/*      */ 
/*      */   private static Method searchMethods(Method[] paramArrayOfMethod, String paramString, Class<?>[] paramArrayOfClass)
/*      */   {
/* 2831 */     Object localObject = null;
/* 2832 */     String str = paramString.intern();
/* 2833 */     for (int i = 0; i < paramArrayOfMethod.length; i++) {
/* 2834 */       Method localMethod = paramArrayOfMethod[i];
/* 2835 */       if ((localMethod.getName() == str) && (arrayContentsEq(paramArrayOfClass, localMethod.getParameterTypes())) && ((localObject == null) || (localObject.getReturnType().isAssignableFrom(localMethod.getReturnType()))))
/*      */       {
/* 2839 */         localObject = localMethod;
/*      */       }
/*      */     }
/* 2842 */     return localObject == null ? localObject : getReflectionFactory().copyMethod(localObject);
/*      */   }
/*      */ 
/*      */   private Method getMethod0(String paramString, Class<?>[] paramArrayOfClass)
/*      */   {
/*      */     Method localMethod;
/* 2856 */     if ((localMethod = searchMethods(privateGetDeclaredMethods(true), paramString, paramArrayOfClass)) != null)
/*      */     {
/* 2859 */       return localMethod;
/*      */     }
/*      */ 
/* 2862 */     if (!isInterface()) {
/* 2863 */       localObject1 = getSuperclass();
/* 2864 */       if ((localObject1 != null) && 
/* 2865 */         ((localMethod = ((Class)localObject1).getMethod0(paramString, paramArrayOfClass)) != null)) {
/* 2866 */         return localMethod;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2871 */     Object localObject1 = getInterfaces();
/* 2872 */     for (int i = 0; i < localObject1.length; i++) {
/* 2873 */       Object localObject2 = localObject1[i];
/* 2874 */       if ((localMethod = localObject2.getMethod0(paramString, paramArrayOfClass)) != null) {
/* 2875 */         return localMethod;
/*      */       }
/*      */     }
/*      */ 
/* 2879 */     return null;
/*      */   }
/*      */ 
/*      */   private Constructor<T> getConstructor0(Class<?>[] paramArrayOfClass, int paramInt)
/*      */     throws NoSuchMethodException
/*      */   {
/* 2885 */     Constructor[] arrayOfConstructor1 = privateGetDeclaredConstructors(paramInt == 0);
/* 2886 */     for (Constructor localConstructor : arrayOfConstructor1) {
/* 2887 */       if (arrayContentsEq(paramArrayOfClass, localConstructor.getParameterTypes()))
/*      */       {
/* 2889 */         return getReflectionFactory().copyConstructor(localConstructor);
/*      */       }
/*      */     }
/* 2892 */     throw new NoSuchMethodException(getName() + ".<init>" + argumentTypesToString(paramArrayOfClass));
/*      */   }
/*      */ 
/*      */   private static boolean arrayContentsEq(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2)
/*      */   {
/* 2900 */     if (paramArrayOfObject1 == null) {
/* 2901 */       return (paramArrayOfObject2 == null) || (paramArrayOfObject2.length == 0);
/*      */     }
/*      */ 
/* 2904 */     if (paramArrayOfObject2 == null) {
/* 2905 */       return paramArrayOfObject1.length == 0;
/*      */     }
/*      */ 
/* 2908 */     if (paramArrayOfObject1.length != paramArrayOfObject2.length) {
/* 2909 */       return false;
/*      */     }
/*      */ 
/* 2912 */     for (int i = 0; i < paramArrayOfObject1.length; i++) {
/* 2913 */       if (paramArrayOfObject1[i] != paramArrayOfObject2[i]) {
/* 2914 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 2918 */     return true;
/*      */   }
/*      */ 
/*      */   private static Field[] copyFields(Field[] paramArrayOfField) {
/* 2922 */     Field[] arrayOfField = new Field[paramArrayOfField.length];
/* 2923 */     ReflectionFactory localReflectionFactory = getReflectionFactory();
/* 2924 */     for (int i = 0; i < paramArrayOfField.length; i++) {
/* 2925 */       arrayOfField[i] = localReflectionFactory.copyField(paramArrayOfField[i]);
/*      */     }
/* 2927 */     return arrayOfField;
/*      */   }
/*      */ 
/*      */   private static Method[] copyMethods(Method[] paramArrayOfMethod) {
/* 2931 */     Method[] arrayOfMethod = new Method[paramArrayOfMethod.length];
/* 2932 */     ReflectionFactory localReflectionFactory = getReflectionFactory();
/* 2933 */     for (int i = 0; i < paramArrayOfMethod.length; i++) {
/* 2934 */       arrayOfMethod[i] = localReflectionFactory.copyMethod(paramArrayOfMethod[i]);
/*      */     }
/* 2936 */     return arrayOfMethod;
/*      */   }
/*      */ 
/*      */   private static <U> Constructor<U>[] copyConstructors(Constructor<U>[] paramArrayOfConstructor) {
/* 2940 */     Constructor[] arrayOfConstructor = (Constructor[])paramArrayOfConstructor.clone();
/* 2941 */     ReflectionFactory localReflectionFactory = getReflectionFactory();
/* 2942 */     for (int i = 0; i < arrayOfConstructor.length; i++) {
/* 2943 */       arrayOfConstructor[i] = localReflectionFactory.copyConstructor(arrayOfConstructor[i]);
/*      */     }
/* 2945 */     return arrayOfConstructor; } 
/*      */   private native Field[] getDeclaredFields0(boolean paramBoolean);
/*      */ 
/*      */   private native Method[] getDeclaredMethods0(boolean paramBoolean);
/*      */ 
/*      */   private native Constructor<T>[] getDeclaredConstructors0(boolean paramBoolean);
/*      */ 
/*      */   private native Class<?>[] getDeclaredClasses0();
/*      */ 
/* 2954 */   private static String argumentTypesToString(Class<?>[] paramArrayOfClass) { StringBuilder localStringBuilder = new StringBuilder();
/* 2955 */     localStringBuilder.append("(");
/* 2956 */     if (paramArrayOfClass != null) {
/* 2957 */       for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 2958 */         if (i > 0) {
/* 2959 */           localStringBuilder.append(", ");
/*      */         }
/* 2961 */         Class<?> localClass = paramArrayOfClass[i];
/* 2962 */         localStringBuilder.append(localClass == null ? "null" : localClass.getName());
/*      */       }
/*      */     }
/* 2965 */     localStringBuilder.append(")");
/* 2966 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public boolean desiredAssertionStatus()
/*      */   {
/* 3018 */     ClassLoader localClassLoader = getClassLoader();
/*      */ 
/* 3020 */     if (localClassLoader == null) {
/* 3021 */       return desiredAssertionStatus0(this);
/*      */     }
/*      */ 
/* 3025 */     synchronized (localClassLoader.assertionLock) {
/* 3026 */       if (localClassLoader.classAssertionStatus != null) {
/* 3027 */         return localClassLoader.desiredAssertionStatus(getName());
/*      */       }
/*      */     }
/* 3030 */     return desiredAssertionStatus0(this);
/*      */   }
/*      */ 
/*      */   private static native boolean desiredAssertionStatus0(Class<?> paramClass);
/*      */ 
/*      */   public boolean isEnum()
/*      */   {
/* 3048 */     return ((getModifiers() & 0x4000) != 0) && (getSuperclass() == Enum.class);
/*      */   }
/*      */ 
/*      */   private static ReflectionFactory getReflectionFactory()
/*      */   {
/* 3054 */     if (reflectionFactory == null) {
/* 3055 */       reflectionFactory = (ReflectionFactory)AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
/*      */     }
/*      */ 
/* 3059 */     return reflectionFactory;
/*      */   }
/*      */ 
/*      */   private static void checkInitted()
/*      */   {
/* 3066 */     if (initted) return;
/* 3067 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Void run()
/*      */       {
/* 3078 */         if (System.out == null)
/*      */         {
/* 3080 */           return null;
/*      */         }
/*      */ 
/* 3083 */         String str = System.getProperty("sun.reflect.noCaches");
/*      */ 
/* 3085 */         if ((str != null) && (str.equals("true"))) {
/* 3086 */           Class.access$502(false);
/*      */         }
/*      */ 
/* 3089 */         Class.access$602(true);
/* 3090 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public T[] getEnumConstants()
/*      */   {
/* 3106 */     Object[] arrayOfObject = getEnumConstantsShared();
/* 3107 */     return arrayOfObject != null ? (Object[])arrayOfObject.clone() : null;
/*      */   }
/*      */ 
/*      */   T[] getEnumConstantsShared()
/*      */   {
/* 3117 */     if (this.enumConstants == null) {
/* 3118 */       if (!isEnum()) return null; try
/*      */       {
/* 3120 */         final Method localMethod = getMethod("values", new Class[0]);
/* 3121 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Void run() {
/* 3124 */             localMethod.setAccessible(true);
/* 3125 */             return null;
/*      */           }
/*      */         });
/* 3128 */         this.enumConstants = ((Object[])localMethod.invoke(null, new Object[0]));
/*      */       }
/*      */       catch (InvocationTargetException localInvocationTargetException)
/*      */       {
/* 3132 */         return null; } catch (NoSuchMethodException localNoSuchMethodException) {
/* 3133 */         return null; } catch (IllegalAccessException localIllegalAccessException) {
/* 3134 */         return null;
/*      */       }
/*      */     }
/* 3136 */     return this.enumConstants;
/*      */   }
/*      */ 
/*      */   Map<String, T> enumConstantDirectory()
/*      */   {
/* 3148 */     if (this.enumConstantDirectory == null) {
/* 3149 */       Object[] arrayOfObject1 = getEnumConstantsShared();
/* 3150 */       if (arrayOfObject1 == null) {
/* 3151 */         throw new IllegalArgumentException(getName() + " is not an enum type");
/*      */       }
/* 3153 */       HashMap localHashMap = new HashMap(2 * arrayOfObject1.length);
/* 3154 */       for (Object localObject : arrayOfObject1)
/* 3155 */         localHashMap.put(((Enum)localObject).name(), localObject);
/* 3156 */       this.enumConstantDirectory = localHashMap;
/*      */     }
/* 3158 */     return this.enumConstantDirectory;
/*      */   }
/*      */ 
/*      */   public T cast(Object paramObject)
/*      */   {
/* 3175 */     if ((paramObject != null) && (!isInstance(paramObject)))
/* 3176 */       throw new ClassCastException(cannotCastMsg(paramObject));
/* 3177 */     return paramObject;
/*      */   }
/*      */ 
/*      */   private String cannotCastMsg(Object paramObject) {
/* 3181 */     return "Cannot cast " + paramObject.getClass().getName() + " to " + getName();
/*      */   }
/*      */ 
/*      */   public <U> Class<? extends U> asSubclass(Class<U> paramClass)
/*      */   {
/* 3205 */     if (paramClass.isAssignableFrom(this)) {
/* 3206 */       return this;
/*      */     }
/* 3208 */     throw new ClassCastException(toString());
/*      */   }
/*      */ 
/*      */   public <A extends Annotation> A getAnnotation(Class<A> paramClass)
/*      */   {
/* 3216 */     if (paramClass == null) {
/* 3217 */       throw new NullPointerException();
/*      */     }
/* 3219 */     initAnnotationsIfNecessary();
/* 3220 */     return (Annotation)this.annotations.get(paramClass);
/*      */   }
/*      */ 
/*      */   public boolean isAnnotationPresent(Class<? extends Annotation> paramClass)
/*      */   {
/* 3229 */     if (paramClass == null) {
/* 3230 */       throw new NullPointerException();
/*      */     }
/* 3232 */     return getAnnotation(paramClass) != null;
/*      */   }
/*      */ 
/*      */   public Annotation[] getAnnotations()
/*      */   {
/* 3240 */     initAnnotationsIfNecessary();
/* 3241 */     return AnnotationParser.toArray(this.annotations);
/*      */   }
/*      */ 
/*      */   public Annotation[] getDeclaredAnnotations()
/*      */   {
/* 3248 */     initAnnotationsIfNecessary();
/* 3249 */     return AnnotationParser.toArray(this.declaredAnnotations);
/*      */   }
/*      */ 
/*      */   private void clearAnnotationCachesOnClassRedefinition()
/*      */   {
/* 3261 */     if (this.lastAnnotationsRedefinedCount != this.classRedefinedCount) {
/* 3262 */       this.annotations = (this.declaredAnnotations = null);
/* 3263 */       this.lastAnnotationsRedefinedCount = this.classRedefinedCount;
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void initAnnotationsIfNecessary() {
/* 3268 */     clearAnnotationCachesOnClassRedefinition();
/* 3269 */     if (this.annotations != null)
/* 3270 */       return;
/* 3271 */     this.declaredAnnotations = AnnotationParser.parseAnnotations(getRawAnnotations(), getConstantPool(), this);
/*      */ 
/* 3273 */     Class localClass1 = getSuperclass();
/* 3274 */     if (localClass1 == null) {
/* 3275 */       this.annotations = this.declaredAnnotations;
/*      */     } else {
/* 3277 */       this.annotations = new HashMap();
/* 3278 */       localClass1.initAnnotationsIfNecessary();
/* 3279 */       for (Map.Entry localEntry : localClass1.annotations.entrySet()) {
/* 3280 */         Class localClass2 = (Class)localEntry.getKey();
/* 3281 */         if (AnnotationType.getInstance(localClass2).isInherited())
/* 3282 */           this.annotations.put(localClass2, localEntry.getValue());
/*      */       }
/* 3284 */       this.annotations.putAll(this.declaredAnnotations);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean casAnnotationType(AnnotationType paramAnnotationType1, AnnotationType paramAnnotationType2)
/*      */   {
/* 3294 */     return Atomic.casAnnotationType(this, paramAnnotationType1, paramAnnotationType2);
/*      */   }
/*      */ 
/*      */   AnnotationType getAnnotationType() {
/* 3298 */     return this.annotationType;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  129 */     registerNatives();
/*      */   }
/*      */ 
/*      */   private static class Atomic
/*      */   {
/* 2351 */     private static final Unsafe unsafe = Unsafe.getUnsafe();
/*      */ 
/* 2359 */     private static final long reflectionDataOffset = objectFieldOffset(arrayOfField, "reflectionData");
/* 2360 */     private static final long annotationTypeOffset = objectFieldOffset(arrayOfField, "annotationType");
/*      */ 
/*      */     private static long objectFieldOffset(Field[] paramArrayOfField, String paramString)
/*      */     {
/* 2364 */       Field localField = Class.searchFields(paramArrayOfField, paramString);
/* 2365 */       if (localField == null) {
/* 2366 */         throw new Error("No " + paramString + " field found in java.lang.Class");
/*      */       }
/* 2368 */       return unsafe.objectFieldOffset(localField);
/*      */     }
/*      */ 
/*      */     static <T> boolean casReflectionData(Class<?> paramClass, SoftReference<Class.ReflectionData<T>> paramSoftReference1, SoftReference<Class.ReflectionData<T>> paramSoftReference2)
/*      */     {
/* 2374 */       return unsafe.compareAndSwapObject(paramClass, reflectionDataOffset, paramSoftReference1, paramSoftReference2);
/*      */     }
/*      */ 
/*      */     static <T> boolean casAnnotationType(Class<?> paramClass, AnnotationType paramAnnotationType1, AnnotationType paramAnnotationType2)
/*      */     {
/* 2380 */       return unsafe.compareAndSwapObject(paramClass, annotationTypeOffset, paramAnnotationType1, paramAnnotationType2);
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/* 2358 */       Field[] arrayOfField = Class.class.getDeclaredFields0(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class EnclosingMethodInfo
/*      */   {
/*      */     private Class<?> enclosingClass;
/*      */     private String name;
/*      */     private String descriptor;
/*      */ 
/*      */     private EnclosingMethodInfo(Object[] paramArrayOfObject)
/*      */     {
/*  978 */       if (paramArrayOfObject.length != 3) {
/*  979 */         throw new InternalError("Malformed enclosing method information");
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  984 */         this.enclosingClass = ((Class)paramArrayOfObject[0]);
/*  985 */         assert (this.enclosingClass != null);
/*      */ 
/*  989 */         this.name = ((String)paramArrayOfObject[1]);
/*      */ 
/*  993 */         this.descriptor = ((String)paramArrayOfObject[2]);
/*  994 */         if ((!$assertionsDisabled) && ((this.name == null) || (this.descriptor == null)) && (this.name != this.descriptor)) throw new AssertionError(); 
/*      */       }
/*  996 */       catch (ClassCastException localClassCastException) { throw new InternalError("Invalid type in enclosing method information"); }
/*      */     }
/*      */ 
/*      */     boolean isPartial()
/*      */     {
/* 1001 */       return (this.enclosingClass == null) || (this.name == null) || (this.descriptor == null);
/*      */     }
/*      */     boolean isConstructor() {
/* 1004 */       return (!isPartial()) && ("<init>".equals(this.name));
/*      */     }
/* 1006 */     boolean isMethod() { return (!isPartial()) && (!isConstructor()) && (!"<clinit>".equals(this.name)); } 
/*      */     Class<?> getEnclosingClass() {
/* 1008 */       return this.enclosingClass;
/*      */     }
/* 1010 */     String getName() { return this.name; } 
/*      */     String getDescriptor() {
/* 1012 */       return this.descriptor;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class MethodArray
/*      */   {
/*      */     private Method[] methods;
/*      */     private int length;
/*      */ 
/*      */     MethodArray()
/*      */     {
/* 2631 */       this.methods = new Method[20];
/* 2632 */       this.length = 0;
/*      */     }
/*      */ 
/*      */     void add(Method paramMethod) {
/* 2636 */       if (this.length == this.methods.length) {
/* 2637 */         this.methods = ((Method[])Arrays.copyOf(this.methods, 2 * this.methods.length));
/*      */       }
/* 2639 */       this.methods[(this.length++)] = paramMethod;
/*      */     }
/*      */ 
/*      */     void addAll(Method[] paramArrayOfMethod) {
/* 2643 */       for (int i = 0; i < paramArrayOfMethod.length; i++)
/* 2644 */         add(paramArrayOfMethod[i]);
/*      */     }
/*      */ 
/*      */     void addAll(MethodArray paramMethodArray)
/*      */     {
/* 2649 */       for (int i = 0; i < paramMethodArray.length(); i++)
/* 2650 */         add(paramMethodArray.get(i));
/*      */     }
/*      */ 
/*      */     void addIfNotPresent(Method paramMethod)
/*      */     {
/* 2655 */       for (int i = 0; i < this.length; i++) {
/* 2656 */         Method localMethod = this.methods[i];
/* 2657 */         if ((localMethod == paramMethod) || ((localMethod != null) && (localMethod.equals(paramMethod)))) {
/* 2658 */           return;
/*      */         }
/*      */       }
/* 2661 */       add(paramMethod);
/*      */     }
/*      */ 
/*      */     void addAllIfNotPresent(MethodArray paramMethodArray) {
/* 2665 */       for (int i = 0; i < paramMethodArray.length(); i++) {
/* 2666 */         Method localMethod = paramMethodArray.get(i);
/* 2667 */         if (localMethod != null)
/* 2668 */           addIfNotPresent(localMethod);
/*      */       }
/*      */     }
/*      */ 
/*      */     int length()
/*      */     {
/* 2674 */       return this.length;
/*      */     }
/*      */ 
/*      */     Method get(int paramInt) {
/* 2678 */       return this.methods[paramInt];
/*      */     }
/*      */ 
/*      */     void removeByNameAndSignature(Method paramMethod) {
/* 2682 */       for (int i = 0; i < this.length; i++) {
/* 2683 */         Method localMethod = this.methods[i];
/* 2684 */         if ((localMethod != null) && (localMethod.getReturnType() == paramMethod.getReturnType()) && (localMethod.getName() == paramMethod.getName()) && (Class.arrayContentsEq(localMethod.getParameterTypes(), paramMethod.getParameterTypes())))
/*      */         {
/* 2689 */           this.methods[i] = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void compactAndTrim() {
/* 2695 */       int i = 0;
/*      */ 
/* 2697 */       for (int j = 0; j < this.length; j++) {
/* 2698 */         Method localMethod = this.methods[j];
/* 2699 */         if (localMethod != null) {
/* 2700 */           if (j != i) {
/* 2701 */             this.methods[i] = localMethod;
/*      */           }
/* 2703 */           i++;
/*      */         }
/*      */       }
/* 2706 */       if (i != this.methods.length)
/* 2707 */         this.methods = ((Method[])Arrays.copyOf(this.methods, i));
/*      */     }
/*      */ 
/*      */     Method[] getArray()
/*      */     {
/* 2712 */       return this.methods;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ReflectionData<T>
/*      */   {
/*      */     volatile Field[] declaredFields;
/*      */     volatile Field[] publicFields;
/*      */     volatile Method[] declaredMethods;
/*      */     volatile Method[] publicMethods;
/*      */     volatile Constructor<T>[] declaredConstructors;
/*      */     volatile Constructor<T>[] publicConstructors;
/*      */     volatile Field[] declaredPublicFields;
/*      */     volatile Method[] declaredPublicMethods;
/*      */     final int redefinedCount;
/*      */ 
/*      */     ReflectionData(int paramInt)
/*      */     {
/* 2406 */       this.redefinedCount = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SecurityManagerHelper
/*      */   {
/*      */     final SecurityManager sm;
/*      */     final boolean overrideCheckMemberAccess;
/*      */ 
/*      */     SecurityManagerHelper(SecurityManager paramSecurityManager)
/*      */     {
/* 2219 */       this.sm = paramSecurityManager;
/*      */ 
/* 2221 */       boolean bool = false;
/* 2222 */       if (paramSecurityManager.getClass() != SecurityManager.class) {
/*      */         try {
/* 2224 */           bool = Class.getCheckMemberAccessMethod(paramSecurityManager.getClass()).getDeclaringClass() != SecurityManager.class;
/*      */         }
/*      */         catch (NoSuchMethodError localNoSuchMethodError)
/*      */         {
/*      */         }
/*      */       }
/* 2230 */       this.overrideCheckMemberAccess = bool;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Class
 * JD-Core Version:    0.6.2
 */