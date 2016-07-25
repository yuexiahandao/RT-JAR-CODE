/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.AWTEventListener;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.LayerUI;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ComponentAccessor;
/*     */ 
/*     */ public final class JLayer<V extends Component> extends JComponent
/*     */   implements Scrollable, PropertyChangeListener, Accessible
/*     */ {
/*     */   private V view;
/*     */   private LayerUI<? super V> layerUI;
/*     */   private JPanel glassPane;
/*     */   private long eventMask;
/*     */   private transient boolean isPainting;
/*     */   private transient boolean isPaintingImmediately;
/* 163 */   private static final LayerEventController eventController = new LayerEventController(null);
/*     */ 
/*     */   public JLayer()
/*     */   {
/* 174 */     this(null);
/*     */   }
/*     */ 
/*     */   public JLayer(V paramV)
/*     */   {
/* 186 */     this(paramV, new LayerUI());
/*     */   }
/*     */ 
/*     */   public JLayer(V paramV, LayerUI<V> paramLayerUI)
/*     */   {
/* 198 */     setGlassPane(createGlassPane());
/* 199 */     setView(paramV);
/* 200 */     setUI(paramLayerUI);
/*     */   }
/*     */ 
/*     */   public V getView()
/*     */   {
/* 213 */     return this.view;
/*     */   }
/*     */ 
/*     */   public void setView(V paramV)
/*     */   {
/* 225 */     Component localComponent = getView();
/* 226 */     if (localComponent != null) {
/* 227 */       super.remove(localComponent);
/*     */     }
/* 229 */     if (paramV != null) {
/* 230 */       super.addImpl(paramV, null, getComponentCount());
/*     */     }
/* 232 */     this.view = paramV;
/* 233 */     firePropertyChange("view", localComponent, paramV);
/* 234 */     revalidate();
/* 235 */     repaint();
/*     */   }
/*     */ 
/*     */   public void setUI(LayerUI<? super V> paramLayerUI)
/*     */   {
/* 245 */     this.layerUI = paramLayerUI;
/* 246 */     super.setUI(paramLayerUI);
/*     */   }
/*     */ 
/*     */   public LayerUI<? super V> getUI()
/*     */   {
/* 255 */     return this.layerUI;
/*     */   }
/*     */ 
/*     */   public JPanel getGlassPane()
/*     */   {
/* 268 */     return this.glassPane;
/*     */   }
/*     */ 
/*     */   public void setGlassPane(JPanel paramJPanel)
/*     */   {
/* 280 */     JPanel localJPanel = getGlassPane();
/* 281 */     boolean bool = false;
/* 282 */     if (localJPanel != null) {
/* 283 */       bool = localJPanel.isVisible();
/* 284 */       super.remove(localJPanel);
/*     */     }
/* 286 */     if (paramJPanel != null) {
/* 287 */       AWTAccessor.getComponentAccessor().setMixingCutoutShape(paramJPanel, new Rectangle());
/*     */ 
/* 289 */       paramJPanel.setVisible(bool);
/* 290 */       super.addImpl(paramJPanel, null, 0);
/*     */     }
/* 292 */     this.glassPane = paramJPanel;
/* 293 */     firePropertyChange("glassPane", localJPanel, paramJPanel);
/* 294 */     revalidate();
/* 295 */     repaint();
/*     */   }
/*     */ 
/*     */   public JPanel createGlassPane()
/*     */   {
/* 306 */     return new DefaultLayerGlassPane();
/*     */   }
/*     */ 
/*     */   public void setLayout(LayoutManager paramLayoutManager)
/*     */   {
/* 320 */     if (paramLayoutManager != null)
/* 321 */       throw new IllegalArgumentException("JLayer.setLayout() not supported");
/*     */   }
/*     */ 
/*     */   public void setBorder(Border paramBorder)
/*     */   {
/* 338 */     if (paramBorder != null)
/* 339 */       throw new IllegalArgumentException("JLayer.setBorder() not supported");
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/* 353 */     throw new UnsupportedOperationException("Adding components to JLayer is not supported, use setView() or setGlassPane() instead");
/*     */   }
/*     */ 
/*     */   public void remove(Component paramComponent)
/*     */   {
/* 362 */     if (paramComponent == null)
/* 363 */       super.remove(paramComponent);
/* 364 */     else if (paramComponent == getView())
/* 365 */       setView(null);
/* 366 */     else if (paramComponent == getGlassPane())
/* 367 */       setGlassPane(null);
/*     */     else
/* 369 */       super.remove(paramComponent);
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 377 */     if (this.view != null) {
/* 378 */       setView(null);
/*     */     }
/* 380 */     if (this.glassPane != null)
/* 381 */       setGlassPane(null);
/*     */   }
/*     */ 
/*     */   protected boolean isPaintingOrigin()
/*     */   {
/* 393 */     return true;
/*     */   }
/*     */ 
/*     */   public void paintImmediately(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 407 */     if ((!this.isPaintingImmediately) && (getUI() != null)) {
/* 408 */       this.isPaintingImmediately = true;
/*     */       try {
/* 410 */         getUI().paintImmediately(paramInt1, paramInt2, paramInt3, paramInt4, this);
/*     */       } finally {
/* 412 */         this.isPaintingImmediately = false;
/*     */       }
/*     */     } else {
/* 415 */       super.paintImmediately(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/* 425 */     if (!this.isPainting) {
/* 426 */       this.isPainting = true;
/*     */       try {
/* 428 */         super.paintComponent(paramGraphics);
/*     */       } finally {
/* 430 */         this.isPainting = false;
/*     */       }
/*     */     } else {
/* 433 */       super.paint(paramGraphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintComponent(Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isOptimizedDrawingEnabled()
/*     */   {
/* 457 */     return false;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 464 */     if (getUI() != null)
/* 465 */       getUI().applyPropertyChange(paramPropertyChangeEvent, this);
/*     */   }
/*     */ 
/*     */   public void setLayerEventMask(long paramLong)
/*     */   {
/* 506 */     long l = getLayerEventMask();
/* 507 */     this.eventMask = paramLong;
/* 508 */     firePropertyChange("layerEventMask", l, paramLong);
/* 509 */     if (paramLong != l) {
/* 510 */       disableEvents(l);
/* 511 */       enableEvents(this.eventMask);
/* 512 */       if (isDisplayable())
/* 513 */         eventController.updateAWTEventListener(l, paramLong);
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getLayerEventMask()
/*     */   {
/* 531 */     return this.eventMask;
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 539 */     if (getUI() != null)
/* 540 */       getUI().updateUI(this);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredScrollableViewportSize()
/*     */   {
/* 555 */     if ((getView() instanceof Scrollable)) {
/* 556 */       return ((Scrollable)getView()).getPreferredScrollableViewportSize();
/*     */     }
/* 558 */     return getPreferredSize();
/*     */   }
/*     */ 
/*     */   public int getScrollableBlockIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*     */   {
/* 575 */     if ((getView() instanceof Scrollable)) {
/* 576 */       return ((Scrollable)getView()).getScrollableBlockIncrement(paramRectangle, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 579 */     return paramInt1 == 1 ? paramRectangle.height : paramRectangle.width;
/*     */   }
/*     */ 
/*     */   public boolean getScrollableTracksViewportHeight()
/*     */   {
/* 596 */     if ((getView() instanceof Scrollable)) {
/* 597 */       return ((Scrollable)getView()).getScrollableTracksViewportHeight();
/*     */     }
/* 599 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean getScrollableTracksViewportWidth()
/*     */   {
/* 615 */     if ((getView() instanceof Scrollable)) {
/* 616 */       return ((Scrollable)getView()).getScrollableTracksViewportWidth();
/*     */     }
/* 618 */     return false;
/*     */   }
/*     */ 
/*     */   public int getScrollableUnitIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*     */   {
/* 641 */     if ((getView() instanceof Scrollable)) {
/* 642 */       return ((Scrollable)getView()).getScrollableUnitIncrement(paramRectangle, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 645 */     return 1;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 650 */     paramObjectInputStream.defaultReadObject();
/* 651 */     if (this.layerUI != null) {
/* 652 */       setUI(this.layerUI);
/*     */     }
/* 654 */     if (this.eventMask != 0L)
/* 655 */       eventController.updateAWTEventListener(0L, this.eventMask);
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 663 */     super.addNotify();
/* 664 */     eventController.updateAWTEventListener(0L, this.eventMask);
/*     */   }
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 671 */     super.removeNotify();
/* 672 */     eventController.updateAWTEventListener(this.eventMask, 0L);
/*     */   }
/*     */ 
/*     */   public void doLayout()
/*     */   {
/* 680 */     if (getUI() != null)
/* 681 */       getUI().doLayout(this);
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 228	javax/swing/JLayer:accessibleContext	Ljavax/accessibility/AccessibleContext;
/*     */     //   4: ifnonnull +15 -> 19
/*     */     //   7: aload_0
/*     */     //   8: new 122	javax/swing/JLayer$1
/*     */     //   11: dup
/*     */     //   12: aload_0
/*     */     //   13: invokespecial 265	javax/swing/JLayer$1:<init>	(Ljavax/swing/JLayer;)V
/*     */     //   16: putfield 228	javax/swing/JLayer:accessibleContext	Ljavax/accessibility/AccessibleContext;
/*     */     //   19: aload_0
/*     */     //   20: getfield 228	javax/swing/JLayer:accessibleContext	Ljavax/accessibility/AccessibleContext;
/*     */     //   23: areturn
/*     */   }
/*     */ 
/*     */   private static class DefaultLayerGlassPane extends JPanel
/*     */   {
/*     */     public DefaultLayerGlassPane()
/*     */     {
/* 833 */       setOpaque(false);
/*     */     }
/*     */ 
/*     */     public boolean contains(int paramInt1, int paramInt2)
/*     */     {
/* 849 */       for (int i = 0; i < getComponentCount(); i++) {
/* 850 */         Component localComponent = getComponent(i);
/* 851 */         Point localPoint = SwingUtilities.convertPoint(this, new Point(paramInt1, paramInt2), localComponent);
/* 852 */         if ((localComponent.isVisible()) && (localComponent.contains(localPoint))) {
/* 853 */           return true;
/*     */         }
/*     */       }
/* 856 */       if ((getMouseListeners().length == 0) && (getMouseMotionListeners().length == 0) && (getMouseWheelListeners().length == 0) && (!isCursorSet()))
/*     */       {
/* 860 */         return false;
/*     */       }
/* 862 */       return super.contains(paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LayerEventController
/*     */     implements AWTEventListener
/*     */   {
/* 705 */     private ArrayList<Long> layerMaskList = new ArrayList();
/*     */     private long currentEventMask;
/*     */     private static final long ACCEPTED_EVENTS = 231487L;
/*     */ 
/*     */     public void eventDispatched(AWTEvent paramAWTEvent)
/*     */     {
/* 724 */       Object localObject1 = paramAWTEvent.getSource();
/* 725 */       if ((localObject1 instanceof Component)) {
/* 726 */         Object localObject2 = (Component)localObject1;
/* 727 */         while (localObject2 != null) {
/* 728 */           if ((localObject2 instanceof JLayer)) {
/* 729 */             JLayer localJLayer = (JLayer)localObject2;
/* 730 */             LayerUI localLayerUI = localJLayer.getUI();
/* 731 */             if ((localLayerUI != null) && (isEventEnabled(localJLayer.getLayerEventMask(), paramAWTEvent.getID())) && ((!(paramAWTEvent instanceof InputEvent)) || (!((InputEvent)paramAWTEvent).isConsumed())))
/*     */             {
/* 734 */               localLayerUI.eventDispatched(paramAWTEvent, localJLayer);
/*     */             }
/*     */           }
/* 737 */           localObject2 = ((Component)localObject2).getParent();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void updateAWTEventListener(long paramLong1, long paramLong2) {
/* 743 */       if (paramLong1 != 0L) {
/* 744 */         this.layerMaskList.remove(Long.valueOf(paramLong1));
/*     */       }
/* 746 */       if (paramLong2 != 0L) {
/* 747 */         this.layerMaskList.add(Long.valueOf(paramLong2));
/*     */       }
/* 749 */       long l = 0L;
/* 750 */       for (Long localLong : this.layerMaskList) {
/* 751 */         l |= localLong.longValue();
/*     */       }
/*     */ 
/* 754 */       l &= 231487L;
/* 755 */       if (l == 0L) {
/* 756 */         removeAWTEventListener();
/* 757 */       } else if (getCurrentEventMask() != l) {
/* 758 */         removeAWTEventListener();
/* 759 */         addAWTEventListener(l);
/*     */       }
/* 761 */       this.currentEventMask = l;
/*     */     }
/*     */ 
/*     */     private long getCurrentEventMask() {
/* 765 */       return this.currentEventMask;
/*     */     }
/*     */ 
/*     */     private void addAWTEventListener(final long paramLong) {
/* 769 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/* 771 */           Toolkit.getDefaultToolkit().addAWTEventListener(JLayer.LayerEventController.this, paramLong);
/*     */ 
/* 773 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     private void removeAWTEventListener()
/*     */     {
/* 780 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/* 782 */           Toolkit.getDefaultToolkit().removeAWTEventListener(JLayer.LayerEventController.this);
/*     */ 
/* 784 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     private boolean isEventEnabled(long paramLong, int paramInt) {
/* 790 */       return (((paramLong & 1L) != 0L) && (paramInt >= 100) && (paramInt <= 103)) || (((paramLong & 0x2) != 0L) && (paramInt >= 300) && (paramInt <= 301)) || (((paramLong & 0x4) != 0L) && (paramInt >= 1004) && (paramInt <= 1005)) || (((paramLong & 0x8) != 0L) && (paramInt >= 400) && (paramInt <= 402)) || (((paramLong & 0x20000) != 0L) && (paramInt == 507)) || (((paramLong & 0x20) != 0L) && ((paramInt == 503) || (paramInt == 506))) || (((paramLong & 0x10) != 0L) && (paramInt != 503) && (paramInt != 506) && (paramInt != 507) && (paramInt >= 500) && (paramInt <= 507)) || (((paramLong & 0x800) != 0L) && (paramInt >= 1100) && (paramInt <= 1101)) || (((paramLong & 0x8000) != 0L) && (paramInt == 1400)) || (((paramLong & 0x10000) != 0L) && ((paramInt == 1401) || (paramInt == 1402)));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JLayer
 * JD-Core Version:    0.6.2
 */