/*     */ package sun.awt;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.awt.AWTKeyStroke;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Image;
/*     */ import java.awt.KeyEventDispatcher;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.MenuBar;
/*     */ import java.awt.MenuComponent;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.FramePeer;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class EmbeddedFrame extends Frame
/*     */   implements KeyEventDispatcher, PropertyChangeListener
/*     */ {
/*  59 */   private boolean isCursorAllowed = true;
/*  60 */   private boolean supportsXEmbed = false;
/*     */   private KeyboardFocusManager appletKFM;
/*     */   private static final long serialVersionUID = 2967042741780317130L;
/*     */   protected static final boolean FORWARD = true;
/*     */   protected static final boolean BACKWARD = false;
/*     */ 
/*     */   public boolean supportsXEmbed()
/*     */   {
/*  73 */     return (this.supportsXEmbed) && (SunToolkit.needsXEmbed());
/*     */   }
/*     */ 
/*     */   protected EmbeddedFrame(boolean paramBoolean) {
/*  77 */     this(0L, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected EmbeddedFrame()
/*     */   {
/*  82 */     this(0L);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected EmbeddedFrame(int paramInt)
/*     */   {
/*  90 */     this(paramInt);
/*     */   }
/*     */ 
/*     */   protected EmbeddedFrame(long paramLong) {
/*  94 */     this(paramLong, false);
/*     */   }
/*     */ 
/*     */   protected EmbeddedFrame(long paramLong, boolean paramBoolean) {
/*  98 */     this.supportsXEmbed = paramBoolean;
/*  99 */     registerListeners();
/*     */   }
/*     */ 
/*     */   public Container getParent()
/*     */   {
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 115 */     if (!paramPropertyChangeEvent.getPropertyName().equals("managingFocus")) {
/* 116 */       return;
/*     */     }
/*     */ 
/* 121 */     if (paramPropertyChangeEvent.getNewValue() == Boolean.TRUE) {
/* 122 */       return;
/*     */     }
/*     */ 
/* 126 */     removeTraversingOutListeners((KeyboardFocusManager)paramPropertyChangeEvent.getSource());
/*     */ 
/* 128 */     this.appletKFM = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/* 129 */     if (isVisible())
/* 130 */       addTraversingOutListeners(this.appletKFM);
/*     */   }
/*     */ 
/*     */   private void addTraversingOutListeners(KeyboardFocusManager paramKeyboardFocusManager)
/*     */   {
/* 138 */     paramKeyboardFocusManager.addKeyEventDispatcher(this);
/* 139 */     paramKeyboardFocusManager.addPropertyChangeListener("managingFocus", this);
/*     */   }
/*     */ 
/*     */   private void removeTraversingOutListeners(KeyboardFocusManager paramKeyboardFocusManager)
/*     */   {
/* 146 */     paramKeyboardFocusManager.removeKeyEventDispatcher(this);
/* 147 */     paramKeyboardFocusManager.removePropertyChangeListener("managingFocus", this);
/*     */   }
/*     */ 
/*     */   public void registerListeners()
/*     */   {
/* 160 */     if (this.appletKFM != null) {
/* 161 */       removeTraversingOutListeners(this.appletKFM);
/*     */     }
/* 163 */     this.appletKFM = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/* 164 */     if (isVisible())
/* 165 */       addTraversingOutListeners(this.appletKFM);
/*     */   }
/*     */ 
/*     */   public void show()
/*     */   {
/* 176 */     if (this.appletKFM != null) {
/* 177 */       addTraversingOutListeners(this.appletKFM);
/*     */     }
/* 179 */     super.show();
/*     */   }
/*     */ 
/*     */   public void hide()
/*     */   {
/* 189 */     if (this.appletKFM != null) {
/* 190 */       removeTraversingOutListeners(this.appletKFM);
/*     */     }
/* 192 */     super.hide();
/*     */   }
/*     */ 
/*     */   public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
/*     */   {
/* 203 */     Container localContainer = AWTAccessor.getKeyboardFocusManagerAccessor().getCurrentFocusCycleRoot();
/*     */ 
/* 207 */     if (this != localContainer) {
/* 208 */       return false;
/*     */     }
/*     */ 
/* 212 */     if (paramKeyEvent.getID() == 400) {
/* 213 */       return false;
/*     */     }
/*     */ 
/* 216 */     if ((!getFocusTraversalKeysEnabled()) || (paramKeyEvent.isConsumed())) {
/* 217 */       return false;
/*     */     }
/*     */ 
/* 220 */     AWTKeyStroke localAWTKeyStroke = AWTKeyStroke.getAWTKeyStrokeForEvent(paramKeyEvent);
/*     */ 
/* 222 */     Component localComponent1 = paramKeyEvent.getComponent();
/*     */ 
/* 224 */     Set localSet = getFocusTraversalKeys(0);
/*     */     Component localComponent2;
/* 225 */     if (localSet.contains(localAWTKeyStroke))
/*     */     {
/* 227 */       localComponent2 = getFocusTraversalPolicy().getLastComponent(this);
/* 228 */       if (((localComponent1 == localComponent2) || (localComponent2 == null)) && 
/* 229 */         (traverseOut(true))) {
/* 230 */         paramKeyEvent.consume();
/* 231 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 236 */     localSet = getFocusTraversalKeys(1);
/* 237 */     if (localSet.contains(localAWTKeyStroke))
/*     */     {
/* 239 */       localComponent2 = getFocusTraversalPolicy().getFirstComponent(this);
/* 240 */       if (((localComponent1 == localComponent2) || (localComponent2 == null)) && 
/* 241 */         (traverseOut(false))) {
/* 242 */         paramKeyEvent.consume();
/* 243 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean traverseIn(boolean paramBoolean)
/*     */   {
/* 269 */     Component localComponent = null;
/*     */ 
/* 271 */     if (paramBoolean == true)
/* 272 */       localComponent = getFocusTraversalPolicy().getFirstComponent(this);
/*     */     else {
/* 274 */       localComponent = getFocusTraversalPolicy().getLastComponent(this);
/*     */     }
/* 276 */     if (localComponent != null)
/*     */     {
/* 279 */       AWTAccessor.getKeyboardFocusManagerAccessor().setMostRecentFocusOwner(this, localComponent);
/* 280 */       synthesizeWindowActivation(true);
/*     */     }
/* 282 */     return null != localComponent;
/*     */   }
/*     */ 
/*     */   protected boolean traverseOut(boolean paramBoolean)
/*     */   {
/* 303 */     return false;
/*     */   }
/*     */   public void setTitle(String paramString) {
/*     */   }
/*     */   public void setIconImage(Image paramImage) {
/*     */   }
/*     */   public void setIconImages(List<? extends Image> paramList) {
/*     */   }
/*     */   public void setMenuBar(MenuBar paramMenuBar) {
/*     */   }
/*     */   public void setResizable(boolean paramBoolean) {
/*     */   }
/*     */   public void remove(MenuComponent paramMenuComponent) {
/*     */   }
/*     */   public boolean isResizable() {
/* 318 */     return true;
/*     */   }
/*     */ 
/*     */   public void addNotify() {
/* 322 */     synchronized (getTreeLock()) {
/* 323 */       if (getPeer() == null) {
/* 324 */         setPeer(new NullEmbeddedFramePeer(null));
/*     */       }
/* 326 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCursorAllowed(boolean paramBoolean)
/*     */   {
/* 332 */     this.isCursorAllowed = paramBoolean;
/* 333 */     getPeer().updateCursorImmediately();
/*     */   }
/*     */   public boolean isCursorAllowed() {
/* 336 */     return this.isCursorAllowed;
/*     */   }
/*     */   public Cursor getCursor() {
/* 339 */     return this.isCursorAllowed ? super.getCursor() : Cursor.getPredefinedCursor(0);
/*     */   }
/*     */ 
/*     */   protected void setPeer(ComponentPeer paramComponentPeer)
/*     */   {
/* 345 */     AWTAccessor.getComponentAccessor().setPeer(this, paramComponentPeer);
/*     */   }
/*     */ 
/*     */   public void synthesizeWindowActivation(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void setLocationPrivate(int paramInt1, int paramInt2)
/*     */   {
/* 385 */     Dimension localDimension = getSize();
/* 386 */     setBoundsPrivate(paramInt1, paramInt2, localDimension.width, localDimension.height);
/*     */   }
/*     */ 
/*     */   protected Point getLocationPrivate()
/*     */   {
/* 414 */     Rectangle localRectangle = getBoundsPrivate();
/* 415 */     return new Point(localRectangle.x, localRectangle.y);
/*     */   }
/*     */ 
/*     */   protected void setBoundsPrivate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 448 */     FramePeer localFramePeer = (FramePeer)getPeer();
/* 449 */     if (localFramePeer != null)
/* 450 */       localFramePeer.setBoundsPrivate(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected Rectangle getBoundsPrivate()
/*     */   {
/* 479 */     FramePeer localFramePeer = (FramePeer)getPeer();
/* 480 */     if (localFramePeer != null) {
/* 481 */       return localFramePeer.getBoundsPrivate();
/*     */     }
/*     */ 
/* 484 */     return getBounds();
/*     */   }
/*     */ 
/*     */   public void toFront()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void toBack()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract void registerAccelerator(AWTKeyStroke paramAWTKeyStroke);
/*     */ 
/*     */   public abstract void unregisterAccelerator(AWTKeyStroke paramAWTKeyStroke);
/*     */ 
/*     */   public static Applet getAppletIfAncestorOf(Component paramComponent)
/*     */   {
/* 502 */     Container localContainer = paramComponent.getParent();
/* 503 */     Applet localApplet = null;
/* 504 */     while ((localContainer != null) && (!(localContainer instanceof EmbeddedFrame))) {
/* 505 */       if ((localContainer instanceof Applet)) {
/* 506 */         localApplet = (Applet)localContainer;
/*     */       }
/* 508 */       localContainer = localContainer.getParent();
/*     */     }
/* 510 */     return localContainer == null ? null : localApplet;
/*     */   }
/*     */   public void notifyModalBlocked(Dialog paramDialog, boolean paramBoolean) {
/*     */   }
/*     */   private static class NullEmbeddedFramePeer extends NullComponentPeer implements FramePeer {
/*     */     public void setTitle(String paramString) {
/*     */     }
/*     */     public void setIconImage(Image paramImage) {
/*     */     }
/*     */     public void updateIconImages() {
/*     */     }
/*     */     public void setMenuBar(MenuBar paramMenuBar) {
/*     */     }
/*     */     public void setResizable(boolean paramBoolean) {
/*     */     }
/*     */     public void setState(int paramInt) {
/*     */     }
/*     */ 
/*     */     public int getState() {
/* 529 */       return 0; } 
/*     */     public void setMaximizedBounds(Rectangle paramRectangle) {  } 
/*     */     public void toFront() {  } 
/*     */     public void toBack() {  } 
/*     */     public void updateFocusableWindowState() {  } 
/*     */     public void updateAlwaysOnTop() {  } 
/*     */     public void updateAlwaysOnTopState() {  } 
/* 536 */     public Component getGlobalHeavyweightFocusOwner() { return null; } 
/*     */     public void setBoundsPrivate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 538 */       setBounds(paramInt1, paramInt2, paramInt3, paramInt4, 3);
/*     */     }
/*     */     public Rectangle getBoundsPrivate() {
/* 541 */       return getBounds();
/*     */     }
/*     */ 
/*     */     public void setModalBlocked(Dialog paramDialog, boolean paramBoolean)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void restack() {
/* 549 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean isRestackSupported()
/*     */     {
/* 556 */       return false;
/*     */     }
/*     */     public boolean requestWindowFocus() {
/* 559 */       return false;
/*     */     }
/*     */ 
/*     */     public void updateMinimumSize()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void setOpacity(float paramFloat)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void setOpaque(boolean paramBoolean)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void updateWindow()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void repositionSecurityWarning()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.EmbeddedFrame
 * JD-Core Version:    0.6.2
 */