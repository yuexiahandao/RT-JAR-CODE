/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.MenuBar;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.peer.FramePeer;
/*     */ import java.security.AccessController;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.FrameAccessor;
/*     */ import sun.awt.im.InputMethodManager;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class WFramePeer extends WWindowPeer
/*     */   implements FramePeer
/*     */ {
/*  73 */   private static final boolean keepOnMinimize = "true".equals((String)AccessController.doPrivileged(new GetPropertyAction("sun.awt.keepWorkingSetOnMinimize")));
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public native void setState(int paramInt);
/*     */ 
/*     */   public native int getState();
/*     */ 
/*     */   public void setExtendedState(int paramInt)
/*     */   {
/*  62 */     AWTAccessor.getFrameAccessor().setExtendedState((Frame)this.target, paramInt);
/*     */   }
/*     */   public int getExtendedState() {
/*  65 */     return AWTAccessor.getFrameAccessor().getExtendedState((Frame)this.target);
/*     */   }
/*     */ 
/*     */   private native void setMaximizedBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   private native void clearMaximizedBounds();
/*     */ 
/*     */   public void setMaximizedBounds(Rectangle paramRectangle)
/*     */   {
/*  79 */     if (paramRectangle == null) {
/*  80 */       clearMaximizedBounds();
/*     */     } else {
/*  82 */       Rectangle localRectangle = (Rectangle)paramRectangle.clone();
/*  83 */       adjustMaximizedBounds(localRectangle);
/*  84 */       setMaximizedBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void adjustMaximizedBounds(Rectangle paramRectangle)
/*     */   {
/* 100 */     GraphicsConfiguration localGraphicsConfiguration1 = getGraphicsConfiguration();
/*     */ 
/* 102 */     GraphicsDevice localGraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
/*     */ 
/* 104 */     GraphicsConfiguration localGraphicsConfiguration2 = localGraphicsDevice.getDefaultConfiguration();
/*     */ 
/* 106 */     if ((localGraphicsConfiguration1 != null) && (localGraphicsConfiguration1 != localGraphicsConfiguration2)) {
/* 107 */       Rectangle localRectangle1 = localGraphicsConfiguration1.getBounds();
/* 108 */       Rectangle localRectangle2 = localGraphicsConfiguration2.getBounds();
/*     */ 
/* 110 */       int i = (localRectangle1.width - localRectangle2.width > 0) || (localRectangle1.height - localRectangle2.height > 0) ? 1 : 0;
/*     */ 
/* 116 */       if (i != 0) {
/* 117 */         paramRectangle.width -= localRectangle1.width - localRectangle2.width;
/* 118 */         paramRectangle.height -= localRectangle1.height - localRectangle2.height;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean updateGraphicsData(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 125 */     boolean bool = super.updateGraphicsData(paramGraphicsConfiguration);
/* 126 */     Rectangle localRectangle = AWTAccessor.getFrameAccessor().getMaximizedBounds((Frame)this.target);
/*     */ 
/* 128 */     if (localRectangle != null) {
/* 129 */       setMaximizedBounds(localRectangle);
/*     */     }
/* 131 */     return bool;
/*     */   }
/*     */ 
/*     */   boolean isTargetUndecorated()
/*     */   {
/* 136 */     return ((Frame)this.target).isUndecorated();
/*     */   }
/*     */ 
/*     */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 140 */     if (((Frame)this.target).isUndecorated())
/* 141 */       super.reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     else
/* 143 */       reshapeFrame(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 148 */     Dimension localDimension = new Dimension();
/* 149 */     if (!((Frame)this.target).isUndecorated()) {
/* 150 */       localDimension.setSize(getSysMinWidth(), getSysMinHeight());
/*     */     }
/* 152 */     if (((Frame)this.target).getMenuBar() != null) {
/* 153 */       localDimension.height += getSysMenuHeight();
/*     */     }
/* 155 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void setMenuBar(MenuBar paramMenuBar)
/*     */   {
/* 162 */     WMenuBarPeer localWMenuBarPeer = (WMenuBarPeer)WToolkit.targetToPeer(paramMenuBar);
/* 163 */     setMenuBar0(localWMenuBarPeer);
/* 164 */     updateInsets(this.insets_);
/*     */   }
/*     */ 
/*     */   private native void setMenuBar0(WMenuBarPeer paramWMenuBarPeer);
/*     */ 
/*     */   WFramePeer(Frame paramFrame)
/*     */   {
/* 175 */     super(paramFrame);
/*     */ 
/* 177 */     InputMethodManager localInputMethodManager = InputMethodManager.getInstance();
/* 178 */     String str = localInputMethodManager.getTriggerMenuString();
/* 179 */     if (str != null)
/*     */     {
/* 181 */       pSetIMMOption(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void createAwtFrame(WComponentPeer paramWComponentPeer);
/*     */ 
/* 187 */   void create(WComponentPeer paramWComponentPeer) { preCreate(paramWComponentPeer);
/* 188 */     createAwtFrame(paramWComponentPeer); }
/*     */ 
/*     */   void initialize()
/*     */   {
/* 192 */     super.initialize();
/*     */ 
/* 194 */     Frame localFrame = (Frame)this.target;
/*     */ 
/* 196 */     if (localFrame.getTitle() != null) {
/* 197 */       setTitle(localFrame.getTitle());
/*     */     }
/* 199 */     setResizable(localFrame.isResizable());
/* 200 */     setState(localFrame.getExtendedState());
/*     */   }
/*     */   private static native int getSysMenuHeight();
/*     */ 
/*     */   native void pSetIMMOption(String paramString);
/*     */ 
/*     */   void notifyIMMOptionChange() {
/* 207 */     InputMethodManager.getInstance().notifyChangeRequest((Component)this.target);
/*     */   }
/*     */ 
/*     */   public void setBoundsPrivate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 211 */     setBounds(paramInt1, paramInt2, paramInt3, paramInt4, 3);
/*     */   }
/*     */   public Rectangle getBoundsPrivate() {
/* 214 */     return getBounds();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  50 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WFramePeer
 * JD-Core Version:    0.6.2
 */