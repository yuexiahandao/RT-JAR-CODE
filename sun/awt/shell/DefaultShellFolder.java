/*     */ package sun.awt.shell;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.ObjectStreamException;
/*     */ 
/*     */ class DefaultShellFolder extends ShellFolder
/*     */ {
/*     */   DefaultShellFolder(ShellFolder paramShellFolder, File paramFile)
/*     */   {
/*  44 */     super(paramShellFolder, paramFile.getAbsolutePath());
/*     */   }
/*     */ 
/*     */   protected Object writeReplace()
/*     */     throws ObjectStreamException
/*     */   {
/*  56 */     return new File(getPath());
/*     */   }
/*     */ 
/*     */   public File[] listFiles()
/*     */   {
/*  64 */     File[] arrayOfFile = super.listFiles();
/*  65 */     if (arrayOfFile != null) {
/*  66 */       for (int i = 0; i < arrayOfFile.length; i++) {
/*  67 */         arrayOfFile[i] = new DefaultShellFolder(this, arrayOfFile[i]);
/*     */       }
/*     */     }
/*  70 */     return arrayOfFile;
/*     */   }
/*     */ 
/*     */   public boolean isLink()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isHidden()
/*     */   {
/*  84 */     String str = getName();
/*  85 */     if (str.length() > 0) {
/*  86 */       return str.charAt(0) == '.';
/*     */     }
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public ShellFolder getLinkLocation()
/*     */   {
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 103 */     return getName();
/*     */   }
/*     */ 
/*     */   public String getFolderType()
/*     */   {
/* 110 */     if (isDirectory()) {
/* 111 */       return "File Folder";
/*     */     }
/* 113 */     return "File";
/*     */   }
/*     */ 
/*     */   public String getExecutableType()
/*     */   {
/* 121 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.shell.DefaultShellFolder
 * JD-Core Version:    0.6.2
 */