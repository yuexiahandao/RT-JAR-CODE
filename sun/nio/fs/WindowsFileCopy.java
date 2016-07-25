/*     */ package sun.nio.fs;
/*     */ 
/*     */ import com.sun.nio.file.ExtendedCopyOption;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.AtomicMoveNotSupportedException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryNotEmptyException;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.LinkPermission;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ 
/*     */ class WindowsFileCopy
/*     */ {
/*     */   static void copy(final WindowsPath paramWindowsPath1, final WindowsPath paramWindowsPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException
/*     */   {
/*  53 */     int i = 0;
/*  54 */     int j = 0;
/*  55 */     boolean bool = true;
/*  56 */     int k = 0;
/*  57 */     for (Object localObject2 : paramArrayOfCopyOption) {
/*  58 */       if (localObject2 == StandardCopyOption.REPLACE_EXISTING) {
/*  59 */         i = 1;
/*     */       }
/*  62 */       else if (localObject2 == LinkOption.NOFOLLOW_LINKS) {
/*  63 */         bool = false;
/*     */       }
/*  66 */       else if (localObject2 == StandardCopyOption.COPY_ATTRIBUTES) {
/*  67 */         j = 1;
/*     */       }
/*  70 */       else if (localObject2 == ExtendedCopyOption.INTERRUPTIBLE) {
/*  71 */         k = 1;
/*     */       }
/*     */       else {
/*  74 */         if (localObject2 == null)
/*  75 */           throw new NullPointerException();
/*  76 */         throw new UnsupportedOperationException("Unsupported copy option");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  81 */     ??? = System.getSecurityManager();
/*  82 */     if (??? != null) {
/*  83 */       paramWindowsPath1.checkRead();
/*  84 */       paramWindowsPath2.checkWrite();
/*     */     }
/*     */ 
/*  92 */     WindowsFileAttributes localWindowsFileAttributes1 = null;
/*  93 */     WindowsFileAttributes localWindowsFileAttributes2 = null;
/*     */ 
/*  95 */     long l1 = 0L;
/*     */     try {
/*  97 */       l1 = paramWindowsPath1.openForReadAttributeAccess(bool);
/*     */     } catch (WindowsException localWindowsException1) {
/*  99 */       localWindowsException1.rethrowAsIOException(paramWindowsPath1);
/*     */     }
/*     */     try
/*     */     {
/*     */       try {
/* 104 */         localWindowsFileAttributes1 = WindowsFileAttributes.readAttributes(l1);
/*     */       } catch (WindowsException localWindowsException2) {
/* 106 */         localWindowsException2.rethrowAsIOException(paramWindowsPath1);
/*     */       }
/*     */ 
/* 110 */       long l2 = 0L;
/*     */       try {
/* 112 */         l2 = paramWindowsPath2.openForReadAttributeAccess(false);
/*     */         try {
/* 114 */           localWindowsFileAttributes2 = WindowsFileAttributes.readAttributes(l2);
/*     */ 
/* 117 */           if (WindowsFileAttributes.isSameFile(localWindowsFileAttributes1, localWindowsFileAttributes2))
/*     */           {
/*     */             return;
/*     */           }
/*     */ 
/* 122 */           if (i == 0)
/* 123 */             throw new FileAlreadyExistsException(paramWindowsPath2.getPathForExceptionMessage());
/*     */         }
/*     */         finally
/*     */         {
/*     */         }
/*     */       }
/*     */       catch (WindowsException localWindowsException3)
/*     */       {
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 135 */       WindowsNativeDispatcher.CloseHandle(l1);
/*     */     }
/*     */ 
/* 139 */     if ((??? != null) && (localWindowsFileAttributes1.isSymbolicLink())) {
/* 140 */       ((SecurityManager)???).checkPermission(new LinkPermission("symbolic"));
/*     */     }
/*     */ 
/* 143 */     String str1 = asWin32Path(paramWindowsPath1);
/* 144 */     final String str2 = asWin32Path(paramWindowsPath2);
/*     */ 
/* 147 */     if (localWindowsFileAttributes2 != null) {
/*     */       try {
/* 149 */         if ((localWindowsFileAttributes2.isDirectory()) || (localWindowsFileAttributes2.isDirectoryLink()))
/* 150 */           WindowsNativeDispatcher.RemoveDirectory(str2);
/*     */         else
/* 152 */           WindowsNativeDispatcher.DeleteFile(str2);
/*     */       }
/*     */       catch (WindowsException localWindowsException4) {
/* 155 */         if (localWindowsFileAttributes2.isDirectory())
/*     */         {
/* 158 */           if ((localWindowsException4.lastError() == 145) || (localWindowsException4.lastError() == 183))
/*     */           {
/* 161 */             throw new DirectoryNotEmptyException(paramWindowsPath2.getPathForExceptionMessage());
/*     */           }
/*     */         }
/*     */ 
/* 165 */         localWindowsException4.rethrowAsIOException(paramWindowsPath2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 170 */     if ((!localWindowsFileAttributes1.isDirectory()) && (!localWindowsFileAttributes1.isDirectoryLink())) {
/* 171 */       final int i1 = (paramWindowsPath1.getFileSystem().supportsLinks()) && (!bool) ? 2048 : 0;
/*     */ 
/* 175 */       if (k != 0)
/*     */       {
/* 177 */         Cancellable local1 = new Cancellable()
/*     */         {
/*     */           public int cancelValue() {
/* 180 */             return 1;
/*     */           }
/*     */ 
/*     */           public void implRun() throws IOException {
/*     */             try {
/* 185 */               WindowsNativeDispatcher.CopyFileEx(this.val$sourcePath, str2, i1, addressToPollForCancel());
/*     */             }
/*     */             catch (WindowsException localWindowsException) {
/* 188 */               localWindowsException.rethrowAsIOException(paramWindowsPath1, paramWindowsPath2);
/*     */             }
/*     */           }
/*     */         };
/*     */         try {
/* 193 */           Cancellable.runInterruptibly(local1);
/*     */         } catch (ExecutionException localExecutionException) {
/* 195 */           Throwable localThrowable = localExecutionException.getCause();
/* 196 */           if ((localThrowable instanceof IOException))
/* 197 */             throw ((IOException)localThrowable);
/* 198 */           throw new IOException(localThrowable);
/*     */         }
/*     */       }
/*     */       else {
/*     */         try {
/* 203 */           WindowsNativeDispatcher.CopyFileEx(str1, str2, i1, 0L);
/*     */         } catch (WindowsException localWindowsException6) {
/* 205 */           localWindowsException6.rethrowAsIOException(paramWindowsPath1, paramWindowsPath2);
/*     */         }
/*     */       }
/* 208 */       if (j != 0)
/*     */         try
/*     */         {
/* 211 */           copySecurityAttributes(paramWindowsPath1, paramWindowsPath2, bool);
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */         }
/* 216 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 221 */       if (localWindowsFileAttributes1.isDirectory()) {
/* 222 */         WindowsNativeDispatcher.CreateDirectory(str2, 0L);
/*     */       } else {
/* 224 */         String str3 = WindowsLinkSupport.readLink(paramWindowsPath1);
/* 225 */         int i2 = 1;
/* 226 */         WindowsNativeDispatcher.CreateSymbolicLink(str2, WindowsPath.addPrefixIfNeeded(str3), i2);
/*     */       }
/*     */     }
/*     */     catch (WindowsException localWindowsException5)
/*     */     {
/* 231 */       localWindowsException5.rethrowAsIOException(paramWindowsPath2);
/*     */     }
/* 233 */     if (j != 0)
/*     */     {
/* 235 */       WindowsFileAttributeViews.Dos localDos = WindowsFileAttributeViews.createDosView(paramWindowsPath2, false);
/*     */       try
/*     */       {
/* 238 */         localDos.setAttributes(localWindowsFileAttributes1);
/*     */       } catch (IOException localIOException2) {
/* 240 */         if (localWindowsFileAttributes1.isDirectory())
/*     */           try {
/* 242 */             WindowsNativeDispatcher.RemoveDirectory(str2);
/*     */           }
/*     */           catch (WindowsException localWindowsException7)
/*     */           {
/*     */           }
/*     */       }
/*     */       try
/*     */       {
/* 250 */         copySecurityAttributes(paramWindowsPath1, paramWindowsPath2, bool);
/*     */       }
/*     */       catch (IOException localIOException3)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void move(WindowsPath paramWindowsPath1, WindowsPath paramWindowsPath2, CopyOption[] paramArrayOfCopyOption)
/*     */     throws IOException
/*     */   {
/* 262 */     int i = 0;
/* 263 */     int j = 0;
/* 264 */     for (Object localObject2 : paramArrayOfCopyOption) {
/* 265 */       if (localObject2 == StandardCopyOption.ATOMIC_MOVE) {
/* 266 */         i = 1;
/*     */       }
/* 269 */       else if (localObject2 == StandardCopyOption.REPLACE_EXISTING) {
/* 270 */         j = 1;
/*     */       }
/* 273 */       else if (localObject2 != LinkOption.NOFOLLOW_LINKS)
/*     */       {
/* 277 */         if (localObject2 == null) throw new NullPointerException();
/* 278 */         throw new UnsupportedOperationException("Unsupported copy option");
/*     */       }
/*     */     }
/* 281 */     ??? = System.getSecurityManager();
/* 282 */     if (??? != null) {
/* 283 */       paramWindowsPath1.checkWrite();
/* 284 */       paramWindowsPath2.checkWrite();
/*     */     }
/*     */ 
/* 287 */     String str1 = asWin32Path(paramWindowsPath1);
/* 288 */     String str2 = asWin32Path(paramWindowsPath2);
/*     */ 
/* 291 */     if (i != 0) {
/*     */       try {
/* 293 */         WindowsNativeDispatcher.MoveFileEx(str1, str2, 1);
/*     */       } catch (WindowsException localWindowsException1) {
/* 295 */         if (localWindowsException1.lastError() == 17) {
/* 296 */           throw new AtomicMoveNotSupportedException(paramWindowsPath1.getPathForExceptionMessage(), paramWindowsPath2.getPathForExceptionMessage(), localWindowsException1.errorString());
/*     */         }
/*     */ 
/* 301 */         localWindowsException1.rethrowAsIOException(paramWindowsPath1, paramWindowsPath2);
/*     */       }
/* 303 */       return;
/*     */     }
/*     */ 
/* 311 */     WindowsFileAttributes localWindowsFileAttributes1 = null;
/* 312 */     WindowsFileAttributes localWindowsFileAttributes2 = null;
/*     */ 
/* 314 */     long l1 = 0L;
/*     */     try {
/* 316 */       l1 = paramWindowsPath1.openForReadAttributeAccess(false);
/*     */     } catch (WindowsException localWindowsException2) {
/* 318 */       localWindowsException2.rethrowAsIOException(paramWindowsPath1);
/*     */     }
/*     */     try
/*     */     {
/*     */       try {
/* 323 */         localWindowsFileAttributes1 = WindowsFileAttributes.readAttributes(l1);
/*     */       } catch (WindowsException localWindowsException3) {
/* 325 */         localWindowsException3.rethrowAsIOException(paramWindowsPath1);
/*     */       }
/*     */ 
/* 329 */       long l2 = 0L;
/*     */       try {
/* 331 */         l2 = paramWindowsPath2.openForReadAttributeAccess(false);
/*     */         try {
/* 333 */           localWindowsFileAttributes2 = WindowsFileAttributes.readAttributes(l2);
/*     */ 
/* 336 */           if (WindowsFileAttributes.isSameFile(localWindowsFileAttributes1, localWindowsFileAttributes2))
/*     */           {
/*     */             return;
/*     */           }
/*     */ 
/* 341 */           if (j == 0)
/* 342 */             throw new FileAlreadyExistsException(paramWindowsPath2.getPathForExceptionMessage());
/*     */         }
/*     */         finally
/*     */         {
/*     */         }
/*     */       }
/*     */       catch (WindowsException localWindowsException9)
/*     */       {
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 354 */       WindowsNativeDispatcher.CloseHandle(l1);
/*     */     }
/*     */ 
/* 358 */     if (localWindowsFileAttributes2 != null) {
/*     */       try {
/* 360 */         if ((localWindowsFileAttributes2.isDirectory()) || (localWindowsFileAttributes2.isDirectoryLink()))
/* 361 */           WindowsNativeDispatcher.RemoveDirectory(str2);
/*     */         else
/* 363 */           WindowsNativeDispatcher.DeleteFile(str2);
/*     */       }
/*     */       catch (WindowsException localWindowsException4) {
/* 366 */         if (localWindowsFileAttributes2.isDirectory())
/*     */         {
/* 369 */           if ((localWindowsException4.lastError() == 145) || (localWindowsException4.lastError() == 183))
/*     */           {
/* 372 */             throw new DirectoryNotEmptyException(paramWindowsPath2.getPathForExceptionMessage());
/*     */           }
/*     */         }
/*     */ 
/* 376 */         localWindowsException4.rethrowAsIOException(paramWindowsPath2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 383 */       WindowsNativeDispatcher.MoveFileEx(str1, str2, 0);
/* 384 */       return;
/*     */     } catch (WindowsException localWindowsException5) {
/* 386 */       if (localWindowsException5.lastError() != 17) {
/* 387 */         localWindowsException5.rethrowAsIOException(paramWindowsPath1, paramWindowsPath2);
/*     */       }
/*     */ 
/* 391 */       if ((!localWindowsFileAttributes1.isDirectory()) && (!localWindowsFileAttributes1.isDirectoryLink())) {
/*     */         try {
/* 393 */           WindowsNativeDispatcher.MoveFileEx(str1, str2, 2);
/*     */         } catch (WindowsException localWindowsException6) {
/* 395 */           localWindowsException6.rethrowAsIOException(paramWindowsPath1, paramWindowsPath2);
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 400 */           copySecurityAttributes(paramWindowsPath1, paramWindowsPath2, false);
/*     */         }
/*     */         catch (IOException localIOException1) {
/*     */         }
/* 404 */         return;
/*     */       }
/*     */ 
/* 408 */       assert ((localWindowsFileAttributes1.isDirectory()) || (localWindowsFileAttributes1.isDirectoryLink()));
/*     */       try
/*     */       {
/* 412 */         if (localWindowsFileAttributes1.isDirectory()) {
/* 413 */           WindowsNativeDispatcher.CreateDirectory(str2, 0L);
/*     */         } else {
/* 415 */           String str3 = WindowsLinkSupport.readLink(paramWindowsPath1);
/* 416 */           WindowsNativeDispatcher.CreateSymbolicLink(str2, WindowsPath.addPrefixIfNeeded(str3), 1);
/*     */         }
/*     */       }
/*     */       catch (WindowsException localWindowsException7)
/*     */       {
/* 421 */         localWindowsException7.rethrowAsIOException(paramWindowsPath2);
/*     */       }
/*     */ 
/* 425 */       WindowsFileAttributeViews.Dos localDos = WindowsFileAttributeViews.createDosView(paramWindowsPath2, false);
/*     */       try
/*     */       {
/* 428 */         localDos.setAttributes(localWindowsFileAttributes1);
/*     */       }
/*     */       catch (IOException localIOException2) {
/*     */         try {
/* 432 */           WindowsNativeDispatcher.RemoveDirectory(str2); } catch (WindowsException localWindowsException10) {
/*     */         }
/* 434 */         throw localIOException2;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 440 */         copySecurityAttributes(paramWindowsPath1, paramWindowsPath2, false);
/*     */       }
/*     */       catch (IOException localIOException3) {
/*     */       }
/*     */       try {
/* 445 */         WindowsNativeDispatcher.RemoveDirectory(str1);
/*     */       }
/*     */       catch (WindowsException localWindowsException8) {
/*     */         try {
/* 449 */           WindowsNativeDispatcher.RemoveDirectory(str2);
/*     */         }
/*     */         catch (WindowsException localWindowsException11) {
/*     */         }
/* 453 */         if ((localWindowsException8.lastError() == 145) || (localWindowsException8.lastError() == 183))
/*     */         {
/* 456 */           throw new DirectoryNotEmptyException(paramWindowsPath2.getPathForExceptionMessage());
/*     */         }
/*     */ 
/* 459 */         localWindowsException8.rethrowAsIOException(paramWindowsPath1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String asWin32Path(WindowsPath paramWindowsPath) throws IOException {
/*     */     try {
/* 466 */       return paramWindowsPath.getPathForWin32Calls();
/*     */     } catch (WindowsException localWindowsException) {
/* 468 */       localWindowsException.rethrowAsIOException(paramWindowsPath);
/* 469 */     }return null;
/*     */   }
/*     */ 
/*     */   private static void copySecurityAttributes(WindowsPath paramWindowsPath1, WindowsPath paramWindowsPath2, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 481 */     String str = WindowsLinkSupport.getFinalPath(paramWindowsPath1, paramBoolean);
/*     */ 
/* 484 */     WindowsSecurity.Privilege localPrivilege = WindowsSecurity.enablePrivilege("SeRestorePrivilege");
/*     */     try
/*     */     {
/* 487 */       int i = 7;
/*     */ 
/* 489 */       NativeBuffer localNativeBuffer = WindowsAclFileAttributeView.getFileSecurity(str, i);
/*     */       try
/*     */       {
/*     */         try {
/* 493 */           WindowsNativeDispatcher.SetFileSecurity(paramWindowsPath2.getPathForWin32Calls(), i, localNativeBuffer.address());
/*     */         }
/*     */         catch (WindowsException localWindowsException) {
/* 496 */           localWindowsException.rethrowAsIOException(paramWindowsPath2);
/*     */         }
/*     */       } finally {
/* 499 */         localNativeBuffer.release();
/*     */       }
/*     */     } finally {
/* 502 */       localPrivilege.drop();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsFileCopy
 * JD-Core Version:    0.6.2
 */