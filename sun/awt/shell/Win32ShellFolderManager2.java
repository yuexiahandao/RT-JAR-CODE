/*     */ package sun.awt.shell;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import sun.awt.OSInfo;
/*     */ import sun.awt.OSInfo.WindowsVersion;
/*     */ import sun.misc.ThreadGroupUtils;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class Win32ShellFolderManager2 extends ShellFolderManager
/*     */ {
/*     */   private static final int VIEW_LIST = 2;
/*     */   private static final int VIEW_DETAILS = 3;
/*     */   private static final int VIEW_PARENTFOLDER = 8;
/*     */   private static final int VIEW_NEWFOLDER = 11;
/* 112 */   private static final Image[] STANDARD_VIEW_BUTTONS = new Image[12];
/*     */   private static Win32ShellFolder2 desktop;
/*     */   private static Win32ShellFolder2 drives;
/*     */   private static Win32ShellFolder2 recent;
/*     */   private static Win32ShellFolder2 network;
/*     */   private static Win32ShellFolder2 personal;
/*     */   private static File[] roots;
/* 470 */   private static List topFolderList = null;
/*     */ 
/*     */   public ShellFolder createShellFolder(File paramFile)
/*     */     throws FileNotFoundException
/*     */   {
/*     */     try
/*     */     {
/*  65 */       return createShellFolder(getDesktop(), paramFile); } catch (InterruptedException localInterruptedException) {
/*     */     }
/*  67 */     throw new FileNotFoundException("Execution was interrupted");
/*     */   }
/*     */ 
/*     */   static Win32ShellFolder2 createShellFolder(Win32ShellFolder2 paramWin32ShellFolder2, File paramFile) throws FileNotFoundException, InterruptedException
/*     */   {
/*     */     long l;
/*     */     try
/*     */     {
/*  75 */       l = paramWin32ShellFolder2.parseDisplayName(paramFile.getCanonicalPath());
/*     */     } catch (IOException localIOException) {
/*  77 */       l = 0L;
/*     */     }
/*  79 */     if (l == 0L)
/*     */     {
/*  81 */       throw new FileNotFoundException("File " + paramFile.getAbsolutePath() + " not found");
/*     */     }
/*     */     try
/*     */     {
/*  85 */       return createShellFolderFromRelativePIDL(paramWin32ShellFolder2, l);
/*     */     } finally {
/*  87 */       Win32ShellFolder2.releasePIDL(l);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Win32ShellFolder2 createShellFolderFromRelativePIDL(Win32ShellFolder2 paramWin32ShellFolder2, long paramLong)
/*     */     throws InterruptedException
/*     */   {
/*  94 */     while (paramLong != 0L) {
/*  95 */       long l = Win32ShellFolder2.copyFirstPIDLEntry(paramLong);
/*  96 */       if (l == 0L) break;
/*  97 */       paramWin32ShellFolder2 = new Win32ShellFolder2(paramWin32ShellFolder2, l);
/*  98 */       paramLong = Win32ShellFolder2.getNextPIDLEntry(paramLong);
/*     */     }
/*     */ 
/* 104 */     return paramWin32ShellFolder2;
/*     */   }
/*     */ 
/*     */   private static Image getStandardViewButton(int paramInt)
/*     */   {
/* 115 */     Image localImage = STANDARD_VIEW_BUTTONS[paramInt];
/*     */ 
/* 117 */     if (localImage != null) {
/* 118 */       return localImage;
/*     */     }
/*     */ 
/* 121 */     BufferedImage localBufferedImage = new BufferedImage(16, 16, 2);
/*     */ 
/* 123 */     localBufferedImage.setRGB(0, 0, 16, 16, Win32ShellFolder2.getStandardViewButton0(paramInt), 0, 16);
/*     */ 
/* 125 */     STANDARD_VIEW_BUTTONS[paramInt] = localBufferedImage;
/*     */ 
/* 127 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   static Win32ShellFolder2 getDesktop()
/*     */   {
/* 138 */     if (desktop == null)
/*     */       try {
/* 140 */         desktop = new Win32ShellFolder2(0);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 149 */     return desktop;
/*     */   }
/*     */ 
/*     */   static Win32ShellFolder2 getDrives() {
/* 153 */     if (drives == null)
/*     */       try {
/* 155 */         drives = new Win32ShellFolder2(17);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 164 */     return drives;
/*     */   }
/*     */ 
/*     */   static Win32ShellFolder2 getRecent() {
/* 168 */     if (recent == null)
/*     */       try {
/* 170 */         String str = Win32ShellFolder2.getFileSystemPath(8);
/* 171 */         if (str != null)
/* 172 */           recent = createShellFolder(getDesktop(), new File(str));
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/* 182 */     return recent;
/*     */   }
/*     */ 
/*     */   static Win32ShellFolder2 getNetwork() {
/* 186 */     if (network == null)
/*     */       try {
/* 188 */         network = new Win32ShellFolder2(18);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 197 */     return network;
/*     */   }
/*     */ 
/*     */   static Win32ShellFolder2 getPersonal() {
/* 201 */     if (personal == null)
/*     */       try {
/* 203 */         String str = Win32ShellFolder2.getFileSystemPath(5);
/* 204 */         if (str != null) {
/* 205 */           Win32ShellFolder2 localWin32ShellFolder2 = getDesktop();
/* 206 */           personal = localWin32ShellFolder2.getChildByPath(str);
/* 207 */           if (personal == null) {
/* 208 */             personal = createShellFolder(getDesktop(), new File(str));
/*     */           }
/* 210 */           if (personal != null)
/* 211 */             personal.setIsPersonal();
/*     */         }
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/* 222 */     return personal;
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */   {
/*     */     Object localObject1;
/* 251 */     if (paramString.equals("fileChooserDefaultFolder")) {
/* 252 */       localObject1 = getPersonal();
/* 253 */       if (localObject1 == null) {
/* 254 */         localObject1 = getDesktop();
/*     */       }
/* 256 */       return checkFile((File)localObject1);
/* 257 */     }if (paramString.equals("roots"))
/*     */     {
/* 259 */       if (roots == null) {
/* 260 */         localObject1 = getDesktop();
/* 261 */         if (localObject1 != null)
/* 262 */           roots = new File[] { localObject1 };
/*     */         else {
/* 264 */           roots = (File[])super.get(paramString);
/*     */         }
/*     */       }
/* 267 */       return checkFiles(roots);
/*     */     }
/*     */     ArrayList localArrayList;
/*     */     Object localObject2;
/* 268 */     if (paramString.equals("fileChooserComboBoxFolders")) {
/* 269 */       localObject1 = getDesktop();
/*     */ 
/* 271 */       if ((localObject1 != null) && (checkFile((File)localObject1) != null)) {
/* 272 */         localArrayList = new ArrayList();
/* 273 */         Win32ShellFolder2 localWin32ShellFolder21 = getDrives();
/*     */ 
/* 275 */         localObject2 = getRecent();
/* 276 */         if ((localObject2 != null) && (OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_2000) >= 0)) {
/* 277 */           localArrayList.add(localObject2);
/*     */         }
/*     */ 
/* 280 */         localArrayList.add(localObject1);
/*     */ 
/* 282 */         File[] arrayOfFile1 = checkFiles(((Win32ShellFolder2)localObject1).listFiles());
/* 283 */         Arrays.sort(arrayOfFile1);
/* 284 */         for (File localFile2 : arrayOfFile1) {
/* 285 */           Win32ShellFolder2 localWin32ShellFolder22 = (Win32ShellFolder2)localFile2;
/* 286 */           if ((!localWin32ShellFolder22.isFileSystem()) || ((localWin32ShellFolder22.isDirectory()) && (!localWin32ShellFolder22.isLink()))) {
/* 287 */             localArrayList.add(localWin32ShellFolder22);
/*     */ 
/* 289 */             if (localWin32ShellFolder22.equals(localWin32ShellFolder21)) {
/* 290 */               File[] arrayOfFile4 = checkFiles(localWin32ShellFolder22.listFiles());
/* 291 */               if ((arrayOfFile4 != null) && (arrayOfFile4.length > 0)) {
/* 292 */                 List localList = Arrays.asList(arrayOfFile4);
/*     */ 
/* 294 */                 localWin32ShellFolder22.sortChildren(localList);
/* 295 */                 localArrayList.addAll(localList);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 300 */         return checkFiles(localArrayList);
/*     */       }
/* 302 */       return super.get(paramString);
/*     */     }
/* 304 */     if (paramString.equals("fileChooserShortcutPanelFolders")) {
/* 305 */       localObject1 = Toolkit.getDefaultToolkit();
/* 306 */       localArrayList = new ArrayList();
/* 307 */       int j = 0;
/*     */       do
/*     */       {
/* 310 */         localObject2 = ((Toolkit)localObject1).getDesktopProperty("win.comdlg.placesBarPlace" + j++);
/*     */         try {
/* 312 */           if ((localObject2 instanceof Integer))
/*     */           {
/* 314 */             localArrayList.add(new Win32ShellFolder2(((Integer)localObject2).intValue()));
/* 315 */           } else if ((localObject2 instanceof String))
/*     */           {
/* 317 */             localArrayList.add(createShellFolder(new File((String)localObject2)));
/*     */           }
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/* 323 */           return new File[0];
/*     */         }
/*     */       }
/* 325 */       while (localObject2 != null);
/*     */ 
/* 327 */       if (localArrayList.size() == 0)
/*     */       {
/* 329 */         for (File localFile1 : new File[] { getRecent(), getDesktop(), getPersonal(), getDrives(), getNetwork() })
/*     */         {
/* 332 */           if (localFile1 != null) {
/* 333 */             localArrayList.add(localFile1);
/*     */           }
/*     */         }
/*     */       }
/* 337 */       return checkFiles(localArrayList);
/*     */     }
/*     */     int i;
/* 338 */     if (paramString.startsWith("fileChooserIcon ")) {
/* 339 */       localObject1 = paramString.substring(paramString.indexOf(" ") + 1);
/*     */ 
/* 343 */       if ((((String)localObject1).equals("ListView")) || (((String)localObject1).equals("ViewMenu")))
/* 344 */         i = 2;
/* 345 */       else if (((String)localObject1).equals("DetailsView"))
/* 346 */         i = 3;
/* 347 */       else if (((String)localObject1).equals("UpFolder"))
/* 348 */         i = 8;
/* 349 */       else if (((String)localObject1).equals("NewFolder"))
/* 350 */         i = 11;
/*     */       else {
/* 352 */         return null;
/*     */       }
/*     */ 
/* 355 */       return getStandardViewButton(i);
/* 356 */     }if (paramString.startsWith("optionPaneIcon "))
/*     */     {
/* 358 */       if (paramString == "optionPaneIcon Error")
/* 359 */         localObject1 = Win32ShellFolder2.SystemIcon.IDI_ERROR;
/* 360 */       else if (paramString == "optionPaneIcon Information")
/* 361 */         localObject1 = Win32ShellFolder2.SystemIcon.IDI_INFORMATION;
/* 362 */       else if (paramString == "optionPaneIcon Question")
/* 363 */         localObject1 = Win32ShellFolder2.SystemIcon.IDI_QUESTION;
/* 364 */       else if (paramString == "optionPaneIcon Warning")
/* 365 */         localObject1 = Win32ShellFolder2.SystemIcon.IDI_EXCLAMATION;
/*     */       else {
/* 367 */         return null;
/*     */       }
/* 369 */       return Win32ShellFolder2.getSystemIcon((Win32ShellFolder2.SystemIcon)localObject1);
/* 370 */     }if ((paramString.startsWith("shell32Icon ")) || (paramString.startsWith("shell32LargeIcon "))) {
/* 371 */       localObject1 = paramString.substring(paramString.indexOf(" ") + 1);
/*     */       try {
/* 373 */         i = Integer.parseInt((String)localObject1);
/* 374 */         if (i >= 0)
/* 375 */           return Win32ShellFolder2.getShell32Icon(i, paramString.startsWith("shell32LargeIcon "));
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {
/*     */       }
/*     */     }
/* 380 */     return null;
/*     */   }
/*     */ 
/*     */   private File checkFile(File paramFile) {
/* 384 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 385 */     return (localSecurityManager == null) || (paramFile == null) ? paramFile : checkFile(paramFile, localSecurityManager);
/*     */   }
/*     */ 
/*     */   private File checkFile(File paramFile, SecurityManager paramSecurityManager) {
/*     */     try {
/* 390 */       paramSecurityManager.checkRead(paramFile.getPath());
/* 391 */       return paramFile; } catch (SecurityException localSecurityException) {
/*     */     }
/* 393 */     return null;
/*     */   }
/*     */ 
/*     */   private File[] checkFiles(File[] paramArrayOfFile)
/*     */   {
/* 398 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 399 */     if ((localSecurityManager == null) || (paramArrayOfFile == null) || (paramArrayOfFile.length == 0)) {
/* 400 */       return paramArrayOfFile;
/*     */     }
/* 402 */     return checkFiles(Arrays.asList(paramArrayOfFile), localSecurityManager);
/*     */   }
/*     */ 
/*     */   private File[] checkFiles(List<File> paramList) {
/* 406 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 407 */     if ((localSecurityManager == null) || (paramList.isEmpty())) {
/* 408 */       return (File[])paramList.toArray(new File[paramList.size()]);
/*     */     }
/* 410 */     return checkFiles(paramList, localSecurityManager);
/*     */   }
/*     */ 
/*     */   private File[] checkFiles(List<File> paramList, SecurityManager paramSecurityManager) {
/* 414 */     ArrayList localArrayList = new ArrayList(paramList.size());
/* 415 */     for (File localFile : paramList) {
/* 416 */       if (checkFile(localFile, paramSecurityManager) != null) {
/* 417 */         localArrayList.add(localFile);
/*     */       }
/*     */     }
/*     */ 
/* 421 */     return (File[])localArrayList.toArray(new File[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public boolean isComputerNode(final File paramFile)
/*     */   {
/* 429 */     if ((paramFile != null) && (paramFile == getDrives())) {
/* 430 */       return true;
/*     */     }
/* 432 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/* 434 */         return paramFile.getAbsolutePath();
/*     */       }
/*     */     });
/* 438 */     return (str.startsWith("\\\\")) && (str.indexOf("\\", 2) < 0);
/*     */   }
/*     */ 
/*     */   public boolean isFileSystemRoot(File paramFile)
/*     */   {
/* 444 */     if (paramFile != null) {
/* 445 */       Win32ShellFolder2 localWin32ShellFolder2 = getDrives();
/* 446 */       if ((paramFile instanceof Win32ShellFolder2)) {
/* 447 */         localObject = (Win32ShellFolder2)paramFile;
/* 448 */         if (((Win32ShellFolder2)localObject).isFileSystem()) {
/* 449 */           if (((Win32ShellFolder2)localObject).parent != null) {
/* 450 */             return ((Win32ShellFolder2)localObject).parent.equals(localWin32ShellFolder2);
/*     */           }
/*     */         }
/*     */         else {
/* 454 */           return false;
/*     */         }
/*     */       }
/* 457 */       Object localObject = paramFile.getPath();
/*     */ 
/* 459 */       if ((((String)localObject).length() != 3) || (((String)localObject).charAt(1) != ':')) {
/* 460 */         return false;
/*     */       }
/*     */ 
/* 463 */       File[] arrayOfFile = localWin32ShellFolder2.listFiles();
/*     */ 
/* 465 */       return (arrayOfFile != null) && (Arrays.asList(arrayOfFile).contains(paramFile));
/*     */     }
/* 467 */     return false;
/*     */   }
/*     */ 
/*     */   static int compareShellFolders(Win32ShellFolder2 paramWin32ShellFolder21, Win32ShellFolder2 paramWin32ShellFolder22)
/*     */   {
/* 472 */     boolean bool1 = paramWin32ShellFolder21.isSpecial();
/* 473 */     boolean bool2 = paramWin32ShellFolder22.isSpecial();
/*     */ 
/* 475 */     if ((bool1) || (bool2)) {
/* 476 */       if (topFolderList == null) {
/* 477 */         ArrayList localArrayList = new ArrayList();
/* 478 */         localArrayList.add(getPersonal());
/* 479 */         localArrayList.add(getDesktop());
/* 480 */         localArrayList.add(getDrives());
/* 481 */         localArrayList.add(getNetwork());
/* 482 */         topFolderList = localArrayList;
/*     */       }
/* 484 */       int i = topFolderList.indexOf(paramWin32ShellFolder21);
/* 485 */       int j = topFolderList.indexOf(paramWin32ShellFolder22);
/* 486 */       if ((i >= 0) && (j >= 0))
/* 487 */         return i - j;
/* 488 */       if (i >= 0)
/* 489 */         return -1;
/* 490 */       if (j >= 0) {
/* 491 */         return 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 496 */     if ((bool1) && (!bool2))
/* 497 */       return -1;
/* 498 */     if ((bool2) && (!bool1)) {
/* 499 */       return 1;
/*     */     }
/*     */ 
/* 502 */     return compareNames(paramWin32ShellFolder21.getAbsolutePath(), paramWin32ShellFolder22.getAbsolutePath());
/*     */   }
/*     */ 
/*     */   static int compareNames(String paramString1, String paramString2)
/*     */   {
/* 507 */     int i = paramString1.compareToIgnoreCase(paramString2);
/* 508 */     if (i != 0) {
/* 509 */       return i;
/*     */     }
/*     */ 
/* 513 */     return paramString1.compareTo(paramString2);
/*     */   }
/*     */ 
/*     */   protected ShellFolder.Invoker createInvoker()
/*     */   {
/* 519 */     return new ComInvoker(null);
/*     */   }
/*     */ 
/*     */   static native void initializeCom();
/*     */ 
/*     */   static native void uninitializeCom();
/*     */ 
/*     */   static
/*     */   {
/*  60 */     AccessController.doPrivileged(new LoadLibraryAction("awt"));
/*     */   }
/*     */ 
/*     */   private static class ComInvoker extends ThreadPoolExecutor
/*     */     implements ThreadFactory, ShellFolder.Invoker
/*     */   {
/*     */     private static Thread comThread;
/*     */ 
/*     */     private ComInvoker()
/*     */     {
/* 526 */       super(1, 0L, TimeUnit.DAYS, new LinkedBlockingQueue());
/* 527 */       allowCoreThreadTimeOut(false);
/* 528 */       setThreadFactory(this);
/* 529 */       final Runnable local1 = new Runnable() {
/*     */         public void run() {
/* 531 */           AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public Void run() {
/* 533 */               Win32ShellFolderManager2.ComInvoker.this.shutdownNow();
/* 534 */               return null;
/*     */             }
/*     */           });
/*     */         }
/*     */       };
/* 539 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/* 541 */           Runtime.getRuntime().addShutdownHook(new Thread(local1));
/*     */ 
/* 544 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     public synchronized Thread newThread(final Runnable paramRunnable) {
/* 550 */       final Runnable local3 = new Runnable() {
/*     */         public void run() {
/*     */           try {
/* 553 */             Win32ShellFolderManager2.initializeCom();
/* 554 */             paramRunnable.run();
/*     */           } finally {
/* 556 */             Win32ShellFolderManager2.uninitializeCom();
/*     */           }
/*     */         }
/*     */       };
/* 560 */       comThread = (Thread)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Thread run()
/*     */         {
/* 567 */           ThreadGroup localThreadGroup = ThreadGroupUtils.getRootThreadGroup();
/* 568 */           Thread localThread = new Thread(localThreadGroup, local3, "Swing-Shell");
/* 569 */           localThread.setDaemon(true);
/* 570 */           return localThread;
/*     */         }
/*     */       });
/* 574 */       return comThread;
/*     */     }
/*     */ 
/*     */     public <T> T invoke(Callable<T> paramCallable) throws Exception {
/* 578 */       if (Thread.currentThread() == comThread)
/*     */       {
/* 581 */         return paramCallable.call();
/*     */       }
/*     */       final Future localFuture;
/*     */       try
/*     */       {
/* 586 */         localFuture = submit(paramCallable);
/*     */       } catch (RejectedExecutionException localRejectedExecutionException) {
/* 588 */         throw new InterruptedException(localRejectedExecutionException.getMessage());
/*     */       }
/*     */       try
/*     */       {
/* 592 */         return localFuture.get();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 594 */         AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Void run() {
/* 596 */             localFuture.cancel(true);
/*     */ 
/* 598 */             return null;
/*     */           }
/*     */         });
/* 602 */         throw localInterruptedException;
/*     */       } catch (ExecutionException localExecutionException) {
/* 604 */         Throwable localThrowable = localExecutionException.getCause();
/*     */ 
/* 606 */         if ((localThrowable instanceof Exception)) {
/* 607 */           throw ((Exception)localThrowable);
/*     */         }
/*     */ 
/* 610 */         if ((localThrowable instanceof Error)) {
/* 611 */           throw ((Error)localThrowable);
/*     */         }
/*     */ 
/* 614 */         throw new RuntimeException("Unexpected error", localThrowable);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.shell.Win32ShellFolderManager2
 * JD-Core Version:    0.6.2
 */