/*     */ package sun.instrument;
/*     */ 
/*     */ import java.lang.instrument.ClassDefinition;
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.Instrumentation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ public class InstrumentationImpl
/*     */   implements Instrumentation
/*     */ {
/*     */   private final TransformerManager mTransformerManager;
/*     */   private TransformerManager mRetransfomableTransformerManager;
/*     */   private final long mNativeAgent;
/*     */   private final boolean mEnvironmentSupportsRedefineClasses;
/*     */   private volatile boolean mEnvironmentSupportsRetransformClassesKnown;
/*     */   private volatile boolean mEnvironmentSupportsRetransformClasses;
/*     */   private final boolean mEnvironmentSupportsNativeMethodPrefix;
/*     */ 
/*     */   private InstrumentationImpl(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  67 */     this.mTransformerManager = new TransformerManager(false);
/*  68 */     this.mRetransfomableTransformerManager = null;
/*  69 */     this.mNativeAgent = paramLong;
/*  70 */     this.mEnvironmentSupportsRedefineClasses = paramBoolean1;
/*  71 */     this.mEnvironmentSupportsRetransformClassesKnown = false;
/*  72 */     this.mEnvironmentSupportsRetransformClasses = false;
/*  73 */     this.mEnvironmentSupportsNativeMethodPrefix = paramBoolean2;
/*     */   }
/*     */ 
/*     */   public void addTransformer(ClassFileTransformer paramClassFileTransformer)
/*     */   {
/*  78 */     addTransformer(paramClassFileTransformer, false);
/*     */   }
/*     */ 
/*     */   public synchronized void addTransformer(ClassFileTransformer paramClassFileTransformer, boolean paramBoolean)
/*     */   {
/*  83 */     if (paramClassFileTransformer == null) {
/*  84 */       throw new NullPointerException("null passed as 'transformer' in addTransformer");
/*     */     }
/*  86 */     if (paramBoolean) {
/*  87 */       if (!isRetransformClassesSupported()) {
/*  88 */         throw new UnsupportedOperationException("adding retransformable transformers is not supported in this environment");
/*     */       }
/*     */ 
/*  91 */       if (this.mRetransfomableTransformerManager == null) {
/*  92 */         this.mRetransfomableTransformerManager = new TransformerManager(true);
/*     */       }
/*  94 */       this.mRetransfomableTransformerManager.addTransformer(paramClassFileTransformer);
/*  95 */       if (this.mRetransfomableTransformerManager.getTransformerCount() == 1)
/*  96 */         setHasRetransformableTransformers(this.mNativeAgent, true);
/*     */     }
/*     */     else {
/*  99 */       this.mTransformerManager.addTransformer(paramClassFileTransformer);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeTransformer(ClassFileTransformer paramClassFileTransformer)
/*     */   {
/* 105 */     if (paramClassFileTransformer == null) {
/* 106 */       throw new NullPointerException("null passed as 'transformer' in removeTransformer");
/*     */     }
/* 108 */     TransformerManager localTransformerManager = findTransformerManager(paramClassFileTransformer);
/* 109 */     if (localTransformerManager != null) {
/* 110 */       localTransformerManager.removeTransformer(paramClassFileTransformer);
/* 111 */       if ((localTransformerManager.isRetransformable()) && (localTransformerManager.getTransformerCount() == 0)) {
/* 112 */         setHasRetransformableTransformers(this.mNativeAgent, false);
/*     */       }
/* 114 */       return true;
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isModifiableClass(Class<?> paramClass)
/*     */   {
/* 121 */     if (paramClass == null) {
/* 122 */       throw new NullPointerException("null passed as 'theClass' in isModifiableClass");
/*     */     }
/*     */ 
/* 125 */     return isModifiableClass0(this.mNativeAgent, paramClass);
/*     */   }
/*     */ 
/*     */   public boolean isRetransformClassesSupported()
/*     */   {
/* 131 */     if (!this.mEnvironmentSupportsRetransformClassesKnown) {
/* 132 */       this.mEnvironmentSupportsRetransformClasses = isRetransformClassesSupported0(this.mNativeAgent);
/* 133 */       this.mEnvironmentSupportsRetransformClassesKnown = true;
/*     */     }
/* 135 */     return this.mEnvironmentSupportsRetransformClasses;
/*     */   }
/*     */ 
/*     */   public void retransformClasses(Class<?>[] paramArrayOfClass)
/*     */   {
/* 140 */     if (!isRetransformClassesSupported()) {
/* 141 */       throw new UnsupportedOperationException("retransformClasses is not supported in this environment");
/*     */     }
/*     */ 
/* 144 */     retransformClasses0(this.mNativeAgent, paramArrayOfClass);
/*     */   }
/*     */ 
/*     */   public boolean isRedefineClassesSupported()
/*     */   {
/* 149 */     return this.mEnvironmentSupportsRedefineClasses;
/*     */   }
/*     */ 
/*     */   public void redefineClasses(ClassDefinition[] paramArrayOfClassDefinition)
/*     */     throws ClassNotFoundException
/*     */   {
/* 155 */     if (!isRedefineClassesSupported()) {
/* 156 */       throw new UnsupportedOperationException("redefineClasses is not supported in this environment");
/*     */     }
/* 158 */     if (paramArrayOfClassDefinition == null) {
/* 159 */       throw new NullPointerException("null passed as 'definitions' in redefineClasses");
/*     */     }
/* 161 */     for (int i = 0; i < paramArrayOfClassDefinition.length; i++) {
/* 162 */       if (paramArrayOfClassDefinition[i] == null) {
/* 163 */         throw new NullPointerException("element of 'definitions' is null in redefineClasses");
/*     */       }
/*     */     }
/* 166 */     if (paramArrayOfClassDefinition.length == 0) {
/* 167 */       return;
/*     */     }
/*     */ 
/* 170 */     redefineClasses0(this.mNativeAgent, paramArrayOfClassDefinition);
/*     */   }
/*     */ 
/*     */   public Class[] getAllLoadedClasses()
/*     */   {
/* 175 */     return getAllLoadedClasses0(this.mNativeAgent);
/*     */   }
/*     */ 
/*     */   public Class[] getInitiatedClasses(ClassLoader paramClassLoader)
/*     */   {
/* 180 */     return getInitiatedClasses0(this.mNativeAgent, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public long getObjectSize(Object paramObject)
/*     */   {
/* 185 */     if (paramObject == null) {
/* 186 */       throw new NullPointerException("null passed as 'objectToSize' in getObjectSize");
/*     */     }
/* 188 */     return getObjectSize0(this.mNativeAgent, paramObject);
/*     */   }
/*     */ 
/*     */   public void appendToBootstrapClassLoaderSearch(JarFile paramJarFile)
/*     */   {
/* 193 */     appendToClassLoaderSearch0(this.mNativeAgent, paramJarFile.getName(), true);
/*     */   }
/*     */ 
/*     */   public void appendToSystemClassLoaderSearch(JarFile paramJarFile)
/*     */   {
/* 198 */     appendToClassLoaderSearch0(this.mNativeAgent, paramJarFile.getName(), false);
/*     */   }
/*     */ 
/*     */   public boolean isNativeMethodPrefixSupported()
/*     */   {
/* 203 */     return this.mEnvironmentSupportsNativeMethodPrefix;
/*     */   }
/*     */ 
/*     */   public synchronized void setNativeMethodPrefix(ClassFileTransformer paramClassFileTransformer, String paramString)
/*     */   {
/* 208 */     if (!isNativeMethodPrefixSupported()) {
/* 209 */       throw new UnsupportedOperationException("setNativeMethodPrefix is not supported in this environment");
/*     */     }
/*     */ 
/* 212 */     if (paramClassFileTransformer == null) {
/* 213 */       throw new NullPointerException("null passed as 'transformer' in setNativeMethodPrefix");
/*     */     }
/*     */ 
/* 216 */     TransformerManager localTransformerManager = findTransformerManager(paramClassFileTransformer);
/* 217 */     if (localTransformerManager == null) {
/* 218 */       throw new IllegalArgumentException("transformer not registered in setNativeMethodPrefix");
/*     */     }
/*     */ 
/* 221 */     localTransformerManager.setNativeMethodPrefix(paramClassFileTransformer, paramString);
/* 222 */     String[] arrayOfString = localTransformerManager.getNativeMethodPrefixes();
/* 223 */     setNativeMethodPrefixes(this.mNativeAgent, arrayOfString, localTransformerManager.isRetransformable());
/*     */   }
/*     */ 
/*     */   private TransformerManager findTransformerManager(ClassFileTransformer paramClassFileTransformer)
/*     */   {
/* 228 */     if (this.mTransformerManager.includesTransformer(paramClassFileTransformer)) {
/* 229 */       return this.mTransformerManager;
/*     */     }
/* 231 */     if ((this.mRetransfomableTransformerManager != null) && (this.mRetransfomableTransformerManager.includesTransformer(paramClassFileTransformer)))
/*     */     {
/* 233 */       return this.mRetransfomableTransformerManager;
/*     */     }
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   private native boolean isModifiableClass0(long paramLong, Class<?> paramClass);
/*     */ 
/*     */   private native boolean isRetransformClassesSupported0(long paramLong);
/*     */ 
/*     */   private native void setHasRetransformableTransformers(long paramLong, boolean paramBoolean);
/*     */ 
/*     */   private native void retransformClasses0(long paramLong, Class<?>[] paramArrayOfClass);
/*     */ 
/*     */   private native void redefineClasses0(long paramLong, ClassDefinition[] paramArrayOfClassDefinition)
/*     */     throws ClassNotFoundException;
/*     */ 
/*     */   private native Class[] getAllLoadedClasses0(long paramLong);
/*     */ 
/*     */   private native Class[] getInitiatedClasses0(long paramLong, ClassLoader paramClassLoader);
/*     */ 
/*     */   private native long getObjectSize0(long paramLong, Object paramObject);
/*     */ 
/*     */   private native void appendToClassLoaderSearch0(long paramLong, String paramString, boolean paramBoolean);
/*     */ 
/*     */   private native void setNativeMethodPrefixes(long paramLong, String[] paramArrayOfString, boolean paramBoolean);
/*     */ 
/*     */   private static void setAccessible(AccessibleObject paramAccessibleObject, final boolean paramBoolean)
/*     */   {
/* 285 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 287 */         this.val$ao.setAccessible(paramBoolean);
/* 288 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void loadClassAndStartAgent(String paramString1, String paramString2, String paramString3)
/*     */     throws Throwable
/*     */   {
/* 299 */     ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/* 300 */     Class localClass = localClassLoader.loadClass(paramString1);
/*     */ 
/* 302 */     Method localMethod = null;
/* 303 */     Object localObject = null;
/* 304 */     int i = 0;
/*     */     try
/*     */     {
/* 323 */       localMethod = localClass.getDeclaredMethod(paramString2, new Class[] { String.class, Instrumentation.class });
/*     */ 
/* 329 */       i = 1;
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException1) {
/* 332 */       localObject = localNoSuchMethodException1;
/*     */     }
/*     */ 
/* 335 */     if (localMethod == null) {
/*     */       try
/*     */       {
/* 338 */         localMethod = localClass.getDeclaredMethod(paramString2, new Class[] { String.class });
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException2)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 346 */     if (localMethod == null) {
/*     */       try
/*     */       {
/* 349 */         localMethod = localClass.getMethod(paramString2, new Class[] { String.class, Instrumentation.class });
/*     */ 
/* 355 */         i = 1;
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException3)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 362 */     if (localMethod == null) {
/*     */       try
/*     */       {
/* 365 */         localMethod = localClass.getMethod(paramString2, new Class[] { String.class });
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException4)
/*     */       {
/* 370 */         throw localObject;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 378 */     setAccessible(localMethod, true);
/*     */ 
/* 381 */     if (i != 0)
/* 382 */       localMethod.invoke(null, new Object[] { paramString3, this });
/*     */     else {
/* 384 */       localMethod.invoke(null, new Object[] { paramString3 });
/*     */     }
/*     */ 
/* 388 */     setAccessible(localMethod, false);
/*     */   }
/*     */ 
/*     */   private void loadClassAndCallPremain(String paramString1, String paramString2)
/*     */     throws Throwable
/*     */   {
/* 397 */     loadClassAndStartAgent(paramString1, "premain", paramString2);
/*     */   }
/*     */ 
/*     */   private void loadClassAndCallAgentmain(String paramString1, String paramString2)
/*     */     throws Throwable
/*     */   {
/* 407 */     loadClassAndStartAgent(paramString1, "agentmain", paramString2);
/*     */   }
/*     */ 
/*     */   private byte[] transform(ClassLoader paramClassLoader, String paramString, Class paramClass, ProtectionDomain paramProtectionDomain, byte[] paramArrayOfByte, boolean paramBoolean)
/*     */   {
/* 418 */     TransformerManager localTransformerManager = paramBoolean ? this.mRetransfomableTransformerManager : this.mTransformerManager;
/*     */ 
/* 421 */     if (localTransformerManager == null) {
/* 422 */       return null;
/*     */     }
/* 424 */     return localTransformerManager.transform(paramClassLoader, paramString, paramClass, paramProtectionDomain, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 274 */     System.loadLibrary("instrument");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.instrument.InstrumentationImpl
 * JD-Core Version:    0.6.2
 */