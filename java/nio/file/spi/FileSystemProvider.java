/*     */ package java.nio.file.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.DirectoryStream.Filter;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ 
/*     */ public abstract class FileSystemProvider
/*     */ {
/*  79 */   private static final Object lock = new Object();
/*     */   private static volatile List<FileSystemProvider> installedProviders;
/*  85 */   private static boolean loadingProviders = false;
/*     */ 
/*     */   private static Void checkPermission() {
/*  88 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  89 */     if (localSecurityManager != null)
/*  90 */       localSecurityManager.checkPermission(new RuntimePermission("fileSystemProvider"));
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   private FileSystemProvider(Void paramVoid)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected FileSystemProvider()
/*     */   {
/* 108 */     this(checkPermission());
/*     */   }
/*     */ 
/*     */   private static List<FileSystemProvider> loadInstalledProviders()
/*     */   {
/* 113 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 115 */     ServiceLoader localServiceLoader = ServiceLoader.load(FileSystemProvider.class, ClassLoader.getSystemClassLoader());
/*     */ 
/* 119 */     for (FileSystemProvider localFileSystemProvider1 : localServiceLoader) {
/* 120 */       String str = localFileSystemProvider1.getScheme();
/*     */ 
/* 123 */       if (!str.equalsIgnoreCase("file")) {
/* 124 */         int i = 0;
/* 125 */         for (FileSystemProvider localFileSystemProvider2 : localArrayList) {
/* 126 */           if (localFileSystemProvider2.getScheme().equalsIgnoreCase(str)) {
/* 127 */             i = 1;
/* 128 */             break;
/*     */           }
/*     */         }
/* 131 */         if (i == 0) {
/* 132 */           localArrayList.add(localFileSystemProvider1);
/*     */         }
/*     */       }
/*     */     }
/* 136 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static List<FileSystemProvider> installedProviders()
/*     */   {
/* 154 */     if (installedProviders == null)
/*     */     {
/* 156 */       FileSystemProvider localFileSystemProvider = FileSystems.getDefault().provider();
/*     */ 
/* 158 */       synchronized (lock) {
/* 159 */         if (installedProviders == null) {
/* 160 */           if (loadingProviders) {
/* 161 */             throw new Error("Circular loading of installed providers detected");
/*     */           }
/* 163 */           loadingProviders = true;
/*     */ 
/* 165 */           List localList = (List)AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public List<FileSystemProvider> run()
/*     */             {
/* 169 */               return FileSystemProvider.access$000();
/*     */             }
/*     */           });
/* 173 */           localList.add(0, localFileSystemProvider);
/*     */ 
/* 175 */           installedProviders = Collections.unmodifiableList(localList);
/*     */         }
/*     */       }
/*     */     }
/* 179 */     return installedProviders;
/*     */   }
/*     */ 
/*     */   public abstract String getScheme();
/*     */ 
/*     */   public abstract FileSystem newFileSystem(URI paramURI, Map<String, ?> paramMap)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract FileSystem getFileSystem(URI paramURI);
/*     */ 
/*     */   public abstract Path getPath(URI paramURI);
/*     */ 
/*     */   public FileSystem newFileSystem(Path paramPath, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 340 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public InputStream newInputStream(Path paramPath, OpenOption[] paramArrayOfOpenOption)
/*     */     throws IOException
/*     */   {
/* 374 */     if (paramArrayOfOpenOption.length > 0) {
/* 375 */       for (OpenOption localOpenOption : paramArrayOfOpenOption) {
/* 376 */         if (localOpenOption != StandardOpenOption.READ)
/* 377 */           throw new UnsupportedOperationException("'" + localOpenOption + "' not allowed");
/*     */       }
/*     */     }
/* 380 */     return Channels.newInputStream(Files.newByteChannel(paramPath, new OpenOption[0]));
/*     */   }
/*     */ 
/*     */   public OutputStream newOutputStream(Path paramPath, OpenOption[] paramArrayOfOpenOption)
/*     */     throws IOException
/*     */   {
/* 417 */     int i = paramArrayOfOpenOption.length;
/* 418 */     HashSet localHashSet = new HashSet(i + 3);
/* 419 */     if (i == 0) {
/* 420 */       localHashSet.add(StandardOpenOption.CREATE);
/* 421 */       localHashSet.add(StandardOpenOption.TRUNCATE_EXISTING);
/*     */     } else {
/* 423 */       for (OpenOption localOpenOption : paramArrayOfOpenOption) {
/* 424 */         if (localOpenOption == StandardOpenOption.READ)
/* 425 */           throw new IllegalArgumentException("READ not allowed");
/* 426 */         localHashSet.add(localOpenOption);
/*     */       }
/*     */     }
/* 429 */     localHashSet.add(StandardOpenOption.WRITE);
/* 430 */     return Channels.newOutputStream(newByteChannel(paramPath, localHashSet, new FileAttribute[0]));
/*     */   }
/*     */ 
/*     */   public FileChannel newFileChannel(Path paramPath, Set<? extends OpenOption> paramSet, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 472 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public AsynchronousFileChannel newAsynchronousFileChannel(Path paramPath, Set<? extends OpenOption> paramSet, ExecutorService paramExecutorService, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 521 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract SeekableByteChannel newByteChannel(Path paramPath, Set<? extends OpenOption> paramSet, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract DirectoryStream<Path> newDirectoryStream(Path paramPath, DirectoryStream.Filter<? super Path> paramFilter)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void createDirectory(Path paramPath, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException;
/*     */ 
/*     */   public void createSymbolicLink(Path paramPath1, Path paramPath2, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 650 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void createLink(Path paramPath1, Path paramPath2)
/*     */     throws IOException
/*     */   {
/* 682 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract void delete(Path paramPath)
/*     */     throws IOException;
/*     */ 
/*     */   public boolean deleteIfExists(Path paramPath)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 735 */       delete(paramPath);
/* 736 */       return true; } catch (NoSuchFileException localNoSuchFileException) {
/*     */     }
/* 738 */     return false;
/*     */   }
/*     */ 
/*     */   public Path readSymbolicLink(Path paramPath)
/*     */     throws IOException
/*     */   {
/* 765 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract void copy(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void move(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean isSameFile(Path paramPath1, Path paramPath2)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean isHidden(Path paramPath)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract FileStore getFileStore(Path paramPath)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void checkAccess(Path paramPath, AccessMode[] paramArrayOfAccessMode)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <V extends FileAttributeView> V getFileAttributeView(Path paramPath, Class<V> paramClass, LinkOption[] paramArrayOfLinkOption);
/*     */ 
/*     */   public abstract <A extends BasicFileAttributes> A readAttributes(Path paramPath, Class<A> paramClass, LinkOption[] paramArrayOfLinkOption)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Map<String, Object> readAttributes(Path paramPath, String paramString, LinkOption[] paramArrayOfLinkOption)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void setAttribute(Path paramPath, String paramString, Object paramObject, LinkOption[] paramArrayOfLinkOption)
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.spi.FileSystemProvider
 * JD-Core Version:    0.6.2
 */