/*     */ package sun.nio.fs;
/*     */ 
/*     */ import com.sun.nio.file.ExtendedOpenOption;
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Set;
/*     */ import sun.misc.JavaIOFileDescriptorAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.nio.ch.FileChannelImpl;
/*     */ import sun.nio.ch.ThreadPool;
/*     */ import sun.nio.ch.WindowsAsynchronousFileChannelImpl;
/*     */ 
/*     */ class WindowsChannelFactory
/*     */ {
/*  50 */   private static final JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
/*     */ 
/*  59 */   static final OpenOption OPEN_REPARSE_POINT = new OpenOption() { } ;
/*     */ 
/*     */   static FileChannel newFileChannel(String paramString1, String paramString2, Set<? extends OpenOption> paramSet, long paramLong)
/*     */     throws WindowsException
/*     */   {
/* 142 */     Flags localFlags = Flags.toFlags(paramSet);
/*     */ 
/* 145 */     if ((!localFlags.read) && (!localFlags.write)) {
/* 146 */       if (localFlags.append)
/* 147 */         localFlags.write = true;
/*     */       else {
/* 149 */         localFlags.read = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 154 */     if ((localFlags.read) && (localFlags.append))
/* 155 */       throw new IllegalArgumentException("READ + APPEND not allowed");
/* 156 */     if ((localFlags.append) && (localFlags.truncateExisting)) {
/* 157 */       throw new IllegalArgumentException("APPEND + TRUNCATE_EXISTING not allowed");
/*     */     }
/* 159 */     FileDescriptor localFileDescriptor = open(paramString1, paramString2, localFlags, paramLong);
/* 160 */     return FileChannelImpl.open(localFileDescriptor, paramString1, localFlags.read, localFlags.write, localFlags.append, null);
/*     */   }
/*     */ 
/*     */   static AsynchronousFileChannel newAsynchronousFileChannel(String paramString1, String paramString2, Set<? extends OpenOption> paramSet, long paramLong, ThreadPool paramThreadPool)
/*     */     throws IOException
/*     */   {
/* 180 */     Flags localFlags = Flags.toFlags(paramSet);
/*     */ 
/* 183 */     localFlags.overlapped = true;
/*     */ 
/* 186 */     if ((!localFlags.read) && (!localFlags.write)) {
/* 187 */       localFlags.read = true;
/*     */     }
/*     */ 
/* 191 */     if (localFlags.append) {
/* 192 */       throw new UnsupportedOperationException("APPEND not allowed");
/*     */     }
/*     */     FileDescriptor localFileDescriptor;
/*     */     try
/*     */     {
/* 197 */       localFileDescriptor = open(paramString1, paramString2, localFlags, paramLong);
/*     */     } catch (WindowsException localWindowsException) {
/* 199 */       localWindowsException.rethrowAsIOException(paramString1);
/* 200 */       return null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 205 */       return WindowsAsynchronousFileChannelImpl.open(localFileDescriptor, localFlags.read, localFlags.write, paramThreadPool);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 209 */       long l = fdAccess.getHandle(localFileDescriptor);
/* 210 */       WindowsNativeDispatcher.CloseHandle(l);
/* 211 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static FileDescriptor open(String paramString1, String paramString2, Flags paramFlags, long paramLong)
/*     */     throws WindowsException
/*     */   {
/* 226 */     int i = 0;
/*     */ 
/* 229 */     int j = 0;
/* 230 */     if (paramFlags.read)
/* 231 */       j |= -2147483648;
/* 232 */     if (paramFlags.write) {
/* 233 */       j |= 1073741824;
/*     */     }
/* 235 */     int k = 0;
/* 236 */     if (paramFlags.shareRead)
/* 237 */       k |= 1;
/* 238 */     if (paramFlags.shareWrite)
/* 239 */       k |= 2;
/* 240 */     if (paramFlags.shareDelete) {
/* 241 */       k |= 4;
/*     */     }
/* 243 */     int m = 128;
/* 244 */     int n = 3;
/* 245 */     if (paramFlags.write) {
/* 246 */       if (paramFlags.createNew) {
/* 247 */         n = 1;
/*     */ 
/* 249 */         m |= 2097152;
/*     */       } else {
/* 251 */         if (paramFlags.create)
/* 252 */           n = 4;
/* 253 */         if (paramFlags.truncateExisting)
/*     */         {
/* 257 */           if (n == 4)
/* 258 */             i = 1;
/*     */           else {
/* 260 */             n = 5;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 266 */     if ((paramFlags.dsync) || (paramFlags.sync))
/* 267 */       m |= -2147483648;
/* 268 */     if (paramFlags.overlapped)
/* 269 */       m |= 1073741824;
/* 270 */     if (paramFlags.deleteOnClose) {
/* 271 */       m |= 67108864;
/*     */     }
/*     */ 
/* 274 */     int i1 = 1;
/* 275 */     if ((n != 1) && ((paramFlags.noFollowLinks) || (paramFlags.openReparsePoint) || (paramFlags.deleteOnClose)))
/*     */     {
/* 280 */       if ((paramFlags.noFollowLinks) || (paramFlags.deleteOnClose))
/* 281 */         i1 = 0;
/* 282 */       m |= 2097152;
/*     */     }
/*     */ 
/* 286 */     if (paramString2 != null) {
/* 287 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 288 */       if (localSecurityManager != null) {
/* 289 */         if (paramFlags.read)
/* 290 */           localSecurityManager.checkRead(paramString2);
/* 291 */         if (paramFlags.write)
/* 292 */           localSecurityManager.checkWrite(paramString2);
/* 293 */         if (paramFlags.deleteOnClose) {
/* 294 */           localSecurityManager.checkDelete(paramString2);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 299 */     long l = WindowsNativeDispatcher.CreateFile(paramString1, j, k, paramLong, n, m);
/*     */ 
/* 307 */     if (i1 == 0) {
/*     */       try {
/* 309 */         if (WindowsFileAttributes.readAttributes(l).isSymbolicLink())
/* 310 */           throw new WindowsException("File is symbolic link");
/*     */       } catch (WindowsException localWindowsException1) {
/* 312 */         WindowsNativeDispatcher.CloseHandle(l);
/* 313 */         throw localWindowsException1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 318 */     if (i != 0) {
/*     */       try {
/* 320 */         WindowsNativeDispatcher.SetEndOfFile(l);
/*     */       } catch (WindowsException localWindowsException2) {
/* 322 */         WindowsNativeDispatcher.CloseHandle(l);
/* 323 */         throw localWindowsException2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 328 */     if ((n == 1) && (paramFlags.sparse)) {
/*     */       try {
/* 330 */         WindowsNativeDispatcher.DeviceIoControlSetSparse(l);
/*     */       }
/*     */       catch (WindowsException localWindowsException3)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 337 */     FileDescriptor localFileDescriptor = new FileDescriptor();
/* 338 */     fdAccess.setHandle(localFileDescriptor, l);
/* 339 */     return localFileDescriptor;
/*     */   }
/*     */ 
/*     */   private static class Flags
/*     */   {
/*     */     boolean read;
/*     */     boolean write;
/*     */     boolean append;
/*     */     boolean truncateExisting;
/*     */     boolean create;
/*     */     boolean createNew;
/*     */     boolean deleteOnClose;
/*     */     boolean sparse;
/*     */     boolean overlapped;
/*     */     boolean sync;
/*     */     boolean dsync;
/*  78 */     boolean shareRead = true;
/*  79 */     boolean shareWrite = true;
/*  80 */     boolean shareDelete = true;
/*     */     boolean noFollowLinks;
/*     */     boolean openReparsePoint;
/*     */ 
/*     */     static Flags toFlags(Set<? extends OpenOption> paramSet)
/*     */     {
/*  85 */       Flags localFlags = new Flags();
/*  86 */       for (OpenOption localOpenOption : paramSet)
/*  87 */         if ((localOpenOption instanceof StandardOpenOption)) {
/*  88 */           switch (WindowsChannelFactory.2.$SwitchMap$java$nio$file$StandardOpenOption[((StandardOpenOption)localOpenOption).ordinal()]) { case 1:
/*  89 */             localFlags.read = true; break;
/*     */           case 2:
/*  90 */             localFlags.write = true; break;
/*     */           case 3:
/*  91 */             localFlags.append = true; break;
/*     */           case 4:
/*  92 */             localFlags.truncateExisting = true; break;
/*     */           case 5:
/*  93 */             localFlags.create = true; break;
/*     */           case 6:
/*  94 */             localFlags.createNew = true; break;
/*     */           case 7:
/*  95 */             localFlags.deleteOnClose = true; break;
/*     */           case 8:
/*  96 */             localFlags.sparse = true; break;
/*     */           case 9:
/*  97 */             localFlags.sync = true; break;
/*     */           case 10:
/*  98 */             localFlags.dsync = true; break;
/*     */           default:
/*  99 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */         }
/* 103 */         else if ((localOpenOption instanceof ExtendedOpenOption)) {
/* 104 */           switch (WindowsChannelFactory.2.$SwitchMap$com$sun$nio$file$ExtendedOpenOption[((ExtendedOpenOption)localOpenOption).ordinal()]) { case 1:
/* 105 */             localFlags.shareRead = false; break;
/*     */           case 2:
/* 106 */             localFlags.shareWrite = false; break;
/*     */           case 3:
/* 107 */             localFlags.shareDelete = false; break;
/*     */           default:
/* 108 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */         }
/* 112 */         else if (localOpenOption == LinkOption.NOFOLLOW_LINKS) {
/* 113 */           localFlags.noFollowLinks = true;
/*     */         }
/* 116 */         else if (localOpenOption == WindowsChannelFactory.OPEN_REPARSE_POINT) {
/* 117 */           localFlags.openReparsePoint = true;
/*     */         }
/*     */         else {
/* 120 */           if (localOpenOption == null)
/* 121 */             throw new NullPointerException();
/* 122 */           throw new UnsupportedOperationException();
/*     */         }
/* 124 */       return localFlags;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsChannelFactory
 * JD-Core Version:    0.6.2
 */