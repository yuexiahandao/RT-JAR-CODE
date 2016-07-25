/*      */ package java.nio.file;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.nio.channels.Channels;
/*      */ import java.nio.channels.SeekableByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.file.attribute.BasicFileAttributeView;
/*      */ import java.nio.file.attribute.BasicFileAttributes;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.nio.file.attribute.FileAttributeView;
/*      */ import java.nio.file.attribute.FileOwnerAttributeView;
/*      */ import java.nio.file.attribute.FileTime;
/*      */ import java.nio.file.attribute.PosixFileAttributeView;
/*      */ import java.nio.file.attribute.PosixFileAttributes;
/*      */ import java.nio.file.attribute.PosixFilePermission;
/*      */ import java.nio.file.attribute.UserPrincipal;
/*      */ import java.nio.file.spi.FileSystemProvider;
/*      */ import java.nio.file.spi.FileTypeDetector;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.ServiceLoader;
/*      */ import java.util.Set;
/*      */ import sun.nio.fs.DefaultFileTypeDetector;
/*      */ 
/*      */ public final class Files
/*      */ {
/*      */   private static final int BUFFER_SIZE = 8192;
/*      */   private static final int MAX_BUFFER_SIZE = 2147483639;
/*      */ 
/*      */   private static FileSystemProvider provider(Path paramPath)
/*      */   {
/*   67 */     return paramPath.getFileSystem().provider();
/*      */   }
/*      */ 
/*      */   public static InputStream newInputStream(Path paramPath, OpenOption[] paramArrayOfOpenOption)
/*      */     throws IOException
/*      */   {
/*  108 */     return provider(paramPath).newInputStream(paramPath, paramArrayOfOpenOption);
/*      */   }
/*      */ 
/*      */   public static OutputStream newOutputStream(Path paramPath, OpenOption[] paramArrayOfOpenOption)
/*      */     throws IOException
/*      */   {
/*  172 */     return provider(paramPath).newOutputStream(paramPath, paramArrayOfOpenOption);
/*      */   }
/*      */ 
/*      */   public static SeekableByteChannel newByteChannel(Path paramPath, Set<? extends OpenOption> paramSet, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  317 */     return provider(paramPath).newByteChannel(paramPath, paramSet, paramArrayOfFileAttribute);
/*      */   }
/*      */ 
/*      */   public static SeekableByteChannel newByteChannel(Path paramPath, OpenOption[] paramArrayOfOpenOption)
/*      */     throws IOException
/*      */   {
/*  361 */     HashSet localHashSet = new HashSet(paramArrayOfOpenOption.length);
/*  362 */     Collections.addAll(localHashSet, paramArrayOfOpenOption);
/*  363 */     return newByteChannel(paramPath, localHashSet, new FileAttribute[0]);
/*      */   }
/*      */ 
/*      */   public static DirectoryStream<Path> newDirectoryStream(Path paramPath)
/*      */     throws IOException
/*      */   {
/*  413 */     return provider(paramPath).newDirectoryStream(paramPath, AcceptAllFilter.FILTER);
/*      */   }
/*      */ 
/*      */   public static DirectoryStream<Path> newDirectoryStream(Path paramPath, String paramString)
/*      */     throws IOException
/*      */   {
/*  469 */     if (paramString.equals("*")) {
/*  470 */       return newDirectoryStream(paramPath);
/*      */     }
/*      */ 
/*  473 */     FileSystem localFileSystem = paramPath.getFileSystem();
/*  474 */     PathMatcher localPathMatcher = localFileSystem.getPathMatcher("glob:" + paramString);
/*  475 */     DirectoryStream.Filter local1 = new DirectoryStream.Filter()
/*      */     {
/*      */       public boolean accept(Path paramAnonymousPath) {
/*  478 */         return this.val$matcher.matches(paramAnonymousPath.getFileName());
/*      */       }
/*      */     };
/*  481 */     return localFileSystem.provider().newDirectoryStream(paramPath, local1);
/*      */   }
/*      */ 
/*      */   public static DirectoryStream<Path> newDirectoryStream(Path paramPath, DirectoryStream.Filter<? super Path> paramFilter)
/*      */     throws IOException
/*      */   {
/*  545 */     return provider(paramPath).newDirectoryStream(paramPath, paramFilter);
/*      */   }
/*      */ 
/*      */   public static Path createFile(Path paramPath, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  586 */     EnumSet localEnumSet = EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
/*      */ 
/*  588 */     newByteChannel(paramPath, localEnumSet, paramArrayOfFileAttribute).close();
/*  589 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static Path createDirectory(Path paramPath, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  630 */     provider(paramPath).createDirectory(paramPath, paramArrayOfFileAttribute);
/*  631 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static Path createDirectories(Path paramPath, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*      */     Path localPath2;
/*      */     try
/*      */     {
/*  683 */       createAndCheckIsDirectory(paramPath, paramArrayOfFileAttribute);
/*  684 */       return paramPath;
/*      */     }
/*      */     catch (FileAlreadyExistsException localFileAlreadyExistsException) {
/*  687 */       throw localFileAlreadyExistsException;
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  691 */       Object localObject = null;
/*      */       try {
/*  693 */         paramPath = paramPath.toAbsolutePath();
/*      */       }
/*      */       catch (SecurityException localSecurityException) {
/*  696 */         localObject = localSecurityException;
/*      */       }
/*      */ 
/*  699 */       Path localPath1 = paramPath.getParent();
/*  700 */       while (localPath1 != null) {
/*      */         try {
/*  702 */           provider(localPath1).checkAccess(localPath1, new AccessMode[0]);
/*      */         }
/*      */         catch (NoSuchFileException localNoSuchFileException)
/*      */         {
/*      */         }
/*  707 */         localPath1 = localPath1.getParent();
/*      */       }
/*  709 */       if (localPath1 == null)
/*      */       {
/*  711 */         if (localObject != null)
/*  712 */           throw localObject;
/*  713 */         throw new IOException("Root directory does not exist");
/*      */       }
/*      */ 
/*  717 */       localPath2 = localPath1;
/*  718 */       for (Path localPath3 : localPath1.relativize(paramPath)) {
/*  719 */         localPath2 = localPath2.resolve(localPath3);
/*  720 */         createAndCheckIsDirectory(localPath2, paramArrayOfFileAttribute);
/*      */       }
/*      */     }
/*  722 */     return paramPath;
/*      */   }
/*      */ 
/*      */   private static void createAndCheckIsDirectory(Path paramPath, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  734 */       createDirectory(paramPath, paramArrayOfFileAttribute);
/*      */     } catch (FileAlreadyExistsException localFileAlreadyExistsException) {
/*  736 */       if (!isDirectory(paramPath, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }))
/*  737 */         throw localFileAlreadyExistsException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Path createTempFile(Path paramPath, String paramString1, String paramString2, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  805 */     return TempFileHelper.createTempFile((Path)Objects.requireNonNull(paramPath), paramString1, paramString2, paramArrayOfFileAttribute);
/*      */   }
/*      */ 
/*      */   public static Path createTempFile(String paramString1, String paramString2, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  850 */     return TempFileHelper.createTempFile(null, paramString1, paramString2, paramArrayOfFileAttribute);
/*      */   }
/*      */ 
/*      */   public static Path createTempDirectory(Path paramPath, String paramString, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  903 */     return TempFileHelper.createTempDirectory((Path)Objects.requireNonNull(paramPath), paramString, paramArrayOfFileAttribute);
/*      */   }
/*      */ 
/*      */   public static Path createTempDirectory(String paramString, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  944 */     return TempFileHelper.createTempDirectory(null, paramString, paramArrayOfFileAttribute);
/*      */   }
/*      */ 
/*      */   public static Path createSymbolicLink(Path paramPath1, Path paramPath2, FileAttribute<?>[] paramArrayOfFileAttribute)
/*      */     throws IOException
/*      */   {
/*  996 */     provider(paramPath1).createSymbolicLink(paramPath1, paramPath2, paramArrayOfFileAttribute);
/*  997 */     return paramPath1;
/*      */   }
/*      */ 
/*      */   public static Path createLink(Path paramPath1, Path paramPath2)
/*      */     throws IOException
/*      */   {
/* 1039 */     provider(paramPath1).createLink(paramPath1, paramPath2);
/* 1040 */     return paramPath1;
/*      */   }
/*      */ 
/*      */   public static void delete(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 1079 */     provider(paramPath).delete(paramPath);
/*      */   }
/*      */ 
/*      */   public static boolean deleteIfExists(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 1118 */     return provider(paramPath).deleteIfExists(paramPath);
/*      */   }
/*      */ 
/*      */   public static Path copy(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*      */     throws IOException
/*      */   {
/* 1224 */     FileSystemProvider localFileSystemProvider = provider(paramPath1);
/* 1225 */     if (provider(paramPath2) == localFileSystemProvider)
/*      */     {
/* 1227 */       localFileSystemProvider.copy(paramPath1, paramPath2, paramArrayOfCopyOption);
/*      */     }
/*      */     else {
/* 1230 */       CopyMoveHelper.copyToForeignTarget(paramPath1, paramPath2, paramArrayOfCopyOption);
/*      */     }
/* 1232 */     return paramPath2;
/*      */   }
/*      */ 
/*      */   public static Path move(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*      */     throws IOException
/*      */   {
/* 1344 */     FileSystemProvider localFileSystemProvider = provider(paramPath1);
/* 1345 */     if (provider(paramPath2) == localFileSystemProvider)
/*      */     {
/* 1347 */       localFileSystemProvider.move(paramPath1, paramPath2, paramArrayOfCopyOption);
/*      */     }
/*      */     else {
/* 1350 */       CopyMoveHelper.moveToForeignTarget(paramPath1, paramPath2, paramArrayOfCopyOption);
/*      */     }
/* 1352 */     return paramPath2;
/*      */   }
/*      */ 
/*      */   public static Path readSymbolicLink(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 1384 */     return provider(paramPath).readSymbolicLink(paramPath);
/*      */   }
/*      */ 
/*      */   public static FileStore getFileStore(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 1413 */     return provider(paramPath).getFileStore(paramPath);
/*      */   }
/*      */ 
/*      */   public static boolean isSameFile(Path paramPath1, Path paramPath2)
/*      */     throws IOException
/*      */   {
/* 1456 */     return provider(paramPath1).isSameFile(paramPath1, paramPath2);
/*      */   }
/*      */ 
/*      */   public static boolean isHidden(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 1483 */     return provider(paramPath).isHidden(paramPath);
/*      */   }
/*      */ 
/*      */   public static String probeContentType(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 1569 */     for (FileTypeDetector localFileTypeDetector : FileTypeDetectors.installeDetectors) {
/* 1570 */       String str = localFileTypeDetector.probeContentType(paramPath);
/* 1571 */       if (str != null) {
/* 1572 */         return str;
/*      */       }
/*      */     }
/*      */ 
/* 1576 */     return FileTypeDetectors.defaultFileTypeDetector.probeContentType(paramPath);
/*      */   }
/*      */ 
/*      */   public static <V extends FileAttributeView> V getFileAttributeView(Path paramPath, Class<V> paramClass, LinkOption[] paramArrayOfLinkOption)
/*      */   {
/* 1626 */     return provider(paramPath).getFileAttributeView(paramPath, paramClass, paramArrayOfLinkOption);
/*      */   }
/*      */ 
/*      */   public static <A extends BasicFileAttributes> A readAttributes(Path paramPath, Class<A> paramClass, LinkOption[] paramArrayOfLinkOption)
/*      */     throws IOException
/*      */   {
/* 1686 */     return provider(paramPath).readAttributes(paramPath, paramClass, paramArrayOfLinkOption);
/*      */   }
/*      */ 
/*      */   public static Path setAttribute(Path paramPath, String paramString, Object paramObject, LinkOption[] paramArrayOfLinkOption)
/*      */     throws IOException
/*      */   {
/* 1754 */     provider(paramPath).setAttribute(paramPath, paramString, paramObject, paramArrayOfLinkOption);
/* 1755 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static Object getAttribute(Path paramPath, String paramString, LinkOption[] paramArrayOfLinkOption)
/*      */     throws IOException
/*      */   {
/* 1816 */     if ((paramString.indexOf('*') >= 0) || (paramString.indexOf(',') >= 0))
/* 1817 */       throw new IllegalArgumentException(paramString);
/* 1818 */     Map localMap = readAttributes(paramPath, paramString, paramArrayOfLinkOption);
/* 1819 */     assert (localMap.size() == 1);
/*      */ 
/* 1821 */     int i = paramString.indexOf(':');
/*      */     String str;
/* 1822 */     if (i == -1)
/* 1823 */       str = paramString;
/*      */     else {
/* 1825 */       str = i == paramString.length() ? "" : paramString.substring(i + 1);
/*      */     }
/* 1827 */     return localMap.get(str);
/*      */   }
/*      */ 
/*      */   public static Map<String, Object> readAttributes(Path paramPath, String paramString, LinkOption[] paramArrayOfLinkOption)
/*      */     throws IOException
/*      */   {
/* 1913 */     return provider(paramPath).readAttributes(paramPath, paramString, paramArrayOfLinkOption);
/*      */   }
/*      */ 
/*      */   public static Set<PosixFilePermission> getPosixFilePermissions(Path paramPath, LinkOption[] paramArrayOfLinkOption)
/*      */     throws IOException
/*      */   {
/* 1953 */     return ((PosixFileAttributes)readAttributes(paramPath, PosixFileAttributes.class, paramArrayOfLinkOption)).permissions();
/*      */   }
/*      */ 
/*      */   public static Path setPosixFilePermissions(Path paramPath, Set<PosixFilePermission> paramSet)
/*      */     throws IOException
/*      */   {
/* 1988 */     PosixFileAttributeView localPosixFileAttributeView = (PosixFileAttributeView)getFileAttributeView(paramPath, PosixFileAttributeView.class, new LinkOption[0]);
/*      */ 
/* 1990 */     if (localPosixFileAttributeView == null)
/* 1991 */       throw new UnsupportedOperationException();
/* 1992 */     localPosixFileAttributeView.setPermissions(paramSet);
/* 1993 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static UserPrincipal getOwner(Path paramPath, LinkOption[] paramArrayOfLinkOption)
/*      */     throws IOException
/*      */   {
/* 2022 */     FileOwnerAttributeView localFileOwnerAttributeView = (FileOwnerAttributeView)getFileAttributeView(paramPath, FileOwnerAttributeView.class, paramArrayOfLinkOption);
/*      */ 
/* 2024 */     if (localFileOwnerAttributeView == null)
/* 2025 */       throw new UnsupportedOperationException();
/* 2026 */     return localFileOwnerAttributeView.getOwner();
/*      */   }
/*      */ 
/*      */   public static Path setOwner(Path paramPath, UserPrincipal paramUserPrincipal)
/*      */     throws IOException
/*      */   {
/* 2068 */     FileOwnerAttributeView localFileOwnerAttributeView = (FileOwnerAttributeView)getFileAttributeView(paramPath, FileOwnerAttributeView.class, new LinkOption[0]);
/*      */ 
/* 2070 */     if (localFileOwnerAttributeView == null)
/* 2071 */       throw new UnsupportedOperationException();
/* 2072 */     localFileOwnerAttributeView.setOwner(paramUserPrincipal);
/* 2073 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static boolean isSymbolicLink(Path paramPath)
/*      */   {
/*      */     try
/*      */     {
/* 2096 */       return readAttributes(paramPath, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }).isSymbolicLink();
/*      */     }
/*      */     catch (IOException localIOException) {
/*      */     }
/* 2100 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isDirectory(Path paramPath, LinkOption[] paramArrayOfLinkOption)
/*      */   {
/*      */     try
/*      */     {
/* 2135 */       return readAttributes(paramPath, BasicFileAttributes.class, paramArrayOfLinkOption).isDirectory(); } catch (IOException localIOException) {
/*      */     }
/* 2137 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isRegularFile(Path paramPath, LinkOption[] paramArrayOfLinkOption)
/*      */   {
/*      */     try
/*      */     {
/* 2172 */       return readAttributes(paramPath, BasicFileAttributes.class, paramArrayOfLinkOption).isRegularFile(); } catch (IOException localIOException) {
/*      */     }
/* 2174 */     return false;
/*      */   }
/*      */ 
/*      */   public static FileTime getLastModifiedTime(Path paramPath, LinkOption[] paramArrayOfLinkOption)
/*      */     throws IOException
/*      */   {
/* 2209 */     return readAttributes(paramPath, BasicFileAttributes.class, paramArrayOfLinkOption).lastModifiedTime();
/*      */   }
/*      */ 
/*      */   public static Path setLastModifiedTime(Path paramPath, FileTime paramFileTime)
/*      */     throws IOException
/*      */   {
/* 2248 */     ((BasicFileAttributeView)getFileAttributeView(paramPath, BasicFileAttributeView.class, new LinkOption[0])).setTimes(paramFileTime, null, null);
/*      */ 
/* 2250 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static long size(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 2275 */     return readAttributes(paramPath, BasicFileAttributes.class, new LinkOption[0]).size();
/*      */   }
/*      */ 
/*      */   private static boolean followLinks(LinkOption[] paramArrayOfLinkOption)
/*      */   {
/* 2284 */     boolean bool = true;
/* 2285 */     for (LinkOption localLinkOption : paramArrayOfLinkOption)
/* 2286 */       if (localLinkOption == LinkOption.NOFOLLOW_LINKS) {
/* 2287 */         bool = false;
/*      */       }
/*      */       else {
/* 2290 */         if (localLinkOption == null)
/* 2291 */           throw new NullPointerException();
/* 2292 */         throw new AssertionError("Should not get here");
/*      */       }
/* 2294 */     return bool;
/*      */   }
/*      */ 
/*      */   public static boolean exists(Path paramPath, LinkOption[] paramArrayOfLinkOption)
/*      */   {
/*      */     try
/*      */     {
/* 2327 */       if (followLinks(paramArrayOfLinkOption)) {
/* 2328 */         provider(paramPath).checkAccess(paramPath, new AccessMode[0]);
/*      */       }
/*      */       else {
/* 2331 */         readAttributes(paramPath, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*      */       }
/*      */ 
/* 2335 */       return true;
/*      */     } catch (IOException localIOException) {
/*      */     }
/* 2338 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean notExists(Path paramPath, LinkOption[] paramArrayOfLinkOption)
/*      */   {
/*      */     try
/*      */     {
/* 2376 */       if (followLinks(paramArrayOfLinkOption)) {
/* 2377 */         provider(paramPath).checkAccess(paramPath, new AccessMode[0]);
/*      */       }
/*      */       else {
/* 2380 */         readAttributes(paramPath, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*      */       }
/*      */ 
/* 2384 */       return false;
/*      */     }
/*      */     catch (NoSuchFileException localNoSuchFileException) {
/* 2387 */       return true; } catch (IOException localIOException) {
/*      */     }
/* 2389 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean isAccessible(Path paramPath, AccessMode[] paramArrayOfAccessMode)
/*      */   {
/*      */     try
/*      */     {
/* 2398 */       provider(paramPath).checkAccess(paramPath, paramArrayOfAccessMode);
/* 2399 */       return true; } catch (IOException localIOException) {
/*      */     }
/* 2401 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isReadable(Path paramPath)
/*      */   {
/* 2433 */     return isAccessible(paramPath, new AccessMode[] { AccessMode.READ });
/*      */   }
/*      */ 
/*      */   public static boolean isWritable(Path paramPath)
/*      */   {
/* 2464 */     return isAccessible(paramPath, new AccessMode[] { AccessMode.WRITE });
/*      */   }
/*      */ 
/*      */   public static boolean isExecutable(Path paramPath)
/*      */   {
/* 2499 */     return isAccessible(paramPath, new AccessMode[] { AccessMode.EXECUTE });
/*      */   }
/*      */ 
/*      */   public static Path walkFileTree(Path paramPath, Set<FileVisitOption> paramSet, int paramInt, FileVisitor<? super Path> paramFileVisitor)
/*      */     throws IOException
/*      */   {
/* 2600 */     if (paramInt < 0)
/* 2601 */       throw new IllegalArgumentException("'maxDepth' is negative");
/* 2602 */     new FileTreeWalker(paramSet, paramFileVisitor, paramInt).walk(paramPath);
/* 2603 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static Path walkFileTree(Path paramPath, FileVisitor<? super Path> paramFileVisitor)
/*      */     throws IOException
/*      */   {
/* 2635 */     return walkFileTree(paramPath, EnumSet.noneOf(FileVisitOption.class), 2147483647, paramFileVisitor);
/*      */   }
/*      */ 
/*      */   public static BufferedReader newBufferedReader(Path paramPath, Charset paramCharset)
/*      */     throws IOException
/*      */   {
/* 2676 */     CharsetDecoder localCharsetDecoder = paramCharset.newDecoder();
/* 2677 */     InputStreamReader localInputStreamReader = new InputStreamReader(newInputStream(paramPath, new OpenOption[0]), localCharsetDecoder);
/* 2678 */     return new BufferedReader(localInputStreamReader);
/*      */   }
/*      */ 
/*      */   public static BufferedWriter newBufferedWriter(Path paramPath, Charset paramCharset, OpenOption[] paramArrayOfOpenOption)
/*      */     throws IOException
/*      */   {
/* 2721 */     CharsetEncoder localCharsetEncoder = paramCharset.newEncoder();
/* 2722 */     OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(newOutputStream(paramPath, paramArrayOfOpenOption), localCharsetEncoder);
/* 2723 */     return new BufferedWriter(localOutputStreamWriter);
/*      */   }
/*      */ 
/*      */   private static long copy(InputStream paramInputStream, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 2732 */     long l = 0L;
/* 2733 */     byte[] arrayOfByte = new byte[8192];
/*      */     int i;
/* 2735 */     while ((i = paramInputStream.read(arrayOfByte)) > 0) {
/* 2736 */       paramOutputStream.write(arrayOfByte, 0, i);
/* 2737 */       l += i;
/*      */     }
/* 2739 */     return l;
/*      */   }
/*      */ 
/*      */   public static long copy(InputStream paramInputStream, Path paramPath, CopyOption[] paramArrayOfCopyOption)
/*      */     throws IOException
/*      */   {
/* 2811 */     Objects.requireNonNull(paramInputStream);
/*      */ 
/* 2814 */     int i = 0;
/* 2815 */     for (localObject2 : paramArrayOfCopyOption) {
/* 2816 */       if (localObject2 == StandardCopyOption.REPLACE_EXISTING) {
/* 2817 */         i = 1;
/*      */       } else {
/* 2819 */         if (localObject2 == null) {
/* 2820 */           throw new NullPointerException("options contains 'null'");
/*      */         }
/* 2822 */         throw new UnsupportedOperationException(localObject2 + " not supported");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2828 */     ??? = null;
/* 2829 */     if (i != 0) {
/*      */       try {
/* 2831 */         deleteIfExists(paramPath);
/*      */       } catch (SecurityException localSecurityException) {
/* 2833 */         ??? = localSecurityException;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     OutputStream localOutputStream1;
/*      */     try
/*      */     {
/* 2843 */       localOutputStream1 = newOutputStream(paramPath, new OpenOption[] { StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE });
/*      */     }
/*      */     catch (FileAlreadyExistsException localFileAlreadyExistsException) {
/* 2846 */       if (??? != null) {
/* 2847 */         throw ((Throwable)???);
/*      */       }
/* 2849 */       throw localFileAlreadyExistsException;
/*      */     }
/*      */ 
/* 2853 */     OutputStream localOutputStream2 = localOutputStream1; Object localObject2 = null;
/*      */     try { return copy(paramInputStream, localOutputStream2); }
/*      */     catch (Throwable localThrowable1)
/*      */     {
/* 2853 */       localObject2 = localThrowable1; throw localThrowable1;
/*      */     } finally {
/* 2855 */       if (localOutputStream2 != null) if (localObject2 != null) try { localOutputStream2.close(); } catch (Throwable localThrowable3) { localObject2.addSuppressed(localThrowable3); } else localOutputStream2.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static long copy(Path paramPath, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 2893 */     Objects.requireNonNull(paramOutputStream);
/*      */ 
/* 2895 */     InputStream localInputStream = newInputStream(paramPath, new OpenOption[0]); Object localObject1 = null;
/*      */     try { return copy(localInputStream, paramOutputStream); }
/*      */     catch (Throwable localThrowable1)
/*      */     {
/* 2895 */       localObject1 = localThrowable1; throw localThrowable1;
/*      */     } finally {
/* 2897 */       if (localInputStream != null) if (localObject1 != null) try { localInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localInputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static byte[] read(InputStream paramInputStream, int paramInt)
/*      */     throws IOException
/*      */   {
/* 2927 */     int i = paramInt;
/* 2928 */     byte[] arrayOfByte = new byte[i];
/* 2929 */     int j = 0;
/*      */     while (true)
/*      */     {
/*      */       int k;
/* 2934 */       if ((k = paramInputStream.read(arrayOfByte, j, i - j)) > 0) {
/* 2935 */         j += k;
/*      */       }
/*      */       else
/*      */       {
/* 2939 */         if ((k < 0) || ((k = paramInputStream.read()) < 0))
/*      */         {
/*      */           break;
/*      */         }
/* 2943 */         if (i <= 2147483639 - i) {
/* 2944 */           i = Math.max(i << 1, 8192);
/*      */         } else {
/* 2946 */           if (i == 2147483639)
/* 2947 */             throw new OutOfMemoryError("Required array size too large");
/* 2948 */           i = 2147483639;
/*      */         }
/* 2950 */         arrayOfByte = Arrays.copyOf(arrayOfByte, i);
/* 2951 */         arrayOfByte[(j++)] = ((byte)k);
/*      */       }
/*      */     }
/* 2953 */     return i == j ? arrayOfByte : Arrays.copyOf(arrayOfByte, j);
/*      */   }
/*      */ 
/*      */   public static byte[] readAllBytes(Path paramPath)
/*      */     throws IOException
/*      */   {
/* 2981 */     SeekableByteChannel localSeekableByteChannel = newByteChannel(paramPath, new OpenOption[0]); Object localObject1 = null;
/*      */     try { InputStream localInputStream = Channels.newInputStream(localSeekableByteChannel);
/*      */ 
/* 2981 */       Object localObject2 = null;
/*      */       try {
/* 2983 */         long l = localSeekableByteChannel.size();
/* 2984 */         if (l > 2147483639L) {
/* 2985 */           throw new OutOfMemoryError("Required array size too large");
/*      */         }
/* 2987 */         byte[] arrayOfByte = read(localInputStream, (int)l);
/* 2988 */         if (localInputStream != null) if (localObject2 != null) try { localInputStream.close(); } catch (Throwable localThrowable3) { localObject2.addSuppressed(localThrowable3); } else localInputStream.close(); 
/* 2988 */         return arrayOfByte;
/*      */       }
/*      */       catch (Throwable localThrowable2)
/*      */       {
/* 2981 */         localObject2 = localThrowable2; throw localThrowable2;
/*      */       }
/*      */       finally
/*      */       {
/* 2988 */         if (localInputStream != null) if (localObject2 != null) try { localInputStream.close(); } catch (Throwable localThrowable5) { localObject2.addSuppressed(localThrowable5); } else localInputStream.close();
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable1)
/*      */     {
/* 2981 */       localObject1 = localThrowable1; throw localThrowable1;
/*      */     }
/*      */     finally
/*      */     {
/* 2988 */       if (localSeekableByteChannel != null) if (localObject1 != null) try { localSeekableByteChannel.close(); } catch (Throwable localThrowable6) { localObject1.addSuppressed(localThrowable6); } else localSeekableByteChannel.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static List<String> readAllLines(Path paramPath, Charset paramCharset)
/*      */     throws IOException
/*      */   {
/* 3033 */     BufferedReader localBufferedReader = newBufferedReader(paramPath, paramCharset); Object localObject1 = null;
/*      */     try { ArrayList localArrayList = new ArrayList();
/*      */       Object localObject2;
/*      */       while (true) { localObject2 = localBufferedReader.readLine();
/* 3037 */         if (localObject2 == null)
/*      */           break;
/* 3039 */         localArrayList.add(localObject2);
/*      */       }
/* 3041 */       return localArrayList;
/*      */     }
/*      */     catch (Throwable localThrowable1)
/*      */     {
/* 3033 */       localObject1 = localThrowable1; throw localThrowable1;
/*      */     }
/*      */     finally
/*      */     {
/* 3042 */       if (localBufferedReader != null) if (localObject1 != null) try { localBufferedReader.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localBufferedReader.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Path write(Path paramPath, byte[] paramArrayOfByte, OpenOption[] paramArrayOfOpenOption)
/*      */     throws IOException
/*      */   {
/* 3090 */     Objects.requireNonNull(paramArrayOfByte);
/*      */ 
/* 3092 */     OutputStream localOutputStream = newOutputStream(paramPath, paramArrayOfOpenOption); Object localObject1 = null;
/*      */     try { int i = paramArrayOfByte.length;
/* 3094 */       int j = i;
/* 3095 */       while (j > 0) {
/* 3096 */         int k = Math.min(j, 8192);
/* 3097 */         localOutputStream.write(paramArrayOfByte, i - j, k);
/* 3098 */         j -= k;
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable2)
/*      */     {
/* 3092 */       localObject1 = localThrowable2; throw localThrowable2;
/*      */     }
/*      */     finally
/*      */     {
/* 3100 */       if (localOutputStream != null) if (localObject1 != null) try { localOutputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localOutputStream.close(); 
/*      */     }
/* 3101 */     return paramPath;
/*      */   }
/*      */ 
/*      */   public static Path write(Path paramPath, Iterable<? extends CharSequence> paramIterable, Charset paramCharset, OpenOption[] paramArrayOfOpenOption)
/*      */     throws IOException
/*      */   {
/* 3149 */     Objects.requireNonNull(paramIterable);
/* 3150 */     CharsetEncoder localCharsetEncoder = paramCharset.newEncoder();
/* 3151 */     OutputStream localOutputStream = newOutputStream(paramPath, paramArrayOfOpenOption);
/* 3152 */     BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(localOutputStream, localCharsetEncoder)); Object localObject1 = null;
/*      */     try { for (CharSequence localCharSequence : paramIterable) {
/* 3154 */         localBufferedWriter.append(localCharSequence);
/* 3155 */         localBufferedWriter.newLine();
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable2)
/*      */     {
/* 3152 */       localObject1 = localThrowable2; throw localThrowable2;
/*      */     }
/*      */     finally
/*      */     {
/* 3157 */       if (localBufferedWriter != null) if (localObject1 != null) try { localBufferedWriter.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localBufferedWriter.close(); 
/*      */     }
/* 3158 */     return paramPath;
/*      */   }
/*      */ 
/*      */   private static class AcceptAllFilter
/*      */     implements DirectoryStream.Filter<Path>
/*      */   {
/*  376 */     static final AcceptAllFilter FILTER = new AcceptAllFilter();
/*      */ 
/*      */     public boolean accept(Path paramPath)
/*      */     {
/*  374 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FileTypeDetectors
/*      */   {
/* 1488 */     static final FileTypeDetector defaultFileTypeDetector = createDefaultFileTypeDetector();
/*      */ 
/* 1490 */     static final List<FileTypeDetector> installeDetectors = loadInstalledDetectors();
/*      */ 
/*      */     private static FileTypeDetector createDefaultFileTypeDetector()
/*      */     {
/* 1495 */       return (FileTypeDetector)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public FileTypeDetector run() {
/* 1498 */           return DefaultFileTypeDetector.create();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     private static List<FileTypeDetector> loadInstalledDetectors() {
/* 1504 */       return (List)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public List<FileTypeDetector> run() {
/* 1507 */           ArrayList localArrayList = new ArrayList();
/* 1508 */           ServiceLoader localServiceLoader = ServiceLoader.load(FileTypeDetector.class, ClassLoader.getSystemClassLoader());
/*      */ 
/* 1510 */           for (FileTypeDetector localFileTypeDetector : localServiceLoader) {
/* 1511 */             localArrayList.add(localFileTypeDetector);
/*      */           }
/* 1513 */           return localArrayList;
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.Files
 * JD-Core Version:    0.6.2
 */