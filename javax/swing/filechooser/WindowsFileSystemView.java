/*     */ package javax.swing.filechooser;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.MessageFormat;
/*     */ import javax.swing.UIManager;
/*     */ import sun.awt.shell.ShellFolder;
/*     */ 
/*     */ class WindowsFileSystemView extends FileSystemView
/*     */ {
/* 672 */   private static final String newFolderString = UIManager.getString("FileChooser.win32.newFolder");
/*     */ 
/* 674 */   private static final String newFolderNextString = UIManager.getString("FileChooser.win32.newFolder.subsequent");
/*     */ 
/*     */   public Boolean isTraversable(File paramFile)
/*     */   {
/* 678 */     return Boolean.valueOf((isFileSystemRoot(paramFile)) || (isComputerNode(paramFile)) || (paramFile.isDirectory()));
/*     */   }
/*     */ 
/*     */   public File getChild(File paramFile, String paramString) {
/* 682 */     if ((paramString.startsWith("\\")) && (!paramString.startsWith("\\\\")) && (isFileSystem(paramFile)))
/*     */     {
/* 687 */       String str = paramFile.getAbsolutePath();
/* 688 */       if ((str.length() >= 2) && (str.charAt(1) == ':') && (Character.isLetter(str.charAt(0))))
/*     */       {
/* 692 */         return createFileObject(str.substring(0, 2) + paramString);
/*     */       }
/*     */     }
/* 695 */     return super.getChild(paramFile, paramString);
/*     */   }
/*     */ 
/*     */   public String getSystemTypeDescription(File paramFile)
/*     */   {
/* 706 */     if (paramFile == null) {
/* 707 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 711 */       return getShellFolder(paramFile).getFolderType(); } catch (FileNotFoundException localFileNotFoundException) {
/*     */     }
/* 713 */     return null;
/*     */   }
/*     */ 
/*     */   public File getHomeDirectory()
/*     */   {
/* 721 */     File[] arrayOfFile = getRoots();
/* 722 */     return arrayOfFile.length == 0 ? null : arrayOfFile[0];
/*     */   }
/*     */ 
/*     */   public File createNewFolder(File paramFile)
/*     */     throws IOException
/*     */   {
/* 729 */     if (paramFile == null) {
/* 730 */       throw new IOException("Containing directory is null:");
/*     */     }
/*     */ 
/* 733 */     File localFile = createFileObject(paramFile, newFolderString);
/* 734 */     int i = 2;
/* 735 */     while ((localFile.exists()) && (i < 100)) {
/* 736 */       localFile = createFileObject(paramFile, MessageFormat.format(newFolderNextString, new Object[] { new Integer(i) }));
/*     */ 
/* 738 */       i++;
/*     */     }
/*     */ 
/* 741 */     if (localFile.exists()) {
/* 742 */       throw new IOException("Directory already exists:" + localFile.getAbsolutePath());
/*     */     }
/* 744 */     localFile.mkdirs();
/*     */ 
/* 747 */     return localFile;
/*     */   }
/*     */ 
/*     */   public boolean isDrive(File paramFile) {
/* 751 */     return isFileSystemRoot(paramFile);
/*     */   }
/*     */ 
/*     */   public boolean isFloppyDrive(final File paramFile) {
/* 755 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/* 757 */         return paramFile.getAbsolutePath();
/*     */       }
/*     */     });
/* 761 */     return (str != null) && ((str.equals("A:\\")) || (str.equals("B:\\")));
/*     */   }
/*     */ 
/*     */   public File createFileObject(String paramString)
/*     */   {
/* 769 */     if ((paramString.length() >= 2) && (paramString.charAt(1) == ':') && (Character.isLetter(paramString.charAt(0)))) {
/* 770 */       if (paramString.length() == 2)
/* 771 */         paramString = paramString + "\\";
/* 772 */       else if (paramString.charAt(2) != '\\') {
/* 773 */         paramString = paramString.substring(0, 2) + "\\" + paramString.substring(2);
/*     */       }
/*     */     }
/* 776 */     return super.createFileObject(paramString);
/*     */   }
/*     */ 
/*     */   protected File createFileSystemRoot(File paramFile)
/*     */   {
/* 782 */     return new FileSystemView.FileSystemRoot(paramFile) {
/*     */       public boolean exists() {
/* 784 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.filechooser.WindowsFileSystemView
 * JD-Core Version:    0.6.2
 */