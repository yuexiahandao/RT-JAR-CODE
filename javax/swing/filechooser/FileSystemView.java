/*     */ package javax.swing.filechooser;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import sun.awt.shell.ShellFolder;
/*     */ 
/*     */ public abstract class FileSystemView
/*     */ {
/*  68 */   static FileSystemView windowsFileSystemView = null;
/*  69 */   static FileSystemView unixFileSystemView = null;
/*     */ 
/*  71 */   static FileSystemView genericFileSystemView = null;
/*     */ 
/*  73 */   private boolean useSystemExtensionHiding = UIManager.getDefaults().getBoolean("FileChooser.useSystemExtensionHiding");
/*     */ 
/*     */   public static FileSystemView getFileSystemView()
/*     */   {
/*  77 */     if (File.separatorChar == '\\') {
/*  78 */       if (windowsFileSystemView == null) {
/*  79 */         windowsFileSystemView = new WindowsFileSystemView();
/*     */       }
/*  81 */       return windowsFileSystemView;
/*     */     }
/*     */ 
/*  84 */     if (File.separatorChar == '/') {
/*  85 */       if (unixFileSystemView == null) {
/*  86 */         unixFileSystemView = new UnixFileSystemView();
/*     */       }
/*  88 */       return unixFileSystemView;
/*     */     }
/*     */ 
/*  98 */     if (genericFileSystemView == null) {
/*  99 */       genericFileSystemView = new GenericFileSystemView();
/*     */     }
/* 101 */     return genericFileSystemView;
/*     */   }
/*     */ 
/*     */   public FileSystemView() {
/* 105 */     final WeakReference localWeakReference = new WeakReference(this);
/*     */ 
/* 107 */     UIManager.addPropertyChangeListener(new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/* 109 */         FileSystemView localFileSystemView = (FileSystemView)localWeakReference.get();
/*     */ 
/* 111 */         if (localFileSystemView == null)
/*     */         {
/* 113 */           UIManager.removePropertyChangeListener(this);
/*     */         }
/* 115 */         else if (paramAnonymousPropertyChangeEvent.getPropertyName().equals("lookAndFeel"))
/* 116 */           localFileSystemView.useSystemExtensionHiding = UIManager.getDefaults().getBoolean("FileChooser.useSystemExtensionHiding");
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public boolean isRoot(File paramFile)
/*     */   {
/* 137 */     if ((paramFile == null) || (!paramFile.isAbsolute())) {
/* 138 */       return false;
/*     */     }
/*     */ 
/* 141 */     File[] arrayOfFile1 = getRoots();
/* 142 */     for (File localFile : arrayOfFile1) {
/* 143 */       if (localFile.equals(paramFile)) {
/* 144 */         return true;
/*     */       }
/*     */     }
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   public Boolean isTraversable(File paramFile)
/*     */   {
/* 161 */     return Boolean.valueOf(paramFile.isDirectory());
/*     */   }
/*     */ 
/*     */   public String getSystemDisplayName(File paramFile)
/*     */   {
/* 177 */     if (paramFile == null) {
/* 178 */       return null;
/*     */     }
/*     */ 
/* 181 */     String str = paramFile.getName();
/*     */ 
/* 183 */     if ((!str.equals("..")) && (!str.equals(".")) && ((this.useSystemExtensionHiding) || (!isFileSystem(paramFile)) || (isFileSystemRoot(paramFile))) && (((paramFile instanceof ShellFolder)) || (paramFile.exists())))
/*     */     {
/*     */       try
/*     */       {
/* 188 */         str = getShellFolder(paramFile).getDisplayName();
/*     */       } catch (FileNotFoundException localFileNotFoundException) {
/* 190 */         return null;
/*     */       }
/*     */ 
/* 193 */       if ((str == null) || (str.length() == 0)) {
/* 194 */         str = paramFile.getPath();
/*     */       }
/*     */     }
/*     */ 
/* 198 */     return str;
/*     */   }
/*     */ 
/*     */   public String getSystemTypeDescription(File paramFile)
/*     */   {
/* 215 */     return null;
/*     */   }
/*     */ 
/*     */   public Icon getSystemIcon(File paramFile)
/*     */   {
/* 231 */     if (paramFile == null) {
/* 232 */       return null;
/*     */     }
/*     */ 
/*     */     ShellFolder localShellFolder;
/*     */     try
/*     */     {
/* 238 */       localShellFolder = getShellFolder(paramFile);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 240 */       return null;
/*     */     }
/*     */ 
/* 243 */     Image localImage = localShellFolder.getIcon(false);
/*     */ 
/* 245 */     if (localImage != null) {
/* 246 */       return new ImageIcon(localImage, localShellFolder.getFolderType());
/*     */     }
/* 248 */     return UIManager.getIcon(paramFile.isDirectory() ? "FileView.directoryIcon" : "FileView.fileIcon");
/*     */   }
/*     */ 
/*     */   public boolean isParent(File paramFile1, File paramFile2)
/*     */   {
/* 263 */     if ((paramFile1 == null) || (paramFile2 == null))
/* 264 */       return false;
/* 265 */     if ((paramFile1 instanceof ShellFolder)) {
/* 266 */       File localFile1 = paramFile2.getParentFile();
/* 267 */       if ((localFile1 != null) && (localFile1.equals(paramFile1))) {
/* 268 */         return true;
/*     */       }
/* 270 */       File[] arrayOfFile1 = getFiles(paramFile1, false);
/* 271 */       for (File localFile2 : arrayOfFile1) {
/* 272 */         if (paramFile2.equals(localFile2)) {
/* 273 */           return true;
/*     */         }
/*     */       }
/* 276 */       return false;
/*     */     }
/* 278 */     return paramFile1.equals(paramFile2.getParentFile());
/*     */   }
/*     */ 
/*     */   public File getChild(File paramFile, String paramString)
/*     */   {
/* 293 */     if ((paramFile instanceof ShellFolder)) {
/* 294 */       File[] arrayOfFile1 = getFiles(paramFile, false);
/* 295 */       for (File localFile : arrayOfFile1) {
/* 296 */         if (localFile.getName().equals(paramString)) {
/* 297 */           return localFile;
/*     */         }
/*     */       }
/*     */     }
/* 301 */     return createFileObject(paramFile, paramString);
/*     */   }
/*     */ 
/*     */   public boolean isFileSystem(File paramFile)
/*     */   {
/* 315 */     if ((paramFile instanceof ShellFolder)) {
/* 316 */       ShellFolder localShellFolder = (ShellFolder)paramFile;
/*     */ 
/* 319 */       return (localShellFolder.isFileSystem()) && ((!localShellFolder.isLink()) || (!localShellFolder.isDirectory()));
/*     */     }
/* 321 */     return true;
/*     */   }
/*     */ 
/*     */   public abstract File createNewFolder(File paramFile)
/*     */     throws IOException;
/*     */ 
/*     */   public boolean isHiddenFile(File paramFile)
/*     */   {
/* 334 */     return paramFile.isHidden();
/*     */   }
/*     */ 
/*     */   public boolean isFileSystemRoot(File paramFile)
/*     */   {
/* 348 */     return ShellFolder.isFileSystemRoot(paramFile);
/*     */   }
/*     */ 
/*     */   public boolean isDrive(File paramFile)
/*     */   {
/* 362 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isFloppyDrive(File paramFile)
/*     */   {
/* 376 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isComputerNode(File paramFile)
/*     */   {
/* 390 */     return ShellFolder.isComputerNode(paramFile);
/*     */   }
/*     */ 
/*     */   public File[] getRoots()
/*     */   {
/* 401 */     File[] arrayOfFile = (File[])ShellFolder.get("roots");
/*     */ 
/* 403 */     for (int i = 0; i < arrayOfFile.length; i++) {
/* 404 */       if (isFileSystemRoot(arrayOfFile[i])) {
/* 405 */         arrayOfFile[i] = createFileSystemRoot(arrayOfFile[i]);
/*     */       }
/*     */     }
/* 408 */     return arrayOfFile;
/*     */   }
/*     */ 
/*     */   public File getHomeDirectory()
/*     */   {
/* 418 */     return createFileObject(System.getProperty("user.home"));
/*     */   }
/*     */ 
/*     */   public File getDefaultDirectory()
/*     */   {
/* 429 */     File localFile = (File)ShellFolder.get("fileChooserDefaultFolder");
/* 430 */     if (isFileSystemRoot(localFile)) {
/* 431 */       localFile = createFileSystemRoot(localFile);
/*     */     }
/* 433 */     return localFile;
/*     */   }
/*     */ 
/*     */   public File createFileObject(File paramFile, String paramString)
/*     */   {
/* 440 */     if (paramFile == null) {
/* 441 */       return new File(paramString);
/*     */     }
/* 443 */     return new File(paramFile, paramString);
/*     */   }
/*     */ 
/*     */   public File createFileObject(String paramString)
/*     */   {
/* 451 */     File localFile = new File(paramString);
/* 452 */     if (isFileSystemRoot(localFile)) {
/* 453 */       localFile = createFileSystemRoot(localFile);
/*     */     }
/* 455 */     return localFile;
/*     */   }
/*     */ 
/*     */   public File[] getFiles(File paramFile, boolean paramBoolean)
/*     */   {
/* 463 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 466 */     if (!(paramFile instanceof ShellFolder)) {
/*     */       try {
/* 468 */         paramFile = getShellFolder(paramFile);
/*     */       } catch (FileNotFoundException localFileNotFoundException1) {
/* 470 */         return new File[0];
/*     */       }
/*     */     }
/*     */ 
/* 474 */     File[] arrayOfFile1 = ((ShellFolder)paramFile).listFiles(!paramBoolean);
/*     */ 
/* 476 */     if (arrayOfFile1 == null) {
/* 477 */       return new File[0];
/*     */     }
/*     */ 
/* 480 */     for (Object localObject : arrayOfFile1) {
/* 481 */       if (Thread.currentThread().isInterrupted())
/*     */       {
/*     */         break;
/*     */       }
/* 485 */       if (!(localObject instanceof ShellFolder)) {
/* 486 */         if (isFileSystemRoot((File)localObject))
/* 487 */           localObject = createFileSystemRoot((File)localObject);
/*     */         try
/*     */         {
/* 490 */           localObject = ShellFolder.getShellFolder((File)localObject);
/*     */         }
/*     */         catch (FileNotFoundException localFileNotFoundException2)
/*     */         {
/* 494 */           continue;
/*     */         }
/*     */         catch (InternalError localInternalError)
/*     */         {
/* 498 */           continue;
/*     */         }
/*     */       }
/* 501 */       if ((!paramBoolean) || (!isHiddenFile((File)localObject))) {
/* 502 */         localArrayList.add(localObject);
/*     */       }
/*     */     }
/*     */ 
/* 506 */     return (File[])localArrayList.toArray(new File[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public File getParentDirectory(File paramFile)
/*     */   {
/* 518 */     if ((paramFile == null) || (!paramFile.exists())) {
/* 519 */       return null;
/*     */     }
/*     */ 
/*     */     ShellFolder localShellFolder;
/*     */     try
/*     */     {
/* 525 */       localShellFolder = getShellFolder(paramFile);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 527 */       return null;
/*     */     }
/*     */ 
/* 530 */     File localFile1 = localShellFolder.getParentFile();
/*     */ 
/* 532 */     if (localFile1 == null) {
/* 533 */       return null;
/*     */     }
/*     */ 
/* 536 */     if (isFileSystem(localFile1)) {
/* 537 */       File localFile2 = localFile1;
/* 538 */       if (!localFile2.exists())
/*     */       {
/* 540 */         File localFile3 = localFile1.getParentFile();
/* 541 */         if ((localFile3 == null) || (!isFileSystem(localFile3)))
/*     */         {
/* 543 */           localFile2 = createFileSystemRoot(localFile2);
/*     */         }
/*     */       }
/* 546 */       return localFile2;
/*     */     }
/* 548 */     return localFile1;
/*     */   }
/*     */ 
/*     */   ShellFolder getShellFolder(File paramFile)
/*     */     throws FileNotFoundException
/*     */   {
/* 556 */     if ((!(paramFile instanceof ShellFolder)) && (!(paramFile instanceof FileSystemRoot)) && (isFileSystemRoot(paramFile))) {
/* 557 */       paramFile = createFileSystemRoot(paramFile);
/*     */     }
/*     */     try
/*     */     {
/* 561 */       return ShellFolder.getShellFolder(paramFile);
/*     */     } catch (InternalError localInternalError) {
/* 563 */       System.err.println("FileSystemView.getShellFolder: f=" + paramFile);
/* 564 */       localInternalError.printStackTrace();
/* 565 */     }return null;
/*     */   }
/*     */ 
/*     */   protected File createFileSystemRoot(File paramFile)
/*     */   {
/* 579 */     return new FileSystemRoot(paramFile);
/*     */   }
/*     */ 
/*     */   static class FileSystemRoot extends File
/*     */   {
/*     */     public FileSystemRoot(File paramFile)
/*     */     {
/* 587 */       super("");
/*     */     }
/*     */ 
/*     */     public FileSystemRoot(String paramString) {
/* 591 */       super();
/*     */     }
/*     */ 
/*     */     public boolean isDirectory() {
/* 595 */       return true;
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 599 */       return getPath();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.filechooser.FileSystemView
 * JD-Core Version:    0.6.2
 */