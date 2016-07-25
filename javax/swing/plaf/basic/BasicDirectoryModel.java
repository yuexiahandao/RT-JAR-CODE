/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.swing.AbstractListModel;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ListDataEvent;
/*     */ import javax.swing.filechooser.FileSystemView;
/*     */ import sun.awt.shell.ShellFolder;
/*     */ 
/*     */ public class BasicDirectoryModel extends AbstractListModel<Object>
/*     */   implements PropertyChangeListener
/*     */ {
/*  45 */   private JFileChooser filechooser = null;
/*     */ 
/*  47 */   private Vector<File> fileCache = new Vector(50);
/*  48 */   private LoadFilesThread loadThread = null;
/*  49 */   private Vector<File> files = null;
/*  50 */   private Vector<File> directories = null;
/*  51 */   private int fetchID = 0;
/*     */   private PropertyChangeSupport changeSupport;
/*  55 */   private boolean busy = false;
/*     */ 
/*     */   public BasicDirectoryModel(JFileChooser paramJFileChooser) {
/*  58 */     this.filechooser = paramJFileChooser;
/*  59 */     validateFileCache();
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/*  63 */     String str = paramPropertyChangeEvent.getPropertyName();
/*  64 */     if ((str == "directoryChanged") || (str == "fileViewChanged") || (str == "fileFilterChanged") || (str == "FileHidingChanged") || (str == "fileSelectionChanged"))
/*     */     {
/*  69 */       validateFileCache();
/*  70 */     } else if ("UI".equals(str)) {
/*  71 */       Object localObject = paramPropertyChangeEvent.getOldValue();
/*  72 */       if ((localObject instanceof BasicFileChooserUI)) {
/*  73 */         BasicFileChooserUI localBasicFileChooserUI = (BasicFileChooserUI)localObject;
/*  74 */         BasicDirectoryModel localBasicDirectoryModel = localBasicFileChooserUI.getModel();
/*  75 */         if (localBasicDirectoryModel != null)
/*  76 */           localBasicDirectoryModel.invalidateFileCache();
/*     */       }
/*     */     }
/*  79 */     else if ("JFileChooserDialogIsClosingProperty".equals(str)) {
/*  80 */       invalidateFileCache();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invalidateFileCache()
/*     */   {
/*  88 */     if (this.loadThread != null) {
/*  89 */       this.loadThread.interrupt();
/*  90 */       this.loadThread.cancelRunnables();
/*  91 */       this.loadThread = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Vector<File> getDirectories() {
/*  96 */     synchronized (this.fileCache) {
/*  97 */       if (this.directories != null) {
/*  98 */         return this.directories;
/*     */       }
/* 100 */       Vector localVector = getFiles();
/* 101 */       return this.directories;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Vector<File> getFiles() {
/* 106 */     synchronized (this.fileCache) {
/* 107 */       if (this.files != null) {
/* 108 */         return this.files;
/*     */       }
/* 110 */       this.files = new Vector();
/* 111 */       this.directories = new Vector();
/* 112 */       this.directories.addElement(this.filechooser.getFileSystemView().createFileObject(this.filechooser.getCurrentDirectory(), ".."));
/*     */ 
/* 116 */       for (int i = 0; i < getSize(); i++) {
/* 117 */         File localFile = (File)this.fileCache.get(i);
/* 118 */         if (this.filechooser.isTraversable(localFile))
/* 119 */           this.directories.add(localFile);
/*     */         else {
/* 121 */           this.files.add(localFile);
/*     */         }
/*     */       }
/* 124 */       return this.files;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void validateFileCache() {
/* 129 */     File localFile = this.filechooser.getCurrentDirectory();
/* 130 */     if (localFile == null) {
/* 131 */       return;
/*     */     }
/* 133 */     if (this.loadThread != null) {
/* 134 */       this.loadThread.interrupt();
/* 135 */       this.loadThread.cancelRunnables();
/*     */     }
/*     */ 
/* 138 */     setBusy(true, ++this.fetchID);
/*     */ 
/* 140 */     this.loadThread = new LoadFilesThread(localFile, this.fetchID);
/* 141 */     this.loadThread.start();
/*     */   }
/*     */ 
/*     */   public boolean renameFile(File paramFile1, File paramFile2)
/*     */   {
/* 156 */     synchronized (this.fileCache) {
/* 157 */       if (paramFile1.renameTo(paramFile2)) {
/* 158 */         validateFileCache();
/* 159 */         return true;
/*     */       }
/* 161 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fireContentsChanged()
/*     */   {
/* 168 */     fireContentsChanged(this, 0, getSize() - 1);
/*     */   }
/*     */ 
/*     */   public int getSize() {
/* 172 */     return this.fileCache.size();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject) {
/* 176 */     return this.fileCache.contains(paramObject);
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject) {
/* 180 */     return this.fileCache.indexOf(paramObject);
/*     */   }
/*     */ 
/*     */   public Object getElementAt(int paramInt) {
/* 184 */     return this.fileCache.get(paramInt);
/*     */   }
/*     */ 
/*     */   public void intervalAdded(ListDataEvent paramListDataEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void intervalRemoved(ListDataEvent paramListDataEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void sort(Vector<? extends File> paramVector)
/*     */   {
/* 200 */     ShellFolder.sort(paramVector);
/*     */   }
/*     */ 
/*     */   protected boolean lt(File paramFile1, File paramFile2)
/*     */   {
/* 206 */     int i = paramFile1.getName().toLowerCase().compareTo(paramFile2.getName().toLowerCase());
/* 207 */     if (i != 0) {
/* 208 */       return i < 0;
/*     */     }
/*     */ 
/* 211 */     return paramFile1.getName().compareTo(paramFile2.getName()) < 0;
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 366 */     if (this.changeSupport == null) {
/* 367 */       this.changeSupport = new PropertyChangeSupport(this);
/*     */     }
/* 369 */     this.changeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 385 */     if (this.changeSupport != null)
/* 386 */       this.changeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public PropertyChangeListener[] getPropertyChangeListeners()
/*     */   {
/* 405 */     if (this.changeSupport == null) {
/* 406 */       return new PropertyChangeListener[0];
/*     */     }
/* 408 */     return this.changeSupport.getPropertyChangeListeners();
/*     */   }
/*     */ 
/*     */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 425 */     if (this.changeSupport != null)
/* 426 */       this.changeSupport.firePropertyChange(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   private synchronized void setBusy(final boolean paramBoolean, int paramInt)
/*     */   {
/* 438 */     if (paramInt == this.fetchID) {
/* 439 */       boolean bool = this.busy;
/* 440 */       this.busy = paramBoolean;
/*     */ 
/* 442 */       if ((this.changeSupport != null) && (paramBoolean != bool))
/* 443 */         SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 445 */             BasicDirectoryModel.this.firePropertyChange("busy", Boolean.valueOf(!paramBoolean), Boolean.valueOf(paramBoolean));
/*     */           }
/*     */         });
/*     */     }
/*     */   }
/*     */ 
/*     */   class DoChangeContents
/*     */     implements Runnable
/*     */   {
/*     */     private List<File> addFiles;
/*     */     private List<File> remFiles;
/* 456 */     private boolean doFire = true;
/*     */     private int fid;
/* 458 */     private int addStart = 0;
/* 459 */     private int remStart = 0;
/*     */ 
/*     */     public DoChangeContents(int paramList, List<File> paramInt1, int paramInt2, int arg5) {
/* 462 */       this.addFiles = paramList;
/* 463 */       this.addStart = paramInt1;
/* 464 */       this.remFiles = paramInt2;
/*     */       int i;
/* 465 */       this.remStart = i;
/*     */       int j;
/* 466 */       this.fid = j;
/*     */     }
/*     */ 
/*     */     synchronized void cancel() {
/* 470 */       this.doFire = false;
/*     */     }
/*     */ 
/*     */     public synchronized void run() {
/* 474 */       if ((BasicDirectoryModel.this.fetchID == this.fid) && (this.doFire)) {
/* 475 */         int i = this.remFiles == null ? 0 : this.remFiles.size();
/* 476 */         int j = this.addFiles == null ? 0 : this.addFiles.size();
/* 477 */         synchronized (BasicDirectoryModel.this.fileCache) {
/* 478 */           if (i > 0) {
/* 479 */             BasicDirectoryModel.this.fileCache.removeAll(this.remFiles);
/*     */           }
/* 481 */           if (j > 0) {
/* 482 */             BasicDirectoryModel.this.fileCache.addAll(this.addStart, this.addFiles);
/*     */           }
/* 484 */           BasicDirectoryModel.this.files = null;
/* 485 */           BasicDirectoryModel.this.directories = null;
/*     */         }
/* 487 */         if ((i > 0) && (j == 0))
/* 488 */           BasicDirectoryModel.this.fireIntervalRemoved(BasicDirectoryModel.this, this.remStart, this.remStart + i - 1);
/* 489 */         else if ((j > 0) && (i == 0) && (this.addStart + j <= BasicDirectoryModel.this.fileCache.size()))
/* 490 */           BasicDirectoryModel.this.fireIntervalAdded(BasicDirectoryModel.this, this.addStart, this.addStart + j - 1);
/*     */         else
/* 492 */           BasicDirectoryModel.this.fireContentsChanged();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class LoadFilesThread extends Thread
/*     */   {
/* 217 */     File currentDirectory = null;
/*     */     int fid;
/* 219 */     Vector<BasicDirectoryModel.DoChangeContents> runnables = new Vector(10);
/*     */ 
/*     */     public LoadFilesThread(File paramInt, int arg3) {
/* 222 */       super();
/* 223 */       this.currentDirectory = paramInt;
/*     */       int i;
/* 224 */       this.fid = i;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 228 */       run0();
/* 229 */       BasicDirectoryModel.this.setBusy(false, this.fid);
/*     */     }
/*     */ 
/*     */     public void run0() {
/* 233 */       FileSystemView localFileSystemView = BasicDirectoryModel.this.filechooser.getFileSystemView();
/*     */ 
/* 235 */       if (isInterrupted()) {
/* 236 */         return;
/*     */       }
/*     */ 
/* 239 */       File[] arrayOfFile = localFileSystemView.getFiles(this.currentDirectory, BasicDirectoryModel.this.filechooser.isFileHidingEnabled());
/*     */ 
/* 241 */       if (isInterrupted()) {
/* 242 */         return;
/*     */       }
/*     */ 
/* 245 */       final Vector localVector1 = new Vector();
/* 246 */       Vector localVector2 = new Vector();
/*     */ 
/* 251 */       for (File localFile : arrayOfFile) {
/* 252 */         if (BasicDirectoryModel.this.filechooser.accept(localFile)) {
/* 253 */           boolean bool = BasicDirectoryModel.this.filechooser.isTraversable(localFile);
/*     */ 
/* 255 */           if (bool)
/* 256 */             localVector1.addElement(localFile);
/* 257 */           else if (BasicDirectoryModel.this.filechooser.isFileSelectionEnabled()) {
/* 258 */             localVector2.addElement(localFile);
/*     */           }
/*     */ 
/* 261 */           if (isInterrupted()) {
/* 262 */             return;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 268 */       BasicDirectoryModel.this.sort(localVector1);
/* 269 */       BasicDirectoryModel.this.sort(localVector2);
/*     */ 
/* 271 */       localVector1.addAll(localVector2);
/*     */ 
/* 275 */       ??? = (BasicDirectoryModel.DoChangeContents)ShellFolder.invoke(new Callable() {
/*     */         public BasicDirectoryModel.DoChangeContents call() {
/* 277 */           int i = localVector1.size();
/* 278 */           int j = BasicDirectoryModel.this.fileCache.size();
/*     */           int k;
/*     */           int m;
/*     */           int n;
/* 280 */           if (i > j)
/*     */           {
/* 282 */             k = j;
/* 283 */             m = i;
/* 284 */             for (n = 0; n < j; n++) {
/* 285 */               if (!((File)localVector1.get(n)).equals(BasicDirectoryModel.this.fileCache.get(n))) {
/* 286 */                 k = n;
/* 287 */                 for (int i1 = n; i1 < i; i1++) {
/* 288 */                   if (((File)localVector1.get(i1)).equals(BasicDirectoryModel.this.fileCache.get(n))) {
/* 289 */                     m = i1;
/* 290 */                     break;
/*     */                   }
/*     */                 }
/* 293 */                 break;
/*     */               }
/*     */             }
/* 296 */             if ((k >= 0) && (m > k) && (localVector1.subList(m, i).equals(BasicDirectoryModel.this.fileCache.subList(k, j))))
/*     */             {
/* 298 */               if (BasicDirectoryModel.LoadFilesThread.this.isInterrupted()) {
/* 299 */                 return null;
/*     */               }
/* 301 */               return new BasicDirectoryModel.DoChangeContents(BasicDirectoryModel.this, localVector1.subList(k, m), k, null, 0, BasicDirectoryModel.LoadFilesThread.this.fid);
/*     */             }
/* 303 */           } else if (i < j)
/*     */           {
/* 305 */             k = -1;
/* 306 */             m = -1;
/* 307 */             for (n = 0; n < i; n++) {
/* 308 */               if (!((File)localVector1.get(n)).equals(BasicDirectoryModel.this.fileCache.get(n))) {
/* 309 */                 k = n;
/* 310 */                 m = n + j - i;
/* 311 */                 break;
/*     */               }
/*     */             }
/* 314 */             if ((k >= 0) && (m > k) && (BasicDirectoryModel.this.fileCache.subList(m, j).equals(localVector1.subList(k, i))))
/*     */             {
/* 316 */               if (BasicDirectoryModel.LoadFilesThread.this.isInterrupted()) {
/* 317 */                 return null;
/*     */               }
/* 319 */               return new BasicDirectoryModel.DoChangeContents(BasicDirectoryModel.this, null, 0, new Vector(BasicDirectoryModel.this.fileCache.subList(k, m)), k, BasicDirectoryModel.LoadFilesThread.this.fid);
/*     */             }
/*     */           }
/* 322 */           if (!BasicDirectoryModel.this.fileCache.equals(localVector1)) {
/* 323 */             if (BasicDirectoryModel.LoadFilesThread.this.isInterrupted()) {
/* 324 */               BasicDirectoryModel.LoadFilesThread.this.cancelRunnables(BasicDirectoryModel.LoadFilesThread.this.runnables);
/*     */             }
/* 326 */             return new BasicDirectoryModel.DoChangeContents(BasicDirectoryModel.this, localVector1, 0, BasicDirectoryModel.this.fileCache, 0, BasicDirectoryModel.LoadFilesThread.this.fid);
/*     */           }
/* 328 */           return null;
/*     */         }
/*     */       });
/* 332 */       if (??? != null) {
/* 333 */         this.runnables.addElement(???);
/* 334 */         SwingUtilities.invokeLater((Runnable)???);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void cancelRunnables(Vector<BasicDirectoryModel.DoChangeContents> paramVector)
/*     */     {
/* 340 */       for (BasicDirectoryModel.DoChangeContents localDoChangeContents : paramVector)
/* 341 */         localDoChangeContents.cancel();
/*     */     }
/*     */ 
/*     */     public void cancelRunnables()
/*     */     {
/* 346 */       cancelRunnables(this.runnables);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicDirectoryModel
 * JD-Core Version:    0.6.2
 */