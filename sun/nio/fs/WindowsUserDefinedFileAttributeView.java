/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class WindowsUserDefinedFileAttributeView extends AbstractUserDefinedFileAttributeView
/*     */ {
/*  46 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private final WindowsPath file;
/*     */   private final boolean followLinks;
/*     */ 
/*     */   private String join(String paramString1, String paramString2)
/*     */   {
/*  50 */     if (paramString2 == null)
/*  51 */       throw new NullPointerException("'name' is null");
/*  52 */     return paramString1 + ":" + paramString2;
/*     */   }
/*     */   private String join(WindowsPath paramWindowsPath, String paramString) throws WindowsException {
/*  55 */     return join(paramWindowsPath.getPathForWin32Calls(), paramString);
/*     */   }
/*     */ 
/*     */   WindowsUserDefinedFileAttributeView(WindowsPath paramWindowsPath, boolean paramBoolean)
/*     */   {
/*  62 */     this.file = paramWindowsPath;
/*  63 */     this.followLinks = paramBoolean;
/*     */   }
/*     */ 
/*     */   private List<String> listUsingStreamEnumeration() throws IOException
/*     */   {
/*  68 */     ArrayList localArrayList = new ArrayList();
/*     */     try {
/*  70 */       WindowsNativeDispatcher.FirstStream localFirstStream = WindowsNativeDispatcher.FindFirstStream(this.file.getPathForWin32Calls());
/*  71 */       if (localFirstStream != null) {
/*  72 */         long l = localFirstStream.handle();
/*     */         try
/*     */         {
/*  75 */           String str = localFirstStream.name();
/*     */           String[] arrayOfString;
/*  76 */           if (!str.equals("::$DATA")) {
/*  77 */             arrayOfString = str.split(":");
/*  78 */             localArrayList.add(arrayOfString[1]);
/*     */           }
/*  80 */           while ((str = WindowsNativeDispatcher.FindNextStream(l)) != null) {
/*  81 */             arrayOfString = str.split(":");
/*  82 */             localArrayList.add(arrayOfString[1]);
/*     */           }
/*     */         } finally {
/*  85 */           WindowsNativeDispatcher.FindClose(l);
/*     */         }
/*     */       }
/*     */     } catch (WindowsException localWindowsException) {
/*  89 */       localWindowsException.rethrowAsIOException(this.file);
/*     */     }
/*  91 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   private List<String> listUsingBackupRead()
/*     */     throws IOException
/*     */   {
/*  97 */     long l1 = -1L;
/*     */     try {
/*  99 */       int i = 33554432;
/* 100 */       if ((!this.followLinks) && (this.file.getFileSystem().supportsLinks())) {
/* 101 */         i |= 2097152;
/*     */       }
/* 103 */       l1 = WindowsNativeDispatcher.CreateFile(this.file.getPathForWin32Calls(), -2147483648, 1, 3, i);
/*     */     }
/*     */     catch (WindowsException localWindowsException1)
/*     */     {
/* 109 */       localWindowsException1.rethrowAsIOException(this.file);
/*     */     }
/*     */ 
/* 114 */     NativeBuffer localNativeBuffer = null;
/*     */ 
/* 117 */     ArrayList localArrayList = new ArrayList();
/*     */     try
/*     */     {
/* 120 */       localNativeBuffer = NativeBuffers.getNativeBuffer(4096);
/* 121 */       long l2 = localNativeBuffer.address();
/*     */ 
/* 137 */       long l3 = 0L;
/*     */       try
/*     */       {
/*     */         while (true) {
/* 141 */           WindowsNativeDispatcher.BackupResult localBackupResult = WindowsNativeDispatcher.BackupRead(l1, l2, 20, false, l3);
/*     */ 
/* 143 */           l3 = localBackupResult.context();
/* 144 */           if (localBackupResult.bytesTransferred() == 0) {
/*     */             break;
/*     */           }
/* 147 */           int j = unsafe.getInt(l2 + 0L);
/* 148 */           long l4 = unsafe.getLong(l2 + 8L);
/* 149 */           int k = unsafe.getInt(l2 + 16L);
/*     */ 
/* 152 */           if (k > 0) {
/* 153 */             localBackupResult = WindowsNativeDispatcher.BackupRead(l1, l2, k, false, l3);
/* 154 */             if (localBackupResult.bytesTransferred() != k)
/*     */             {
/*     */               break;
/*     */             }
/*     */           }
/* 159 */           if (j == 4) {
/* 160 */             char[] arrayOfChar = new char[k / 2];
/* 161 */             unsafe.copyMemory(null, l2, arrayOfChar, Unsafe.ARRAY_CHAR_BASE_OFFSET, k);
/*     */ 
/* 164 */             String[] arrayOfString = new String(arrayOfChar).split(":");
/* 165 */             if (arrayOfString.length == 3) {
/* 166 */               localArrayList.add(arrayOfString[1]);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 171 */           if (j == 9) {
/* 172 */             throw new IOException("Spare blocks not handled");
/*     */           }
/*     */ 
/* 176 */           if (l4 > 0L) {
/* 177 */             WindowsNativeDispatcher.BackupSeek(l1, l4, l3);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 185 */         if (l3 != 0L)
/*     */           try {
/* 187 */             WindowsNativeDispatcher.BackupRead(l1, 0L, 0, true, l3);
/*     */           }
/*     */           catch (WindowsException localWindowsException2)
/*     */           {
/*     */           }
/*     */       }
/*     */       catch (WindowsException localWindowsException3)
/*     */       {
/* 182 */         throw new IOException(localWindowsException3.errorString());
/*     */       }
/*     */       finally {
/* 185 */         if (l3 != 0L)
/*     */           try {
/* 187 */             WindowsNativeDispatcher.BackupRead(l1, 0L, 0, true, l3);
/*     */           } catch (WindowsException localWindowsException4) {
/*     */           }
/*     */       }
/*     */     } finally {
/* 192 */       if (localNativeBuffer != null)
/* 193 */         localNativeBuffer.release();
/* 194 */       WindowsNativeDispatcher.CloseHandle(l1);
/*     */     }
/* 196 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List<String> list() throws IOException
/*     */   {
/* 201 */     if (System.getSecurityManager() != null) {
/* 202 */       checkAccess(this.file.getPathForPermissionCheck(), true, false);
/*     */     }
/* 204 */     if (this.file.getFileSystem().supportsStreamEnumeration()) {
/* 205 */       return listUsingStreamEnumeration();
/*     */     }
/* 207 */     return listUsingBackupRead();
/*     */   }
/*     */ 
/*     */   public int size(String paramString)
/*     */     throws IOException
/*     */   {
/* 213 */     if (System.getSecurityManager() != null) {
/* 214 */       checkAccess(this.file.getPathForPermissionCheck(), true, false);
/*     */     }
/*     */ 
/* 217 */     FileChannel localFileChannel = null;
/*     */     try {
/* 219 */       HashSet localHashSet = new HashSet();
/* 220 */       localHashSet.add(StandardOpenOption.READ);
/* 221 */       if (!this.followLinks)
/* 222 */         localHashSet.add(WindowsChannelFactory.OPEN_REPARSE_POINT);
/* 223 */       localFileChannel = WindowsChannelFactory.newFileChannel(join(this.file, paramString), null, localHashSet, 0L);
/*     */     }
/*     */     catch (WindowsException localWindowsException) {
/* 226 */       localWindowsException.rethrowAsIOException(join(this.file.getPathForPermissionCheck(), paramString));
/*     */     }
/*     */     try {
/* 229 */       long l = localFileChannel.size();
/* 230 */       if (l > 2147483647L)
/* 231 */         throw new ArithmeticException("Stream too large");
/* 232 */       return (int)l;
/*     */     } finally {
/* 234 */       localFileChannel.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read(String paramString, ByteBuffer paramByteBuffer) throws IOException
/*     */   {
/* 240 */     if (System.getSecurityManager() != null) {
/* 241 */       checkAccess(this.file.getPathForPermissionCheck(), true, false);
/*     */     }
/*     */ 
/* 244 */     FileChannel localFileChannel = null;
/*     */     try {
/* 246 */       HashSet localHashSet = new HashSet();
/* 247 */       localHashSet.add(StandardOpenOption.READ);
/* 248 */       if (!this.followLinks)
/* 249 */         localHashSet.add(WindowsChannelFactory.OPEN_REPARSE_POINT);
/* 250 */       localFileChannel = WindowsChannelFactory.newFileChannel(join(this.file, paramString), null, localHashSet, 0L);
/*     */     }
/*     */     catch (WindowsException localWindowsException) {
/* 253 */       localWindowsException.rethrowAsIOException(join(this.file.getPathForPermissionCheck(), paramString));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 258 */       if (localFileChannel.size() > paramByteBuffer.remaining())
/* 259 */         throw new IOException("Stream too large");
/* 260 */       int i = 0;
/*     */       int j;
/* 261 */       while (paramByteBuffer.hasRemaining()) {
/* 262 */         j = localFileChannel.read(paramByteBuffer);
/* 263 */         if (j < 0)
/*     */           break;
/* 265 */         i += j;
/*     */       }
/* 267 */       return i;
/*     */     } finally {
/* 269 */       localFileChannel.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int write(String paramString, ByteBuffer paramByteBuffer) throws IOException
/*     */   {
/* 275 */     if (System.getSecurityManager() != null) {
/* 276 */       checkAccess(this.file.getPathForPermissionCheck(), false, true);
/*     */     }
/*     */ 
/* 286 */     long l = -1L;
/*     */     try {
/* 288 */       int i = 33554432;
/* 289 */       if (!this.followLinks) {
/* 290 */         i |= 2097152;
/*     */       }
/* 292 */       l = WindowsNativeDispatcher.CreateFile(this.file.getPathForWin32Calls(), -2147483648, 7, 3, i);
/*     */     }
/*     */     catch (WindowsException localWindowsException1)
/*     */     {
/* 298 */       localWindowsException1.rethrowAsIOException(this.file);
/*     */     }
/*     */     try {
/* 301 */       HashSet localHashSet = new HashSet();
/* 302 */       if (!this.followLinks)
/* 303 */         localHashSet.add(WindowsChannelFactory.OPEN_REPARSE_POINT);
/* 304 */       localHashSet.add(StandardOpenOption.CREATE);
/* 305 */       localHashSet.add(StandardOpenOption.WRITE);
/* 306 */       localHashSet.add(StandardOpenOption.TRUNCATE_EXISTING);
/* 307 */       FileChannel localFileChannel = null;
/*     */       try {
/* 309 */         localFileChannel = WindowsChannelFactory.newFileChannel(join(this.file, paramString), null, localHashSet, 0L);
/*     */       }
/*     */       catch (WindowsException localWindowsException2) {
/* 312 */         localWindowsException2.rethrowAsIOException(join(this.file.getPathForPermissionCheck(), paramString));
/*     */       }
/*     */       try
/*     */       {
/* 316 */         int j = paramByteBuffer.remaining();
/* 317 */         while (paramByteBuffer.hasRemaining()) {
/* 318 */           localFileChannel.write(paramByteBuffer);
/*     */         }
/* 320 */         int k = j;
/*     */ 
/* 322 */         localFileChannel.close();
/*     */ 
/* 325 */         return k;
/*     */       }
/*     */       finally
/*     */       {
/* 322 */         localFileChannel.close();
/*     */       }
/*     */     } finally {
/* 325 */       WindowsNativeDispatcher.CloseHandle(l);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(String paramString) throws IOException
/*     */   {
/* 331 */     if (System.getSecurityManager() != null) {
/* 332 */       checkAccess(this.file.getPathForPermissionCheck(), false, true);
/*     */     }
/* 334 */     String str1 = WindowsLinkSupport.getFinalPath(this.file, this.followLinks);
/* 335 */     String str2 = join(str1, paramString);
/*     */     try {
/* 337 */       WindowsNativeDispatcher.DeleteFile(str2);
/*     */     } catch (WindowsException localWindowsException) {
/* 339 */       localWindowsException.rethrowAsIOException(str2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsUserDefinedFileAttributeView
 * JD-Core Version:    0.6.2
 */