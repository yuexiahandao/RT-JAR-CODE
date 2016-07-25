/*     */ package javax.swing.filechooser;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.MessageFormat;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ class UnixFileSystemView extends FileSystemView
/*     */ {
/* 609 */   private static final String newFolderString = UIManager.getString("FileChooser.other.newFolder");
/*     */ 
/* 611 */   private static final String newFolderNextString = UIManager.getString("FileChooser.other.newFolder.subsequent");
/*     */ 
/*     */   public File createNewFolder(File paramFile)
/*     */     throws IOException
/*     */   {
/* 618 */     if (paramFile == null) {
/* 619 */       throw new IOException("Containing directory is null:");
/*     */     }
/*     */ 
/* 623 */     File localFile = createFileObject(paramFile, newFolderString);
/* 624 */     int i = 1;
/* 625 */     while ((localFile.exists()) && (i < 100)) {
/* 626 */       localFile = createFileObject(paramFile, MessageFormat.format(newFolderNextString, new Object[] { new Integer(i) }));
/*     */ 
/* 628 */       i++;
/*     */     }
/*     */ 
/* 631 */     if (localFile.exists()) {
/* 632 */       throw new IOException("Directory already exists:" + localFile.getAbsolutePath());
/*     */     }
/* 634 */     localFile.mkdirs();
/*     */ 
/* 637 */     return localFile;
/*     */   }
/*     */ 
/*     */   public boolean isFileSystemRoot(File paramFile) {
/* 641 */     return (paramFile != null) && (paramFile.getAbsolutePath().equals("/"));
/*     */   }
/*     */ 
/*     */   public boolean isDrive(File paramFile) {
/* 645 */     return isFloppyDrive(paramFile);
/*     */   }
/*     */ 
/*     */   public boolean isFloppyDrive(File paramFile)
/*     */   {
/* 652 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isComputerNode(File paramFile) {
/* 656 */     if (paramFile != null) {
/* 657 */       String str = paramFile.getParent();
/* 658 */       if ((str != null) && (str.equals("/net"))) {
/* 659 */         return true;
/*     */       }
/*     */     }
/* 662 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.filechooser.UnixFileSystemView
 * JD-Core Version:    0.6.2
 */