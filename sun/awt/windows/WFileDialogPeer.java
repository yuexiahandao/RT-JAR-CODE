/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Event;
/*     */ import java.awt.FileDialog;
/*     */ import java.awt.Font;
/*     */ import java.awt.Window;
/*     */ import java.awt.dnd.DropTarget;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.FileDialogPeer;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.List;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ComponentAccessor;
/*     */ import sun.awt.AWTAccessor.FileDialogAccessor;
/*     */ import sun.awt.CausedFocusEvent.Cause;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class WFileDialogPeer extends WWindowPeer
/*     */   implements FileDialogPeer
/*     */ {
/*     */   private WComponentPeer parent;
/*     */   private FilenameFilter fileFilter;
/*  50 */   private Vector<WWindowPeer> blockedWindows = new Vector();
/*     */ 
/*     */   private static native void setFilterString(String paramString);
/*     */ 
/*     */   public void setFilenameFilter(FilenameFilter paramFilenameFilter)
/*     */   {
/*  56 */     this.fileFilter = paramFilenameFilter;
/*     */   }
/*     */ 
/*     */   boolean checkFilenameFilter(String paramString) {
/*  60 */     FileDialog localFileDialog = (FileDialog)this.target;
/*  61 */     if (this.fileFilter == null) {
/*  62 */       return true;
/*     */     }
/*  64 */     File localFile = new File(paramString);
/*  65 */     return this.fileFilter.accept(new File(localFile.getParent()), localFile.getName());
/*     */   }
/*     */ 
/*     */   WFileDialogPeer(FileDialog paramFileDialog)
/*     */   {
/*  70 */     super(paramFileDialog);
/*     */   }
/*     */ 
/*     */   void create(WComponentPeer paramWComponentPeer) {
/*  74 */     this.parent = paramWComponentPeer;
/*     */   }
/*     */ 
/*     */   protected void checkCreation()
/*     */   {
/*     */   }
/*     */ 
/*     */   void initialize() {
/*  82 */     setFilenameFilter(((FileDialog)this.target).getFilenameFilter());
/*     */   }
/*     */   private native void _dispose();
/*     */ 
/*     */   protected void disposeImpl() {
/*  87 */     WToolkit.targetDisposedPeer(this.target, this);
/*  88 */     _dispose();
/*     */   }
/*     */   private native void _show();
/*     */ 
/*     */   private native void _hide();
/*     */ 
/*     */   public void show() {
/*  95 */     new Thread(new Runnable() {
/*     */       public void run() {
/*  97 */         WFileDialogPeer.this._show();
/*     */       }
/*     */     }).start();
/*     */   }
/*     */ 
/*     */   public void hide()
/*     */   {
/* 103 */     _hide();
/*     */   }
/*     */ 
/*     */   void setHWnd(long paramLong)
/*     */   {
/* 108 */     if (this.hwnd == paramLong) {
/* 109 */       return;
/*     */     }
/* 111 */     this.hwnd = paramLong;
/* 112 */     for (WWindowPeer localWWindowPeer : this.blockedWindows)
/* 113 */       if (paramLong != 0L)
/* 114 */         localWWindowPeer.modalDisable((Dialog)this.target, paramLong);
/*     */       else
/* 116 */         localWWindowPeer.modalEnable((Dialog)this.target);
/*     */   }
/*     */ 
/*     */   void handleSelected(char[] paramArrayOfChar)
/*     */   {
/* 137 */     String[] arrayOfString = new String(paramArrayOfChar).split("");
/* 138 */     int i = arrayOfString.length > 1 ? 1 : 0;
/*     */ 
/* 140 */     String str1 = null;
/* 141 */     String str2 = null;
/* 142 */     File[] arrayOfFile = null;
/*     */     int j;
/* 144 */     if (i != 0) {
/* 145 */       str1 = arrayOfString[0];
/* 146 */       j = arrayOfString.length - 1;
/* 147 */       arrayOfFile = new File[j];
/* 148 */       for (int k = 0; k < j; k++) {
/* 149 */         arrayOfFile[k] = new File(str1, arrayOfString[(k + 1)]);
/*     */       }
/* 151 */       str2 = arrayOfString[1];
/*     */     } else {
/* 153 */       j = arrayOfString[0].lastIndexOf(File.separatorChar);
/* 154 */       if (j == -1) {
/* 155 */         str1 = "." + File.separator;
/* 156 */         str2 = arrayOfString[0];
/*     */       } else {
/* 158 */         str1 = arrayOfString[0].substring(0, j + 1);
/* 159 */         str2 = arrayOfString[0].substring(j + 1);
/*     */       }
/* 161 */       arrayOfFile = new File[] { new File(str1, str2) };
/*     */     }
/*     */ 
/* 164 */     final FileDialog localFileDialog = (FileDialog)this.target;
/* 165 */     AWTAccessor.FileDialogAccessor localFileDialogAccessor = AWTAccessor.getFileDialogAccessor();
/*     */ 
/* 167 */     localFileDialogAccessor.setDirectory(localFileDialog, str1);
/* 168 */     localFileDialogAccessor.setFile(localFileDialog, str2);
/* 169 */     localFileDialogAccessor.setFiles(localFileDialog, arrayOfFile);
/*     */ 
/* 171 */     WToolkit.executeOnEventHandlerThread(localFileDialog, new Runnable() {
/*     */       public void run() {
/* 173 */         localFileDialog.hide();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   void handleCancel()
/*     */   {
/* 181 */     final FileDialog localFileDialog = (FileDialog)this.target;
/*     */ 
/* 183 */     AWTAccessor.getFileDialogAccessor().setFile(localFileDialog, null);
/* 184 */     AWTAccessor.getFileDialogAccessor().setFiles(localFileDialog, null);
/*     */ 
/* 186 */     WToolkit.executeOnEventHandlerThread(localFileDialog, new Runnable() {
/*     */       public void run() {
/* 188 */         localFileDialog.hide();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   void blockWindow(WWindowPeer paramWWindowPeer)
/*     */   {
/* 210 */     this.blockedWindows.add(paramWWindowPeer);
/*     */ 
/* 213 */     if (this.hwnd != 0L)
/* 214 */       paramWWindowPeer.modalDisable((Dialog)this.target, this.hwnd);
/*     */   }
/*     */ 
/*     */   void unblockWindow(WWindowPeer paramWWindowPeer) {
/* 218 */     this.blockedWindows.remove(paramWWindowPeer);
/*     */ 
/* 221 */     if (this.hwnd != 0L)
/* 222 */       paramWWindowPeer.modalEnable((Dialog)this.target);
/*     */   }
/*     */ 
/*     */   public void blockWindows(List<Window> paramList)
/*     */   {
/* 227 */     for (Window localWindow : paramList) {
/* 228 */       WWindowPeer localWWindowPeer = (WWindowPeer)AWTAccessor.getComponentAccessor().getPeer(localWindow);
/* 229 */       if (localWWindowPeer != null)
/* 230 */         blockWindow(localWWindowPeer); 
/*     */     }
/*     */   }
/*     */   public native void toFront();
/*     */ 
/*     */   public native void toBack();
/*     */ 
/*     */   public void updateAlwaysOnTopState() {
/*     */   }
/*     */   public void setDirectory(String paramString) {
/*     */   }
/*     */   public void setFile(String paramString) {
/*     */   }
/*     */   public void setTitle(String paramString) {
/*     */   }
/*     */   public void setResizable(boolean paramBoolean) {  }
/*     */ 
/*     */   public void enable() {  }
/*     */ 
/*     */   public void disable() {  } 
/*     */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {  } 
/* 249 */   public boolean handleEvent(Event paramEvent) { return false; } 
/*     */   public void setForeground(Color paramColor) {
/*     */   }
/*     */   public void setBackground(Color paramColor) {
/*     */   }
/*     */   public void setFont(Font paramFont) {  } 
/*     */   public void updateMinimumSize() {  } 
/*     */   public void updateIconImages() {  } 
/* 257 */   public boolean requestFocus(boolean paramBoolean1, boolean paramBoolean2) { return false; }
/*     */ 
/*     */ 
/*     */   public boolean requestFocus(Component paramComponent, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause)
/*     */   {
/* 264 */     return false;
/*     */   }
/*     */   void start() {
/*     */   }
/*     */   public void beginValidate() {
/*     */   }
/*     */   public void endValidate() {
/*     */   }
/*     */   void invalidate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*     */   }
/*     */   public void addDropTarget(DropTarget paramDropTarget) {
/*     */   }
/*     */   public void removeDropTarget(DropTarget paramDropTarget) {
/*     */   }
/*     */   public void updateFocusableWindowState() {
/*     */   }
/*     */   public void setZOrder(ComponentPeer paramComponentPeer) {
/*     */   }
/*     */   private static native void initIDs();
/*     */ 
/*     */   public void applyShape(Region paramRegion) {
/*     */   }
/*     */   public void setOpacity(float paramFloat) {
/*     */   }
/*     */   public void setOpaque(boolean paramBoolean) {
/*     */   }
/*     */   public void updateWindow(BufferedImage paramBufferedImage) {
/*     */   }
/*     */   public void createScreenSurface(boolean paramBoolean) {
/*     */   }
/*     */   public void replaceSurfaceData() {  } 
/* 295 */   public boolean isMultipleMode() { FileDialog localFileDialog = (FileDialog)this.target;
/* 296 */     return AWTAccessor.getFileDialogAccessor().isMultipleMode(localFileDialog);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  44 */     initIDs();
/*     */ 
/* 195 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 199 */           ResourceBundle localResourceBundle = ResourceBundle.getBundle("sun.awt.windows.awtLocalization");
/* 200 */           return localResourceBundle.getString("allFiles"); } catch (MissingResourceException localMissingResourceException) {
/*     */         }
/* 202 */         return "All Files";
/*     */       }
/*     */     });
/* 206 */     setFilterString(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WFileDialogPeer
 * JD-Core Version:    0.6.2
 */