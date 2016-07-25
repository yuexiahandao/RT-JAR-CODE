/*     */ package sun.awt.shell;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.net.URI;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.Callable;
/*     */ 
/*     */ public abstract class ShellFolder extends File
/*     */ {
/*     */   private static final String COLUMN_NAME = "FileChooser.fileNameHeaderText";
/*     */   private static final String COLUMN_SIZE = "FileChooser.fileSizeHeaderText";
/*     */   private static final String COLUMN_DATE = "FileChooser.fileDateHeaderText";
/*     */   protected ShellFolder parent;
/*     */   private static ShellFolderManager shellFolderManager;
/* 231 */   private static Invoker invoker = shellFolderManager.createInvoker();
/*     */ 
/* 554 */   private static final Comparator DEFAULT_COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2)
/*     */     {
/*     */       int i;
/* 558 */       if ((paramAnonymousObject1 == null) && (paramAnonymousObject2 == null))
/* 559 */         i = 0;
/* 560 */       else if ((paramAnonymousObject1 != null) && (paramAnonymousObject2 == null))
/* 561 */         i = 1;
/* 562 */       else if ((paramAnonymousObject1 == null) && (paramAnonymousObject2 != null))
/* 563 */         i = -1;
/* 564 */       else if ((paramAnonymousObject1 instanceof Comparable))
/* 565 */         i = ((Comparable)paramAnonymousObject1).compareTo(paramAnonymousObject2);
/*     */       else {
/* 567 */         i = 0;
/*     */       }
/*     */ 
/* 570 */       return i;
/*     */     }
/* 554 */   };
/*     */ 
/* 574 */   private static final Comparator<File> FILE_COMPARATOR = new Comparator() {
/*     */     public int compare(File paramAnonymousFile1, File paramAnonymousFile2) {
/* 576 */       ShellFolder localShellFolder1 = null;
/* 577 */       ShellFolder localShellFolder2 = null;
/*     */ 
/* 579 */       if ((paramAnonymousFile1 instanceof ShellFolder)) {
/* 580 */         localShellFolder1 = (ShellFolder)paramAnonymousFile1;
/* 581 */         if (localShellFolder1.isFileSystem()) {
/* 582 */           localShellFolder1 = null;
/*     */         }
/*     */       }
/* 585 */       if ((paramAnonymousFile2 instanceof ShellFolder)) {
/* 586 */         localShellFolder2 = (ShellFolder)paramAnonymousFile2;
/* 587 */         if (localShellFolder2.isFileSystem()) {
/* 588 */           localShellFolder2 = null;
/*     */         }
/*     */       }
/*     */ 
/* 592 */       if ((localShellFolder1 != null) && (localShellFolder2 != null))
/* 593 */         return localShellFolder1.compareTo(localShellFolder2);
/* 594 */       if (localShellFolder1 != null)
/*     */       {
/* 596 */         return -1;
/* 597 */       }if (localShellFolder2 != null) {
/* 598 */         return 1;
/*     */       }
/* 600 */       String str1 = paramAnonymousFile1.getName();
/* 601 */       String str2 = paramAnonymousFile2.getName();
/*     */ 
/* 604 */       int i = str1.compareToIgnoreCase(str2);
/* 605 */       if (i != 0) {
/* 606 */         return i;
/*     */       }
/*     */ 
/* 610 */       return str1.compareTo(str2);
/*     */     }
/* 574 */   };
/*     */ 
/*     */   ShellFolder(ShellFolder paramShellFolder, String paramString)
/*     */   {
/*  52 */     super(paramString != null ? paramString : "ShellFolder");
/*  53 */     this.parent = paramShellFolder;
/*     */   }
/*     */ 
/*     */   public boolean isFileSystem()
/*     */   {
/*  60 */     return !getPath().startsWith("ShellFolder");
/*     */   }
/*     */ 
/*     */   protected abstract Object writeReplace()
/*     */     throws ObjectStreamException;
/*     */ 
/*     */   public String getParent()
/*     */   {
/*  88 */     if ((this.parent == null) && (isFileSystem())) {
/*  89 */       return super.getParent();
/*     */     }
/*  91 */     if (this.parent != null) {
/*  92 */       return this.parent.getPath();
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public File getParentFile()
/*     */   {
/* 111 */     if (this.parent != null)
/* 112 */       return this.parent;
/* 113 */     if (isFileSystem()) {
/* 114 */       return super.getParentFile();
/*     */     }
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   public File[] listFiles()
/*     */   {
/* 121 */     return listFiles(true);
/*     */   }
/*     */ 
/*     */   public File[] listFiles(boolean paramBoolean) {
/* 125 */     File[] arrayOfFile = super.listFiles();
/*     */ 
/* 127 */     if (!paramBoolean) {
/* 128 */       Vector localVector = new Vector();
/* 129 */       int i = arrayOfFile == null ? 0 : arrayOfFile.length;
/* 130 */       for (int j = 0; j < i; j++) {
/* 131 */         if (!arrayOfFile[j].isHidden()) {
/* 132 */           localVector.addElement(arrayOfFile[j]);
/*     */         }
/*     */       }
/* 135 */       arrayOfFile = (File[])localVector.toArray(new File[localVector.size()]);
/*     */     }
/*     */ 
/* 138 */     return arrayOfFile;
/*     */   }
/*     */ 
/*     */   public abstract boolean isLink();
/*     */ 
/*     */   public abstract ShellFolder getLinkLocation()
/*     */     throws FileNotFoundException;
/*     */ 
/*     */   public abstract String getDisplayName();
/*     */ 
/*     */   public abstract String getFolderType();
/*     */ 
/*     */   public abstract String getExecutableType();
/*     */ 
/*     */   public int compareTo(File paramFile)
/*     */   {
/* 174 */     if ((paramFile == null) || (!(paramFile instanceof ShellFolder)) || (((paramFile instanceof ShellFolder)) && (((ShellFolder)paramFile).isFileSystem())))
/*     */     {
/* 177 */       if (isFileSystem()) {
/* 178 */         return super.compareTo(paramFile);
/*     */       }
/* 180 */       return -1;
/*     */     }
/*     */ 
/* 183 */     if (isFileSystem()) {
/* 184 */       return 1;
/*     */     }
/* 186 */     return getName().compareTo(paramFile.getName());
/*     */   }
/*     */ 
/*     */   public Image getIcon(boolean paramBoolean)
/*     */   {
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   public static ShellFolder getShellFolder(File paramFile)
/*     */     throws FileNotFoundException
/*     */   {
/* 239 */     if ((paramFile instanceof ShellFolder)) {
/* 240 */       return (ShellFolder)paramFile;
/*     */     }
/* 242 */     if (!paramFile.exists()) {
/* 243 */       throw new FileNotFoundException();
/*     */     }
/* 245 */     return shellFolderManager.createShellFolder(paramFile);
/*     */   }
/*     */ 
/*     */   public static Object get(String paramString)
/*     */   {
/* 254 */     return shellFolderManager.get(paramString);
/*     */   }
/*     */ 
/*     */   public static boolean isComputerNode(File paramFile)
/*     */   {
/* 262 */     return shellFolderManager.isComputerNode(paramFile);
/*     */   }
/*     */ 
/*     */   public static boolean isFileSystemRoot(File paramFile)
/*     */   {
/* 269 */     return shellFolderManager.isFileSystemRoot(paramFile);
/*     */   }
/*     */ 
/*     */   public static File getNormalizedFile(File paramFile)
/*     */     throws IOException
/*     */   {
/* 277 */     File localFile = paramFile.getCanonicalFile();
/* 278 */     if (paramFile.equals(localFile))
/*     */     {
/* 280 */       return localFile;
/*     */     }
/*     */ 
/* 284 */     return new File(paramFile.toURI().normalize());
/*     */   }
/*     */ 
/*     */   public static void sort(List<? extends File> paramList)
/*     */   {
/* 290 */     if ((paramList == null) || (paramList.size() <= 1)) {
/* 291 */       return;
/*     */     }
/*     */ 
/* 296 */     invoke(new Callable()
/*     */     {
/*     */       public Void call()
/*     */       {
/* 301 */         Object localObject = null;
/*     */ 
/* 303 */         for (File localFile1 : this.val$files) {
/* 304 */           File localFile2 = localFile1.getParentFile();
/*     */ 
/* 306 */           if ((localFile2 == null) || (!(localFile1 instanceof ShellFolder))) {
/* 307 */             localObject = null;
/*     */ 
/* 309 */             break;
/*     */           }
/*     */ 
/* 312 */           if (localObject == null) {
/* 313 */             localObject = localFile2;
/*     */           }
/* 315 */           else if ((localObject != localFile2) && (!localObject.equals(localFile2))) {
/* 316 */             localObject = null;
/*     */ 
/* 318 */             break;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 323 */         if ((localObject instanceof ShellFolder))
/* 324 */           ((ShellFolder)localObject).sortChildren(this.val$files);
/*     */         else {
/* 326 */           Collections.sort(this.val$files, ShellFolder.FILE_COMPARATOR);
/*     */         }
/*     */ 
/* 329 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void sortChildren(final List<? extends File> paramList)
/*     */   {
/* 337 */     invoke(new Callable() {
/*     */       public Void call() {
/* 339 */         Collections.sort(paramList, ShellFolder.FILE_COMPARATOR);
/*     */ 
/* 341 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public boolean isAbsolute() {
/* 347 */     return (!isFileSystem()) || (super.isAbsolute());
/*     */   }
/*     */ 
/*     */   public File getAbsoluteFile() {
/* 351 */     return isFileSystem() ? super.getAbsoluteFile() : this;
/*     */   }
/*     */ 
/*     */   public boolean canRead() {
/* 355 */     return isFileSystem() ? super.canRead() : true;
/*     */   }
/*     */ 
/*     */   public boolean canWrite()
/*     */   {
/* 364 */     return isFileSystem() ? super.canWrite() : false;
/*     */   }
/*     */ 
/*     */   public boolean exists()
/*     */   {
/* 370 */     return (!isFileSystem()) || (isFileSystemRoot(this)) || (super.exists());
/*     */   }
/*     */ 
/*     */   public boolean isDirectory() {
/* 374 */     return isFileSystem() ? super.isDirectory() : true;
/*     */   }
/*     */ 
/*     */   public boolean isFile() {
/* 378 */     return !isDirectory() ? true : isFileSystem() ? super.isFile() : false;
/*     */   }
/*     */ 
/*     */   public long lastModified() {
/* 382 */     return isFileSystem() ? super.lastModified() : 0L;
/*     */   }
/*     */ 
/*     */   public long length() {
/* 386 */     return isFileSystem() ? super.length() : 0L;
/*     */   }
/*     */ 
/*     */   public boolean createNewFile() throws IOException {
/* 390 */     return isFileSystem() ? super.createNewFile() : false;
/*     */   }
/*     */ 
/*     */   public boolean delete() {
/* 394 */     return isFileSystem() ? super.delete() : false;
/*     */   }
/*     */ 
/*     */   public void deleteOnExit() {
/* 398 */     if (isFileSystem())
/* 399 */       super.deleteOnExit();
/*     */   }
/*     */ 
/*     */   public boolean mkdir()
/*     */   {
/* 406 */     return isFileSystem() ? super.mkdir() : false;
/*     */   }
/*     */ 
/*     */   public boolean mkdirs() {
/* 410 */     return isFileSystem() ? super.mkdirs() : false;
/*     */   }
/*     */ 
/*     */   public boolean renameTo(File paramFile) {
/* 414 */     return isFileSystem() ? super.renameTo(paramFile) : false;
/*     */   }
/*     */ 
/*     */   public boolean setLastModified(long paramLong) {
/* 418 */     return isFileSystem() ? super.setLastModified(paramLong) : false;
/*     */   }
/*     */ 
/*     */   public boolean setReadOnly() {
/* 422 */     return isFileSystem() ? super.setReadOnly() : false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 426 */     return isFileSystem() ? super.toString() : getDisplayName();
/*     */   }
/*     */ 
/*     */   public static ShellFolderColumnInfo[] getFolderColumns(File paramFile) {
/* 430 */     ShellFolderColumnInfo[] arrayOfShellFolderColumnInfo = null;
/*     */ 
/* 432 */     if ((paramFile instanceof ShellFolder)) {
/* 433 */       arrayOfShellFolderColumnInfo = ((ShellFolder)paramFile).getFolderColumns();
/*     */     }
/*     */ 
/* 436 */     if (arrayOfShellFolderColumnInfo == null) {
/* 437 */       arrayOfShellFolderColumnInfo = new ShellFolderColumnInfo[] { new ShellFolderColumnInfo("FileChooser.fileNameHeaderText", Integer.valueOf(150), Integer.valueOf(10), true, null, FILE_COMPARATOR), new ShellFolderColumnInfo("FileChooser.fileSizeHeaderText", Integer.valueOf(75), Integer.valueOf(4), true, null, DEFAULT_COMPARATOR, true), new ShellFolderColumnInfo("FileChooser.fileDateHeaderText", Integer.valueOf(130), Integer.valueOf(10), true, null, DEFAULT_COMPARATOR, true) };
/*     */     }
/*     */ 
/* 450 */     return arrayOfShellFolderColumnInfo;
/*     */   }
/*     */ 
/*     */   public ShellFolderColumnInfo[] getFolderColumns() {
/* 454 */     return null;
/*     */   }
/*     */ 
/*     */   public static Object getFolderColumnValue(File paramFile, int paramInt) {
/* 458 */     if ((paramFile instanceof ShellFolder)) {
/* 459 */       Object localObject = ((ShellFolder)paramFile).getFolderColumnValue(paramInt);
/* 460 */       if (localObject != null) {
/* 461 */         return localObject;
/*     */       }
/*     */     }
/*     */ 
/* 465 */     if ((paramFile == null) || (!paramFile.exists())) {
/* 466 */       return null;
/*     */     }
/*     */ 
/* 469 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/* 472 */       return paramFile;
/*     */     case 1:
/* 475 */       return paramFile.isDirectory() ? null : Long.valueOf(paramFile.length());
/*     */     case 2:
/* 478 */       if (isFileSystemRoot(paramFile)) {
/* 479 */         return null;
/*     */       }
/* 481 */       long l = paramFile.lastModified();
/* 482 */       return l == 0L ? null : new Date(l);
/*     */     }
/*     */ 
/* 485 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getFolderColumnValue(int paramInt)
/*     */   {
/* 490 */     return null;
/*     */   }
/*     */ 
/*     */   public static <T> T invoke(Callable<T> paramCallable)
/*     */   {
/*     */     try
/*     */     {
/* 500 */       return invoke(paramCallable, RuntimeException.class); } catch (InterruptedException localInterruptedException) {
/*     */     }
/* 502 */     return null;
/*     */   }
/*     */ 
/*     */   public static <T, E extends Throwable> T invoke(Callable<T> paramCallable, Class<E> paramClass)
/*     */     throws InterruptedException, Throwable
/*     */   {
/*     */     try
/*     */     {
/* 514 */       return invoker.invoke(paramCallable);
/*     */     } catch (Exception localException) {
/* 516 */       if ((localException instanceof RuntimeException))
/*     */       {
/* 518 */         throw ((RuntimeException)localException);
/*     */       }
/*     */ 
/* 521 */       if ((localException instanceof InterruptedException))
/*     */       {
/* 523 */         Thread.currentThread().interrupt();
/*     */ 
/* 526 */         throw ((InterruptedException)localException);
/*     */       }
/*     */ 
/* 529 */       if (paramClass.isInstance(localException)) {
/* 530 */         throw ((Throwable)paramClass.cast(localException));
/*     */       }
/*     */ 
/* 533 */       throw new RuntimeException("Unexpected error", localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 207 */     String str = (String)Toolkit.getDefaultToolkit().getDesktopProperty("Shell.shellFolderManager");
/*     */ 
/* 209 */     Object localObject = null;
/*     */     try {
/* 211 */       localObject = Class.forName(str);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/*     */     }
/*     */     catch (NullPointerException localNullPointerException) {
/*     */     }
/* 217 */     if (localObject == null)
/* 218 */       localObject = ShellFolderManager.class;
/*     */     try
/*     */     {
/* 221 */       shellFolderManager = (ShellFolderManager)((Class)localObject).newInstance();
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 224 */       throw new Error("Could not instantiate Shell Folder Manager: " + ((Class)localObject).getName());
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 227 */       throw new Error("Could not access Shell Folder Manager: " + ((Class)localObject).getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Invoker
/*     */   {
/*     */     public abstract <T> T invoke(Callable<T> paramCallable)
/*     */       throws Exception;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.shell.ShellFolder
 * JD-Core Version:    0.6.2
 */