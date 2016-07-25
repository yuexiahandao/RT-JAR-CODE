/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.DesktopPaneUI;
/*     */ 
/*     */ public class JDesktopPane extends JLayeredPane
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "DesktopPaneUI";
/*     */   transient DesktopManager desktopManager;
/* 100 */   private transient JInternalFrame selectedFrame = null;
/*     */   public static final int LIVE_DRAG_MODE = 0;
/*     */   public static final int OUTLINE_DRAG_MODE = 1;
/* 120 */   private int dragMode = 0;
/* 121 */   private boolean dragModeSet = false;
/*     */   private transient List<JInternalFrame> framesCache;
/* 123 */   private boolean componentOrderCheckingEnabled = true;
/* 124 */   private boolean componentOrderChanged = false;
/*     */ 
/*     */   public JDesktopPane()
/*     */   {
/* 130 */     setUIProperty("opaque", Boolean.TRUE);
/* 131 */     setFocusCycleRoot(true);
/*     */ 
/* 133 */     setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
/*     */       public Component getDefaultComponent(Container paramAnonymousContainer) {
/* 135 */         JInternalFrame[] arrayOfJInternalFrame1 = JDesktopPane.this.getAllFrames();
/* 136 */         Component localComponent = null;
/* 137 */         for (JInternalFrame localJInternalFrame : arrayOfJInternalFrame1) {
/* 138 */           localComponent = localJInternalFrame.getFocusTraversalPolicy().getDefaultComponent(localJInternalFrame);
/* 139 */           if (localComponent != null) {
/*     */             break;
/*     */           }
/*     */         }
/* 143 */         return localComponent;
/*     */       }
/*     */     });
/* 146 */     updateUI();
/*     */   }
/*     */ 
/*     */   public DesktopPaneUI getUI()
/*     */   {
/* 156 */     return (DesktopPaneUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void setUI(DesktopPaneUI paramDesktopPaneUI)
/*     */   {
/* 171 */     super.setUI(paramDesktopPaneUI);
/*     */   }
/*     */ 
/*     */   public void setDragMode(int paramInt)
/*     */   {
/* 192 */     int i = this.dragMode;
/* 193 */     this.dragMode = paramInt;
/* 194 */     firePropertyChange("dragMode", i, this.dragMode);
/* 195 */     this.dragModeSet = true;
/*     */   }
/*     */ 
/*     */   public int getDragMode()
/*     */   {
/* 206 */     return this.dragMode;
/*     */   }
/*     */ 
/*     */   public DesktopManager getDesktopManager()
/*     */   {
/* 214 */     return this.desktopManager;
/*     */   }
/*     */ 
/*     */   public void setDesktopManager(DesktopManager paramDesktopManager)
/*     */   {
/* 230 */     DesktopManager localDesktopManager = this.desktopManager;
/* 231 */     this.desktopManager = paramDesktopManager;
/* 232 */     firePropertyChange("desktopManager", localDesktopManager, this.desktopManager);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 243 */     setUI((DesktopPaneUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 255 */     return "DesktopPaneUI";
/*     */   }
/*     */ 
/*     */   public JInternalFrame[] getAllFrames()
/*     */   {
/* 265 */     return (JInternalFrame[])getAllFrames(this).toArray(new JInternalFrame[0]);
/*     */   }
/*     */ 
/*     */   private static Collection<JInternalFrame> getAllFrames(Container paramContainer)
/*     */   {
/* 270 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 271 */     int j = paramContainer.getComponentCount();
/* 272 */     for (int i = 0; i < j; i++) {
/* 273 */       Component localComponent = paramContainer.getComponent(i);
/* 274 */       if ((localComponent instanceof JInternalFrame)) {
/* 275 */         localLinkedHashSet.add((JInternalFrame)localComponent);
/* 276 */       } else if ((localComponent instanceof JInternalFrame.JDesktopIcon)) {
/* 277 */         JInternalFrame localJInternalFrame = ((JInternalFrame.JDesktopIcon)localComponent).getInternalFrame();
/* 278 */         if (localJInternalFrame != null)
/* 279 */           localLinkedHashSet.add(localJInternalFrame);
/*     */       }
/* 281 */       else if ((localComponent instanceof Container)) {
/* 282 */         localLinkedHashSet.addAll(getAllFrames((Container)localComponent));
/*     */       }
/*     */     }
/* 285 */     return localLinkedHashSet;
/*     */   }
/*     */ 
/*     */   public JInternalFrame getSelectedFrame()
/*     */   {
/* 298 */     return this.selectedFrame;
/*     */   }
/*     */ 
/*     */   public void setSelectedFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 314 */     this.selectedFrame = paramJInternalFrame;
/*     */   }
/*     */ 
/*     */   public JInternalFrame[] getAllFramesInLayer(int paramInt)
/*     */   {
/* 327 */     Collection localCollection = getAllFrames(this);
/* 328 */     Iterator localIterator = localCollection.iterator();
/* 329 */     while (localIterator.hasNext()) {
/* 330 */       if (((JInternalFrame)localIterator.next()).getLayer() != paramInt) {
/* 331 */         localIterator.remove();
/*     */       }
/*     */     }
/* 334 */     return (JInternalFrame[])localCollection.toArray(new JInternalFrame[0]);
/*     */   }
/*     */ 
/*     */   private List<JInternalFrame> getFrames()
/*     */   {
/* 339 */     TreeSet localTreeSet = new TreeSet();
/* 340 */     for (int i = 0; i < getComponentCount(); i++) {
/* 341 */       Object localObject = getComponent(i);
/* 342 */       if ((localObject instanceof JInternalFrame)) {
/* 343 */         localTreeSet.add(new ComponentPosition((JInternalFrame)localObject, getLayer((Component)localObject), i));
/*     */       }
/* 346 */       else if ((localObject instanceof JInternalFrame.JDesktopIcon)) {
/* 347 */         localObject = ((JInternalFrame.JDesktopIcon)localObject).getInternalFrame();
/* 348 */         localTreeSet.add(new ComponentPosition((JInternalFrame)localObject, getLayer((Component)localObject), i));
/*     */       }
/*     */     }
/*     */ 
/* 352 */     ArrayList localArrayList = new ArrayList(localTreeSet.size());
/*     */ 
/* 354 */     for (ComponentPosition localComponentPosition : localTreeSet) {
/* 355 */       localArrayList.add(localComponentPosition.component);
/*     */     }
/* 357 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private JInternalFrame getNextFrame(JInternalFrame paramJInternalFrame, boolean paramBoolean)
/*     */   {
/* 382 */     verifyFramesCache();
/* 383 */     if (paramJInternalFrame == null) {
/* 384 */       return getTopInternalFrame();
/*     */     }
/* 386 */     int i = this.framesCache.indexOf(paramJInternalFrame);
/* 387 */     if ((i == -1) || (this.framesCache.size() == 1))
/*     */     {
/* 389 */       return null;
/*     */     }
/* 391 */     if (paramBoolean)
/*     */     {
/* 393 */       i++; if (i == this.framesCache.size())
/*     */       {
/* 395 */         i = 0;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 400 */       i--; if (i == -1)
/*     */       {
/* 402 */         i = this.framesCache.size() - 1;
/*     */       }
/*     */     }
/* 405 */     return (JInternalFrame)this.framesCache.get(i);
/*     */   }
/*     */ 
/*     */   JInternalFrame getNextFrame(JInternalFrame paramJInternalFrame) {
/* 409 */     return getNextFrame(paramJInternalFrame, true);
/*     */   }
/*     */ 
/*     */   private JInternalFrame getTopInternalFrame() {
/* 413 */     if (this.framesCache.size() == 0) {
/* 414 */       return null;
/*     */     }
/* 416 */     return (JInternalFrame)this.framesCache.get(0);
/*     */   }
/*     */ 
/*     */   private void updateFramesCache() {
/* 420 */     this.framesCache = getFrames();
/*     */   }
/*     */ 
/*     */   private void verifyFramesCache()
/*     */   {
/* 425 */     if (this.componentOrderChanged) {
/* 426 */       this.componentOrderChanged = false;
/* 427 */       updateFramesCache();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(Component paramComponent)
/*     */   {
/* 436 */     super.remove(paramComponent);
/* 437 */     updateFramesCache();
/*     */   }
/*     */ 
/*     */   public JInternalFrame selectFrame(boolean paramBoolean)
/*     */   {
/* 451 */     JInternalFrame localJInternalFrame1 = getSelectedFrame();
/* 452 */     JInternalFrame localJInternalFrame2 = getNextFrame(localJInternalFrame1, paramBoolean);
/* 453 */     if (localJInternalFrame2 == null) {
/* 454 */       return null;
/*     */     }
/*     */ 
/* 458 */     setComponentOrderCheckingEnabled(false);
/* 459 */     if ((paramBoolean) && (localJInternalFrame1 != null))
/* 460 */       localJInternalFrame1.moveToBack();
/*     */     try {
/* 462 */       localJInternalFrame2.setSelected(true); } catch (PropertyVetoException localPropertyVetoException) {
/*     */     }
/* 464 */     setComponentOrderCheckingEnabled(true);
/* 465 */     return localJInternalFrame2;
/*     */   }
/*     */ 
/*     */   void setComponentOrderCheckingEnabled(boolean paramBoolean)
/*     */   {
/* 476 */     this.componentOrderCheckingEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/* 484 */     super.addImpl(paramComponent, paramObject, paramInt);
/* 485 */     if ((this.componentOrderCheckingEnabled) && (
/* 486 */       ((paramComponent instanceof JInternalFrame)) || ((paramComponent instanceof JInternalFrame.JDesktopIcon))))
/*     */     {
/* 488 */       this.componentOrderChanged = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt)
/*     */   {
/* 498 */     if (this.componentOrderCheckingEnabled) {
/* 499 */       Component localComponent = getComponent(paramInt);
/* 500 */       if (((localComponent instanceof JInternalFrame)) || ((localComponent instanceof JInternalFrame.JDesktopIcon)))
/*     */       {
/* 502 */         this.componentOrderChanged = true;
/*     */       }
/*     */     }
/* 505 */     super.remove(paramInt);
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 513 */     if (this.componentOrderCheckingEnabled) {
/* 514 */       int i = getComponentCount();
/* 515 */       for (int j = 0; j < i; j++) {
/* 516 */         Component localComponent = getComponent(j);
/* 517 */         if (((localComponent instanceof JInternalFrame)) || ((localComponent instanceof JInternalFrame.JDesktopIcon)))
/*     */         {
/* 519 */           this.componentOrderChanged = true;
/* 520 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 524 */     super.removeAll();
/*     */   }
/*     */ 
/*     */   public void setComponentZOrder(Component paramComponent, int paramInt)
/*     */   {
/* 532 */     super.setComponentZOrder(paramComponent, paramInt);
/* 533 */     if ((this.componentOrderCheckingEnabled) && (
/* 534 */       ((paramComponent instanceof JInternalFrame)) || ((paramComponent instanceof JInternalFrame.JDesktopIcon))))
/*     */     {
/* 536 */       this.componentOrderChanged = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 546 */     paramObjectOutputStream.defaultWriteObject();
/* 547 */     if (getUIClassID().equals("DesktopPaneUI")) {
/* 548 */       byte b = JComponent.getWriteObjCounter(this);
/* 549 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 550 */       if ((b == 0) && (this.ui != null))
/* 551 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setUIProperty(String paramString, Object paramObject)
/*     */   {
/* 557 */     if (paramString == "dragMode") {
/* 558 */       if (!this.dragModeSet) {
/* 559 */         setDragMode(((Integer)paramObject).intValue());
/* 560 */         this.dragModeSet = false;
/*     */       }
/*     */     }
/* 563 */     else super.setUIProperty(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 577 */     String str = this.desktopManager != null ? this.desktopManager.toString() : "";
/*     */ 
/* 580 */     return super.paramString() + ",desktopManager=" + str;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 599 */     if (this.accessibleContext == null) {
/* 600 */       this.accessibleContext = new AccessibleJDesktopPane();
/*     */     }
/* 602 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJDesktopPane extends JComponent.AccessibleJComponent
/*     */   {
/*     */     protected AccessibleJDesktopPane()
/*     */     {
/* 620 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 630 */       return AccessibleRole.DESKTOP_PANE;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ComponentPosition
/*     */     implements Comparable<ComponentPosition>
/*     */   {
/*     */     private final JInternalFrame component;
/*     */     private final int layer;
/*     */     private final int zOrder;
/*     */ 
/*     */     ComponentPosition(JInternalFrame paramJInternalFrame, int paramInt1, int paramInt2)
/*     */     {
/* 367 */       this.component = paramJInternalFrame;
/* 368 */       this.layer = paramInt1;
/* 369 */       this.zOrder = paramInt2;
/*     */     }
/*     */ 
/*     */     public int compareTo(ComponentPosition paramComponentPosition) {
/* 373 */       int i = paramComponentPosition.layer - this.layer;
/* 374 */       if (i == 0) {
/* 375 */         return this.zOrder - paramComponentPosition.zOrder;
/*     */       }
/* 377 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JDesktopPane
 * JD-Core Version:    0.6.2
 */