/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dialog.ModalityType;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.SystemColor;
/*     */ import java.awt.Window;
/*     */ import java.awt.peer.DialogPeer;
/*     */ import java.util.List;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ComponentAccessor;
/*     */ import sun.awt.im.InputMethodManager;
/*     */ 
/*     */ class WDialogPeer extends WWindowPeer
/*     */   implements DialogPeer
/*     */ {
/*  39 */   static final Color defaultBackground = SystemColor.control;
/*     */   boolean needDefaultBackground;
/*     */ 
/*     */   WDialogPeer(Dialog paramDialog)
/*     */   {
/*  46 */     super(paramDialog);
/*     */ 
/*  48 */     InputMethodManager localInputMethodManager = InputMethodManager.getInstance();
/*  49 */     String str = localInputMethodManager.getTriggerMenuString();
/*  50 */     if (str != null)
/*     */     {
/*  52 */       pSetIMMOption(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void createAwtDialog(WComponentPeer paramWComponentPeer);
/*     */ 
/*  58 */   void create(WComponentPeer paramWComponentPeer) { preCreate(paramWComponentPeer);
/*  59 */     createAwtDialog(paramWComponentPeer); }
/*     */ 
/*     */   native void showModal();
/*     */ 
/*     */   native void endModal();
/*     */ 
/*     */   void initialize() {
/*  66 */     Dialog localDialog = (Dialog)this.target;
/*     */ 
/*  69 */     if (this.needDefaultBackground) {
/*  70 */       localDialog.setBackground(defaultBackground);
/*     */     }
/*     */ 
/*  73 */     super.initialize();
/*     */ 
/*  75 */     if (localDialog.getTitle() != null) {
/*  76 */       setTitle(localDialog.getTitle());
/*     */     }
/*  78 */     setResizable(localDialog.isResizable());
/*     */   }
/*     */ 
/*     */   protected void realShow() {
/*  82 */     Dialog localDialog = (Dialog)this.target;
/*  83 */     if (localDialog.getModalityType() != Dialog.ModalityType.MODELESS)
/*  84 */       showModal();
/*     */     else
/*  86 */       super.realShow();
/*     */   }
/*     */ 
/*     */   public void hide()
/*     */   {
/*  91 */     Dialog localDialog = (Dialog)this.target;
/*  92 */     if (localDialog.getModalityType() != Dialog.ModalityType.MODELESS)
/*  93 */       endModal();
/*     */     else
/*  95 */       super.hide();
/*     */   }
/*     */ 
/*     */   public void blockWindows(List<Window> paramList)
/*     */   {
/* 100 */     for (Window localWindow : paramList) {
/* 101 */       WWindowPeer localWWindowPeer = (WWindowPeer)AWTAccessor.getComponentAccessor().getPeer(localWindow);
/* 102 */       if (localWWindowPeer != null)
/* 103 */         localWWindowPeer.setModalBlocked((Dialog)this.target, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 109 */     if (((Dialog)this.target).isUndecorated()) {
/* 110 */       return super.getMinimumSize();
/*     */     }
/* 112 */     return new Dimension(getSysMinWidth(), getSysMinHeight());
/*     */   }
/*     */ 
/*     */   boolean isTargetUndecorated()
/*     */   {
/* 118 */     return ((Dialog)this.target).isUndecorated();
/*     */   }
/*     */ 
/*     */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 122 */     if (((Dialog)this.target).isUndecorated())
/* 123 */       super.reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     else
/* 125 */       reshapeFrame(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   private void setDefaultColor()
/*     */   {
/* 139 */     this.needDefaultBackground = true;
/*     */   }
/*     */   native void pSetIMMOption(String paramString);
/*     */ 
/*     */   void notifyIMMOptionChange() {
/* 144 */     InputMethodManager.getInstance().notifyChangeRequest((Component)this.target);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WDialogPeer
 * JD-Core Version:    0.6.2
 */