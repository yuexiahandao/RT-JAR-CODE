/*      */ package sun.awt.shell;
/*      */ 
/*      */ import java.awt.Image;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.Callable;
/*      */ import sun.java2d.Disposer;
/*      */ import sun.java2d.DisposerRecord;
/*      */ 
/*      */ final class Win32ShellFolder2 extends ShellFolder
/*      */ {
/*      */   public static final int DESKTOP = 0;
/*      */   public static final int INTERNET = 1;
/*      */   public static final int PROGRAMS = 2;
/*      */   public static final int CONTROLS = 3;
/*      */   public static final int PRINTERS = 4;
/*      */   public static final int PERSONAL = 5;
/*      */   public static final int FAVORITES = 6;
/*      */   public static final int STARTUP = 7;
/*      */   public static final int RECENT = 8;
/*      */   public static final int SENDTO = 9;
/*      */   public static final int BITBUCKET = 10;
/*      */   public static final int STARTMENU = 11;
/*      */   public static final int DESKTOPDIRECTORY = 16;
/*      */   public static final int DRIVES = 17;
/*      */   public static final int NETWORK = 18;
/*      */   public static final int NETHOOD = 19;
/*      */   public static final int FONTS = 20;
/*      */   public static final int TEMPLATES = 21;
/*      */   public static final int COMMON_STARTMENU = 22;
/*      */   public static final int COMMON_PROGRAMS = 23;
/*      */   public static final int COMMON_STARTUP = 24;
/*      */   public static final int COMMON_DESKTOPDIRECTORY = 25;
/*      */   public static final int APPDATA = 26;
/*      */   public static final int PRINTHOOD = 27;
/*      */   public static final int ALTSTARTUP = 29;
/*      */   public static final int COMMON_ALTSTARTUP = 30;
/*      */   public static final int COMMON_FAVORITES = 31;
/*      */   public static final int INTERNET_CACHE = 32;
/*      */   public static final int COOKIES = 33;
/*      */   public static final int HISTORY = 34;
/*      */   public static final int ATTRIB_CANCOPY = 1;
/*      */   public static final int ATTRIB_CANMOVE = 2;
/*      */   public static final int ATTRIB_CANLINK = 4;
/*      */   public static final int ATTRIB_CANRENAME = 16;
/*      */   public static final int ATTRIB_CANDELETE = 32;
/*      */   public static final int ATTRIB_HASPROPSHEET = 64;
/*      */   public static final int ATTRIB_DROPTARGET = 256;
/*      */   public static final int ATTRIB_LINK = 65536;
/*      */   public static final int ATTRIB_SHARE = 131072;
/*      */   public static final int ATTRIB_READONLY = 262144;
/*      */   public static final int ATTRIB_GHOSTED = 524288;
/*      */   public static final int ATTRIB_HIDDEN = 524288;
/*      */   public static final int ATTRIB_FILESYSANCESTOR = 268435456;
/*      */   public static final int ATTRIB_FOLDER = 536870912;
/*      */   public static final int ATTRIB_FILESYSTEM = 1073741824;
/*      */   public static final int ATTRIB_HASSUBFOLDER = -2147483648;
/*      */   public static final int ATTRIB_VALIDATE = 16777216;
/*      */   public static final int ATTRIB_REMOVABLE = 33554432;
/*      */   public static final int ATTRIB_COMPRESSED = 67108864;
/*      */   public static final int ATTRIB_BROWSABLE = 134217728;
/*      */   public static final int ATTRIB_NONENUMERATED = 1048576;
/*      */   public static final int ATTRIB_NEWCONTENT = 2097152;
/*      */   public static final int SHGDN_NORMAL = 0;
/*      */   public static final int SHGDN_INFOLDER = 1;
/*      */   public static final int SHGDN_INCLUDE_NONFILESYS = 8192;
/*      */   public static final int SHGDN_FORADDRESSBAR = 16384;
/*      */   public static final int SHGDN_FORPARSING = 32768;
/*  199 */   FolderDisposer disposer = new FolderDisposer();
/*      */ 
/*  209 */   private long pIShellIcon = -1L;
/*  210 */   private String folderType = null;
/*  211 */   private String displayName = null;
/*  212 */   private Image smallIcon = null;
/*  213 */   private Image largeIcon = null;
/*  214 */   private Boolean isDir = null;
/*      */   private boolean isPersonal;
/*      */   private volatile Boolean cachedIsFileSystem;
/*      */   private volatile Boolean cachedIsLink;
/*  904 */   private static Map smallSystemImages = new HashMap();
/*  905 */   private static Map largeSystemImages = new HashMap();
/*  906 */   private static Map smallLinkedSystemImages = new HashMap();
/*  907 */   private static Map largeLinkedSystemImages = new HashMap();
/*      */   private static final int LVCFMT_LEFT = 0;
/*      */   private static final int LVCFMT_RIGHT = 1;
/*      */   private static final int LVCFMT_CENTER = 2;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   private void setIShellFolder(long paramLong)
/*      */   {
/*  201 */     this.disposer.pIShellFolder = paramLong;
/*      */   }
/*      */   private void setRelativePIDL(long paramLong) {
/*  204 */     this.disposer.relativePIDL = paramLong;
/*      */   }
/*      */ 
/*      */   private static String composePathForCsidl(int paramInt)
/*      */     throws IOException, InterruptedException
/*      */   {
/*  222 */     String str = getFileSystemPath(paramInt);
/*  223 */     return str == null ? "ShellFolder: 0x" + Integer.toHexString(paramInt) : str;
/*      */   }
/*      */ 
/*      */   Win32ShellFolder2(final int paramInt)
/*      */     throws IOException, InterruptedException
/*      */   {
/*  235 */     super(null, composePathForCsidl(paramInt));
/*      */ 
/*  237 */     invoke(new Callable() {
/*      */       public Void call() throws InterruptedException {
/*  239 */         if (paramInt == 0) {
/*  240 */           Win32ShellFolder2.this.initDesktop();
/*      */         } else {
/*  242 */           Win32ShellFolder2.this.initSpecial(Win32ShellFolder2.access$200(Win32ShellFolder2.this.getDesktop()), paramInt);
/*      */ 
/*  248 */           long l1 = Win32ShellFolder2.this.disposer.relativePIDL;
/*  249 */           Win32ShellFolder2.this.parent = Win32ShellFolder2.this.getDesktop();
/*  250 */           while (l1 != 0L)
/*      */           {
/*  252 */             long l2 = Win32ShellFolder2.copyFirstPIDLEntry(l1);
/*  253 */             if (l2 == 0L) {
/*      */               break;
/*      */             }
/*  256 */             l1 = Win32ShellFolder2.getNextPIDLEntry(l1);
/*  257 */             if (l1 != 0L)
/*      */             {
/*  261 */               Win32ShellFolder2.this.parent = new Win32ShellFolder2((Win32ShellFolder2)Win32ShellFolder2.this.parent, l2);
/*      */             }
/*      */             else
/*      */             {
/*  265 */               Win32ShellFolder2.this.disposer.relativePIDL = l2;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  272 */         return null;
/*      */       }
/*      */     }
/*      */     , InterruptedException.class);
/*      */ 
/*  276 */     Disposer.addRecord(this, this.disposer);
/*      */   }
/*      */ 
/*      */   Win32ShellFolder2(Win32ShellFolder2 paramWin32ShellFolder2, long paramLong1, long paramLong2, String paramString)
/*      */   {
/*  284 */     super(paramWin32ShellFolder2, paramString != null ? paramString : "ShellFolder: ");
/*  285 */     this.disposer.pIShellFolder = paramLong1;
/*  286 */     this.disposer.relativePIDL = paramLong2;
/*  287 */     Disposer.addRecord(this, this.disposer);
/*      */   }
/*      */ 
/*      */   Win32ShellFolder2(Win32ShellFolder2 paramWin32ShellFolder2, final long paramLong)
/*      */     throws InterruptedException
/*      */   {
/*  295 */     super(paramWin32ShellFolder2, (String)invoke(new Callable()
/*      */     {
/*      */       public String call() {
/*  298 */         return Win32ShellFolder2.getFileSystemPath(Win32ShellFolder2.access$200(Win32ShellFolder2.this), paramLong);
/*      */       }
/*      */     }
/*      */     , RuntimeException.class));
/*      */ 
/*  302 */     this.disposer.relativePIDL = paramLong;
/*  303 */     Disposer.addRecord(this, this.disposer);
/*      */   }
/*      */ 
/*      */   private native void initDesktop();
/*      */ 
/*      */   private native void initSpecial(long paramLong, int paramInt);
/*      */ 
/*      */   public void setIsPersonal()
/*      */   {
/*  317 */     this.isPersonal = true;
/*      */   }
/*      */ 
/*      */   protected Object writeReplace()
/*      */     throws ObjectStreamException
/*      */   {
/*  332 */     return invoke(new Callable() {
/*      */       public File call() {
/*  334 */         if (Win32ShellFolder2.this.isFileSystem()) {
/*  335 */           return new File(Win32ShellFolder2.this.getPath());
/*      */         }
/*  337 */         Win32ShellFolder2 localWin32ShellFolder21 = Win32ShellFolderManager2.getDrives();
/*  338 */         if (localWin32ShellFolder21 != null) {
/*  339 */           File[] arrayOfFile = localWin32ShellFolder21.listFiles();
/*  340 */           if (arrayOfFile != null) {
/*  341 */             for (int i = 0; i < arrayOfFile.length; i++) {
/*  342 */               if ((arrayOfFile[i] instanceof Win32ShellFolder2)) {
/*  343 */                 Win32ShellFolder2 localWin32ShellFolder22 = (Win32ShellFolder2)arrayOfFile[i];
/*  344 */                 if ((localWin32ShellFolder22.isFileSystem()) && (!localWin32ShellFolder22.hasAttribute(33554432))) {
/*  345 */                   return new File(localWin32ShellFolder22.getPath());
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  352 */         return new File("C:\\");
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   protected void dispose()
/*      */   {
/*  363 */     this.disposer.dispose();
/*      */   }
/*      */ 
/*      */   static native long getNextPIDLEntry(long paramLong);
/*      */ 
/*      */   static native long copyFirstPIDLEntry(long paramLong);
/*      */ 
/*      */   private static native long combinePIDLs(long paramLong1, long paramLong2);
/*      */ 
/*      */   static native void releasePIDL(long paramLong);
/*      */ 
/*      */   private static native void releaseIShellFolder(long paramLong);
/*      */ 
/*      */   private long getIShellFolder()
/*      */   {
/*  396 */     if (this.disposer.pIShellFolder == 0L) {
/*      */       try {
/*  398 */         this.disposer.pIShellFolder = ((Long)invoke(new Callable() {
/*      */           public Long call() {
/*  400 */             assert (Win32ShellFolder2.this.isDirectory());
/*  401 */             assert (Win32ShellFolder2.this.parent != null);
/*  402 */             long l1 = Win32ShellFolder2.this.getParentIShellFolder();
/*  403 */             if (l1 == 0L) {
/*  404 */               throw new InternalError("Parent IShellFolder was null for " + Win32ShellFolder2.this.getAbsolutePath());
/*      */             }
/*      */ 
/*  410 */             long l2 = Win32ShellFolder2.bindToObject(l1, Win32ShellFolder2.this.disposer.relativePIDL);
/*      */ 
/*  412 */             if (l2 == 0L) {
/*  413 */               throw new InternalError("Unable to bind " + Win32ShellFolder2.this.getAbsolutePath() + " to parent");
/*      */             }
/*      */ 
/*  416 */             return Long.valueOf(l2);
/*      */           }
/*      */         }
/*      */         , RuntimeException.class)).longValue();
/*      */       }
/*      */       catch (InterruptedException localInterruptedException)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  423 */     return this.disposer.pIShellFolder;
/*      */   }
/*      */ 
/*      */   public long getParentIShellFolder()
/*      */   {
/*  430 */     Win32ShellFolder2 localWin32ShellFolder2 = (Win32ShellFolder2)getParentFile();
/*  431 */     if (localWin32ShellFolder2 == null)
/*      */     {
/*  434 */       return getIShellFolder();
/*      */     }
/*  436 */     return localWin32ShellFolder2.getIShellFolder();
/*      */   }
/*      */ 
/*      */   public long getRelativePIDL()
/*      */   {
/*  443 */     if (this.disposer.relativePIDL == 0L) {
/*  444 */       throw new InternalError("Should always have a relative PIDL");
/*      */     }
/*  446 */     return this.disposer.relativePIDL;
/*      */   }
/*      */ 
/*      */   private long getAbsolutePIDL() {
/*  450 */     if (this.parent == null)
/*      */     {
/*  452 */       return getRelativePIDL();
/*      */     }
/*  454 */     if (this.disposer.absolutePIDL == 0L) {
/*  455 */       this.disposer.absolutePIDL = combinePIDLs(((Win32ShellFolder2)this.parent).getAbsolutePIDL(), getRelativePIDL());
/*      */     }
/*      */ 
/*  458 */     return this.disposer.absolutePIDL;
/*      */   }
/*      */ 
/*      */   public Win32ShellFolder2 getDesktop()
/*      */   {
/*  466 */     return Win32ShellFolderManager2.getDesktop();
/*      */   }
/*      */ 
/*      */   public long getDesktopIShellFolder()
/*      */   {
/*  473 */     return getDesktop().getIShellFolder();
/*      */   }
/*      */ 
/*      */   private static boolean pathsEqual(String paramString1, String paramString2)
/*      */   {
/*  478 */     return paramString1.equalsIgnoreCase(paramString2);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  485 */     if ((paramObject == null) || (!(paramObject instanceof Win32ShellFolder2)))
/*      */     {
/*  487 */       if (!(paramObject instanceof File)) {
/*  488 */         return super.equals(paramObject);
/*      */       }
/*  490 */       return pathsEqual(getPath(), ((File)paramObject).getPath());
/*      */     }
/*  492 */     Win32ShellFolder2 localWin32ShellFolder2 = (Win32ShellFolder2)paramObject;
/*  493 */     if (((this.parent == null) && (localWin32ShellFolder2.parent != null)) || ((this.parent != null) && (localWin32ShellFolder2.parent == null)))
/*      */     {
/*  495 */       return false;
/*      */     }
/*      */ 
/*  498 */     if ((isFileSystem()) && (localWin32ShellFolder2.isFileSystem()))
/*      */     {
/*  500 */       return (pathsEqual(getPath(), localWin32ShellFolder2.getPath())) && ((this.parent == localWin32ShellFolder2.parent) || (this.parent.equals(localWin32ShellFolder2.parent)));
/*      */     }
/*      */ 
/*  504 */     if ((this.parent == localWin32ShellFolder2.parent) || (this.parent.equals(localWin32ShellFolder2.parent))) {
/*      */       try {
/*  506 */         return pidlsEqual(getParentIShellFolder(), this.disposer.relativePIDL, localWin32ShellFolder2.disposer.relativePIDL);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  508 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  512 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean pidlsEqual(long paramLong1, long paramLong2, final long paramLong3) throws InterruptedException
/*      */   {
/*  517 */     return ((Boolean)invoke(new Callable() {
/*      */       public Boolean call() {
/*  519 */         return Boolean.valueOf(Win32ShellFolder2.compareIDs(this.val$pIShellFolder, paramLong3, this.val$pidl2) == 0);
/*      */       }
/*      */     }
/*      */     , RuntimeException.class)).booleanValue();
/*      */   }
/*      */ 
/*      */   private static native int compareIDs(long paramLong1, long paramLong2, long paramLong3);
/*      */ 
/*      */   public boolean isFileSystem()
/*      */   {
/*  533 */     if (this.cachedIsFileSystem == null) {
/*  534 */       this.cachedIsFileSystem = Boolean.valueOf(hasAttribute(1073741824));
/*      */     }
/*      */ 
/*  537 */     return this.cachedIsFileSystem.booleanValue();
/*      */   }
/*      */ 
/*      */   public boolean hasAttribute(final int paramInt)
/*      */   {
/*  544 */     Boolean localBoolean = (Boolean)invoke(new Callable()
/*      */     {
/*      */       public Boolean call() {
/*  547 */         return Boolean.valueOf((Win32ShellFolder2.getAttributes0(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), paramInt) & paramInt) != 0);
/*      */       }
/*      */     });
/*  553 */     return (localBoolean != null) && (localBoolean.booleanValue());
/*      */   }
/*      */ 
/*      */   private static native int getAttributes0(long paramLong1, long paramLong2, int paramInt);
/*      */ 
/*      */   private static String getFileSystemPath(long paramLong1, long paramLong2)
/*      */   {
/*  570 */     int i = 536936448;
/*  571 */     if ((paramLong1 == Win32ShellFolderManager2.getNetwork().getIShellFolder()) && (getAttributes0(paramLong1, paramLong2, i) == i))
/*      */     {
/*  574 */       String str = getFileSystemPath(Win32ShellFolderManager2.getDesktop().getIShellFolder(), getLinkLocation(paramLong1, paramLong2, false));
/*      */ 
/*  577 */       if ((str != null) && (str.startsWith("\\\\"))) {
/*  578 */         return str;
/*      */       }
/*      */     }
/*  581 */     return getDisplayNameOf(paramLong1, paramLong2, 32768);
/*      */   }
/*      */ 
/*      */   static String getFileSystemPath(int paramInt) throws IOException, InterruptedException
/*      */   {
/*  586 */     String str = (String)invoke(new Callable() {
/*      */       public String call() throws IOException {
/*  588 */         return Win32ShellFolder2.getFileSystemPath0(this.val$csidl);
/*      */       }
/*      */     }
/*      */     , IOException.class);
/*      */ 
/*  591 */     if (str != null) {
/*  592 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  593 */       if (localSecurityManager != null) {
/*  594 */         localSecurityManager.checkRead(str);
/*      */       }
/*      */     }
/*  597 */     return str;
/*      */   }
/*      */ 
/*      */   private static native String getFileSystemPath0(int paramInt)
/*      */     throws IOException;
/*      */ 
/*      */   private static boolean isNetworkRoot(String paramString)
/*      */   {
/*  606 */     return (paramString.equals("\\\\")) || (paramString.equals("\\")) || (paramString.equals("//")) || (paramString.equals("/"));
/*      */   }
/*      */ 
/*      */   public File getParentFile()
/*      */   {
/*  614 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public boolean isDirectory() {
/*  618 */     if (this.isDir == null)
/*      */     {
/*  621 */       if ((hasAttribute(536870912)) && (!hasAttribute(134217728))) {
/*  622 */         this.isDir = Boolean.TRUE;
/*  623 */       } else if (isLink()) {
/*  624 */         ShellFolder localShellFolder = getLinkLocation(false);
/*  625 */         this.isDir = Boolean.valueOf((localShellFolder != null) && (localShellFolder.isDirectory()));
/*      */       } else {
/*  627 */         this.isDir = Boolean.FALSE;
/*      */       }
/*      */     }
/*  630 */     return this.isDir.booleanValue();
/*      */   }
/*      */ 
/*      */   private long getEnumObjects(final boolean paramBoolean)
/*      */     throws InterruptedException
/*      */   {
/*  639 */     return ((Long)invoke(new Callable() {
/*      */       public Long call() {
/*  641 */         boolean bool = Win32ShellFolder2.this.disposer.pIShellFolder == Win32ShellFolder2.this.getDesktopIShellFolder();
/*      */ 
/*  643 */         return Long.valueOf(Win32ShellFolder2.this.getEnumObjects(Win32ShellFolder2.this.disposer.pIShellFolder, bool, paramBoolean));
/*      */       }
/*      */     }
/*      */     , RuntimeException.class)).longValue();
/*      */   }
/*      */ 
/*      */   private native long getEnumObjects(long paramLong, boolean paramBoolean1, boolean paramBoolean2);
/*      */ 
/*      */   private native long getNextChild(long paramLong);
/*      */ 
/*      */   private native void releaseEnumObjects(long paramLong);
/*      */ 
/*      */   private static native long bindToObject(long paramLong1, long paramLong2);
/*      */ 
/*      */   public File[] listFiles(final boolean paramBoolean)
/*      */   {
/*  674 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  675 */     if (localSecurityManager != null) {
/*  676 */       localSecurityManager.checkRead(getPath());
/*      */     }
/*      */     try
/*      */     {
/*  680 */       return (File[])invoke(new Callable() {
/*      */         public File[] call() throws InterruptedException {
/*  682 */           if (!Win32ShellFolder2.this.isDirectory()) {
/*  683 */             return null;
/*      */           }
/*      */ 
/*  688 */           if ((Win32ShellFolder2.this.isLink()) && (!Win32ShellFolder2.this.hasAttribute(536870912))) {
/*  689 */             return new File[0];
/*      */           }
/*      */ 
/*  692 */           Win32ShellFolder2 localWin32ShellFolder21 = Win32ShellFolderManager2.getDesktop();
/*  693 */           Win32ShellFolder2 localWin32ShellFolder22 = Win32ShellFolderManager2.getPersonal();
/*      */ 
/*  698 */           long l1 = Win32ShellFolder2.this.getIShellFolder();
/*      */ 
/*  700 */           ArrayList localArrayList = new ArrayList();
/*  701 */           long l2 = Win32ShellFolder2.this.getEnumObjects(paramBoolean);
/*  702 */           if (l2 != 0L) {
/*      */             try
/*      */             {
/*  705 */               int i = 1342177280;
/*      */               do {
/*  707 */                 long l3 = Win32ShellFolder2.this.getNextChild(l2);
/*  708 */                 int j = 1;
/*  709 */                 if ((l3 != 0L) && ((Win32ShellFolder2.getAttributes0(l1, l3, i) & i) != 0))
/*      */                 {
/*      */                   Win32ShellFolder2 localWin32ShellFolder23;
/*  712 */                   if ((Win32ShellFolder2.this.equals(localWin32ShellFolder21)) && (localWin32ShellFolder22 != null) && (Win32ShellFolder2.pidlsEqual(l1, l3, localWin32ShellFolder22.disposer.relativePIDL)))
/*      */                   {
/*  715 */                     localWin32ShellFolder23 = localWin32ShellFolder22;
/*      */                   } else {
/*  717 */                     localWin32ShellFolder23 = new Win32ShellFolder2(Win32ShellFolder2.this, l3);
/*  718 */                     j = 0;
/*      */                   }
/*  720 */                   localArrayList.add(localWin32ShellFolder23);
/*      */                 }
/*  722 */                 if (j != 0) {
/*  723 */                   Win32ShellFolder2.releasePIDL(l3);
/*      */                 }
/*  725 */                 if (l3 == 0L) break;  } while (!Thread.currentThread().isInterrupted());
/*      */             } finally {
/*  727 */               Win32ShellFolder2.this.releaseEnumObjects(l2);
/*      */             }
/*      */           }
/*  730 */           return Thread.currentThread().isInterrupted() ? new File[0] : (File[])localArrayList.toArray(new ShellFolder[localArrayList.size()]);
/*      */         }
/*      */       }
/*      */       , InterruptedException.class);
/*      */     }
/*      */     catch (InterruptedException localInterruptedException)
/*      */     {
/*      */     }
/*      */ 
/*  736 */     return new File[0];
/*      */   }
/*      */ 
/*      */   Win32ShellFolder2 getChildByPath(final String paramString)
/*      */     throws InterruptedException
/*      */   {
/*  747 */     return (Win32ShellFolder2)invoke(new Callable() {
/*      */       public Win32ShellFolder2 call() throws InterruptedException {
/*  749 */         long l1 = Win32ShellFolder2.this.getIShellFolder();
/*  750 */         long l2 = Win32ShellFolder2.this.getEnumObjects(true);
/*  751 */         Win32ShellFolder2 localWin32ShellFolder2 = null;
/*      */         long l3;
/*  754 */         while ((l3 = Win32ShellFolder2.this.getNextChild(l2)) != 0L) {
/*  755 */           if (Win32ShellFolder2.getAttributes0(l1, l3, 1073741824) != 0) {
/*  756 */             String str = Win32ShellFolder2.getFileSystemPath(l1, l3);
/*  757 */             if ((str != null) && (str.equalsIgnoreCase(paramString))) {
/*  758 */               long l4 = Win32ShellFolder2.bindToObject(l1, l3);
/*  759 */               localWin32ShellFolder2 = new Win32ShellFolder2(Win32ShellFolder2.this, l4, l3, str);
/*      */ 
/*  761 */               break;
/*      */             }
/*      */           }
/*  764 */           Win32ShellFolder2.releasePIDL(l3);
/*      */         }
/*  766 */         Win32ShellFolder2.this.releaseEnumObjects(l2);
/*  767 */         return localWin32ShellFolder2;
/*      */       }
/*      */     }
/*      */     , InterruptedException.class);
/*      */   }
/*      */ 
/*      */   public boolean isLink()
/*      */   {
/*  778 */     if (this.cachedIsLink == null) {
/*  779 */       this.cachedIsLink = Boolean.valueOf(hasAttribute(65536));
/*      */     }
/*      */ 
/*  782 */     return this.cachedIsLink.booleanValue();
/*      */   }
/*      */ 
/*      */   public boolean isHidden()
/*      */   {
/*  789 */     return hasAttribute(524288);
/*      */   }
/*      */ 
/*      */   private static native long getLinkLocation(long paramLong1, long paramLong2, boolean paramBoolean);
/*      */ 
/*      */   public ShellFolder getLinkLocation()
/*      */   {
/*  803 */     return getLinkLocation(true);
/*      */   }
/*      */ 
/*      */   private ShellFolder getLinkLocation(final boolean paramBoolean) {
/*  807 */     return (ShellFolder)invoke(new Callable() {
/*      */       public ShellFolder call() {
/*  809 */         if (!Win32ShellFolder2.this.isLink()) {
/*  810 */           return null;
/*      */         }
/*      */ 
/*  813 */         Win32ShellFolder2 localWin32ShellFolder2 = null;
/*  814 */         long l = Win32ShellFolder2.getLinkLocation(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), paramBoolean);
/*      */ 
/*  816 */         if (l != 0L) {
/*      */           try {
/*  818 */             localWin32ShellFolder2 = Win32ShellFolderManager2.createShellFolderFromRelativePIDL(Win32ShellFolder2.this.getDesktop(), l);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*      */           }
/*      */           catch (InternalError localInternalError)
/*      */           {
/*      */           }
/*      */         }
/*      */ 
/*  828 */         return localWin32ShellFolder2;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   long parseDisplayName(final String paramString) throws IOException, InterruptedException
/*      */   {
/*  835 */     return ((Long)invoke(new Callable() {
/*      */       public Long call() throws IOException {
/*  837 */         return Long.valueOf(Win32ShellFolder2.parseDisplayName0(Win32ShellFolder2.access$200(Win32ShellFolder2.this), paramString));
/*      */       }
/*      */     }
/*      */     , IOException.class)).longValue();
/*      */   }
/*      */ 
/*      */   private static native long parseDisplayName0(long paramLong, String paramString)
/*      */     throws IOException;
/*      */ 
/*      */   private static native String getDisplayNameOf(long paramLong1, long paramLong2, int paramInt);
/*      */ 
/*      */   public String getDisplayName()
/*      */   {
/*  855 */     if (this.displayName == null) {
/*  856 */       this.displayName = ((String)invoke(new Callable()
/*      */       {
/*      */         public String call() {
/*  859 */           return Win32ShellFolder2.getDisplayNameOf(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), 0);
/*      */         }
/*      */       }));
/*      */     }
/*      */ 
/*  864 */     return this.displayName;
/*      */   }
/*      */ 
/*      */   private static native String getFolderType(long paramLong);
/*      */ 
/*      */   public String getFolderType()
/*      */   {
/*  875 */     if (this.folderType == null) {
/*  876 */       final long l = getAbsolutePIDL();
/*  877 */       this.folderType = ((String)invoke(new Callable()
/*      */       {
/*      */         public String call() {
/*  880 */           return Win32ShellFolder2.getFolderType(l);
/*      */         }
/*      */       }));
/*      */     }
/*  884 */     return this.folderType;
/*      */   }
/*      */ 
/*      */   private native String getExecutableType(String paramString);
/*      */ 
/*      */   public String getExecutableType()
/*      */   {
/*  894 */     if (!isFileSystem()) {
/*  895 */       return null;
/*      */     }
/*  897 */     return getExecutableType(getAbsolutePath());
/*      */   }
/*      */ 
/*      */   private static native long getIShellIcon(long paramLong);
/*      */ 
/*      */   private static native int getIconIndex(long paramLong1, long paramLong2);
/*      */ 
/*      */   private static native long getIcon(String paramString, boolean paramBoolean);
/*      */ 
/*      */   private static native long extractIcon(long paramLong1, long paramLong2, boolean paramBoolean);
/*      */ 
/*      */   private static native long getSystemIcon(int paramInt);
/*      */ 
/*      */   private static native long getIconResource(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
/*      */ 
/*      */   private static native int[] getIconBits(long paramLong, int paramInt);
/*      */ 
/*      */   private static native void disposeIcon(long paramLong);
/*      */ 
/*      */   static native int[] getStandardViewButton0(int paramInt);
/*      */ 
/*      */   private long getIShellIcon()
/*      */   {
/*  939 */     if (this.pIShellIcon == -1L) {
/*  940 */       this.pIShellIcon = getIShellIcon(getIShellFolder());
/*      */     }
/*      */ 
/*  943 */     return this.pIShellIcon;
/*      */   }
/*      */ 
/*      */   private static Image makeIcon(long paramLong, boolean paramBoolean) {
/*  947 */     if ((paramLong != 0L) && (paramLong != -1L))
/*      */     {
/*  949 */       int i = paramBoolean ? 32 : 16;
/*  950 */       int[] arrayOfInt = getIconBits(paramLong, i);
/*  951 */       if (arrayOfInt != null) {
/*  952 */         BufferedImage localBufferedImage = new BufferedImage(i, i, 2);
/*  953 */         localBufferedImage.setRGB(0, 0, i, i, arrayOfInt, 0, i);
/*  954 */         return localBufferedImage;
/*      */       }
/*      */     }
/*  957 */     return null;
/*      */   }
/*      */ 
/*      */   public Image getIcon(final boolean paramBoolean)
/*      */   {
/*  965 */     Image localImage = paramBoolean ? this.largeIcon : this.smallIcon;
/*  966 */     if (localImage == null) {
/*  967 */       localImage = (Image)invoke(new Callable()
/*      */       {
/*      */         public Image call() {
/*  970 */           Image localImage = null;
/*      */           long l1;
/*  971 */           if (Win32ShellFolder2.this.isFileSystem()) {
/*  972 */             l1 = Win32ShellFolder2.this.parent != null ? ((Win32ShellFolder2)Win32ShellFolder2.this.parent).getIShellIcon() : 0L;
/*      */ 
/*  975 */             long l2 = Win32ShellFolder2.this.getRelativePIDL();
/*      */ 
/*  978 */             int i = Win32ShellFolder2.getIconIndex(l1, l2);
/*  979 */             if (i > 0)
/*      */             {
/*      */               Map localMap;
/*  981 */               if (Win32ShellFolder2.this.isLink())
/*  982 */                 localMap = paramBoolean ? Win32ShellFolder2.largeLinkedSystemImages : Win32ShellFolder2.smallLinkedSystemImages;
/*      */               else {
/*  984 */                 localMap = paramBoolean ? Win32ShellFolder2.largeSystemImages : Win32ShellFolder2.smallSystemImages;
/*      */               }
/*  986 */               localImage = (Image)localMap.get(Integer.valueOf(i));
/*  987 */               if (localImage == null) {
/*  988 */                 long l3 = Win32ShellFolder2.getIcon(Win32ShellFolder2.this.getAbsolutePath(), paramBoolean);
/*  989 */                 localImage = Win32ShellFolder2.makeIcon(l3, paramBoolean);
/*  990 */                 Win32ShellFolder2.disposeIcon(l3);
/*  991 */                 if (localImage != null) {
/*  992 */                   localMap.put(Integer.valueOf(i), localImage);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*  998 */           if (localImage == null)
/*      */           {
/* 1000 */             l1 = Win32ShellFolder2.extractIcon(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), paramBoolean);
/*      */ 
/* 1002 */             localImage = Win32ShellFolder2.makeIcon(l1, paramBoolean);
/* 1003 */             Win32ShellFolder2.disposeIcon(l1);
/*      */           }
/*      */ 
/* 1006 */           if (localImage == null) {
/* 1007 */             localImage = Win32ShellFolder2.this.getIcon(paramBoolean);
/*      */           }
/* 1009 */           return localImage;
/*      */         }
/*      */       });
/* 1012 */       if (paramBoolean)
/* 1013 */         this.largeIcon = localImage;
/*      */       else {
/* 1015 */         this.smallIcon = localImage;
/*      */       }
/*      */     }
/* 1018 */     return localImage;
/*      */   }
/*      */ 
/*      */   static Image getSystemIcon(SystemIcon paramSystemIcon)
/*      */   {
/* 1025 */     long l = getSystemIcon(paramSystemIcon.getIconID());
/* 1026 */     Image localImage = makeIcon(l, true);
/* 1027 */     disposeIcon(l);
/* 1028 */     return localImage;
/*      */   }
/*      */ 
/*      */   static Image getShell32Icon(int paramInt, boolean paramBoolean)
/*      */   {
/* 1035 */     boolean bool = true;
/*      */ 
/* 1037 */     int i = paramBoolean ? 32 : 16;
/*      */ 
/* 1039 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1040 */     String str = (String)localToolkit.getDesktopProperty("win.icon.shellIconBPP");
/* 1041 */     if (str != null) {
/* 1042 */       bool = str.equals("4");
/*      */     }
/*      */ 
/* 1045 */     long l = getIconResource("shell32.dll", paramInt, i, i, bool);
/* 1046 */     if (l != 0L) {
/* 1047 */       Image localImage = makeIcon(l, paramBoolean);
/* 1048 */       disposeIcon(l);
/* 1049 */       return localImage;
/*      */     }
/* 1051 */     return null;
/*      */   }
/*      */ 
/*      */   public File getCanonicalFile()
/*      */     throws IOException
/*      */   {
/* 1061 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean isSpecial()
/*      */   {
/* 1068 */     return (this.isPersonal) || (!isFileSystem()) || (this == getDesktop());
/*      */   }
/*      */ 
/*      */   public int compareTo(File paramFile)
/*      */   {
/* 1077 */     if (!(paramFile instanceof Win32ShellFolder2)) {
/* 1078 */       if ((isFileSystem()) && (!isSpecial())) {
/* 1079 */         return super.compareTo(paramFile);
/*      */       }
/* 1081 */       return -1;
/*      */     }
/*      */ 
/* 1084 */     return Win32ShellFolderManager2.compareShellFolders(this, (Win32ShellFolder2)paramFile);
/*      */   }
/*      */ 
/*      */   public ShellFolderColumnInfo[] getFolderColumns()
/*      */   {
/* 1093 */     return (ShellFolderColumnInfo[])invoke(new Callable() {
/*      */       public ShellFolderColumnInfo[] call() {
/* 1095 */         ShellFolderColumnInfo[] arrayOfShellFolderColumnInfo = Win32ShellFolder2.this.doGetColumnInfo(Win32ShellFolder2.access$200(Win32ShellFolder2.this));
/*      */ 
/* 1097 */         if (arrayOfShellFolderColumnInfo != null) {
/* 1098 */           ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1100 */           for (int i = 0; i < arrayOfShellFolderColumnInfo.length; i++) {
/* 1101 */             ShellFolderColumnInfo localShellFolderColumnInfo = arrayOfShellFolderColumnInfo[i];
/* 1102 */             if (localShellFolderColumnInfo != null) {
/* 1103 */               localShellFolderColumnInfo.setAlignment(Integer.valueOf(localShellFolderColumnInfo.getAlignment().intValue() == 2 ? 0 : localShellFolderColumnInfo.getAlignment().intValue() == 1 ? 4 : 10));
/*      */ 
/* 1109 */               localShellFolderColumnInfo.setComparator(new Win32ShellFolder2.ColumnComparator(Win32ShellFolder2.this, i));
/*      */ 
/* 1111 */               localArrayList.add(localShellFolderColumnInfo);
/*      */             }
/*      */           }
/* 1114 */           arrayOfShellFolderColumnInfo = new ShellFolderColumnInfo[localArrayList.size()];
/* 1115 */           localArrayList.toArray(arrayOfShellFolderColumnInfo);
/*      */         }
/* 1117 */         return arrayOfShellFolderColumnInfo;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public Object getFolderColumnValue(final int paramInt) {
/* 1123 */     return invoke(new Callable() {
/*      */       public Object call() {
/* 1125 */         return Win32ShellFolder2.this.doGetColumnValue(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), paramInt);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private native ShellFolderColumnInfo[] doGetColumnInfo(long paramLong);
/*      */ 
/*      */   private native Object doGetColumnValue(long paramLong1, long paramLong2, int paramInt);
/*      */ 
/*      */   private static native int compareIDsByColumn(long paramLong1, long paramLong2, long paramLong3, int paramInt);
/*      */ 
/*      */   public void sortChildren(final List<? extends File> paramList)
/*      */   {
/* 1143 */     invoke(new Callable() {
/*      */       public Void call() {
/* 1145 */         Collections.sort(paramList, new Win32ShellFolder2.ColumnComparator(Win32ShellFolder2.this, 0));
/*      */ 
/* 1147 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   77 */     initIDs();
/*      */   }
/*      */ 
/*      */   private static class ColumnComparator
/*      */     implements Comparator<File>
/*      */   {
/*      */     private final Win32ShellFolder2 shellFolder;
/*      */     private final int columnIdx;
/*      */ 
/*      */     public ColumnComparator(Win32ShellFolder2 paramWin32ShellFolder2, int paramInt)
/*      */     {
/* 1158 */       this.shellFolder = paramWin32ShellFolder2;
/* 1159 */       this.columnIdx = paramInt;
/*      */     }
/*      */ 
/*      */     public int compare(final File paramFile1, final File paramFile2)
/*      */     {
/* 1164 */       Integer localInteger = (Integer)ShellFolder.invoke(new Callable() {
/*      */         public Integer call() {
/* 1166 */           if (((paramFile1 instanceof Win32ShellFolder2)) && ((paramFile2 instanceof Win32ShellFolder2)))
/*      */           {
/* 1169 */             return Integer.valueOf(Win32ShellFolder2.compareIDsByColumn(Win32ShellFolder2.access$200(Win32ShellFolder2.this), ((Win32ShellFolder2)paramFile1).getRelativePIDL(), ((Win32ShellFolder2)paramFile2).getRelativePIDL(), Win32ShellFolder2.ColumnComparator.this.columnIdx));
/*      */           }
/*      */ 
/* 1174 */           return Integer.valueOf(0);
/*      */         }
/*      */       });
/* 1178 */       return localInteger == null ? 0 : localInteger.intValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FolderDisposer
/*      */     implements DisposerRecord
/*      */   {
/*      */     long absolutePIDL;
/*      */     long pIShellFolder;
/*      */     long relativePIDL;
/*      */     boolean disposed;
/*      */ 
/*      */     public void dispose()
/*      */     {
/*  181 */       if (this.disposed) return;
/*  182 */       ShellFolder.invoke(new Callable() {
/*      */         public Void call() {
/*  184 */           if (Win32ShellFolder2.FolderDisposer.this.relativePIDL != 0L) {
/*  185 */             Win32ShellFolder2.releasePIDL(Win32ShellFolder2.FolderDisposer.this.relativePIDL);
/*      */           }
/*  187 */           if (Win32ShellFolder2.FolderDisposer.this.absolutePIDL != 0L) {
/*  188 */             Win32ShellFolder2.releasePIDL(Win32ShellFolder2.FolderDisposer.this.absolutePIDL);
/*      */           }
/*  190 */           if (Win32ShellFolder2.FolderDisposer.this.pIShellFolder != 0L) {
/*  191 */             Win32ShellFolder2.releaseIShellFolder(Win32ShellFolder2.FolderDisposer.this.pIShellFolder);
/*      */           }
/*  193 */           return null;
/*      */         }
/*      */       });
/*  196 */       this.disposed = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum SystemIcon
/*      */   {
/*  145 */     IDI_APPLICATION(32512), 
/*  146 */     IDI_HAND(32513), 
/*  147 */     IDI_ERROR(32513), 
/*  148 */     IDI_QUESTION(32514), 
/*  149 */     IDI_EXCLAMATION(32515), 
/*  150 */     IDI_WARNING(32515), 
/*  151 */     IDI_ASTERISK(32516), 
/*  152 */     IDI_INFORMATION(32516), 
/*  153 */     IDI_WINLOGO(32517);
/*      */ 
/*      */     private final int iconID;
/*      */ 
/*      */     private SystemIcon(int paramInt) {
/*  158 */       this.iconID = paramInt;
/*      */     }
/*      */ 
/*      */     public int getIconID() {
/*  162 */       return this.iconID;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.shell.Win32ShellFolder2
 * JD-Core Version:    0.6.2
 */