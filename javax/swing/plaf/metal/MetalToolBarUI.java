/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ContainerListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI.DockingListener;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI.DragWindow;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI.PropertyListener;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI.ToolBarContListener;
/*     */ 
/*     */ public class MetalToolBarUI extends BasicToolBarUI
/*     */ {
/*  64 */   private static List<WeakReference<JComponent>> components = new ArrayList();
/*     */   protected ContainerListener contListener;
/*     */   protected PropertyChangeListener rolloverListener;
/*     */   private static Border nonRolloverBorder;
/*     */   private JMenuBar lastMenuBar;
/*     */ 
/*     */   static synchronized void register(JComponent paramJComponent)
/*     */   {
/*  93 */     if (paramJComponent == null)
/*     */     {
/*  96 */       throw new NullPointerException("JComponent must be non-null");
/*     */     }
/*  98 */     components.add(new WeakReference(paramJComponent));
/*     */   }
/*     */ 
/*     */   static synchronized void unregister(JComponent paramJComponent)
/*     */   {
/* 105 */     for (int i = components.size() - 1; i >= 0; i--)
/*     */     {
/* 108 */       JComponent localJComponent = (JComponent)((WeakReference)components.get(i)).get();
/*     */ 
/* 110 */       if ((localJComponent == paramJComponent) || (localJComponent == null))
/* 111 */         components.remove(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   static synchronized Object findRegisteredComponentOfType(JComponent paramJComponent, Class paramClass)
/*     */   {
/* 122 */     JRootPane localJRootPane = SwingUtilities.getRootPane(paramJComponent);
/* 123 */     if (localJRootPane != null) {
/* 124 */       for (int i = components.size() - 1; i >= 0; i--) {
/* 125 */         Object localObject = ((WeakReference)components.get(i)).get();
/*     */ 
/* 128 */         if (localObject == null)
/*     */         {
/* 130 */           components.remove(i);
/*     */         }
/* 132 */         else if ((paramClass.isInstance(localObject)) && (SwingUtilities.getRootPane((Component)localObject) == localJRootPane))
/*     */         {
/* 134 */           return localObject;
/*     */         }
/*     */       }
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   static boolean doesMenuBarBorderToolBar(JMenuBar paramJMenuBar)
/*     */   {
/* 146 */     JToolBar localJToolBar = (JToolBar)findRegisteredComponentOfType(paramJMenuBar, JToolBar.class);
/*     */ 
/* 148 */     if ((localJToolBar != null) && (localJToolBar.getOrientation() == 0)) {
/* 149 */       JRootPane localJRootPane = SwingUtilities.getRootPane(paramJMenuBar);
/* 150 */       Point localPoint = new Point(0, 0);
/* 151 */       localPoint = SwingUtilities.convertPoint(paramJMenuBar, localPoint, localJRootPane);
/* 152 */       int i = localPoint.x;
/* 153 */       int j = localPoint.y;
/* 154 */       localPoint.x = (localPoint.y = 0);
/* 155 */       localPoint = SwingUtilities.convertPoint(localJToolBar, localPoint, localJRootPane);
/* 156 */       return (localPoint.x == i) && (j + paramJMenuBar.getHeight() == localPoint.y) && (paramJMenuBar.getWidth() == localJToolBar.getWidth());
/*     */     }
/*     */ 
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 164 */     return new MetalToolBarUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 169 */     super.installUI(paramJComponent);
/* 170 */     register(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 175 */     super.uninstallUI(paramJComponent);
/* 176 */     nonRolloverBorder = null;
/* 177 */     unregister(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/* 181 */     super.installListeners();
/*     */ 
/* 183 */     this.contListener = createContainerListener();
/* 184 */     if (this.contListener != null) {
/* 185 */       this.toolBar.addContainerListener(this.contListener);
/*     */     }
/* 187 */     this.rolloverListener = createRolloverListener();
/* 188 */     if (this.rolloverListener != null)
/* 189 */       this.toolBar.addPropertyChangeListener(this.rolloverListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 194 */     super.uninstallListeners();
/*     */ 
/* 196 */     if (this.contListener != null) {
/* 197 */       this.toolBar.removeContainerListener(this.contListener);
/*     */     }
/* 199 */     this.rolloverListener = createRolloverListener();
/* 200 */     if (this.rolloverListener != null)
/* 201 */       this.toolBar.removePropertyChangeListener(this.rolloverListener);
/*     */   }
/*     */ 
/*     */   protected Border createRolloverBorder()
/*     */   {
/* 206 */     return super.createRolloverBorder();
/*     */   }
/*     */ 
/*     */   protected Border createNonRolloverBorder() {
/* 210 */     return super.createNonRolloverBorder();
/*     */   }
/*     */ 
/*     */   private Border createNonRolloverToggleBorder()
/*     */   {
/* 218 */     return createNonRolloverBorder();
/*     */   }
/*     */ 
/*     */   protected void setBorderToNonRollover(Component paramComponent) {
/* 222 */     if (((paramComponent instanceof JToggleButton)) && (!(paramComponent instanceof JCheckBox)))
/*     */     {
/* 231 */       JToggleButton localJToggleButton = (JToggleButton)paramComponent;
/* 232 */       Border localBorder = localJToggleButton.getBorder();
/* 233 */       super.setBorderToNonRollover(paramComponent);
/* 234 */       if ((localBorder instanceof UIResource)) {
/* 235 */         if (nonRolloverBorder == null) {
/* 236 */           nonRolloverBorder = createNonRolloverToggleBorder();
/*     */         }
/* 238 */         localJToggleButton.setBorder(nonRolloverBorder);
/*     */       }
/*     */     } else {
/* 241 */       super.setBorderToNonRollover(paramComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ContainerListener createContainerListener()
/*     */   {
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createRolloverListener()
/*     */   {
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createDockingListener()
/*     */   {
/* 270 */     return new MetalDockingListener(this.toolBar);
/*     */   }
/*     */ 
/*     */   protected void setDragOffset(Point paramPoint) {
/* 274 */     if (!GraphicsEnvironment.isHeadless()) {
/* 275 */       if (this.dragWindow == null) {
/* 276 */         this.dragWindow = createDragWindow(this.toolBar);
/*     */       }
/* 278 */       this.dragWindow.setOffset(paramPoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 295 */     if (paramGraphics == null) {
/* 296 */       throw new NullPointerException("graphics must be non-null");
/*     */     }
/* 298 */     if ((paramJComponent.isOpaque()) && ((paramJComponent.getBackground() instanceof UIResource)) && (((JToolBar)paramJComponent).getOrientation() == 0) && (UIManager.get("MenuBar.gradient") != null))
/*     */     {
/* 302 */       JRootPane localJRootPane = SwingUtilities.getRootPane(paramJComponent);
/* 303 */       JMenuBar localJMenuBar = (JMenuBar)findRegisteredComponentOfType(paramJComponent, JMenuBar.class);
/*     */ 
/* 305 */       if ((localJMenuBar != null) && (localJMenuBar.isOpaque()) && ((localJMenuBar.getBackground() instanceof UIResource)))
/*     */       {
/* 307 */         Point localPoint = new Point(0, 0);
/* 308 */         localPoint = SwingUtilities.convertPoint(paramJComponent, localPoint, localJRootPane);
/* 309 */         int i = localPoint.x;
/* 310 */         int j = localPoint.y;
/* 311 */         localPoint.x = (localPoint.y = 0);
/* 312 */         localPoint = SwingUtilities.convertPoint(localJMenuBar, localPoint, localJRootPane);
/* 313 */         if ((localPoint.x == i) && (j == localPoint.y + localJMenuBar.getHeight()) && (localJMenuBar.getWidth() == paramJComponent.getWidth()) && (MetalUtils.drawGradient(paramJComponent, paramGraphics, "MenuBar.gradient", 0, -localJMenuBar.getHeight(), paramJComponent.getWidth(), paramJComponent.getHeight() + localJMenuBar.getHeight(), true)))
/*     */         {
/* 318 */           setLastMenuBar(localJMenuBar);
/* 319 */           paint(paramGraphics, paramJComponent);
/* 320 */           return;
/*     */         }
/*     */       }
/* 323 */       if (MetalUtils.drawGradient(paramJComponent, paramGraphics, "MenuBar.gradient", 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), true))
/*     */       {
/* 325 */         setLastMenuBar(null);
/* 326 */         paint(paramGraphics, paramJComponent);
/* 327 */         return;
/*     */       }
/*     */     }
/* 330 */     setLastMenuBar(null);
/* 331 */     super.update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   private void setLastMenuBar(JMenuBar paramJMenuBar) {
/* 335 */     if ((MetalLookAndFeel.usingOcean()) && 
/* 336 */       (this.lastMenuBar != paramJMenuBar))
/*     */     {
/* 339 */       if (this.lastMenuBar != null) {
/* 340 */         this.lastMenuBar.repaint();
/*     */       }
/* 342 */       if (paramJMenuBar != null) {
/* 343 */         paramJMenuBar.repaint();
/*     */       }
/* 345 */       this.lastMenuBar = paramJMenuBar;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MetalContainerListener extends BasicToolBarUI.ToolBarContListener {
/*     */     protected MetalContainerListener() {
/* 351 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MetalDockingListener extends BasicToolBarUI.DockingListener
/*     */   {
/* 359 */     private boolean pressedInBumps = false;
/*     */ 
/*     */     public MetalDockingListener(JToolBar arg2) {
/* 362 */       super(localJToolBar);
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 366 */       super.mousePressed(paramMouseEvent);
/* 367 */       if (!this.toolBar.isEnabled()) {
/* 368 */         return;
/*     */       }
/* 370 */       this.pressedInBumps = false;
/* 371 */       Rectangle localRectangle = new Rectangle();
/*     */ 
/* 373 */       if (this.toolBar.getOrientation() == 0) {
/* 374 */         int i = MetalUtils.isLeftToRight(this.toolBar) ? 0 : this.toolBar.getSize().width - 14;
/* 375 */         localRectangle.setBounds(i, 0, 14, this.toolBar.getSize().height);
/*     */       } else {
/* 377 */         localRectangle.setBounds(0, 0, this.toolBar.getSize().width, 14);
/*     */       }
/* 379 */       if (localRectangle.contains(paramMouseEvent.getPoint())) {
/* 380 */         this.pressedInBumps = true;
/* 381 */         Point localPoint = paramMouseEvent.getPoint();
/* 382 */         if (!MetalUtils.isLeftToRight(this.toolBar)) {
/* 383 */           localPoint.x -= this.toolBar.getSize().width - this.toolBar.getPreferredSize().width;
/*     */         }
/*     */ 
/* 386 */         MetalToolBarUI.this.setDragOffset(localPoint);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 391 */       if (this.pressedInBumps)
/* 392 */         super.mouseDragged(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MetalRolloverListener extends BasicToolBarUI.PropertyListener
/*     */   {
/*     */     protected MetalRolloverListener()
/*     */     {
/* 355 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalToolBarUI
 * JD-Core Version:    0.6.2
 */