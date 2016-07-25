/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.SecureClassLoader;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ public abstract class SecureCaller
/*     */ {
/*  57 */   private static final byte[] secureCallerImplBytecode = loadBytecode();
/*     */ 
/*  64 */   private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers = new WeakHashMap();
/*     */ 
/*     */   public abstract Object call(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
/*     */ 
/*     */   static Object callSecurely(final CodeSource paramCodeSource, Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/*  77 */     Thread localThread = Thread.currentThread();
/*     */ 
/*  80 */     ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  83 */         return this.val$thread.getContextClassLoader();
/*     */       }
/*     */     });
/*     */     Object localObject1;
/*  87 */     synchronized (callers)
/*     */     {
/*  89 */       localObject1 = (Map)callers.get(paramCodeSource);
/*  90 */       if (localObject1 == null)
/*     */       {
/*  92 */         localObject1 = new WeakHashMap();
/*  93 */         callers.put(paramCodeSource, localObject1);
/*     */       }
/*     */     }
/*     */ 
/*  97 */     synchronized (localObject1)
/*     */     {
/*  99 */       SoftReference localSoftReference = (SoftReference)((Map)localObject1).get(localClassLoader);
/* 100 */       if (localSoftReference != null)
/* 101 */         ??? = (SecureCaller)localSoftReference.get();
/*     */       else {
/* 103 */         ??? = null;
/*     */       }
/* 105 */       if (??? == null)
/*     */       {
/*     */         try
/*     */         {
/* 110 */           ??? = (SecureCaller)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Object run()
/*     */               throws Exception
/*     */             {
/* 116 */               Class localClass1 = getClass();
/*     */               ClassLoader localClassLoader;
/* 117 */               if (this.val$classLoader.loadClass(localClass1.getName()) != localClass1)
/* 118 */                 localClassLoader = localClass1.getClassLoader();
/*     */               else {
/* 120 */                 localClassLoader = this.val$classLoader;
/*     */               }
/* 122 */               SecureCaller.SecureClassLoaderImpl localSecureClassLoaderImpl = new SecureCaller.SecureClassLoaderImpl(localClassLoader);
/*     */ 
/* 124 */               Class localClass2 = localSecureClassLoaderImpl.defineAndLinkClass(SecureCaller.class.getName() + "Impl", SecureCaller.secureCallerImplBytecode, paramCodeSource);
/*     */ 
/* 127 */               return localClass2.newInstance();
/*     */             }
/*     */           });
/* 130 */           ((Map)localObject1).put(localClassLoader, new SoftReference(???));
/*     */         }
/*     */         catch (PrivilegedActionException localPrivilegedActionException)
/*     */         {
/* 134 */           throw new UndeclaredThrowableException(localPrivilegedActionException.getCause());
/*     */         }
/*     */       }
/*     */     }
/* 138 */     return ((SecureCaller)???).call(paramCallable, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private static byte[] loadBytecode()
/*     */   {
/* 158 */     return (byte[])AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 162 */         return SecureCaller.access$100();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static byte[] loadBytecodePrivileged()
/*     */   {
/* 169 */     URL localURL = SecureCaller.class.getResource("SecureCallerImpl.clazz");
/*     */     try
/*     */     {
/* 172 */       InputStream localInputStream = localURL.openStream();
/*     */       try
/*     */       {
/* 175 */         ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*     */         while (true)
/*     */         {
/* 178 */           int i = localInputStream.read();
/* 179 */           if (i == -1)
/*     */           {
/* 181 */             return localByteArrayOutputStream.toByteArray();
/*     */           }
/* 183 */           localByteArrayOutputStream.write(i);
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 188 */         localInputStream.close();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 193 */       throw new UndeclaredThrowableException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SecureClassLoaderImpl extends SecureClassLoader
/*     */   {
/*     */     SecureClassLoaderImpl(ClassLoader paramClassLoader)
/*     */     {
/* 145 */       super();
/*     */     }
/*     */ 
/*     */     Class<?> defineAndLinkClass(String paramString, byte[] paramArrayOfByte, CodeSource paramCodeSource)
/*     */     {
/* 150 */       Class localClass = defineClass(paramString, paramArrayOfByte, 0, paramArrayOfByte.length, paramCodeSource);
/* 151 */       resolveClass(localClass);
/* 152 */       return localClass;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.SecureCaller
 * JD-Core Version:    0.6.2
 */