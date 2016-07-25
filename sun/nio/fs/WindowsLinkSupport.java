/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOError;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileSystemException;
/*     */ import java.nio.file.NotLinkException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class WindowsLinkSupport
/*     */ {
/*  43 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */ 
/*     */   static String readLink(WindowsPath paramWindowsPath)
/*     */     throws IOException
/*     */   {
/*  52 */     long l = 0L;
/*     */     try {
/*  54 */       l = paramWindowsPath.openForReadAttributeAccess(false);
/*     */     } catch (WindowsException localWindowsException) {
/*  56 */       localWindowsException.rethrowAsIOException(paramWindowsPath);
/*     */     }
/*     */     try {
/*  59 */       return readLinkImpl(l);
/*     */     } finally {
/*  61 */       WindowsNativeDispatcher.CloseHandle(l);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getFinalPath(WindowsPath paramWindowsPath)
/*     */     throws IOException
/*     */   {
/*  70 */     long l = 0L;
/*     */     try {
/*  72 */       l = paramWindowsPath.openForReadAttributeAccess(true);
/*     */     } catch (WindowsException localWindowsException1) {
/*  74 */       localWindowsException1.rethrowAsIOException(paramWindowsPath);
/*     */     }
/*     */     try {
/*  77 */       return stripPrefix(WindowsNativeDispatcher.GetFinalPathNameByHandle(l));
/*     */     }
/*     */     catch (WindowsException localWindowsException2)
/*     */     {
/*  81 */       if (localWindowsException2.lastError() != 124)
/*  82 */         localWindowsException2.rethrowAsIOException(paramWindowsPath);
/*     */     } finally {
/*  84 */       WindowsNativeDispatcher.CloseHandle(l);
/*     */     }
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   static String getFinalPath(WindowsPath paramWindowsPath, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  96 */     WindowsFileSystem localWindowsFileSystem = paramWindowsPath.getFileSystem();
/*     */     try
/*     */     {
/*  99 */       if ((!paramBoolean) || (!localWindowsFileSystem.supportsLinks())) {
/* 100 */         return paramWindowsPath.getPathForWin32Calls();
/*     */       }
/*     */ 
/* 103 */       if (!WindowsFileAttributes.get(paramWindowsPath, false).isSymbolicLink())
/* 104 */         return paramWindowsPath.getPathForWin32Calls();
/*     */     }
/*     */     catch (WindowsException localWindowsException1) {
/* 107 */       localWindowsException1.rethrowAsIOException(paramWindowsPath);
/*     */     }
/*     */ 
/* 111 */     String str = getFinalPath(paramWindowsPath);
/* 112 */     if (str != null) {
/* 113 */       return str;
/*     */     }
/*     */ 
/* 117 */     WindowsPath localWindowsPath1 = paramWindowsPath;
/* 118 */     int i = 0;
/*     */     do {
/*     */       try {
/* 121 */         WindowsFileAttributes localWindowsFileAttributes = WindowsFileAttributes.get(localWindowsPath1, false);
/*     */ 
/* 124 */         if (!localWindowsFileAttributes.isSymbolicLink())
/* 125 */           return localWindowsPath1.getPathForWin32Calls();
/*     */       }
/*     */       catch (WindowsException localWindowsException2) {
/* 128 */         localWindowsException2.rethrowAsIOException(localWindowsPath1);
/*     */       }
/* 130 */       WindowsPath localWindowsPath2 = WindowsPath.createFromNormalizedPath(localWindowsFileSystem, readLink(localWindowsPath1));
/*     */ 
/* 132 */       WindowsPath localWindowsPath3 = localWindowsPath1.getParent();
/* 133 */       if (localWindowsPath3 == null)
/*     */       {
/* 135 */         WindowsPath localWindowsPath4 = localWindowsPath1;
/* 136 */         localWindowsPath1 = (WindowsPath)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public WindowsPath run()
/*     */           {
/* 140 */             return this.val$t.toAbsolutePath();
/*     */           }
/*     */         });
/* 142 */         localWindowsPath3 = localWindowsPath1.getParent();
/*     */       }
/* 144 */       localWindowsPath1 = localWindowsPath3.resolve(localWindowsPath2);
/*     */ 
/* 146 */       i++; } while (i < 32);
/*     */ 
/* 148 */     throw new FileSystemException(paramWindowsPath.getPathForExceptionMessage(), null, "Too many links");
/*     */   }
/*     */ 
/*     */   static String getRealPath(WindowsPath paramWindowsPath, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 159 */     WindowsFileSystem localWindowsFileSystem = paramWindowsPath.getFileSystem();
/* 160 */     if ((paramBoolean) && (!localWindowsFileSystem.supportsLinks())) {
/* 161 */       paramBoolean = false;
/*     */     }
/*     */ 
/* 164 */     String str1 = null;
/*     */     try {
/* 166 */       str1 = paramWindowsPath.toAbsolutePath().toString();
/*     */     } catch (IOError localIOError) {
/* 168 */       throw ((IOException)localIOError.getCause());
/*     */     }
/*     */ 
/* 172 */     if (str1.indexOf('.') >= 0) {
/*     */       try {
/* 174 */         str1 = WindowsNativeDispatcher.GetFullPathName(str1);
/*     */       } catch (WindowsException localWindowsException1) {
/* 176 */         localWindowsException1.rethrowAsIOException(paramWindowsPath);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 181 */     StringBuilder localStringBuilder = new StringBuilder(str1.length());
/*     */ 
/* 185 */     char c = str1.charAt(0);
/* 186 */     int i = str1.charAt(1);
/*     */     String str2;
/* 187 */     if (((c <= 'z') && (c >= 'a')) || ((c <= 'Z') && (c >= 'A') && (i == 58) && (str1.charAt(2) == '\\')))
/*     */     {
/* 190 */       localStringBuilder.append(Character.toUpperCase(c));
/* 191 */       localStringBuilder.append(":\\");
/* 192 */       str2 = 3;
/* 193 */     } else if ((c == '\\') && (i == 92))
/*     */     {
/* 195 */       int j = str1.length() - 1;
/* 196 */       int m = str1.indexOf('\\', 2);
/*     */ 
/* 198 */       if ((m == -1) || (m == j))
/*     */       {
/* 200 */         throw new FileSystemException(paramWindowsPath.getPathForExceptionMessage(), null, "UNC has invalid share");
/*     */       }
/*     */ 
/* 203 */       m = str1.indexOf('\\', m + 1);
/* 204 */       if (m < 0) {
/* 205 */         m = j;
/* 206 */         localStringBuilder.append(str1).append("\\");
/*     */       } else {
/* 208 */         localStringBuilder.append(str1, 0, m + 1);
/*     */       }
/* 210 */       str2 = m + 1;
/*     */     } else {
/* 212 */       throw new AssertionError("path type not recognized");
/*     */     }
/*     */ 
/* 216 */     if (str2 >= str1.length()) {
/* 217 */       str3 = localStringBuilder.toString();
/*     */       try {
/* 219 */         WindowsNativeDispatcher.GetFileAttributes(str3);
/*     */       } catch (WindowsException localWindowsException2) {
/* 221 */         localWindowsException2.rethrowAsIOException(str1);
/*     */       }
/* 223 */       return str3;
/*     */     }
/*     */ 
/* 228 */     String str3 = str2;
/* 229 */     while (str3 < str1.length()) {
/* 230 */       int n = str1.indexOf('\\', str3);
/* 231 */       int i1 = n == -1 ? str1.length() : n;
/* 232 */       String str4 = localStringBuilder.toString() + str1.substring(str3, i1);
/*     */       try {
/* 234 */         WindowsNativeDispatcher.FirstFile localFirstFile = WindowsNativeDispatcher.FindFirstFile(WindowsPath.addPrefixIfNeeded(str4));
/* 235 */         WindowsNativeDispatcher.FindClose(localFirstFile.handle());
/*     */ 
/* 239 */         if ((paramBoolean) && (WindowsFileAttributes.isReparsePoint(localFirstFile.attributes())))
/*     */         {
/* 242 */           String str5 = getFinalPath(paramWindowsPath);
/*     */           WindowsPath localWindowsPath;
/* 243 */           if (str5 == null)
/*     */           {
/* 246 */             localWindowsPath = resolveAllLinks(WindowsPath.createFromNormalizedPath(localWindowsFileSystem, str1));
/*     */           }
/* 248 */           return getRealPath(localWindowsPath, false);
/*     */         }
/*     */ 
/* 254 */         localStringBuilder.append(localFirstFile.name());
/* 255 */         if (n != -1)
/* 256 */           localStringBuilder.append('\\');
/*     */       }
/*     */       catch (WindowsException localWindowsException3) {
/* 259 */         localWindowsException3.rethrowAsIOException(str1);
/*     */       }
/* 261 */       int k = i1 + 1;
/*     */     }
/*     */ 
/* 264 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static String readLinkImpl(long paramLong)
/*     */     throws IOException
/*     */   {
/* 272 */     int i = 16384;
/* 273 */     NativeBuffer localNativeBuffer = NativeBuffers.getNativeBuffer(i);
/*     */     try {
/*     */       try {
/* 276 */         WindowsNativeDispatcher.DeviceIoControlGetReparsePoint(paramLong, localNativeBuffer.address(), i);
/*     */       }
/*     */       catch (WindowsException localWindowsException) {
/* 279 */         if (localWindowsException.lastError() == 4390)
/* 280 */           throw new NotLinkException(null, null, localWindowsException.errorString());
/* 281 */         localWindowsException.rethrowAsIOException((String)null);
/*     */       }
/*     */ 
/* 315 */       int j = (int)unsafe.getLong(localNativeBuffer.address() + 0L);
/* 316 */       if (j != -1610612724)
/*     */       {
/* 318 */         throw new NotLinkException(null, null, "Reparse point is not a symbolic link");
/*     */       }
/*     */ 
/* 322 */       int k = unsafe.getShort(localNativeBuffer.address() + 8L);
/* 323 */       int m = unsafe.getShort(localNativeBuffer.address() + 10L);
/* 324 */       if (m % 2 != 0) {
/* 325 */         throw new FileSystemException(null, null, "Symbolic link corrupted");
/*     */       }
/*     */ 
/* 328 */       char[] arrayOfChar = new char[m / 2];
/* 329 */       unsafe.copyMemory(null, localNativeBuffer.address() + 20L + k, arrayOfChar, Unsafe.ARRAY_CHAR_BASE_OFFSET, m);
/*     */ 
/* 333 */       String str1 = stripPrefix(new String(arrayOfChar));
/* 334 */       if (str1.length() == 0) {
/* 335 */         throw new IOException("Symbolic link target is invalid");
/*     */       }
/* 337 */       return str1;
/*     */     } finally {
/* 339 */       localNativeBuffer.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static WindowsPath resolveAllLinks(WindowsPath paramWindowsPath)
/*     */     throws IOException
/*     */   {
/* 349 */     assert (paramWindowsPath.isAbsolute());
/* 350 */     WindowsFileSystem localWindowsFileSystem = paramWindowsPath.getFileSystem();
/*     */ 
/* 354 */     int i = 0;
/* 355 */     int j = 0;
/* 356 */     while (j < paramWindowsPath.getNameCount()) {
/* 357 */       WindowsPath localWindowsPath1 = paramWindowsPath.getRoot().resolve(paramWindowsPath.subpath(0, j + 1));
/*     */ 
/* 359 */       WindowsFileAttributes localWindowsFileAttributes = null;
/*     */       try {
/* 361 */         localWindowsFileAttributes = WindowsFileAttributes.get(localWindowsPath1, false);
/*     */       } catch (WindowsException localWindowsException1) {
/* 363 */         localWindowsException1.rethrowAsIOException(localWindowsPath1);
/*     */       }
/*     */ 
/* 373 */       if (localWindowsFileAttributes.isSymbolicLink()) {
/* 374 */         i++;
/* 375 */         if (i > 32)
/* 376 */           throw new IOException("Too many links");
/* 377 */         WindowsPath localWindowsPath2 = WindowsPath.createFromNormalizedPath(localWindowsFileSystem, readLink(localWindowsPath1));
/*     */ 
/* 379 */         WindowsPath localWindowsPath3 = null;
/* 380 */         int k = paramWindowsPath.getNameCount();
/* 381 */         if (j + 1 < k) {
/* 382 */           localWindowsPath3 = paramWindowsPath.subpath(j + 1, k);
/*     */         }
/* 384 */         paramWindowsPath = localWindowsPath1.getParent().resolve(localWindowsPath2);
/*     */         try {
/* 386 */           String str = WindowsNativeDispatcher.GetFullPathName(paramWindowsPath.toString());
/* 387 */           if (!str.equals(paramWindowsPath.toString()))
/* 388 */             paramWindowsPath = WindowsPath.createFromNormalizedPath(localWindowsFileSystem, str);
/*     */         }
/*     */         catch (WindowsException localWindowsException2) {
/* 391 */           localWindowsException2.rethrowAsIOException(paramWindowsPath);
/*     */         }
/* 393 */         if (localWindowsPath3 != null) {
/* 394 */           paramWindowsPath = paramWindowsPath.resolve(localWindowsPath3);
/*     */         }
/*     */ 
/* 398 */         j = 0;
/*     */       }
/*     */       else {
/* 401 */         j++;
/*     */       }
/*     */     }
/*     */ 
/* 405 */     return paramWindowsPath;
/*     */   }
/*     */ 
/*     */   private static String stripPrefix(String paramString)
/*     */   {
/* 413 */     if (paramString.startsWith("\\\\?\\")) {
/* 414 */       if (paramString.startsWith("\\\\?\\UNC\\"))
/* 415 */         paramString = "\\" + paramString.substring(7);
/*     */       else {
/* 417 */         paramString = paramString.substring(4);
/*     */       }
/* 419 */       return paramString;
/*     */     }
/*     */ 
/* 423 */     if (paramString.startsWith("\\??\\")) {
/* 424 */       if (paramString.startsWith("\\??\\UNC\\"))
/* 425 */         paramString = "\\" + paramString.substring(7);
/*     */       else {
/* 427 */         paramString = paramString.substring(4);
/*     */       }
/* 429 */       return paramString;
/*     */     }
/* 431 */     return paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsLinkSupport
 * JD-Core Version:    0.6.2
 */