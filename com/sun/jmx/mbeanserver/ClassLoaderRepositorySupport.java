/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.MBeanPermission;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.loading.PrivateClassLoader;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ final class ClassLoaderRepositorySupport
/*     */   implements ModifiableClassLoaderRepository
/*     */ {
/*  69 */   private static final LoaderEntry[] EMPTY_LOADER_ARRAY = new LoaderEntry[0];
/*     */ 
/*  81 */   private LoaderEntry[] loaders = EMPTY_LOADER_ARRAY;
/*     */ 
/* 132 */   private final Map<String, List<ClassLoader>> search = new Hashtable(10);
/*     */ 
/* 138 */   private final Map<ObjectName, ClassLoader> loadersWithNames = new Hashtable(10);
/*     */ 
/*     */   private synchronized boolean add(ObjectName paramObjectName, ClassLoader paramClassLoader)
/*     */   {
/*  89 */     ArrayList localArrayList = new ArrayList(Arrays.asList(this.loaders));
/*     */ 
/*  91 */     localArrayList.add(new LoaderEntry(paramObjectName, paramClassLoader));
/*  92 */     this.loaders = ((LoaderEntry[])localArrayList.toArray(EMPTY_LOADER_ARRAY));
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   private synchronized boolean remove(ObjectName paramObjectName, ClassLoader paramClassLoader)
/*     */   {
/* 109 */     int i = this.loaders.length;
/* 110 */     for (int j = 0; j < i; j++) {
/* 111 */       LoaderEntry localLoaderEntry = this.loaders[j];
/* 112 */       boolean bool = paramObjectName == null ? false : paramClassLoader == localLoaderEntry.loader ? true : paramObjectName.equals(localLoaderEntry.name);
/*     */ 
/* 116 */       if (bool) {
/* 117 */         LoaderEntry[] arrayOfLoaderEntry = new LoaderEntry[i - 1];
/* 118 */         System.arraycopy(this.loaders, 0, arrayOfLoaderEntry, 0, j);
/* 119 */         System.arraycopy(this.loaders, j + 1, arrayOfLoaderEntry, j, i - 1 - j);
/*     */ 
/* 121 */         this.loaders = arrayOfLoaderEntry;
/* 122 */         return true;
/*     */       }
/*     */     }
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   public final Class<?> loadClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 144 */     return loadClass(this.loaders, paramString, null, null);
/*     */   }
/*     */ 
/*     */   public final Class<?> loadClassWithout(ClassLoader paramClassLoader, String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 151 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 152 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "loadClassWithout", paramString + " without " + paramClassLoader);
/*     */     }
/*     */ 
/* 159 */     if (paramClassLoader == null) {
/* 160 */       return loadClass(this.loaders, paramString, null, null);
/*     */     }
/*     */ 
/* 164 */     startValidSearch(paramClassLoader, paramString);
/*     */     try {
/* 166 */       return loadClass(this.loaders, paramString, paramClassLoader, null);
/*     */     } finally {
/* 168 */       stopValidSearch(paramClassLoader, paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Class<?> loadClassBefore(ClassLoader paramClassLoader, String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 175 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 176 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "loadClassBefore", paramString + " before " + paramClassLoader);
/*     */     }
/*     */ 
/* 181 */     if (paramClassLoader == null) {
/* 182 */       return loadClass(this.loaders, paramString, null, null);
/*     */     }
/* 184 */     startValidSearch(paramClassLoader, paramString);
/*     */     try {
/* 186 */       return loadClass(this.loaders, paramString, null, paramClassLoader);
/*     */     } finally {
/* 188 */       stopValidSearch(paramClassLoader, paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Class<?> loadClass(LoaderEntry[] paramArrayOfLoaderEntry, String paramString, ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*     */     throws ClassNotFoundException
/*     */   {
/* 198 */     ReflectUtil.checkPackageAccess(paramString);
/* 199 */     int i = paramArrayOfLoaderEntry.length;
/* 200 */     for (int j = 0; j < i; j++)
/*     */       try {
/* 202 */         ClassLoader localClassLoader = paramArrayOfLoaderEntry[j].loader;
/* 203 */         if (localClassLoader == null)
/* 204 */           return Class.forName(paramString, false, null);
/* 205 */         if (localClassLoader != paramClassLoader1)
/*     */         {
/* 207 */           if (localClassLoader == paramClassLoader2)
/*     */             break;
/* 209 */           if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 210 */             JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "loadClass", "Trying loader = " + localClassLoader);
/*     */           }
/*     */ 
/* 226 */           return Class.forName(paramString, false, localClassLoader);
/*     */         }
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException)
/*     */       {
/*     */       }
/* 232 */     throw new ClassNotFoundException(paramString);
/*     */   }
/*     */ 
/*     */   private synchronized void startValidSearch(ClassLoader paramClassLoader, String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 240 */     Object localObject = (List)this.search.get(paramString);
/* 241 */     if ((localObject != null) && (((List)localObject).contains(paramClassLoader))) {
/* 242 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 243 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "startValidSearch", "Already requested loader = " + paramClassLoader + " class = " + paramString);
/*     */       }
/*     */ 
/* 248 */       throw new ClassNotFoundException(paramString);
/*     */     }
/*     */ 
/* 253 */     if (localObject == null) {
/* 254 */       localObject = new ArrayList(1);
/* 255 */       this.search.put(paramString, localObject);
/*     */     }
/* 257 */     ((List)localObject).add(paramClassLoader);
/* 258 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER))
/* 259 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "startValidSearch", "loader = " + paramClassLoader + " class = " + paramString);
/*     */   }
/*     */ 
/*     */   private synchronized void stopValidSearch(ClassLoader paramClassLoader, String paramString)
/*     */   {
/* 271 */     List localList = (List)this.search.get(paramString);
/* 272 */     if (localList != null) {
/* 273 */       localList.remove(paramClassLoader);
/* 274 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER))
/* 275 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, ClassLoaderRepositorySupport.class.getName(), "stopValidSearch", "loader = " + paramClassLoader + " class = " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void addClassLoader(ClassLoader paramClassLoader)
/*     */   {
/* 284 */     add(null, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public final void removeClassLoader(ClassLoader paramClassLoader) {
/* 288 */     remove(null, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public final synchronized void addClassLoader(ObjectName paramObjectName, ClassLoader paramClassLoader)
/*     */   {
/* 293 */     this.loadersWithNames.put(paramObjectName, paramClassLoader);
/* 294 */     if (!(paramClassLoader instanceof PrivateClassLoader))
/* 295 */       add(paramObjectName, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public final synchronized void removeClassLoader(ObjectName paramObjectName) {
/* 299 */     ClassLoader localClassLoader = (ClassLoader)this.loadersWithNames.remove(paramObjectName);
/* 300 */     if (!(localClassLoader instanceof PrivateClassLoader))
/* 301 */       remove(paramObjectName, localClassLoader);
/*     */   }
/*     */ 
/*     */   public final ClassLoader getClassLoader(ObjectName paramObjectName) {
/* 305 */     ClassLoader localClassLoader = (ClassLoader)this.loadersWithNames.get(paramObjectName);
/* 306 */     if (localClassLoader != null) {
/* 307 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 308 */       if (localSecurityManager != null) {
/* 309 */         MBeanPermission localMBeanPermission = new MBeanPermission(localClassLoader.getClass().getName(), null, paramObjectName, "getClassLoader");
/*     */ 
/* 314 */         localSecurityManager.checkPermission(localMBeanPermission);
/*     */       }
/*     */     }
/* 317 */     return localClassLoader;
/*     */   }
/*     */ 
/*     */   private static class LoaderEntry
/*     */   {
/*     */     ObjectName name;
/*     */     ClassLoader loader;
/*     */ 
/*     */     LoaderEntry(ObjectName paramObjectName, ClassLoader paramClassLoader)
/*     */     {
/*  64 */       this.name = paramObjectName;
/*  65 */       this.loader = paramClassLoader;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.ClassLoaderRepositorySupport
 * JD-Core Version:    0.6.2
 */