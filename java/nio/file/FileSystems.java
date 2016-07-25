/*     */ package java.nio.file;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URI;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import sun.nio.fs.DefaultFileSystemProvider;
/*     */ 
/*     */ public final class FileSystems
/*     */ {
/*     */   public static FileSystem getDefault()
/*     */   {
/* 176 */     return DefaultFileSystemHolder.defaultFileSystem;
/*     */   }
/*     */ 
/*     */   public static FileSystem getFileSystem(URI paramURI)
/*     */   {
/* 214 */     String str = paramURI.getScheme();
/* 215 */     for (FileSystemProvider localFileSystemProvider : FileSystemProvider.installedProviders()) {
/* 216 */       if (str.equalsIgnoreCase(localFileSystemProvider.getScheme())) {
/* 217 */         return localFileSystemProvider.getFileSystem(paramURI);
/*     */       }
/*     */     }
/* 220 */     throw new ProviderNotFoundException("Provider \"" + str + "\" not found");
/*     */   }
/*     */ 
/*     */   public static FileSystem newFileSystem(URI paramURI, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 272 */     return newFileSystem(paramURI, paramMap, null);
/*     */   }
/*     */ 
/*     */   public static FileSystem newFileSystem(URI paramURI, Map<String, ?> paramMap, ClassLoader paramClassLoader)
/*     */     throws IOException
/*     */   {
/* 317 */     String str = paramURI.getScheme();
/*     */ 
/* 320 */     for (Object localObject1 = FileSystemProvider.installedProviders().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (FileSystemProvider)((Iterator)localObject1).next();
/* 321 */       if (str.equalsIgnoreCase(((FileSystemProvider)localObject2).getScheme()))
/* 322 */         return ((FileSystemProvider)localObject2).newFileSystem(paramURI, paramMap);
/*     */     }
/*     */     Object localObject2;
/* 327 */     if (paramClassLoader != null) {
/* 328 */       localObject1 = ServiceLoader.load(FileSystemProvider.class, paramClassLoader);
/*     */ 
/* 330 */       for (localObject2 = ((ServiceLoader)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { FileSystemProvider localFileSystemProvider = (FileSystemProvider)((Iterator)localObject2).next();
/* 331 */         if (str.equalsIgnoreCase(localFileSystemProvider.getScheme())) {
/* 332 */           return localFileSystemProvider.newFileSystem(paramURI, paramMap);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 337 */     throw new ProviderNotFoundException("Provider \"" + str + "\" not found");
/*     */   }
/*     */ 
/*     */   public static FileSystem newFileSystem(Path paramPath, ClassLoader paramClassLoader)
/*     */     throws IOException
/*     */   {
/* 379 */     if (paramPath == null)
/* 380 */       throw new NullPointerException();
/* 381 */     Map localMap = Collections.emptyMap();
/*     */ 
/* 384 */     for (Object localObject1 = FileSystemProvider.installedProviders().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (FileSystemProvider)((Iterator)localObject1).next();
/*     */       try {
/* 386 */         return ((FileSystemProvider)localObject2).newFileSystem(paramPath, localMap);
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException1)
/*     */       {
/*     */       }
/*     */     }
/*     */     Object localObject2;
/* 392 */     if (paramClassLoader != null) {
/* 393 */       localObject1 = ServiceLoader.load(FileSystemProvider.class, paramClassLoader);
/*     */ 
/* 395 */       for (localObject2 = ((ServiceLoader)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { FileSystemProvider localFileSystemProvider = (FileSystemProvider)((Iterator)localObject2).next();
/*     */         try {
/* 397 */           return localFileSystemProvider.newFileSystem(paramPath, localMap);
/*     */         }
/*     */         catch (UnsupportedOperationException localUnsupportedOperationException2)
/*     */         {
/*     */         } }
/*     */     }
/* 403 */     throw new ProviderNotFoundException("Provider not found");
/*     */   }
/*     */ 
/*     */   private static class DefaultFileSystemHolder
/*     */   {
/*  90 */     static final FileSystem defaultFileSystem = defaultFileSystem();
/*     */ 
/*     */     private static FileSystem defaultFileSystem()
/*     */     {
/*  95 */       FileSystemProvider localFileSystemProvider = (FileSystemProvider)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public FileSystemProvider run() {
/*  98 */           return FileSystems.DefaultFileSystemHolder.access$000();
/*     */         }
/*     */       });
/* 103 */       return localFileSystemProvider.getFileSystem(URI.create("file:///"));
/*     */     }
/*     */ 
/*     */     private static FileSystemProvider getDefaultProvider()
/*     */     {
/* 108 */       FileSystemProvider localFileSystemProvider = DefaultFileSystemProvider.create();
/*     */ 
/* 112 */       String str1 = System.getProperty("java.nio.file.spi.DefaultFileSystemProvider");
/*     */ 
/* 114 */       if (str1 != null) {
/* 115 */         for (String str2 : str1.split(",")) {
/*     */           try {
/* 117 */             Class localClass = Class.forName(str2, true, ClassLoader.getSystemClassLoader());
/*     */ 
/* 119 */             Constructor localConstructor = localClass.getDeclaredConstructor(new Class[] { FileSystemProvider.class });
/*     */ 
/* 121 */             localFileSystemProvider = (FileSystemProvider)localConstructor.newInstance(new Object[] { localFileSystemProvider });
/*     */ 
/* 124 */             if (!localFileSystemProvider.getScheme().equals("file"))
/* 125 */               throw new Error("Default provider must use scheme 'file'");
/*     */           }
/*     */           catch (Exception localException) {
/* 128 */             throw new Error(localException);
/*     */           }
/*     */         }
/*     */       }
/* 132 */       return localFileSystemProvider;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.FileSystems
 * JD-Core Version:    0.6.2
 */