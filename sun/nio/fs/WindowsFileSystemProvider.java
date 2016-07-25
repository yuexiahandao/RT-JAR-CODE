/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryNotEmptyException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.DirectoryStream.Filter;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystemAlreadyExistsException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.LinkPermission;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.attribute.AclFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.DosFileAttributeView;
/*     */ import java.nio.file.attribute.DosFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileOwnerAttributeView;
/*     */ import java.nio.file.attribute.UserDefinedFileAttributeView;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.nio.ch.ThreadPool;
/*     */ 
/*     */ public class WindowsFileSystemProvider extends AbstractFileSystemProvider
/*     */ {
/*  47 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final String USER_DIR = "user.dir";
/*     */   private final WindowsFileSystem theFileSystem;
/*     */ 
/*     */   public WindowsFileSystemProvider()
/*     */   {
/*  53 */     this.theFileSystem = new WindowsFileSystem(this, System.getProperty("user.dir"));
/*     */   }
/*     */ 
/*     */   public String getScheme()
/*     */   {
/*  58 */     return "file";
/*     */   }
/*     */ 
/*     */   private void checkUri(URI paramURI) {
/*  62 */     if (!paramURI.getScheme().equalsIgnoreCase(getScheme()))
/*  63 */       throw new IllegalArgumentException("URI does not match this provider");
/*  64 */     if (paramURI.getAuthority() != null)
/*  65 */       throw new IllegalArgumentException("Authority component present");
/*  66 */     if (paramURI.getPath() == null)
/*  67 */       throw new IllegalArgumentException("Path component is undefined");
/*  68 */     if (!paramURI.getPath().equals("/"))
/*  69 */       throw new IllegalArgumentException("Path component should be '/'");
/*  70 */     if (paramURI.getQuery() != null)
/*  71 */       throw new IllegalArgumentException("Query component present");
/*  72 */     if (paramURI.getFragment() != null)
/*  73 */       throw new IllegalArgumentException("Fragment component present");
/*     */   }
/*     */ 
/*     */   public FileSystem newFileSystem(URI paramURI, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/*  80 */     checkUri(paramURI);
/*  81 */     throw new FileSystemAlreadyExistsException();
/*     */   }
/*     */ 
/*     */   public final FileSystem getFileSystem(URI paramURI)
/*     */   {
/*  86 */     checkUri(paramURI);
/*  87 */     return this.theFileSystem;
/*     */   }
/*     */ 
/*     */   public Path getPath(URI paramURI)
/*     */   {
/*  92 */     return WindowsUriSupport.fromUri(this.theFileSystem, paramURI);
/*     */   }
/*     */ 
/*     */   public FileChannel newFileChannel(Path paramPath, Set<? extends OpenOption> paramSet, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 101 */     if (paramPath == null)
/* 102 */       throw new NullPointerException();
/* 103 */     if (!(paramPath instanceof WindowsPath))
/* 104 */       throw new ProviderMismatchException();
/* 105 */     WindowsPath localWindowsPath = (WindowsPath)paramPath;
/*     */ 
/* 107 */     WindowsSecurityDescriptor localWindowsSecurityDescriptor = WindowsSecurityDescriptor.fromAttribute(paramArrayOfFileAttribute);
/*     */     try {
/* 109 */       return WindowsChannelFactory.newFileChannel(localWindowsPath.getPathForWin32Calls(), localWindowsPath.getPathForPermissionCheck(), paramSet, localWindowsSecurityDescriptor.address());
/*     */     }
/*     */     catch (WindowsException localWindowsException)
/*     */     {
/* 115 */       localWindowsException.rethrowAsIOException(localWindowsPath);
/* 116 */       return null;
/*     */     } finally {
/* 118 */       if (localWindowsSecurityDescriptor != null)
/* 119 */         localWindowsSecurityDescriptor.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AsynchronousFileChannel newAsynchronousFileChannel(Path paramPath, Set<? extends OpenOption> paramSet, ExecutorService paramExecutorService, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 130 */     if (paramPath == null)
/* 131 */       throw new NullPointerException();
/* 132 */     if (!(paramPath instanceof WindowsPath))
/* 133 */       throw new ProviderMismatchException();
/* 134 */     WindowsPath localWindowsPath = (WindowsPath)paramPath;
/* 135 */     ThreadPool localThreadPool = paramExecutorService == null ? null : ThreadPool.wrap(paramExecutorService, 0);
/* 136 */     WindowsSecurityDescriptor localWindowsSecurityDescriptor = WindowsSecurityDescriptor.fromAttribute(paramArrayOfFileAttribute);
/*     */     try
/*     */     {
/* 139 */       return WindowsChannelFactory.newAsynchronousFileChannel(localWindowsPath.getPathForWin32Calls(), localWindowsPath.getPathForPermissionCheck(), paramSet, localWindowsSecurityDescriptor.address(), localThreadPool);
/*     */     }
/*     */     catch (WindowsException localWindowsException)
/*     */     {
/* 146 */       localWindowsException.rethrowAsIOException(localWindowsPath);
/* 147 */       return null;
/*     */     } finally {
/* 149 */       if (localWindowsSecurityDescriptor != null)
/* 150 */         localWindowsSecurityDescriptor.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   public <V extends FileAttributeView> V getFileAttributeView(Path paramPath, Class<V> paramClass, LinkOption[] paramArrayOfLinkOption)
/*     */   {
/* 159 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 160 */     if (paramClass == null)
/* 161 */       throw new NullPointerException();
/* 162 */     boolean bool = Util.followLinks(paramArrayOfLinkOption);
/* 163 */     if (paramClass == BasicFileAttributeView.class)
/* 164 */       return WindowsFileAttributeViews.createBasicView(localWindowsPath, bool);
/* 165 */     if (paramClass == DosFileAttributeView.class)
/* 166 */       return WindowsFileAttributeViews.createDosView(localWindowsPath, bool);
/* 167 */     if (paramClass == AclFileAttributeView.class)
/* 168 */       return new WindowsAclFileAttributeView(localWindowsPath, bool);
/* 169 */     if (paramClass == FileOwnerAttributeView.class) {
/* 170 */       return new FileOwnerAttributeViewImpl(new WindowsAclFileAttributeView(localWindowsPath, bool));
/*     */     }
/* 172 */     if (paramClass == UserDefinedFileAttributeView.class)
/* 173 */       return new WindowsUserDefinedFileAttributeView(localWindowsPath, bool);
/* 174 */     return (FileAttributeView)null;
/*     */   }
/*     */ 
/*     */   public <A extends BasicFileAttributes> A readAttributes(Path paramPath, Class<A> paramClass, LinkOption[] paramArrayOfLinkOption)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject;
/* 185 */     if (paramClass == BasicFileAttributes.class) {
/* 186 */       localObject = BasicFileAttributeView.class;
/* 187 */     } else if (paramClass == DosFileAttributes.class) {
/* 188 */       localObject = DosFileAttributeView.class; } else {
/* 189 */       if (paramClass == null) {
/* 190 */         throw new NullPointerException();
/*     */       }
/* 192 */       throw new UnsupportedOperationException();
/* 193 */     }return ((BasicFileAttributeView)getFileAttributeView(paramPath, (Class)localObject, paramArrayOfLinkOption)).readAttributes();
/*     */   }
/*     */ 
/*     */   public DynamicFileAttributeView getFileAttributeView(Path paramPath, String paramString, LinkOption[] paramArrayOfLinkOption)
/*     */   {
/* 198 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 199 */     boolean bool = Util.followLinks(paramArrayOfLinkOption);
/* 200 */     if (paramString.equals("basic"))
/* 201 */       return WindowsFileAttributeViews.createBasicView(localWindowsPath, bool);
/* 202 */     if (paramString.equals("dos"))
/* 203 */       return WindowsFileAttributeViews.createDosView(localWindowsPath, bool);
/* 204 */     if (paramString.equals("acl"))
/* 205 */       return new WindowsAclFileAttributeView(localWindowsPath, bool);
/* 206 */     if (paramString.equals("owner")) {
/* 207 */       return new FileOwnerAttributeViewImpl(new WindowsAclFileAttributeView(localWindowsPath, bool));
/*     */     }
/* 209 */     if (paramString.equals("user"))
/* 210 */       return new WindowsUserDefinedFileAttributeView(localWindowsPath, bool);
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */   public SeekableByteChannel newByteChannel(Path paramPath, Set<? extends OpenOption> paramSet, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 220 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 221 */     WindowsSecurityDescriptor localWindowsSecurityDescriptor = WindowsSecurityDescriptor.fromAttribute(paramArrayOfFileAttribute);
/*     */     try
/*     */     {
/* 224 */       return WindowsChannelFactory.newFileChannel(localWindowsPath.getPathForWin32Calls(), localWindowsPath.getPathForPermissionCheck(), paramSet, localWindowsSecurityDescriptor.address());
/*     */     }
/*     */     catch (WindowsException localWindowsException)
/*     */     {
/* 230 */       localWindowsException.rethrowAsIOException(localWindowsPath);
/* 231 */       return null;
/*     */     } finally {
/* 233 */       localWindowsSecurityDescriptor.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean implDelete(Path paramPath, boolean paramBoolean) throws IOException
/*     */   {
/* 239 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 240 */     localWindowsPath.checkDelete();
/*     */ 
/* 242 */     WindowsFileAttributes localWindowsFileAttributes = null;
/*     */     try
/*     */     {
/* 245 */       localWindowsFileAttributes = WindowsFileAttributes.get(localWindowsPath, false);
/* 246 */       if ((localWindowsFileAttributes.isDirectory()) || (localWindowsFileAttributes.isDirectoryLink()))
/* 247 */         WindowsNativeDispatcher.RemoveDirectory(localWindowsPath.getPathForWin32Calls());
/*     */       else {
/* 249 */         WindowsNativeDispatcher.DeleteFile(localWindowsPath.getPathForWin32Calls());
/*     */       }
/* 251 */       return true;
/*     */     }
/*     */     catch (WindowsException localWindowsException)
/*     */     {
/* 255 */       if ((!paramBoolean) && ((localWindowsException.lastError() == 2) || (localWindowsException.lastError() == 3)))
/*     */       {
/* 257 */         return false;
/*     */       }
/* 259 */       if ((localWindowsFileAttributes != null) && (localWindowsFileAttributes.isDirectory()))
/*     */       {
/* 262 */         if ((localWindowsException.lastError() == 145) || (localWindowsException.lastError() == 183))
/*     */         {
/* 265 */           throw new DirectoryNotEmptyException(localWindowsPath.getPathForExceptionMessage());
/*     */         }
/*     */       }
/*     */ 
/* 269 */       localWindowsException.rethrowAsIOException(localWindowsPath);
/* 270 */     }return false;
/*     */   }
/*     */ 
/*     */   public void copy(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException
/*     */   {
/* 278 */     WindowsFileCopy.copy(WindowsPath.toWindowsPath(paramPath1), WindowsPath.toWindowsPath(paramPath2), paramArrayOfCopyOption);
/*     */   }
/*     */ 
/*     */   public void move(Path paramPath1, Path paramPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException
/*     */   {
/* 287 */     WindowsFileCopy.move(WindowsPath.toWindowsPath(paramPath1), WindowsPath.toWindowsPath(paramPath2), paramArrayOfCopyOption);
/*     */   }
/*     */ 
/*     */   private static boolean hasDesiredAccess(WindowsPath paramWindowsPath, int paramInt)
/*     */     throws IOException
/*     */   {
/* 297 */     boolean bool = false;
/* 298 */     String str = WindowsLinkSupport.getFinalPath(paramWindowsPath, true);
/* 299 */     NativeBuffer localNativeBuffer = WindowsAclFileAttributeView.getFileSecurity(str, 7);
/*     */     try
/*     */     {
/* 305 */       bool = WindowsSecurity.checkAccessMask(localNativeBuffer.address(), paramInt, 1179785, 1179926, 1179808, 2032127);
/*     */     }
/*     */     catch (WindowsException localWindowsException)
/*     */     {
/* 311 */       localWindowsException.rethrowAsIOException(paramWindowsPath);
/*     */     } finally {
/* 313 */       localNativeBuffer.release();
/*     */     }
/* 315 */     return bool;
/*     */   }
/*     */ 
/*     */   private void checkReadAccess(WindowsPath paramWindowsPath)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 323 */       Set localSet = Collections.emptySet();
/* 324 */       FileChannel localFileChannel = WindowsChannelFactory.newFileChannel(paramWindowsPath.getPathForWin32Calls(), paramWindowsPath.getPathForPermissionCheck(), localSet, 0L);
/*     */ 
/* 329 */       localFileChannel.close();
/*     */     }
/*     */     catch (WindowsException localWindowsException)
/*     */     {
/*     */       try
/*     */       {
/* 335 */         new WindowsDirectoryStream(paramWindowsPath, null).close();
/*     */       }
/*     */       catch (IOException localIOException) {
/* 338 */         localWindowsException.rethrowAsIOException(paramWindowsPath);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkAccess(Path paramPath, AccessMode[] paramArrayOfAccessMode) throws IOException
/*     */   {
/* 345 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/*     */ 
/* 347 */     int i = 0;
/* 348 */     int j = 0;
/* 349 */     int k = 0;
/* 350 */     for (AccessMode localAccessMode : paramArrayOfAccessMode) {
/* 351 */       switch (1.$SwitchMap$java$nio$file$AccessMode[localAccessMode.ordinal()]) { case 1:
/* 352 */         i = 1; break;
/*     */       case 2:
/* 353 */         j = 1; break;
/*     */       case 3:
/* 354 */         k = 1; break;
/*     */       default:
/* 355 */         throw new AssertionError("Should not get here");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 361 */     if ((j == 0) && (k == 0)) {
/* 362 */       checkReadAccess(localWindowsPath);
/* 363 */       return;
/*     */     }
/*     */ 
/* 366 */     int m = 0;
/* 367 */     if (i != 0) {
/* 368 */       localWindowsPath.checkRead();
/* 369 */       m |= 1;
/*     */     }
/* 371 */     if (j != 0) {
/* 372 */       localWindowsPath.checkWrite();
/* 373 */       m |= 2;
/*     */     }
/*     */     Object localObject;
/* 375 */     if (k != 0) {
/* 376 */       localObject = System.getSecurityManager();
/* 377 */       if (localObject != null)
/* 378 */         ((SecurityManager)localObject).checkExec(localWindowsPath.getPathForPermissionCheck());
/* 379 */       m |= 32;
/*     */     }
/*     */ 
/* 382 */     if (!hasDesiredAccess(localWindowsPath, m)) {
/* 383 */       throw new AccessDeniedException(localWindowsPath.getPathForExceptionMessage(), null, "Permissions does not allow requested access");
/*     */     }
/*     */ 
/* 389 */     if (j != 0) {
/*     */       try {
/* 391 */         localObject = WindowsFileAttributes.get(localWindowsPath, true);
/* 392 */         if ((!((WindowsFileAttributes)localObject).isDirectory()) && (((WindowsFileAttributes)localObject).isReadOnly()))
/* 393 */           throw new AccessDeniedException(localWindowsPath.getPathForExceptionMessage(), null, "DOS readonly attribute is set");
/*     */       }
/*     */       catch (WindowsException localWindowsException)
/*     */       {
/* 397 */         localWindowsException.rethrowAsIOException(localWindowsPath);
/*     */       }
/*     */ 
/* 400 */       if (WindowsFileStore.create(localWindowsPath).isReadOnly())
/* 401 */         throw new AccessDeniedException(localWindowsPath.getPathForExceptionMessage(), null, "Read-only file system");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isSameFile(Path paramPath1, Path paramPath2)
/*     */     throws IOException
/*     */   {
/* 409 */     WindowsPath localWindowsPath1 = WindowsPath.toWindowsPath(paramPath1);
/* 410 */     if (localWindowsPath1.equals(paramPath2))
/* 411 */       return true;
/* 412 */     if (paramPath2 == null)
/* 413 */       throw new NullPointerException();
/* 414 */     if (!(paramPath2 instanceof WindowsPath))
/* 415 */       return false;
/* 416 */     WindowsPath localWindowsPath2 = (WindowsPath)paramPath2;
/*     */ 
/* 419 */     localWindowsPath1.checkRead();
/* 420 */     localWindowsPath2.checkRead();
/*     */ 
/* 423 */     long l1 = 0L;
/*     */     try {
/* 425 */       l1 = localWindowsPath1.openForReadAttributeAccess(true);
/*     */     } catch (WindowsException localWindowsException1) {
/* 427 */       localWindowsException1.rethrowAsIOException(localWindowsPath1);
/*     */     }
/*     */     try {
/* 430 */       WindowsFileAttributes localWindowsFileAttributes1 = null;
/*     */       try {
/* 432 */         localWindowsFileAttributes1 = WindowsFileAttributes.readAttributes(l1);
/*     */       } catch (WindowsException localWindowsException2) {
/* 434 */         localWindowsException2.rethrowAsIOException(localWindowsPath1);
/*     */       }
/* 436 */       long l2 = 0L;
/*     */       try {
/* 438 */         l2 = localWindowsPath2.openForReadAttributeAccess(true);
/*     */       } catch (WindowsException localWindowsException3) {
/* 440 */         localWindowsException3.rethrowAsIOException(localWindowsPath2);
/*     */       }
/*     */       try {
/* 443 */         WindowsFileAttributes localWindowsFileAttributes2 = null;
/*     */         try {
/* 445 */           localWindowsFileAttributes2 = WindowsFileAttributes.readAttributes(l2);
/*     */         } catch (WindowsException localWindowsException4) {
/* 447 */           localWindowsException4.rethrowAsIOException(localWindowsPath2);
/*     */         }
/* 449 */         return WindowsFileAttributes.isSameFile(localWindowsFileAttributes1, localWindowsFileAttributes2);
/*     */       } finally {
/*     */       }
/*     */     }
/*     */     finally {
/* 454 */       WindowsNativeDispatcher.CloseHandle(l1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isHidden(Path paramPath) throws IOException
/*     */   {
/* 460 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 461 */     localWindowsPath.checkRead();
/* 462 */     WindowsFileAttributes localWindowsFileAttributes = null;
/*     */     try {
/* 464 */       localWindowsFileAttributes = WindowsFileAttributes.get(localWindowsPath, true);
/*     */     } catch (WindowsException localWindowsException) {
/* 466 */       localWindowsException.rethrowAsIOException(localWindowsPath);
/*     */     }
/*     */ 
/* 469 */     if (localWindowsFileAttributes.isDirectory())
/* 470 */       return false;
/* 471 */     return localWindowsFileAttributes.isHidden();
/*     */   }
/*     */ 
/*     */   public FileStore getFileStore(Path paramPath) throws IOException
/*     */   {
/* 476 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 477 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 478 */     if (localSecurityManager != null) {
/* 479 */       localSecurityManager.checkPermission(new RuntimePermission("getFileStoreAttributes"));
/* 480 */       localWindowsPath.checkRead();
/*     */     }
/* 482 */     return WindowsFileStore.create(localWindowsPath);
/*     */   }
/*     */ 
/*     */   public void createDirectory(Path paramPath, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 490 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 491 */     localWindowsPath.checkWrite();
/* 492 */     WindowsSecurityDescriptor localWindowsSecurityDescriptor = WindowsSecurityDescriptor.fromAttribute(paramArrayOfFileAttribute);
/*     */     try {
/* 494 */       WindowsNativeDispatcher.CreateDirectory(localWindowsPath.getPathForWin32Calls(), localWindowsSecurityDescriptor.address());
/*     */     } catch (WindowsException localWindowsException) {
/* 496 */       localWindowsException.rethrowAsIOException(localWindowsPath);
/*     */     } finally {
/* 498 */       localWindowsSecurityDescriptor.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DirectoryStream<Path> newDirectoryStream(Path paramPath, DirectoryStream.Filter<? super Path> paramFilter)
/*     */     throws IOException
/*     */   {
/* 506 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 507 */     localWindowsPath.checkRead();
/* 508 */     if (paramFilter == null)
/* 509 */       throw new NullPointerException();
/* 510 */     return new WindowsDirectoryStream(localWindowsPath, paramFilter);
/*     */   }
/*     */ 
/*     */   public void createSymbolicLink(Path paramPath1, Path paramPath2, FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 517 */     WindowsPath localWindowsPath1 = WindowsPath.toWindowsPath(paramPath1);
/* 518 */     WindowsPath localWindowsPath2 = WindowsPath.toWindowsPath(paramPath2);
/*     */ 
/* 520 */     if (!localWindowsPath1.getFileSystem().supportsLinks()) {
/* 521 */       throw new UnsupportedOperationException("Symbolic links not supported on this operating system");
/*     */     }
/*     */ 
/* 526 */     if (paramArrayOfFileAttribute.length > 0) {
/* 527 */       WindowsSecurityDescriptor.fromAttribute(paramArrayOfFileAttribute);
/* 528 */       throw new UnsupportedOperationException("Initial file attributesnot supported when creating symbolic link");
/*     */     }
/*     */ 
/* 533 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 534 */     if (localSecurityManager != null) {
/* 535 */       localSecurityManager.checkPermission(new LinkPermission("symbolic"));
/* 536 */       localWindowsPath1.checkWrite();
/*     */     }
/*     */ 
/* 543 */     if (localWindowsPath2.type() == WindowsPathType.DRIVE_RELATIVE)
/* 544 */       throw new IOException("Cannot create symbolic link to working directory relative target");
/*     */     WindowsPath localWindowsPath3;
/* 553 */     if (localWindowsPath2.type() == WindowsPathType.RELATIVE) {
/* 554 */       WindowsPath localWindowsPath4 = localWindowsPath1.getParent();
/* 555 */       localWindowsPath3 = localWindowsPath4 == null ? localWindowsPath2 : localWindowsPath4.resolve(localWindowsPath2);
/*     */     } else {
/* 557 */       localWindowsPath3 = localWindowsPath1.resolve(localWindowsPath2);
/*     */     }
/* 559 */     int i = 0;
/*     */     try {
/* 561 */       WindowsFileAttributes localWindowsFileAttributes = WindowsFileAttributes.get(localWindowsPath3, false);
/* 562 */       if ((localWindowsFileAttributes.isDirectory()) || (localWindowsFileAttributes.isDirectoryLink()))
/* 563 */         i |= 1;
/*     */     }
/*     */     catch (WindowsException localWindowsException1)
/*     */     {
/*     */     }
/*     */     try
/*     */     {
/* 570 */       WindowsNativeDispatcher.CreateSymbolicLink(localWindowsPath1.getPathForWin32Calls(), WindowsPath.addPrefixIfNeeded(localWindowsPath2.toString()), i);
/*     */     }
/*     */     catch (WindowsException localWindowsException2)
/*     */     {
/* 574 */       if (localWindowsException2.lastError() == 4392)
/* 575 */         localWindowsException2.rethrowAsIOException(localWindowsPath1, localWindowsPath2);
/*     */       else
/* 577 */         localWindowsException2.rethrowAsIOException(localWindowsPath1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void createLink(Path paramPath1, Path paramPath2)
/*     */     throws IOException
/*     */   {
/* 584 */     WindowsPath localWindowsPath1 = WindowsPath.toWindowsPath(paramPath1);
/* 585 */     WindowsPath localWindowsPath2 = WindowsPath.toWindowsPath(paramPath2);
/*     */ 
/* 588 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 589 */     if (localSecurityManager != null) {
/* 590 */       localSecurityManager.checkPermission(new LinkPermission("hard"));
/* 591 */       localWindowsPath1.checkWrite();
/* 592 */       localWindowsPath2.checkWrite();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 597 */       WindowsNativeDispatcher.CreateHardLink(localWindowsPath1.getPathForWin32Calls(), localWindowsPath2.getPathForWin32Calls());
/*     */     }
/*     */     catch (WindowsException localWindowsException) {
/* 600 */       localWindowsException.rethrowAsIOException(localWindowsPath1, localWindowsPath2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Path readSymbolicLink(Path paramPath) throws IOException
/*     */   {
/* 606 */     WindowsPath localWindowsPath = WindowsPath.toWindowsPath(paramPath);
/* 607 */     WindowsFileSystem localWindowsFileSystem = localWindowsPath.getFileSystem();
/* 608 */     if (!localWindowsFileSystem.supportsLinks()) {
/* 609 */       throw new UnsupportedOperationException("symbolic links not supported");
/*     */     }
/*     */ 
/* 613 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 614 */     if (localSecurityManager != null) {
/* 615 */       localObject = new FilePermission(localWindowsPath.getPathForPermissionCheck(), "readlink");
/*     */ 
/* 617 */       AccessController.checkPermission((Permission)localObject);
/*     */     }
/*     */ 
/* 620 */     Object localObject = WindowsLinkSupport.readLink(localWindowsPath);
/* 621 */     return WindowsPath.createFromNormalizedPath(localWindowsFileSystem, (String)localObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsFileSystemProvider
 * JD-Core Version:    0.6.2
 */