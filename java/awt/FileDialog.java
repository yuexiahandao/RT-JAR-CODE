/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.FileDialogPeer;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.FileDialogAccessor;
/*     */ 
/*     */ public class FileDialog extends Dialog
/*     */ {
/*     */   public static final int LOAD = 0;
/*     */   public static final int SAVE = 1;
/*     */   int mode;
/*     */   String dir;
/*     */   String file;
/*     */   private File[] files;
/* 115 */   private boolean multipleMode = false;
/*     */   FilenameFilter filter;
/*     */   private static final String base = "filedlg";
/* 131 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = 5035145889651310422L;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public FileDialog(Frame paramFrame)
/*     */   {
/* 182 */     this(paramFrame, "", 0);
/*     */   }
/*     */ 
/*     */   public FileDialog(Frame paramFrame, String paramString)
/*     */   {
/* 195 */     this(paramFrame, paramString, 0);
/*     */   }
/*     */ 
/*     */   public FileDialog(Frame paramFrame, String paramString, int paramInt)
/*     */   {
/* 218 */     super(paramFrame, paramString, true);
/* 219 */     setMode(paramInt);
/* 220 */     setLayout(null);
/*     */   }
/*     */ 
/*     */   public FileDialog(Dialog paramDialog)
/*     */   {
/* 240 */     this(paramDialog, "", 0);
/*     */   }
/*     */ 
/*     */   public FileDialog(Dialog paramDialog, String paramString)
/*     */   {
/* 264 */     this(paramDialog, paramString, 0);
/*     */   }
/*     */ 
/*     */   public FileDialog(Dialog paramDialog, String paramString, int paramInt)
/*     */   {
/* 298 */     super(paramDialog, paramString, true);
/* 299 */     setMode(paramInt);
/* 300 */     setLayout(null);
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 308 */     synchronized (FileDialog.class) {
/* 309 */       return "filedlg" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 318 */     synchronized (getTreeLock()) {
/* 319 */       if ((this.parent != null) && (this.parent.getPeer() == null)) {
/* 320 */         this.parent.addNotify();
/*     */       }
/* 322 */       if (this.peer == null)
/* 323 */         this.peer = getToolkit().createFileDialog(this);
/* 324 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMode()
/*     */   {
/* 340 */     return this.mode;
/*     */   }
/*     */ 
/*     */   public void setMode(int paramInt)
/*     */   {
/* 359 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/* 362 */       this.mode = paramInt;
/* 363 */       break;
/*     */     default:
/* 365 */       throw new IllegalArgumentException("illegal file dialog mode");
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getDirectory()
/*     */   {
/* 377 */     return this.dir;
/*     */   }
/*     */ 
/*     */   public void setDirectory(String paramString)
/*     */   {
/* 395 */     this.dir = ((paramString != null) && (paramString.equals("")) ? null : paramString);
/* 396 */     FileDialogPeer localFileDialogPeer = (FileDialogPeer)this.peer;
/* 397 */     if (localFileDialogPeer != null)
/* 398 */       localFileDialogPeer.setDirectory(this.dir);
/*     */   }
/*     */ 
/*     */   public String getFile()
/*     */   {
/* 411 */     return this.file;
/*     */   }
/*     */ 
/*     */   public File[] getFiles()
/*     */   {
/* 427 */     synchronized (getObjectLock()) {
/* 428 */       if (this.files != null) {
/* 429 */         return (File[])this.files.clone();
/*     */       }
/* 431 */       return new File[0];
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setFiles(File[] paramArrayOfFile)
/*     */   {
/* 450 */     synchronized (getObjectLock()) {
/* 451 */       this.files = paramArrayOfFile;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFile(String paramString)
/*     */   {
/* 469 */     this.file = ((paramString != null) && (paramString.equals("")) ? null : paramString);
/* 470 */     FileDialogPeer localFileDialogPeer = (FileDialogPeer)this.peer;
/* 471 */     if (localFileDialogPeer != null)
/* 472 */       localFileDialogPeer.setFile(this.file);
/*     */   }
/*     */ 
/*     */   public void setMultipleMode(boolean paramBoolean)
/*     */   {
/* 485 */     synchronized (getObjectLock()) {
/* 486 */       this.multipleMode = paramBoolean;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isMultipleMode()
/*     */   {
/* 499 */     synchronized (getObjectLock()) {
/* 500 */       return this.multipleMode;
/*     */     }
/*     */   }
/*     */ 
/*     */   public FilenameFilter getFilenameFilter()
/*     */   {
/* 515 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public synchronized void setFilenameFilter(FilenameFilter paramFilenameFilter)
/*     */   {
/* 529 */     this.filter = paramFilenameFilter;
/* 530 */     FileDialogPeer localFileDialogPeer = (FileDialogPeer)this.peer;
/* 531 */     if (localFileDialogPeer != null)
/* 532 */       localFileDialogPeer.setFilenameFilter(paramFilenameFilter);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 547 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 550 */     if ((this.dir != null) && (this.dir.equals(""))) {
/* 551 */       this.dir = null;
/*     */     }
/* 553 */     if ((this.file != null) && (this.file.equals("")))
/* 554 */       this.file = null;
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 568 */     String str = super.paramString();
/* 569 */     str = str + ",dir= " + this.dir;
/* 570 */     str = str + ",file= " + this.file;
/* 571 */     return str + (this.mode == 0 ? ",load" : ",save");
/*     */   }
/*     */ 
/*     */   boolean postsOldMouseEvents() {
/* 575 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 141 */     Toolkit.loadLibraries();
/* 142 */     if (!GraphicsEnvironment.isHeadless()) {
/* 143 */       initIDs();
/*     */     }
/*     */ 
/* 148 */     AWTAccessor.setFileDialogAccessor(new AWTAccessor.FileDialogAccessor()
/*     */     {
/*     */       public void setFiles(FileDialog paramAnonymousFileDialog, File[] paramAnonymousArrayOfFile) {
/* 151 */         paramAnonymousFileDialog.setFiles(paramAnonymousArrayOfFile);
/*     */       }
/*     */       public void setFile(FileDialog paramAnonymousFileDialog, String paramAnonymousString) {
/* 154 */         paramAnonymousFileDialog.file = ("".equals(paramAnonymousString) ? null : paramAnonymousString);
/*     */       }
/*     */       public void setDirectory(FileDialog paramAnonymousFileDialog, String paramAnonymousString) {
/* 157 */         paramAnonymousFileDialog.dir = ("".equals(paramAnonymousString) ? null : paramAnonymousString);
/*     */       }
/*     */       public boolean isMultipleMode(FileDialog paramAnonymousFileDialog) {
/* 160 */         synchronized (paramAnonymousFileDialog.getObjectLock()) {
/* 161 */           return paramAnonymousFileDialog.multipleMode;
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.FileDialog
 * JD-Core Version:    0.6.2
 */