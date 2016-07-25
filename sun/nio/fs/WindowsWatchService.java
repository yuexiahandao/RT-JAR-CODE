/*     */ package sun.nio.fs;
/*     */ 
/*     */ import com.sun.nio.file.ExtendedWatchEventModifier;
/*     */ import com.sun.nio.file.SensitivityWatchEventModifier;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.NotDirectoryException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent.Kind;
/*     */ import java.nio.file.WatchEvent.Modifier;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class WindowsWatchService extends AbstractWatchService
/*     */ {
/*     */   private static final int WAKEUP_COMPLETION_KEY = 0;
/*  45 */   private final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private final Poller poller;
/*     */   private static final int ALL_FILE_NOTIFY_EVENTS = 351;
/*     */ 
/*     */   WindowsWatchService(WindowsFileSystem paramWindowsFileSystem)
/*     */     throws IOException
/*     */   {
/*  55 */     long l = 0L;
/*     */     try {
/*  57 */       l = WindowsNativeDispatcher.CreateIoCompletionPort(-1L, 0L, 0L);
/*     */     } catch (WindowsException localWindowsException) {
/*  59 */       throw new IOException(localWindowsException.getMessage());
/*     */     }
/*     */ 
/*  62 */     this.poller = new Poller(paramWindowsFileSystem, this, l);
/*  63 */     this.poller.start();
/*     */   }
/*     */ 
/*     */   WatchKey register(Path paramPath, WatchEvent.Kind<?>[] paramArrayOfKind, WatchEvent.Modifier[] paramArrayOfModifier)
/*     */     throws IOException
/*     */   {
/*  73 */     return this.poller.register(paramPath, paramArrayOfKind, paramArrayOfModifier);
/*     */   }
/*     */ 
/*     */   void implClose()
/*     */     throws IOException
/*     */   {
/*  79 */     this.poller.close();
/*     */   }
/*     */ 
/*     */   private static class FileKey
/*     */   {
/*     */     private final int volSerialNumber;
/*     */     private final int fileIndexHigh;
/*     */     private final int fileIndexLow;
/*     */ 
/*     */     FileKey(int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 208 */       this.volSerialNumber = paramInt1;
/* 209 */       this.fileIndexHigh = paramInt2;
/* 210 */       this.fileIndexLow = paramInt3;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 215 */       return this.volSerialNumber ^ this.fileIndexHigh ^ this.fileIndexLow;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 220 */       if (paramObject == this)
/* 221 */         return true;
/* 222 */       if (!(paramObject instanceof FileKey))
/* 223 */         return false;
/* 224 */       FileKey localFileKey = (FileKey)paramObject;
/* 225 */       if (this.volSerialNumber != localFileKey.volSerialNumber) return false;
/* 226 */       if (this.fileIndexHigh != localFileKey.fileIndexHigh) return false;
/* 227 */       return this.fileIndexLow == localFileKey.fileIndexLow;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Poller extends AbstractPoller
/*     */   {
/*     */     private static final short SIZEOF_DWORD = 4;
/*     */     private static final short SIZEOF_OVERLAPPED = 32;
/*     */     private static final short OFFSETOF_NEXTENTRYOFFSET = 0;
/*     */     private static final short OFFSETOF_ACTION = 4;
/*     */     private static final short OFFSETOF_FILENAMELENGTH = 8;
/*     */     private static final short OFFSETOF_FILENAME = 12;
/*     */     private static final int CHANGES_BUFFER_SIZE = 16384;
/*     */     private final WindowsFileSystem fs;
/*     */     private final WindowsWatchService watcher;
/*     */     private final long port;
/*     */     private final Map<Integer, WindowsWatchService.WindowsWatchKey> ck2key;
/*     */     private final Map<WindowsWatchService.FileKey, WindowsWatchService.WindowsWatchKey> fk2key;
/*     */     private int lastCompletionKey;
/*     */ 
/*     */     Poller(WindowsFileSystem paramWindowsWatchService, WindowsWatchService paramLong, long arg4)
/*     */     {
/* 289 */       this.fs = paramWindowsWatchService;
/* 290 */       this.watcher = paramLong;
/*     */       Object localObject;
/* 291 */       this.port = localObject;
/* 292 */       this.ck2key = new HashMap();
/* 293 */       this.fk2key = new HashMap();
/* 294 */       this.lastCompletionKey = 0;
/*     */     }
/*     */ 
/*     */     void wakeup() throws IOException
/*     */     {
/*     */       try {
/* 300 */         WindowsNativeDispatcher.PostQueuedCompletionStatus(this.port, 0L);
/*     */       } catch (WindowsException localWindowsException) {
/* 302 */         throw new IOException(localWindowsException.getMessage());
/*     */       }
/*     */     }
/*     */ 
/*     */     Object implRegister(Path paramPath, Set<? extends WatchEvent.Kind<?>> paramSet, WatchEvent.Modifier[] paramArrayOfModifier)
/*     */     {
/* 320 */       WindowsPath localWindowsPath = (WindowsPath)paramPath;
/* 321 */       boolean bool = false;
/*     */       Object localObject1;
/* 324 */       for (localObject1 : paramArrayOfModifier) {
/* 325 */         if (localObject1 == ExtendedWatchEventModifier.FILE_TREE) {
/* 326 */           bool = true;
/*     */         } else {
/* 328 */           if (localObject1 == null)
/* 329 */             return new NullPointerException();
/* 330 */           if (!(localObject1 instanceof SensitivityWatchEventModifier))
/*     */           {
/* 332 */             return new UnsupportedOperationException("Modifier not supported");
/*     */           }
/*     */         }
/*     */       }
/*     */       long l1;
/*     */       try
/*     */       {
/* 339 */         l1 = WindowsNativeDispatcher.CreateFile(localWindowsPath.getPathForWin32Calls(), 1, 7, 3, 1107296256);
/*     */       }
/*     */       catch (WindowsException localWindowsException1)
/*     */       {
/* 345 */         return localWindowsException1.asIOException(localWindowsPath);
/*     */       }
/*     */ 
/* 348 */       int k = 0;
/*     */       try
/*     */       {
/*     */         try
/*     */         {
/* 353 */           localObject1 = WindowsFileAttributes.readAttributes(l1);
/*     */         } catch (WindowsException localWindowsException2) {
/* 355 */           return localWindowsException2.asIOException(localWindowsPath);
/*     */         }
/* 357 */         if (!((WindowsFileAttributes)localObject1).isDirectory()) {
/* 358 */           return new NotDirectoryException(localWindowsPath.getPathForExceptionMessage());
/*     */         }
/*     */ 
/* 362 */         Object localObject2 = new WindowsWatchService.FileKey(((WindowsFileAttributes)localObject1).volSerialNumber(), ((WindowsFileAttributes)localObject1).fileIndexHigh(), ((WindowsFileAttributes)localObject1).fileIndexLow());
/*     */ 
/* 365 */         Object localObject3 = (WindowsWatchService.WindowsWatchKey)this.fk2key.get(localObject2);
/*     */ 
/* 369 */         if ((localObject3 != null) && (bool == ((WindowsWatchService.WindowsWatchKey)localObject3).watchSubtree())) {
/* 370 */           ((WindowsWatchService.WindowsWatchKey)localObject3).setEvents(paramSet);
/* 371 */           return localObject3;
/*     */         }
/*     */ 
/* 376 */         int m = ++this.lastCompletionKey;
/* 377 */         if (m == 0) {
/* 378 */           m = ++this.lastCompletionKey;
/*     */         }
/*     */         try
/*     */         {
/* 382 */           WindowsNativeDispatcher.CreateIoCompletionPort(l1, this.port, m);
/*     */         } catch (WindowsException localWindowsException3) {
/* 384 */           return new IOException(localWindowsException3.getMessage());
/* 389 */         }
/*     */ int n = 16420;
/* 390 */         Object localObject5 = NativeBuffers.getNativeBuffer(n);
/*     */ 
/* 392 */         long l2 = ((NativeBuffer)localObject5).address();
/* 393 */         long l3 = l2 + n - 32L;
/* 394 */         long l4 = l3 - 4L;
/*     */         Object localObject6;
/*     */         try { WindowsNativeDispatcher.ReadDirectoryChangesW(l1, l2, 16384, bool, 351, l4, l3); }
/*     */         catch (WindowsException localWindowsException4)
/*     */         {
/* 406 */           ((NativeBuffer)localObject5).release();
/* 407 */           return new IOException(localWindowsException4.getMessage());
/*     */         }
/*     */         WindowsWatchService.WindowsWatchKey localWindowsWatchKey;
/* 411 */         if (localObject3 == null)
/*     */         {
/* 413 */           localWindowsWatchKey = new WindowsWatchService.WindowsWatchKey(WindowsWatchService.this, localWindowsPath, this.watcher, (WindowsWatchService.FileKey)localObject2).init(l1, paramSet, bool, (NativeBuffer)localObject5, l4, l3, m);
/*     */ 
/* 417 */           this.fk2key.put(localObject2, localWindowsWatchKey);
/*     */         }
/*     */         else
/*     */         {
/* 423 */           this.ck2key.remove(Integer.valueOf(((WindowsWatchService.WindowsWatchKey)localObject3).completionKey()));
/* 424 */           ((WindowsWatchService.WindowsWatchKey)localObject3).releaseResources();
/* 425 */           localWindowsWatchKey = ((WindowsWatchService.WindowsWatchKey)localObject3).init(l1, paramSet, bool, (NativeBuffer)localObject5, l4, l3, m);
/*     */         }
/*     */ 
/* 429 */         this.ck2key.put(Integer.valueOf(m), localWindowsWatchKey);
/*     */ 
/* 431 */         k = 1;
/* 432 */         return localWindowsWatchKey;
/*     */       }
/*     */       finally {
/* 435 */         if (k == 0) WindowsNativeDispatcher.CloseHandle(l1);
/*     */       }
/*     */     }
/*     */ 
/*     */     void implCancelKey(WatchKey paramWatchKey)
/*     */     {
/* 442 */       WindowsWatchService.WindowsWatchKey localWindowsWatchKey = (WindowsWatchService.WindowsWatchKey)paramWatchKey;
/* 443 */       if (localWindowsWatchKey.isValid()) {
/* 444 */         this.fk2key.remove(localWindowsWatchKey.fileKey());
/* 445 */         this.ck2key.remove(Integer.valueOf(localWindowsWatchKey.completionKey()));
/* 446 */         localWindowsWatchKey.invalidate();
/*     */       }
/*     */     }
/*     */ 
/*     */     void implCloseAll()
/*     */     {
/* 454 */       for (Map.Entry localEntry : this.ck2key.entrySet()) {
/* 455 */         ((WindowsWatchService.WindowsWatchKey)localEntry.getValue()).invalidate();
/*     */       }
/* 457 */       this.fk2key.clear();
/* 458 */       this.ck2key.clear();
/*     */ 
/* 461 */       WindowsNativeDispatcher.CloseHandle(this.port);
/*     */     }
/*     */ 
/*     */     private WatchEvent.Kind<?> translateActionToEvent(int paramInt)
/*     */     {
/* 467 */       switch (paramInt) {
/*     */       case 3:
/* 469 */         return StandardWatchEventKinds.ENTRY_MODIFY;
/*     */       case 1:
/*     */       case 5:
/* 473 */         return StandardWatchEventKinds.ENTRY_CREATE;
/*     */       case 2:
/*     */       case 4:
/* 477 */         return StandardWatchEventKinds.ENTRY_DELETE;
/*     */       }
/*     */ 
/* 480 */       return null;
/*     */     }
/*     */ 
/*     */     private void processEvents(WindowsWatchService.WindowsWatchKey paramWindowsWatchKey, int paramInt)
/*     */     {
/* 486 */       long l = paramWindowsWatchKey.buffer().address();
/*     */       int i;
/*     */       do
/*     */       {
/* 490 */         int j = WindowsWatchService.this.unsafe.getInt(l + 4L);
/*     */ 
/* 493 */         WatchEvent.Kind localKind = translateActionToEvent(j);
/* 494 */         if (paramWindowsWatchKey.events().contains(localKind))
/*     */         {
/* 496 */           int k = WindowsWatchService.this.unsafe.getInt(l + 8L);
/* 497 */           if (k % 2 != 0) {
/* 498 */             throw new AssertionError("FileNameLength.FileNameLength is not a multiple of 2");
/*     */           }
/* 500 */           char[] arrayOfChar = new char[k / 2];
/* 501 */           WindowsWatchService.this.unsafe.copyMemory(null, l + 12L, arrayOfChar, Unsafe.ARRAY_CHAR_BASE_OFFSET, k);
/*     */ 
/* 505 */           WindowsPath localWindowsPath = WindowsPath.createFromNormalizedPath(this.fs, new String(arrayOfChar));
/*     */ 
/* 507 */           paramWindowsWatchKey.signalEvent(localKind, localWindowsPath);
/*     */         }
/*     */ 
/* 511 */         i = WindowsWatchService.this.unsafe.getInt(l + 0L);
/* 512 */         l += i;
/* 513 */       }while (i != 0);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       while (true)
/*     */       {
/*     */         WindowsNativeDispatcher.CompletionStatus localCompletionStatus;
/*     */         try
/*     */         {
/* 524 */           localCompletionStatus = WindowsNativeDispatcher.GetQueuedCompletionStatus(this.port);
/*     */         }
/*     */         catch (WindowsException localWindowsException1) {
/* 527 */           localWindowsException1.printStackTrace();
/* 528 */           return;
/*     */         }
/*     */ 
/* 532 */         if (localCompletionStatus.completionKey() == 0L)
/*     */         {
/* 533 */           boolean bool = processRequests();
/* 534 */           if (!bool);
/*     */         }
/*     */         else
/*     */         {
/* 541 */           WindowsWatchService.WindowsWatchKey localWindowsWatchKey = (WindowsWatchService.WindowsWatchKey)this.ck2key.get(Integer.valueOf((int)localCompletionStatus.completionKey()));
/* 542 */           if (localWindowsWatchKey != null)
/*     */           {
/* 549 */             int i = 0;
/* 550 */             int j = localCompletionStatus.error();
/* 551 */             int k = localCompletionStatus.bytesTransferred();
/* 552 */             if (j == 1022)
/*     */             {
/* 554 */               localWindowsWatchKey.signalEvent(StandardWatchEventKinds.OVERFLOW, null);
/* 555 */             } else if ((j != 0) && (j != 234))
/*     */             {
/* 557 */               i = 1;
/*     */             }
/*     */             else
/*     */             {
/* 563 */               if (k > 0)
/*     */               {
/* 565 */                 processEvents(localWindowsWatchKey, k);
/* 566 */               } else if (j == 0)
/*     */               {
/* 569 */                 localWindowsWatchKey.signalEvent(StandardWatchEventKinds.OVERFLOW, null);
/*     */               }
/*     */ 
/*     */               try
/*     */               {
/* 574 */                 WindowsNativeDispatcher.ReadDirectoryChangesW(localWindowsWatchKey.handle(), localWindowsWatchKey.buffer().address(), 16384, localWindowsWatchKey.watchSubtree(), 351, localWindowsWatchKey.countAddress(), localWindowsWatchKey.overlappedAddress());
/*     */               }
/*     */               catch (WindowsException localWindowsException2)
/*     */               {
/* 583 */                 i = 1;
/*     */               }
/*     */             }
/* 586 */             if (i != 0) {
/* 587 */               implCancelKey(localWindowsWatchKey);
/* 588 */               localWindowsWatchKey.signal();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class WindowsWatchKey extends AbstractWatchKey
/*     */   {
/*     */     private final WindowsWatchService.FileKey fileKey;
/*  90 */     private volatile long handle = -1L;
/*     */     private Set<? extends WatchEvent.Kind<?>> events;
/*     */     private boolean watchSubtree;
/*     */     private NativeBuffer buffer;
/*     */     private long countAddress;
/*     */     private long overlappedAddress;
/*     */     private int completionKey;
/*     */ 
/*     */     WindowsWatchKey(Path paramAbstractWatchService, AbstractWatchService paramFileKey, WindowsWatchService.FileKey arg4)
/*     */     {
/* 114 */       super(paramFileKey);
/*     */       Object localObject;
/* 115 */       this.fileKey = localObject;
/*     */     }
/*     */ 
/*     */     WindowsWatchKey init(long paramLong1, Set<? extends WatchEvent.Kind<?>> paramSet, boolean paramBoolean, NativeBuffer paramNativeBuffer, long paramLong2, long paramLong3, int paramInt)
/*     */     {
/* 126 */       this.handle = paramLong1;
/* 127 */       this.events = paramSet;
/* 128 */       this.watchSubtree = paramBoolean;
/* 129 */       this.buffer = paramNativeBuffer;
/* 130 */       this.countAddress = paramLong2;
/* 131 */       this.overlappedAddress = paramLong3;
/* 132 */       this.completionKey = paramInt;
/* 133 */       return this;
/*     */     }
/*     */ 
/*     */     long handle() {
/* 137 */       return this.handle;
/*     */     }
/*     */ 
/*     */     Set<? extends WatchEvent.Kind<?>> events() {
/* 141 */       return this.events;
/*     */     }
/*     */ 
/*     */     void setEvents(Set<? extends WatchEvent.Kind<?>> paramSet) {
/* 145 */       this.events = paramSet;
/*     */     }
/*     */ 
/*     */     boolean watchSubtree() {
/* 149 */       return this.watchSubtree;
/*     */     }
/*     */ 
/*     */     NativeBuffer buffer() {
/* 153 */       return this.buffer;
/*     */     }
/*     */ 
/*     */     long countAddress() {
/* 157 */       return this.countAddress;
/*     */     }
/*     */ 
/*     */     long overlappedAddress() {
/* 161 */       return this.overlappedAddress;
/*     */     }
/*     */ 
/*     */     WindowsWatchService.FileKey fileKey() {
/* 165 */       return this.fileKey;
/*     */     }
/*     */ 
/*     */     int completionKey() {
/* 169 */       return this.completionKey;
/*     */     }
/*     */ 
/*     */     void releaseResources()
/*     */     {
/* 174 */       WindowsNativeDispatcher.CloseHandle(this.handle);
/* 175 */       this.buffer.cleaner().clean();
/*     */     }
/*     */ 
/*     */     void invalidate()
/*     */     {
/* 180 */       releaseResources();
/* 181 */       this.handle = -1L;
/* 182 */       this.buffer = null;
/* 183 */       this.countAddress = 0L;
/* 184 */       this.overlappedAddress = 0L;
/*     */     }
/*     */ 
/*     */     public boolean isValid()
/*     */     {
/* 189 */       return this.handle != -1L;
/*     */     }
/*     */ 
/*     */     public void cancel()
/*     */     {
/* 194 */       if (isValid())
/*     */       {
/* 196 */         WindowsWatchService.this.poller.cancel(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsWatchService
 * JD-Core Version:    0.6.2
 */