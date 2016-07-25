/*     */ package javax.swing.filechooser;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ class GenericFileSystemView extends FileSystemView
/*     */ {
/* 796 */   private static final String newFolderString = UIManager.getString("FileChooser.other.newFolder");
/*     */ 
/*     */   public File createNewFolder(File paramFile)
/*     */     throws IOException
/*     */   {
/* 803 */     if (paramFile == null) {
/* 804 */       throw new IOException("Containing directory is null:");
/*     */     }
/*     */ 
/* 807 */     File localFile = createFileObject(paramFile, newFolderString);
/*     */ 
/* 809 */     if (localFile.exists()) {
/* 810 */       throw new IOException("Directory already exists:" + localFile.getAbsolutePath());
/*     */     }
/* 812 */     localFile.mkdirs();
/*     */ 
/* 815 */     return localFile;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.filechooser.GenericFileSystemView
 * JD-Core Version:    0.6.2
 */