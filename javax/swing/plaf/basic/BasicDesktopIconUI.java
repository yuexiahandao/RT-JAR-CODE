/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.DefaultDesktopManager;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.DesktopIconUI;
/*     */ 
/*     */ public class BasicDesktopIconUI extends DesktopIconUI
/*     */ {
/*     */   protected JInternalFrame.JDesktopIcon desktopIcon;
/*     */   protected JInternalFrame frame;
/*     */   protected JComponent iconPane;
/*     */   MouseInputListener mouseInputListener;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  59 */     return new BasicDesktopIconUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  66 */     this.desktopIcon = ((JInternalFrame.JDesktopIcon)paramJComponent);
/*  67 */     this.frame = this.desktopIcon.getInternalFrame();
/*  68 */     installDefaults();
/*  69 */     installComponents();
/*     */ 
/*  72 */     JInternalFrame localJInternalFrame = this.desktopIcon.getInternalFrame();
/*  73 */     if ((localJInternalFrame.isIcon()) && (localJInternalFrame.getParent() == null)) {
/*  74 */       JDesktopPane localJDesktopPane = this.desktopIcon.getDesktopPane();
/*  75 */       if (localJDesktopPane != null) {
/*  76 */         DesktopManager localDesktopManager = localJDesktopPane.getDesktopManager();
/*  77 */         if ((localDesktopManager instanceof DefaultDesktopManager)) {
/*  78 */           localDesktopManager.iconifyFrame(localJInternalFrame);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  83 */     installListeners();
/*  84 */     JLayeredPane.putLayer(this.desktopIcon, JLayeredPane.getLayer(this.frame));
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent) {
/*  88 */     uninstallDefaults();
/*  89 */     uninstallComponents();
/*     */ 
/*  92 */     JInternalFrame localJInternalFrame = this.desktopIcon.getInternalFrame();
/*  93 */     if (localJInternalFrame.isIcon()) {
/*  94 */       JDesktopPane localJDesktopPane = this.desktopIcon.getDesktopPane();
/*  95 */       if (localJDesktopPane != null) {
/*  96 */         DesktopManager localDesktopManager = localJDesktopPane.getDesktopManager();
/*  97 */         if ((localDesktopManager instanceof DefaultDesktopManager))
/*     */         {
/*  99 */           localJInternalFrame.putClientProperty("wasIconOnce", null);
/*     */ 
/* 101 */           this.desktopIcon.setLocation(-2147483648, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 106 */     uninstallListeners();
/* 107 */     this.frame = null;
/* 108 */     this.desktopIcon = null;
/*     */   }
/*     */ 
/*     */   protected void installComponents() {
/* 112 */     this.iconPane = new BasicInternalFrameTitlePane(this.frame);
/* 113 */     this.desktopIcon.setLayout(new BorderLayout());
/* 114 */     this.desktopIcon.add(this.iconPane, "Center");
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents() {
/* 118 */     this.desktopIcon.remove(this.iconPane);
/* 119 */     this.desktopIcon.setLayout(null);
/* 120 */     this.iconPane = null;
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/* 124 */     this.mouseInputListener = createMouseInputListener();
/* 125 */     this.desktopIcon.addMouseMotionListener(this.mouseInputListener);
/* 126 */     this.desktopIcon.addMouseListener(this.mouseInputListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 130 */     this.desktopIcon.removeMouseMotionListener(this.mouseInputListener);
/* 131 */     this.desktopIcon.removeMouseListener(this.mouseInputListener);
/* 132 */     this.mouseInputListener = null;
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/* 136 */     LookAndFeel.installBorder(this.desktopIcon, "DesktopIcon.border");
/* 137 */     LookAndFeel.installProperty(this.desktopIcon, "opaque", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults() {
/* 141 */     LookAndFeel.uninstallBorder(this.desktopIcon);
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener() {
/* 145 */     return new MouseInputHandler();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent) {
/* 149 */     return this.desktopIcon.getLayout().preferredLayoutSize(this.desktopIcon);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/* 153 */     Dimension localDimension = new Dimension(this.iconPane.getMinimumSize());
/* 154 */     Border localBorder = this.frame.getBorder();
/*     */ 
/* 156 */     if (localBorder != null) {
/* 157 */       localDimension.height += localBorder.getBorderInsets(this.frame).bottom + localBorder.getBorderInsets(this.frame).top;
/*     */     }
/*     */ 
/* 160 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 170 */     return this.iconPane.getMaximumSize();
/*     */   }
/*     */ 
/*     */   public Insets getInsets(JComponent paramJComponent) {
/* 174 */     JInternalFrame localJInternalFrame = this.desktopIcon.getInternalFrame();
/* 175 */     Border localBorder = localJInternalFrame.getBorder();
/* 176 */     if (localBorder != null) {
/* 177 */       return localBorder.getBorderInsets(localJInternalFrame);
/*     */     }
/* 179 */     return new Insets(0, 0, 0, 0);
/*     */   }
/*     */   public void deiconize() {
/*     */     try {
/* 183 */       this.frame.setIcon(false); } catch (PropertyVetoException localPropertyVetoException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public class MouseInputHandler extends MouseInputAdapter {
/*     */     int _x;
/*     */     int _y;
/*     */     int __x;
/*     */     int __y;
/*     */     Rectangle startingBounds;
/*     */ 
/*     */     public MouseInputHandler() {
/*     */     }
/*     */ 
/* 201 */     public void mouseReleased(MouseEvent paramMouseEvent) { this._x = 0;
/* 202 */       this._y = 0;
/* 203 */       this.__x = 0;
/* 204 */       this.__y = 0;
/* 205 */       this.startingBounds = null;
/*     */       JDesktopPane localJDesktopPane;
/* 208 */       if ((localJDesktopPane = BasicDesktopIconUI.this.desktopIcon.getDesktopPane()) != null) {
/* 209 */         DesktopManager localDesktopManager = localJDesktopPane.getDesktopManager();
/* 210 */         localDesktopManager.endDraggingFrame(BasicDesktopIconUI.this.desktopIcon);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 216 */       Point localPoint = SwingUtilities.convertPoint((Component)paramMouseEvent.getSource(), paramMouseEvent.getX(), paramMouseEvent.getY(), null);
/*     */ 
/* 218 */       this.__x = paramMouseEvent.getX();
/* 219 */       this.__y = paramMouseEvent.getY();
/* 220 */       this._x = localPoint.x;
/* 221 */       this._y = localPoint.y;
/* 222 */       this.startingBounds = BasicDesktopIconUI.this.desktopIcon.getBounds();
/*     */       JDesktopPane localJDesktopPane;
/* 225 */       if ((localJDesktopPane = BasicDesktopIconUI.this.desktopIcon.getDesktopPane()) != null) {
/* 226 */         DesktopManager localDesktopManager = localJDesktopPane.getDesktopManager();
/* 227 */         localDesktopManager.beginDraggingFrame(BasicDesktopIconUI.this.desktopIcon);
/*     */       }
/*     */       try {
/* 230 */         BasicDesktopIconUI.this.frame.setSelected(true); } catch (PropertyVetoException localPropertyVetoException) {
/* 231 */       }if ((BasicDesktopIconUI.this.desktopIcon.getParent() instanceof JLayeredPane)) {
/* 232 */         ((JLayeredPane)BasicDesktopIconUI.this.desktopIcon.getParent()).moveToFront(BasicDesktopIconUI.this.desktopIcon);
/*     */       }
/*     */ 
/* 235 */       if ((paramMouseEvent.getClickCount() > 1) && 
/* 236 */         (BasicDesktopIconUI.this.frame.isIconifiable()) && (BasicDesktopIconUI.this.frame.isIcon()))
/* 237 */         BasicDesktopIconUI.this.deiconize();
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent paramMouseEvent)
/*     */     {
/* 252 */       Point localPoint = SwingUtilities.convertPoint((Component)paramMouseEvent.getSource(), paramMouseEvent.getX(), paramMouseEvent.getY(), null);
/*     */ 
/* 255 */       Insets localInsets = BasicDesktopIconUI.this.desktopIcon.getInsets();
/*     */ 
/* 257 */       int k = ((JComponent)BasicDesktopIconUI.this.desktopIcon.getParent()).getWidth();
/* 258 */       int m = ((JComponent)BasicDesktopIconUI.this.desktopIcon.getParent()).getHeight();
/*     */ 
/* 260 */       if (this.startingBounds == null)
/*     */       {
/* 262 */         return;
/*     */       }
/* 264 */       int i = this.startingBounds.x - (this._x - localPoint.x);
/* 265 */       int j = this.startingBounds.y - (this._y - localPoint.y);
/*     */ 
/* 267 */       if (i + localInsets.left <= -this.__x)
/* 268 */         i = -this.__x - localInsets.left;
/* 269 */       if (j + localInsets.top <= -this.__y)
/* 270 */         j = -this.__y - localInsets.top;
/* 271 */       if (i + this.__x + localInsets.right > k)
/* 272 */         i = k - this.__x - localInsets.right;
/* 273 */       if (j + this.__y + localInsets.bottom > m)
/* 274 */         j = m - this.__y - localInsets.bottom;
/*     */       JDesktopPane localJDesktopPane;
/* 277 */       if ((localJDesktopPane = BasicDesktopIconUI.this.desktopIcon.getDesktopPane()) != null) {
/* 278 */         DesktopManager localDesktopManager = localJDesktopPane.getDesktopManager();
/* 279 */         localDesktopManager.dragFrame(BasicDesktopIconUI.this.desktopIcon, i, j);
/*     */       } else {
/* 281 */         moveAndRepaint(BasicDesktopIconUI.this.desktopIcon, i, j, BasicDesktopIconUI.this.desktopIcon.getWidth(), BasicDesktopIconUI.this.desktopIcon.getHeight());
/*     */       }
/*     */     }
/*     */ 
/*     */     public void moveAndRepaint(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 289 */       Rectangle localRectangle = paramJComponent.getBounds();
/* 290 */       paramJComponent.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/* 291 */       SwingUtilities.computeUnion(paramInt1, paramInt2, paramInt3, paramInt4, localRectangle);
/* 292 */       paramJComponent.getParent().repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicDesktopIconUI
 * JD-Core Version:    0.6.2
 */